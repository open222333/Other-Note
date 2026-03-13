# Linux 工具 systemctl(初始化系統)

```
systemctl 是 systemd 的主要控制指令，systemd 是現在 Linux 最主流的「初始化系統」（init system），它負責：

系統開機流程
服務（daemon）管理
日誌管理（journald）
設定掛載點、網路、開機目標等
```

## 目錄

- [Linux 工具 systemctl(初始化系統)](#linux-工具-systemctl初始化系統)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [幾乎一定有 systemctl 的發行版](#幾乎一定有-systemctl-的發行版)
- [沒有 systemctl 的情況](#沒有-systemctl-的情況)
  - [沒有 systemctl 可改用舊的管理方式](#沒有-systemctl-可改用舊的管理方式)
- [指令](#指令)
  - [檢查是否支援 systemctl](#檢查是否支援-systemctl)
  - [服務操作](#服務操作)
  - [查出所有 已啟用開機自動啟動 的服務](#查出所有-已啟用開機自動啟動-的服務)
  - [看目前正在執行的服務](#看目前正在執行的服務)


## 參考資料

[systemctl(1) — Linux man page](https://linux.die.net/man/1/systemctl)

[systemctl(1) — Linux manual page](https://man7.org/linux/man-pages/man1/systemctl.1.html)

[systemctl(1) — Linux debian manpage](https://manpages.debian.org/stretch/systemd/systemctl.1.en.html)

# 幾乎一定有 systemctl 的發行版

| 發行版                      | 是否使用 systemd | 備註                    |
| ------------------------ | ------------ | --------------------- |
| **Ubuntu 15.04+**        | ✅            | 自 2015 年起全面改用 systemd |
| **Debian 8+ (Jessie)**   | ✅            | 預設 systemd            |
| **CentOS 7+ / RHEL 7+**  | ✅            | 完全用 systemd           |
| **Fedora 15+**           | ✅            | 最早導入 systemd 的發行版之一   |
| **Rocky / AlmaLinux 8+** | ✅            | 與 RHEL 相同             |
| **openSUSE / SUSE 12+**  | ✅            | 預設 systemd            |

# 沒有 systemctl 的情況

| 發行版 / 系統                               | 使用的 init 系統            | 備註                          |
| -------------------------------------- | ---------------------- | --------------------------- |
| **CentOS 6 / RHEL 6 / Ubuntu 14.04**   | `SysVinit` / `upstart` | 使用 `service` / `chkconfig`  |
| **Alpine Linux**                       | `OpenRC`               | 用 `rc-service`, `rc-update` |
| **BusyBox 系統 (Docker base, 路由器, IoT)** | `busybox init`         | 沒有 `systemctl`              |
| **WSL (早期版本)**                         | 可能不啟動 systemd          | 需手動啟用 systemd 支援            |

## 沒有 systemctl 可改用舊的管理方式

| 功能       | 舊系統指令                  |
| -------- | ---------------------- |
| 啟動服務     | `service <name> start` |
| 停止服務     | `service <name> stop`  |
| 設定開機自動啟動 | `chkconfig <name> on`  |
| 檢查開機狀態   | `chkconfig --list`     |

# 指令

## 檢查是否支援 systemctl

```sh
ps 1
```

輸出

```
PID TTY      STAT   TIME COMMAND
1   ?        Ss     0:03 /sbin/init
```

```sh
stat /sbin/init
```

輸出

```
/sbin/init -> /lib/systemd/systemd
```

代表系統使用 systemd，因此可用 systemctl

## 服務操作

```bash
# 啟動服務
systemctl start <服務>

# 查詢啟動狀態
systemctl status <服務>

# 重新啟動
systemctl restart <服務>

# 停止服務
systemctl stop <服務>

# 開啟開機自動啟動
systemctl enable <服務>

# 關閉開機自動啟動
systemctl disable <服務>

### 不是所有的服務都支持 ###
# (start, stop, restart, try-restart, reload, force-reload, status)
# 重新載入
service <服務> reload
```

## 查出所有 已啟用開機自動啟動 的服務

```sh
systemctl list-unit-files --type=service --state=enabled
```

```sh
systemctl --type=service --state=enabled
```

## 看目前正在執行的服務

```sh
systemctl list-units --type=service --state=running
```