# MySQL 資料表設計工具

```
用於設計 MySQL 資料表結構與關聯（ER 圖）的工具。
支援正向工程（圖 → SQL）與逆向工程（DB → 圖）兩種工作流程。
```

## 目錄

- [MySQL 資料表設計工具](#mysql-資料表設計工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [工具比較](#工具比較)
- [MySQL Workbench](#mysql-workbench)
  - [安裝](#安裝)
  - [建立 ER 圖（正向工程）](#建立-er-圖正向工程)
  - [逆向工程（從現有 DB 產生 ER 圖）](#逆向工程從現有-db-產生-er-圖)
  - [正向工程（從 ER 圖產生 SQL）](#正向工程從-er-圖產生-sql)
- [dbdiagram.io](#dbdiagramio)
  - [DBML 語法](#dbml-語法)
  - [匯出](#匯出)
- [DrawDB](#drawdb)
- [DBeaver](#dbeaver)

## 參考資料

[MySQL Workbench 官方](https://www.mysql.com/products/workbench/)

[dbdiagram.io 官網](https://dbdiagram.io/)

[DBML 語法文檔](https://dbml.dbdiagram.io/docs/)

[DrawDB GitHub](https://github.com/drawdb-io/drawdb)

[DBeaver 官網](https://dbeaver.io/)

---

# 工具比較

| 工具 | 類型 | 費用 | 逆向工程 | 正向工程 | 協作 | 適合情境 |
|---|---|---|---|---|---|---|
| **MySQL Workbench** | 桌面 | 免費 | ✅ | ✅ | ❌ | 官方工具，ER 圖設計、DB 管理一體 |
| **dbdiagram.io** | 網頁 | 免費（付費進階） | ❌ | ✅（匯出 SQL） | ✅ | 用程式碼快速設計，適合開發初期討論 |
| **DrawDB** | 網頁（開源） | 免費 | ❌ | ✅ | ❌（可自架） | 拖拉介面，可自架，無需帳號 |
| **DBeaver** | 桌面 | 免費（社群版） | ✅ | ❌ | ❌ | 通用 DB 工具，ER 圖是附加功能 |

---

# MySQL Workbench

官方出品的 MySQL 圖形化工具，同時支援 ER 圖設計、SQL 編輯、伺服器管理。

## 安裝

```bash
# macOS
brew install --cask mysqlworkbench

# Ubuntu
sudo apt install mysql-workbench-community

# Windows / 其他
# https://dev.mysql.com/downloads/workbench/
```

## 建立 ER 圖（正向工程）

1. 開啟 Workbench → `File > New Model`
2. 點擊 `Add Diagram` 新增畫布
3. 從左側工具列拖入 **Table** 物件
4. 雙擊 Table → 設定欄位名稱、型別、PK、NOT NULL、預設值
5. 用 **Relationship** 工具連接兩個 Table，選擇關聯類型：
   - `1:1`、`1:N`、`N:M`
6. `File > Save Model`（儲存為 `.mwb` 格式）

## 逆向工程（從現有 DB 產生 ER 圖）

```
Database → Reverse Engineer...
→ 輸入連線資訊
→ 選擇要匯入的 Schema
→ 選擇 Table
→ 完成後自動產生 ER 圖
```

## 正向工程（從 ER 圖產生 SQL）

```
Database → Forward Engineer...
→ 設定連線（或選擇匯出為 SQL 檔）
→ 預覽 CREATE TABLE SQL
→ 執行或儲存 .sql
```

---

# dbdiagram.io

用 **DBML**（Database Markup Language）語法描述 schema，即時渲染 ER 圖。
適合開發初期快速溝通、版本控制 schema 設計。

## DBML 語法

```dbml
// 建立 Table
Table users {
  id         int           [pk, increment]
  username   varchar(50)   [not null, unique]
  email      varchar(100)  [not null, unique]
  password   varchar(255)  [not null]
  created_at timestamp     [default: `now()`]
}

Table posts {
  id         int           [pk, increment]
  user_id    int           [not null, ref: > users.id]  // 多對一
  title      varchar(200)  [not null]
  content    text
  status     varchar(20)   [default: 'draft', note: 'draft / published / archived']
  created_at timestamp     [default: `now()`]
}

Table tags {
  id   int         [pk, increment]
  name varchar(50) [not null, unique]
}

// 多對多中間表
Table post_tags {
  post_id int [ref: > posts.id]
  tag_id  int [ref: > tags.id

  indexes {
    (post_id, tag_id) [pk]
  }
}
```

關聯符號：

| 符號 | 意義 |
|---|---|
| `>` | 多對一（Many to One） |
| `<` | 一對多（One to Many） |
| `-` | 一對一（One to One） |
| `<>` | 多對多（Many to Many） |

## 匯出

- **Export to SQL**：直接產生 `CREATE TABLE` SQL（支援 MySQL、PostgreSQL 等）
- **Export to PDF / PNG**：匯出圖片
- **Share Link**：分享設計連結給團隊

---

# DrawDB

開源的瀏覽器端 ER 圖工具，無需帳號，可自架。

- 線上使用：[app.drawdb.app](https://app.drawdb.app/)
- 拖拉介面，直接點擊新增 Table / 欄位 / 關聯
- 支援匯出 SQL（MySQL / PostgreSQL / SQLite）
- 可匯入現有 SQL 自動建圖

```bash
# 自架（Docker）
docker run -d -p 3000:80 drawdb/drawdb
```

---

# DBeaver

通用資料庫 GUI 工具，ER 圖為附加功能（從實際 DB 連線後檢視）。

```
1. 連接到 MySQL 資料庫
2. 右鍵點擊 Schema / Database
3. → View Diagram
4. 自動產生該 Schema 下所有 Table 的 ER 圖
```

逆向工程用途佳，但不支援純設計（需先有 DB 連線）。
