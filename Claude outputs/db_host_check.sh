#!/usr/bin/env bash
# Managed MySQL 連線測試：比較 Private Host / Public Host
#
# 用法（在 test-jp-vpc1 / vpc2 / vpc3 和 Mac 上各執行一次）：
#   export PRIVATE_HOST=private-xxxx.g2a.akamaidb.net
#   export PUBLIC_HOST=public-xxxx.g2a.akamaidb.net   # 沒開 Public Access 可留空
#   export DB_PORT=3306 DB_USER=akmadmin
#   read -rs -p "DB password: " MYSQL_PWD; echo; export MYSQL_PWD   # 不設就跳過登入測試
#   bash db_host_check.sh
#
# 結果欄位：
#   DNS   解析到的 IP（NO_DNS = 解析不到）
#   TCP   port 是否可達（FAIL = 網路層不通：路由、防火牆或 Manage Access）
#   LOGIN mysql 登入結果與 DB 看到的來源（USER()），1045 = 帳號層、2003 = 網路層

set -u
: "${PRIVATE_HOST:?請先 export PRIVATE_HOST}"
PUBLIC_HOST="${PUBLIC_HOST:-}"
DB_PORT="${DB_PORT:-3306}"
DB_USER="${DB_USER:-akmadmin}"

echo "== 測試端：$(hostname)"
if command -v ip >/dev/null 2>&1; then
  ip -4 -o addr show scope global | awk '{print "   " $2 " " $4}'
fi
echo

resolve() {
  # 優先用 getent（走系統 resolver，跟應用程式一致）；macOS 沒有 getent 就用 dscacheutil
  if command -v getent >/dev/null 2>&1; then
    getent ahostsv4 "$1" | awk 'NR==1{print $1}'
  else
    dscacheutil -q host -a name "$1" 2>/dev/null | awk '/^ip_address/{print $2; exit}'
  fi
}

tcp_check() {
  if command -v nc >/dev/null 2>&1; then
    nc -z -w 3 "$1" "$2" >/dev/null 2>&1
  else
    timeout 3 bash -c "</dev/tcp/$1/$2" >/dev/null 2>&1
  fi
}

login_check() {
  if [ -z "${MYSQL_PWD:-}" ]; then echo "SKIP(未設 MYSQL_PWD)"; return; fi
  if ! command -v mysql >/dev/null 2>&1; then echo "SKIP(沒有 mysql client)"; return; fi
  local out
  out=$(mysql -h "$1" -P "$DB_PORT" -u "$DB_USER" --ssl-mode=REQUIRED \
          --connect-timeout=5 -N -B -e "SELECT USER();" 2>&1 | grep -v 'Using a password')
  if echo "$out" | grep -q '^ERROR'; then
    echo "FAIL $(echo "$out" | grep -o 'ERROR [0-9]*' | head -1)"
  else
    echo "OK source=$out"
  fi
}

printf '%-8s %-16s %-6s %s\n' "HOST" "DNS" "TCP" "LOGIN"
for kind in private public; do
  if [ "$kind" = private ]; then h="$PRIVATE_HOST"; else h="$PUBLIC_HOST"; fi
  if [ -z "$h" ]; then
    printf '%-8s %-16s %-6s %s\n' "$kind" "-" "-" "SKIP(未設 PUBLIC_HOST)"; continue
  fi
  addr=$(resolve "$h")
  if [ -z "$addr" ]; then
    printf '%-8s %-16s %-6s %s\n' "$kind" "NO_DNS" "-" "-"; continue
  fi
  if tcp_check "$h" "$DB_PORT"; then tcp=OK; login=$(login_check "$h"); else tcp=FAIL; login="-"; fi
  printf '%-8s %-16s %-6s %s\n' "$kind" "$addr" "$tcp" "$login"
done
