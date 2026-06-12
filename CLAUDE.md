# Claude Code 專案指令

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
