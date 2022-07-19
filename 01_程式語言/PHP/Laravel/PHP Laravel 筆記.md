# PHP Laravel 筆記

## 目錄

- [PHP Laravel 筆記](#php-laravel-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [使用docker-compose 架設 laravel環境](#使用docker-compose-架設-laravel環境)
	- [安裝步驟](#安裝步驟)
		- [安裝laravel 以及 相關套件](#安裝laravel-以及-相關套件)
	- [.env.example](#envexample)
- [Laravel 指令](#laravel-指令)
	- [由資源控制器處理的行為](#由資源控制器處理的行為)
- [Laravel 技巧](#laravel-技巧)
	- [開啟debug](#開啟debug)
	- [Setting a foreign key bigInteger to bigIncrements](#setting-a-foreign-key-biginteger-to-bigincrements)
	- [命令](#命令)
	- [任務排程](#任務排程)

## 參考資料

```
免費的開源 PHP Web 框架，旨在實作的Web軟體的MVC架構
```

[laravel Youtube教程](https://www.youtube.com/watch?v=EU7PRmCpx-0&t=1s&ab_channel=TraversyMedia)

[目錄結構](https://laravel.tw/docs/5.3/structure#the-root-app-directory)

[中文官網](https://laravel.tw/)

[Laravel中文文檔](https://laravel.tw/docs/5.0)

[Laravel官方文檔](https://laravel.com/docs/9.x)

[Laravel guide](https://laravel-guide.readthedocs.io/en/latest/)

[Laravel API文檔](https://laravel.com/api/9.x/)

[Eloquent ORM(資料庫)](https://laravel.tw/docs/5.0/eloquent)

[laravel-admin官網 (建構管理後台)](https://laravel-admin.org/)

[laravel-admin文檔](https://laravel-admin.org/docs/zh/1.x)

## 使用docker-compose 架設 laravel環境

[Deploying Laravel, Nginx, and MySQL with Docker Compose](https://www.cloudsigma.com/deploying-laravel-nginx-and-mysql-with-docker-compose/)

## 安裝步驟

### 安裝laravel 以及 相關套件

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

# 創建laravel
composer create-project --prefer-dist laravel/laravel  "Laravel"  6.*

# storage & cache給予寫入權限
chmod -R 755 "project_name"/storage
chmod -R 755 "project_name"/bootstrap/cache

# 觀察系統所有的程序資料
ps uax
```

## .env.example

```env
APP_NAME=Laravel
APP_ENV=local
APP_KEY=
APP_DEBUG=true
APP_URL=http://localhost

LOG_CHANNEL=stack

DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=laravel
DB_USERNAME=root
DB_PASSWORD=

BROADCAST_DRIVER=log
CACHE_DRIVER=file
QUEUE_CONNECTION=sync
SESSION_DRIVER=file
SESSION_LIFETIME=120

REDIS_HOST=127.0.0.1
REDIS_PASSWORD=null
REDIS_PORT=6379

MAIL_MAILER=smtp
MAIL_HOST=smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=null
MAIL_PASSWORD=null
MAIL_ENCRYPTION=null
MAIL_FROM_ADDRESS=null
MAIL_FROM_NAME="${APP_NAME}"

AWS_ACCESS_KEY_ID=
AWS_SECRET_ACCESS_KEY=
AWS_DEFAULT_REGION=us-east-1
AWS_BUCKET=
AWS_FILEPATH=

PRODCTION_PATH=

PUSHER_APP_ID=
PUSHER_APP_KEY=
PUSHER_APP_SECRET=
PUSHER_APP_CLUSTER=mt1

MIX_PUSHER_APP_KEY="${PUSHER_APP_KEY}"
MIX_PUSHER_APP_CLUSTER="${PUSHER_APP_CLUSTER}"

SHARE_URL =

MKT_YOURLS_IP=
mkt_yourls_dh=
mkt_yourls_st=
```

# Laravel 指令

[Laravel 資料庫 遷移](https://laravel.tw/docs/5.2/migrations)

```bash
### 資料庫: 遷移 migrations ###
# 建立遷移 預設路徑 database/migrations/{$datetime_now}_{$name}.php
php artisan make:migration $name
	--table
		指定資料表的名稱
	--create
		遷移會建立新的資料表

# 執行所有未完成的遷移
php artisan migrate
	--force
		強制執行遷移

# 還原遷移至上一個「操作」 批次
php artisan migrate:rollback

# 還原所有遷移
php artisan migrate:reset

# 首先會還原資料庫的所有遷移，接著再執行 migrate 指令。
# 此指令能有效的重新建立整個資料庫。
php artisan migrate:refresh

# 運行 /database/seeds/DatabaseSeeder.php 和其中定義的所有 Seeder
php artisan migrate:refresh --seed

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

# 建立 command 預設路徑 app/Console/Commands/{$command_name}.php
php artisan make:command $command_name
```
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

# Laravel 技巧

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

[Command Bus](https://laravel.tw/docs/5.0/bus#creating-commands)

[Artisan 指令列](https://laravel.tw/docs/5.2/artisan#writing-commands)

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
