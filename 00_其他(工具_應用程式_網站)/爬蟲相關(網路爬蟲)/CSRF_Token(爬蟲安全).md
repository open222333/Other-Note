# CSRF（Cross Site Request Forgery）

```
跨站請求偽造（Cross-Site Request Forgery，CSRF）是一種網絡攻擊方式，攻擊者通過受害者已經認證的會話，冒充受害者向目標網站發送未經授權的請求。這種攻擊利用了受害者的身份驗證信息，而不是直接破壞網站的安全性。

CSRF 攻擊的工作原理
受害者登錄目標網站：受害者在瀏覽器中登錄到可信的網站（如銀行網站）並保持會話有效。
訪問惡意網站：受害者在同一瀏覽器中訪問了一個包含惡意代碼的網站。
惡意請求發送：惡意網站觸發一個請求，該請求在受害者的身份驗證信息（如 cookies）的上下文中被發送到受害者已經登錄的網站。
目標網站接受請求：因為請求包含有效的身份驗證信息，目標網站無法區分這是合法用戶的請求還是攻擊者的偽造請求，並執行了請求中的操作。
```

## 目錄

- [CSRF（Cross Site Request Forgery）](#csrfcross-site-request-forgery)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [破解相關](#破解相關)

## 參考資料

### 破解相關

[Passing csrftoken with python Requests](https://stackoverflow.com/questions/13567507/passing-csrftoken-with-python-requests)
