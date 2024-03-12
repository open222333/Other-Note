# Python 模組-內建 base64(資料編碼)

```
Base16、Base32、Base64、Base85 資料編碼
```

## 目錄

- [Python 模組-內建 base64(資料編碼)](#python-模組-內建-base64資料編碼)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[base64 官方文檔](https://docs.python.org/zh-tw/3/library/base64.html)

# 用法

```Python
import base64

# 字符串轉換為 Base64 編碼
original_string = "Hello, World!"
encoded_bytes = base64.b64encode(original_string.encode('utf-8'))
encoded_string = encoded_bytes.decode('utf-8')

print(f"Original String: {original_string}")
print(f"Encoded String: {encoded_string}")

# Base64 編碼解碼為字符串
decoded_bytes = base64.b64decode(encoded_string)
decoded_string = decoded_bytes.decode('utf-8')

print(f"Decoded String: {decoded_string}")
```
