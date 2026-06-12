#!/usr/bin/env python3
"""Fix MySQL schema dump for MySQL 8.4 compatibility.

Usage:
    python3 fix_mysql84.py <input.sql> [output.sql]

    If output.sql is omitted, writes to <input>_mysql84.sql
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
    p = Path(input_file)
    output_file = str(p.with_stem(p.stem + "_mysql84"))

with open(input_file, "r", encoding="utf-8") as f:
    content = f.read()

original = content
counters = {}

# 1. Remove integer display widths (removed in MySQL 8.4)
#    e.g. int(11) -> int, bigint(20) -> bigint, tinyint(1) -> tinyint
pattern = r'\b(tinyint|smallint|mediumint|int|bigint)\(\d+\)'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['integer_display_width'] = len(matches)
content = re.sub(pattern, r'\1', content, flags=re.IGNORECASE)

# 2. Convert utf8 charset to utf8mb4
#    Use word boundary to avoid double-converting utf8mb4 -> utf8mb4mb4
#    Match utf8 NOT followed by mb4
pattern = r'\butf8\b(?!mb4)'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['utf8_to_utf8mb4'] = len(matches)
content = re.sub(pattern, 'utf8mb4', content, flags=re.IGNORECASE)

# 3. Convert latin1 charset to utf8mb4
pattern = r'\bCHARSET=latin1\b'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['latin1_to_utf8mb4'] = len(matches)
content = re.sub(pattern, 'CHARSET=utf8mb4', content, flags=re.IGNORECASE)

# 4. Remove ROW_FORMAT (MySQL 8.4 defaults to DYNAMIC)
pattern = r'\s*ROW_FORMAT=\w+'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['row_format_removed'] = len(matches)
content = re.sub(pattern, '', content, flags=re.IGNORECASE)

# 5. Unify all collations to utf8mb4_0900_ai_ci
#    Covers both column-level (COLLATE utf8mb4_xxx) and table-level (COLLATE=utf8mb4_xxx)
pattern = r'COLLATE(=|\s+)utf8mb4_\w+'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['collation_unified'] = len(matches)
content = re.sub(pattern, r'COLLATE\1utf8mb4_0900_ai_ci', content, flags=re.IGNORECASE)

# 6. Add COLLATE=utf8mb4_0900_ai_ci to tables that have CHARSET=utf8mb4 but no COLLATE
#    Match CHARSET=utf8mb4 not already followed by COLLATE
pattern = r'(CHARSET=utf8mb4)(?!\s*COLLATE)'
matches = re.findall(pattern, content, flags=re.IGNORECASE)
counters['charset_add_collate'] = len(matches)
content = re.sub(pattern, r'\1 COLLATE=utf8mb4_0900_ai_ci', content, flags=re.IGNORECASE)

# 7. Remove redundant column-level CHARACTER SET and COLLATE
#    Table-level DEFAULT CHARSET already covers this
#    Column-level uses space: COLLATE utf8mb4_xxx (vs table-level COLLATE=utf8mb4_xxx with =)
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

print("=== MySQL 8.4 Schema Fix Complete ===")
print(f"Output: {output_file}")
print()
print("Changes made:")
for k, v in counters.items():
    print(f"  {k}: {v} replacements")
print()
if content == original:
    print("WARNING: No changes were made!")
else:
    print(f"File updated successfully.")
