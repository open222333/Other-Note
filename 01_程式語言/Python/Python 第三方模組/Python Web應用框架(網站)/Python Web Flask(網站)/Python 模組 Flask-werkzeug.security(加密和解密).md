# Python 模組 Flask-werkzeug.security(加密和解密)

```
```

## 目錄

- [Python 模組 Flask-werkzeug.security(加密和解密)](#python-模組-flask-werkzeugsecurity加密和解密)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[werkzeug.security pypi](https://pypi.org/project/werkzeug.security/)

# 指令

```bash
# 安裝
pip install werkzeug.security
```

# 用法

```Python
from flask import Flask
from werkzeug.security import generate_password_hash, check_password_hash

app = Flask(__name__)

# 加密密碼
def encrypt_password(password):
    return generate_password_hash(password, method='sha256')

# 驗證密碼
def verify_password(encrypted_password, password):
    return check_password_hash(encrypted_password, password)

@app.route('/')
def index():
    # 假設這是用戶註冊時的密碼
    user_password = "mysecretpassword"

    # 加密密碼
    encrypted_password = encrypt_password(user_password)

    # 模擬用戶登入，驗證密碼
    entered_password = "mysecretpassword"
    is_password_correct = verify_password(encrypted_password, entered_password)

    if is_password_correct:
        return "密碼正確！"
    else:
        return "密碼錯誤！"

if __name__ == '__main__':
    app.run(debug=True)
```
