# Linux 工具 dig(DNS偵錯工具)

```
dig是一個網絡管理命令行工具，用於查詢域名系統。
dig是域名伺服器軟體套件BIND的組成部分。
```

## 目錄

- [Linux 工具 dig(DNS偵錯工具)](#linux-工具-digdns偵錯工具)
	- [目錄](#目錄)
- [參考資料](#參考資料)
- [指令](#指令)

# 參考資料

[dig (domain information groper)](https://ss64.com/bash/dig.html)

# 指令

```bash
dig [@server]

### 回應說明 ###
# 指向
;; ANSWER SECTION:

# 檢測 DNS TXT 配置結果是否正確
dig @223.5.5.5 subdomain.domain.com txt +short

# 檢測 DNS CNAME 配置結果
dig [domain] -t CNAME +short
```
