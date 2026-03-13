# Lumen 筆記

```
```

## 目錄

- [Lumen 筆記](#lumen-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [例外狀況相關](#例外狀況相關)
      - [錯誤訊息 A facade root has not been set.](#錯誤訊息-a-facade-root-has-not-been-set)
      - [laravel 與 lumen 路由寫法不同](#laravel-與-lumen-路由寫法不同)
- [安裝](#安裝)
  - [git clone 專案](#git-clone-專案)
- [指令](#指令)
- [用法](#用法)
  - [建立一個簡易 api 將傳入資料 顯示在 log](#建立一個簡易-api-將傳入資料-顯示在-log)
- [狀況](#狀況)
  - [錯誤訊息 A facade root has not been set.](#錯誤訊息-a-facade-root-has-not-been-set-1)
  - [laravel 與 lumen 路由寫法不同](#laravel-與-lumen-路由寫法不同-1)

## 參考資料

[官方文檔](https://lumen.laravel.com/docs/master)

[Lumen 中文文檔](https://lumen.golaravel.com/docs/)

### 例外狀況相關

#### 錯誤訊息 A facade root has not been set.

[Error Lumen 5.7 | A Facade Root Has Not Been Set](https://chalidade.medium.com/error-lumen-5-7-a-facade-root-has-not-been-set-18b13521c834)

[Eloquent error: A facade root has not been set](https://stackoverflow.com/questions/35418810/eloquent-error-a-facade-root-has-not-been-set)

[Where to register Facades & Service Providers in Lumen](https://stackoverflow.com/questions/30399766/where-to-register-facades-service-providers-in-lumen)

#### laravel 與 lumen 路由寫法不同

[Call to undefined method Laravel\Lumen\Routing\Router::where()](https://stackoverflow.com/questions/48655597/call-to-undefined-method-laravel-lumen-routing-routerwhere)

[Lumen - HTTP Routing](https://lumen.laravel.com/docs/5.5/routing)

# 安裝

```bash
# Installing Lumen
composer create-project --prefer-dist laravel/lumen $project_name

# Serving Your Application
php -S localhost:1111 -t public
```

## git clone 專案

安裝相依套件：

```bash
composer install
```

設置環境變數：

```bash
cp .env.example .env
```

設置權限

```bash
chown -R nginx:nginx /path/to/your_project/storage
chmod -R 775 /path/to/your_project/storage
```

產生應用程式金鑰：

```bash
php artisan key:generate
```

執行資料庫遷移（如果有需要）：
如果專案需要使用資料庫，你可能需要執行資料庫遷移來建立資料庫結構：

```bash
php artisan migrate
```

要填充資料

```bash
php artisan migrate --seed
```

# 指令

使用內建的 PHP 伺服器來運行專案，可以執行以下指令：

```bash
php -S localhost:8000 -t public
```

# 用法

## 建立一個簡易 api 將傳入資料 顯示在 log

routes/web.php

```php
<?php

/** @var \Laravel\Lumen\Routing\Router $router */

use Illuminate\Support\Facades\Log;
use Illuminate\Http\Response;
use Illuminate\Http\Request;

$router->post('/aaaaa/eeeee', function (Request $request) use ($router) {
    $requestData = [
        "data" => $request->getContent(),
        "json" => $request->json()->all(),
        "form" => $request->all(),
        "args" => $request->query(),
        "headers" => $request->headers->all()
    ];

    Log::info($requestData);
    // print_r($requestData);
    return response()->json($requestData);

    // var_dump($request->all());
    // return response(null, 200);
});

$router->post('/api/example', function (Request $request) {
    $requestData = [
        "data" => $request->getContent(),
        "json" => $request->json()->all(),
        "form" => $request->all(),
        "args" => $request->query(),
        "headers" => $request->headers->all()
    ];

    // 記錄請求資料到指定的日誌文件
    Log::channel('nginx')->info('Request Data: ' . json_encode($requestData));

    return response()->json($requestData);
});

// 捕捉所有未定義的路由並傳回 JSON 錯誤訊息
$router->get('/{any:.*}', function () {
    return response()->json(['message' => 'Not Page'], 404);
});
```

確定日誌檔案和目錄的權限

紀錄在 storage/

```php
use Illuminate\Support\Facades\Log;

$router->get('/example', function () {
    // 記錄資訊等級的日誌
    Log::info('This is an informational message.');

    // 記錄錯誤等級的日誌
    Log::error('An error occurred.');

    // 記錄帶有上下文的日誌
    Log::warning('User login failed.', ['username' => 'john.doe']);

    return response()->json(['message' => 'Logged some messages.']);
});
```

# 狀況

## 錯誤訊息 A facade root has not been set.

`bootstrap/app.php`

```php
// Facades 啟用
$app->withFacades();
// Eloquent ORM 啟用
$app->withEloquent();
```

## laravel 與 lumen 路由寫法不同

```php
// laravel route寫法
Route::get('/{shorturl}','RedirectToProductUrl@redirectToProductUrl')->name('shorturl');

// lumen route寫法
$router->get('/{shorturl}', [
    'as' => 'profile',
	'uses' => 'RedirectToProductUrl@redirectToProductUrl',
	function ($shorturl) {

	}
]);
```
