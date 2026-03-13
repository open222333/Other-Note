# Python 模組-內建 secrets(產生用於管理機密的安全亂數)

```
```

## 目錄

- [Python 模組-內建 secrets(產生用於管理機密的安全亂數)](#python-模組-內建-secrets產生用於管理機密的安全亂數)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[secrets 官方文檔](https://docs.python.org/zh-tw/3/library/secrets.html)

# 用法

```Python
import secrets

# 生成一個安全的隨機字串，預設長度為 32 字元
random_string = secrets.token_hex()

print(f"Random String: {random_string}")


# 生成一個安全的隨機字串，長度為 16 字元
short_random_string = secrets.token_hex(8)

print(f"Short Random String: {short_random_string}")

```
