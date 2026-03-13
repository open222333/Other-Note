# PHP 函式庫 PCRE(正規表示式)

```
正規表示式 描述了一種字串匹配的模式，可以用來檢查一個串是否含有某種子串、將匹配的子串做替換或者從某個串中取出符合某個條件的子串等。
```

## 目錄

- [PHP 函式庫 PCRE(正規表示式)](#php-函式庫-pcre正規表示式)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [驗證除錯工具相關](#驗證除錯工具相關)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[Regular Expressions (Perl-Compatible)](https://www.php.net/manual/en/book.pcre.php)

### 驗證除錯工具相關

[語法測試工具網站 - 推薦](https://regex101.com/)

[語法測試工具網站](https://www.debuggex.com/)

[語法測試工具網站](https://regexr.com/)

# 指令

```bash
```

# 用法

```PHP
$re = '/^(?!)night.*$|.*(?:m3_u8|m3u8|account).*/m';
$str = 'night.ttt
night.2222m3_u8
night.aaaa_m3u8_dada
pwa.dawdwa_accunt
admin.sdwda';

preg_match_all($re, $str, $matches, PREG_SET_ORDER, 0);

// Print the entire match result
var_dump($matches);
```
