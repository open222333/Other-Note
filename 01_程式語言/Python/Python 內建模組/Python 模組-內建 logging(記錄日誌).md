# Python 模組-內建 logging(記錄日誌)

```
協助除錯，而在正式環境也會留下一些必要的 log 以便日後追蹤
例如記下用戶登入的時間／地點／裝置等資訊，讓我們可以利用這些 log 來檢測出不尋常的登入行為。

開發人員可以為每一個「事件」貼上重要程度等級的標籤 ，稱作程度 (Level) 或是嚴重性 (Critical)

在專案內導入 log 機制，還需要考慮這幾點：

專案內的其它套件是否也有定義自己的 log？
要如何讓自己的 log 與其他套件的 log 和平共存而不互相干擾？
或者是如何依自己的需求魔改其他套件的 log 行為？

⬤  Logging 套件用於「追縱事件」，目的是想知道程式運作的過程中發生了那些事件?
⬤ 「 事件」會被一段訊息所描述，稱作「log」。
⬤「log」有兩種呈現方式：第一種是輸出到螢幕控制台；第二種是寫入日誌檔案 (.log檔)。
```

## 目錄

- [Python 模組-內建 logging(記錄日誌)](#python-模組-內建-logging記錄日誌)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [教學範例](#教學範例)
	- [Log 的呈現 / 輸出方式](#log-的呈現--輸出方式)
- [用法](#用法)
	- [log 訊息分別以不同的格式輸出到不同的地方](#log-訊息分別以不同的格式輸出到不同的地方)
	- [同一個 application 中使用不同型態的 logger 的結果 dictConfig()用法](#同一個-application-中使用不同型態的-logger-的結果-dictconfig用法)
	- [使用配置文檔](#使用配置文檔)
	- [Formatter 格式代號](#formatter-格式代號)

## 參考資料

[logging 官方文檔](https://docs.python.org/zh-tw/3/library/logging.html)

### 教學範例

[Python Log 從小白到入門](https://editor.leonh.space/2022/python-log/)

[Python Logging 套件最詳盡中文教學 | Python教學、Python入門](https://stylengineer.com/program/python-logging-guide/)

[logging：如何使用 logging 紀錄事件](https://orcahmlee.github.io/python/python-logging/)

[python log 檔限制大小問題](https://mark1002.github.io/2018/05/27/python-log-%E6%AA%94%E9%99%90%E5%88%B6%E5%A4%A7%E5%B0%8F%E5%95%8F%E9%A1%8C/)

## Log 的呈現 / 輸出方式

事件會被記錄成 log，而 log 有兩種呈現 / 輸出方式：

1. 輸出到螢幕控制台 (Console)：最簡單且直接的方式，缺點是控制台視窗一旦關掉就再也看不到，由 stream handler 負責。
2. 寫入硬碟上的 log 日誌文件：記錄成日誌檔案 (俗稱Log檔案，一般會以.log 副檔名命名此文件) ，由 file handler 負責。

# 用法

```Python
import logging

# create logger
# 建立 logger，每個 logger 都可以有自己的組態，例如儲存到不同的檔案，或者不同的輸出的格式等等
# 雖然 root logger 也是可以被做設定的，但建議另外建立自己的 logger，而不要去更動 root logger 的行為，避免無意中改掉專案內依賴套件的 logger 行為。
logger = logging.getLogger('simple_example')
logger.setLevel(logging.DEBUG)

# create console handler and set level to debug
ch = logging.StreamHandler()
# 設定 logger 輸出級別的函式
ch.setLevel(logging.DEBUG)

# create formatter
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

# add formatter to ch
ch.setFormatter(formatter)

# add ch to logger
logger.addHandler(ch)

# 'application' code
logger.debug('debug message')
logger.info('info message')
logger.warning('warn message')
logger.error('error message')
logger.critical('critical message')
```

## log 訊息分別以不同的格式輸出到不同的地方

```Python
import logging
import logging.handlers


# 1. Create handlers
console_handler = logging.StreamHandler()
file_handler = logging.FileHandler('handler.log')
time_rotating_handler = logging.handlers.TimedRotatingFileHandler('time-rotating-handler.log')

# 2. Create formatters
console_format = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
file_format = logging.Formatter('%(levelname)s - %(message)s')
time_rotating_format = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

# 3. Set the format for handlers
console_handler.setFormatter(console_format)
file_handler.setFormatter(file_format)
time_rotating_handler.setFormatter(time_rotating_format)

# 4. Set the log level for handlers
console_handler.setLevel(logging.INFO)
file_handler.setLevel(logging.INFO)
time_rotating_handler.setLevel(logging.INFO)

# 5. Get a logger
logger = logging.getLogger(__name__)

# 6. Add the handlers to the logger
logger.addHandler(console_handler)
logger.addHandler(file_handler)
logger.addHandler(time_rotating_handler)

if __name__ == "__main__":
    logger.info('info')
    logger.warning('warning')
```

## 同一個 application 中使用不同型態的 logger 的結果 dictConfig()用法

```Python
import logging
import logging.config


LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'normal': {  # the name of formatter
            'format': '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
        },
        'simple': {  # the name of formatter
            'format': '%(levelname)s - %(message)s'
        },
    },
    'handlers': {
        'console1': {  # the name of handler
            'class': 'logging.StreamHandler',  # emit to sys.stderr(default)
            'formatter': 'normal',  # use the above "normal" formatter
        },
        'console2': {  # the name of handler
            'class': 'logging.StreamHandler',  # emit to sys.stderr(default)
            'formatter': 'simple',  # use the above "simple" formatter
        },
        'file1': {  # the name of handler
            'class': 'logging.FileHandler',  # emit to disk file
            'filename': 'f1.log',  # the path of the log file
            'formatter': 'normal',  # use the above "normal" formatter
        },
        'file2': {  # the name of handler
            'class': 'logging.FileHandler',  # emit to disk file
            'filename': 'f2.log',  # the path of the log file
            'formatter': 'simple',  # use the above "simple" formatter
        },
        'time-rotating-file': {  # the name of handler
            'class': 'logging.handlers.TimedRotatingFileHandler',  # the log rotation by time interval
            'filename': 'f3.log',  # the path of the log file
            'when': 'midnight',  # time interval
            'formatter': 'normal',  # use the above "simple" formatter
        },
    },
    'loggers': {
        'c': {  # the name of logger
            'handlers': ['console1', 'console2'],  # use the above "console1" and "console2" handler
            'level': 'INFO',  # logging level
        },
        'f1': {  # the name of logger
            'handlers': ['file1'],  # use the above "file1" handler
            'level': 'INFO',  # logging level
            'propagate': True,
        },
        'f2': {  # the name of logger
            'handlers': ['file2'],  # use the above "file2" handler
            'level': 'INFO',  # logging level
            'propagate': True,
        },
        'time-f': {  # the name of logger
            'handlers': ['time-rotating-file'],  # use the above "time-rotating-file" handler
            'level': 'INFO',  # logging level
            'propagate': True,
        },
    },
}

logging.config.dictConfig(config=LOGGING)

if __name__ == "__main__":
    logger_c = logging.getLogger('c')
    logger_c.info('info')

    logger_f1 = logging.getLogger('f1')
    logger_f1.info('info')

    logger_f2 = logging.getLogger('f2')
    logger_f2.info('info')

    logger_time_f = logging.getLogger('time-f')
    logger_time_f.info('info')
```

## 使用配置文檔

```Python
import logging
import logging.config

logging.config.fileConfig('logging.conf')

# create logger
logger = logging.getLogger('simpleExample')
```

```conf
[loggers]
keys=root,simpleExample

[handlers]
keys=consoleHandler

[formatters]
keys=simpleFormatter

[logger_root]
level=DEBUG
handlers=consoleHandler

[logger_simpleExample]
level=DEBUG
handlers=consoleHandler
qualname=simpleExample
propagate=0

[handler_consoleHandler]
class=StreamHandler
level=DEBUG
formatter=simpleFormatter
args=(sys.stdout,)

[formatter_simpleFormatter]
format=%(asctime)s - %(name)s - %(levelname)s - %(message)s
```

## Formatter 格式代號

```
建立 log 時間
%(asctime)s

建立 log 的 Unix 時間
%(created)f

發出 log 的程式檔名
%(filename)s

發出 log 的函式名
%(funcName)s

log 的級別
%(levelname)s

log 的級別數字
%(levelno)s

發出 log 的行號
%(lineno)d

log 的訊息內容
%(message)s

發出 log 的模組名稱
%(module)s

建立 log 時間的毫秒部份
%(msecs)d

發出 log 的 logger 名稱
%(name)s

發出 log 的程式的路徑
%(pathname)s

發出 log 的程序的 ID
%(process)d

發出 log 的程序名稱
%(processName)s

log 從創建到發出的毫秒時間差
%(relativeCreated)d

發出 log 的線程 ID
%(thread)d

發出 log 的線程名稱
%(threadName)s
```
