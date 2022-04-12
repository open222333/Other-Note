# Homebrew 筆記

## 參考資料

[官網](https://brew.sh/)

[官方文檔](https://docs.brew.sh/)

[brew指令列表](https://docs.brew.sh/Manpage)

```bash
# brew指令列表
man brew`
```

## 安裝Homebrew
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

使用 homebrew 安裝 anaconda
```bash
brew install --cask anaconda
```

## 添加環境變數 

狀況 排除 brew: command not found

第一種
```bash
vi ~/.bash_profile

# 添加一行
export PATH="/opt/homebrew/bin/:$PATH"

# 讓.bash_profile配置的全域性變數理解生效
source ~/.bash_profile
```

第二種
```bash
# Intel晶片 /usr/local

echo 'export PATH="/usr/local/opt/{名稱}/bin:$PATH"' >> $HOME/.bash_profile
echo 'export PATH="/usr/local/opt/{名稱}/sbin:$PATH"' >> $HOME/.bash_profile

# APPLE晶片 /opt/homebrew

echo 'export PATH="/opt/homebrew/opt/{名稱}/bin:$PATH"' >> $HOME/.bash_profile
echo 'export PATH="/opt/homebrew/opt/{名稱}/sbin:$PATH"' >> $HOME/.bash_profile
```

---
## 指令

`M1晶片 與 Intel CPU 不同安裝位置`

```bash
# 升級Homebrew
brew update

# 查看版本
brew --version

# 查看設定
brew config

# 查看所有安裝
brew list

# 查找
brew search 名稱

# 升級
brew upgrade 名稱

# 安裝
brew install 名稱

# 卸載
brew uninstall 名稱

# 查看資訊
brew info 名稱

# 查看安裝位置
brew list 名稱

# 查看所有安裝軟體
brew list

# 開啟服務
brew services start 服務

# 停止服務
brew services stop  服務

# 重啟服務
brew services restart 服務

# 啟動服務
brew services run 服務

# 列出被管理的服務
brew services list
```