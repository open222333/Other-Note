# Python 模組 celery(排程)

```
```

## 目錄

- [Python 模組 celery(排程)](#python-模組-celery排程)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [範例相關](#範例相關)
- [指令](#指令)
- [用法](#用法)
	- [與 docker-compose.yml](#與-docker-composeyml)
	- [celery flower docker-compose  mongodb 範例, command 設定在 docker-compose內](#celery-flower-docker-compose--mongodb-範例-command-設定在-docker-compose內)

## 參考資料

[celery pypi](https://pypi.org/project/celery/)

[celery 文檔](https://docs.celeryproject.org/en/stable/userguide/periodic-tasks.html)

[Celery 官網](http://www.celeryproject.org/)

[Celery 官方文件英文版](http://docs.celeryproject.org/en/latest/index.html)

[celery配置 文檔](https://docs.celeryproject.org/en/3.1/configuration.html)

[Celery 文件中文版](http://docs.jinkan.org/docs/celery/)

[Configuration and defaults - 參數 設定](https://docs.celeryq.dev/en/latest/userguide/configuration.html#configuration-and-defaults)

[Canvas: Designing Work-flows - 指令](https://docs.celeryq.dev/en/latest/userguide/canvas.html)

### 範例相關

[docker-django-celery-tutorial 基本教學](https://github.com/twtrubiks/docker-django-celery-tutorial)

[Celery 分散式工作佇列排程系統入門教學（一）](https://officeguide.cc/celery-distributed-task-queue-getting-started-1/)

[取的celery ID](https://micewww.pp.rl.ac.uk/projects/maus/wiki/MAUSCelery)

[Celery的基本使用](https://www.796t.com/article.php?id=57131)

[celery + flask](https://github.com/a607ernie/flask-celery-demo)

[如何使用 Celery multi worker 啟用自動縮放？](https://stackoverflow.com/questions/46989636/how-to-enable-auto-scaling-with-celery-multi-workers)

[多worker、多队列](https://www.cnblogs.com/yangjian319/p/9097171.html)

[Python celery原理及執行流程解析](https://www.796t.com/article.php?id=23198)

[使用 Celery Once 來防止 Celery 重複執行同一個任務](https://www.796t.com/article.php?id=177399)

[Celery_Tasks](https://hackmd.io/@shaoeChen/BJkMPVRuX?type=view)

[mysql - celery worker 之间如何实现类似锁文件的机制？](https://www.coder.work/article/4558945)

[python - 在Celery - Python中進程( worker )之間共享數據的最佳解決方案是什麼？](https://www.coder.work/article/546114)

[Celery autodiscover_tasks用途](https://stackoverflow.com/questions/53726215/what-is-the-purpose-of-celerys-autodiscover-tasks-function)

# 指令

```bash
# 安裝
pip install celery

# Flower 是一個用於監控 Celery 集群的實用工具，它提供了一個簡潔的 Web 界面，用於實時監控 Celery worker、任務、和群集的狀態。
pip install flower


# 若有其他app 會偵測 app1.tasks app2.tasks
celery -A celery_worker.celery worker --loglevel=info
celery -A [celery worker的檔案名稱].[celery名稱] worker --loglevel=info

# celery worker的檔案名稱 : 這邊使用celeryworker.py，所以是celeryworker
# celery 名稱 : 這個定義在app.__init__.py裡面的celery = Celery(__name__)
celery worker -A avnight.celery -l info -E -P gevent --purge --autoscale=3,1
celery beat -A avnight.celery -l info --pidfile=workers.html#inspecting-workers

# 啟動 Flower
flower -A your_celery_app

flower -A your_celery_app --address=0.0.0.0 --port=5555
```

# 用法

```Python
```

## 與 docker-compose.yml

```Python
version: '3'
services:
  redis:
    image: 'redis:latest'
    ports:
      - '6379:6379'

  web:
    build: .
    ports:
      - '5000:5000'
    depends_on:
      - redis

  celery-worker:
    build: .
    command: celery -A tasks worker --loglevel=info
    depends_on:
      - redis
    volumes:
      - .:/app
    environment:
      - CELERY_BROKER_URL=redis://redis:6379/0
      - FLASK_APP=app.py
      - FLASK_RUN_HOST=0.0.0.0
      - FLASK_RUN_PORT=5000
```

```Dockerfile
FROM python:3.8

WORKDIR /app

COPY requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

COPY . /app
```

```Python
from flask import Flask
from celery import Celery


app = Flask(__name__)

# 配置 Flask 應用
# https://docs.celeryq.dev/en/latest/userguide/configuration.html#configuration-and-defaults
app.config.update(
    CELERY_BROKER_URL='pyamqp://guest:guest@localhost//',
    CELERY_RESULT_BACKEND='rpc://',
    CELERY_TASK_SERIALIZER='json',
    CELERY_RESULT_SERIALIZER='json',
    CELERY_ACCEPT_CONTENT=['json'],
    CELERY_TRACK_STARTED=True,
    CELERY_DISABLE_RATE_LIMITS=True
)

# 創建 Celery 實例
celery = Celery('tasks')

# 將 Flask 應用的配置應用到 Celery 實例
celery.conf.update(app.config)

'''
celery.conf: 這是 Celery 實例的配置對象。
通過它，你可以設置和檢索與 Celery 相關的配置選項。

update(app.config): 這是一個字典更新操作，將 Flask 應用的配置字典應用到 Celery 實例的配置中。
這將 Flask 應用的配置覆蓋到 Celery 實例的配置中。
'''


@celery.task
def add(x, y):
    return x + y

@app.route('/')
def hello():
    result = add.delay(4, 4)
    return f'Hello, Celery! Task ID: {result.id}'
```

## celery flower docker-compose  mongodb 範例, command 設定在 docker-compose內

```yml
version: '3'
services:
  celery:
    image: your_celery_image
    command: celery -A your_celery_app worker --loglevel=info
    depends_on:
      - mongodb
  flower:
    image: mher/flower
    command: flower -A your_celery_app --broker=pyamqp://guest:guest@celery:5672
    ports:
      - "5555:5555"
    depends_on:
      - celery
  mongodb:
    image: "mongo:latest"
    ports:
      - "27017:27017"
```