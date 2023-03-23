# LibreNMS(開源網路裝置服務監控系統)

```
LibreNMS 是一套開源的網路與服務監控系統，主要以 PHP、MariaDB、SNMP 技術為主進行裝置資料搜集，它所能夠提供的功能如下：

全 Web 化操作介面
監測網路裝置連線狀態
提供裝置效能數據記錄
記錄網路裝置流量
檢查服務運作狀態
多樣化的警報發送機制
支援 Nagios Plugin (Service)
支援多種身份驗證
```

## 參考資料

[官方網站](https://www.librenms.org/)

[librenms docker- github](https://github.com/librenms/docker)

### 教學相關

[開源網路裝置服務監控系統](https://www.jason.tools/%E9%96%8B%E6%BA%90%E7%B6%B2%E8%B7%AF%E8%A3%9D%E7%BD%AE%E6%9C%8D%E5%8B%99%E7%9B%A3%E6%8E%A7%E7%B3%BB%E7%B5%B1)

# 安裝

## CentOS

```bash
```

## 使用 Docker

```bash
# clone docker-librenms
git clone https://github.com/librenms/docker.git docker-librenms
cd docker-librenms
docker buildx bake

vim examples/compose/.env
	TZ=Asia/Taipei
	APP_DEBUG=true

# 建立自己的docker-compose.yml
rm -rf examples/compose/docker-compose.yml
vim examples/compose/docker-compose.yml
```

```yml
version: "3.5"
services:
  db:
    image: mariadb:10.5
    container_name: librenms_db
    command:
      - "mysqld"
      - "--innodb-file-per-table=1"
      - "--lower-case-table-names=0"
      - "--character-set-server=utf8mb4"
      - "--collation-server=utf8mb4_unicode_ci"
    volumes:
      - "./db:/var/lib/mysql"
    environment:
      - "TZ=${TZ}"
      - "MYSQL_ALLOW_EMPTY_PASSWORD=yes"
      - "MYSQL_DATABASE=${MYSQL_DATABASE}"
      - "MYSQL_USER=${MYSQL_USER}"
      - "MYSQL_PASSWORD=${MYSQL_PASSWORD}"
    restart: always
  redis:
    image: redis:5.0-alpine
    container_name: librenms_redis
    environment:
      - "TZ=${TZ}"
    restart: always
  msmtpd:
    image: crazymax/msmtpd:latest
    container_name: librenms_msmtpd
    env_file:
      - "./msmtpd.env"
    restart: always
  librenms:
    image: librenms/librenms:latest
    container_name: librenms
    hostname: librenms
    cap_add:
      - NET_ADMIN
      - NET_RAW
    ports:
      - target: 8000
        published: 8000
        protocol: tcp
    depends_on:
      - db
      - redis
      - msmtpd
    volumes:
      - "./librenms:/data"
    env_file:
      - "./librenms.env"
    environment:
      - "TZ=${TZ}"
      - "PUID=${PUID}"
      - "PGID=${PGID}"
      - "DB_HOST=db"
      - "DB_NAME=${MYSQL_DATABASE}"
      - "DB_USER=${MYSQL_USER}"
      - "DB_PASSWORD=${MYSQL_PASSWORD}"
      - "DB_TIMEOUT=60"
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
      - "./rrd:/opt/librenms/rrd"
    env_file:
      - "./librenms.env"
    environment:
      - "TZ=${TZ}"
      - "PUID=${PUID}"
      - "PGID=${PGID}"
      - "DB_HOST=db"
      - "DB_NAME=${MYSQL_DATABASE}"
      - "DB_USER=${MYSQL_USER}"
      - "DB_PASSWORD=${MYSQL_PASSWORD}"
      - "DB_TIMEOUT=60"
      - "DISPATCHER_NODE_ID=dispatcher1"
      - "SIDECAR_DISPATCHER=1"
      - "APP_DEBUG=${APP_DEBUG}"
    restart: always
  syslogng:
    image: librenms/librenms:latest
    container_name: librenms_syslogng
    hostname: librenms-syslogng
    cap_add:
      - NET_ADMIN
      - NET_RAW
    depends_on:
      - librenms
      - redis
    ports:
      - target: 514
        published: 514
        protocol: tcp
      - target: 514
        published: 514
        protocol: udp
    volumes:
      - "./librenms:/data"
    env_file:
      - "./librenms.env"
    environment:
      - "TZ=${TZ}"
      - "PUID=${PUID}"
      - "PGID=${PGID}"
      - "DB_HOST=db"
      - "DB_NAME=${MYSQL_DATABASE}"
      - "DB_USER=${MYSQL_USER}"
      - "DB_PASSWORD=${MYSQL_PASSWORD}"
      - "DB_TIMEOUT=60"
      - "SIDECAR_SYSLOGNG=1"
      - "APP_DEBUG=${APP_DEBUG}"
    restart: always
  snmptrapd:
    image: librenms/librenms:latest
    container_name: librenms_snmptrapd
    hostname: librenms-snmptrapd
    cap_add:
      - NET_ADMIN
      - NET_RAW
    depends_on:
      - librenms
      - redis
    ports:
      - target: 162
        published: 162
        protocol: tcp
      - target: 162
        published: 162
        protocol: udp
    volumes:
      - "./librenms:/data"
    env_file:
      - "./librenms.env"
    environment:
      - "TZ=${TZ}"
      - "PUID=${PUID}"
      - "PGID=${PGID}"
      - "DB_HOST=db"
      - "DB_NAME=${MYSQL_DATABASE}"
      - "DB_USER=${MYSQL_USER}"
      - "DB_PASSWORD=${MYSQL_PASSWORD}"
      - "DB_TIMEOUT=60"
      - "SIDECAR_SNMPTRAPD=1"
      - "APP_DEBUG=${APP_DEBUG}"
    restart: always
```