# Linux 工具 Google BBR(擁塞演算法)

```
TCP BBR（Bottleneck Bandwidth and Round-trip propagation time）是由Google設計，於2016年發布的擁塞演算法。
以往大部分擁塞演算法是基於丟包來作為降低傳輸速率的訊號，而BBR則基於模型主動探測。
該演算法使用網路最近出站資料分組當時的最大頻寬和往返時間來建立網路的顯式模型。
封包傳輸的每個累積或選擇性確認用於生成記錄在封包傳輸過程和確認返回期間的時間內所傳送資料量的取樣率。
```

[Github google/bbr](https://github.com/google/bbr)

[Linux都該安裝的好東西 Google BBR](https://medium.com/kaito-blog-%E6%B5%B7%E6%96%97%E6%A8%A3-%E3%81%AE-it%E5%AE%85/google-bbr-e39e0df69e3b)

# 部署步驟

[How to Deploy Google BBR on CentOS 7](https://www.vultr.com/docs/how-to-deploy-google-bbr-on-centos-7)

## `CentOS 7`

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
# 修改sysctl配置
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