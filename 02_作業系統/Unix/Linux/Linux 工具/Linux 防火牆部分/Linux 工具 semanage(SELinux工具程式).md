# Linux 工具 semanage(SELinux工具程式)

```
Semanage是用於配置SELinux策略某些元素而無需修改或重新編譯策略源的工具。
```

## 目錄

- [Linux 工具 semanage(SELinux工具程式)](#linux-工具-semanageselinux工具程式)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[semanage(8) - Linux man page](https://linux.die.net/man/8/semanage)

# 指令

```bash
semanage {login|user|port|interface|fcontext|translation} -l
semanage fcontext -{a|d|m} [-frst] file_spec
	-l：查询。
	fcontext：主要用在安全上下文方面。
	-a：增加，你可以增加一些目录的默认安全上下文类型设置。
	-m：修改。
	-d：删除。

### semanage(SELinux工具程式 CentOS 防火牆 功能(預設開啟)) ###

# -m為修改，添加tcp port 5000到http_port_t
semanage port -m -t http_port_t -p tcp 3128

# -a 添加 啟動的端口加入到端口列表中
semanage port -a -t http_port_t -p tcp 3128

# 查看http允許訪問的端口：
semanage port -l | grep http_port_t
```

```bash
# SELinux 設置為寬容模式，方便調試：
sudo setenforce 0

### === 關閉 SELinux === ###
vim /etc/selinux/config

# 關閉 SELinux：
#     SELINUX=enforce
#     改成:
#     SELINUX=disabled

#	enforcing：強制模式，SELinux 執行中，依 SELinux 設定限制存取
#	permissive：寬容模式：SELinux 執行中，但僅顯示警告，而不限制存取
#	disabled：關閉，SELinux 未執行

# 將 SELINUXTYPE=targeted 註釋

# 儲存後離開編輯器, 需要重新開機設定才會生效。

# 檢查SELinux的狀態
sestatus
```