# 外送平台 API 串接總覽

```
彙整外送平台 API 串接筆記：Foodpanda（Delivery Hero）、Uber Eats
認證方式：Bearer Token / OAuth 2.0 Client Credentials
Webhook 驗簽：HMAC-SHA256
```

## 目錄

- [外送平台 API 串接總覽](#外送平台-api-串接總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [平台比較](#平台比較)
  - [基本資訊](#基本資訊)
  - [費用](#費用)
  - [訂單 Webhook 事件對應](#訂單-webhook-事件對應)
  - [訂單欄位對應](#訂單欄位對應)
- [共用概念](#共用概念)
  - [HMAC-SHA256 驗簽（兩平台通用）](#hmac-sha256-驗簽兩平台通用)
  - [Webhook 處理骨架](#webhook-處理骨架)
- [Foodpanda](#foodpanda)
  - [認證](#認證)
  - [主要端點](#主要端點)
  - [取消原因代碼](#取消原因代碼)
  - [Webhook 事件](#webhook-事件)
- [Uber Eats](#uber-eats)
  - [認證（OAuth 2.0）](#認證oauth-20)
  - [主要端點](#主要端點-1)
  - [拒絕原因代碼](#拒絕原因代碼)
  - [Webhook 事件](#webhook-事件-1)

---

## 參考資料

[Foodpanda Vendor Portal](https://vendor.foodpanda.com.tw/)

[Delivery Hero Developer Portal（需合作夥伴帳號）](https://developers.deliveryhero.com/)

[Uber Eats Developer Portal](https://developer.uber.com/)

[Uber Eats POS Integration Guide](https://developer.uber.com/docs/eats/guides/pos-integration)

---

# 平台比較

## 基本資訊

| 項目 | Foodpanda | Uber Eats |
|---|---|---|
| 底層平台 | Delivery Hero Vendor API | Uber Eats Marketplace API |
| 認證方式 | Bearer Token（靜態 API Key） | OAuth 2.0 Client Credentials（Token 每小時更新） |
| Token URL | — | `https://auth.uber.com/oauth/v2/token` |
| API Base | `https://tw.fd-api.com` | `https://api.uber.com` |
| Webhook 驗簽 Header | `X-FP-Signature` | `X-Uber-Signature` |
| 驗簽演算法 | HMAC-SHA256 | HMAC-SHA256 |
| 申請難度 | 需業務窗口，文件非公開 | 受邀制，需業務窗口 |
| 測試環境 | Staging（需向 FP 申請） | Sandbox（部分 region 開放） |
| 本地測試工具 | ngrok + Vendor Portal 模擬訂單 | ngrok + Dashboard Send Test Event |
| 菜單價格單位 | 元（NT$180 → `180.0`） | 分（NT$180 → `18000`） |

## 費用

> 費率依簽約方案與月交易量而異，以下為一般商家參考值，實際費率請向各平台業務確認。

| 費用項目 | Foodpanda | Uber Eats |
|---|---|---|
| 平台抽成（每筆訂單） | 約 25–35% | 約 25–35% |
| API 串接費 | 無（需合作夥伴資格） | 無（需審核資格） |
| 月費 / 上架費 | 視合約而定 | 視合約而定 |
| 金流手續費 | 由平台代收，含在抽成內 | 由平台代收，含在抽成內 |
| 活動補貼分攤 | 視促銷活動而定 | 視促銷活動而定 |

```
抽成計算方式：以訂單食物小計（不含外送費）為基準
例：訂單 $200，抽成 30% → 平台收 $60，店家收 $140（扣除其他成本前）
```

## 訂單 Webhook 事件對應

| 情境 | Foodpanda 事件 | Uber Eats 事件 |
|---|---|---|
| 新訂單 | `order.created` | `eats.order.created` |
| 訂單更新 | `order.updated` | `eats.order.status_changed` |
| 訂單取消 | `order.cancelled` | `eats.order.cancelled` |
| 訂單完成 | `order.delivered` | `eats.order.fulfilled` |
| 外送員指派 | `rider.assigned` | `eats.courier.assigned` |
| 外送員抵達 | — | `eats.courier.arriving` |
| 預約訂單 | — | `eats.order.scheduled` |

## 訂單欄位對應

| 語意 | Foodpanda 欄位 | Uber Eats 欄位 |
|---|---|---|
| 訂單識別碼 | `code` | `id`（UUID） |
| 顯示編號 | — | `display_id`（#1234） |
| 訂單狀態 | `status.code` | `current_state` |
| 客戶姓名 | `customer.first_name + last_name` | `eater.first_name` |
| 客戶電話 | `customer.mobile_number` | — |
| 品項單價 | `products[].unit_price`（元） | `cart.items[].price.unit_price.amount`（分） |
| 總金額 | `order_total.grand_total`（元） | `payment.charges.total.amount`（分） |
| 付款方式 | `payment.type` | `payment.payment_method_info[0].payment_method_family` |
| 備註 | `customer_comment` | `cart.special_instructions` |
| 預計取餐時間 | `estimated_delivery_time` | `estimated_ready_for_pickup_at` |

---

# 共用概念

## HMAC-SHA256 驗簽（兩平台通用）

```python
import hmac
import hashlib


def verify_hmac_signature(payload_bytes: bytes, signature: str, secret: str) -> bool:
    """兩平台驗簽邏輯相同，差異只在 Header 名稱"""
    expected = hmac.new(
        secret.encode("utf-8"), payload_bytes, hashlib.sha256
    ).hexdigest()
    return hmac.compare_digest(expected, signature)
```

```
Foodpanda  → Header: X-FP-Signature
Uber Eats  → Header: X-Uber-Signature
```

## Webhook 處理骨架

```python
from flask import Flask, request, abort
import json

app = Flask(__name__)


@app.route("/webhook/<platform>", methods=["POST"])
def webhook(platform: str):
    # 1. 取得驗簽 Header
    header_map = {
        "foodpanda": "X-FP-Signature",
        "ubereats" : "X-Uber-Signature",
    }
    sig = request.headers.get(header_map[platform], "")

    # 2. 驗簽
    secret = get_secret(platform)
    if not verify_hmac_signature(request.data, sig, secret):
        abort(400, "Invalid signature")

    payload = request.get_json()
    event   = payload.get("event") or payload.get("event_type")

    # 3. 冪等：避免重複處理
    order_id = extract_order_id(platform, payload)
    if is_already_processed(order_id):
        return "", 200

    # 4. 處理業務邏輯
    handle_event(platform, event, payload)

    return "", 200
```

---

# Foodpanda

## 認證

```python
import requests

API_KEY     = "your_api_key"
VENDOR_CODE = "abc1"
BASE_URL    = "https://tw.fd-api.com"   # Staging: https://tw-stg.fd-api.com

HEADERS = {
    "Authorization": f"Bearer {API_KEY}",
    "Content-Type" : "application/json",
    "Accept"       : "application/json",
}
```

## 主要端點

| 方法 | 路徑 | 說明 |
|---|---|---|
| `GET` | `/api/v5/vendors/{vendor_code}/orders?status=new` | 列出新訂單 |
| `GET` | `/api/v5/orders/{order_code}` | 取得單筆訂單 |
| `PUT` | `/api/v5/orders/{order_code}/status` | 接受訂單（`confirmed`）或取消（`cancelled`） |
| `GET` | `/api/v5/vendors/{vendor_code}/menu` | 取得菜單 |
| `PUT` | `/api/v5/vendors/{vendor_code}/menu` | 全量更新菜單 |
| `PATCH` | `/api/v5/vendors/{vendor_code}/menu/products/{product_id}` | 更新單品（上下架/價格） |

```python
# 接受訂單
requests.put(
    f"{BASE_URL}/api/v5/orders/{order_code}/status",
    json={"status": "confirmed", "preparation_time": 20},
    headers=HEADERS,
)

# 取消訂單
requests.put(
    f"{BASE_URL}/api/v5/orders/{order_code}/status",
    json={"status": "cancelled", "reason_code": "ITEM_NOT_AVAILABLE"},
    headers=HEADERS,
)
```

## 取消原因代碼

| reason_code | 說明 |
|---|---|
| `VENDOR_BUSY` | 店家忙碌中 |
| `ITEM_NOT_AVAILABLE` | 品項售罄 |
| `CLOSING_EARLY` | 提早關店 |
| `TECHNICAL_FAILURE` | 系統故障 |

## Webhook 事件

```python
@app.route("/webhook/foodpanda", methods=["POST"])
def foodpanda_webhook():
    sig = request.headers.get("X-FP-Signature", "")
    if not verify_hmac_signature(request.data, sig, WEBHOOK_SECRET):
        abort(400)

    payload = request.get_json()
    event   = payload.get("event")
    data    = payload.get("data", {})

    if event == "order.created":
        order_code = data["code"]
        # 接受訂單...

    return "", 200
```

---

# Uber Eats

## 認證（OAuth 2.0）

```python
import requests
import time

CLIENT_ID     = "your_client_id"
CLIENT_SECRET = "your_client_secret"
STORE_ID      = "your_store_uuid"
BASE_URL      = "https://api.uber.com"

_token_cache = {"token": None, "expires_at": 0}


def get_access_token() -> str:
    if time.time() < _token_cache["expires_at"] - 60:
        return _token_cache["token"]

    resp = requests.post(
        "https://auth.uber.com/oauth/v2/token",
        data={
            "client_id"    : CLIENT_ID,
            "client_secret": CLIENT_SECRET,
            "grant_type"   : "client_credentials",
            "scope"        : "eats.order eats.store.menu.write eats.store.menu.read",
        },
    )
    resp.raise_for_status()
    data = resp.json()
    _token_cache["token"]      = data["access_token"]
    _token_cache["expires_at"] = time.time() + data["expires_in"]
    return _token_cache["token"]


def get_headers() -> dict:
    return {
        "Authorization": f"Bearer {get_access_token()}",
        "Content-Type" : "application/json",
    }
```

## 主要端點

| 方法 | 路徑 | 說明 |
|---|---|---|
| `GET` | `/v1/eats/stores/{store_id}/orders?status=active` | 列出訂單 |
| `GET` | `/v1/eats/orders/{order_id}` | 取得單筆訂單 |
| `POST` | `/v1/eats/orders/{order_id}/accept_pos_order` | 接受訂單 |
| `POST` | `/v1/eats/orders/{order_id}/deny_pos_order` | 拒絕訂單 |
| `GET` | `/v2/eats/stores/{store_id}/menu` | 取得菜單 |
| `POST` | `/v2/eats/stores/{store_id}/menu` | 全量更新菜單 |
| `POST` | `/v2/eats/stores/{store_id}/menu/items/{item_id}/availability` | 上下架品項 |

```python
# 接受訂單
requests.post(
    f"{BASE_URL}/v1/eats/orders/{order_id}/accept_pos_order",
    json={"reason": "READY_FOR_PICKUP"},
    headers=get_headers(),
)

# 下架品項（注意：價格單位為「分」）
requests.post(
    f"{BASE_URL}/v2/eats/stores/{STORE_ID}/menu/items/{item_id}/availability",
    json={"is_suspended": True},
    headers=get_headers(),
)
```

> 菜單 API 價格單位為**分**：NT$180 → `18000`

## 拒絕原因代碼

| reason | 說明 |
|---|---|
| `OUT_OF_ITEMS` | 品項售罄 |
| `KITCHEN_CLOSED` | 廚房關閉 |
| `RESTAURANT_TOO_BUSY` | 店家過忙 |
| `CUSTOMER_CALLED_TO_CANCEL` | 顧客致電取消 |
| `TECHNICAL_FAILURE` | 系統故障 |

## Webhook 事件

```python
@app.route("/webhook/ubereats", methods=["POST"])
def ubereats_webhook():
    sig = request.headers.get("X-Uber-Signature", "")
    if not verify_hmac_signature(request.data, sig, WEBHOOK_SECRET):
        abort(400)

    payload    = request.get_json()
    event_type = payload.get("event_type")
    data       = payload.get("data", {})

    if event_type == "eats.order.created":
        order_id = data["id"]
        # 接受訂單...

    return "", 200
```
