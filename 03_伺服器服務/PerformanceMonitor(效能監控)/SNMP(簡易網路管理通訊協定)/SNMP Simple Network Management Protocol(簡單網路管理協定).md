# SNMP Simple Network Management Protocol(簡單網路管理協定)

```
簡單網路管理協定（SNMP，Simple Network Management Protocol）構成了網際網路工程工作小組（IETF，Internet Engineering Task Force）定義的Internet協定族的一部分。
該協定能夠支援網路管理系統，用以監測連接到網路上的裝置是否有任何引起管理上關注的情況。
它由一組網路管理的標準組成，包含一個應用層協定（application layer protocol）、資料庫模式（database schema），和一組資料物件。
```

## 參考資料

[簡單網路管理協定](https://zh.wikipedia.org/wiki/%E7%AE%80%E5%8D%95%E7%BD%91%E7%BB%9C%E7%AE%A1%E7%90%86%E5%8D%8F%E8%AE%AE)

[snmpd.conf – configuration file for the Net-SNMP agent” (man page) - 此頁對 view、include／exclude、OID 子樹等指令有詳細說明](https://www.net-snmp.org/docs/man/snmpd.conf.html?utm_source=chatgpt.com)

[“snmpd.examples(5) – example configuration for the Net-SNMP agent” (openSUSE 手冊頁) - 提供多個實例，包含 view-based 權限控制的範例。](https://manpages.opensuse.org/Tumbleweed/net-snmp/snmpd.examples.5.en.html)

[IBM 文件 “Configuring access control using View-Based Access Control” - 非專門針對 Net-SNMP，但其講解了 SNMP 的 View／Access Control 機制](https://www.ibm.com/docs/en/linux-on-systems?topic=s-access-control)

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

安裝套件

```bash
apt install snmpd snmp -y
```

修改配置

```sh
vim /etc/snmp/snmpd.conf
```

```conf
# 註解 註解掉，是要讓 SNMP 可以被外部主機存取
agentAddress  127.0.0.1
agentaddress  127.0.0.1,[::1]
# 取消註解 SNMP 會在 UDP 161 埠 上監聽 IPv4。 也會在 IPv6 本機位址 (::1) 上監聽。
# 設定後外部網段可以用 IPv4（UDP 161）來連線這台主機的 SNMP 服務
agentAddress udp:161,udp6:[::1]:161
agentaddress udp:161,udp6:[::1]:161
# 新增 View（檢視定義） systemonly 的「View」，允許存取 SNMP OID 樹 .1（即整個 MIB 树的根節點）
# IP 要帶入
view   systemonly  included   .1
rocommunity private  {IP}/24    -V systemonly
# public 改 private 允許的社群與網段, SNMP 服務能讓特定網段的監控主機存取
; 社群名稱：private
; 允許的來源網段：{IP}/24
; 可使用的 View：systemonly
; 權限為只讀（rocommunity = read-only community）
rocommunity private  default    -V systemonly
rocommunity6 private  default   -V systemonly
```

```sh
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

# 配置範例

修改完後重啟 SNMPD

```sh
systemctl restart snmpd
systemctl status snmpd
```

測試 SNMP 是否正常

在監控主機或本機測試

```sh
snmpwalk -v2c -c private localhost system
```

## 推薦最小化安全配置

```conf
# SNMP agent 設定
agentAddress udp:161,udp6:[::1]:161

# 定義允許查看的 OID 範圍
view   systemonly  included   .1.3.6.1.2.1.1

# 允許特定網段存取 SNMP
rocommunity private  192.168.1.0/24  -V systemonly

# 預設只讀社群
rocommunity private  default    -V systemonly
rocommunity6 private  default   -V systemonly
```

# 說明

## view   systemonly  included   .1.3.6.1.2.1.1

```
view：設定一個「View」（檢視／可見的 OID 範圍）。
systemonly：這個 view 的名稱（你可以自訂，例如 limited, sysview）。
included：表示包含該 OID 範圍（相對於 excluded）。
.1.3.6.1.2.1.1：要包含的 OID 範圍（這裡是「system」群組的根）。
```

### .1.3.6.1.2.1.1 是什麼？

這個 OID 對應到 SNMP MIB-2 的 system 群組，典型包含像是：

```
sysDescr（系統描述）
sysObjectID
sysUpTime
sysContact
sysName
sysLocation
sysServices
```