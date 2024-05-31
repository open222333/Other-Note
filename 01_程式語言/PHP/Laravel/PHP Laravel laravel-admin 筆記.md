# PHP Laravel laravel-admin 筆記

```
```

## 目錄

- [PHP Laravel laravel-admin 筆記](#php-laravel-laravel-admin-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [常用指令](#常用指令)
  - [安裝laravel-admin](#安裝laravel-admin)
    - [修改mysql連接資訊(資料庫須先建好)](#修改mysql連接資訊資料庫須先建好)
  - [git clone 專案後使用](#git-clone-專案後使用)
  - [常用指令](#常用指令-1)
  - [自定義 批量操作](#自定義-批量操作)
- [Console 自製終端機命令](#console-自製終端機命令)
  - [自製命令範例](#自製命令範例)
  - [狀況處理筆記](#狀況處理筆記)
- [model 建立](#model-建立)
  - [Setting a foreign key bigInteger to bigIncrements](#setting-a-foreign-key-biginteger-to-bigincrements)
- [慣例 筆記(非必要)](#慣例-筆記非必要)

## 參考資料

[官網](https://laravel-admin.org/)

[文檔](https://laravel-admin.org/docs/zh/1.x)

# 常用指令

## 安裝laravel-admin

```bash
# 進入專案資料夾
cd "project_name"

# 自動確認目前系統環境是否可使用這個套件
composer require encore/laravel-admin

# 運行下面的命令來發布資源：
php artisan vendor:publish --provider="Encore\Admin\AdminServiceProvider"

# 安裝 laravel-admin
php artisan admin:install
```

### 修改mysql連接資訊(資料庫須先建好)

```bash
vi .env
```

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=laravel
DB_USERNAME="mysqlusername"
DB_PASSWORD="mysqlpasswd"
```

## git clone 專案後使用

```bash
# 安裝composer.json內紀錄的框架所需套件
composer install

# 將資源複製到指定的發佈位置
php artisan vendor:publish --provider="Encore\Admin\AdminServiceProvider"

# 給予權限 (若使用nginx 給予nginx使用者帳號權限)
cd "project_name"
chown -R nginx.nginx storage
chown -R nginx.nginx bootstrap/cache
```

## 常用指令

```bash
# 透過artisan產生一組網站專屬密鑰
php artisan key:generate

# 清空 參數設定緩存
php artisan config:clear

# 建立遷移 預設路徑 database/migrations/{$datetime_now}_{$name}.php
php artisan make:migration $name
	--table
		指定資料表的名稱
	--create
		遷移會建立新的資料表

# php內置 web環境
php artisan serve

# 建立 model 預設路徑 app/{$model_name}.php
php artisan make:model 'Path\To\Model'

# 建立 admin controller，傳入一個model 預設路徑 app/Admin/Controllers/{$controller_name}.php
php artisan admin:make {$controller_name} --model='Path\To\Model'

# 指定用戶重置密碼
php artisan admin:reset-password

# 根據路由創建選單(需修正內容 標題 路徑)
php artisan admin:generate-menu

# 遷移資料庫
php artisan migrate

## git pull 有新增 Contrallor
# 根據路由創建選單(需修正內容 標題 路徑)
php artisan admin:generate-menu
```

## 自定義 批量操作

[自定義 批量操作](https://laravel-admin.org/docs/zh/1.x/model-grid-custom-actions#%E6%89%B9%E9%87%8F%E6%93%8D%E4%BD%9C)

```bash
# 生成批量操作類 文件 app/Admin/Actions/Post/Batch.php
php artisan admin:action Post\\Batch --grid-batch --name="批量操作"

# 創建一個普通的操作類 文件 app/Admin/Actions/Post/ImportPost.php
php artisan admin:action Post\\ImportPost --name="導入數據"
```

# Console 自製終端機命令

```php
<?php

namespace App\Console;

use Illuminate\Console\Scheduling\Schedule;
use Illuminate\Foundation\Console\Kernel as ConsoleKernel;

class Kernel extends ConsoleKernel
{
    /**
     * The Artisan commands provided by your application.
     * 應用程序提供的 Artisan 命令。
     * @var array
     */
    protected $commands = [
		\App\Console\Commands\Test::class,
        //
    ];

    /**
     * Define the application's command schedule.
     *
     * @param  \Illuminate\Console\Scheduling\Schedule  $schedule
     * @return void
     */
    protected function schedule(Schedule $schedule)
    {
        // $schedule->command('inspire')
        //          ->hourly();
    }

    /**
     * Register the commands for the application.
     *
     * @return void
     */
    protected function commands()
    {
        $this->load(__DIR__.'/Commands');

        require base_path('routes/console.php');
    }
}
```

## 自製命令範例

```php
<?php
namespace App\Console\Commands;
use Illuminate\Console\Command;

class Test extends Command
{
    // 命令名稱
    protected $signature = 'test:test';

    // 說明文字
    protected $description = 'test';

    public function __construct()
    {
        parent::__construct();
    }

    // Console 執行的程式
    public function handle()
    {
		// ......
    }
}
```

## 狀況處理筆記

[降版本處理 Class 'Doctrine\DBAL\Driver\PDOMySql\Driver' not found](https://laracasts.com/discuss/channels/laravel/class-doctrinedbaldriverpdomysqldriver-not-found)

```bash
composer require doctrine/dbal:^2.12.1
```

# model 建立

[數據模型詳情](https://laravel-admin.org/docs/zh/1.x/model-show)

## Setting a foreign key bigInteger to bigIncrements

```
bigIncrements bigInteger
設置 foreign key

當外鍵有參照到自動增量時，記得設定外鍵為 unsigned 型態。
```

[Setting a foreign key bigInteger to bigIncrements](https://stackoverflow.com/questions/42442498/setting-a-foreign-key-biginteger-to-bigincrements-in-laravel-5-4)

# 慣例 筆記(非必要)

* 控制器

```
對外
laravel 控制器 在 app/Http/Controllers
路由 在 routes/

對內
laravel-admin 控制器 在 app/Admin/Controllers
路由 在 app/Admin/routes.php

* 可同時用
```