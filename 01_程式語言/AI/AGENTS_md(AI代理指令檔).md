# AGENTS md(AI代理指令檔)

```
AGENTS.md 是給 AI 程式碼代理（coding agent）看的專案說明檔，可以想成「給 AI 看的 README」。
放在專案根目錄，寫建置/測試指令、程式風格、注意事項等，讓 Codex、Cursor、Gemini CLI、Claude Code 等工具共用同一份指令。
開放格式、純 Markdown、沒有必填欄位，目前由 Linux Foundation 底下的 Agentic AI Foundation 維護。
```

## 目錄

- [AGENTS md(AI代理指令檔)](#agents-mdai代理指令檔)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用途](#用途)
- [支援的工具](#支援的工具)
- [檔案位置與優先順序](#檔案位置與優先順序)
- [建議內容與範例](#建議內容與範例)
- [AGENTS.md 與 CLAUDE.md](#agentsmd-與-claudemd)
  - [Claude Code 讀取規則](#claude-code-讀取規則)
  - [切換讀取模式（Project instructions）](#切換讀取模式project-instructions)
  - [一份檔案給所有工具共用](#一份檔案給所有工具共用)
- [各工具設定](#各工具設定)
- [例外狀況](#例外狀況)
  - [Claude Code 沒有讀到 AGENTS.md](#claude-code-沒有讀到-agentsmd)

## 參考資料

[AGENTS.md 官方網站](https://agents.md/)

[agentsmd/agents.md GitHub](https://github.com/agentsmd/agents.md)

[Claude Code - How Claude remembers your project（AGENTS.md 章節）](https://code.claude.com/docs/en/memory#agents-md)

[CLAUDE.md 筆記](../../00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Claude(AI)/CLAUDE_md(專案設定檔).md)

# 用途

| 檔案 | 讀者 | 內容 |
|---|---|---|
| `README.md` | 人 | 專案介紹、快速開始、貢獻方式 |
| `AGENTS.md` | AI 代理 | 建置/測試指令、程式慣例、安全注意事項等，放進 README 會太雜、或對人類貢獻者不重要的細節 |

- 刻意與 README 分開：讓 AI 有固定、可預期的位置找指令，README 也能保持精簡。
- 不是某家廠商的專屬格式（不像 `CLAUDE.md`、`.cursorrules`），換工具不用重寫一份。

# 支援的工具

原生讀取 AGENTS.md（官網列出的部分）：

```
OpenAI Codex、Google Jules、Cursor、GitHub Copilot coding agent、VS Code、
Zed、Warp、Devin、Windsurf、Amp、Factory、opencode、goose、
JetBrains Junie、RooCode、Kilo Code、Augment Code
```

需要額外設定才會讀：Aider、Gemini CLI（見「各工具設定」）。

Claude Code：v2.1.277 起可以直接讀 `AGENTS.md`（舊版只讀 `CLAUDE.md`，要靠 `@AGENTS.md` 匯入）。

# 檔案位置與優先順序

- 放在 repo 根目錄：`<project>/AGENTS.md`
- 大型 monorepo：每個子專案（package）各放一份，AI 會讀**離正在編輯的檔案最近**的那份，最近的優先。
- 指令衝突時：最近的 AGENTS.md 優先；使用者在對話中直接下的指令又優先於所有檔案。
- 有列出測試指令的話，代理會嘗試自己執行並修到通過才結束任務。

# 建議內容與範例

常見章節：專案概覽、建置與測試指令、程式風格、測試說明、安全注意事項；另外 commit / PR 規範、部署步驟、大型資料集位置等，「會跟新進同事交代的事」都可以寫。

```markdown
# AGENTS.md

## Setup commands
- Install deps: `pnpm install`
- Start dev server: `pnpm dev`
- Run tests: `pnpm test`

## Code style
- TypeScript strict mode
- Single quotes, no semicolons
- Use functional patterns where possible

## PR instructions
- Title format: [<project_name>] <Title>
- Always run `pnpm lint` and `pnpm test` before committing.
```

舊檔名遷移（例如原本叫 `AGENT.md`）：改名後留 symlink 相容。

```bash
mv AGENT.md AGENTS.md && ln -s AGENTS.md AGENT.md
```

# AGENTS.md 與 CLAUDE.md

| | AGENTS.md | CLAUDE.md |
|---|---|---|
| 性質 | 跨工具開放格式 | Claude Code 專屬 |
| 讀取工具 | Codex、Cursor、Copilot、Claude Code（v2.1.277+）等 | Claude Code |
| 匯入其他檔案 | 視工具而定（Claude Code 讀取時支援 `@path`） | 支援 `@path/to/file` |
| 個人不入版控的版本 | 無統一規範（Claude Code 不讀 `AGENTS.local.md`） | `CLAUDE.local.md` |

## Claude Code 讀取規則

預設（`claude-md-or-agents-md`）：**有 CLAUDE.md 就只讀 CLAUDE.md，沒有才讀 AGENTS.md**。

| repo 裡有 | Claude Code 讀 |
|---|---|
| 只有 `AGENTS.md`（工作目錄與上層都沒有 `CLAUDE.md` / `CLAUDE.local.md`） | `AGENTS.md` |
| `AGENTS.md` + `CLAUDE.md`（或 `CLAUDE.local.md`） | 只讀 `CLAUDE.md` |
| `CLAUDE.md` 裡有 `@AGENTS.md` 匯入 | `CLAUDE.md`，`AGENTS.md` 透過匯入一起讀 |

- 會「擋掉」AGENTS.md 的：工作目錄或上層的 `CLAUDE.md`、`.claude/CLAUDE.md`、`CLAUDE.local.md`。
- 不會擋、會一起載入的：`~/.claude/CLAUDE.md`（使用者層級）、組織 managed CLAUDE.md、`.claude/rules/`。
- 注意：只是為了放個人設定而建 `CLAUDE.local.md`，也會讓 Claude 不再讀 AGENTS.md。
- 不會讀：`AGENTS.local.md`、`AGENTS.override.md`、`.agents/` 目錄下的檔案。

## 切換讀取模式（Project instructions）

在 Claude Code 輸入 `/config` → **Project instructions**：

| 值 | 讀取內容 |
|---|---|
| `claude-md-or-agents-md` | 預設；有 CLAUDE.md 讀 CLAUDE.md，沒有才讀 AGENTS.md |
| `claude-md-and-agents-md` | 兩者都讀（同一目錄先 CLAUDE.md 再 AGENTS.md，已匯入的不重複讀） |
| `claude-md` | 只讀 CLAUDE.md |
| `managed-only` | 只讀組織 managed CLAUDE.md 與 auto memory |

也可以寫在 `~/.claude/settings.json`（專案層級的 settings 不吃這個設定）：

```json
{
  "pluginConfigs": {
    "cc-plugin-agents-md@builtin": {
      "options": { "instructionFiles": "claude-md-and-agents-md" }
    }
  }
}
```

v2.1.285 以前 plugin ID 是 `agents-md@builtin`。

## 一份檔案給所有工具共用

把共用規則寫在 `AGENTS.md`，`CLAUDE.md` 只放匯入與 Claude 專屬設定（推薦）：

```markdown
@AGENTS.md

## Claude Code

Use plan mode for changes under `src/billing/`.
```

不需要 Claude 專屬內容時也可以用 symlink：

```bash
ln -s AGENTS.md CLAUDE.md
```

- symlink 的限制：Claude 的 Edit/Write 工具不會透過 symlink 寫入，會改去編輯 `AGENTS.md`。
- 有人用 Windows clone 時改用 `@AGENTS.md` 匯入：Windows 建 symlink 需要系統管理員或開發者模式，且 Git 沒開 `core.symlinks` 時會把 symlink 變成一行文字的普通檔案。
- 設定完在新 session 執行 `/context`，確認 `CLAUDE.md` 出現在 **Memory files**。

# 各工具設定

```yaml
# Aider：.aider.conf.yml
read: AGENTS.md
```

```jsonc
// Gemini CLI：.gemini/settings.json
{ "context": { "fileName": "AGENTS.md" } }
```

Claude Code 從其他工具遷移：`/init` 會讀 `.cursor/rules/`、`.cursorrules`、`.github/copilot-instructions.md`，設定 `CLAUDE_CODE_NEW_INIT=1` 時也會讀 `AGENTS.md`，整合進產生的 `CLAUDE.md`。

# 例外狀況

## Claude Code 沒有讀到 AGENTS.md

最常見原因是專案路徑上有 `CLAUDE.md`。依序檢查：

```bash
# 1. 工作目錄與上層是否有 CLAUDE.md / .claude/CLAUDE.md / CLAUDE.local.md（~/.claude/CLAUDE.md 不算）
ls CLAUDE.md .claude/CLAUDE.md CLAUDE.local.md ../CLAUDE.md 2>/dev/null

# 2. 版本需 v2.1.277 以上；Amazon Bedrock、關閉遙測等 session 需 v2.1.281 以上
claude --version
```

```
3. 在 session 輸入 /config，確認 Project instructions 不是 claude-md 或 managed-only；
   設定面板裡完全沒有這個選項，代表這個 session 無法直接讀 AGENTS.md
   （版本太舊、用 /plugin 停用了內建 plugin，或剛升級後的第一個 session）
4. 輸入 /memory，確認清單裡有 AGENTS.md 的路徑
```

解法：保留 CLAUDE.md 的話，把 Project instructions 改成 `claude-md-and-agents-md`，或在 `CLAUDE.md` 加一行 `@AGENTS.md` 匯入。
