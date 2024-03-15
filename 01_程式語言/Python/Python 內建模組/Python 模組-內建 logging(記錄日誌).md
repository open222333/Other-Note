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
    - [類別相關](#類別相關)
    - [教學範例](#教學範例)
  - [Log 的呈現 / 輸出方式](#log-的呈現--輸出方式)
- [用法](#用法)
  - [基本用法](#基本用法)
  - [基本用法 - 指定log檔案大小](#基本用法---指定log檔案大小)
  - [基本用法 - 指定時間](#基本用法---指定時間)
  - [使用自定義 log 級別](#使用自定義-log-級別)
  - [log 訊息分別以不同的格式輸出到不同的地方](#log-訊息分別以不同的格式輸出到不同的地方)
  - [同一個 application 中使用不同型態的 logger 的結果 dictConfig()用法](#同一個-application-中使用不同型態的-logger-的結果-dictconfig用法)
  - [使用配置文檔](#使用配置文檔)
  - [範例 - 自制 Log Class](#範例---自制-log-class)
  - [範例 - 沒有使用 Log Class 的設定](#範例---沒有使用-log-class-的設定)
- [Formatter 格式代號](#formatter-格式代號)

## 參考資料

[logging 官方文檔](https://docs.python.org/zh-tw/3/library/logging.html)

### 類別相關

[RotatingFileHandler](https://docs.python.org/zh-tw/3/library/logging.handlers.html#rotatingfilehandler)

[TimedRotatingFileHandler](https://docs.python.org/zh-tw/3/library/logging.handlers.html#timedrotatingfilehandler)

### 教學範例

[Python Log 從小白到入門](https://editor.leonh.space/2022/python-log/)

[Python Logging 套件最詳盡中文教學 | Python教學、Python入門](https://stylengineer.com/program/python-logging-guide/)

[logging：如何使用 logging 紀錄事件](https://orcahmlee.github.io/python/python-logging/)

[python log 檔限制大小問題](https://mark1002.github.io/2018/05/27/python-log-%E6%AA%94%E9%99%90%E5%88%B6%E5%A4%A7%E5%B0%8F%E5%95%8F%E9%A1%8C/)

[python通過TimedRotatingFileHandler按時間切割日誌](https://www.796t.com/content/1563349863.html)

[TimedRotatingFileHandler Changing File Name? - 解決不會刪除超過數量的問題](https://stackoverflow.com/questions/338450/timedrotatingfilehandler-changing-file-name)

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

## 基本用法

```Python
import logging

logger = logging.getLogger('sample')
log_handler = logging.FileHandler('sample.log')
log_formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
log_handler.setFormatter(log_formatter)
logger.addHandler(log_handler)
```

## 基本用法 - 指定log檔案大小

```Python
import logging
from logging.handlers import RotatingFileHandler

logger = logging.getLogger('sample')
# 使用 maxBytes 和 backupCount 值來允許文件以預定的大小
log_handler = RotatingFileHandler('sample.log', maxBytes=0, backupCount=0)
log_formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
log_handler.setFormatter(log_formatter)
logger.addHandler(log_handler)
```

## 基本用法 - 指定時間

```Python
import logging
from logging.handlers import TimedRotatingFileHandler

logger = logging.getLogger('sample')
# https://docs.python.org/zh-tw/3/library/logging.handlers.html#timedrotatingfilehandler
log_handler = TimedRotatingFileHandler('sample.log', when='D', interval=1, backupCount=1)
log_formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
log_handler.setFormatter(log_formatter)
logger.addHandler(log_handler)
```

```Python
from logging import handlers
import logging
import os


def _logging(**kwargs):

    level = kwargs.pop('level', None)
    filename = kwargs.pop('filename', None)
    datefmt = kwargs.pop('datefmt', None)
    format = kwargs.pop('format', None)

    if level is None:
        level = logging.DEBUG
    if filename is None:
        filename = 'default.log'
    if datefmt is None:
        datefmt = '%Y-%m-%d %H:%M:%S'
    if format is None:
        format = '%(asctime)s [%(module)s] %(levelname)s [%(lineno)d] %(message)s'

    log = logging.getLogger(filename)
    format_str = logging.Formatter(format, datefmt)

    def namer(filename):
        return filename.split('default.')[1]

    # cmd = logging.StreamHandler()
    # cmd.setFormatter(format_str)
    # cmd.setLevel(level)
    # log.addHandler(cmd)

    os.makedirs("./debug/logs", exist_ok=True)
    th_debug = handlers.TimedRotatingFileHandler(filename="./debug/" + filename, when='D', backupCount=3, encoding='utf-8')
    # th_debug.namer = namer
    th_debug.suffix = "%Y-%m-%d.log"
    th_debug.setFormatter(format_str)
    th_debug.setLevel(logging.DEBUG)
    log.addHandler(th_debug)

    th = handlers.TimedRotatingFileHandler(filename=filename, when='D', backupCount=3, encoding='utf-8')
    # th.namer = namer
    th.suffix = "%Y-%m-%d.log"
    th.setFormatter(format_str)
    th.setLevel(logging.INFO)
    log.addHandler(th)
    log.setLevel(level)
    return log


os.makedirs('./logs', exist_ok=True)
logger = _logging(filename='./logs/default')
```

## 使用自定義 log 級別

```
DEBUG（10）
INFO（20）
WARNING（30）
ERROR（40）
CRITICAL（50）
```

```Python
import logging

# 定義一個自訂的日誌級別
MY_LOG_LEVEL = 25
logging.addLevelName(MY_LOG_LEVEL, "MY_LOG")

# 建立日誌記錄器
logger = logging.getLogger("my_logger")
logger.setLevel(logging.DEBUG)

# 建立日誌處理器並設定格式
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
stream_handler = logging.StreamHandler()
stream_handler.setFormatter(formatter)
logger.addHandler(stream_handler)

# 使用自訂日誌等級記錄日誌
logger.log(MY_LOG_LEVEL, "This is a custom log message.")

# 使用內建日誌等級記錄日誌
logger.debug("This is a debug message.")
logger.info("This is an info message.")
logger.warning("This is a warning message.")
logger.error("This is an error message.")
logger.critical("This is a critical message.")
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

## 範例 - 自制 Log Class

```Python
from logging.handlers import TimedRotatingFileHandler, RotatingFileHandler
from datetime import datetime
from traceback import print_exc
from configparser import ConfigParser
import logging
import socket
import os


'''
設計 Log class

; ******log設定******
; 關閉log功能 輸入選項 (true, True, 1) 預設 不關閉
; LOG_DISABLE=1

; logs路徑 預設 logs
; LOG_PATH=

; 關閉紀錄log檔案 輸入選項 (true, True, 1)  預設 不關閉
; LOG_FILE_DISABLE=1

; 設定紀錄log等級 DEBUG,INFO,WARNING,ERROR,CRITICAL 預設WARNING
; LOG_LEVEL=

; 指定log大小(輸入數字) 單位byte, 與 LOG_DAYS 只能輸入一項 若都輸入 LOG_SIZE優先
; LOG_SIZE=

; 指定保留log天數(輸入數字) 預設7
; LOG_DAYS=
'''


class Log():

    def __init__(self, log_name: str = None) -> None:
        """
        Args:
            log_name (str, optional): logger名稱. Defaults to 主機名稱.
        """
        if log_name:
            self.log_name = log_name
        else:
            self.log_name = socket.gethostname()

        self.log_path = 'logs'
        self.logfile_name = None

        self.logger = logging.getLogger(self.log_name)
        self.logger.setLevel(logging.WARNING)

        self.formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

    def set_log_path(self, log_path: str):
        """設置log檔存放位置

        Args:
            log_path (str): 路徑 預設為 logs
        """
        self.log_path = log_path

    def set_log_file_name(self, name: str):
        """設置log檔名稱 預設為 主機名稱.log

        Args:
            name (str): 名稱
        """
        self.logfile_name = name

    def set_date_handler(self, amount: int = 3, when: str = 'D') -> TimedRotatingFileHandler:
        """設置每日log檔

        Args:
            `amount` (int, optional): 保留檔案數量. Defaults to 3.\n
            `when` (str, optional): S - Seconds, M - Minutes, H - Hours, D - Days., Defaults to 'D'.

        Returns:
            TimedRotatingFileHandler: _description_
        """
        if not os.path.exists(self.log_path):
            os.makedirs(self.log_path)

        def my_namer(default_name:str):
            """替換 TimedRotatingFileHandler namer

            [TimedRotatingFileHandler Changing File Name?](https://stackoverflow.com/questions/338450/timedrotatingfilehandler-changing-file-name)\n

            Args:
                default_name (str): _description_

            Returns:
                _type_: _description_
            """
            base_filename, ext, date = default_name.split(".")
            return f"{base_filename}.{date}.{ext}"

        # 當前
        if when == 'S':
            date_format = '%Y-%m-%d_%H-%M-%S'
        elif when == 'M':
            date_format = '%Y-%m-%d_%H-%M'
        elif when == 'H':
            date_format = '%Y-%m-%d_%H'
        elif when == 'D' or when == 'MIDNIGHT':
            date_format = '%Y-%m-%d'

        if not self.logfile_name:
            self.logfile_name = self.log_name

        self.now_time = datetime.now().__format__(date_format)
        self.log_file = os.path.join(self.log_path, f'{self.logfile_name}')
        handler = TimedRotatingFileHandler(self.log_file ,when=when, backupCount=amount)
        handler.namer = my_namer
        handler.setFormatter(self.formatter)
        self.logger.addHandler(handler)

    def set_file_handler(self, size: int = 1 * 1024 * 1024, file_amount: int = 1) -> RotatingFileHandler:
        """設置log檔案大小限制

        Args:
            log_file (_type_): log檔名
            size (int, optional): 檔案大小. Defaults to 1*1024*1024 (1M).
            file_amount (int, optional): 檔案數量. Defaults to 1.

        Returns:
            RotatingFileHandler: _description_
        """
        if not os.path.exists(self.log_path):
            os.makedirs(self.log_path)

        if not self.logfile_name:
            self.logfile_name = self.log_name

        self.log_file = os.path.join(self.log_path, f'{self.logfile_name}')
        handler = RotatingFileHandler(self.log_file, maxBytes=size, backupCount=file_amount)
        handler.setFormatter(self.formatter)
        self.logger.addHandler(handler)

    def set_msg_handler(self) -> logging.StreamHandler:
        """設置log steam

        Returns:
            logging.StreamHandler: _description_
        """
        handler = logging.StreamHandler()
        handler.setFormatter(self.formatter)
        self.logger.addHandler(handler)

    def set_log_formatter(self, formatter: str):
        """設置log格式 formatter

        %(asctime)s - %(name)s - %(levelname)s - %(message)s

        Args:
            formatter (str): log格式.
        """
        self.formatter = logging.Formatter(formatter)

    def set_level(self, level: str = 'WARNING'):
        """設置log等級

        Args:
            level (str): 設定紀錄log等級 DEBUG,INFO,WARNING,ERROR,CRITICAL 預設WARNING
        """
        if level == 'DEBUG':
            self.logger.setLevel(logging.DEBUG)
        elif level == 'INFO':
            self.logger.setLevel(logging.INFO)
        elif level == 'WARNING':
            self.logger.setLevel(logging.WARNING)
        elif level == 'ERROR':
            self.logger.setLevel(logging.ERROR)
        elif level == 'CRITICAL':
            self.logger.setLevel(logging.CRITICAL)

    def disable_log(self):
        """關閉log
        """
        logging.disable()

    def debug(self, message: str, exc_info: bool = False):
        self.logger.debug(message, exc_info=exc_info)

    def info(self, message: str, exc_info: bool = False):
        self.logger.info(message, exc_info=exc_info)

    def warning(self, message: str, exc_info: bool = False):
        self.logger.warning(message, exc_info=exc_info)

    def error(self, message: str, exc_info: bool = False):
        self.logger.error(message, exc_info=exc_info)

    def critical(self, message: str, exc_info: bool = False):
        self.logger.critical(message, exc_info=exc_info)

### 用法範例 ###
LOG_LEVEL = os.environ.get('LOG_LEVEL', None)
LOG_DAYS = os.environ.get('LOG_DAYS', None)
date = datetime.now().__format__("%Y%m%d")

logger = Log(__name__)
logger.set_log_file_name(f'{__name__}-{date}.log')
logger.set_date_handler()
logger.set_msg_handler()
if not LOG_LEVEL:
    logger.set_level(LOG_LEVEL)

### 設定範例 - 環境變數 ###
try:
    HOSTNAME = socket.gethostname()

    LOG_PATH = os.environ.get('LOG_PATH', 'logs')

    # 關閉log
    LOG_DISABLE = os.environ.get('LOG_DISABLE', False)
    if LOG_DISABLE == 'true' or LOG_DISABLE == 'True' or LOG_DISABLE == '1':
        LOG_DISABLE = True

    # 關閉記錄檔案
    LOG_FILE_DISABLE = os.environ.get('LOG_FILE_DISABLE', False)
    if LOG_FILE_DISABLE == 'true' or LOG_FILE_DISABLE == 'True' or LOG_FILE_DISABLE == '1':
        LOG_FILE_DISABLE = True

    # 設定紀錄log等級 預設WARNING, DEBUG,INFO,WARNING,ERROR,CRITICAL
    LOG_LEVEL = os.environ.get('LOG_LEVEL', 'WARNING')

    # 指定log大小(輸入數字) 單位byte
    LOG_SIZE = int(os.environ.get('LOG_SIZE', 0))
    # 指定保留log天數(輸入數字) 預設7
    LOG_DAYS = int(os.environ.get('LOG_DAYS', 7))

    log_setting = {
        'LOG_PATH': LOG_PATH,
        'LOG_DISABLE': LOG_DISABLE,
        'LOG_FILE_DISABLE': LOG_FILE_DISABLE,
        'LOG_LEVEL': LOG_LEVEL,
        'LOG_SIZE': LOG_SIZE,
        'LOG_DAYS': LOG_DAYS
    }
except Exception as err:
    print_exc()

print(log_setting)

# 建立log資料夾
if not os.path.exists(LOG_PATH) and not LOG_DISABLE:
    os.makedirs(LOG_PATH)

if LOG_DISABLE:
    logging.disable()

logger = Log(__name__)
if not LOG_FILE_DISABLE:
    logger.set_date_handler()
logger.set_msg_handler()
if LOG_LEVEL:
    logger.set_level(LOG_LEVEL)

err_logger = Log(f'{__name__}-error')
if not LOG_FILE_DISABLE:
    err_logger.set_date_handler()
err_logger.set_msg_handler()


### 設定範例 - 設定檔 ###
config = ConfigParser()
config.read('config.ini')

try:
    HOSTNAME = socket.gethostname()

    LOG_PATH = config.get('INFO', 'LOG_PATH', fallback='logs')

    # 關閉log
    LOG_DISABLE = config.getboolean('INFO', 'LOG_DISABLE', fallback=False)

    # 關閉記錄檔案
    LOG_FILE_DISABLE = config.getboolean('INFO', 'LOG_FILE_DISABLE', fallback=False)

    # 設定紀錄log等級 預設WARNING, DEBUG,INFO,WARNING,ERROR,CRITICAL
    LOG_LEVEL = config.get('INFO', 'LOG_LEVEL', fallback='WARNING')

    # 指定log大小(輸入數字) 單位byte
    LOG_SIZE = config.getint('INFO', 'LOG_SIZE', fallback=0)
    # 指定保留log天數(輸入數字) 預設7
    LOG_DAYS = config.getint('INFO', 'LOG_DAYS', fallback=7)

    log_setting = {
        'HOSTNAME': HOSTNAME,
        'LOG_PATH': LOG_PATH,
        'LOG_DISABLE': LOG_DISABLE,
        'LOG_FILE_DISABLE': LOG_FILE_DISABLE,
        'LOG_LEVEL': LOG_LEVEL,
        'LOG_SIZE': LOG_SIZE,
        'LOG_DAYS': LOG_DAYS
    }
except Exception as err:
    print_exc()

print(log_setting)

# 建立log資料夾
if not os.path.exists(LOG_PATH) and not LOG_DISABLE:
    os.makedirs(LOG_PATH)

if LOG_DISABLE:
    logging.disable()

logger = Log(__name__)
if not LOG_FILE_DISABLE:
    logger.set_date_handler()
logger.set_msg_handler()
if LOG_LEVEL:
    logger.set_level(LOG_LEVEL)

err_logger = Log(f'{__name__}-error')
if not LOG_FILE_DISABLE:
    err_logger.set_date_handler()
err_logger.set_msg_handler()
```

## 範例 - 沒有使用 Log Class 的設定

```Python
from logging.handlers import TimedRotatingFileHandler, RotatingFileHandler
from traceback import print_exc
from datetime import datetime
import logging
import socket
import os


"""沒有使用 Log Class 的設定範例

; ******log設定******
; 關閉log功能 輸入選項 (true, True, 1) 預設 不關閉
; LOG_DISABLE=1

; logs路徑 預設 logs
; LOG_PATH=

; 關閉紀錄log檔案 輸入選項 (true, True, 1)  預設 不關閉
; LOG_FILE_DISABLE=1

; 設定紀錄log等級 DEBUG,INFO,WARNING,ERROR,CRITICAL 預設WARNING
; LOG_LEVEL=

; 指定log大小(輸入數字) 單位byte, 與 LOG_DAYS 只能輸入一項 若都輸入 LOG_SIZE優先
; LOG_SIZE=

; 指定保留log天數(輸入數字) 預設7
; LOG_DAYS=
"""


try:
    HOSTNAME = socket.gethostname()

    LOG_PATH = os.environ.get('LOG_PATH', 'logs')

    # 關閉log
    LOG_DISABLE = os.environ.get('LOG_DISABLE', False)
    if LOG_DISABLE == 'true' or LOG_DISABLE == 'True' or LOG_DISABLE == '1':
        LOG_DISABLE = True

    # 關閉記錄檔案
    LOG_FILE_DISABLE = os.environ.get('LOG_FILE_DISABLE', False)
    if LOG_FILE_DISABLE == 'true' or LOG_FILE_DISABLE == 'True' or LOG_FILE_DISABLE == '1':
        LOG_FILE_DISABLE = True

    # 設定紀錄log等級 預設WARNING, DEBUG,INFO,WARNING,ERROR,CRITICAL
    LOG_LEVEL = os.environ.get('LOG_LEVEL', 'WARNING')

    # 指定log大小(輸入數字) 單位byte
    LOG_SIZE = int(os.environ.get('LOG_SIZE', 0))
    # 指定保留log天數(輸入數字) 預設7
    LOG_DAYS = int(os.environ.get('LOG_DAYS', 7))

    log_setting = {
        'LOG_PATH': LOG_PATH,
        'LOG_DISABLE': LOG_DISABLE,
        'LOG_FILE_DISABLE': LOG_FILE_DISABLE,
        'LOG_LEVEL': LOG_LEVEL,
        'LOG_SIZE': LOG_SIZE,
        'LOG_DAYS': LOG_DAYS
    }
except Exception as err:
    print_exc()

# 建立log資料夾
if not os.path.exists(LOG_PATH) and not LOG_DISABLE:
    os.makedirs(LOG_PATH)

if LOG_DISABLE:
    logging.disable()
else:
    log_formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

    if LOG_SIZE:
        log_file = f'{LOG_PATH}/{HOSTNAME}-mega.log'
        if not LOG_FILE_DISABLE:
            log_file_handler = RotatingFileHandler(f'logs/{log_file}.log', maxBytes=LOG_SIZE, backupCount=5)
            log_file_handler.setFormatter(log_formatter)
    else:
        log_file = f'{LOG_PATH}/{datetime.now().__format__("%Y%m%d")}-{HOSTNAME}.log'
        if not LOG_FILE_DISABLE:
            log_file_handler = TimedRotatingFileHandler(log_file, when='D', backupCount=LOG_DAYS)
            log_file_handler.setFormatter(log_formatter)

    log_msg_handler = logging.StreamHandler()
    log_msg_handler.setFormatter(log_formatter)

    logger = logging.getLogger(HOSTNAME)

    if LOG_LEVEL == 'DEBUG':
        logger.setLevel(logging.DEBUG)
    elif LOG_LEVEL == 'INFO':
        logger.setLevel(logging.INFO)
    elif LOG_LEVEL == 'WARNING':
        logger.setLevel(logging.WARNING)
    elif LOG_LEVEL == 'ERROR':
        logger.setLevel(logging.ERROR)
    elif LOG_LEVEL == 'CRITICAL':
        logger.setLevel(logging.CRITICAL)

    if not LOG_FILE_DISABLE:
        logger.addHandler(log_file_handler)
    logger.addHandler(log_msg_handler)

    logger.debug(log_setting)
```

# Formatter 格式代號

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
