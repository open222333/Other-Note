# Python 模組 megaclient(非官方的 MEGA API)

```
一個非官方的 MEGA API 模組，提供更靈活的功能，支持檔案上傳、下載、登入等。
```

## 目錄

- [Python 模組 megaclient(非官方的 MEGA API)](#python-模組-megaclient非官方的-mega-api)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[megaclient pypi](https://pypi.org/project/megaclient/)

# 指令

```bash
# 安裝
pip install megaclient
```

# 用法

```Python
from megaclient.megaclient import MegaClient

client = MegaClient()
client.login("your_email", "your_password")

# 上傳檔案
client.upload("local_file.txt", "/remote_folder/")

# 下載檔案
client.download("/remote_folder/remote_file.txt", "local_file.txt")
```
