# Go(Golang) 筆記

## 目錄

- [Go(Golang) 筆記](#gogolang-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [Go modules 套件管理工具](#go-modules-套件管理工具)
- [安裝步驟 CentOS7](#安裝步驟-centos7)

## 參考資料

[官方網站](https://go.dev/)

[Go 安裝](https://willh.gitbook.io/build-web-application-with-golang-zhtw/01.0/01.1)

[模組文檔](https://pkg.go.dev/)

[下載](https://go.dev/dl/)

# Go modules 套件管理工具

```
套件管理工具，就像 Nodejs 的 npm 或是 python 的 pip
```

```bash
# 初始化 類似npm init
go mod init go-phishing
# go: creating new go.mod: module go-phishing

# go.mod（就像 Nodejs 中的 package.json）
cat go.mod

# 記錄的是所有用到的 package 版本，類似 Nodejs 的 package-lock.json
cat go.sum
```

# 安裝步驟 CentOS7

```bash
# 安裝 wget 下載工具
yum install wget -y

# 下載 go安裝包 以下網址取得下載網址
# https://go.dev/dl/
wget [url]

# 解壓縮到/usr/local
tar -C /usr/local -xzf go1.4.linux-amd64.tar.gz

# 添加環境變數到 ~/.bash_profile 或者 /etc/profile
vim ~/.bash_profile

export GOPATH=$HOME/gopath # 第三方套件安裝路徑
export PATH=$PATH:$HOME/go/bin:$GOPATH/bin

source ~/.bash_profile
```
