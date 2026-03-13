# Linux 工具 ufw(Ubuntu系統預設的防火牆)

```
Uncomplicated Firewall(UFW)是Ubuntu系統預設的防火牆
```

## 目錄

- [Linux 工具 ufw(Ubuntu系統預設的防火牆)](#linux-工具-ufwubuntu系統預設的防火牆)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
  - [查看](#查看)
  - [規則設定](#規則設定)
  - [刪除](#刪除)
- [範例](#範例)
  - [DNS 查詢（UDP/53 和 TCP/53）](#dns-查詢udp53-和-tcp53)
  - [HTTP/HTTPS 流量（TCP/80 和 TCP/443）](#httphttps-流量tcp80-和-tcp443)

## 參考資料

[ufw manpage](https://manpages.ubuntu.com/manpages/bionic/man8/ufw.8.html)

[Ubuntu 防火牆設定 - 使用 ufw 指令](https://blog.tarswork.com/article/ubuntu-firewall-setting-using-ufw/)

# 指令

## 查看

```bash
# 啟動防火牆，執行後開機也會自動啟動
ufw enable
# 關閉防火牆
ufw disable
# 查看防火牆狀態
ufw status
# 查看防火牆詳細狀態
ufw status verbose
# 查看每一條規則的編號
ufw status numbered
```

## 規則設定

```bash
# 預設全部允許
ufw default allow
# 預設全部阻擋
ufw default deny
```

```bash
# 允許SSH服務連線
ufw allow ssh
# 允許通過80 Port使用tcp與udp連線
ufw allow 80
# 允許通過80 Port使用tcp連線
ufw allow 80/tcp
# 允許來自192.168.0.1通過所有連線
ufw allow from 192.168.0.1
# 允許來自192.168.0.1通過3306 Port
ufw allow from 192.168.0.1 to any port 3306
```

重新加載 UFW（Uncomplicated Firewall）防火牆的規則和設置的命令

使用 ufw reload 可以讓這些更改立即生效，而無需停止或重新啟動防火牆服務。

這在需要調整防火牆規則時非常有用，因為它允許將新的設置應用到防火牆上而不中斷現有的連接或服務。

```bash
ufw reload
```

## 刪除

```bash
```

# 範例

## DNS 查詢（UDP/53 和 TCP/53）

```sh
ufw allow out to any port 53 proto udp
ufw allow out to any port 53 proto tcp
```

## HTTP/HTTPS 流量（TCP/80 和 TCP/443）

```sh
ufw allow out to any port 80 proto tcp
ufw allow out to any port 443 proto tcp
```