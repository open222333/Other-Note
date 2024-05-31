# PHP 工具 packagist(套件管理器)

```
Packagist 是 PHP 的包管理器和依賴管理工具 Composer 的官方包倉庫。
它是一個集中存儲庫，讓開發者可以查找和下載 PHP 的開源庫和依賴項。
Packagist 是 Composer 的默認包源，當你使用 Composer 安裝依賴時，它會從 Packagist 下載所需的包。

依賴管理：

    開發者可以在 composer.json 文件中定義項目的依賴項，並使用 Composer 安裝和管理這些依賴項。
    Packagist 提供了所有這些依賴項的索引，使得 Composer 可以自動解決依賴關係並下載所需的庫。

搜索和瀏覽包：

    Packagist 提供了一個強大的搜索功能，可以讓開發者輕鬆地查找需要的 PHP 包。
    每個包的頁面上都會顯示包的詳細信息，包括版本、下載次數、依賴關係、安裝命令等。

發布和分享包：

    開發者可以將自己的 PHP 包發布到 Packagist，這樣其他開發者就可以輕鬆地找到並使用這些包。
    發布包時，只需在 GitHub 或其他版本控制系統中託管代碼，然後在 Packagist 上提交包的元數據。

版本管理：

    Packagist 支持語義版本控制（Semantic Versioning），讓開發者可以指定和管理不同版本的依賴項。
    你可以在 composer.json 文件中精確地指定所需的包版本，確保項目依賴的穩定性。
```

## 目錄

- [PHP 工具 packagist(套件管理器)](#php-工具-packagist套件管理器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[官方網站](https://packagist.org/)

# 用法

創建 composer.json 文件

```json
{
    "require": {
        "monolog/monolog": "^2.0"
    }
}
```

使用 Composer 安裝依賴

```bash
composer install
```