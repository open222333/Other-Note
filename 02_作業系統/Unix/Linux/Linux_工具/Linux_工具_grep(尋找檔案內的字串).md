# Linux 工具 grep(尋找檔案內的字串)

```
```

## 目錄

- [Linux 工具 grep(尋找檔案內的字串)](#linux-工具-grep尋找檔案內的字串)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[尋找檔案內的字串（grep 指令）](https://www.ibm.com/docs/zh-tw/aix/7.1?topic=files-finding-text-strings-within-grep-command)

# 指令

```bash
# 在檔案 pgm.s 中搜尋含部分型樣相符字元 *、^、?、[、]、\(、\)、\{ 和 \} 的型樣
grep "^[a-zA-Z]" pgm.s

# 顯示檔案 sort.c 中與特定型樣不符的所有行
grep -v bubble sort.c

# 顯示 ls 指令輸出中與字串 staff 相符的字行
ls -l | grep staff
```