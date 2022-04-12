# Linux 工具 systemctl(系統管理工具)

# 說明

幾種比較常見的 systemd 的服務類型如下：

副檔名	主要服務功能

.service

```
一般服務類型 (service unit)：主要是系統服務，包括伺服器本身所需要的本機服務以及網路服務都是！比較經常被使用到的服務大多是這種類型！ 所以，這也是最常見的類型了！
```

.socket
```
內部程序資料交換的插槽服務 (socket unit)：主要是 IPC (Inter-process communication) 的傳輸訊息插槽檔 (socket file) 功能。 這種類型的服務通常在監控訊息傳遞的插槽檔，當有透過此插槽檔傳遞訊息來說要連結服務時，就依據當時的狀態將該用戶的要求傳送到對應的 daemon， 若 daemon 尚未啟動，則啟動該 daemon 後再傳送用戶的要求。
使用 socket 類型的服務一般是比較不會被用到的服務，因此在開機時通常會稍微延遲啟動的時間 (因為比較沒有這麼常用嘛！)。一般用於本機服務比較多，例如我們的圖形界面很多的軟體都是透過 socket 來進行本機程序資料交換的行為。 (這與早期的 xinetd 這個 super daemon 有部份的相似喔！)
```

.target

```
執行環境類型 (target unit)：其實是一群 unit 的集合，例如上面表格中談到的 multi-user.target 其實就是一堆服務的集合～也就是說，選擇執行 multi-user.target 就是執行一堆其他 .service 或/及 .socket 之類的服務就是了！
```

.mount
.automount
```
檔案系統掛載相關的服務 (automount unit / mount unit)：例如來自網路的自動掛載、NFS 檔案系統掛載等與檔案系統相關性較高的程序管理。
```

.path
```
偵測特定檔案或目錄類型 (path unit)：某些服務需要偵測某些特定的目錄來提供佇列服務，例如最常見的列印服務，就是透過偵測列印佇列目錄來啟動列印功能！ 這時就得要 .path 的服務類型支援了！
```

.timer
```
循環執行的服務 (timer unit)：這個東西有點類似 anacrontab 喔！不過是由 systemd 主動提供的，比 anacrontab 更加有彈性！
```

# 指令 使用

```
不應該使用 kill 的方式來關掉一個正常的服務
否則 systemctl 會無法繼續監控該服務的！
```

```bash
# 管理服務基本指令：
systemctl [command] [unit]
	command 主要有：
		start     ：立刻啟動後面接的 unit
		stop      ：立刻關閉後面接的 unit
		restart   ：立刻關閉後啟動後面接的 unit，亦即執行 stop 再 start 的意思
		reload    ：不關閉後面接的 unit 的情況下，重新載入設定檔，讓設定生效
		enable    ：設定下次開機時，後面接的 unit 會被啟動
		disable   ：設定下次開機時，後面接的 unit 不會被啟動
		status    ：目前後面接的這個 unit 的狀態，會列出有沒有正在執行、開機預設執行否、登錄等資訊等！
		is-active ：目前有沒有正在運作中
		is-enabled：開機時有沒有預設要啟用這個 unit
		mask      ：強迫服務註銷
		unmask    ：取消註銷

# 觀察系統上所有的服務
systemctl [command] [--type=TYPE] [--all]
	command:
	    list-units      ：依據 unit 列出目前有啟動的 unit。若加上 --all 才會列出沒啟動的。
	    list-unit-files ：依據 /usr/lib/systemd/system/ 內的檔案，將所有檔案列表說明。
		--type=TYPE：unit type，主要有 service, socket, target 等
```