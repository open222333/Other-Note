# Cloudflare 筆記

## 目錄

- [Cloudflare 筆記](#cloudflare-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [心得相關](#心得相關)
- [狀況](#狀況)
	- [新增網站 出現 is not a registered domain](#新增網站-出現-is-not-a-registered-domain)
- [回報](#回報)
	- [取得票證](#取得票證)
	- [票證追蹤](#票證追蹤)


## 參考資料

[官方網站](https://www.cloudflare.com/zh-tw/)

[Cloudflare API v4 Documentation](https://api.cloudflare.com/)

[Cloudflare’s API](https://developers.cloudflare.com/fundamentals/api/)

[Getting access to the Cloudflare API](https://developers.cloudflare.com/api/)

[Add domain API](https://developers.cloudflare.com/api/operations/pages-domains-add-domain)

### 心得相關

[无法将我的域名添加到 Cloudflare](https://support.cloudflare.com/hc/zh-cn/articles/205359838-%E6%97%A0%E6%B3%95%E5%B0%86%E6%88%91%E7%9A%84%E5%9F%9F%E5%90%8D%E6%B7%BB%E5%8A%A0%E5%88%B0-Cloudflare-)

[通过自动化将多个站点添加到 Cloudflare](https://developers.cloudflare.com/support/other-languages/%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87/%E9%80%9A%E8%BF%87%E8%87%AA%E5%8A%A8%E5%8C%96%E5%B0%86%E5%A4%9A%E4%B8%AA%E7%AB%99%E7%82%B9%E6%B7%BB%E5%8A%A0%E5%88%B0-cloudflare/)

# 狀況

## 新增網站 出現 is not a registered domain

```
域名沒辦法新增到cloudflare的話，要先檢查  whois 的註冊紀錄，沒註冊紀錄的話要找 namecheap ，有註冊紀錄要這邊回報給cloudflare
```

```bash
# 確認域名沒有DNSSEC設定
dig +short ds cloudflare.com

# 將域添加到 Cloudflare 之前
# 域必須為有效的工作域名服務器返回 NS 記錄
dig +short ns cloudflare.com
ns3.cloudflare.com. ns4.cloudflare.com. ns5.cloudflare.com. ns6.cloudflare.com. ns7.cloudflare.com.

# 將域添加到 Cloudflare 之前
# 域在查詢時必須返回有效的 SOA 記錄
dig +short soa cloudflare.com
ns3.cloudflare.com. dns.cloudflare.com.2029202248 10000 2400 604800 300
```

# 回報

## 取得票證

位置：
右上角 支援 -> 聯絡支援(需開啟新的支援入口)

有什麼我們可以幫忙的嗎? -> Cloudflare Registrar
您遇到什麼類型的 cloudflare registrar issue? -> WHOIS

## 票證追蹤

https://support.cloudflare.com/hc/zh-cn/requests/{票證ID}