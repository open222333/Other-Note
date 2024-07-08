# Python 模組 GoogleAPI Gmail(郵箱)

```
使用 Gmail API 本身是免費的，但有一些限制和配額。Google 提供了一定的免費使用限額，超過這些限額後，可能會產生費用。具體來說：

每日 API 配額：Google 為每個 API 提供免費的每日配額。例如，對於 Gmail API，每個用戶每天可以進行 1,000,000 次讀取操作。
配額使用：如果你的應用程式需要更多的 API 配額，可以向 Google 申請配額增加，這通常適用於大型應用程式。
Google Cloud 平台費用：除了 API 使用外，使用 Google Cloud 平台可能會產生其他費用，例如存儲數據或使用其他 Google 服務。
要查看具體的 Gmail API 配額和限制，你可以參考 Gmail API 配額文檔。

對於大多數個人項目或小型應用程式，免費配額通常已經足夠。如果你預期會超過這些配額，則需要考慮相關費用。

建立 Google Cloud 專案：
    前往 Google Cloud Console 並建立一個新專案。

啟用 Gmail API：
    在專案頁面，啟用 Gmail API。你可以在「API 與服務 > 庫」中搜尋「Gmail API」，然後啟用它。

建立憑證：
    在 Gmail API 頁面，點選「建立憑證」。
    在憑證類型中，選擇「OAuth 客戶端 ID」。
    在應用程式類型中，選擇「其他非桌面應用程式」。
    點選「建立」，並下載 JSON 檔案。這是你的憑證檔案，稍後在程式中會使用到。
```

## 目錄

- [Python 模組 GoogleAPI Gmail(郵箱)](#python-模組-googleapi-gmail郵箱)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學相關](#教學相關)
- [安裝](#安裝)
- [用法](#用法)
  - [範例2 - 進行身份驗證並讀取郵件](#範例2---進行身份驗證並讀取郵件)

## 參考資料

[Gmail API](https://developers.google.com/gmail/api/reference/rest)

[Choose Auth Scopes](https://developers.google.com/gmail/api/auth/scopes/)

[Gmail API python 文檔](https://developers.google.com/resources/api-libraries/documentation/gmail/v1/python/latest/index.html)

[Gmail API Docs](https://googleapis.github.io/google-api-python-client/docs/epy/index.html)

### 教學相關

[Gmail API python 範例](https://www.thepythoncode.com/article/use-gmail-api-in-python)

[官方Github](https://github.com/googleapis/google-api-python-client)

# 安裝

建立 Google Cloud 專案並啟用 Gmail API：

```
進入 Google Cloud Console
建立新專案
在 API & Services 中啟用 Gmail API
```

建立 OAuth 2.0 憑證：

```
在 Google Cloud Console 的「憑證」頁面中，建立 OAuth 2.0 客戶端 ID。
下載 client_secret.json 文件
```

安裝 google-auth, google-auth-oauthlib, 和 google-auth-httplib2 套件

安裝 google-api-python-client 套件

```bash
pip install google-auth google-auth-oauthlib google-auth-httplib2 google-api-python-client
```


# 用法

```Python
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build

SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']

def authenticate_gmail():
    flow = InstalledAppFlow.from_client_secrets_file('path/to/your/credentials.json', SCOPES)
    creds = flow.run_local_server(port=0)
    return creds

def main():
    creds = authenticate_gmail()
    service = build('gmail', 'v1', credentials=creds)

    # 現在你可以使用 service 來存取 Gmail API
    # 例如：取得收件匣中的郵件清單
    results = service.users().messages().list(userId='me').execute()
    messages = results.get('messages', [])

    if not messages:
        print('No messages found.')
    else:
        print('Messages:')
        for message in messages:
            print(f'- {message["id"]}')

if __name__ == '__main__':
    main()
```

## 範例2 - 進行身份驗證並讀取郵件

```
檢查是否已經有 token.json 憑證文件。
如果沒有，讓用戶通過 OAuth 2.0 認證。
使用 Gmail API 讀取用戶郵件並列出每封郵件的摘要。
```

```Python
import os.path
import google.auth.transport.requests
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build

# 如果修改這些範圍，刪除文件 token.json
SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']

def main():
    creds = None
    # 檢查 token.json 文件，這是用來存儲用戶會話憑證的
    if os.path.exists('token.json'):
        creds = Credentials.from_authorized_user_file('token.json', SCOPES)
    # 如果沒有有效憑證，讓用戶登錄
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(google.auth.transport.requests.Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'client_secret.json', SCOPES)
            creds = flow.run_local_server(port=0)
        # 儲存憑證以供未來使用
        with open('token.json', 'w') as token:
            token.write(creds.to_json())

    service = build('gmail', 'v1', credentials=creds)

    # 呼叫 Gmail API
    results = service.users().messages().list(userId='me').execute()
    messages = results.get('messages', [])

    if not messages:
        print('No messages found.')
    else:
        print('Messages:')
        for message in messages:
            msg = service.users().messages().get(userId='me', id=message['id']).execute()
            print(msg['snippet'])

if __name__ == '__main__':
    main()
```
