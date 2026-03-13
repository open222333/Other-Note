# Python 模組-內建 os(作業系統接口)

```
與操作系統相關的功能的便捷式途徑

備註 由於不同的作業系統具有不同的路徑命名慣例，在標準函式庫中的路徑模組有數個版本可供使用，而 os.path 模組都會是運行 Python 之作業系統所適用本地路徑。

然而，如果你想要操作始終以某個不同於本機格式表示的路徑，你也可以引入並使用對應的模組。

它們都具有相同的介面：
posixpath 用於 UNIX 形式的路徑
ntpath 用於 Windows 的路徑
```

## 目錄

- [Python 模組-內建 os(作業系統接口)](#python-模組-內建-os作業系統接口)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [ntpath 範例](#ntpath-範例)

## 參考資料

[os--- 多種操作系統接口](https://docs.python.org/zh-tw/3/library/os.html)


# 用法

```Python
import os


# 顯示 環境變數
os.environ

# 顯示檔案名
os.path.basename(path)

# 創建多層次資料夾
os.makedirs(path)

# 檔案是否存在
os.path.exists(path):


def get_file_extension(file_path):
    '''取得 副檔名'''
    _, extension = os.path.splitext(file_path)  # 路徑 以及副檔名
    return extension
```

## ntpath 範例

```Python
# 取得 網址的檔案名稱
from urllib import parse
import ntpath
import posixpath


a = 'https://example.com/path/to/8.jpg'
r = parse.urlparse(a)
s1 = ntpath.basename(r.path)
s2 = posixpath.basename(r.path)
print(s1, s2)
 # 8.jpg
```
