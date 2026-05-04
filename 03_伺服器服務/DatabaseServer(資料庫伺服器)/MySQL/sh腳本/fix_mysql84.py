#!/usr/bin/env python3
"""修正 MySQL schema dump，使其相容 MySQL 8.4。

用法：
    python3 fix_mysql84.py <input.sql> [output.sql]

    若未指定 output.sql，則輸出至 <input>_mysql84.sql
"""
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
    # 未指定輸出檔時，在原檔名後加 _mysql84
    p = Path(input_file)
    output_file = str(p.with_stem(p.stem + "_mysql84"))

with open(input_file, "r", encoding="utf-8") as f:
    content = f.read()

original = content
counters = {}

# 1. 移除整數顯示寬度（MySQL 8.4 已廢棄此語法）
#    例：int(11) → int、bigint(20) → bigint、tinyint(1) → tinyint
pattern = r'\b(tinyint|smallint|mediumint|int|bigint)\(\d+\)'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['integer_display_width'] = len(matches)
content = re.sub(pattern, r'\1', content, flags=re.IGNORECASE)

# 2. 將 utf8 字元集轉換為 utf8mb4
#    使用單字邊界避免將 utf8mb4 二次轉換為 utf8mb4mb4
#    只比對 utf8 後面沒有接 mb4 的情況
pattern = r'\butf8\b(?!mb4)'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['utf8_to_utf8mb4'] = len(matches)
content = re.sub(pattern, 'utf8mb4', content, flags=re.IGNORECASE)

# 3. 將 latin1 字元集轉換為 utf8mb4
pattern = r'\bCHARSET=latin1\b'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['latin1_to_utf8mb4'] = len(matches)
content = re.sub(pattern, 'CHARSET=utf8mb4', content, flags=re.IGNORECASE)

# 4. 移除 ROW_FORMAT 設定（MySQL 8.4 預設已使用 DYNAMIC，無需明確指定）
pattern = r'\s*ROW_FORMAT=\w+'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['row_format_removed'] = len(matches)
content = re.sub(pattern, '', content, flags=re.IGNORECASE)

# 5. 統一所有 collation 為 utf8mb4_0900_ai_ci
#    同時處理欄位層級（COLLATE utf8mb4_xxx）與資料表層級（COLLATE=utf8mb4_xxx）
pattern = r'COLLATE(=|\s+)utf8mb4_\w+'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['collation_unified'] = len(matches)
content = re.sub(pattern, r'COLLATE\1utf8mb4_0900_ai_ci', content, flags=re.IGNORECASE)

# 6. 對已有 CHARSET=utf8mb4 但缺少 COLLATE 的資料表補上 COLLATE=utf8mb4_0900_ai_ci
#    比對 CHARSET=utf8mb4 後面沒有緊接 COLLATE 的情況
pattern = r'(CHARSET=utf8mb4)(?!\s*COLLATE)'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['charset_add_collate'] = len(matches)
content = re.sub(pattern, r'\1 COLLATE=utf8mb4_0900_ai_ci', content, flags=re.IGNORECASE)

# 7. 移除欄位層級的 CHARACTER SET 與 COLLATE（冗餘設定）
#    資料表層級的 DEFAULT CHARSET 已涵蓋所有欄位，無需在每個欄位重複宣告
#    欄位層級使用空格：COLLATE utf8mb4_xxx（資料表層級用等號：COLLATE=utf8mb4_xxx）
pattern = r'\s*CHARACTER SET utf8mb4'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['column_charset_removed'] = len(matches)
content = re.sub(pattern, '', content, flags=re.IGNORECASE)

pattern = r'\s*COLLATE utf8mb4_0900_ai_ci'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['column_collate_removed'] = len(matches)
content = re.sub(pattern, '', content, flags=re.IGNORECASE)

with open(output_file, "w", encoding="utf-8") as f:
    f.write(content)

print("=== MySQL 8.4 Schema 修正完成 ===")
print(f"輸出檔案：{output_file}")
print()
print("修改項目：")
for k, v in counters.items():
    print(f"  {k}: {v} 處")
print()
if content == original:
    print("警告：未進行任何修改！")
else:
    print("檔案已成功更新。")
