# Linux 工具 Fail2Ban(防止暴力破解攻擊)

```
Fail2Ban 是一個用來防止暴力破解攻擊的開源軟件。
它通過監控日誌文件來檢測潛在的攻擊，然後使用防火牆規則或其他方法來禁止這些攻擊源的 IP 地址。
這樣可以有效地防止暴力破解 SSH、HTTP、SMTP 等服務。
```

## 目錄

- [Linux 工具 Fail2Ban(防止暴力破解攻擊)](#linux-工具-fail2ban防止暴力破解攻擊)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [配置文檔](#配置文檔)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[Github](https://github.com/fail2ban/fail2ban?tab=readme-ov-file)

[Github - 安裝教學](https://github.com/fail2ban/fail2ban/wiki/How-to-install-fail2ban-packages)

[官方文檔](https://fail2ban.readthedocs.io/en/latest/)

# 安裝

## Debian (Ubuntu)

```bash
apt update -y
apt install fail2ban -y
```

## RedHat (CentOS)

```bash
yum install epel-release -y
yum install fail2ban -y
```

## 配置文檔

Fail2Ban 的配置文件位於 `/etc/fail2ban` 目錄下。

主要的配置文件是 fail2ban.conf 和 jail.conf。

為了避免將來的更新覆蓋自定義配置，建議創建一個本地配置文件 jail.local 來進行自定義設置。

```bash
cp /etc/fail2ban/jail.conf /etc/fail2ban/jail.local
```

```conf
[sshd]
enabled = true
port    = ssh
logpath = /var/log/auth.log
bantime = 3600
findtime = 600
maxretry = 5
```

```
enabled：啟用此 jail。
port：要監控的端口，這裡是 SSH 默認的 ssh 端口。
logpath：SSH 服務的日誌文件路徑，Debian/Ubuntu 系統通常是 /var/log/auth.log，CentOS/RHEL 通常是 /var/log/secure。
bantime：封禁時間，單位是秒，這裡設置為 3600 秒（1 小時）。
findtime：檢測時間窗口，單位是秒，這裡設置為 600 秒（10 分鐘）。
maxretry：在 findtime 時間內允許的最大失敗次數，這裡設置為 5 次。
```

# 指令

查看封禁列表

```bash
fail2ban-client status sshd
```

解封 IP

```bash
fail2ban-client set sshd unbanip <IP_ADDRESS>
```

## 服務操作

```bash
# 啟動服務
systemctl start fail2ban

# 查詢啟動狀態
systemctl status fail2ban

# 重新啟動
systemctl restart fail2ban

# 停止服務
systemctl stop fail2ban

# 開啟開機自動啟動
systemctl enable fail2ban

# 關閉開機自動啟動
systemctl disable fail2ban
```
