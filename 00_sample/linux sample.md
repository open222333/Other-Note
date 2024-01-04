# Linux 工具 sample()

```
```

## 目錄

- [Linux 工具 sample()](#linux-工具-sample)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[]()

# 安裝

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令

## 服務操作

```bash
# 啟動服務
systemctl start sample

# 查詢啟動狀態
systemctl status sample

# 重新啟動
systemctl restart sample

# 停止服務
systemctl stop sample

# 開啟開機自動啟動
systemctl enable sample

# 關閉開機自動啟動
systemctl disable sample

### 不是所有的服務都支持 ###
# (start, stop, restart, try-restart, reload, force-reload, status)
# 重新載入
service sample reload
```
