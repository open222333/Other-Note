# Nagios Remote Plugin Executor(nrpe)(Nagios 監控系統)

```
NRPE 讓 Nagios 監控主機能 遠端登入伺服器、執行監控指令（plugins）並取得結果。

NRPE 通常用來執行這些監控腳本（plugins）：

check_load：CPU 負載

check_disk：磁碟使用率

check_mem：記憶體使用率

check_procs：程序數量

check_swap：swap 使用率
```

架構關係圖(NRPE 的工作原理)

```
[Nagios Server]  ←→  [NRPE (port 5666)]  ←→  [被監控的伺服器]
```

流程

```
Nagios 主伺服器（通常安裝在中央監控機）發出請求，連線到主機的 TCP 5666 埠。
主機上 NRPE 代理程式接收到請求後，執行 /etc/nagios/nrpe.cfg 中允許的指令。
執行結果（例如 CPU、記憶體、磁碟空間、服務狀態等）回傳給 Nagios 主機。
```

## 目錄

- [Nagios Remote Plugin Executor(nrpe)(Nagios 監控系統)](#nagios-remote-plugin-executornrpenagios-監控系統)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[]()

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
```

## Homebrew (MacOS)

```bash
```

## Windows

```
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令
