# Linux 基本 網路相關

## `參考資料:`

[RHEL / CentOS 8 重新啟動網路](https://www.opencli.com/linux/rhel-centos8-restart-network)

[CentOS 官方](https://wiki.centos.org/FrontPage)

[CentOS SELinux](https://wiki.centos.org/HowTos/SELinux)

## `ssh遠端設定:`

[How to Enable SSH on CentOS 8](https://linuxhint.com/enable_ssh_centos8/)

[Linux：修改ssh設定、root登入、更改port、密碼登入](https://www.ewdna.com/2012/05/linuxsshrootport.html)

[CentOS 7 文字模式下更改為固定 IP](https://ithelp.ithome.com.tw/articles/10214435)

# CentOS

[CentOS 官方](https://wiki.centos.org/HowTos/SELinux)

```bash
### semanage(SELinux工具程式 CentOS 防火牆 功能(預設開啟)) ###

# 通過將-a選項替換-m為修改，添加tcp port 5000到http_port_t
semanage port -m -t http_port_t -p tcp 5000

# 啟動的端口加入到如上端口列表中
semanage port -a -t http_port_t -p tcp 8090

# 查看http允許訪問的端口：
semanage port -l | grep http_port_t

# SELinux 設置為寬容模式，方便調試：
sudo setenforce 0

vim /etc/selinux/config

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

### CentOS 7 ###
# 重新啟動網路
systemctl restart network

# 查看網卡資訊
ifconfig

# 啟動網卡介面
ifup [ifcsg]

# 關閉網卡介面
ifdown [ifcsg]

### CentOS 8 ###
# 重新啟動 NetworkManager, 這時便會一同重新啟動網路
systemctl restart NetworkManager.

# nmcli是NetworkManager的指令工具
# 停止及開啟網路的指令:
nmcli networking off
nmcli networking on
```

# 要確定對方主機某一個埠 TCP port 是否有開啟

[Linux – Ping TCP port?](http://benjr.tw/91897)

```bash
# PaPing:http://code.google.com/p/paping
paping www.google.com -p 80 -c 4

# nc (或是 netcat)
nc -vz google.com 80

hping google.com -S -V -p 80

nmap -p 80 google.com
# 其他參數使用請參考 http://benjr.tw/28087

# Telnet
telnet www.google.com 23
```
