#!/bin/bash
# mysqlrestore_and_fix_mysql84_timer.sh
# 階段一：fix_mysql84.py 修正 SQL 相容性 → 階段二：mysql 匯入還原，分別計時。
#
# Usage: bash mysqlrestore_and_fix_mysql84_timer.sh <input.sql[.gz]> [database]
#        bash mysqlrestore_and_fix_mysql84_timer.sh -h
# 不帶 database → 匯入全部資料庫（適用 --all-databases dump）
# 帶   database → 匯入指定單一資料庫

# ============================================================
# !!                  使用前請修改以下設定                  !!
# ============================================================

# MySQL 連線
DB_USER="db_user"
DB_PASS="db_password"
DB_HOST="127.0.0.1"
DB_PORT="3306"

# fix_mysql84.py 完整路徑
FIX_SCRIPT="/path/to/fix_mysql84.py"

# ============================================================
# !!                     以下不需更動                       !!
# ============================================================

# Python 直譯器
PYTHON="python3"

# ==================== 函式 ====================

msg() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

elapsed() {
    local sec=$(( $2 - $1 ))
    if [ "$sec" -ge 60 ]; then
        printf "%d 分 %d 秒" $(( sec / 60 )) $(( sec % 60 ))
    else
        printf "%d 秒" "$sec"
    fi
}

# spinner <pid> <message>
spinner() {
    local pid=$1 msg="${2:-執行中}"
    local chars='|/-\' i=0
    while kill -0 "$pid" 2>/dev/null; do
        printf "\r  [%s] %s " "${chars:$((i % 4)):1}" "$msg"
        i=$(( i + 1 ))
        sleep 0.2
    done
    printf "\r\033[K"
}

# ==================== 參數解析 ====================

usage() {
    echo "Usage: $0 <input.sql[.gz]> [database]"
    echo ""
    echo "Arguments:"
    echo "  <input.sql>   要還原的 SQL 檔（支援 .sql 或 .sql.gz）"
    echo "  [database]    匯入目標資料庫（省略 = 匯入全部資料庫，適用 --all-databases dump）"
    echo ""
    echo "Options:"
    echo "  -h, --help    顯示此說明"
    echo ""
    echo "Examples:"
    echo "  $0 /tmp/all_databases.sql.gz           # 匯入全部資料庫"
    echo "  $0 /tmp/dump.sql.gz mydb               # 匯入指定資料庫"
}

if [ $# -lt 1 ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    usage
    exit 0
fi

INPUT_SQL="$1"
DATABASE="${2:-}"
FIXED_SQL=""

# ==================== 前置檢查 ====================

if [ ! -f "$INPUT_SQL" ]; then
    echo "[ERROR] 找不到輸入檔案：${INPUT_SQL}"
    exit 1
fi

if [ ! -f "$FIX_SCRIPT" ]; then
    echo "[ERROR] 找不到 fix_mysql84.py：${FIX_SCRIPT}"
    exit 1
fi

if ! command -v "$PYTHON" &>/dev/null; then
    echo "[ERROR] 找不到 Python 直譯器：${PYTHON}"
    exit 1
fi

# 自動命名修正後的輸出檔
if [ -z "$FIXED_SQL" ]; then
    BASE="${INPUT_SQL%.sql.gz}"
    BASE="${BASE%.sql}"
    FIXED_SQL="${BASE}_mysql84.sql"
fi

# ==================== 初始化 ====================

echo "================================================================"
echo "  mysqlrestore + fix_mysql84 計時腳本"
echo "  INPUT  : ${INPUT_SQL}"
echo "  FIXED  : ${FIXED_SQL}"
echo "  TARGET : ${DB_USER}@${DB_HOST}:${DB_PORT}/${DATABASE:-（all databases）}"
echo "================================================================"

TOTAL_START=$(date +%s)

# ==================== 階段一：fix_mysql84.py ====================

msg "[階段一] fix_mysql84.py 開始修正"
STAGE1_START=$(date +%s)

# 若輸入是 .sql.gz，先解壓後再修正
if [[ "$INPUT_SQL" == *.gz ]]; then
    UNZIPPED="${INPUT_SQL%.gz}"
    msg "  解壓縮 ${INPUT_SQL} → ${UNZIPPED}"
    gunzip -k "$INPUT_SQL"
    if [ $? -ne 0 ]; then
        echo "[ERROR] 解壓縮失敗"
        exit 1
    fi
    "$PYTHON" "$FIX_SCRIPT" "$UNZIPPED" "$FIXED_SQL"
    FIX_EXIT=$?
    rm -f "$UNZIPPED"
else
    "$PYTHON" "$FIX_SCRIPT" "$INPUT_SQL" "$FIXED_SQL"
    FIX_EXIT=$?
fi

STAGE1_END=$(date +%s)

if [ "$FIX_EXIT" -eq 0 ]; then
    SIZE=$(du -sh "$FIXED_SQL" 2>/dev/null | cut -f1)
    msg "[階段一] 完成  輸出=${FIXED_SQL}  大小=${SIZE}  耗時=$(elapsed $STAGE1_START $STAGE1_END)"
else
    msg "[階段一] 失敗（exit=${FIX_EXIT}）"
    exit 1
fi

# ==================== 階段二：mysql 匯入還原 ====================

msg "[階段二] mysql 匯入開始 → ${DATABASE:-all databases}"
STAGE2_START=$(date +%s)

if [ -n "$DATABASE" ]; then
    mysql -u"$DB_USER" -p"$DB_PASS" -h "$DB_HOST" -P "$DB_PORT" "$DATABASE" < "$FIXED_SQL" &
else
    mysql -u"$DB_USER" -p"$DB_PASS" -h "$DB_HOST" -P "$DB_PORT" < "$FIXED_SQL" &
fi
MYSQL_PID=$!
spinner $MYSQL_PID "匯入 ${DATABASE:-all databases}"
wait $MYSQL_PID
RESTORE_EXIT=$?

STAGE2_END=$(date +%s)

if [ "$RESTORE_EXIT" -eq 0 ]; then
    msg "[階段二] 完成  耗時=$(elapsed $STAGE2_START $STAGE2_END)"
else
    msg "[階段二] 失敗（exit=${RESTORE_EXIT}）"
fi

# ==================== 總結 ====================

echo "----------------------------------------------------------------"
msg "總結  階段一=$(elapsed $STAGE1_START $STAGE1_END)  階段二=$(elapsed $STAGE2_START $STAGE2_END)  總耗時=$(elapsed $TOTAL_START $STAGE2_END)"
echo "================================================================"

[ "$FIX_EXIT" -eq 0 ] && [ "$RESTORE_EXIT" -eq 0 ] && exit 0 || exit 1
