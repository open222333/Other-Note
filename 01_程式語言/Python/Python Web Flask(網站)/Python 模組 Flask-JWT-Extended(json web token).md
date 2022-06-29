# Python 模組 Flask-JWT-Extended(json web token)

```
此套件除了提供實現 JSON Web Tokens 功能外，還提供許多其他功能，像是 Refresh Tokens 功能，或是 Partially protecting routes 功能
```

## 參考資料

[Flask-JWT-Extended pypi](https://pypi.org/project/Flask-JWT-Extended/)

[【Flask 教學系列】淺談 JWT 與 Flask JWT 實作](https://www.maxlist.xyz/2020/05/01/flask-jwt-extended/)

[Flask-JWT-Extended 文檔](https://flask-jwt-extended.readthedocs.io/en/stable/)

# JWT基本介紹

```
流程 1 – 產生 JWT Token

流程 2 – 驗證 JWT Token

JWT 其實是由三段亂碼組成，分別是
	Header
		Header 存放的是一種聲明，alg 中說明著數位簽章使用的加密演算法是 HS256，而 type 說明這個 token 是 JWT。
	Payload
		將使用者的狀態存放於此，官方有建議註冊參數，像是 iss (issuer), exp (expiration time), sub (subject), aud (audience), and others.
	Signature
		數位簽章的部分，首先會在 Server 建立一組 secret_key，然後再將 Header、Playload 和這組 secret_key，使用 Hash 256 加密。
```

# 指令

```bash
# 安裝
pip install Flask-JWT-Extended
```

# 用法

```Python
```
