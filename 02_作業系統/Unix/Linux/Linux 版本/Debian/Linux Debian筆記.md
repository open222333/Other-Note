# Linux Debian筆記

## 目錄

- [Linux Debian筆記](#linux-debian筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [狀況](#狀況)
  - [執行 apt-get update 時發生錯誤，顯示無法解析一些 Debian 軟體來源的域名（例如 deb.debian.org 和 security.debian.org）。](#執行-apt-get-update-時發生錯誤顯示無法解析一些-debian-軟體來源的域名例如-debdebianorg-和-securitydebianorg)

## 參考資料


# 狀況

## 執行 apt-get update 時發生錯誤，顯示無法解析一些 Debian 軟體來源的域名（例如 deb.debian.org 和 security.debian.org）。

這通常是因為 DNS 設定或網路問題導致的。

確保系統可以連接到網際網路。

嘗試使用 ping 測試是否可以解析域名：

如果域名無法解析，則可能是 DNS 配置有問題。

```sh
ping deb.debian.org
```

確認 /etc/resolv.conf 文件中是否有有效的 DNS 伺服器配置

```
nameserver 8.8.8.8
nameserver 1.1.1.1

nameserver 8.8.8.8
nameserver 8.8.4.4
```

```sh
echo "nameserver 8.8.8.8" | sudo tee /etc/resolv.conf
echo "nameserver 1.1.1.1" | sudo tee -a /etc/resolv.conf
```

檢查 /etc/apt/sources.list 文件是否正確且最新。

Bookworm（最新穩定版）20241127

```sh
deb http://deb.debian.org/debian bookworm main contrib non-free
deb http://security.debian.org/debian-security bookworm-security main contrib non-free
deb http://deb.debian.org/debian bookworm-updates main contrib non-free
```

```sh
sudo apt-get update
```
