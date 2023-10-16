# JavaScript Node 工具 node nvm(node版本管理器)

```
nvm 是 Node.js 的版本管理器 (version manager)，可在同一台主機上安裝多個版本的 Node.js 環境，因為不同專案可能會使用不同的 Node.js 版本，那就需要透過一個版本管理器來切換不同的 Node.js 版本。
```

## 目錄

- [JavaScript Node 工具 node nvm(node版本管理器)](#javascript-node-工具-node-nvmnode版本管理器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [nvm相關 (版本管理器)](#nvm相關-版本管理器)
    - [node相關](#node相關)
- [安裝](#安裝)
  - [Linux](#linux)
  - [MacOS](#macos)
- [指令](#指令)
  - [node 指令](#node-指令)
- [特殊狀況](#特殊狀況)
  - [MacOS M1](#macos-m1)

## 參考資料

### nvm相關 (版本管理器)

[nvm github](https://github.com/nvm-sh/nvm)

[NVM 安裝與使用 Node.js](https://pjchender.dev/nodejs/nvm/)

### node相關

[官方網站](https://nodejs.org/en/)

[官方API文檔](https://nodejs.org/api/)

[node cli 指令列表](https://nodejs.org/api/cli.html)

# 安裝

## Linux

```bash
# 安裝nvm
# 將內容加入環境變數文檔
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# 安裝 NVM
bash /root/.nvm/install.sh

# 為了讓 NVM 生效，需要重新整理終端機的設定。
# 執行以下其中一個指令，或是重新啟動終端機應用程式
source ~/.bashrc
source ~/.zshrc

# 檢測是否安裝成功
nvm --version

# 使用 NVM 安裝 Node.js。輸入以下指令以安裝最新的穩定版本 node
nvm install stable

# 在終端機中輸入以下指令，應該會顯示安裝的 Node.js 版本號
node --version
```

## MacOS

```bash
# 安裝nvm
# MacOS 可透過 homebrew安裝:
# 安裝 NVM（Node Version Manager）
brew install nvm
```

# 指令

```bash
# 安裝某個版本的 node
nvm install $version
nvm install 16
#
# https://nodejs.org/en/about/releases/

# 解除安裝指定版本
nvm uninstall $version

# 使用某個版本的 node
nvm use $version

# 列出本機已安裝的版本
nvm ls

# 列出所有可安裝版本
nvm ls-remote

# 察看別名對應node版本
nvm alias

# 設定別名
nvm alias $name $version

# 設定預設開啟的 node 版本
nvm alias default $version

# 查看 nvm 版本
nvm --version

# 察看 Node.js 的安裝路徑
nvm which $version

# 指定要執行的 Node.js 版本
nvm exec $version node

# 察看目前使用版本
nvm current

# 查看某一版本 nvm 的 PATH
nvm which 8.11.1

# MacOS
# 透過 Homebrew 移除Node.js
brew uninstall node
```

## node 指令

```bash
# 進入nodejs 工作階段
node

# 執行javascript
node $javascript_file

# 查看版本
node -v
```

# 特殊狀況

## MacOS M1

```bash
# 安裝14以下的版本，先進入Rosetta shell環境
# https://dev.to/httpjunkie/setup-node-version-manager-nvm-on-mac-m1-7kl
arch -x86_64 zsh
```
