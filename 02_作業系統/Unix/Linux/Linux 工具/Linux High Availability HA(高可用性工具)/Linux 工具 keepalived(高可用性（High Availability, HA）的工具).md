# Linux 工具 keepalived(高可用性（High Availability, HA）的工具)

```
Keepalived 並不是只能在內網使用。
關鍵在於：Keepalived 使用的 VRRP（虛擬路由器冗餘協定）在公網上通常不可行或不安全

Keepalived 是一套 Linux 高可用性框架，主要提供：
    VRRP 虛擬 IP（VIP）高可用性
    服務健康檢查
    自動主備切換（Failover）
    負載平衡（搭配 LVS）

它最核心的用途就是：
    讓服務永遠有一個可以連的 IP，不管 Master 掛掉、重啟或網路異常，都會自動切換到備援節點。

最常用來做：
    Redis 主備（你正在做的 HA）
    MySQL/MariaDB 主備切換
    Nginx / HAProxy 前端 VIP
    檔案伺服器 HA（NFS/Gluster）
    任意 TCP/HTTP 服務 HA

只要想做：
    ✔ 有兩台以上伺服器
    ✔ 一台掛掉另一台會立刻接手
    ✔ 客戶端永遠只連 VIP 不需要重新設定

Keepalived 的核心技術是：
    VRRP（Virtual Router Redundancy Protocol） 作用：
        模擬一個「虛擬 IP」
        Master 取得 VIP → 接受所有 client 連線
        Master 掛掉時 → VIP 立即漂移到 Backup
        客戶端永遠無感，仍然連 VIP
```

## 目錄

- [Linux 工具 keepalived($num)()](#linux-工具-keepalived$num)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[官方網站](https://www.keepalived.org/)

[官方文檔](https://www.keepalived.org/doc/introduction.html?utm_source=chatgpt.com)

[Keepalived User Guide（中文／英文）：完整手冊，包含安裝、配置語法、健康檢查機制、VRRP／VIP／LVS 設定](https://keepalived-doc.readthedocs.io/zh-cn/latest/?utm_source=chatgpt.com)

[Read the Docs 上的 Keepalived 配置語法摘要 - 列出所有 keepalived.conf 可用指令、設定選項與參數](https://keepalived.readthedocs.io/en/latest/configuration_synopsis.html?utm_source=chatgpt.com)

[官方原始碼倉庫（GitHub）](https://github.com/acassen/keepalived?utm_source=chatgpt.com)

### 心得相關

[實戰 Keepalived 建置高可用性主機叢集](https://ithelp.ithome.com.tw/articles/10166157)

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
apt-get -y update
apt-get -y install keepalived
```

## RedHat (CentOS)

```bash
yum -y install keepalived
```

## Homebrew (MacOS)

```bash
```

## 配置文檔


通常在`/etc/keepalived/keepalived.conf`

### 配置虛擬 IP（VIP）

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

### 配置節點優先權

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

### 基本範例

```
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

# 例外：可以在「公網 IP + 內網 VRRP」的方式使用

公網上使用 Keepalived / VRRP 會有安全問題

```
VRRP 原始協定無加密
有可能被 VRRP spoofing
造成錯誤的 VIP 佔用
```

因此 ISP/Cloud 不允許 VRRP 穿越。

```
以下方式仍可用 Keepalived：

1. 公網 IP 是 NAT / DNAT → VIP 維持在內網

    例如：

        公網 IP → 負載平衡器（VIP）

        再 NAT 到後端 Keepalived 節點

        這是常見的高可用架構。

2. 使用雲端環境的 private network

    GCP / AWS / Azure 自家的 VPC 網段
    允許 multicast VRRP（或用 unicast 模式）
    → Keepalived 可正常運作。

3. Keepalived 用 unicast 模式（非 multicast）

    Keepalived 支援 unicast_peer：
    可用 unicast VRRP 運作，但仍需在同一網段。
```

## 狀況是不能的

| 情境                           | 可以？ | 原因                           |
| ---------------------------- | --- | ---------------------------- |
| 在 Internet 上讓 VIP 直接漂移       | ❌   | ISP 不會送 VRRP multicast；L2 不通 |
| 跨機房漂移公網 IP                   | ❌   | 無 L2 鄰接，不支援                  |
| 雲端 EC2 用 VRRP 直接拿 Elastic IP | ❌   | 雲端廠商不允許                      |
