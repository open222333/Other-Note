# Linux 工具 nslookup(DNS偵錯工具)

```
輸入 nslookup 命令後，會看到 > 提示符號，之後就可輸入查詢指令。
一般輸入IP address或是domain name來做反向及正向的解析。
```

## 參考資料

[nslookup](https://ss64.com/bash/nslookup.html)

# 指令

```bash
nslookup [IP/domain]

# 檢測 DNS TXT 配置結果是否正確
nslookup -type=txt subdomain.domain.com
```
