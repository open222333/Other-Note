# Claude Skill（自訂指令）

## 目錄

- [什麼是 Skill](#什麼是-skill)
- [放置位置](#放置位置)
- [檔案格式](#檔案格式)
- [呼叫方式](#呼叫方式)
- [接收引數](#接收引數)
- [動態注入指令輸出](#動態注入指令輸出)
- [範例：域名比對 Skill](#範例域名比對-skill)

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

---

## 相關文件

- [Claude.md](Claude.md)
