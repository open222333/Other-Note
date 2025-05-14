# Python 模組 tencentcloud-sdk-python(騰訊雲SDK)

```
```

## 目錄

- [Python 模組 tencentcloud-sdk-python(騰訊雲SDK)](#python-模組-tencentcloud-sdk-python騰訊雲sdk)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [CDN](#cdn)
- [指令](#指令)
  - [CDN 預熱自動化流程圖與 Python 腳本架構](#cdn-預熱自動化流程圖與-python-腳本架構)

## 參考資料

[腾讯云开发者工具套件（SDK）3.0 Python](https://intl.cloud.tencent.com/zh/document/product/583/19698)

[TencentCloud/tencentcloud-sdk-python Github](https://github.com/TencentCloud/tencentcloud-sdk-python)

[API文檔](https://intl.cloud.tencent.com/zh/document/api)

[tencentyun/cos-python-sdk-v5 Github](https://github.com/tencentyun)

[支持 SDK 3.0版本的云产品列表(20211112)](https://cloud.tencent.com/document/product/494/42698#.E6.94.AF.E6.8C.81-sdk-3.0.E7.89.88.E6.9C.AC.E7.9A.84.E4.BA.91.E4.BA.A7.E5.93.81.E5.88.97.E8.A1.A8)

### CDN

[Github 騰訊雲 Python SDK 中 CDN 模組](https://github.com/TencentCloud/tencentcloud-sdk-python/blob/master/tencentcloud/cdn/v20180606/models.py#L27295)

[CDN 預熱的 API 方法定義](https://github.com/TencentCloud/tencentcloud-sdk-python/blob/master/tencentcloud/cdn/v20180606/cdn_client.py#L1742)

# 指令

```bash
# 腾讯云 Python SDK
pip install --upgrade tencentcloud-sdk-python

# 只安裝個別產品(不能與總包共同啟用)
pip install --upgrade tencentcloud-sdk-python-common tencentcloud-sdk-python-cvm

# 对象存储的 XML Python SDK
pip install -U cos-python-sdk-v5
```

## CDN 預熱自動化流程圖與 Python 腳本架構

```
自動解析 .m3u8 轉成 .ts 清單
建立 MongoDB 紀錄已預熱影片（避免重複）
環境變數設定域名、API 金鑰等
支援 Docker 打包執行
批次預熱 .ts 並避免超過每日 50 萬條限制
```

```
        ┌──────────────┐
        │ 使用者輸入影片 │
        └──────┬───────┘
               │
        ┌──────▼───────┐
        │ 解析 .m3u8 檔 │──→ 取得所有 .ts URL
        └──────┬───────┘
               │
        ┌──────▼────────────┐
        │ 檢查 MongoDB：是否已預熱 │
        └──────┬────────────┘
           否  │        是
               │         │
        ┌──────▼──────┐   │
        │ 分批送給 API │   │
        └──────┬──────┘   │
               │         │
        ┌──────▼──────┐   │
        │ 寫入 MongoDB │◄──┘
        └─────────────┘
```

MongoDB 結構（影片紀錄）

```json
{
  "video_id": "abc123",
  "m3u8_url": "https://yourcdn.com/playlist.m3u8",
  "ts_count": 143,
  "pushed_at": "2024-05-13T08:32:00Z"
}
```

主要 Python 模組架構

```
cdn_preheat/
├── main.py                # 主程式入口
├── parser.py              # 解析 m3u8 拿到所有 ts URL
├── preheat.py             # 呼叫騰訊雲 PushUrlsCache
├── db.py                  # MongoDB 操作邏輯
├── config.py              # 讀取環境變數
├── Dockerfile             # 打包執行
└── requirements.txt
```

main.py

```python
from parser import parse_m3u8
from db import has_been_pushed, record_pushed
from preheat import push_urls
import sys

video_id = sys.argv[1]
m3u8_url = sys.argv[2]

if has_been_pushed(video_id):
    print("Already preheated.")
    sys.exit(0)

ts_urls = parse_m3u8(m3u8_url)

success = push_urls(ts_urls)
if success:
    record_pushed(video_id, m3u8_url, len(ts_urls))
```

Dockerfile

```Dockerfile
FROM python:3.11-slim

WORKDIR /app
COPY . .
RUN pip install -r requirements.txt

ENV TZ=Asia/Taipei
CMD ["python", "main.py"]
```

可用環境變數（.env 或 Docker 內注入）

| 名稱                   | 說明                  |
| -------------------- | ------------------- |
| `TENCENT_SECRET_ID`  | 騰訊雲金鑰 ID            |
| `TENCENT_SECRET_KEY` | 騰訊雲金鑰 Key           |
| `CDN_HOST`           | CDN 前綴域名（自動補 .ts 用） |
| `MONGODB_URI`        | MongoDB 連線字串        |


```Python
### config.py
import os

TENCENT_SECRET_ID = os.getenv("TENCENT_SECRET_ID")
TENCENT_SECRET_KEY = os.getenv("TENCENT_SECRET_KEY")
CDN_HOST = os.getenv("CDN_HOST")  # example: cdn.example.com
MONGODB_URI = os.getenv("MONGODB_URI", "mongodb://localhost:27017/")


### parser.py
import requests
import re

def parse_m3u8(m3u8_url):
    resp = requests.get(m3u8_url)
    resp.raise_for_status()
    base_url = m3u8_url.rsplit("/", 1)[0]

    ts_urls = []
    for line in resp.text.splitlines():
        if line and not line.startswith("#") and line.endswith(".ts"):
            if line.startswith("http"):
                ts_urls.append(line)
            else:
                ts_urls.append(f"{base_url}/{line}")

    return ts_urls


### db.py
from pymongo import MongoClient
from config import MONGODB_URI
from datetime import datetime

client = MongoClient(MONGODB_URI)
db = client.cdn_preheat
collection = db.preheated_videos

def has_been_pushed(video_id):
    return collection.find_one({"video_id": video_id}) is not None

def record_pushed(video_id, m3u8_url, ts_count):
    collection.insert_one({
        "video_id": video_id,
        "m3u8_url": m3u8_url,
        "ts_count": ts_count,
        "pushed_at": datetime.utcnow()
    })

def clear_video(video_id):
    collection.delete_many({"video_id": video_id})


### preheat.py
from tencentcloud.cdn.v20180606 import cdn_client, models
from tencentcloud.common.credential import Credential
from config import TENCENT_SECRET_ID, TENCENT_SECRET_KEY
import time

def chunked(lst, size):
    for i in range(0, len(lst), size):
        yield lst[i:i + size]

def push_urls(ts_urls):
    cred = Credential(TENCENT_SECRET_ID, TENCENT_SECRET_KEY)
    client = cdn_client.CdnClient(cred, "ap-guangzhou")

    total = len(ts_urls)
    pushed = 0

    for batch in chunked(ts_urls, 100):
        req = models.PushUrlsCacheRequest()
        req.Urls = batch
        try:
            resp = client.PushUrlsCache(req)
            pushed += len(batch)
            print(f"[OK] Pushed {len(batch)} URLs, total pushed: {pushed}/{total}")
            time.sleep(0.05)  # 防止頻率超標
        except Exception as e:
            print(f"[ERROR] Push failed: {e}")
            return False
    return True


### main.py
import sys
from parser import parse_m3u8
from db import has_been_pushed, record_pushed, clear_video
from preheat import push_urls

if len(sys.argv) < 3:
    print("用法: python main.py <video_id> <m3u8_url> [--reset]")
    sys.exit(1)

video_id = sys.argv[1]
m3u8_url = sys.argv[2]
reset = "--reset" in sys.argv

if reset:
    clear_video(video_id)

if has_been_pushed(video_id):
    print("影片已預熱過，若需重跑請加上 --reset")
    sys.exit(0)

print("解析 .m3u8...")
ts_urls = parse_m3u8(m3u8_url)

print(f"共找到 {len(ts_urls)} 個 .ts 片段，開始預熱...")
success = push_urls(ts_urls)

if success:
    record_pushed(video_id, m3u8_url, len(ts_urls))
    print("✅ 預熱完成")
else:
    print("❌ 預熱失敗")


### requirements.txt
requests
pymongo
tencentcloud-sdk-python


### Dockerfile
FROM python:3.11-slim

WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt

ENV TZ=Asia/Taipei
CMD ["python", "main.py"]
```