# 歐付寶（O'Pay / allPay）串接筆記

```
金流服務，介面與流程與綠界（ECPay）相似
認證方式：MerchantID + HashKey + HashIV → CheckMacValue（SHA256）
正式環境：https://payment.opay.tw
測試環境：https://payment-stage.opay.tw
```

## 目錄

- [歐付寶（O'Pay / allPay）串接筆記](#歐付寶opay--allpay串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [認證方式與驗簽](#認證方式與驗簽)
  - [測試環境與測試帳號](#測試環境與測試帳號)
  - [金流服務](#金流服務)
    - [支援付款方式](#支援付款方式)
    - [建立訂單](#建立訂單)
    - [付款結果通知（ReturnURL）](#付款結果通知returnurl)
  - [與綠界（ECPay）差異](#與綠界ecpay差異)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

[歐付寶開發者中心](https://www.opay.tw/Service/ApplyBusiness)

[歐付寶 API 文件](https://forum.opay.tw/forum.php)

---

## 申請流程

1. 至 [歐付寶官網](https://www.opay.tw/) 申請商家帳號
2. 完成審核後，後台取得：
   - `MerchantID`（特店編號）
   - `HashKey`
   - `HashIV`
3. 測試環境使用歐付寶提供的測試商家帳號

---

## 認證方式與驗簽

驗簽邏輯與綠界（ECPay）相同，可複用同一函式。

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
測試環境 Base URL：https://payment-stage.opay.tw

測試信用卡：
  卡號：4311-9522-2222-2222
  到期：任何未來日期
  CVV ：任意三位數
```

---

## 金流服務

### 支援付款方式

| 付款方式 | ChoosePayment 值 |
|---|---|
| 信用卡 | `Credit` |
| ATM 虛擬帳號 | `ATM` |
| 超商代碼 | `CVS` |
| 超商條碼 | `BARCODE` |
| 全通路 | `ALL` |

### 建立訂單

```
POST https://payment-stage.opay.tw/Cashier/AioCheckOut/V5
Content-Type: application/x-www-form-urlencoded
```

```python
import datetime

params = {
    "MerchantID"        : "your_merchant_id",
    "MerchantTradeNo"   : "ORD" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
    "MerchantTradeDate" : datetime.datetime.now().strftime("%Y/%m/%d %H:%M:%S"),
    "PaymentType"       : "aio",
    "TotalAmount"       : "100",
    "TradeDesc"         : "商品描述",
    "ItemName"          : "商品名稱",
    "ReturnURL"         : "https://yourdomain.com/opay/return",   # 非同步通知
    "OrderResultURL"    : "https://yourdomain.com/opay/result",   # 前台跳轉
    "ChoosePayment"     : "Credit",
    "EncryptType"       : "1",
}
params["CheckMacValue"] = generate_check_mac_value(
    params, hash_key="your_hash_key", hash_iv="your_hash_iv"
)
```

### 付款結果通知（ReturnURL）

```python
from flask import Flask, request

app = Flask(__name__)

HASH_KEY = "your_hash_key"
HASH_IV  = "your_hash_iv"


@app.route("/opay/return", methods=["POST"])
def opay_return():
    data = request.form.to_dict()

    if not verify_check_mac_value(data.copy(), HASH_KEY, HASH_IV):
        return "0|Error", 200

    rtn_code = data.get("RtnCode")

    if rtn_code == "1":
        # 付款成功
        pass

    return "1|OK", 200            # 必須回傳 1|OK
```

---

## 與綠界（ECPay）差異

| 項目 | 歐付寶（O'Pay） | 綠界（ECPay） |
|---|---|---|
| 驗簽方式 | SHA256（相同） | SHA256 |
| API 端點路徑 | `/Cashier/AioCheckOut/V5` | `/Cashier/AioCheckOut/V5`（相同） |
| 回傳格式 | 同 ECPay | 同 ECPay |
| 電子發票 | 需另外串接（不如 ECPay 完整） | 原生整合 |
| 市佔率 | 較低 | 較高 |

> 串接邏輯幾乎相同，主要差異在 Base URL 與商家帳號參數。

---

## 錯誤處理

| RtnCode | 說明 |
|---|---|
| `1` | 成功 |
| `10200047` | CheckMacValue 驗證失敗 |
| `10200048` | 參數格式錯誤 |
| `10200056` | 訂單編號重複 |

---

## 範例程式碼

```python
# Flask 前台跳轉付款
from flask import Flask, render_template_string
import datetime

app = Flask(__name__)

MERCHANT_ID = "your_merchant_id"
HASH_KEY    = "your_hash_key"
HASH_IV     = "your_hash_iv"
STAGE_URL   = "https://payment-stage.opay.tw/Cashier/AioCheckOut/V5"


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
        "ReturnURL"         : "https://yourdomain.com/opay/return",
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
