# Jenkins(開源CI工具)

```
Jenkins是一個開源的CI工具，它提供了豐富的功能和插件生態系統，支援各種編程語言和版本控制系統。
```

## 目錄

- [Jenkins(開源CI工具)](#jenkins開源ci工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
	- [CentOS](#centos)
	- [Debian Ubuntu](#debian-ubuntu)
	- [Docker-compose](#docker-compose)
- [指令](#指令)

## 參考資料

[Jenkins 官方網站](https://www.jenkins.io/)

[Jenkins 官方文檔](https://www.jenkins.io/zh/doc/)

[Jenkins 官方文檔 eng](https://www.jenkins.io/doc/)

[Jenkins 安裝教學](https://www.jenkins.io/zh/doc/book/installing/)

# 安裝

## CentOS

```bash
# 安裝Java Development Kit (JDK)
yum install java-11-openjdk-devel

# 添加Jenkins的YUM Repository
wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

# 安裝Jenkins
yum install jenkins

# 啟動服務後
# http://your_server_ip_or_domain:8080
# 網頁將顯示Jenkins的安裝向導，要求輸入初始管理員密碼。
# 可以在伺服器上的 /var/lib/jenkins/secrets/initialAdminPassword 文件中找到此密碼。
```

## Debian Ubuntu

```bash
wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo apt-key add -
sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
apt-get update
apt-get install jenkins

# 啟動服務後
# http://your_server_ip_or_domain:8080
# 網頁將顯示Jenkins的安裝向導，要求輸入初始管理員密碼。
# 可以在伺服器上的 /var/lib/jenkins/secrets/initialAdminPassword 文件中找到此密碼。
```

## Docker-compose

```yml
version: '3'
services:
  jenkins:
    image: jenkins/jenkins:lts
    ports:
      - 8080:8080
    volumes:
      - jenkins_home:/var/jenkins_home
```

# 指令

```bash
# 啟動服務
systemctl start jenkins

# 查詢啟動狀態
systemctl status jenkins

# 重新啟動
systemctl restart jenkins

# 停止服務
systemctl stop jenkins

# 開啟開機自動啟動
systemctl enable jenkins

# 關閉開機自動啟動
systemctl disable jenkins
```