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
    - [JDBC插件相關](#jdbc插件相關)
    - [心得相關](#心得相關)
  - [配置文檔](#配置文檔)
- [JDBC 插件](#jdbc-插件)
  - [安裝 JDBC](#安裝-jdbc)
  - [配置 JDBC](#配置-jdbc)
- [指令](#指令)
- [docker-compose 部署](#docker-compose-部署)
  - [Github deviantony/docker-elk](#github-deviantonydocker-elk)
- [範例](#範例)
  - [假設有兩個 MySQL 資料表 table1 和 table2，希望將它們的數據分別發送到帶有前綴 prefix\_ 的不同 Elasticsearch 索引中。](#假設有兩個-mysql-資料表-table1-和-table2希望將它們的數據分別發送到帶有前綴-prefix_-的不同-elasticsearch-索引中)

## 參考資料

[elastic 官方網站 logstash 部分](https://www.elastic.co/logstash)

[Structure of a pipeline](https://www.elastic.co/guide/en/logstash/current/configuration-file-structure.html)

### docker-compose 相關

[Github deviantony/docker-elk](https://github.com/deviantony/docker-elk/blob/main/docker-compose.yml)

### JDBC插件相關

[如何使用 Logstash 和 JDBC 确保 Elasticsearch 与关系型数据库保持同步](https://www.elastic.co/cn/blog/how-to-keep-elasticsearch-synchronized-with-a-relational-database-using-logstash)

### 心得相關

[2.1 Config Of Logstash](https://mmx362003.gitbooks.io/elk-stack-guide/content/config_of_logstash.html)

[一文快速上手Logstash](https://elasticsearch.cn/article/6141)

[ElasticSearch7.3学习(三十二)----logstash三大插件（input、filter、output）及其综合示例](https://www.cnblogs.com/xiaoyh/p/16270516.html)

[ELK中Logstash的配置和用法](https://blog.csdn.net/rxbook/article/details/132405459)

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

# JDBC 插件

```
Logstash 自身並不具備持續同步 MySQL 數據庫的功能，但可以使用 Logstash 的 JDBC 插件來實現這一目標。
這個插件可以定期查詢 MySQL 數據庫，並將新數據導入 Elasticsearch 或其他目標存儲。
```

## 安裝 JDBC

```bash
bin/logstash-plugin install logstash-input-jdbc
```

## 配置 JDBC

```conf
input {
  jdbc {
    jdbc_connection_string => "jdbc:mysql://localhost:3306/your_database"
    jdbc_user => "your_user"
    jdbc_password => "your_password"
    jdbc_driver_library => "/path/to/mysql-connector-java.jar"
    jdbc_driver_class => "com.mysql.cj.jdbc.Driver"
    statement => "SELECT * FROM your_table WHERE updated_at > :sql_last_value"
    use_column_value => true
    tracking_column => "updated_at"
    schedule => "*/5 * * * *"  # 每 5 分鐘運行一次
    clean_run => false
    last_run_metadata_path => "/path/to/.logstash_jdbc_last_run"
  }
}
```

```
jdbc_connection_string：MySQL 連接字串。
jdbc_user 和 jdbc_password：MySQL 用戶名和密碼。
jdbc_driver_library：MySQL 驅動程序的路徑。
jdbc_driver_class：MySQL 驅動程序類。
statement：SQL 查詢語句。:sql_last_value 是 Logstash 跟踪上次運行的時間戳。
use_column_value 和 tracking_column：跟踪列的設置。
schedule：定期運行的時間表，這裡設置為每 5 分鐘運行一次。
clean_run：設置為 false 以保持上次運行的狀態。
last_run_metadata_path：存儲上次運行狀態的文件路徑。
```

```conf
output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "your_index"
  }
  stdout {
    codec => rubydebug
  }
}
```

```
elasticsearch：將數據發送到 Elasticsearch。
stdout：將數據輸出到控制台，便於調試。
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

# docker-compose 部署

```yml
version: '3'
services:
  elasticsearch:
    build: ./elasticsearch
    container_name: elasticsearch
    privileged: true
    environment:
      - "cluster.name=elasticsearch"
      - "discovery.type=single-node"
      - "ES_JAVA_OPTS=-Xms512m -Xmx2g"
      - bootstrap.memory_lock=true
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es/data:/usr/share/elasticsearch/data
      - ./es/logs:/usr/share/elasticsearch/logs
    ports:
      - 9200:9200
      - 9300:9300

  kibana:
    image: kibana:7.13.3
    container_name: kibana
    depends_on:
      - elasticsearch
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
      I18N_LOCALE: zh-CN
    ports:
      - 5601:5601

  logstash:
    image: logstash:7.13.3
    container_name: logstash
    volumes:
      - ./logstash/pipeline:/usr/share/logstash/pipeline
      - ./logstash/config:/usr/share/logstash/config
    environment:
      - "xpack.monitoring.elasticsearch.hosts=http://elasticsearch:9200"
    depends_on:
      - elasticsearch
    ports:
      - 5044:5044
      - 9600:9600
```

```conf
input {
  jdbc {
    jdbc_connection_string => "jdbc:mysql://your_mysql_host:3306/your_database"
    jdbc_user => "your_user"
    jdbc_password => "your_password"
    jdbc_driver_library => "/usr/share/logstash/mysql-connector-java.jar"
    jdbc_driver_class => "com.mysql.cj.jdbc.Driver"
    statement => "SELECT * FROM your_table WHERE updated_at > :sql_last_value"
    use_column_value => true
    tracking_column => "updated_at"
    schedule => "*/5 * * * *"  # 每 5 分钟运行一次
    clean_run => false
    last_run_metadata_path => "/usr/share/logstash/last_run_metadata/.logstash_jdbc_last_run"
  }
}

filter {
  # 在這裡新增任何需要的過濾和處理邏輯
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "your_index"
  }
  stdout {
    codec => rubydebug
  }
}
```

```
your_project/
├── docker-compose.yml
├── elasticsearch/
│   └── Dockerfile
├── es/
│   ├── plugins/
│   ├── data/
│   └── logs/
└── logstash/
    ├── pipeline/
    │   └── logstash.conf
    └── config/
```

下載 MySQL JDBC 驅動程式（例如 mysql-connector-java.jar）並將其放入 logstash/config 目錄中。

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
      FILEBEAT_INTERNAL_PASSWORD: ${FILEBEAT_INTERNAL_PASSWORD:-}
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

## 假設有兩個 MySQL 資料表 table1 和 table2，希望將它們的數據分別發送到帶有前綴 prefix_ 的不同 Elasticsearch 索引中。

```conf
input {
  jdbc {
    jdbc_connection_string => "jdbc:mysql://localhost:3306/your_database"
    jdbc_user => "your_username"
    jdbc_password => "your_password"
    jdbc_driver_library => "/path/to/mysql-connector-java.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    schedule => "*/5 * * * *" # 每5分鐘執行一次查詢
    statement => "SELECT * FROM table1"
    type => "table1"
  }

  jdbc {
    jdbc_connection_string => "jdbc:mysql://localhost:3306/your_database"
    jdbc_user => "your_username"
    jdbc_password => "your_password"
    jdbc_driver_library => "/path/to/mysql-connector-java.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    schedule => "*/5 * * * *" # 每5分鐘執行一次查詢
    statement => "SELECT * FROM table2"
    type => "table2"
  }
}

filter {
  if [type] == "table1" {
    mutate {
      add_field => { "table" => "table1" }
    }
  }

  if [type] == "table2" {
    mutate {
      add_field => { "table" => "table2" }
    }
  }
}

output {
  if [type] == "table1" {
    elasticsearch {
      hosts => ["localhost:9200"]
      index => "prefix_table1-%{+YYYY.MM.dd}" # 按日期分割索引
      document_id => "%{id}" # 假設每個表都有一個唯一的 id 欄位
    }
  }

  if [type] == "table2" {
    elasticsearch {
      hosts => ["localhost:9200"]
      index => "prefix_table2-%{+YYYY.MM.dd}" # 按日期分割索引
      document_id => "%{id}" # 假設每個表都有一個唯一的 id 欄位
    }
  }

  stdout {
    codec => rubydebug
  }
}
```