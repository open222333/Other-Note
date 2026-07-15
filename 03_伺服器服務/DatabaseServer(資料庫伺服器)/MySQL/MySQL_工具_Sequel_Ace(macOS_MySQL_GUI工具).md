# MySQL 工具 Sequel Ace（macOS MySQL GUI 工具）

```
macOS 專屬的 MySQL / MariaDB GUI 用戶端，是停止維護的 Sequel Pro 的繼承版本。
免費開源，支援 SSH Tunnel、多分頁、查詢歷史、資料匯入匯出等常用功能。
```

## 目錄

- [MySQL 工具 Sequel Ace（macOS MySQL GUI 工具）](#mysql-工具-sequel-acemacos-mysql-gui-工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [連線設定](#連線設定)
  - [Standard TCP/IP](#standard-tcpip)
  - [SSH Tunnel](#ssh-tunnel)
  - [Socket](#socket)
- [主要功能](#主要功能)
  - [Query 編輯器](#query-編輯器)
  - [Table Structure（資料表結構）](#table-structure資料表結構)
  - [Table Content（資料瀏覽）](#table-content資料瀏覽)
  - [匯入 / 匯出](#匯入--匯出)
- [常用操作](#常用操作)

## 參考資料

[Sequel Ace 官網](https://sequel-ace.com)

[Sequel Ace GitHub](https://github.com/Sequel-Ace/Sequel-Ace)

### 相關筆記

[MySQL 筆記（主）](./MySQL_筆記.md)
[MySQL 工具 - phpMyAdmin](./MySQL_工具_phpMyAdmin(MySQL資料庫管理工具).md)
[MySQL 工具 - Adminer](./MySQL_工具_Adminer(輕量級MySQL管理工具).md)

---

# 安裝

**系統需求**：macOS 10.15（Catalina）以上。

**方式一：Mac App Store（推薦，自動更新）**

```
App Store → 搜尋「Sequel Ace」→ 取得
```

**方式二：直接下載**

至 [GitHub Releases](https://github.com/Sequel-Ace/Sequel-Ace/releases) 下載最新 `.dmg`，拖入 Applications 即可。

---

# 連線設定

## Standard TCP/IP

| 欄位 | 說明 |
|------|------|
| Host | MySQL 伺服器 IP 或域名 |
| Username | 資料庫帳號 |
| Password | 資料庫密碼 |
| Database | 預設資料庫（可留空，連線後再選） |
| Port | 預設 `3306` |

## SSH Tunnel

透過 SSH 跳板連線，適用於 DB 不對外開放的情況。

| 欄位 | 說明 |
|------|------|
| SSH Host | 跳板機 IP |
| SSH User | 跳板機帳號 |
| SSH Password / Key | 密碼或私鑰檔案路徑 |
| SSH Port | 預設 `22` |
| MySQL Host | 目標 DB 的內網 IP（通常 `127.0.0.1`） |
| MySQL Port | `3306` |

> 私鑰需先在 Sequel Ace 的 SSH Keys 設定中登記，或透過系統 Keychain 管理。

## Socket

連線本機 MySQL，直接指定 socket 路徑：

```
/tmp/mysql.sock
/var/run/mysqld/mysqld.sock
```

---

# 主要功能

## Query 編輯器

- 語法高亮、自動補全（表名、欄位名）
- 多分頁同時開啟多個查詢
- 查詢歷史（⌘ + ↑/↓ 快速切換歷史）
- 常用查詢加入 Favorites（⌘ + Shift + F）
- 執行選取範圍的 SQL（選取後 ⌘ + R）
- 結果集可直接編輯後寫回

## Table Structure（資料表結構）

- 新增 / 修改 / 刪除欄位
- 新增索引（PRIMARY / UNIQUE / INDEX / FULLTEXT）
- 設定外鍵（Foreign Key）
- 修改儲存引擎、字元集、排序規則

## Table Content（資料瀏覽）

- 分頁瀏覽大型資料表
- 條件篩選（WHERE 條件直接在 UI 輸入）
- 欄位排序
- 直接在表格內編輯 / 新增 / 刪除資料列

## 匯入 / 匯出

**匯出**：

```
File → Export → 選擇格式
```

| 格式 | 說明 |
|------|------|
| SQL | 標準 dump，含 CREATE TABLE + INSERT |
| CSV | 純資料，可指定分隔符 |
| JSON | 每列一個 JSON 物件 |
| XML | 標準 XML 格式 |

**匯入**：

```
File → Import → 選擇 .sql 或 .csv 檔
```

> 匯入大型 SQL 時建議先調高 `max_allowed_packet` 或改用 CLI `mysql` 指令。

---

# 常用操作

| 操作 | 方式 |
|------|------|
| 執行全部 SQL | ⌘ + R |
| 執行選取的 SQL | 選取後 ⌘ + R |
| 切換資料庫 | 左側選單點選或頂端下拉選單 |
| 查詢歷史 | ⌘ + ↑ / ↓ |
| 新分頁 | ⌘ + T |
| 加入 Favorites | ⌘ + Shift + F |
| 複製欄位名稱 | 右鍵欄位標頭 → Copy Field Name |
| 複製為 INSERT 語法 | 右鍵資料列 → Copy as SQL INSERT |
