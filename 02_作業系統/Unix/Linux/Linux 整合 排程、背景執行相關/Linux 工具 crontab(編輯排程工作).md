# Linux 工具 crontab(編輯排程工作)

```
```

## 目錄

- [Linux 工具 crontab(編輯排程工作)](#linux-工具-crontab編輯排程工作)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [crontab 指令](#crontab-指令)
	- [crontab 排程時間設定](#crontab-排程時間設定)
		- [欄位說明:](#欄位說明)
		- [特殊字元:](#特殊字元)
		- [特殊排程規則:](#特殊排程規則)

## 參考資料

User Commands:

[crontab(1) — Linux manual page](https://man7.org/linux/man-pages/man1/crontab.1.html)

File Formats:

[crontab(5) — Linux manual page](https://man7.org/linux/man-pages/man5/crontab.5.html)

## crontab 指令

```bash
# 查看目前設置
crontab -l

# 查看指定使用者的 crontab
crontab -u gtwang -l

# 編輯設置
crontab -e

# 編輯指定使用者的 crontab
crontab -u gtwang -e

# 刪除 crontab 內容
crontab -r
```

## crontab 排程時間設定

格式:

```
MIN HOUR DOM MON DOW USER CMD
```

```bash
# ┌───────────── 分鐘   (0 - 59)
# │ ┌─────────── 小時   (0 - 23)
# │ │ ┌───────── 日     (1 - 31)
# │ │ │ ┌─────── 月     (1 - 12)
# │ │ │ │ ┌───── 星期幾 (0 - 7，0 是週日，6 是週六，7 也是週日)
# │ │ │ │ │
# * * * * * /path/to/command
```

### 欄位說明:

欄位 | 說明 | 可設定的值
--- | --- | ---
MIN | 分鐘| 0~59
HOUR | 小時 | 0~23
DOM | 日 | 1~31
MON | 月份 | 1~12<br>可用英文簡稱取代,例如:一月 Jan
DOW | 星期幾 | 0(週日)~6(週六),7也代表週日<br>此欄位亦可用英文簡稱取代,例如:週日 Sun。
USER | 使用者帳號 | 以指定帳號權限執行
CMD | 定期執行指令 | 例如:/path/to/cmd --your --parameter

### 特殊字元:

特殊字元 | 代表意義
--- | ---
星號(*) | 代表接受任意時刻，例如若在月份那一欄填入星號，則代表任一月份皆可。
逗號(,) | 分隔多個不同時間點。例如若要指定 3:00、6:00 與 9:00 三個時間點執行指令，就可以在第二欄填入 3,6,9。
減號(-) | 代表一段時間區間，例如若在第二欄填入 8-12 就代表從 8 點到 12 點的意思，也就是等同於 8,9,10,11,12。
斜線加數字(/n) | n代表數字，這樣寫的意思就是「每隔n的單位」的意思，例如若在第一欄填入 */5 就代表每間隔五分鐘執行一次的意思，也可以寫成 0-59/5。

### 特殊排程規則:

排程規則 | 說明
--- | ---
@reboot | 每次重新開機之後，執行一次。
@yearly | 每年執行一次，亦即 0 0 1 1 *。
@annually | 每年執行一次，亦即 0 0 1 1 *。
@monthly | 每月執行一次，亦即 0 0 1 * *。
@weekly | 每週執行一次，亦即 0 0 * * 0。
@daily | 每天執行一次，亦即 0 0 * * *。
@hourly | 每小時執行一次，亦即 0 * * * *。
