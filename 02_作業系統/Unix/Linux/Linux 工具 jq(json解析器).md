# Linux 工具 jq(json解析器)

```
```

## 目錄

- [Linux 工具 jq(json解析器)](#linux-工具-jqjson解析器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [基本指令](#基本指令)

## 參考資料

[How to Install jq(JSON processor) on RHEL/CentOS 7/8](https://www.cyberithub.com/how-to-install-jq-json-processor-on-rhel-centos-7-8/)

## 基本指令

```bash
# Install EPEL Repository
yum install epel-release -y

# Update Your Server
yum update -y

# Install jq(JSON Processor) tool
yum install jq -y

# Verify jq package Installation
rpm -qa | grep -i jq

# Check jq version
jq -Version

# Remove jq(JSON Processor) from Server
yum remove jq -y

# Remove EPEL Repository
yum remove epel-release -y
```