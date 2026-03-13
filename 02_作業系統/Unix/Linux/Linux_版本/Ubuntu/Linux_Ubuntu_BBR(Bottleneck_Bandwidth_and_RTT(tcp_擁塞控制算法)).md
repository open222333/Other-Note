# Linux Ubuntu BBR(Bottleneck Bandwidth and RTT(tcp 擁塞控制算法))

```
apt-get(軟體套件管理工具)
```

## 目錄

- [Linux Ubuntu BBR(Bottleneck Bandwidth and RTT(tcp 擁塞控制算法))](#linux-ubuntu-bbrbottleneck-bandwidth-and-rtttcp-擁塞控制算法)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
  - [BBR on Ubuntu 24.04](#bbr-on-ubuntu-2404)

## 參考資料

[How to Enable BBR on Ubuntu 24.04](https://wiki.crowncloud.net/?How_to_enable_BBR_on_Ubuntu_24_04)

# 指令

## BBR on Ubuntu 24.04

检查内核版本

BBR 需要 Linux 内核 4.9 或更高版本

```sh
uname -r
```

啟用 BBR

編輯 sysctl.conf 文件

```sh
nano /etc/sysctl.conf
```

添加

```
net.core.default_qdisc=fq
net.ipv4.tcp_congestion_control=bbr
```

保存並更改 (Ctrl+X, Y, Enter)

應用更改

```sh
sysctl -p
```

查看

```sh
sysctl net.ipv4.tcp_congestion_control
```

應顯示 bbr

```
bbr
```

```sh
lsmod | grep bbr
```
