#!/bin/bash

echo "🚀 開始批次更新 monstache.* 索引設定"

for index in $(curl -s "http://localhost:9200/_cat/indices/monstache.*?h=index"); do
  echo "📦 處理索引：$index"

  # 備份現有設定
  echo "🔒 備份設定到 backup_settings_$index.json"
  curl -s "http://localhost:9200/$index/_settings" > "backup_settings_$index.json"

  # 檢查當前設定（可擴充）
  replicas=$(grep -o "\"number_of_replicas\"[[:space:]]*:[[:space:]]*\"[0-9]*\"" "backup_settings_$index.json" | sed -E 's/.*"([0-9]+)".*/\1/')
  refresh_interval=$(grep -o "\"refresh_interval\"[[:space:]]*:[[:space:]]*\"[^\"]*\"" "backup_settings_$index.json" | sed -E 's/.*"([^"]+)".*/\1/')

  echo "🔍 當前副本數：$replicas，refresh_interval：$refresh_interval"

  # 設定新的值
  echo "🛠️  更新 $index 設定..."
  curl -s -X PUT "http://localhost:9200/$index/_settings" \
    -H 'Content-Type: application/json' \
    -d '{
      "index": {
        "number_of_replicas": 0,
        "refresh_interval": "24h"
      }
    }' > /dev/null

  echo "✅ 完成更新：$index"
done

echo "🎉 所有索引處理完成！"
