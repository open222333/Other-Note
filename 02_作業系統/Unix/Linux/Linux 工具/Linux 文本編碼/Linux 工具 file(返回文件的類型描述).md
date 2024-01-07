# Linux 工具 file(返回文件的類型描述)

```
通常返回文件的類型描述，例如 ASCII 文本、空白文件、PNG 圖像等等。
```

## 目錄

- [Linux 工具 file(返回文件的類型描述)](#linux-工具-file返回文件的類型描述)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[file(1) — Linux manual page](https://www.man7.org/linux/man-pages/man1/file.1.html)

# 指令

`確定單個文件的類型`

```bash
file myfile.txt
```
`以 MIME 類型輸出文件信息`

```bash
file -i myfile.txt
```

`簡短描述文件類型`

```bash
file -b myfile.txt
```

`確定多個文件的類型`

```bash
file file1.txt file2.jpg file3.pdf
```

`遞歸確定目錄中所有文件的類型`

```bash
file -i *
```