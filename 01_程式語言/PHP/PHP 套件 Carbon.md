#

## 參考資料

[Carbon 官方文檔](https://carbon.nesbot.com/docs/)

[時區參考](https://www.php.net/manual/en/timezones.php)

## 安裝

```bash
# 使用 composer
composer require nesbot/carbon
```

```php
require 'vendor/autoload.php';

use Carbon\Carbon;

$start = Carbon::yesterday()->startOfDay();
$end = Carbon::yesterday()->endOfDay();

print "$start\n";
print "$end\n";
```