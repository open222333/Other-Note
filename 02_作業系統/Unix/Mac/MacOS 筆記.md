# MacOS 筆記

```
```

## 目錄

- [MacOS 筆記](#macos-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [操作指令](#操作指令)
- [Mac 環境變數](#mac-環境變數)
	- [狀況 Mac 一直進入安全模式](#狀況-mac-一直進入安全模式)

## 參考資料

[指令查詢](https://ss64.com/osx/)

## 操作指令

```bash
# 查看監聽的port
netstat -an | grep LISTEN

lsof -i -P -n | grep LISTEN

# 查看 ssh連線 進程
ps aux | grep ssh

# 修改主機名稱 指令
sudo scutil -—set pref HostName WorkMacBookPro
```

# Mac 環境變數
```bash
# 列出環境變數
printenv

# 列出環境變數下的 Path
echo $PATH
```

新增環境變數

```bash
# 打開 bash profile
vi ~/.bash_profile

# 新增路徑到 PATH
export PATH=$PATH:路徑名稱
export PATH=$PATH:$HOME/bin/

# 執行 bash profile
source ~/.bash_profile
```

## 狀況 Mac 一直進入安全模式

解決方法：
```
重置NVRAM
```

開機時一直按著

	Command + Option + P + R

Mac會默默存取使用者的喜好設定

也就是`NVRAM` 以前叫做`PRAM`

NVRAM是 **非揮發性的隨機存取記憶體**`(Non-Volatile Random-Access Memory)`

