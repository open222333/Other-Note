# Python 工具 sphinx(自動生成文檔工具)

```
自動萃取註解成一個可讀檔(html形式)
```

## 目錄

- [Python 工具 sphinx(自動生成文檔工具)](#python-工具-sphinx自動生成文檔工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[sphinx pypi](https://pypi.org/project/sphinx/)

[官方網站](https://www.sphinx-doc.org/en/master/tutorial/index.html)

[隨手養成 Python 好習慣－勤註解、善用自動文件產生工具 Sphinx](https://myapollo.com.tw/zh-tw/python-autodoc/)

[Coding起來- Python工具-Sphinx 操作教學](https://chwang12341.medium.com/coding%E8%B5%B7%E4%BE%86-python%E5%B7%A5%E5%85%B7-sphinx-%E6%93%8D%E4%BD%9C%E6%95%99%E5%AD%B8-d35640a33ffe)

# 指令

```bash
# 安裝
pip install sphinx
```

# 用法

初始需手動建立資料夾結構:

```
python_project/
├── doc/
└── src/
```

```bash
# 建立sphinx專案(在Python專案資料夾)
sphinx-quickstart doc

	# 會詢問一些資訊
	# Separate source and build directories(y/n): 是否把source跟build資料夾分開
	# Project name: (專案名稱)
	# Author name(s): (作者)
	# Project release []: (版本)
	# Project language [en] : (語言) 英文 en, 中文 zh-TW
```

修改 doc/source/conf.py 設定檔

```Python
```

修改 doc/source/index.rst 檔案

```
```