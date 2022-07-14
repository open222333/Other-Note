# Linux 工具 ulimit(使用者資源限制)

```
可以設定或者彙報當前使用者資源限制的命令。
需要有管理員許可權
它只能在允許使用shell進行控制的系統中使用。
也就是說它已經被嵌入到shell當中了。
```

## 參考資料

[Linux ulimit使用](https://iter01.com/585301.html)

[Linux ulimit Command](https://linuxhint.com/linux_ulimit_command/)

# 指令

```bash
# 展示出詳細的引數，即可以對什麼資源做限制。
# 這裡的限制有兩種型別：soft & hard。
# hard資源限制意味著是物理限制；
# soft資源限制是由使用者進行管理的，soft的最大值由hard來限制。
ulimit -a

# 檢視core file檔案的最大值
ulimit -c

# 檢視資料段的最大值
ulimit -d

# 檢視當前使用者的最大排程優先順序
ulimit -e

# 當前使用者的最大棧大小
ulimit -s

# 當前使用者的最大程式數
ulimit -u

# 檢視虛擬記憶體的大小
ulimit -v

# 檢視socket buffer的大小
ulimit -b

# 檢視每個程式允許執行的時間
ulimit -t

# 檢視一個程式可以最多有多少檔案描述符
ulimit -n
```

# 配置文檔

系統資源被定義在了`/etc/security/limits.conf`的檔案當中

```bash
vim /etc/security/limits.conf
	按照如下的格式編輯檔案
	<domain> <type> <item> <value>

	domain:
		一個特定的使用者
		一個組
		wildcard（* and %）
	type:
		soft 限制
		hard 限制
	item:
		core：core檔案大小（KB）
		data：最大資料大小（KB）
		fsize：最大檔案大小（KB）
		memlock：最大locked-in-memory地址空間（KB）
		nofile：最大的open files的數目
		rss：最大的resident set大小（KB）
		stack：最大棧大小（KB）
		cpu：最大cpu時間（分鐘）
		nproc：最大程式數
		as：地址空間的限制（KB）
		maxlogins：當前使用者的最大登陸數目
		maxsyslogins：當前系統的最大登陸數目
		priority：跑使用者程式的優先順序
		locks：使用者可以持有的file locks的數目
		sigpending：最大的pending signals的數目
	value:
		具體的整數值
```