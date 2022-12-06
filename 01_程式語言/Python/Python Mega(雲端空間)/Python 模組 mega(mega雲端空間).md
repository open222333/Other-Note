# Python 模組 mega(mega雲端空間)

```
```

## 目錄

- [Python 模組 mega(mega雲端空間)](#python-模組-megamega雲端空間)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [指令工具相關](#指令工具相關)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[mega.py pypi](https://pypi.org/project/mega.py/)

### 指令工具相關

[MEGAcmd User Guide - Github](https://github.com/meganz/MEGAcmd/blob/master/UserGuide.md)

# 指令

```bash
# 安裝
pip install mega.py
```

# 用法

```Python
from mega import Mega


mega = Mega()

Login to Mega
m = mega.login(email, password)
# login using a temporary anonymous account
m = mega.login()

# Get user details
details = m.get_user()

# Get account balance (Pro accounts only)
balance = m.get_balance()

# Get account disk quota
quota = m.get_quota()

# Get account storage space
# specify unit output kilo, mega, gig, else bytes will output
space = m.get_storage_space(kilo=True)

# Get account files
files = m.get_files()

# Upload a file, and get its public link
file = m.upload('myfile.doc')
m.get_upload_link(file)
# see mega.py for destination and filename options

# Export a file or folder
public_exported_web_link = m.export('myfile.doc')
public_exported_web_link = m.export('my_mega_folder/my_sub_folder_to_share')
# e.g. https://mega.nz/#F!WlVl1CbZ!M3wmhwZDENMNUJoBsdzFng

# Find a file or folder
folder = m.find('my_mega_folder')
# Excludes results which are in the Trash folder (i.e. deleted)
folder = m.find('my_mega_folder', exclude_deleted=True)

# Upload a file to a destination folder
folder = m.find('my_mega_folder')
m.upload('myfile.doc', folder[0])
```
