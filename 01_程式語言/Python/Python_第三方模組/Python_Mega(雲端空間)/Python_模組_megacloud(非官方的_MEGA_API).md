# Python 模組 megacloud(非官方的 MEGA API)

```
提供更直接的命令列操作，支援 API 調用。
```

## 目錄

- [Python 模組 megacloud(非官方的 MEGA API)](#python-模組-megacloud非官方的-mega-api)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[megacloud pypi](https://pypi.org/project/megacloud/)

# 指令

```bash
# 安裝
pip install megacloud
```

# 用法

```Python
from megacloud import MegaCloud

mc = MegaCloud(email="your_email", password="your_password")
mc.login()

# 列出檔案
print(mc.list_files())

# 上傳檔案
mc.upload("local_file.txt", "/remote/folder/")
```
