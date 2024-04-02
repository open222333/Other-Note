# Homebrew 筆記

```
```

## 目錄

- [Homebrew 筆記](#homebrew-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝步驟](#安裝步驟)
- [環境變數](#環境變數)
- [指令](#指令)
- [用法](#用法)
  - [Homebrew 的 formula 定義 用於安裝名為 sshpass 的軟體包](#homebrew-的-formula-定義-用於安裝名為-sshpass-的軟體包)

## 參考資料

[官網](https://brew.sh/)

[官方文檔](https://docs.brew.sh/)

[brew指令列表](https://docs.brew.sh/Manpage)

```bash
# brew指令列表
man brew`
```

# 安裝步驟

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

使用 homebrew 安裝 anaconda
```bash
brew install --cask anaconda
```

# 環境變數

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

# 指令

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

# 用法

## Homebrew 的 formula 定義 用於安裝名為 sshpass 的軟體包

```ruby
=begin
require 'formula'：引入 Homebrew 的 formula 庫，這是撰寫 Homebrew formula 必需的。

class Sshpass < Formula：定義一個名為 Sshpass 的 formula，表示我們要安裝的軟體包叫做 sshpass。

url 'http://sourceforge.net/projects/sshpass/files/sshpass/1.06/sshpass-1.06.tar.gz'：指定要下載的軟體包的 URL。

homepage 'http://sourceforge.net/projects/sshpass'：指定軟體包的官方網站 URL。

sha256 'sha256_content'：指定軟體包的 SHA256 校驗碼，用於驗證下載的軟體包是否完整且未被篡改，sha256_content 這裡應該是實際的 SHA256 校驗碼。

def install：定義安裝過程，包括下載、解壓、配置、編譯和安裝。

system "./configure", "--disable-debug", "--disable-dependency-tracking", "--prefix=#{prefix}"：執行 configure 腳本，設置編譯選項，其中 --prefix=#{prefix} 指定安裝路徑為 Homebrew 的安裝路徑。

system "make install"：執行 make install 命令，進行編譯並安裝到指定路徑。

def test：定義測試過程，通常用於確保安裝正常並可以正確運行。

system "sshpass"：執行 sshpass 命令，用於測試安裝的 sshpass 是否可以正確執行。

需要將 sha256_content 替換為實際的 SHA256 校驗碼，然後將這段程式碼保存為一個 .rb 文件，放置在 Homebrew 的 formula 存儲庫中，以便可以使用 brew install sshpass 命令來安裝 sshpass 軟體包。
=end

require 'formula'

class Sshpass < Formula
  url 'http://sourceforge.net/projects/sshpass/files/sshpass/1.06/sshpass-1.06.tar.gz'
  homepage 'http://sourceforge.net/projects/sshpass'
  sha256 'sha256_content'

  def install
    system "./configure", "--disable-debug", "--disable-dependency-tracking",
                          "--prefix=#{prefix}"
    system "make install"
  end

  def test
    system "sshpass"
  end
end
```