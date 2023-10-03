# Python 模組 GoogleAPI Gmail(郵箱)

```
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
- [指令](#指令)
- [用法](#用法)

## 參考資料

[Gmail API](https://developers.google.com/gmail/api/reference/rest)

[Choose Auth Scopes](https://developers.google.com/gmail/api/auth/scopes/)

[Gmail API python 文檔](https://developers.google.com/resources/api-libraries/documentation/gmail/v1/python/latest/index.html)

[Gmail API Docs](https://googleapis.github.io/google-api-python-client/docs/epy/index.html)

### 教學相關

[Gmail API python 範例](https://www.thepythoncode.com/article/use-gmail-api-in-python)

[官方Github](https://github.com/googleapis/google-api-python-client)

# 指令

```bash
# OAuth2.0 驗證 安裝
pip install --upgrade google-api-python-client google-auth-httplib2 google-auth-oauthlib
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
