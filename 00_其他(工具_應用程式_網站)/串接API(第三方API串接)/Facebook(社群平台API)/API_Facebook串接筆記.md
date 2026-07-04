# Facebook Graph API 串接筆記

```
社交平台 API：社團發文、粉專管理、用戶資訊
認證方式：Access Token（短效 / 長效 / Page Token）
API 版本：v18.0（建議固定版本，避免自動升級）
正式環境：https://graph.facebook.com/{version}/
費用：免費（需 Meta Developer App）
```

## 目錄

- [Facebook Graph API 串接筆記](#facebook-graph-api-串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [Access Token 說明](#access-token-說明)
    - [短效 User Token](#短效-user-token)
    - [長效 User Token](#長效-user-token)
    - [Page Access Token](#page-access-token)
  - [社團發文](#社團發文)
    - [前置條件](#前置條件)
    - [發文 API](#發文-api)
    - [附圖發文](#附圖發文)
  - [粉絲專頁發文](#粉絲專頁發文)
  - [權限列表](#權限列表)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

[Meta for Developers](https://developers.facebook.com/)

[Graph API 文件](https://developers.facebook.com/docs/graph-api/)

[Graph API Explorer](https://developers.facebook.com/tools/explorer/)

[Groups API 文件](https://developers.facebook.com/docs/groups-api/)

[Access Token 說明](https://developers.facebook.com/docs/facebook-login/access-tokens/)

---

## 申請流程

1. 前往 [developers.facebook.com](https://developers.facebook.com) → 建立 App
2. App 類型選 **Business**（若只需社團發文也可選 None）
3. 在 App 主控台加入所需產品：
   - **Groups API**（社團發文）
   - **Pages API**（粉專發文）
4. 設定 → 基本資料：記下 `App ID` 與 `App Secret`
5. 將你的 Facebook 帳號加入 App 為管理員
6. 若要發文至社團：在社團設定 → 應用程式 → 安裝你的 App

---

## Access Token 說明

### 短效 User Token

- 有效期：約 **1~2 小時**
- 取得方式：Graph API Explorer 直接產生，或 Facebook Login 流程
- 用途：開發測試用

### 長效 User Token

- 有效期：**60 天**
- 取得方式：用短效 Token 換取

```
GET https://graph.facebook.com/oauth/access_token
  ?grant_type=fb_exchange_token
  &client_id={APP_ID}
  &client_secret={APP_SECRET}
  &fb_exchange_token={短效USER_TOKEN}
```

回應：

```json
{
  "access_token": "EAAxxxxxx...",
  "token_type": "bearer",
  "expires_in": 5183944
}
```

> 長效 Token 到期前可重新換發，實際上接近「永久」。  
> 但若使用者更改密碼或撤銷授權，Token 立即失效。

### Page Access Token

- 有效期：**永不過期**（若由長效 User Token 換取）
- 取得方式：

```
GET https://graph.facebook.com/{PAGE_ID}?fields=access_token&access_token={長效USER_TOKEN}
```

---

## 社團發文

### 前置條件

| 項目 | 說明 |
|------|------|
| App 安裝到社團 | 社團設定 → 應用程式 → 加入你的 App |
| Token 權限 | `groups_manage_posts`、`publish_to_groups` |
| 帳號身份 | 發文者需為社團成員（管理員更佳） |
| 公開社團 | 需通過 Meta 應用程式審查（私人社團無此限制） |

### 發文 API

```
POST https://graph.facebook.com/v18.0/{GROUP_ID}/feed
```

**Request Body（JSON）：**

```json
{
  "message": "發文內容",
  "access_token": "USER_ACCESS_TOKEN"
}
```

**回應：**

```json
{
  "id": "123456789_987654321"
}
```

回傳的 `id` 格式為 `{group_id}_{post_id}`。

### 附圖發文

先上傳圖片取得 photo_id，再帶入發文：

```
POST https://graph.facebook.com/v18.0/{GROUP_ID}/photos
Content-Type: multipart/form-data

url={IMAGE_URL}&published=false&access_token={TOKEN}
```

回傳 `id` 後，發文時帶上：

```json
{
  "message": "發文內容",
  "attached_media": [{"media_fbid": "{photo_id}"}],
  "access_token": "USER_ACCESS_TOKEN"
}
```

---

## 粉絲專頁發文

```
POST https://graph.facebook.com/v18.0/{PAGE_ID}/feed
```

**Request Body：**

```json
{
  "message": "發文內容",
  "access_token": "PAGE_ACCESS_TOKEN"
}
```

> 粉絲專頁發文需使用 **Page Access Token**，不能用 User Token。

---

## 權限列表

| 權限 | 用途 | 需審查 |
|------|------|--------|
| `groups_manage_posts` | 社團發文 | 公開社團需要 |
| `publish_to_groups` | 代表用戶發文至社團 | 公開社團需要 |
| `pages_manage_posts` | 粉專發文 | 是 |
| `pages_read_engagement` | 讀取粉專互動資料 | 否 |
| `user_posts` | 讀取用戶貼文 | 否（限自己） |

---

## 錯誤處理

| 錯誤碼 | 說明 | 處理方式 |
|--------|------|----------|
| `190` | Token 無效或過期 | 重新換發長效 Token |
| `200` | 權限不足 | 確認 Token 包含所需權限 |
| `100` | 參數錯誤 | 確認 Group ID / Page ID 正確 |
| `368` | 帳號被暫時封鎖 | 等待解鎖，降低發文頻率 |
| `(#10)` | App 未安裝到社團 | 在社團設定中安裝 App |

---

## 範例程式碼

### Python — 發文至社團

```python
import requests

FB_API_VERSION = 'v18.0'
FB_ACCESS_TOKEN = 'your_user_access_token'
FB_GROUP_ID = 'your_group_id'

def post_to_group(message: str, group_id: str = FB_GROUP_ID) -> dict:
    url = f'https://graph.facebook.com/{FB_API_VERSION}/{group_id}/feed'
    payload = {
        'message': message,
        'access_token': FB_ACCESS_TOKEN,
    }
    res = requests.post(url, json=payload, timeout=15)
    data = res.json()
    if 'error' in data:
        return {'success': False, 'error': data['error'].get('message', '未知錯誤')}
    return {'success': True, 'post_id': data.get('id')}
```

### Python — 短效換長效 Token

```python
import requests

def exchange_long_lived_token(short_token: str, app_id: str, app_secret: str) -> str:
    url = 'https://graph.facebook.com/oauth/access_token'
    params = {
        'grant_type': 'fb_exchange_token',
        'client_id': app_id,
        'client_secret': app_secret,
        'fb_exchange_token': short_token,
    }
    res = requests.get(url, params=params, timeout=10)
    data = res.json()
    return data.get('access_token', '')
```

### Python — 取得 Page Access Token

```python
def get_page_token(page_id: str, user_long_token: str) -> str:
    url = f'https://graph.facebook.com/{page_id}'
    params = {
        'fields': 'access_token',
        'access_token': user_long_token,
    }
    res = requests.get(url, params=params, timeout=10)
    data = res.json()
    return data.get('access_token', '')
```

### config.ini 設定範例

```ini
[FACEBOOK]
FB_ACCESS_TOKEN = EAAxxxxxx...
FB_GROUP_ID     = 123456789
FB_API_VERSION  = v18.0
```
