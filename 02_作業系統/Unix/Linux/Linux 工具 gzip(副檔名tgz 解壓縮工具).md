# Linux 工具 gzip(副檔名tgz 壓縮工具)

```
Gzip是一種壓縮檔案格式並且也是一個在類 Unix 上的一種檔案解壓縮的軟體
```

## 參考資料

[OpenBSD gzip(1) manual page](https://man.openbsd.org/gzip#HISTORY)

# 指令

```bash
gzip [options] [filename]
	# -c，--stdout將解壓縮的內容輸出到標準輸出，原檔案保持不變
	# -d，--decompress解壓縮
	# -f，--force強制覆蓋舊檔案
	# -l，--list列出壓縮檔內儲存的原始檔案的資訊（如，解壓後的名字、壓縮率等）
	# -n，--no-name壓縮時不儲存原始檔案的檔名和時間戳，解壓縮時不恢復原始檔案的檔名和時間戳（此時，解出來的檔案，其檔名為壓縮檔的檔名）
	# -N，--name壓縮時儲存原始檔案的檔名和時間戳，解壓縮時恢復原始檔案的檔名和時間戳
	# -q，--quiet抑制所有警告資訊
	# -r，--recursive遞迴
	# -t，--test測試壓縮檔案完整性
	# -v，--verbose冗餘模式（即顯示每一步的執行內容）
	# -1、-2、...、-9壓縮率依次增大，速度依次減慢，預設為-6
```
