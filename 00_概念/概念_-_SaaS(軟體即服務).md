# 概念 - SaaS(軟體即服務)

```
SaaS（Software as a Service，軟體即服務）：使用者透過瀏覽器或 API 使用供應商架設在雲端的應用程式，
不用自己安裝、維運，也不管底層的伺服器、作業系統、儲存；通常以訂閱方式付費。
常見例子：Gmail、Google Workspace、Microsoft 365、Slack、Notion、Salesforce、Shopify。
```

## 目錄

- [概念 - SaaS(軟體即服務)](#概念---saas軟體即服務)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [定義](#定義)
- [IaaS / PaaS / SaaS 比較](#iaas--paas--saas-比較)
- [SaaS 的核心特性](#saas-的核心特性)
- [多租戶（Multi-tenancy）](#多租戶multi-tenancy)
	- [隔離模式](#隔離模式)
- [SaaS 成熟度模型](#saas-成熟度模型)
- [優缺點](#優缺點)

## 參考資料

[NIST SP 800-145 The NIST Definition of Cloud Computing（2011）](https://csrc.nist.gov/pubs/sp/800/145/final)

[NIST Glossary - Software as a Service](https://csrc.nist.gov/glossary/term/software_as_a_service)

[Architecture Strategies for Catching the Long Tail（Chong & Carraro, Microsoft, 2006）](https://learn.microsoft.com/en-us/previous-versions/aa479069(v=msdn.10))

[SaaS Architectures（Gianpaolo Carraro, 2006）](https://learn.microsoft.com/en-us/archive/blogs/gianpaolo/saas-architectures)

[AWS Well-Architected SaaS Lens](https://docs.aws.amazon.com/wellarchitected/latest/saas-lens/saas-lens.html)

[軟體即服務 - 維基百科](https://zh.wikipedia.org/zh-tw/軟體即服務)

# 定義

最常被引用的是美國 NIST SP 800-145（2011）的定義，SaaS 是雲端運算的三種服務模式之一：

```
提供給使用者的能力，是使用供應商在雲端基礎設施上執行的應用程式。
應用程式可以從各種用戶端裝置，透過瀏覽器等精簡用戶端或程式介面存取。
使用者不管理、也不控制底層雲端基礎設施（網路、伺服器、作業系統、儲存，甚至個別應用程式功能），
只能調整有限的、使用者層級的應用程式設定。
```

SaaS 的「定義」是公認的；但「SaaS 怎麼分級、怎麼設計」沒有單一標準，各家（Microsoft、AWS、顧問公司）各有模型。

# IaaS / PaaS / SaaS 比較

| 項目 | IaaS（基礎設施即服務） | PaaS（平台即服務） | SaaS（軟體即服務） |
|---|---|---|---|
| 供應商提供 | 虛擬機、網路、儲存 | 執行環境、資料庫、部署平台 | 完整可用的應用程式 |
| 使用者負責 | 作業系統、中介軟體、程式、資料 | 程式與資料 | 只有資料與帳號設定 |
| 例子 | AWS EC2、Linode、GCP Compute Engine | Heroku、Google App Engine、Firebase | Gmail、Slack、Notion |
| 彈性 / 管理負擔 | 最高 / 最重 | 中 / 中 | 最低 / 最輕 |

# SaaS 的核心特性

| 特性 | 說明 |
|---|---|
| 透過網路存取 | 瀏覽器或 API，不需安裝（或只裝輕量用戶端） |
| 訂閱制計費 | 按月 / 按年、按使用者數或用量，取代一次買斷授權 |
| 多租戶 | 多個客戶（租戶）共用同一套程式與基礎設施，資料彼此隔離 |
| 集中更新 | 供應商統一升級，所有客戶同時用到新版本 |
| 可設定而非客製 | 透過設定（metadata）調整功能、外觀、流程，而不是為每個客戶改程式碼 |
| 可擴展 | 架構要能承受大量客戶同時使用 |

Microsoft（Carraro）把 SaaS 應用架構的三大挑戰稱為「三頭怪」：**多租戶效率、可設定（在多租戶前提下）、可擴展**。

# 多租戶（Multi-tenancy）

租戶（tenant）= 一個客戶組織（公司、團隊），底下可以有多個使用者。

| | 單租戶（Single-tenant） | 多租戶（Multi-tenant） |
|---|---|---|
| 架構 | 每個客戶一套獨立的程式與資料庫 | 所有客戶共用程式，資料以租戶 ID 或獨立資源區隔 |
| 成本 | 高（每個客戶一份資源） | 低（資源共用） |
| 隔離性 | 強 | 依隔離模式而定 |
| 升級 | 要逐套升級 | 一次升級所有客戶 |
| 客製化 | 容易 | 只能靠設定 |

## 隔離模式

AWS SaaS Lens 把資料 / 資源的隔離方式分成三種：

| 模式 | 做法 | 適合 |
|---|---|---|
| Silo（獨立） | 每個租戶獨立資源，例如各自的資料庫 | 法規要求高、大客戶、需要強隔離 |
| Pool（共用） | 所有租戶共用資源，例如同一張表用 `tenant_id` 區分 | 大量中小客戶、追求成本效率 |
| Bridge（混合） | 部分共用、部分獨立，例如共用程式、資料庫依租戶分 schema | 介於兩者之間 |

Pool 模式最常見的風險：查詢忘了加 `tenant_id` 條件，導致 A 客戶看到 B 客戶的資料，必須在框架層（ORM 全域過濾、資料庫 Row-Level Security）強制套用。

# SaaS 成熟度模型

最常被引用的是 Microsoft 2006 年 Chong & Carraro 在《Architecture Strategies for Catching the Long Tail》提出的四級模型：每升一級多具備一項特性（可設定 → 多租戶效率 → 可擴展）。

| Level | 名稱 | 特徵 | 每個客戶 |
|---|---|---|---|
| 1 | Ad Hoc / Custom（客製） | 類似 1990 年代的 ASP 代管模式：每個客戶一套獨立、各自客製的版本 | 獨立程式版本 + 獨立實例 |
| 2 | Configurable（可設定） | 所有客戶同一套程式碼，差異靠設定，但仍各自一個實例 | 同版本、獨立實例 |
| 3 | Configurable, Multi-Tenant-Efficient（可設定 + 多租戶） | 單一實例服務所有客戶，靠設定與資料隔離區分 | 共用同一實例 |
| 4 | Scalable, Configurable, Multi-Tenant-Efficient（可擴展） | 多個相同實例放在負載平衡後面，可依客戶量水平擴展 | 共用一組可擴展的實例池 |

注意：

- 不是每個產品都要做到 Level 4；Level 越高越省成本、越好擴展，但開發難度與隔離風險也越高。
- 成熟度模型不只這一套（例如 Dharmesh Shah 的五級模型），各家名稱與分法不同，**沒有業界統一的「M0～M3」之類編號**；若團隊內部要用自己的編號，記得在文件裡寫清楚對應到哪一套模型。

# 優缺點

| 對象 | 優點 | 缺點 |
|---|---|---|
| 使用者 | 免安裝維運、開通快、初期成本低、隨時用到新版 | 資料放在供應商、依賴網路、客製化受限、長期訂閱費可能更高、有供應商綁定（lock-in）風險 |
| 供應商 | 經常性收入、一套程式服務所有客戶、集中更新、容易收集使用數據 | 多租戶隔離與資安責任重、要承擔全部維運與可用性（SLA）、架構擴展難度高 |
