# Linux 工具 vim(文字編輯器)

```
Vim是從vi發展出來的一個文字編輯器。
其代碼補完、編譯及錯誤跳轉等方便編程的功能特別豐富，在程式設計師中被廣泛使用。
和Emacs並列成為類Unix系統使用者最喜歡的編輯器。
```

## 目錄

- [Linux 工具 vim(文字編輯器)](#linux-工具-vim文字編輯器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [設定檔](#設定檔)
- [指令](#指令)
	- [Vim 如果直接接上代碼，出現格式跑掉（自動縮排）可透過以下方式解決](#vim-如果直接接上代碼出現格式跑掉自動縮排可透過以下方式解決)
	- [vim 出現亂碼](#vim-出現亂碼)
	- [tab 長度](#tab-長度)
	- [搜尋](#搜尋)
	- [取代](#取代)

## 參考資料

[官方網站](https://www.vim.org/)

[官網文檔 (書籍)](https://vimdoc.sourceforge.net/)

[set 官網文檔](https://vimdoc.sourceforge.net/htmldoc/options.html#options)

# 安裝

```bash
# 在 Ubuntu 或 Debian 上：
apt-get update && apt-get install vim -y

# 在 CentOS 或 Red Hat 上：
yum install vim -y

# 在 Fedora 上：
dnf install vim -y

# 在 openSUSE 上：
zypper install vim -y
```

# 設定檔

通常是 `~/.vimrc` 或 `~/.vim/vimrc`

`顯示行數`

```bash
echo "set number" >> ~/.vimrc
```

`vim 編輯時 進入命令模式（按 Esc） 輸入指令`

```bash
:set number
```

# 指令

`顯示行數`

```bash
:set number
```

```bash
echo "set number" >> ~/.vimrc
```

```bash
:set nu
```

```bash
echo "set nu" >> ~/.vimrc
```

## Vim 如果直接接上代碼，出現格式跑掉（自動縮排）可透過以下方式解決

`關閉縮排`

```bash
:set paste
```

```bash
echo "set paste" >> ~/.vimrc
```

`開啟縮排`

```bash
:set nopaste
```

```bash
echo "set nopaste" >> ~/.vimrc
```

## vim 出現亂碼

`檢查終端的編碼`

```bash
echo $LANG
```

`設置顯示編碼 在 ~/.vimrc 或 ~/.vim/vimrc 中添加以下設置`

```bash
echo "set encoding=utf-8" >> ~/.vimrc
echo "set fileencodings=utf-8" >> ~/.vimrc
```

```bash
echo "set encoding=utf8" >> ~/.vimrc
```

## tab 長度

`設置 tab 長度`

```bash
echo "set tabstop=4" >> ~/.vimrc
```

## 搜尋

```
/pattern      向下搜尋
?pattern      向上搜尋
n             跳到下一個符合
N             跳到上一個符合
*             搜尋游標所在的單字（向下）
#             搜尋游標所在的單字（向上）
```

`搜尋後高亮顯示`

```bash
:set hlsearch

# 關閉高亮
:nohlsearch
:noh
```

`大小寫`

```bash
# 忽略大小寫
:set ignorecase

# 搜尋含大寫時自動區分大小寫
:set smartcase
```

## 取代

```bash
# 取代當前行第一個符合
:s/old/new

# 取代當前行所有符合
:s/old/new/g

# 取代全文所有符合
:%s/old/new/g

# 取代前逐一確認（y=取代 n=跳過 a=全部 q=結束）
:%s/old/new/gc

# 取代指定行範圍（第 5 到 10 行）
:5,10s/old/new/g

# 忽略大小寫取代
:%s/old/new/gi
```
