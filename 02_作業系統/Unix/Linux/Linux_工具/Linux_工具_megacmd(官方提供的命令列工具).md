# Linux 工具 megacmd(官方提供的命令列工具)

```
MEGA 官方提供的命令列工具 megacmd，然後透過 Python 執行命令來整合功能
```

## 目錄

- [Linux 工具 megacmd(官方提供的命令列工具)](#linux-工具-megacmd官方提供的命令列工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)
  - [登入](#登入)
  - [上傳檔案](#上傳檔案)
  - [下載檔案](#下載檔案)
  - [刪除檔案或資料夾](#刪除檔案或資料夾)
  - [移動或重新命名](#移動或重新命名)
  - [建立新資料夾](#建立新資料夾)
  - [同步](#同步)
  - [分享檔案或資料夾](#分享檔案或資料夾)
  - [搜尋檔案](#搜尋檔案)
- [用法](#用法)
  - [Python](#python)

## 參考資料

[meganz/MEGAcmd Github](https://github.com/meganz/MEGAcmd)

[meganz/sdk Github](https://github.com/meganz/sdk)

# 安裝


## Debian (Ubuntu)

```bash
apt install megatools
```

## RedHat (CentOS)

```bash
```

# 指令

```bash
mega-cmd [命令] [選項] [參數]
```

## 登入

```sh
mega-login your_email@example.com your_password
```

登出 MEGA 帳戶

```sh
mega-logout
```

使用會話金鑰登入（安全性較高）

```sh
mega-login YOUR_SESSION_KEY
```

## 上傳檔案

```sh
mega-put /path/to/local/file /MEGA/folder/path
```

上傳整個目錄

```sh
mega-put /path/to/local/directory /MEGA/folder/path
```

## 下載檔案

```sh
mega-get /MEGA/folder/file /path/to/local/folder
```

下載整個資料夾

```sh
mega-get /MEGA/folder /path/to/local/directory
```

## 刪除檔案或資料夾

```sh
mega-rm /MEGA/folder/file_or_folder
```

## 移動或重新命名

```sh
mega-mv /MEGA/folder/file /MEGA/new/folder
```

重新命名檔案

```sh
mega-mv /MEGA/folder/oldname /MEGA/folder/newname
```

## 建立新資料夾

```sh
mega-mkdir /MEGA/new/folder
```

## 同步

```sh
mega-sync /path/to/local/folder /MEGA/folder
```

```sh
mega-sync --remove /path/to/local/folder
```

## 分享檔案或資料夾

```sh
mega-export -a /MEGA/folder/file_or_folder
```

```sh
mega-export -d /MEGA/folder/file_or_folder
```

## 搜尋檔案

```sh
mega-find filename
```

# 用法

## Python

```Python
import subprocess

# 上傳檔案
subprocess.run(["megacmd", "put", "local_file.txt", "/remote_folder/"])

# 列出檔案
subprocess.run(["megacmd", "ls", "/remote_folder/"])
```

```Python
import os
import subprocess
from src.logger import Log


class MegaCMDUploader:
    def __init__(self, email, password, mega_folder_path, log_level="DEBUG"):
        self.email = email
        self.password = password
        self.mega_folder_path = mega_folder_path

        self.logger = Log('MegaCMDUploader')
        self.logger.set_level(log_level)
        self.logger.set_msg_handler()

    def ensure_login(self):
        """檢查是否已登入，未登入則執行登入"""
        try:
            result = subprocess.run(["mega-whoami"], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
            if result.returncode != 0:  # 未登入
                self.logger.info("未登入，執行登入...")
                login = subprocess.run(["mega-login", self.email, self.password], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                if login.returncode == 0:
                    self.logger.info("登入成功！")
                else:
                    raise Exception(f"登入失敗：{login.stderr}")
            else:
                self.logger.info("已登入，不需要重新登入。")
        except Exception as e:
            raise Exception(f"登入檢查失敗：{str(e)}")

    def upload_files(self, local_files):
        """批量上傳檔案"""
        for file_path in local_files:
            if not os.path.exists(file_path):
                self.logger.info(f"檔案不存在，跳過：{file_path}")
                continue

            dest_file_name = os.path.basename(file_path)  # 使用原檔名
            command = ["mega-put", file_path, f"{self.mega_folder_path}/{dest_file_name}"]

            try:
                self.logger.info(f"上傳中：{file_path} -> {self.mega_folder_path}/{dest_file_name}")
                result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                if result.returncode == 0:
                    self.logger.info(f"成功上傳：{dest_file_name}")
                else:
                    self.logger.error(f"上傳失敗：{result.stderr}")
            except Exception as e:
                self.logger.error(f"上傳時發生例外：{str(e)}")
```