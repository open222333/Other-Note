# Python 模組-內建 time(時間)

```
```

## 目錄

- [Python 模組-內建 time(時間)](#python-模組-內建-time時間)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[time 官方文檔](https://docs.python.org/zh-tw/3/library/time.html)

# 用法

```Python
# 時間戳
timeStamp = 1557502800
timeArray = time.localtime(timeStamp)
otherStyleTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
print(otherStyleTime) # 2019-05-10 23:40:00
```
