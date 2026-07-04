# CLAUDE md(專案設定檔)

```
放在專案根目錄，讓 Claude 每次進入專案時自動讀取的指令設定檔
```

## 目錄

- [CLAUDE.md](#claudemd)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [作用](#作用)
- [檔案位置](#檔案位置)
- [基本結構](#基本結構)
  - [完整範例](#完整範例)
- [常用設定項目](#常用設定項目)

## 參考資料

[CLAUDE.md 官方說明](https://docs.anthropic.com/claude-code/memory)

# 作用

- Claude Code 啟動時自動載入，不需每次重新說明專案背景
- 定義專案規範、禁止行為、常用指令
- 可設定多層級（全域 / 專案 / 子目錄）

# 檔案位置

| 位置 | 路徑 | 適用範圍 |
|------|------|----------|
| 全域 | `~/.claude/CLAUDE.md` | 所有專案 |
| 專案 | `<project>/CLAUDE.md` | 該專案 |
| 子目錄 | `<project>/src/CLAUDE.md` | 該子目錄 |

# 基本結構

```markdown
# 專案名稱

## 專案說明
簡短描述這個專案是什麼

## 技術棧
- 語言：Python 3.12
- 框架：FastAPI
- 資料庫：MySQL 8.0

## 常用指令
...

## 開發規範
...

## 注意事項
...
```

## 完整範例

```markdown
# MyProject

## 專案說明
電商後端 API，使用 FastAPI + MySQL

## 技術棧
- Python 3.12 / FastAPI
- MySQL 8.0（主從架構）
- Docker Compose 部署

## 常用指令

啟動開發環境
\`\`\`bash
docker compose up -d
\`\`\`

執行測試
\`\`\`bash
pytest tests/ -v
\`\`\`

## 開發規範
- 所有 API 回傳統一使用 `{"code": 0, "data": ..., "msg": ""}` 格式
- 資料庫操作一律使用 ORM，禁止裸 SQL
- 函式超過 50 行需拆分

## 注意事項
- 不要修改 `migrations/` 目錄下的舊版本檔案
- `.env` 不可提交至 git
```

# 常用設定項目

```markdown
## 禁止行為
- 不要自動 git commit，需經確認
- 不要刪除任何 migration 檔案
- 不要修改 production config

## 偏好設定
- 程式碼註解使用繁體中文
- 變數命名使用 snake_case
- 錯誤處理優先使用 raise，不用 return None

## 測試規範
- 新功能必須附上對應測試
- 測試檔案命名：`test_<module>.py`
```
