# PHP 筆記

## 目錄

- [PHP 筆記](#php-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [特殊 浮點數比較方法](#特殊-浮點數比較方法)
    - [伺服器變數 $\_SERVER](#伺服器變數-_server)
    - [ICU4C](#icu4c)
    - [例外狀況相關](#例外狀況相關)
      - [docker-compose 相關](#docker-compose-相關)
    - [](#)
- [安裝](#安裝)
  - [CentOS7](#centos7)
  - [MacOS](#macos)
    - [安裝 php@7.4 (從第三方)](#安裝-php74-從第三方)
- [指令](#指令)
- [設定檔](#設定檔)
- [docker-compose 架設PHP開發環境](#docker-compose-架設php開發環境)
- [PHP擴展庫 PECL](#php擴展庫-pecl)
- [基本概念](#基本概念)
  - [型別轉換](#型別轉換)
    - [使用 `C 語言`的 type casting 語法](#使用-c-語言的-type-casting-語法)
    - [使用 `settype` 函數](#使用-settype-函數)
  - [比較運算子](#比較運算子)
  - [條件式](#條件式)
  - [迴圈](#迴圈)
  - [物件](#物件)
  - [正則表達式](#正則表達式)
- [技巧](#技巧)
  - [快速替換程式建議（可丟進 log/debug 用）](#快速替換程式建議可丟進-logdebug-用)
  - [大量資料 避免記憶體限制使用 chunk 批次處理](#大量資料-避免記憶體限制使用-chunk-批次處理)
- [例外狀況](#例外狀況)
  - [MacOS - dyld\[60237\]: Library not loaded: /usr/local/opt/icu4c/lib/libicuio.70.dylib](#macos---dyld60237-library-not-loaded-usrlocalopticu4cliblibicuio70dylib)

## 參考資料

[官方文檔](https://www.php.net/docs.php)

[PDO Drivers](https://www.php.net/manual/en/pdo.drivers.php)

[安裝 php-pdo(php資料庫驅動套件)](https://www.php.net/manual/zh/pdo.installation.php)

[時區參考](https://www.php.net/manual/en/timezones.php)

[php命令列指令(右上角可選語言)](https://www.php.net/manual/en/features.commandline.options.php)

[PECL是一個PHP擴展庫，提供了一個PHP所有已知擴展的下載和託管目錄。](https://pecl.php.net/packages.php)

[docker 分別建立 Nginx 和 PHP-FPM 容器](https://blogs.slat.org/blog/otakupapa/42)

[Dockerise your PHP application with Nginx and PHP7-FPM](http://geekyplatypus.com/dockerise-your-php-application-with-nginx-and-php7-fpm/)

[fastcgi_pass](https://kejyuntw.gitbooks.io/ubuntu-learning-notes/content/web/php/web-php-fastcgi-pass.html)

[Nginx 與 PHP-FPM 最佳化效能設定教學與技巧](https://blog.gtwang.org/linux/nginx-php-fpm-configuration-optimization/)

[官方 安裝教學(包含各種web server)](https://www.php.net/manual/en/install.php)

[FPM 配置文檔 參數 php-fpm.conf](https://www.php.net/manual/en/install.fpm.configuration.php)

[使用brew 下載安裝](https://formulae.brew.sh/formula/php#default)

### 特殊 浮點數比較方法

[php 浮點數比較方法](https://www.twblogs.net/a/5c6f8455bd9eee7f07323dbb)

### 伺服器變數 $_SERVER

[$_SERVER內容](https://www.php.net/manual/zh/reserved.variables.server.php)

### ICU4C

```
Common Component ICU4C 代表適用於 C/C++ 類別庫的 Unicode 國際元件，它提供以 C/C++ 程式設計語言撰寫廣域應用程式的全球化公用程式。 在 AIX® 作業系統上執行的許多產品正在使用 ICU4C 程式庫。
```

[ICU4C 程式庫](https://www.ibm.com/docs/zh-tw/aix/7.3?topic=programs-icu4c-libraries)

[ICU Documentation](https://unicode-org.github.io/icu/)

[ICU4C Readme](https://unicode-org.github.io/icu/userguide/icu4c/)

### 例外狀況相關

[dyld: Library not loaded: /usr/local/opt/icu4c/lib/libicui18n.62.dylib error running php after installing node with brew on Mac](https://stackoverflow.com/questions/53828891/dyld-library-not-loaded-usr-local-opt-icu4c-lib-libicui18n-62-dylib-error-run)

#### docker-compose 相關

[Cron does not run in a PHP Docker container - crontab 無法在 php-fpm container 內執行](https://forums.docker.com/t/cron-does-not-run-in-a-php-docker-container/103897)

[Docker and cron is broken: can't lock /var/run/crond.pid](https://unix.stackexchange.com/questions/620452/docker-and-cron-is-broken-cant-lock-var-run-crond-pid)

[在 Docker container 中使用 Cron](https://bingdoal.github.io/others/2021/04/crontab-on-docker-container/)

[[Day4] Linux 排程工具 Crontab，也有Docker 的範例喔](https://ithelp.ithome.com.tw/m/articles/10293218)

###

[](https://packagist.org/)

# 安裝

## CentOS7

```bash
yum install epel-release -y
# rpm-gpg-key
yum install http://rpms.remirepo.net/enterprise/remi-release-7.rpm -y

# 列出並選擇可以安裝的 php 版本
yum search php

# 安裝 php74 架設開發環境 安裝 php-fpm
yum install php74 php74-php-fpm php74-php-gd -y
yum install php74-php-json php74-php-mbstring php74-php-mysqlnd -y
yum install php74-php-openssl php74-php-pdo php74-php-nette-tokenizer php74-php-xml php74-php-ctype -y

# php74 link到php目錄位置
# ln -s “$php 安裝位置” “$指令存放位置”
ln -s /opt/remi/php74/root/usr/bin/php /usr/bin/php

# 設定開機自動啟動 php-fpm
systemctl enable php74-php-fpm

# 立即啟動 php-fpm
systemctl start php74-php-fpm

# 查看 php-fpm
systemctl status php74-php-fpm

# 重新啟動 php-fpm
systemctl restart php74-php-fpm

# 查看ini檔位置
php --ini

# 對外開放 6379 port
# --permanent 指定為永久設定，否則在 firewalld重啟或是重新讀取設定，就會失效
firewall-cmd --zone=public --add-port=6379/tcp --permanent

# 重新讀取 firewall 設定
firewall-cmd --reload
```

## MacOS

查看有哪些版本

```bash
brew search php
```

```bash
brew install php@8.1
```

### 安裝 php@7.4 (從第三方)

```bash
brew tap shivammathur/php
brew install shivammathur/php/php@7.4
```

```bash
# 添加 環境變數到 .bash_profile
echo 'export PATH="/usr/local/opt/php@{version版本}/bin:$PATH"' >> ~/.bash_profile
echo 'export PATH="/usr/local/opt/php@{version版本}/sbin:$PATH"' >> ~/.bash_profile
```

# 指令

```bash
# 查看PHP版本
php --version

# 查看PHP設定檔位置
php --ini
php -i | grep php.ini
```

MacOS

```bash
# 啟動服務
brew services start php@7.2
# 重新啟動
brew services restart php@7.4
# 停止服務
brew services stop php@7.2
```

```bash
# 切換版本
brew unlink php
brew link --overwrite --force php@8.1
```

# 設定檔

`php.ini`

```bash
# 查看PHP設定檔位置
php --ini
php -i | grep php.ini
```

`www.conf - {php.ini所在資料夾}/php-fpm.d/www.conf`

```conf
# 預設apache
user=nginx
group=nginx
```

# docker-compose 架設PHP開發環境

[Dockerise your PHP application with Nginx and PHP7-FPM](http://geekyplatypus.com/dockerise-your-php-application-with-nginx-and-php7-fpm/)

nginx 設定檔:

```conf
server {
	index index.php index.html;
	server_name php-docker.local;
	error_log  /var/log/nginx/error.log;
	access_log /var/log/nginx/access.log;
	# 工作目錄
	root /code;

	location ~ \.php$ {
		try_files $uri = 404;
		fastcgi_split_path_info ^(.+\.php)(/.+)$;
		fastcgi_pass php:9000;
		fastcgi_index index.php;
		include fastcgi_params;
		fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
		fastcgi_param PATH_INFO $fastcgi_path_info;
	}
}
```

docker-compose.yml文檔:

```yml
web:
	image: nginx:latest
	ports:
		- 80:80
	volumes:
		- ./code:/code
		- ./default.conf:/etc/nginx/conf.d/default.conf
	links:
		- php
php:
	image: php:7-fpm
	volumes:
		- ./code:/code
```

# PHP擴展庫 PECL

```
PECL是一個PHP擴展庫，提供了一個PHP所有已知擴展的下載和託管目錄。
```

[PECL官網](https://pecl.php.net/)

```bash
# 顯示pear設置 若無帶 variable 則顯示全部 - [詳細變數說明](https://pear.php.net/manual/en/guide.users.commandline.# config.php)
pear config-show 'variable'

# 查看命令說明 若無帶 command 顯示命令列表
pear help 'command'

# 安裝 套件包
pear install 'package'-'version'
	-f 強制

	# version 可為
	# 	stable
	# 	beta
	# 	alpha
	# 	devel
	# 	1.2.3(版本號)

# 更新 套件包
pear upgrade 'package'-'version'
	-f 強制

	# version 可為
	# 	stable
	# 	beta
	# 	alpha
	# 	devel
	# 	1.2.3(版本號)

# 離線安裝 套件包
pear download Foo
pear install Foo-1.2.3.tgz

# 取得套件包信息
pear info 'package'

# 顯示套件包安裝的檔案列表
pear list-files 'package'

# 顯示已安裝的套件包
pear list
```

# 基本概念

## 型別轉換

### 使用 `C 語言`的 type casting 語法

1. (int)，(integer) - 轉換成 integer 型別
2. (bool)，(boolean) - 轉換成 boolean 型別
3. (float)，(double)，(real) - 轉換成 float 型別
4. (string) - 轉換成 string 型別
5. (array) - 轉換成 array 型別
6. (object) - 轉換成 object 型別

### 使用 `settype` 函數

```php
int settype(string var, string type);
```
將 `let` `var` 變數轉換為 `type` 型別，可指定的型別參數有：

1. "boolean" (PHP 4.2.0 與之後的版本也可以簡寫成 "bool")
2. "integer" (PHP 4.2.0 與之後的版本也可以簡寫成 "int")
3. "float" (PHP 4.2.0 與之後的版本才支援)
4. "double"
5. "string"
6. "array"
7. "object"
8. "null" (PHP 4.0.8 以後的版本才支援)

```
轉換成功傳回 true，否則傳回 false。
當不知道某個變數的值是什麼型別時，可以利用 gettype() 函數來取得。
```

## 比較運算子
範例 | 名稱 | 解釋
$a == $b | 判斷等於 | 如果 $a 等於 $b，則結果為真
$a === $b | 全等 | 如果 $a 等於 $b，並且它們的類型也相同，則結果為真
$a != $b | 不等 | 如果 $a 不等於 $b，則結果為真
$a <> $b | 不等 | 如果 $a 不等於 $b，則結果為真
$a !== $b | 非全等 | 如果 $a 不等於 $b，或者它們的類型不同，則結果為真
$a < $b | 小與 | 如果 $a 小於 $b，則結果為真
$a > $b | 大於 | 如果 $a 大於 $b，則結果為真
$a <= $b | 小於等於 | 如果 $a 小於或者等於 $b，則結果為真
$a >= $b | 大於等於 | 如果 $a 大於或者等於，則結果為真

## 條件式

```
if (條件式) {
	述句;
} elseif (條件式) {
	述句;
} else {
	述句;
}
```

## 迴圈

```
<?php
while ( 條件判斷 ) {
　...some code...
}
?>

<?php
for ( 初始值 ; 比對運算式 ; 初始值+步進值  ) {
　... some code ...
}
?>
```

foreach

```
foreach(array_expression as $value) statement
```

```
foreach(array_expression as $key => $value) st
```

## 物件

```
類別(class):

	定義物件的變數與功能

方法(method):

	類別中定義的功能

屬性(property):

	類別中定義的變數

實例(instance):

	由類別實作出的實體

建構子(Constructor):

	物件實例化時,建構子會被呼叫一個特別方法,建構子會設定好物件的屬性還有一些初始工作使物件可用

靜態方法(static method):

	物件未被實例化即可使用,無法使用屬性值
```

## 正則表達式

```php
// 執行一個正則表達式搜索和替換
preg_filter()

// 返回匹配模式的數組條目
preg_grep()

// 返回最後一個PCRE正則執行產生的錯誤代碼
preg_last_error()

// 執行一個全局正則表達式匹配
preg_match_all()

// 執行一個正則表達式匹配
// 匹配則返回1 不匹配則返回0 錯誤返回false
$result = preg_match($pat, $target, $matches);

// 轉義正則表達式字符
preg_quote()

// 執行一個正則表達式搜索並且使用一個回調進行替換
preg_replace_callback_array()

// 執行一個正則表達式搜索並且使用一個回調進行替換
preg_replace_callback()

// 執行一個正則表達式的搜索和替換
// 匹配則返回替換後內容 不匹配則返回原本內容 錯誤返回NULL
$result = preg_replace($pat, $new, $target);

// 通過一個正則表達式分隔字符串
preg_split()
```

# 技巧

## 快速替換程式建議（可丟進 log/debug 用）

這樣可以快速產出含值的 SQL 字串，方便貼去資料庫測試。

```php
$sql = $query->toSql();
$bindings = $query->getBindings();

foreach ($bindings as $binding) {
    $sql = preg_replace('/\?/', is_numeric($binding) ? $binding : "'$binding'", $sql, 1);
}

dd($sql);
```

## 大量資料 避免記憶體限制使用 chunk 批次處理

只適合只讀操作（例如查詢、彙整報表等），不要在 chunk 內執行 update 或 delete。

```php
function chunkedQueryToArray($query, $chunkSize = 500): array
{
    $results = [];

    $query->chunk($chunkSize, function ($rows) use (&$results) {
        foreach ($rows as $row) {
            $results[] = $row;
        }
    });

    return $results;
}
```

# 例外狀況

## MacOS - dyld[60237]: Library not loaded: /usr/local/opt/icu4c/lib/libicuio.70.dylib

重新安裝 ICU4C

```bash
brew reinstall icu4c
```
