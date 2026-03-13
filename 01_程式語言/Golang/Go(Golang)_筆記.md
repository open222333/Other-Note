# Go(Golang) 筆記

## 目錄

- [Go(Golang) 筆記](#gogolang-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [Go 的版本管理工具](#go-的版本管理工具)
  - [gimme](#gimme)
  - [goenv](#goenv)
  - [gvm](#gvm)
- [Go modules 套件管理工具](#go-modules-套件管理工具)
- [安裝](#安裝)
  - [基本](#基本)
  - [CentOS7](#centos7)
  - [移除](#移除)

## 參考資料

[官方網站](https://go.dev/)

[Go 安裝](https://willh.gitbook.io/build-web-application-with-golang-zhtw/01.0/01.1)

[模組文檔](https://pkg.go.dev/)

[下載](https://go.dev/dl/)

# Go 的版本管理工具

## gimme

`安裝 gimme`

```bash
curl -sL -o /usr/local/bin/gimme https://raw.githubusercontent.com/travis-ci/gimme/master/gimme
chmod +x /usr/local/bin/gimme
```

使用 gimme 安裝 Go 版本

```bash
gimme 1.16.0
```

設定環境變數

```bash
export GOROOT_BOOTSTRAP=$GOROOT
export GIMME_ENV=$HOME/.gimme/envs/latest.env
source $HOME/.gimme/envs/latest.env
```

## goenv

`安裝 goenv`

```bash
git clone https://github.com/syndbg/goenv.git ~/.goenv
```

`將 ~/.goenv/bin 添加到您的 PATH 中，以便能夠在終端中執行 goenv`

```bash
echo 'export PATH="$HOME/.goenv/bin:$PATH"' >> ~/.bashrc
```

`使用 goenv install 命令安裝需要的 Go 版本`

```bash
goenv install 1.16.0
```

`設定全域 Go 版本`

```bash
goenv global 1.16.0
```

`局部 Go 版本`

```bash
cd /path/to/your/project
goenv local 1.16.0
```

## gvm

安裝 gvm

```bash
bash < <(curl -s -S -L https://raw.githubusercontent.com/moovweb/gvm/master/binscripts/gvm-installer)
source ~/.gvm/scripts/gvm
```

驗證 gvm 是否成功安裝

```bash
gvm version
```

使用 gvm 安裝 Go

```bash
gvm install go1.17
```

使用特定版本的 Go

```bash
gvm use go1.17
```

列出已經安裝的 Go 版本

```bash
gvm list
```

設定 Go 版本作為默認版本

```bash
gvm default go1.17
```

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

# 安裝

## 基本

[官方網站 下載](https://golang.org/dl/)

```
下載Go的二進制發行版。
選擇適用Linux系統的版本，通常是以`.tar.gz`結尾的文件。
```

`解壓縮Go二進制文件`

```bash
# 替換goX.X.X.linux-amd64.tar.gz中的實際文件名。
tar -C /usr/local -xzf goX.X.X.linux-amd64.tar.gz
```

`設置環境變數`

```bash
export PATH=$PATH:/usr/local/go/bin
export GOPATH=$HOME/go
export PATH=$PATH:$GOPATH/bin
```

`重新載入配置文件`

```bash
# .bash_profile: 這是一個在登錄時執行的配置文件。
# 當你登錄到一個bash shell時，它會被執行一次。
# 通常用於設置環境變數和其他登錄時需要執行的操作。

# .bashrc: 這是一個在每次新開的交互式bash shell中執行的配置文件。
# 它在你每次打開終端窗口或啟動新的bash shell時都會被執行。
# 通常用於設置別名、自定義命令、顯示提示符等。

# 在一些Linux系統中，.bash_profile可能不存在，但.bashrc通常是存在的。
# 如果你使用的是bash shell，你可以在.bash_profile中設置環境變數，然後在其中調用.bashrc，以確保登錄時和每次新的交互式shell都執行相應的配置。
source ~/.bashrc   # 或者 source ~/.zshrc，視你的shell而定
```

`檢查安裝是否成功`

```bash
go version
```

## CentOS7

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

## 移除

刪除之前解壓縮的 Go 安裝目錄

```bash
rm -rf /usr/local/go
```

移除環境變數

打開 Shell 配置文件 例如 .bashrc .bash_profile .zshrc

將這兩行從配置文件中刪除

```bash
export PATH=$PATH:/usr/local/go/bin
export GOPATH=$HOME/go
```

應用對環境變數的更改

```bash
source ~/.bashrc
```

檢查 Go 是否成功移除

```bash
go version
```
