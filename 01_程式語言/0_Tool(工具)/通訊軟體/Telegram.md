# Telegram

```
```

## 目錄

- [Telegram](#telegram)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [建立 Telegram 機器人](#建立-telegram-機器人)
- [Python](#python)

## 參考資料

[官方API文檔](https://core.telegram.org/bots/api)

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

# Python

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