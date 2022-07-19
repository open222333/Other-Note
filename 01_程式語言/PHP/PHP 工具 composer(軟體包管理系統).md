# PHP 工具 composer(軟體包管理系統)

```
提供用於管理PHP軟體和依賴庫關係的標準格式。
```

## 目錄

- [PHP 工具 composer(軟體包管理系統)](#php-工具-composer軟體包管理系統)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟(官方建議建立腳本)](#安裝步驟官方建議建立腳本)

## 參考資料

[官方網站](https://getcomposer.org/)

[下載教學](https://getcomposer.org/download/)

[原始碼](https://github.com/composer/composer)

[指令列表](https://getcomposer.org/doc/03-cli.md)

# 安裝步驟(官方建議建立腳本)

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