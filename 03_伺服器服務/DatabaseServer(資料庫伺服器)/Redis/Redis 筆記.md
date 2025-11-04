# Redis 筆記

## 目錄

- [Redis 筆記](#redis-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [GUI相關](#gui相關)
    - [安裝步驟 CentOS7](#安裝步驟-centos7)
    - [安裝步驟 Mac](#安裝步驟-mac)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Mac](#mac)
- [配置文件](#配置文件)
- [指令](#指令)
  - [遠端測試](#遠端測試)
- [master-slave 配置](#master-slave-配置)
  - [slave 的設定檔](#slave-的設定檔)

## 參考資料

[官方網站](https://redis.io/)

[官方文檔](https://redis.io/documentation)

[官方文檔 redis 配置 設定檔](https://redis.io/topics/config)

[官方文檔 教學](https://www.tutorialspoint.com/redis/index.htm)

[Redis Master-Slave](https://stevenitlife.blogspot.com/2018/09/redis-master-slave.html)

### GUI相關

[Redis GUI工具](https://tableplus.com/)

[Redis GUI工具 (下載點在下方介紹)](https://github.com/qishibo/AnotherRedisDesktopManager)

[Redis GUI工具 (Mac)](https://github.com/qishibo/AnotherRedisDesktopManager/releases)

### 安裝步驟 CentOS7

[Centos安裝與配置Redis](https://iter01.com/569100.html)

[Redis系列 - 環境建置篇](https://jed1978.github.io/2018/05/02/Redis-Environment-Installation-Configuration.html)

### 安裝步驟 Mac

[Install Redis on macOS](https://redis.io/docs/getting-started/installation/install-redis-on-mac-os/)

# 安裝

## Debian (Ubuntu)

```bash
apt update
```

```sh
apt install redis-server -y
```

## RedHat (CentOS)

```bash
# 新增EPEL倉庫並更新yum源
yum install epel-release -y
yum update -y

# 安裝Redis資料庫
yum -y install redis
```

`設定檔`

```bash
vim /etc/redis.conf
```

```conf
# 註解或改成 0.0.0.0
bind 127.0.0.1

port 6379

# 修改密碼
equirepass pwd

timeout 10

# 關閉保護模式
protected-mode no

# 其他拿掉
save ""
```

## Mac

```bash
# 安裝
brew install redis

# 測試 Redis 安裝
# 如果成功，將看到 Redis 的啟動日誌，並且 Redis 將在前台運行。
# 要停止 Redis，請輸入 Ctrl-C。
redis-server

# 後台啟動進程 啟動 Redis 並在登錄時重新啟動它。
brew services start redis

# 檢查已啟動的託管 Redis 的狀態
# 如果服務正在運行:
# redis (homebrew.mxcl.redis)
# Running: ✔
# Loaded: ✔
# User: miranda
# PID: 67975
brew services info redis

# 停止服務
brew services stop redis

# 重啟
brew services restart redis

# 進入 redis
redis-cli
```

# 配置文件

`/etc/redis/redis.conf`

```conf
# 註解或改成 0.0.0.0
bind 127.0.0.1
bind 0.0.0.0

bind 127.0.0.1 -::1
# Redis 支援 IPv4 和 IPv6
bind 0.0.0.0 ::1

port 6379

# 修改密碼 取消註解並改成想設定的密碼
# requirepass foobared
requirepass pwd

# 閒置連線自動關閉時間（秒）（通常預設為 0，代表永不超時）
# timeout 0
timeout 10

# 關閉永久化
# 停用自動快照（RDB）持久化功能。
# 也就是說 Redis 不會自動將資料寫入 dump.rdb 檔案，除非手動執行 SAVE 或 BGSAVE 指令。
save ""

# 預設的 Redis 會定期自動保存資料 會在 /var/lib/redis/dump.rdb（或容器的 /data/dump.rdb）自動產生快照
save 900 1    # 若 900 秒內有至少 1 個 key 被修改，則儲存一次
save 300 10   # 若 300 秒內有至少 10 個 key 被修改，則儲存一次
save 60 10000 # 若 60 秒內有至少 10000 個 key 被修改，則儲存一次
```

# 指令

```sh
# 啟動服務
systemctl start redis
# 開機啟動
systemctl enable redis
systemctl enable redis-server
# 查詢啟動狀態
systemctl status redis
# 重啟
systemctl restart redis
# 停止
systemctl stop redis
```

## 遠端測試

```sh
redis-cli -h <Redis-server-IP> -p 6379
```

# master-slave 配置

## slave 的設定檔

預設的設定檔

```
/etc/redis/redis.conf
```

複製一份成 redis-slave.conf

修改裡面的內容如下:

```conf
; port 是 slave redis 要傾聽的 port 號
; master 已經使用了預設的 6379
; 這裡改成 6380 避免衝突
port 6380

; 指定 pidfile
; UNIX / Linux 的習慣
; 許多程式執行起來後會產生一個 pid 檔
; 如果要把這個程式停掉，可以刪除 pid 檔
; 程式就會停止了
pidfile /var/run/redis_6380.pid

; dir 是運行過程中的 dump 檔要存在那個目錄
dir ./slave

; slaveof 指出這個 slave 要跟隨那一個 master
; 這裡指向 port 6379 的那一個 Redis
slaveof 127.0.0.1 6379


; 設定redis master
slaveof {redis-master ip} {port}

; 只讀
slave-read-only yes

; 關閉永久化
save ""
```

```bash
# 重啟redis-slave
redis-server restart
# 檢查redis master-slave
redis-cli
info replicationÏ
```
