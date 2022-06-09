# Linux 工具 firewall-cmd(SELinux防火牆)
```
```

## 參考資料

[]()

# 指令

```bash
# 查看
firewall-cmd --zone=public --list-all

# 對外開放 6379 port
firewall-cmd --zone=public --add-port=30000/tcp --permanent

# 重新讀取 firewall 設定
firewall-cmd --reload
	# --permanent 指定為永久設定，否則在 firewalld重啟或是重新讀取設定，就會失效

### FirewallD is not running ###

# 取消服務的鎖定
systemctl unmask firewalld

# 鎖定該服務時執行
systemctl mask firewalld

# 關閉。
systemctl stop firewalld

# 啟動服務
systemctl start firewalld
# 開機自動啟動
systemctl enable firewalld
# 開機不會啟動
systemctl disable firewalld
# 查詢啟動狀態
systemctl status firewalld
# 重啟
systemctl restart firewalld
# 停止
systemctl stop firewalld
```

