# Python 模組 python-telegram-bot(通訊軟體)

```
```

## 目錄

- [Python 模組 python-telegram-bot(通訊軟體)](#python-模組-python-telegram-bot通訊軟體)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [建立 Telegram 機器人](#建立-telegram-機器人)
- [指令](#指令)
- [用法](#用法)
	- [獲取 Telegram 聊天 ID](#獲取-telegram-聊天-id)

## 參考資料

[官方API文檔](https://core.telegram.org/bots/api)

[套件官方文檔](https://python-telegram-bot.readthedocs.io/en/stable/)

[python-telegram-bot pypi](https://pypi.org/project/python-telegram-bot/)

[python-telegram-bot](https://pypi.org/project/python-telegram-bot/)

[Python Telegram Bot 入門教學（一）](https://matters.news/@MeowMeow/python-telegram-bot-%E5%85%A5%E9%96%80%E6%95%99%E5%AD%B8-%E4%B8%80-bafyreiec3ydpasl5s336uiaoeqwmhuh7c7bjnmkxkcf4qnalxhbiz7pdre)

[【Telegram API】Python打造Telegram機器人手把手教學：最輕鬆最詳細的方法](https://pixnashpython.pixnet.net/blog/post/32391757-%E3%80%90telegram-api%E3%80%91python%E6%89%93%E9%80%A0telegrame%E6%A9%9F%E5%99%A8%E4%BA%BA%E6%89%8B%E6%8A%8A%E6%89%8B%E6%95%99)

# 建立 Telegram 機器人

```
建立 Telegram 機器人，請按照以下步驟進行：

在 Telegram 上搜尋 "BotFather"，這是一個 Telegram 官方提供的機器人，用於建立和管理其他機器人。

與 BotFather 對話，並遵從其指示，依次執行以下操作：

發送 /newbot 來創建一個新的機器人。
提供機器人的名稱（例如：MyTestBot）。
提供機器人的用戶名（例如：MyTestBot1234_bot）。請注意，機器人的用戶名必須以 "_bot" 結尾。
BotFather 將返回一條消息，包含你的機器人的 API 密鑰（例如：123456789:ABCdefGHIjklmnopQRSTuVWXyz）。
複製並保存 API 密鑰，它將用於在你的程式碼中驗證和操作機器人。
```


# 指令

```bash
# 安裝
pip install python-telegram-bot
```

# 用法

```Python
import telegram
from telegram.ext import Updater, CommandHandler

# 建立 Telegram 機器人：在 Telegram 上與 BotFather 對話，創建一個新的機器人並獲取 API 密鑰。
# 建立一個 Telegram 機器人的實例
bot = telegram.Bot(token='YOUR_API_TOKEN')

# 建立一個更新者的實例，並連結到 Telegram 伺服器
updater = Updater(token='YOUR_API_TOKEN', use_context=True)

# 建立一個指令處理器的實例
dispatcher = updater.dispatcher

# 建立指令處理函式：根據你的需求，建立相應的指令處理函式，處理接收到的指令。
# 建立 `/start` 指令的處理函式
def start(update, context):
    context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm your Telegram bot!")

# 將處理函式與指令綁定
start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

# 啟動機器人：啟動更新者，讓機器人開始監聽和回應使用者的指令。
updater.start_polling()
```

## 獲取 Telegram 聊天 ID

```Python
from telegram.ext import Updater, CommandHandler

# 建立一個指令處理函式
def get_chat_id(update, context):
    chat_id = update.effective_chat.id
    context.bot.send_message(chat_id=chat_id, text=f"Chat ID: {chat_id}")

# 建立一個更新者的實例，並連結到 Telegram 伺服器
updater = Updater(token='YOUR_API_TOKEN', use_context=True)

# 將處理函式與指令綁定
get_chat_id_handler = CommandHandler('get_chat_id', get_chat_id)
updater.dispatcher.add_handler(get_chat_id_handler)

# 啟動機器人
updater.start_polling()
```