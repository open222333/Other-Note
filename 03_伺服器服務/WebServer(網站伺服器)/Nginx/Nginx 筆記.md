# Nginx 筆記

## 參考資料

[官方文檔](http://nginx.org/en/docs/)

[中文文檔](https://www.docs4dev.com/docs/zh/nginx/current/reference/)

[DevOps：如何將 Nginx + uWSGI + Django 的服務部署在一台主機的根目錄](https://orcahmlee.github.io/devops/nginx-uwsgi-django-root/)


# 基本指令

```bash
### macOS ###
# 安裝
brew install nginx

# 重啟
brew services restart nginx

### CentOS 7 ###
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

# nginx conf 設定檔說明

[nginx documentation](http://nginx.org/en/docs/)

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
