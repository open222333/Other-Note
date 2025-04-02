# PHP Laravel 筆記

## 目錄

- [PHP Laravel 筆記](#php-laravel-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
    - [安裝相關](#安裝相關)
- [安裝](#安裝)
  - [安裝 composer](#安裝-composer)
  - [docker-compose 部署](#docker-compose-部署)
- [配置文檔](#配置文檔)
  - [.env.example](#envexample)
  - [nginx 設定檔](#nginx-設定檔)
- [指令](#指令)
  - [創建專案(基本起始步驟)](#創建專案基本起始步驟)
  - [遷移資料庫](#遷移資料庫)
  - [自定義指令](#自定義指令)
    - [建立自定義指令](#建立自定義指令)
    - [執行指令](#執行指令)
  - [排程](#排程)
    - [測試特定任務](#測試特定任務)
    - [立即執行所有排程](#立即執行所有排程)
    - [檢視所有排程](#檢視所有排程)
- [用法](#用法)
  - [自定義指令](#自定義指令-1)
  - [設定任務排程](#設定任務排程)
    - [設定任務排程的頻率](#設定任務排程的頻率)
    - [直接執行匿名函數](#直接執行匿名函數)
    - [指定環境](#指定環境)
    - [防止任務重複執行](#防止任務重複執行)
    - [設定錯誤通知](#設定錯誤通知)
- [Laravel 技巧](#laravel-技巧)
  - [由資源控制器處理的行為](#由資源控制器處理的行為)
  - [開啟debug](#開啟debug)
  - [Setting a foreign key bigInteger to bigIncrements](#setting-a-foreign-key-biginteger-to-bigincrements)
  - [命令](#命令)
  - [任務排程](#任務排程)

## 參考資料

```
免費的開源 PHP Web 框架，旨在實作的Web軟體的MVC架構
```

[packagist - Composer 官方](https://packagist.org/packages/laravel/laravel)

[Command Bus](https://laravel.tw/docs/5.0/bus#creating-commands)

[Artisan 指令列](https://laravel.tw/docs/5.2/artisan#writing-commands)

[中文官網](https://laravel.tw/)

[Laravel中文文檔](https://laravel.tw/docs/5.0)

[Laravel官方文檔](https://laravel.com/docs/9.x)

[Laravel guide](https://laravel-guide.readthedocs.io/en/latest/)

[Laravel API文檔](https://laravel.com/api/9.x/)

[目錄結構](https://laravel.tw/docs/5.3/structure#the-root-app-directory)

[Eloquent ORM(資料庫)](https://laravel.tw/docs/5.0/eloquent)

[Laravel 資料庫 遷移](https://laravel.tw/docs/5.2/migrations)

### 心得相關

[laravel Youtube教程](https://www.youtube.com/watch?v=EU7PRmCpx-0&t=1s&ab_channel=TraversyMedia)

### 安裝相關

[Deploying Laravel, Nginx, and MySQL with Docker Compose - 使用 docker-compose 架設 laravel環境](https://www.cloudsigma.com/deploying-laravel-nginx-and-mysql-with-docker-compose/)

[Laravel Development Setup with Docker Compose](https://docs.docker.com/guides/frameworks/laravel/development-setup/)

# 安裝

## 安裝 composer

```bash
# 下載compser安裝檔
php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"

# 安裝composer(PHP軟體包管理)
php composer-setup.php

# 安裝composer.json內紀錄的框架所需套件
composer install

# 建立指令
sudo mv composer.phar {/usr/bin/composer}

# 移除compser安裝檔
php -r "unlink('composer-setup.php');"

# 觀察系統所有的程序資料
ps uax
```

## docker-compose 部署

```yml
version: '3'
services:
    nginx:
        container_name: nginx
        image: nginx:1.10
        ports:
            - 80:80
        volumes:
            - ./docker/conf/nginx:/etc/nginx/conf.d
            - ./local_project_path:/usr/src/project
    php-fpm:
        container_name: php-fpm
        image: php:7.4.3-fpm
        volumes:
            - ./local_project_path:/usr/src/project
    mysql57:
        container_name: mysql57
        hostname: mysql-container
        image: mysql:5.7
        ports:
            - 3306:3306
        environment:
            MYSQL_ROOT_PASSWORD: password
        volumes:
            - ./conf/mysql57:/etc/mysql2/conf.d
    phpmyadmin:
        container_name: phpmyadmin
        hostname: phpmyadmin-container
        image: phpmyadmin/phpmyadmin
        volumes:
            - ./conf/phpmyadmin/config.user.inc.php:/etc/phpmyadmin/config.user.inc.php
        ports:
            - 8080:80
        environment:
            PMA_HOST: mysql57
            PMA_PORT: 3306
            PMA_USER: root
            PMA_PASSWORD: root
            MYSQL_ROOT_PASSWORD: password
            UPLOAD_LIMIT: '100M'
        depends_on:
            - mysql57
```

# 配置文檔

## .env.example

```env
APP_NAME=Laravel
APP_ENV=local
APP_KEY=
APP_DEBUG=true
APP_URL=http://localhost

LOG_CHANNEL=stack
LOG_DEPRECATIONS_CHANNEL=null
LOG_LEVEL=debug

DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=laravel
DB_USERNAME=root
DB_PASSWORD=

BROADCAST_DRIVER=log
CACHE_DRIVER=file
FILESYSTEM_DRIVER=local
QUEUE_CONNECTION=sync
SESSION_DRIVER=file
SESSION_LIFETIME=120

REDIS_HOST=127.0.0.1
REDIS_PASSWORD=null
REDIS_PORT=6379

MAIL_MAILER=smtp
MAIL_HOST=mailhog
MAIL_PORT=1025
MAIL_USERNAME=null
MAIL_PASSWORD=null
MAIL_ENCRYPTION=null
MAIL_FROM_ADDRESS=null
MAIL_FROM_NAME="${APP_NAME}"

AWS_ACCESS_KEY_ID=
AWS_SECRET_ACCESS_KEY=
AWS_DEFAULT_REGION=us-east-1
AWS_BUCKET=

PUSHER_APP_ID=
PUSHER_APP_KEY=
PUSHER_APP_SECRET=
PUSHER_HOST=
PUSHER_PORT=443
PUSHER_SCHEME=https
PUSHER_APP_CLUSTER=mt1

MIX_PUSHER_APP_KEY="${PUSHER_APP_KEY}"
MIX_PUSHER_APP_CLUSTER="${PUSHER_APP_CLUSTER}"
```

```
基本應用設置
APP_NAME：應用的名稱。
APP_ENV：應用的運行環境（如 local, production）。
APP_KEY：應用的加密密鑰，用於加密數據，使用 php artisan key:generate 生成。
APP_DEBUG：是否啟用調試模式（true 或 false）。
APP_URL：應用的基礎 URL。

日誌配置
LOG_CHANNEL：日誌頻道。
LOG_DEPRECATIONS_CHANNEL：棄用警告的日誌頻道。
LOG_LEVEL：日誌級別。

數據庫配置
DB_CONNECTION：數據庫連接類型（如 mysql, pgsql）。
DB_HOST：數據庫伺服器地址。
DB_PORT：數據庫伺服器端口。
DB_DATABASE：數據庫名稱。
DB_USERNAME：數據庫用戶名。
DB_PASSWORD：數據庫密碼。

緩存和隊列配置
BROADCAST_DRIVER：廣播驅動（如 log, pusher）。
CACHE_DRIVER：緩存驅動（如 file, redis）。
FILESYSTEM_DRIVER：文件系統驅動（如 local, s3）。
QUEUE_CONNECTION：隊列連接（如 sync, database）。
SESSION_DRIVER：會話驅動（如 file, cookie）。
SESSION_LIFETIME：會話生命周期（分鐘）。

Redis 配置
REDIS_HOST：Redis 伺服器地址。
REDIS_PASSWORD：Redis 密碼（如果有）。
REDIS_PORT：Redis 伺服器端口。

郵件配置
MAIL_MAILER：郵件發送驅動（如 smtp, sendmail）。
MAIL_HOST：郵件伺服器地址。
MAIL_PORT：郵件伺服器端口。
MAIL_USERNAME：郵件伺服器用戶名。
MAIL_PASSWORD：郵件伺服器密碼。
MAIL_ENCRYPTION：郵件加密方式（如 tls, ssl）。
MAIL_FROM_ADDRESS：郵件發送地址。
MAIL_FROM_NAME：郵件發送名稱。

AWS 配置
AWS_ACCESS_KEY_ID：AWS 訪問密鑰 ID。
AWS_SECRET_ACCESS_KEY：AWS 秘密訪問密鑰。
AWS_DEFAULT_REGION：AWS 預設區域。
AWS_BUCKET：S3 存儲桶名稱。

Pusher 配置
PUSHER_APP_ID：Pusher 應用 ID。
PUSHER_APP_KEY：Pusher 應用密鑰。
PUSHER_APP_SECRET：Pusher 應用密碼。
PUSHER_HOST：Pusher 伺服器地址。
PUSHER_PORT：Pusher 伺服器端口。
PUSHER_SCHEME：Pusher 連接方案（如 http, https）。
PUSHER_APP_CLUSTER：Pusher 應用集群。

前端混合（Mix）配置
MIX_PUSHER_APP_KEY：前端使用的 Pusher 應用密鑰。
MIX_PUSHER_APP_CLUSTER：前端使用的 Pusher 應用集群。
```

## nginx 設定檔

```
server {
    listen 80;
    listen [::]:80;
    server_name example.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;

    add_header Cache-Control no-cache;

    index index.php index.html;
    server_name example.com;

    error_log /var/log/nginx/example_error.log;
    access_log /var/log/nginx/example_access.log;

    ssl_certificate /etc/letsencrypt/live/iavnight.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/iavnight.com/privkey.pem;

    root /usr/share/nginx/html/example.com/public;
    include allow_ip.conf;

    set $app_debug false;

    if ($is_ip_whitelist){
        set $app_debug true;
    }

    location ~ \.php$ {
        try_files $uri = 404;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_pass 127.0.0.1:9000;
        fastcgi_index index.php;
        include fastcgi_params;
        fastcgi_param APP_DEBUG $app_debug;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        fastcgi_param PATH_INFO $fastcgi_path_info;
    }

    location / {
    	try_files $uri $uri/ /index.php?$query_string;
    }
}
```

```conf
server {
    listen 80;
    server_name your_domain.com; # Replace with your domain or IP address

    root /path/to/laravel/public; # Replace with the path to your Laravel project's public directory

    index index.php index.html index.htm;

    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \.php$ {
        include snippets/fastcgi-php.conf;
        fastcgi_pass unix:/var/run/php/php7.4-fpm.sock; # Replace with your PHP-FPM version and path
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include fastcgi_params;
    }

    location ~ /\.ht {
        deny all;
    }

    error_log /var/log/nginx/laravel_error.log;
    access_log /var/log/nginx/laravel_access.log;
}

server {
    listen 443 ssl;
    server_name your_domain.com; # Replace with your domain or IP address

    ssl_certificate /etc/nginx/ssl/your_domain.com.crt; # Replace with the path to your SSL certificate
    ssl_certificate_key /etc/nginx/ssl/your_domain.com.key; # Replace with the path to your SSL certificate key
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    root /path/to/laravel/public; # Replace with the path to your Laravel project's public directory

    index index.php index.html index.htm;

    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \.php$ {
        include snippets/fastcgi-php.conf;
        fastcgi_pass unix:/var/run/php/php7.4-fpm.sock; # Replace with your PHP-FPM version and path
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include fastcgi_params;
    }

    location ~ /\.ht {
        deny all;
    }

    error_log /var/log/nginx/laravel_error.log;
    access_log /var/log/nginx/laravel_access.log;
}
```

# 指令

查看 laravel/laravel 包的可用版本

```bash
composer show laravel/laravel --all | grep versions
```

## 創建專案(基本起始步驟)

```bash
# 創建laravel專案, 使用 Composer 創建一個新的 Laravel 專案
composer create-project --prefer-dist laravel/laravel your_project_name version
composer create-project --prefer-dist laravel/laravel  "Laravel"  6.*

# 環境文件：將 .env.example 文件複製為 .env，並配置你的環境變量
cp .env.example .env

# 生成應用程序密鑰：生成一個新的應用程序密鑰，用於加密。
php artisan key:generate

# storage & cache給予寫入權限 文件權限：確保 storage 和 bootstrap/cache 目錄可寫。
chmod -R 755 "project_name"/storage
chmod -R 755 "project_name"/bootstrap/cache

chown -R www-data:www-data /path/to/your_project_name/storage /path/to/your_project_name/bootstrap/cache
chmod -R 775 /path/to/your_project_name/storage /path/to/your_project_name/bootstrap/cache

# 遷移數據庫
php artisan migrate

# 使用 Artisan 提供的內建 PHP 伺服器
php artisan serve
```

生產環境 配置 Nginx

```conf
server {
    listen 80;
    server_name your_domain.com; # 替換為你的域名或 IP 地址

    root /path/to/your_project_name/public; # 替換為你的 Laravel 專案 public 目錄的路徑

    index index.php index.html index.htm;

    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \.php$ {
        include snippets/fastcgi-php.conf;
        fastcgi_pass unix:/var/run/php/php7.4-fpm.sock; # 替換為你的 PHP-FPM 版本和路徑
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include fastcgi_params;
    }

    location ~ /\.ht {
        deny all;
    }

    error_log /var/log/nginx/laravel_error.log;
    access_log /var/log/nginx/laravel_access.log;
}
```

```bash
sudo ln -s /etc/nginx/sites-available/your_project_name /etc/nginx/sites-enabled/
```

## 遷移資料庫

```bash
### 資料庫: 遷移 migrations ###
# 遷移資料庫
# 用於 Laravel（一個 PHP 框架）的命令，用來執行資料庫遷移。
# 遷移是一種以版本控制方式定義和管理資料庫結構的方法，使能夠修改和共享應用程式的資料庫結構定義。
# 注意資料庫是否已存在
php artisan migrate
# 建立遷移 預設路徑 database/migrations/{$datetime_now}_{$name}.php
# 會在 database/migrations 目錄中建立一個新的遷移檔。
php artisan make:migration create_users_table
php artisan make:migration $name
	--table
		指定資料表的名稱
	--create
		遷移會建立新的資料表
```

打開新建立的遷移檔，並定義表格的結構。

```php
<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateUsersTable extends Migration
{
    /**
     * 執行遷移。
     *
     * @return void
     */
    public function up()
    {
        Schema::create('users', function (Blueprint $table) {
            $table->id();
            $table->string('name');
            $table->string('email')->unique();
            $table->timestamp('email_verified_at')->nullable();
            $table->string('password');
            $table->rememberToken();
            $table->timestamps();
        });
    }

    /**
     * 回滾遷移。
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('users');
    }
}
```

```bash
# 執行所有未完成的遷移
php artisan migrate
	--force
		強制執行遷移

# rollback 「回滾」指的是撤銷或還原之前已經執行的遷移操作。這通常用於恢復資料庫到遷移之前的狀態。
# 還原遷移至上一個「操作」 批次
php artisan migrate:rollback
# 指定要回滾多少批次的遷移
php artisan migrate:rollback --step=2


# 回滾所有遷移
php artisan migrate:reset

# 回滾並重新執行所有遷移 refresh
# 首先會還原資料庫的所有遷移，接著再執行 migrate 指令。
# 此指令能有效的重新建立整個資料庫。
php artisan migrate:refresh

# 回滾並重新執行所有遷移 運行 /database/seeds/DatabaseSeeder.php 和其中定義的所有 Seeder
php artisan migrate:refresh --seed
# 指定要回滾並重新執行的批次數
php artisan migrate:refresh --step=2

### Laravel 基本設置 ###
# 透過artisan產生一組網站專屬密鑰
php artisan key:generate

# 部署網站相關設定
# 相關的參數設定緩存在單一文件(bootstrap/cache/config.php)
php artisan config:cache

# 清空 參數設定緩存
php artisan config:clear

# 清空所有快取
php artisan optimize:clear

### 測試 操作 ###
# php內置 web環境
php artisan serve

# 檢視所有有效的指令
php artisan list make 指令

### 操作 ###
### --help 看詳細說明

# 建立 controller 預設路徑 app/Http/Controllers/{$controller_name}.php
php artisan make:controller {$controller_name}

# 建立 model 預設路徑 app/{$model_name}.php
php artisan make:model $model_name
```

## 自定義指令

### 建立自定義指令

```sh
# 建立 command 預設路徑 app/Console/Commands/{$command_name}.php
# 建立自定義指令 生成一個自定義的 Artisan 指令
php artisan make:command MyCustomCommand
php artisan make:command $command_name
```

### 執行指令

```sh
php artisan custom:run
```

## 排程

### 測試特定任務

```sh
php artisan schedule:test
```

### 立即執行所有排程

```sh
php artisan schedule:run
```

### 檢視所有排程

```sh
php artisan schedule:list
```

# 用法

顯示所有 Artisan 指令

```sh
php artisan list
```

顯示 Artisan 指令詳細資訊

```sh
php artisan help your:command
```

使用 route:list 查看 Artisan 內部結構

```sh
php artisan route:list
```

## 自定義指令

```sh
# 建立 command 預設路徑 app/Console/Commands/{$command_name}.php
# 建立自定義指令 生成一個自定義的 Artisan 指令
php artisan make:command MyCustomCommand
php artisan make:command $command_name
```

```php
namespace App\Console\Commands;

use Illuminate\Console\Command;

class MyCustomCommand extends Command
{
    /**
     * 指令名稱，在 Artisan CLI 中執行的命令
     *
     * @var string
     */
    protected $signature = 'custom:run';

    /**
     * 指令的描述，會顯示在 `php artisan list`
     *
     * @var string
     */
    protected $description = '這是一個自定義的 Artisan 指令';

    /**
     * 指令執行時會執行的邏輯
     */
    public function handle()
    {
        $this->info("Hello! 這是自定義 Artisan 指令！");
    }
}
```

Laravel 會自動偵測 app/Console/Commands 內的指令

但可以手動在 app/Console/Kernel.php 中註冊

```php
protected $commands = [
    \App\Console\Commands\MyCustomCommand::class,
];
```

執行指令

```sh
php artisan custom:run
```

## 設定任務排程

Laravel 的所有排程任務都會被定義在 app/Console/Kernel.php 檔案內的 schedule() 方法。

```php
// custom:run 指令就會 每分鐘 執行一次
protected function schedule(Schedule $schedule)
{
    $schedule->command('custom:run')->everyMinute();
}
```

Laravel 任務排程需要 Crontab 執行 artisan schedule:run

```sh
* * * * * php /path-to-your-project/artisan schedule:run >> /dev/null 2>&1
```

### 設定任務排程的頻率

```
->everyMinute()	每分鐘執行
->everyTwoMinutes()	每 2 分鐘執行
->everyFiveMinutes()	每 5 分鐘執行
->everyTenMinutes()	每 10 分鐘執行
->hourly()	每小時執行
->daily()	每天午夜 00:00 執行
->dailyAt('13:00')	每天 13:00 執行
->weekly()	每週執行一次
->monthly()	每月執行一次
->yearly()	每年執行一次
->mondays()	每週一執行
->weekdays()	週一到週五執行
->weekends()	週六、週日執行
```

### 直接執行匿名函數

```php
protected function schedule(Schedule $schedule)
{
    $schedule->call(function () {
        \Log::info('這是一個 Laravel 任務排程！');
    })->dailyAt('01:00');
}
```

### 指定環境

```php
$schedule->command('custom:run')->daily()->environments(['production']);
```

### 防止任務重複執行

```php
$schedule->command('custom:run')->everyMinute()->withoutOverlapping();
```

### 設定錯誤通知

```php
$schedule->command('custom:run')
    ->daily()
    ->onFailure(function () {
        \Log::error("任務失敗！");
    });
```

# Laravel 技巧

## 由資源控制器處理的行為

動詞 | 路徑 | 行為 | 路由名稱
--- | --- | --- | ---
GET | /test | index | test.index
GET | /test/create | create | test.create
POST | /test | store | test.store
GET | /test/{test} | show | test.show
GET | /test/{test}/edit | edit | test.edit
PUT/PATCH | /test/{test} | update | test.update
DELETE | /test/{test} | destroy | test.destroy

## 開啟debug

修改.env參數

```conf
APP_DEBUG=True
```

或是

```
位置
config -> app.php
```

```php
# 將false改成true
'debug' => env('APP_DEBUG', true),
```

## Setting a foreign key bigInteger to bigIncrements

```
bigIncrements bigInteger
設置 foreign key

當外鍵有參照到自動增量時，記得設定外鍵為 unsigned 型態。
```

[bigincrements foreign key laravel](https://stackoverflow.com/questions/42442498/setting-a-foreign-key-biginteger-to-bigincrements-in-laravel-5-4)

## 命令

預設生成路徑

```
app/Console/Commands
```

```php
<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;

class CommandName extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
	// 命令名稱
    protected $signature = 'command:name';

    /**
     * The console command description.
     *
     * @var string
     */
	// 說明文字
    protected $description = 'Command description';

    /**
     * Create a new command instance.
     *
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Execute the console command.
     *
     * @return mixed
     */
	// Console 執行的程式
    public function handle()
    {
        //
    }
}
```

## 任務排程

[任務排程](https://laravel.tw/docs/5.2/scheduling)

```
可以將所有排定的任務定義在 App\Console\Kernel 類別的 schedule 方法
```

```php
<?php

namespace App\Console;

use DB;
use Illuminate\Console\Scheduling\Schedule;
use Illuminate\Foundation\Console\Kernel as ConsoleKernel;

class Kernel extends ConsoleKernel
{
    /**
     * 你的應用程式提供的 Artisan 指令。
     *
     * @var array
     */
    protected $commands = [
        \App\Console\Commands\Inspire::class,
    ];

    /**
     * 定義應用程式的指令排程。
     *
     * @param  \Illuminate\Console\Scheduling\Schedule  $schedule
     * @return void
     */
    protected function schedule(Schedule $schedule)
    {
        $schedule->call(function () {
            DB::table('recent_users')->delete();
        })->daily();
    }
}
```

```php
// command 方法排定一個 Artisan 指令
$schedule->command('emails:send --force')->daily();

// exec 方法用於發送指令到作業系統
$schedule->exec('node /home/forge/script.js')->daily();
```

`啟動排程器`

```bash
# 查看目前設置
crontab -l

# 編輯設置
crontab -e

# 起動排程(加入到伺服器的 Cron 項目)
* * * * * php /path/to/artisan schedule:run >> /dev/null 2>&1
```
