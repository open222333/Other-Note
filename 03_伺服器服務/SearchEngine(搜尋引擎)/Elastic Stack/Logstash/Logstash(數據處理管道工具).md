# Logstash(數據處理管道工具)

```
Logstash 是一個開源的數據處理管道工具，它能夠從多個數據源收集數據，並且對數據進行過濾、解析和轉換，最後將數據傳輸到一個或多個數據儲存目標。
Logstash 是 Elastic Stack（以前稱為 ELK Stack）的一部分，與 Elasticsearch 和 Kibana 一起使用，可以實現強大的數據分析和可視化功能。

主要特點：

數據收集： Logstash 支持從多種數據源收集數據，包括日誌文件、消息隊列、數據庫等。

數據處理： Logstash 可以使用一系列過濾器來解析和轉換數據，這些過濾器可以處理複雜的數據格式並對數據進行豐富的操作。

數據輸出： Logstash 支持將處理後的數據發送到多個目標，包括 Elasticsearch、文件、數據庫等。

靈活性和可擴展性： Logstash 擁有豐富的插件系統，允許用戶根據需求擴展其功能。

典型使用場景

日誌管理： 收集和處理來自不同應用程序和系統的日誌數據，將其發送到 Elasticsearch 進行搜索和分析。
數據管道： 構建數據處理管道，將數據從多個源收集並進行轉換，然後發送到數據湖或數據倉庫。
實時數據處理： 實時處理和分析流式數據，以便快速響應業務需求。
```

## 目錄

- [Logstash(數據處理管道工具)](#logstash數據處理管道工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [docker-compose 相關](#docker-compose-相關)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [CentOS7](#centos7)
  - [配置文檔](#配置文檔)
- [指令](#指令)
  - [服務操作](#服務操作)
  - [自建 docker-compose](#自建-docker-compose)
  - [Github deviantony/docker-elk](#github-deviantonydocker-elk)
- [範例](#範例)
  - [將已經匯出的 MySQL 資料檔案（例如 CSV 檔案）導入 Elasticsearch](#將已經匯出的-mysql-資料檔案例如-csv-檔案導入-elasticsearch)
  - [使用 Python 將 sql 檔建立 index](#使用-python-將-sql-檔建立-index)
  - [使用 Python 將 csv 檔建立 index](#使用-python-將-csv-檔建立-index)

## 參考資料

[elastic 官方網站 logstash 部分](https://www.elastic.co/logstash)

[Structure of a pipeline](https://www.elastic.co/guide/en/logstash/current/configuration-file-structure.html)

### docker-compose 相關

[Github deviantony/docker-elk](https://github.com/deviantony/docker-elk/blob/main/docker-compose.yml)

### 心得相關

[2.1 Config Of Logstash](https://mmx362003.gitbooks.io/elk-stack-guide/content/config_of_logstash.html)

[一文快速上手Logstash](https://elasticsearch.cn/article/6141)

[ElasticSearch7.3学习(三十二)----logstash三大插件（input、filter、output）及其综合示例](https://www.cnblogs.com/xiaoyh/p/16270516.html)

[ELK中Logstash的配置和用法](https://blog.csdn.net/rxbook/article/details/132405459)

[筆記 Github conf 範例](https://github.com/open222333/Other-Elasticstack/tree/main/single-node-01/logstash/pipeline)

# 安裝

## CentOS7

```bash
yum install logstash
```

## 配置文檔

創建配置文件

首先，創建一個配置文件（例如 logstash.conf）

```conf
input {
  file {
    path => "/path/to/your/logfile.log"
    start_position => "beginning"
  }
}

filter {
  grok {
    match => { "message" => "%{COMBINEDAPACHELOG}" }
  }
  date {
    match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ]
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "logstash-%{+YYYY.MM.dd}"
  }
}
```

Input（輸入）部分

```conf
input {
  file {
    path => "/path/to/your/logfile.log"
    start_position => "beginning"
    sincedb_path => "/dev/null"
  }
}
```

```
path：指定要讀取的日誌文件路徑。
start_position：設定為 beginning 以便從文件的開始位置讀取數據（僅在首次運行時有效）。
sincedb_path：使用 /dev/null 來禁用 sincedb（僅在測試環境中使用，不建議在生產環境中使用）。
```

Filter（過濾器）部分

```conf
filter {
  grok {
    match => { "message" => "%{COMBINEDAPACHELOG}" }
  }
  date {
    match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ]
  }
  mutate {
    remove_field => [ "message", "host" ]
  }
}
```

```
grok：解析 Apache 日誌格式。
date：解析日誌中的時間戳，並將其轉換為 Logstash 的 @timestamp 字段。
mutate：移除不需要的字段，如 message 和 host。
```

Output（輸出）部分

```conf
output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "logstash-%{+YYYY.MM.dd}"
  }
  stdout {
    codec => rubydebug
  }
}
```

```
elasticsearch：將處理後的數據發送到 Elasticsearch，並以日期為索引名稱的一部分。
stdout：將數據輸出到控制台，使用 rubydebug 編解碼器以便於閱讀。
```

# 指令

安裝路徑都是在/usr/share/logstash

如果找不到請用下列指令去尋找

```bash
find / -type d -name 'logstash'
```

確定後請下(啟動logstash)

```bash
systemctl start logstash
```

如果發現logstash.service 沒有被enable，請先把他enable 起來

```bash
systemctl enable application.service
```

停止service

```bash
systemctl start logstash
```

運行 Logstash

配置文件編寫完成後，可以使用以下命令運行 Logstash

Logstash 將開始讀取指定的日誌文件，解析數據，並將處理後的數據發送到 Elasticsearch，同時將數據輸出到控制台以便於調試。

```bash
bin/logstash -f /path/to/your/logstash.conf
```

檢查已安裝的插件列表

```bash
bin/logstash-plugin list
```

## 服務操作

```bash
# 啟動服務
systemctl start logstash

# 查詢啟動狀態
systemctl status logstash

# 重新啟動
systemctl restart logstash

# 停止服務
systemctl stop logstash

# 開啟開機自動啟動
systemctl enable logstash

# 關閉開機自動啟動
systemctl disable logstash
```

## 自建 docker-compose

```yml
version: '3'
services:
  elasticsearch:
    image: elasticsearch:${STACK_VERSION}
    container_name: elasticsearch
    privileged: true
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx1096m" # 設置使用jvm內存大小
      - "ES_HEAP_SIZE=512m"
      - "MAX_OPEN_FILES=65535"
      - "MAX_LOCKED_MEMORY=unlimited"
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml:ro
      - ./es/data:/usr/share/elasticsearch/data:rw # 數據文件掛載
      - ./es/logs:/usr/share/elasticsearch/logs:rw
    ports:
      - 9200:9200
  kibana:
    image: kibana:${STACK_VERSION}
    container_name: kibana
    depends_on:
      - elasticsearch # kibana在elasticsearch啟動之後再啟動
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200 # 設置訪問elasticsearch的地址
      I18N_LOCALE: zh-CN
    ports:
      - 5601:5601
  logstash:
    image: logstash:${STACK_VERSION}
    container_name: logstash
    depends_on:
      - elasticsearch # logstash在elasticsearch啟動之後再啟動
    volumes:
      - ./logstash/pipeline:/usr/share/logstash/pipeline:ro # 將 Logstash pipeline 配置文件掛載到容器中
    ports:
      - 5044:5044 # 預設 Logstash 的 Beats input port
    environment:
      - "XPACK_MONITORING_ELASTICSEARCH_HOSTS=http://elasticsearch:9200" # 設置監控地址
    command: logstash -f /usr/share/logstash/pipeline/logstash.conf # 設置 Logstash 配置文件
```

專案結構

```
project-root/
├── docker-compose.yml
├── es/
│   ├── config/
│   │   └── elasticsearch.yml
│   ├── data/
│   ├── logs/
│   └── plugins/
├── kibana/
│   └── config/
│       └── kibana.yml
└── logstash/
    ├── config/
    │   └── logstash.yml
    └── pipeline/
        └── logstash.conf
```

## Github deviantony/docker-elk

```yml
version: '3.7'

services:

  # The 'setup' service runs a one-off script which initializes users inside
  # Elasticsearch — such as 'logstash_internal' and 'kibana_system' — with the
  # values of the passwords defined in the '.env' file. It also creates the
  # roles required by some of these users.
  #
  # This task only needs to be performed once, during the *initial* startup of
  # the stack. Any subsequent run will reset the passwords of existing users to
  # the values defined inside the '.env' file, and the built-in roles to their
  # default permissions.
  #
  # By default, it is excluded from the services started by 'docker compose up'
  # due to the non-default profile it belongs to. To run it, either provide the
  # '--profile=setup' CLI flag to Compose commands, or "up" the service by name
  # such as 'docker compose up setup'.
  setup:
    profiles:
      - setup
    build:
      context: setup/
      args:
        ELASTIC_VERSION: ${ELASTIC_VERSION}
    init: true
    volumes:
      - ./setup/entrypoint.sh:/entrypoint.sh:ro,Z
      - ./setup/lib.sh:/lib.sh:ro,Z
      - ./setup/roles:/roles:ro,Z
    environment:
      ELASTIC_PASSWORD: ${ELASTIC_PASSWORD:-}
      LOGSTASH_INTERNAL_PASSWORD: ${LOGSTASH_INTERNAL_PASSWORD:-}
      KIBANA_SYSTEM_PASSWORD: ${KIBANA_SYSTEM_PASSWORD:-}
      METRICBEAT_INTERNAL_PASSWORD: ${METRICBEAT_INTERNAL_PASSWORD:-}
      LOGSTASH_INTERNAL_PASSWORD: ${LOGSTASH_INTERNAL_PASSWORD:-}
      HEARTBEAT_INTERNAL_PASSWORD: ${HEARTBEAT_INTERNAL_PASSWORD:-}
      MONITORING_INTERNAL_PASSWORD: ${MONITORING_INTERNAL_PASSWORD:-}
      BEATS_SYSTEM_PASSWORD: ${BEATS_SYSTEM_PASSWORD:-}
    networks:
      - elk
    depends_on:
      - elasticsearch

  elasticsearch:
    build:
      context: elasticsearch/
      args:
        ELASTIC_VERSION: ${ELASTIC_VERSION}
    volumes:
      - ./elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml:ro,Z
      - elasticsearch:/usr/share/elasticsearch/data:Z
    ports:
      - 9200:9200
      - 9300:9300
    environment:
      node.name: elasticsearch
      ES_JAVA_OPTS: -Xms512m -Xmx512m
      # Bootstrap password.
      # Used to initialize the keystore during the initial startup of
      # Elasticsearch. Ignored on subsequent runs.
      ELASTIC_PASSWORD: ${ELASTIC_PASSWORD:-}
      # Use single node discovery in order to disable production mode and avoid bootstrap checks.
      # see: https://www.elastic.co/guide/en/elasticsearch/reference/current/bootstrap-checks.html
      discovery.type: single-node
    networks:
      - elk
    restart: unless-stopped

  logstash:
    build:
      context: logstash/
      args:
        ELASTIC_VERSION: ${ELASTIC_VERSION}
    volumes:
      - ./logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml:ro,Z
      - ./logstash/pipeline:/usr/share/logstash/pipeline:ro,Z
    ports:
      - 5044:5044
      - 50000:50000/tcp
      - 50000:50000/udp
      - 9600:9600
    environment:
      LS_JAVA_OPTS: -Xms256m -Xmx256m
      LOGSTASH_INTERNAL_PASSWORD: ${LOGSTASH_INTERNAL_PASSWORD:-}
    networks:
      - elk
    depends_on:
      - elasticsearch
    restart: unless-stopped

  kibana:
    build:
      context: kibana/
      args:
        ELASTIC_VERSION: ${ELASTIC_VERSION}
    volumes:
      - ./kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml:ro,Z
    ports:
      - 5601:5601
    environment:
      KIBANA_SYSTEM_PASSWORD: ${KIBANA_SYSTEM_PASSWORD:-}
    networks:
      - elk
    depends_on:
      - elasticsearch
    restart: unless-stopped

networks:
  elk:
    driver: bridge

volumes:
  elasticsearch:
```

# 範例

## 將已經匯出的 MySQL 資料檔案（例如 CSV 檔案）導入 Elasticsearch

將 MySQL 資料匯出為 CSV 檔案：假設已經完成這一步。

設定 Logstash 的配置檔：配置 Logstash 以從 CSV 檔案讀取資料並將其發送到 Elasticsearch。

啟動 Logstash：啟動 Logstash 以執行配置檔並將資料匯入 Elasticsearch。

```conf
input {
  file {
    path => "/path/to/data.csv"
    start_position => "beginning"
    sincedb_path => "/dev/null"
  }
}

filter {
  csv {
    separator => ","
    columns => ["id", "name", "age"]
  }

  mutate {
    ; integer ：整數
    ; float ：浮點數
    ; string ：字符串
    ; boolean ：布爾值（true 或 false）
    ; array ：數組
    convert => {
      "id" => "integer"
      "age" => "integer"
    }
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "your_index_name"
    document_id => "%{id}"
  }
  stdout {
    codec => rubydebug
  }
}
```

```
input

    path: CSV 檔案的路徑。
    start_position: 指定從檔案的開頭開始讀取。
    sincedb_path: 指定 sincedb 路徑（在這裡使用 /dev/null 以防止 Logstash 記住文件位置，適合於測試）。

filter

    csv: 用於解析 CSV 資料。
    separator: 指定 CSV 的分隔符號。
    columns: 指定 CSV 的欄位名稱。
    mutate: 用於轉換資料類型。
    convert: 將 id 和 age 欄位轉換為整數。

output

    elasticsearch: 發送資料到 Elasticsearch。
    hosts: Elasticsearch 伺服器的地址。
    index: 在 Elasticsearch 中創建的索引名稱。
    document_id: Elasticsearch 中文件的 ID，這裡假設表中有一個 id 欄位。
    stdout: 將處理後的資料輸出到控制台（用於調試）。
```

```bash
logstash -f /path/to/logstash.conf
```

## 使用 Python 將 sql 檔建立 index

```Python
import sqlparse
import json
from elasticsearch import Elasticsearch

# 讀取 .sql 檔案
with open('data.sql', 'r') as file:
    sql_content = file.read()

# 解析 .sql 檔案內容
parsed = sqlparse.parse(sql_content)

# 提取列名和數據
columns = ['id', 'name', 'age']  # 根據你的 .sql 檔案中的表結構
data = []
for statement in parsed:
    if statement.get_type() == 'INSERT':
        values = statement.get_values()
        data.append(dict(zip(columns, values)))

# 將數據轉換為 JSON
json_data = json.dumps(data, indent=2)

# 連接到 Elasticsearch
es = Elasticsearch(['http://localhost:9200'])

# 將數據插入 Elasticsearch 索引
index_name = 'your_index_name'
for doc in data:
    es.index(index=index_name, body=doc)

print('Data imported successfully')
```

## 使用 Python 將 csv 檔建立 index

匯出資料為 CSV

```sql
SELECT * INTO OUTFILE '/path/to/yourfile.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
FROM your_table;
```

將 CSV 轉換為 JSON

```Python
import csv
import json

csv_file_path = '/path/to/yourfile.csv'
json_file_path = '/path/to/yourfile.json'

# 讀取 CSV 並轉換為 JSON
with open(csv_file_path, mode='r', encoding='utf-8') as csv_file:
    csv_reader = csv.DictReader(csv_file)
    rows = list(csv_reader)

with open(json_file_path, mode='w', encoding='utf-8') as json_file:
    json.dump(rows, json_file, indent=2)
```

將 JSON 資料導入 Elasticsearch

```Python
from elasticsearch import Elasticsearch
import json

# 連接到 Elasticsearch
es = Elasticsearch(['http://localhost:9200'])

index_name = 'your_index_name'
json_file_path = '/path/to/yourfile.json'

# 讀取 JSON 檔案
with open(json_file_path, mode='r', encoding='utf-8') as json_file:
    data = json.load(json_file)

# 將資料插入 Elasticsearch 索引
for doc in data:
    es.index(index=index_name, body=doc)

print('Data imported successfully')
```
