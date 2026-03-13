# Linux 工具 ab(網站伺服器壓力測試工具)

```
ApacheBench是一個極為輕量級的壓力測試工具，通常只要電腦有安裝Apache伺服器就會有這個工具。
ApacheBench僅能測試一個網頁所能承受的負載能力，並無法模擬真正使用者操作網站的複雜動作。
```

## 目錄

- [Linux 工具 ab(網站伺服器壓力測試工具)](#linux-工具-ab網站伺服器壓力測試工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
	- [CentOS7](#centos7)
	- [Ubuntu](#ubuntu)
- [指令](#指令)

## 參考資料

`完整指令說明` : [ab - Apache HTTP server benchmarking tool](https://httpd.apache.org/docs/2.4/programs/ab.html)

[【軟體測試】使用 ApacheBench 進行網站伺服器壓力測試](https://blog.markgdi.com/article/stress-test-using-apache-bench/)

[ab - Apache HTTP 服務器基準測試工具](https://httpd.apache.org/docs/2.4/programs/ab.html)

[驗證 APACHE SOFTWARE FOUNDATION 版本](https://www.apache.org/info/verification.html)

[CentOS 下安装 ab 测试工具（Apache Benchmarking Tool）](https://www.jianshu.com/p/8bc6c26d5a88)

[编译安装apache](https://blog.51cto.com/lavender7n/2052260)

[To install Apache from Source Code on CentOS 6](https://www.linuxhelp.com/how-to-install-apache-from-source-code-on-centos-6)

# 安裝

## CentOS7

```bash
# 安裝
yum install httpd-tools -y

# 安裝 2.3以上
# 參考
# https://www.linuxhelp.com/how-to-install-apache-from-source-code-on-centos-6
# Download httpd
# https://httpd.apache.org/download.cgi#apache24
wget https://dlcdn.apache.org/httpd/httpd-2.4.54.tar.gz
tar -zxvf httpd-2.4.54.tar.gz

# Download apr
# https://apr.apache.org/download.cgi
wget https://dlcdn.apache.org//apr/apr-1.7.0.tar.gz
tar -zxvf apr-1.7.0.tar.gz

# Download apr-util
# https://apr.apache.org/download.cgi
wget https://dlcdn.apache.org//apr/apr-util-1.6.1.tar.gz
tar -zxvf apr-util-1.6.1.tar.gz

# 安裝pcre-devel pcre 的開發文件
# https://centos.pkgs.org/7/centos-x86_64/pcre-devel-8.32-17.el7.x86_64.rpm.html
yum install pcre-devel -y

# 安裝 expat 使用 expat 開發應用程序的庫和頭文件
# https://centos.pkgs.org/7/centos-x86_64/expat-devel-2.1.0-12.el7.x86_64.rpm.html
yum install expat-devel -y

# GNU Libtool是一種屬於GNU建構系統的GNU程式設計工具，用來產生可攜式的函式庫。
yum install libtool -y

# 安裝開發文件
yum install openssl-devel -y

mkdir httpd-2.4.54/srclib/apr
mkdir httpd-2.4.54/srclib/apr-util

cp -r apr-1.7.0/* httpd-2.4.54/srclib/apr
cp -r apr-util-1.6.1/* httpd-2.4.54/srclib/apr-util

# cd ~/httpd-2.4.54/srclib/apr
# ./configure --prefix=/usr/local/apr
# make && make install

# cd ~/httpd-2.4.54/srclib/apr-util
# ./configure --prefix=/usr/local/apr-util --with-apr=/usr/local/apr 
# make && make install

cd ~/httpd-2.4.54

# 編譯 Compile
# configure 裡面的腳本
./configure --prefix=/usr/local/apache2 --with-included-apr
# build
make
# install
make install

# 錯誤 Nothing to be done for `local-shared-build'.
# make clean all將刪除hello並需要重建
make clean

# 手動創建 httpd 指令(重新建立session)
vim /etc/profile.d/httpd.sh

pathmunge /usr/local/apache2/bin

# 創建初始化文檔
vim /etc/init.d/httpd

#!/bin/sh
#
# Startup script for the Apache Web Server
#
# chkconfig: 345 85 15
# description: Apache is a World Wide Web server.  It is used to serve
#          HTML files and CGI.
# processname: httpd
# pidfile: /var/run/httpd.pid
# config: /etc/httpd/conf/httpd.conf

# Source function library.
. /etc/rc.d/init.d/functions

# See how we were called.
case " $1"  in
start)
echo -n " Starting httpd: "
daemon /usr/local/apache2/bin/httpd -DSSL
echo
touch /var/lock/subsys/httpd

stop)
echo -n " Shutting down httpd: "
killproc httpd
echo
rm -f /var/lock/subsys/httpd
rm -f /var/run/httpd.pid

status)
status httpd

restart)
$0 stop
$0 start

reload)
echo -n " Reloading httpd: "
killproc httpd -HUP
echo

*)
echo " Usage: $0 {start|stop|restart|reload|status}"
exit 1
esac

exit 0

# 新增權限
chmod +x /etc/init.d/httpd
```

## Ubuntu

```bash
# 安裝
apt-get install apache2-utils
```

# 指令

```bash
ab http://127.0.0.1/
ab -c {同時進行的request數量} -t {時間} {url}
ab -c {同時進行的request數量} -n {次數} {url}
	-c concurrency
		併發
		同時向服務器端發送的請求數目，默認狀態下是一次 只執行一個http請求.
	-n requests
		總數
        執行一次測試會話的時候所發出的請求數目,默認是執行一個單一的請求

# 範例
ab -c 20 -t 30 http://127.0.0.1:8081/pull_all

# json格式文檔 POST
ab -c 100 -t 50 -T application/json -p query.json http://elastic:9200/index/_search

#這裡是版權宣告
This is ApacheBench, Version 2.0.40-dev <$Revision: 1.146 $> apache-2.0
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Copyright 2006 The Apache Software Foundation, http://www.apache.org/

Benchmarking xxx (be patient)
#這裡會將完成的req次數顯示出來，如果每超過5000會再次顯示
Finished 1028 requests

#主機資訊
Server Software:        Apache/2.2.3
Server Hostname:        xxx
Server Port:            80

Document Path:          /test/
#要注意，如果傳輸的資料大小超過1MB，表示使用ADSL的人多少會因為資料量感受到緩慢
#，就可能不會是準確的測試結果
Document Length:        8943 bytes

#這裡表示你下了 -c 20
Concurrency Level:      20
#總測試時間，應該不會跟-t的時間差太遠
Time taken for tests:   30.27095 seconds
Complete requests:      1028
#這裡的Fail表示在TCP階段就連線失敗，如果fail太多次，出來的數據絕對不正確
Failed requests:        0
Write errors:           0
Total transferred:      9478392 bytes
HTML transferred:       9197475 bytes
#每秒鐘的Request次數，可以視為效能的指標。因為這次測試我們使用了-c 20
#表示在20個人同時連線的情況下，還可以保持每秒34個request。
Requests per second:    34.24 [#/sec] (mean)
#表示這20個人裡「平均」每個人感受到的回應時間(不包括瀏覽器顯示出來的時間)
#約是584ms，也就是0.58秒。
Time per request:       584.185 [ms] (mean)
Time per request:       29.209 [ms] (mean, across all concurrent requests)
Transfer rate:          308.25 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0   49 382.7      0    3000
Processing:    91  519 1287.7    262   20913
Waiting:       90  518 1287.7    260   20912
Total:         91  569 1542.9    262   21543

#這個曲線圖比較重要，算是ab的數據價值所在。這裡表示了這20個人所感受到的
#回應速度曲線，可以從0.2秒到21秒不等。也就是說，在這裡約有20*90%=18人，
#他們感受到的速度會低於1秒，而其他人會高於1秒。這個比平均數值還要更能表達
#使用者大多都是感受到什麼速度，因為在伺服器很忙碌的情況下，會有像21秒這種
#數值，這是會大大地拖累平均速度及每秒request數。
Percentage of the requests served within a certain time (ms)
  50%    262
  66%    327
  75%    397
  80%    449
  90%    730
  95%   1338
  98%   5224
  99%   8504
100%  21543 (longest request)
```