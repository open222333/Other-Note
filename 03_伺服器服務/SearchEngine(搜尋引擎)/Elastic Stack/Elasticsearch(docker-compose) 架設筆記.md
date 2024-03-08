# Elasticsearch(docker-compose) 架設筆記

```
Elasticsearch是一個基於Lucene庫的搜尋引擎。
它提供了一個分散式、支援多租戶的全文搜尋引擎，具有HTTP Web介面和無模式JSON文件。
Elasticsearch是用Java開發的，並在Apache授權條款下作為開源軟體釋出。
官方客戶端在Java、.NET（C#）、PHP、Python、Apache Groovy、Ruby和許多其他語言中都是可用的。
```

```
Kibana 是一個免費且開放的用戶界面，能夠讓您對Elasticsearch 數據進行可視化，並讓您在Elastic Stack 中進行導航。
```

# 指令

```bash
# 查看節點訊息
curl http://localhost:9200/_cat/nodes?v

# 測試
curl http://localhost:9200

# 啟動pm2(一個個啟動)
pm2 start /usr/local/elasticsearch/js-pm2/bot.json
```

# 使用方法

`安裝 必要工具 git gcc wget docker docker-compose pm2 golang nodejs`

```bash
# 安裝 git
yum install git -y

# 安裝 gcc
yum install gcc -y

# 安裝 wget
yum install wget -y
```

```bash
# 設置存儲庫
yum install -y yum-utils
yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

# 驗證 Docker Engine 是否已正確安裝。
docker --version

# 安裝Docker-Compose
# 下載 Docker Compose 的當前穩定版本
# https://docs.docker.com/compose/release-notes/
curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 對二進製文件應用可執行權限
chmod +x /usr/local/bin/docker-compose

# 安裝 Docker Compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# 驗證安裝：
docker-compose --version
```

`添加常用配置 docker 設定檔 daemon.json (沒有的話直接新增)`

```bash
vim /etc/docker/daemon.json
```

```json
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "50m",
    "max-file": "3"
  },
  "ipv6": true,
  "fixed-cidr-v6": "2001:db8:1::/64"
}
```

`使設定生效 重新載入 daemon 設定, 重啟 docker`

```bash
systemctl daemon-reload
systemctl restart docker
```

`安裝nvm nodejs pm2`

```bash
# 將內容加入環境變數文檔
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# 安裝 NVM
bash /root/.nvm/install.sh

# 為了讓 NVM 生效，需要重新整理終端機的設定。
source ~/.bashrc

# 檢測是否安裝成功
nvm --version

# 安裝 nodejs
nvm install 16.16.0

# 將 Node.js 16.16.0 設置為默認版本
nvm alias default 16.16.0

# 安裝 pm2
npm install pm2@latest -g

# 安裝 pm2-logrotate (管理和維護應用程序的日誌檔案)
pm2 install pm2-logrotate
```

`安裝 Go`

```bash
# 下載 安裝包
wget https://golang.org/dl/go1.17.12.linux-amd64.tar.gz

# 解壓
tar -C /usr/local -xzf go1.17.12.linux-amd64.tar.gz

# 設定環境變數
export GOPATH=$HOME/go
export PATH=$PATH:$HOME/go/bin:$GOPATH/bin

# 重新整理終端機的設定。
source ~/.bashrc

# 檢測是否安裝成功
go version
```

```bash
cd /usr/local
git clone https://bitbucket.org/avnight/elasticsearch.git

# 進入 /usr/local/elasticsearch
mkdir es/data
mkdir es/logs

# 若 elasticsearch 啟動失敗 將es內的 data/ logs/ 權限開至最大
chmod 777 es/data/
chmod 777 es/logs/

# 啟動
docker-compose up -d

docker exec -ti elasticsearch bash

# 安裝ik分詞器 elasticsearch的版本和ik分詞器的版本需要保持一致
# Elasticsearch中預設的標準分詞器(analyze)對中文分詞不是很友好 因此需下載ik分詞器
# https://github.com/medcl/elasticsearch-analysis-ik/releases
cd /usr/share/elasticsearch/plugins
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.13.3/elasticsearch-analysis-ik-7.13.3.zip

docker-compose restart elasticsearch
```

```bash
# 創建mapping (可以在後台用模板)
curl -X POST http://localhost:9200/index/_mapping?pretty -H 'Content-Type:application/json' -d'
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

後台模板設定

http://{$host_ip}:5601/app/management/data/index_management/templates

`索引設置`

```json
{
  "index": {
    "number_of_shards": "5",
    "number_of_replicas": "0",
    "refresh_interval": "5m"
  }
}
```

`映射 - 動態模板`

```json
[
  {
    "strings": {
      "mapping": {
        "search_analyzer": "ik_max_word",
        "analyzer": "ik_max_word",
        "type": "text",
        "fields": {
          "keyword": {
            "ignore_above": 256,
            "type": "keyword"
          }
        }
      },
      "match_mapping_type": "string"
    }
  }
]
```

```bash
# clone monstache專案
git clone https://github.com/rwynn/monstache.git
cd monstache

# 版本
git checkout v6.7.10

# 安裝 monstache
go install
```

```json
// 修改設定檔 js-pm2/monstache_pm2.json
{
  "apps" : [{
    "name"        : "monstache", // pm2 app 名稱
    "script"      : "/root/go/bin/monstache",
    "watch"       : true,
    "cwd"         : "/path",
    "args"        : "-f /path/to/monstache/{name}_config.toml" // monstache 設定檔路徑
  }]
}
```

```toml
# 修改設定檔 monstache/config.toml
# connection settings
# monstache 設定檔

# connect to MongoDB using the following URL
mongo-url = "mongodb://username:password@host:port/?connect=direct"

# connect to the Elasticsearch REST API at the following node URLs
elasticsearch-urls = ["http://127.0.0.1:9200"]

# frequently required settings

# if you need to seed an index from a collection and not just listen and sync changes events
# you can copy entire collections or views from MongoDB to Elasticsearch
# direct-read-namespaces = ["mydb.mycollection", "db.collection", "test.test", "db2.myview"]
direct-read-namespaces = [""]

# if you want to use MongoDB change streams instead of legacy oplog tailing use change-stream-namespaces
# change streams require at least MongoDB API 3.6+ 3.6版本以上 實時同步
# if you have MongoDB 4+ you can listen for changes to an entire database or entire deployment
# in this case you usually don't need regexes in your config to filter collections unless you target the deployment.
# to listen to an entire db use only the database name.  For a deployment use an empty string.
# change-stream-namespaces = ["mydb.mycollection", "db.collection", "test.test"]
change-stream-namespaces = [""]

# additional settings

# if you don't want to listen for changes to all collections in MongoDB but only a few
# e.g. only listen for inserts, updates, deletes, and drops from mydb.mycollection
# this setting does not initiate a copy, it is only a filter on the change event listener
# namespace-regex = '^mydb\.mycollection$'

# compress requests to Elasticsearch
gzip = true
# generate indexing statistics
stats = true
# index statistics into Elasticsearch
index-stats = true
# use the following user name for Elasticsearch basic auth
# elasticsearch-user = "someuser"
# use the following password for Elasticsearch basic auth
# elasticsearch-password = "somepassword"
# use 4 go routines concurrently pushing documents to Elasticsearch
elasticsearch-max-conns = 4
# use the following PEM file to connections to Elasticsearch
# elasticsearch-pem-file = "/path/to/elasticCert.pem"
# validate connections to Elasticsearch
# elastic-validate-pem-file = true
# propogate dropped collections in MongoDB as index deletes in Elasticsearch
dropped-collections = false
# propogate dropped databases in MongoDB as index deletes in Elasticsearch
dropped-databases = false
# do not start processing at the beginning of the MongoDB oplog
# if you set the replay to true you may see version conflict messages
# in the log if you had synced previously. This just means that you are replaying old docs which are already
# in Elasticsearch with a newer version. Elasticsearch is preventing the old docs from overwriting new ones.
replay = false
# resume processing from a timestamp saved in a previous run
resume = true
# do not validate that progress timestamps have been saved
resume-write-unsafe = false
# override the name under which resume state is saved
resume-name = "default"
# use a custom resume strategy (tokens) instead of the default strategy (timestamps)
# tokens work with MongoDB API 3.6+ while timestamps work only with MongoDB API 4.0+
resume-strategy = 1
# exclude documents whose namespace matches the following pattern
# namespace-exclude-regex = '^mydb\.ignorecollection$'
# turn on indexing of GridFS file content
index-files = false
# turn on search result highlighting of GridFS content
# file-highlighting = true
# index GridFS files inserted into the following collections
file-namespaces = ["users.fs.files"]
# print detailed information including request traces
verbose = true
# enable clustering mode
# cluster-name = 'apollo'
# do not exit after full-sync, rather continue tailing the oplog
exit-after-direct-reads = false

# [[mapping]]
# namespace = "db.collection"
# index = ""

# 排除
direct-read-dynamic-exclude-regex = "(dbname1|dbname2).*(m3_u8|m3u8|account|.*log.*).*"

[[script]]
script="""
module.exports = function (doc, ns) {
  var index = "{名稱}-{日期}." + ns.split(".")[1];
  doc._meta_monstache = { index: index };
  return doc;
}
"""
```

`啟動 monstache 同步 mongo 建立索引`

```bash
pm2 start /usr/local/elasticsearch/js-pm2/bot_{產品名稱}.json
```

執行 pm2 startup 生成初始化系統腳本，然後執行 pm2 save 保存當前的 PM2 配置。

```bash
pm2 startup
pm2 save
```

# 詳細內容

## docker-compose

```yml
version: '3'
services:
  elasticsearch:
    image: elasticsearch:7.13.3
    container_name: elasticsearch
    privileged: true
	# 安裝 ik分詞器 需根據elasticsearch版本替換版本號
	command: bash -c "cd /usr/local/dockercompose/elasticsearch/plugins && elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.13.3/elasticsearch-analysis-ik-7.13.3.zip"
    environment:
      - "cluster.name=elasticsearch" # 設置集群名稱為elasticsearch
      - "discovery.type=single-node" # 以單一節點模式啟動
      - "ES_JAVA_OPTS=-Xms512m -Xmx2g" # 設置使用jvm內存大小
      - bootstrap.memory_lock=true # 關閉 swap
    volumes:
      - ./es/plugins:/usr/local/dockercompose/elasticsearch/plugins # 插件文件掛載
      - ./es/data:/usr/local/dockercompose/elasticsearch/data:rw # 數據文件掛載
      - ./es/logs:/usr/local/dockercompose/elasticsearch/logs:rw
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

## 配置文檔 elasticsearch.yml

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