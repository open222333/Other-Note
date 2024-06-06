# PHP Laravel laravel-admin 筆記

```
```

路由慣例 筆記(非必要)

```
對外
laravel 控制器 在 app/Http/Controllers
路由 在 routes/

對內
laravel-admin 控制器 在 app/Admin/Controllers
路由 在 app/Admin/routes.php

* 可同時用
```

## 目錄

- [PHP Laravel laravel-admin 筆記](#php-laravel-laravel-admin-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [範例相關](#範例相關)
    - [狀況處理相關](#狀況處理相關)
- [安裝](#安裝)
- [常用指令](#常用指令)
- [用法](#用法)
  - [Console 自製終端機命令](#console-自製終端機命令)
  - [自製命令範例](#自製命令範例)
  - [配置任務排程](#配置任務排程)
- [狀況處理](#狀況處理)
  - [降版本處理](#降版本處理)
  - [Setting a foreign key bigInteger to bigIncrements](#setting-a-foreign-key-biginteger-to-bigincrements)

## 參考資料

[官網](https://laravel-admin.org/)

[文檔](https://laravel-admin.org/docs/zh/1.x)

### 範例相關

[自定義 批量操作](https://laravel-admin.org/docs/zh/1.x/model-grid-custom-actions#%E6%89%B9%E9%87%8F%E6%93%8D%E4%BD%9C)

[數據模型詳情 - model 建立](https://laravel-admin.org/docs/zh/1.x/model-show)

### 狀況處理相關

[降版本處理 Class 'Doctrine\DBAL\Driver\PDOMySql\Driver' not found](https://laracasts.com/discuss/channels/laravel/class-doctrinedbaldriverpdomysqldriver-not-found)

[Setting a foreign key bigInteger to bigIncrements](https://stackoverflow.com/questions/42442498/setting-a-foreign-key-biginteger-to-bigincrements-in-laravel-5-4)

# 安裝

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

修改mysql連接資訊(資料庫須先建好)

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

# 常用指令

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
# 用於 Laravel（一個 PHP 框架）的命令，用來執行資料庫遷移。
# 遷移是一種以版本控制方式定義和管理資料庫結構的方法，使能夠修改和共享應用程式的資料庫結構定義。
php artisan migrate

## git pull 有新增 Contrallor
# 根據路由創建選單(需修正內容 標題 路徑)
php artisan admin:generate-menu
```

git clone 專案後使用

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

自定義 批量操作

```bash
# 生成批量操作類 文件 app/Admin/Actions/Post/Batch.php
php artisan admin:action Post\\Batch --grid-batch --name="批量操作"

# 創建一個普通的操作類 文件 app/Admin/Actions/Post/ImportPost.php
php artisan admin:action Post\\ImportPost --name="導入數據"
```

# 用法

## Console 自製終端機命令

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

創建一個自定義 Artisan 命令。使用以下命令創建一個新命令

```bash
php artisan make:command DailyClickStats
```

```php
<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use DB;

class DailyClickStats extends Command
{
    // 命令名稱
    protected $signature = 'stats:daily-click {--Y} {--R} {--r}';
    // 說明文字
    protected $description = 'Generate daily click statistics';

    public function __construct()
    {
        parent::__construct();
    }

    // Console 執行的程式
    public function handle()
    {
        // 獲取選項
        $yearly = $this->option('Y');
        $monthly = $this->option('R');
        $recursive = $this->option('r');

        // 實現統計邏輯
        if ($yearly) {
            $this->info('Generating yearly click statistics...');
            // 添加年統計邏輯
        }

        if ($monthly) {
            $this->info('Generating monthly click statistics...');
            // 添加月統計邏輯
        }

        if ($recursive) {
            $this->info('Generating recursive statistics...');
            // 添加遞歸統計邏輯
        }

        // 示例查詢
        $clicks = DB::table('clicks')
            ->select(DB::raw('DATE(created_at) as date'), DB::raw('count(*) as clicks'))
            ->groupBy('date')
            ->get();

        foreach ($clicks as $click) {
            $this->info("Date: {$click->date}, Clicks: {$click->clicks}");
        }

        $this->info('Daily click statistics generated successfully!');
    }
}
```

打開 app/Console/Kernel.php 並在 commands 屬性中註冊命令

```php
protected $commands = [
    \App\Console\Commands\DailyClickStats::class,
];
```

運行自定義 Artisan 命令

```bash
php artisan stats:daily-click -Y -R -r
```

## 配置任務排程

打開 App\Console\Kernel.php 文件

```php
<?php

namespace App\Console;

use Illuminate\Console\Scheduling\Schedule;
use Illuminate\Foundation\Console\Kernel as ConsoleKernel;

class Kernel extends ConsoleKernel
{
    /**
     * The Artisan commands provided by your application.
     *
     * @var array
     */
    protected $commands = [
        // 在這裡註冊您的命令
        \App\Console\Commands\DailyClickStats::class,
    ];

    /**
     * Define the application's command schedule.
     *
     * @param  \Illuminate\Console\Scheduling\Schedule  $schedule
     * @return void
     */
    protected function schedule(Schedule $schedule)
    {
        // 排程每日運行 stats:daily-click 命令
        $schedule->command('stats:daily-click -Y -R -r')->daily();
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

Laravel 的任務排程依賴於系統的定時任務（如 cron）來驅動。您需要在服務器上設置一個定時任務來調用 Laravel 的排程器。

```bash
* * * * * cd /path-to-your-project && php artisan schedule:run >> /dev/null 2>&1
```

手動運行一次排程來測試配置是否正確

```bash
php artisan schedule:run
```

# 狀況處理

## 降版本處理

```bash
composer require doctrine/dbal:^2.12.1
```

## Setting a foreign key bigInteger to bigIncrements

```
bigIncrements bigInteger
設置 foreign key

當外鍵有參照到自動增量時，記得設定外鍵為 unsigned 型態。
```
