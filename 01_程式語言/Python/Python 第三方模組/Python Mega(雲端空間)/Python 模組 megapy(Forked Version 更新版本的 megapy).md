# Python 模組 megapy(Forked Version 更新版本的 megapy)

```
一個更新版本的 mega.py，專注於解決原模組的 Bug 和相容性問題。
```

## 目錄

- [Python 模組 megapy(Forked Version 更新版本的 megapy)](#python-模組-megapyforked-version-更新版本的-megapy)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[megapy pypi](https://pypi.org/project/megapy/)

# 指令

```bash
# 安裝
pip install megapy
```

# 用法

```Python
from megapy import Mega

mega = Mega()
mega.login("your_email", "your_password")

# 上傳檔案
mega.upload("local_file.txt", "remote_folder/")

# 下載檔案
mega.download("remote_file_id", "local_file.txt")
```
