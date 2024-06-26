# Python 工具 anacodna(虛擬環境) 筆記

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

### 例外狀況

[解決 CommandNotFoundError：正確配置 Shell 以使用“conda activate”](https://saturncloud.io/blog/solving-the-commandnotfounderror-properly-configuring-your-shell-to-use-conda-activate/)

# 安裝

```bash
# 下載到本地 https://www.anaconda.com/products/distribution 取得下載網址
wget 下載網址

# 進行Anaconda的安裝
bash Anaconda3-5.3.1-Linux-x86_64.sh

source ~/.bashrc
```

## Windows PowerShell 設定 環境變數路徑

```PowerShell
# 開啟檔案總管
# 對 本機 按右鍵 > 內容 > 進階系統設定 > 進階 > 環境變數 >點擊系統變數的 Path > 編輯 > 新增 >
# C:\Path\to\Anaconda3
# C:\Path\to\Anaconda3\Scripts
# C:\Path\to\Anaconda3\Library\bin

# https://learn.microsoft.com/zh-tw/powershell/module/microsoft.powershell.core/about/about_profiles?view=powershell-7.3#the-profile-variable
# 只能在當前 PowerShell 生效
$env:PATH += ";C:\Path\to\Anaconda3"
$env:PATH += ";C:\Path\to\Anaconda3\Scripts"
$env:PATH += ";C:\Path\to\Anaconda3\Library\bin"

# 建立 PowerShell 設定檔
if (!(Test-Path -Path <profile-name>)) {
  New-Item -ItemType File -Path <profile-name> -Force
}

# 在目前的 PowerShell 主應用程式中建立目前使用者的設定檔
if (!(Test-Path -Path $PROFILE)) {
  New-Item -ItemType File -Path $PROFILE -Force
}

# 開啟目前 PowerShell 主應用程式中目前使用者的設定檔
notepad $PROFILE
```

## CentOS7 Miniconda(輕量化)

```bash
# https://docs.conda.io/en/latest/miniconda.html 取得下載網址
wget 下載網址

# 進行安裝
bash Miniconda3-latest-Linux-x86_64.sh

source ~/.bashrc
```

```bash
# 下載 Miniconda 安裝腳本
wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh

# 執行安裝腳本
bash Miniconda3-latest-Linux-x86_64.sh
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
# Windows，啟動虛擬環境
activate $ENV_NAME

# LINUX或macOS，啟動虛擬環境
source /usr/local/anaconda3/bin/activate
conda activate $ENV_NAME

# Windows，關閉虛擬環境
deactivate

# macOS或LINUX，關閉虛擬環境
source deactivate

# 檢查目前版本
conda --version

# 進行更新
conda update conda

# 在此虛擬環境安裝 套件
conda install $PACKAGE_NAME

# 使用檔案 安裝 套件
conda install --yes --file requirements.txt

# 顯示有​​關當前 conda 安裝的信息。
conda info
	# -a, --all
	# 顯示所有信息。
	# --base
	# 顯示基礎環境路徑。
	# -e, --envs
	# 列出所有已知的 conda 環境。
	# -s, --system
	# 列出環境變量。
	# --unsafe-channels
	# 顯示公開令牌的頻道列表。
	# --json
	# 將所有輸出報告為 json。適合以編程方式使用 conda。
	# -v, --verbose
	# 一次用於信息，兩次用於調試，三次用於跟踪。
	# -q, --quiet
	# 不顯示進度條。
# 顯示目前所有環境
conda info --envs

# 列出可用於安裝的 Python 版本
conda search python
conda search --full-name python

# 建立虛擬環境
conda create
	# --clone
	# 創建一個新環境作為現有本地環境的副本。
	# --file
	# 從給定文件中讀取包版本。可以傳遞重複的文件規範（例如 --file=file1 --file=file2）。
	# --dev
	# 在包裝器腳本中使用sys.executable -m conda而不是 CONDA_EXE。這主要用於在我們針對舊 Python 版本測試新 conda 源的測試期間使用。
	# -n, --name
	# 環境名稱。
	# -p, --prefix
	# 環境位置的完整路徑（即前綴）。
	# -d, --dry-run
	# 僅顯示將要完成的操作。
	# --json
	# 將所有輸出報告為 json。適合以編程方式使用 conda。
	# -q, --quiet
	# 不顯示進度條。
	# -v, --verbose
	# 可以多次使用。一次用於 INFO，兩次用於 DEBUG，三次用於 TRACE。
	# -y, --yes
	# 自動將任何確認值設置為“是”。不會要求用戶確認任何添加、刪除、備份等。
	# --download-only
	# 解決環境並確保填充包緩存，但在取消鏈接並將包鏈接到前綴之前退出。
	# --show-channel-urls
	# 顯示頻道網址。覆蓋conda config --show show_channel_urls給出的值。
# 建立虛擬環境
conda create --name $ENV_NAME python=3.5

# 從指定的 conda 環境中刪除包列表。使用--all標誌刪除所有包和環境本身。
conda remove
	# -n, --name
	# 環境名稱。
	# -p, --prefix
	# 環境位置的完整路徑（即前綴）。
	# --all
	# 刪除所有包，即整個環境。
	# --features
	# 刪除功能（而不是包）。
	# --force-remove, --force
	# 強制刪除包而不刪除依賴它的包。使用此選項通常會使您的環境處於損壞和不一致的狀態。
	# --no-pin
	# 忽略適用於當前操作的固定包。這些固定包可能來自 .condarc 文件或 <TARGET_ENVIRONMENT>/conda-meta/pinned 中的文件。
	# --solver
	# 可能的選擇：經典 選擇要使用的求解器後端。
	# -C, --use-index-cache
	# 使用頻道索引文件的緩存，即使它已經過期。如果您不想讓 conda 檢查是否存在新版本的 repodata 文件，這將很有用，這將節省帶寬。
	# -k, --insecure
	# 允許 conda 執行“不安全”的 SSL 連接和傳輸。相當於將“ssl_verify”設置為“false”。
	# --offline
	# 離線模式。不要連接到互聯網。
	# -d, --dry-run
	# 僅顯示將要完成的操作。
	# --json
	# 將所有輸出報告為 json。適合以編程方式使用 conda。
	# -q, --quiet
	# 不顯示進度條。
	# -v, --verbose
	# 可以多次使用。一次用於 INFO，兩次用於 DEBUG，三次用於 TRACE。
	# -y, --yes
	# 自動將任何確認值設置為“是”。不會要求用戶確認任何添加、刪除、備份等。
	# conda remove -n $ENV_NAME --all

# 從當前活動的環境中刪除包 $PACKAGE_NAME
conda remove $PACKAGE_NAME
# 從環境“$ENV_NAME”中刪除包列表
conda remove -n $ENV_NAME $PACKAGE_NAME1 $PACKAGE_NAME2 $PACKAGE_NAME3
# 從環境$ENV_NAME和環境本身中刪除所有包
conda remove -n $ENV_NAME --all

# 列出 conda 環境中已安裝的包
conda list
	# --show-channel-urls
	# 顯示頻道網址。覆蓋conda config --show show_channel_urls給出的值。
	# -c, --canonical
	# 僅輸出包的規範名稱。
	# -f, --full-name
	# 只搜索全名，即 ^<regex>$。--full-name NAME 與正則表達式“^NAME$”相同。
	# --explicit
	# 使用 URL 明確列出所有已安裝的 conda 包（輸出可能由 conda create --file 使用）。
	# --md5
	# 使用 --explicit 時添加 MD5 哈希值。
	# -e, --export
	# 輸出明確的、機器可讀的需求字符串，而不是人類可讀的包列表。conda create --file 可以使用此輸出。
	# -r, --revisions
	# 列出修訂歷史。
	# --no-pip
	# 不要包括 pip-only 安裝的包。
	# -n, --name
	# 環境名稱。
	# -p, --prefix
	# 環境位置的完整路徑（即前綴）。
	# --json
	# 將所有輸出報告為 json。適合以編程方式使用 conda。
	# -v, --verbose
	# 一次用於信息，兩次用於調試，三次用於跟踪。
	# -q, --quiet
	# 不顯示進度條。

# 列出安裝到環境“$ENV_NAME”中的所有包
conda list -n $ENV_NAME
# 使用正則表達式列出所有以字母“py”開頭的包
conda list ^py
# 保存包以備將來使用
conda list --export > package-list.txt
# 從導出文件重新安裝包
conda create -n $ENV_NAME --file package-list.txt

# 如果requirements.txt中的包不可用，則會拋出“無包錯誤”
while read requirement; do conda install --yes $requirement; done < requirements.txt

# 在conda命令無效時使用pip命令來代替
while read requirement; do conda install --yes $requirement || pip install $requirement; done < requirements.txt
```

# 例外狀況

## CommandNotFoundError: Properly Configuring Your Shell to Use 'conda activate'

確認使用的 shell

```
echo $SHELL
```

編輯 Shell 的啟動文件

For bash, edit ~/.bashrc or ~/.bash_profile
For zsh, edit ~/.zshrc
For fish, edit ~/.config/fish/config.fish

將 /path/to/anaconda3 替換為 Anaconda 安裝的實際路徑。

如果不確定路徑，可以透過在終端機中輸入 which conda 來找到它

```bash
# >>> conda initialize >>>
# !! Contents within this block are managed by 'conda init' !!
__conda_setup="$('/path/to/anaconda3/bin/conda' 'shell.bash' 'hook' 2> /dev/null)"
if [ $? -eq 0 ]; then
    eval "$__conda_setup"
else
    if [ -f "/path/to/anaconda3/etc/profile.d/conda.sh" ]; then
        . "/path/to/anaconda3/etc/profile.d/conda.sh"
    else
        export PATH="/path/to/anaconda3/bin:$PATH"
    fi
fi
unset __conda_setup
# <<< conda initialize <<<
```

Shell 的啟動文件

For bash or zsh, type:

```bash
source ~/.bashrc  # or source ~/.zshrc for zsh
```

For fish, type:

```bash
source ~/.config/fish/config.fish
```
