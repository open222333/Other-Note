# Claude Cowork(協作)

```
在 IDE 內與 Claude 協作開發，即時讀寫程式碼、執行指令
```

## 目錄

- [Cowork(協作)](#cowork協作)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [相關心得](#相關心得)
- [協作模式](#協作模式)
- [常見協作情境](#常見協作情境)
  - [閱讀與理解程式碼](#閱讀與理解程式碼)
  - [修改與重構](#修改與重構)
  - [Debug](#debug)
  - [撰寫測試](#撰寫測試)
  - [執行指令](#執行指令)
- [IDE 整合](#ide-整合)
- [協作技巧](#協作技巧)

## 參考資料

[Claude Code 文件](https://docs.anthropic.com/claude-code)

[Claude Code VS Code 擴充](https://marketplace.visualstudio.com/items?itemName=Anthropic.claude-code)

### 相關心得

[隆重推出 Claude Cowork 遠端存取功能（研究預覽版）](https://www.reddit.com/r/ClaudeAI/comments/1rwiop4/introducing_remote_access_for_claude_cowork/?tl=zh-hant)

# 協作模式

| 模式 | 說明 |
|------|------|
| 終端機互動 | `claude` 啟動後對話，Claude 可讀寫檔案、執行指令 |
| VS Code 側欄 | IDE 內嵌對話視窗，支援選取程式碼後直接問問題 |
| Background Agent | 背景執行長時間任務，完成後通知 |

# 常見協作情境

## 閱讀與理解程式碼

```
解釋 src/auth/middleware.py 的邏輯
這個函式的輸入輸出是什麼？
這個專案的資料夾結構是怎麼設計的？
```

## 修改與重構

```
把 get_user() 改成非同步函式
將這段重複邏輯抽成共用函式
把硬編碼的 URL 改為讀取環境變數
```

## Debug

```
這個錯誤訊息是什麼意思？
[貼上 stack trace]
找出為什麼 user_id 會是 None
```

## 撰寫測試

```
幫 create_order() 寫單元測試
補上這個 API endpoint 的整合測試
```

## 執行指令

Claude 可以直接在終端機執行指令並回傳結果

```
跑一下測試看有沒有錯誤
git status 目前有哪些變更？
docker compose up 然後告訴我 log 有沒有異常
```

# IDE 整合

VS Code 安裝 Claude Code 擴充後可使用：

- 側欄對話視窗
- 選取程式碼 → 右鍵 → Ask Claude
- `Cmd+Esc` 快速開啟對話

# 協作技巧

- 提供具體檔案路徑，減少 Claude 猜測範圍
- 貼上錯誤訊息或 log，讓 Claude 有完整資訊
- 使用 `/compact` 壓縮對話，節省 context
- 複雜任務拆成小步驟逐步確認，避免大範圍改錯
- 在 CLAUDE.md 寫好專案規範，減少每次重複說明
