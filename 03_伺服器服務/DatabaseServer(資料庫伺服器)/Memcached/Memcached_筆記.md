# Memcached 筆記

```
```

## 目錄

- [Memcached 筆記](#memcached-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [Memcached開機啟動方式](#memcached開機啟動方式)
		- [檔案中追加啟動命令](#檔案中追加啟動命令)
		- [寫服務指令碼](#寫服務指令碼)
	- [設定文檔位置](#設定文檔位置)

## 參考資料

```
分散式的高速緩衝記憶體系統
```

[Memcached 教程](https://www.runoob.com/memcached/memcached-tutorial.html)

[Memcached開機啟動方式](https://wellbay.cc/thread-979423.htm)

[RHEL / CentOS 7 安裝 PHP Memcached 緩存](https://intone.cc/2018/05/rhel-centos-7-%E5%AE%89%E8%A3%9D-php-memcached-%E7%B7%A9%E5%AD%98/)


```bash
# Ubuntu/Debian安裝
sudo apt-get install memcached

# Redhat/Fedora/Centos安裝
yum install memcached

# 啟動服務
systemctl start memcached
# 查詢啟動狀態
systemctl status memcached
# 重新啟動
systemctl restart memcached
# 停止服務
systemctl stop memcached
# 設定開機自動啟動
systemctl enable memcached
# 取消開機自動啟動
systemctl disable memcached
```

## Memcached開機啟動方式

### 檔案中追加啟動命令

```bash
vim /etc/rc.d/rc.local
```

/etc/rc.d/rc.local

```
使用者最好是 apache 或 deamon -u

/usr/local/memcached/bin/memcached  -u root -d -m 2048 -l 192.168.137.99 -p 11211 -P /tmp/memcached.pid

不指定IP，預設是本機
/usr/local/memcached/bin/memcached  -u deamon -d -m 2048 -p 11211 -P /tmp/memcached.pid
```

### 寫服務指令碼

```bash
vim /etc/init.d/memcached
```

/etc/init.d/memcached

```conf
#!/bin/sh
#
# memcached:    MemCached Daemon
#
# chkconfig:    - 90 25
# description:  MemCached Daemon
#
# Source function library.

. /etc/rc.d/init.d/functions
. /etc/sysconfig/network

#[ ${NETWORKING} = "no" ] && exit 0
#[ -r /etc/sysconfig/dund ] || exit 0
#. /etc/sysconfig/dund
#[ -z "$DUNDARGS" ] && exit 0

MEMCACHED="/usr/local/memcached/bin/memcached"
SERVER_IP="192.168.137.98"
SERVER_PORT="11211"

[ -f $MEMCACHED ] || exit 1

start()
{
		echo -n $"Starting memcached: "
		daemon $MEMCACHED -u daemon -d -m 2048 -l $SERVER_IP -p $SERVER_PORT -P /tmp/memcached.pid
		echo
}
stop()
{
		echo -n $"Shutting down memcached: "
		killproc memcached
		echo
}

# See how we were called.
case "$1" in
	start)
		start
		;;
	stop)
		stop
		;;
	restart)
		stop
		sleep 3
		start
		;;
	*)
		echo $"Usage: $0 {start|stop|restart}"
		exit 1
esac
exit 0
```

```bash
## 設定啟動服務

# 增加執行許可權
chmod 755 /etc/init.d/memcached
# 新增memcached到服務項
chkconfig --add memcached
# 設定開機啟動
chkconfig --level 2345 memcached on
# 檢視是否設定成功
chkconfig --list memcached

## 服務管理命令

# 啟動memcached
service memcached start
# 關閉memcached
service memcached stop
# 重啟memcached
service memcached restart
```


## 設定文檔位置

```
# CentOS 預設
/etc/sysconfig/memcached
```

```conf
PORT="11211"
USER="memcached"
MAXCONN="1024"
CACHESIZE="512"
OPTIONS=""
```