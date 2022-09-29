# Linux 工具 mount(檔案系統掛載)

```
```

## 目錄

- [Linux 工具 mount(檔案系統掛載)](#linux-工具-mount檔案系統掛載)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[mount(8) - Linux man page](https://linux.die.net/man/8/mount)

[磁碟掛載與卸載](https://dywang.csie.cyut.edu.tw/dywang/linuxSystem/node43.html)

# 指令

```bash
mount [-tonL]  裝置名稱代號  掛載點
	參數：
	-a  ：依照 /etc/fstab 的內容將所有相關的磁碟都掛載
	-n  ：掛載而不記錄到 /etc/mtab
	-L  ：後接掛載 partition 的表頭名稱( Label )
	-t  ：指定掛載裝置的檔案格式
	-o  ：後面可接額外參數：
		ro, rw:       此 partition 為唯讀(ro) 或可讀寫(rw)
		async, sync:  此 partition 為同步寫入 (sync) 或非同步 (async)
		auto, noauto: 允許此 partition 被以 mount -a 自動掛載(auto)
		dev, nodev:   是否允許此 partition 上，可建立裝置檔案？ dev 為可允許
		suid, nosuid: 是否允許此 partition 含有 suid/sgid 的檔案格式？
		exec, noexec: 是否允許此 partition 上擁有可執行 binary 檔案？
		user, nouser: 是否允許此 partition 讓 user 執行 mount ？
						一般 user 也能夠對此 partition 進行 mount 。
		defaults:     預設值為：rw, suid, dev, exec, auto, nouser, and async
		remount:      重新掛載，這在系統出錯，或重新更新參數時，
		iocharset=    設定字元編碼，如 big5,utf8 等
```
