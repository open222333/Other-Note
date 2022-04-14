# PHP 模組 Carbon(日期時間)

## 參考資料

[Carbon 官方文檔](https://carbon.nesbot.com/docs/)

[時區參考](https://www.php.net/manual/en/timezones.php)

# 指令

```bash
# 使用 composer
composer require nesbot/carbon
```

# 用法

```PHP
require 'vendor/autoload.php';

use Carbon\Carbon;

$start = Carbon::yesterday()->startOfDay();
$end = Carbon::yesterday()->endOfDay();

print "$start\n";
print "$end\n";
```