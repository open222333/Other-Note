# 文件 G｜環境事實檔（ENVIRONMENT_FACTS）

> 版本：1.0（2026-07-04）
> 定位：**當下為真的環境事實**，唯一一份。AI 接任務時先讀本檔，禁止重新探索環境（對治痛點一的 token 浪費、痛點三的假設腐化）。
> 分工：安裝「步驟」留在 `ubuntu-server-setup` skill；環境「現狀」記在本檔。兩者衝突以本檔為準並回頭刪修 skill。
> `<待填>` 欄位請使用者或首個接到環境任務的 W 補齊；本檔**不得**填入任何密碼、token、私鑰內容。

---

## 1. 主機清單

| 代號 | 供應商/規格 | 角色 | OS | 內網 IP | 對外服務 |
|------|------------|------|-----|---------|---------|
| db-master | Linode Dedicated 64GB | MySQL Master | Ubuntu 24.04 LTS | <待填> | <待填> |
| db-slave | Linode 32GB | MySQL Replica | Ubuntu 24.04 LTS | <待填> | 無（唯讀） |
| <待填> | | app / bot 所在主機 | | | |
| 本機 | MacBook Air (macOS) | 開發 | — | — | — |

## 2. 服務版本與關鍵設定事實

### MySQL 8.4（deb bundle 安裝，非 APT repo）
- 版本：8.4.5，安裝來源：`mysql-server_8.4.5-1ubuntu24.04_amd64.deb-bundle.tar`（官方 APT repo GPG key 過期，故不用）。
- Replication：GTID 模式；連線需 `GET_SOURCE_PUBLIC_KEY=1`。
- 認證插件：僅 `caching_sha2_password`（`mysql_native_password` 在 8.4 已移除——任何要求改回舊插件的做法都是錯的）。
- Master：`innodb_buffer_pool_size = 48G`。
- Slave：`innodb_buffer_pool_size = 22G`、`super_read_only = ON`（常態，不得解除）。
- binlog 保留策略：<待填：binlog_expire_logs_seconds>

### Redis
- 安裝：apt `redis-server`，版本：<待填>。
- Port 6379、`requirepass` 啟用、`save ""`（**無持久化**，重啟掉資料屬預期）。
- 已知風險：`bind 0.0.0.0` + `protected-mode no`（文件 A 第 4 章待修正項；修正後刪除本行）。

### Docker
- Docker Engine + Compose v2（`docker compose`）；另有 `/usr/local/bin/docker-compose` wrapper script 供舊指令相容。
- Compose 專案路徑：<待填>。

### 應用
- ERP-WMS：前端 Vue、後端 Python；repo：<待填>。
- Telegram bot：Node.js（express + node-telegram-bot-api + @anthropic-ai/sdk），部署位置：<待填>。
- 其他 repo：`avnight/proxysql`（Bitbucket）。

## 3. Port 對照表

| Port | 服務 | 開放範圍 |
|------|------|---------|
| 3306 | MySQL | <待填：僅內網？> |
| 6379 | Redis | <待填> |
| 3000 | Telegram bot webhook | 經 HTTPS 反代 / ngrok（測試） |
| <待填> | | |

## 4. `.env` 欄位清單（只列欄位名，不列值）

### Telegram bot
`TELEGRAM_TOKEN`、`ANTHROPIC_API_KEY`、`ALLOWED_USER_IDS`、`PROJECT_ROOT`、`WEBHOOK_URL`、`PORT`、`CHAT_ID`

### <其他服務待填>

## 5. 認證與金鑰盤點

| 金鑰/憑證 | 用途 | 所在位置 | 建立日期 | 備註 |
|-----------|------|---------|---------|------|
| SSH key <待填名稱> | Bitbucket git 操作 | <主機> | <待填> | 全 repo 已統一 SSH（HTTPS token 已淘汰） |
| repl 帳號 | MySQL replication | db-master | <待填> | caching_sha2_password |
| <待填> | | | | |

## 6. 外部服務依賴

| 服務 | 用途 | 已知政策/變動風險 |
|------|------|-------------------|
| Bitbucket | Git 託管 | App Password 已淘汰，僅用 SSH |
| Telegram Bot API | 通知/指令 | 群組升級 supergroup 會改 chat_id（程式已/應自動處理） |
| Linode | 主機 | — |
| Anthropic API | AI 代理 | 模型字串更新時同步改文件 B 對應表 |
| <待填：CDN/DNS 供應商> | | |

## 7. 備份現況

- MySQL 備份方式：<待填：工具、頻率、存放位置、保留份數>
- 最近一次還原演練：<待填日期>（超過 90 天未演練 → 開 D 任務單）

---

**維護規則**：任何任務造成環境事實變動（換版本、開 port、加主機、換 key），該任務在文件 C 清單 2 打勾前，必須先更新本檔對應行。
