# Python 模組 mega(非官方的 MEGA API)

```
另一個非官方 MEGA API 實現，簡單易用，支持大部分 MEGA 的功能。
```

## 目錄

- [Python 模組 mega(非官方的 MEGA API)](#python-模組-mega非官方的-mega-api)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[mega pypi](https://pypi.org/project/mega/)

# 指令

```bash
# 安裝
pip install mega
```

# 用法

```Python
from mega import Mega

mega = Mega()
mega.login("your_email", "your_password")

# 上傳檔案
mega.upload("local_file.txt")

# 下載檔案
mega.download("file_id", "destination_path")
```
