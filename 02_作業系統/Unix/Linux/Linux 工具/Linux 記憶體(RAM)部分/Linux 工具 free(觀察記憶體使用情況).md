# Linux 工具 free(觀察記憶體使用情況)

```
```

## 目錄

- [Linux 工具 free(觀察記憶體使用情況)](#linux-工具-free觀察記憶體使用情況)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[free ：觀察記憶體使用情況 - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0440processcontrol.php#free)

# 指令

```bash
# 觀察記憶體使用情況
free [-b|-k|-m|-g|-h] [-t] [-s N -c N]
	-b  ：直接輸入 free 時，顯示的單位是 Kbytes，我們可以使用 b(bytes), m(Mbytes)
		k(Kbytes), 及 g(Gbytes) 來顯示單位喔！也可以直接讓系統自己指定單位 (-h)
	-t  ：在輸出的最終結果，顯示實體記憶體與 swap 的總量。
	-s  ：可以讓系統每幾秒鐘輸出一次，不間斷的一直輸出的意思！對於系統觀察挺有效！
	-c  ：與 -s 同時處理～讓 free 列出幾次的意思～

# 顯示目前系統的記憶體容量
free -m
			  total        used        free      shared  buff/cache   available
Mem:           2848         346        1794           8         706        2263
Swap:          1023           0        1023


total: 總記憶體量，表示系統中總共可用的記憶體大小。
used: 已使用的記憶體量，表示已經被系統或應用程序使用的記憶體大小。
free: 空閒的記憶體量，表示目前空閒可用的記憶體大小。
shared: 共享記憶體量，表示被多個進程共享使用的記憶體大小。
buff/cache: 緩存和緩衝記憶體量，表示系統用於緩存數據和緩衝IO的記憶體大小。
available: 可用記憶體量，表示當前可用於新進程或應用程序的記憶體大小。
```
