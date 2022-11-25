# Python 模組-內建 json(資料格式)

```
物件(object):用大括號{}表示，鍵：值{key1:value1,key2:value2}配對儲存
陣列(array):用中括號[]表示，值[value1,value2...]

Python以及json互相對應的資料型態
dict            object
list,tuple      array
str,unicode     string
int,float,long  number
True            true
False           false
None            null
```

## 目錄

- [Python 模組-內建 json(資料格式)](#python-模組-內建-json資料格式)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[json 官方文檔](https://docs.python.org/zh-tw/3/library/json.html)

# 用法

```Python
# json模組：
# 方法 dumps()：將python資料轉成json字串格式。
#     參數 sort_keys:將轉成json格式的物件排序
#     參數 indent=int:使用內縮呈現json物件，int=字元寬度
# 方法 dump(資料,檔案物件)：將Python資料轉成json檔案格式，生成的檔案格式副檔名為json。
# 方法 loads()：將json資料格式轉成Python資料。
# 方法 load(檔案物件)：讀取json檔案，讀取完後，此json檔案將轉換成Python的資料格式。
```


