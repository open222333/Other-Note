# GitHub Copilot

```
GitHub / Microsoft 開發的 AI 程式碼助手，深度整合 IDE
```

## 目錄

- [GitHub Copilot](#github-copilot)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [版本方案](#版本方案)
- [使用方式](#使用方式)
  - [VS Code 安裝](#vs-code-安裝)
  - [JetBrains 安裝](#jetbrains-安裝)
  - [CLI 安裝](#cli-安裝)
- [主要功能](#主要功能)

## 參考資料

[GitHub Copilot 官方網站](https://github.com/features/copilot)

[Copilot 文件](https://docs.github.com/copilot)

[VS Code Copilot 擴充](https://marketplace.visualstudio.com/items?itemName=GitHub.copilot)

# 版本方案

| 方案 | 說明 |
|------|------|
| Free | 每月限量補全與對話 |
| Pro | $10/月，無限補全 |
| Business | $19/月，企業管理功能 |
| Enterprise | $39/月，自訂模型微調 |

# 使用方式

## VS Code 安裝

```
Extensions → 搜尋 "GitHub Copilot" → Install
```

登入 GitHub 帳號後即可使用

## JetBrains 安裝

```
Settings → Plugins → 搜尋 "GitHub Copilot" → Install
```

## CLI 安裝

```bash
gh extension install github/gh-copilot
```

使用

```bash
gh copilot explain "git rebase -i HEAD~3"
gh copilot suggest "如何壓縮 Docker image"
```

# 主要功能

- 程式碼自動補全（行內建議）
- Copilot Chat：IDE 內對話問答
- Inline Chat：選取程式碼直接問問題
- `/explain`：解釋程式碼
- `/fix`：自動修復 Bug
- `/tests`：自動生成測試
- Copilot Workspace：自然語言生成 PR
