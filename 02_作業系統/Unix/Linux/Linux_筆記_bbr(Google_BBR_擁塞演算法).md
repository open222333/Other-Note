# Linux 筆記 bbr(Google BBR 擁塞演算法)

```
TCP BBR（Bottleneck Bandwidth and Round-trip propagation time）是由Google設計，於2016年發布的擁塞演算法。
以往大部分擁塞演算法是基於丟包來作為降低傳輸速率的訊號，而BBR則基於模型主動探測。
該演算法使用網路最近出站資料分組當時的最大頻寬和往返時間來建立網路的顯式模型。
封包傳輸的每個累積或選擇性確認用於生成記錄在封包傳輸過程和確認返回期間的時間內所傳送資料量的取樣率。
```

## 目錄

- [Linux 筆記 bbr(Google BBR 擁塞演算法)](#linux-筆記-bbrgoogle-bbr-擁塞演算法)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [部署步驟 CentOS7](#部署步驟-centos7)
		- [第一步 安裝內核](#第一步-安裝內核)
		- [啟用 BBR](#啟用-bbr)
- [部署步驟 CentOS6](#部署步驟-centos6)

## 參考資料

[Github google/bbr](https://github.com/google/bbr)

[Linux都該安裝的好東西 Google BBR](https://medium.com/kaito-blog-%E6%B5%B7%E6%96%97%E6%A8%A3-%E3%81%AE-it%E5%AE%85/google-bbr-e39e0df69e3b)

# 部署步驟 CentOS7

```
BBR TCP加速，需 kernel 4.9以上。
```

[How to Deploy Google BBR on CentOS 7](https://www.vultr.com/docs/how-to-deploy-google-bbr-on-centos-7)

### 第一步 安裝內核

```bash
# 確認 kernel
uname -r

# 安裝 ELRepo
rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-2.el7.elrepo.noarch.rpm

# 安裝 kernel
yum --enablerepo=elrepo-kernel install kernel-ml -y

# 確認結果
rpm -qa | grep kernel

# 顯示 grub2 菜單中的所有條目
egrep ^menuentry /etc/grub2.cfg | cut -f 2 -d \'

# 索引從 0 開始。這意味著 內核位於 1
grub2-set-default 1

# 重啟伺服器
shutdown -r now
```

### 啟用 BBR

```bash
# 修改sysctl配置 (主要)
echo 'net.core.default_qdisc=fq' | sudo tee -a /etc/sysctl.conf
echo 'net.ipv4.tcp_congestion_control=bbr' | sudo tee -a /etc/sysctl.conf
sysctl -p

# 驗證 BBR 是否已啟用
sysctl net.ipv4.tcp_available_congestion_control
# 輸出應類似於：
# net.ipv4.tcp_available_congestion_control = bbr cubic reno

sysctl -n net.ipv4.tcp_congestion_control
# 輸出應類似於：
# bbr

lsmod | grep bbr
# 輸出應類似於：
# tcp_bbr                16384  0
```

# 部署步驟 CentOS6

[CentOS6開啟BBR加速](https://www.796t.com/content/1549300867.html)

```bash
# 安裝 elrepo 擴充套件源 - 預設yum沒有kernel 4.9的源
rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
rpm -Uvh http://www.elrepo.org/elrepo-release-6-6.el6.elrepo.noarch.rpm

vi  /etc/yum.repos.d/elrepo.repo
# 修改 `[elrepo-kernel]` 的 `enabled=0` 為 `enabled=1`

# 下載
yum install kernel-ml -y

# grub2-set-default 0 是CentOS7的命令，會報錯 command not found
sed -i 's/^default=.*/default=0/g' /boot/grub/grub.conf

# 重啟伺服器
reboot

# 顯示的版本號 ≥4.9 就OK
uname -a

# 啟用bbr
echo "net.core.default_qdisc=fq" >> /etc/sysctl.conf
echo "net.ipv4.tcp_congestion_control=bbr" >> /etc/sysctl.conf

# 重啟伺服器
reboot

# 檢視是否開啟成功
sysctl net.ipv4.tcp_available_congestion_control
# 輸出應類似於：
# bbr
lsmod | grep bbr
# 輸出應類似於：
# tcp_bbr                16384  0
```