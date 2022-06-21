# Nginx conf php 範例

```nginx
server {
    index index.php index.html;
    server_name php-docker.local;
    error_log /var/log/nginx/error.log;
    access_log /var/log/nginx/access.log;
	# 工作目錄
    root /code;

	# php
    location ~ \.php$ {
        try_files $uri = 404;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_pass {php-fpm 伺服器位置}:9000;
        fastcgi_index index.php;
        include fastcgi_params;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        fastcgi_param PATH_INFO $fastcgi_path_info;
    }

	# laravel添加
	location / {
		try_files $uri $uri/ /index.php?$query_string;
	}
}
```

# php laravel nginx 官方推薦配置

[參考文章](https://learnku.com/articles/25861)

```nginx
server {
    # 監聽 HTTP 協議默認的 [80] 端口。
    listen 80;
    # 綁定主機名 [example.com]。
    server_name example.com;
    # 服務器站點根目錄 [/example.com/public]。
    root /example.com/public;

    # 添加幾條有關安全的響應頭；與 Google+ 的配置類似，詳情參見文末。
    add_header X-Frame-Options "SAMEORIGIN";
    add_header X-XSS-Protection "1; mode=block";
    add_header X-Content-Type-Options "nosniff";

    # 站點默認頁面；可指定多個，將順序查找。
    # 例如，訪問 http://example.com/ Nginx 將首先嘗試「站點根目錄/index.html」是否存在，不存在則繼續嘗試「站點根目錄/index.htm」，以此類推...
    index index.html index.htm index.php;

    # 指定字符集為 UTF-8
    charset utf-8;

    # Laravel 默認重寫規則；刪除將導致 Laravel 路由失效且 Nginx 響應 404。
    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    # 關閉 [/favicon.ico] 和 [/robots.txt] 的訪問日誌。
    # 並且即使它們不存在，也不寫入錯誤日誌。
    location = /favicon.ico { access_log off; log_not_found off; }
    location = /robots.txt  { access_log off; log_not_found off; }

    # 將 [404] 錯誤交給 [/index.php] 處理，表示由 Laravel 渲染美觀的錯誤頁面。
    error_page 404 /index.php;

    # URI 符合正則表達式 [\.php$] 的請求將進入此段配置
    location ~ \.php$ {
        # 配置 FastCGI 服務地址，可以為 IP:端口，也可以為 Unix socket。
        fastcgi_pass unix:/var/run/php/php7.2-fpm.sock;
        # 配置 FastCGI 的主頁為 index.php。
        fastcgi_index index.php;
        # 配置 FastCGI 參數 SCRIPT_FILENAME 為 $realpath_root$fastcgi_script_name。
        fastcgi_param SCRIPT_FILENAME $realpath_root$fastcgi_script_name;
        # 引用更多默認的 FastCGI 參數。
        include fastcgi_params;
    }
    # 通俗地說，以上配置將所有 URI 以 .php 結尾的請求，全部交給 PHP-FPM 處理。

    # 除符合正則表達式 [/\.(?!well-known).*] 之外的 URI，全部拒絕訪問
    # 也就是說，拒絕公開以 [.] 開頭的目錄，[.well-known] 除外
    location ~ /\.(?!well-known).* {
        deny all;
    }
}
```

`real_ip_header http頭解釋`

[CF-Connecting-IP](https://stackoverflow.com/questions/65525253/does-cloudflare-cf-connecting-ip-affect-http-header)

```http
X-Forwarded-For：
位於HTTP請求頭，是HTTP的擴充套件 header，用於表示HTTP請求端 真實IP 。

Remote Address：
HTTP協議沒有IP的概念， Remote Address 來自於TCP連線，表示與服務端建立TCP連線的裝置IP，因此，Remote Address無法偽造。

X-Real-IP：
HTTP代理用於表示與它產生TCP連線的裝置IP，可能是其他代理，也可能是真正的請求端。
```

# nginx 配置php / pathinfo 模式/ php 多版本服務

`/etc/nginx/pathinfo.conf`

解析並設定PATH_INFO

```nginx
fastcgi_split_path_info ^(.+?\.php)(/.*)$ ;
set  $path_info  $fastcgi_path_info ;
fastcgi_param PATH_INFO $path_info ;
fastcgi_param SCRIPT_NAME $fastcgi_script_name ;
fastcgi_param SCRIPT_FILENAME $document_root $fastcgi_script_name ;
```

`/etc/nginx/fastcgi.conf`

環境變量

```nginx
fastcgi_param  SCRIPT_FILENAME    $document_root$fastcgi_script_name;
fastcgi_param  QUERY_STRING       $query_string;
fastcgi_param  REQUEST_METHOD     $request_method;
fastcgi_param  CONTENT_TYPE       $content_type;
fastcgi_param  CONTENT_LENGTH     $content_length;

fastcgi_param  SCRIPT_NAME        $fastcgi_script_name;
fastcgi_param  REQUEST_URI        $request_uri;
fastcgi_param  DOCUMENT_URI       $document_uri;
fastcgi_param  DOCUMENT_ROOT      $document_root;
fastcgi_param  SERVER_PROTOCOL    $server_protocol;
fastcgi_param  REQUEST_SCHEME     $scheme;
fastcgi_param  HTTPS              $https if_not_empty;

fastcgi_param  GATEWAY_INTERFACE  CGI/1.1;
fastcgi_param  SERVER_SOFTWARE    nginx/$nginx_version;

fastcgi_param  REMOTE_ADDR        $remote_addr;
fastcgi_param  REMOTE_PORT        $remote_port;
fastcgi_param  SERVER_ADDR        $server_addr;
fastcgi_param  SERVER_PORT        $server_port;
fastcgi_param  SERVER_NAME        $server_name;

# PHP only, required if PHP was built with --enable-force-cgi-redirect
fastcgi_param  REDIRECT_STATUS    200;
fastcgi_param PHP_ADMIN_VALUE "open_basedir=$document_root/:/tmp/:/proc/";
```

## pathinfo 模式

`/etc/nginx/enable-php.conf`

只啟用 php 不開啟pathinfo

```nginx
location ~ [^/]\.php(/|$)
{
    # try_files $uri =404;
    # fastcgi_pass  unix:/tmp/php-cgi.sock;
    fastcgi_pass  127.0.0.0:9000;
    fastcgi_index index.php;
    include fastcgi.conf;
}
```

`/etc/nginx/enable-php-pathinfo.conf`

啟用 php 並開啟pathinfo

```nginx
location ~ [^/]\.php(/|$)
{
    # try_files $uri =404; pathinfo 下绝对是 404
    # fastcgi_pass  unix:/tmp/php-cgi.sock;
    fastcgi_pass  127.0.0.0:9000;
    fastcgi_index index.php;
    include pathinfo.conf;
    include fastcgi.conf;
}
```

```
location / {
    # dispatch static/dynamic request and hidde index.php
	# 不想讓別人知道你使用的服務端語言是什麼
    try_files $uri $uri/ /youwantkonwlanginmyserverhahahnoway.php?$query_string =404;
}
```

```
server {
    listen 80 default_server reuseport;
    #listen [::]:80 default_server ipv6only=on;
    server_name _;
    index index.html index.htm index.php;
    root  /home/wwwroot/default;

    location / {
        # thinkphp 兼容模式
        # last 使用重写后的 url 再一次进入 location 解析
        # break 直接返回重写后的 url 对应的资源
        #if (!-e $request_filename){
        #    rewrite  ^(.*)$  /index.php?s=$1  last;
        #}

        # dispatch static/dynamic request and hidde index.php
        # 隐藏 index.php /
        try_files $uri $uri/ /index.php$is_args$query_string =404;
        # 隐藏 index.php 并开启 pathinfo
        try_files $uri $uri/ /index.php$uri?$query_string;
    }

    location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$ {
        expires 30d;
    }

    location ~ .*\.(js|css)?$ {
        expires 12h;
    }

    location ~ /.well-known {
        allow all;
    }

    location ~ /\. {
        deny all;
    }

    # 防止上传 web-shell your documentRoot's directory
    location ~ /(wp-content|uploads|wp-includes|images)/.*\.php$ { deny all; }

    # 选择要启用的模式
    # include enable-php.conf
    include enable-php-pathinfo.conf

    access_log  /home/wwwlogs/access.log;
    error_log   /home/wwwlogs/error.log;
}
```

# php 多版本服務

`enable-php74-pathinfo.conf`

```nginx
location ~ [^/]\.php(/|$)
{
    # try_files $uri =404;
    fastcgi_pass  127.0.0.0:9074;
    fastcgi_index index.php;
    include pathinfo.conf;
    include fastcgi.conf;
}
```

`enable-php56-pathinfo.conf`

```nginx
location ~ [^/]\.php(/|$)
{
    # try_files $uri =404;
    fastcgi_pass  127.0.0.0:9056;
    fastcgi_index index.php;
    include pathinfo.conf;
    include fastcgi.conf;
}
```

`enable-php53-pathinfo.conf`

```nginx
location ~ [^/]\.php(/|$)
{
    # try_files $uri =404;
    fastcgi_pass  127.0.0.0:9053;
    fastcgi_index index.php;
    include pathinfo.conf;
    include fastcgi.conf;
}
```