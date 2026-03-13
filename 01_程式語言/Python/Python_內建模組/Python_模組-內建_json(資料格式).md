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
  - [預設的 JSON 序列化不支援直接將 bytes datetime ObjectId 轉換為 JSON](#預設的-json-序列化不支援直接將-bytes-datetime-objectid-轉換為-json)
  - [避免預設的 JSON 序列化對特殊類型進行轉碼](#避免預設的-json-序列化對特殊類型進行轉碼)

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

## 預設的 JSON 序列化不支援直接將 bytes datetime ObjectId 轉換為 JSON

```Python
import json
from bson import ObjectId
from datetime import datetime

# 你的字典資料，其中包含了 ObjectId、datetime 和 bytes
info = {
    "_id": ObjectId("5f86ec4a7a9a24fbfc8d2563"),
    "name": "John",
    "age": 30,
    "city": "New York",
    "timestamp": datetime.now(),
    "binary_data": b"example bytes"
}

# 自定義 JSON 序列化函數
def custom_encoder(obj):
    if isinstance(obj, (ObjectId, datetime)):
        return str(obj)  # 將 ObjectId 和 datetime 轉換為字符串
    elif isinstance(obj, bytes):
        return obj.decode('utf-8')  # 將 bytes 轉換為字符串
    raise TypeError(f"Object of type {obj.__class__.__name__} is not JSON serializable")

# 寫入 JSON 檔案，使用自定義的 JSON 序列化函數
with open('output.json', 'w') as json_file:
    json.dump(info, json_file, indent=4, default=custom_encoder)
```

## 避免預設的 JSON 序列化對特殊類型進行轉碼

```Python
import json
from bson import ObjectId
from datetime import datetime

'''
使用 json.dump() 時碰到不希望進行轉碼的情況，
可以使用 default 參數搭配 json.JSONEncoder 的子類自定義轉碼邏輯。
'''

# 你的字典資料，其中包含了 ObjectId、datetime 和 bytes
info = {
    "_id": ObjectId("5f86ec4a7a9a24fbfc8d2563"),
    "name": "John",
    "age": 30,
    "city": "New York",
    "timestamp": datetime.now(),
    "binary_data": b"example bytes"
}

class CustomEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, (ObjectId, datetime)):
            return str(obj)  # 將 ObjectId 和 datetime 轉換為字符串
        elif isinstance(obj, bytes):
            return obj.decode('utf-8')  # 將 bytes 轉換為字符串
        return super().default(obj)

# 寫入 JSON 檔案，使用自定義的 JSONEncoder
with open('output.json', 'w') as json_file:
    json.dump(info, json_file, indent=4, cls=CustomEncoder)
```
