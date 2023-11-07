# Windows PowerShell 筆記

## 目錄

- [Windows PowerShell 筆記](#windows-powershell-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [設定 安裝路徑(以Conda為例)](#設定-安裝路徑以conda為例)

## 參考資料

[PowerShell - wiki](https://zh.wikipedia.org/zh-tw/PowerShell)

[PowerShell - 官方文檔](https://learn.microsoft.com/zh-tw/powershell/)

[指令查詢](https://ss64.com/ps/)

# 用法

## 設定 安裝路徑(以Conda為例)

```PowerShell
# 獲取 Conda 的安裝路徑
$condaPath = (Get-Command conda.exe).Source

# 將 Conda 的安裝路徑添加到 PATH
$env:PATH += ";$condaPath"

# 確認變更
echo $env:PATH


# 設定 Anaconda3 路徑到系統環境變數 PATH
$env:PATH += ";C:\Path\to\Anaconda3"
$env:PATH += ";C:\Path\to\Anaconda3\Scripts"
$env:PATH += ";C:\Path\to\Anaconda3\Library\bin"

# 安裝 Python 套件
pip install requests

# 啟動 Anaconda Navigator
start anaconda-navigator

# 列出當前目錄下的文件
Get-ChildItem

# 創建一個新資料夾
New-Item -ItemType Directory -Path C:\NewFolder

# 下載文件
Invoke-WebRequest -Uri "https://example.com/file.zip" -OutFile "C:\Path\to\file.zip"

# 切換目錄
Set-Location -Path C:\Path\to\Your\Directory

# 啟動 Visual Studio Code
code

# 顯示系統日期和時間
Get-Date
```