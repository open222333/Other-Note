# Claude Code 專案指令

## 常用筆記索引

更新筆記前先查這裡，直接定位正確檔案。

| 關鍵字 | 筆記路徑 |
|---|---|
| Apache、httpd、apache2、VirtualHost | `03_伺服器服務/WebServer(網站伺服器)/Apache/Apache_筆記.md` |
| MySQL、mysqldump、my.cnf | `03_伺服器服務/.../MySQL/MySQL_筆記.md` |
| XtraBackup、全量備份、增量備份 | `03_伺服器服務/.../MySQL/MySQL_工具_Percona_XtraBackup(資料備份的工具).md` |
| ProxySQL、讀寫分離、Hostgroup | `03_伺服器服務/.../MySQL/MySQL_工具_ProxySQL(高性能_高可用性的_MySQL_代理).md` |
| MongoDB、Replica Set、rs.reconfig | `03_伺服器服務/.../MongoDB/MongoDB_筆記_Replica-Set(複本集).md` |
| Redis、maxmemory、CONFIG SET | `03_伺服器服務/.../Redis/Redis_筆記.md` |
| Git、fetch、stash、branch | `00_其他/.../Git(版本紀錄)/Git_筆記.md` |
| Bitbucket、API Token、credential | `00_其他/.../Git(版本紀錄)/Bucket/Bitbucket_API_Token.md` |
| iOS、Xcode、IPA、TestFlight | `00_其他/行動應用程式(Mobile_App)/iOS_建置與打包.md` |
| Android、APK、AAB、Keystore | `00_其他/行動應用程式(Mobile_App)/Android_建置與打包.md` |
| Capacitor、cap sync、WebView | `00_其他/行動應用程式(Mobile_App)/Capacitor_筆記.md` |
| PWA、manifest.json、Service Worker | `00_其他/行動應用程式(Mobile_App)/PWA_Progressive_Web_App.md` |
| React Native、Expo | `00_其他/行動應用程式(Mobile_App)/React_Native_筆記.md` |
| Flutter、Dart、Widget | `00_其他/行動應用程式(Mobile_App)/Flutter_筆記.md` |
| GPU 雲端市場、選擇建議、品牌比較 | `00_其他/.../LLM/GPU雲端市場/GPU雲端市場_總覽與選擇建議.md` |
| Vast.ai、vastai CLI | `00_其他/.../LLM/GPU雲端市場/Vast.ai.md` |
| RunPod、Serverless GPU | `00_其他/.../LLM/GPU雲端市場/RunPod.md` |
| Lambda Labs、A100、H100 | `00_其他/.../LLM/GPU雲端市場/Lambda_Labs.md` |
| Salad、SaladCloud、分散式 GPU | `00_其他/.../LLM/GPU雲端市場/Salad.md` |
| Paperspace、Gradient Notebook | `00_其他/.../LLM/GPU雲端市場/Paperspace.md` |
| CoreWeave、企業 GPU、k8s | `00_其他/.../LLM/GPU雲端市場/CoreWeave.md` |

> 路徑中 `...` 為省略的中間目錄，實際搜尋用 `find . -name "筆記名稱"`。

## 筆記更新優先規則

新增筆記內容前，先確認 repo 內是否已有主題相符的筆記：

- **已有適合筆記**：在現有筆記中新增章節或補充內容，不建立新檔案
- **無適合筆記**：依下方「新增筆記規則」建立新檔案

## 新增筆記規則

在此 repo 建立新的 Markdown 筆記時，必須依照 `00_sample/` 目錄內的對應範本：

| 筆記類型 | 使用範本 |
|---|---|
| 工具 / 服務（含安裝步驟） | `00_sample/00_common_sample.md` |
| 概念 / 方法論（無安裝步驟） | `00_sample/00_note_sample.md` |
| Python 語法筆記 | `00_sample/python_sample.md` |
| JavaScript 語法筆記 | `00_sample/javascript_sample.md` |
| MySQL 語法筆記 | `00_sample/mysql_sample.md` |
| MongoDB 語法筆記 | `00_sample/mongodb_sample.md` |
| Linux 指令筆記 | `00_sample/linux_sample.md` |
| Go 語法筆記 | `00_sample/golang_sample.md` |
| GitHub 相關筆記 | `00_sample/github_sample.md` |

### 結構要求

- 開頭用 ```` ```...``` ```` 區塊寫一行摘要
- 必須有 `## 目錄` 與 `## 參考資料` section
- 章節標題使用繁體中文，程式碼識別字保留英文
- 工具類筆記包含：安裝（docker-compose / Debian / RedHat / Homebrew / Windows）、配置文檔、指令

## 現有總覽筆記

| 總覽 | 路徑 |
|---|---|
| WebServer（Nginx / Apache / Gunicorn） | `03_伺服器服務/WebServer(網站伺服器)/WebServer_總覽.md` |
| DatabaseServer（MySQL / MongoDB / Redis / SQLite / Memcached） | `03_伺服器服務/DatabaseServer(資料庫伺服器)/DatabaseServer_總覽.md` |
| GPU 雲端市場（Vast.ai / RunPod / Lambda 等） | `00_其他/.../LLM/GPU雲端市場/GPU雲端市場_總覽與選擇建議.md` |
| API 金流串接 | `00_其他/串接API/金流/API_金流串接總覽.md` |
| API 外送平台串接 | `00_其他/串接API/外送平台/API_外送平台串接總覽.md` |

## 總覽筆記規則

當一個目錄下收錄了**同類型的多個工具**時，建立 `XXX_總覽.md` 作為導覽入口。

### 命名

`{目錄名稱}_總覽.md`，放在該目錄的根層。

### 必要章節

| 章節 | 說明 |
|---|---|
| 開頭摘要區塊 | 說明此目錄收錄哪類工具 |
| `## 目錄` | 含錨點連結的 TOC |
| `## 參考資料` | 連結至子目錄各筆記 |
| **工具清單** | 表格列出所有工具，含類型、主要用途、筆記連結 |
| **類型 / 特性比較表** | 以表格對比各工具的核心差異（架構、效能、適用情境等） |
| **各工具說明** | 每個工具一節，說明定位、適用 / 不適用情境，有子工具時列出生態連結表 |
| **需求選擇建議** | 以情境為列，對應推薦工具，說明理由 |

### 選填章節（視工具性質加入）

| 章節 | 適用時機 |
|---|---|
| **HA / 叢集方案總覽** | 工具有多種 HA 或叢集部署模式時（資料庫、快取） |
| **常見部署組合** | 工具通常需要搭配其他工具使用時（Web Server、Application Server） |
| **角色區分** | 目錄內工具分屬不同層次（如 Web Server vs App Server）時 |
