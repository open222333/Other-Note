# Python 模組 Flask-MongoEngine(mongoDB資料庫)

```
```

## 目錄

- [Python 模組 Flask-MongoEngine(mongoDB資料庫)](#python-模組-flask-mongoenginemongodb資料庫)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [查詢相關](#查詢相關)
- [指令](#指令)
- [用法](#用法)
	- [配置](#配置)
	- [映射文檔](#映射文檔)
	- [創建數據](#創建數據)
	- [查詢](#查詢)

## 參考資料

[flask-mongoengine pypi](https://pypi.org/project/flask-mongoengine/)

[mongoengine 官方網站](http://mongoengine.org/)

[mongoengine 文檔](http://docs.mongoengine.org/)

[Flask-MongoEngine 文檔](http://docs.mongoengine.org/projects/flask-mongoengine/en/latest/)

[通過 MongoEngine 使用 MongoDB](https://dormousehole.readthedocs.io/en/latest/patterns/mongoengine.html)

### 查詢相關

[MongoEngine - Query Operators 查詢運算子](https://www.tutorialspoint.com/mongoengine/mongoengine_query_operators.htm)

# 指令

```bash
# 安裝
pip install flask-mongoengine
```

# 用法

## 配置

```Python
# apps.py
from flask import Flask
from flask_mongoengine import MongoEngine

app = Flask(__name__)
app.config['MONGODB_SETTINGS'] = {
    "db": "myapp",
}
db = MongoEngine(app)
```

## 映射文檔

```Python
from apps import mongo

class Movie(mongo.Document):
    title = mongo.StringField(required=True)
    year = mongo.IntField()
    rated = mongo.StringField()
    director = mongo.StringField()
    actors = mongo.ListField()
```

## 創建數據

```Python
bttf = Movie(title="Back To The Future", year=1985)
bttf.actors = [
    "Michael J. Fox",
    "Christopher Lloyd"
]
bttf.imdb = Imdb(imdb_id="tt0088763", rating=8.5)
bttf.save()
```

## 查詢

```Python
# 使用類的 objects 屬性來執行查詢。關鍵字參數用於字段的等值查詢
bttf = Movies.objects(title="Back To The Future").get_or_404()


# 字段名稱後加雙下劃線可以連接查詢運算子。
# objects 及其返回的查詢是可迭代的
some_theron_movie = Movie.objects(actors__in=["Charlize Theron"]).first()

for recents in Movie.objects(year__gte=2017):
    print(recents.title)
```
