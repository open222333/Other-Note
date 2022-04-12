# Nginx conf 正式測試分離 範例

`/etc/nginx/conf.d/web.sample.com.conf`

```nginx
# 正式
server {
    listen  80;
    listen  443 ssl http2;
    server_name web.sample.com;

    access_log /var/log/nginx/web.sample.com.log;
    error_log  /var/log/nginx/web.sample.com.error.log;

	# SSL證書
    ssl_certificate /etc/letsencrypt/live/sample.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/sample.com/privkey.pem;

    root /usr/share/nginx/laravel-admin/public;
    index index.html index.htm index.php;

    set $app_debug false;
    if ($is_ip_whitelist){
        set $app_debug true;
    }

    #charset koi8-r;
    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \/$ {
        return 200 OK;
    }

    location ~ /\.git {
        deny all;
    }

    location ~ /admin {
		#
        include allow_ip.conf;

        try_files $uri $uri/ /index.php?$query_string;
        location ~ \.php$ {
            fastcgi_param  APP_URL $scheme://$http_host;
	    fastcgi_param  APP_DEBUG $app_debug;
            fastcgi_pass   127.0.0.1:9000;
            fastcgi_index  index.php;
            fastcgi_param  SCRIPT_FILENAME $document_root$fastcgi_script_name;
            include        fastcgi_params;
        }
    }
	
    location ~ \.php$ {
        #include allow_ip.conf;
        fastcgi_param  APP_URL $scheme://$http_host;
        fastcgi_param  APP_DEBUG $app_debug;
	fastcgi_pass   127.0.0.1:9000;
	fastcgi_index  index.php;
	fastcgi_param  SCRIPT_FILENAME $document_root$fastcgi_script_name;
	include        fastcgi_params;
    }
}
```

`/etc/nginx/conf.d/web-test.sample.com.conf`

```nginx
# 測試
server {
    listen  8081 ssl http2 default_server;
    server_name web-test.sample.com;
    access_log /var/log/nginx/web-test.sample.com.log;
    error_log  /var/log/nginx/web-test.sample.com.error.log;

	# SSL證書
    ssl_certificate /etc/letsencrypt/live/sample.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/sample.com/privkey.pem;

	# 根目錄位置
    root /usr/share/nginx/web-test/public;
	# 首頁預設檔
    index index.html index.htm index.php;

    set $app_debug false;
    if ($is_ip_whitelist){
        set $app_debug true;
    }

    #charset koi8-r;
    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \/$ {
        return 200 OK;
    }

    location ~ /\.git {
        deny all;
    }

    location ~ /admin {
        include allow_ip.conf;
        try_files $uri $uri/ /index.php?$query_string;
        location ~ \.php$ {
            fastcgi_param  APP_DEBUG $app_debug;
            fastcgi_param  APP_URL $scheme://$http_host;
            fastcgi_pass   127.0.0.1:9000;
            fastcgi_index  index.php;
            fastcgi_param  SCRIPT_FILENAME $document_root$fastcgi_script_name;
            include        fastcgi_params;
        }
    }

    location ~ \.php$ {
        include allow_ip.conf;

		# 傳遞給FastCGI 服務器的參數
		fastcgi_param  APP_DEBUG $app_debug;

		# 設置FastCGI 服務器的地址
		# 該地址可以指定為域名或IP 地址以及端口
		# 或作為UNIX 域套接字路徑
		fastcgi_pass   127.0.0.1:9000;

		# 傳遞給FastCGI 服務器的參數
        fastcgi_param  APP_URL $scheme://$http_host;

		fastcgi_index  index.php;

		# 傳遞給FastCGI 服務器的參數
		fastcgi_param  SCRIPT_FILENAME $document_root$fastcgi_script_name;
		include        fastcgi_params;
    }
}
```

`allow_ip.conf`

```nginx
# 設置允許ip
allow 127.0.0.1;

# 拒絕全部
deny all;
```