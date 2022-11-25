# Python 模組-內建 traceback(例外追蹤)

```
```

## 目錄

- [Python 模組-內建 traceback(例外追蹤)](#python-模組-內建-traceback例外追蹤)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[traceback 官方文檔](https://docs.python.org/zh-tw/3/library/traceback.html)

# 用法

```Python
import sys
import traceback
import test

try:
    # 程式區段
except Exception as e:
    # print(e)
    error_class = e.__class__.__name__  # 取得錯誤類型
    detail = e.args[0]  # 取得詳細內容
    cl, exc, tb = sys.exc_info()  # 取得Call Stack
    lastCallStack = traceback.extract_tb(tb)[-1]  # 取得Call Stack的最後一筆資料
    fileName = lastCallStack[0]  # 取得發生的檔案名稱
    lineNum = lastCallStack[1]  # 取得發生的行號
    funcName = lastCallStack[2]  # 取得發生的函數名稱
    errMsg = "File \"{}\", line {}, in {}: [{}] {}".format(fileName, lineNum, funcName, error_class, detail)
    print(errMsg)

```
