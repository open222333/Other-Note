# Windows 工具 wsl(Windows 子系統 Linux 版 (WSL) )

```
Windows 子系統 Linux 版 (WSL) 可讓開發人員執行 GNU/Linux 環境，包括大部分命令列工具、公用程式和應用程式 ，直接在Windows、未修改，而不需要傳統虛擬機器或雙開機設定的額外負荷。
```

## 目錄

- [Windows 工具 wsl(Windows 子系統 Linux 版 (WSL) )](#windows-工具-wslwindows-子系統-linux-版-wsl-)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[適用於 Linux 的 Windows 子系統文件](https://learn.microsoft.com/zh-tw/windows/wsl/)

# 安裝

```PowerShell
# 列出可用的 Linux 發行版本
wsl --list --online

# 安裝 WSL 和 Linux 的預設 Ubuntu 發行版本。
wsl --install

	# --distribution：指定要安裝的 Linux 發行版本。執行 wsl --list --online 來尋找可用的散發套件。
	# --no-launch：安裝 Linux 發行版本，但不會自動啟動它。
	# --web-download：從線上來源安裝，而不是使用 Microsoft Store。

	# 未安裝 WSL 選項時包括：

	# --inbox：使用 Windows 元件安裝 WSL，而不是使用Microsoft市集。(WSL 更新會透過 Windows 更新接收，而不是透過市集) 依可用方式推送。
	# --enable-wsl1：在安裝 Microsoft Store 版本的 WSL 期間啟用 WSL 1，同時啟用 「Windows 子系統 Linux 版」 選擇性元件。
	# --no-distribution：安裝 WSL 時請勿安裝散發套件。
```

# 指令

```PowerShell
# 列出可用的 Linux 發行版本
wsl --list --online

# 列出已安裝的 Linux 發行版本
wsl --list --verbose

# 設定預設 Linux 發行版本
wsl --set-default <Distribution Name>

# 將目錄變更為首頁
wsl ~

# 從 PowerShell 或 CMD 執行特定的 Linux 發行版本
wsl --distribution <Distribution Name> --user <User Name>

# 更新 WSL
wsl --update

# 檢查 WSL 狀態
wsl --status

# 檢查 WSL 版本
wsl --version

# 說明命令
wsl --help

# 以特定使用者身分執行
wsl -u <Username>`, `wsl --user <Username>

# 變更散發套件的預設使用者
<DistributionName> config --default-user <Username>
```
