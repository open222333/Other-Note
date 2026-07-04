# 財政部電子發票（直連）串接筆記

```
不透過 ECPay / EIVO 等三方服務，直接串接財政部電子發票整合服務平台
認證方式：AES-128 加密 + MerchantID（統一編號）
平台網址：https://www.einvoice.nat.gov.tw
```

## 目錄

- [財政部電子發票（直連）串接筆記](#財政部電子發票直連串接筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [三方服務 vs 直連比較](#三方服務-vs-直連比較)
  - [申請流程](#申請流程)
    - [資格申請](#資格申請)
    - [工商憑證申請](#工商憑證申請)
    - [字軌申請](#字軌申請)
  - [字軌管理](#字軌管理)
  - [認證方式](#認證方式)
  - [測試環境](#測試環境)
  - [主要 API](#主要-api)
    - [開立 B2C 發票](#開立-b2c-發票)
    - [作廢 B2C 發票](#作廢-b2c-發票)
    - [折讓 B2C 發票](#折讓-b2c-發票)
    - [開立 B2B 發票](#開立-b2b-發票)
    - [查詢發票](#查詢發票)
  - [載具類型](#載具類型)
  - [錯誤處理](#錯誤處理)
  - [範例程式碼](#範例程式碼)

---

## 參考資料

[財政部電子發票整合服務平台](https://www.einvoice.nat.gov.tw/)

[電子發票 API 規格書（MIG）](https://www.einvoice.nat.gov.tw/APMEMBERVAN/APIService/APIService)

[電子發票實施作業要點](https://www.einvoice.nat.gov.tw/home/DownLoad?fileName=1455079532718_.pdf)

[工商憑證管理中心（MOEACA）](https://moeaca.nat.gov.tw/)

[財政部電子申報繳稅服務網](https://efiling.tax.nat.gov.tw/)

---

## 三方服務 vs 直連比較

| 項目 | 直連財政部 | 透過三方（ECPay / EIVO） |
|---|---|---|
| 串接複雜度 | 高（需自管字軌、憑證） | 低（三方代管） |
| 字軌管理 | 自行向財政部申請，自行控管 | 由三方代為申請及補領 |
| 工商憑證 | 需自行申請並保管 | 不需要 |
| 費用 | 低（無每張發票手續費） | 有（每張約 1–2 元） |
| 維護成本 | 高（需追蹤字軌、API 版本） | 低（三方負責） |
| 適用情境 | 發票量大（月 > 數萬張）、需完全自主控制 | 一般中小型商家 |

> 月開立量超過約 **3 萬張** 後，直連節省的手續費才能覆蓋維護成本，否則建議使用三方服務。

---

## 申請流程

### 資格申請

1. 備妥公司統一編號、負責人資料
2. 至所轄**國稅局**申請「使用電子發票」資格
3. 取得**收銀機或POS系統核准文件**（視實際情況）
4. 至[財政部電子發票整合服務平台](https://www.einvoice.nat.gov.tw/)申請**加入會員**
5. 審核通過後取得：
   - `MerchantID`（即公司統一編號）
   - `API Key`（平台金鑰，用於 AES 加密）

### 工商憑證申請

B2B 發票（電子計算機統一發票）及部分 API 操作需要工商憑證簽章：

1. 至[工商憑證管理中心（MOEACA）](https://moeaca.nat.gov.tw/)申請
2. 備妥：統一編號、公司大小章、負責人身分證
3. 申請「法人憑證」，取得智慧卡或 `.p12` 憑證檔
4. 費用：約 700–1,400 元 / 年（依憑證類型）

### 字軌申請

電子發票號碼由「**兩碼字軌 + 八碼流水號**」組成（如 `AB12345678`）：

```
申請時間：每期開始前（奇數月 1 日）
申請方式：登入財政部電子發票平台 → 字軌管理 → 申請字軌
字軌有效期：當期（雙月，如 1–2 月為同一期）
未用完字軌：期末需辦理「作廢截止」
```

---

## 字軌管理

```python
import threading

class InvoiceNumberManager:
    """管理字軌號碼的使用與剩餘量"""

    def __init__(self, prefix: str, start: int, end: int):
        self.prefix  = prefix    # 兩碼字軌，如 "AB"
        self.current = start     # 當前流水號（整數）
        self.end     = end       # 字軌最大號
        self._lock   = threading.Lock()

    def next(self) -> str:
        with self._lock:
            if self.current > self.end:
                raise RuntimeError("字軌號碼已用完，請申請新字軌")
            no = f"{self.prefix}{self.current:08d}"
            self.current += 1
            return no

    def remaining(self) -> int:
        return self.end - self.current + 1


# 範例：字軌 AB00000001 ~ AB99999999
manager = InvoiceNumberManager("AB", 1, 99999999)
invoice_no = manager.next()   # "AB00000001"
```

> 字軌號碼應持久化至資料庫，重啟後從 DB 恢復，避免號碼重複。

---

## 認證方式

財政部 API 使用 **AES-128 CBC** 加密請求參數：

```python
import hashlib
import base64
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
import urllib.parse


def mof_aes_encrypt(data: str, api_key: str) -> str:
    """財政部 AES-128 加密（key 取前 16 bytes，IV 取後 16 bytes）"""
    key = api_key[:16].encode("utf-8")
    iv  = api_key[-16:].encode("utf-8")
    cipher = AES.new(key, AES.MODE_CBC, iv)
    encrypted = cipher.encrypt(pad(data.encode("utf-8"), AES.block_size))
    return base64.b64encode(encrypted).decode("utf-8")


def mof_aes_decrypt(encrypted_b64: str, api_key: str) -> str:
    key = api_key[:16].encode("utf-8")
    iv  = api_key[-16:].encode("utf-8")
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypted = cipher.decrypt(base64.b64decode(encrypted_b64))
    return unpad(decrypted, AES.block_size).decode("utf-8")
```

```bash
pip install pycryptodome
```

---

## 測試環境

```
測試平台：https://einvoice-stage.nat.gov.tw
（測試環境需向財政部申請開通，部分 API 可用模擬模式）

測試用 MerchantID：向平台申請時配發
測試用 API Key   ：向平台申請時配發
測試發票         ：不上傳至正式平台，不具法律效力
```

---

## 主要 API

所有 API 均為 `POST`，Content-Type: `application/x-www-form-urlencoded`

### 開立 B2C 發票

```
POST https://einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp
```

```python
import datetime
import urllib.parse
import requests

MOF_B2C_URL = "https://einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp"
MERCHANT_ID = "your_unified_business_number"   # 統一編號
API_KEY     = "your_api_key"                   # 平台金鑰（32碼）


def issue_b2c_invoice(
    order_no    : str,
    amount      : int,
    items       : list,
    buyer_email : str = "",
    carrier_type: str = "",
    carrier_num : str = "",
) -> dict:
    """
    items: [{"name": "商品", "count": 1, "unit": "個", "unit_price": 100, "amount": 100}]
    """
    invoice_no = manager.next()          # 從字軌管理器取號
    now        = datetime.datetime.now()

    params = {
        "version"           : "0.5",
        "type"              : "07",          # 07=一般稅額
        "taxType"           : "1",           # 1=應稅
        "taxRate"           : "5",
        "taxAmt"            : str(amount - int(amount / 1.05)),
        "amt"               : str(int(amount / 1.05)),
        "totalAmt"          : str(amount),
        "invNum"            : invoice_no,
        "invDate"           : now.strftime("%Y%m%d"),
        "invTime"           : now.strftime("%H%M%S"),
        "sellerID"          : MERCHANT_ID,
        "sellerName"        : "商店名稱",
        "sellerAddress"     : "商店地址",
        "sellerPhone"       : "0212345678",
        "buyerName"         : "",
        "buyerAddress"      : "",
        "buyerPhone"        : "",
        "buyerEmail"        : buyer_email,
        "carrierType"       : carrier_type,
        "carrierNum"        : carrier_num,
        "loveCode"          : "",            # 捐贈碼（與載具擇一）
        "printMark"         : "N",
        "invoiceRemark"     : "",
        "items"             : str(len(items)),
        "description1"      : items[0]["name"] if items else "",
        "quantity1"         : str(items[0]["count"]) if items else "1",
        "unit1"             : items[0]["unit"] if items else "個",
        "unitPrice1"        : str(items[0]["unit_price"]) if items else str(amount),
        "amt1"              : str(items[0]["amount"]) if items else str(amount),
    }

    # 依品項數量動態加入 description2, quantity2 ...
    for i, item in enumerate(items[1:], start=2):
        params[f"description{i}"] = item["name"]
        params[f"quantity{i}"]    = str(item["count"])
        params[f"unit{i}"]        = item["unit"]
        params[f"unitPrice{i}"]   = str(item["unit_price"])
        params[f"amt{i}"]         = str(item["amount"])

    query_string = urllib.parse.urlencode(params)
    encrypted    = mof_aes_encrypt(query_string, API_KEY)

    resp = requests.post(MOF_B2C_URL, data={
        "version"   : "0.5",
        "action"    : "issueInvoice",
        "id"        : MERCHANT_ID,
        "iBan"      : "",
        "appID"     : "INVOICE-APP-001",
        "encrypt"   : encrypted,
    })
    resp.raise_for_status()
    result = dict(urllib.parse.parse_qsl(mof_aes_decrypt(resp.text, API_KEY)))
    return result
```

### 作廢 B2C 發票

```
POST https://einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp
```

```python
def void_b2c_invoice(invoice_no: str, invoice_date: str, reason: str) -> dict:
    """
    invoice_date: YYYYMMDD
    """
    params = {
        "version"    : "0.5",
        "invNum"     : invoice_no,
        "invDate"    : invoice_date,
        "reason"     : reason,
        "sellerID"   : MERCHANT_ID,
    }
    encrypted = mof_aes_encrypt(urllib.parse.urlencode(params), API_KEY)
    resp = requests.post(MOF_B2C_URL, data={
        "version" : "0.5",
        "action"  : "cancelInvoice",
        "id"      : MERCHANT_ID,
        "appID"   : "INVOICE-APP-001",
        "encrypt" : encrypted,
    })
    resp.raise_for_status()
    return dict(urllib.parse.parse_qsl(mof_aes_decrypt(resp.text, API_KEY)))
```

### 折讓 B2C 發票

```python
def allowance_b2c_invoice(invoice_no: str, invoice_date: str, items: list) -> dict:
    params = {
        "version"        : "0.5",
        "invNum"         : invoice_no,
        "invDate"        : invoice_date,
        "sellerID"       : MERCHANT_ID,
        "allowanceAmt"   : str(sum(i["amount"] for i in items)),
        "items"          : str(len(items)),
        "description1"   : items[0]["name"],
        "quantity1"      : str(items[0]["count"]),
        "unitPrice1"     : str(items[0]["unit_price"]),
        "amt1"           : str(items[0]["amount"]),
    }
    encrypted = mof_aes_encrypt(urllib.parse.urlencode(params), API_KEY)
    resp = requests.post(MOF_B2C_URL, data={
        "version" : "0.5",
        "action"  : "allowanceInvoice",
        "id"      : MERCHANT_ID,
        "appID"   : "INVOICE-APP-001",
        "encrypt" : encrypted,
    })
    resp.raise_for_status()
    return dict(urllib.parse.parse_qsl(mof_aes_decrypt(resp.text, API_KEY)))
```

### 開立 B2B 發票

B2B（電子計算機統一發票）需額外傳送 XML 電文並以工商憑證簽章，流程較複雜：

```
1. 組成符合財政部 MIG 規格的 XML 電文
2. 使用工商憑證（.p12）對 XML 進行數位簽章
3. POST 至 B2B API 端點
4. 財政部回傳確認收執
```

```python
# B2B 端點（需工商憑證）
MOF_B2B_URL = "https://einvoice.nat.gov.tw/BIZB2CAPIVAN/invapp/InvApp"

# 工商憑證簽章（需搭配 OpenSSL 或 pyhanko）
# pip install pyhanko pyhanko-certvalidator
```

> B2B 電子發票涉及 XML 簽章，建議參考財政部 MIG 規格書（Message Implementation Guide），
> 或評估使用三方服務（ECPay / 藍新）代勞。

### 查詢發票

```python
MOF_QUERY_URL = "https://einvoice.nat.gov.tw/PB2CAPIVAN/invServ/InvServ"


def query_invoice(invoice_no: str, invoice_date: str) -> dict:
    params = {
        "version"  : "0.5",
        "type"     : "General",
        "invNum"   : invoice_no,
        "action"   : "qryInvDetail",
        "generation": "V2",
        "invDate"  : invoice_date,
        "uuid"     : "",
        "randomNumber": invoice_no[-4:],   # 發票末 4 碼隨機碼
    }
    encrypted = mof_aes_encrypt(urllib.parse.urlencode(params), API_KEY)
    resp = requests.post(MOF_QUERY_URL, data={
        "version" : "0.5",
        "action"  : "qryInvDetail",
        "id"      : MERCHANT_ID,
        "appID"   : "INVOICE-APP-001",
        "encrypt" : encrypted,
    })
    resp.raise_for_status()
    return dict(urllib.parse.parse_qsl(mof_aes_decrypt(resp.text, API_KEY)))
```

---

## 載具類型

| 載具 | carrierType | 說明 |
|---|---|---|
| 不使用（雲端） | `""` | 存入財政部雲端發票 |
| 手機條碼 | `3J0002` | 消費者手機載具，需驗證格式 `/XXXXXXX` |
| 自然人憑證 | `CQ0001` | 自然人憑證條碼（16碼） |
| 捐贈 | — | 不帶 carrierType，改帶 `loveCode`（3–7碼） |

```python
# 手機條碼格式驗證（/ 開頭 + 7碼英數）
import re
def is_valid_carrier(carrier_num: str) -> bool:
    return bool(re.fullmatch(r"/[A-Z0-9+\-.]{7}", carrier_num))
```

---

## 錯誤處理

| 回傳碼 | 說明 | 處理方式 |
|---|---|---|
| `200` | 成功 | — |
| `E001` | 身分驗證失敗 | 確認 MerchantID / API Key |
| `E002` | 加密格式錯誤 | 確認 AES key/iv 長度 |
| `E003` | 發票號碼重複 | 字軌管理有誤，不可重複使用號碼 |
| `E004` | 字軌已逾期 | 申請新字軌，舊字軌辦理截止 |
| `E005` | 金額格式錯誤 | 確認 `amt + taxAmt = totalAmt` |
| `E006` | 手機條碼格式錯誤 | 需驗證 `/XXXXXXX` 格式 |
| `E009` | 發票已作廢 | 不可重複作廢 |

```python
def call_mof_api(action: str, params: dict) -> dict:
    encrypted = mof_aes_encrypt(urllib.parse.urlencode(params), API_KEY)
    resp = requests.post(MOF_B2C_URL, data={
        "version": "0.5",
        "action" : action,
        "id"     : MERCHANT_ID,
        "appID"  : "INVOICE-APP-001",
        "encrypt": encrypted,
    })
    resp.raise_for_status()
    result = dict(urllib.parse.parse_qsl(mof_aes_decrypt(resp.text, API_KEY)))

    if result.get("code") != "200":
        raise RuntimeError(f"財政部 API 錯誤：{result.get('code')} {result.get('msg')}")

    return result
```

---

## 範例程式碼

```python
# Flask 整合範例：付款完成後自動開立財政部電子發票
from flask import Flask, request

app = Flask(__name__)


@app.route("/payment/callback", methods=["POST"])
def payment_callback():
    data = request.form.to_dict()

    # 驗簽（依金流平台不同）
    if data.get("RtnCode") != "1":
        return "1|OK", 200

    order_no    = data["MerchantTradeNo"]
    amount      = int(data["TradeAmt"])
    buyer_email = data.get("Email", "")

    try:
        result = issue_b2c_invoice(
            order_no    = order_no,
            amount      = amount,
            items       = [{"name": "商品", "count": 1, "unit": "個",
                            "unit_price": amount, "amount": amount}],
            buyer_email = buyer_email,
        )
        invoice_no = result.get("invoiceNumber")
        # 儲存 invoice_no 至訂單資料庫
    except RuntimeError as e:
        # 開立失敗：記錄 log，排程補開
        app.logger.error(f"發票開立失敗：{e}，訂單：{order_no}")

    return "1|OK", 200
```

```python
# 字軌剩餘量監控（排程每日執行）
def check_invoice_quota():
    remaining = manager.remaining()
    if remaining < 1000:
        # 發送告警通知，提醒人工至財政部申請新字軌
        send_alert(f"字軌剩餘量不足：{remaining} 張")
```

> **注意事項**
>
> - 字軌號碼一旦使用不可重複，需用資料庫鎖（`SELECT FOR UPDATE`）確保併發安全
> - 每期（奇數月）需主動申請下期字軌，建議在期末前 2 週申請
> - 作廢的發票號碼視同已使用，不可再開立
> - 正式環境與測試環境 API Key 不同，務必以環境變數管理
