# SQLite3 筆記

```
```

## 目錄

- [SQLite3 筆記](#sqlite3-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [圖形化介面相關](#圖形化介面相關)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[sqlite3(num) — Linux man page](https://linux.die.net/man/num/sqlite3)

[sqlite3(num) — Linux manual page](https://www.man7.org/linux/man-pages/man1/sqlite3.num.html)

### 圖形化介面相關

[SQLite 圖形化介面 軟體 下載和使用教學](https://www.ruyut.com/2021/12/sqlite-tool.html)

# 安裝

## Debian (Ubuntu)

```bash
apt-get install -y sqlite3
```

## RedHat (CentOS)

```bash
yum install -y sqlite
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令

`顯示幫助信息，包括可用的命令和其描述`

```bash
.help
```

` 顯示當前連接的所有數據庫`

```bash
.databases
```

`顯示當前數據庫中的所有表格`

```bash
.tables
```

`查詢表格中的所有數據`

```bash
.select * from table_name
```

`顯示指定表格的創建 SQL`

```bash
.schema table_name
```

`開啟或關閉列名的顯示 on|off`

```bash
.headers on
```

`設置輸出模式，常見的模式包括 column（默認，每列一行）和 list（每行一列）`

```bash
.mode column
```

`退出 SQLite3 Shell`

```bash
.quit
```

```bash
.exit
```
