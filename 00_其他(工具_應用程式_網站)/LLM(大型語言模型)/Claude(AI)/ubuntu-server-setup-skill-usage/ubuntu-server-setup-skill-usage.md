# ubuntu-server-setup.skill 使用說明

## .skill 檔案是什麼

`.skill` 檔案本質上是一個 **zip 壓縮檔**，安裝後 Claude 會在相關情境下自動載入內容。

---

## 安裝方式

### Claude Code（命令列）

```bash
claude skill install ubuntu-server-setup.skill
```

安裝後存放在：

```
~/.claude/skills/ubuntu-server-setup/
```

### Claude Cowork（桌面應用）

1. 開啟 Claude Cowork
2. 進入 **Settings → Skills**
3. 點擊 **Install Skill**
4. 選擇 `ubuntu-server-setup.skill` 檔案

---

## 安裝後的效果

當對話中提到以下關鍵字時，Claude 會自動載入這份指南：

- MySQL 安裝、主從複製、Master Slave
- Redis 安裝、設定
- Docker、Docker Compose 安裝
- Ubuntu 伺服器環境建置

---

## 自行編輯

### 1. 解壓縮

```bash
unzip ubuntu-server-setup.skill -d ubuntu-server-setup
```

解壓後結構：

```
ubuntu-server-setup/
├── SKILL.md                      ← 主要編輯這個
└── references/
    └── mysql84_master_slave.md   ← MySQL 完整文檔
```

### 2. 編輯

用任何文字編輯器開啟 `.md` 檔案：

```bash
# vim
vim ubuntu-server-setup/SKILL.md

# nano
nano ubuntu-server-setup/SKILL.md

# VSCode
code ubuntu-server-setup/
```

### 3. frontmatter 格式說明

`SKILL.md` 最上方的 frontmatter 不要動格式：

```yaml
---
name: ubuntu-server-setup
description: 這裡是觸發條件描述，Claude 根據這段決定何時載入這份 skill
---
```

`description` 寫得越清楚，Claude 越能在正確時機自動使用這份指南。

### 4. 編輯完重新打包

```bash
cd ubuntu-server-setup
zip -r ../ubuntu-server-setup.skill .
```

---

## 注意事項

目前 **claude.ai 網頁版**無法直接安裝 `.skill` 檔案，需在 Claude Code 或 Claude Cowork 環境下使用。
