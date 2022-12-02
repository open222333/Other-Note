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
```
