# PHP 工具 anacodna(虛擬環境)

```
Conda 主要用於管理 Python 和 R 的環境及依賴，但它也能安裝一些其他語言的軟件包。
```

## 目錄

- [Python 工具 anacodna(虛擬環境) 筆記](# python-工具-anacodna虛擬環境-筆記)
	- [目錄](# 目錄)
	- [參考資料](# 參考資料)
- [安裝步驟 Anaconda](# 安裝步驟-anaconda)
- [安裝步驟 CentOS7 Miniconda](# 安裝步驟-centos7-miniconda)
- [指令](# 指令)

## 參考資料

[Anaconda 官方網站](https://www.anaconda.com/)

[Anaconda 文檔](https://docs.anaconda.com/)

[Command reference - conda 命令參考](https://docs.conda.io/projects/conda/en/latest/commands.html)

[conda/conda Github](https://github.com/conda/conda)

### Miniconda相關

[Miniconda 文檔](https://docs.conda.io/en/latest/miniconda.html)

[Installing on Linux](https://docs.conda.io/projects/conda/en/latest/user-guide/install/linux.html)

# 安裝

```bash
# 下載到本地 https://www.anaconda.com/products/distribution 取得下載網址
wget 下載網址

# 進行Anaconda的安裝
bash Anaconda3-5.3.1-Linux-x86_64.sh

source ~/.bashrc
```

## CentOS7 Miniconda(輕量化)

```bash
# 下載 Miniconda 安裝腳本
wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh

# 執行安裝腳本
bash Miniconda3-latest-Linux-x86_64.sh
source ~/.bashrc
```

添加 Conda Forge 為 Conda 的包源之一

```bash
conda config --add channels conda-forge
conda config --set channel_priority strict
```

```bash
conda search php
```

如果搜索結果中有 PHP 包，可以使用以下命令安裝

```bash
conda install php
```

## 卸載

```bash
# 卸載 Anaconda 或 Miniconda
# 使用以下命令刪除整個 Miniconda 安裝目錄：
rm -rf ~/miniconda

# 可選：編輯~/.bash_profile以從 PATH 環境變量中刪除 Miniconda 目錄。
# 可選：刪除可能已在主目錄中創建的以下隱藏文件和文件夾：
# .condarc文件
# .conda目錄
# .continuum目錄
rm -rf ~/.condarc ~/.conda ~/.continuum
```

# 指令

```bash
```
