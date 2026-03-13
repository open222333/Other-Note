#!/bin/bash

echo "🚀 開始還原 monstache.* 索引設定"

# 假設備份檔案為 "backup_settings_$index.json"
for index in $(curl -s "http://localhost:9200/_cat/indices/monstache.*?h=index"); do
  echo "📦 處理索引：$index"

  # 確認備份檔案是否存在
  backup_file="backup_settings_$index.json"
  if [ -f "$backup_file" ]; then
    echo "🔄 找到備份檔案：$backup_file，開始還原設定..."
    curl -s -X PUT "http://localhost:9200/$index/_settings" \
      -H 'Content-Type: application/json' \
      -d @"$backup_file" > /dev/null
    echo "✅ 完成還原：$index"
  else
    echo "⚠️ 未找到備份檔案：$backup_file，無法還原設定。"
  fi
done

echo "🎉 還原完成！"
