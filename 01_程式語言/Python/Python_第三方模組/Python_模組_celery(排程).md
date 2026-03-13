# Python 模組 celery(排程)

```
```

## 目錄

- [Python 模組 celery(排程)](#python-模組-celery排程)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [flower相關](#flower相關)
    - [範例相關](#範例相關)
    - [筆記連結](#筆記連結)
- [安裝](#安裝)
- [指令](#指令)
- [用法](#用法)
  - [@app.task(bind=True) 用法](#apptaskbindtrue-用法)

## 參考資料

[celery pypi](https://pypi.org/project/celery/)

[celery 文檔](https://docs.celeryproject.org/en/stable/userguide/periodic-tasks.html)

[Celery 官網](http://www.celeryproject.org/)

[Celery 官方文件英文版](http://docs.celeryproject.org/en/latest/index.html)

[celery配置 文檔](https://docs.celeryproject.org/en/3.1/configuration.html)

[Celery 文件中文版](http://docs.jinkan.org/docs/celery/)

[Configuration and defaults - 參數 設定](https://docs.celeryq.dev/en/latest/userguide/configuration.html#configuration-and-defaults)

[Canvas: Designing Work-flows - 指令](https://docs.celeryq.dev/en/latest/userguide/canvas.html)

### flower相關

[Flower - 一個用於監控和管理Celery叢集的開源 Web 應用程式。它提供有關 Celery 工作人員和任務狀態的即時資訊。](https://flower.readthedocs.io/en/latest/)

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

### 筆記連結

[Python-Celery 練習](https://github.com/open222333/Python-Celery)

# 安裝

```bash
# 安裝
pip install celery

# Flower 是一個用於監控 Celery 集群的實用工具，它提供了一個簡潔的 Web 界面，用於實時監控 Celery worker、任務、和群集的狀態。
pip install flower

# Celery 的 Redis 支持
pip install 'celery[redis]'
```

# 指令

```bash
# 若有其他app 會偵測 app1.tasks app2.tasks
celery -A celery_worker.celery worker --loglevel=info
celery -A [celery worker的檔案名稱].[celery名稱] worker --loglevel=info

# celery worker的檔案名稱 : 這邊使用celeryworker.py，所以是celeryworker
# celery 名稱 : 這個定義在app.__init__.py裡面的celery = Celery(__name__)
celery worker -A avnight.celery -l info -E -P gevent --purge --autoscale=3,1
celery beat -A avnight.celery -l info --pidfile=workers.html#inspecting-workers

# 啟動 Flower
# 要運行 Flower，您需要提供代理 URL
celery --broker=amqp://guest:guest@localhost:5672// flower

# 使用celery應用程式的配置
# https://docs.celeryq.dev/en/stable/userguide/application.html
celery -A tasks.app flower

# 預設情況下，flower 運行在連接埠 5555，可以使用 port 選項進行修改
celery -A tasks.app flower --port=5001


docker run -v examples:/data -p 5555:5555 mher/flower celery --app=tasks.app flower
```

# 用法

```Python
# celery_config.py

from kombu import Exchange, Queue

CELERY_BROKER_URL = 'pyamqp://guest:guest@localhost//'
CELERY_RESULT_BACKEND = 'redis://localhost:6379/0'  # 使用 Redis 作為結果儲存

CELERY_TASK_SERIALIZER = 'json'
CELERY_RESULT_SERIALIZER = 'json'
CELERY_ACCEPT_CONTENT = ['json']

CELERY_DEFAULT_QUEUE = 'default'
CELERY_QUEUES = (
    Queue('default', Exchange('default'), routing_key='default'),
    Queue('queue1', Exchange('queue1'), routing_key='queue1'),
    Queue('queue2', Exchange('queue2'), routing_key='queue2'),
    # 其他佇列...
)

CELERY_ROUTES = {
    'your_task_for_queue1': {'queue': 'queue1'},
    'your_task_for_queue2': {'queue': 'queue2'},
    # 其他任務...
}

CELERYBEAT_SCHEDULE = {
    'task1': {
        'task': 'your_task_for_queue1',
        'schedule': crontab(minute=0, hour=0),  # 每天午夜執行
    },
    'task2': {
        'task': 'your_task_for_queue2',
        'schedule': crontab(minute=0, hour=1),  # 每天凌晨1點執行
    },
    # 其他計劃...
}
```

```Python
# celery_app/tasks.py

from celery import Celery

app = Celery('celery_app')
app.config_from_object('celery_config')

@app.task
def your_task_for_queue1():
    return "Task result for queue1"

@app.task
def your_task_for_queue2():
    return "Task result for queue2"

# 使用 result 參數來獲取任務的結果：
result = your_task_for_queue1.apply_async()
print(result.get())
```

## @app.task(bind=True) 用法

```Python
from celery import Celery

app = Celery('myapp', broker='pyamqp://guest@localhost//')

@app.task(bind=True)
def my_task(self, arg1, arg2):
    # 使用 self 可以訪問當前任務的上下文，例如 task ID、任務名稱等
    task_id = self.request.id
    task_name = self.name

    # 其他任務邏輯
    result = arg1 + arg2
    return result
```
