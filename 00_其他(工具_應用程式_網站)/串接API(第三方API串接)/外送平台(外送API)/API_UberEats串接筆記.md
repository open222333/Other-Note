# Uber Eats Marketplace API 串接筆記

```
Uber Eats Marketplace API（POS Integration）
認證方式：OAuth 2.0 — Client Credentials
Token URL:  https://auth.uber.com/oauth/v2/token
API Base:   https://api.uber.com
```

## 目錄

- [Uber Eats Marketplace API 串接筆記](#uber-eats-marketplace-api-串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [認證方式（OAuth 2.0）](#認證方式oauth-20)
  - [測試環境與測試帳號](#測試環境與測試帳號)
  - [API 端點](#api-端點)
    - [訂單管理](#訂單管理)
    - [菜單管理](#菜單管理)
  - [Webhook 設定](#webhook-設定)
    - [驗簽流程](#驗簽流程)
    - [Webhook 事件類型](#webhook-事件類型)
  - [訂單資料格式](#訂單資料格式)
  - [菜單資料格式](#菜單資料格式)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

- [Uber Eats Developer Portal](https://developer.uber.com/)
- [Uber Eats API 文件首頁](https://developer.uber.com/docs/eats/introduction)
- [POS Integration Guide](https://developer.uber.com/docs/eats/guides/pos-integration)
- [Menu API 文件](https://developer.uber.com/docs/eats/references/api/v2/post-eats-stores-storeid-menu)
- [Order API 文件](https://developer.uber.com/docs/eats/references/api/v1/get-eats-stores-storeid-orders)
- [Webhook 事件文件](https://developer.uber.com/docs/eats/guides/webhooks)
- [OAuth 2.0 Token 說明](https://developer.uber.com/docs/riders/guides/authentication/oauth-20)
- [Uber Developer App 管理後台](https://developer.uber.com/dashboard)
- [API 狀態頁](https://status.uber.com/)
- [Uber Eats 台灣餐廳夥伴申請](https://www.ubereats.com/tw/restaurant)

---

## 申請流程

1. 至 [Uber Developer Portal](https://developer.uber.com/) 建立帳號
2. 建立新的 **Application**，選擇 `Eats` 產品
3. 申請 Eats API 存取權（需填寫商業資訊，由 Uber 審核）
4. 審核通過後，在 Dashboard 取得：
   - **Client ID**
   - **Client Secret**
5. 在 Uber Eats 商家後台取得 **Store ID**（`restaurant.uuid`）
6. 設定 Webhook URL 及取得 **Webhook Secret**

> ⚠️ Uber Eats API 屬於**受邀制（Invite-only）**，需透過 Uber for Business 業務窗口或已有合作夥伴關係才能申請正式存取。

---

## 認證方式（OAuth 2.0）

使用 **Client Credentials Flow**，不需使用者授權：

```
POST https://auth.uber.com/oauth/v2/token
Content-Type: application/x-www-form-urlencoded

client_id=<CLIENT_ID>
client_secret=<CLIENT_SECRET>
grant_type=client_credentials
scope=eats.order eats.store.menu.write eats.store.menu.read
```

### 所需 Scope

| Scope | 說明 |
|---|---|
| `eats.order` | 讀取與操作訂單 |
| `eats.store.menu.read` | 讀取菜單 |
| `eats.store.menu.write` | 更新菜單 |

### Token 回應

```json
{
  "access_token": "eyJhbGci...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

### 後續請求 Header

```http
Authorization: Bearer <access_token>
Content-Type: application/json
```

> 💡 Access Token 有效期通常為 **3600 秒（1 小時）**，建議在到期前 60 秒自動更新（程式碼已內建此機制）。

---

## 測試環境與測試帳號

### Sandbox 環境

```
Sandbox Token URL: https://sandbox-auth.uber.com/oauth/v2/token  (部分 region 提供)
Sandbox API Base:  https://sandbox-api.uber.com
```

> ⚠️ Sandbox 環境**並非所有 region 都開放**，台灣市場需向 Uber 技術整合窗口確認是否可用。

### 申請 Sandbox 測試帳號

1. 至 [Uber Developer Dashboard](https://developer.uber.com/dashboard)
2. 選擇 Application → **Sandbox** 分頁
3. 使用 Sandbox Client ID / Secret 進行測試
4. 測試用 Store ID 需向 Uber 整合團隊申請

### 模擬訂單觸發

若有 Sandbox 環境，可用 Uber 提供的測試工具觸發模擬訂單：

```bash
# 使用 curl 觸發 sandbox 測試訂單（需 sandbox token）
curl -X POST https://sandbox-api.uber.com/v1/eats/stores/{store_id}/test-order \
  -H "Authorization: Bearer <sandbox_access_token>" \
  -H "Content-Type: application/json" \
  -d '{"items": [{"id": "item-001", "quantity": 1}]}'
```

### 本地 Webhook 測試

```bash
# 安裝 ngrok 並暴露本地 port
ngrok http 5000

# 在 Developer Dashboard → Webhooks 設定 Endpoint URL
# https://<ngrok-id>.ngrok.io/delivery/webhook/ubereats
```

使用 Uber Webhook 測試工具（Dashboard → Webhooks → Send Test Event）發送模擬事件。

---

## API 端點

### 訂單管理

| 方法 | 路徑 | 說明 |
|---|---|---|
| `GET` | `/v1/eats/stores/{store_id}/orders` | 列出訂單 |
| `GET` | `/v1/eats/orders/{order_id}` | 取得單筆訂單詳情 |
| `POST` | `/v1/eats/orders/{order_id}/accept_pos_order` | 接受訂單 |
| `POST` | `/v1/eats/orders/{order_id}/deny_pos_order` | 拒絕 / 取消訂單 |

**列出訂單（Query Params）**

```
GET /v1/eats/stores/{store_id}/orders
  ?status=active          # active / completed / cancelled
```

**接受訂單**

```json
POST /v1/eats/orders/{order_id}/accept_pos_order
{
  "reason": "READY_FOR_PICKUP"
}
```

**拒絕訂單**

```json
POST /v1/eats/orders/{order_id}/deny_pos_order
{
  "reason": "OUT_OF_ITEMS"
}
```

拒絕原因代碼（`reason`）：

| 代碼 | 說明 |
|---|---|
| `OUT_OF_ITEMS` | 品項售罄 |
| `KITCHEN_CLOSED` | 廚房關閉 |
| `CUSTOMER_CALLED_TO_CANCEL` | 顧客致電取消 |
| `RESTAURANT_TOO_BUSY` | 店家過忙 |
| `CANNOT_COMPLETE_CUSTOMER_NOTE` | 無法完成特殊要求 |
| `TECHNICAL_FAILURE` | 系統故障 |

---

### 菜單管理

| 方法 | 路徑 | 說明 |
|---|---|---|
| `GET` | `/v2/eats/stores/{store_id}/menu` | 取得目前菜單 |
| `POST` | `/v2/eats/stores/{store_id}/menu` | 全量覆蓋菜單 |
| `PATCH` | `/v2/eats/stores/{store_id}/menu/items/{item_id}` | 更新單品價格 |
| `POST` | `/v2/eats/stores/{store_id}/menu/items/{item_id}/availability` | 設定上下架 |

**下架品項**

```json
POST /v2/eats/stores/{store_id}/menu/items/{item_id}/availability
{
  "is_suspended": true
}
```

**修改價格（單位：分）**

```json
PATCH /v2/eats/stores/{store_id}/menu/items/{item_id}
{
  "price_info": {
    "price": 18000
  }
}
```

> ⚠️ Uber Eats 菜單 API 的**價格單位為「分」**，NT$180 → `18000`。

---

## Webhook 設定

### 設定步驟

1. 登入 [Uber Developer Dashboard](https://developer.uber.com/dashboard)
2. 選擇 Application → **Webhooks** → **Add Endpoint**
3. 填入 Webhook URL（需 HTTPS）
4. 選擇訂閱事件類型
5. 儲存後取得 **Signing Secret**（Webhook 驗簽用）

### 驗簽流程

```
Signature Header: X-Uber-Signature
演算法: HMAC-SHA256(webhook_secret, raw_request_body)
比對: 收到的 signature == 計算的 hexdigest
```

```python
import hmac, hashlib

def verify_webhook(payload_bytes: bytes, signature: str, secret: str) -> bool:
    expected = hmac.new(
        secret.encode(), payload_bytes, hashlib.sha256
    ).hexdigest()
    return hmac.compare_digest(expected, signature)
```

### Webhook 事件類型

| 事件 | 說明 |
|---|---|
| `eats.order.created` | 新訂單建立 |
| `eats.order.scheduled` | 預約訂單 |
| `eats.order.status_changed` | 訂單狀態變更 |
| `eats.order.cancelled` | 訂單取消 |
| `eats.order.fulfilled` | 訂單完成 |
| `eats.courier.assigned` | 外送員指派 |
| `eats.courier.arriving` | 外送員抵達店家 |

**Webhook Payload 範例**

```json
{
  "event_id": "evt-abc123",
  "event_type": "eats.order.created",
  "event_time": 1705312200,
  "meta": {
    "resource_id": "order-uuid-here",
    "resource_type": "eats.order",
    "user_id": "store-uuid-here"
  },
  "data": {
    "id": "order-uuid-here",
    "display_id": "#1234",
    "current_state": "created",
    "placed_at": "2024-01-15T10:30:00Z",
    "eater": { "first_name": "Xiao Ming" },
    "cart": {
      "items": [
        {
          "id": "item-001",
          "title": "牛肉漢堡",
          "quantity": 2,
          "price": {
            "unit_price": { "amount": 18000, "currency_code": "TWD" },
            "total_price": { "amount": 36000, "currency_code": "TWD" }
          }
        }
      ]
    },
    "payment": {
      "charges": {
        "total": { "amount": 40000, "currency_code": "TWD" }
      },
      "payment_method_info": [
        { "payment_method_family": "online" }
      ]
    }
  }
}
```

---

## 訂單資料格式

Uber Eats 原始訂單 → 系統內部通用格式對應：

| 原始欄位 | 系統欄位 | 備註 |
|---|---|---|
| `id` | `external_order_id` | UUID 格式 |
| `display_id` | `order_no` | 顯示用編號（#1234） |
| `current_state` | `status` | lowercase |
| `eater.first_name` | `customer_name` | |
| `cart.items[].id` | `items[].external_id` | |
| `cart.items[].price.unit_price.amount` | `items[].unit_price` | 分 ÷ 100 = 元 |
| `payment.charges.total.amount` | `total_amount` | 分 ÷ 100 = 元 |
| `payment.payment_method_info[0].payment_method_family` | `payment_method` | online / cash |
| `cart.special_instructions` | `note` | 備註 |
| `placed_at` | `placed_at` | ISO 8601 |
| `estimated_ready_for_pickup_at` | `estimated_pickup_at` | |

---

## 菜單資料格式

**GET /menu 回應結構**

```json
{
  "menus": [
    {
      "id": "main-menu",
      "title": "主選單",
      "service_availability": [...]
    }
  ],
  "categories": [
    {
      "id": "cat-001",
      "title": "主餐",
      "entities": [
        { "id": "item-001", "type": "ITEM" }
      ]
    }
  ],
  "items": [
    {
      "id": "item-001",
      "title": "牛肉漢堡",
      "description": "",
      "price_info": {
        "price": 18000,
        "core_price": 18000
      },
      "suspension_info": {
        "suspension": { "suspend": false }
      }
    }
  ],
  "modifier_groups": []
}
```

**POST /menu 全量更新**（同上格式，完整結構覆蓋）

```json
{
  "menus": [
    {
      "id": "main-menu",
      "title": "主選單",
      "service_availability": [
        { "day_of_week": "monday",    "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] },
        { "day_of_week": "tuesday",   "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] },
        { "day_of_week": "wednesday", "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] },
        { "day_of_week": "thursday",  "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] },
        { "day_of_week": "friday",    "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] },
        { "day_of_week": "saturday",  "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] },
        { "day_of_week": "sunday",    "time_periods": [{ "start_time": "00:00", "end_time": "23:59" }] }
      ],
      "category_ids_sorted_with_type": [
        { "id": "cat-default", "type": "DEFAULT_MENU_CATEGORY" }
      ]
    }
  ],
  "categories": [
    {
      "id": "cat-default",
      "title": "商品",
      "entities": [
        { "id": "item-001", "type": "ITEM" }
      ]
    }
  ],
  "items": [
    {
      "id": "item-001",
      "title": "牛肉漢堡",
      "description": "",
      "price_info": { "price": 18000, "core_price": 18000 },
      "quantity_info": { "overrides": [] },
      "suspension_info": { "suspension": { "suspend": false } }
    }
  ],
  "modifier_groups": []
}
```

---

## 錯誤處理

| HTTP Status | 說明 | 處理方式 |
|---|---|---|
| `400` | 請求格式錯誤 | 檢查 JSON body / scope |
| `401` | Token 無效或過期 | 重新取得 access_token |
| `403` | 無此 store 的存取權 | 確認 store_id 與 scope |
| `404` | 資源不存在 | 確認 order_id / item_id |
| `409` | 衝突（如訂單已接受） | 檢查操作順序 |
| `429` | Rate limit 超出 | 加入 retry with backoff |
| `5xx` | Uber 伺服器錯誤 | 重試，記錄錯誤 |

**Rate Limit 建議策略**

```python
import time

def request_with_retry(fn, max_retries=3):
    for attempt in range(max_retries):
        try:
            return fn()
        except requests.HTTPError as e:
            status = e.response.status_code
            if status == 429:
                retry_after = int(e.response.headers.get('Retry-After', 5))
                time.sleep(retry_after)
            elif status >= 500:
                time.sleep(2 ** attempt)
            else:
                raise
    raise RuntimeError('Max retries exceeded')
```

---

## 範例程式碼

### 初始化 Client

```python
from app.delivery.adapters.ubereats import UberEatsClient

client = UberEatsClient(
    client_id     = 'your_client_id',
    client_secret = 'your_client_secret',
    store_id      = 'your_store_uuid',
    webhook_secret= 'your_webhook_signing_secret',
)
# Token 在首次 API 呼叫時自動取得，並在到期前自動更新
```

### 取得 Access Token（手動）

```python
import requests

resp = requests.post(
    'https://auth.uber.com/oauth/v2/token',
    data={
        'client_id':     'your_client_id',
        'client_secret': 'your_client_secret',
        'grant_type':    'client_credentials',
        'scope':         'eats.order eats.store.menu.write eats.store.menu.read',
    }
)
token = resp.json()['access_token']
```

### 拉取新訂單

```python
orders = client.list_orders(status='active')
for order in orders:
    print(order['id'], order['current_state'])
```

### 接受訂單

```python
success = client.accept_order(order_id='order-uuid-here')
print('接受成功' if success else '接受失敗')
```

### 全量更新菜單

```python
from app.delivery.adapters.ubereats import build_menu_from_products

products = [...]  # 從系統取得商品列表
menu_payload = build_menu_from_products(products, category_name='商品')
result = client.upsert_menu(menu_payload)
```

### Webhook 接收（Flask 範例）

```python
from flask import request, abort

@app.route('/delivery/webhook/ubereats', methods=['POST'])
def ubereats_webhook():
    sig = request.headers.get('X-Uber-Signature', '')
    if not client.verify_webhook(request.data, sig):
        abort(400, 'Invalid signature')

    payload    = request.get_json()
    event_type = payload.get('event_type')
    data       = payload.get('data', {})

    if event_type == 'eats.order.created':
        normalized = UberEatsClient.normalize_order(data)
        # 處理新訂單...

    return '', 200
```

### 環境變數設定（`.env`）

```ini
UBEREATS_CLIENT_ID=your_client_id
UBEREATS_CLIENT_SECRET=your_client_secret
UBEREATS_STORE_ID=your_store_uuid
UBEREATS_WEBHOOK_SECRET=your_webhook_signing_secret
```
