# VSCode 擴充套件 GitLens（Git 歷史視覺化）

```
GitLens 是 VSCode 最熱門的 Git 擴充套件，
在編輯器內直接顯示每一行的 commit 來源（Blame），
並提供 Commit Graph、檔案歷史、分支比較、互動式 Rebase 等功能。
```

## 目錄

- [VSCode 擴充套件 GitLens（Git 歷史視覺化）](#vscode-擴充套件-gitlens-git-歷史視覺化)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [核心功能](#核心功能)
  - [Inline Blame（行內追責）](#inline-blame行內追責)
  - [File Annotations（整份檔案 Blame）](#file-annotations整份檔案-blame)
  - [File History（檔案歷史）](#file-history檔案歷史)
  - [Commit Graph（提交圖）](#commit-graph提交圖)
  - [Branch / Tag 比較](#branch--tag-比較)
  - [Interactive Rebase Editor](#interactive-rebase-editor)
- [側邊欄面板](#側邊欄面板)
- [常用快捷鍵 / 指令](#常用快捷鍵--指令)
- [設定](#設定)
- [免費 vs GitLens+（付費）](#免費-vs-gitlens付費)

## 參考資料

[GitLens 官網](https://www.gitkraken.com/gitlens)

[VSCode 市集頁](https://marketplace.visualstudio.com/items?itemName=eamodio.gitlens)

[GitLens 文檔](https://help.gitkraken.com/gitlens/gitlens-home/)

---

# 安裝

```
Extensions（Ctrl+Shift+X）→ 搜尋 "GitLens" → Install
```

或透過指令列：

```bash
code --install-extension eamodio.gitlens
```

---

# 核心功能

## Inline Blame（行內追責）

游標停在某一行，行尾自動顯示該行最後一次 commit 的作者、時間與 commit message。

```
Tom Li, 2 days ago • fix: correct order calculation logic
```

- 點擊 Blame 標註 → 開啟完整 Commit 詳情
- 可在 Status Bar 看到目前游標所在行的 commit 摘要

## File Annotations（整份檔案 Blame）

為整份檔案每一行加上 Blame 標註。

```
Command Palette → GitLens: Toggle File Blame
```

或點擊右上角 GitLens 圖示切換顯示模式：
- **Blame**：每行顯示作者 + commit
- **Changes**：高亮顯示與上個 commit 相比的異動行
- **Heatmap**：依修改時間新舊用顏色標示（越紅越新）

## File History（檔案歷史）

查看單一檔案所有修改記錄。

```
右鍵點擊檔案 → Open File History
或
Command Palette → GitLens: Show File History
```

每筆記錄可展開查看 diff，也可比較任意兩個版本。

## Commit Graph（提交圖）

視覺化顯示整個 repo 的 commit 樹狀結構，類似 GitKraken。

```
Command Palette → GitLens: Show Commit Graph
```

- 顯示所有 branch、tag、remote
- 可直接從圖上右鍵操作：cherry-pick、checkout、reset、merge、rebase

> Commit Graph 在舊版需 GitLens+，**2023 年後免費開放**。

## Branch / Tag 比較

比較兩個分支（或 commit）之間的差異。

```
GitLens 側邊欄 → Search & Compare → Add Comparison
```

選擇任意兩個 ref（branch / tag / commit hash）後：
- 顯示兩者之間所有 commit 列表
- 可展開查看每個 commit 的 diff

## Interactive Rebase Editor

圖形化的互動式 Rebase，取代終端機的 `git rebase -i`。

```bash
git rebase -i HEAD~5
# VSCode 自動開啟 GitLens 的圖形介面（需設定 core.editor=code --wait）
```

介面支援：
- **Reorder**：拖拉調整 commit 順序
- **Pick / Squash / Fixup / Reword / Drop**：點選操作，不用記指令
- 完成後點擊「Start Rebase」執行

---

# 側邊欄面板

點擊左側 GitLens 圖示展開：

| 面板 | 功能 |
|---|---|
| **Commits** | 目前分支的 commit 列表，可展開看 diff |
| **File History** | 目前開啟檔案的歷史 |
| **Branches** | 所有本地 / 遠端分支 |
| **Remotes** | Remote 列表與遠端分支 |
| **Stashes** | Stash 列表，可直接 apply / drop |
| **Tags** | Tag 列表 |
| **Search & Compare** | 搜尋 commit、比較分支 |
| **Worktrees** | Git Worktree 管理（付費） |

---

# 常用快捷鍵 / 指令

| 動作 | 方式 |
|---|---|
| 切換行內 Blame 顯示 | `Command Palette` → `GitLens: Toggle Line Blame` |
| 查看目前行的 commit 詳情 | 點擊行尾 Blame 標註 |
| 開啟 Commit Graph | `Command Palette` → `GitLens: Show Commit Graph` |
| 查看檔案歷史 | 右鍵 → `Open File History` |
| 比較工作區與分支 | `Command Palette` → `GitLens: Open Changes with Branch or Tag` |
| 搜尋 commit 訊息 | GitLens 側邊欄 → Search & Compare → 輸入關鍵字 |

---

# 設定

常用 `settings.json` 設定：

```jsonc
{
  // 關閉行內 blame（預設開啟，覺得干擾可關）
  "gitlens.currentLine.enabled": false,

  // 狀態列顯示目前行的 blame
  "gitlens.statusBar.enabled": true,

  // Blame 標註格式（${author}、${agoOrDate}、${message} 等）
  "gitlens.currentLine.format": "${author}, ${agoOrDate} • ${message}",

  // Heatmap 色彩方向（oldest = 越舊越紅；newest = 越新越紅）
  "gitlens.heatmap.ageThreshold": 90,

  // 是否在 hover 時顯示 commit 詳情
  "gitlens.hovers.currentLine.over": "line"
}
```

---

# 免費 vs GitLens+（付費）

| 功能 | 免費 | GitLens+（付費） |
|---|---|---|
| Inline Blame | ✅ | ✅ |
| File / Line History | ✅ | ✅ |
| Commit Graph | ✅（本地 repo） | ✅（含 remote） |
| Interactive Rebase Editor | ✅ | ✅ |
| Worktrees 管理 | ❌ | ✅ |
| Visual File History（視覺化時間軸） | ❌ | ✅ |
| Cloud Patches（分享 diff） | ❌ | ✅ |

> 個人本地開發所需功能**免費版已足夠**。
