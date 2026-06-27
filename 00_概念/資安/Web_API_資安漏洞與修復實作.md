# Web API 資安漏洞與修復實作

```
常見 Web / REST API 資安漏洞分類、風險說明與 Flask 修復範例
涵蓋：暴力破解、JWT 洩漏、XSS Token 竊取、CSRF、注入、IDOR、敏感資料暴露
      Function Level 授權、密碼儲存、Timing Attack、業務邏輯、SSRF
      依賴套件 CVE、Insecure Deserialization、CORS、API 版本管理
      ReDoS（正規表達式阻斷服務）、Race Condition / TOCTOU、Python 設定陷阱
```

## 目錄

- [Web API 資安漏洞與修復實作](#web-api-資安漏洞與修復實作)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [風險等級總覽](#風險等級總覽)
- [H1 無 Rate Limiting — 暴力破解](#h1-無-rate-limiting--暴力破解)
- [H2 機密設定進版控 — JWT 偽造](#h2-機密設定進版控--jwt-偽造)
- [H3 Refresh Token 存 localStorage — XSS 竊取](#h3-refresh-token-存-localstorage--xss-竊取)
- [H4 無 CSRF 保護 — 跨站請求偽造](#h4-無-csrf-保護--跨站請求偽造)
- [其他常見漏洞](#其他常見漏洞)
  - [H5 SQL Injection — 資料庫注入](#h5-sql-injection--資料庫注入)
  - [H6 XSS — 跨站腳本注入](#h6-xss--跨站腳本注入)
  - [H7 IDOR — 不安全的直接物件參考](#h7-idor--不安全的直接物件參考)
  - [H8 敏感資料暴露 — 回應洩漏內部資訊](#h8-敏感資料暴露--回應洩漏內部資訊)
  - [H9 JWT Algorithm Confusion — 演算法混淆攻擊](#h9-jwt-algorithm-confusion--演算法混淆攻擊)
  - [H10 不限制檔案上傳 — 惡意檔案執行](#h10-不限制檔案上傳--惡意檔案執行)
  - [H11 Mass Assignment — 批次欄位覆寫](#h11-mass-assignment--批次欄位覆寫)
  - [H12 安全 HTTP Headers 缺失](#h12-安全-http-headers-缺失)
  - [H13 Broken Function Level Authorization — 功能層級授權缺失](#h13-broken-function-level-authorization--功能層級授權缺失)
  - [H14 密碼儲存不安全 — MD5 / SHA1](#h14-密碼儲存不安全--md5--sha1)
  - [H15 Timing Attack — 計時攻擊](#h15-timing-attack--計時攻擊)
  - [H16 Business Logic Vulnerability — 業務邏輯漏洞](#h16-business-logic-vulnerability--業務邏輯漏洞)
  - [H17 SSRF — 伺服器端請求偽造](#h17-ssrf--伺服器端請求偽造)
  - [H18 Outdated Dependencies — 套件 CVE 漏洞](#h18-outdated-dependencies--套件-cve-漏洞)
  - [H19 Insecure Deserialization — 不安全的反序列化](#h19-insecure-deserialization--不安全的反序列化)
  - [H20 無 Security Logging / Monitoring](#h20-無-security-logging--monitoring)
  - [H21 CORS 設定過寬](#h21-cors-設定過寬)
  - [H22 API 版本洩漏 / 過時端點](#h22-api-版本洩漏--過時端點)
  - [H23 ReDoS — 正規表達式阻斷服務](#h23-redos--正規表達式阻斷服務)
  - [H24 Race Condition / TOCTOU — 並發競爭條件](#h24-race-condition--toctou--並發競爭條件)
  - [H25 Python 設定陷阱 — 尾端逗號 Tuple](#h25-python-設定陷阱--尾端逗號-tuple)

## 參考資料

[OWASP Top 10](https://owasp.org/www-project-top-ten/)

[OWASP API Security Top 10](https://owasp.org/www-project-api-security/)

[Flask-Limiter 文件](https://flask-limiter.readthedocs.io/)

[Flask-WTF CSRF 文件](https://flask-wtf.readthedocs.io/en/stable/csrf/)

---

# 風險等級總覽

| 編號 | 漏洞 | OWASP 對應 | 風險 | 修復難度 |
|---|---|---|---|---|
| H1 | 無 Rate Limiting（登入/Refresh） | API4: Unrestricted Resource Consumption | 帳號被暴力破解 | ★★☆ |
| H2 | SECRET_KEY / 設定檔進版控 | A02: Cryptographic Failures | JWT 可偽造，任意身份 | ★☆☆ |
| H3 | Refresh Token 存 localStorage | A02: Cryptographic Failures | XSS 可完整竊取 | ★★☆ |
| H4 | 無 CSRF 保護 | A01: Broken Access Control | 跨站操作使用者帳號 | ★★☆ |
| H5 | SQL Injection | A03: Injection | 資料庫任意讀寫/刪除 | ★☆☆ |
| H6 | XSS | A03: Injection | 竊取 Cookie/Token、執行任意 JS | ★★☆ |
| H7 | IDOR | A01: Broken Access Control | 存取他人資料 | ★★☆ |
| H8 | 敏感資料暴露 | A02: Cryptographic Failures | Stack Trace / 密碼外洩 | ★☆☆ |
| H9 | JWT Algorithm Confusion | A02: Cryptographic Failures | 偽造任意 Token | ★☆☆ |
| H10 | 不限制檔案上傳 | A03: Injection | 上傳後執行惡意程式 | ★★☆ |
| H11 | Mass Assignment | A01: Broken Access Control | 竄改 role/is_admin 等敏感欄位 | ★☆☆ |
| H12 | 安全 Header 缺失 | A05: Security Misconfiguration | 協助其他攻擊向量 | ★☆☆ |
| H13 | Broken Function Level Authorization | A01: Broken Access Control | 一般用戶呼叫管理端點 | ★★☆ |
| H14 | 密碼儲存不安全（MD5/SHA1） | A02: Cryptographic Failures | 資料庫外洩後密碼可被反查 | ★☆☆ |
| H15 | Timing Attack | A02: Cryptographic Failures | 推測 Token / 密碼正確性 | ★★☆ |
| H16 | Business Logic Vulnerability | A01: Broken Access Control | 繞過金額/庫存/優惠邏輯 | ★★★ |
| H17 | SSRF | A10: Server-Side Request Forgery | 存取內網服務 / Cloud Metadata | ★★★ |
| H18 | Outdated Dependencies（CVE） | A06: Vulnerable Components | 已知漏洞被自動化工具掃描利用 | ★★☆ |
| H19 | Insecure Deserialization | A08: Data Integrity Failures | 任意程式碼執行（RCE） | ★★★ |
| H20 | 無 Security Logging / Monitoring | A09: Logging Failures | 攻擊發生後無法追溯 | ★☆☆ |
| H21 | CORS 設定過寬 | A05: Security Misconfiguration | 跨域請求攜帶 Cookie 存取 API | ★★☆ |
| H22 | API 版本洩漏 / 過時端點 | A09: Security Misconfiguration | 舊版端點繞過新版安全機制 | ★★☆ |
| H23 | ReDoS（正規表達式阻斷服務） | A03: Injection | 惡意輸入讓正規表達式回溯爆炸，CPU 100%，API 不可用 | ★☆☆ |
| H24 | Race Condition / TOCTOU | A01: Broken Access Control | 並發請求繞過庫存/狀態機驗證，造成超賣或重複操作 | ★★★ |
| H25 | Python 設定陷阱（Tuple / 尾端逗號） | A05: Security Misconfiguration | `DEBUG = (False,)` 永遠為 True，生產環境開啟 debug | ★☆☆ |

---

# H1 無 Rate Limiting — 暴力破解

**問題**：`/login`、`/refresh` 無請求頻率限制，攻擊者可無限嘗試密碼組合或大量刷新 Token。

**修復**：使用 `flask-limiter` 搭配 Redis 進行 IP + 帳號雙層限速。

```bash
pip install flask-limiter redis
```

```python
# extensions.py
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address

limiter = Limiter(
    key_func=get_remote_address,
    storage_uri="redis://localhost:6379",   # 分散式環境必須用 Redis，否則重啟後計數重置
    default_limits=["200 per day", "50 per hour"],
)
```

```python
# app.py
from extensions import limiter

limiter.init_app(app)
```

```python
# auth/routes.py
from extensions import limiter
from flask_limiter.util import get_remote_address

def get_login_key():
    """IP + username 雙重 key，避免同一帳號從多 IP 暴力破解"""
    username = request.json.get("username", "unknown")
    return f"{get_remote_address()}:{username}"


@auth_bp.route("/login", methods=["POST"])
@limiter.limit("5 per minute", key_func=get_login_key)   # 同帳號同 IP：5次/分鐘
@limiter.limit("20 per hour", key_func=get_remote_address)  # 同 IP 總量
def login():
    ...


@auth_bp.route("/refresh", methods=["POST"])
@limiter.limit("10 per minute")
def refresh():
    ...
```

```python
# 自訂超限回應（預設 429 Too Many Requests）
@app.errorhandler(429)
def ratelimit_handler(e):
    return {"error": "請求過於頻繁，請稍後再試", "retry_after": e.description}, 429
```

```
進階做法：
- 連續失敗 N 次後暫時鎖定帳號（寫入 Redis，key = f"lockout:{username}"）
- 搭配 captcha（第 3 次失敗後要求驗證碼）
- 異常 IP 自動加入黑名單
```

---

# H2 機密設定進版控 — JWT 偽造

**問題**：`conf/flask.json` 含 `SECRET_KEY` 且未加入 `.gitignore`，任何有 git 存取權的人可用此 Key 偽造任意使用者的 JWT。

**修復步驟**

**① 立即從 git history 移除（若已 commit）**

```bash
# 安裝 git-filter-repo
pip install git-filter-repo

# 從所有 commit history 中刪除敏感檔案
git filter-repo --path conf/flask.json --invert-paths

# 強制推送（會改寫 history，通知所有協作者重新 clone）
git push origin --force --all
```

**② 改用環境變數管理機密**

```bash
pip install python-dotenv
```

```ini
# .env（本地開發，絕不 commit）
SECRET_KEY=your-very-long-random-secret-key-here
JWT_SECRET_KEY=another-random-key
DATABASE_URL=postgresql://user:pass@localhost/db
```

```python
# config.py
import os
from dotenv import load_dotenv

load_dotenv()   # 載入 .env

class Config:
    SECRET_KEY     = os.environ["SECRET_KEY"]       # 不存在時直接 KeyError，快速失敗
    JWT_SECRET_KEY = os.environ["JWT_SECRET_KEY"]
    DATABASE_URL   = os.environ.get("DATABASE_URL", "sqlite:///dev.db")

    @classmethod
    def validate(cls):
        required = ["SECRET_KEY", "JWT_SECRET_KEY"]
        missing  = [k for k in required if not os.environ.get(k)]
        if missing:
            raise RuntimeError(f"缺少必要環境變數: {missing}")
```

**③ .gitignore 必加項目**

```gitignore
# 環境設定
.env
.env.*
!.env.example          # 範例檔可 commit

# 機密設定
conf/flask.json
conf/secrets.json
*.pem
*.key
*.p12

# Python
__pycache__/
*.pyc
.venv/
```

```ini
# .env.example（commit 給其他開發者參考，不含真實值）
SECRET_KEY=replace-with-random-string
JWT_SECRET_KEY=replace-with-another-random-string
DATABASE_URL=postgresql://user:pass@localhost/mydb
```

**④ 產生高強度 SECRET_KEY**

```python
import secrets
print(secrets.token_hex(64))   # 128 字元 hex 字串
```

---

# H3 Refresh Token 存 localStorage — XSS 竊取

**問題**：`localStorage` 可被任意 JavaScript 讀取，頁面上任何 XSS 漏洞都能竊取 Refresh Token，進而長期維持存取。

**正確做法：Token 分層儲存**

| Token | 儲存位置 | 說明 |
|---|---|---|
| Access Token（短效，15分鐘） | JS 記憶體（變數） | 頁面重整後消失，需重新 refresh |
| Refresh Token（長效，7天） | HttpOnly Cookie | JS 無法讀取，只有瀏覽器自動帶上 |

**後端：設定 HttpOnly Cookie**

```python
from datetime import timedelta
from flask import make_response
from flask_jwt_extended import create_access_token, create_refresh_token


@auth_bp.route("/login", methods=["POST"])
def login():
    # ... 驗證帳密 ...

    access_token  = create_access_token(identity=user_id, expires_delta=timedelta(minutes=15))
    refresh_token = create_refresh_token(identity=user_id, expires_delta=timedelta(days=7))

    response = make_response({"access_token": access_token})   # Access Token 放 body
    response.set_cookie(
        "refresh_token",
        refresh_token,
        httponly=True,          # JS 無法讀取
        secure=True,            # 僅 HTTPS 傳送（prod 必開）
        samesite="Strict",      # 禁止跨站請求攜帶 Cookie
        max_age=60 * 60 * 24 * 7,
        path="/auth/refresh",   # 限縮 Cookie 只在 refresh 端點有效
    )
    return response


@auth_bp.route("/refresh", methods=["POST"])
def refresh():
    refresh_token = request.cookies.get("refresh_token")
    if not refresh_token:
        return {"error": "Missing refresh token"}, 401

    # 驗證後發新 access token
    data = decode_token(refresh_token)
    new_access_token = create_access_token(identity=data["sub"])
    return {"access_token": new_access_token}


@auth_bp.route("/logout", methods=["POST"])
def logout():
    response = make_response({"message": "logged out"})
    response.delete_cookie("refresh_token", path="/auth/refresh")
    # 同時將 refresh token 加入黑名單（Redis）
    jti = get_jwt()["jti"]
    redis_client.setex(f"revoked:{jti}", timedelta(days=7), "1")
    return response
```

**前端：Access Token 存記憶體**

```javascript
// auth.js — Access Token 存在 module 變數，不碰 localStorage
let _accessToken = null;

export function setAccessToken(token) { _accessToken = token; }
export function getAccessToken()      { return _accessToken; }

// 頁面重整後透過 silent refresh 取得新 token
async function silentRefresh() {
    const resp = await fetch("/auth/refresh", {
        method: "POST",
        credentials: "include",   // 攜帶 HttpOnly Cookie
    });
    if (resp.ok) {
        const { access_token } = await resp.json();
        setAccessToken(access_token);
    }
}

// 頁面載入時執行一次
silentRefresh();

// API 呼叫範例
async function apiCall(url, options = {}) {
    const resp = await fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            "Authorization": `Bearer ${getAccessToken()}`,
        },
    });
    if (resp.status === 401) {
        // Token 過期：嘗試 refresh 後重試
        await silentRefresh();
        return fetch(url, { ...options, headers: { "Authorization": `Bearer ${getAccessToken()}` } });
    }
    return resp;
}
```

---

# H4 無 CSRF 保護 — 跨站請求偽造

**問題**：若 Refresh Token 存在 Cookie（修復 H3 後），攻擊者可誘導使用者瀏覽惡意網站，瀏覽器會自動攜帶 Cookie 發出非預期請求。

**修復方式一：Double Submit Cookie（推薦，適合 SPA）**

```python
# 登入時同時發一個可讀的 CSRF Token Cookie（非 HttpOnly）
import secrets

@auth_bp.route("/login", methods=["POST"])
def login():
    # ... 驗證帳密 ...
    csrf_token = secrets.token_hex(32)

    response = make_response({"access_token": access_token})
    response.set_cookie("refresh_token", refresh_token, httponly=True, secure=True, samesite="Strict")
    response.set_cookie(
        "csrf_token",
        csrf_token,
        httponly=False,     # 前端 JS 需能讀取
        secure=True,
        samesite="Strict",
        max_age=60 * 60 * 24 * 7,
    )
    return response
```

```python
# Middleware：驗證 refresh 端點的 CSRF Token
from functools import wraps

def csrf_protect(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        cookie_csrf  = request.cookies.get("csrf_token", "")
        header_csrf  = request.headers.get("X-CSRF-Token", "")
        if not cookie_csrf or not hmac.compare_digest(cookie_csrf, header_csrf):
            return {"error": "CSRF token mismatch"}, 403
        return f(*args, **kwargs)
    return decorated


@auth_bp.route("/refresh", methods=["POST"])
@csrf_protect
def refresh():
    ...
```

```javascript
// 前端：每次呼叫 refresh 時帶上 CSRF Header
function getCsrfToken() {
    return document.cookie
        .split("; ")
        .find(row => row.startsWith("csrf_token="))
        ?.split("=")[1];
}

async function silentRefresh() {
    const resp = await fetch("/auth/refresh", {
        method: "POST",
        credentials: "include",
        headers: { "X-CSRF-Token": getCsrfToken() },
    });
    ...
}
```

**修復方式二：Flask-WTF（適合傳統表單）**

```python
from flask_wtf.csrf import CSRFProtect

csrf = CSRFProtect()
csrf.init_app(app)
```

```html
<form method="POST">
    <input type="hidden" name="csrf_token" value="{{ csrf_token() }}">
    ...
</form>
```

```python
# API 端點豁免 CSRF（改用 JWT Bearer Token 保護）
@csrf.exempt
@api_bp.route("/orders", methods=["POST"])
@jwt_required()
def create_order():
    ...
```

---

# 其他常見漏洞

## H5 SQL Injection — 資料庫注入

**問題**：字串拼接 SQL 語句，攻擊者輸入 `' OR '1'='1` 可繞過驗證或刪除資料。

```python
# 危險寫法
cursor.execute(f"SELECT * FROM users WHERE username = '{username}'")

# 正確：參數化查詢
cursor.execute("SELECT * FROM users WHERE username = %s", (username,))

# 更好：使用 ORM（SQLAlchemy）
user = User.query.filter_by(username=username).first()
```

```python
# SQLAlchemy — 永遠不要用 text() 拼接使用者輸入
from sqlalchemy import text

# 危險
db.session.execute(text(f"SELECT * FROM users WHERE id = {user_id}"))

# 正確
db.session.execute(text("SELECT * FROM users WHERE id = :uid"), {"uid": user_id})
```

---

## H6 XSS — 跨站腳本注入

**問題**：將使用者輸入直接輸出到 HTML，攻擊者注入 `<script>` 竊取資料。

```python
# Flask Jinja2 預設自動 escape，但 Markup() 或 | safe 會關閉保護
# 危險
return render_template_string(f"<p>{user_input}</p>")   # 不用 Jinja 變數，直接拼接

# 正確：永遠透過 Jinja2 變數
return render_template("page.html", content=user_input)
# {{ content }} → 自動 escape
```

```python
# API 回應：設定 Content-Type 防止瀏覽器猜測內容類型
from flask import jsonify

# jsonify 會自動設定 application/json，瀏覽器不會執行 JSON 為 HTML
return jsonify({"data": user_input})
```

```python
# Content Security Policy Header
@app.after_request
def set_csp(response):
    response.headers["Content-Security-Policy"] = (
        "default-src 'self'; "
        "script-src 'self'; "    # 禁止 inline script
        "object-src 'none';"
    )
    return response
```

---

## H7 IDOR — 不安全的直接物件參考

**問題**：`GET /orders/123` 沒有驗證 `123` 是否屬於當前使用者，可存取任意訂單。

```python
# 危險
@app.route("/orders/<int:order_id>")
@jwt_required()
def get_order(order_id):
    order = Order.query.get(order_id)       # 只驗證 JWT，沒驗證所有權
    return jsonify(order.to_dict())

# 正確：加上所有權驗證
@app.route("/orders/<int:order_id>")
@jwt_required()
def get_order(order_id):
    current_user_id = get_jwt_identity()
    order = Order.query.filter_by(id=order_id, user_id=current_user_id).first()
    if not order:
        abort(404)                          # 不回 403，避免資訊洩漏（對方不知道此資源存在）
    return jsonify(order.to_dict())
```

```python
# 進階：使用 UUID 取代自增 ID，降低被猜中的機率
import uuid
from sqlalchemy import Column, String

class Order(db.Model):
    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
```

---

## H8 敏感資料暴露 — 回應洩漏內部資訊

**問題**：DEBUG 模式在 Production 開啟、錯誤回應洩漏 Stack Trace、回應含密碼欄位。

```python
# config.py
class ProductionConfig(Config):
    DEBUG   = False
    TESTING = False

class DevelopmentConfig(Config):
    DEBUG = True
```

```python
# 統一錯誤處理：不暴露內部細節
@app.errorhandler(Exception)
def handle_exception(e):
    app.logger.exception(e)            # 記錄完整 stack trace 到 log
    if app.debug:
        raise e                        # 開發環境：顯示完整錯誤
    return {"error": "伺服器發生錯誤"}, 500   # 正式環境：只回通用訊息
```

```python
# Serializer：明確排除敏感欄位
class UserSchema(ma.Schema):
    class Meta:
        fields = ("id", "username", "email", "created_at")
        # 不列出 password_hash、is_admin、internal_notes
```

---

## H9 JWT Algorithm Confusion — 演算法混淆攻擊

**問題**：若後端未鎖定演算法，攻擊者可將 Header 改為 `"alg": "none"` 或將 RS256 公鑰當 HS256 密鑰，偽造任意 Token。

```python
# 危險：不指定演算法
payload = jwt.decode(token, public_key)

# 正確：明確指定允許的演算法
payload = jwt.decode(
    token,
    SECRET_KEY,
    algorithms=["HS256"],   # 白名單，嚴禁 "none"
)
```

```python
# 使用 flask-jwt-extended 時在 config 鎖定
app.config["JWT_ALGORITHM"]             = "HS256"
app.config["JWT_DECODE_ALGORITHMS"]     = ["HS256"]   # 只接受這個
```

```
建議：
- 純內部服務用 HS256（對稱，密鑰管理簡單）
- 跨服務 / 多方驗證用 RS256（非對稱，公鑰可公開分發）
- 絕不允許 "none" 演算法
```

---

## H10 不限制檔案上傳 — 惡意檔案執行

**問題**：未驗證副檔名與 MIME type，攻擊者上傳 `.php`、`.py` 等可執行檔。

```python
import os
import magic                                   # pip install python-magic
from werkzeug.utils import secure_filename
from uuid import uuid4

ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "pdf"}
ALLOWED_MIMES      = {"image/jpeg", "image/png", "image/gif", "application/pdf"}
MAX_FILE_SIZE      = 10 * 1024 * 1024          # 10 MB


def validate_upload(file) -> str:
    if file.content_length and file.content_length > MAX_FILE_SIZE:
        raise ValueError("檔案超過大小限制")

    filename  = secure_filename(file.filename)
    extension = filename.rsplit(".", 1)[-1].lower() if "." in filename else ""

    if extension not in ALLOWED_EXTENSIONS:
        raise ValueError(f"不允許的副檔名：{extension}")

    # 讀取前 2048 bytes 判斷真實 MIME（防止偽造副檔名）
    header    = file.read(2048)
    file.seek(0)
    mime_type = magic.from_buffer(header, mime=True)

    if mime_type not in ALLOWED_MIMES:
        raise ValueError(f"不允許的檔案類型：{mime_type}")

    # 儲存時用 UUID 重新命名，避免路徑穿越
    safe_name = f"{uuid4().hex}.{extension}"
    return safe_name


@app.route("/upload", methods=["POST"])
@jwt_required()
def upload():
    file = request.files.get("file")
    if not file:
        return {"error": "無檔案"}, 400
    try:
        safe_name = validate_upload(file)
        file.save(os.path.join(app.config["UPLOAD_FOLDER"], safe_name))
        return {"filename": safe_name}, 201
    except ValueError as e:
        return {"error": str(e)}, 400
```

```
其他注意事項：
- UPLOAD_FOLDER 設在 web root 以外，防止直接 URL 存取執行
- 上傳後透過應用程式提供下載，而非直接映射 static URL
- 圖片建議用 Pillow 重新渲染（消除 EXIF 與隱藏 payload）
```

---

## H11 Mass Assignment — 批次欄位覆寫

**問題**：直接將 request body 對應到 ORM 物件，攻擊者可傳入 `{"is_admin": true}` 提升權限。

```python
# 危險
@app.route("/users/<int:uid>", methods=["PUT"])
@jwt_required()
def update_user(uid):
    user = User.query.get(uid)
    for key, value in request.json.items():
        setattr(user, key, value)             # 所有欄位都可被覆寫

# 正確：白名單允許的欄位
UPDATABLE_FIELDS = {"display_name", "email", "avatar_url"}

@app.route("/users/<int:uid>", methods=["PUT"])
@jwt_required()
def update_user(uid):
    user = User.query.get_or_404(uid)
    data = {k: v for k, v in request.json.items() if k in UPDATABLE_FIELDS}
    for key, value in data.items():
        setattr(user, key, value)
    db.session.commit()
    return jsonify(user.to_dict())
```

```python
# 使用 marshmallow 做欄位過濾（推薦）
class UserUpdateSchema(ma.Schema):
    display_name = ma.String(load_only=True)
    email        = ma.Email(load_only=True)
    avatar_url   = ma.URL(load_only=True)
    # is_admin、role 等欄位完全不宣告

schema = UserUpdateSchema()
data   = schema.load(request.json)            # 未宣告欄位會被忽略
```

---

## H12 安全 HTTP Headers 缺失

**問題**：缺少安全 Headers，讓瀏覽器暴露於 Clickjacking、MIME Sniffing、資訊洩漏等風險。

```bash
pip install flask-talisman
```

```python
from flask_talisman import Talisman

csp = {
    "default-src": "'self'",
    "script-src" : ["'self'"],
    "style-src"  : ["'self'", "https://fonts.googleapis.com"],
    "img-src"    : ["'self'", "data:"],
    "object-src" : "'none'",
}

Talisman(
    app,
    force_https=True,                   # 強制 HTTPS（生產環境）
    strict_transport_security=True,     # HSTS
    content_security_policy=csp,
    referrer_policy="strict-origin-when-cross-origin",
    feature_policy={"geolocation": "'none'"},
)
```

**或手動設定（不用套件）**

```python
@app.after_request
def set_security_headers(response):
    response.headers["X-Content-Type-Options"]    = "nosniff"
    response.headers["X-Frame-Options"]           = "DENY"
    response.headers["X-XSS-Protection"]          = "1; mode=block"
    response.headers["Referrer-Policy"]           = "strict-origin-when-cross-origin"
    response.headers["Permissions-Policy"]        = "geolocation=(), microphone=()"
    response.headers["Strict-Transport-Security"] = "max-age=63072000; includeSubDomains; preload"
    # 隱藏 server 資訊
    response.headers.pop("Server", None)
    response.headers.pop("X-Powered-By", None)
    return response
```

| Header | 防禦目的 |
|---|---|
| `X-Content-Type-Options: nosniff` | 防止 MIME Sniffing（瀏覽器猜測 Content-Type） |
| `X-Frame-Options: DENY` | 防止 Clickjacking（嵌入 iframe） |
| `Strict-Transport-Security` | 強制 HTTPS，防止 SSL Strip |
| `Content-Security-Policy` | 限制資源來源，防止 XSS |
| `Referrer-Policy` | 控制 Referer Header，防止資訊外洩 |
| `Permissions-Policy` | 限制瀏覽器功能（麥克風、鏡頭等） |

---

## H13 Broken Function Level Authorization — 功能層級授權缺失

**問題**：管理端點（`/admin/*`、`/internal/*`）僅靠前端隱藏，後端未驗證 role，一般使用者直接呼叫即可存取。

```python
# 危險：只驗證是否登入，沒驗證 role
@app.route("/admin/users")
@jwt_required()
def list_all_users():
    return jsonify(User.query.all())

# 正確：role-based decorator
from functools import wraps
from flask_jwt_extended import get_jwt

def require_role(*roles):
    def decorator(f):
        @wraps(f)
        def wrapper(*args, **kwargs):
            claims = get_jwt()
            if claims.get("role") not in roles:
                abort(403)
            return f(*args, **kwargs)
        return wrapper
    return decorator


@app.route("/admin/users")
@jwt_required()
@require_role("admin", "superadmin")
def list_all_users():
    return jsonify([u.to_dict() for u in User.query.all()])
```

```python
# JWT 建立時將 role 加入 claims
from flask_jwt_extended import create_access_token

additional_claims = {"role": user.role}   # "admin" / "user" / "staff"
access_token = create_access_token(identity=user.id, additional_claims=additional_claims)
```

```python
# Blueprint 層級保護：整個 admin blueprint 都需要 admin 角色
@admin_bp.before_request
@jwt_required()
def require_admin():
    if get_jwt().get("role") != "admin":
        abort(403)
```

---

## H14 密碼儲存不安全 — MD5 / SHA1

**問題**：密碼用 MD5 / SHA1 / 無加鹽 SHA256 儲存，資料庫外洩後可用彩虹表反查。

```python
# 危險
import hashlib
password_hash = hashlib.md5(password.encode()).hexdigest()       # 不可用
password_hash = hashlib.sha1(password.encode()).hexdigest()      # 不可用
password_hash = hashlib.sha256(password.encode()).hexdigest()    # 無 salt，不夠安全

# 正確：bcrypt（推薦，Flask 生態系標準）
pip install flask-bcrypt
```

```python
from flask_bcrypt import Bcrypt

bcrypt = Bcrypt(app)

# 儲存密碼（自動加 salt，每次不同）
password_hash = bcrypt.generate_password_hash(password).decode("utf-8")

# 驗證密碼
is_valid = bcrypt.check_password_hash(password_hash, input_password)
```

```python
# 或使用 argon2（更現代，記憶體消耗可調）
# pip install argon2-cffi
from argon2 import PasswordHasher

ph = PasswordHasher(time_cost=2, memory_cost=65536, parallelism=2)

# 儲存
hash_str = ph.hash(password)

# 驗證
try:
    ph.verify(hash_str, input_password)
    if ph.check_needs_rehash(hash_str):
        hash_str = ph.hash(input_password)   # 參數變更後自動升級
except Exception:
    pass  # 驗證失敗
```

| 演算法 | 安全性 | 說明 |
|---|---|---|
| MD5 | 不可用 | 已有大量彩虹表，秒破 |
| SHA1 | 不可用 | 同上 |
| SHA256（無 salt） | 不可用 | 相同密碼 hash 相同，查表即破 |
| bcrypt | 可用 | 業界標準，work factor 可調 |
| argon2id | 最佳 | PHC 競賽冠軍，抗 GPU/ASIC |

---

## H15 Timing Attack — 計時攻擊

**問題**：用 `==` 比較 Token 或密碼時，Python 字串比較遇到第一個不同字元就提早返回，攻擊者可透過回應時間差逐位元推測正確值。

```python
# 危險：提早返回，有時間差
if user_token == expected_token:
    ...

if request.headers.get("X-API-Key") == API_KEY:
    ...

# 正確：固定時間比較，無論在哪個位元不同都花相同時間
import hmac

if hmac.compare_digest(user_token, expected_token):
    ...

if hmac.compare_digest(
    request.headers.get("X-API-Key", "").encode(),
    API_KEY.encode()
):
    ...
```

```python
# Webhook 驗簽也需用 compare_digest（H1 的 verify_hmac_signature 已正確使用）
def verify_token(provided: str, expected: str) -> bool:
    if not provided or not expected:
        return False
    return hmac.compare_digest(provided.encode("utf-8"), expected.encode("utf-8"))
```

```
適用場景：
- API Key 驗證
- Webhook 簽名比對
- Magic Link / One-Time Token 驗證
- 任何「比對使用者輸入與儲存值」的場景

密碼比對不需要手動用 compare_digest，bcrypt.check_password_hash 內部已處理。
```

---

## H16 Business Logic Vulnerability — 業務邏輯漏洞

**問題**：系統未對業務規則做伺服器端驗證，攻擊者透過修改請求繞過金額、庫存、優惠碼限制。

**① 金額竄改**

```python
# 危險：信任前端傳來的金額
@app.route("/checkout", methods=["POST"])
@jwt_required()
def checkout():
    amount = request.json["amount"]        # 攻擊者可傳 0.01 或負數
    charge_user(amount)

# 正確：後端自行計算金額
@app.route("/checkout", methods=["POST"])
@jwt_required()
def checkout():
    cart_items = get_cart(get_jwt_identity())
    amount = sum(item.price * item.quantity for item in cart_items)  # 後端計算
    if amount <= 0:
        abort(400, "無效金額")
    charge_user(amount)
```

**② 負數數量**

```python
from marshmallow import validate

class OrderItemSchema(ma.Schema):
    product_id = ma.Integer(required=True)
    quantity   = ma.Integer(required=True, validate=validate.Range(min=1, max=99))
    # 不接受 0 或負數
```

**③ 優惠碼無限重用**

```python
# 驗證優惠碼時同時檢查使用次數與個人使用記錄
def apply_coupon(user_id: int, coupon_code: str) -> int:
    coupon = Coupon.query.filter_by(code=coupon_code, is_active=True).first()
    if not coupon:
        raise ValueError("無效優惠碼")
    if coupon.used_count >= coupon.max_uses:
        raise ValueError("優惠碼已達使用上限")

    already_used = CouponUsage.query.filter_by(
        user_id=user_id, coupon_id=coupon.id
    ).first()
    if already_used:
        raise ValueError("您已使用過此優惠碼")

    # 用資料庫層級鎖定防止 Race Condition
    coupon.used_count = Coupon.used_count + 1   # SQL: UPDATE SET used_count = used_count + 1
    db.session.add(CouponUsage(user_id=user_id, coupon_id=coupon.id))
    db.session.commit()
    return coupon.discount
```

**④ Race Condition（超賣）**

```python
# 使用資料庫悲觀鎖（SELECT FOR UPDATE）防止並發超賣
from sqlalchemy import select
from sqlalchemy.orm import with_for_update

def reserve_stock(product_id: int, qty: int):
    product = db.session.execute(
        select(Product).where(Product.id == product_id).with_for_update()
    ).scalar_one()

    if product.stock < qty:
        raise ValueError("庫存不足")

    product.stock -= qty
    db.session.commit()
```

**⑤ 狀態機未強制驗證（Status Machine Bypass）**

攻擊者可跳過正常流程，將訂單從 `pending` 直接改為 `completed`，繞過付款步驟。

```python
# 危險：只驗證新狀態合法，沒驗證「可從當前狀態轉換過去」
@app.route("/order/<id>/status", methods=["PATCH"])
def update_status(id):
    new_status = request.json["status"]
    if new_status not in ("pending", "processing", "completed", "cancelled"):
        abort(400)
    db.orders.update_one({"_id": id}, {"$set": {"status": new_status}})

# 正確：明確定義允許的狀態轉換矩陣
VALID_TRANSITIONS = {
    "pending":    ["processing", "cancelled"],
    "processing": ["completed",  "cancelled"],
    # delivered / cancelled 為終止態，無法再轉換
}

@app.route("/order/<id>/status", methods=["PATCH"])
@jwt_required()
def update_status(id):
    order = db.orders.find_one({"_id": ObjectId(id)})
    if not order:
        abort(404)
    new_status = request.json.get("status", "")
    if new_status not in VALID_TRANSITIONS.get(order["status"], []):
        return jsonify({"success": False,
                        "message": f"不允許從 {order['status']} 轉換至 {new_status}"}), 400
    db.orders.update_one({"_id": ObjectId(id)}, {"$set": {"status": new_status}})
    return jsonify({"success": True})
```

**⑥ 鎖定帳號可繞過登入**

帳號被停用後，若 login / refresh 端點沒有檢查 `locked` 欄位，攻擊者仍可取得有效 Token。

```python
# 危險：鎖定帳號仍可登入
@app.route("/auth/login", methods=["POST"])
def login():
    user = db.users.find_one({"username": data["username"]})
    if not user or not verify_password(data["password"], user["password_hash"]):
        abort(401)
    return jsonify({"token": create_token(user)})   # 未檢查 locked

# 正確：login 與 refresh 都要檢查
@app.route("/auth/login", methods=["POST"])
def login():
    user = db.users.find_one({"username": data["username"]})
    if not user or not verify_password(data["password"], user["password_hash"]):
        abort(401)
    if user.get("locked"):
        return jsonify({"success": False, "message": "帳號已停用"}), 403
    return jsonify({"token": create_token(user)})

@app.route("/auth/refresh", methods=["POST"])
@jwt_required(refresh=True)
def refresh():
    identity = get_jwt_identity()
    user = db.users.find_one({"username": identity})
    if user and user.get("locked"):
        return jsonify({"success": False, "message": "帳號已停用"}), 403
    return jsonify({"token": create_access_token(identity)})
```

---

## H17 SSRF — 伺服器端請求偽造

**問題**：後端依使用者輸入的 URL 發出 HTTP 請求，攻擊者可讓伺服器存取內網服務（`http://192.168.1.1`）或 Cloud Metadata（`http://169.254.169.254/latest/meta-data/`）。

```python
# 危險：直接用使用者輸入的 URL 發請求
@app.route("/preview")
def preview():
    url = request.args.get("url")
    resp = requests.get(url)               # SSRF 漏洞
    return resp.content
```

```python
# 正確：域名白名單 + 阻擋私有 IP
import ipaddress
import socket
from urllib.parse import urlparse

ALLOWED_DOMAINS = {"example.com", "api.partner.com"}

PRIVATE_RANGES = [
    ipaddress.ip_network("10.0.0.0/8"),
    ipaddress.ip_network("172.16.0.0/12"),
    ipaddress.ip_network("192.168.0.0/16"),
    ipaddress.ip_network("127.0.0.0/8"),
    ipaddress.ip_network("169.254.0.0/16"),   # Cloud Metadata（AWS/GCP/Azure）
    ipaddress.ip_network("::1/128"),
]


def is_safe_url(url: str) -> bool:
    parsed = urlparse(url)
    if parsed.scheme not in ("http", "https"):
        return False
    if parsed.hostname not in ALLOWED_DOMAINS:
        return False

    # DNS 解析後再次確認 IP 不在私有範圍
    try:
        ip = ipaddress.ip_address(socket.gethostbyname(parsed.hostname))
        for private_range in PRIVATE_RANGES:
            if ip in private_range:
                return False
    except (socket.gaierror, ValueError):
        return False

    return True


@app.route("/preview")
def preview():
    url = request.args.get("url", "")
    if not is_safe_url(url):
        abort(400, "不允許的 URL")
    resp = requests.get(url, timeout=5, allow_redirects=False)   # 禁止重新導向
    return resp.content
```

```
Cloud 環境額外措施：
- AWS: IMDSv2（需 PUT 取得 Token 才能存取 Metadata，防止 SSRF 直接取用）
- GCP: 設定 metadata.google.internal 防火牆規則
- 不必要時不給 VM/Container 存取 Metadata Service 的網路權限
```

---

## H18 Outdated Dependencies — 套件 CVE 漏洞

**問題**：使用有已知 CVE 的舊版套件，攻擊者可用自動化工具（Nuclei、Trivy）掃描並直接利用。

**① 定期掃描**

```bash
# pip-audit（官方推薦）
pip install pip-audit
pip-audit                          # 掃描當前環境
pip-audit -r requirements.txt     # 掃描指定檔案
pip-audit --fix                    # 自動升級修復

# safety（另一選擇）
pip install safety
safety check -r requirements.txt
```

**② 固定版本 + 定期更新**

```bash
# 產生固定版本的 requirements.txt
pip freeze > requirements.txt

# 區分直接依賴（手動維護）與鎖定檔（自動產生）
# pyproject.toml 或 requirements.in  ← 只列直接依賴
# requirements.txt                   ← pip-compile 產生的完整鎖定版本

pip install pip-tools
pip-compile requirements.in          # 產生鎖定的 requirements.txt
pip-sync requirements.txt            # 同步環境
```

**③ CI/CD 自動掃描**

```yaml
# .github/workflows/security.yml
name: Security Scan
on: [push, pull_request]

jobs:
  audit:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: "3.12"
      - run: pip install pip-audit
      - run: pip-audit -r requirements.txt --severity medium   # medium 以上即失敗
```

```
建議節奏：
- 每週執行一次 pip-audit（可加入 cron job 或 Dependabot）
- CVE CVSS >= 7.0（High）：當週修復
- CVE CVSS < 7.0（Medium/Low）：納入下次 sprint
```

---

## H19 Insecure Deserialization — 不安全的反序列化

**問題**：`pickle.loads()` 可執行任意 Python 程式碼；`yaml.load()` 不安全版本同樣危險。

```python
# 危險：pickle 反序列化使用者輸入
import pickle
data = pickle.loads(request.data)            # RCE

# 危險：yaml.load 不指定 Loader
import yaml
data = yaml.load(request.data)              # 可執行任意指令

# 正確：永遠不要對不可信輸入使用 pickle
# 改用 JSON（只支援基本型別，無法執行程式碼）
import json
data = json.loads(request.data)

# 正確：yaml 必須指定 SafeLoader
data = yaml.safe_load(request.data)         # 或 yaml.load(data, Loader=yaml.SafeLoader)
```

```python
# 若業務需要序列化 Python 物件（例如 Task Queue），改用安全替代方案
# ① msgpack（高效能二進制，不執行程式碼）
import msgpack
data = msgpack.unpackb(raw_bytes, raw=False)

# ② marshmallow / pydantic（Schema 明確定義型別）
from pydantic import BaseModel

class TaskPayload(BaseModel):
    task_type: str
    user_id:   int
    params:    dict

payload = TaskPayload.model_validate_json(request.data)
```

```
快速檢查清單：
□ grep -r "pickle.loads" .        ← 是否有不可信來源的 pickle 反序列化
□ grep -r "yaml.load(" .          ← 是否未指定 SafeLoader
□ grep -r "eval(" .               ← 是否對使用者輸入使用 eval
□ grep -r "exec(" .               ← 是否對使用者輸入使用 exec
```

---

## H20 無 Security Logging / Monitoring

**問題**：攻擊發生後無審計日誌，無法判斷「何時、誰、做了什麼」，也無法觸發即時告警。

```python
# security_logger.py
import logging
import json
from datetime import datetime, timezone
from flask import request, g

security_logger = logging.getLogger("security")
security_logger.setLevel(logging.INFO)

handler = logging.FileHandler("/var/log/app/security.log")
handler.setFormatter(logging.Formatter("%(message)s"))
security_logger.addHandler(handler)


def log_security_event(event_type: str, user_id=None, detail: dict = None, level="info"):
    record = {
        "timestamp" : datetime.now(timezone.utc).isoformat(),
        "event"     : event_type,
        "user_id"   : user_id,
        "ip"        : request.remote_addr,
        "method"    : request.method,
        "path"      : request.path,
        "user_agent": request.user_agent.string,
        "detail"    : detail or {},
    }
    getattr(security_logger, level)(json.dumps(record, ensure_ascii=False))
```

```python
# 在關鍵操作點埋入日誌
@auth_bp.route("/login", methods=["POST"])
def login():
    username = request.json.get("username")
    user     = User.query.filter_by(username=username).first()

    if not user or not bcrypt.check_password_hash(user.password_hash, request.json.get("password")):
        log_security_event("login_failed", detail={"username": username}, level="warning")
        return {"error": "帳號或密碼錯誤"}, 401

    log_security_event("login_success", user_id=user.id)
    ...


# 其他需要記錄的事件
log_security_event("permission_denied",  user_id=uid, detail={"resource": "/admin/users"}, level="warning")
log_security_event("password_changed",   user_id=uid)
log_security_event("mfa_disabled",       user_id=uid, level="warning")
log_security_event("api_key_generated",  user_id=uid)
log_security_event("rate_limit_hit",     detail={"endpoint": "/login"}, level="warning")
log_security_event("file_uploaded",      user_id=uid, detail={"filename": safe_name})
```

```python
# 連續失敗告警（可整合 Slack / PagerDuty）
def check_brute_force(username: str):
    key   = f"login_fail:{username}"
    fails = redis_client.incr(key)
    redis_client.expire(key, 300)   # 5 分鐘視窗

    if fails == 5:
        log_security_event("brute_force_suspected", detail={"username": username}, level="error")
        send_alert(f"疑似暴力破解：{username}，5分鐘內失敗 {fails} 次")
```

```
應記錄的事件清單：
□ 登入成功 / 失敗
□ 密碼錯誤超過閾值
□ Token 驗證失敗（JWT 無效 / 過期）
□ 403 / 401 回應（未授權存取）
□ 管理操作（新增/刪除使用者、權限變更）
□ 敏感資料存取（匯出、批次查詢）
□ Rate Limit 觸發
□ 檔案上傳 / 下載
```

---

## H21 CORS 設定過寬

**問題**：`Access-Control-Allow-Origin: *` 搭配 `credentials: true` 是無效設定（瀏覽器會拒絕），但若設為 `*` 的 API 沒有使用 Cookie 認證，仍會讓任何來源的前端讀取回應內容。

```python
# 危險：允許所有來源（含未知第三方網站讀取 API 回應）
from flask_cors import CORS
CORS(app, resources={r"/api/*": {"origins": "*"}})

# 危險：反射 Origin（動態接受所有來源）
@app.after_request
def add_cors(response):
    response.headers["Access-Control-Allow-Origin"] = request.headers.get("Origin", "*")
    response.headers["Access-Control-Allow-Credentials"] = "true"
    return response
```

```python
# 正確：明確白名單
import os

ALLOWED_ORIGINS = os.environ.get("CORS_ORIGINS", "").split(",")
# 例：CORS_ORIGINS=https://app.example.com,https://admin.example.com

CORS(
    app,
    origins=ALLOWED_ORIGINS,
    supports_credentials=True,              # 允許攜帶 Cookie
    allow_headers=["Content-Type", "Authorization", "X-CSRF-Token"],
    methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],
    max_age=86400,                          # Preflight 快取 24 小時
)
```

```python
# 動態 Origin 驗證（支援 subdomain 的情境）
def is_allowed_origin(origin: str) -> bool:
    if not origin:
        return False
    allowed = {"https://app.example.com", "https://admin.example.com"}
    return origin in allowed


@app.after_request
def handle_cors(response):
    origin = request.headers.get("Origin", "")
    if is_allowed_origin(origin):
        response.headers["Access-Control-Allow-Origin"]      = origin
        response.headers["Access-Control-Allow-Credentials"] = "true"
        response.headers["Vary"]                             = "Origin"   # 告知 CDN 快取需分 Origin
    return response
```

| 設定 | 說明 |
|---|---|
| `Allow-Origin: *` + `Credentials: true` | 瀏覽器直接拒絕，無效 |
| `Allow-Origin: *` 無 Credentials | 允許任何網站讀取回應（公開 API 才適用） |
| `Allow-Origin: https://app.example.com` | 最安全，只允許指定來源 |
| `Vary: Origin` | 有多個合法 Origin 時必須加，告知 CDN 分別快取 |

---

## H22 API 版本洩漏 / 過時端點

**問題**：`/v1/` 端點已棄用但仍存活，可能比 `/v2/` 缺少驗證、Rate Limiting 或其他安全機制，成為繞過防護的後門。

```python
# 危險：v1 端點棄用後忘記移除或缺少安全機制
@app.route("/api/v1/users/<int:uid>")   # 無 jwt_required
def get_user_v1(uid):
    return jsonify(User.query.get(uid).to_dict())

@app.route("/api/v2/users/<int:uid>")
@jwt_required()
@require_role("admin")
def get_user_v2(uid):
    ...
```

```python
# 正確做法一：棄用端點回 410 Gone
@app.route("/api/v1/<path:path>", methods=["GET", "POST", "PUT", "DELETE"])
def v1_deprecated(path):
    return {
        "error"     : "API v1 已於 2025-01-01 停止服務",
        "migrate_to": f"/api/v2/{path}",
        "docs"      : "https://docs.example.com/migration",
    }, 410
```

```python
# 正確做法二：版本管理 Blueprint，共用安全 Middleware
from flask import Blueprint

def create_versioned_blueprint(version: str) -> Blueprint:
    bp = Blueprint(f"api_{version}", __name__, url_prefix=f"/api/{version}")

    @bp.before_request
    @jwt_required()
    def require_auth():
        pass

    return bp

api_v2 = create_versioned_blueprint("v2")
api_v3 = create_versioned_blueprint("v3")
```

```python
# 隱藏版本資訊（避免 Server Header 洩漏框架與版本）
@app.after_request
def remove_version_headers(response):
    response.headers.pop("Server",       None)
    response.headers.pop("X-Powered-By", None)
    return response
```

```
維運建議：
- 在 API Gateway（Nginx / Kong）層面直接封鎖已棄用的版本路徑
- 棄用前發出 Deprecation Header 通知使用者
- 定期用 grep / route inventory 確認有哪些端點存活
- 使用 Swagger / OpenAPI 文件管理端點生命週期
```

---

## H23 ReDoS — 正規表達式阻斷服務

**問題**：將使用者輸入直接放入正規表達式搜尋時，精心構造的輸入（如 `(a+)+b`）會讓回溯指數爆炸，讓單次請求占滿一顆 CPU 核心數秒甚至更久，達成阻斷服務。

**常見場景**：關鍵字搜尋、過濾器、資料驗證

```python
from flask import request, jsonify

# 危險：使用者輸入直接進入 $regex，或用 re.compile
@app.route("/product/search")
def search():
    keyword = request.args.get("keyword", "")
    # 攻擊輸入例：'((((((((((a+)+)+)+)+)+)+)+)+)+)!'
    results = db.products.find({"name": {"$regex": keyword}})  # 危險
    return jsonify(list(results))

# 同樣危險：直接 re.compile 後 match
import re
pattern = re.compile(keyword)  # 攻擊者控制 pattern
```

```python
import re
from flask import request, jsonify

# 正確：re.escape() 轉義所有特殊字元，讓輸入只能當字面字串
@app.route("/product/search")
def search():
    keyword = request.args.get("keyword", "")
    safe_keyword = re.escape(keyword)           # 轉義 . * + ? [ ] ( ) 等特殊字元
    results = db.products.find({
        "name": {"$regex": safe_keyword, "$options": "i"}  # i = case-insensitive
    })
    return jsonify([...])
```

```
額外防護措施：
- 對 keyword 長度做限制（例如最長 100 字元），避免過長輸入放大回溯
- 設定 API timeout（Gunicorn worker timeout）或 per-request timeout，
  防止單一請求長期占用 worker
- 使用 Full-Text Search（MongoDB Atlas Search / Elasticsearch）取代 $regex，
  FTS 索引查詢不受 ReDoS 影響
- Rate Limiting：限制搜尋端點每秒請求次數
```

---

## H24 Race Condition / TOCTOU — 並發競爭條件

**問題**：先讀取狀態再更新（Check-then-Act / TOCTOU）在高並發下可能讓兩個請求都通過驗證，導致超賣、重複完成訂單、庫存負值等問題。

**① MongoDB 原子操作（find_one_and_update + filter）**

```python
# 危險：讀取後更新，兩步驟之間有競爭窗口
def complete_order(order_id):
    order = col.find_one({"_id": ObjectId(order_id)})
    if order["status"] != "confirmed":
        return None
    # ← 此處另一個請求也可能通過上面的判斷
    col.update_one({"_id": ObjectId(order_id)},
                   {"$set": {"status": "completed"}})
    return order

# 正確：將條件放入 filter，原子性地「條件成立才更新」
from pymongo import ReturnDocument

def complete_order(order_id):
    result = col.find_one_and_update(
        {"_id": ObjectId(order_id), "status": "confirmed"},  # 條件：必須是 confirmed
        {"$set": {"status": "completed", "completed_at": datetime.utcnow()}},
        return_document=False,  # 回傳更新前文件；若條件不符則回傳 None
    )
    if result is None:
        return None  # 已被完成或狀態不對，直接忽略
    return result
```

**② MongoDB 原子庫存扣減（$inc + rollback）**

```python
from datetime import datetime
from bson import ObjectId

def adjust_inventory(product_id, warehouse_id, delta):
    """
    原子扣減庫存，delta 負數=減少。
    若結果為負值，回滾 $inc 並拋出例外。
    """
    q = {"product_id": ObjectId(product_id), "warehouse_id": ObjectId(warehouse_id)}

    # 1. 原子 $inc：不管當前值為何，先扣減
    before_doc = col.find_one_and_update(
        q,
        {"$inc": {"quantity": delta}, "$setOnInsert": {"created_at": datetime.utcnow()}},
        upsert=True,
        return_document=False,
    )
    before_qty = before_doc.get("quantity", 0) if before_doc else 0
    after_qty  = before_qty + delta

    # 2. 結果驗證：若為負值，回滾
    if after_qty < 0:
        col.update_one(q, {"$inc": {"quantity": -delta}})
        raise ValueError(f"庫存不足（現有 {before_qty}，欲扣 {abs(delta)}）")

    return before_qty, after_qty
```

**③ Thread-safe Singleton（Python MongoClient）**

```python
# 危險：多執行緒可能同時進入 if 判斷，建立多個連線池
_client = None

def get_client():
    global _client
    if _client is None:
        _client = MongoClient(MONGO_URI)   # 競爭條件！
    return _client

# 正確：雙重檢查鎖定（Double-Checked Locking）
import threading

_client = None
_lock   = threading.Lock()

def get_client():
    global _client
    if _client is None:           # 第一次快速判斷（不鎖）
        with _lock:
            if _client is None:   # 第二次在鎖內確認
                _client = MongoClient(MONGO_URI)
    return _client
```

```
適用原則：
- 所有「先讀取狀態，再根據狀態執行操作」的邏輯都是 TOCTOU 風險點
- 使用資料庫原子操作（MongoDB $inc / findOneAndUpdate，SQL SELECT FOR UPDATE）
  把 Check + Act 合併為一次資料庫呼叫
- Python Gunicorn 多 worker 共享同一 MongoClient 時，務必用 Lock 保護初始化
```

---

## H25 Python 設定陷阱 — 尾端逗號 Tuple

**問題**：Python 中 `(False,)` 是一個含有一個元素的 tuple，不是 `False`。tuple 在 boolean context 永遠為 `True`（非空），因此 `DEBUG = (False,)` 等同於 `DEBUG = True`，導致生產環境不知不覺開啟 debug mode。

```python
# conf/config.py

# 危險：尾端多了一個逗號，值是 tuple 而非 bool
class ProductionConfig(Config):
    DEBUG   = False,   # ← 這是 (False,)，永遠為 True！
    TESTING = False,   # ← 同上

# 正確：去掉逗號
class ProductionConfig(Config):
    DEBUG   = False
    TESTING = False
```

**為什麼難發現？**

```python
>>> x = False,
>>> type(x)
<class 'tuple'>
>>> bool(x)
True         # 非空 tuple 永遠為 True

>>> if x:
...     print("debug is ON")   # 永遠執行！
```

**影響**：

| 副作用 | 說明 |
|---|---|
| Flask debug mode 開啟 | Werkzeug interactive debugger 可在瀏覽器執行任意 Python |
| Stack trace 暴露 | 任何例外都回傳完整堆疊給客戶端 |
| Auto-reloader 啟動 | 每次檔案變更重啟，生產環境不穩定 |
| Pin secret 洩漏 | debug console 有 PIN，但若被猜中即 RCE |

```python
# 快速驗證設定是否正確
python -c "from conf.config import ProductionConfig; c = ProductionConfig(); print('DEBUG:', c.DEBUG, type(c.DEBUG))"
# 期望輸出：DEBUG: False <class 'bool'>
# 危險輸出：DEBUG: (False,) <class 'tuple'>
```

```
預防建議：
- CI 加入設定型別檢查：assert isinstance(app.config["DEBUG"], bool)
- 使用 pydantic / dynaconf 管理設定，強制型別驗證
- 生產部署前跑 pytest 確認 ProductionConfig.DEBUG is False（is 比對，不是 ==）
- Code Review 重點掃描 config.py 中所有 = False / = True 結尾，確認沒有多餘逗號
```
