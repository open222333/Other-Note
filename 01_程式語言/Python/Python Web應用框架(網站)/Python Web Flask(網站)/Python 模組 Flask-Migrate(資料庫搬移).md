# Python 模組 Flask-Migrate(資料庫搬移)

```
```

## 目錄

- [Python 模組 Flask-Migrate(資料庫搬移)](#python-模組-flask-migrate資料庫搬移)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [範例相關](#範例相關)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[Flask-Migrate pypi](https://pypi.org/project/Flask-Migrate/)

[Flask-Migrate - 官方文件](https://flask-migrate.readthedocs.io/en/latest/)

### 範例相關

[DAY26-搞懂Flask-Migrate](https://ithelp.ithome.com.tw/articles/10205751)

[Flask實作_ext_06_Flask-Migrate](https://hackmd.io/@shaoeChen/HJiZtEngG/https%3A%2F%2Fhackmd.io%2Fs%2Fr1luGOkVf)

# 指令

```bash
# 安裝
pip install Flask-Migrate
```

# 用法

```Python
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db'

db = SQLAlchemy(app)
migrate = Migrate(app, db)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(128))
```
