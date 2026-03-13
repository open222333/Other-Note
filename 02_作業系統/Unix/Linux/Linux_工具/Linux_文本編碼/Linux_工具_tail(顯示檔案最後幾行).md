# Linux 工具 tail(顯示檔案最後幾行)

```
```

## 目錄

- [Linux 工具 tail(顯示檔案最後幾行)](#linux-工具-tail顯示檔案最後幾行)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[顯示檔案的最後幾行（tail 指令）](https://www.ibm.com/docs/zh-tw/aix/7.1?topic=files-displaying-last-lines-tail-command)

# 指令

```bash
# 查看log檔案 查找有404的紀錄
tail -f $log_file | grep 404
	-f 持續顯示
```