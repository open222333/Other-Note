# Nginx conf 多網站 範例

```nginx
# /etc/nginx/nginx.conf
user nginx nginx;
worker_processes auto;

# pid進程文件
pid /var/run/nginx.pid;

# 工作進程最大打開文件數( RLIMIT_NOFILE)的限制
worker_rlimit_nofile 65535;

# 載入模組
load_module modules/ngx_http_subs_filter_module.so;
load_module modules/ngx_http_image_filter_module.so;
load_module modules/ngx_http_geoip_module.so;
load_module modules/ngx_stream_geoip_module.so;
# load_module modules/ngx_http_headers_more_filter_module.so;

events {
	# 使用 epoll模組
	use epoll;
	worker_connections 65535;
}

http {
	include /etc/nginx/mime.types;
	default_type application/octet-stream;


	limit_req_zone $http_x_forwarded_for zone=one:10m rate=1r/s;

	# 日誌格式
	log_format main '$remote_addr - "$server_name" $remote_user [$time_local] "$request"'
	'$status $body_bytes_sent "$http_referer" '
	'"$http_user_agent"';


	error_page 403 /403.html;
        server_names_hash_max_size 1024;

	add_header Cache-Control 'private, no-store, max-age=0';

	# access_log  /var/log/nginx/access.log  main;
	# error_log  /var/log/nginx/error.log notice;

	geo $purge_allowed {
		default 0; # deny from other
		127.0.0.1 1; # deny from other
	}

	map $request_method $purge_method {
		PURGE $purge_allowed;
		default 0;
	}
	geoip_country /usr/share/GeoIP/GeoIP.dat;
	geoip_city /usr/share/GeoIP/GeoIPCity.dat;
	geoip_proxy_recursive on;

	map $geoip_country_code $images_nearest_server {
		default g;
		CN      g;
	}

	map $geoip_country_code $css_nearest_server {
		default g;
		CN cn;
	}

	map $geoip_country_code $js_nearest_server {
		default g;
		CN cn;
	}

	geo $remote_addr $ip_whitelist {
		default 0;
		127.0.0.1 1;
	}

	# geoip_country /usr/share/GeoIP/GeoLite2-Country_20181225/GeoLite2-Country.dat;
	# geoip_city    /usr/share/GeoIP/GeoLite2-City_20181225/GeoLite2-City.dat;

	# 設置緩存的路徑和其他參數。緩存數據存儲在文件中。
	proxy_cache_path /data/nginx/cache levels=1:2 keys_zone=dh_seo_sites_cache:1024m max_size=100g
	inactive=1h use_temp_path=off;

	map $http_user_agent $seo_bots {
		default 0;
		~*(Googlebot|Slurp|DuckDuckBot|Baiduspider|YandexBot) 1;
		~*(Bingbot|bingbot|msnbot|msnbot-media) 1;
		~*(Yisouspider|360Spider|Sosospider|YoudaoBot|YodaoBot) 1;
		~*(Sogou) 1;
	}

	charset utf-8;
	server_names_hash_bucket_size 128;
	client_header_buffer_size 64k;
	large_client_header_buffers 4 32k;
	client_max_body_size 20M;
	sendfile on;
	tcp_nopush on;
	server_tokens off;
	proxy_buffer_size 128k;
	proxy_buffers 32 256k;
	proxy_busy_buffers_size 256k;


	keepalive_timeout 200;

	gzip on;
	gzip_comp_level 9;
	gzip_min_length 10k;
	gzip_buffers 4 64k;
	gzip_http_version 1.1;
	gzip_proxied any;
	gzip_vary on;
	gzip_types text/plain text/css application/xml application/x-httpd-php
	application/javascript application/x-javascript application/json text/javascript
	image/jpeg image/gif image/png;

	tcp_nodelay on;

	resolver 8.8.8.8 ipv6=off;

	# 依據 GET 參數的 w & h 決定 圖片的寬度
	map $arg_w:$arg_h $imgs_width {
		~^[0-9]+:.*$ $arg_w;
		~^\s*:.*$ -;
		default -;
		volatile;
	}

	# 依據 GET 參數的 h & w 決定 圖片的高度
	map $arg_h:$arg_w $imgs_height {
		~^[0-9]+:(?:\s*|(?!\d+$).+)$ $arg_h;
		~^[0-9]+:[0-9]+$ -;
		~^\s*:.*$ -;
		default -;
		volatile;
	}

	# live stream forward
	map $arg_origin_livestream $origin_livestream {
		~.*$ $arg_origin_livestream;

		default 'test.com';
	}

	map $arg_stream_server_id $stream_server_id {
		~^[1-2]+:.*$ $arg_stream_server_id;
	}

	map $arg_nw_ip $nw_ip {
		1 127.0.0.1;
		2 127.0.0.1;
		3 127.0.0.1;
	}

	include /etc/nginx/map.conf;
	include /etc/nginx/realip.conf;
	include /etc/nginx/upstream.conf;
	include /etc/nginx/conf.d/*.conf;
}
```

```nginx
# /etc/nginx/conf.d/default.conf
server {
	status_zone default;
	listen 81;
	server_name localhost;

	location / {
		root /usr/share/nginx/html;
		index index.html index.htm;
	}

	# error_page 404 /404.html;

	# redirect server error pages to the static page /50x.html
	error_page 500 502 503 504 /50x.html;
	location = /50x.html {
		root /usr/share/nginx/html;
	}

	location ~ /\.git {
		deny all;
	}
}
```

```nginx
# /etc/nginx/conf.d/test.com.conf
server {
	status_zone test.com;
	listen 443 ssl http2;
	listen 80;
	server_name test.com www.test.com admin.test.com;

	# SSL 證書位置
	# certbot 產生的證書
	ssl_certificate /etc/letsencrypt/live/test.com/fullchain.pem;
	# certbot 產生的私鑰
	ssl_certificate_key /etc/letsencrypt/live/test.com/privkey.pem;


	location / {
		access_log off;
		# 轉址 源站 http://host:port
		proxy_pass https://host:port;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header Host $http_host;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	}

	location ~ /\.git {
		deny all;
	}
}
```