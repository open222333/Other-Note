# Python 模組 2captcha-python(驗證碼識別服務)

```
如何自動解決驗證碼
在 2captcha.com/in.php 上傳驗證碼
服務器存儲您的驗證碼並返回您的驗證碼 ID
服務器立即將您的驗證碼分發給工作人員
工作人員解決驗證碼並將答案發送回服務器
您正在使用您的 ID 向服務器發送請求以獲取答案
```

## 目錄

- [Python 模組 2captcha-python(驗證碼識別服務)](#python-模組-2captcha-python驗證碼識別服務)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[官方網站](https://2captcha.com/)

[官方網站 - 價格](https://2captcha.com/for-customer)

[2captcha-python pypi](https://pypi.org/project/2captcha-python/)

[Bypass captcha in Python - 官方範例](https://2captcha.com/lang/python)

# 指令

```bash
# 安裝
pip install 2captcha-python
```

# 用法

```Python
from twocaptcha import TwoCaptcha

# solver = TwoCaptcha('YOUR_API_KEY')
config = {
	'server':           '2captcha.com',
	'apiKey':           'YOUR_API_KEY',
	'softId':            123,
	'callback':         'https://your.site/result-receiver',
	'defaultTimeout':    120,
	'recaptchaTimeout':  600,
	'pollingInterval':   10,
}
solver = TwoCaptcha(**config)
```
