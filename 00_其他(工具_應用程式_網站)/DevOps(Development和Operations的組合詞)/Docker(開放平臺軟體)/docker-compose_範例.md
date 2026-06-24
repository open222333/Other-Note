# docker-compose 範例

```
收集各服務的 docker-compose.yml 範例，依服務類型分類。
```

## 目錄

- [docker-compose 範例](#docker-compose-範例)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [Dockerfile 範例](#dockerfile-範例)
- [基礎語法](#基礎語法)
  - [完整欄位說明](#完整欄位說明)
  - [網路設定](#網路設定)
  - [多服務共用設定（extends）](#多服務共用設定extends)
  - [連線主機別名](#連線主機別名)
- [服務範例](#服務範例)
  - [CI/CD](#cicd)
    - [Jenkins](#jenkins)
  - [AI / LLM](#ai--llm)
    - [Ollama](#ollama)
  - [資料庫](#資料庫)
    - [MySQL + phpMyAdmin](#mysql--phpmyadmin)
    - [MySQL Master-Slave](#mysql-master-slave)
    - [MySQL InnoDB Cluster](#mysql-innodb-cluster)
    - [ProxySQL](#proxysql)
    - [MongoDB Replica Set（3 節點）](#mongodb-replica-set3-節點)
    - [MongoDB Replica Set（單節點）](#mongodb-replica-set單節點)
    - [Redis Cluster（6 節點）](#redis-cluster6-節點)
  - [Elastic Stack](#elastic-stack)
    - [Elasticsearch + Kibana（單節點）](#elasticsearch--kibana單節點)
    - [Elasticsearch 3 節點叢集（無 TLS）](#elasticsearch-3-節點叢集無-tls)
    - [Elasticsearch 3 節點叢集（官方 TLS）](#elasticsearch-3-節點叢集官方-tls)
    - [Elasticsearch + Kibana + Logstash](#elasticsearch--kibana--logstash)
    - [Elasticsearch + Kibana + Logstash JDBC](#elasticsearch--kibana--logstash-jdbc)
    - [Elasticsearch + Kibana + APM Server](#elasticsearch--kibana--apm-server)
    - [docker-elk（deviantony）](#docker-elkdeviantony)
  - [郵件伺服器](#郵件伺服器)
    - [docker-mailserver（後端 MTA）](#docker-mailserver後端-mta)
    - [Poste.io（全功能郵件）](#posteio全功能郵件)
    - [RainLoop（Web 客戶端）](#rainloopweb-客戶端)
    - [Roundcube（Web 客戶端 + DB）](#roundcubeweb-客戶端--db)
  - [監控](#監控)
    - [LibreNMS（SNMP 網路監控）](#librenmssnmp-網路監控)
  - [代理 / 負載均衡](#代理--負載均衡)
    - [HAProxy（Web）](#haproxyweb)
    - [HAProxy（Redis 代理）](#haproxyredis-代理)
    - [Redis（HAProxy 測試用雙節點）](#redishaproxy-測試用雙節點)
  - [檔案伺服器](#檔案伺服器)
    - [FileBrowser（基本）](#filebrowser基本)
    - [FileBrowser（固定 IP）](#filebrowser固定-ip)
  - [網站框架](#網站框架)
    - [PHP（Nginx + PHP-FPM + MongoDB）](#phpnginx--php-fpm--mongodb)
    - [PHP Laravel（Nginx + PHP-FPM + MySQL）](#php-laravelnginx--php-fpm--mysql)
    - [Nginx Plus（CentOS）+ PHP-FPM](#nginx-pluscentos-php-fpm)
    - [CentOS 容器](#centos-容器)
  - [爬蟲工具](#爬蟲工具)
    - [Scrapy + Splash（基本）](#scrapy--splash基本)
    - [Scrapy + Splash（含 volume）](#scrapy--splash含-volume)
    - [FlareSolverr（反爬蟲繞過）](#flaresolverr反爬蟲繞過)
    - [Celery（Python 任務佇列）](#celerypython-任務佇列)
  - [Git 服務](#git-服務)
    - [Gitea](#gitea)

## 參考資料

[官方文檔](https://docs.docker.com/reference/)

[官方網站](https://docs.docker.com/)

[docker 指令(官方)](https://docs.docker.com/engine/reference/commandline/cli/)

[docker-compose 指令 (官方)](https://docs.docker.com/compose/reference/)

[docker Hub 官網](https://hub.docker.com/search?type=image&image_filter=store%2Cofficial)

[Enable IPv6 support](https://docs.docker.com/config/daemon/ipv6/)

[官方文檔](https://docs.docker.com/compose/samples-for-compose/)

[Docker 官方 GitHub 組織](https://github.com/docker-library)

[Docker 官方 GitHub 組織 - healthcheck](https://github.com/docker-library/healthcheck)

### 安裝相關

[安裝官方文檔 右邊列表有其他系統的安裝步驟](https://docs.docker.com/engine/install/)

[CentOS 安裝 Docker 官方文檔](https://docs.docker.com/engine/install/centos/)

### 配置相關

[Daemon configuration file](https://docs.docker.com/engine/reference/commandline/dockerd/#daemon-configuration-file)

[dockerd daemon](https://docs.docker.com/engine/reference/commandline/dockerd/#options)

#### log配置

[Docker Container Log 文件限制](https://medium.com/%E7%A8%8B%E5%BC%8F%E8%A3%A1%E6%9C%89%E8%9F%B2/docker-container-log-%E6%96%87%E4%BB%B6%E9%99%90%E5%88%B6-1ab8559f1308)

### 網路相關

[Docker 网络模式详解及容器间网络通信](https://cloud.tencent.com/developer/news/687189)

[Network drivers overview](https://docs.docker.com/network/drivers/)

[Networking in Compose](https://docs.docker.com/compose/networking/)

[Networks top-level element](https://docs.docker.com/compose/compose-file/06-networks/#networks-top-level-element)

#### 網路 心得相關

[Provide static IP to docker containers via docker-compose](https://stackoverflow.com/questions/39493490/provide-static-ip-to-docker-containers-via-docker-compose/67856292#67856292)

[Docker 設定避開衝突網段 ( 172.17.0.0/16 )](https://blog.jks.coffee/docker-escape-subnet/)

### Docker Hub相關

[docker Hub 官網](https://hub.docker.com/search?type=image&image_filter=store%2Cofficial)

### 範例相關

[Dockerfile reference](https://docs.docker.com/engine/reference/builder/)

[docker-compose Github 範例](https://github.com/docker/awesome-compose)

[docker-compose 撰寫規範 官方文檔](https://docs.docker.com/compose/compose-file/)

[docker-compose 撰寫規範 - Github](https://github.com/compose-spec/compose-spec/blob/master/spec.md)

[docker-compose command 官方文檔](https://docs.docker.com/compose/compose-file/05-services/#command)

[Dockerfile CMD 官方文檔](https://docs.docker.com/engine/reference/builder/#cmd)

### 例外狀況

#### PHP相關

[Cron does not run in a PHP Docker container - crontab 無法在 php-fpm container 內執行](https://forums.docker.com/t/cron-does-not-run-in-a-php-docker-container/103897)

[Docker and cron is broken: can't lock /var/run/crond.pid](https://unix.stackexchange.com/questions/620452/docker-and-cron-is-broken-cant-lock-var-run-crond-pid)

[在 Docker container 中使用 Cron](https://bingdoal.github.io/others/2021/04/crontab-on-docker-container/)

[[Day4] Linux 排程工具 Crontab，也有Docker 的範例喔](https://ithelp.ithome.com.tw/m/articles/10293218)

---

# Dockerfile 範例

`檔名要是 Dockerfile`

```dockerfile
# 使用官方的 Python 執行環境作為基本的 Docker 影像
FROM python:2.7-slim
# 設定工作目錄為 /app
WORKDIR /app
# 複製目前目錄下的內容，放進 Docker 容器中的 /app
ADD . /app
# 安裝 requirements.txt 中所列的必要套件
RUN pip install -r requirements.txt
# 讓 80 連接埠可以從 Docker 容器外部存取
EXPOSE 80
# 定義環境變數
ENV NAME World
# 當 Docker 容器啟動時，自動執行 app.py
CMD ["python", "app.py"]
```

Python Dockerfile 範例：

```dockerfile
FROM python:3.6.12-buster
MAINTAINER ex@mail.com
WORKDIR /usr/src/app
RUN apt-get update; exit 0
RUN apt-get install vim net-tools iftop -y
RUN apt-get install libmediainfo-dev -y

COPY requirements.txt /usr/src/app/requirements.txt
RUN pip3 install -r /usr/src/app/requirements.txt
RUN apt-get install ffmpeg -y

COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
```

---

# 基礎語法

## 完整欄位說明

> `version:` 在 Docker Compose V2（`docker compose`，現在的預設）已廢棄，不需要加，加了會出現 `WARN: version is obsolete`。

```yml
services:
  sample:
    # 指定服務的映像檔名稱或映像檔ID
    image: dockerhub/sample
    volumes:
      - db-data:/etc/data
    # 定義容器內的環境變數
    environment:
      - MYSQL_USER=wordpress
    # 設定容器間的依賴關係
    depends_on:
      - db
    # 本機與容器間 Port 對映
    ports:
      - "3000"          # 不指定本機 Port，隨機產生
      - "45678:22"      # 本機 45678 → 容器 22
    expose:
      - 1234
    # 重啟規則：no / always / on-failure / unless-stopped
    restart: always
    networks:
      - front-tier
      - back-tier
    healthcheck:
      test: ["CMD-SHELL", "curl -f http://localhost:80/ || exit 1"]
      interval: 30s
      timeout: 10s
      retries: 3

networks:
  front-tier:
  back-tier:
  app-network:
    driver: bridge

volumes:
  db-data:
```

## 網路設定

```yml
networks:
  # bridge：預設網路驅動
  app-network:
    driver: bridge

  # 設定固定網段
  mynetwork:
    ipam:
      config:
        - subnet: '172.6.0.0/16'

  mynet1:
    ipam:
      driver: default
      config:
        - subnet: 172.28.0.0/16
          ip_range: 172.28.5.0/24
          gateway: 172.28.5.254

  # 使用預先存在的網絡
  default:
    external:
      name: my-pre-existing-network
```

## 多服務共用設定（extends）

在 `common.yml` 中定義通用配置：

```yml
services:
  app:
    build: .
    environment:
      CONFIG_FILE_PATH: /code/config
      API_KEY: xxxyyy
    cpu_shares: 5
```

在 `docker-compose.yml` 中引用：

```yml
services:
  webapp:
    extends:
      file: common.yml
      service: app
    command: /code/run_web_app
    ports:
      - 8080:8080
    depends_on:
      - queue
      - db

  queue_worker:
    extends:
      file: common.yml
      service: app
    command: /code/run_worker
    depends_on:
      - queue
```

## 連線主機別名

`IMAGE://DOCKER_IP:PORT`

```yml
services:
  web:
    build:
      context: ./docker
      dockerfile: Dockerfile.custom
    ports:
      - "8000:8000"
  db:
    image: postgres
    ports:
      - "8001:5432"
```

---

# 服務範例

## CI/CD

### Jenkins

```yml
services:
  jenkins:
    image: jenkins/jenkins:lts
    container_name: jenkins
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - jenkins_home:/var/jenkins_home
    restart: unless-stopped

volumes:
  jenkins_home:
```

---

## AI / LLM

### Ollama

```yml
services:
  ollama:
    image: ollama/ollama
    container_name: ollama
    ports:
      - "11434:11434"
    volumes:
      - ollama_data:/root/.ollama
    restart: unless-stopped

volumes:
  ollama_data:
```

---

## 資料庫

### MySQL + phpMyAdmin

```yml
services:
  mysql:
    container_name: mysql
    hostname: mysql-container
    image: mysql:5.7
    ports:
      - 3306:3306
    environment:
      MYSQL_ROOT_PASSWORD: root
    volumes:
      - ./data/mysql:/var/lib/mysql
      - ./log/mysql:/var/log/mysql
      - ./conf/mysql:/etc/mysql/conf.d
  phpmyadmin:
    container_name: phpmyadmin
    hostname: phpmyadmin-container
    image: phpmyadmin/phpmyadmin
    volumes:
      - ./conf/phpmyadmin/config.user.inc.php:/etc/phpmyadmin/config.user.inc.php
    ports:
      - 80:80
    environment:
      PMA_HOST: mysql
      PMA_PORT: 3306
      PMA_USER: root
      PMA_PASSWORD: root
      MYSQL_ROOT_PASSWORD: root
    depends_on:
      - mysql
```

### MySQL Master-Slave

```yml
services:
  mysql1:
    container_name: mysql1
    hostname: mysql1-container
    image: mysql:5.7
    ports:
      - 31216:3306
    environment:
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - ./data/mysql1:/var/lib/mysql
      - ./config/mysql1/master.cnf:/etc/mysql/my.cnf
  mysql2:
    container_name: mysql2
    hostname: mysql2-container
    image: mysql:5.7
    ports:
      - 31217:3306
    depends_on:
      - mysql1
    environment:
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - ./data/mysql2:/var/lib/mysql
      - ./config/mysql2/slave.cnf:/etc/mysql/my.cnf
```

```conf
; master.cnf
[mysqld]
server-id = 1
log-bin = mysql-bin
```

```conf
; slave.cnf
[mysqld]
server-id = 2
read-only = ON
```

### MySQL InnoDB Cluster

```yml
services:
  common:
    image: neumayer/mysql-shell-batch
    volumes:
      - ./scripts/:/scripts/
    environment:
      - MYSQL_USER=mysql_innodb_cluster_admin
      - MYSQL_PASSWORD=mysql_innodb_cluster_admin_password
      - MYSQL_HOST=node1
      - MYSQL_PORT=3306
  boot:
    extends:
      service: common
    environment:
      - EXECUTE_SCRIPT=boot.js
    network_mode: host
  run:
    extends:
      service: common
    environment:
      - EXECUTE_SCRIPT=run.js
    network_mode: host
```

### ProxySQL

```yml
services:
  proxysql:
    image: proxysql/proxysql
    container_name: proxysql
    ports:
      - "6032:6032"
      - "6033:6033"
    volumes:
      - ./proxysql.cnf:/etc/proxysql.cnf
    restart: always
```

### MongoDB Replica Set（3 節點）

含 healthcheck + 自動初始化：

```yml

services:
  mongo1:
    image: mongo:6
    container_name: mongo1
    command: ["mongod", "--replSet", "rs0", "--bind_ip_all"]
    ports:
      - "27017:27017"
    volumes:
      - mongo1_data:/data/db
    healthcheck:
      test: echo 'db.runCommand("ping").ok' | mongosh localhost:27017/test --quiet
      interval: 10s
      timeout: 10s
      retries: 5
      start_period: 40s

  mongo2:
    image: mongo:6
    container_name: mongo2
    command: ["mongod", "--replSet", "rs0", "--bind_ip_all"]
    ports:
      - "27018:27017"
    volumes:
      - mongo2_data:/data/db
    healthcheck:
      test: echo 'db.runCommand("ping").ok' | mongosh localhost:27017/test --quiet
      interval: 10s
      timeout: 10s
      retries: 5
      start_period: 40s

  mongo3:
    image: mongo:6
    container_name: mongo3
    command: ["mongod", "--replSet", "rs0", "--bind_ip_all"]
    ports:
      - "27019:27017"
    volumes:
      - mongo3_data:/data/db
    healthcheck:
      test: echo 'db.runCommand("ping").ok' | mongosh localhost:27017/test --quiet
      interval: 10s
      timeout: 10s
      retries: 5
      start_period: 40s

  mongo-init:
    image: mongo:6
    container_name: mongo-init
    depends_on:
      mongo1:
        condition: service_healthy
      mongo2:
        condition: service_healthy
      mongo3:
        condition: service_healthy
    command: >
      mongosh --host mongo1:27017 --eval
      '
      rs.initiate({
        _id: "rs0",
        members: [
          { _id: 0, host: "mongo1:27017" },
          { _id: 1, host: "mongo2:27017" },
          { _id: 2, host: "mongo3:27017" }
        ]
      })
      '

volumes:
  mongo1_data:
  mongo2_data:
  mongo3_data:
```

舊版 3 節點（mongo image 預設版）：

```yml
services:
  mongo1:
    image: mongo
    container_name: mongo1
    ports:
      - 31141:27017
    command: mongod --replSet RS --bind_ip_all --dbpath /data/db
    volumes:
      - ./data/mongo1:/data/db
  mongo2:
    image: mongo
    container_name: mongo2
    ports:
      - 31142:27017
    command: mongod --replSet RS --bind_ip_all --dbpath /data/db
    volumes:
      - ./data/mongo2:/data/db
  mongo3:
    image: mongo
    container_name: mongo3
    ports:
      - 31143:27017
    command: mongod --replSet RS --bind_ip_all --dbpath /data/db
    volumes:
      - ./data/mongo3:/data/db
```

### MongoDB Replica Set（單節點）

```yml

services:
  mongo:
    image: mongo:6
    container_name: mongo
    command: ["mongod", "--replSet", "rs0", "--bind_ip_all"]
    ports:
      - "27017:27017"
    volumes:
      - mongo_data:/data/db

volumes:
  mongo_data:
```

### Redis Cluster（6 節點）

```yml

services:
  redis-7001:
    image: redis:7
    container_name: redis-7001
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7001:7001"
    volumes:
      - ./conf/redis/7001:/etc/redis
    networks:
      - redis-cluster-net

  redis-7002:
    image: redis:7
    container_name: redis-7002
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7002:7002"
    volumes:
      - ./conf/redis/7002:/etc/redis
    networks:
      - redis-cluster-net

  redis-7003:
    image: redis:7
    container_name: redis-7003
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7003:7003"
    volumes:
      - ./conf/redis/7003:/etc/redis
    networks:
      - redis-cluster-net

  redis-7004:
    image: redis:7
    container_name: redis-7004
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7004:7004"
    volumes:
      - ./conf/redis/7004:/etc/redis
    networks:
      - redis-cluster-net

  redis-7005:
    image: redis:7
    container_name: redis-7005
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7005:7005"
    volumes:
      - ./conf/redis/7005:/etc/redis
    networks:
      - redis-cluster-net

  redis-7006:
    image: redis:7
    container_name: redis-7006
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7006:7006"
    volumes:
      - ./conf/redis/7006:/etc/redis
    networks:
      - redis-cluster-net

networks:
  redis-cluster-net:
    driver: bridge
```

---

## Elastic Stack

### Elasticsearch + Kibana（單節點）

```yml
services:
  elasticsearch:
    image: elasticsearch:7.13.3
    container_name: elasticsearch
    privileged: true
    environment:
      - "cluster.name=elasticsearch"
      - "discovery.type=single-node"
      - "ES_JAVA_OPTS=-Xms512m -Xmx2g"
      - bootstrap.memory_lock=true
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es/data:/usr/share/elasticsearch/data:rw
    ports:
      - 9200:9200
      - 9300:9300
    deploy:
      resources:
        limits:
          cpus: "2"
          memory: 1000M
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
```

### Elasticsearch 3 節點叢集（無 TLS）

```yml
services:
  es01:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.13.4
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - data01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - elastic

  es02:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.13.4
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - data02:/usr/share/elasticsearch/data
    networks:
      - elastic

  es03:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.13.4
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - data03:/usr/share/elasticsearch/data
    networks:
      - elastic

  kibana:
    image: docker.elastic.co/kibana/kibana:7.13.4
    container_name: kibana
    ports:
      - 5601:5601
    environment:
      ELASTICSEARCH_HOSTS: '["http://es01:9200","http://es02:9200","http://es03:9200"]'
    networks:
      - elastic

volumes:
  data01:
  data02:
  data03:

networks:
  elastic:
    driver: bridge
```

### Elasticsearch 3 節點叢集（官方 TLS）

> 透過 `.env` 設定 `ELASTIC_PASSWORD`、`KIBANA_PASSWORD`、`STACK_VERSION` 等環境變數。

```yml

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
        fi;
        if [ ! -f config/certs/ca.zip ]; then
          bin/elasticsearch-certutil ca --silent --pem -out config/certs/ca.zip;
          unzip config/certs/ca.zip -d config/certs;
        fi;
        if [ ! -f config/certs/certs.zip ]; then
          echo -ne "instances:\n  - name: es01\n    dns:\n      - es01\n      - localhost\n    ip:\n      - 127.0.0.1\n  - name: es02\n    dns:\n      - es02\n      - localhost\n    ip:\n      - 127.0.0.1\n  - name: es03\n    dns:\n      - es03\n      - localhost\n    ip:\n      - 127.0.0.1\n" > config/certs/instances.yml;
          bin/elasticsearch-certutil cert --silent --pem -out config/certs/certs.zip --in config/certs/instances.yml --ca-cert config/certs/ca/ca.crt --ca-key config/certs/ca/ca.key;
          unzip config/certs/certs.zip -d config/certs;
        fi;
        chown -R root:root config/certs;
        find . -type d -exec chmod 750 \{\} \;;
        find . -type f -exec chmod 640 \{\} \;;
        until curl -s --cacert config/certs/ca/ca.crt https://es01:9200 | grep -q "missing authentication credentials"; do sleep 30; done;
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
      - xpack.security.transport.ssl.enabled=true
      - xpack.security.transport.ssl.key=certs/es01/es01.key
      - xpack.security.transport.ssl.certificate=certs/es01/es01.crt
      - xpack.security.transport.ssl.certificate_authorities=certs/ca/ca.crt
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1

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
      - ELASTIC_PASSWORD=${ELASTIC_PASSWORD}
      - bootstrap.memory_lock=true
      - xpack.security.enabled=true
      - xpack.security.http.ssl.enabled=true
      - xpack.security.http.ssl.key=certs/es02/es02.key
      - xpack.security.http.ssl.certificate=certs/es02/es02.crt
      - xpack.security.http.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.transport.ssl.enabled=true
      - xpack.security.transport.ssl.key=certs/es02/es02.key
      - xpack.security.transport.ssl.certificate=certs/es02/es02.crt
      - xpack.security.transport.ssl.certificate_authorities=certs/ca/ca.crt
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1

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
      - ELASTIC_PASSWORD=${ELASTIC_PASSWORD}
      - bootstrap.memory_lock=true
      - xpack.security.enabled=true
      - xpack.security.http.ssl.enabled=true
      - xpack.security.http.ssl.key=certs/es03/es03.key
      - xpack.security.http.ssl.certificate=certs/es03/es03.crt
      - xpack.security.http.ssl.certificate_authorities=certs/ca/ca.crt
      - xpack.security.transport.ssl.enabled=true
      - xpack.security.transport.ssl.key=certs/es03/es03.key
      - xpack.security.transport.ssl.certificate=certs/es03/es03.crt
      - xpack.security.transport.ssl.certificate_authorities=certs/ca/ca.crt
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1

  kibana:
    depends_on:
      es01:
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

volumes:
  certs:
  esdata01:
  esdata02:
  esdata03:
  kibanadata:
```

### Elasticsearch + Kibana + Logstash

```yml
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.13.3
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
    ports:
      - "9200:9200"

  kibana:
    image: docker.elastic.co/kibana/kibana:7.13.3
    container_name: kibana
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch

  logstash:
    image: docker.elastic.co/logstash/logstash:7.13.3
    container_name: logstash
    volumes:
      - ./pipeline:/usr/share/logstash/pipeline
    depends_on:
      - elasticsearch
```

### Elasticsearch + Kibana + Logstash JDBC

```yml
services:
  elasticsearch:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
    ports:
      - "9200:9200"

  kibana:
    image: docker.elastic.co/kibana/kibana:7.13.3
    container_name: kibana
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch

  logstash:
    image: docker.elastic.co/logstash/logstash:7.13.3
    container_name: logstash
    volumes:
      - ./pipeline:/usr/share/logstash/pipeline
      - ./drivers:/usr/share/logstash/drivers
    depends_on:
      - elasticsearch
```

### Elasticsearch + Kibana + APM Server

```yml

services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:${STACK_VERSION}
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
    volumes:
      - esdata:/usr/share/elasticsearch/data

  kibana:
    image: docker.elastic.co/kibana/kibana:${STACK_VERSION}
    container_name: kibana
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200

  apm-server:
    image: docker.elastic.co/apm/apm-server:${STACK_VERSION}
    container_name: apm-server
    ports:
      - "8200:8200"
    depends_on:
      - elasticsearch
      - kibana
    environment:
      - output.elasticsearch.hosts=["elasticsearch:9200"]
      - apm-server.kibana.enabled=true
      - apm-server.kibana.host=kibana:5601

volumes:
  esdata:
```

### docker-elk（deviantony）

```yml

services:
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
      ES_JAVA_OPTS: -Xms256m -Xmx256m
      ELASTIC_PASSWORD: ${ELASTIC_PASSWORD:-}
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

---

## 郵件伺服器

### docker-mailserver（後端 MTA）

```yml
services:
  mailserver:
    image: docker.io/mailserver/docker-mailserver:latest
    container_name: mailserver
    hostname: mail.example.com
    ports:
      - "25:25"
      - "465:465"
      - "587:587"
      - "993:993"
    volumes:
      - ./docker-data/dms/mail-data/:/var/mail/
      - ./docker-data/dms/mail-state/:/var/mail-state/
      - ./docker-data/dms/mail-logs/:/var/log/mail/
      - ./docker-data/dms/config/:/tmp/docker-mailserver/
      - /etc/localtime:/etc/localtime:ro
    environment:
      - ENABLE_RSPAMD=1
      - ENABLE_CLAMAV=1
      - ENABLE_FAIL2BAN=1
    restart: always
    stop_grace_period: 1m
    cap_add:
      - NET_ADMIN
```

### Poste.io（全功能郵件）

```yml
services:
  poste:
    image: analogic/poste.io
    container_name: poste
    ports:
      - "25:25"
      - "80:80"
      - "443:443"
      - "110:110"
      - "143:143"
      - "993:993"
      - "995:995"
    volumes:
      - ./data:/data
    restart: always
```

### RainLoop（Web 客戶端）

```yml
services:
  rainloop:
    image: hardware/rainloop
    container_name: rainloop
    ports:
      - "8888:80"
    volumes:
      - ./data:/rainloop/data
    restart: always
```

### Roundcube（Web 客戶端 + DB）

```yml
services:
  db:
    image: mariadb
    container_name: roundcube-db
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: roundcube
      MYSQL_USER: roundcube
      MYSQL_PASSWORD: roundcubepassword
    volumes:
      - db-data:/var/lib/mysql
    restart: always

  roundcube:
    image: roundcube/roundcubemail
    container_name: roundcube
    ports:
      - "8080:80"
    environment:
      ROUNDCUBEMAIL_DB_TYPE: mysql
      ROUNDCUBEMAIL_DB_HOST: db
      ROUNDCUBEMAIL_DB_NAME: roundcube
      ROUNDCUBEMAIL_DB_USER: roundcube
      ROUNDCUBEMAIL_DB_PASSWORD: roundcubepassword
      ROUNDCUBEMAIL_DEFAULT_HOST: ssl://mail.example.com
      ROUNDCUBEMAIL_SMTP_SERVER: tls://mail.example.com
    volumes:
      - roundcube-data:/var/www/html
    depends_on:
      - db
    restart: always

volumes:
  db-data:
  roundcube-data:
```

---

## 監控

### LibreNMS（SNMP 網路監控）

```yml

services:
  db:
    image: "mariadb:10.5"
    container_name: librenms_db
    command:
      - "mysqld"
      - "--innodb-file-per-table=1"
      - "--lower-case-table-names=0"
      - "--character-set-server=utf8mb4"
      - "--collation-server=utf8mb4_unicode_ci"
    environment:
      - "TZ=${TZ}"
      - "MYSQL_ALLOW_EMPTY_PASSWORD=yes"
      - "MYSQL_DATABASE=${MYSQL_DATABASE}"
      - "MYSQL_USER=${MYSQL_USER}"
      - "MYSQL_PASSWORD=${MYSQL_PASSWORD}"
    volumes:
      - "./db:/var/lib/mysql"
    restart: always

  redis:
    image: "redis:5.0-alpine"
    container_name: librenms_redis
    environment:
      - "TZ=${TZ}"
    restart: always

  librenms:
    image: librenms/librenms:latest
    container_name: librenms
    hostname: librenms
    cap_add:
      - NET_ADMIN
      - NET_RAW
    ports:
      - "8000:8000"
    depends_on:
      - db
      - redis
    volumes:
      - "./librenms:/data"
    environment:
      - "TZ=${TZ}"
      - "DB_HOST=db"
      - "DB_NAME=${MYSQL_DATABASE}"
      - "DB_USER=${MYSQL_USER}"
      - "DB_PASSWORD=${MYSQL_PASSWORD}"
      - "REDIS_HOST=redis"
      - "REDIS_PORT=6379"
    restart: always

  dispatcher:
    image: librenms/librenms:latest
    container_name: librenms_dispatcher
    hostname: librenms-dispatcher
    cap_add:
      - NET_ADMIN
      - NET_RAW
    depends_on:
      - librenms
      - redis
    volumes:
      - "./librenms:/data"
    environment:
      - "TZ=${TZ}"
      - "DB_HOST=db"
      - "DB_NAME=${MYSQL_DATABASE}"
      - "DB_USER=${MYSQL_USER}"
      - "DB_PASSWORD=${MYSQL_PASSWORD}"
      - "REDIS_HOST=redis"
      - "REDIS_PORT=6379"
      - "DISPATCHER_NODE_ID=dispatcher1"
      - "SIDECAR_DISPATCHER=1"
    restart: always
```

---

## 代理 / 負載均衡

### HAProxy（Web）

```yml
services:
  haproxy:
    image: haproxy:latest
    container_name: haproxy
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg:ro
    restart: always
```

### HAProxy（Redis 代理）

```yml
services:
  haproxy:
    image: haproxy:latest
    container_name: haproxy
    ports:
      - "6379:6379"
    volumes:
      - ./haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg
    networks:
      - my_shared_net

networks:
  my_shared_net:
    external: true
```

### Redis（HAProxy 測試用雙節點）

```yml
services:
  redis1:
    image: redis:latest
    container_name: redis1
    ports:
      - "6380:6379"
    networks:
      - my_shared_net

  redis2:
    image: redis:latest
    container_name: redis2
    ports:
      - "6381:6379"
    networks:
      - my_shared_net

networks:
  my_shared_net:
    external: true
```

---

## 檔案伺服器

### FileBrowser（基本）

```yml
services:
  filebrowser:
    image: filebrowser/filebrowser
    container_name: filebrowser
    ports:
      - "8080:80"
    volumes:
      - ./data:/srv
      - ./database.db:/database.db
    restart: always
```

### FileBrowser（固定 IP）

```yml
services:
  filebrowser:
    image: filebrowser/filebrowser
    container_name: filebrowser
    ports:
      - "8080:80"
    volumes:
      - ./data:/srv
      - ./database.db:/database.db
    restart: always
    networks:
      restricted_network:
        ipv4_address: 192.168.1.100

networks:
  restricted_network:
    driver: bridge
    ipam:
      config:
        - subnet: 192.168.1.0/24
```

---

## 網站框架

### PHP（Nginx + PHP-FPM + MongoDB）

```yml
services:
  nginx:
    container_name: nginx
    image: nginx
    volumes:
      - ./code:/code
      - ./config/default.conf:/etc/nginx/conf.d/default.conf:ro
    ports:
      - 80:80
      - 443:443
  php-fpm:
    container_name: php-fpm
    image: php:7.4.3-fpm
    volumes:
      - ./code:/code
  mongo:
    container_name: mongo
    image: mongo
    expose:
      - 27017
```

### PHP Laravel（Nginx + PHP-FPM + MySQL）

```yml
services:
  nginx:
    build:
      context: .
      dockerfile: nginx/Dockerfile
    container_name: nginx
    ports:
      - "80:80"
    volumes:
      - ./:/var/www/html
      - ./nginx/conf.d:/etc/nginx/conf.d
    depends_on:
      - php
    networks:
      - app_network

  php:
    build:
      context: .
      dockerfile: php/Dockerfile
    container_name: php-fpm
    volumes:
      - ./:/var/www/html
    networks:
      - app_network

  mysql57:
    image: mysql:5.7
    container_name: mysql57
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: mydb
      MYSQL_USER: myuser
      MYSQL_PASSWORD: mypassword
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app_network

  phpmyadmin:
    image: phpmyadmin/phpmyadmin
    container_name: phpmyadmin
    environment:
      PMA_HOST: mysql57
      MYSQL_ROOT_PASSWORD: rootpassword
    ports:
      - "8080:80"
    depends_on:
      - mysql57
    networks:
      - app_network

networks:
  app_network:
    driver: bridge

volumes:
  mysql_data:
```

### Nginx Plus（CentOS）+ PHP-FPM

```yml
services:
  centos:
    container_name: centos
    build:
      context: ./docker
      dockerfile: Dockerfile.custom
    ports:
      - 8080:80
      - 8443:443
    command: ping 127.0.0.1
    volumes:
      - ./html:/var/www/html
    links:
      - php_fpm
    networks:
      - app-network
  php_fpm:
    container_name: php_fpm
    build: ./php
    ports:
      - 9000:9000
    volumes:
      - ./html:/var/www/html
    networks:
      - app-network
networks:
  app-network:
    driver: bridge
```

### CentOS 容器

```yml
services:
  centos:
    container_name: centos
    image: centos:centos7
    ports:
      - "8080:80"
      - "1022:22"
    command: ping 127.0.0.1
    volumes:
      - ./etc/ssl/nginx:/etc/ssl/nginx
```

---

## 爬蟲工具

### Scrapy + Splash（基本）

```yml
services:
  splash:
    image: scrapinghub/splash
    container_name: splash
    ports:
      - "8050:8050"
    restart: always
```

### Scrapy + Splash（含 volume）

```yml
services:
  scrapy:
    container_name: scrapy
    image: scrapinghub/splash
    ports:
      - 8050:8050
    volumes:
      - ../spiders:/usr/src/app
    command: ping 127.0.0.1
```

### FlareSolverr（反爬蟲繞過）

```yml
services:
  flaresolverr:
    image: ghcr.io/flaresolverr/flaresolverr:latest
    container_name: flaresolverr
    environment:
      - LOG_LEVEL=${LOG_LEVEL:-info}
      - LOG_HTML=${LOG_HTML:-false}
      - CAPTCHA_SOLVER=${CAPTCHA_SOLVER:-none}
      - TZ=${TZ:-Europe/London}
    ports:
      - "${PORT:-8191}:8191"
    restart: unless-stopped
```

### Celery（Python 任務佇列）

```yml
services:
  redis_celery:
    image: redis
  worker:
    build:
      context: ./docker
      dockerfile: Dockerfile.custom
    env_file:
      - ./env/celery.env
    volumes:
      - .:/usr/src/app
    command: celery worker -A celery -l info -E -P gevent --purge -n worker%i@%h
  beat:
    build:
      context: ./docker
      dockerfile: Dockerfile.custom
    env_file:
      - ./env/celery.env
    volumes:
      - .:/usr/src/app
    command: celery beat -A celery -l info --pidfile=
```

```.env
# python celery
CELERY_BROKER_URL=redis://redis:6379/0
CELERY_RESULT_BACKEND=redis://redis:6379/1
```

---

## Git 服務

### Gitea

```yml
services:
  gitea:
    image: gitea/gitea:1.13.1
    container_name: gitea
    restart: always
    volumes:
      - ./gitea:/data
      - /etc/timezone:/etc/timezone:ro
      - /etc/localtime:/etc/localtime:ro
    ports:
      - "80:80"
      - "22:22"
```
