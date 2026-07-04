# Claude Code Terminal(終端機)

```
在終端機內直接與 Claude 協作開發的 CLI 工具
```

## 目錄

- [Claude Code 終端機](#claude-code-終端機)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [啟動方式](#啟動方式)
- [常用指令](#常用指令)
  - [Slash 指令](#slash-指令)
  - [CLI 旗標](#cli-旗標)
- [權限模式](#權限模式)

## 參考資料

[Claude Code 文件](https://docs.anthropic.com/claude-code)

[Claude Code GitHub](https://github.com/anthropics/claude-code)

# 安裝

```bash
npm install -g @anthropic-ai/claude-code
```

# 啟動方式

```bash
# 互動模式
claude

# 直接執行單一指令
claude "解釋這個專案的架構"

# 指定目錄啟動
claude --dir /path/to/project

# 從 stdin 輸入
echo "列出所有 TODO" | claude
```

# 常用指令

## Slash 指令

| 指令 | 說明 |
|------|------|
| `/help` | 查看所有指令 |
| `/clear` | 清除對話記錄 |
| `/compact` | 壓縮對話節省 context |
| `/cost` | 查看本次對話花費 |
| `/model` | 切換模型 |
| `/review` | 檢視目前變更 |
| `/exit` | 離開 |

## CLI 旗標

```bash
# 指定模型
claude --model claude-opus-4-6

# 不自動執行，每步都確認
claude --permission-mode default

# 完全自動執行（謹慎使用）
claude --permission-mode bypassPermissions

# 輸出 JSON 格式（用於腳本整合）
claude --output-format json "分析這個檔案"
```

# 權限模式

| 模式 | 說明 |
|------|------|
| `default` | 危險操作需確認 |
| `acceptEdits` | 自動接受檔案編輯，其他需確認 |
| `bypassPermissions` | 全自動（適合 CI/CD） |
