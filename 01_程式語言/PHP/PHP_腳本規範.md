# PHP 腳本規範

```
撰寫 PHP CLI 腳本與一般 PHP 程式的統一約束：
結構、命名、相依、錯誤處理、檢查工具。以 PSR-12 為基礎。
```

## 目錄

- [PHP 腳本規範](#php-腳本規範)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [基本結構](#基本結構)
- [命名](#命名)
- [相依管理](#相依管理)
- [參數與設定](#參數與設定)
- [錯誤處理與日誌](#錯誤處理與日誌)
- [檢查工具](#檢查工具)
- [安全](#安全)

## 參考資料

[PSR-12 編碼風格](https://www.php-fig.org/psr/psr-12/)

[PHP 官方文檔](https://www.php.net/docs.php)

[Laravel 筆記](Laravel/PHP_Laravel_筆記.md)

# 基本結構

- CLI 腳本開頭：

```php
#!/usr/bin/env php
<?php
declare(strict_types=1);

// 用途：匯出訂單報表（一行說明）
// 用法：php export_orders.php <tag> [--cleanup]
```

- `declare(strict_types=1)` 一律加，函式參數與回傳都寫型別。
- 邏輯包進函式 / 類別，頂層只留參數解析與進入點呼叫。
- Laravel 專案的排程腳本一律寫成 Artisan Command，不另放裸 PHP 檔。

# 命名

- 類別：`PascalCase`；方法 / 變數：`camelCase`；常數：`UPPER_SNAKE_CASE`。
- 檔名：類別檔與類別同名；純腳本用小寫底線動詞開頭：`export_orders.php`。

# 相依管理

- 一律 Composer 管理，提交 `composer.lock`；部署用 `composer install --no-dev`。
- `composer.json` 的 `require.php` 指定 PHP 版本。
- autoload 用 PSR-4，禁止手寫一串 `require_once`。

# 參數與設定

- CLI 參數用 `getopt()` 或框架的 Command 參數定義，禁止手刻 `$argv` 解析（單一位置參數除外）。
- 環境相關值集中 `.env`（`vlucas/phpdotenv` 或框架內建），程式內用 `getenv()` / `env()` 讀取。
- 密碼、token 不寫死在程式碼與版控。

# 錯誤處理與日誌

- 用例外處理錯誤，禁止 `@` 錯誤抑制符。
- 只捕捉預期例外；捕捉後要嘛處理、要嘛 rethrow。
- CLI 腳本失敗以非 0 退出：`exit(1)`。
- 日誌用 `monolog`（或框架 Log facade），輸出含時間戳與等級；長迴圈定期輸出進度。

# 檢查工具

- 提交前必跑：

```bash
# 語法檢查
php -l script.php

# 風格檢查與修正（PSR-12）
vendor/bin/php-cs-fixer fix --rules=@PSR12

# 靜態分析（擇一，專案內統一）
vendor/bin/phpstan analyse
vendor/bin/psalm
```

# 安全

- SQL 一律 PDO prepared statement，禁止字串拼接。
- 外部輸入用 `filter_var()` 或框架 validation 驗證後再用。
- 呼叫外部指令先 `escapeshellarg()`，能不用 `shell_exec` 就不用。
- 輸出到 HTML 一律 `htmlspecialchars()`。
