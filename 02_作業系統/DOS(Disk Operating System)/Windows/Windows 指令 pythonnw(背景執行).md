# Windows 指令 pythonnw(背景執行)\

```
```

## 目錄

- [Windows 指令 pythonnw(背景執行)\](#windows-指令-pythonnw背景執行)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料


# 指令

```PowerShell
# 後台使用Python運行test.py文件，無日誌輸出。
pythonnw test.py

# 後台使用Python運行test.py文件，日誌輸出到當前目錄test.log文件。
pythonw test.py > test.log

# 查看windows下所有進程
tasklist

# 指定進程號或名稱殺死進程
taskkill /f /im 進程號
```
