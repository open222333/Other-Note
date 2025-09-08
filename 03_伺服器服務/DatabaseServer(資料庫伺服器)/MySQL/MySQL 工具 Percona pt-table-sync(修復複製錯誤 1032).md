# MySQL 工具 Percona pt-table-sync(修復複製錯誤 1032)

```
Percona Toolkit 提供的工具，專門修復複製錯誤 (像 1032 找不到紀錄這種)。

--print：檢查差異，輸出 SQL
--execute：真的執行修復

適合用來修 Slave 的 1032 / 1062 複製錯誤

風險主要是 資料被強制改成 Master 的版本，所以 Slave 的異動會消失。

只要確認 Slave 只是備援 / 報表用，不需要保留獨立資料 → 風險其實可接受。

如果要保險，務必先用 --print 看清楚，然後再決定要不要 --execute。
```

## 目錄

- [MySQL 工具 Percona pt-table-sync(修復複製錯誤 1032)](#mysql-工具-percona-pt-table-sync修復複製錯誤-1032)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
  - [可能的風險](#可能的風險)
    - [降低風險的方法](#降低風險的方法)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)
  - [基本用法](#基本用法)
    - [修復 Master 與 Slave 的差異](#修復-master-與-slave-的差異)
    - [先檢查再修復](#先檢查再修復)
    - [修復整個資料](#修復整個資料)
    - [解決 Slave 報錯 (1032)，建議流程](#解決-slave-報錯-1032建議流程)

## 參考資料

[Percona Toolkit 官方文件 – pt-table-sync](https://docs.percona.com/percona-toolkit/pt-table-sync.html)

[Ubuntu 手冊頁 (man page)](https://manpages.ubuntu.com/manpages/plucky/man1/pt-table-sync.1p.html)

[Percona 官方部落格：使用 pt-table-sync 修復同步錯誤 Percona Toolkit + pt-table-checksum + pt-table-sync = Faster Replica Recovery](https://www.percona.com/blog/how-to-use-percona-toolkits-pt-table-sync-for-replica-tables-with-triggers-in-mysql/)

[Percona 社群：pt-table-checksum + pt-table-sync 修復 replica](https://percona.community/blog/2021/07/22/lets-be-insync/)

[PSCE MySQL 同步流程概述](https://www.psce.com/en/blog/2015/01/19/syncronizing-slaves-with-pt-table-sync-on-tables-with-triggers-and-foreign-keys-defined/)

### 心得相關

[Percona-Toolkit 之 pt-table-sync 总结](https://www.cnblogs.com/dbabd/p/10690429.html)

[Percona-Toolkit 之 pt-online-schema-change 总结](https://www.cnblogs.com/dbabd/p/10605629.html)

[Percona-Toolkit 之 pt-table-checksum 总结](https://www.cnblogs.com/dbabd/p/10653408.html)

## 可能的風險

資料被覆蓋或刪除

    pt-table-sync 的原理是比對 Master 和 Slave 的差異 → 然後 用 Master 的資料覆蓋 Slave。

    如果 Slave 上有「Master 沒有的資料」，它會被刪掉。

    如果 Slave 的資料跟 Master 不一樣，它會被更新成跟 Master 一致。

    所以一旦執行 --execute，Slave 上不同步的資料就會被修改/刪除。

誤操作的風險

    DSN 寫錯 (例如把 Master/Slave 順序寫反) → 可能會用 Slave 的資料覆蓋 Master 😱

    雖然 pt-table-sync 默認是「第一個 DSN 是來源 (master) → 第二個是目標 (slave)」，但一旦寫錯就很危險。

大表效能問題

    pt-table-sync 比對資料時，可能會掃描整張表 → 如果表很大，會吃 CPU / IO，導致 Master 或 Slave 短暫負載升高。

    線上高峰時操作要小心。

不適合頻繁異動的表

    如果表在 Master 上變動很快 (INSERT/UPDATE/DELETE 很頻繁)，比對過程中可能還是會產生差異。

    最好選低峰期操作，或只針對錯誤的表修復。

### 降低風險的方法

先用 --print 看差異，不要直接 --execute

看清楚 SQL 是不是合理，必要時先人工檢查。

```sh
pt-table-sync --print ... > sync.sql
```

針對單一表修復

先修正在錯的表（例如 table），不要一口氣跑全資料庫。

加上備份

在執行前可以備份 Slave 的表：

```sh
mysqldump -u root -p database table > table_slave.sql
```

避免在 Master 高峰期跑，減少對業務的影響。

# 安裝

## Debian (Ubuntu)

```bash
apt-get install percona-toolkit
```

## RedHat (CentOS)

```bash
yum install percona-toolkit
```

# 指令

```sh
pt-table-sync [OPTIONS] DSN [DSN]
```

## 基本用法

### 修復 Master 與 Slave 的差異

```sh
pt-table-sync --execute --verbose \
  h=master_host,u=repl,p=repl_pass,D=database,t=table \
  h=slave_host,u=repl,p=repl_pass
```

參數解釋：

```
--execute → 真正執行修復（沒有這個選項時只會輸出 SQL，不會動資料）
--verbose → 顯示過程
h → host
u → 使用者
p → 密碼
D → database
t → table
```

### 先檢查再修復

只顯示 SQL，不執行

```sh
pt-table-sync --print \
  h=master_host,u=repl,p=repl_pass,D=database,t=table \
  h=slave_host,u=repl,p=repl_pass
```

確認輸出的 SQL 正確後，再加上 --execute 來執行

```sh
pt-table-sync --execute \
  h=master_host,u=repl,p=repl_pass,D=database,t=table \
  h=slave_host,u=repl,p=repl_pass
```

### 修復整個資料

如果不只是單一 table，而是整個 DB：

```sh
pt-table-sync --execute --verbose \
  h=master_host,u=repl,p=repl_pass,D=database \
  h=slave_host,u=repl,p=repl_pass
```

甚至可以針對所有 DB：

```sh
pt-table-sync --execute --verbose \
  h=master_host,u=repl,p=repl_pass \
  h=slave_host,u=repl,p=repl_pass
```

### 解決 Slave 報錯 (1032)，建議流程

先讓 Slave 停下來

```sql
STOP SLAVE;
```

執行 pt-table-sync 修復資料

```sh
pt-table-sync --execute --verbose \
  h=master_host,u=repl,p=repl_pass,D=database,t=table \
  h=slave_host,u=repl,p=repl_pass
```

再啟動 Slave

```sql
START SLAVE;
SHOW SLAVE STATUS\G
```

