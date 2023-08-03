# Linux CentOS 筆記

```
```

## 目錄

- [Linux CentOS 筆記](#linux-centos-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [linux 工具 yum(軟體套件管理工具)](#linux-工具-yum軟體套件管理工具)
- [大流量網站使用 linux CentOS7 BBR](#大流量網站使用-linux-centos7-bbr)
- [SELinux工具程式](#selinux工具程式)
- [CentOS 7 網路指令](#centos-7-網路指令)
- [例外狀況](#例外狀況)
	- [curl: (35) Cannot communicate securely with peer: no common encryption algorithm(s).](#curl-35-cannot-communicate-securely-with-peer-no-common-encryption-algorithms)

## 參考資料

[CentOS 7.0 不完全安裝手冊 - Step 8 設定「軟體選擇」](http://blog.itist.tw/2014/08/centos7-install08.html)

[CentOS 官方](https://wiki.centos.org/HowTos/SELinux)

[CentOS Stream(不穩定的測試版本)](https://www.centos.org/centos-stream/)

```
在 Red Hat Enterprise Linux (RHEL) 開發之前持續交付的發行版，定位為 Fedora Linux 和 RHEL 之間的中游。
```

# linux 工具 yum(軟體套件管理工具)

```
更新下載工具為 dnf
```

```bash
# 安裝 指定的安裝包package
yum install package

# 全部更新
yum update

# 更新指定程式包package
yum update package

# 檢查可更新的程式
yum check-update

# 升級指定程式包package
yum upgrade package

# 列出所有可以安裝或更新的包的資訊
yum info

# 顯示安裝包資訊package
yum info package

# 顯示所有已經安裝和可以安裝的程式包
yum list

# 顯示指定程式包安裝情況package
yum list package

# 搜尋匹配特定字元的package的詳細資訊
yum search package

# 刪除程式包package
yum remove | erase package

# 檢視程式package依賴情況
yum deplist package

# 清除快取目錄下的軟體包
yum clean packages

# 清除快取目錄下的 headers
yum clean headers

# 清除快取目錄下舊的 headers
yum clean oldheaders

# 清除快取目錄下的軟體包及舊的headers
yum clean, yum clean all
```

# 大流量網站使用 linux CentOS7 BBR

[Bottleneck Bandwidth and RTT](https://www.vultr.com/docs/how-to-deploy-google-bbr-on-centos-7)

```
為了使用 BBR，需要將 CentOS 7 機器的內核升級到 4.9.0。
(linode 可在Linodes - Configurations - Edit 下更改內核版本)
```

```bash
### 安裝內核 ###
# 查看內核(kernel) 列出類似 3.10.0-514.2.2.el7.x86_64
uname -r

# 安裝 ELRepo 存儲庫 , Install the ELRepo repo
sudo rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
sudo rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-2.el7.elrepo.noarch.rpm

# Install the 4.9.0 kernel using the ELRepo repo (使用 ELRepo 存儲庫安裝 4.9.0 內核):
sudo yum --enablerepo=elrepo-kernel install kernel-ml -y

# Confirm the result:
# The result should resemble (印出以下結果):
# kernel-ml-4.9.0-1.el7.elrepo.x86_64
# kernel-3.10.0-514.el7.x86_64
# kernel-tools-libs-3.10.0-514.2.2.el7.x86_64
# kernel-tools-3.10.0-514.2.2.el7.x86_64
# kernel-3.10.0-514.2.2.el7.x86_64
rpm -qa | grep kernel

# Show all entries in the grub2 menu (顯示 grub2 菜單中的所有條目):
# The result should resemble (印出以下結果):
# CentOS Linux 7 Rescue a0cbf86a6ef1416a8812657bb4f2b860 (4.9.0-1.el7.elrepo.x86_64)
# CentOS Linux (4.9.0-1.el7.elrepo.x86_64) 7 (Core)
# CentOS Linux (3.10.0-514.2.2.el7.x86_64) 7 (Core)
# CentOS Linux (3.10.0-514.el7.x86_64) 7 (Core)
# CentOS Linux (0-rescue-bf94f46c6bd04792a6a42c91bae645f7) 7 (Core)
sudo egrep ^menuentry /etc/grub2.cfg | cut -f 2 -d \'

# 根據上面列出的列表 輸入數字選擇要的內核 第一個是0
sudo grub2-set-default 1

# 重啟伺服器
sudo shutdown -r now

### Enable BBR 啟用BBR ###
# In order to enable the BBR algorithm, you need to modify the sysctl configuration as follows:
# 為了啟用 BBR 算法，需要修改 sysctl 配置
echo 'net.core.default_qdisc=fq' | sudo tee -a /etc/sysctl.conf
echo 'net.ipv4.tcp_congestion_control=bbr' | sudo tee -a /etc/sysctl.conf
sudo sysctl -p

# confirm that BBR is enabled:
# 確認 BBR 已啟用
# The output should resemble (印出以下結果):
# net.ipv4.tcp_available_congestion_control = bbr cubic reno
sudo sysctl net.ipv4.tcp_available_congestion_control

# 驗證
# 輸出：
# bbr
sudo sysctl -n net.ipv4.tcp_congestion_control



# 檢查內核模塊是否已加載
# 輸出將類似於：
# tcp_bbr                16384  0
lsmod | grep bbr
```

# SELinux工具程式

[CentOS 官方](https://wiki.centos.org/HowTos/SELinux)

```bash
### semanage(SELinux工具程式 CentOS 防火牆 功能(預設開啟)) ###

# -m為修改，添加tcp port 5000到http_port_t
semanage port -m -t http_port_t -p tcp 3128

# -a 添加 啟動的端口加入到端口列表中
semanage port -a -t http_port_t -p tcp 3128

# 查看http允許訪問的端口：
semanage port -l | grep http_port_t

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

# CentOS 7 網路指令

```bash
###  ###
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

# 例外狀況

## curl: (35) Cannot communicate securely with peer: no common encryption algorithm(s).

```bash
yum update curl -y
```