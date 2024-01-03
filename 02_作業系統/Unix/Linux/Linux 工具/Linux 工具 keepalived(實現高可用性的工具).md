# Linux 工具 keepalived(實現高可用性的工具)

```
實現高可用性的工具，通常用於確保多個伺服器之間的故障轉移。
```

## 目錄

- [Linux 工具 keepalived(實現高可用性的工具)](#linux-工具-keepalived實現高可用性的工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
- [設定](#設定)
  - [配置虛擬 IP（VIP）](#配置虛擬-ipvip)
  - [配置節點優先權](#配置節點優先權)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[keepalived 官網](https://www.keepalived.org/)

[Keepalive wiki](https://zh.wikipedia.org/zh-tw/Keepalive)

# 安裝

## Debian (Ubuntu)

```bash
apt-get -y update
apt-get -y install keepalived
```

## RedHat (CentOS)

```bash
yum -y install keepalived
```

# 設定

通常在`/etc/keepalived/keepalived.conf`

## 配置虛擬 IP（VIP）

在設定檔中新增虛擬 IP 位址。

這是在故障切換時移動到新主機的 IP 位址。

```
vrrp_instance VI_1 {
    state MASTER
    interface eth0
    virtual_router_id 51
    priority 100
    advert_int 1
    virtual_ipaddress {
        192.168.1.100
    }
}
```

## 配置節點優先權

配置每個節點的優先級，以確定哪個節點將首先成為 MASTER。

更高的優先權意味著更有可能成為 MASTER。

```
vrrp_instance VI_1 {
    state MASTER
    interface eth0
    virtual_router_id 51
    priority 100
    advert_int 1
    virtual_ipaddress {
        192.168.1.100
    }
}
```

# 指令

## 服務操作

```bash
# 啟動服務
systemctl start keepalived

# 查詢啟動狀態
systemctl status keepalived

# 重新啟動
systemctl restart keepalived

# 停止服務
systemctl stop keepalived

# 開啟開機自動啟動
systemctl enable keepalived

# 關閉開機自動啟動
systemctl disable keepalived

### 不是所有的服務都支持 ###
# (start, stop, restart, try-restart, reload, force-reload, status)
# 重新載入
service keepalived reload
```
