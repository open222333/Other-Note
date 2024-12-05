# Python 工具 megacmd(官方提供的命令列工具)

```
MEGA 官方提供的命令列工具 megacmd，然後透過 Python 執行命令來整合功能
```

## 目錄

- [Python 工具 megacmd(官方提供的命令列工具)](#python-工具-megacmd官方提供的命令列工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)

## 參考資料

[meganz/MEGAcmd Github](https://github.com/meganz/MEGAcmd)

# 指令

```bash
```

# 安裝

```bash
apt install megatools
```

# 用法

```Python
import subprocess

# 上傳檔案
subprocess.run(["megacmd", "put", "local_file.txt", "/remote_folder/"])

# 列出檔案
subprocess.run(["megacmd", "ls", "/remote_folder/"])
```
