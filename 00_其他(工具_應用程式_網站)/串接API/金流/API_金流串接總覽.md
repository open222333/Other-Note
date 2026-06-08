# 金流 API 串接總覽

```
彙整金流與電子發票 API 串接筆記：ECPay 綠界、NewebPay 藍新、allPay 歐付寶、EIVO 聚財資訊
金流認證：MerchantID + HashKey + HashIV → CheckMacValue / AES+SHA256
發票認證：同金流 or Bearer Token（EIVO）
```

## 目錄

- [金流 API 串接總覽](#金流-api-串接總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [平台比較](#平台比較)
  - [基本資訊](#基本資訊)
  - [費用](#費用)
  - [付款方式支援](#付款方式支援)
- [電子發票合規說明](#電子發票合規說明)
  - [使用第三方服務 vs 自行串接財政部](#使用第三方服務-vs-自行串接財政部)
  - [第三方服務選擇](#第三方服務選擇)
  - [POS 場景建議](#pos-場景建議)
- [共用概念](#共用概念)
  - [CheckMacValue 驗簽（ECPay / allPay 通用）](#checkmacvalue-驗簽ecpay--allpay-通用)
  - [NewebPay AES256 加密流程](#newebpay-aes256-加密流程)
  - [Webhook 回傳格式](#webhook-回傳格式)
  - [環境切換](#環境切換)
- [ECPay 綠界](#ecpay-綠界)
  - [測試帳號](#測試帳號)
  - [主要端點](#主要端點)
  - [建立訂單參數](#建立訂單參數)
  - [ReturnURL 處理](#returnurl-處理)
- [NewebPay 藍新](#newebpay-藍新)
  - [測試環境](#測試環境)
  - [主要端點](#主要端點-1)
  - [建立訂單參數](#建立訂單參數-1)
  - [NotifyURL 處理](#notifyurl-處理)
- [allPay 歐付寶](#allpay-歐付寶)
  - [測試環境](#測試環境-1)
  - [主要端點](#主要端點-2)
  - [與 ECPay 差異](#與-ecpay-差異)
- [EIVO 聚財資訊（純發票）](#eivo-聚財資訊純發票)
  - [認證](#認證)
  - [主要端點](#主要端點-3)
  - [載具類型](#載具類型)

---

## 參考資料

[ECPay 開發者中心](https://developers.ecpay.com.tw/)

[NewebPay 開發者中心](https://developer.newebpay.com/)

[歐付寶開發者中心](https://www.opay.tw/)

[聚財資訊官網](https://www.eivo.com.tw/)

[財政部電子發票整合服務平台](https://www.einvoice.nat.gov.tw/)

---

# 平台比較

## 基本資訊

| 項目 | ECPay 綠界 | NewebPay 藍新 | allPay 歐付寶 | EIVO 聚財 |
|---|---|---|---|---|
| 服務範疇 | 金流 + 電子發票 | 金流 + 電子發票 | 金流（發票較不完整） | 純電子發票 |
| 認證核心 | SHA256 CheckMacValue | AES256 + SHA256 | SHA256（同 ECPay） | Bearer Token |
| 正式環境 | `payment.ecpay.com.tw` | `core.newebpay.com` | `payment.opay.tw` | `api.eivo.com.tw` |
| 測試環境 | `payment-stage.ecpay.com.tw` | `ccore.newebpay.com` | `payment-stage.opay.tw` | — |
| 測試帳號 | 提供固定帳號 | 需自行申請 | 提供固定帳號 | — |
| 測試信用卡 | 4311-9522-2222-2222 | 4000-2211-1111-1111 | 4311-9522-2222-2222 | — |
| 額外依賴套件 | 無 | `pycryptodome` | 無 | 無 |
| 市佔率 | 高 | 中 | 低 | — |

## 費用

> 費率依簽約方案與月交易量而異，以下為一般商家參考值，實際費率請向各平台業務確認。

### 金流手續費

| 付款方式 | ECPay 綠界 | NewebPay 藍新 | allPay 歐付寶 |
|---|---|---|---|
| 信用卡（一次付清） | 約 2.75% | 約 2.75% | 約 2.75–3% |
| 信用卡（分期） | 約 1.65–2% | 約 1.65–2% | 約 1.65–2% |
| ATM 虛擬帳號 | 約 10–15 元/筆 | 約 10–15 元/筆 | 約 10–15 元/筆 |
| 超商代碼 | 約 25–30 元/筆 | 約 25–35 元/筆 | 約 25–30 元/筆 |
| 超商條碼 | 約 25–30 元/筆 | 約 25–35 元/筆 | 約 25–30 元/筆 |
| 月費 | 視方案（部分免月費） | 視方案 | 視方案 |

### 電子發票費用

| 服務 | 費用 | 說明 |
|---|---|---|
| ECPay 電子發票 | 約 0.5–1 元/張 | 整合於 ECPay 帳戶，無需另外開戶 |
| NewebPay 電子發票 | 約 1–2 元/張 | 整合於藍新帳戶 |
| allPay 電子發票 | 視方案 | 功能較不完整 |
| EIVO 聚財資訊 | 約 0.5–2 元/張 | 純發票服務，依月用量分級定價 |
| 財政部直連 | 無手續費 | 需自行開發維護，人力成本高 |

```
損益試算（以電子發票為例）：
月開立 1,000 張 × 1 元 = 月費 1,000 元
月開立 30,000 張 × 1 元 = 月費 30,000 元
→ 超過約 3 萬張/月，才值得評估財政部直連自建
```

## 付款方式支援

| 付款方式 | ECPay | NewebPay | allPay |
|---|---|---|---|
| 信用卡（一次） | `Credit` | `CREDIT=1` | `Credit` |
| 信用卡（分期） | `Credit`+`CreditInstallment` | `CREDITINST=3,6,12` | `Credit` |
| ATM 虛擬帳號 | `ATM` | `VACC=1` | `ATM` |
| 超商代碼 | `CVS` | `CVS=1` | `CVS` |
| 超商條碼 | `BARCODE` | `BARCODE=1` | `BARCODE` |
| Google Pay | — | `ANDROIDPAY=1` | — |
| 超商取貨付款 | — | `CVSCOM=1` | — |
| 全通路 | `ALL` | — | `ALL` |

---

# 電子發票合規說明

台灣電子發票受財政部管制，每張發票須上傳至**財政部電子發票整合服務平台**才具法律效力，不是「自己印一張」就算數。

## 使用第三方服務 vs 自行串接財政部

| 工作項目 | 說明 |
|---|---|
| 申請 MIG 接入資格 | 需向財政部申請成為「營業人自行開立」，審核流程繁瑣 |
| 字軌號碼管理 | 財政部每兩個月配號（例：`AB-12345678`），自行管理發號序列與防重複 |
| 上傳 C0401 XML | 每張發票在開立後規定時間內以 XML 格式上傳至平台 |
| 載具整合 | 手機條碼（`3J0002`）、自然人憑證（`CQ0001`）、愛心碼各需對接驗證 API |
| 作廢 / 折讓 | C0501（作廢）、C0401 折讓格式，需個別上傳 |
| 雙月申報 | 每兩個月整批上傳發票，對帳報表須自行維護 |
| 維護成本 | 財政部改版 API 需自行跟進 |

> 自行串接工作量約 **2～4 個月**，且有持續維護成本；除非交易量極大或有特殊合規需求，否則不划算。

## 第三方服務選擇

| 方案 | 適用情境 |
|---|---|
| 綠界（ECPay） | 金流 + 電子發票一站整合，市佔最高、文件完整 |
| 藍新（NewebPay） | 金流 + 電子發票，Google Pay 支援較好 |
| 歐付寶（allPay） | 串接邏輯與 ECPay 幾乎相同，可直接移植 |
| EIVO 聚財資訊 | 純發票需求、已有其他金流方案時使用 |
| 直接串財政部 | 月交易量極大，有專職後端工程師維護，或有特殊合規需求 |

## POS 場景建議

| 情境 | 建議做法 |
|---|---|
| 月營業額 < 20 萬（小規模免開統一發票） | 只需列印收據，不需串任何發票 API |
| 一般零售 POS | 串綠界 / 藍新電子發票 API，結帳後自動開立 B2C 雲端發票 |
| 需熱感機列印發票 | 雲端發票開立後，透過 ESC/POS 印出發票號碼 + QR 條碼供消費者對獎 |
| 需支援載具歸戶 | 結帳時收集手機條碼 / 自然人憑證，帶入 `CarrierType` + `CarrierNum` 欄位 |

---

# 共用概念

## CheckMacValue 驗簽（ECPay / allPay 通用）

```python
import hashlib
import urllib.parse


def generate_check_mac_value(params: dict, hash_key: str, hash_iv: str) -> str:
    sorted_params = sorted(params.items(), key=lambda x: x[0].lower())
    raw = "&".join(f"{k}={v}" for k, v in sorted_params)
    raw = f"HashKey={hash_key}&{raw}&HashIV={hash_iv}"
    encoded = urllib.parse.quote_plus(raw).lower()
    return hashlib.sha256(encoded.encode("utf-8")).hexdigest().upper()


def verify_check_mac_value(params: dict, hash_key: str, hash_iv: str) -> bool:
    received = params.pop("CheckMacValue", "")
    expected = generate_check_mac_value(params, hash_key, hash_iv)
    return received == expected
```

## NewebPay AES256 加密流程

```bash
pip install pycryptodome
```

```python
import hashlib
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad


def aes_encrypt(data: str, hash_key: str, hash_iv: str) -> str:
    cipher = AES.new(hash_key.encode(), AES.MODE_CBC, hash_iv.encode())
    return cipher.encrypt(pad(data.encode(), AES.block_size)).hex()


def aes_decrypt(encrypted_hex: str, hash_key: str, hash_iv: str) -> str:
    cipher = AES.new(hash_key.encode(), AES.MODE_CBC, hash_iv.encode())
    return unpad(cipher.decrypt(bytes.fromhex(encrypted_hex)), AES.block_size).decode()


def sha256_trade_sha(encrypted_data: str, hash_key: str, hash_iv: str) -> str:
    raw = f"HashKey={hash_key}&{encrypted_data}&HashIV={hash_iv}"
    return hashlib.sha256(raw.encode()).hexdigest().upper()
```

```
流程：交易參數 → urlencode → AES256(CBC) → hex → TradeInfo
      TradeInfo → sha256_trade_sha → TradeSha
```

## Webhook 回傳格式

| 平台 | 必須回傳 | 說明 |
|---|---|---|
| ECPay / allPay | `1\|OK` | 否則持續重送 |
| NewebPay | `OK` | 純文字 |
| EIVO | HTTP 200 | 無特定格式要求 |

## 環境切換

```python
import os

ENV = os.getenv("APP_ENV", "staging")

URLS = {
    "ecpay"   : {"staging": "https://payment-stage.ecpay.com.tw",  "production": "https://payment.ecpay.com.tw"},
    "newebpay": {"staging": "https://ccore.newebpay.com",           "production": "https://core.newebpay.com"},
    "opay"    : {"staging": "https://payment-stage.opay.tw",        "production": "https://payment.opay.tw"},
}
```

---

# ECPay 綠界

## 測試帳號

```
MerchantID : 2000132
HashKey    : 5294y06JbISpM5x9
HashIV     : v77hoKGq4kWxNNIS
信用卡     : 4311-9522-2222-2222（任意未來到期日，任意 CVV）
```

## 主要端點

| 功能 | 方法 | Endpoint |
|---|---|---|
| 前台付款 | `POST` | `/Cashier/AioCheckOut/V5` |
| 付款通知（非同步） | Webhook | ReturnURL |
| 前台跳轉（同步） | Redirect | OrderResultURL |
| 開立發票 | `POST` | `/B2CInvoice/Issue` |
| 作廢發票 | `POST` | `/B2CInvoice/Invalid` |

## 建立訂單參數

```python
import datetime

params = {
    "MerchantID"        : "2000132",
    "MerchantTradeNo"   : "ORD" + datetime.datetime.now().strftime("%Y%m%d%H%M%S"),
    "MerchantTradeDate" : datetime.datetime.now().strftime("%Y/%m/%d %H:%M:%S"),
    "PaymentType"       : "aio",
    "TotalAmount"       : "100",
    "TradeDesc"         : "商品描述",
    "ItemName"          : "商品 x1",
    "ReturnURL"         : "https://yourdomain.com/ecpay/return",
    "OrderResultURL"    : "https://yourdomain.com/ecpay/result",
    "ChoosePayment"     : "ALL",
    "EncryptType"       : "1",
}
params["CheckMacValue"] = generate_check_mac_value(params, "5294y06JbISpM5x9", "v77hoKGq4kWxNNIS")
# 將 params 以 HTML form POST 方式送出（前台導向）
```

## ReturnURL 處理

```python
from flask import Flask, request

app = Flask(__name__)

@app.route("/ecpay/return", methods=["POST"])
def ecpay_return():
    data = request.form.to_dict()
    if not verify_check_mac_value(data.copy(), "5294y06JbISpM5x9", "v77hoKGq4kWxNNIS"):
        return "0|Error", 200
    if data.get("RtnCode") == "1":
        pass   # 付款成功，更新訂單
    return "1|OK", 200
```

---

# NewebPay 藍新

## 測試環境

```
Base URL   : https://ccore.newebpay.com
信用卡     : 4000-2211-1111-1111（任意未來到期日，任意 CVV）
測試帳號   : 需至 https://ccore.newebpay.com 自行申請
```

## 主要端點

| 功能 | 方法 | Endpoint |
|---|---|---|
| 前台付款（MPG） | `POST` | `/MPG/mpg_gateway` |
| 付款通知（非同步） | Webhook | NotifyURL（需解密 TradeInfo） |
| 前台跳轉（同步） | Redirect | ReturnURL |
| 開立發票 | `POST` | `/API/invoice` |

## 建立訂單參數

```python
import datetime
import urllib.parse

def build_mpg_params(merchant_id, hash_key, hash_iv, order_info: dict) -> dict:
    trade_params = {
        "MerchantID"     : merchant_id,
        "RespondType"    : "JSON",
        "TimeStamp"      : str(int(datetime.datetime.now().timestamp())),
        "Version"        : "2.0",
        "MerchantOrderNo": order_info["order_no"],
        "Amt"            : str(order_info["amount"]),
        "ItemDesc"       : order_info["desc"],
        "Email"          : order_info["email"],
        "ReturnURL"      : order_info["return_url"],
        "NotifyURL"      : order_info["notify_url"],
        "CREDIT"         : "1",
    }
    trade_info = aes_encrypt(urllib.parse.urlencode(trade_params), hash_key, hash_iv)
    trade_sha  = sha256_trade_sha(trade_info, hash_key, hash_iv)
    return {"MerchantID": merchant_id, "TradeInfo": trade_info, "TradeSha": trade_sha, "Version": "2.0"}
```

## NotifyURL 處理

```python
@app.route("/newebpay/notify", methods=["POST"])
def newebpay_notify():
    trade_info_hex = request.form.get("TradeInfo", "")
    trade_sha      = request.form.get("TradeSha", "")

    if sha256_trade_sha(trade_info_hex, HASH_KEY, HASH_IV) != trade_sha:
        return "FAIL", 200

    result   = dict(urllib.parse.parse_qsl(aes_decrypt(trade_info_hex, HASH_KEY, HASH_IV)))
    order_no = result.get("MerchantOrderNo")

    if result.get("Status") == "SUCCESS":
        pass   # 付款成功，更新訂單
    return "OK", 200
```

---

# allPay 歐付寶

## 測試環境

```
Base URL   : https://payment-stage.opay.tw
信用卡     : 4311-9522-2222-2222（同 ECPay）
```

## 主要端點

| 功能 | 方法 | Endpoint |
|---|---|---|
| 前台付款 | `POST` | `/Cashier/AioCheckOut/V5` |
| 付款通知（非同步） | Webhook | ReturnURL（同 ECPay 處理方式） |

## 與 ECPay 差異

| 項目 | allPay 歐付寶 | ECPay 綠界 |
|---|---|---|
| 驗簽函式 | 完全相同，可直接複用 | — |
| API 路徑 | `/Cashier/AioCheckOut/V5`（相同） | — |
| Base URL | `payment-stage.opay.tw` / `payment.opay.tw` | `payment-stage.ecpay.com.tw` / `payment.ecpay.com.tw` |
| 電子發票 | 較不完整 | 原生整合，功能完整 |
| 回傳格式 | `1\|OK` （相同） | `1\|OK` |

> 串接邏輯幾乎完全相同，替換 Base URL 與商家帳號即可從 ECPay 移植。

---

# EIVO 聚財資訊（純發票）

## 認證

```python
import requests

EIVO_API_URL = "https://api.eivo.com.tw"
API_KEY      = "your_api_key"


def get_headers() -> dict:
    return {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type" : "application/json",
    }
```

## 主要端點

| 功能 | 方法 | Endpoint |
|---|---|---|
| 開立 B2C 發票 | `POST` | `/invoice/issue` |
| 開立 B2B 發票 | `POST` | `/invoice/issue`（帶 `BuyerIdentifier`） |
| 作廢發票 | `POST` | `/invoice/void` |
| 折讓發票 | `POST` | `/invoice/allowance` |
| 查詢發票 | `GET` | `/invoice/query` |

```python
# 開立 B2C 發票（最小範例）
payload = {
    "MerchantOrderNo": order_no,
    "Status"         : "1",          # 1=立即開立
    "Category"       : "B2C",
    "BuyerEmail"     : buyer_email,
    "TaxType"        : "1",          # 1=應稅
    "TaxRate"        : 5,
    "Amt"            : int(amount / 1.05),
    "TaxAmt"         : amount - int(amount / 1.05),
    "TotalAmt"       : amount,
    "Items"          : [{"ItemName": "商品", "ItemCount": 1, "ItemWord": "個",
                         "ItemPrice": amount, "ItemAmt": amount}],
}
resp = requests.post(f"{EIVO_API_URL}/invoice/issue", json=payload, headers=get_headers())
invoice_no = resp.json()["InvoiceNo"]
```

## 載具類型

| 載具 | CarrierType | CarrierNum |
|---|---|---|
| 不使用 | `""` | — |
| 手機條碼 | `3J0002` | `/ABC1234`（消費者條碼） |
| 自然人憑證 | `CQ0001` | 憑證條碼 |
| 會員載具 | `1` | 平台會員 ID |
