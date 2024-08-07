# Logstash 插件 JDBC

```
Logstash 自身並不具備持續同步 MySQL 數據庫的功能，但可以使用 Logstash 的 JDBC 插件來實現這一目標。
這個插件可以定期查詢 MySQL 數據庫，並將新數據導入 Elasticsearch 或其他目標存儲。
```

MySQL Connector/J

```
MySQL Connector/J 是 MySQL 的官方 JDBC 驅動程序，用於讓 Java 應用程序與 MySQL 數據庫進行通信。
JDBC（Java Database Connectivity）是一個 Java API，提供了一種通用的方式來連接和操作各種數據庫。
MySQL Connector/J 實現了這個 API，使得 Java 應用程序可以使用 JDBC 來連接到 MySQL 數據庫。
```

## 目錄

- [Logstash 插件 JDBC](#logstash-插件-jdbc)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
- [範例](#範例)
  - [docker-compose 部署](#docker-compose-部署)
  - [假設有兩個 MySQL 資料表 table1 和 table2，將它們的數據分別發送到帶有前綴 prefix\_ 的不同 Elasticsearch 索引中](#假設有兩個-mysql-資料表-table1-和-table2將它們的數據分別發送到帶有前綴-prefix_-的不同-elasticsearch-索引中)

## 參考資料

[如何使用 Logstash 和 JDBC 确保 Elasticsearch 与关系型数据库保持同步](https://www.elastic.co/cn/blog/how-to-keep-elasticsearch-synchronized-with-a-relational-database-using-logstash)

[MySQL Connector/J 下載頁面](https://dev.mysql.com/downloads/connector/j/)

# 安裝

## Debian (Ubuntu)

```bash
bin/logstash-plugin install logstash-input-jdbc
```

## 配置文檔

通常在 `/path/to/your/logstash.conf`

### 基本範例

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

    jdbc_paging_enabled => true
    jdbc_page_size => 10000  # 每次查詢 10000 條數據
  }
}
```

```
jdbc_connection_string：MySQL 連接字串。
jdbc_user 和 jdbc_password：MySQL 用戶名和密碼。
jdbc_driver_library：MySQL 驅動程序的路徑。
jdbc_driver_class：MySQL 驅動程序類。
statement：SQL 查詢語句。:sql_last_value 是 Logstash 跟踪上次運行的時間戳。
clean_run：設置為 false 以保持上次運行的狀態。
last_run_metadata_path：存儲上次運行狀態的文件路徑。

use_column_value： 設置為 true，表示將使用 tracking_column 指定的列值來跟踪數據變更。
tracking_column： 設置為 updated_at，表示將使用這個時間戳列來判斷哪些數據是新的或更新的。
tracking_column_type： 設置為 timestamp，表示跟踪列的數據類型是時間戳。
last_run_metadata_path： 指定了一個文件路徑，用來存儲上次查詢的跟踪列值。
schedule： 設置了查詢的調度計劃。定期運行的時間表，這裡設置為每 5 分鐘運行一次。

工作原理
    首次運行：Logstash 將執行查詢，選擇所有符合條件的數據（updated_at > :sql_last_value）。
    保存狀態：查詢完成後，Logstash 將保存最新的 updated_at 值到 last_run_metadata_path 指定的文件中。
    後續運行：Logstash 在後續的查詢中，將使用保存的 updated_at 值作為基準，只選擇更新或新增的數據。
```

Logstash 會自動分批加載數據，直到所有數據都被處理完畢，從而減少內存壓力和提高查詢性能。

```
jdbc_paging_enabled 設置為 true，啟用分頁查詢。
jdbc_page_size 設置為 10000，每次查詢返回 10000 條數據。
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

```bash
bin/logstash -f /path/to/your/logstash.conf
```

檢查已安裝的插件列表

```bash
bin/logstash-plugin list
```

# 範例

## docker-compose 部署

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

## 假設有兩個 MySQL 資料表 table1 和 table2，將它們的數據分別發送到帶有前綴 prefix_ 的不同 Elasticsearch 索引中

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
