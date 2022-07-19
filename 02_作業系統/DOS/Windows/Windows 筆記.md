# Windows 筆記

## 目錄

- [Windows 筆記](#windows-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [Windows 搜尋快捷](#windows-搜尋快捷)
- [Windows 密碼原則](#windows-密碼原則)
- [Windows 10 因為您組織的安全性原則已封鎖未經驗證的來賓資料](#windows-10-因為您組織的安全性原則已封鎖未經驗證的來賓資料)
- [需求 Windows 製作支援UEFI的NTFS格式Windows 10 USB安裝碟](#需求-windows-製作支援uefi的ntfs格式windows-10-usb安裝碟)
- [需求 Windows 如何分辨硬碟的分割表是MBR還是GPT](#需求-windows-如何分辨硬碟的分割表是mbr還是gpt)

## 參考資料

# Windows 搜尋快捷

Windows `win+R` 開始執行指令

```
群組原則
gpedit.msc

本地機用戶和組
lusrmgr.msc

本機服務設定
services.msc

電腦管理
compmgmt.msc

註冊表
regedit.exe

開啟控制台
mmc

CMD命令提示字元
cmd.exe
```

```
群組原則
gpedit.msc

錄音機
sndrec32

IP位址偵測器
Nslookup

開啟檔案總管
explorer

登出指令
logoff

本地機用戶和組
lusrmgr.msc

本機服務設定
services.msc

檢查XP是否啟動
oobe/msoobe /a

開啟記事本
notepad

磁碟垃圾整理
cleanmgr

開始信使服務
net start messenger

電腦管理
compmgmt.msc

停止信使服務
net stop messenger

啟動netmeeting
conf

DVD播放器
netmeeting dvdplay

磁牒管理實用程序
diskmgmt.msc

啟動電子計算器
calc

磁碟重組工具
dfrg.msc

Chkdsk磁牒檢查
chkdsk.exe

裝置管理員
devmgmt.msc

系統醫生
drwtsn32

15秒關機
rononce -p

檢查DirectX資訊
dxdiag

註冊表編輯器
regedt32

系統配置實用程序
Msconfig.exe

群組原則結果集
rsop.msc

顯示記憶體使用情況
mem.exe

註冊表
regedit.exe

啟動字元對應表
charmap

程序管理器
progman

系統資訊
winmsd

電腦效能監測程序
perfmon.msc

檢查Windows版本
winver

掃瞄錯誤並復原
sfc /scannow

工作管理器（2000／xp／-2003）
taskmgr

事件檢視器
eventvwr.msc

本機安全性設定
secpol.msc

原則的結果集
rsop.msc

啟動制作備份還原嚮導
ntbackup

簡易widnows media player
mplayer2

開啟windows管理體系結構WMI)
wmimgmt.msc

windows更新程序
wupdmgr

windows指令碼宿主設定
wscript

寫字板
write

掃瞄儀和照相機嚮導
wiaacmgr

XP原有的區域網路聊天
winchat

簡易
mplayer2

畫圖板
widnows media player mspaint

遠端桌面連接
mstsc

媒體播放機
mplayer2

放大鏡實用程序
magnify

開啟控制台
mmc

同步指令
mobsync

磁碟重組程式
dfrg.msc

開啟系統元件服務
dcomcnfg

開啟DDE共享設定
ddeshare

DVD播放器
dvdplay

網路管理的工具嚮導
nslookup

系統制作備份和還原
ntbackup

螢幕「講述人」
narrator

移動存儲管理器
ntmsmgr.msc

移動存儲管理員操作請求
ntmsoprq.msc

(TC)指令檢查連接
netstat -an

新增一個公文包
syncapp

系統配置編輯器
sysedit

文件簽名驗證程序
sigverif

新增共用資料夾
shrpubw

本機安全原則
secpol.msc

系統加密，一旦加密就不能解開，保護windows xp系統的雙重密碼
syskey

音量控制程序
Sndvol32

系統檔案檢查器
sfc.exe

windows文件保護
sfc /scannow

程式簡介（安裝完成後出現的漫遊程序）
tourstart

工作管理器
taskmgr

事件檢視器
eventvwr

造字程序
eudcedit

開啟檔案總管
explorer

對像包裝程序
packager

停止dll文件執行
regsvr32 /u *.dll

取消ZIP支持
regsvr32 /u zipfldr.dll

CMD命令提示字元
cmd.exe

Chkdsk磁牒檢查
chkdsk.exe

證書管理實用程序
certmgr.msc

啟動計算器
calc

SQL SERVER 客戶端網路實用程序
cliconfg

剪貼板檢視器
Clipbrd

電腦管理
netmeeting compmgmt.msc

索引服務程序
ciadv.msc

開啟螢幕小鍵盤
osk

ODBC資料來源管理器
odbcad32

木馬元件服務工具，系統原有的
iexpress

共用資料夾管理器
fsmgmt.msc

協助工具管理器
utilman

啟動字元對應表格
charmap

對像包裝程序
packager

windows文件保護
sfc /scannow

60秒倒計時關機指令
tsshutdn
```

# Windows 密碼原則

```
電腦設定\Windows 設定\安全性設定\帳戶原則\密碼原則
```


# Windows 10 因為您組織的安全性原則已封鎖未經驗證的來賓資料

[Windows 10 存取網域內分享的資料](https://blog.xuite.net/yh96301/blog/548723165-Windows+10+%E5%AD%98%E5%8F%96%E7%B6%B2%E5%9F%9F%E5%85%A7%E5%88%86%E4%BA%AB%E7%9A%84%E8%B3%87%E6%96%99)

```
Windows 10修改了安全性原則，想要開啟同一網域內的電腦網所分享的共用資料夾〈不需要帳號密碼就可以存取的資料夾〉，會無法開啟，出現「Microsoft Windows Network：您無法存取此共用資料夾，因為您組織的安全性原則已封鎖未經驗證的來賓資料。…」，必須修改安全性設定，才能存取共用資料夾，解決的方法說明如下：

regedit
HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\LanmanWorkstation，選擇資料夾Parameters檔案AllowInsecureGuestAuth 預設數值資料為0將數值資料修改為1
```

# 需求 Windows 製作支援UEFI的NTFS格式Windows 10 USB安裝碟

[Modern Standby Basic Test & Windows Sleep Study](http://jasonyychiu.blogspot.com/2019/12/ntfswindows-10usb-how-to-make-ntfs.html)

[Bootx64.efi 下載](https://www67.zippyshare.com/v/d4dI9XYN/file.html)

```
Bootx64.efi，也稱為 Boot Manager 檔案，由 CFS-Technologies 創建，用於開發 Microsoft® Windows® Operating System。EFI files 屬於 Win64 DLL (可執行的應用程式) 檔案類型類別。

Bootx64.efi 最初在 Windows 10 作業系統中發行於 09/11/2014，包括於 PhotoScape X 3.7 中。 最新版本 [檔案版本 10.0.18362.1 (WinBuild.160101.0800)] 引入於 07/22/2002，適用於 Speakonia 1.3.5。 Bootx64.efi 包括在 Speakonia 1.3.5、Free Convert to DIVX AVI WMV MP4 MPEG Converter 6.2、RAR Password Cracker 4.4 中。
```

# 需求 Windows 如何分辨硬碟的分割表是MBR還是GPT

[如何分辨硬碟的分割表是MBR還是GPT](https://blog.xuite.net/yh96301/blog/585117486)
