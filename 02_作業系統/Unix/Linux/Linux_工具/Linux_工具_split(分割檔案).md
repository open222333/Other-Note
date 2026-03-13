# Linux 工具 split(分割檔案)

```
將一個大檔案，依據檔案大小或行數來分割
```

## 目錄

- [Linux 工具 split(分割檔案)](#linux-工具-split分割檔案)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[split(1) — Linux manual page](https://man7.org/linux/man-pages/man1/split.1.html)

# 指令

```bash
split [-bl] file PREFIX
	選項與參數：
	-b  ：後面可接欲分割成的檔案大小，可加單位，例如 b, k, m 等；
	-l  ：以行數來進行分割。
	PREFIX ：代表前置字元的意思，可作為分割檔案的前導文字。

# /etc/services 有六百多K，若想要分成 300K 一個檔案
split -b 300k /etc/services services

# 將上面的三個小檔案合成一個檔案，檔名為 servicesback
cat services* >> servicesback
```
