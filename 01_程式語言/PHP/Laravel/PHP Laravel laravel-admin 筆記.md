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
    - [API 文件](#api-文件)
- [安裝](#安裝)
- [常用指令](#常用指令)
  - [git clone 專案後使用](#git-clone-專案後使用)
- [用法](#用法)
  - [Route API](#route-api)
  - [範例流程](#範例流程)
    - [建立 Model、Migration、Factory](#建立-modelmigrationfactory)
    - [建立 laravel-admin CRUD 頁面](#建立-laravel-admin-crud-頁面)
    - [API 建立範例](#api-建立範例)
  - [API 驗證（選用：Laravel Sanctum）](#api-驗證選用laravel-sanctum)
  - [在既有 Controller 中，新增一個自定義 API](#在既有-controller-中新增一個自定義-api)
    - [添加 API (自行)](#添加-api-自行)
    - [加驗證與錯誤處理](#加驗證與錯誤處理)
  - [Console 自製終端機命令](#console-自製終端機命令)
  - [自製命令範例](#自製命令範例)
  - [配置任務排程](#配置任務排程)
  - [搜尋條件（filter）套用 paginate() 查詢](#搜尋條件filter套用-paginate-查詢)
- [狀況處理](#狀況處理)
  - [降版本處理](#降版本處理)
  - [Setting a foreign key bigInteger to bigIncrements](#setting-a-foreign-key-biginteger-to-bigincrements)

## 參考資料

[laravel-admin官網 (建構管理後台)](https://laravel-admin.org/)

[laravel-admin文檔](https://laravel-admin.org/docs/zh/1.x)

### 範例相關

[自定義 批量操作](https://laravel-admin.org/docs/zh/1.x/model-grid-custom-actions#%E6%89%B9%E9%87%8F%E6%93%8D%E4%BD%9C)

[數據模型詳情 - model 建立](https://laravel-admin.org/docs/zh/1.x/model-show)

### 狀況處理相關

[降版本處理 Class 'Doctrine\DBAL\Driver\PDOMySql\Driver' not found](https://laracasts.com/discuss/channels/laravel/class-doctrinedbaldriverpdomysqldriver-not-found)

[Setting a foreign key bigInteger to bigIncrements](https://stackoverflow.com/questions/42442498/setting-a-foreign-key-biginteger-to-bigincrements-in-laravel-5-4)

### API 文件

[Github DarkaOnLine / L5-Swagger](https://github.com/DarkaOnLine/L5-Swagger)

# 安裝

```bash
# 進入專案資料夾
cd "project_name"

# 自動確認目前系統環境是否可使用這個套件
composer require encore/laravel-admin

# 運行下面的命令來發布資源：
php artisan vendor:publish --provider="Encore\Admin\AdminServiceProvider"

# 安裝 laravel-admin (建立 admin 用戶)
php artisan admin:install

# 根據路由創建選單(需修正內容 標題 路徑)
php artisan admin:generate-menu
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

# 建立一個admin用戶
php artisan admin:create-user

# 指定用戶重置密碼
php artisan admin:reset-password

# 遷移資料庫
# 用於 Laravel（一個 PHP 框架）的命令，用來執行資料庫遷移。
# 遷移是一種以版本控制方式定義和管理資料庫結構的方法，使能夠修改和共享應用程式的資料庫結構定義。
php artisan migrate

## git pull 有新增 Contrallor
# 根據路由創建選單(需修正內容 標題 路徑)
php artisan admin:generate-menu
```

自定義 批量操作

```bash
# 生成批量操作類 文件 app/Admin/Actions/Post/Batch.php
php artisan admin:action Post\\Batch --grid-batch --name="批量操作"

# 創建一個普通的操作類 文件 app/Admin/Actions/Post/ImportPost.php
php artisan admin:action Post\\ImportPost --name="導入數據"
```

## git clone 專案後使用

```bash
# 安裝composer.json內紀錄的框架所需套件
composer install

# 將資源複製到指定的發佈位置
php artisan vendor:publish --provider="Encore\Admin\AdminServiceProvider"

# 給予權限 (若使用nginx 給予nginx使用者帳號權限)
chown -R nginx.nginx storage
chown -R nginx.nginx bootstrap/cache

# 檢查是否有未運行的遷移：
php artisan migrate:status
# 如果有未運行的遷移，運行以下命令來運行它們：
php artisan migrate

# 安裝 laravel-admin (建立 admin 用戶)(非必要)
php artisan admin:install

# 根據路由創建選單(需修正內容 標題 路徑)
php artisan admin:generate-menu

# 清除快取
php artisan optimize:clear
```

# 用法

## Route API

```php
<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Route;

Route::match(['get', 'post'], '/example', function (Request $request) {
    $requestData = [
        "data" => $request->getContent(),
        "json" => $request->json()->all(),
        "form" => $request->all(),
        "args" => $request->query(),
        "headers" => $request->headers->all(),
        "files" => $request->files->all(),
        "cookies" => $request->cookies->all(),
    ];

    // 將請求內容記錄到日誌
    Log::info('Request Data: ', $requestData);

    // 返回 JSON 響應
    return response()->json($requestData);
});
```

## 範例流程

```
預期功能（舉例：User 管理）

使用 laravel-admin 管理介面建立/修改使用者。

提供外部 API：/api/users、/api/users/{id}。

支援 Request 驗證與 Token 驗證（選用）。
```

### 建立 Model、Migration、Factory

```sh
php artisan make:model User -m -f
```

編輯 database/migrations/xxxx_create_users_table.php

```php
$table->id();
$table->string('name');
$table->string('email')->unique();
$table->string('password');
$table->timestamps();
```

```sh
php artisan migrate
```

### 建立 laravel-admin CRUD 頁面

```sh
php artisan admin:make UserController --model=App\\Models\\User
```

會自動建立 app/Admin/Controllers/UserController.php 並註冊路由到 /admin/users

如需自訂表單或 Grid 列表樣式，可修改該 Controller

```php
$form->text('name')->rules('required');
$form->email('email')->rules('required|email|unique:users,email,{{id}}');
$form->password('password')->rules('nullable|min:6')->default(function ($form) {
    return $form->model()->password;
});
```

### API 建立範例

建立 API 專用 Controller

```sh
php artisan make:controller Api/UserApiController
```

routes/api.php 設定

```php
use App\Http\Controllers\Api\UserApiController;

Route::middleware('auth:sanctum')->group(function () {
    Route::apiResource('users', UserApiController::class);
});
```

app/Http/Controllers/Api/UserApiController.php

```php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class UserApiController extends Controller
{
    public function index()
    {
        return response()->json(User::paginate(10));
    }

    public function show(User $user)
    {
        return response()->json($user);
    }

    public function store(Request $request)
    {
        $data = $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|email|unique:users',
            'password' => 'required|string|min:6',
        ]);

        $data['password'] = Hash::make($data['password']);
        $user = User::create($data);

        return response()->json($user, 201);
    }

    public function update(Request $request, User $user)
    {
        $data = $request->validate([
            'name' => 'sometimes|string|max:255',
            'email' => 'sometimes|email|unique:users,email,' . $user->id,
            'password' => 'nullable|string|min:6',
        ]);

        if (!empty($data['password'])) {
            $data['password'] = Hash::make($data['password']);
        } else {
            unset($data['password']);
        }

        $user->update($data);

        return response()->json($user);
    }

    public function destroy(User $user)
    {
        $user->delete();
        return response()->json(['message' => 'deleted']);
    }
}
```

## API 驗證（選用：Laravel Sanctum）

```sh
composer require laravel/sanctum
php artisan vendor:publish --provider="Laravel\Sanctum\SanctumServiceProvider"
php artisan migrate
```

加入 Sanctum 中介層

```php
// app/Http/Kernel.php
'api' => [
    \Laravel\Sanctum\Http\Middleware\EnsureFrontendRequestsAreStateful::class,
    'throttle:api',
    \Illuminate\Routing\Middleware\SubstituteBindings::class,
],
```

登入發 Token

```php
Route::post('/login', function (Request $request) {
    $credentials = $request->only('email', 'password');

    if (!Auth::attempt($credentials)) {
        return response()->json(['message' => 'invalid credentials'], 401);
    }

    return response()->json([
        'token' => $request->user()->createToken('api-token')->plainTextToken
    ]);
});
```

## 在既有 Controller 中，新增一個自定義 API

```
在既有 UserController 中，新增一個自定義 API：

GET /api/users/{id}/profile
```

新增方法到現有 Controller

```php
public function profile($id)
{
    $user = User::findOrFail($id);

    return response()->json([
        'id' => $user->id,
        'name' => $user->name,
        'email' => $user->email,
        'registered_at' => $user->created_at->toDateTimeString(),
    ]);
}
```

新增 API 路由

routes/api.php

```php
use App\Http\Controllers\Api\UserController;

Route::get('/users/{id}/profile', [UserController::class, 'profile']);
```

如果你有設定驗證保護 API，可加上 middleware

```php
Route::middleware('auth:sanctum')->group(function () {
    Route::get('/users/{id}/profile', [UserController::class, 'profile']);
});
```

測試

```sh
curl -H "Authorization: Bearer <token>" \
     http://your-domain.com/api/users/1/profile
```

### 添加 API (自行)

```
中介層 中介器 （Middleware）
```

`admin 底下的路由 會因為中介器(admin.auth middleware)需要登入驗證`

Laravel-Admin 預設會對 /admin 底下的所有路由加上 admin.auth middleware

```php
// app/Admin/route.php
use Illuminate\Routing\Router;

Admin::routes();

Route::group([
    'prefix'        => config('admin.route.prefix'),
    'namespace'     => config('admin.route.namespace'),
    'middleware'    => config('admin.route.middleware'),
    'as'            => config('admin.route.prefix') . '.',
], function (Router $router) {

    $router->get('自訂路徑', '控制器@方法');
    $router->post('api', 'ApiController@api');

    $router->get('path', 'Controller@method');
    $router->post('path', 'Controller@method');
    $router->put('path', 'Controller@method');
    $router->delete('path', 'Controller@method');
    $router->patch('path', 'Controller@method');
    $router->options('path', 'Controller@method');
    $router->any('path', 'Controller@method');
}
```

```php
// routes/api.php

<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\ApiController;

Route::middleware('auth:api')->group(function () {
    // 需要驗證的 API
    Route::get('/api', [ApiController::class, 'api']);
});

// 不需要驗證的 API 路由
Route::get('/public-api', [ApiController::class, 'api']);

// GET 請求
Route::get('/public-api', [ApiController::class, 'api']);

// POST 請求
Route::post('/public-api', [ApiController::class, 'store']);

// PUT 請求，帶 id 參數
Route::put('/public-api/{id}', [ApiController::class, 'update']);

// DELETE 請求，帶 id 參數
Route::delete('/public-api/{id}', [ApiController::class, 'destroy']);

// 接受所有方法
Route::any('/public-api/any', [ApiController::class, 'handleAny']);
```

```php
use Illuminate\Support\Facades\Request;

class ApiController extends AdminController {

    /*
        其他程式碼........
    */
    protected function api(httpRequest $request){
        $id = $request->get('id');

        // 資料驗證，用來檢查輸入的參數是否符合格式與規則
        $request->validate([
            'id' => 'required|string',
            'date' => 'required|date_format:Y-m-d',
        ]);

        $arr = Db::table("test")
        ->where(["id" => $id])
        ->distinct()
        ->get([Db::raw('id'), Db::raw('id as text')]);
        return $arr;
    }

    public function api(Request $request)
    {
        $message = 'ok';
        $status_code = 200;

        return response()->json([
            'message' => $message,
            'error_code' => $status_code,
        ], $status_code);
    }
}
```

curl 測試範例

```sh
# http://your-domain/admin/api/test?id=1
curl -X GET "http://127.0.0.1:8000/admin/api/test?id=1"
```

若 API 有登入驗證（Laravel Admin 常會有登入 Cookie），你需要攜帶 cookie

```sh
curl -X GET "http://127.0.0.1:8000/admin/api/test?id=1" \
  -H "Cookie: laravel_session=你的 session id"
```

### 加驗證與錯誤處理

```php
public function profile($id)
{
    $user = User::find($id);

    if (!$user) {
        return response()->json(['message' => 'User not found'], 404);
    }

    return response()->json([
        'id' => $user->id,
        'name' => $user->name,
        'email' => $user->email,
        'registered_at' => $user->created_at->toDateTimeString(),
    ]);
}
```

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

## 搜尋條件（filter）套用 paginate() 查詢

在 DailyClickReport::paginate() 裡補上對 shorturl_name 的搜尋邏輯

```php
$filter->like('shorturl_name', __('縮網址名稱'));
```

在 Controller 的 grid() 裡，使用自定義欄位名稱，把使用者輸入的搜尋參數「轉存」到 shorturl_name

```php
$result = Shorturl::join('daily_click_report', function ($join) {
	$join->on('shorturl.id', '=', 'daily_click_report.shorturl_id');
})
	->selectRaw("shorturl.id, shorturl.name, SUM(daily_click_report.total_click) AS sum_total_click")
	->where("daily_click_report.date", ">=", $sdate)
	->where("daily_click_report.date", "<=", $edate);

// 加入 shorturl_name 關鍵字查詢
$shorturl_name = Request::get('shorturl_name', null);
if (!is_null($shorturl_name)) {
	$result = $result->where('shorturl.name', 'like', "%$shorturl_name%");
}

// 加入 product_id 過濾條件
$product_id = Request::get('product_id', NULL);
if (!is_null($product_id)) {
	$result = $result->where('daily_click_report.product_id', '=', $product_id);
}

// 排序與群組
$result = $result
	->groupBy('daily_click_report.shorturl_id')
	->orderBy($orderby, $sort_type)
	->get();
```

# 狀況處理

## 降版本處理

```bash
composer require doctrine/dbal:^2.12.1
```

先移除 lock file 和 vendor，重新安裝乾淨套件

```sh
rm -rf vendor composer.lock
composer install
composer require doctrine/dbal:^2.13 -W
```

手動解除 Carbon 鎖定並安裝正確版本

```sh
composer require nesbot/carbon:^2.69 --no-update
composer update nesbot/carbon
composer remove carbonphp/carbon-doctrine-types
```

## Setting a foreign key bigInteger to bigIncrements

```
bigIncrements bigInteger
設置 foreign key

當外鍵有參照到自動增量時，記得設定外鍵為 unsigned 型態。
```
