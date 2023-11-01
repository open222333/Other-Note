# Linux 工具 tmux(終端機管理工具)

```
```

## 目錄

- [Linux 工具 tmux(終端機管理工具)](#linux-工具-tmux終端機管理工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)
  - [設定檔](#設定檔)

## 參考資料

[Linux tmux 終端機管理工具使用教學](https://blog.gtwang.org/linux/linux-tmux-terminal-multiplexer-tutorial/)

[Tmux Command Examples To Manage Multiple Terminal Sessions](https://ostechnix.com/tmux-command-examples-to-manage-multiple-terminal-sessions/)

[tmux wiki](https://wiki.archlinux.org/title/tmux)

# 安裝

```bash
# Ubuntu Linux 安裝 tmux
apt install tmux

# CentOS Linux 安裝 tmux
yum install tmux
```

# 指令

```bash
# 詳細的用法 參考 tmux 的線上手冊
man tmux

# 進入 tmux
tmux

# 離開 tmux
Control + B 放開後 按 D

# 列出目前
tmux ls

# 再次進入
tmux a -t 1

# Creating detached sessions
tmux new -s (名字) -d

# Kill Tmux sessions
tmux kill-session -t (名字)

# 重啟tmux服務，重新讀取配置文件
tmux start
start-server
```

Panes（分割視窗）

在 tmux 的環境中，若想要將 window 視窗分割成多個 pane，並管理建立的 panes，可以使用以下的操作組合鍵：

```
組合鍵	說明
Ctrl+b 再輸入 %	垂直分割視窗。
Ctrl+b 再輸入 "	水平分割視窗。
Ctrl+b 再輸入 o	以輪流方式輪流切換 pane。
Ctrl+b 再輸入 方向鍵	切換至指定方向的 pane。
Ctrl+b 再輸入 空白鍵	切換佈局。
Ctrl+b 再輸入 !	將目前的 pane 抽出來，獨立建立一個 window 視窗。
Ctrl+b 再輸入 x	關閉目前的 pane。
```

Windows

若要建立與管理多個 windows 視窗，可以使用以下的操作組合鍵：

```
組合鍵	說明
Ctrl+b 再輸入 c	建立新 window 視窗（create）。
Ctrl+b 再輸入 w	以視覺化選單切換 window 視窗（很好用）。
Ctrl+b 再輸入 n	切換至下一個 window 視窗（next）。
Ctrl+b 再輸入 p	切換至上一個 window 視窗（previous）。
Ctrl+b 再輸入 數字鍵	切換至指定的 window 視窗。
Ctrl+b 再輸入 &	關閉目前的 window 視窗。
```

Sessions

每執行一個 tmux 就會建立一個 session，若要列出目前所有的 sessions，可以執行：

```bash
# 列出所有 Sessions
tmux ls

0: 5 windows (created Wed Dec  4 10:02:22 2019)
1: 1 windows (created Wed Dec  4 10:12:14 2019)

# 若要繼續使用指定的 session，可以使用 attach 並指定要續用的 session 編號：

# 繼續使用第 0 個 Sessions
tmux attach -t 0
```

而在 tmux 環境下，可以利用以下的操作組合鍵來操作 sessions：

```
組合鍵	說明
Ctrl+b 再輸入 $	重新命名目前的 session。
Ctrl+b 再輸入 d	分離目前的 session（detach），離開 tmux 環境。
Ctrl+b 再輸入 s	以視覺化選單切換 session（select，很好用）。
Ctrl+b 再輸入 L	切換至上一個使用過的 session。
Ctrl+b 再輸入 (	切換至上一個 session。
Ctrl+b 再輸入 )	切換至下一個 session。
```

搜尋功能

```
組合鍵	說明
Ctrl+b 再輸入 f	在所有 window 視窗中搜尋關鍵字（很好用）。
```

## 設定檔

tmux 的按鍵組合 prefix 是 Ctrl+b，可以用 config 去調整。

進入命令模式（按 Ctrl-b 然後 :）

tmux 設定檔檔案默認位置:

```
用戶配置
~/.tmux.conf
~/.config/tmux/tmux.conf
全局配置
/etc/tmux.conf
```

讓設定檔立即生效

```bash
restart tmux
source-file ~/.tmux.conf
```

使滑鼠可使用

```conf
setw -g mode-mouse on # 支持鼠標選取文本等
setw -g mouse-resize-pane on # 支持鼠標拖動調整面板的大小(通過拖動面板間的分割線)
setw -g mouse-select-pane on # 支持鼠標選中並切換面板
setw -g mouse-select-window on # 支持鼠標選中並切換窗口(通過點擊狀態欄窗口名稱)
```

