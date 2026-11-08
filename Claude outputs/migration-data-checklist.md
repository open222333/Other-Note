# 遷移時間預估：需要收集的資料清單

> 對應 `20261107_mysql_slave_to_managed_migration/20261107_slave-to-managed-db-migration-estimate.md`。
> 每一項執行指令後，把輸出貼在「結果」下方。密碼不要貼。

---

## 1. admin-test（`172.104.86.251`）

### 1-1 主機規格

```bash
lsb_release -ds; nproc; free -g; df -h /data
```

結果：

```text

```

### 1-2 工具版本

```bash
mysql --version; mydumper --version; myloader --version; sysbench --version
```

結果：

```text

```

### 1-3 連新 DB

```bash
mysql -h public-a554244-akamai-prod-1053367-default.g2a.akamaidb.net -P 28707 -u akmadmin -p --ssl-mode=REQUIRED \
  -e "SELECT USER(), @@hostname, @@version;"
```

結果：

```text

```

### 1-4 連 slave

```bash
mysql -h 139.162.114.98 -P 3306 -u <slave 帳號> -p \
  -e "SELECT @@hostname, @@version, @@read_only, @@gtid_mode;"
```

結果：

```text

```

### 1-5 網路延遲

```bash
ping -c 20 139.162.114.98 | tail -2
```

結果：

```text

```

---

## 2. slave（`139.162.114.98`）

### 2-1 連線資訊

| 項目 | 值 |
|------|----|
| port |  |
| 匯出帳號（不寫密碼） |  |
| 防火牆是否已放行 `172.104.86.251` → 3306 |  |

### 2-2 主機規格與複製狀態

```bash
nproc; free -g; df -h
mysql -u root -p -e "SHOW REPLICA STATUS\G" | grep -E "Replica_IO_Running|Replica_SQL_Running|Seconds_Behind_Source"
```

結果：

```text

```

### 2-3 各 schema 大小

```sql
SELECT table_schema,
       ROUND(SUM(data_length)/1024/1024/1024, 2)  AS data_gb,
       ROUND(SUM(index_length)/1024/1024/1024, 2) AS index_gb,
       SUM(table_rows)                            AS rows_est
FROM information_schema.tables
WHERE table_schema NOT IN ('mysql','sys','information_schema','performance_schema')
GROUP BY table_schema ORDER BY data_gb DESC;
```

結果：

```text

```

### 2-4 最大的 10 張表

```sql
SELECT table_schema, table_name,
       ROUND((data_length + index_length)/1024/1024/1024, 2) AS size_gb, table_rows
FROM information_schema.tables
WHERE table_schema NOT IN ('mysql','sys','information_schema','performance_schema')
ORDER BY data_length + index_length DESC LIMIT 10;
```

結果：

```text

```

### 2-5 沒有主鍵的表

```sql
SELECT t.table_schema, t.table_name
FROM information_schema.tables t
LEFT JOIN information_schema.table_constraints c
  ON c.table_schema = t.table_schema AND c.table_name = t.table_name AND c.constraint_type = 'PRIMARY KEY'
WHERE t.table_type = 'BASE TABLE' AND c.constraint_name IS NULL
  AND t.table_schema NOT IN ('mysql','sys','information_schema','performance_schema');
```

結果：

```text

```

### 2-6 DEFINER 物件數量

```sql
SELECT 'ROUTINE' AS type, COUNT(*) FROM information_schema.routines WHERE routine_schema NOT IN ('mysql','sys')
UNION ALL SELECT 'TRIGGER', COUNT(*) FROM information_schema.triggers WHERE trigger_schema NOT IN ('mysql','sys')
UNION ALL SELECT 'VIEW',    COUNT(*) FROM information_schema.views    WHERE table_schema   NOT IN ('mysql','sys')
UNION ALL SELECT 'EVENT',   COUNT(*) FROM information_schema.events;
```

結果：

```text

```

### 2-7 儲存引擎

```sql
SELECT engine, COUNT(*) FROM information_schema.tables
WHERE table_type = 'BASE TABLE' AND table_schema NOT IN ('mysql','sys','information_schema','performance_schema')
GROUP BY engine;
```

結果：

```text

```

### 2-8 參數

```sql
SELECT @@version, @@sql_require_primary_key, @@lower_case_table_names,
       @@character_set_server, @@collation_server, @@max_allowed_packet;
```

結果：

```text

```

---

## 3. 新 DB（cluster `a554244`）

### 3-1 參數（與 2-8 比對）

```bash
mysql -h public-a554244-akamai-prod-1053367-default.g2a.akamaidb.net -P 28707 -u akmadmin -p --ssl-mode=REQUIRED -e "
SELECT @@version, @@sql_require_primary_key, @@lower_case_table_names,
       @@character_set_server, @@collation_server, @@max_allowed_packet,
       @@max_connections, ROUND(@@innodb_buffer_pool_size/1024/1024/1024,1) AS buffer_pool_gb,
       @@innodb_flush_log_at_trx_commit, @@sync_binlog;"
```

結果：

```text

```

### 3-2 Cloud Manager 資訊

| 項目 | 值 |
|------|----|
| 儲存空間（總量／已用） |  |
| Manage Access 是否已有 `172.104.86.251/32` |  |

---

## 4. 抽樣計時（C-1～C-3）

| 項目 | 值 |
|------|----|
| 抽樣表（`<資料庫>.<表>`） |  |
| 抽樣表大小（GB，取 2-4） |  |
| C-1 mydumper `real` 秒數 |  |
| C-1 `du -sh /data/dump_sample` |  |
| C-2 myloader `real` 秒數 |  |
| C-3 slave 筆數 |  |
| C-3 新 DB 筆數 |  |
| 錯誤訊息（若有） |  |
