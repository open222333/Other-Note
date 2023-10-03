# Python 模組 GoogleAPI Drive(雲端硬碟)

```
```

## 目錄

- [Python 模組 GoogleAPI Drive(雲端硬碟)](#python-模組-googleapi-drive雲端硬碟)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [使用 Google API 客戶端程式庫 驗證身分權限](#使用-google-api-客戶端程式庫-驗證身分權限)
  - [google drive 下載範例](#google-drive-下載範例)

## 參考資料

# 指令

```bash
# 安裝
pip install
```

# 用法

## 使用 Google API 客戶端程式庫 驗證身分權限

```Python
from google.oauth2 import id_token
from google.auth.transport import requests

# (Receive token by HTTPS POST)
# ...

try:
    # Specify the CLIENT_ID of the app that accesses the backend:
    idinfo = id_token.verify_oauth2_token(token, requests.Request(), CLIENT_ID)

    # Or, if multiple clients access the backend server:
    # idinfo = id_token.verify_oauth2_token(token, requests.Request())
    # if idinfo['aud'] not in [CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]:
    #     raise ValueError('Could not verify audience.')

    # If auth request is from a G Suite domain:
    # if idinfo['hd'] != GSUITE_DOMAIN_NAME:
    #     raise ValueError('Wrong hosted domain.')

    # ID token is valid. Get the user's Google Account ID from the decoded token.
    userid = idinfo['sub']
except ValueError:
    # Invalid token
    pass
```

## google drive 下載範例

```Python
class GoogleDrvieApi():
    # google drive api settings
    def __init__(self):
        SCOPES = 'https://www.googleapis.com/auth/drive'
        CLIENT_SECRET_FILE = 'client_secret.json'
        APPLICATION_NAME = 'Drive API Python Quickstart'
        self.authInst = google_api_auth.auth(
            SCOPES, CLIENT_SECRET_FILE, APPLICATION_NAME)
        self.credentials = self.authInst.getCredentials()
        self.http = self.credentials.authorize(httplib2.Http())
        self.drive_service = discovery.build(
            'drive', 'v3', http=self.http, cache_discovery=False)

    # use google drive data_id for download
    def downloadSingleFile(self, file_id, filepath):
        request = self.drive_service.files().get_media(fileId=file_id)
        fh = io.FileIO(filepath, mode='wb')
        downloader = MediaIoBaseDownload(fh, request, chunksize=500*1024*1024)
        done = False
        while done is False:
            try:
                status, done = downloader.next_chunk()
                print("Download %d%%." % int(status.progress() * 100))
            except:
                os.remove(filepath)
```
