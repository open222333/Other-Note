# Python 模組 GoogleAPI Auth(驗證)

```
```

## 目錄

- [Python 模組 GoogleAPI Auth(驗證)](#python-模組-googleapi-auth驗證)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [使用 Google API 客戶端程式庫 驗證身分權限](#使用-google-api-客戶端程式庫-驗證身分權限)
  - [快速開始官方範例](#快速開始官方範例)
  - [google drive 下載範例](#google-drive-下載範例)

## 參考資料

[Google API Client Library for Python Docs](https://github.com/googleapis/google-api-python-client/blob/main/docs/README.md)

[OAuth 2.0](https://github.com/googleapis/google-api-python-client/blob/main/docs/oauth.md)

[Google API 的 OAuth 2.0 範圍](https://developers.google.com/identity/protocols/oauth2/scopes)

[Google API 快速開始 Python範例](https://developers.google.com/drive/api/quickstart/python#run_the_sample)

[Google API 快速開始 Python範例 Github](https://github.com/googleworkspace/python-samples/blob/main/drive/quickstart/quickstart.py)

[How to authenticate to any Google API](https://flaviocopes.com/google-api-authentication/)

[使用後端服務器進行身份驗證](https://developers.google.com/identity/sign-in/web/backend-auth)

[以服务帐号身份进行身份验证](https://cloud.google.com/docs/authentication/production#auth-cloud-implicit-python)

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

## 快速開始官方範例

```Python
from __future__ import print_function

import os.path

from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError

# If modifying these scopes, delete the file token.json.
SCOPES = ['https://www.googleapis.com/auth/drive.metadata.readonly']


def main():
    """Shows basic usage of the Drive v3 API.
    Prints the names and ids of the first 10 files the user has access to.
    """
    creds = None
    # The file token.json stores the user's access and refresh tokens, and is
    # created automatically when the authorization flow completes for the first
    # time.
    if os.path.exists('token.json'):
        creds = Credentials.from_authorized_user_file('token.json', SCOPES)
    # If there are no (valid) credentials available, let the user log in.
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)
        # Save the credentials for the next run
        with open('token.json', 'w') as token:
            token.write(creds.to_json())

    try:
        service = build('drive', 'v3', credentials=creds)

        # Call the Drive v3 API
        results = service.files().list(
            pageSize=10, fields="nextPageToken, files(id, name)").execute()
        items = results.get('files', [])

        if not items:
            print('No files found.')
            return
        print('Files:')
        for item in items:
            print(u'{0} ({1})'.format(item['name'], item['id']))
    except HttpError as error:
        # TODO(developer) - Handle errors from drive API.
        print(f'An error occurred: {error}')


if __name__ == '__main__':
    main()
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
