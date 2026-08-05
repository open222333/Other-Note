# Telegram

```
即時通訊軟體，提供 Bot API 供程式化發送訊息與互動
適用場景：系統告警通知、排程回報、聊天機器人
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
- [例外狀況](#例外狀況)
  - [Bot Token 外洩處理流程（Incident Response）](#bot-token-外洩處理流程incident-response)
    - [第一階段：立即處理](#第一階段立即處理)
    - [第二階段：確認是否遭到利用](#第二階段確認是否遭到利用)
    - [第三階段：確認恢復正常](#第三階段確認恢復正常)
    - [第四階段：後續改善](#第四階段後續改善)
    - [事件記錄表](#事件記錄表)

## 參考資料

[官方API文檔 Telegram Bot API](https://core.telegram.org/bots/api)

[命令 Commands](https://core.telegram.org/bots/features#commands)

[Web API 資安漏洞與修復實作(資安)](../../00_概念/資安/Web_API_資安漏洞與修復實作(資安).md)

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

> Token 一律以環境變數注入（`$TELEGRAM_BOT_TOKEN`），不得寫入程式碼、README 或版控。

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

# 例外狀況

## Bot Token 外洩處理流程（Incident Response）

**適用情境**：Bot Token 疑似或確認外洩（誤 commit 進版控、貼上公開平台、伺服器遭入侵等）。

> Token 只要曾出現在公開場所（含 Git 歷史紀錄），即視為**永久外洩**，無論後續是否刪除，一律 Revoke。

### 第一階段：立即處理

**1. 重設（Revoke）Bot Token**

使用 BotFather：

```
/mybots
→ 選擇 Bot
→ API Token
→ Revoke current token
```

- [ ] 已取得新 Token
- [ ] 已安全保存新 Token（Secret Manager / 環境變數）

> 舊 Token 於 Revoke 後立即失效，任何人都無法再使用。

**2. 更新所有使用該 Token 的服務**

- [ ] Production Server
- [ ] Staging
- [ ] Docker
- [ ] Kubernetes Secret
- [ ] CI/CD Secret
- [ ] GitHub Actions Secret
- [ ] Railway
- [ ] Render
- [ ] Vercel
- [ ] Cloud Run
- [ ] 其他：

完成後重新部署服務。

### 第二階段：確認是否遭到利用

**1. 檢查 Webhook**

```
GET https://api.telegram.org/bot<NEW_TOKEN>/getWebhookInfo
```

- [ ] URL 是自己的網址
- [ ] 無陌生網址
- [ ] `pending_update_count` 正常

若被改成陌生網址：先 `deleteWebhook`，再重新 `setWebhook`。

**2. 檢查 Bot 設定是否遭修改**

- [ ] Bot Name
- [ ] Username
- [ ] Description
- [ ] About
- [ ] Commands
- [ ] Profile Photo

若異常，使用 BotFather 改回。

**3. 檢查是否發送異常訊息**

檢查來源：

- [ ] Bot Logs
- [ ] Server Logs
- [ ] 群組聊天紀錄
- [ ] 使用者回報

異常樣態：垃圾訊息、詐騙連結、未授權通知、大量廣播。

**4. 檢查 Token 曾公開於何處**

- [ ] GitHub Repository（**含 commit 歷史，非僅目前檔案**）
- [ ] GitLab
- [ ] Gist
- [ ] Pastebin
- [ ] 文件 / README
- [ ] Log
- [ ] Discord / Slack / Email

Git 歷史掃描：

```bash
# 目前工作目錄
grep -rnE '[0-9]{8,10}:[A-Za-z0-9_-]{35}' --include='*.md' --include='*.py' .

# 歷史提交（找出曾經 commit 過的 Token）
git log --all --oneline --pickaxe-regex -S'[0-9]{8,10}:[A-Za-z0-9_-]{35}'

# 檢視命中的 commit 實際內容
git show <commit> --unified=0 | grep -E '^[+-].*[0-9]{8,10}:[A-Za-z0-9_-]{35}'
```

若曾公開 → 視為永久外洩，必須 Revoke；歷史清除（`git filter-repo` / BFG）屬善後，不能取代 Revoke。

**5. 檢查伺服器是否遭入侵**

- [ ] SSH Login 紀錄
- [ ] 新增使用者
- [ ] SSH Key
- [ ] Cron Job
- [ ] Background Process
- [ ] Docker Container
- [ ] Access Log

若伺服器遭入侵，需重新建立所有憑證與 Token，不只 Bot Token。

### 第三階段：確認恢復正常

- [ ] Bot 可正常回覆
- [ ] Webhook 正常
- [ ] Long Polling 正常（若使用）
- [ ] 無異常訊息
- [ ] 無陌生請求
- [ ] 新 Token 已部署完成

### 第四階段：後續改善

**Token 管理**

- [ ] Token 放入 Secret Manager
- [ ] 不寫入 Git
- [ ] 不寫入 README
- [ ] 不放在程式碼
- [ ] 使用環境變數

**Git 安全**

`.gitignore` 加入：

```
.env
.env.*
config.local
```

**定期檢查**

- 每月檢查一次 Webhook
- 定期檢查 BotFather 設定
- 定期輪替 Token（高風險服務）

### 事件記錄表

事件記錄不入本筆記庫，複製下列欄位另存至事件管理系統或工單：

```
發現時間：
Bot 名稱：
Bot Username：
建立者帳號：
外洩原因：
處理人員：

事件原因：
影響範圍：
改善措施：
結案日期：
```
