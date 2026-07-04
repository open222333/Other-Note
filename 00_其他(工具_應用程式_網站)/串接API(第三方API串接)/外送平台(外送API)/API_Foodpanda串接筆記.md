# Foodpanda Vendor API 串接筆記

```
Delivery Hero Vendor API（foodpanda 使用 Delivery Hero 平台）
認證方式：API Key（Bearer Token）
台灣 Base URL：https://tw.fd-api.com
```

## 目錄

- [Foodpanda Vendor API 串接筆記](#foodpanda-vendor-api-串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [認證方式](#認證方式)
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

- [Delivery Hero Vendor Portal](https://vendor.foodpanda.com.tw/)
- [Delivery Hero Developer Portal（需合作夥伴帳號）](https://developers.deliveryhero.com/)
- [foodpanda 商業夥伴申請入口](https://www.foodpanda.com.tw/contents/partner)
- [API 狀態頁](https://status.foodpanda.com/)
- [Webhook 文件（合作夥伴帳號才可存取）](https://developers.deliveryhero.com/docs/webhooks)

---

## 申請流程

1. 至 [foodpanda 商業夥伴申請](https://www.foodpanda.com.tw/contents/partner) 填寫合作申請
2. 通過審核後，foodpanda 業務提供：
   - **Vendor Code**（店家代碼，如 `abc1`）
   - **API Key**（Bearer Token 憑證）
   - **Webhook Secret**（用於 HMAC-SHA256 驗簽）
3. 至 Vendor Portal 確認店家基本資料與菜單
4. 索取 API 文件存取權限（需向 foodpanda 技術窗口申請 Developers Portal 帳號）

> ⚠️ foodpanda API 文件為非公開，**必須向合作夥伴業務申請開發者存取權**後才能取得完整文件。

---

## 認證方式

所有 API 請求使用 **HTTP Bearer Token**：

```http
Authorization: Bearer <API_KEY>
Content-Type: application/json
Accept: application/json
```

| 參數 | 說明 |
|---|---|
| `API_KEY` | foodpanda 提供的 API Key |
| `vendor_code` | 店家代碼（URL path 參數） |
| `base_url` | 台灣：`https://tw.fd-api.com` |

---

## 測試環境與測試帳號

### Sandbox 環境

```
Sandbox Base URL: https://tw-stg.fd-api.com   # (staging，需向 FP 申請開通)
```

foodpanda **沒有公開的 sandbox**，需透過業務窗口申請 Staging 環境：

1. 聯繫 foodpanda 技術整合窗口（Integration Team）
2. 申請 Staging vendor_code 與對應 API Key
3. Staging 環境使用和 Production 相同的 API 結構，但資料互相隔離

### 測試訂單模擬

Vendor Portal 提供「模擬訂單」功能（Staging 環境）：

1. 登入 [Vendor Portal - Staging](https://vendor-staging.foodpanda.com.tw/)
2. 進入 **Order Management** → **Simulate Order**
3. 選擇商品，送出後 Webhook 會收到 `order.created` 事件

### 本地 Webhook 測試

```bash
# 使用 ngrok 暴露本地 server
ngrok http 5000

# 將 ngrok URL 設為 Webhook endpoint（需在 Vendor Portal 設定）
# https://<ngrok-id>.ngrok.io/delivery/webhook/foodpanda
```

---

## API 端點

### 訂單管理

| 方法 | 路徑 | 說明 |
|---|---|---|
| `GET` | `/api/v5/vendors/{vendor_code}/orders` | 列出訂單 |
| `GET` | `/api/v5/orders/{order_code}` | 取得單筆訂單詳情 |
| `PUT` | `/api/v5/orders/{order_code}/status` | 更新訂單狀態（接受/取消） |

**列出訂單（Query Params）**

```
GET /api/v5/vendors/{vendor_code}/orders
  ?status=new          # new / confirmed / delivered / cancelled
  &limit=50
```

**接受訂單**

```json
PUT /api/v5/orders/{order_code}/status
{
  "status": "confirmed",
  "preparation_time": 20
}
```

**取消訂單**

```json
PUT /api/v5/orders/{order_code}/status
{
  "status": "cancelled",
  "reason_code": "VENDOR_BUSY"
}
```

取消原因代碼（`reason_code`）：

| 代碼 | 說明 |
|---|---|
| `VENDOR_BUSY` | 店家忙碌中 |
| `ITEM_NOT_AVAILABLE` | 品項售罄 |
| `CLOSING_EARLY` | 提早關店 |
| `TECHNICAL_FAILURE` | 系統故障 |

---

### 菜單管理

| 方法 | 路徑 | 說明 |
|---|---|---|
| `GET` | `/api/v5/vendors/{vendor_code}/menu` | 取得完整菜單 |
| `PUT` | `/api/v5/vendors/{vendor_code}/menu` | 全量更新菜單 |
| `PATCH` | `/api/v5/vendors/{vendor_code}/menu/products/{product_id}` | 更新單品（價格/上下架） |

**更新單品上下架**

```json
PATCH /api/v5/vendors/{vendor_code}/menu/products/{product_id}
{
  "active": false
}
```

**更新單品價格**

```json
PATCH /api/v5/vendors/{vendor_code}/menu/products/{product_id}
{
  "price": 120.0
}
```

---

## Webhook 設定

### 設定步驟

1. 登入 **Vendor Portal** → **Settings** → **Integrations**
2. 填入 Webhook URL（需 HTTPS，且能從公網訪問）
3. 儲存後 foodpanda 提供 `webhook_secret`

### 驗簽流程

```
Signature Header: X-FP-Signature
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

> ⚠️ `hmac.new` 在部分 Python 版本應改為 `hmac.new`，注意 Python 3.x 使用 `hmac.new`

### Webhook 事件類型

| 事件 | 說明 |
|---|---|
| `order.created` | 新訂單進來 |
| `order.updated` | 訂單資訊更新 |
| `order.cancelled` | 訂單被取消 |
| `order.delivered` | 訂單已送達 |
| `rider.assigned` | 外送員已指派 |

**Webhook Payload 範例**

```json
{
  "event": "order.created",
  "data": {
    "code": "abc123xyz",
    "status": { "code": "new" },
    "placed_at": "2024-01-15T10:30:00Z",
    "customer": {
      "first_name": "Wang",
      "last_name": "Xiao Ming",
      "mobile_number": "+886912345678"
    },
    "products": [
      {
        "id": "prod-001",
        "name": "牛肉漢堡",
        "quantity": 2,
        "unit_price": 180.0
      }
    ],
    "order_total": {
      "subtotal": 360.0,
      "delivery_fee": 40.0,
      "grand_total": 400.0
    },
    "payment": { "type": "online" }
  }
}
```

---

## 訂單資料格式

foodpanda 原始訂單 → 系統內部通用格式對應：

| 原始欄位 | 系統欄位 | 備註 |
|---|---|---|
| `code` | `external_order_id` | 訂單代碼 |
| `status.code` | `status` | lowercase |
| `customer.first_name + last_name` | `customer_name` | |
| `customer.mobile_number` | `customer_phone` | |
| `products[].id` | `items[].external_id` | |
| `products[].unit_price` | `items[].unit_price` | 台幣，非分 |
| `order_total.grand_total` | `total_amount` | |
| `payment.type` | `payment_method` | online / cash |
| `customer_comment` | `note` | 備註 |
| `estimated_delivery_time` | `estimated_pickup_at` | |

---

## 菜單資料格式

**GET /menu 回應結構**

```json
{
  "data": {
    "categories": [
      {
        "id": "cat-001",
        "name": "主餐",
        "products": [
          {
            "id": "prod-001",
            "name": "牛肉漢堡",
            "description": "...",
            "price": 180.0,
            "active": true
          }
        ]
      }
    ]
  }
}
```

**PUT /menu 更新格式（全量覆蓋）**

```json
{
  "categories": [
    {
      "id": "cat-default",
      "name": "全部商品",
      "products": [
        {
          "id": "prod-001",
          "name": "牛肉漢堡",
          "description": "",
          "price": 180.0,
          "active": true
        }
      ]
    }
  ]
}
```

---

## 錯誤處理

| HTTP Status | 說明 | 處理方式 |
|---|---|---|
| `400` | 請求格式錯誤 | 檢查 JSON body 格式 |
| `401` | API Key 無效或過期 | 重新申請 API Key |
| `403` | 無此 vendor_code 權限 | 確認 vendor_code 正確 |
| `404` | 訂單 / 品項不存在 | 確認 order_code / product_id |
| `422` | 業務邏輯錯誤（如訂單狀態不對） | 查看 response body |
| `429` | 超出速率限制 | 加入 retry with backoff |
| `5xx` | foodpanda 伺服器錯誤 | 重試，記錄錯誤 |

```python
import time

def request_with_retry(fn, max_retries=3, backoff=2.0):
    for attempt in range(max_retries):
        try:
            return fn()
        except requests.HTTPError as e:
            if e.response.status_code == 429:
                time.sleep(backoff ** attempt)
            elif e.response.status_code >= 500:
                time.sleep(backoff ** attempt)
            else:
                raise
    raise RuntimeError('Max retries exceeded')
```

---

## 範例程式碼

### 初始化 Client

```python
from app.delivery.adapters.foodpanda import FoodpandaClient

client = FoodpandaClient(
    api_key      = 'your_api_key_here',
    vendor_code  = 'abc1',
    base_url     = 'https://tw.fd-api.com',       # Production
    # base_url   = 'https://tw-stg.fd-api.com',   # Staging（測試用）
    webhook_secret = 'your_webhook_secret',
)
```

### 拉取新訂單

```python
orders = client.list_orders(status='new', limit=50)
for order in orders:
    print(order['code'], order['status'])
```

### 接受訂單

```python
success = client.confirm_order(order_code='abc123xyz', prep_time_minutes=20)
print('接受成功' if success else '接受失敗')
```

### 全量更新菜單

```python
from app.delivery.adapters.foodpanda import build_menu_from_products

products = [...]  # 從系統取得商品列表
menu_payload = build_menu_from_products(products, category_name='全部商品')
result = client.update_menu(menu_payload)
```

### Webhook 接收（Flask 範例）

```python
from flask import request, abort

@app.route('/delivery/webhook/foodpanda', methods=['POST'])
def foodpanda_webhook():
    sig = request.headers.get('X-FP-Signature', '')
    if not client.verify_webhook(request.data, sig):
        abort(400, 'Invalid signature')

    payload = request.get_json()
    event   = payload.get('event')
    data    = payload.get('data', {})

    if event == 'order.created':
        normalized = FoodpandaClient.normalize_order(data)
        # 處理新訂單...

    return '', 200
```

### 環境變數設定（`.env`）

```ini
FOODPANDA_API_KEY=your_api_key
FOODPANDA_VENDOR_CODE=abc1
FOODPANDA_BASE_URL=https://tw.fd-api.com
FOODPANDA_WEBHOOK_SECRET=your_webhook_secret
```
