# Python 模組-內建 socket(底層網絡接口)

```
柏克萊軟體套件（英語：Berkeley Software Distribution，縮寫：BSD；
也被稱為柏克萊Unix或Berkeley Unix）是一個衍生自Unix（類Unix）的作業系統

這個模塊提供了訪問 BSD 的接口。
在所有現代 Unix 系統、Windows、macOS 和其他一些平台上可用。
```

## 目錄

- [Python 模組-內建 socket(底層網絡接口)](#python-模組-內建-socket底層網絡接口)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [應用相關](#應用相關)
- [用法](#用法)
	- [通過UDP獲取本機IP](#通過udp獲取本機ip)

## 參考資料

[socket 官方文檔](https://docs.python.org/zh-tw/3/library/socket.html)

### 應用相關

[Python 获取本机内网IP](https://www.cnblogs.com/lianshuiwuyi/p/11636876.html)

# 用法

## 通過UDP獲取本機IP

```Python
import socket
'''
此方法利用UDP協議，生成一個UDP包，將自己的IP放入UDP協議頭中，然後再從中獲取本機的IP。
此方法雖然不會真實向外發包，但仍然會申請一個UDP的端口，所以如果頻繁調用的話也是比較耗時的；可以將查詢到的IP緩存在別處以供使用。
'''
 
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(('8.8.8.8', 80))
    ip = s.getsockname()[0]
finally:
    s.close()
return ip
```
