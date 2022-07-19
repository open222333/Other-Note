# Linux 工具 chkconfig(系統服務操作)

```
主要用來更新（啟動或停止）和查詢系統服務的運行級資訊。
```

## 目錄

- [Linux 工具 chkconfig(系統服務操作)](#linux-工具-chkconfig系統服務操作)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [指令 使用](#指令-使用)

## 參考資料

[LINUX chkconfig命令](https://b8807053.pixnet.net/blog/post/336314975-linux-chkconfig%E5%91%BD%E4%BB%A4)


## 指令 使用

```bash
# 將系統上所有的服務全部列出來
systemctl list-unit-files

# 觀察預設啟動否
chkconfig --list

# 增加一項新的服務 增加一個服務：服務指令碼必須存放在/etc/ini.d/目錄下
chkconfig --add name

# 刪除服務
chkconfig –-del name

# 設定httpd在執行級別為2、3、4、5的情況下都是on（開啟）的狀態
chkconfig –level httpd 2345 on
```