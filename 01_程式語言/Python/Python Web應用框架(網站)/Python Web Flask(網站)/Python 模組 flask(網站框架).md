# Python 模組 flask(網站框架)

```
```

## 目錄

- [Python 模組 flask(網站框架)](#python-模組-flask網站框架)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [通知相關](#通知相關)
		- [資料庫 相關](#資料庫-相關)
		- [REST APIs 相關](#rest-apis-相關)
		- [gunicorn(WSGI伺服器)相關](#gunicornwsgi伺服器相關)
		- [nginx(WSGI伺服器)相關](#nginxwsgi伺服器相關)
		- [反向代理](#反向代理)
- [指令](#指令)
- [啟動方式](#啟動方式)
	- [選項1(官方推薦此選項)](#選項1官方推薦此選項)
	- [選項2](#選項2)
	- [選項3 (不建議使用)：](#選項3-不建議使用)
- [範例](#範例)
	- [基本入門](#基本入門)
	- [Flask + Nginx + Docker-compose 建議結構](#flask--nginx--docker-compose-建議結構)
	- [實作 Flask Blueprints(藍圖)](#實作-flask-blueprints藍圖)
		- [第一種 資料結構](#第一種-資料結構)
		- [第二種 資料結構](#第二種-資料結構)
		- [實作](#實作)
	- [實作 Flask Application Factories(工廠模式)](#實作-flask-application-factories工廠模式)
		- [實作](#實作-1)

## 參考資料

[flask pypi](https://pypi.org/project/flask/)

[flask 官方文檔](https://flask.palletsprojects.com/en/2.2.x/)

[【Flask 教學】2022 Flask 入門指南](https://www.maxlist.xyz/2020/05/01/flask-list/)

[flask核心机制：current_app](https://www.jianshu.com/p/4548516ca896)

[Flask 實作公開筆記](https://hackmd.io/@shaoeChen?tags=%5B%22flask%22%5D)

[在 Flask 中使用 Celery](http://www.pythondoc.com/flask-celery/first.html)

[Flask进阶系列(六)–蓝图(Blueprint)](http://www.bjhee.com/flask-ad6.html)

### 通知相關

[Web Push Notification Using Python - 使用 Python 的 Web 推送通知](https://raturi.in/blog/webpush-notification-using-python-and-flask/)

### 資料庫 相關

[MongoEngine 文檔](https://docs.mongoengine.org/guide/querying.html)

[flask-sqlalchemy Github](https://github.com/pallets/flask-sqlalchemy)

[flask-sqlalchemy 文檔](https://flask-sqlalchemy.palletsprojects.com/en/2.x/)

### REST APIs 相關

[Flask 傳入參數](https://ithelp.ithome.com.tw/m/articles/10263722)

### gunicorn(WSGI伺服器)相關

[gunicorn官方文檔](https://docs.gunicorn.org/en/stable/run.html)

[gunicorn指令](https://docs.gunicorn.org/en/latest/run.html#commonly-used-arguments)

[Flask with Gunicorn](https://sean22492249.medium.com/flask-with-gunicorn-9a37bca29227)

[Nginx+gunicorn+flask+docker演算法部署](https://www.796t.com/article.php?id=102596)


### nginx(WSGI伺服器)相關

[Dockerizing Flask with Postgres, Gunicorn, and Nginx](https://testdriven.io/blog/dockerizing-flask-with-postgres-gunicorn-and-nginx/)

[python flask项目Nginx代理添加前缀](https://www.codenong.com/cs105254408/)

[python flask项目Nginx代理添加前缀](https://www.twblogs.net/a/5efde90dd496dddbb541e9d2)

### 反向代理

[Flask Reverse Proxy](https://github.com/wilbertom/flask-reverse-proxy/blob/master/flask_reverse_proxy/__init__.py)

[Create Proxy for Python Flask Application](https://stackoverflow.com/questions/30743696/create-proxy-for-python-flask-application)

# 指令

```bash
# 安裝
pip install Flask

flask run --reload --debugger --host 0.0.0.0 --port 80
	–reload # 修改 py 檔後，Flask server 會自動 reload
	–debugger # 如果有錯誤，會在頁面上顯示是哪一行錯誤
	–host # 可以指定允許訪問的主機IP，0.0.0.0 為所有主機的意思
	–port # 自訂網路埠號的參數

# 更新資料庫
export FLASK_APP = myapp.py

# 輸入下面指令設定migrations資料夾：
# 執行指令後，專案中會出現一個migrations資料夾
flask db init

flask db stamp head

# 然後接著執行下面指令，這個指令可以設定migrations檔案，其中「-m」後面是屬於說明文字。
flask db migrate -m "說明文字"

# 執行upgrade指令，將migrations檔案更新至資料庫中。
flask db upgrade
```

# 啟動方式

## 選項1(官方推薦此選項)
```bash
export FLASK_APP=main.py
flask run

# Flask run 參數還可以加上以下指令
export FLASK_APP=main.py
flask run --reload --debugger --host 0.0.0.0 --port 80
	–reload # 修改 py 檔後，Flask server 會自動 reload
	–debugger # 如果有錯誤，會在頁面上顯示是哪一行錯誤
	–host # 可以指定允許訪問的主機IP，0.0.0.0 為所有主機的意思
	–port # 自訂網路埠號的參數
```

## 選項2

```bash
export FLASK_APP=app.py
python -m flask run
```


## 選項3 (不建議使用)：

```Python
# 在 main.py 的下方加入 if name == 'main':

if __name__ == '__main__':
    app.run()
```

```bash
# 在終端機輸入以下指令
python app.py
```

# 範例

## 基本入門

```
# 資料結構

| – Flask
` – main.py
```

```Python
# main.py

from flask import Flask
# __name__ 這邊是用來定位目前載入資料夾的位置，用來判別 template__folder 或 static_folder 資料夾位置
'''
Flask (import_name, static_path=None, static_url_path=None, static_folder=’static’,template_folder
=’templates’, instance_path=None, instance_relative_config=False, root_path=None)
'''
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'
```

## Flask + Nginx + Docker-compose 建議結構

[Flask 實作 Docker-compose (Flask+Nginx+PostgreSQL)](https://www.maxlist.xyz/2020/06/14/flask-docker-compose/)

[GitHub flask-template](https://github.com/hsuanchi/flask-template/tree/master/template3-docker-compose-flask-nginx-postgres)

```
template
├── docker-compose.yml
├── flask
│   ├── Dockerfile
│   ├── app
│   ├── app.ini
│   ├── main.py
│   ├── requirements.txt
│   └── venv
└── nginx
    ├── Dockerfile
    ├── nginx.conf
    ├── ssl.csr
    └── ssl.key
```

## 實作 Flask Blueprints(藍圖)

### 第一種 資料結構

```
ecommerce/
|
├── auth/
|   ├── templates/
|   |   └── auth/
|   |       ├── login.html
|   |       ├── forgot_password.html
|   |       └── signup.html
|   ├── __init__.py
|   └── auth.py
|
├── cart/
|   ├── templates/
|   |   └── cart/
|   |       ├── checkout.html
|   |       └── view.html
|   ├── __init__.py
|   └── cart.py
|
├── static/
|   ├── logo.png
|   ├── main.css
|   └── generic.js
|
├── app.py
├── config.py
└── models.py
```

### 第二種 資料結構

```
ecommerce/
|
├── static/
|   ├── logo.png
|   └── main.css
|
├── templates/
|   ├── auth/
|   |   ├── login.html
|   |   ├── forgot_password.html
|   |   └── signup.html
|   └── cart/
|       ├── checkout.html
|       └── view.html
|
├── view/
|   ├── auth.py
|   └── cart.py
|
├── app.py
├── config.py
└── models.py
```

### 實作

```
/flask
├── /view
│   └── api.py
└── main.py
```

`flask/main.py`

```Python
from flask import Flask, Blueprint
from view.api import app2

app = Flask(__name__)

@app.route('/')
def index():
    return "Hello index"

# 代表未來所有的 app2 所創建出來的路徑，前面網址都需要加上 pages
# @app2.route('/app2')，網址是 http://127.0.0.1:5000/pages/app2
app.register_blueprint(app2, url_prefix='/pages')


```

`flask/view/api.py`

```Python
from flask import Blueprint

app2 = Blueprint(
	'app2',
	__name__,
	# 常用的參數設定： static_folder、template_folder
	# 指定新註冊的 app2 使用的 static 位置
	static_folder='static',
	# 指定新註冊的 app2 使用的 template 位置
	template_folder='templates'
)

@app2.route('/app2')
def show(page):
    return "Hello Blueprint app2"
```

## 實作 Flask Application Factories(工廠模式)

```
關於工廠模式 – 維基百科 的補充：

建立物件可能會導致大量的重複代碼，可能會需要複合物件存取不到的資訊，也可能提供不了足夠級別的抽象，還可能並不是複合物件概念的一部分。
工廠方法模式通過定義一個單獨的建立物件的方法來解決這些問題。
```

### 實作

```
/flask
├── /app
│   ├── __init__.py
│   └── config
│       └── config.py
└── main.py
```