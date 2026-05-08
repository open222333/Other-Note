#!/bin/bash
# mysqldump_and_sync_timer.sh
# 階段一：mysqldump 壓縮備份（.sql.gz）→ 階段二：rsync 傳送至遠端，分別計時。
#
# Usage: bash mysqldump_and_sync_timer.sh [--pass] [--single|--multi] all
#        bash mysqldump_and_sync_timer.sh [--pass] [--single|--multi] list <db1> <db2> ...
#        bash mysqldump_and_sync_timer.sh -h
#
# 預設行為：all → 單一 all_databases-DATE.sql.gz；list → 每個 DB 獨立檔案
# --single 強制單一檔案；--multi 強制各自獨立；--pass 改用密碼傳送（需安裝 sshpass）

# ============================================================
# !!                  使用前請修改以下設定                  !!
# ============================================================

# MySQL 連線
DB_USER="db_user"
DB_PASS="db_password"
DB_HOST="127.0.0.1"
DB_PORT="3306"

# 本地備份暫存路徑
BACKUP_DIR="/tmp/mysql_backup"

# 遠端傳送 - SSH Key（預設）
REMOTE_USER="remote_user"
REMOTE_HOST="192.168.1.100"
REMOTE_DIR="/backup/mysql"
SSH_KEY="/root/.ssh/id_rsa"

# 遠端傳送 - 密碼（使用 --pass 時）
REMOTE_PASS="remote_password"

# 日誌路徑
LOG_FILE="/tmp/mysql_backup_sync.log"

# ============================================================
# !!                     以下不需更動                       !!
# ============================================================

# all 模式下排除的系統資料庫
EXCLUDE_DBS=("information_schema" "performance_schema" "sys" "mysql")

# ==================== 函式 ====================

log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

elapsed() {
    local sec=$(( $2 - $1 ))
    if [ "$sec" -ge 60 ]; then
        printf "%d 分 %d 秒" $(( sec / 60 )) $(( sec % 60 ))
    else
        printf "%d 秒" "$sec"
    fi
}

is_excluded() {
    local target="$1"
    for ex in "${EXCLUDE_DBS[@]}"; do
        [ "$target" = "$ex" ] && return 0
    done
    return 1
}

# progress_bar <current> <total>
progress_bar() {
    local cur=$1 total=$2
    local width=30 filled empty bar="" i
    filled=$(( cur * width / total ))
    empty=$(( width - filled ))
    for ((i=0; i<filled; i++)); do bar+="█"; done
    for ((i=0; i<empty; i++)); do bar+="░"; done
    printf "[%s] %d/%d" "$bar" "$cur" "$total"
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
    echo "Usage: $0 [--pass] [--single] <mode> [databases...]"
    echo ""
    echo "Modes:"
    echo "  all               備份所有使用者資料庫（預設：單一 all_databases-DATE.sql.gz）"
    echo "  list <db1> <db2>  只備份指定資料庫（預設：每個 DB 一個檔案）"
    echo ""
    echo "Options:"
    echo "  --single          強制合併為單一 all_databases-DATE.sql.gz"
    echo "  --multi           強制每個 DB 各自獨立 .sql.gz（覆蓋 all 的預設）"
    echo "  --pass            使用密碼傳送（需安裝 sshpass），預設使用 SSH Key"
    echo "  -h, --help        顯示此說明"
    echo ""
    echo "Examples:"
    echo "  $0 all                    # 單一檔案（預設）"
    echo "  $0 --multi all            # 各自獨立檔案"
    echo "  $0 --pass all             # 單一檔案 + 密碼傳送"
    echo "  $0 list mydb1 mydb2       # 各自獨立檔案（預設）"
    echo "  $0 --single list mydb1    # 單一檔案"
}

if [ $# -lt 1 ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    usage
    exit 0
fi

AUTH_MODE="key"
SINGLE_FILE=""  # 空白 = 依模式自動決定（all→單一檔，list→各自獨立）

# 解析選項旗標（可任意順序）
while [[ "$1" == --* ]]; do
    case "$1" in
        --pass)   AUTH_MODE="pass";  shift ;;
        --single) SINGLE_FILE=true;  shift ;;
        --multi)  SINGLE_FILE=false; shift ;;
        *) break ;;
    esac
done

if [ $# -lt 1 ]; then
    usage
    exit 1
fi

BACKUP_MODE="$1"
shift

if [ "$BACKUP_MODE" = "list" ]; then
    if [ $# -eq 0 ]; then
        echo "[ERROR] list 模式需至少指定一個資料庫名稱"
        exit 1
    fi
    DATABASES=("$@")
fi

# 未明確指定時，all 預設單一檔案，list 預設各自獨立
[ -z "$SINGLE_FILE" ] && { [ "$BACKUP_MODE" = "all" ] && SINGLE_FILE=true || SINGLE_FILE=false; }

# ==================== 前置檢查 ====================

if [ "$AUTH_MODE" = "pass" ] && ! command -v sshpass &>/dev/null; then
    echo "[ERROR] 找不到 sshpass，請先安裝：apt install sshpass / yum install sshpass"
    exit 1
fi

# ==================== 初始化 ====================

DATE=$(date +"%Y-%m-%d")
mkdir -p "$BACKUP_DIR"

log "================================================================"
log "開始備份  DATE=${DATE}  MODE=${BACKUP_MODE}  SINGLE=${SINGLE_FILE}  HOST=${DB_HOST}  AUTH=${AUTH_MODE}"
log "================================================================"

# ==================== 階段一：mysqldump + 壓縮 ====================

log "[階段一] mysqldump 壓縮備份開始"
STAGE1_START=$(date +%s)
DUMP_OK=0
DUMP_FAIL=0

case "$BACKUP_MODE" in
    all)
        TARGET_DBS=$(mysql -u"$DB_USER" -p"$DB_PASS" -h "$DB_HOST" -P "$DB_PORT" \
            -N -e "SHOW DATABASES;" 2>>"$LOG_FILE")
        if [ $? -ne 0 ]; then
            log "[ERROR] 無法連線至 MySQL，請確認帳號權限"
            exit 1
        fi
        ;;
    list)
        TARGET_DBS="${DATABASES[*]}"
        ;;
    *)
        log "[ERROR] 不支援的 BACKUP_MODE：${BACKUP_MODE}（可選：all / list）"
        exit 1
        ;;
esac

# 過濾系統 DB，建立實際備份清單
DUMP_LIST=()
for DB in $TARGET_DBS; do
    is_excluded "$DB" || DUMP_LIST+=("$DB")
done
DUMP_TOTAL=${#DUMP_LIST[@]}

if $SINGLE_FILE; then
    # ── 單一檔案模式 ──────────────────────────────────────
    OUTFILE="${BACKUP_DIR}/all_databases-${DATE}.sql.gz"
    T_START=$(date +%s)

    if [ "$BACKUP_MODE" = "all" ]; then
        mysqldump -u"$DB_USER" -p"$DB_PASS" -h "$DB_HOST" -P "$DB_PORT" \
            --all-databases --single-transaction --routines --triggers --events \
            2>>"$LOG_FILE" | gzip > "$OUTFILE" &
    else
        mysqldump -u"$DB_USER" -p"$DB_PASS" -h "$DB_HOST" -P "$DB_PORT" \
            --single-transaction --routines --triggers --events \
            --databases "${DUMP_LIST[@]}" \
            2>>"$LOG_FILE" | gzip > "$OUTFILE" &
    fi
    DUMP_PID=$!
    spinner $DUMP_PID "匯出 ${DUMP_TOTAL} 個資料庫 → $(basename "$OUTFILE")"
    wait $DUMP_PID
    PIPE_STATUS=("${PIPESTATUS[@]}")
    T_END=$(date +%s)

    if [ "${PIPE_STATUS[0]}" -eq 0 ]; then
        SIZE=$(du -sh "$OUTFILE" 2>/dev/null | cut -f1)
        log "  [OK] all_databases → ${OUTFILE}  大小=${SIZE}  耗時=$(elapsed $T_START $T_END)"
        DUMP_OK=$DUMP_TOTAL
    else
        log "  [FAIL] all_databases  exit=${PIPE_STATUS[0]}"
        rm -f "$OUTFILE"
        DUMP_FAIL=$DUMP_TOTAL
    fi
else
    # ── 每個 DB 獨立檔案模式 ──────────────────────────────
    DUMP_IDX=0
    for DB in "${DUMP_LIST[@]}"; do
        DUMP_IDX=$(( DUMP_IDX + 1 ))
        printf "\r\033[K  $(progress_bar $DUMP_IDX $DUMP_TOTAL) 備份 %s ..." "$DB"

        OUTFILE="${BACKUP_DIR}/${DB}-${DATE}.sql.gz"
        T_START=$(date +%s)

        mysqldump -u"$DB_USER" -p"$DB_PASS" -h "$DB_HOST" -P "$DB_PORT" \
            --single-transaction --routines --triggers --events \
            "$DB" 2>>"$LOG_FILE" | gzip > "$OUTFILE"

        PIPE_STATUS=("${PIPESTATUS[@]}")
        T_END=$(date +%s)
        printf "\r\033[K"

        if [ "${PIPE_STATUS[0]}" -eq 0 ] && [ "${PIPE_STATUS[1]}" -eq 0 ]; then
            SIZE=$(du -sh "$OUTFILE" 2>/dev/null | cut -f1)
            log "  [OK] ${DB}  大小=${SIZE}  耗時=$(elapsed $T_START $T_END)"
            DUMP_OK=$(( DUMP_OK + 1 ))
        else
            log "  [FAIL] ${DB}  mysqldump=${PIPE_STATUS[0]}  gzip=${PIPE_STATUS[1]}"
            rm -f "$OUTFILE"
            DUMP_FAIL=$(( DUMP_FAIL + 1 ))
        fi
    done
fi

STAGE1_END=$(date +%s)
log "[階段一] 完成  成功=${DUMP_OK}  失敗=${DUMP_FAIL}  耗時=$(elapsed $STAGE1_START $STAGE1_END)"

# ==================== 階段二：rsync 傳送 ====================

log "[階段二] rsync 傳送開始 → ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}  (auth=${AUTH_MODE})"
STAGE2_START=$(date +%s)

if [ "$AUTH_MODE" = "pass" ]; then
    sshpass -p "$REMOTE_PASS" rsync -avz --delete \
        -e "ssh -o StrictHostKeyChecking=no -o ConnectTimeout=10" \
        "${BACKUP_DIR}/" \
        "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/" >> "$LOG_FILE" 2>&1 &
else
    rsync -avz --delete \
        -e "ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no -o ConnectTimeout=10" \
        "${BACKUP_DIR}/" \
        "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/" >> "$LOG_FILE" 2>&1 &
fi
RSYNC_PID=$!
spinner $RSYNC_PID "rsync 傳送至 ${REMOTE_HOST}"
wait $RSYNC_PID
SYNC_EXIT=$?

STAGE2_END=$(date +%s)

if [ "$SYNC_EXIT" -eq 0 ]; then
    log "[階段二] 完成  耗時=$(elapsed $STAGE2_START $STAGE2_END)"
    rm -f "${BACKUP_DIR}"/*.sql.gz
    log "[清理] 傳送成功，已刪除本地備份"
else
    log "[階段二] 失敗（exit=${SYNC_EXIT}），保留本地備份  耗時=$(elapsed $STAGE2_START $STAGE2_END)"
fi

# ==================== 總結 ====================

log "----------------------------------------------------------------"
log "總結  階段一=$(elapsed $STAGE1_START $STAGE1_END)  階段二=$(elapsed $STAGE2_START $STAGE2_END)  總耗時=$(elapsed $STAGE1_START $STAGE2_END)"
log "================================================================"

[ "$DUMP_FAIL" -eq 0 ] && [ "$SYNC_EXIT" -eq 0 ] && exit 0 || exit 1
