# Python 模組 dotenv(環境變數)

## 參考資料

https://pypi.org/project/python-dotenv/

## 說明

```
將一些重要的資料存在環境變數(environment variable)中，是開發時常見的手段，不僅可以避免將重要的資料不小心 commit 進 codebase 之外，也可以利用環境變數儲存系統或程式設定，實務上也經常利用環境變數區隔開發環境(development)與生產環境(production)
但隨著需要設定的環境變數增多，可能導致每次進行開發都有一堆環境變數要先塞好，如果你有遇到這種情況，不妨試試 python-dotenv 吧！
```

## 操作 用法

```bash
# 安裝套件
pip install python-dotenv
```

```
.env檔
預設 python-dotenv 會載入 .env 檔案，然後將設定寫入環境變數之中，接著就能夠透過 os.getenv(key, default=None) 取得環境變數中的值。

.env 檔的格式大致如下，基本上等同 shell 中設定環境變數的用法：
    export DBHOST=localhost
    export DBPORT=5432

python-dotenv 中可以省略 export ，讓 .env 檔更簡潔：
    MODE=development
    DBHOST=localhost
    DBPORT=5432
指令：
load_dotenv() 載入 .env 檔
```