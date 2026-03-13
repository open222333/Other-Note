# MySQL 工具 MySQL Router(輕量級的路由器)

```
MySQL Router 主要用於處理 MySQL InnoDB Cluster 和 MySQL Group Replication 中的連接。這兩種技術都是 MySQL 的高可用性和集群解決方案。

MySQL InnoDB Cluster：
InnoDB Cluster 是 MySQL 提供的一種集群解決方案，它包含了 MySQL Shell、Group Replication 和 MySQL Router。
MySQL Router 在 InnoDB Cluster 中的角色是負責處理客戶端的連接，並將其路由到 InnoDB Cluster 中的適當節點。
InnoDB Cluster 可以實現數據庫的高可用性和自動故障檢測。

MySQL Group Replication：
MySQL Group Replication 是 MySQL 提供的另一種集群解決方案，它使用了 Group Replication 插件。
MySQL Router 在 Group Replication 中的角色類似，它用於處理客戶端的連接，並將其路由到 Group Replication 中的活動節點。
Group Replication 提供了多主節點同步的功能，以實現數據庫的高可用性和可擴展性。
```

```
MySQL Router 的一些主要用法和特點：
高可用性： MySQL Router 可以在數個 MySQL 服務器之間進行負載均衡和數據路由，以實現高可用性。它可以檢測到服務器的可用性並自動將流量重定向到可用的服務器。
負載均衡： MySQL Router 可以根據配置的負載均衡策略將流量分發到多個 MySQL 服務器上，以確保各個服務器的負載分配均衡。
讀寫分離： MySQL Router 支持讀寫分離，可以將讀取請求路由到一個或多個讀取實例，同時將寫入請求路由到主要寫入實例。這有助於提高讀取效能和分擔主要寫入實例的負擔。
自動故障切換： MySQL Router 可以自動檢測數據庫服務器的故障，並將流量重定向到可用的服務器，以確保應用程序的連接不受影響。
SSL 支持： MySQL Router 支持 SSL 加密，可以保護數據在客戶端和服務器之間的傳輸。
動態配置： MySQL Router 可以通過配置文件進行動態配置，您可以定義路由規則、服務器組和讀寫分離設置。
```

```
MySQL Router 是 MySQL 提供的一個輕量級的路由器，用於提供高可用性和負載均衡功能，特別是在 MySQL InnoDB Cluster 和 MySQL Group Replication 環境中。以下是 MySQL Router 的一些特點和功能：
連接路由： MySQL Router 接受來自應用程序的連接，並將這些連接路由到 MySQL InnoDB Cluster 或 MySQL Group Replication 中的活動節點。它可以根據不同的策略（例如，輪詢、最小連接數等）來分發連接，實現負載均衡。
高可用性： MySQL Router 監控集群中的 MySQL Server 節點，如果某個節點失效，Router 會自動將新的連接路由到健康的節點，實現高可用性。
事務一致性： 在 MySQL Group Replication 環境中，MySQL Router 可以確保一個事務的所有操作都路由到同一個 Group 中的節點，確保事務的一致性。
安全性： MySQL Router 支持 SSL/TLS 來保護與 MySQL Server 之間的通信。這有助於確保數據在傳輸過程中的安全性。
輕量級： MySQL Router 是一個輕量級的進程，可以部署在應用程序和 MySQL Server 之間，不需要太多系統資源。
動態配置： MySQL Router 允許動態地配置 InnoDB Cluster 或 Group Replication 的相關信息，並自動應對拓撲的變化。
多版本支持： MySQL Router 支持同時運行不同版本的 MySQL Server，這使得升級或修改集群時更加靈活。
```

## 目錄

- [MySQL 工具 MySQL Router(輕量級的路由器)](#mysql-工具-mysql-router輕量級的路由器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[MySQL Router 8.0 - 官方文件](https://dev.mysql.com/doc/mysql-router/8.0/en/)

[MySQL Community Downloads - MySQL Router](https://dev.mysql.com/downloads/router/)

[Chapter 2 Installing MySQL Router - 安裝](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-installation.html)

[2.1 Installing MySQL Router on Linux](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-installation-linux.html)

[4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)

[4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)

[4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

[4.3.2.1 mysqlrouter — 命令行選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysqlrouter.html#option_mysqlrouter_force-password-validation)

### 心得相關

[Oracle官方轻量级中间件MySQL Router介绍与性能测试](https://www.modb.pro/db/77315)

[Ubuntu20.04安装MySQL Router](http://www.884358.com/ubuntu-install-mysql-router/)

[Docker image - mysql/mysql-router](https://hub.docker.com/r/mysql/mysql-router)

# 安裝

## Debian (Ubuntu)

```bash
dpkg -i mysql-apt-config_0.8.25-1_all.deb

# 更新套件信息
apt-get update

# 安裝 MySQL Router
apt-get -y install mysql-router

# CentOS 7 系統
# 安裝 MySQL 軟件庫的 RPM 配置文件
rpm -Uvh mysql80-community-release-el7-7.noarch.rpm

# 安裝 MySQL Router
yum install -y mysql-router-community

# 查看系統架構
uname -m
```

## RedHat (CentOS)

```bash
# https://dev.mysql.com/downloads/router/
yum update -y
yum install -y wget tar
wget https://dev.mysql.com/get/Downloads/MySQL-Router/mysql-router-8.1.0-linux-glibc2.28-x86_64.tar.xz
tar -zxvf mysql-router-8.1.0-linux-glibc2.28-x86_64.tar.xz
mkdir /usr/local/mysql-router
mv /root/mysql-router-8.1.0-linux-glibc2.28-x86_64/* /usr/local/mysql-router
export PATH=$PATH:/usr/local/mysql-router/bin
echo "export PATH=$PATH:/usr/local/mysql-router/bin" >> /root/.bashrc
source /root/.bashrc
```

## Docker 部署

```yml
version: '2'
services:

common: &baseDefine
    environment:
        MYSQL_HOST: "192.168.213.6"
        MYSQL_PORT: 3306
        MYSQL_USER: 'root'
        MYSQL_PASSWORD: 'urpwd.root'
        MYSQL_INNODB_CLUSTER_MEMBERS: 3
        MYSQL_CREATE_ROUTER_USER: 0
    image: "docker.io/mysql/mysql-router:latest"

    volumes:
        - "./conf:/tmp/myrouter"

    network_mode: "host"

boot:
    container_name: "mysql_router_boot"

    command: ["mysqlrouter","--bootstrap","root@192.168.213.7:3306","--directory","/tmp/myrouter","--conf-use-sockets","--conf-skip-tcp",
                    "--force","--strict","--user","mysqlrouter","--account","sqlrouter","--account-create","if-not-exists"]

    <<: *baseDefine


run:
    container_name: "mysql_router"
    restart: always

    command: ["mysqlrouter","--config","/tmp/myrouter/mysqlrouter.conf"]

    <<: *baseDefine
```

```bash
#!/bin/bash
# Copyright (c) 2018, 2021, Oracle and/or its affiliates.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; version 2 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
set -e

if [ "$1" = 'mysqlrouter' ]; then
    if [[ -z $MYSQL_HOST || -z $MYSQL_PORT || -z $MYSQL_USER || -z $MYSQL_PASSWORD ]]; then
	    echo "We require all of"
	    echo "    MYSQL_HOST"
	    echo "    MYSQL_PORT"
	    echo "    MYSQL_USER"
	    echo "    MYSQL_PASSWORD"
	    echo "to be set."
	    echo "In addition you can set"
	    echo "    MYSQL_INNODB_CLUSTER_MEMBERS "
	    echo "    MYSQL_CREATE_ROUTER_USER"
	    echo "    MYSQL_ROUTER_BOOTSTRAP_EXTRA_OPTIONS"
	    echo "Exiting."
	    exit 1
    fi

    PASSFILE=$(mktemp)
    echo "$MYSQL_PASSWORD" > "$PASSFILE"
    if [ -z $MYSQL_CREATE_ROUTER_USER ]; then
      echo "$MYSQL_PASSWORD" >> "$PASSFILE"
      MYSQL_CREATE_ROUTER_USER=1
      echo "[Entrypoint] MYSQL_CREATE_ROUTER_USER is not set, Router will generate a new account to be used at runtime."
      echo "[Entrypoint] Set it to 0 to reuse $MYSQL_USER instead."
    elif [ "$MYSQL_CREATE_ROUTER_USER" = "0" ]; then
      echo "$MYSQL_PASSWORD" >> "$PASSFILE"
      echo "[Entrypoint] MYSQL_CREATE_ROUTER_USER is 0, Router will reuse $MYSQL_USER account at runtime"
    else
      echo "[Entrypoint] MYSQL_CREATE_ROUTER_USER is not 0, Router will generate a new account to be used at runtime"
    fi

    DEFAULTS_EXTRA_FILE=$(mktemp)
    cat >"$DEFAULTS_EXTRA_FILE" <<EOF
[client]
password="$MYSQL_PASSWORD"
EOF
    unset MYSQL_PASSWORD
    max_tries=12
    attempt_num=0
    until (echo > "/dev/tcp/$MYSQL_HOST/$MYSQL_PORT") >/dev/null 2>&1; do
      echo "[Entrypoint] Waiting for mysql server $MYSQL_HOST ($attempt_num/$max_tries)"
      sleep $(( attempt_num++ ))
      if (( attempt_num == max_tries )); then
        exit 1
      fi
    done
    echo "[Entrypoint] Succesfully contacted mysql server at $MYSQL_HOST:$MYSQL_PORT. Checking for cluster state."
    if ! [[ "$(mysql --defaults-extra-file="$DEFAULTS_EXTRA_FILE" -u "$MYSQL_USER" -h "$MYSQL_HOST" -P "$MYSQL_PORT" -e "show status;" 2> /dev/null)" ]]; then
      echo "[Entrypoint] ERROR: Can not connect to database. Exiting."
      exit 1
    fi
    if [[ -n $MYSQL_INNODB_CLUSTER_MEMBERS ]]; then
      attempt_num=0
      echo $attempt_num
      echo $max_tries
      until [ "$(mysql --defaults-extra-file="$DEFAULTS_EXTRA_FILE" -u "$MYSQL_USER" -h "$MYSQL_HOST" -P "$MYSQL_PORT" -N performance_schema -e "select count(MEMBER_STATE) >= $MYSQL_INNODB_CLUSTER_MEMBERS from replication_group_members where MEMBER_STATE = 'ONLINE';" 2> /dev/null)" -eq 1 ]; do
             echo "[Entrypoint] Waiting for $MYSQL_INNODB_CLUSTER_MEMBERS cluster instances to become available via $MYSQL_HOST ($attempt_num/$max_tries)"
             sleep $(( attempt_num++ ))
             if (( attempt_num == max_tries )); then
                     exit 1
             fi
      done
      echo "[Entrypoint] Successfully contacted cluster with $MYSQL_INNODB_CLUSTER_MEMBERS members. Bootstrapping."
    fi
    if [ $(id -u) = "0" ]; then
      opt_user=--user=mysqlrouter
    fi
    if [ "$MYSQL_CREATE_ROUTER_USER" = "0" ]; then
        echo "[Entrypoint] Succesfully contacted mysql server at $MYSQL_HOST. Trying to bootstrap reusing account \"$MYSQL_USER\"."
        mysqlrouter --bootstrap "$MYSQL_USER@$MYSQL_HOST:$MYSQL_PORT" --directory /tmp/mysqlrouter --force --account-create=never --account=$MYSQL_USER $opt_user $MYSQL_ROUTER_BOOTSTRAP_EXTRA_OPTIONS < "$PASSFILE" || exit 1
    else
        echo "[Entrypoint] Succesfully contacted mysql server at $MYSQL_HOST. Trying to bootstrap."
        mysqlrouter --bootstrap "$MYSQL_USER@$MYSQL_HOST:$MYSQL_PORT" --directory /tmp/mysqlrouter --force $opt_user $MYSQL_ROUTER_BOOTSTRAP_EXTRA_OPTIONS < "$PASSFILE" || exit 1
    fi

    sed -i -e 's/logging_folder=.*$/logging_folder=/' /tmp/mysqlrouter/mysqlrouter.conf
    echo "[Entrypoint] Starting mysql-router."
    exec "$@" --config /tmp/mysqlrouter/mysqlrouter.conf

    rm -f "$PASSFILE"
    rm -f "$DEFAULTS_EXTRA_FILE"
    unset DEFAULTS_EXTRA_FILE
else
    exec "$@"
fi
```

## 配置文檔

```conf
# [4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)
# [4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)
# [4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

# MySQL Router sample configuration
#
# The following is a sample configuration file which shows
# most of the plugins available and most of their options.
#
# The paths used are defaults and should be adapted based
# on how MySQL Router was installed, for example, using the
# CMake option CMAKE_INSTALL_PREFIX
#
# The logging_folder is kept empty so message go to the
# console.
#

[DEFAULT]
# 設定日誌文件的保存目錄
logging_folder = /home/cluster/mysql-router-2.1.6/logs
# 設定插件文件的目錄
plugin_folder = /home/cluster/mysql-router-2.1.6/lib/mysqlrouter
# 設定配置文件的目錄
config_folder = /home/cluster/mysql-router-2.1.6
# 設定運行時文件的目錄
runtime_folder = /home/cluster/mysql-router-2.1.6/run
# 設定資料文件的目錄
data_folder = /var/lib
# 設定金鑰環的路徑
keyring_path = /var/lib/keyring-data
# 設定主金鑰的路徑
master_key_path = /var/lib/keyring-key

[logger]
# 設定日誌記錄的級別
level = INFO

[routing:basic_failover]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address = 0.0.0.0
# 綁定的監聽端口
bind_port = 23306
# 設定模式，這裡設定為 read-write，表示這個路由器實例可以處理讀和寫的查詢
mode = read-write
# 設定目標資料庫實例的地址和端口，這裡設定兩個目標實例
destinations = 192.168.62.37:3306,192.168.62.15:3306

[routing:balancing]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address = 0.0.0.0
# 綁定的監聽端口
bind_port = 23307
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-only，表示這個路由器實例只處理讀取查詢
mode = read-only
# 設定目標資料庫實例的地址和端口
destinations = 192.168.62.15:3306,192.168.62.37:3306

[keepalive]
# 設定心跳檢測的間隔時間，這裡設定為 60 秒
# 在設定的間隔時間內，MySQL Router 會定期向後端數據庫服務器發送心跳消息，以確保連接的有效性。
interval = 60
```

```conf
# [4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)
# [4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)
# [4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

[DEFAULT]
# 設定日誌文件的保存目錄
# logging_folder = /home/cluster/mysql-router-2.1.6/logs
# 設定插件文件的目錄
# plugin_folder = /home/cluster/mysql-router-2.1.6/lib/mysqlrouter
# 設定配置文件的目錄
# config_folder = /home/cluster/mysql-router-2.1.6
# 設定運行時文件的目錄
# runtime_folder = /home/cluster/mysql-router-2.1.6/run

[logger]
# 設定日誌記錄的級別
level = INFO

[metadata_cache:avnight]
# 元數據緩存的名稱，可根據需要命名
router_id=1
# 啟動元數據緩存時要連接的目標 MySQL 服務器地址和端口，多個地址使用逗號分隔
bootstrap_server_addresses=node_1:3306,node_2:4306,node_3:5306,node_4:6306,node_5:7306,node_6:8306,node_7:9306
# 連接 MySQL 服務器時要使用的用戶名
user=root
# 元數據緩存所屬的集群名稱，用於識別元數據緩存屬於哪個集群
metadata_cluster=avnight
# 元數據的存活時間（Time-To-Live），單位是秒。這裡設置為 0.5 秒，表示元數據在緩存中的存活時間很短。
ttl=0.5

[routing:primary]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address=0.0.0.0
# 綁定的監聽端口
bind_port=6446
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-write，表示這個路由器實例可以處理讀和寫的查詢
mode=read-write
# 設定目標資料庫實例的地址和端口
destinations=metadata-cache://avnight/default?role=PRIMARY
# 假設有三個目標資料庫實例，它們的地址分別是 A、B 和 C。如果路由策略設置為 routing_strategy=round-robin，則第一個查詢會被發送到實例 A，第二個查詢會被發送到實例 B，第三個查詢會被發送到實例 C，然後循環進行。
# 配置將查詢請求按照順序依次分發給不同的目標資料庫實例，實現查詢負載均衡。
# 這種策略適用於平均分配查詢請求的情況，但不考慮目標實例的當前負載或可用性。
routing_strategy=round-robin
protocol=classic

[routing:secondary]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address=0.0.0.0
# 綁定的監聽端口
bind_port=6447
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-only，表示這個路由器實例只處理讀取查詢
mode = read-only
# 設定目標資料庫實例的地址和端口
# destinations: 這個參數指定了路由的目標，即要將查詢路由到哪些資料庫實例。
# 在這個範例中，metadata-cache://ClusterName/default?role=SECONDARY 表示要將查詢路由到元數據緩存（metadata cache）中標記為 SECONDARY 角色的資料庫實例。
# metadata-cache://: 這是一個指示 MySQL Router 使用元數據緩存的 URL 方案。
# ClusterName/default: ClusterName 是元數據緩存的名稱，default 表示元數據緩存中的預設配置。
# role=SECONDARY: 這是一個查詢參數，表示只選擇元數據緩存中標記為 SECONDARY 角色的資料庫實例作為目標。這通常用於路由讀取查詢到次要節點，以實現讀取的負載平衡和高可用性。
destinations=metadata-cache://avnight/default?role=SECONDARY
# 這個參數設定了路由策略為“循環分發（Round Robin）加備用（Fallback）”。
# 這意味著 MySQL Router 會按照設定的順序將查詢請求依次分發給目標資料庫實例，如果其中某個實例無法正常響應或處於不可用狀態，Router 會自動將查詢轉發給下一個可用的實例。
# 這樣可以在一定程度上實現高可用性和負載均衡。
routing_strategy=round-robin-with-fallback
# 這個參數設定了數據庫連接所使用的協議為“經典協議（Classic Protocol）”。
# MySQL Router 支持多種協議，包括經典協議和 X Protocol。經典協議是傳統的 MySQL 連接協議，而 X Protocol 則是 MySQL 的新一代協議，提供更好的性能和功能支持。
protocol=classic

[keepalive]
# 設定心跳檢測的間隔時間，這裡設定為 60 秒
# 在設定的間隔時間內，MySQL Router 會定期向後端數據庫服務器發送心跳消息，以確保連接的有效性。
interval = 60
```

```conf
; https://www.cnblogs.com/fander/p/10071357.html
; mysqlrouter.conf
[DEFAULT]
name=system
user=root
keyring_path=/opt/mysql-8.0-router/data/keyring
master_key_path=/opt/mysql-8.0-router/bin/.././mysqlrouter.key
connect_timeout=30
read_timeout=30

[logger]
level = INFO

[metadata_cache:fandercluster]
router_id=1
bootstrap_server_addresses=mysql://192-168-199-122:3306,mysql://192-168-199-123:3306,mysql://192-168-199-121:3306
user=mysql_router1_klh7m3xmmru0
metadata_cluster=fandercluster
ttl=0.5

[routing:fandercluster_default_rw]
bind_address=0.0.0.0
bind_port=6446
# destinations: 這個參數指定了路由的目標，即要將查詢路由到哪些資料庫實例。
# 在這個範例中，metadata-cache://fandercluster/default?role=PRIMARY 表示要將查詢路由到元數據緩存（metadata cache）中標記為 PRIMARY 角色的資料庫實例。
# metadata-cache://: 這是一個指示 MySQL Router 使用元數據緩存的 URL 方案。
# fandercluster/default: fandercluster 是元數據緩存的名稱，default 表示元數據緩存中的預設配置。
# role=PRIMARY: 這是一個查詢參數，表示只選擇元數據緩存中標記為 PRIMARY 角色的資料庫實例作為目標。這通常用於路由讀取查詢到次要節點，以實現讀取的負載平衡和高可用性。
destinations=metadata-cache://fandercluster/default?role=PRIMARY
# 假設有三個目標資料庫實例，它們的地址分別是 A、B 和 C。如果路由策略設置為 routing_strategy=round-robin，則第一個查詢會被發送到實例 A，第二個查詢會被發送到實例 B，第三個查詢會被發送到實例 C，然後循環進行。
# 配置將查詢請求按照順序依次分發給不同的目標資料庫實例，實現查詢負載均衡。
# 這種策略適用於平均分配查詢請求的情況，但不考慮目標實例的當前負載或可用性。
routing_strategy=round-robin
# 這個參數設定了數據庫連接所使用的協議為“經典協議（Classic Protocol）”。
# MySQL Router 支持多種協議，包括經典協議和 X Protocol。經典協議是傳統的 MySQL 連接協議，而 X Protocol 則是 MySQL 的新一代協議，提供更好的性能和功能支持。
protocol=classic

[routing:fandercluster_default_ro]
bind_address=0.0.0.0
bind_port=6447
destinations=metadata-cache://fandercluster/default?role=SECONDARY
#routing_strategy=round-robin
routing_strategy=round-robin-with-fallback
protocol=classic

[routing:fandercluster_default_x_rw]
bind_address=0.0.0.0
bind_port=64460
destinations=metadata-cache://fandercluster/default?role=PRIMARY
routing_strategy=round-robin
protocol=x

[routing:fandercluster_default_x_ro]
bind_address=0.0.0.0
bind_port=64470
destinations=metadata-cache://fandercluster/default?role=SECONDARY
routing_strategy=round-robin
protocol=x
```

```conf
; router.conf
[DEFAULT]
user=mysqlrouter
keyring_path=/var/lib/mysqlrouter/keyring

[mysqlrouter]
user=mysqlrouter
password=routerpassword

; 此處的 routing:primary 表示主要路由
[routing:primary]
bind_address=127.0.0.1
bind_port=3306
destinations=127.0.0.1:3306
routing_strategy=first-available
```

### 基本範例

```
```

# 指令

```bash
# https://dev.mysql.com/doc/mysql-router/8.0/en/mysqlrouter.html#option_mysqlrouter_force-password-validation
# 查看指令
mysqlrouter --help

# 啟動 MySQL Router
mysqlrouter -c /path/to/router.conf
```
