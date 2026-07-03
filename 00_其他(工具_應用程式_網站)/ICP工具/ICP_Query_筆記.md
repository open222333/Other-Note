# ICP_Query 筆記

```
查詢中國工信部 ICP 備案資訊的工具，支援網站域名、App、小程式、快應用等類型。
資料直接來源於工信部官方平台，內建 Web UI 與本地 REST API，無需外部依賴。
```

## 目錄

- [ICP\_Query 筆記](#icp_query-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Docker](#docker)
  - [docker-compose 部署](#docker-compose-部署)
  - [原始碼（uv）](#原始碼uv)
  - [執行檔](#執行檔)
- [使用方式](#使用方式)
  - [Web UI](#web-ui)
  - [REST API](#rest-api)
  - [Python 模組](#python-模組)
- [查詢類型](#查詢類型)
- [回應欄位說明](#回應欄位說明)

## 參考資料

[HG-ha/ICP_Query - GitHub](https://github.com/HG-ha/ICP_Query)

---

# 安裝

## Docker

```bash
docker run -d -p 16181:16181 --name ymicp yiminger/ymicp
```

## docker-compose 部署

```yml
services:
  ymicp:
    image: yiminger/ymicp
    container_name: ymicp
    ports:
      - "16181:16181"
    restart: unless-stopped
```

## 原始碼（uv）

```bash
git clone https://github.com/HG-ha/ICP_Query.git
cd ICP_Query

uv venv --python 3.11
uv pip install -r requirements.txt
uv run icpApi.py
```

## 執行檔

至 [GitHub Releases](https://github.com/HG-ha/ICP_Query/releases) 下載對應平台的預編譯執行檔，直接執行即可。

---

# 使用方式

服務啟動後監聽 `http://127.0.0.1:16181`。

## Web UI

瀏覽器直接開啟：

```
http://127.0.0.1:16181
```

## REST API

**查詢網站域名**

```bash
curl "http://127.0.0.1:16181/query/web?search=baidu.com"
```

**查詢 ICP 備案號**

```bash
curl "http://127.0.0.1:16181/query/web?search=京ICP证030173号"
```

**查詢企業名稱（含分頁）**

```bash
curl "http://127.0.0.1:16181/query/web?search=企業名稱&pageNum=1&pageSize=20"
```

| 參數 | 說明 | 預設值 |
|------|------|--------|
| `search` | 查詢關鍵字（域名 / 備案號 / 企業名稱） | 必填 |
| `pageNum` | 頁碼 | `1` |
| `pageSize` | 每頁筆數 | `10` |

## Python 模組

```python
import asyncio
from ymicp import beian

async def main():
    icp = beian()

    # 查詢網站
    result = await icp.ymWeb("baidu.com")
    print(result)

    # 查詢 App
    result = await icp.ymApp("微信")
    print(result)

asyncio.run(main())
```

---

# 查詢類型

| 類型 | API 路徑 | Python 方法 | 說明 |
|------|----------|-------------|------|
| 網站 | `/query/web` | `ymWeb()` | 域名備案查詢 |
| App | `/query/app` | `ymApp()` | 應用程式備案 |
| 小程式 | `/query/mapp` | `ymMapp()` | 微信小程式等 |
| 快應用 | `/query/kapp` | `ymKapp()` | 快應用備案 |
| 黑名單網站 | `/query/bweb` | — | 違規網站名單 |
| 黑名單 App | `/query/bapp` | — | 違規 App 名單 |

---

# 回應欄位說明

| 欄位 | 說明 |
|------|------|
| `domain` | 域名 |
| `unitName` | 主辦單位名稱（企業 / 個人） |
| `mainLicence` | 主體備案號 |
| `serviceLicence` | 網站備案號 |
| `contentTypeName` | 服務類型 |
| `updateRecordTime` | 核准時間 |
| `serviceName` | App / 小程式名稱 |
| `blackListLevel` | 違規等級（黑名單查詢用） |
