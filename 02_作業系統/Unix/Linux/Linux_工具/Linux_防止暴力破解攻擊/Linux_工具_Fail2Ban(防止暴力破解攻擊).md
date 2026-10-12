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
    - [Ubuntu 24.04 SSH 防護範例](#ubuntu-2404-ssh-防護範例)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[Github](https://github.com/fail2ban/fail2ban?tab=readme-ov-file)

[Github - 安裝教學](https://github.com/fail2ban/fail2ban/wiki/How-to-install-fail2ban-packages)

[官方文檔](https://fail2ban.readthedocs.io/en/latest/)

[排查 SSH 暴力破解（日誌分析、關閉密碼登入）](<../Linux_SSH_工具/Linux_工具_OpenSSL_OpenSSH(連線、密鑰).md#排查-ssh-暴力破解大量密碼登入失敗>)

# 安裝

## Debian (Ubuntu)

```bash
apt update && apt install -y fail2ban
```

## RedHat (CentOS)

```bash
yum install epel-release -y
yum install fail2ban -y
```

## 配置文檔

Fail2Ban 的配置文件位於 `/etc/fail2ban` 目錄下，主要是 `fail2ban.conf` 和 `jail.conf`。

- **不要直接改 `jail.conf`**，套件更新時會被覆蓋
- 自訂設定寫在 `jail.local`（只寫要覆蓋的參數即可，不需整份複製 `jail.conf`）

| 參數 | 說明 |
|---|---|
| `ignoreip` | 白名單，不會被封鎖的 IP / 網段（空白分隔） |
| `bantime` | 封鎖時間，可用 `10m`、`1h`、`1d` 等單位（不加單位為秒） |
| `findtime` | 計算失敗次數的時間窗口 |
| `maxretry` | `findtime` 內允許的最大失敗次數 |
| `backend` | 讀日誌的方式；`systemd` 直接讀 journal，不需指定 `logpath` |
| `mode` | sshd filter 模式：`normal`、`ddos`、`extra`、`aggressive`（= normal + ddos + extra） |

### Ubuntu 24.04 SSH 防護範例

效果：同一個 IP 在 10 分鐘內失敗 5 次 → 封鎖 1 天；自己的 IP 不會被封。

```bash
cat > /etc/fail2ban/jail.local <<'EOF'
[DEFAULT]
# 不封鎖自己的 IP（平常登入的來源），<管理者IP> 換成實際 IP
ignoreip = 127.0.0.1/8 ::1 <管理者IP>
bantime  = 1d
findtime = 10m
maxretry = 5
backend  = systemd

[sshd]
enabled = true
# 已關閉密碼登入時，攻擊會變成「連線後被拒」而非 Failed password，
# aggressive 模式才抓得到這類紀錄
mode    = aggressive
EOF

# 啟動並設為開機自動執行
systemctl enable --now fail2ban
```

> Ubuntu 24.04 預設可能沒有 `/var/log/auth.log`，沿用舊的 `logpath = /var/log/auth.log` 會導致 fail2ban 啟動失敗，所以改用 `backend = systemd`。
> 關閉密碼登入的做法見 [排查 SSH 暴力破解](<../Linux_SSH_工具/Linux_工具_OpenSSL_OpenSSH(連線、密鑰).md#關閉密碼登入>)。

# 指令

```bash
# 確認 sshd 規則有在運作、查看封鎖列表
fail2ban-client status sshd

# 即時看封鎖紀錄
tail -f /var/log/fail2ban.log

# 手動解除封鎖某個 IP
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
