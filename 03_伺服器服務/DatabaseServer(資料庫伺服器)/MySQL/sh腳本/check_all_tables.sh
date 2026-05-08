#!/bin/bash
# 檢查 Slave 缺表，若缺少則從 Master 匯出 schema 並建立
# 已存在的表不會覆蓋
# 可用 --dry-run 只檢查不建立

DRYRUN=false
if [[ "$1" == "--dry-run" ]]; then
  DRYRUN=true
  echo "📝 目前為 Dry-Run 模式，只檢查，不會建立任何表"
fi

read -p "輸入 Master IP: " MASTER_HOST
read -p "輸入 Master 帳號: " MASTER_USER
read -s -p "輸入 Master 使用者 ($MASTER_USER) 的密碼: " MASTER_PASS
echo ""
read -p "輸入 Master 資料庫: " MASTER_DB

read -p "輸入 Slave IP: " SLAVE_HOST
read -p "輸入 Slave 帳號: " SLAVE_USER
read -s -p "輸入 Slave 使用者 ($SLAVE_USER) 的密碼: " SLAVE_PASS
echo ""
read -p "輸入 Slave 資料庫: " SLAVE_DB

echo ""
echo "📋 連線資訊確認："
echo "   Master: $MASTER_USER@$MASTER_HOST/$MASTER_DB"
echo "   Slave : $SLAVE_USER@$SLAVE_HOST/$SLAVE_DB"
echo ""

echo "🔍 從 Master 讀取所有表... (需要 Master 密碼)"
TABLES=$(mysql -h"$MASTER_HOST" -u"$MASTER_USER" --password="$MASTER_PASS" -Nse "SHOW TABLES FROM $MASTER_DB;")

if [ -z "$TABLES" ]; then
  echo "❌ 無法讀取 Master ($MASTER_HOST) 的資料庫 $MASTER_DB"
  exit 1
fi

# 統計用變數
TOTAL=0
MISSING=0
CREATED=0
EXISTED=0

for TABLE in $TABLES; do
  TOTAL=$((TOTAL+1))
  echo "➡️ 檢查表: $TABLE (需要 Slave 密碼)"

  SLAVE_TABLE_EXISTS=$(mysql -h"$SLAVE_HOST" -u"$SLAVE_USER" --password="$SLAVE_PASS" -Nse "SHOW TABLES FROM $SLAVE_DB LIKE '$TABLE';" 2>/dev/null)

  if [ -z "$SLAVE_TABLE_EXISTS" ]; then
    MISSING=$((MISSING+1))
    if $DRYRUN; then
      echo "⚠️  Slave 缺少表 $SLAVE_DB.$TABLE（Dry-Run: 不建立）"
    else
      echo "⚠️  Slave 缺少表 $SLAVE_DB.$TABLE，正在建立..."
      echo "👉 從 Master 匯出 $TABLE schema (需要 Master 密碼)"
      mysqldump -h"$MASTER_HOST" -u"$MASTER_USER" --password="$MASTER_PASS" $MASTER_DB $TABLE --no-data > /tmp/${TABLE}_schema.sql

      echo "👉 匯入到 Slave $SLAVE_DB (需要 Slave 密碼)"
      mysql -h"$SLAVE_HOST" -u"$SLAVE_USER" --password="$SLAVE_PASS" $SLAVE_DB < /tmp/${TABLE}_schema.sql

      echo "✅ 已建立表結構: $TABLE"
      CREATED=$((CREATED+1))
    fi
  else
    echo "✅ Slave 已存在表: $TABLE（略過）"
    EXISTED=$((EXISTED+1))
  fi
done

echo ""
echo "📊 檢查完成摘要："
echo "   總表數   : $TOTAL"
echo "   已存在   : $EXISTED"
echo "   缺少表   : $MISSING"
if $DRYRUN; then
  echo "   已建立   : 0（因為 Dry-Run 模式）"
else
  echo "   已建立   : $CREATED"
fi

echo "🎉 任務完成！"
