#!/bin/bash
# check_table_binlog.sh
# 檢查 Master 與 Slave 的 binlog 是否有特定表的操作

# 1️⃣ 輸入 Master 資訊
read -p "輸入 Master IP: " MASTER_HOST
read -p "輸入 Master 帳號: " MASTER_USER
read -s -p "輸入 Master 使用者 ($MASTER_USER) 的密碼: " MASTER_PASS
echo ""
read -p "輸入 Master 資料庫: " MASTER_DB

# 2️⃣ 輸入 Slave 資訊
read -p "輸入 Slave IP: " SLAVE_HOST
read -p "輸入 Slave 帳號: " SLAVE_USER
read -s -p "輸入 Slave 使用者 ($SLAVE_USER) 的密碼: " SLAVE_PASS
echo ""
read -p "輸入 Slave 資料庫: " SLAVE_DB

# 3️⃣ 輸入要檢查的表
read -p "輸入要檢查的表名: " TABLE_NAME

echo ""
echo "📥 從 Master 讀取 binlog 列表..."
BINLOGS=$(mysql -h $MASTER_HOST -u $MASTER_USER -p$MASTER_PASS -N -e "SHOW BINARY LOGS;" | awk '{print $1}')

FOUND=false

for LOG in $BINLOGS; do
  echo "🔍 檢查 Master binlog: $LOG ..."
  mysqlbinlog -h $MASTER_HOST -u $MASTER_USER -p$MASTER_PASS --read-from-remote-server $LOG | grep -qi "$TABLE_NAME"
  if [ $? -eq 0 ]; then
    FOUND=true
    echo "⚠️  在 $LOG 發現 $TABLE_NAME 相關操作"
  fi
done

if [ "$FOUND" = true ]; then
  echo "⚠️ Master binlog 裡有 $TABLE_NAME 相關操作"
else
  echo "✅ Master binlog 沒有 $TABLE_NAME 相關操作"
fi

# 可選：檢查 Slave 是否也有相同事件
echo ""
echo "📥 從 Slave 讀取 binlog 列表..."
SLAVE_BINLOGS=$(mysql -h $SLAVE_HOST -u $SLAVE_USER -p$SLAVE_PASS -N -e "SHOW BINARY LOGS;" | awk '{print $1}')

FOUND_SLAVE=false

for LOG in $SLAVE_BINLOGS; do
  echo "🔍 檢查 Slave binlog: $LOG ..."
  mysqlbinlog -h $SLAVE_HOST -u $SLAVE_USER -p$SLAVE_PASS --read-from-remote-server $LOG | grep -qi "$TABLE_NAME"
  if [ $? -eq 0 ]; then
    FOUND_SLAVE=true
    echo "⚠️  在 $LOG 發現 $TABLE_NAME 相關操作"
  fi
done

if [ "$FOUND_SLAVE" = true ]; then
  echo "⚠️ Slave binlog 裡有 $TABLE_NAME 相關操作"
else
  echo "✅ Slave binlog 沒有 $TABLE_NAME 相關操作"
fi
