# Python 模組 Flask-Minify(縮小 HTML)

```
最小化代碼就是最小化網頁中存在的代碼：
	刪除多餘的空格
	刪除評論
	緊縮變量名

儘管註釋和空格有助於代碼的可讀性，但它們會增加應用程序的大小。結果，它增加了網絡的帶寬請求。
縮小 HTML 將：
	更快地加載網頁
	減少文件大小和帶寬
	改善用戶體驗和搜索引擎優化
```

## 目錄

- [Python 模組 Flask-Minify(縮小 HTML)](#python-模組-flask-minify縮小-html)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[Flask-Minify pypi](https://pypi.org/project/Flask-Minify/)

# 指令

```bash
# 安裝
pip install Flask-Minify
```

# 用法

```Python
from flask import Flask
from flask_minify import Minify

# 縮小每個請求。這將縮小從應用程序返回的每個請求。
app = Flask(__name__)
Minify(app=app, html=True, js=True, cssless=True)
```

```Python
from flask import Flask
from flask_minify import Minify, decorators as minify_decorators

app = Flask(__name__)
Minify(app=app, passive=True)

# 只縮小裝飾路線。這將僅縮小指定的路線。其他人將正常送達。
@app.route('/')
@minify_decorators.minify(html=True, js=True, cssless=True)
def example():
  return '<h1>Example...</h1>'
```
