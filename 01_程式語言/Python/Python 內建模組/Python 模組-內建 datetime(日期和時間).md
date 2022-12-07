# Python 模組-內建 datetime(日期和時間)

```
```

## 目錄

- [Python 模組-內建 datetime(日期和時間)](#python-模組-內建-datetime日期和時間)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[datetime 官方文檔](https://docs.python.org/zh-tw/3/library/datetime.html)

# 用法

```Python
# 計算30天
date_30_days_ago = (datetime.utcnow() - timedelta(days=30)).timestamp()

# 現在時間帶入時區
datetime.datetime.now(datetime.timezone(datetime.timedelta(hours=8)))

# 獲取時間戳
stamp = datetime.datetime.now().timestamp()

# 格式
now_time = datetime.now()
now_utctime = datetime.utcnow()

datati = '2021-08-13 17:00:02'
# Python time strptime() 函數根據指定的格式把一個時間字符串解析為時間元組。
# datetime.strptim(不帶格式): time.struct_time(tm_year=2021, tm_mon=8, tm_mday=13, tm_hour=17, tm_min=0, tm_sec=2, tm_wday=4, tm_yday=225, tm_isdst=-1)
datetime.strptime(datati, '%Y-%m-%d %H:%M:%S').timetuple()

# 2021-08-13 17:00:02
datetime.strptime(datati, '%Y-%m-%d %H:%M:%S')

# 2022-12-07 15:25:30
datetime.now().__format__('%Y-%m-%d %H:%M:%S')

# 2022-12-07
datetime.now().__format__('%Y-%m-%d')

# <class 'str'>
type(datetime.now().__format__('%Y-%m-%d %H:%M:%S'))

# 1670397930.0
time.mktime(now_time.timetuple())

# time.struct_time(tm_year=2022, tm_mon=12, tm_mday=7, tm_hour=15, tm_min=25, tm_sec=30, tm_wday=2, tm_yday=341, tm_isdst=0)
datetime.utctimetuple(now_time)

# 2022-12-07 07:25:30.623071
datetime.utcnow()

# Wed Dec  7 15:25:30 2022
datetime.ctime(now_time)

# 時間戳轉 '%Y-%m-%d %H:%M:%S'
# 2022-12-07 07:25:30
datetime.utcfromtimestamp(time.mktime(now_time.timetuple()))

# 1670397930
int(datetime.timestamp(now_time))

# 1670397930.623112
datetime.timestamp(datetime.now())

# isoformat 2019-05-18T15:17:00+08:00
datetime(2019, 5, 18, 15, 17, tzinfo=timezone(timedelta(hours=8))).isoformat()

# isoformat 2019-05-18T15:17:00+08:00
datetime(2019, 5, 18, 15, 17, tzinfo=timezone(timedelta(hours=8))).isoformat()

# 時間相加
datetime.now().astimezone(timezone(timedelta(hours=8))).isoformat(timespec='seconds')
```
