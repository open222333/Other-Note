# ProxySQL master/slave（HA 熱備，無自動切換機制）

## 目錄

- [架構說明](#架構說明)
- [節點資訊](#節點資訊)
- [Hostgroup 設計](#hostgroup-設計)
- [路由規則](#路由規則)
- [應用連線帳號](#應用連線帳號)
- [修改後端 IP（舊 → 新）](#修改後端-ip舊--新)
  - [連入 ProxySQL Admin](#連入-proxysql-admin)
  - [更新後端節點 IP](#更新後端節點-ip)
  - [確認結果](#確認結果)
- [全新部署步驟](#全新部署步驟)
  - [Step 1：MySQL 主機建立帳號](#step-1mysql-主機建立帳號)
  - [Step 2：部署 ProxySQL](#step-2部署-proxysql兩台相同操作)
  - [Step 3：設定後端節點](#step-3設定後端節點)
  - [Step 4：設定監控帳號](#step-4設定監控帳號)
  - [Step 5：設定應用連線帳號](#step-5設定應用連線帳號)
  - [Step 6：設定路由規則](#step-6設定路由規則)
  - [Step 7：驗證](#step-7驗證)
- [手動故障切換流程](#手動故障切換流程無自動機制)

---

## 架構說明

兩台 ProxySQL 設定完全相同，分別部署在不同主機，共同代理同一組 MySQL master/slave。

- **MySQL 單節點故障**：ProxySQL 內建 monitor 自動偵測並移除故障節點，應用程式連線 IP 不需變更，讀取保持可用。
- **ProxySQL 本身故障**：無 Keepalived / VIP，需手動將應用程式連線目標改為另一台 ProxySQL。

```
應用程式 A ──→ proxysql_avnight_master（ProxySQL 1）
應用程式 B ──→ proxysql_avnight_slave（ProxySQL 2）
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
  MySQL Master             MySQL Slave
  192.168.1.10           192.168.1.20
  （HG1 寫入 + HG2 讀取）  （HG2 讀取）
```

## 節點資訊

| 角色 | IP | Port | 說明 |
|------|----|------|------|
| MySQL Master | 192.168.1.10 | 3306 | 處理寫入；同時加入 HG2 分擔讀取（weight 5） |
| MySQL Slave  | 192.168.1.20 | 3306 | 僅處理讀取（weight 3） |
| ProxySQL 1 | proxysql_avnight_master | 6032 / 6033 | Admin / MySQL 代理 |
| ProxySQL 2 | proxysql_avnight_slave  | 6032 / 6033 | Admin / MySQL 代理（設定與 ProxySQL 1 相同） |

## Hostgroup 設計

| Hostgroup | 用途 | 成員 | Weight |
|-----------|------|------|--------|
| HG 1（寫入） | INSERT / UPDATE / DDL / SELECT FOR UPDATE | MySQL Master | 1 |
| HG 2（讀取） | SELECT | MySQL Master | 5 |
| HG 2（讀取） | SELECT | MySQL Slave  | 3 |

> 讀流量分配：master weight=5 / slave weight=3 → master 約承擔 62.5% 讀取，slave 約 37.5%。

## 路由規則

| rule_id | match_pattern | 目標 HG | 說明 |
|---------|---------------|---------|------|
| 1 | `^INSERT` | 1 | 寫入 → master |
| 2 | `^UPDATE` | 1 | 更新 → master |
| 3 | `^SELECT.*FOR UPDATE$` | 1 | 排他鎖 → master（必須在 rule 4 之前） |
| 4 | `^SELECT` | 2 | 一般讀取 → HG2（master+slave） |
| 5 | `.*` | 1 | 其餘兜底 → master |

## 應用連線帳號

| username | default_hostgroup | max_connections |
|----------|-------------------|-----------------|
| avnight | 1（master） | 10000 |
| avnight_cpi_user | 1（master） | 10000 |
| laravel_admin | 1（master） | 10000 |
| proxysql | 1（master） | 10000 |

> `proxysql` 帳號僅存在於 proxysql_avnight_master，slave 端無此帳號。

---

## 修改後端 IP（舊 → 新）

以下 SQL 在**兩台 ProxySQL** 各自執行。

舊 IP：
- Master：`192.168.1.100`
- Slave：`192.168.1.200`

新 IP：
- Master：`192.168.1.10`
- Slave：`192.168.1.20`

### 連入 ProxySQL Admin

```bash
mysql -uadmin -p<ADMIN_PASSWORD> -h127.0.0.1 -P6032 --prompt='ProxySQL> '
```

### 更新後端節點 IP

```sql
-- 更新 HG1 master 寫入節點
UPDATE mysql_servers
SET hostname = '192.168.1.10'
WHERE hostname = '192.168.1.100' AND hostgroup_id = 1;

-- 更新 HG2 master 讀取節點
UPDATE mysql_servers
SET hostname = '192.168.1.10'
WHERE hostname = '192.168.1.100' AND hostgroup_id = 2;

-- 更新 HG2 slave 讀取節點
UPDATE mysql_servers
SET hostname = '192.168.1.20'
WHERE hostname = '192.168.1.200' AND hostgroup_id = 2;

LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

### 確認結果

```sql
SELECT hostgroup_id, hostname, port, status, weight, comment
FROM mysql_servers
ORDER BY hostgroup_id, hostname;
```

預期輸出：

```
+-------------+-----------------+------+--------+--------+------------------------+
| hostgroup_id| hostname        | port | status | weight | comment                |
+-------------+-----------------+------+--------+--------+------------------------+
| 1           | 192.168.1.10  | 3306 | ONLINE | 1      | jp-avnight-mysql-master|
| 2           | 192.168.1.10  | 3306 | ONLINE | 5      | jp-avnight-mysql-master|
| 2           | 192.168.1.20 | 3306 | ONLINE | 3      | jp-avnight-mysql-slave |
+-------------+-----------------+------+--------+--------+------------------------+
```

---

## 全新部署步驟

### Step 1：MySQL 主機建立帳號

在 **MySQL Master（192.168.1.10）** 執行：

```sql
-- ProxySQL 健康監控帳號
CREATE USER 'monitor'@'%' IDENTIFIED WITH mysql_native_password BY '<MONITOR_PASSWORD>';
GRANT REPLICATION CLIENT ON *.* TO 'monitor'@'%';

-- 應用連線帳號（依需求建立）
CREATE USER 'avnight'@'%' IDENTIFIED WITH mysql_native_password BY '<AVNIGHT_PASSWORD>';
GRANT ALL PRIVILEGES ON *.* TO 'avnight'@'%';

CREATE USER 'avnight_cpi_user'@'%' IDENTIFIED WITH mysql_native_password BY '<CPI_PASSWORD>';
GRANT ALL PRIVILEGES ON *.* TO 'avnight_cpi_user'@'%';

CREATE USER 'laravel_admin'@'%' IDENTIFIED WITH mysql_native_password BY '<LARAVEL_PASSWORD>';
GRANT ALL PRIVILEGES ON *.* TO 'laravel_admin'@'%';

FLUSH PRIVILEGES;
```

> **MySQL 8 注意**：ProxySQL 不支援 `caching_sha2_password`，建帳號時必須指定 `IDENTIFIED WITH mysql_native_password`。

### Step 2：部署 ProxySQL（兩台相同操作）

```bash
cp docker-compose.yml.default docker-compose.yml
cp conf/proxysql.cnf.default conf/proxysql.cnf
cp conf/phpmyadmin/config.user.inc.php.default conf/phpmyadmin/config.user.inc.php
mkdir -p logs/proxysql
docker-compose up -d
```

### Step 3：設定後端節點

```bash
mysql -uadmin -p<ADMIN_PASSWORD> -h127.0.0.1 -P6032 --prompt='ProxySQL> '
```

```sql
-- Hostgroup 主從對應
INSERT INTO mysql_replication_hostgroups (writer_hostgroup, reader_hostgroup, check_type)
VALUES (1, 2, 'read_only');

-- 後端節點
INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections, comment)
VALUES (1, '192.168.1.10',  3306, 1, 10000, 'jp-avnight-mysql-master');
INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections, comment)
VALUES (2, '192.168.1.10',  3306, 5, 10000, 'jp-avnight-mysql-master');
INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections, comment)
VALUES (2, '192.168.1.20', 3306, 3, 10000, 'jp-avnight-mysql-slave');

LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

### Step 4：設定監控帳號

```sql
SET mysql-monitor_username = 'monitor';
SET mysql-monitor_password = '<MONITOR_PASSWORD>';
LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

### Step 5：設定應用連線帳號

```sql
INSERT INTO mysql_users (username, password, active, default_hostgroup, max_connections)
VALUES ('avnight',          '<AVNIGHT_PASSWORD>', 1, 1, 10000);
INSERT INTO mysql_users (username, password, active, default_hostgroup, max_connections)
VALUES ('avnight_cpi_user', '<CPI_PASSWORD>',     1, 1, 10000);
INSERT INTO mysql_users (username, password, active, default_hostgroup, max_connections)
VALUES ('laravel_admin',    '<LARAVEL_PASSWORD>', 1, 1, 10000);

LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

### Step 6：設定路由規則

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^INSERT',              1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '^UPDATE',              1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (3, 1, '^SELECT.*FOR UPDATE$', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (4, 1, '^SELECT',              2, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (5, 1, '.*',                   1, 1);

LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

### Step 7：驗證

```sql
-- 後端節點狀態（全部 ONLINE）
SELECT hostgroup_id, hostname, port, status, weight
FROM mysql_servers
ORDER BY hostgroup_id, hostname;

-- 監控連線正常
SELECT * FROM monitor.mysql_server_connect_log
ORDER BY time_start_us DESC LIMIT 10;

-- 監控 Ping 正常
SELECT * FROM monitor.mysql_server_ping_log
ORDER BY time_start_us DESC LIMIT 10;

-- 路由規則確認
SELECT rule_id, active, match_pattern, destination_hostgroup, apply
FROM mysql_query_rules ORDER BY rule_id;
```

---

## 手動故障切換流程（無自動機制）

當 ProxySQL 1 故障時，將應用程式連線改指向 ProxySQL 2：

```bash
# 確認 ProxySQL 2 後端節點正常
mysql -uadmin -p<ADMIN_PASSWORD> -h<PROXYSQL_2_IP> -P6032 -e "
  SELECT hostgroup_id, hostname, port, status FROM mysql_servers;
"

# 將應用設定中的 ProxySQL 連線 IP 從 ProxySQL_1_IP 改為 ProxySQL_2_IP
# 重啟應用或熱更新設定
```

> 若需自動切換，部署 Keepalived + VIP 方案（見 proxysql/README.md「方案一」）。
