# Python 模組 flask(網站框架)

## 參考資料

[flask pypi](https://pypi.org/project/flask/)

[【Flask 教學】2022 Flask 入門指南](https://www.maxlist.xyz/2020/05/01/flask-list/)

[flask核心机制：current_app](https://www.jianshu.com/p/4548516ca896)

[Flask 實作公開筆記](https://hackmd.io/@shaoeChen?tags=%5B%22flask%22%5D)

[flask-sqlalchemy Github](https://github.com/pallets/flask-sqlalchemy)

[flask-sqlalchemy 文檔](https://flask-sqlalchemy.palletsprojects.com/en/2.x/)

[Dockerizing Flask with Postgres, Gunicorn, and Nginx](https://testdriven.io/blog/dockerizing-flask-with-postgres-gunicorn-and-nginx/)

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

# 啟動方式
# ▍選項1： (官方推薦此選項)
export FLASK_APP=main.py
flask run

# Flask run 參數還可以加上以下指令
export FLASK_APP=main.py
flask run --reload --debugger --host 0.0.0.0 --port 80
	–reload # 修改 py 檔後，Flask server 會自動 reload
	–debugger # 如果有錯誤，會在頁面上顯示是哪一行錯誤
	–host # 可以指定允許訪問的主機IP，0.0.0.0 為所有主機的意思
	–port # 自訂網路埠號的參數


# ▍選項2：
export FLASK_APP=app.py
python -m flask run

# ▍選項3 (不建議使用)：
# 在 main.py 的下方加入 if name == 'main':

if __name__ == '__main__':
    app.run()

# 在終端機輸入以下指令
python app.py
```

# 用法

```Python
```

# Flask + Nginx + Docker-compose 建議結構

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

# 在 Flask 中使用 Celery

[在 Flask 中使用 Celery](http://www.pythondoc.com/flask-celery/first.html)

# Flask 藍圖 Blueprint

[Flask进阶系列(六)–蓝图(Blueprint)](http://www.bjhee.com/flask-ad6.html)

# Flask MongoDB

[MongoEngine 文檔](https://docs.mongoengine.org/guide/querying.html)


# Flask POST GET

[Flask 傳入參數](https://ithelp.ithome.com.tw/m/articles/10263722)

# gunicorn(WSGI伺服器)

[gunicorn官方文檔](https://docs.gunicorn.org/en/stable/run.html)

[gunicorn指令](https://docs.gunicorn.org/en/latest/run.html#commonly-used-arguments)

[Flask with Gunicorn](https://sean22492249.medium.com/flask-with-gunicorn-9a37bca29227)

[Nginx+gunicorn+flask+docker演算法部署](https://www.796t.com/article.php?id=102596)


# Flask Reverse Proxy - Nginx(WSGI伺服器)

```
建立一個類別處理反向代理
```

[Flask Reverse Proxy](https://github.com/wilbertom/flask-reverse-proxy/blob/master/flask_reverse_proxy/__init__.py)

[python flask项目Nginx代理添加前缀](https://www.codenong.com/cs105254408/)

[python flask项目Nginx代理添加前缀](https://www.twblogs.net/a/5efde90dd496dddbb541e9d2)

[Create Proxy for Python Flask Application](https://stackoverflow.com/questions/30743696/create-proxy-for-python-flask-application)