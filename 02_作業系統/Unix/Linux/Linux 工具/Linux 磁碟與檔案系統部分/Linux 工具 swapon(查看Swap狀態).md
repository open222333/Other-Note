# Linux 工具 swapon(查看Swap狀態)

```
```

## 目錄

- [Linux 工具 swapon(查看Swap狀態)](#linux-工具-swapon查看swap狀態)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[swapon(8) — Linux manual page](https://man7.org/linux/man-pages/man8/swapon.8.html)

# 指令

```bash
# 查看目前的交換空間狀態
swapon -s

# 啟用指定的交換分區或交換檔案
# 請將 {device_or_file} 替換為您要啟用的交換分區路徑或交換檔案路徑
swapon {device_or_file}

# 禁用指定的交換分區或交換檔案
# 請將 {device_or_file} 替換為您要禁用的交換分區路徑或交換檔案路徑
swapoff {device_or_file}

# 創建一個交換檔案並設定其大小
# 請將 {file_path} 替換為您希望創建的交換檔案的路徑
# 請將 {size} 替換為您希望設定的交換檔案大小（例如：1G、512M 等）
dd if=/dev/zero of={file_path} bs=1M count={size}
chmod 600 {file_path}
mkswap {file_path}
```
