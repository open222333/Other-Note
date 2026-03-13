# docker-compose 範例

## 目錄

- [docker-compose 範例](#docker-compose-範例)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [安裝相關](#安裝相關)
    - [配置相關](#配置相關)
      - [log配置](#log配置)
    - [網路相關](#網路相關)
      - [網路 心得相關](#網路-心得相關)
    - [Docker Hub相關](#docker-hub相關)
    - [範例相關](#範例相關)
    - [例外狀況](#例外狀況)
      - [PHP相關](#php相關)
- [範例](#範例)
  - [Dockerfile](#dockerfile)
  - [docker-compose](#docker-compose)
    - [docker-compose.centos.yml](#docker-composecentosyml)
    - [docker-compose.elasticsearch.yml](#docker-composeelasticsearchyml)
    - [docker-compose.gitea.yml](#docker-composegiteayml)
    - [docker-compose.mongodb\_clusters\_replica\_set.yml](#docker-composemongodb_clusters_replica_setyml)
    - [docker-compose.mysql\_phpmyadmin.yml](#docker-composemysql_phpmyadminyml)
    - [docker-compose.php.yml](#docker-composephpyml)
    - [docker-compose.scrapy\_splash.yml](#docker-composescrapy_splashyml)
    - [docker-compose.celery\_python.yml](#docker-composecelery_pythonyml)
    - [docker-compose.mysql\_master\_slave.yml](#docker-composemysql_master_slaveyml)
    - [docker-compose.nginx\_plus(centos)-php\_fpm.yml](#docker-composenginx_pluscentos-php_fpmyml)
    - [多個服務使用同資料 示例用例](#多個服務使用同資料-示例用例)
    - [連線 主機別名](#連線-主機別名)

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

# 範例

## Dockerfile

`檔名要是Dockerfile`

`Dockerfile.sample`

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

FROM pytorch/pytorch:latest
RUN pip install Pillow
RUN pip install flask
WORKDIR /models
COPY ./script ./
EXPOSE 5002
ENTRYPOINT ["bash","run.sh"]


# From: 依據哪個Docker image，可以直接在Docker hub去搜尋已經被建立好的Docker Image，好處是可以節省自己建立環境、安裝套件的時間。
# RUN: 執行的指令，通常會在內部用 pip install 去安裝一些套件
# WORKDIR: 去到該資料夾，跟 RUN cd 不同的地方在於，RUN cd 只會在剛行程式碼去到指定位置，但下一行執行時，就又會回到原本的地方。
# COPY: 可以將本機端的資料，複製到 Docker 內部所指定的位置
# EXPOSE: 開啟的 Port
# ENTRYPOINT: Docker 建立好後，需要執行的指令
```

Python Dockerfile範例：

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

ENTRYPOINT      ["/entrypoint.sh"]
```

## docker-compose

```yml
version: "3"
# 容器
services:
    sample:
		# 指定服務的映像檔名稱或映像檔ID
		image: dockerhub/sample
		volumes:
			- db-data:/etc/data # 設定volume
		# 定義容器內的環境變數，類似docker run -e的效果
		environment:
			- MYSQL_USER=wordpress
		# 設定容器間的依賴關係，即決定容器啟動的先後順序
		depends_on: # 依賴 db 服務
			- db
		# 用來定義本機與容器間Port的對映關係，基本的格式也是HOST:CONTAINER
		ports:
			- "3000" # 不指定本機Port，本機會隨機自動產生
			- "45678:22" # 本機的45678 Port對映到容器的Port 22
		# 開放port
		expose:
			- 1234
		# 設定Container重新啟動的規則
		# 目前支援no、always、on-failure、unless-stopped等四個選項
		# 一般會設為「always」，即當Container停止或開機時可以自動啟動Container
		restart: always
		# 容器設定networks
		networks:
			- front-tier
			- back-tier
		healthcheck:
			# 指定了健康檢查的命令
			test: ['CMD', 'mysql/healthcheck.sh']
			test: ["CMD-SHELL", "curl -f http://localhost:80/ || exit 1"]
			# 健康檢查的間隔時間
			interval: 30s
			# 設定了每次健康檢查的超時時間，即每次健康檢查最多執行 10 秒，超過這個時間將視為失敗。
			timeout: 10s
			# 設定了容器允許的最大重試次數，即如果連續 3 次健康檢查都失敗，則容器被標記為不健康。
			retries: 3

# 定義網路
networks:
	front-tier:
	back-tier:
	app-network:
		driver: bridge
		# 官方說明
		# https://docs.docker.com/network/
		# https://docs.docker.com/compose/networking/

		# bridge:默認網絡驅動程序。橋接網絡。
		host: Docker主機網路。

		# # 錯誤 解決方案
		# Docker ERROR: only one instance of "host" network is allowed
		# https://stackoverflow.com/questions/63777655/docker-error-only-one-instance-of-host-network-is-allowed
		# network_mode: host

	# 使用預先存在的網絡
	# 如果希望容器加入預先存在的網絡，請使用以下external選項：
	default:
		external:
			name: my-pre-existing-network

		# overlay:將多個Docker守護進程連接在一起，使swarm服務能夠相互通信。
		# ipvlan:完全控制IPv4和IPv6尋址。
		# macvlan:將MAC地址分配給容器，網絡上顯示為物理設備。

		# 預設Docker Compose會自動建立default network，其名稱為「目錄名稱_default」
		# 換言之，networks是可以省略的

	# 設定網段
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
					aux_addresses:
					host1: 172.28.1.5
					host2: 172.28.1.6
					host3: 172.28.1.7
		options:
			foo: bar
			baz: "0"
```

### docker-compose.centos.yml

```yml
version: '3'
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

### docker-compose.elasticsearch.yml

```yml
version: '3'
services:
  elasticsearch:
    image: elasticsearch:7.13.3
    container_name: elasticsearch
    privileged: true
    command: bash -c "cd /usr/local/dockercompose/elasticsearch/plugins && elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.13.3/elasticsearch-analysis-ik-7.13.3.zip" # 安裝 ik分詞器 需根據elasticsearch版本替換版本號
    environment:
      - "cluster.name=elasticsearch" # 設置集群名稱為elasticsearch
      - "discovery.type=single-node" # 以單一節點模式啟動
      - "ES_JAVA_OPTS=-Xms512m -Xmx2g" # 設置使用jvm內存大小
      - bootstrap.memory_lock=true # 關閉 swap
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins # 插件文件掛載
      - ./es/data:/usr/local/dockercompose/elasticsearch/data:rw # 數據文件掛載
      - ./es/logs:/usr/local/dockercompose/elasticsearch/logs:rw
    ports:
      - 9200:9200
      - 9300:9300
    deploy: # 限制物理資源
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

### docker-compose.gitea.yml

```yml
version: "3"
services:
  gitea:
    image: gitea/gitea:1.13.1
    container_name: gitea
    # environment:
    #   - USER_UID=1000
    #   - USER_GID=1000
    restart: always
    volumes:
      - ./gitea:/data
      - /etc/timezone:/etc/timezone:ro
      - /etc/localtime:/etc/localtime:ro
    ports:
      - "80:80"
      - "22:22"
```

### docker-compose.mongodb_clusters_replica_set.yml

```yml
version: "3"
services:
  mongo1:
    image: mongo
    container_name: mongo1
    ports:
      - 31141:27017
    # expose: # 開給內網
    #     - 5000
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

### docker-compose.mysql_phpmyadmin.yml

```yml
version: '3'
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
      - ./data/phpmyadmin/:/srv/phpmyadmin/
    ports:
      - 80:80
    environment:
      PMA_HOST: mysql
      PMA_PORT: 3306
      PMA_USER: root
      PMA_PASSWORD: root
      MYSQL_ROOT_PASSWORD: root
      # UPLOAD_LIMIT: '100M'
    depends_on:
      - mysql
```

### docker-compose.php.yml

```yml
version: '3'
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

### docker-compose.scrapy_splash.yml

```yml
version: '3'
# 爬蟲 splash伺服器
services:
  scrapy:
    container_name: scrapy
    image: scrapinghub/splash
    ports:
      - 8050:8050
    volumes:
      - ../spiders:/usr/src/app
    command: ping 127.0.0.1 # 為了不讓他中止
```

### docker-compose.celery_python.yml

```yml
version: '3'
services:
  redis_celery:
    image: redis
  worker:
    build:
      # Dockerfile 路徑
      context: ./docker
      dockerfile: Dockerfile.custom
    env_file:
      - ./env/celery.env
    volumes:
      - .:/usr/src/app
    command: celery worker -A celery -l info -E -P gevent --purge -n worker%i@%h
  beat:
    build:
      # Dockerfile 路徑
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

### docker-compose.mysql_master_slave.yml

```yml
version: '3'
services:
  mysql1:
    container_name: mysql1
    hostname: mysql1-container
    image: mysql:5.7
    ports:
      - 31216:3306
    environment:
      - MYSQL_ROOT_PASSWORD=root
      # - MYSQL_REPLICATION_MODE=master
      # - MYSQL_REPLICATION_USER=replication
      # - MYSQL_REPLICATION_PASSWORD=testpassword
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
      # - MYSQL_REPLICATION_MODE=slave
      # - MYSQL_REPLICATION_USER=replication
      # - MYSQL_REPLICATION_PASSWORD=testpassword
      # - MYSQL_MASTER_HOST=mysql1
      # - MYSQL_MASTER_PORT_NUMBER=31216
      # - MYSQL_MASTER_ROOT_PASSWORD=root
    volumes:
      - ./data/mysql2:/var/lib/mysql
      - ./config/mysql2/slave.cnf:/etc/mysql/my.cnf
```

```conf
; master.cnf
[mysqld]
server-id = 1
log-bin = mysql-bin
# 第一個參數是新增時從第幾個編號開始，第二個參數是每次新增資料要加幾號
# auto-increment-increment = 1
# auto-increment-offset = 1
```

```conf
; slave.cnf
[mysqld]
server-id = 2
# log-bin=MySQL-bin
# super user 依然可寫入
read-only = ON
# 第一個參數是新增時從第幾個編號開始，第二個參數是每次新增資料要加幾號
# auto-increment-increment = 1
# auto-increment-offset = 1
```

```sql
clone 資料庫的資料夾 已建立
docker-compose up -d 拉起容器

建立 MySQL master-slave 架構

 ======== master server 部分 ========

-- 確認mysql-master server_id和log_bin變數
SHOW VARIABLES LIKE 'server_id%';
SHOW VARIABLES LIKE 'log_bin%';

-- 顯示master狀態：該File列顯示日誌文件的名稱並Position顯示文件中的位置。記錄這些值。稍後在設置副本時需要它們。它們表示副本應開始處理來自源的新更新的複制坐標。
SHOW MASTER STATUS;

-- 建立master-slave使用者：要創建新帳戶，請使用CREATE USER。要授予此帳戶複製所需的權限，請使用該GRANT 語句。如果您僅為複制目的創建帳戶，則該帳戶只需要 REPLICATION SLAVE權限。例如，要設置一個repl可以從example.com域內的任何主機連接以進行複制 的新用戶
CREATE USER 'user'@'host' IDENTIFIED BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'user'@'host';

-- 列出所有使用者帳號
SELECT User,Host FROM mysql.user;

 ======== slave server 部分 ========

-- 檢查mysql-slave server_id和read_only變數
SHOW VARIABLES LIKE 'server_id%';
SHOW VARIABLES LIKE 'read_only%';

-- 新增mysql-slave設定master資料 綁定到master
CHANGE MASTER TO MASTER_HOST = 'master ip',
MASTER_PORT = 3306,
MASTER_USER = 'user',
MASTER_PASSWORD = 'password',
-- 在master server時，輸入 SHOW MASTER STATUS 找出的資訊;
MASTER_LOG_FILE = 'master bin_log filename',
MASTER_LOG_POS = 'log position';

-- 確認slave狀態：SHOW SLAVE STATUS，注意：Slave_IO_Running: Yes、Slave_SQL_Running: Yes
SHOW SLAVE STATUS\G

-- 執行slave
START SLAVE;
```

### docker-compose.nginx_plus(centos)-php_fpm.yml

```yml
version: '3'
services:
  centos:
    container_name: centos
    build:
      # Dockerfile 路徑
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

```Dockerfile
# php
FROM php:7.4-fpm

RUN apt-get update
RUN mv "$PHP_INI_DIR/php.ini-production" "$PHP_INI_DIR/php.ini"
```

```Dockerfile
# centos
FROM centos/systemd
COPY . .

RUN yum install wget -y
RUN yum install vim -y
RUN yum install GeoIP  GeoIP-data -y

COPY ./setup_info/nginx-repo.crt /etc/ssl/nginx/nginx-repo.crt
COPY ./setup_info/nginx-repo.key /etc/ssl/nginx/nginx-repo.key

RUN touch /etc/ssl/nginx/nginx-repo.crt
RUN touch /etc/ssl/nginx/nginx-repo.key

RUN wget -P /etc/yum.repos.d git@cs.nginx.com/static/files/nginx-plus-7.4.repo

RUN yum install nginx-plus -y
RUN yum install nginx-plus-module-geoip -y
RUN yum install nginx-plus-module-image-filter -y
RUN yum install nginx-plus-module-subs-filter -y

RUN cp -r /etc/nginx /etc/nginx_bak
RUN rm -rf /etc/nginx/*
CMD ["nginx", "-g", "daemon off;"]
```

### 多個服務使用同資料 示例用例

```
當您有多個具有通用配置的服務時，擴展單個服務非常有用。
下面的示例是具有兩個服務的 Compose 應用程序：一個 Web 應用程序和一個隊列工作器。
這兩種服務使用相同的代碼庫並共享許多配置選項。
```

在common.yml 中，我們定義了通用配置：

```yml
services:
  app:
    build: .
    environment:
      CONFIG_FILE_PATH: /code/config
      API_KEY: xxxyyy
    cpu_shares: 5
```

在docker-compose.yml 中，我們定義了使用通用配置的具體服務：

```yml
services:
  webapp:
    extends:
      file: common.yml
      service: app
    command: /code/run_web_app
    ports:
      - 8080:8080
    expose:
      - "80"
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

### 連線 主機別名

`IMAGE`://`DOCKER_IP`:`PORT`

```yml
version: "3.9"
services:
  web:
    build:
      # Dockerfile 路徑
      context: ./docker
      dockerfile: Dockerfile.custom
    ports:
      - "8000:8000"
  db:
    image: postgres
    ports:
      - "8001:5432"
```
