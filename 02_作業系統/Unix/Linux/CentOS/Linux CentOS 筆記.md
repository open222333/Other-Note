# Linux CentOS 筆記

## 參考資料

[CentOS 7.0 不完全安裝手冊 - Step 8 設定「軟體選擇」](http://blog.itist.tw/2014/08/centos7-install08.html)

[CentOS 官方](https://wiki.centos.org/HowTos/SELinux)

[CentOS Stream(不穩定的測試版本)](https://www.centos.org/centos-stream/)

```
在 Red Hat Enterprise Linux (RHEL) 開發之前持續交付的發行版，定位為 Fedora Linux 和 RHEL 之間的中游。
```

## linux 工具 yum(軟體套件管理工具)

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

---
## 大流量網站使用 linux CentOS7 BBR

[Bottleneck Bandwidth and RTT](https://www.vultr.com/docs/how-to-deploy-google-bbr-on-centos-7)


## 指令 升級內核

為了使用 BBR，需要將 CentOS 7 機器的內核升級到 4.9.0。
(linode 可在Linodes - Configurations - Edit 下更改內核版本)

```bash
# 升級前可以先看看當前內核
uname -r

	# 此命令應輸出類似於以下內容的字符串：
	# 3.10.0-514.2.2.el7.x86_64

# 使用 ELRepo 存儲庫安裝 4.9.0 內核：
sudo rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
sudo rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-2.el7.elrepo.noarch.rpm

# 使用 ELRepo 存儲庫安裝 4.9.0 內核：
sudo yum --enablerepo=elrepo-kernel install kernel-ml -y

# 確認結果：
rpm -qa | grep kernel

    # 如果安裝成功，您應該kernel-ml-4.9.0-1.el7.elrepo.x86_64在輸出列表中看到：

    # kernel-ml-4.9.0-1.el7.elrepo.x86_64
    # kernel-3.10.0-514.el7.x86_64
    # kernel-tools-libs-3.10.0-514.2.2.el7.x86_64
    # kernel-tools-3.10.0-514.2.2.el7.x86_64
    # kernel-3.10.0-514.2.2.el7.x86_64

# 顯示 grub2 菜單中的所有條目：
sudo egrep ^menuentry /etc/grub2.cfg | cut -f 2 -d \'

    # 結果應該類似於：
    #     CentOS Linux 7 Rescue a0cbf86a6ef1416a8812657bb4f2b860 (4.9.0-1.el7.elrepo.x86_64)
    #     CentOS Linux (4.9.0-1.el7.elrepo.x86_64) 7 (Core)
    #     CentOS Linux (3.10.0-514.2.2.el7.x86_64) 7 (Core)
    #     CentOS Linux (3.10.0-514.el7.x86_64) 7 (Core)
    #     CentOS Linux (0-rescue-bf94f46c6bd04792a6a42c91bae645f7) 7 (Core)

# 索引從 開始0。這意味著 4.9.0 內核位於1：
sudo grub2-set-default 1

# 重新啟動系統：
sudo shutdown -r now


# 啟用 BBR
# 為了啟用 BBR 算法，需要修改sysctl配置如下：
echo 'net.core.default_qdisc=fq' | sudo tee -a /etc/sysctl.conf
echo 'net.ipv4.tcp_congestion_control=bbr' | sudo tee -a /etc/sysctl.conf
sudo sysctl -p

# 使用以下命令確認 BBR 已啟用
sudo sysctl net.ipv4.tcp_available_congestion_control

    # 輸出應類似於：
    # net.ipv4.tcp_available_congestion_control = bbr cubic reno

# 驗證
sudo sysctl -n net.ipv4.tcp_congestion_control

    # 輸出：
    # bbr

# 檢查內核模塊是否已加載
lsmod | grep bbr

    # 輸出將類似於：
    # tcp_bbr                16384  0
```