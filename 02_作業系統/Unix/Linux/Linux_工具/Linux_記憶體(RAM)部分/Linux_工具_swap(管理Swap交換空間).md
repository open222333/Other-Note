# Linux 工具 swap(管理 Swap 交換空間)

```
swapon  - 啟用 swap 空間
swapoff - 停用 swap 空間
mkswap  - 建立 swap 空間
```

## 目錄

- [Linux 工具 swap(管理 Swap 交換空間)](#linux-工具-swap管理-swap-交換空間)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
  - [查看 Swap 狀態](#查看-swap-狀態)
  - [建立 Swap 檔案](#建立-swap-檔案)
  - [啟用 / 停用 Swap](#啟用--停用-swap)
  - [設定開機自動掛載](#設定開機自動掛載)
  - [調整 Swappiness](#調整-swappiness)

## 參考資料

[swapon(8) — Linux manual page](https://man7.org/linux/man-pages/man8/swapon.8.html)

[swapoff(8) — Linux manual page](https://man7.org/linux/man-pages/man8/swapoff.8.html)

[mkswap(8) — Linux manual page](https://man7.org/linux/man-pages/man8/mkswap.8.html)

[swap 的建立 - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0230filesystem.php#swap)

# 指令

## 查看 Swap 狀態

```bash
# 查看目前 swap 使用情況（透過 free）
free -h
              total        used        free      shared  buff/cache   available
Mem:           7.7G        2.1G        3.8G        312M        1.7G        5.0G
Swap:          2.0G          0B        2.0G

# 查看所有 swap 裝置與檔案的詳細資訊
swapon --show
NAME      TYPE SIZE USED PRIO
/swapfile file   2G   0B   -2

# 查看 /proc/swaps（原始資訊）
cat /proc/swaps
Filename        Type        Size    Used    Priority
/swapfile       file        2097148 0       -2
```

## 建立 Swap 檔案

```bash
# 方法一：使用 fallocate 建立指定大小的 swap 檔案（較快）
fallocate -l 2G /swapfile

# 方法二：使用 dd 建立 swap 檔案（較通用）
dd if=/dev/zero of=/swapfile bs=1M count=2048

# 設定檔案權限（只允許 root 存取）
chmod 600 /swapfile

# 將檔案格式化為 swap 格式
mkswap /swapfile

# mkswap 常用選項
mkswap [選項] 裝置或檔案
    -L label  ：為 swap 空間設定標籤名稱
    -U uuid   ：為 swap 空間設定 UUID
    -f        ：強制建立，即使大小不建議也繼續
```

## 啟用 / 停用 Swap

```bash
# 啟用指定的 swap 檔案或裝置
swapon /swapfile

# swapon 常用選項
swapon [選項] 裝置或檔案
    -a        ：啟用 /etc/fstab 中所有標記為 swap 的裝置
    -s        ：顯示目前所有 swap 的摘要資訊（等同 --show）
    -p N      ：設定優先權（數字越大優先使用，預設 -1）
    -e        ：若裝置不存在則略過，不報錯

# 停用指定的 swap 檔案或裝置
swapoff /swapfile

# 停用所有 swap
swapoff -a
```

## 設定開機自動掛載

```bash
# 編輯 /etc/fstab，加入以下一行讓開機自動啟用 swap
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# /etc/fstab 欄位說明
# /swapfile  none  swap  sw  0  0
#  ^裝置路徑  ^掛載點(swap固定none)  ^檔案系統類型  ^選項  ^dump  ^fsck順序

# 驗證設定（重新掛載 fstab 中的 swap）
swapon -a
swapon --show
```

## 調整 Swappiness

```bash
# 查看目前 swappiness 值（0~100，預設通常為 60）
# 值越低：系統越傾向使用實體記憶體；值越高：系統越積極使用 swap
cat /proc/sys/vm/swappiness

# 臨時調整 swappiness（重開機後失效）
sysctl vm.swappiness=10

# 永久調整 swappiness（寫入設定檔）
echo 'vm.swappiness=10' | sudo tee -a /etc/sysctl.conf
sysctl -p

# swappiness 建議值
# 10  ：適合記憶體充足的桌面或伺服器環境
# 60  ：系統預設值
# 100 ：儘量使用 swap，適合記憶體極度不足的情況
```
