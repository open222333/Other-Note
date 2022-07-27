# Elasticsearch(搜尋引擎) & Kibana(Elasticsearch用戶界面) 筆記

```
Elasticsearch是一個基於Lucene庫的搜尋引擎。
它提供了一個分散式、支援多租戶的全文搜尋引擎，具有HTTP Web介面和無模式JSON文件。
Elasticsearch是用Java開發的，並在Apache授權條款下作為開源軟體釋出。
官方客戶端在Java、.NET（C#）、PHP、Python、Apache Groovy、Ruby和許多其他語言中都是可用的。
```

```
Kibana 是一個免費且開放的用戶界面，能夠讓您對Elasticsearch 數據進行可視化，並讓您在Elastic Stack 中進行導航。
```

## 目錄

- [Elasticsearch(搜尋引擎) & Kibana(Elasticsearch用戶界面) 筆記](#elasticsearch搜尋引擎--kibanaelasticsearch用戶界面-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [觀念](#觀念)
	- [ELK](#elk)
	- [index](#index)
- [指令](#指令)
- [安裝步驟 docker-compose](#安裝步驟-docker-compose)
- [安裝步驟 docker-compose 集群](#安裝步驟-docker-compose-集群)
- [安裝步驟 Elasticsearch Docker](#安裝步驟-elasticsearch-docker)
- [安裝步驟 CentOS7](#安裝步驟-centos7)
- [配置文檔 Java jvm.options](#配置文檔-java-jvmoptions)
- [配置文檔 elasticsearch.yml](#配置文檔-elasticsearchyml)
- [配置文檔 override.conf](#配置文檔-overrideconf)
- [生產環境 建議設定](#生產環境-建議設定)
- [REST APIs](#rest-apis)
	- [index API](#index-api)
- [Mongodb 同步資料](#mongodb-同步資料)
	- [Python - mongo-connector](#python---mongo-connector)
		- [config.json](#configjson)
	- [Golang - monstache](#golang---monstache)
		- [安裝步驟 CentOS7](#安裝步驟-centos7-1)

## 參考資料

[REST APIs - 官方API文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/rest-apis.html)

[Elasticsearch Guide - 官方教學文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

[Elasticsearch WIKI](https://zh.wikipedia.org/zh-tw/Elasticsearch)

[[Elasticsearch] 基本概念 & 搜尋入門](https://godleon.github.io/blog/Elasticsearch/Elasticsearch-getting-started/)

[全文搜索引擎 Elasticsearch 入门教程](http://www.ruanyifeng.com/blog/2017/08/elasticsearch.html)

[Kibana 介紹](https://www.elastic.co/cn/kibana/)

[docker-compose安裝elasticsearch及kibana](https://www.cnblogs.com/chenyuanbo/p/16183304.html)

[How To Install ElasticSearch 7.x on CentOS 7](https://computingforgeeks.com/how-to-install-elasticsearch-on-centos/)

[Docker安装ElasticSearch和Kibana](https://blog.csdn.net/ThinkWon/article/details/122808762)

[docker-compose快速部署elasticsearch-8.x集群+kibana](https://blog.csdn.net/boling_cavalry/article/details/125232858)

[生產環境的 ElasticSearch 安裝指南](https://iter01.com/74792.html)

[理解ElasticSearch工作原理](https://www.jianshu.com/p/52b92f1a9c47)

[elasticsearch-analysis-ik - ik分詞器 github專案](https://github.com/medcl/elasticsearch-analysis-ik)

[elasticsearch-analysis-ik - ik分詞器 所有版本 手動下載](https://github.com/medcl/elasticsearch-analysis-ik/releases)

[Using Elasticsearch to Offload Real-Time Analytics from MongoDB](https://rockset.com/blog/using-elasticsearch-to-offload-real-time-analytics-from-mongodb/)

# 觀念

## ELK

```
ELK包含三個東西 Elasticsearch、Logstash、Kibana

LogStash 是 Indexer & Shipper
Elasticsearch 是 Search & Storage
Kibana 是 Web Interface

Logstash蒐集Log，透過Broker(透過Redis，也可以透過Kafka或是message queue工具，主要負責多手去蒐集以及暫存log)接著傳遞給予Logstash運作進行Index的動作
最後儲存在Elasticsearch中，可以供查詢以及其他應用
Kibana在進行web介面上的串接，前端視覺化
```

## index

* index 在 ES 中是個邏輯空間的概念，用來儲存 document 的容器，而這些 document 內容都是相似的 (跟其他領域的 index 用法不太一樣)

* shard 在 ES 中則是個物理空間的的概念，index 中的資料會分散放在不同的 shard 中

* index 由以下幾個部份組成：

	- data：由 document + metadata 所組成

	- mapping：用來定義每個欄位名稱 & 類型

	- setting：定義資料是如何存放(例如：replication 數量, 使用的 shard 數量)

* 在 ES 7.0 的版本後，index 在 type 部份只能設定為 _doc (在以前的版本是可以設定不同的 type)

# 指令

```bash
# 查看節點訊息
curl http://localhost:9200/_cat/nodes?v

# 查看當前節點的所有索引。
curl -X GET 'http://localhost:9200/_cat/indexes?v'

# 測試
curl http://localhost:9200

# 查看plugin 訊息
elasticsearch-plugin -h
```

# 安裝步驟 docker-compose

```yml
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

```bash
# 開啟防火牆
iptables -A INPUT -p tcp --dport 5601 -j ACCEPT
iptables -A INPUT -p tcp --dport 9200 -j ACCEPT
```

# 安裝步驟 docker-compose 集群

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

# 安裝步驟 Elasticsearch Docker

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

# 安裝步驟 CentOS7

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
iptables -A INPUT -p tcp --dport 5601 -j ACCEPT
iptables -A INPUT -p tcp --dport 9200 -j ACCEPT

firewall-cmd --add-port=5601/tcp --permanent
firewall-cmd --reload
```

# 配置文檔 Java jvm.options

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

# 配置文檔 elasticsearch.yml

```yml
### 集群(Cluster) ###
# 配置集群名稱，由多個es實例組成的集群，有一個共同的名稱。
cluster.name: my-application

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

# 禁止swapping交換。
bootstrap.mlockall: true

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
# 刪除索引時需要顯式名稱。
action.destructive_requires_name: true
```

# 配置文檔 override.conf

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

# 生產環境 建議設定

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

# REST APIs

[REST APIs - 官方API文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/rest-apis.html)

## index API

[Index APIs - 官方API文檔](https://www.elastic.co/guide/en/elasticsearch/reference/current/indices.html)

```
Index APIs are used to manage individual indices, index settings, aliases, mappings, and index templates.
```

```bash
# 1.create a index
curl -XPUT http://localhost:9200/index

# 2.create a mapping
curl -XPOST http://localhost:9200/index/_mapping -H 'Content-Type:application/json' -d'
{
	"properties": {
		"content": {
			"type": "text",
			"analyzer": "ik_max_word",
			"search_analyzer": "ik_smart"
		}
	}
}'

# 3.index some docs
curl -XPOST http://localhost:9200/index/_create/1 -H 'Content-Type:application/json' -d'{"content":"美国留给伊拉克的是个烂摊子吗"}'
```

# Mongodb 同步資料

`可使用工具`

  * [monstache - Golang](https://github.com/rwynn/monstache)

  * [mongo-connector - Python](https://github.com/yougov/mongo-connector)

  * [Mongoosastic - NodeJS](https://github.com/mongoosastic/mongoosastic)

## Python - mongo-connector

[mongo-connector实现MongoDB与elasticsearch实时同步](https://blog.csdn.net/jerrism/article/details/110318159)

[Python 模組 mongo-connector(MongoDB and Elasticsearch)](../../01_程式語言/Python/Python%20Elasticsearch(搜尋引擎)/Python%20模組%20mongo-connector(MongoDB%20and%20Elasticsearch).md)

[mongo-connector实现MongoDB与elasticsearch实时同步深入详解](https://blog.csdn.net/laoyang360/article/details/51842822)

[29.mongo-connector實現MongoDB與elasticsearch實時同步(ES與非關係型資料庫同步)](https://www.796t.com/content/1549085781.html)

[https://www.796t.com/content/1549137781.html](mongo-connector實現MongoDB與elasticsearch實時同步深入詳解)

[利用mongo-connector將mongodb數據同步到elasticsearch的流程以及會遇到的坑](https://www.twblogs.net/a/5b8c06592b717718832fe1d2)

[Configuration Options](https://github.com/yougov/mongo-connector/wiki/Configuration-Options#configure-authentication)

`配置文檔 config.json`

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

### config.json

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

[monstache-showcase/docker-compose.sc.yml](https://github.com/rwynn/monstache-showcase/blob/a25cddeedc9e8f1481aa7de19cd634158792b28c/docker-compose.sc.yml#L53)

[monstache Configuration](https://rwynn.github.io/monstache-site/config/)

[Monstache](https://rwynn.github.io/monstache-site/start/)

### 安裝步驟 CentOS7

```bash
# 安裝gcc
yum install gcc -y

# somewhere outside your $GOPATH(golang 第三方套件安裝路徑)
cd ~/build

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
# 另外，為了清楚起見，monstache 僅在配置中啟用文件內容索引時才需要 ingest-attachment 插件。 如果您將以下內容更改為 false monstache 將不會嘗試需要攝取附件的請求。
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
```