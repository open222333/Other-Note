# Other-Note

```
個人技術筆記知識庫（繁體中文）：工具、程式語言、作業系統、伺服器服務、雲端平台的安裝、設定、指令與排錯紀錄。
找筆記：先看下方「筆記索引」，找不到再用分類地圖縮小範圍搜尋。
```

## 目錄

- [Other-Note](#other-note)
  - [目錄](#目錄)
  - [怎麼找筆記](#怎麼找筆記)
  - [目錄結構](#目錄結構)
  - [參考資料](#參考資料)
- [筆記索引](#筆記索引)
  - [分類地圖](#分類地圖)
  - [常用筆記索引](#常用筆記索引)
  - [現有總覽筆記](#現有總覽筆記)

## 怎麼找筆記

1. 先看下方「[筆記索引](#筆記索引)」：常查主題直接對應到筆記，另有分類地圖與總覽筆記清單。
2. 索引沒有 → 依分類地圖縮小目錄後再搜尋。

## 目錄結構

| 目錄 | 內容 |
|---|---|
| `00_sample/` | 筆記範本與 [筆記規範](00_sample/00_筆記規範.md) |
| `00_概念/` | 概念、方法論（網路、資安、數學、硬體、工作流程…） |
| `00_其他(工具_應用程式_網站)/` | 工具、應用程式、網站、LLM |
| `01_程式語言/` | 各語言筆記（`Python/Python_Code/` 是範例程式，不是筆記） |
| `02_作業系統/` | Linux / macOS / Windows |
| `03_伺服器服務/` | 資料庫、Web Server、郵件、VPN、監控… |
| `04_雲端平台(CloudPlatform)/` | AWS / GCP / Linode 等 |
| `05_樹莓派/` | Raspberry Pi |

## 參考資料

[CLAUDE.md（AI 協作時的行為規則）](CLAUDE.md)

[筆記規範（範本、命名、結構、總覽規則）](00_sample/00_筆記規範.md)

# 筆記索引

> 更新筆記前先查這裡，直接定位正確檔案，不做全目錄掃描。
> **維護規則**：新增、移動、改名筆記後，必須同步更新本節對應列；路徑失效的列直接修正或刪除。
> 連結一律寫 repo 根目錄起的完整相對路徑，禁止縮寫目錄名；路徑含空格時用角括號包住：`[名稱](<路徑 含空格.md>)`。

## 分類地圖

索引查不到時，用這裡縮小 `find` / `grep` 範圍。

| 頂層目錄 | 主要子分類 |
|---|---|
| [00_概念/](00_概念/) | 根層、網路、資安、應用流程、協議 / 多媒體 / 數學 / 深度學習 / 硬體 |
| [00_其他(工具_應用程式_網站)/](00_其他(工具_應用程式_網站)/) | LLM、DevOps、IDE、串接API、CDN、域名、行動應用程式、API、PerformanceMonitor、爬蟲、HTML生成 / 影片 / 影像、筆記軟體 / NAS、其餘 |
| [01_程式語言/](01_程式語言/) | Python、JavaScript、PHP、Algorithm、AI、Java、CLI、Golang、SQL、C / Markdown、Lua / Mojo / Ruby / Rust / TypeScript |
| [02_作業系統/](02_作業系統/) | Unix(Linux)、DOS(Windows) |
| [03_伺服器服務/](03_伺服器服務/) | DatabaseServer、Vector_Database、WebServer、PerformanceMonitor、MailServer、FileServer、Proxy / VPN、Firebase、LDAP、根層 |
| [04_雲端平台(CloudPlatform)/](04_雲端平台(CloudPlatform)/) | AWS、Linode、GCP / Baidu / Tencent、根層：總覽 |
| [05_樹莓派/](05_樹莓派/) | 根層 |

## 常用筆記索引

| 關鍵字 | 筆記 |
|---|---|
| Apache、httpd、apache2、VirtualHost | [Apache_筆記](03_伺服器服務/WebServer(網站伺服器)/Apache/Apache_筆記.md) |
| Nginx、反向代理、nginx.conf | [Nginx_筆記](03_伺服器服務/WebServer(網站伺服器)/Nginx/Nginx_筆記.md) |
| MySQL、mysqldump、my.cnf | [MySQL_筆記](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_筆記.md) |
| max_connections 自動調降、open_files_limit、LimitNOFILE | [MySQL_筆記](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_筆記.md)（見「max_connections 被自動調降」章節） |
| MySQL 主從、Replication、Master-Slave | [MySQL_筆記_Replication(Master-Slave_主從)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_筆記_Replication(Master-Slave_主從).md) |
| MySQL Cluster、叢集 | [MySQL_筆記_Cluster(叢集)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_筆記_Cluster(叢集).md) |
| XtraBackup、全量備份、增量備份 | [MySQL_工具_Percona_XtraBackup(資料備份的工具)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_工具_Percona_XtraBackup(資料備份的工具).md) |
| ProxySQL、讀寫分離、Hostgroup | [MySQL_工具_ProxySQL(高性能_高可用性的_MySQL_代理)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_工具_ProxySQL(高性能_高可用性的_MySQL_代理).md) |
| Sequel Ace、macOS MySQL GUI、SSH Tunnel | [MySQL_工具_Sequel_Ace(macOS_MySQL_GUI工具)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_工具_Sequel_Ace(macOS_MySQL_GUI工具).md) |
| ER 圖、資料表設計、dbdiagram、Workbench | [資料表設計工具_總覽](03_伺服器服務/DatabaseServer(資料庫伺服器)/資料表設計工具/資料表設計工具_總覽.md) |
| MongoDB 文件設計、嵌入 vs 引用、Hackolade | [MongoDB_資料表設計工具](03_伺服器服務/DatabaseServer(資料庫伺服器)/資料表設計工具/MongoDB_資料表設計工具.md) |
| MongoDB、find、aggregate、mongodump | [MongoDB_筆記](03_伺服器服務/DatabaseServer(資料庫伺服器)/MongoDB/MongoDB_筆記.md) |
| MongoDB、Replica Set、rs.reconfig | [MongoDB_筆記_Replica-Set(複本集)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MongoDB/MongoDB_筆記_Replica-Set(複本集).md) |
| Mongo 連線診斷、ReplicaSetNoPrimary、No replica set members found、directConnection、mongo_conn_doctor | [MongoDB_筆記_Replica-Set(複本集)](03_伺服器服務/DatabaseServer(資料庫伺服器)/MongoDB/MongoDB_筆記_Replica-Set(複本集).md)（見「特別工具」章節） |
| Redis、maxmemory、CONFIG SET | [Redis_筆記](03_伺服器服務/DatabaseServer(資料庫伺服器)/Redis/Redis_筆記.md) |
| Redis Cluster、分散式部署 | [Redis_筆記_Redis_Cluster(分散式部署模式)](03_伺服器服務/DatabaseServer(資料庫伺服器)/Redis/Redis_筆記_Redis_Cluster(分散式部署模式).md) |
| Elasticsearch、Kibana、index、query DSL | [Elasticsearch(搜尋引擎)_&_Kibana(Elasticsearch用戶界面)_筆記](03_伺服器服務/Vector_Database(向量資料庫)/Elastic_Stack/Elasticsearch(搜尋引擎)_&_Kibana(Elasticsearch用戶界面)_筆記.md) |
| Samba、smb.conf、網路芳鄰 | [Samba(UNIX系列_與_微軟Windows_溝通)_筆記](03_伺服器服務/FileServer(檔案伺服器)/Samba(UNIX系列_與_微軟Windows_溝通)_筆記.md) |
| Poste.io、郵件伺服器、新增網域、DKIM、SPF、DMARC、PTR 反解 | [Poste.io(郵件伺服器管理工具)](03_伺服器服務/MailServer(郵箱伺服器)/Poste.io(郵件伺服器管理工具).md) |
| New Relic、NRQL、Query Builder、NerdGraph、匯出 CSV、TransactionError | [New_Relic(雲端應用程式效能監控)](03_伺服器服務/PerformanceMonitor(效能監控)/New_Relic/New_Relic(雲端應用程式效能監控).md) |
| Git、fetch、stash、branch、submodule | [Git_筆記](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Git(版本紀錄)/Git_筆記.md) |
| Bitbucket、API Token、credential | [Bitbucket_API_Token(認證設定)](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Git(版本紀錄)/Bucket(Bitbucket)/Bitbucket_API_Token(認證設定).md) |
| App Password 停用、410、CHANGE-3222、osxkeychain | [Bitbucket_API_Token(認證設定)](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Git(版本紀錄)/Bucket(Bitbucket)/Bitbucket_API_Token(認證設定).md)（見「例外狀況」章節） |
| Telegram、Bot API、BotFather、Bot Token 外洩 | [Telegram(通訊軟體)](00_其他(工具_應用程式_網站)/通訊軟體(即時通訊)/Telegram(通訊軟體).md) |
| Docker、docker compose、Dockerfile | [Docker_筆記](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Docker(開放平臺軟體)/Docker_筆記.md) |
| Kubernetes、K8s、kubectl、kubeadm | [Kubernetes_K8S(DevOps平台)](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Kubernetes_K8S(DevOps平台).md) |
| k3s、輕量 K8s | [k3s(輕量K8s)](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/k3s(輕量K8s).md) |
| CI/CD、Jenkins、CircleCI、Bamboo、TeamCity、Travis CI | [CI_CD_總覽](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/CI_CD_-_持續整合(Continuous_Integration)_持續交付(Continuous_Delivery)_工具/CI_CD_總覽.md) |
| VSCode、extension、settings.json | [VSCode_筆記](00_其他(工具_應用程式_網站)/IDE(編譯工具)/VSCode(程式碼編輯器)/VSCode_筆記.md) |
| Claude、Claude Code、模型系列、API、Claude 系列筆記入口 | [Claude(AI)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/Claude(AI).md) |
| CLAUDE.md、專案設定檔、讀取順序 | [CLAUDE_md(專案設定檔)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/CLAUDE_md(專案設定檔).md) |
| Claude Code settings.json、權限、hooks | [Claude_Code_專案設定(CLI設定)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/Claude_Code_專案設定(CLI設定).md) |
| Claude Skill、自訂技能、SKILL.md | [Claude_Skill(自訂技能)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/Claude_Skill(自訂技能).md) |
| Claude Dispatch、Agent SDK、子 Agent 並行 | [Claude_Dispatch(多Agent分派)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/Claude_Dispatch(多Agent分派).md) |
| Gemini、Google AI Studio、Gemini API | [Gemini(Google_AI)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Gemini(Google_AI)/Gemini(Google_AI).md) |
| Chrome DevTools、Network 分頁、追蹤 API 呼叫、SSL 證書 | [Chrome(Google瀏覽器)](00_其他(工具_應用程式_網站)/影像工具(圖片處理)/瀏覽器開發者工具(偵錯工具)/Chrome(Google瀏覽器).md) |
| iOS、Xcode、IPA、TestFlight | [iOS_建置與打包(行動App)](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/iOS_建置與打包(行動App).md) |
| Android、APK、AAB、Keystore | [Android_建置與打包(行動App)](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/Android_建置與打包(行動App).md) |
| Android 抓包、HTTP Toolkit、BlueStacks、Network Profiler、certificate pinning | [Android_網路抓包(HTTP攔截除錯)](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/Android_網路抓包(HTTP攔截除錯).md) |
| Capacitor、cap sync、WebView | [Capacitor_筆記](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/Capacitor_筆記.md) |
| PWA、manifest.json、Service Worker | [PWA_Progressive_Web_App(網頁App)](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/PWA_Progressive_Web_App(網頁App).md) |
| React Native、Expo | [React_Native_筆記](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/React_Native_筆記.md) |
| Flutter、Dart、Widget | [Flutter_筆記](00_其他(工具_應用程式_網站)/行動應用程式(Mobile_App)/Flutter_筆記.md) |
| GPU 雲端市場、選擇建議、品牌比較 | [GPU雲端市場_總覽與選擇建議](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/GPU雲端市場_總覽與選擇建議.md) |
| Vast.ai、vastai CLI | [Vast.ai(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/Vast.ai(GPU雲端).md) |
| RunPod、Serverless GPU | [RunPod(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/RunPod(GPU雲端).md) |
| Lambda Labs、A100、H100 | [Lambda_Labs(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/Lambda_Labs(GPU雲端).md) |
| Salad、SaladCloud、分散式 GPU | [Salad(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/Salad(GPU雲端).md) |
| Paperspace、Gradient Notebook | [Paperspace(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/Paperspace(GPU雲端).md) |
| CoreWeave、企業 GPU、k8s | [CoreWeave(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/CoreWeave(GPU雲端).md) |
| TensorDock、低價 GPU | [TensorDock(GPU雲端)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/TensorDock(GPU雲端).md) |
| BlackHole、虛擬音訊裝置、macOS 錄製系統聲音 | [BlackHole(虛擬音訊裝置)](00_其他(工具_應用程式_網站)/影片工具(影音處理)/BlackHole(虛擬音訊裝置).md) |
| PLAUD、錄音轉寫、Google AI Studio 逐字稿、剪映/Vrew 字幕 | [PLAUD_錄音轉寫(免費AI逐字稿工具)](00_其他(工具_應用程式_網站)/影片工具(影音處理)/PLAUD_錄音轉寫(免費AI逐字稿工具).md) |
| WebP、cwebp、圖片壓縮、壓到指定檔案大小以下 | [cwebp(WebP圖片壓縮工具)](00_其他(工具_應用程式_網站)/影像工具(圖片處理)/cwebp(WebP圖片壓縮工具).md) |
| Scrape.do、Web Scraping API、繞過 WAF/CAPTCHA、住宅代理、Proxy Mode | [Scrape.do(反爬蟲代理API)](00_其他(工具_應用程式_網站)/爬蟲相關(網路爬蟲)/反爬蟲工具(繞過反爬蟲)/Scrape.do(反爬蟲代理API)/Scrape.do(反爬蟲代理API).md) |
| FlareSolverr、繞過 Cloudflare 5秒盾、自架反爬蟲代理 | [FlareSolverr(處理網站的反爬蟲機制)](00_其他(工具_應用程式_網站)/爬蟲相關(網路爬蟲)/反爬蟲工具(繞過反爬蟲)/FlareSolverr(反爬蟲代理)/FlareSolverr(處理網站的反爬蟲機制).md) |
| dev_note、開發紀錄、bug 追查紀錄、分類 fix/feat/data/investigate、嚴重度 P0~P3、章節骨架 | [工作流程_-_DevNote(開發紀錄撰寫規範)](00_概念/工作流程_-_DevNote(開發紀錄撰寫規範).md) |
| Commit 分類、Conventional Commits、commit type | [Commit_分類(內容分類建議)](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Git(版本紀錄)/Commit_分類(內容分類建議).md) |
| SaaS、IaaS/PaaS/SaaS、多租戶、Silo/Pool/Bridge、SaaS 成熟度模型 | [概念_-_SaaS(軟體即服務)](00_概念/概念_-_SaaS(軟體即服務).md) |
| AI 自動化、BDD、RPA、n8n、MCP、Agent、RAG、成熟度分級 | [技術_-_AI自動化(AI_Automation)](00_概念/技術_-_AI自動化(AI_Automation).md) |
| AGENTS.md、AI 代理指令檔、Codex/Cursor/Gemini CLI 共用規則、AGENTS.md vs CLAUDE.md | [AGENTS_md(AI代理指令檔)](01_程式語言/AI/AGENTS_md(AI代理指令檔).md) |
| Claude Code subagent、主機巡檢、多主機異常檢查、claude -p 排程、.claude/agents | [Claude_Code_主機巡檢(多主機異常檢查)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/Claude_Code_主機巡檢(多主機異常檢查).md) |
| 主機巡檢系統設計文件、告警狀態機、alert_key 去重、資料模型（PostgreSQL / MySQL / SQLite / MongoDB）、介面契約、驗收測試 | [AI_主機巡檢系統設計(系統設計文件)](01_程式語言/AI/AI_主機巡檢系統設計(系統設計文件).md) |
| Claude API tool use、主機巡檢系統 Python 參考實作、外掛式檢查項目 / 回報方式、Telegram / webhook 告警 | [Claude_API_主機巡檢系統(可擴充架構)](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/Claude_API_主機巡檢系統(可擴充架構).md) |
| 多人 AI 協作開發、Multi-Agent、AI IDE 共用 | [AI_多人協作開發架構(團隊AI整合)](01_程式語言/AI/AI_多人協作開發架構(團隊AI整合).md) |
| 金流串接、綠界 ECPay、藍新 NewebPay、歐付寶、電子發票 | [API_金流串接總覽](00_其他(工具_應用程式_網站)/串接API(第三方API串接)/金流(金流API)/API_金流串接總覽.md) |
| 外送串接、UberEats、Foodpanda | [API_外送平台串接總覽](00_其他(工具_應用程式_網站)/串接API(第三方API串接)/外送平台(外送API)/API_外送平台串接總覽.md) |
| Linode VPC、子網路、VLAN、Private IP、1:1 NAT、手動指定 VPC IP、VPC 防火牆搭配、不同 VPC 互連、CIDR 網段規劃、Invalid filterable vpc_type、not in the allowed VPC ranges、linode-cli vpcs | [Linode_VPC(私有網路)](04_雲端平台(CloudPlatform)/Linode/Linode_VPC(私有網路).md) |
| Linode Cloud Firewall、Linode Interfaces vs Configuration Profile、主機防火牆搭配 | [Linode_筆記](04_雲端平台(CloudPlatform)/Linode/Linode_筆記.md)（見「網路介面」「Firewall」章節） |
| 雲端主機比價、Hetzner/Vultr/DigitalOcean/Azure VM | [04_雲端平台(CloudPlatform)_總覽](04_雲端平台(CloudPlatform)/04_雲端平台(CloudPlatform)_總覽.md) |
| Fail2Ban、封鎖 IP、jail.local、unbanip、ignoreip、永久封鎖、bantime -1、dbpurgeage | [Linux_工具_Fail2Ban(防止暴力破解攻擊)](02_作業系統/Unix/Linux/Linux_工具/Linux_防止暴力破解攻擊/Linux_工具_Fail2Ban(防止暴力破解攻擊).md) |
| SSH 暴力破解、Failed password、關閉密碼登入、PasswordAuthentication、攻擊 IP 排行 | [Linux_工具_OpenSSL_OpenSSH(連線、密鑰)](02_作業系統/Unix/Linux/Linux_工具/Linux_SSH_工具/Linux_工具_OpenSSL_OpenSSH(連線、密鑰).md)（見「排查 SSH 暴力破解」章節） |
| 敏感檔案加密選擇、GPG、7-Zip、SOPS、git-crypt、LUKS、VeraCrypt、gocryptfs、restic、密碼管理器 | [資安_-_敏感檔案加密(加密工具比較與選擇)](00_概念/資安/資安_-_敏感檔案加密(加密工具比較與選擇).md) |
| age、檔案加密、公鑰加密、age-keygen、加密備份、SSH 金鑰加密檔案 | [Linux_工具_age(檔案公鑰加密)](02_作業系統/Unix/Linux/Linux_工具/Linux_加密/Linux_工具_age(檔案公鑰加密).md) |
| iptables、netfilter-persistent、rules.v4、防火牆規則 | [Linux_工具_iptables_ip6tables(管理網路封包的處理和轉發)](02_作業系統/Unix/Linux/Linux_工具/Linux_防火牆部分/Linux_工具_iptables_ip6tables(管理網路封包的處理和轉發).md) |
| 網路診斷、ping、dig、curl、mtr、netstat、tcpdump、iftop | [Linux_網路狀態部分_總覽](02_作業系統/Unix/Linux/Linux_工具/Linux_網路狀態部分/Linux_網路狀態部分_總覽.md) |
| 磁碟、掛載、NFS、sshfs、rsync、swap | [Linux_磁碟與檔案系統部分_總覽](02_作業系統/Unix/Linux/Linux_工具/Linux_磁碟與檔案系統部分/Linux_磁碟與檔案系統部分_總覽.md) |
| systemctl、systemd、service、enable、unit 類型 | [Linux_工具_systemctl(系統管理工具)](02_作業系統/Unix/Linux/Linux_工具/Linux_基本/Linux_工具_systemctl(系統管理工具).md) |
| sh、ash、dash、/bin/sh、chsh、登入 shell | [Unix_Shell(UNIX作業系統_殼層與命令列介面)](02_作業系統/Unix/Unix_Shell(殼層與命令列介面)/Unix_Shell(UNIX作業系統_殼層與命令列介面).md) |
| 磁碟滿了、df、du、ncdu、tune2fs 保留空間、lsof +L1 | [Linux_工具_df_du(硬碟空間資訊)](02_作業系統/Unix/Linux/Linux_工具/Linux_磁碟與檔案系統部分/Linux_工具_df_du(硬碟空間資訊).md)（見「例外狀況」） |
| Linux 基本、常用指令 | [Linux_基本_筆記](02_作業系統/Unix/Linux/Linux_基本_筆記.md) |
| CentOS、yum、firewalld | [Linux_CentOS_筆記](02_作業系統/Unix/Linux/Linux_版本/CentOS/Linux_CentOS_筆記.md) |
| Windows、cmd、系統設定 | [Windows_筆記](02_作業系統/DOS(Disk_Operating_System)/Windows/Windows_筆記.md) |
| macOS、mac 常用指令、環境變數、NVRAM | [MacOS_筆記](02_作業系統/Unix/Mac/MacOS_筆記.md) |
| iCloud Drive、移除下載項目、Remove Download、Optimize Mac Storage | [MacOS_筆記](02_作業系統/Unix/Mac/MacOS_筆記.md)（見「iCloud Drive 檔案管理」章節）|
| System Data、mac 儲存空間爆滿、tmutil 本機快照、Docker.raw | [MacOS_筆記](02_作業系統/Unix/Mac/MacOS_筆記.md)（見「狀況 System Data 佔用大量儲存空間」章節）|
| set -euo pipefail、嚴格模式、errexit、nounset、pipefail、unbound variable | [Bash_基本_筆記](01_程式語言/CLI(Command-Line_Interface)/Bash/Bash_基本_筆記.md)（見「嚴格模式 set -euo pipefail」章節） |
| Bash、shell script、變數、迴圈 | [Bash_基本_筆記](01_程式語言/CLI(Command-Line_Interface)/Bash/Bash_基本_筆記.md) |
| Bash 腳本規範、preflight、設定區、--resend/--cleanup | [Bash_腳本規範](01_程式語言/CLI(Command-Line_Interface)/Bash/Bash_腳本規範.md) |
| Python 腳本規範、argparse、logging、venv 慣例 | [Python_腳本規範](01_程式語言/Python/Python_腳本規範.md) |
| JavaScript 腳本規範、Node CLI、async、ESLint 慣例 | [JavaScript_腳本規範](01_程式語言/JavaScript/JavaScript_腳本規範.md) |
| PHP 腳本規範、PSR-12、Composer、strict_types 慣例 | [PHP_腳本規範](01_程式語言/PHP/PHP_腳本規範.md) |
| Golang 腳本規範、gofmt、go vet、錯誤包裝慣例 | [Golang_腳本規範](01_程式語言/Golang/Golang_腳本規範.md) |
| SQL 腳本規範、先SELECT再UPDATE、交易、migration 慣例 | [SQL_腳本規範](01_程式語言/SQL/SQL_腳本規範.md) |
| vim、貼上縮排跑掉、autoindent、set paste、pastetoggle | [Linux_工具_vim(文字編輯器)](02_作業系統/Unix/Linux/Linux_工具/Linux_文本編碼/Linux_工具_vim(文字編輯器).md) |
| Flask、Werkzeug、WSGI、Flask 與 Werkzeug 的關係 | [Python_模組_Flask(網站框架)](01_程式語言/Python/Python_第三方模組/Python_Web應用框架(網站)/Python_Web_Flask(網站)/Python_模組_Flask(網站框架).md) |
| Celery、排程、非同步任務、worker、beat | [Python_模組_celery(排程)](01_程式語言/Python/Python_第三方模組/Python_模組_celery(排程).md) |
| Python 基本語法 | [Python_基本_筆記](01_程式語言/Python/Python_基本_筆記.md) |
| Anaconda、conda、虛擬環境 | [Python_工具_anacodna(虛擬環境)_筆記](01_程式語言/Python/Python_工具_anacodna(虛擬環境)_筆記.md) |
| Python 爬蟲、requests、BeautifulSoup | [Python_基本_爬蟲筆記](01_程式語言/Python/Python_第三方模組/Python_Crawler(爬蟲)/Python_基本_爬蟲筆記.md) |
| puppeteer、無頭瀏覽器、截圖、exposeFunction | [JavaScript_Node_模組_puppeteer(網頁自動化)](01_程式語言/JavaScript/NodeJs(執行環境)/JavaScript_Node_模組_puppeteer(網頁自動化).md) |
| JavaScript 基本語法 | [JavaScript_筆記](01_程式語言/JavaScript/JavaScript_筆記.md) |
| PHP 漏洞、CVE、RCE、PHP2Shell、CVE-2026-109618、CVE-2024-4577、php-cgi、支援分支 | [PHP_資安漏洞紀錄(CVE與緊急應變)](01_程式語言/PHP/PHP_資安漏洞紀錄(CVE與緊急應變).md) |
| PHP 基本語法 | [PHP_筆記](01_程式語言/PHP/PHP_筆記.md) |
| Laravel、artisan、Eloquent | [PHP_Laravel_筆記](01_程式語言/PHP/Laravel/PHP_Laravel_筆記.md) |
| Algorithm、演算法基本 | [Algorithm_基本_筆記](01_程式語言/0_Algorithm(演算法)/Algorithm_基本_筆記.md) |
| 網路概念、OSI、TCP/IP | [NetWork_筆記](00_概念/網路/NetWork_筆記.md) |

## 現有總覽筆記

| 總覽 | 筆記 |
|---|---|
| WebServer（Nginx / Apache / Gunicorn） | [WebServer_總覽](03_伺服器服務/WebServer(網站伺服器)/WebServer_總覽.md) |
| DatabaseServer（MySQL / MongoDB / Redis / SQLite / Memcached） | [DatabaseServer_總覽](03_伺服器服務/DatabaseServer(資料庫伺服器)/DatabaseServer_總覽.md) |
| 資料表設計工具（Workbench / dbdiagram / Hackolade） | [資料表設計工具_總覽](03_伺服器服務/DatabaseServer(資料庫伺服器)/資料表設計工具/資料表設計工具_總覽.md) |
| GPU 雲端市場（Vast.ai / RunPod / Lambda 等） | [GPU雲端市場_總覽與選擇建議](00_其他(工具_應用程式_網站)/LLM(大型語言模型)/GPU雲端市場(GPU_Cloud_Marketplace)/GPU雲端市場_總覽與選擇建議.md) |
| 金流串接（綠界 / 藍新 / 歐付寶 / 電子發票等） | [API_金流串接總覽](00_其他(工具_應用程式_網站)/串接API(第三方API串接)/金流(金流API)/API_金流串接總覽.md) |
| 外送平台串接（UberEats / Foodpanda） | [API_外送平台串接總覽](00_其他(工具_應用程式_網站)/串接API(第三方API串接)/外送平台(外送API)/API_外送平台串接總覽.md) |
| 雲端平台比較（AWS / GCP / Linode / Hetzner 等） | [04_雲端平台(CloudPlatform)_總覽](04_雲端平台(CloudPlatform)/04_雲端平台(CloudPlatform)_總覽.md) |
| CI/CD（Jenkins / CircleCI / GitLab CI / Bamboo / TeamCity / Travis CI） | [CI_CD_總覽](00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/CI_CD_-_持續整合(Continuous_Integration)_持續交付(Continuous_Delivery)_工具/CI_CD_總覽.md) |
| Linux 網路工具（ping / dig / curl / mtr / netstat / tcpdump 等） | [Linux_網路狀態部分_總覽](02_作業系統/Unix/Linux/Linux_工具/Linux_網路狀態部分/Linux_網路狀態部分_總覽.md) |
| Linux 磁碟與檔案系統（df / du / mount / NFS / sshfs / rsync） | [Linux_磁碟與檔案系統部分_總覽](02_作業系統/Unix/Linux/Linux_工具/Linux_磁碟與檔案系統部分/Linux_磁碟與檔案系統部分_總覽.md) |
| 域名商比較（Cloudflare / Porkbun / Namecheap / GoDaddy 等） | [域名商(比較與選擇)](00_其他(工具_應用程式_網站)/域名(Domain_Name)/域名商(比較與選擇).md) |
