# Lumen 筆記

## 參考資料

[官方文檔](https://lumen.laravel.com/docs/master)

[Lumen 中文文檔](https://lumen.golaravel.com/docs/)

```bash
# Installing Lumen
composer create-project --prefer-dist laravel/lumen $project_name

# Serving Your Application
php -S localhost:1111 -t public
```

# 狀況

## 錯誤訊息 A facade root has not been set.

[Error Lumen 5.7 | A Facade Root Has Not Been Set](https://chalidade.medium.com/error-lumen-5-7-a-facade-root-has-not-been-set-18b13521c834)

[Eloquent error: A facade root has not been set](https://stackoverflow.com/questions/35418810/eloquent-error-a-facade-root-has-not-been-set)

[Where to register Facades & Service Providers in Lumen](https://stackoverflow.com/questions/30399766/where-to-register-facades-service-providers-in-lumen)

`bootstrap/app.php`

```php
// Facades 啟用
$app->withFacades();
// Eloquent ORM 啟用
$app->withEloquent();
```

## laravel 與 lumen 路由寫法不同

[Call to undefined method Laravel\Lumen\Routing\Router::where()](https://stackoverflow.com/questions/48655597/call-to-undefined-method-laravel-lumen-routing-routerwhere)

[Lumen - HTTP Routing](https://lumen.laravel.com/docs/5.5/routing)

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