# Linux 工具 ufw(Ubuntu系統預設的防火牆)

```
Uncomplicated Firewall(UFW)是Ubuntu系統預設的防火牆
```

## 目錄

- [Linux 工具 ufw(Ubuntu系統預設的防火牆)](#linux-工具-ufwubuntu系統預設的防火牆)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[ufw manpage](https://manpages.ubuntu.com/manpages/bionic/man8/ufw.8.html)

[Ubuntu 防火牆設定 - 使用 ufw 指令](https://blog.tarswork.com/article/ubuntu-firewall-setting-using-ufw/)

# 指令

```bash
# 啟動防火牆，執行後開機也會自動啟動
ufw enable
# 關閉防火牆
ufw disable
# 查看防火牆狀態
ufw status
# 查看防火牆詳細狀態
ufw status verbose


# 預設全部允許
ufw default allow
# 預設全部阻擋
ufw default deny
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
