# Linux 工具 top htop(主機資源監控)

```
```

## 目錄

- [Linux 工具 top htop(主機資源監控)](#linux-工具-top-htop主機資源監控)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[top(1) — Linux manual page](https://man7.org/linux/man-pages/man1/top.1.html)

[htop(1) — Linux manual page](https://man7.org/linux/man-pages/man1/htop.1.html)

[htop explained](https://peteris.rocks/blog/htop/)

[Linux htop 系統程序監控工具簡介](https://matthung0807.blogspot.com/2019/06/linux-htop.html)

[htop 每個欄位](https://medium.com/starbugs/do-you-understand-htop-ffb72b3d5629)

# 安裝

```bash
# 安裝
# CentOS Linux:
yum install epel-release
yum update

# Debian/Ubuntu Linux:
apt-get install htop
```

設定檔位置:`$HOME/.config/htop/htoprc` - 直接把這個檔案刪除，會恢復預設的設定值

```
標頭中每個代號的意思如下：

    PID：Process ID，程序編號
    USER：開啟程序的使用者名稱
    PRI：Priority，Linux kernal排程優先順序，數值從0（最高優先權）到139（最低優先權）。
    NI：Niceness，數值從-20（最高優先權）到19（最低優先權）。
    VIRT：Virtual memory usage，虛擬記憶體用量。
    RES：Resident memory usage，常駐記憶體用量。
    SHR：Shared memory usage，共享記憶體用量。
    S：Process state，程序狀態。
    D：不可中斷的睡眠狀態（例如IO）
    R：執行中或可執行
    S：可中斷的睡眠
    T：工作停止
    t：除錯中斷
    Z：Zombie Process，殭屍程序
    CPU%：CPU使用率。
    MEM%：記憶體使用率。
    TIME+：程序執行時間。
    Comamnd：執行程序的命令。
```

# 指令

```bash
```