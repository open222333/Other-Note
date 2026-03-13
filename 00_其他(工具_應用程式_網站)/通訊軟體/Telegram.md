# Telegram

```
```

## 目錄

- [Telegram](#telegram)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
  - [Bot Command](#bot-command)
- [範例](#範例)
  - [建立 Telegram 機器人](#建立-telegram-機器人)
  - [Python](#python)
    - [不使用 Python 的 Python-telegram-bot 函式庫 提取 Chat ID](#不使用-python-的-python-telegram-bot-函式庫-提取-chat-id)
    - [使用 Python 的 Python-telegram-bot 函式庫 提取 Chat ID](#使用-python-的-python-telegram-bot-函式庫-提取-chat-id)
    - [發送訊息到tg](#發送訊息到tg)

## 參考資料

[官方API文檔 Telegram Bot API](https://core.telegram.org/bots/api)

[命令 Commands](https://core.telegram.org/bots/features#commands)

# 指令

## Bot Command

```
/start： 啟動機器人，有時會觸發機器人發送歡迎消息或提供使用說明。
/help： 獲取機器人的幫助信息。有些機器人會回覆可用指令的列表或提供使用說明。
/settings： 設定機器人的一些參數或選項，具體功能視機器人而定。
/stop 或 /cancel： 停止機器人的某個操作或交互，或取消當前操作。
/commands 或 /cmdlist： 顯示機器人可用的指令列表。
/mybots： 現有機器人列表
```

# 範例

## 建立 Telegram 機器人

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

## Python

### 不使用 Python 的 Python-telegram-bot 函式庫 提取 Chat ID

```Python
import requests

def get_chat_id(api_token):
    # 發送 getUpdates 請求獲取最新的更新
    response = requests.get(f"https://api.telegram.org/bot{api_token}/getUpdates")

    # 解析 API 响應，提取聊天 ID
    if response.status_code == 200:
        data = response.json()
        if "result" in data and data["result"]:
            chat_id = data["result"][0]["message"]["chat"]["id"]
            return chat_id
    return None

# 設置您的 Telegram Bot API Token
api_token = "YOUR_API_TOKEN"

# 獲取聊天 ID
chat_id = get_chat_id(api_token)
if chat_id:
    print(f"聊天 ID: {chat_id}")
else:
    print("獲取聊天 ID 失敗。")
```

### 使用 Python 的 Python-telegram-bot 函式庫 提取 Chat ID

```Python
from telegram import Update
from telegram.ext import Updater, MessageHandler, Filters, CallbackContext

# 定義處理消息的函數
def handle_messages(update: Update, context: CallbackContext) -> None:
    chat_id = update.message.chat_id
    print(f"Received a message in chat {chat_id}: {update.message.text}")

def main() -> None:
    # 初始化機器人，使用你的 API 令牌
    updater = Updater("YOUR_API_TOKEN")

    # 綁定處理器到消息事件
    updater.dispatcher.add_handler(MessageHandler(Filters.text & ~Filters.command, handle_messages))

    # 開始機器人
    updater.start_polling()

    # 使機器人一直運行，直到按 Ctrl+C 結束
    updater.idle()

if __name__ == '__main__':
    main()
```

### 發送訊息到tg

```Python
def telegram_bot_send_message(self, message):
        '''發送訊息到tg'''
        bot_token = ''
        chat_id = ''
        api_url = f'https://api.telegram.org/bot{bot_token}/sendMessage?chat_id={chat_id}&text={message}'
        requests.get(api_url)
```
