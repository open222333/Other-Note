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

## 參考資料

[celery pypi](https://pypi.org/project/celery/)

[celery 文檔](https://docs.celeryproject.org/en/stable/userguide/periodic-tasks.html)

[Celery 官網](http://www.celeryproject.org/)

[Celery 官方文件英文版](http://docs.celeryproject.org/en/latest/index.html)

[celery配置 文檔](https://docs.celeryproject.org/en/3.1/configuration.html)

[Celery 文件中文版](http://docs.jinkan.org/docs/celery/)

### 範例相關

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
pip install sample

# 若有其他app 會偵測 app1.tasks app2.tasks
celery -A celery_worker.celery worker --loglevel=info
celery -A [celery worker的檔案名稱].[celery名稱] worker --loglevel=info

celery worker的檔案名稱 : 這邊使用celeryworker.py，所以是celeryworker
celery 名稱 : 這個定義在app.__init__.py裡面的celery = Celery(__name__)


celery worker -A avnight.celery -l info -E -P gevent --purge --autoscale=3,1

celery beat -A avnight.celery -l info --pidfile=

workers.html#inspecting-workers
```

# 用法

```Python
```