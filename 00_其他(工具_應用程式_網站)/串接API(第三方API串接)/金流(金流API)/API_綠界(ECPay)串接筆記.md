# 綠界（ECPay）串接筆記

```
金流 + 電子發票一體服務
認證方式：MerchantID + HashKey + HashIV → CheckMacValue（SHA256）
正式環境：https://payment.ecpay.com.tw
測試環境：https://payment-stage.ecpay.com.tw
```

## 目錄

- [綠界（ECPay）串接筆記](#綠界ecpay串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [認證方式與驗簽](#認證方式與驗簽)
  - [測試環境與測試帳號](#測試環境與測試帳號)
  - [金流服務](#金流服務)
    - [支援付款方式](#支援付款方式)
    - [建立訂單（前台付款）](#建立訂單前台付款)
    - [付款結果通知（ReturnURL）](#付款結果通知returnurl)
  - [電子發票服務](#電子發票服務)
    - [開立發票](#開立發票)
    - [作廢發票](#作廢發票)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

[ECPay 開發者中心](https://developers.ecpay.com.tw/)

[ECPay 金流 API 文件](https://developers.ecpay.com.tw/?p=2509)

[ECPay 電子發票 API 文件](https://developers.ecpay.com.tw/?p=7384)

[ECPay 測試平台](https://vendor-stage.ecpay.com.tw/)

---

## 申請流程

1. 至 [ECPay 官網](https://www.ecpay.com.tw/) 申請商家帳號
2. 完成身份審核後，後台取得：
   - `MerchantID`（特店編號）
   - `HashKey`
   - `HashIV`
3. 測試環境使用 ECPay 提供的測試商家 ID（見下方）

---

## 認證方式與驗簽

所有請求皆須產生 `CheckMacValue`，防止資料竄改。

```python
import hashlib
import urllib.parse


def generate_check_mac_value(params: dict, hash_key: str, hash_iv: str) -> str:
    # 1. 按 key 字母排序
    sorted_params = sorted(params.items(), key=lambda x: x[0].lower())

    # 2. 組成字串並加上 HashKey/HashIV
    raw = "&".join(f"{k}={v}" for k, v in sorted_params)
    raw = f"HashKey={hash_key}&{raw}&HashIV={hash_iv}"

    # 3. URL encode（轉小寫）
    encoded = urllib.parse.quote_plus(raw).lower()

    # 4. SHA256 雜湊並轉大寫
    return hashlib.sha256(encoded.encode("utf-8")).hexdigest().upper()


def verify_check_mac_value(params: dict, hash_key: str, hash_iv: str) -> bool:
    received = params.pop("CheckMacValue", "")
    expected = generate_check_mac_value(params, hash_key, hash_iv)
    return received == expected
```

---

## 測試環境與測試帳號

```
測試環境 Base URL：https://payment-stage.ecpay.com.tw

測試商家資訊：
  MerchantID : 2000132
  HashKey    : 5294y06JbISpM5x9
  HashIV     : v77hoKGq4kWxNNIS

測試信用卡：
  卡號：4311-9522-2222-2222
  到期：任何未來日期
  CVV ：任意三位數
```

---

## 金流服務

### 支援付款方式

| 付款方式 | PaymentType 值 |
|---|---|
| 信用卡（一次付清） | `Credit` |
| 信用卡（分期） | `Credit`（帶 CreditInstallment） |
| ATM 虛擬帳號 | `ATM` |
| 超商代碼（ibon/FamiPort） | `CVS` |
| 超商條碼 | `BARCODE` |
| 全通路（讓用戶自選） | `ALL` |

### 建立訂單（前台付款）

```
POST https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5
Content-Type: application/x-www-form-urlencoded
```

```python
import datetime

params = {
    "MerchantID"        : "2000132",
    "MerchantTradeNo"   : "TEST" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
    "MerchantTradeDate" : datetime.datetime.now().strftime("%Y/%m/%d %H:%M:%S"),
    "PaymentType"       : "aio",
    "TotalAmount"       : "100",
    "TradeDesc"         : "測試交易",
    "ItemName"          : "商品名稱",
    "ReturnURL"         : "https://yourdomain.com/ecpay/return",  # 非同步通知
    "OrderResultURL"    : "https://yourdomain.com/ecpay/result",  # 前台跳轉
    "ChoosePayment"     : "ALL",
    "EncryptType"       : "1",                                    # 1 = SHA256
}
params["CheckMacValue"] = generate_check_mac_value(
    params, hash_key="5294y06JbISpM5x9", hash_iv="v77hoKGq4kWxNNIS"
)

# 將 params 以 HTML form POST 方式送出（前台導向）
```

### 付款結果通知（ReturnURL）

```python
from flask import Flask, request

app = Flask(__name__)

HASH_KEY = "5294y06JbISpM5x9"
HASH_IV  = "v77hoKGq4kWxNNIS"


@app.route("/ecpay/return", methods=["POST"])
def ecpay_return():
    data = request.form.to_dict()

    if not verify_check_mac_value(data.copy(), HASH_KEY, HASH_IV):
        return "0|Error", 200

    rtn_code = data.get("RtnCode")
    trade_no = data.get("MerchantTradeNo")

    if rtn_code == "1":
        # 付款成功，更新訂單狀態
        pass

    return "1|OK", 200            # 必須回傳 1|OK，否則 ECPay 會持續重送
```

---

## 電子發票服務

### 開立發票

```
POST https://einvoice-stage.ecpay.com.tw/B2CInvoice/Issue
Content-Type: application/x-www-form-urlencoded
```

```python
params = {
    "MerchantID"      : "2000132",
    "RelateNumber"    : "INV" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
    "CustomerEmail"   : "customer@example.com",
    "Print"           : "0",          # 0=不列印, 1=列印
    "Donation"        : "0",          # 0=不捐贈
    "TaxType"         : "1",          # 1=應稅
    "SalesAmount"     : "100",
    "InvoiceRemark"   : "備註",
    "Items"           : [
        {"ItemName": "商品", "ItemCount": 1, "ItemWord": "個",
         "ItemPrice": 100, "ItemTaxType": "1", "ItemAmount": 100}
    ],
    "InvType"         : "07",         # 07=一般稅額
    "vat"             : "1",
}
```

### 作廢發票

```python
params = {
    "MerchantID"    : "2000132",
    "InvoiceNo"     : "AB12345678",
    "InvoiceDate"   : "2024-01-01",
    "Reason"        : "作廢原因",
}
```

---

## 錯誤處理

| RtnCode | 說明 |
|---|---|
| `1` | 成功 |
| `10200047` | CheckMacValue 驗證失敗 |
| `10200048` | 參數格式錯誤 |
| `10200056` | 訂單編號重複 |
| `10200066` | 金額錯誤 |

---

## 範例程式碼

```python
# Flask 完整串接範例（前台表單導向）
from flask import Flask, render_template_string, request
import datetime
import hashlib
import urllib.parse

app = Flask(__name__)

MERCHANT_ID = "2000132"
HASH_KEY    = "5294y06JbISpM5x9"
HASH_IV     = "v77hoKGq4kWxNNIS"
STAGE_URL   = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5"


@app.route("/checkout")
def checkout():
    params = {
        "MerchantID"        : MERCHANT_ID,
        "MerchantTradeNo"   : "ORD" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
        "MerchantTradeDate" : datetime.datetime.now().strftime("%Y/%m/%d %H:%M:%S"),
        "PaymentType"       : "aio",
        "TotalAmount"       : "100",
        "TradeDesc"         : "Test",
        "ItemName"          : "商品 x1",
        "ReturnURL"         : "https://yourdomain.com/ecpay/return",
        "ChoosePayment"     : "Credit",
        "EncryptType"       : "1",
    }
    params["CheckMacValue"] = generate_check_mac_value(params, HASH_KEY, HASH_IV)

    inputs = "".join(f'<input type="hidden" name="{k}" value="{v}">' for k, v in params.items())
    html = f'''
        <form id="f" action="{STAGE_URL}" method="post">{inputs}</form>
        <script>document.getElementById("f").submit();</script>
    '''
    return render_template_string(html)
```
