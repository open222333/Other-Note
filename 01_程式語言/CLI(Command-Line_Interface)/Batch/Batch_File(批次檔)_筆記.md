# Batch File(批次檔) 筆記

```
副檔名 bat

批次檔，又稱批處理文件，在DOS、OS/2、Microsoft Windows中，是一種用來當成手稿語言運作程式的檔案。
它本身是文字文件，其中包含了一系列讓具備命令列介面的直譯器讀取並執行的指令。
它應用於DOS和Windows系統中，它是由DOS或者Windows系統內嵌的直譯器解釋執行。
```

## 目錄

- [Batch File(批次檔) 筆記](#batch-file批次檔-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [範例](#範例)
	- [DNS快取清除](#dns快取清除)
	- [清除DNS快取 flushdns](#清除dns快取-flushdns)
	- [ip查詢](#ip查詢)
	- [batch](#batch)
	- [](#)

## 參考資料

[batch-file pdf](chrome-extension://bocbaocobfecmglnmeaeppambideimao/pdf/viewer.html?file=https%3A%2F%2Friptutorial.com%2FDownload%2Fbatch-file.pdf)

[Windows的自動化工作](https://peterju.gitbooks.io/cmddoc/content/)

[windows bat批处理基础命令学习教程](https://blog.csdn.net/huwei2003/article/details/66968001)

[指令查詢](https://ss64.com/nt/)

# 範例

## DNS快取清除

```batch
@echo off
echo Clean up the system junk file,Please wait..
del /f /s /q %systemdrive%\*.tmp
del /f /s /q %systemdrive%\*._mp
del /f /s /q %systemdrive%\*.log
del /f /s /q %systemdrive%\*.gid
del /f /s /q %systemdrive%\*.chk
del /f /s /q %systemdrive%\*.old
del /f /s /q %systemdrive%\recycled\*.*
del /f /s /q %windir%\*.bak
del /f /s /q %windir%\prefetch\*.*
del /f /q %userprofile%\cookies\*.*
del /f /q %userprofile%\recent\*.*
del /f /s /q "%userprofile%\Local Settings\Temporary Internet Files\*.*"
del /f /s /q "%userprofile%\Local Settings\Temp\*.*"
del /f /s /q "%userprofile%\recent\*.*"
DEL /S /F /Q "%systemroot%\Temp\*.*"
DEL /S /F /Q "%AllUsersProfile%\「開始」功能表\程式集\Windows Messenger.lnk"
RD /S /Q %windir%\temp & md %windir%\temp
RD /S /Q "%userprofile%\Local Settings\Temp"
MD "%userprofile%\Local Settings\Temp"
RD /S /Q "%systemdrive%\Program Files\Temp"
MD "%systemdrive%\Program Files\Temp"
RD /S /Q "%systemdrive%\d"
net user aspnet /delete
ipconfig /flushdns
cleanmgr /sagerun:99
echo Clean up all done!
echo. & pause
```

## 清除DNS快取 flushdns

```
DNS 快取（或DNS 解析快取）是在電腦上儲存的DNS 解析結果。
這些快取結果用於在電腦上對相同主機名稱進行多次解析時加快速度。
```

```batch
@echo off
echo flushdns
for /L %%i in (0 1 150) do (ipconfig /flushdns)

ipconfig /displaydns

echo Clean up all done!
echo. & pause
```

## ip查詢

```batch
@echo off
ipconfig
pause
```

## batch

```
REM 是批處理文件中的註解標記
:: 和 REM 在批處理（Batch Script）中都用作註解符號
```

```batch
@echo off
REM 關閉命令提示符顯示
net use * /delete /yes
REM 刪除所有現有的網絡驅動器映射
net use V: \\10.0.103.9\baobiao$ /user:mxnoc\%username%
REM 連接到 \\10.0.103.9 的 baobiao$ 資源，使用指定的用戶名和當前用戶名
net use Y: \\10.0.103.17\chukuan$ /user:mxnoc\%username%
REM 連接到 \\10.0.103.17 的 chukuan$ 資源，使用指定的用戶名和當前用戶名
net use X: \\10.0.103.17\zidongrukuan$ /user:mxnoc\%username%
REM 連接到 \\10.0.103.17 的 zidongrukuan$ 資源，使用指定的用戶名和當前用戶名
net use W: \\10.0.103.17\xiafa$ /user:mxnoc\%username%
REM 連接到 \\10.0.103.17 的 xiafa$ 資源，使用指定的用戶名和當前用戶名
net use T: \\10.0.103.17\cunkuan$ /user:mxnoc\%username%
REM 連接到 \\10.0.103.17 的 cunkuan$ 資源，使用指定的用戶名和當前用戶名
exit
REM 退出腳本執行
```

##

```batch
:: https://blog.xuite.net/tolarku/blog/31746025-Windows++%E5%BC%B7%E5%88%B6%E5%88%AA%E9%99%A4%E6%AA%94%E6%A1%88%E5%8F%8A%E8%B3%87%E6%96%99%E5%A4%BE+-+%E9%80%A3+unlock+%E9%83%BD%E7%9C%81%E4%BA%86
:: 將你無法刪除的檔案或目錄拉到該 BAT 檔
DEL /F /A /Q \\?\%1
RD /S /Q \\?\%1
```