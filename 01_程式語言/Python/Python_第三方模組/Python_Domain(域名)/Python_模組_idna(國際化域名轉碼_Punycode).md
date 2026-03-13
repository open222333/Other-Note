# Python 模組 idna(國際化域名轉碼 Punycode)

```
提供了域名的國際化域名轉碼（IDNA）功能。

該模組可用於處理域名中的非ASCII字符，並將其轉換為ASCII兼容的Punycode格式。
```

## 目錄

- [Python 模組 idna(國際化域名轉碼 Punycode)](#python-模組-idna國際化域名轉碼-punycode)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[idna pypi](https://pypi.org/project/idna/)

# 指令

```bash
# 安裝
pip install idna
```

# 用法

```Python
import idna

# 域名轉碼（編碼）
domain = '例子.com'
punycode = idna.encode(domain).decode('utf-8')
print(punycode)  # 輸出: xn--fsq33d9b.com

# 域名解碼
decoded_domain = idna.decode(punycode)
print(decoded_domain)  # 輸出: 例子.com
```
