# Python 工具 anacodna(虛擬環境) 筆記

```
```

## 目錄

- [Python 工具 anacodna(虛擬環境) 筆記](#python-工具-anacodna虛擬環境-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [安裝步驟](#安裝步驟)
- [指令](#指令)

## 參考資料

[Anaconda 文檔](https://docs.anaconda.com/)

## 安裝步驟

```bash
# 下載到本地
wget https://repo.anaconda.com/archive/Anaconda3-2020.02-Linux-x86_64.sh

# 進行Anaconda的安裝
bash Anaconda3-5.3.1-Linux-x86_64.sh

source ~/.bashrc
```

# 指令

```bash
#
source /usr/local/anaconda3/bin/activate

#檢查目前版本
conda –V

#進行更新
conda update conda

#查詢目前已啟動的虛擬環境
conda info -envis
conda info -e

#查看目前系統已經安裝幾個虛擬環境
conda env list

#建立虛擬環境，例如：虛擬環境myenv，python 3.5
conda create --name myenv python=3.5

#建立備份檔案.yaml
conda env export

# 匯入/匯出環境
conda env export > environment.yaml
conda env create -f environment.yaml

#Windows，啟動虛擬環境
activate myenv

#LINUX或macOS，啟動虛擬環境
conda activate myenv

#查看目前此虛擬環境中已經先安裝了那些東西
conda list

#在此虛擬環境下安裝所需套件，例如：numpy
conda install numpy

#Windows，關閉虛擬環境
deactivate

#macOS或LINUX，關閉虛擬環境
source deactivate

#刪除虛擬環境中某個package，例如：虛擬環境myenv中的numpy
conda remove --name myenv numpy
```