#!/usr/bin/env python3
"""修正 MySQL schema dump，使其相容 MySQL 8.4。

用法：
    python3 fix_mysql84.py <input.sql[.gz]> [output.sql] [--workers N]

    若未指定 output.sql，則輸出至 <input>_mysql84.sql
    --workers N  指定平行工作數（預設：CPU 核心數）
    .gz 檔自動解壓縮，無需事先 gunzip
"""
import collections
import gzip
import io
import os
import re
import sys
from pathlib import Path
from concurrent.futures import ProcessPoolExecutor

# 預先編譯所有 pattern（模組層級，子程序 import 後可直接使用）
PATTERNS = [
    # 1. 移除整數顯示寬度（MySQL 8.4 已廢棄此語法）
    ('integer_display_width',
     re.compile(r'\b(tinyint|smallint|mediumint|int|bigint)\(\d+\)', re.IGNORECASE),
     r'\1'),

    # 2. 將 utf8 字元集轉換為 utf8mb4
    ('utf8_to_utf8mb4',
     re.compile(r'\butf8\b(?!mb4)', re.IGNORECASE),
     'utf8mb4'),

    # 3. 將 utf8_xxx collation 轉換為 utf8mb4_xxx
    #    例：utf8_bin → utf8mb4_bin、utf8_general_ci → utf8mb4_general_ci
    #    （\butf8\b 不匹配 utf8_bin，因為 _ 是 word character，需獨立處理）
    ('utf8_collation_to_utf8mb4',
     re.compile(r'\butf8_(\w+)', re.IGNORECASE),
     r'utf8mb4_\1'),

    # 4. 將 utf8mb3 轉換為 utf8mb4（utf8mb3 是 utf8 在 MySQL 8.0+ 的正式內部名稱）
    #    例：utf8mb3_bin → utf8mb4_bin、CHARSET=utf8mb3 → CHARSET=utf8mb4
    ('utf8mb3_to_utf8mb4',
     re.compile(r'\butf8mb3', re.IGNORECASE),
     'utf8mb4'),

    # 5. 將 MyISAM 轉換為 InnoDB
    #    utf8→utf8mb4 後每字元從 3 bytes 變 4 bytes，MyISAM 的 1000 bytes key 上限容易超出
    #    InnoDB + DYNAMIC row format 的 key 上限為 3072 bytes
    ('myisam_to_innodb',
     re.compile(r'\bENGINE=MyISAM\b', re.IGNORECASE),
     'ENGINE=InnoDB'),

    # 6. 將 latin1 字元集轉換為 utf8mb4
    ('latin1_to_utf8mb4',
     re.compile(r'\bCHARSET=latin1\b', re.IGNORECASE),
     'CHARSET=utf8mb4'),

    # 4. 移除 ROW_FORMAT 設定
    ('row_format_removed',
     re.compile(r'\s*ROW_FORMAT=\w+', re.IGNORECASE),
     ''),

    # 5. 統一所有 collation 為 utf8mb4_0900_ai_ci
    ('collation_unified',
     re.compile(r'COLLATE(=|\s+)utf8mb4_\w+', re.IGNORECASE),
     r'COLLATE\1utf8mb4_0900_ai_ci'),

    # 6. 對已有 CHARSET=utf8mb4 但缺少 COLLATE 的資料表補上 COLLATE
    ('charset_add_collate',
     re.compile(r'(CHARSET=utf8mb4)(?!\s*COLLATE)', re.IGNORECASE),
     r'\1 COLLATE=utf8mb4_0900_ai_ci'),

    # 7. 移除欄位層級的 CHARACTER SET（冗餘設定）
    ('column_charset_removed',
     re.compile(r'\s*CHARACTER SET utf8mb4', re.IGNORECASE),
     ''),

    # 8. 移除欄位層級的 COLLATE（冗餘設定）
    ('column_collate_removed',
     re.compile(r'\s*COLLATE utf8mb4_0900_ai_ci', re.IGNORECASE),
     ''),
]

CHUNK_BYTES = 32 * 1024 * 1024  # 每個工作區塊最大 32 MB


def process_chunk(lines):
    """處理一個 chunk，回傳 (修正後的行列表, 各 pattern 計數)。"""
    local_counters = {name: 0 for name, _, _ in PATTERNS}
    result = []
    for line in lines:
        new_line = line
        for name, pattern, repl in PATTERNS:
            fixed, n = pattern.subn(repl, new_line)
            if n:
                local_counters[name] += n
                new_line = fixed
        result.append(new_line)
    return result, local_counters


def _progress(processed, total, label=''):
    pct = processed / total * 100
    filled = int(pct / 2)
    bar = '█' * filled + '░' * (50 - filled)
    suffix = f'  {label}' if label else ''
    print(f'\r[{bar}] {pct:5.1f}%{suffix}', end='', flush=True)


def main():
    if len(sys.argv) < 2:
        print(f"Usage: python3 {sys.argv[0]} <input.sql[.gz]> [output.sql] [--workers N]")
        sys.exit(1)

    input_file = sys.argv[1]
    output_file = None
    workers = os.cpu_count() or 4

    i = 2
    while i < len(sys.argv):
        if sys.argv[i] == '--workers' and i + 1 < len(sys.argv):
            workers = int(sys.argv[i + 1])
            i += 2
        elif output_file is None:
            output_file = sys.argv[i]
            i += 1
        else:
            i += 1

    if output_file is None:
        p = Path(input_file)
        stem = p.stem if not p.suffix == '.gz' else Path(p.stem).stem
        output_file = str(p.with_name(stem + '_mysql84.sql'))

    counters = {name: 0 for name, _, _ in PATTERNS}

    # 開啟輸入檔（.gz 直接串流解壓縮，追蹤壓縮檔位置作為進度）
    is_gz = input_file.lower().endswith('.gz')
    total_ref = os.path.getsize(input_file)

    if is_gz:
        raw_fh = open(input_file, 'rb')
        fin = io.TextIOWrapper(
            gzip.GzipFile(fileobj=raw_fh),
            encoding='utf-8', errors='surrogateescape'
        )
        progress_fh = raw_fh          # 追蹤壓縮檔讀取位置
        label = '解壓縮中'
    else:
        fin = open(input_file, 'r', encoding='utf-8', errors='surrogateescape')
        progress_fh = fin.buffer      # 追蹤原始檔讀取位置
        raw_fh = None
        label = '處理中'

    _progress(0, total_ref, label)
    last_pct = -1
    pending = collections.deque()
    WINDOW = workers * 2  # 同時 in-flight 的 chunk 數，控制記憶體用量

    try:
        with ProcessPoolExecutor(max_workers=workers) as executor, \
             open(output_file, 'w', encoding='utf-8', errors='surrogateescape') as fout:

            chunk = []
            chunk_size = 0  # 目前 chunk 的 byte 數

            def flush_oldest():
                future, _ = pending.popleft()
                lines, local_counters = future.result()
                for name, count in local_counters.items():
                    counters[name] += count
                fout.writelines(lines)

            for line in fin:
                chunk.append(line)
                chunk_size += len(line.encode('utf-8', errors='surrogateescape'))

                # 更新進度（依壓縮檔或原始檔讀取位置）
                pos = progress_fh.tell()
                pct_int = int(pos / total_ref * 1000)
                if pct_int != last_pct:
                    _progress(pos, total_ref, label)
                    last_pct = pct_int

                if chunk_size >= CHUNK_BYTES:
                    pending.append((executor.submit(process_chunk, chunk), chunk_size))
                    chunk = []
                    chunk_size = 0
                    if len(pending) >= WINDOW:
                        flush_oldest()

            if chunk:
                pending.append((executor.submit(process_chunk, chunk), chunk_size))

            while pending:
                flush_oldest()

    finally:
        fin.close()
        if raw_fh:
            raw_fh.close()

    print()
    print("=== MySQL 8.4 Schema 修正完成 ===")
    print(f"輸出檔案：{output_file}")
    print()
    print("修改項目：")
    for k, v in counters.items():
        print(f"  {k}: {v} 處")
    print()
    changed = any(v > 0 for v in counters.values())
    if not changed:
        print("警告：未進行任何修改！")
    else:
        print("檔案已成功更新。")


if __name__ == "__main__":
    main()
