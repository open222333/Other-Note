# Elasticsearch(搜尋引擎) & Kibana(Elasticsearch用戶界面) 筆記

```
Elasticsearch是一個基於Lucene庫的搜尋引擎。
它提供了一個分散式、支援多租戶的全文搜尋引擎，具有HTTP Web介面和無模式JSON文件。
Elasticsearch是用Java開發的，並在Apache授權條款下作為開源軟體釋出。
官方客戶端在Java、.NET（C#）、PHP、Python、Apache Groovy、Ruby和許多其他語言中都是可用的。

ELK包含三個東西 Elasticsearch、Logstash、Kibana

LogStash 是 Indexer & Shipper
Elasticsearch 是 Search & Storage
Kibana 是 Web Interface

Logstash蒐集Log，透過Broker(透過Redis，也可以透過Kafka或是message queue工具，主要負責多手去蒐集以及暫存log)接著傳遞給予Logstash運作進行Index的動作
最後儲存在Elasticsearch中，可以供查詢以及其他應用
Kibana在進行web介面上的串接，前端視覺化

Kibana 是一個免費且開放的用戶界面，能夠讓對Elasticsearch 數據進行可視化，並讓在Elastic Stack 中進行導航。

索引(index)
index 在 ES 中是個邏輯空間的概念，用來儲存 document 的容器，而這些 document 內容都是相似的 (跟其他領域的 index 用法不太一樣)
shard 在 ES 中則是個物理空間的的概念，index 中的資料會分散放在不同的 shard 中
index 由以下幾個部份組成：
    - data：由 document + metadata 所組成
    - mapping：用來定義每個欄位名稱 & 類型
    - setting：定義資料是如何存放(例如：replication 數量, 使用的 shard 數量)
在 ES 7.0 的版本後，index 在 type 部份只能設定為 _doc (在以前的版本是可以設定不同的 type)

集群(cluster)
可以水平擴展儲存空間，支援 PB 等級的資料儲存

	可以根據 request & data 增加的需求進行 scale out；資料分散儲存，因此在 storage 的部份同樣也是可以 scale out 的

提供系統高可用性(HA)，當某些節點停止服務時，整個 cluster 的服務不會受影響

	Service HA：若有 node 停止服務，整個 cluster 還是可以提供服務
	Data HA：若有 node 掛掉，資料不會遺失

cluster name 可以透過設定檔修改，也可以在啟動指令中指定 -E cluster.name=[CLUSTER_NAME] 進行設定

節點(node)
Node 就是一個 Elasticsearch 的 Java process；
基本上一台機器上可以同時運行多個 Elasticsearch process，但 production 使用建議還是只要一個就好
每個 node 都有名稱，可透過設定檔配置，也可以在啟動時透過 -E node.name=[NODE_NAME] 進行設定
每個 node 啟動之後都會分配一個 UID，並儲存在 /usr/share/elasticsearch/data 目錄下

節點類型(Node Type)

Master Eligible Node
node.roles: [ master ]
node.roles: [ data, master, voting_only ] 僅投票
具有主節點角色的節點，這使得它有資格被選為主節點，控制集群。

Data Node
具有數據角色的節點。
數據節點保存數據並執行數據相關操作，例如 CRUD、搜索和聚合。
具有數據角色的節點可以填充任何專門的數據節點角色。

Ingest Node
具有攝取角色的節點。
攝取節點能夠將攝取管道應用到文檔，以便在索引之前轉換和豐富文檔。
在攝取負載很重的情況下，使用專用攝取節點並且不包括來自具有主角色或數據角色的節點的攝取角色是有意義的。

Machine Learning Node
node.roles: [ ml, remote_cluster_client]
專門用來跑 machine learning 的相關工作，可用來搭配異常自動偵測之用

Transform Node
node.roles: [ transform, remote_cluster_client ]
轉換節點運行轉換並處理轉換 API 請求。

Shard & Cluster 的故障轉移

Primary Shard (提昇系統儲存容量)
shard 是 Elasticsearch 分散式儲存的基礎，包含 primary shard & replica shard
每一個 shard 就是一個 Lucene instance
primary shard 功能是將一份被索引後的資料，分散到多個 data node 上存放，實現儲存方面的水平擴展
primary shard 的數量在建立 index 時就會指定，後續是無法修改的，若要修改就必須要進行 reindex

Replica Shard (提高資料可用性)
replica shard 用來提供資料高可用性，當 primary shard 遺失時，replica shard 就可以被 promote 成 primary shard 來保持資料完整性
replica shard 數量可以動態調整，讓每個 data node 上都有完整的資料
replica shard 可以一定程度的提高讀取(查詢)的效能
若不設定 replica shard，一旦有 data node 故障導致 primary shard 遺失，資料可能就無法恢復了
ES 7.0 開始，primary shard 預設為 1，replica shard 預設為 0
```

## 目錄

- [Elasticsearch(搜尋引擎) \& Kibana(Elasticsearch用戶界面) 筆記](#elasticsearch搜尋引擎--kibanaelasticsearch用戶界面-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學心得相關](#教學心得相關)
    - [執行緒相關](#執行緒相關)
    - [搜尋相關](#搜尋相關)
    - [集群相關](#集群相關)
    - [分詞器相關](#分詞器相關)
    - [\_score評分相關](#_score評分相關)
    - [mongo資料同步相關](#mongo資料同步相關)
      - [mongo資料同步工具](#mongo資料同步工具)
      - [Python - mongo-connector](#python---mongo-connector)
      - [Golang - monstache](#golang---monstache)
    - [例外狀況](#例外狀況)
      - [Error: disk usage exceeded flood-stage watermark, index has read-only-allow-delete blockedit](#error-disk-usage-exceeded-flood-stage-watermark-index-has-read-only-allow-delete-blockedit)
- [安裝方式](#安裝方式)
  - [安裝步驟 docker-compose cluster](#安裝步驟-docker-compose-cluster)
    - [官方](#官方)
    - [自行架設](#自行架設)
  - [安裝步驟 docker-compose](#安裝步驟-docker-compose)
  - [安裝步驟 Elasticsearch Docker](#安裝步驟-elasticsearch-docker)
  - [安裝步驟 CentOS7](#安裝步驟-centos7)
  - [安裝步驟 ik分詞器](#安裝步驟-ik分詞器)
    - [docker 安裝 ik分詞器](#docker-安裝-ik分詞器)
    - [自定義 ik 的啟用詞和停用詞](#自定義-ik-的啟用詞和停用詞)
- [設定檔](#設定檔)
  - [配置文檔 elasticsearch.yml (主要)](#配置文檔-elasticsearchyml-主要)
  - [配置文檔 Java jvm.options](#配置文檔-java-jvmoptions)
  - [配置文檔 override.conf](#配置文檔-overrideconf)
  - [生產環境 建議設定](#生產環境-建議設定)
- [集群 Cluster](#集群-cluster)
- [操作](#操作)
  - [指令 API](#指令-api)
    - [alias(別名)](#alias別名)
    - [新增 刪除 別名至索引](#新增-刪除-別名至索引)
    - [創建索引模板(index temple)](#創建索引模板index-temple)
    - [搜尋API(Search API)](#搜尋apisearch-api)
  - [Kibana(後台)](#kibana後台)
  - [Python 基本範例](#python-基本範例)
  - [創建模板](#創建模板)
  - [使用 Elasticsearch ILM 自動刪除索引的基本步驟](#使用-elasticsearch-ilm-自動刪除索引的基本步驟)
- [同步資料 Mongodb](#同步資料-mongodb)
  - [Python - mongo-connector](#python---mongo-connector-1)
    - [配置文檔 config.json](#配置文檔-configjson)
  - [Golang - monstache](#golang---monstache-1)
    - [安裝步驟 CentOS7](#安裝步驟-centos7-1)
- [例外狀況](#例外狀況-1)
  - [Error: disk usage exceeded flood-stage watermark, index has read-only-allow-delete blockedit](#error-disk-usage-exceeded-flood-stage-watermark-index-has-read-only-allow-delete-blockedit-1)
  - [Validation Failed: 1: this action would add \[5\] shards, but this cluster currently has \[5000\]/\[5000\] maximum normal shards open;](#validation-failed-1-this-action-would-add-5-shards-but-this-cluster-currently-has-50005000-maximum-normal-shards-open)
  - [kibana 發生 search\_phase\_execution\_exception 錯誤](#kibana-發生-search_phase_execution_exception-錯誤)

## 參考資料

[Elasticsearch Guide - 官方教學文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

[Kibana 介紹 - 官方](https://www.elastic.co/cn/kibana/)

[REST APIs - 官方 API文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/rest-apis.html)

[Important Elasticsearch configuration - 官方 設定檔說明](https://www.elastic.co/guide/en/elasticsearch/reference/current/important-settings.html)

[自定义分析器 - 2.x 官方分詞器](https://www.elastic.co/guide/cn/elasticsearch/guide/current/custom-analyzers.html)

[分析与分析器 - 2.x 官方分詞器](https://www.elastic.co/guide/cn/elasticsearch/guide/current/analysis-intro.html)

[Mapping parameters - 官方映射 映射參數](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-params.html)

[Dynamic templates - 官方映射 索引模板 動態映射](https://www.elastic.co/guide/en/elasticsearch/reference/7.13/dynamic-templates.html#dynamic-templates)

[Bootstrapping a cluster - 官方集群 引導集群編輯](https://www.elastic.co/guide/en/elasticsearch/reference/master/modules-discovery-bootstrap-cluster.html)

[Discovery and cluster formation setting - 官方集群 發現和集群形成設置](https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-discovery-settings.html)

[Index templates - 官方 索引模板(index template)相關](https://www.elastic.co/guide/en/elasticsearch/reference/current/index-templates.html#index-templates)

[Create or update index template API - 官方 創建或更新索引模板 API](https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-put-template.html)

[Field data types - 官方 資料類型(data type)相關](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html)

[Machine learning settings in Elasticsearchedit- 官方 機器學習節點說明](https://www.elastic.co/guide/en/elasticsearch/reference/current/ml-settings.html)

[Transforms settings in Elasticsearchedit- 官方 節點說明](https://www.elastic.co/guide/en/elasticsearch/reference/current/transform-settings.html)

[Elasticsearch WIKI](https://zh.wikipedia.org/zh-tw/Elasticsearch)

[elasticsearch-analysis-ik - ik分詞器 github專案](https://github.com/medcl/elasticsearch-analysis-ik)

[elasticsearch-analysis-ik - ik分詞器 所有版本 手動下載](https://github.com/medcl/elasticsearch-analysis-ik/releases)

[Elasticsearch Index Lifecycle Management（ILM）](https://www.elastic.co/guide/en/elasticsearch/reference/current/index-lifecycle-management.html#index-lifecycle-management)

```
Elasticsearch Index Lifecycle Management（ILM）是一個功能強大的工具，可以幫助自動管理索引的生命週期。
這包括了定義索引的不同階段（如熱、暖、冷、刪除等），以及在每個階段進行的操作，例如刪除、壓縮、移動到低成本存儲等。
以下是 Elasticsearch ILM 的一些主要特性和功能：
簡化管理：ILM 讓可以通過定義一個策略，而不是手動管理每個索引的生命週期。這樣可以節省大量時間和精力。
自動化操作：ILM 可以自動執行一系列操作，如刪除過期索引、將索引從熱節點移動到冷節點等。這樣可以提高系統的效率和運行成本。
智能調整：ILM 可以根據需求和策略自動調整索引的生命週期，例如根據索引的使用模式調整索引的熱度和冷度。
監控和警報：ILM 提供了監控和警報功能，可以讓及時了解索引生命週期管理的情況，並採取必要的措施。
適應多種用例：ILM 可以適應各種不同的用例，包括日誌管理、時間序列數據、存檔數據等等。
```

### 教學心得相關

[[Elasticsearch] 基本概念 & 搜尋入門](https://godleon.github.io/blog/Elasticsearch/Elasticsearch-getting-started/)

[全文搜索引擎 Elasticsearch 入门教程](http://www.ruanyifeng.com/blog/2017/08/elasticsearch.html)

[docker-compose安裝elasticsearch及kibana](https://www.cnblogs.com/chenyuanbo/p/16183304.html)

[How To Install ElasticSearch 7.x on CentOS 7](https://computingforgeeks.com/how-to-install-elasticsearch-on-centos/)

[Docker安装ElasticSearch和Kibana](https://blog.csdn.net/ThinkWon/article/details/122808762)

[docker-compose快速部署elasticsearch-8.x集群+kibana](https://blog.csdn.net/boling_cavalry/article/details/125232858)

[生產環境的 ElasticSearch 安裝指南](https://iter01.com/74792.html)

[理解ElasticSearch工作原理](https://www.jianshu.com/p/52b92f1a9c47)

[Using Elasticsearch to Offload Real-Time Analytics from MongoDB](https://rockset.com/blog/using-elasticsearch-to-offload-real-time-analytics-from-mongodb/)

[Elasticsearch 高手之路](https://xiaoxiami.gitbook.io/elasticsearch/)

[Youtube - 最新ElasticSearch教程](https://www.youtube.com/playlist?list=PLd1mymN837zK59aWFWS_gT9KdnsZZTI4u)

[ELASTICSEARCH MAPPING 原理及範例說明](https://hoohoo.top/blog/elasticsearch-mapping-tutorial/)

[Elastic Kibana 快速入門](https://linyencheng.github.io/2020/09/10/elastic-kibana-quick-start/)

[Elastic Kibana Quick Start: 第一次使用 Kibana 就上手 (11)](https://ithelp.ithome.com.tw/articles/10236315)

### 執行緒相關

[Elasticsearch 執行緒池和佇列問題，請先看這一篇](https://www.gushiciku.cn/pl/gc1J/zh-tw)

### 搜尋相關

[elasticsearch query DSL 整理總結（一）—— Query DSL 概要，MatchAllQuery，全文查詢簡述](https://www.itread01.com/qqifi.html)

[Elasticsearch Query DSL概述与查询、过滤上下文](https://bbs.huaweicloud.com/blogs/259264)

[ElasticSearch DSL python](https://blog.csdn.net/u012089823/article/details/82424679)

[Search API - 官方API文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-search.html)

[Script query - 腳本查詢](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-script-query.html)

[elasticsearch return total hits only - 只回傳hits數](https://stackoverflow.com/questions/43758813/elasticsearch-return-total-hits-only)

[總點擊數限制為 10000](https://www.drupal.org/project/elasticsearch_connector/issues/3250145)

### 集群相關

[Discovery and cluster formation settings](https://www.elastic.co/guide/en/elasticsearch/reference/7.3/modules-discovery-settings.html)

[[Elasticsearch] 分散式特性 & 分散式搜尋的機制](https://godleon.github.io/blog/Elasticsearch/Elasticsearch-distributed-mechanism/)

### 分詞器相關

[IK 分詞器配置文件和自定義詞庫](https://zq99299.github.io/note-book/elasticsearch-senior/ik/31-config.html#%E4%B8%BB%E8%A6%81%E9%85%8D%E7%BD%AE%E8%A7%A3%E8%AF%B4)

[掌握 analyze API，一举搞定 Elasticsearch 分词难题](https://elasticsearch.cn/article/771)

[ElasticSearch 分词器，了解一下](https://cloud.tencent.com/developer/article/1595785)

[ElasticSearch - 自定義 analysis](https://kucw.github.io/blog/2018/6/elasticsearch-analysis/)

[Elasticsearch —— docker部署+ik分词器](https://www.jianshu.com/p/d8b0c736070f)

[ElasticSearch-IK分詞器和整合使用](https://iter01.com/583193.html)

[IK分词器下载、使用和测试](https://www.freesion.com/article/5737557424/)

[ELK入门（十七）——Kibana之IK分词器安装、自定义和详细使用测试](https://blog.csdn.net/Netceor/article/details/114020196)

[ik中文分词器安装以及简单新增词典操作](https://blog.csdn.net/qq_42572322/article/details/107979724)

[為Elasticsarch添增ik分析器優化中文搜索 - 熱更新](https://tomme.me/elasticsearch-ik-analyzer-optimize/)

### _score評分相關

[相关度评分背后的理论](https://www.elastic.co/guide/cn/elasticsearch/guide/2.x/scoring-theory.html)

[ElasticSearch 的分数 (_score) 是怎么计算得出 (2.X & 5.X)](https://ruby-china.org/topics/31934)

[实战 | Elasticsearch自定义评分的N种方法](https://cloud.tencent.com/developer/article/1600163)

### mongo資料同步相關

#### mongo資料同步工具

`可使用工具`

  * [monstache - Golang](https://github.com/rwynn/monstache)

  * [mongo-connector - Python](https://github.com/yougov/mongo-connector)

  * [Mongoosastic - NodeJS](https://github.com/mongoosastic/mongoosastic)

#### Python - mongo-connector

[mongo-connector实现MongoDB与elasticsearch实时同步](https://blog.csdn.net/jerrism/article/details/110318159)

[Python 模組 mongo-connector(MongoDB and Elasticsearch)](../../01_程式語言/Python/Python%20Elasticsearch(搜尋引擎)/Python%20模組%20mongo-connector(MongoDB%20and%20Elasticsearch).md)

[mongo-connector实现MongoDB与elasticsearch实时同步深入详解](https://blog.csdn.net/laoyang360/article/details/51842822)

[29.mongo-connector實現MongoDB與elasticsearch實時同步(ES與非關係型資料庫同步)](https://www.796t.com/content/1549085781.html)

[https://www.796t.com/content/1549137781.html](mongo-connector實現MongoDB與elasticsearch實時同步深入詳解)

[利用mongo-connector將mongodb數據同步到elasticsearch的流程以及會遇到的坑](https://www.twblogs.net/a/5b8c06592b717718832fe1d2)

[Configuration Options](https://github.com/yougov/mongo-connector/wiki/Configuration-Options#configure-authentication)

#### Golang - monstache

[monstache-showcase/docker-compose.sc.yml](https://github.com/rwynn/monstache-showcase/blob/a25cddeedc9e8f1481aa7de19cd634158792b28c/docker-compose.sc.yml#L53)

[monstache Configuration](https://rwynn.github.io/monstache-site/config/)

[Monstache](https://rwynn.github.io/monstache-site/start/)

[從mongodb到elasticsearch的實時同步 - 包含分詞器](https://www.cxyzjd.com/article/zhangyonguu/80914496)

### 例外狀況

#### Error: disk usage exceeded flood-stage watermark, index has read-only-allow-delete blockedit

[官方解決方案](https://www.elastic.co/guide/en/elasticsearch/reference/master/disk-usage-exceeded.html)

[集群級分片分配和路由設置(Cluster-level shard allocation and routing settings)](https://www.elastic.co/guide/en/elasticsearch/reference/7.13/modules-cluster.html)

# 安裝方式

```bash
# 開啟防火牆
iptables -A INPUT -p tcp --dport 5601 -j ACCEPT
iptables -A INPUT -p tcp --dport 9200 -j ACCEPT
```

## 安裝步驟 docker-compose cluster

### 官方

```yml
# 官方
version: "3"
services:
  setup:
    image: docker.elastic.co/elasticsearch/elasticsearch:${STACK_VERSION}
    volumes:
      - certs:/usr/share/elasticsearch/config/certs
    user: "0"
    command: >
      bash -c '
        if [ x${ELASTIC_PASSWORD} == x ]; then
          echo "Set the ELASTIC_PASSWORD environment variable in the .env file";
          exit 1;
        elif [ x${KIBANA_PASSWORD} == x ]; then
          echo "Set the KIBANA_PASSWORD environment variable in the .env file";
          exit 1;
        fi;
        if [ ! -f config/certs/ca.zip ]; then
          echo "Creating CA";
          bin/elasticsearch-certutil ca --silent --pem -out config/certs/ca.zip;
          unzip config/certs/ca.zip -d config/certs;
        fi;
        if [ ! -f config/certs/certs.zip ]; then
          echo "Creating certs";
          echo -ne \
          "instances:\n"\
          "  - name: es01\n"\
          "    dns:\n"\
          "      - es01\n"\
          "      - localhost\n"\
          "    ip:\n"\
          "      - 127.0.0.1\n"\
          "  - name: es02\n"\
          "    dns:\n"\
          "      - es02\n"\
          "      - localhost\n"\
          "    ip:\n"\
          "      - 127.0.0.1\n"\
          "  - name: es03\n"\
          "    dns:\n"\
          "      - es03\n"\
          "      - localhost\n"\
          "    ip:\n"\
          "      - 127.0.0.1\n"\
          > config/certs/instances.yml;
          bin/elasticsearch-certutil cert --silent --pem -out config/certs/certs.zip --in config/certs/instances.yml --ca-cert config/certs/ca/ca.crt --ca-key config/certs/ca/ca.key;
          unzip config/certs/certs.zip -d config/certs;
        fi;
        echo "Setting file permissions"
        chown -R root:root config/certs;
        find . -type d -exec chmod 750 \{\} \;;
        find . -type f -exec chmod 640 \{\} \;;
        echo "Waiting for Elasticsearch availability";
        until curl -s --cacert config/certs/ca/ca.crt https://es01:9200 | grep -q "missing authentication credentials"; do sleep 30; done;
        echo "Setting kibana_system password";
        until curl -s -X POST --cacert config/certs/ca/ca.crt -u elastic:${ELASTIC_PASSWORD} -H "Content-Type: application/json" https://es01:9200/_security/user/kibana_system/_password -d "{\"password\":\"${KIBANA_PASSWORD}\"}" | grep -q "^{}"; do sleep 10; done;
        echo "All done!";
      '
    healthcheck:
      test: ["CMD-SHELL", "[ -f config/certs/es01/es01.crt ]"]
      interval: 1s
      timeout: 5s
      retries: 120

  es01:
    depends_on:
      setup:
        condition: service_healthy
    image: docker.elastic.co/elasticsearch/elasticsearch:${STACK_VERSION}
    volumes:
      - certs:/usr/share/elasticsearch/config/certs
      - esdata01:/usr/share/elasticsearch/data
    ports:
      - ${ES_PORT}:9200
    environment:
      - node.name=es01
      - cluster.name=${CLUSTER_NAME}
      - cluster.initial_master_nodes=es01,es02,es03
      - discovery.seed_hosts=es02,es03
      - ELASTIC_PASSWORD=${ELASTIC_PASSWORD}
      - bootstrap.memory_lock=true
      - xpack.security.enabled=true
      - xpack.security.http.ssl.enabled=true
      - xpack.security.http.ssl.key=certs/es01/es01.key
      - xpack.security.http.ssl.certificate=certs/es01/es01.crt
      - xpack.security.http.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.http.ssl.verification_mode=certificate
      - xpack.security.transport.ssl.enabled=true
      - xpack.security.transport.ssl.key=certs/es01/es01.key
      - xpack.security.transport.ssl.certificate=certs/es01/es01.crt
      - xpack.security.transport.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.transport.ssl.verification_mode=certificate
      - xpack.license.self_generated.type=${LICENSE}
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1
    healthcheck:
      test:
        [
          "CMD-SHELL",
          "curl -s --cacert config/certs/ca/ca.crt https://localhost:9200 | grep -q 'missing authentication credentials'",
        ]
      interval: 10s
      timeout: 10s
      retries: 120

  es02:
    depends_on:
      - es01
    image: docker.elastic.co/elasticsearch/elasticsearch:${STACK_VERSION}
    volumes:
      - certs:/usr/share/elasticsearch/config/certs
      - esdata02:/usr/share/elasticsearch/data
    environment:
      - node.name=es02
      - cluster.name=${CLUSTER_NAME}
      - cluster.initial_master_nodes=es01,es02,es03
      - discovery.seed_hosts=es01,es03
      - bootstrap.memory_lock=true
      - xpack.security.enabled=true
      - xpack.security.http.ssl.enabled=true
      - xpack.security.http.ssl.key=certs/es02/es02.key
      - xpack.security.http.ssl.certificate=certs/es02/es02.crt
      - xpack.security.http.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.http.ssl.verification_mode=certificate
      - xpack.security.transport.ssl.enabled=true
      - xpack.security.transport.ssl.key=certs/es02/es02.key
      - xpack.security.transport.ssl.certificate=certs/es02/es02.crt
      - xpack.security.transport.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.transport.ssl.verification_mode=certificate
      - xpack.license.self_generated.type=${LICENSE}
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1
    healthcheck:
      test:
        [
          "CMD-SHELL",
          "curl -s --cacert config/certs/ca/ca.crt https://localhost:9200 | grep -q 'missing authentication credentials'",
        ]
      interval: 10s
      timeout: 10s
      retries: 120

  es03:
    depends_on:
      - es02
    image: docker.elastic.co/elasticsearch/elasticsearch:${STACK_VERSION}
    volumes:
      - certs:/usr/share/elasticsearch/config/certs
      - esdata03:/usr/share/elasticsearch/data
    environment:
      - node.name=es03
      - cluster.name=${CLUSTER_NAME}
      - cluster.initial_master_nodes=es01,es02,es03
      - discovery.seed_hosts=es01,es02
      - bootstrap.memory_lock=true
      - xpack.security.enabled=true
      - xpack.security.http.ssl.enabled=true
      - xpack.security.http.ssl.key=certs/es03/es03.key
      - xpack.security.http.ssl.certificate=certs/es03/es03.crt
      - xpack.security.http.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.http.ssl.verification_mode=certificate
      - xpack.security.transport.ssl.enabled=true
      - xpack.security.transport.ssl.key=certs/es03/es03.key
      - xpack.security.transport.ssl.certificate=certs/es03/es03.crt
      - xpack.security.transport.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.transport.ssl.verification_mode=certificate
      - xpack.license.self_generated.type=${LICENSE}
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1
    healthcheck:
      test:
        [
          "CMD-SHELL",
          "curl -s --cacert config/certs/ca/ca.crt https://localhost:9200 | grep -q 'missing authentication credentials'",
        ]
      interval: 10s
      timeout: 10s
      retries: 120

  kibana:
    depends_on:
      es01:
        condition: service_healthy
      es02:
        condition: service_healthy
      es03:
        condition: service_healthy
    image: docker.elastic.co/kibana/kibana:${STACK_VERSION}
    volumes:
      - certs:/usr/share/kibana/config/certs
      - kibanadata:/usr/share/kibana/data
    ports:
      - ${KIBANA_PORT}:5601
    environment:
      - SERVERNAME=kibana
      - ELASTICSEARCH_HOSTS=https://es01:9200
      - ELASTICSEARCH_USERNAME=kibana_system
      - ELASTICSEARCH_PASSWORD=${KIBANA_PASSWORD}
      - ELASTICSEARCH_SSL_CERTIFICATEAUTHORITIES=config/certs/ca/ca.crt
    mem_limit: ${MEM_LIMIT}
    healthcheck:
      test:
        [
          "CMD-SHELL",
          "curl -s -I http://localhost:5601 | grep -q 'HTTP/1.1 302 Found'",
        ]
      interval: 10s
      timeout: 10s
      retries: 120

volumes:
  certs:
    driver: local
  esdata01:
    driver: local
  esdata02:
    driver: local
  esdata03:
    driver: local
  kibanadata:
    driver: local
```

`.env環境變數`

```.env
# 'elastic' 用戶的密碼（至少 6 個字符）
ELASTIC_PASSWORD=

# 'kibana_system' 用戶的密碼（至少 6 個字符）
KIBANA_PASSWORD=

# Elastic 產品版本
STACK_VERSION=8.3.2

# 設置集群名稱
CLUSTER_NAME=docker-cluster

# 設置為“基本”或“試用”以自動開始 30 天試用
LICENSE=basic
#LICENSE=trial

# 向主機公開 Elasticsearch HTTP API 的端口
ES_PORT=9200
#ES_PORT=127.0.0.1:9200

# 將 Kibana 暴露給主機的端口
KIBANA_PORT=5601
#KIBANA_PORT=80

# 根據可用主機內存增加或減少（以字節為單位）
MEM_LIMIT=1073741824

# 項目命名空間（如果未設置，則默認為當前文件夾名稱）
#COMPOSE_PROJECT_NAME=myproject
```

### 自行架設

```yml
version: '3'
services:
  es01:
    image: elasticsearch:${STACK_VERSION}
    container_name: es01
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es01/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      - ./es01/data:/usr/share/elasticsearch/data # 數據文件掛載
      - ./es01/logs:/usr/share/elasticsearch/logs
    ports:
      - 9200:9200
  es02:
    image: elasticsearch:${STACK_VERSION}
    container_name: es02
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es02/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      - ./es02/data:/usr/share/elasticsearch/data:rw # 數據文件掛載
      - ./es02/logs:/usr/share/elasticsearch/logs:rw
    depends_on:
      - es01
  es03:
    image: elasticsearch:${STACK_VERSION}
    container_name: es03
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es03/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      - ./es03/data:/usr/share/elasticsearch/data:rw # 數據文件掛載
      - ./es03/logs:/usr/share/elasticsearch/logs:rw
    depends_on:
      - es01
  kibana:
    image: kibana:7.13.3
    container_name: kibana
    depends_on:
      - es01 # kibana在elasticsearch啟動之後再啟動
      - es02
      - es03
    environment:
      ELASTICSEARCH_HOSTS: http://es01:9200 # 設置訪問elasticsearch的地址
      I18N_LOCALE: zh-CN
    ports:
      - 5601:5601
```

```yml
# 節點名稱
node.name: es01

# 設置集群名稱
cluster.name: test-cluster
# 發現種子節點
discovery.seed_hosts:
  - es02
  - es03
# 集群初始化
cluster.initial_master_nodes:
  - es01
  - es02
  - es03
bootstrap.memory_lock: true
network.host: 0.0.0.0
http.cors.enabled: true
http.cors.allow-origin: "*"
```

```yml
# 節點名稱
node.name: es02

# 設置集群名稱
cluster.name: test-cluster
# 發現種子節點
discovery.seed_hosts:
  - es01
  - es03
# 集群初始化
cluster.initial_master_nodes:
  - es01
  - es02
  - es03
bootstrap.memory_lock: true
network.host: 0.0.0.0
```

```yml
# 節點名稱
node.name: es03

# 設置集群名稱
cluster.name: test-cluster
# 發現種子節點
discovery.seed_hosts:
  - es01
  - es02
# 集群初始化
cluster.initial_master_nodes:
  - es01
  - es02
  - es03
bootstrap.memory_lock: true
network.host: 0.0.0.0
```

```env
# Version of Elastic products 版本號
STACK_VERSION=7.13.3

# Set the cluster name 集群名
CLUSTER_NAME=test-cluster

# Increase or decrease based on the available host memory (in bytes)
MEM_LIMIT=1073741824
```

## 安裝步驟 docker-compose

```yml
# 原始範本
version: '3'
services:
  elasticsearch:
    image: elasticsearch:7.13.3
    container_name: elasticsearch
    privileged: true
    environment:
      - "cluster.name=elasticsearch" # 設置集群名稱為elasticsearch
      - "discovery.type=single-node" # 以單一節點模式啟動
      - "ES_JAVA_OPTS=-Xms512m -Xmx2g" # 設置使用jvm內存大小
      - bootstrap.memory_lock=true # 關閉 swap
    volumes:
	  - ./es/plugins:/usr/share/elasticsearch/plugins # 插件文件掛載
	  # chmod -R 777 ./es/data  若出現權限問題
      - ./es/data:/usr/share/elasticsearch/data:rw # 數據文件掛載
      # chmod -R 777 ./es/logs  若出現權限問題
      - ./es/logs:/usr/share/elasticsearch/logs:rw
	  - ./es/config:/usr/share/elasticsearch/config_default # 複製設定文檔到這資料夾
    ports:
      - 9200:9200
      - 9300:9300
	# 限制物理資源
    deploy:
      resources:
        limits:
          cpus: "2"
          memory: 1000M
        reservations:
          memory: 200M
  kibana:
    image: kibana:7.13.3
    container_name: kibana
    depends_on:
      - elasticsearch # kibana在elasticsearch啟動之後再啟動
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200 # 設置訪問elasticsearch的地址
      I18N_LOCALE: zh-CN
      # English - en (default)
      # Chinese - zh-CN
      # Japanese - ja-JP
      # French - fr-FR
    ports:
      - 5601:5601
```

```yml
# 20220815
version: '3'
services:
  elasticsearch:
    image: elasticsearch:7.13.3
    container_name: elasticsearch
    privileged: true
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx1096m" # 設置使用jvm內存大小
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml:ro
      - ./es/data:/usr/share/elasticsearch/data:rw # 數據文件掛載
      - ./es/logs:/usr/share/elasticsearch/logs:rw
    ports:
      - 9200:9200
      - 9300:9300
  kibana:
    image: kibana:7.13.3
    container_name: kibana
    depends_on:
      - elasticsearch # kibana在elasticsearch啟動之後再啟動
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200 # 設置訪問elasticsearch的地址
      I18N_LOCALE: zh-CN
    ports:
      - 5601:5601
```

```yml
# elasticsearch.yml
# 設置集群名稱
cluster.name: "es_test_cluster"
# 以單一節點模式啟動
discovery.type: "single-node"
bootstrap.memory_lock: true
network.host: 0.0.0.0
```

## 安裝步驟 Elasticsearch Docker

```bash
# docker安裝es
docker pull elasticsearch

# 啟動es
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -d elasticsearch:7.2.0

# 測試
curl http://localhost:9200

	# {
	# "name" : "530dd7820315",
	# "cluster_name" : "docker-cluster",
	# "cluster_uuid" : "7O0fjpBJTkmn_axwmZX0RQ",
	# "version" : {
	# 	"number" : "7.2.0",
	# 	"build_flavor" : "default",
	# 	"build_type" : "docker",
	# 	"build_hash" : "508c38a",
	# 	"build_date" : "2019-06-20T15:54:18.811730Z",
	# 	"build_snapshot" : false,
	# 	"lucene_version" : "8.0.0",
	# 	"minimum_wire_compatibility_version" : "6.8.0",
	# 	"minimum_index_compatibility_version" : "6.0.0-beta1"
	# },
	# "tagline" : "You Know, for Search"
	# }

# 修改配置，解決跨域訪問問題
# 首先進入到容器中，然後進入到指定目錄修改elasticsearch.yml文件。

docker exec -it elasticsearch /bin/bash

cd /usr/share/elasticsearch/config/

vi elasticsearch.yml
	加上
	http.cors.enabled: true
	http.cors.allow-origin: "*"

# 重啟
docker restart elasticsearch

# 安裝ik分詞器 elasticsearch的版本和ik分詞器的版本需要保持一致
# Elasticsearch中預設的標準分詞器(analyze)對中文分詞不是很友好 因此需下載ik分詞器
# https://github.com/medcl/elasticsearch-analysis-ik/releases
cd /usr/share/elasticsearch/plugins/
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.2.0/elasticsearch-analysis-ik-7.2.0.zip
exit
docker restart elasticsearch
```

## 安裝步驟 CentOS7

```bash
# 安裝 java
yum -y install java-1.8.0-openjdk  java-1.8.0-openjdk-devel

# Set Java home
cat <<EOF | sudo tee /etc/profile.d/java8.sh
export JAVA_HOME=/usr/lib/jvm/jre-openjdk
export PATH=\$PATH:\$JAVA_HOME/bin
export CLASSPATH=.:\$JAVA_HOME/jre/lib:\$JAVA_HOME/lib:\$JAVA_HOME/lib/tools.jar
EOF

# 創建文件以更新環境
source /etc/profile.d/java8.sh

# 添加 ElasticSearch Yum 存儲庫
cat <<EOF | sudo tee /etc/yum.repos.d/elasticsearch.repo
[elasticsearch-7.x]
name=Elasticsearch repository for 7.x packages
baseurl=https://artifacts.elastic.co/packages/oss-7.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
EOF

	# 如果要安裝 Elasticsearch 6，請將所有出現的 7 替換為 6。添加存儲庫後，清除並更新 YUM 包索引。
	yum clean all
	yum makecache

# 在 CentOS 7 上安裝 ElasticSearch 7
yum -y install elasticsearch-oss

# 在 CentOS 7 上確認 ElasticSearch 7 安裝
rpm -qi elasticsearch-oss

	Name        : elasticsearch-oss
	Epoch       : 0
	Version     : 7.4.0
	Release     : 1
	Architecture: x86_64
	Install Date: Thu 17 Oct 2019 05:10:43 AM UTC
	Group       : Application/Internet
	Size        : 395896718
	License     : ASL 2.0
	Signature   : RSA/SHA512, Fri 27 Sep 2019 10:40:01 AM UTC, Key ID d27d666cd88e42b4
	Source RPM  : elasticsearch-oss-7.4.0-1-src.rpm
	Build Date  : Fri 27 Sep 2019 08:49:06 AM UTC
	Build Host  : packer-virtualbox-iso-1559162487
	Relocations : /usr
	Packager    : Elasticsearch
	Vendor      : Elasticsearch
	URL         : https://www.elastic.co/
	Summary     : Distributed RESTful search engine built for the cloud
	Description :
	Reference documentation can be found at
	https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html
	and the 'Elasticsearch: The Definitive Guide' book can be found at
	https://www.elastic.co/guide/en/elasticsearch/guide/current/index.html

# 在開機時啟動 並啟用 elasticsearch 服務
systemctl enable --now elasticsearch

# 測試
curl http://127.0.0.1:9200

# 在 CentOS 7 上安裝 Kibana 7
yum install kibana-oss logstash

# 配置 Kibana
vi /etc/kibana/kibana.yml
	server.host: "0.0.0.0"
	server.name: "kibana.example.com"
	elasticsearch.url: "http://localhost:9200"

# 在開機時啟動 並啟用 Kibana 服務
systemctl enable --now kibana

# 開啟防火牆
vim /etc/sysconfig/iptables
iptables -A INPUT -p tcp --dport 5601 -j ACCEPT
iptables -A INPUT -p tcp --dport 9200 -j ACCEPT

firewall-cmd --add-port=5601/tcp --permanent
firewall-cmd --add-port=9200/tcp --permanent
firewall-cmd --reload
```

## 安裝步驟 ik分詞器

```bash
# 安裝ik分詞器 elasticsearch的版本和ik分詞器的版本需要保持一致
# Elasticsearch中預設的標準分詞器(analyze)對中文分詞不是很友好 因此需下載ik分詞器
# https://github.com/medcl/elasticsearch-analysis-ik/releases
cd /usr/share/elasticsearch/plugins/
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.2.0/elasticsearch-analysis-ik-7.2.0.zip


# 創建mapping
curl -XPOST http://localhost:9200/index/_mapping?pretty -H 'Content-Type:application/json' -d'
{
	"properties": {
		"content": {
			"type": "text",
			"analyzer": "ik_max_word",
			"search_analyzer": "ik_smart"
		}
	}
}'
```

### docker 安裝 ik分詞器

```env
# Version of Elastic products 版本號
STACK_VERSION=

# Set the cluster name 集群名
CLUSTER_NAME=test-cluster

# Increase or decrease based on the available host memory (in bytes)
MEM_LIMIT=1073741824
```

```yml
version: '3'
services:
  es01:
    image: elasticsearch:${STACK_VERSION}
    container_name: es01
    environment:
      - node.name=es01
      - discovery.seed_hosts=es02
      - cluster.initial_master_nodes=es01,es02
      - cluster.name=docker-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - esdata01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - esnet
  es02:
    image: elasticsearch:${STACK_VERSION}
    container_name: es02
    environment:
      - node.name=es02
      - discovery.seed_hosts=es01
      - cluster.initial_master_nodes=es01,es02
      - cluster.name=docker-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - esdata02:/usr/share/elasticsearch/data
    networks:
      - esnet

volumes:
  esdata01:
    driver: local
  esdata02:
    driver: local

networks:
  esnet:
```

```bash
# 集群
docker-compose exec es01 elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.2.0/elasticsearch-analysis-ik-7.2.0.zip
docker-compose exec es02 elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.2.0/elasticsearch-analysis-ik-7.2.0.zip
# 重啟es容器
docker-compose restart es01
docker-compose restart es02
```

### 自定義 ik 的啟用詞和停用詞

`修改 IKAnalyzer.cfg.xml 文件`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <comment>IK Analyzer 扩展配置</comment>
    <!-- 配置擴展字典 檔案在 analysis-ik/config-->
    <entry key="ext_dict">custom-ext.dic</entry>
    <!-- 配置擴展停止詞字典 檔案在 analysis-ik/config-->
    <entry key="ext_stopwords">custom-stop.dic</entry>
    <!-- 配置遠程擴展字典 -->
    <!-- <entry key="remote_ext_dict">words_location</entry> -->
	<!--
	1、custom-ext.dic配置到nginx中。
	2、(不需)http請求需要返回兩個頭部(header)，一個是Last-Modified，一個是ETag，這兩者都是字符串類型，只要有一個發生變化，該插件就會去抓取新的分詞進而更新詞庫。
	3、http請求返回的內容格式是一行一個分詞，換行符用\n即可。
	4、在nginx的root(預設在/usr/share/nginx/html)目錄下放置一個custom-ext.dic文件
	-->
	<entry key="remote_ext_dict">http://localhost:8686/custom-ext.dic</entry>
    <!-- 配置遠程擴展停止詞字典 -->
    <!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```

# 設定檔

## 配置文檔 elasticsearch.yml (主要)

```yml
### 集群(Cluster) ###
# 配置集群名稱，由多個es實例組成的集群，有一個共同的名稱。
cluster.name: my-application

# 新節點用於加入集群的主節點列表
discovery.seed_hosts:
	- 192.168.1.10:9300
	- 192.168.1.11
	- seeds.mydomain.com

# 初始化設置 使用node.name或限定域名 引導集群的初始列表
cluster.initial_master_nodes:
	- master-node-a
	- master-node-b
	- master-node-c

# 集群端口設置
transport.tcp.port: 9300

# 防止同一個shard的主副本存在同一個物理機上。
cluster.routing.allocation.same_shard.host: true

# 初始化數據恢復時，並發恢復線程的個數，默認是4個。
cluster.routing.allocation.node_initial_primaries_recoveries: 4

# 添加刪除節點或者負載均衡時並發恢復線程的個數。默認是4個。
cluster.routing.allocation.node_concurrent_recoveries: 4

### 節點 ###
# 節點名稱配置，一個es實例其實是一個es進程，在集群中被稱為節點。如果一個服務器上配置集群，各節點的名稱不能重複。
node.name: node-1

# 為節點添加自定義屬性，
node.attr.rack: r1

# 該節點是否有資格成為主節點，默認為true。
node.master: true

# 設置節點是否存儲數據。
node.data: true

# 設置節點角色 默認
# 集群都需要以下節點角色: master, data_content data_hot OR data
# 跨集群搜索和跨集群複製需要 remote_cluster_client
# Stack Monitoring and ingest pipelines 堆棧監控和攝取管道需要 ingest
# Fleet、Elastic Security 應用程序和轉換需要 transform，還需要 remote_cluster_client 角色來使用具有這些功能的跨集群搜索。
# Machine learning features, such as anomaly detection機器學習功能（例如異常檢測）需要 ml
node.roles: [ master ]

node.roles: [ data, master, voting_only ]

# 設置默認主分片的個數，默認為5片，需要說明的是，主分片一經分配則無法更改。
index.number_of_shards: 5

# 設置默認複製分片的個數，默認一個主分片對應一個複制分片，需要說明的是，複製分片可以手動調整。
index.number_of_replicas: 1

# 設置數據恢復時限制的帶寬，默認0及不限制。
indices.recovery.max_size_per_ser: 0

# 設置這個參數來限制從其它分片恢復數據時最大同時打開並發流的個數，默認為5。
indices.recovery.concurrent_streams: 5

# 設置數據恢復時限制的帶寬，默認0及不限制。
indices.recovery.max_size_per_ser: 0

# 設置這個參數來限制從其它分片恢復數據時最大同時打開並發流的個數，默認為5。
indices.recovery.concurrent_streams: 5

### Paths ###
# 存儲數據路徑設置，多個路徑以英文狀態的逗號分隔，默認根目錄下的conf目錄。
path.data: /path/to/data
# path.data: /path/to/data1,/path/to/data1 # Deprecated in 7.13.0.

# 設置臨時文件存儲路徑，默認是es目錄下的work目錄。
path.work: /path/to/work

# 日誌文件路徑，默認為根目錄下的logs目錄。
path.logs: /path/to/logs

# 設置日誌文件的存儲路徑，默認是es目錄下的logs目錄。
path.logs: /path/to/logs

# 設置插件的存放路徑，默認是es目錄下的plugins目錄。
path.plugins: /path/to/plugins

### Network ###
# 為es實例綁定特定的IP地址。
network.host: 192.168.0.1

# 上面的設置可以拆分為兩個參數。
network.bind_host: 192.168.0.1  # 設置綁定的ip地址，ipv4或ipv6都可以
network.publish_host: 192.168.0.1  # 設置其它節點和該節點交互的ip地址，如果不設置它會自動判斷，值必須是個真實的ip地址

# 為es實例設置特定的端口，默認為9200端口。
http.port: 9200

### Discovery ###
# 以單一節點模式啟動
discovery.type: single-node

# 設置是否打開多播發現節點，默認是true。
discovery.zen.ping.multicast.enabled: true

# 配置es單播發現列表，在es啟動時，通過這個列表發現別的es實例，從而加入集群。
discovery.zen.ping.unicast.hosts: ["host1", "host2"]
discovery.zen.ping.unicast.hosts: ["10.0.0.1", "10.0.0.3:9300", "10.0.0.6[9300-9400]"]

# 告訴集群有多少個節點有資格成為主節點，一般的規則是集群節點數除以2（向下取整）再加一。
# 比如3個節點集群要設置為2。
discovery.zen.minimum_master_nodes: 2

# 設置集群中自動發現其它節點時ping連接超時時間，默認為3秒，對於比較差的網絡環境可以高點的值來防止自動發現時出錯。
discovery.zen.ping.timeout: 3s

### Memory ###
# 啟動時鎖定內存，默認為true，因為當jvm開始swapping時es的效率會降低，所以要保證它不swap，可以把ES_MIN_MEM和ES_MAX_MEM兩個環境變量設置成同一個值，並且保證機器有足夠的內存分配給es。同時也要允許elasticsearch的進程可以鎖住內存，linux下可以通過ulimit -l unlimited命令
bootstrap.memory_lock: true

# 禁止swapping交換。 舊版
# bootstrap.mlockall: true

### Gateway ###
# 設置是否壓縮tcp傳輸時的數據。默認是false不壓縮。
transport.tcp.compress: true

# 設置內容的最大容量，默認是100mb。
http.max_content_length: 100mb

# 是否使用http協議對外提供服務。默認為true。
http.enabled: false

# 設置gateway的類型，默認為本地文件系統，也可以設置分佈式文件系統、Hadoop的HDFS或者AWS的都可以。
gateway.type: local

# 在完全重新啟動集群之後阻塞初始恢復，直到啟動N個節點為止，詳情參見Recovery
gateway.recover_after_nodes: 3

# 設置初始化數據恢復進程的超時時間。默認是5分鐘。
gateway.recover_after_time: 5m

# 設置該集群中節點的數量，默認為2個，一旦這N個節點啟動，就會立即進行數據恢復。
gateway.expected_nodes: 2

### Various ###
# 刪除索引時需要顯式名稱。 避免刪除全部索引操作
action.destructive_requires_name: true
```

## 配置文檔 Java jvm.options

```
不要修改根 jvm.options 文件。
請改用 jvm.options.d/ 中的文件。
/etc/elasticsearch/jvm.options
```

自定義 JVM 選項文件(光芳建議在資料夾底下 建立options文檔 覆寫):

tar.gz 或 .zip：
`config/jvm.options.d/.`

Debian 或 RPM：
`/etc/elasticsearch/jvm.options.d/.`

Docker：
`/usr/share/elasticsearch/config/jvm.options.d/.`

```
設置jvm堆的大小，最大值和最小值，應該是一致的，並且應該根據你的物理內存決定。
-Xms1g     # 设置最小堆为1g
-Xmx1g      # 设置最大堆为1g
```

## 配置文檔 override.conf

RPM: `/etc/sysconfig/elasticsearch`

Debian: `/etc/default/elasticsearch`


The systemd service file:`/usr/lib/systemd/system/elasticsearch.service`

`/etc/systemd/system/elasticsearch.service.d/override.conf`

```conf
[Service]
LimitMEMLOCK=infinity
```

```bash
# 完成後，運行以下命令重新加載單元
systemctl daemon-reload
```

## 生產環境 建議設定

```bash
### MMapFs 配置 ###
# Elasticsearch 默認使用 mmapfs 目錄來存儲其索引。
# 默認操作系統對 mmap 計數的限制可能太低，這可能會導致內存不足異常。 需高於 262144
sysctl -w vm.max_map_count=262144
# 配置 swappiness 讓 kenerl 在正常情況下不會使用 swap 交換記憶體,緊急情況下仍然會使用
sysctl vm.swappiness=1

# or

# 重啟後配置仍然保留
vim /etc/sysctl.conf
	# MMapFs 配置
	vm.max_map_count = 262144
	# 配置 swappiness
	vm.swappiness = 1

### 修改檔案控制程式碼數 ###
vim /etc/security/limits.conf

	# 部署使用者是elasticsearch
	elasticsearch  -  nofile  65536

	elasticsearch - nofile 65535
	elasticsearch - memlock unlimited
	# 不限制任何使用者
	*  -  nofile  65536
	# hard資源限制意味著是物理限制；
	# soft資源限制是由使用者進行管理的，soft的最大值由hard來限制。
	* soft nofile 1024000
	* hard nofile 1024000
	### 關閉swap ###
	# 使用 linux 的mlockall進行進行記憶體鎖定,防止使用 swap
	* soft memlock unlimited
	* hard memlock unlimited
	# 修改 user 能建立的執行緒限制
	* soft nproc 1024000
	* hard nproc 1024000

### 關閉swap ###
# 如果 Es 用到了 swap 作為記憶體,效能將會變得極差,所以建議關閉
# 臨時關閉
swapoff -a

# 永久關閉 檔案中所有包含swap的行全部註釋掉
vim /etc/fstab

# 開啟 ES 的配置 bootstrap.memory_lock
vim config/elasticsearch.yml # 具體位置須根據安裝方式確認
	bootstrap.memory_lock: true

### ES自身配置 ###
vim config/jvm.properties # 具體位置須根據安裝方式確認
	# 通常情況下配置為機器記憶體的一半左右,另外一半留給 ES 的堆外記憶體.master
	-Xms16g
	-Xmx16g

# 限制住elasticsearch佔用的內存情況，可選少用swap
vim /etc/systemd/system.conf
	DefaultLimitNOFILE=65536
	DefaultLimitNPROC=32000
	DefaultLimitMEMLOCK=infinity
```

`elasticsearch.yml`設定建議

```yml
# 引數
# 將 master 和 data(worder) 節點分開,master只負責排程,不儲存資料
cluster.name: example-es-cluster
node.name: node-worker-74
# 是否是 master
node.master: false
# 是否儲存資料
node.data: true
# 資料儲存位置
path.data: /data/elasticsearch/data
path.logs: /data/elasticsearch/logs

bootstrap.memory_lock: true
bootstrap.system_call_filter: false

network.host: 192.168.8.74
# http 埠
http.port: 9200
# tcp 埠
transport.tcp.port: 9300
# 叢集節點
discovery.zen.ping.unicast.hosts: ["192.168.8.75", "192.168.8.76", "192.168.8.72", "192.168.8.73", "192.168.8.74"]

discovery.zen.ping_timeout: 120s
discovery.zen.minimum_master_nodes: 1
discovery.zen.fd.ping_interval: 5s
discovery.zen.fd.ping_retries: 5

gateway.recover_after_nodes: 1


http.cors.enabled: true
http.cors.allow-origin: "*"

action.auto_create_index: false
action.destructive_requires_name: true
search.default_search_timeout: 150s
indices.fielddata.cache.size: 20%
indices.breaker.fielddata.limit: 40%
indices.breaker.request.limit: 30%
indices.breaker.total.limit: 60%
indices.recovery.max_bytes_per_sec: 200mb
indices.memory.index_buffer_size: 20%
http.max_content_length: 1024mb
thread_pool.bulk.queue_size: 3000
thread_pool.index.queue_size: 2000
thread_pool.search.queue_size: 1000
thread_pool.get.queue_size: 1000
```

# 集群 Cluster

- 設置一個新的 Elasticsearch 實例。

- 使用 elasticsearch.yml 中的 cluster.name 設置指定集群的名稱。

- 啟動彈性搜索。節點自動發現並加入指定的集群。要將節點添加到在多台機器上運行的集群中，還必須設置 discovery.seed_hosts 以便新節點可以發現其集群的其餘部分

# 操作

## 指令 API

```bash
# 查看節點訊息
curl -X GET http://localhost:9200/_cat/nodes?v

# 查看節點
curl -X GET http://localhost:9200/_nodes/stats?pretty

# 查看伺服器參數
curl -X GET http://localhost:9200/_cat/thread_pool/?v&h=id,name,active,rejected,completed,size,type&pretty&s=type

# 測試
curl -X GET http://localhost:9200

# 創建索引
curl -X PUT http://localhost:9200/index

# 將某個索引的 refresh_interval 設置為 1 分鐘
# ms: 毫秒
# s: 秒
# m: 分钟
curl -X PUT http://localhost:9200/{index}/_settings -d '
{
    "index" : {
        "refresh_interval" : "1m"
    }
}'

# 獲取集群設置 JSON
curl -X GET http://localhost:9200/_cluster/settings?pretty&include_defaults

curl -X GET http://localhost:9200/_nodes/stats?metric=adaptive_selection,breaker,discovery,fs,http,indices,jvm,os,process,thread_pool,transport&filter_path=nodes.*.adaptive_selection*,nodes.*.breaker*,nodes.*.fs*,nodes.*.os*,nodes.*.jvm*,nodes.*.process*,nodes.*.thread_pool*,nodes.*.discovery.cluster_state_queue,nodes.*.discovery.published_cluster_states,nodes.process.*.*,nodes.*.indices*,nodes.*.http.current_open,nodes.*.http.total_opened,_nodes,cluster_name,nodes.*.attributes,nodes.*.timestamp,nodes.*.transport*,nodes.*.transport_address,nodes.*.transport_address,nodes.*.host,nodes.*.ip,,nodes.*.roles,nodes.*.name&pretty

# 創建索引 accounts 使用分詞器
# analyzer是字段文本的分詞器，search_analyzer是搜索詞的分詞器。 ik_max_word分詞器是插件ik提供的，可以對文本進行最大數量的分詞。
curl -X PUT 'localhost:9200/accounts' -d '
{
  "mappings": {
    "person": {
      "properties": {
        "user": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "title": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "desc": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        }
      }
    }
  }
}'

# 建立 mapping
curl -X POST http://localhost:9200/index/_mapping -H 'Content-Type:application/json' -d'
{
	"properties": {
		"content": {
			"type": "text",
			"analyzer": "ik_max_word",
			"search_analyzer": "ik_smart"
		}
	}
}'

# 新增doc
curl -X POST http://localhost:9200/index/_create/1 -H 'Content-Type:application/json' -d '{"content":"內容"}'

# 刪除多個索引
curl -X DELETE 'http://localhost:9200/index_one,index_two'
curl -X DELETE 'http://localhost:9200/index_*'

# 刪除 全部索引
# action.destructive_requires_name: true 避免刪除全部索引 刪除需提供名稱
curl -X DELETE 'http://localhost:9200/_all'
curl -X DELETE 'http://localhost:9200/*'

# 查看plugin 訊息
elasticsearch-plugin -h

# 返回集群的健康狀態
curl -X GET "localhost:9200/_cluster/health?wait_for_status=yellow&timeout=50s&pretty"

# 修改 分片數量上限
curl -X PUT localhost:9200/_cluster/settings -H "Content-Type: application/json" -d '{ "persistent": { "cluster.max_shards_per_node": "5000" } }'
```

### alias(別名)

```bash
# 取得 index 的別名資訊
curl -X GET http://{{elastic_host}}/{{index}}/_alias/{{alias}}

# 檢測這個別名指向哪一個索引
curl -X GET http://{{elastic_host}}/*/_alias/{{alias}}

# 哪些別名指向這個索引
curl -X GET http://{{elastic_host}}/{{index}}/_alias/*
```

### 新增 刪除 別名至索引

```
添加別名到新索引的同時必須從舊的索引中刪除它 在零停機的情況下從舊索引遷移到新索引
```

```bash
curl -X POST http://{{elastic_host}}/{{index}}/_aliases -H 'Content-Type: application/json' \
-d 'body'
```

```json
{
    "actions": [
        {
            "remove": {
                "index": "{{old_index_alias}}",
                "alias": "{{alias}}"
            }
        },
        {
            "add": {
                "index": "{{new_index_alias}}",
                "alias": "{{alias}}",
				// 指定 routing 的值，或是 filter 的條件，來讓這個 alias 有限定的用途。
				// filter 若使用別名 將從指定的資料範圍搜尋資料
				"filter": { "term": { "user.id": "kimchy" } },
				// 指定 routing 可以減少讓 request 跑到其他 shard 運作的時間，能直接強制導到某些 shard 身上。
				"search_routing": "1,2",
        		"index_routing": "2",
				// 若會需要將資料透過 alias 來寫入，必預要明確的標示哪個 index 是 is_write_index
				"is_write_index": true
            }
        }
    ]
}
```

### 創建索引模板(index temple)

```bash
# 創建範例
curl -X PUT "localhost:9200/_index_template/{模板名稱}?pretty" -H 'Content-Type: application/json' \
-d 'body 以下json檔範例'
```

```json
{
  // index 或 data stream 的名字，可以使用萬用字元 * 來定義這個 pattern。
  "index_patterns": ["te*", "bar*"],
  // 索引的模板
  "template": {
    // 設定
    "settings": {
      "number_of_shards": 1
    },
    // 映射
    "mappings": {
      "_source": {
        "enabled": true
      },
      // 屬性(根據欄位)
      "properties": {
        "欄位名稱": {
          "type": "keyword"
        },
        "欄位名稱": {
          // 日期
          "type": "date",
          "format": "EEE MMM dd HH:mm:ss Z yyyy"
        },
        "欄位名稱": {
          "type": "date",
          //將 index 設定為 false，ES 就不會索引該 field 的資料
          "index": false,
		  "analyzer": "standard",
		  // "analyzer": "english",
          "search_analyzer": "english",
          "search_quote_analyzer": "standard"
        }
      },
      // 動態模板
      "dynamic_templates": [{
          "strings": {
            "match_mapping_type": "string",
            "mapping": {
              "type": "text",
              // 建立索引時指定使用分詞器
              "analyzer": "ik_max_word",
              // 搜尋時指定使用分詞器
              "search_analyzer": "ik_max_word",
              "fields": {
                "keyword": {
                  "type": "keyword",
                  "ignore_above": 256
                }
              }
            }
          }
        },
        {
          // 此示例將 name 對像中任何字段的值複製到頂級 full_name 字段 middle除外
          "full_name": {
            "path_match": "name.*",
            "path_unmatch": "*.middle",
            "mapping": {
              "type": "text",
              "copy_to": "full_name"
            }
          }
        }
      ]
    },
    "aliases": {
      "mydata": {}
    }
  },
  "priority": 500,
  "composed_of": ["component_template1", "runtime_component_template"],
  "version": 3,
  // 自定義元數據 可以不使用 以下範例為 更新訊息資料
  "_meta": {
    "class": "MyApp2::User3",
    "version": {
      "min": "1.3",
      "max": "1.5"
    }
  }
}
```

### 搜尋API(Search API)

`使用 Elasticsearch 的 REST API 可以輕鬆查詢索引的映射`

```bash
curl -XGET 'http://localhost:9200/your_index/_mapping'
```

```bash
# 返回與請求中定義的查詢匹配的搜索命中
curl -X GET "localhost:9200/{索引名稱}/_search?pretty"  -H 'Content-Type: application/json' -d 'json格式body'
```

```json
// bool-query
// bool 查詢採用 “匹配越多越好” 的方法，因此每個匹配 must 或 should 子句的分數將被加在一起，為每個文檔提供最終的_score

// must 該子句 (查詢) 必須出現在匹配的文件中，並將影響評分。
// filter 該子句 (查詢) 必須出現在匹配的文檔中。然而，與 must 不同的是，查詢的分數將被忽略。過濾器子句在過濾器上下文中執行，這意味著評分將被忽略，子句將被考慮用於緩存。
// should 該子句 (查詢) 應該出現在匹配的文檔中。
// must_not 子句 (查詢) 不能出現在匹配的文檔中。子句在過濾器上下文中執行，這意味著忽略評分，子句被考慮用於緩存。因為忽略評分，所以返回所有文檔的評分為 0。
{
  "query": {
    "bool" : {
      "must" : {
		// term 是直接把 field 拿去查詢倒排索引中確切的 term
		// match 會先對 field 進行分詞操作，然後再去倒排索引中查詢
        "term" : { "user.id" : "kimchy" }
      },
      "filter": {
        "term" : { "tags" : "production" }
      },
      "must_not" : {
		// 範圍
        "range" : {
          "age" : { "gte" : 10, "lte" : 20 }
        }
      },
      "should" : [
        { "term" : { "tags" : "env1" } },
        { "term" : { "tags" : "deployed" } }
      ],
	  // should需有幾條語句必須匹配
      "minimum_should_match" : 1,
	  // 個別字段可以自動提升 — 計入相關性分數
      "boost" : 1.0
    }
  }
}

// boosting-query
// 使查詢内容的结果減少分數 排序靠後
{
  "query": {
    "boosting": {
      "positive": {
        "term": {
          "text": "apple"
        }
      },
      "negative": {
        "term": {
          "text": "pie tart fruit crumble tree"
        }
      },
      "negative_boost": 0.5
    }
  }
}

// constant_score query
// https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-constant-score-query.html
// 包裝過濾器查詢並返回每個匹配的文檔，其相關性分數等於 boost 參數值。
{
  "query": {
    "constant_score": {
      "filter": { // 必須出現在匹配的文檔中。查詢的分數將被忽略。
        "term": { "user.id": "kimchy" }
      },
	  // 浮點數，用作與過濾器查詢匹配的每個文檔的恆定相關性分數。默認為 1.0
      "boost": 1.2
    }
  }
}

// dis_max query
// https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-dis-max-query.html
// 返回匹配一個或多個包裝查詢的文檔，稱為查詢子句或子句。
{
  "query": {
    "dis_max": {
      "queries": [ // queries 包含一個或多個查詢子句。返回的文檔必須與這些查詢中的一個或多個匹配
        { "term": { "title": "Quick pets" } },
        { "term": { "body": "Quick pets" } }
      ],
	  // tie_breaker 0 到 1.0 之間的浮點數，用於增加匹配多個查詢子句的文檔的相關性分數。默認為 0.0。
      "tie_breaker": 0.7
    }
  }
}

// function_score query
// https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-function-score-query.html#function-random
{
  "query": {
    "function_score": {
      "query": { "match_all": {} },
      "boost": "5",
      "functions": [
        {
          "filter": { "match": { "test": "bar" } },
          "random_score": {},
          "weight": 23
        },
        {
          "filter": { "match": { "test": "cat" } },
          "weight": 42
        }
      ],
      "max_boost": 42,
      "score_mode": "max",
      "boost_mode": "multiply",
      "min_score": 42
    }
  }
}
```

## Kibana(後台)

通過 Kibana 的 Dev Tools 或 Discover 頁面來查看索引映射

Dev Tools:

打開 Kibana，進入 Dev Tools 頁面，然後執行以下查詢：

```
GET /your_index/_mapping
```

在 Kibana 的 Discover 頁面，選擇要查看的索引，然後選擇左側菜單中的「Index Management」。

## Python 基本範例

```Python
from elasticsearch import Elasticsearch

es = Elasticsearch(['http://localhost:9200'])
mapping = es.indices.get_mapping(index='your_index')
print(mapping)
```

## 創建模板

`創建映射定義的 JSON 文件, 並使用 curl 或其他 HTTP 工具發送 HTTP PUT 請求`

```json
{
  "mappings": {
    "properties": {
      "field1": { "type": "text" },
      "field2": { "type": "keyword" },
      "field3": { "type": "integer" }
      // 添加其他字段及其屬性
    }
  }
}
```

```bash
curl -X PUT "http://localhost:9200/your_index_name" -H 'Content-Type: application/json' -d @mapping.json
```

`Python`

```Python
from elasticsearch import Elasticsearch

es = Elasticsearch("http://localhost:9200")

index_name = "your_index_name"
mapping_definition = {
    "mappings": {
        "properties": {
            "field1": {"type": "text"},
            "field2": {"type": "keyword"},
            "field3": {"type": "integer"}
            # 添加其他字段及其屬性
        }
    }
}

es.indices.create(index=index_name, body=mapping_definition)
```

## 使用 Elasticsearch ILM 自動刪除索引的基本步驟

定義索引生命週期策略：首先，需要定義一個索引生命週期策略，其中包括了索引的生命週期階段（例如熱、暖、冷、刪除等），以及定義在每個階段進行的操作（如何處理索引或文檔）。

應用策略到索引：將定義好的索引生命週期策略應用到索引上，這樣 Elasticsearch 就會根據策略來管理索引的生命週期，包括自動刪除過期的索引或文檔。

定義刪除策略：在索引生命週期策略中，可以定義一個刪除階段，並設置相應的條件來指示 Elasticsearch 在符合條件時自動刪除索引或文檔。

監控和調整：定期監控索引的生命週期管理情況，並根據需求調整相應的策略和條件。

# 同步資料 Mongodb

## Python - mongo-connector

### 配置文檔 config.json

```json
{
    "__comment__": "Configuration options starting with '__' are disabled",
    "__comment__": "To enable them, remove the preceding '__'",

    "mainAddress": "mongodb://username:password@host:port",
    "oplogFile": "/var/log/mongo-connector/oplog.timestamp",
    "noDump": false,
    "batchSize": -1,
    "verbosity": 0,
    "continueOnError": false,

    "logging": {
        "type": "file",
        "filename": "/var/log/mongo-connector/mongo-connector.log",
        "__format": "%(asctime)s [%(levelname)s] %(name)s:%(lineno)d - %(message)s",
        "__rotationWhen": "D",
        "__rotationInterval": 1,
        "__rotationBackups": 10,

        "__type": "syslog",
        "__host": "localhost:514"
    },

    "authentication": {
        "__adminUsername": "username",
        "__password": "password",
        "__passwordFile": "mongo-connector.pwd"
    },

    "__comment__": "For more information about SSL with MongoDB, please see http://docs.mongodb.org/manual/tutorial/configure-ssl-clients/",
    "__ssl": {
        "__sslCertfile": "Path to certificate to identify the local connection against MongoDB",
        "__sslKeyfile": "Path to the private key for sslCertfile. Not necessary if already included in sslCertfile.",
        "__sslCACerts": "Path to concatenated set of certificate authority certificates to validate the other side of the connection",
        "__sslCertificatePolicy": "Policy for validating SSL certificates provided from the other end of the connection. Possible values are 'required' (require and validate certificates), 'optional' (validate but don't require a certificate), and 'ignored' (ignore certificates)."
    },

	// 指定 db.collection
    "__namespaces": {
        "excluded.collection": false,
        "excluded_wildcard.*": false,
        "*.exclude_collection_from_every_database": false,
        "included.collection1": true,
        "included.collection2": {},
        "included.collection4": {
            "includeFields": ["included_field", "included.nested.field"]
        },
        "included.collection5": {
            "rename": "included.new_collection5_name",
            "includeFields": ["included_field", "included.nested.field"]
        },
        "included.collection6": {
            "excludeFields": ["excluded_field", "excluded.nested.field"]
        },
        "included.collection7": {
            "rename": "included.new_collection7_name",
            "excludeFields": ["excluded_field", "excluded.nested.field"]
        },
        "included_wildcard1.*": true,
        "included_wildcard2.*": true,
        "renamed.collection1": "something.else1",
        "renamed.collection2": {
            "rename": "something.else2"
        },
        "renamed_wildcard.*": {
            "rename": "new_name.*"
        },
        "gridfs.collection": {
            "gridfs": true
        },
        "gridfs_wildcard.*": {
            "gridfs": true
        }
    },

    "docManagers": [
        {
            "docManager": "elastic_doc_manager",
            "targetURL": "localhost:9200",
            "__bulkSize": 1000,
            "__uniqueKey": "_id",
            "__autoCommitInterval": null
        }
    ]
}
```

```json
{
    "__comment__": "Configuration options starting with '__' are disabled",
    "__comment__": "To enable them, remove the preceding '__'",

    "mainAddress": "mongodb://username:password@host:port",
    "oplogFile": "oplog.timestamp",
    "noDump": false,
    "batchSize": -1,
    "verbosity": 0,
    "continueOnError": false,

    "logging": {
        "type": "file",
        "filename": "mongo-connector.log",
        "__format": "%(asctime)s [%(levelname)s] %(name)s:%(lineno)d - %(message)s",
        "__rotationWhen": "D",
        "__rotationInterval": 1,
        "__rotationBackups": 10,

        "__type": "syslog",
        "__host": "localhost:514"
    },


    "__namespaces": {
        "db.*": true
    },

    "docManagers": [
        {
            "docManager": "elastic2_doc_manager",
            "targetURL": "localhost:9200",
            "__bulkSize": 1000,
            "__uniqueKey": "_id",
            "__autoCommitInterval": null
        }
    ]
}
```

## Golang - monstache

### 安裝步驟 CentOS7

```bash
# 安裝gcc
yum install gcc -y

# 在 CentOS 上安裝 Go
wget https://golang.org/dl/go1.17.1.linux-amd64.tar.gz
tar -C /usr/local -xzf go1.17.1.linux-amd64.tar.gz
export PATH=$PATH:/usr/local/go/bin
export GOPATH=$HOME/go
source ~/.bashrc
go version

# somewhere outside your $GOPATH(golang 第三方套件安裝路徑)
# cd ~/build

# clone 專案
git clone https://github.com/rwynn/monstache.git
cd monstache

# 選擇版本
git checkout <branch-or-tag-to-build>

# 安裝
go install

# 執行同步(需先編輯config.toml)
monstache -f /path/to/config.toml
```

```conf
# toml檔
# connection settings

# connect to MongoDB using the following URL
# MongoDB實例的主節點訪問地址
# /?connect=direct 直連
# https://www.mongodb.com/docs/mongodb-shell/connect/
mongo-url = "mongodb://someuser:password@localhost:40001"

# connect to the Elasticsearch REST API at the following node URLs
# Elasticsearch的訪問地址。
elasticsearch-urls = ["https://es1:9200", "https://es2:9200"]

# frequently required settings

# if you need to seed an index from a collection and not just listen and sync changes events
# you can copy entire collections or views from MongoDB to Elasticsearch
direct-read-namespaces = ["mydb.mycollection", "db.collection", "test.test", "db2.myview"]

# if you want to use MongoDB change streams instead of legacy oplog tailing use change-stream-namespaces
# change streams require at least MongoDB API 3.6+
# if you have MongoDB 4+ you can listen for changes to an entire database or entire deployment
# in this case you usually don't need regexes in your config to filter collections unless you target the deployment.
# to listen to an entire db use only the database name.  For a deployment use an empty string.
# 追蹤更新自動同步 mongodb 需使用 replica sets
# 如果要使用MongoDB變更流功能，需要指定此參數。啟用此參數後，oplog追踪會被設置為無效
# MongoDB 版本4以上 可更改 db.collection 使用 db 追蹤整個db
change-stream-namespaces = ["mydb.mycollection", "db.collection", "test.test"]

# 追蹤全部的
change-stream-namespaces = [""]

# additional settings

# if you don't want to listen for changes to all collections in MongoDB but only a few
# e.g. only listen for inserts, updates, deletes, and drops from mydb.mycollection
# this setting does not initiate a copy, it is only a filter on the change event listener
# 通過正則表達式指定需要監聽的集合。
namespace-regex = '^mydb\.mycollection$'

# compress requests to Elasticsearch
gzip = true

# generate indexing statistics
stats = true

# index statistics into Elasticsearch
index-stats = true

# use the following user name for Elasticsearch basic auth
# 訪問Elasticsearch的用戶名。
elasticsearch-user = "someuser"

# use the following password for Elasticsearch basic auth
# 訪問Elasticsearch的用戶密碼。
elasticsearch-password = "somepassword"

# use 4 go routines concurrently pushing documents to Elasticsearch
# 定義連接ES的線程數。默認為4，即使用4個Go線程同時將數據同步到ES。
elasticsearch-max-conns = 4

# use the following PEM file to connections to Elasticsearch
elasticsearch-pem-file = "/path/to/elasticCert.pem"

# validate connections to Elasticsearch
elastic-validate-pem-file = true

# propogate dropped collections in MongoDB as index deletes in Elasticsearch
# 表示當刪除MongoDB集合時，會同時刪除ES中對應的索引。
dropped-collections = true

# propogate dropped databases in MongoDB as index deletes in Elasticsearch
# 表示當刪除MongoDB數據庫時，會同時刪除ES中對應的索引。
dropped-databases = true

# do not start processing at the beginning of the MongoDB oplog
# if you set the replay to true you may see version conflict messages
# in the log if you had synced previously. This just means that you are replaying old docs which are already
# in Elasticsearch with a newer version. Elasticsearch is preventing the old docs from overwriting new ones.
replay = false

# resume processing from a timestamp saved in a previous run
# Monstache會將已成功同步到ES的MongoDB操作的時間戳寫入monstache.monstache集合中。當Monstache因為意外停止時，可通過該時間戳恢復同步任務，避免數據丟失。如果指定了cluster-name，該參數將自動開啟
resume = true

# do not validate that progress timestamps have been saved
resume-write-unsafe = false

# override the name under which resume state is saved
resume-name = "default"

# use a custom resume strategy (tokens) instead of the default strategy (timestamps)
# tokens work with MongoDB API 3.6+ while timestamps work only with MongoDB API 4.0+
# 指定恢復策略。
# Strategy 0 -default- Timestamp based resume of change streams. Compatible with MongoDB API 4.0+.
# Stategy 1 Token based resume of change streams. Compatible with MongoDB API 3.6+.
resume-strategy = 1

# exclude documents whose namespace matches the following pattern
namespace-exclude-regex = '^mydb\.ignorecollection$'

# turn on indexing of GridFS file content
# https://github.com/rwynn/monstache/issues/33
# 另外，為了清楚起見，monstache 僅在配置中啟用文件內容索引時才需要 ingest-attachment 插件。 如果將以下內容更改為 false monstache 將不會嘗試需要攝取附件的請求。
index-files = false

# turn on search result highlighting of GridFS content
file-highlighting = true

# index GridFS files inserted into the following collections
file-namespaces = ["users.fs.files"]

# print detailed information including request traces
verbose = true

# enable clustering mode
# 指定集群名稱。
cluster-name = 'apollo'

# do not exit after full-sync, rather continue tailing the oplog
exit-after-direct-reads = false

# 排除
direct-read-dynamic-exclude-regex = ".*(dbname1|dbname2).*\\.(m3_u8|m3u8|.*log.*).*"
direct-read-dynamic-exclude-regex = ".*\\.(.*m3_u8.*|.*m3u8.*|account|.*log.*)"
direct-read-dynamic-exclude-regex = "(admin|config|local)\\..*|.*\\.(.*m3_u8.*|.*m3u8.*|account|.*log.*)"

# direct-read-split-max 設置確實可能會導致使用大量記憶體，特別是當設置為較大的值時。
# 這是因為該設置決定了一次處理的最大文件數量，如果設置得太大，系統可能需要同時處理大量的文件，導致記憶體壓力增加。
# 為了降低記憶體使用量，可以嘗試將 direct-read-split-max 設置為較小的值，例如設置為 1 或 2，這樣可以減少系統同時處理的文件數量，從而減輕記憶體壓力。
# 另外，還可以優化的程序邏輯，以減少在單個操作中需要處理的數據量，從而進一步減少記憶體需求。
direct-read-split-max = 1

[[script]]
script="""
module.exports = function (doc, ns) {
  var index = "{名稱}-{日期}." + ns.split(".")[1];
  doc._meta_monstache = { index: index };
  return doc;
}
"""
```

# 例外狀況

## Error: disk usage exceeded flood-stage watermark, index has read-only-allow-delete blockedit

```
此錯誤表明數據節點的磁盤空間嚴重不足，並且已達到洪水階段磁盤使用水位線。
為防止磁盤已滿，當節點達到此水印時，Elasticsearch 會阻止寫入該節點上具有分片的任何索引。
如果塊影響相關係統索引，Kibana 和其他 Elastic Stack 功能可能會變得不可用。
```

```bash
# 要立即恢復寫入操作，可以暫時增加磁盤水印。
curl -X PUT "localhost:9200/_cluster/settings?pretty" -H 'Content-Type: application/json' -d'
{
  "persistent": {
    "cluster.routing.allocation.disk.watermark.low": "90%",
    "cluster.routing.allocation.disk.watermark.low.max_headroom": "100GB",
    "cluster.routing.allocation.disk.watermark.high": "95%",
    "cluster.routing.allocation.disk.watermark.high.max_headroom": "20GB",
    "cluster.routing.allocation.disk.watermark.flood_stage": "97%",
    "cluster.routing.allocation.disk.watermark.flood_stage.max_headroom": "5GB",
    "cluster.routing.allocation.disk.watermark.flood_stage.frozen": "97%",
    "cluster.routing.allocation.disk.watermark.flood_stage.frozen.max_headroom": "5GB"
  }
}
'

# 移除寫入塊
curl -X PUT "localhost:9200/*/_settings?expand_wildcards=all&pretty" -H 'Content-Type: application/json' -d'
{
  "index.blocks.read_only_allow_delete": null
}
'

#############################################

# 刪除不必要索引
curl -X DELETE "localhost:9200/my-index?pretty"

# 當長期解決方案到位時，重置或重新配置磁盤水印。
curl -X PUT "localhost:9200/_cluster/settings?pretty" -H 'Content-Type: application/json' -d'
{
  "persistent": {
    "cluster.routing.allocation.disk.watermark.low": null,
    "cluster.routing.allocation.disk.watermark.low.max_headroom": null,
    "cluster.routing.allocation.disk.watermark.high": null,
    "cluster.routing.allocation.disk.watermark.high.max_headroom": null,
    "cluster.routing.allocation.disk.watermark.flood_stage": null,
    "cluster.routing.allocation.disk.watermark.flood_stage.max_headroom": null,
    "cluster.routing.allocation.disk.watermark.flood_stage.frozen": null,
    "cluster.routing.allocation.disk.watermark.flood_stage.frozen.max_headroom": null
  }
}
'
```

## Validation Failed: 1: this action would add [5] shards, but this cluster currently has [5000]/[5000] maximum normal shards open;

```
提高分片上限

yml設置無效果 需使用api
```

```bash
curl -X PUT localhost:9200/_cluster/settings -H "Content-Type: application/json" -d '{ "persistent": { "cluster.max_shards_per_node": "30000" } }'
```

## kibana 發生 search_phase_execution_exception 錯誤

```bash
curl -X DELETE http://localhost:9200/.kibana*
```