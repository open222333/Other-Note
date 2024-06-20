# SNMP Simple Network Management Protocol(簡單網路管理協定)

```
簡單網路管理協定（SNMP，Simple Network Management Protocol）構成了網際網路工程工作小組（IETF，Internet Engineering Task Force）定義的Internet協定族的一部分。
該協定能夠支援網路管理系統，用以監測連接到網路上的裝置是否有任何引起管理上關注的情況。
它由一組網路管理的標準組成，包含一個應用層協定（application layer protocol）、資料庫模式（database schema），和一組資料物件。
```

## 參考資料

[簡單網路管理協定](https://zh.wikipedia.org/wiki/%E7%AE%80%E5%8D%95%E7%BD%91%E7%BB%9C%E7%AE%A1%E7%90%86%E5%8D%8F%E8%AE%AE)

# 安裝

## Client端

### CentOS7

```bash
# 安裝套件
yum -y install net-snmp net-snmp-utils

# 修改配置
vim /etc/snmp/snmpd.conf

	# 註解
	com2sec notConfigUser  default       public
	access  notConfigGroup ""      any       noauth    exact  systemview none none
	# 新增
	com2sec notConfigUser  {IP}/24       private
	access  notConfigGroup ""      any       noauth    exact  all none none
	# 取消註解
	view all    included  .1                               80
	disk / 10000

# 重啟&預設開機啟動
systemctl restart snmpd.service
systemctl enable snmpd.service

# 測試
snmpwalk -c private -v 2c {IP}

# 根據firewall設定 開放防火牆
```

### Ubuntu

```bash
# 安裝套件
apt install snmpd snmp

# 修改配置
vim /etc/snmp/snmpd.conf

	# 註解
	agentAddress  127.0.0.1
	# 取消註解
	agentAddress udp:161,udp6:[::1]:161
	# 新增
	view   systemonly  included   .1
	rocommunity private  {IP}/24    -V systemonly
	# public 改 private
	rocommunity private  default    -V systemonly
	rocommunity6 private  default   -V systemonly

# 重啟&預設開機啟動
systemctl restart snmpd.service
systemctl enable snmpd.service

# 測試
snmpwalk -c private -v 2c {IP}

# 根據firewall設定 開放防火牆
```

### firewall設定

### Linode
```
Protocol:	UDP
Ports:		161
IP/Netmask	{IP}/32
```

### Iptable

```bash
# snmp port:
### Ubuntu: 若無法 listen udp:161 的話請改用 tcp
iptables -A INPUT -s {IP}/32 -p udp --dport 161 -j ACCEPT

# icmp:
iptables -A INPUT -s {IP}/32 -p icmp -j ACCEPT
```

## Server端

### LibreNMS

```bash
# 安裝docker docker-compose
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-compose
# 啟動&預設開機啟動
systemctl start docker
systemctl enable docker

# clone docker-librenms
git clone https://github.com/librenms/docker.git docker-librenms
cd docker-librenms
docker buildx bake

vim  examples/compose/.env
	TZ=Asia/Taipei
	APP_DEBUG=true

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

```bash
cd examples/compose/
docker-compose up -d
```

# 用法

```bash
```