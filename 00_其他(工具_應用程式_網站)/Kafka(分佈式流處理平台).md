# Kafka(分佈式流處理平台)

```
Kafka 是由 Apache Software Foundation 開發的一個分佈式流處理平台。

它主要用於實時數據流的發布和訂閱（pub-sub）系統，並能夠高效地傳輸、存儲和處理大量數據流。以下是 Kafka 的主要特點和用途：

Kafka 的主要特點

    高吞吐量：Kafka 可以處理每秒數百萬條消息，適合大規模的數據流應用。
    可擴展性：Kafka 是分佈式系統，可以在需要時通過添加更多的節點來擴展系統容量。
    持久化：消息在 Kafka 中是持久化存儲的，並且可以根據配置保留一段時間，保證消息的可靠性和容錯性。
    實時處理：Kafka 支持低延遲的實時數據處理，使其成為構建實時數據流應用的理想選擇。
    分區和複製：數據在 Kafka 主題中可以分區（partition），每個分區可以有多個副本（replica），以確保數據的高可用性和容錯性。

Kafka 的主要組件

    Producer：負責將數據發送到 Kafka 主題的客戶端應用。
    Consumer：從 Kafka 主題中讀取數據的客戶端應用。
    Broker：Kafka 集群中的服務器，每個 Broker 處理一定數量的分區。
    ZooKeeper：用於管理和協調 Kafka 集群節點的服務器。

Kafka 的主要用途

    消息系統：Kafka 可以用作高效的消息隊列系統，支持發送和接收消息的應用。
    實時流處理：Kafka 可以用於實時數據流的處理，與 Apache Flink、Apache Storm、Apache Samza 等流處理框架集成。
    數據管道：Kafka 可以作為數據管道的核心組件，用於連接不同的數據源和數據存儲系統，實現數據的實時傳輸和轉換。
    事件源系統：Kafka 可以用於構建事件驅動的系統，記錄應用中的事件並觸發相應的處理邏輯。

Kafka 的工作原理

    Kafka 的工作原理基於發布-訂閱模型：

    發布：Producer 將消息發送到 Kafka 主題，主題可以有多個分區，消息根據鍵值（key）進行哈希分配到不同的分區。
    訂閱：Consumer 訂閱一個或多個主題，並從指定的分區中讀取消息。每個分區內的消息是有序的，但不同分區之間的消息是無序的。
    持久化：消息在分區內持久化存儲，可以配置保留策略（如保留多久或保留多少條消息）。
    複製和高可用性：每個分區可以有多個副本，主副本負責讀寫操作，其餘副本用於故障恢復。

Kafka 是構建分佈式流處理和實時數據管道的強大工具，廣泛應用於金融、電信、物聯網等領域。


Kafka 確實是一個強大的數據處理工具，但它的功能範圍更廣泛，不僅僅局限於資料處理。以下是 Kafka 在數據處理方面的一些特點和使用場景：

Kafka 作為資料處理工具
數據傳輸管道：

數據整合：Kafka 可以作為數據管道，將不同來源的數據（例如資料庫、日誌系統、應用程序等）整合起來並傳輸到下游系統（例如數據倉庫、數據湖、大數據處理框架）。
實時傳輸：Kafka 支持低延遲的實時數據傳輸，適用於需要實時處理數據的場景。
事件流處理：

事件驅動架構：Kafka 適用於構建事件驅動架構，應用程序可以發布和訂閱事件，從而觸發相應的業務邏輯處理。
複雜事件處理：可以與流處理框架（如 Apache Flink、Apache Storm）結合，進行複雜的事件流處理。
數據分析：

實時分析：Kafka 可以與實時分析工具結合，對流入的數據進行實時分析和報告。
批處理：Kafka 中的數據可以導入到大數據處理框架（如 Apache Hadoop、Apache Spark）進行批處理分析。
Kafka 生態系統
除了核心的 Kafka 系統，Kafka 生態系統中還包含許多工具和組件，這些工具使得 Kafka 更加強大和靈活：

Kafka Connect：

數據集成框架：Kafka Connect 是一個用於連接 Kafka 與其他數據系統的框架，提供了大量的連接器（Connector），如連接資料庫、文件系統、消息隊列等。
標準化和可擴展：簡化了數據流管道的構建和管理，支持分佈式和可擴展的連接器運行環境。
Kafka Streams：

流處理庫：Kafka Streams 是一個輕量級的流處理庫，允許用戶直接在 Kafka 中進行數據流的處理和轉換。
無需外部依賴：與其他流處理框架不同，Kafka Streams 直接運行在 Kafka 集群上，不需要外部依賴。
ksqlDB：

流數據庫：ksqlDB 是一個基於 SQL 的流數據庫，允許用戶使用 SQL 查詢來處理和分析 Kafka 中的數據流。
簡化流處理：降低了流處理的技術門檻，使得非技術人員也能方便地進行實時數據流處理和分析。
使用場景
    日誌和監控：
        Kafka 被廣泛用於收集和處理應用程序的日誌數據，並進行實時監控和告警。
    金融交易：
        Kafka 適用於金融行業的交易處理，保證數據的實時性和一致性。
    物聯網：
        Kafka 可以處理大量來自物聯網設備的數據，進行實時數據流處理和分析。
    網站活動跟踪：
        Kafka 可用於跟踪網站用戶行為，進行實時分析和個性化推薦。
總的來說，Kafka 是一個功能強大且靈活的數據流平台，能夠應對多種不同的數據處理需求。
```

## 目錄

- [sample()](#sample)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[Apache Kafka 官方網站 - 下載 Kafka](https://kafka.apache.org/downloads)

# 安裝

`安裝 Kafka 和 Kafka Connect`

從 Apache Kafka 的官方網站 下載 Kafka。選擇最新版本並下載二進制文件。

```bash
wget https://downloads.apache.org/kafka/3.5.0/kafka_2.13-3.5.0.tgz
```

解壓縮文件

```bash
tar -xzf kafka_2.13-3.5.0.tgz
cd kafka_2.13-3.5.0
```

啟動 ZooKeeper

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

啟動 Kafka 伺服器

```bash
bin/kafka-server-start.sh config/server.properties
```

Kafka Connect 是 Kafka 的一部分，所以不需要額外下載或安裝。只需配置並啟動它。

創建一個新的配置文件 connect-distributed.properties（或使用預設的 config/connect-distributed.properties）。

```ini
bootstrap.servers=localhost:9092
group.id=connect-cluster

key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter
key.converter.schemas.enable=false
value.converter.schemas.enable=false

offset.storage.topic=connect-offsets
config.storage.topic=connect-configs
status.storage.topic=connect-status

offset.storage.replication.factor=1
config.storage.replication.factor=1
status.storage.replication.factor=1

rest.port=8083
```

啟動 Kafka Connect

```bash
bin/connect-distributed.sh config/connect-distributed.properties
```

`驗證 Kafka 和 Kafka Connect`

創建一個 Kafka 主題

```bash
bin/kafka-topics.sh --create --topic test --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

發送一些消息到主題

```bash
bin/kafka-console-producer.sh --topic test --bootstrap-server localhost:9092
>Hello, Kafka!
>Welcome to Kafka Connect.
```

從主題中消費消息

```bash
bin/kafka-console-consumer.sh --topic test --bootstrap-server localhost:9092 --from-beginning
```

安裝 MySQL Connector

```bash
confluent-hub install debezium/debezium-connector-mysql:latest
```

# 用法

啟用 MySQL Binlog

編輯 MySQL 配置文件（通常是 my.cnf 或 my.ini）並添加以下設置：

```
[mysqld]
log-bin=mysql-bin
binlog-format=row
server-id=1
```

重啟 MySQL 服務以應用更改。

創建一個配置文件 mysql-source-connector.json 配置 MySQL Source Connector

```json
{
  "name": "mysql-source-connector",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "localhost",
    "database.port": "3306",
    "database.user": "your_user",
    "database.password": "your_password",
    "database.server.id": "184054",
    "database.server.name": "dbserver1",
    "database.whitelist": "your_database",
    "database.history.kafka.bootstrap.servers": "localhost:9092",
    "database.history.kafka.topic": "schema-changes.your_database"
  }
}
```

啟動 Connector

```bash
curl -X POST -H "Content-Type: application/json" --data @mysql-source-connector.json http://localhost:8083/connectors
```

使用 Kafka Connect Elasticsearch Sink Connector

這個 Connector 將 Kafka 主題的資料寫入 Elasticsearch

```bash
confluent-hub install confluentinc/kafka-connect-elasticsearch:latest
```

配置 Elasticsearch Sink Connector

創建一個配置文件 elasticsearch-sink-connector.json

```json
{
  "name": "elasticsearch-sink-connector",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "dbserver1.your_database.your_table",
    "key.ignore": "true",
    "connection.url": "http://localhost:9200",
    "type.name": "_doc",
    "name": "elasticsearch-sink-connector",
    "schema.ignore": "true"
  }
}
```

啟動 Connector

```bash
curl -X POST -H "Content-Type: application/json" --data @elasticsearch-sink-connector.json http://localhost:8083/connectors
```
