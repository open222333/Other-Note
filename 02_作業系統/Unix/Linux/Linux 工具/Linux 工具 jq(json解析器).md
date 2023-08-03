# Linux 工具 jq(json解析器)

```
```

## 目錄

- [Linux 工具 jq(json解析器)](#linux-工具-jqjson解析器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[How to Install jq(JSON processor) on RHEL/CentOS 7/8](https://www.cyberithub.com/how-to-install-jq-json-processor-on-rhel-centos-7-8/)

# 安裝

```bash
# Install EPEL Repository
yum install epel-release -y

# Update Your Server
yum update -y

# Install jq(JSON Processor) tool
yum install jq -y

# Verify jq package Installation
rpm -qa | grep -i jq

# Remove jq(JSON Processor) from Server
yum remove jq -y

# Remove EPEL Repository
yum remove epel-release -y

# 安裝 jq
# 在 Ubuntu 或 Debian 上：
apt-get update -y
apt-get install jq -y

# 在 CentOS 或 Red Hat 上：
yum install jq -y

# 在 Fedora 上：
dnf install jq -y

# 在 openSUSE 上：
zypper install jq
```

# 指令

```bash
# Check jq version
jq -Version

# 解析 JSON 數據
# 假設有一個 JSON 檔案 data.json，其中包含以下內容：
# {
#   "name": "John",
#   "age": 30,
#   "email": "john@example.com"
# }

# 使用 jq 查詢和顯示 name 的值
jq '.name' data.json

# 使用 jq 過濾和顯示 age 的值
jq '.age' data.json

# 使用 jq 格式化 JSON 數據並將結果輸出到新的檔案 result.json
jq '.' data.json > result.json
```