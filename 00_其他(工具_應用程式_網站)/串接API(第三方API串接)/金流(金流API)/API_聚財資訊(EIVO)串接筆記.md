# 聚財資訊（EIVO）串接筆記

```
純電子發票服務，不含金流
認證方式：API Key（Bearer Token）或帳號密碼取得 Token
官方網站：https://www.eivo.com.tw
```

## 目錄

- [聚財資訊（EIVO）串接筆記](#聚財資訊eivo串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [認證方式](#認證方式)
  - [測試環境](#測試環境)
  - [發票服務](#發票服務)
    - [開立發票（B2C）](#開立發票b2c)
    - [開立發票（B2B）](#開立發票b2b)
    - [作廢發票](#作廢發票)
    - [折讓發票](#折讓發票)
    - [查詢發票](#查詢發票)
  - [載具類型](#載具類型)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

[聚財資訊官網](https://www.eivo.com.tw/)

[EIVO API 文件（需登入後台）](https://www.eivo.com.tw/)

[財政部電子發票整合服務平台](https://www.einvoice.nat.gov.tw/)

---

## 申請流程

1. 至 [聚財資訊官網](https://www.eivo.com.tw/) 申請帳號
2. 提供統一編號、公司資料完成審核
3. 後台取得 API 金鑰或帳號密碼
4. 向財政部申請電子發票字軌（或委由 EIVO 代申請）

> 電子發票字軌需每期（每兩個月）向財政部領用，EIVO 可代為管理。

---

## 認證方式

```python
import requests

EIVO_API_URL = "https://api.eivo.com.tw"   # 請以官方文件確認實際 URL
API_KEY      = "your_api_key"


def get_headers() -> dict:
    return {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type" : "application/json",
    }
```

---

## 測試環境

```
測試環境：請參考 EIVO 後台提供的測試 API 端點
測試發票：測試環境開立的發票不會上傳財政部，僅供驗證串接流程
```

---

## 發票服務

### 開立發票（B2C）

```python
import datetime


def issue_b2c_invoice(order_no: str, amount: int, items: list, buyer_email: str = "") -> dict:
    """
    items: [{"name": "商品名稱", "count": 1, "unit": "個", "price": 100, "amount": 100}]
    """
    payload = {
        "MerchantOrderNo" : order_no,
        "Status"          : "1",              # 1=立即開立
        "Category"        : "B2C",
        "BuyerEmail"      : buyer_email,
        "PrintFlag"       : "N",
        "TaxType"         : "1",              # 1=應稅, 2=零稅率, 3=免稅
        "TaxRate"         : 5,
        "Amt"             : int(amount / 1.05),
        "TaxAmt"          : amount - int(amount / 1.05),
        "TotalAmt"        : amount,
        "Items"           : [
            {
                "ItemName"  : item["name"],
                "ItemCount" : item["count"],
                "ItemWord"  : item["unit"],
                "ItemPrice" : item["price"],
                "ItemAmt"   : item["amount"],
            }
            for item in items
        ],
    }
    resp = requests.post(
        f"{EIVO_API_URL}/invoice/issue",
        json=payload,
        headers=get_headers(),
    )
    resp.raise_for_status()
    return resp.json()
```

### 開立發票（B2B）

```python
def issue_b2b_invoice(order_no: str, amount: int, buyer_identifier: str, buyer_name: str, items: list) -> dict:
    payload = {
        "MerchantOrderNo"  : order_no,
        "Status"           : "1",
        "Category"         : "B2B",
        "BuyerIdentifier"  : buyer_identifier,   # 買方統一編號
        "BuyerName"        : buyer_name,
        "PrintFlag"        : "Y",                 # B2B 通常需要列印
        "TaxType"          : "1",
        "TaxRate"          : 5,
        "Amt"              : int(amount / 1.05),
        "TaxAmt"           : amount - int(amount / 1.05),
        "TotalAmt"         : amount,
        "Items"            : items,
    }
    resp = requests.post(
        f"{EIVO_API_URL}/invoice/issue",
        json=payload,
        headers=get_headers(),
    )
    resp.raise_for_status()
    return resp.json()
```

### 作廢發票

```python
def void_invoice(invoice_no: str, invoice_date: str, reason: str) -> dict:
    """
    invoice_date: YYYY-MM-DD
    """
    payload = {
        "InvoiceNo"   : invoice_no,
        "InvoiceDate" : invoice_date,
        "Reason"      : reason,
    }
    resp = requests.post(
        f"{EIVO_API_URL}/invoice/void",
        json=payload,
        headers=get_headers(),
    )
    resp.raise_for_status()
    return resp.json()
```

### 折讓發票

```python
def allowance_invoice(invoice_no: str, items: list) -> dict:
    """開立折讓單（部分退款）"""
    payload = {
        "InvoiceNo" : invoice_no,
        "Items"     : items,
    }
    resp = requests.post(
        f"{EIVO_API_URL}/invoice/allowance",
        json=payload,
        headers=get_headers(),
    )
    resp.raise_for_status()
    return resp.json()
```

### 查詢發票

```python
def query_invoice(order_no: str) -> dict:
    resp = requests.get(
        f"{EIVO_API_URL}/invoice/query",
        params={"MerchantOrderNo": order_no},
        headers=get_headers(),
    )
    resp.raise_for_status()
    return resp.json()
```

---

## 載具類型

| 載具類型 | CarrierType 值 | 說明 |
|---|---|---|
| 不使用 | `""` | 直接開立紙本或雲端發票 |
| 手機條碼 | `3J0002` | 消費者手機載具 |
| 自然人憑證 | `CQ0001` | 需帶 CarrierNum |
| 會員載具 | `1` | 平台自建載具 |

```python
# 使用手機載具範例
payload["CarrierType"] = "3J0002"
payload["CarrierNum"]  = "/ABC1234"    # 消費者手機條碼
```

---

## 錯誤處理

| 錯誤碼 | 說明 |
|---|---|
| `SUCCESS` | 成功 |
| `E001` | 認證失敗（API Key 錯誤） |
| `E002` | 發票字軌不足（需向財政部補領） |
| `E003` | 訂單編號重複 |
| `E004` | 統一編號格式錯誤 |
| `E005` | 發票已作廢，無法重複作廢 |

---

## 範例程式碼

```python
# Flask 整合範例：付款完成後自動開立發票
from flask import Flask, request

app = Flask(__name__)


@app.route("/payment/callback", methods=["POST"])
def payment_callback():
    """金流（如綠界）付款完成後觸發，自動開立電子發票"""
    data = request.form.to_dict()

    if data.get("RtnCode") != "1":
        return "1|OK", 200

    order_no = data["MerchantTradeNo"]
    amount   = int(data["TradeAmt"])

    try:
        result = issue_b2c_invoice(
            order_no    = order_no,
            amount      = amount,
            items       = [{"name": "商品", "count": 1, "unit": "個", "price": amount, "amount": amount}],
            buyer_email = data.get("Email", ""),
        )
        # 儲存發票號碼 result["InvoiceNo"]
    except Exception as e:
        # 發票開立失敗，記錄 log 供後續手動補開
        print(f"發票開立失敗：{e}")

    return "1|OK", 200
```
