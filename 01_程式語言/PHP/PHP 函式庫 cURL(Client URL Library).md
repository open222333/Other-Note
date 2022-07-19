# PHP 函式庫 cURL(Client URL Library)

```
連接通訊各種服務器、使用各種協議。

libcurl 目前支持的協議有http、https、ftp、gopher、telnet、dict、file、ldap。

libcurl 同時支持HTTPS 證書、HTTP POST、HTTP PUT、 FTP 上傳(也能通過PHP 的FTP 擴展完成)、HTTP 基於表單的上傳、代理、cookies、用戶名+密碼的認證。
```

## 目錄

- [PHP 函式庫 cURL(Client URL Library)](#php-函式庫-curlclient-url-library)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[Client URL](https://www.php.net/manual/zh/book.curl.php)

[cURL轉成個別語言範本](https://curlconverter.com/#php)

# 指令

```bash
```

# 用法

```PHP
// GET

// cURL Command
// curl 'http://en.wikipedia.org/' \
//     -H 'Accept-Encoding: gzip, deflate, sdch' \
//     -H 'Accept-Language: en-US,en;q=0.8' \
//     -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36' \
//     -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' \
//     -H 'Referer: http://www.wikipedia.org/' \
//     -H 'Connection: keep-alive' --compressed

<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://en.wikipedia.org/');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    'Accept-Encoding' => 'gzip, deflate, sdch',
    'Accept-Language' => 'en-US,en;q=0.8',
    'User-Agent' => 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36',
    'Accept' => 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Referer' => 'http://www.wikipedia.org/',
    'Connection' => 'keep-alive',
]);

$response = curl_exec($ch);

curl_close($ch);
```

```PHP
// POST

// cURL Command
// curl 'http://fiddle.jshell.net/echo/html/' \
//     -H 'Origin: http://fiddle.jshell.net' \
//     -H 'Accept-Encoding: gzip, deflate' \
//     -H 'Accept-Language: en-US,en;q=0.8' \
//     -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36' \
//     -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
//     -H 'Accept: */*' \
//     -H 'Referer: http://fiddle.jshell.net/_display/' \
//     -H 'X-Requested-With: XMLHttpRequest' \
//     -H 'Connection: keep-alive' \
//     --data 'msg1=wow&msg2=such&msg3=data' --compressed

<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://fiddle.jshell.net/echo/html/');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    'Origin' => 'http://fiddle.jshell.net',
    'Accept-Encoding' => 'gzip, deflate',
    'Accept-Language' => 'en-US,en;q=0.8',
    'User-Agent' => 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36',
    'Content-Type' => 'application/x-www-form-urlencoded; charset=UTF-8',
    'Accept' => '*/*',
    'Referer' => 'http://fiddle.jshell.net/_display/',
    'X-Requested-With' => 'XMLHttpRequest',
    'Connection' => 'keep-alive',
]);
curl_setopt($ch, CURLOPT_POSTFIELDS, 'msg1=wow&msg2=such&msg3=data');

$response = curl_exec($ch);

curl_close($ch);
```

```PHP
// Basic Auth

// cURL Command
// curl "https://api.test.com/" -u "some_username:some_password"

<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'https://api.test.com/');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($ch, CURLOPT_USERPWD, 'some_username:some_password');

$response = curl_exec($ch);

curl_close($ch);
```
