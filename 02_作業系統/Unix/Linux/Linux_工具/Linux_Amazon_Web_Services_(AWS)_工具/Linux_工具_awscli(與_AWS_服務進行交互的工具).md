# Linux 工具 awscli(與 AWS 服務進行交互的工具)

```
Amazon Web Services (AWS) 官方提供的用於在命令行界面下與 AWS 服務進行交互的工具
```

## 目錄

- [Linux 工具 awscli(與 AWS 服務進行交互的工具)](#linux-工具-awscli與-aws-服務進行交互的工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[]()

# 安裝

```bash
# 安裝 awscli
# 在 Ubuntu 或 Debian 上：
apt-get update
apt-get install awscli

# 在 CentOS 或 Red Hat 上：
yum install awscli

# 在 macOS 上，使用 Homebrew 安裝：
brew install awscli
```

# 指令

```bash
# 使用 aws s3 cp 指令下載文件
# 請將 {your_file} 替換為您要下載的 S3 存儲桶中的文件路徑
# 請將 {destination_path} 替換為您希望文件下載到的本地路徑
aws s3 cp s3://inhand/{your_file} {destination_path}
```
