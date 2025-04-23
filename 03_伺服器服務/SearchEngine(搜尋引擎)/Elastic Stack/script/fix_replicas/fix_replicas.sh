#!/bin/bash

echo "🔍 檢查並修改 monstache.* index 的副本數 (不使用 jq)..."

# 取得所有 monstache.* 的索引名稱
for index in $(curl -s "http://localhost:9200/_cat/indices/monstache.*?h=index"); do
  # 抓取該索引的設定並用 grep/sed 解析出副本數
  replicas=$(curl -s "http://localhost:9200/$index/_settings" \
    | grep -o "\"number_of_replicas\"[[:space:]]*:[[:space:]]*\"[0-9]*\"" \
    | sed -E 's/.*"([0-9]+)".*/\1/')

  if [[ "$replicas" != "0" ]]; then
    echo "⚠️  $index 的副本數是 $replicas，正在修改為 0..."
    curl -s -X PUT "http://localhost:9200/$index/_settings" \
      -H 'Content-Type: application/json' \
      -d '{"index": {"number_of_replicas": 0}}' > /dev/null
    echo "✅  $index 修改完成。"
  else
    echo "✔️  $index 已是 replicas=0，略過。"
  fi
done

echo "🎉 所有 index 處理完成。"
