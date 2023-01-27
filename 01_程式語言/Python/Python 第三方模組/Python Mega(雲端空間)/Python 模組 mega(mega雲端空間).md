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
	- [範例](#範例)
	- [資料欄位](#資料欄位)

## 參考資料

[mega.py pypi](https://pypi.org/project/mega.py/)

[mega.py Github](https://github.com/odwyersoftware/mega.py)

[mega.py API_INFO.md 詳細說明](https://github.com/odwyersoftware/mega.py/blob/master/API_INFO.md)

### 指令工具相關

[MEGAcmd User Guide - Github](https://github.com/meganz/MEGAcmd/blob/master/UserGuide.md)

[mega sdk](https://github.com/meganz/sdk)

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

# 根據 private id 刪除檔案
client.destroy(private_id)

# 根據 private id 搜尋檔案
r = client.find(handle=private_id)
```

## 範例

```Python
from general import MEGA_ACCOUNT, MEGA_PASSWORD
from mega import Mega
from pprint import pprint
from datetime import datetime

'''測試mega'''


mega = Mega()
client = mega.login(MEGA_ACCOUNT, MEGA_PASSWORD)
folder = 'Upload'

def get_folder_files(folder):
    result = client.find(folder)
    folder_id = result[0]
    all_files = client.get_files()
    folder_files = {}
    for _, file_info in all_files.items():
        if file_info['p'] == folder_id:
            # file_info['h']: private id
            folder_files[file_info['h']] = file_info

    # pprint(folder_files)
    return folder_files


def remove_file(private_id):
    client.destroy(private_id)


def remove_file(private_id):
    return client.find(handle=private_id)
```

## 資料欄位

```json

{'f': [
	{
		'a': '',
        'h': '',
        'k': '',
        'p': '',
        's': 500000000,
        't': 0,
        'ts': 1672039067,
        'u': ''
	}]
}

Node attributes (json properties)
'a' Type
'h' Id
'p' Parent Id
'a' encrypted Attributes (within this: 'n' Name)
'k' Node Key
'u' User Id
's' Size
'ts' Time Stamp

Node types
0 File
1 Folder
2 Root Folder
3 Inbox
4 Trash
-1 Dummy
```