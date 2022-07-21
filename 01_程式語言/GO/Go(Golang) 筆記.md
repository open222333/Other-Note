# Go(Golang) 筆記

## 目錄

- [Go(Golang) 筆記](#gogolang-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [Go modules 套件管理工具](#go-modules-套件管理工具)

## 參考資料

[官方網站](https://go.dev/)

[安裝](https://go.dev/doc/install)

[模組文檔](https://pkg.go.dev/)

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