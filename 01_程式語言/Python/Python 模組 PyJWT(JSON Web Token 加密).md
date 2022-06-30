# Python 模組 PyJWT(JSON Web Token 加密)

```
JWT(JSON Web Token) 是 RFC 7519 定義的一套標準，用以確保應用(application)之間傳遞訊息的安全性與完整性(integrity)。 JWT 常常與傳統的 Cookie/Session 技術一起被比較，然而這些技術是為了解決不同問題所發明的，也有各自的優缺點與特別合適的應用場景，沒有誰優誰劣的絕對定論。

目前實務上也越來越多應用會利用 JWT 傳遞資料，譬如 APP 在使用者登入時透過 JWT 取得常用的「非機敏性資料」（例如，暱稱、語系設定等等），並且儲存在裝置內，以減少詢問伺服器的次數，達到節省伺服器資源與增加下一次 APP 啟動速度的效果，運用得當的話也是一個加分的技術。

JWT 是個很長的字串，例如：
    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJoZWxsbyI6IndvcmxkIn0.bqxXg9VwcbXKoiWtp-osd0WKPX307RjcN7EuXbdq-CE

這個字串會用 . 進行分隔，分成 3 個部分：
    Header
    Payload
        將使用者的狀態存放於此，官方有建議註冊參數，像是 iss (issuer), exp (expiration time), sub (subject), aud (audience), and others.
        (這段是可以被解密的)
    Signature (簽章)
        數位簽章的部分，首先會在 Server 建立一組 secret_key，然後再將 Header、Playload 和這組 secret_key，使用 Hash 256 加密。
        HMACSHA256(
            base64UrlEncode(header) + "." +
            base64UrlEncode(payload),
            secret_key)

未來拿要做驗證時，只需用相同的方法 (收到的 Header + 收到的 Payload + Server 的 key )加密後比對與收到的 JWT 是不是一樣的亂碼，如果有不同就可以知道資料被竄改過了。

如果用上面的範例進行對應就是：
    Header: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
    Payload: eyJoZWxsbyI6IndvcmxkIn0
    Signature: bqxXg9VwcbXKoiWtp-osd0WKPX307RjcN7EuXbdq-CE

JWT 目前提供 7 種關鍵字，可以放在 Payload 內，讓後端伺服器可以根據這些關鍵字判斷 Token 有效與否：
    iss (Issuer) Token 的發行者
    sub (Subject) 也就是使用該 Token 的使用者
    aud (Audience) Token 的接收者，也就是後端伺服器
    exp (Expiration Time) Token 的過期時間
    nbf (Not Before) Token 的生效時間
    iat (Issued At) Token 的發行時間
    jti (JWT ID) Token 的 ID


嚴謹一點的說法是 JWT 將 JSON 結構的資料進行 Base64Url 編碼並加上數位簽章 Signature 後組成 Token 傳遞給 Client 端，然後此 Token 可用於：
    伺服器端進行驗證身分
    訊息交換使用
```

## 參考資料

[PyJWT pypi](https://pypi.org/project/PyJWT/)

[Python - JWT (JSON Web Token)](https://myapollo.com.tw/zh-tw/python-json-web-token/)

[淺談 JWT 與 Flask JWT 實作](https://www.maxlist.xyz/2020/05/01/flask-jwt-extended/)

[Flask-JWT-Extended’s Documentation 文檔](https://flask-jwt-extended.readthedocs.io/en/stable/)

[PyJWT 文檔](https://pyjwt.readthedocs.io/en/stable/)

[手動加密網址](https://jwt.io/)

# 指令

```bash
# 安裝
pip install PyJWT
```

# 用法

```Python
```