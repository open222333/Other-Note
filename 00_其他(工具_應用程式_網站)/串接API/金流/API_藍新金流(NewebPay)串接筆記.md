# 藍新金流（NewebPay）串接筆記

```
金流 + 電子發票一體服務
認證方式：MerchantID + HashKey + HashIV → AES256 加密 + SHA256 雜湊
正式環境：https://core.newebpay.com
測試環境：https://ccore.newebpay.com
```

## 目錄

- [藍新金流（NewebPay）串接筆記](#藍新金流newebpay串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [申請流程](#申請流程)
  - [認證方式與加密](#認證方式與加密)
  - [測試環境與測試帳號](#測試環境與測試帳號)
  - [金流服務](#金流服務)
    - [支援付款方式](#支援付款方式)
    - [建立訂單（MPG 多功能付款）](#建立訂單mpg-多功能付款)
    - [付款結果通知（NotifyURL）](#付款結果通知notifyurl)
  - [電子發票服務](#電子發票服務)
    - [開立發票](#開立發票)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

[藍新金流開發者中心](https://developer.newebpay.com/)

[MPG 多功能付款 API 文件](https://developer.newebpay.com/Singlepage/MPG)

[電子發票 API 文件](https://developer.newebpay.com/Singlepage/Invoice)

[藍新測試平台](https://ccore.newebpay.com/)

---

## 申請流程

1. 至 [藍新金流官網](https://www.newebpay.com/) 申請商家帳號
2. 完成審核後，後台取得：
   - `MerchantID`（商店代號）
   - `HashKey`
   - `HashIV`
3. 測試環境使用測試商家帳號（見下方）

---

## 認證方式與加密

藍新採用 **AES256 加密** 交易資料，再對加密結果做 **SHA256 雜湊**，與綠界僅做驗簽不同。

```python
import hashlib
import urllib.parse
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
import base64


def aes_encrypt(data: str, hash_key: str, hash_iv: str) -> str:
    """將交易參數 AES256 加密"""
    key = hash_key.encode("utf-8")
    iv  = hash_iv.encode("utf-8")
    cipher = AES.new(key, AES.MODE_CBC, iv)
    encrypted = cipher.encrypt(pad(data.encode("utf-8"), AES.block_size))
    return encrypted.hex()                  # 藍新要求十六進位格式


def aes_decrypt(encrypted_hex: str, hash_key: str, hash_iv: str) -> str:
    """解密藍新回傳的加密資料"""
    key = hash_key.encode("utf-8")
    iv  = hash_iv.encode("utf-8")
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypted = cipher.decrypt(bytes.fromhex(encrypted_hex))
    return unpad(decrypted, AES.block_size).decode("utf-8")


def sha256_hash(encrypted_data: str, hash_key: str, hash_iv: str) -> str:
    """對加密後資料產生 SHA256 雜湊（用於 CheckValue）"""
    raw = f"HashKey={hash_key}&{encrypted_data}&HashIV={hash_iv}"
    return hashlib.sha256(raw.encode("utf-8")).hexdigest().upper()
```

```bash
# 安裝 pycryptodome
pip install pycryptodome
```

---

## 測試環境與測試帳號

```
測試環境 Base URL：https://ccore.newebpay.com

測試商家資訊：（需至測試平台自行申請）
  https://ccore.newebpay.com

測試信用卡：
  卡號：4000-2211-1111-1111
  到期：任何未來日期
  CVV ：任意三位數
```

---

## 金流服務

### 支援付款方式

| 付款方式 | 參數值 |
|---|---|
| 信用卡 | `CREDIT=1` |
| 信用卡分期 | `CREDITINST=3,6,12`（期數） |
| Google Pay | `ANDROIDPAY=1` |
| Samsung Pay | `SAMSUNGPAY=1` |
| ATM 虛擬帳號 | `VACC=1` |
| 超商代碼 | `CVS=1` |
| 超商條碼 | `BARCODE=1` |
| 超商取貨付款 | `CVSCOM=1` |

### 建立訂單（MPG 多功能付款）

```
POST https://ccore.newebpay.com/MPG/mpg_gateway
Content-Type: application/x-www-form-urlencoded
```

```python
import datetime
import urllib.parse


def build_mpg_params(merchant_id, hash_key, hash_iv, order_info: dict) -> dict:
    # 1. 組成交易參數字串
    trade_params = {
        "MerchantID"  : merchant_id,
        "RespondType" : "JSON",
        "TimeStamp"   : str(int(datetime.datetime.now().timestamp())),
        "Version"     : "2.0",
        "MerchantOrderNo": order_info["order_no"],
        "Amt"         : str(order_info["amount"]),
        "ItemDesc"    : order_info["desc"],
        "Email"       : order_info["email"],
        "ReturnURL"   : order_info["return_url"],
        "NotifyURL"   : order_info["notify_url"],
        "CREDIT"      : "1",
    }
    query_string = urllib.parse.urlencode(trade_params)

    # 2. AES 加密
    trade_info = aes_encrypt(query_string, hash_key, hash_iv)

    # 3. SHA256 驗證碼
    trade_sha = sha256_hash(trade_info, hash_key, hash_iv)

    return {
        "MerchantID" : merchant_id,
        "TradeInfo"  : trade_info,
        "TradeSha"   : trade_sha,
        "Version"    : "2.0",
    }
```

### 付款結果通知（NotifyURL）

```python
from flask import Flask, request
import urllib.parse

app = Flask(__name__)

HASH_KEY = "your_hash_key"
HASH_IV  = "your_hash_iv"


@app.route("/newebpay/notify", methods=["POST"])
def newebpay_notify():
    trade_info_hex = request.form.get("TradeInfo", "")
    trade_sha      = request.form.get("TradeSha", "")

    # 1. 驗證 TradeSha
    expected_sha = sha256_hash(trade_info_hex, HASH_KEY, HASH_IV)
    if trade_sha != expected_sha:
        return "FAIL", 200

    # 2. 解密 TradeInfo
    decrypted = aes_decrypt(trade_info_hex, HASH_KEY, HASH_IV)
    result = dict(urllib.parse.parse_qsl(decrypted))

    status = result.get("Status")
    order_no = result.get("MerchantOrderNo")

    if status == "SUCCESS":
        # 更新訂單狀態
        pass

    return "OK", 200
```

---

## 電子發票服務

### 開立發票

```
POST https://ccore.newebpay.com/API/invoice
Content-Type: application/x-www-form-urlencoded
```

```python
invoice_params = {
    "RespondType"    : "JSON",
    "Version"        : "1.5",
    "TimeStamp"      : str(int(datetime.datetime.now().timestamp())),
    "MerchantOrderNo": "INV" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
    "Status"         : "1",            # 1=立即開立, 0=預約開立
    "Category"       : "B2C",
    "BuyerEmail"     : "customer@example.com",
    "CarrierType"    : "",             # 空=不使用載具
    "TaxType"        : "1",            # 1=應稅
    "TaxRate"        : "5",
    "Amt"            : "95",           # 未稅金額
    "TaxAmt"         : "5",
    "TotalAmt"       : "100",
    "ItemName"       : "商品",
    "ItemCount"      : "1",
    "ItemUnit"       : "個",
    "ItemPrice"      : "100",
    "ItemAmt"        : "100",
}
```

---

## 錯誤處理

| Status | 說明 |
|---|---|
| `SUCCESS` | 成功 |
| `MPG020003` | 商店代號錯誤 |
| `MPG020019` | TradeSha 驗證失敗 |
| `MPG020028` | 訂單編號重複 |
| `MPG020038` | 金額格式錯誤 |

---

## 範例程式碼

```python
# Flask 完整串接範例
from flask import Flask, render_template_string, request
import datetime
import urllib.parse

app = Flask(__name__)

MERCHANT_ID = "your_merchant_id"
HASH_KEY    = "your_hash_key"
HASH_IV     = "your_hash_iv"
STAGE_URL   = "https://ccore.newebpay.com/MPG/mpg_gateway"


@app.route("/checkout")
def checkout():
    order_info = {
        "order_no"  : "ORD" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
        "amount"    : 100,
        "desc"      : "商品描述",
        "email"     : "user@example.com",
        "return_url": "https://yourdomain.com/newebpay/return",
        "notify_url": "https://yourdomain.com/newebpay/notify",
    }
    params = build_mpg_params(MERCHANT_ID, HASH_KEY, HASH_IV, order_info)

    inputs = "".join(f'<input type="hidden" name="{k}" value="{v}">' for k, v in params.items())
    html = f'''
        <form id="f" action="{STAGE_URL}" method="post">{inputs}</form>
        <script>document.getElementById("f").submit();</script>
    '''
    return render_template_string(html)
```
