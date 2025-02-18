# 防盜鏈 (Hotlink Protection) 筆記

## 參考資料

[Referer偽造，防盜鏈與反盜鏈](https://kknews.cc/zh-tw/history/al5l2yx.html)

用途

1. 獲取訪問來源，統計訪問流量的來源和搜索的關鍵詞

2. 防盜鏈

# 防盜鏈 (Hotlink Protection)

防盜鏈 (Hotlink Protection)，是針對哪種資源？

是 影片串流 (m3u8、MP4)、圖片 (JPG、PNG)，還是 API 請求？

常見的防盜鏈方法：

HTTP Referer 檢查 → 限制只有特定來源能存取

Token 簽名驗證 → 生成短效 URL (如 Cloudflare Signed URL)

User-Agent / IP 限制 → 只允許特定裝置或 IP 存取

HLS m3u8 加密 → 針對影片串流內容進行 AES-128 加密
