# WebServer 總覽

```
網站伺服器（Web Server）負責接收 HTTP/HTTPS 請求並回應內容。
本目錄收錄常見的 Web Server 與 WSGI/ASGI Server 工具筆記。
```

## 目錄

- [WebServer 總覽](#webserver-總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [工具清單](#工具清單)
- [角色區分](#角色區分)
  - [Web Server vs Application Server](#web-server-vs-application-server)
  - [WSGI vs ASGI](#wsgi-vs-asgi)
- [工具比較](#工具比較)
  - [Web Server 比較（Nginx vs Apache）](#web-server-比較nginx-vs-apache)
  - [Python Application Server 比較](#python-application-server-比較)
- [常見部署組合](#常見部署組合)

## 參考資料

- [Nginx 筆記](./Nginx/Nginx_筆記.md)
- [Apache 筆記](./Apache/Apache_筆記.md)
- [Gunicorn 筆記](./Gunicorn(WSGI_server)/Gunicorn_筆記.md)

---

# 工具清單

| 工具 | 類型 | 語言 | 筆記 |
|---|---|---|---|
| **Nginx** | Web Server / Reverse Proxy | C | [Nginx_筆記.md](./Nginx/Nginx_筆記.md) |
| **Apache** | Web Server | C | [Apache_筆記.md](./Apache/Apache_筆記.md) |
| **Gunicorn** | Python WSGI Server | Python | [Gunicorn_筆記.md](./Gunicorn(WSGI_server)/Gunicorn_筆記.md) |

---

# 角色區分

## Web Server vs Application Server

```
請求流向：Client → Web Server → Application Server → 應用程式（Flask / Django）
```

| 角色 | 負責 | 代表工具 |
|---|---|---|
| **Web Server** | 靜態檔案、SSL 終止、反向代理、負載均衡 | Nginx、Apache |
| **Application Server** | 執行 Python 應用程式，處理動態請求 | Gunicorn、uWSGI、Uvicorn |

> Web Server 本身不執行 Python 程式碼，需將動態請求轉發給 Application Server。

## WSGI vs ASGI

| 協議 | 特性 | 代表 Server | 適用框架 |
|---|---|---|---|
| **WSGI** | 同步，一次一個請求 | Gunicorn、uWSGI | Flask、Django |
| **ASGI** | 非同步，支援 WebSocket / SSE | Uvicorn、Daphne | FastAPI、Django Channels |

---

# 工具比較

## Web Server 比較（Nginx vs Apache）

| 項目 | Nginx | Apache |
|---|---|---|
| 架構 | 事件驅動（非同步） | 多進程 / 多執行緒（同步） |
| 靜態檔案效能 | 極高 | 高 |
| 高並發 | 優（記憶體用量低） | 較差（每個連線一個進程/執行緒） |
| 動態設定（不重啟） | 需要 reload | **.htaccess** 可即時生效 |
| 設定語法 | 區塊式（`server {}`） | 指令式（`<VirtualHost>`） |
| 模組載入 | 編譯時靜態載入 | 執行期動態載入（`a2enmod`） |
| 反向代理 | 內建，效能強 | 需啟用 `mod_proxy` |
| 市佔率趨勢 | 持續成長 | 逐漸下降 |
| 適合情境 | 高流量、靜態資源、反向代理 | 需要 .htaccess、共享主機環境 |
| Debian 服務名稱 | `nginx` | `apache2` |
| RedHat 服務名稱 | `nginx` | `httpd` |

## Python Application Server 比較

| 項目 | Gunicorn | uWSGI | Uvicorn |
|---|---|---|---|
| 協議 | WSGI | WSGI / uWSGI | ASGI |
| 設定複雜度 | 低（選項少） | 高（功能多） | 低 |
| 效能 | 高 | 高 | 極高（非同步） |
| 適用框架 | Flask、Django | Flask、Django | FastAPI、Starlette |
| 多進程 | ✅（`-w` 指定 worker 數） | ✅ | ✅（需搭配 Gunicorn） |
| WebSocket | ❌ | ❌ | ✅ |
| 推薦情境 | WSGI 首選，簡單可靠 | 需要進階調校 | 非同步框架 |

---

# 常見部署組合

| 情境 | 組合 | 說明 |
|---|---|---|
| Flask / Django（同步） | **Nginx + Gunicorn** | 最常見，穩定、文件多 |
| FastAPI（非同步） | **Nginx + Uvicorn** | 高效能 API 服務 |
| FastAPI（非同步，多進程） | **Nginx + Gunicorn + Uvicorn workers** | Gunicorn 管理進程，Uvicorn 處理請求 |
| PHP 應用 | **Nginx + PHP-FPM** 或 **Apache + mod_php** | PHP 傳統部署 |
| 靜態網站 | **Nginx** 單獨 | 無需 Application Server |
| 共享主機 / cPanel | **Apache + mod_php** | .htaccess 支援好，老牌主機商常用 |

```
# FastAPI 推薦組合啟動範例
gunicorn main:app \
  -w 4 \
  -k uvicorn.workers.UvicornWorker \
  --bind 0.0.0.0:8000
```
