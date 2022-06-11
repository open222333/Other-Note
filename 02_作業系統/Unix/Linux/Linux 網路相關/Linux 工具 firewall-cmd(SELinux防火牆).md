# Linux 工具 firewall-cmd(防火牆)

```
```

## 參考資料

[FIREWALL-CMD(1)](https://manpages.debian.org/testing/firewalld/firewall-cmd.1.en.html)

[CENTOS 7 firewall-cmd指令常見使用方式](https://kirby86a.pixnet.net/blog/post/118276362-centos-7-%E4%BD%BF%E7%94%A8firewall-cmd%E6%8C%87%E4%BB%A4)

# 指令

```bash
# 取消服務的鎖定
systemctl unmask firewalld
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

firewall-cmd - firewalld command line client

# 查看
firewall-cmd --zone=public --list-all

# 對外開放 6379 port
# --permanent 指定為永久設定，否則在 firewalld重啟或是重新讀取設定，就會失效
firewall-cmd --zone=public --add-port=6379/tcp --permanent

# 重新讀取 firewall 設定
firewall-cmd --reload

# 列出目前定義好的區域zone
firewall-cmd --get-zones

# 列出所有區域zone與其詳細設定內容
firewall-cmd --list-all-zones

# 查詢目前運作中的區域zone與各網路介面interface所屬區域
firewall-cmd --get-active-zones

# 查詢預設執行區域zone
firewall-cmd --get-default-zone

# 查看區域zone=dmz開放的服務
firewall-cmd --zone=dmz --list-all

# 查看區域zone=dmz永久開放的服務
firewall-cmd --zone=dmz --list-all --permanent

# 查看區域zone=dmz 開放的通訊埠
firewall-cmd --zone=dmz --list-ports

# 查詢http(80)服務是否開放
firewall-cmd --query-port=80/tcp

# 於區域zone=dmz 永久開放http(80)服務
firewall-cmd --permanent --zone=dmz --add-port=80/tcp

# 永久移除區域zone=dmz http(80)服務
firewall-cmd --permanent --zone=dmz --remove-port=80/tcp

# 更新防火牆規則，已連線服務不會被中斷
firewall-cmd --reload

# 更新防火牆規則，所有連線服務會被中斷
firewall-cmd --complete-reload

# 查詢firewalld提供的預設服務項目
firewall-cmd --get-services

# 於public zone中的interface，臨時新增網路卡介面 eth2
firewall-cmd --zone=public --change-interface=eth2

# 於public zone中的interface，永久新增網路卡介面 eth2
firewall-cmd --permanent --zone=public --change-interface=eth2

# 於public zone中的interface移除網路卡介面 eth2
firewall-cmd --zone=public --remove-interface=eth2

# 查詢指定網路介面eth2 所屬區域zone
firewall-cmd --get-zone-of-interface=eth2

# 使用firewalld提供的預設服務項目名稱，永久新增服務至區域zone=dmz中，以新增http (80)服務為例
firewall-cmd --zone=dmz --permanent --add-service=http

# 使用firewalld提供的預設服務項目名稱，永久刪除區域zone=dmz中，http (80)服務
firewall-cmd --zone=dmz --permanent --remove-service=http

# 查看目前預設區域zone開放的服務
firewall-cmd --list-service

# 查看目前區域zone=dmz開放的服務
firewall-cmd --zone=dmz --list-service

# 查看目前預設區域zone開放的通訊埠，若使用firewalld預設服務啟用的服務時，會無法正確顯示埠號
firewall-cmd --list-ports

# 查看目前預設區域zone=dmz開放的通訊埠，若使用firewalld預設服務啟用的服務時，會無法正確顯示埠號
firewall-cmd --zone=dmz --list-ports

# 設定預設區域zone 為 dmz
firewall-cmd --set-default-zone=dmz

# 檢查版本
firewall-cmd --version

# 檢查執行狀態
firewall-cmd --state
```

