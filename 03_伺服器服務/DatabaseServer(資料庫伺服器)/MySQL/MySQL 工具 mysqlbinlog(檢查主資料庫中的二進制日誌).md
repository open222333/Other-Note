# MySQL 工具 mysqlbinlog(檢查主資料庫中的二進制日誌)

```
檢查主資料庫中的二進制日誌（Binary Log）

mysqlbinlog 是 MySQL 提供的一個命令行工具，用於解析和顯示二進制日誌文件的內容。
二進制日誌是 MySQL 中記錄數據庫更改操作的一種方式，它包含了插入、更新、刪除等操作的詳細信息。
mysqlbinlog 可以用來查看這些二進制日誌的內容，並且還可以將其輸出為文本格式，便於分析和恢復數據。

要檢查主資料庫中的二進制日誌（Binary Log），可以使用 MySQL 的命令行工具 mysqlbinlog。
這個工具可以查看和解析 MySQL 的二進制日誌，以了解其中的更新操作。
```

## 目錄

- [MySQL 工具 mysqlbinlog(檢查主資料庫中的二進制日誌)](#mysql-工具-mysqlbinlog檢查主資料庫中的二進制日誌)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [指令](#指令)

## 參考資料

### 心得相關

# 指令

```bash
# 進入 MySQL 的安裝目錄，並找到二進制日誌的儲存位置。
# 通常，MySQL 的二進制日誌儲存在 datadir 目錄下的 mysql-bin 目錄中。
# 可以使用以下指令查找 datadir 的位置：
mysql -u root -p -e "SHOW VARIABLES LIKE 'datadir';"

# 使用 mysqlbinlog 命令檢視二進制日誌。
# 請將 mysql-bin.000001 替換為錯誤訊息中提供的實際日誌檔案名稱，並將 566933897 替換為結束日誌位置：
mysqlbinlog mysql-bin.000001 --start-position=566933897

# 利用mysqlbinlog工具找出440267874的事件
mysqlbinlog --base64-output=decode-rows -vv mysql-bin.000003 |grep -A 20 '440267874'
mysqlbinlog --base64-output=decode-rows -vv mysql-bin.000003 --stop-position=440267874 | tail -20
mysqlbinlog --base64-output=decode-rows -vv mysql-bin.000003 > decode.log
```

查看二進制日誌

可以使用 `mysqlbinlog` 來查看二進制日誌的內容，包括 SQL 語句、事務信息等。

```bash
mysqlbinlog [options] binary-log-file
```

將二進制日誌轉換為 SQL 語句

`mysqlbinlog` 可以將二進制日誌的內容轉換為可執行的 SQL 語句，以便進行數據恢復或分析。

```bash
mysqlbinlog [options] binary-log-file > output.sql
```

指定時間範圍查看日誌

可以使用 `--start-datetime` 和 `--stop-datetime` 選項指定查看日誌的時間範圍。

```bash
mysqlbinlog --start-datetime="YYYY-MM-DD HH:MM:SS" --stop-datetime="YYYY-MM-DD HH:MM:SS" binary-log-file
```

僅查看特定數據庫的日誌

使用 `--database` 選項可以指定僅查看特定數據庫的日誌。

```bash
mysqlbinlog --database=db_name binary-log-file
```

忽略特定數據庫的日誌

使用 `--ignore-database` 選項可以忽略特定數據庫的日誌。

```bash
mysqlbinlog --ignore-database=db_name binary-log-file
```

顯示事件詳細信息

使用 `-v` 或 `--verbose` 選項可以顯示更詳細的事件信息。

```bash
mysqlbinlog -v binary-log-file
```
