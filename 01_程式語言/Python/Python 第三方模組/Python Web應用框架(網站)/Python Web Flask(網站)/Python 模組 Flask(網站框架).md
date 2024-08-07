# Python 模組 Flask(網站框架)

```
```

## 目錄

- [Python 模組 Flask(網站框架)](#python-模組-flask網站框架)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [通知相關](#通知相關)
    - [資料庫 相關](#資料庫-相關)
    - [REST APIs 相關](#rest-apis-相關)
    - [gunicorn(WSGI伺服器)相關](#gunicornwsgi伺服器相關)
    - [nginx(WSGI伺服器)相關](#nginxwsgi伺服器相關)
    - [反向代理 相關](#反向代理-相關)
    - [Flask Application Factories 工廠模式 相關](#flask-application-factories-工廠模式-相關)
    - [Cookie相關](#cookie相關)
    - [Session相關](#session相關)
    - [CSRF相關](#csrf相關)
    - [網站模版樣本 相關](#網站模版樣本-相關)
    - [教學範例相關](#教學範例相關)
    - [細節相關](#細節相關)
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
    - [使用 Blueprint 進行模組化 簡易範例](#使用-blueprint-進行模組化-簡易範例)
  - [實作 Flask Application Factories(工廠模式)](#實作-flask-application-factories工廠模式)
    - [實作](#實作-1)
  - [實作 Cookie 設定](#實作-cookie-設定)
  - [實作 Session 設定](#實作-session-設定)
    - [方法一：使用 Flask 內建的 Session](#方法一使用-flask-內建的-session)
    - [方法二：使用 Flask 擴充套件 Flask-Session](#方法二使用-flask-擴充套件-flask-session)
  - [實作 Flask Session-base login 登入驗證](#實作-flask-session-base-login-登入驗證)
    - [架構](#架構)
    - [實作 Flask Login 驗證](#實作-flask-login-驗證)
  - [實作 Flask CSRF Protection](#實作-flask-csrf-protection)
    - [避免 CSRF 攻擊](#避免-csrf-攻擊)
      - [方法一. CSRF token](#方法一-csrf-token)
      - [方法二. SameSite cookie](#方法二-samesite-cookie)
  - [變量與請求, 各種 API 範例](#變量與請求-各種-api-範例)
  - [全域（global）的容器對象](#全域global的容器對象)
  - [實作 flask與pymysql](#實作-flask與pymysql)
    - [`講解 _app_ctx_stack.top`](#講解-_app_ctx_stacktop)
  - [](#)

## 參考資料

[flask pypi](https://pypi.org/project/flask/)

[flask 官方文檔](https://flask.palletsprojects.com/en/latest/)

[API - Flask 官方文件](https://flask.palletsprojects.com/en/latest/api/)

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

### 反向代理 相關

[Flask Reverse Proxy](https://github.com/wilbertom/flask-reverse-proxy/blob/master/flask_reverse_proxy/__init__.py)

[Create Proxy for Python Flask Application](https://stackoverflow.com/questions/30743696/create-proxy-for-python-flask-application)

### Flask Application Factories 工廠模式 相關

[【Flask 教學】實作 Flask Application Factories 工廠模式](https://www.maxlist.xyz/2020/08/06/flask-application-factories/)

[The Application Factory](https://flask.palletsprojects.com/en/1.1.x/tutorial/factory/#the-application-factory)

[Finding Extensions](https://flask.palletsprojects.com/en/1.1.x/extensions/#building-extensions)

[Flask Factory Pattern to set up your project.](https://medium.com/thedevproject/flask-factory-pattern-to-setup-your-project-8fe7d6b23247)

### Cookie相關

[[Flask教學] 5分鐘快速設定 Flask 取得 Cookie](https://www.maxlist.xyz/2019/05/11/flask-cookie/)

### Session相關

[[Flask教學] Flask Session 使用方法和介紹](https://www.maxlist.xyz/2019/06/29/flask-session/)

[Flask-Session](https://flask-session.readthedocs.io/en/latest/) <- `可查看Session參數 細節`

[【Flask教學系列】實作 Flask Session-base login 登入驗證](https://www.maxlist.xyz/2020/05/24/flask-session-base-login/)

[Python Flask_變量與請求](https://hackmd.io/@shaoeChen/rJnJWaq1z?type=view)

### CSRF相關

[【Flask教學系列】實作 Flask CSRF Protection](https://www.maxlist.xyz/2020/05/07/flask-csrf/)

['SameSite' cookie attribute](https://caniuse.com/?search=samesite)

### 網站模版樣本 相關

[Flask Website Templates - Open-Source and Free](https://blog.appseed.us/flask-templates-curated-list-18vq/)

[Flask 網站模板 - 開源種子項目](https://www.codementor.io/@chirilovadrian360/flask-website-templates-open-source-seed-projects-1b6tya9jnl)

[使用 Flask 和 Vue.js 来构建全栈单页应用](https://learnku.com/python/t/24985)

### 教學範例相關

[Flask實作](https://hackmd.io/@shaoeChen/HJiZtEngG/https%3A%2F%2Fhackmd.io%2Fs%2FrkgXYoBeG)

### 細節相關

[Flask应用上下文 - _app_ctx_stack.top](https://zhuanlan.zhihu.com/p/150219295)

# 指令

```bash
# 安裝
pip install Flask
pip install flask

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
    app.run(debug=True, host='0.0.0.0', port=5000)
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

[GitHub flask-template](https://github.com/hsuanchi/flask-template/tree/master/template3-docker-compose-flask-nginx-postgres) <-
`查詢各瀏覽器版本的 SameSite`

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

### 使用 Blueprint 進行模組化 簡易範例

```Python
from flask import Flask, Blueprint

app = Flask(__name__)
api_bp = Blueprint('api', __name__)

@api_bp.route('/endpoint1')
def endpoint1():
    return 'This is endpoint 1'

@api_bp.route('/endpoint2')
def endpoint2():
    return 'This is endpoint 2'

app.register_blueprint(api_bp, url_prefix='/api')

if __name__ == '__main__':
    app.run(debug=True)
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

`flask/main.py`

```Python
from app import create_app

app = create_app('development')
```

`flask/app/__init__.py`

```Python
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from .config.config import config

db = SQLAlchemy()

def create_app(config_name):

    app = Flask(__name__)
    app.config.from_object(config[config_name])

    db.init_app(app)

    @app.route('/')
    def index():
        return 'welcome'

    return app
```

`flask/app/config/config.py`

```Python
import os
import datetime

basedir = os.path.abspath(os.path.dirname(__file__))


def create_sqlite_uri(db_name):
    return "sqlite:///" + os.path.join(basedir, db_name)


class BaseConfig:  # 基本配置
    SECRET_KEY = 'THIS IS MAX'
    PERMANENT_SESSION_LIFETIME = datetime.timedelta(days=14)

class DevelopmentConfig(BaseConfig):
    DEBUG = False
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://username:password@ip:3306/tablename'

class TestingConfig(BaseConfig):
    TESTING = True
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_DATABASE_URI = create_sqlite_uri("test.db")

config = {
    'development': DevelopmentConfig,
    'testing': TestingConfig,
}
```

## 實作 Cookie 設定

```
因為 HTTP stateless 無狀態的設計，Cookie 提供了更容易實現保存 Session 的方式，來讓 HTTP request 保有狀態的功能，達到區分出不同的 HTTP request 是來自同一個瀏覽器
```

```Python
# 設定 Cookie
@app.route("/set")
def setcookie():
    resp = make_response('Setting cookie!')
    resp.set_cookie(key='framework', value='flask', expires=time.time()+6*60)
    return resp

# 取得 Cookie
@app.route("/get")
def getcookie():
    framework = request.cookies.get('framework')
    return 'The framework is ' + framework

# 刪除 Cookie
@app.route('/del')
def del_cookie():
    res = Response('delete cookies')
    res.set_cookie(key='framework', value='', expires=0)
    return res
```

## 實作 Session 設定

### 方法一：使用 Flask 內建的 Session

```
讀取 session.get(‘username’)
新增/更新 session[‘username’] = ‘name’
刪除 session[‘username’] = False
```

```Python
from flask import Flask,session
from datetime import timedelta
import os

app = Flask(__name__)
app.config['SECRET_KEY'] = os.urandom(24)
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(days=31)


 # 设置session
 @app.route('/')
 def index():

    #設置session
     session['username'] = 'name'
    #如果設置了 session.permanent 為 True，那麽過期時間是31天
     session.permanent = True

    ＃讀取session
     session.get('username')

    #刪除session
     session['username'] = False
```

### 方法二：使用 Flask 擴充套件 Flask-Session

```bash
# 安裝 Flask-Session 擴充套件
pip install flask-session
```

## 實作 Flask Session-base login 登入驗證

### 架構

`Flask Application Factories (工廠模式)`
`MVC (Model–view–controller)`

* Model – 負責資料庫操作和儲存。
* View (Flask 內稱為 Templates) – 負責使用者介面設計。
* Controller (Flask 內稱為 View) – 負責對 Request / Response 處理，和負責與 Model 的資料溝通，並將資料串接到 View (Templates)。

```
├── app
│   ├── __init__.py
│   ├── config
│   │   ├── config.py
│   │   └── test.db
│   ├── model
│   │   └── user.py
│   ├── templates
│   │   ├── login.html
│   │   └── signup.html
│   └── view
│       ├── abort_msg.py
│       └── auth.py
├── main.py
└── requirements.txt
```

### 實作 Flask Login 驗證

1. 設定 註冊會員頁

```Python
# app/view/auth.py
class Signup(Resource):
    def post(self):
        try:
            # 資料驗證
            user_data = users_schema.load(request.form, partial=True)
            # 註冊
            new_user = UserModel(user_data)
            new_user.save_db()
            new_user.save_session()
            return {'msg': 'registration success'}, 200

        except ValidationError as error:
            return {'errors': error.messages}, 400

        except Exception as e:
            return {'errors': abort_msg(e)}, 500

    def get(self):
        return make_response(render_template('signup.html'))

api.add_resource(Signup, '/signup')

new_user = UserModel(user_data) # 實例化新使用者
new_user.save_db() # 將新使用者存入 db
new_user.save_session() # 設定新使用者的 session
```

2. 設定 model

```Python
# app/mdoel/user.py
class UserModel(db.Model):
    __tablename__ = 'user'
    uid = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(30), unique=True)
    password_hash = db.Column(db.String(255))
    role = db.Column(db.String(10), default='normal')
    insert_time = db.Column(db.DateTime, default=datetime.now)
    update_time = db.Column(db.DateTime,
                            onupdate=datetime.now,
                            default=datetime.now)

    def __init__(self, user_data):
        self.name = user_data['name']
        self.password = user_data['password']

    @property
    def password(self):
        raise AttributeError('passowrd is not readabilty attribute')

    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)

    def verify_password(self, password):
        return check_password_hash(self.password_hash, password)

    @classmethod
    def get_user(cls, name):
        return cls.query.filter_by(name=name).first()

    def save_db(self):
        db.session.add(self)
        db.session.commit()

	# 在 Session 中儲存了使用者的 username 和 role 以及 uid
	# 而如果登出後只需要呼叫 remove_session() 就會將剛剛設定的 Session 清空
    def save_session(self):
        session['username'] = self.name
        session['role'] = self.role
        session['uid'] = self.uid

    @staticmethod
    def remove_session():
        session['username'] = ''
        session['role'] = ''
        session['uid'] = ''


class UserSchema(Schema):
    uid = fields.Integer(dump_only=True)
    name = fields.String(required=True, validate=validate.Length(3))
    password = fields.String(required=True, validate=validate.Length(6))
    role = fields.String()
    insert_time = fields.DateTime()
    update_time = fields.DateTime()
```

3. 驗證使用者登入狀態

```Python
@app.route('/normal_member')
@check_login('normal')
def member_normal_page():
    return 'ok'
```

```Python
# 首先會先去檢查使用者 Session 內有沒有 role session.get('role')，如果沒有 role 則噴 401 權限不足錯誤訊息。
# 有 role 的話，則判斷裝飾詞傳入的 check_role，與 Session 內的 role 是否有資格登入。
def check_login(check_role):
    def decorator(func):
        def wrap(*args, **kw):
            user_role = session.get('role')

            if user_role == None or user_role == '':
                return abort(401)
            else:
                if check_role == 'admin' and check_role == user_role:
                    return func(*args, **kw)
                if check_role == 'normal':
                    return func(*args, **kw)
                else:
                    return abort(401)

        wrap.__name__ = func.__name__
        return wrap

    return decorator
```

## 實作 Flask CSRF Protection

```
CSRF 是一種 Web 上的攻擊手法，全名是 Cross Site Request Forgery，跨站請求偽造，又稱 one-click attack。

CSRF 在使用者不知情的情況下，讓使用者的瀏覽器自動送出請求給目標網站，利用使用者當前的身份去做一些未經過授權的操作以達攻擊目的。
```

### 避免 CSRF 攻擊

#### 方法一. CSRF token

```bash
# 安裝 flask-wtf
pip install flask-wtf
```

```Python
# 從 flask_wtf 中載入 CSRFProtect，並設定 SECRET_KEY
import os
from flask_wtf.csrf import CSRFProtect

app.config['SECRET_KEY'] = os.urandom(24)

csrf = CSRFProtect(app)
```

```html
<!-- 所有表單中加入 hidden 欄位 csrf_token -->
<form method="post">
    <input type="hidden" name="csrf_token" value="{{ csrf_token() }}"/>
    <input type="text" name="email" class="form-control form-control-lg" placeholder="max@email.com">
</form>
```

```JavaScript
// 如果是使用 ajax 或 axios 可在 header 中加入 csrf-token
<script>
var csrf = "{{ csrf_token() }}";
axios.post('/auth/login', data, {
        headers: {'x-csrf-token': csrf}
        })
</script>
```

#### 方法二. SameSite cookie

```
SameSite 的用途為防止瀏覽器因跨站請求傳送 cookies，目的是降低跨站資料外洩與 CSRF 攻擊的風險。SameSite 設有三個數值層級，分別為 Strict, Lax 和 None，由嚴格至寬鬆不同程度地限制 cookies 的傳輸。

當 cookie 中設定了 SameSite = ‘Strict’ 的話，代表著這個 cookie 只允許 same site 情況下使用。
而 SameSite = ‘Lax’ 放寬部份限制，只限制當使用 POST、PUT 或 DELETE 方法進行跨站請求時，就不會帶上此 cookie。

目前並非所有的瀏覽器版本都支持 SameSite 的功能，所以並非加上了 SameSite=’Strict’ 就可以完全避免 CSRF 的攻擊。
```

```Python
# Flask 實作 SameSite cookie
'''
set cookie 時，有多設定三個參數：secure=False, httponly=False, samesite=None

secure：如果設定為 True，表示此 cookie 僅在 Https 時才被傳送
httponly：如設定為 True，表示 JavaScript 的Document.cookie API 無法取得 HttpOnly cookies
samesite：可以設定成 Strict、Lax 和 None，功能如同剛剛所述
'''
@app.route("/set")
def setcookie():
    resp = make_response('Setting cookie!')
    resp.set_cookie(key='framework', value='flask', expires=time.time()+6*60, secure=False, httponly=False, samesite=None)
    return resp
```

## 變量與請求, 各種 API 範例

```
如果有多個before_request的時候，只要中間有執行return的部份，後續就不再被執行。

before_first_request
註冊一個函數，在處理第一個請求之前執行
before_request
註冊一個函數，在每次請求之前執行
after_request
註冊一個函數，如果沒有未處理的異常拋出，在每次請求之後執行
teardown_request
註冊一個函數，如果有未處理的異常拋出，在每次請求之後執行
```

```Python
from flask import Flask, g, request, jsonify
from werkzeug.utils import secure_filename
import os

app = Flask(__name__)


# 前置請求鉤子 1
@app.before_request
def before_request():
    """
    這個函數在每次請求進入應用程序時執行。
    它會印出 'before request started' 和請求的 URL。
    """
    print('before request started')
    print(request.url)

# 前置請求鉤子 2


@app.before_request
def before_request2():
    """
    這個函數同樣在每次請求進入時執行。
    它會印出 'before request started 2' 和請求的 URL。
    它在 'g' 對象中設置了一個名為 'name' 的變數為 "Test request"。
    """
    print('before request started 2')
    print(request.url)
    g.name = "Test request"

# 後置請求鉤子


@app.after_request
def after_request(response):
    """
    這個函數在每次請求完成並生成響應後執行。
    它會印出 'after request finished' 和請求的 URL。
    它在響應的標頭中添加了一對 "key: value"。
    """
    print('after request finished')
    print(request.url)
    response.headers['key'] = 'value'
    return response

# 拆除請求鉤子


@app.teardown_request
def teardown_request(exception):
    """
    這個函數在請求上下文被撤銷時執行。
    它會印出 'teardown request' 和請求的 URL。
    """
    print('teardown request')
    print(request.url)


@app.route('/abc/<name>/<age>')
def greet(name, age):
    """帶有參數的 api

    Args:
        name (_type_): _description_
        age (_type_): _description_

    Returns:
        _type_: _description_
    """
    g.name = name
    g.age = age
    return 'Hello, %s! You are %s years old.' % (name, age)


@app.route('/abc', methods=['POST'])
def post_api():
    data = request.form.get('data')

    # 假設你有一個名為 "data" 的表單字段
    if data:
        g.name = data
        return 'Hello, %s!' % data
    else:
        return 'Please provide data in the POST request.'


@app.route('/api/data', methods=['GET', 'POST'])
def api_data():
    """處理 GET 和 POST 請求

    Returns:
        _type_: _description_
    """
    if request.method == 'GET':
        return 'Received a GET request.'
    elif request.method == 'POST':
        data = request.get_json()
        return f'Received a POST request with data: {data}'


@app.route('/upload', methods=['POST'])
def upload_file():
    """文件上傳 API

    Returns:
        _type_: _description_
    """
    if 'file' not in request.files:
        return 'No file part'

    file = request.files['file']

    if file.filename == '':
        return 'No selected file'

    if file:
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        return 'File uploaded successfully'


@app.route('/abc', methods=['*'])
def index():
    """
    處理 '/abc' 端點的請求，支援所有 HTTP 方法。

    如果請求中包含名為 'data' 的表單字段，將其值存儲在 g.name 中，
    並返回包含該值的歡迎消息。否則，返回請求中沒有提供 'data' 的提示信息。
    """
    data = request.form.get('data')  # 從表單中獲取 'data' 字段的值

    if data:
        g.name = data  # 將 'data' 的值存儲在 g.name 中
        return 'Hello, %s!' % data  # 返回歡迎消息
    else:
        return 'Please provide data in the request.'  # 返回提示信息


@app.route('/api/data', methods=['POST'])
def api_data():
    """
    處理 '/api/data' 端點的 POST 請求。

    接收請求頭中的 'Authorization'，以及表單中的 'username' 和 'password'。
    返回包含接收到的數據的 JSON 響應。
    """
    # 檢查是否包含 'Authorization' 請求頭
    authorization_header = request.headers.get('Authorization')

    if authorization_header:
        # 獲取 'username' 和 'password' 表單字段的值
        username = request.form.get('username')
        password = request.form.get('password')

        # 在實際應用中，這裡應該有身份驗證邏輯

        # 組合回應 JSON
        response_data = {
            'Authorization': authorization_header,
            'username': username,
            'password': password
        }

        return jsonify(response_data)

    else:
        return 'Authorization header is missing.', 401  # 返回未授權的錯誤碼

@app.route('/api/form', methods=['POST'])
def api_form():
    """
    處理 '/api/form' 端點的 POST 請求。

    接收表單數據中的 'name' 和 'email' 字段。
    返回包含接收到的數據的 JSON 響應。
    """
    # 獲取 'name' 和 'email' 表單字段的值
    name = request.form.get('name')
    email = request.form.get('email')

    # 在實際應用中，這裡可以有更多的邏輯，例如數據驗證、存儲等

    # 組合回應 JSON
    response_data = {
        'name': name,
        'email': email
    }

    return jsonify(response_data)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
```

## 全域（global）的容器對象

```Python
from flask import Flask, g

'''
在 Flask 中，g 是一個全域（global）的容器對象，用於存儲在請求生命週期中共享的數據。

g 的名稱來自 "context globals" 的縮寫，表示它是與請求上下文（request context）相關聯的全域對象。

在 Flask 應用中，當一個請求進入時，Flask 會創建一個請求上下文，該上下文存在於整個請求的生命週期中。

g 對象允許在這個上下文中存儲數據，以便在應用的不同部分共享它。
'''

app = Flask(__name__)

@app.before_request
def before_request():
    g.user = get_current_user()  # 假設有一個函數用於獲取當前用戶

@app.route('/')
def index():
    # 在這裡可以訪問 g.user，它將包含在 before_request 中設置的用戶對象
    return f'Hello, {g.user.username}'

if __name__ == '__main__':
    app.run(debug=True)
```

## 實作 flask與pymysql

```Python
import pymysql.cursors
from flask import _app_ctx_stack, current_app


class FlaskMySQL(object):

    def __init__(self, app=None):
        self.app = app
        if app is not None:
            self.init_app(app)

    def init_app(self, app):
        app.config.setdefault('MYSQL_HOST', 'localhost')
        app.config.setdefault('MYSQL_USER', None)
        app.config.setdefault('MYSQL_PASSWORD', None)
        app.config.setdefault('MYSQL_DB', None)
        app.config.setdefault('MYSQL_PORT', 3306)
        app.config.setdefault('MYSQL_UNIX_SOCKET', None)
        app.config.setdefault('MYSQL_CONNECT_TIMEOUT', 10)
        app.config.setdefault('MYSQL_READ_DEFAULT_FILE', None)
        app.config.setdefault('MYSQL_USE_UNICODE', True)
        app.config.setdefault('MYSQL_CHARSET', 'utf8')
        app.config.setdefault('MYSQL_SQL_MODE', None)
        app.config.setdefault('MYSQL_CURSORCLASS', None)

        if hasattr(app, 'teardown_appcontext'):
            app.teardown_appcontext(self.teardown)

    @property
    def connect(self):
        kwargs = {}

        if current_app.config['MYSQL_HOST']:
            kwargs['host'] = current_app.config['MYSQL_HOST']

        if current_app.config['MYSQL_USER']:
            kwargs['user'] = current_app.config['MYSQL_USER']

        if current_app.config['MYSQL_PASSWORD']:
            kwargs['passwd'] = current_app.config['MYSQL_PASSWORD']

        if current_app.config['MYSQL_DB']:
            kwargs['db'] = current_app.config['MYSQL_DB']

        if current_app.config['MYSQL_PORT']:
            kwargs['port'] = current_app.config['MYSQL_PORT']

        if current_app.config['MYSQL_UNIX_SOCKET']:
            kwargs['unix_socket'] = current_app.config['MYSQL_UNIX_SOCKET']

        if current_app.config['MYSQL_CONNECT_TIMEOUT']:
            kwargs['connect_timeout'] = \
                current_app.config['MYSQL_CONNECT_TIMEOUT']

        if current_app.config['MYSQL_READ_DEFAULT_FILE']:
            kwargs['read_default_file'] = \
                current_app.config['MYSQL_READ_DEFAULT_FILE']

        if current_app.config['MYSQL_USE_UNICODE']:
            kwargs['use_unicode'] = current_app.config['MYSQL_USE_UNICODE']

        if current_app.config['MYSQL_CHARSET']:
            kwargs['charset'] = current_app.config['MYSQL_CHARSET']

        if current_app.config['MYSQL_SQL_MODE']:
            kwargs['sql_mode'] = current_app.config['MYSQL_SQL_MODE']

        if current_app.config['MYSQL_CURSORCLASS']:
            kwargs['cursorclass'] = getattr(pymysql.cursors, current_app.config['MYSQL_CURSORCLASS'])

        return pymysql.connect(**kwargs)

    @property
    def connection(self):
        """Attempts to connect to the MySQL server.

        :return: Bound MySQL connection object if successful or ``None`` if
            unsuccessful.
        """

        ctx = _app_ctx_stack.top
        if ctx is not None:
            if not hasattr(ctx, 'pymysql_db'):
                ctx.pymysql_db = self.connect
            return ctx.pymysql_db

    def teardown(self, exception):
        ctx = _app_ctx_stack.top
        if hasattr(ctx, 'pymysql_db'):
            ctx.pymysql_db.close()
```

### `講解 _app_ctx_stack.top`

Flask支持多應用，即多個app

為了實現app隔離，Flask使用LocalStack數據結構來記錄每個app的上下文(即運行配置)。
這裡定義了一個_app_ctx_stack 變量，類型為LocalStack()，用來存儲app的上下文。

```Python
# globals.py(site-packages\flask\globals.py)

def _find_app():
    top = _app_ctx_stack.top
    if top is None:
        raise RuntimeError(_app_ctx_err_msg)
    return top.app


# context locals
_request_ctx_stack = LocalStack()
_app_ctx_stack = LocalStack()
current_app = LocalProxy(_find_app)
request = LocalProxy(partial(_lookup_req_object, 'request'))
session = LocalProxy(partial(_lookup_req_object, 'session'))
g = LocalProxy(partial(_lookup_app_object, 'g'))
```

往裡面添加內容

```Python
# app.py(site-packages\flask\app.py)
def app_context(self):
	"""Create an :class:`~flask.ctx.AppContext`. Use as a ``with``
	block to push the context, which will make :data:`current_app`
	point at this application.

	An application context is automatically pushed by
	:meth:`RequestContext.push() <flask.ctx.RequestContext.push>`
	when handling a request, and when running a CLI command. Use
	this to manually create a context outside of these situations.

	::

		with app.app_context():
			init_db()

	See :doc:`/appcontext`.

	.. versionadded:: 0.9
	"""
	return AppContext(self)
```

在創建app實例的時候，一般會緊跟著加載相關配置，例如

```Python
app = Flask(__name__)
with app.app_context():
	init_db()
	init_log()
```

在調用app_context的時候，會返回AppContext(self)。

```Python
# site-packages\flask\ctx.py
class AppContext(object):
    """The application context binds an application object implicitly
    to the current thread or greenlet, similar to how the
    :class:`RequestContext` binds request information.  The application
    context is also implicitly created if a request context is created
    but the application is not on top of the individual application
    context.
    """

    def __init__(self, app):
        self.app = app
        self.url_adapter = app.create_url_adapter(None)
        self.g = app.app_ctx_globals_class()

        # Like request context, app contexts can be pushed multiple times
        # but there a basic "refcount" is enough to track them.
        self._refcnt = 0

    def push(self):
        """Binds the app context to the current context."""
        self._refcnt += 1
        if hasattr(sys, 'exc_clear'):
            sys.exc_clear()
        _app_ctx_stack.push(self)
        appcontext_pushed.send(self.app)

    def pop(self, exc=_sentinel):
        """Pops the app context."""
        try:
            self._refcnt -= 1
            if self._refcnt <= 0:
                if exc is _sentinel:
                    exc = sys.exc_info()[1]
                self.app.do_teardown_appcontext(exc)
        finally:
            rv = _app_ctx_stack.pop()
        assert rv is self, 'Popped wrong app context.  (%r instead of %r)' \
            % (rv, self)
        appcontext_popped.send(self.app)

    def __enter__(self):
        self.push()
        return self

    def __exit__(self, exc_type, exc_value, tb):
        self.pop(exc_value)

        if BROKEN_PYPY_CTXMGR_EXIT and exc_type is not None:
            reraise(exc_type, exc_value, tb)
```

AppContext類有一個_enter方法，當with語句運行的時候，會調用_enter_方法，

而_ enter _裡面的push方法就將app相關的配置push到_app_ctx_stack裡面。

結合globals.py裡面的_find_app，如果要拿到當前的app，就可以使用下列語句

```Python
from flask import current_app
```

## 

```Python
from flask import Flask, request

app = Flask(__name__)

@app.route('/example', methods=['GET', 'POST'])
def example():
    # 所有請求資料
    request_data = request.data  # 字節形式的請求資料
    request_json = request.get_json()  # JSON 資料
    request_form = request.form  # 表單資料
    request_args = request.args  # URL 參數
    request_headers = request.headers  # 請求標頭
    request_files = request.files  # 上傳的文件
    request_cookies = request.cookies  # 請求的 Cookie

    # 將所有資料放入一個字典中
    all_data = {
        "data": request_data,
        "json": request_json,
        "form": request_form,
        "args": request_args,
        "headers": dict(request_headers),
        "files": request_files,
        "cookies": request_cookies
    }

    return all_data

if __name__ == '__main__':
    app.run(debug=True)
```
