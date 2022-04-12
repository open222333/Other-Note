# PHP 筆記

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

## 安裝步驟

CentOS7
```bash

yum install epel-release -y
# rpm-gpg-key
yum install http://rpms.remirepo.net/enterprise/remi-release-7.rpm -y

# 列出並選擇可以安裝的 php 版本
yum search php

# 安裝 php74
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
```

MacOS [使用brew 下載安裝](https://formulae.brew.sh/formula/php#default)

```bash
# 添加 環境變數到 .bash_profile
echo 'export PATH="/usr/local/opt/php@{version版本}/bin:$PATH"' >> ~/.bash_profile
echo 'export PATH="/usr/local/opt/php@{version版本}/sbin:$PATH"' >> ~/.bash_profile
```

# php 指令

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

# 設定檔

php.ini

```bash
# 查看PHP設定檔位置
php --ini
php -i | grep php.ini
```

```ini
```

www.conf - {php.ini所在資料夾}/php-fpm.d/www.conf

```conf
# 預設apache
user=nginx
group=nginx
```

## 架設開發環境 安裝 php-fpm

[官方 安裝教學(包含各種web server)](https://www.php.net/manual/en/install.php)

[FPM 配置文檔 參數 php-fpm.conf](https://www.php.net/manual/en/install.fpm.configuration.php)

## docker-compose 架設開發環境

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

# 軟體包管理系統 composer

```
提供用於管理PHP軟體和依賴庫關係的標準格式。
```

[官方網站](https://getcomposer.org/)

[下載教學](https://getcomposer.org/download/)

[原始碼](https://github.com/composer/composer)

[指令列表](https://getcomposer.org/doc/03-cli.md)

## 安裝步驟(官方建議建立腳本)

```bash
# 下載安裝程序到當前目錄
php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"

# 驗證安裝程序 SHA-384(不一定要)
php -r "if (hash_file('sha384', 'composer-setup.php') === '906a84df04cea2aa72f40b5f787e49f22d4c2f19492ac310e8cba5b96ac8b64115ac402c8cd292b8a03482574915d1a8') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"

# 運行安裝程序
php composer-setup.php

# 刪除安裝程序
php -r "unlink('composer-setup.php');"

# 建立指令
sudo mv composer.phar /usr/local/bin/composer
```

[sh腳本 安裝Composer](https://getcomposer.org/doc/faqs/how-to-install-composer-programmatically.md)

```sh
### sh範例
#!/bin/sh

EXPECTED_CHECKSUM="$(php -r 'copy("https://composer.github.io/installer.sig", "php://stdout");')"
php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"
ACTUAL_CHECKSUM="$(php -r "echo hash_file('sha384', 'composer-setup.php');")"

if [ "$EXPECTED_CHECKSUM" != "$ACTUAL_CHECKSUM" ]
then
	>&2 echo 'ERROR: Invalid installer checksum'
	rm composer-setup.php
	exit 1
fi

php composer-setup.php --quiet
RESULT=$?
rm composer-setup.php
exit $RESULT
```

自動確認目前系統環境是否可使用這個套件(前提是套件作者有設定)，或是有沒有與其它套件衝突。

[composer --prefer-dist 和 --prefer-source 的區別](https://www.itread01.com/content/1545115698.html)

基本指令：

```sh
# 安裝 composer.lock 中指定的套件及版本。
composer install
	# --ignore-platform-reqs

# 讀取 composer.json 去下載指定的套件及版本，完成後會自動產生 composer.lock
composer update
	# --ignore-platform-reqs

# 更新某一套件到最新版本
composer update 套件提供者/套件名稱:套件版本

# 安裝專案 套件提供者/套件名稱:套件版本
composer require 套件提供者/套件名稱:套件版本
	# --prefer-dist
	# 	會從github上下載.zip壓縮包，並快取到本地。

	# --prefer-source
	# 	會從github上克隆原始碼，不會在本地快取。

	# --with-all-dependencies , -W
	#	允許對當前鎖定到特定版本的軟件包進行升級、降級和刪除。
```

安裝完成後，它會在當前資料夾下建立 composer.json、composer.lock 及 vendor/ 的資料夾。

* composer.json 及 composer.lock 要加入版本控制

* vendor/ 資料夾不要加入版本控制

```sh
# 建立自己的 package
composer init
```

`composer` 會詢問一些問題：專案名稱、專案類型、作者、授權條款，並且詢問是否需要安裝既有的套件。

```sh
# 安裝composer.json內紀錄框架所需的相依套件
composer install
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

## 特殊 浮點數比較方法

[php 浮點數比較方法](https://www.twblogs.net/a/5c6f8455bd9eee7f07323dbb)

## 伺服器變數 $_SERVER

[$_SERVER內容](https://www.php.net/manual/zh/reserved.variables.server.php)
