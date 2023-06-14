# Nginx 筆記

## 參考資料

[官方文檔](http://nginx.org/en/docs/)

[中文文檔](https://www.docs4dev.com/docs/zh/nginx/current/reference/)

[DevOps：如何將 Nginx + uWSGI + Django 的服務部署在一台主機的根目錄](https://orcahmlee.github.io/devops/nginx-uwsgi-django-root/)

### 監控相關

[啟用 Nginx Status 的設定](https://blog.longwin.com.tw/2011/05/nginx-status-set-2011/)

[即時監控 Nginx 網頁伺服器狀態，啟用 stub_status 模組](https://blog.gtwang.org/linux/nginx-enable-stub_status-module-to-collect-metrics/)

### 例外狀況

[Nginx启动报错Cannot allocate memory](https://www.cnblogs.com/93bok/p/12424895.html)

[Nginx 24: Too Many Open Files Error And Solution](https://www.cyberciti.biz/faq/linux-unix-nginx-too-many-open-files/)

# 安裝

## 安裝步驟 MacOS

```bash
### macOS ###
# 安裝
brew install nginx

# 重啟
brew services restart nginx
```

## 安裝步驟 CentOS7

```bash
### CentOS7 ###
# 安裝擴展庫
yum install epel-release -y

# 更新 yum
yum update -y

# 安裝 nginx
yum install nginx -y

# 查看版本
nginx -v

# 啟動服務
systemctl start nginx
# 開機啟動
systemctl enable nginx
# 查詢啟動狀態
systemctl status nginx
# 重啟
systemctl restart nginx
# 停止
systemctl stop nginx
# 列出有在開機預設開啟中的程式或服務
systemctl list-unit-files | grep enabled
# 目前有在執行的服務
systemctl | grep running

# 建立連接 保持文件同步
ln -s /etc/nginx/sites-available/deploy-at-root-uwsgi-pass.conf /etc/nginx/sites-enabled/

# 檢查設定檔是否正確
nginx -t

# 重啟
nginx -s reload

# 對外開放 80 port
# --permanent 指定為永久設定，否則在 firewalld重啟或是重新讀取設定，就會失效
firewall-cmd --zone=public --add-port=80/tcp --permanent

vim /etc/sysconfig/iptables
iptables -A INPUT -p tcp --dport 80 -j ACCEPT

# 重新讀取 firewall 設定
firewall-cmd --reload
```

## 安裝步驟 Ubuntu

```bash
# 更新apt庫
apt update

# 安裝
apt install nginx -y

# 啟動服務
systemctl start nginx
# 開機啟動
systemctl enable nginx
# 查詢啟動狀態
systemctl status nginx
# 重啟
systemctl restart nginx
# 停止
systemctl stop nginx
```

## 安裝 nginx-plus

```bash
### 安裝 nginx-plus ###
# 設定 nginx-plus 安裝所需的憑證
mkdir /etc/ssl/nginx

# 需有憑證
touch /etc/ssl/nginx/nginx-repo.crt
touch /etc/ssl/nginx/nginx-repo.key

# 設定 nginx-plus 的套yum件庫
wget -P /etc/yum.repos.d https://cs.nginx.com/static/files/nginx-plus-7.4.repo

# 安裝 nginx-plus
yum install nginx-plus -y

# 安裝官網所使用的 nginx-plus 套件
yum install nginx-plus-module-geoip -y
yum install nginx-plus-module-image-filter -y
yum install nginx-plus-module-subs-filter -y
```

# 指令

```bash
# 查看幫助
nginx -h

# 查看nginx的版本
nginx -v

# 查看版本和nginx的配置選項
nginx -V

# 測試配置文件的正確性
nginx -t

# 測試配置文件，並顯示配置文件（這個命令可以快速查看配置文件）
Nginx -T

# 測試配置文件，但是只顯示錯誤信息
nginx -q

# 發送信號，下面詳細介紹
nginx -s

# 設置前綴
nginx -p

# 設置配置文件
nginx -c

# 附加配置文件路徑
nginx -g
```

# 設定檔 conf

[nginx documentation - 參數說明](http://nginx.org/en/docs/)

預設設定檔位置

`/etc/nginx/conf.d/`

配置(預設讀取)`/etc/nginx/nginx.conf`

`/etc/nginx/conf.d/[自行命名].conf`


`/etc/nginx/mime.types` 媒體類別(多用途網際網路郵件擴展或是MIME類別)是一種表示文件、檔案或各式位元組的標準。

```nginx
# 放置不同域名的 config file
# 主設定檔中的 http context 加入一行
include /etc/nginx/conf.d/*.conf;
# 即可將不同域名的設定引入
# 達成方便管理與修改不同域名設定的特性
```

```nginx
### Module ngx_http_core_module location ###
location [ = | ~ | ~* | ^~ ] uri { ... }
location @name { ... }

# =  表示精確匹配。只有請求的url路徑與後面的字符串完全相等時，才會命中。
# ~  表示該規則是使用正則定義的，區分大小寫。
# ~* 表示該規則是使用正則定義的，不區分大小寫。
# ^~ 表示如果該符號後面的字符是最佳匹配，採用該規則，不再進行後續的查找。

location @name的用法

# @用來定義一個命名location。
# 主要用於內部重定向，不能用來處理正常的請求。

location / {
    try_files $uri $uri/ @custom
}
location @custom {
    # ...do something
}

# 隱藏index.php
location / {
    try_files $uri $uri/ /index.php?$query_string;
}

user  nginx;
worker_processes  1;

# 錯誤寫入的地方
error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;

events {
	worker_connections  1024;
}

http {
	include       /etc/nginx/mime.types;
	default_type  application/octet-stream;

	# 紀錄 設定日誌格式
	log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
						'$status $body_bytes_sent "$http_referer" '
						'"$http_user_agent" "$http_x_forwarded_for"';

	access_log  /var/log/nginx/access.log  main;

	sendfile        on;
	# tcp_nopush     on;
	keepalive_timeout  65;
	# gzip  on;

	# 載入/etc/nginx/conf.d/內所有 .conf 設定
	include /etc/nginx/conf.d/*.conf;

	server {
		listen 80;
		listen [::]:80;
		server_name localhost;

		location / {
			root /usr/share/nginx/html;
			index index.html index.htm;
		}
	}

	# 代表可以將請求 proxy 到分別監聽 5000 與 5001 port 的兩個應用
	upstream api {
		server localhost:5000;
		server localhost:5001;
	}

	server {
		listen 80;
		listen [::]:80;
		server_name SERVER_IP;
		root /home/ryan;

		proxy_set_header Host $http_host;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

		location / {
				proxy_pass http://api/;
		}
	}

	## Settings for a TLS enabled server.
	server {
		listen       443 ssl http2;
		listen       [::]:443 ssl http2;
		server_name  _;
		root         /usr/share/nginx/html;

		## 配置 SSL 證照
		ssl_certificate "/etc/pki/nginx/server.crt";
		ssl_certificate_key "/etc/pki/nginx/private/server.key";

		ssl_session_cache shared:SSL:1m;
		ssl_session_timeout  10m;
		ssl_ciphers PROFILE=SYSTEM;
		ssl_prefer_server_ciphers on;

		# Load configuration files for the default server block.
		include /etc/nginx/default.d/*.conf;

		error_page 404 /404.html;
			location = /40x.html {
		}

		error_page 500 502 503 504 /50x.html;
			location = /50x.html {
		}
	}
}
```

## upstream

```
upstream 定義將 request proxy 過去的應用
上方的例子代表可以將請求 proxy 到分別監聽 5000 與 5001 port 的兩個應用
可以達到 load balancer 負載平衡的功能
```

## server

```
定義了 proxy server 的相關設定
包括要監聽的 port (http 為 80 ，https 為 443)
規定哪些 domain 或 ip 的 request 會被 nginx server 處理（server_name）
```

## location

```
像是 routing 的概念
設定不同的 path 要對應到怎麼樣的設定
上圖的範例 location 後面接的是 /
代表任何路徑都會被接收處理
```

## try_files

```nginx
# try_files 只能運行於 server, location 之中，有兩種不同的用法：
file 遵循該 context 中所提供的 root / alias 為根目錄，往後尋找相對路徑的結果

# try_files file ... uri;
# try_files file ... =code;

### 第一種用法
# 當一個來自 yoursite.org 的請求，nginx 會去尋找 /var/www/youtsite_path/$uri 這個檔案是否存在，如果找不到時回傳 /var/www/youtsite_path/index.html 這個檔案出去。
server {
  listen 80;
  server_name yoursite.org;
  root /var/www/yoursite_path;

  location / {
    try_files $uri /$uri /index.html;
  }
}


### 第二種用法
# 當找不到圖片時，自動跳轉到另一台上傳的服務站
location /images {
  root /var/www/yoursite_path/images;
  try_files $uri /$uri =404;
}
```


```nginx
# 將所有的 HTTP 路由，在目標路徑找不到時，重導向去回應 index.html 的檔案，進而使 SPA 在抓取其路徑進行渲染，並使用 History API 來控制各種頁面的跳轉與資料的傳遞。
try_files $uri /$uri /index.html;
```

## 範例

### 從 HTTP 重定向到 HTTPS

```nginx
server {
    listen 80 default_server;
    server_name _;
    return 301 https://$host$request_uri;
}
```

# 紀錄檔 log

```
放在nginx.conf裡面 設定的格式如下

access_log path [format [buffer=size | off]]

預設的combined,並且日誌記錄是存放在/var/log/nginx/nginx.log.
```

# 即時監控 - 啟用 stub_status 模組

```bash
# 雖然 stub_status 是 Nginx 內建的模組，但是 Nginx 在編譯時並不會自動把它納入
# 檢查 Nginx 編譯版本是否有納入這個功能
# 輸出有看到 with-http_stub_status_module 就表示沒問題
nginx -V 2>&1 | grep -o with-http_stub_status_module
```

```conf
server {

  # ...

  location /nginx_status {
    # 啟用 stub_status 模組
    stub_status on;

    # 關閉紀錄功能
    access_log off;

    # 限制可存取的 IP 位址
    allow 127.0.0.1;
    deny all;
  }
}


; 除了使用 IP 位址的方式來限制存取之外，也可以使用帳號與密碼的方式：
server {

  # ...

  location /nginx_status {
    # 啟用 stub_status 模組
    stub_status on;

    # 關閉紀錄功能
    access_log off;

    # 設定帳號與密碼
    auth_basic "closed site";
    auth_basic_user_file /path/to/htpasswd;
  }
}
```

```
nginx status詳解

active connections – 活躍的連接數量

server accepts handled requests — 處理連接數量 , 成功創建握手次數, 處理請求次數

reading — 讀取客戶端的連接數

writing — 響應數據到客戶端的數量

waiting — 開啟 keep-alive 的情況下,這個值等於 active – (reading+writing), 意思就是 Nginx 已經處理完正在等候下一次請求指令的駐留連接.
```

# 例外處理

## nginx: [warn] could not build optimal server_names_hash, you should increase either server_names_hash_max_size: 1024 or server_names_hash_bucket_size: 128; ignoring server_names_hash_bucket_size

```
nginx -t 有出現此警告
```

```
nginx: [warn] could not build optimal server_names_hash, you should increase either server_names_hash_max_size: 1024 or server_names_hash_bucket_size: 128; ignoring server_names_hash_bucket_size
nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration file /etc/nginx/nginx.conf test is successful
```

`修改 /etc/nginx/nginx.conf`

```conf
; /etc/nginx/nginx.conf
server_names_hash_max_size = 2048
server_names_hash_bucket_size = 256
```

## Cannot allocate memory

```bash
# 查看記憶體 還剩多少
free -m
```

`/etc/nginx/nginx.conf`

```conf
; 把上邊的500m改成free內存的數量即可，但是也不要一點不剩，這裡改成了50m
fastcgi_cache_path /data/www/wordpress/fastcgi/ngx_fcgi_cache levels=2:2 keys_zone=ngx_fcgi_cache:500m inactive=30s max_size=5g;
```

## Nginx 24: Too Many Open Files Error And Solution

```bash
# Linux/UNIX 對文件句柄數和打開文件數設置了軟硬限制。可以使用 ulimit 命令查看這些限制。例如：
su - nginx
# 或者在 Debian 或 Ubuntu Linux 上使用 www-data 帳戶
su - www-data

# 要查看硬值和軟值，請按如下方式發出命令
ulimit -Hn
ulimit -Sn

#
runuser -u nginx -- bash
runuser -u www-data -- bash # For Debian/Ubuntu
## OR ##
runuser -u nginx -- sh
```

```bash
# 另一種選擇是使用 cat 命令和 grep 命令，如下所示，
# 使用名為 /var/run/nginx.pid 或 /var/run/nginx/nginx.pid 的 nginx pid 文件（文件名可能會根據 Linux/Unix 變體更改）
cat /proc/$(cat /var/run/nginx.pid)/limits
## OR ##
grep -i 'Max open files' /proc/$(cat /var/run/nginx.pid)/limits

# 方法 1 – 在 Linux 操作系統級別增加 Open FD 限制（沒有 systemd）
# 操作系統對 nginx 服務器可以打開的文件數量進行了限制。
# 可以通過在 Linux 下設置或增加系統打開文件限制來輕鬆解決此問題。
# 編輯文件/etc/sysctl.conf 添加 fs.file-max = 70000
vim /etc/sysctl.conf

# 編輯/etc/security/limits.conf，輸入：
# nginx       soft    nofile   10000
# nginx       hard    nofile  30000
vim /etc/security/limits.conf

# 使用 sysctl 命令重新加載更改
# Making changes to /proc filesystem permanently
# https://www.cyberciti.biz/faq/making-changes-to-proc-filesystem-permanently/
sysctl -p

# 方法 2 – 使用 systemd 在 Linux 操作系統級別增加 Open FD 限制
# 編輯或創建一個新文件 /etc/systemd/system/nginx.service.d/override.conf
# 根據需要替換 4096，例如 65535
# [Service]
# LimitNOFILE=65535
systemctl edit nginx.service

# 使用 systemctl 命令重新加載 systemd 的磁盤更改
systemctl daemon-reload

# nginx worker_rlimit_nofile Option (Increase Open FD Limit at the Nginx Level)
# nginx worker_rlimit_nofile 選項（在 Nginx 級別增加 Open FD 限制）
vi /usr/local/nginx/conf/nginx.conf
## OR ##
vi /etc/nginx/nginx.conf

# 範例
# events {
#     worker_connections 65535; #vg
#     multi_accept on; #vg
# }

# server {
#     worker_connections 65535; #vg
#     multi_accept on; #vg
# }


# 重新加載 nginx 網絡服務器。根據 Linux 下的 init 使用 systemctl 命令或服務命令。
systemctl restart nginx #< SYSTEMD
service nginx restart #< SYS V init

# 再次測試新的 FD 限制：
su - nginx
## OR ##
runuser -u nginx -- bash
ulimit -Hn
ulimit -Sn

# 或者使用 grep 命令：
grep -i 'Max open files' /proc/$(cat /var/run/nginx.pid)/limits

# 或者對於其他發行版，例如 Alpine Linux：
grep -i 'Max open files' /proc/$(cat /var/run/nginx/nginx.pid)/limits
```