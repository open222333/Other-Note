# FileZilla(FTP軟體)

```
FileZilla 是一個免費且開源的跨平台 FTP (File Transfer Protocol) 軟體，用於在電腦之間傳輸檔案。它提供了一個簡單易用的用戶界面，可以幫助您輕鬆地上傳和下載檔案，以及管理遠程伺服器上的檔案。
```

## 目錄

- [FileZilla(FTP軟體)](#filezillaftp軟體)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Linux](#linux)

## 參考資料

[官方網站](https://filezilla-project.org/)

[官方文檔 Documentation](https://wiki.filezilla-project.org/Documentation)

[Installing, configuring and accessing FTP server via Filezilla in Centos 7](https://swwapnilp.medium.com/installing-configuring-and-accessing-ftp-server-via-filezilla-in-centos-7-5b99fad1f69c)

# 安裝

## Linux

```bash
# 安裝 必要工具

# File Transfer Protocol（FTP）是一種用於在計算機之間傳輸文件的協議。
# ftp 軟件包包含了命令行工具，允許用戶使用 FTP 協議進行文件傳輸。
# 通過在命令行中輸入 ftp 命令，您可以連接到遠程 FTP 服務器並進行文件傳輸。
yum install ftp -y

# Very Secure FTP Daemon（vsftpd）是一個用於 CentOS 和其他 Linux 發行版的 FTP 服務器軟件。
# 它提供了一個安全、高效的方式，讓其他用戶可以通過 FTP 協議訪問您的系統上的文件。
# vsftpd 軟件包包含了 vsftpd 服務器以及相關的配置工具。
yum install vsftpd -y
```

```conf
; /etc/vsftpd/vsftpd.conf
# 禁用匿名登錄
anonymous_enable=NO

# 啟用 ASCII 模式上傳和下載
ascii_upload_enable=YES
ascii_download_enable=YES

# 顯示用戶連接時的歡迎消息
ftpd_banner=Welcome to My FTP service.

# 使用本地時間顯示文件修改時間
use_localtime=YES

# 啟用匿名用戶上傳和創建目錄、寫入功能
anon_upload_enable=YES
anon_mkdir_write_enable=YES

# 設置文件的默認打開模式為 0777，可讀、可寫、可執行對所有用戶
file_open_mode=0777


systemctl restart vsftpd
```

```bash
# 安裝用戶端

# 在 Ubuntu 或基於 Debian 的系統上安裝 FileZilla
apt update
apt install filezilla -y

# 在 Fedora 系統上安裝 FileZilla
dnf install filezilla -y

# 在 CentOS 或 RHEL 系統上安裝 FileZilla
yum update
yum install epel-release -y
yum install filezilla -y
```