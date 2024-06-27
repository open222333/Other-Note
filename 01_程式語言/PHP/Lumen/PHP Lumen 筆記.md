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