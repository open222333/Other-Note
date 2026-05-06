#!/usr/bin/env python3
"""修正 MySQL schema dump，使其相容 MySQL 8.4。

用法：
    python3 fix_mysql84.py <input.sql> [output.sql]

    若未指定 output.sql，則輸出至 <input>_mysql84.sql
"""
import os
import re
import sys
from pathlib import Path

if len(sys.argv) < 2:
    print(f"Usage: python3 {sys.argv[0]} <input.sql> [output.sql]")
    sys.exit(1)

input_file = sys.argv[1]
if len(sys.argv) >= 3:
    output_file = sys.argv[2]
else:
    p = Path(input_file)
    output_file = str(p.with_stem(p.stem + "_mysql84"))

# 預先編譯所有 pattern
PATTERNS = [
    # 1. 移除整數顯示寬度（MySQL 8.4 已廢棄此語法）
    #    例：int(11) → int、bigint(20) → bigint、tinyint(1) → tinyint
    ('integer_display_width',
     re.compile(r'\b(tinyint|smallint|mediumint|int|bigint)\(\d+\)', re.IGNORECASE),
     r'\1'),

    # 2. 將 utf8 字元集轉換為 utf8mb4
    #    使用單字邊界避免將 utf8mb4 二次轉換為 utf8mb4mb4
    ('utf8_to_utf8mb4',
     re.compile(r'\butf8\b(?!mb4)', re.IGNORECASE),
     'utf8mb4'),

    # 3. 將 latin1 字元集轉換為 utf8mb4
    ('latin1_to_utf8mb4',
     re.compile(r'\bCHARSET=latin1\b', re.IGNORECASE),
     'CHARSET=utf8mb4'),

    # 4. 移除 ROW_FORMAT 設定（MySQL 8.4 預設已使用 DYNAMIC，無需明確指定）
    ('row_format_removed',
     re.compile(r'\s*ROW_FORMAT=\w+', re.IGNORECASE),
     ''),

    # 5. 統一所有 collation 為 utf8mb4_0900_ai_ci
    #    同時處理欄位層級（COLLATE utf8mb4_xxx）與資料表層級（COLLATE=utf8mb4_xxx）
    ('collation_unified',
     re.compile(r'COLLATE(=|\s+)utf8mb4_\w+', re.IGNORECASE),
     r'COLLATE\1utf8mb4_0900_ai_ci'),

    # 6. 對已有 CHARSET=utf8mb4 但缺少 COLLATE 的資料表補上 COLLATE=utf8mb4_0900_ai_ci
    ('charset_add_collate',
     re.compile(r'(CHARSET=utf8mb4)(?!\s*COLLATE)', re.IGNORECASE),
     r'\1 COLLATE=utf8mb4_0900_ai_ci'),

    # 7. 移除欄位層級的 CHARACTER SET 與 COLLATE（冗餘設定）
    ('column_charset_removed',
     re.compile(r'\s*CHARACTER SET utf8mb4', re.IGNORECASE),
     ''),

    ('column_collate_removed',
     re.compile(r'\s*COLLATE utf8mb4_0900_ai_ci', re.IGNORECASE),
     ''),
]

counters = {name: 0 for name, _, _ in PATTERNS}
changed = False

total_bytes = os.path.getsize(input_file)
processed_bytes = 0
last_pct = -1

def _progress(processed, total):
    pct = processed / total * 100
    filled = int(pct / 2)
    bar = '█' * filled + '░' * (50 - filled)
    print(f'\r[{bar}] {pct:5.1f}%', end='', flush=True)

with open(input_file, "r", encoding="utf-8") as fin, \
        open(output_file, "w", encoding="utf-8") as fout:
    for line in fin:
        processed_bytes += len(line.encode("utf-8"))
        pct_int = int(processed_bytes / total_bytes * 1000)
        if pct_int != last_pct:
            _progress(processed_bytes, total_bytes)
            last_pct = pct_int

        new_line = line
        for name, pattern, repl in PATTERNS:
            result, n = pattern.subn(repl, new_line)
            if n:
                counters[name] += n
                changed = True
                new_line = result
        fout.write(new_line)

print()  # 進度條換行

print("=== MySQL 8.4 Schema 修正完成 ===")
print(f"輸出檔案：{output_file}")
print()
print("修改項目：")
for k, v in counters.items():
    print(f"  {k}: {v} 處")
print()
if not changed:
    print("警告：未進行任何修改！")
else:
    print("檔案已成功更新。")
