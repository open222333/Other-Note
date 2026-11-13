# Claude Skill（自訂指令）

```
Claude Skill（自訂技能）的放置位置、檔案格式與呼叫方式。
```

## 目錄

- [Claude Skill（自訂指令）](#claude-skill自訂指令)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [什麼是 Skill](#什麼是-skill)
  - [放置位置](#放置位置)
  - [檔案格式](#檔案格式)
    - [Frontmatter 可用欄位](#frontmatter-可用欄位)
  - [呼叫方式](#呼叫方式)
  - [接收引數](#接收引數)
  - [動態注入指令輸出](#動態注入指令輸出)
  - [範例：域名比對 Skill](#範例域名比對-skill)
  - [公開 Skill：避免 AI 用舊技術寫前端](#公開-skill避免-ai-用舊技術寫前端)
    - [問題成因](#問題成因)
    - [即時查文件（Context7 等）](#即時查文件context7-等)
    - [官方廠商 Skill](#官方廠商-skill)
    - [框架官方 llms.txt](#框架官方-llmstxt)
    - [不靠 Skill 的做法](#不靠-skill-的做法)
    - [選用注意事項](#選用注意事項)

## 參考資料

[Claude(AI) 筆記](Claude(AI).md)

[skills.sh（公開 Skill 目錄）](https://skills.sh/topic/react)

[Context7：Up-to-Date Documentation for AI Coding Agents](https://context7.com/up-to-date-documentation-for-coding-agents)

## 什麼是 Skill

Skill 是 Claude Code 的自訂斜線指令（slash command），將常用的工作流程封裝成 `.md` 檔，之後輸入 `/指令名稱` 即可重複使用。

---

## 放置位置

| 範圍 | 路徑 |
|---|---|
| 所有專案共用（全域） | `~/.claude/commands/skill-name.md` |
| 單一專案專用 | `.claude/commands/skill-name.md` |

> 檔案名稱即為指令名稱，例如 `check-domain.md` → `/check-domain`

---

## 檔案格式

```markdown
---
description: 這個 skill 的說明（Claude 用來判斷何時自動呼叫）
---

指令的具體說明或步驟內容...
```

### Frontmatter 可用欄位

| 欄位 | 說明 |
|---|---|
| `description` | 指令說明，Claude 會依此判斷是否自動呼叫 |
| `argument-hint` | 輸入提示，例如 `"[檔案名稱]"` |
| `allowed-tools` | 預先授權的工具，不需每次確認 |

---

## 呼叫方式

```
/skill-name
/skill-name 參數1 參數2
```

---

## 接收引數

在 md 內使用 `$ARGUMENTS` 接收傳入的參數：

```markdown
---
description: 分析指定檔案
---

請分析 $ARGUMENTS 中的內容...
```

也可以用索引取得個別參數：

| 變數 | 說明 |
|---|---|
| `$ARGUMENTS` | 所有引數（完整字串） |
| `$0`, `$1`, `$2` | 第 1、2、3 個引數 |

---

## 動態注入指令輸出

用 `` !`shell指令` `` 在執行前先跑 shell，將結果嵌入提示：

```markdown
---
description: 分析目前 PR 差異
---

PR 差異內容：
!`gh pr diff`

請根據以上差異說明變更重點。
```

---

## 範例：域名比對 Skill

```markdown
---
description: 比對域名清單與 Excel 總表，輸出用途及子域名使用狀態
argument-hint: "[xlsx檔] [txt檔]"
---

請執行以下域名比對分析：

1. 讀取 $1（Excel 域名總表）與 $2（域名清單 txt）
2. 從 `域名總表` sheet 取得每個域名的「產品/用途」（第4欄）
3. 搜尋其餘所有 sheet，找出各域名的子域名（含 `.domain.com` 的儲存格）
4. 判斷子域名儲存格是否有刪除線（strike = True 表示未使用）

分類規則：
- 有刪除線的域名：有取り消し線子域名，且完全沒有無刪除線子域名
- 其餘皆列為「無刪除線（使用中）」

輸出格式：
=== 有刪除線的域名 ===
domain.com (用途) | 刪除線: sheet名稱

=== 無刪除線（使用中）===
domain.com (用途)
```

## 公開 Skill：避免 AI 用舊技術寫前端

> 適用時間：2026-11 整理；Skill 目錄變動快，安裝前以實際頁面為準。

### 問題成因

- AI 偏向訓練資料中數量最多的寫法；舊教學累積多年，新寫法資料少
- 知識截止日之後的新版本不知道，只能照舊版印象寫
- 常見症狀：Create React App、class component、Tailwind v3 的 `tailwind.config.js`、React Router v5 `<Switch>`、React Query v3 寫法
- 實際多半不是「很舊」，而是在**最新一兩個大版本**上偏保守或用錯 API

### 即時查文件（Context7 等）

最有效：寫程式前先抓**指定套件、指定版本**的最新文件給 AI 參考。

| 名稱 | 類型 | 連結 |
|---|---|---|
| Context7（Upstash） | MCP 伺服器 | [官方說明](https://context7.com/up-to-date-documentation-for-coding-agents)、[mcpservers.org](https://mcpservers.org/servers/upstash/context7-mcp) |
| context7（社群包成 Skill） | Skill | [agentskills.so](https://agentskills.so/skills/julianobarbosa-claude-code-skills-context7)、[context7-auto-research](https://skillstore.io/skills/sickn33-context7-auto-research) |
| library-docs | Skill | [skills.sh](https://skills.sh/pfangueiro/claude-code-agents/library-docs) |
| documentation-lookup | Skill | [連結](https://killer-skills-3vi.pages.dev/en/skills/affaan-m/everything-claude-code/documentation-lookup) |
| up-to-date | Skill | [tessl](https://tessl.io/registry/skills/github/LeonMelamud/up-to-date-skill/up-to-date) |
| staying-current | Skill | [claudedirectory](https://claudedirectory.org/skills/ultraship-staying-current) |

### 官方廠商 Skill

品質較可靠，優先選用：

| Skill | 用途 | 連結 |
|---|---|---|
| shadcn 官方 skill | shadcn/ui 官方 repo 提供，依新版寫法加元件 | [skills.sh](https://www.skills.sh/shadcn/ui/shadcn) |
| Vercel react-best-practices | Vercel Labs 的 React 現代寫法規範 | [skills.sh](https://www.skills.sh/vercel-labs/agent-skills/vercel-react-best-practices)、[Vercel 文件](https://examples.vercel.com/docs/agent-resources/skills) |

### 框架官方 llms.txt

免安裝，直接叫 AI「先讀這個網址再寫」：

- [TanStack llms-full.txt](https://tanstack.com/llms-full.txt)
- [TanStack Router LLM 支援說明](https://tanstack.com/router/v1/docs/framework/react/llm-support)
- 其他框架：在官網搜尋 `llms.txt`

### 不靠 Skill 的做法

- 需求中**直接指定技術與版本**（例如「Vite + React 19 + Tailwind v4」）
- 先讓 AI 讀專案的 `package.json` 確認實際版本
- 用腳手架建專案骨架，版本必定是最新：

```bash
npm create vite@latest my-app -- --template react-ts
```

- 寫完跑 typecheck 與 build：舊 API 用在新版套件通常會直接報錯

```bash
npx tsc -b && npm run build
```

### 選用注意事項

- 推薦組合：**Context7 + 專案常用框架的官方 Skill**
- 很多網站是 Skill 目錄 / 鏡像，同一個 Skill 有多份，品質落差大；優先選官方來源（shadcn、Vercel Labs、Upstash）
- **安裝前先讀 SKILL.md**：Skill 內容會成為 AI 的工作指示，來路不明的可能夾帶不想要的指令
