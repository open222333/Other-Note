# Golang 模組 monstache(MongoDB to Elasticsearch)

```
可用於將 MongoDB 數據同步到 Elasticsearch 的最全面的庫
```

## 目錄

- [Golang 模組 monstache(MongoDB to Elasticsearch)](#golang-模組-monstachemongodb-to-elasticsearch)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟](#安裝步驟)
- [用法](#用法)

## 參考資料

[github.com/rwynn/monstache GoDoc 文檔](https://pkg.go.dev/github.com/rwynn/monstache)

[文檔](https://rwynn.github.io/monstache-site/)

# 安裝步驟

```bash
cd ~/build # somewhere outside your $GOPATH
git clone https://github.com/rwynn/monstache.git
cd monstache
git checkout <branch-or-tag-to-build>
go install
# monstache binary should now be in $GOPATH/bin
```

# 用法

```bash
# 查看版本
monstache -v

# 啟動(需先建立 TOML config file.)
monstache -f /path/to/config.toml
```
