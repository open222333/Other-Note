# Claude Desktop 多實例(多帳號)

```
在同一台電腦同時執行多個 Claude Desktop，分別登入不同帳號（非官方做法）。
```

## 目錄

- [Claude Desktop 多實例(多帳號)](#claude-desktop-多實例多帳號)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [背景與原理](#背景與原理)
- [安裝](#安裝)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [配置文檔](#配置文檔)
- [指令](#指令)
  - [Mac 啟動第二個實例](#mac-啟動第二個實例)
- [例外狀況](#例外狀況)
  - [注意事項](#注意事項)

## 參考資料

[Claude(AI) 系列筆記入口](Claude(AI).md)

# 背景與原理

- 官方 Claude Desktop 一次只能登入一個帳號，也無法開第二個視窗；重複安裝無效，因為共用同一個資料夾。
- Claude Desktop 是 Electron / Chromium 應用程式，單一實例鎖綁在 `--user-data-dir` 指定的資料夾。
- 指定不同資料夾 → 啟動另一個獨立實例 → 可登入不同帳號。

# 安裝

## Homebrew (MacOS)

不需額外安裝，用系統 `open` 指令即可（見 [指令](#指令)）。現成工具：

| 工具 | 來源 | 備註 |
|---|---|---|
| Parall | Mac App Store | 建立獨立資料路徑的捷徑；建議只用 Parall 捷徑啟動 |
| TrackMyUsage | GitHub | 為每個實例複製並重新簽章 App |

## Windows

| 工具 | 來源 | 備註 |
|---|---|---|
| claude-desktop-clone | GitHub | 免費開源，不需管理員權限 |
| Multi Instance for Claude Desktop | Microsoft Store | 原標免費，後更正；底層腳本 `claude-desktop-multi` 開源 |

## 配置文檔

每個實例的設定與登入資料存在各自的 `--user-data-dir` 資料夾，例如 `$HOME/.claude-work`。

# 指令

## Mac 啟動第二個實例

先完全關閉 Claude，再執行：

```bash
open -n /Applications/Claude.app --args --user-data-dir="$HOME/.claude-work"
```

- `-n`：強制開新實例
- `--user-data-dir`：指定獨立的設定 / 登入資料夾

# 例外狀況

## 注意事項

- 皆為非官方做法，官方更新後可能失效。
- 安裝第三方工具前先確認來源與原始碼。
- 最簡單的替代方案：用瀏覽器不同 Profile 分別登入 claude.ai。
- Claude Code（CLI）是另一個程式，可與桌面版同時安裝。
