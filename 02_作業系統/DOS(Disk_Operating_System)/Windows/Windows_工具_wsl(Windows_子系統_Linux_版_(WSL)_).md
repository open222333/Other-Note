# Windows 工具 wsl(Windows 子系統 Linux 版 (WSL) )

```
Windows 子系統 Linux 版 (WSL) 可讓開發人員執行 GNU/Linux 環境，包括大部分命令列工具、公用程式和應用程式 ，直接在Windows、未修改，而不需要傳統虛擬機器或雙開機設定的額外負荷。
```

## 目錄

- [Windows 工具 wsl(Windows 子系統 Linux 版 (WSL) )](#windows-工具-wslwindows-子系統-linux-版-wsl-)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [](#)
- [安裝](#安裝)
	- [在 Windows Terminal 使用 Bash.exe](#在-windows-terminal-使用-bashexe)
- [指令](#指令)

## 參考資料

[Microsoft Store - Windows Terminal(Windows 終端機)](https://apps.microsoft.com/detail/9N0DX20HK701?rtc=1&hl=zh-tw&gl=TW)

[Microsoft Store - Ubuntu](https://apps.microsoft.com/detail/9PDXGNCFSCZV?hl=zh-tw&gl=TW)

[Microsoft Store - Windows Subsystem for Linux](https://apps.microsoft.com/detail/9P9TQF7MRM4R?hl=zh-tw&gl=TW)

[適用於 Linux 的 Windows 子系統文件](https://learn.microsoft.com/zh-tw/windows/wsl/)

### bash 相關

[忘記 WSL 執行個體密碼時，如何重置？](https://www.weithenn.org/2023/02/reset-password-for-wsl-linux.html)

[[Windows] 在 Windows Terminal 新增 Linux Bash Commnadline](https://marcus116.blogspot.com/2019/07/how-to-add-linux-bash-windows-terminal.html)

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

## 在 Windows Terminal 使用 Bash.exe

```PowerShell
# 使用管理者 admin 權限開啟 powershell
# 啟用 Linux Bash Shell (重開機)
Enable-WindowsOptionalFeature -Online -FeatureName Microsoft-Windows-Subsystem-Linux
# 啟用 Hyper-V (重開機)
Enable-WindowsOptionalFeature -Online -FeatureName Microsoft-Hyper-V -All

# 到 Windows 商店下載 Linux 子系統
# 在 Microsoft Store 提供多個 Linux 子系統像是 Ubuntu、OpenSUSE、SLES 可以下載

# 新增 Command Line
# 在 Windows Terminal 中的設定可以透過  ⬇  中的 settings 來進行設定

# 在 Windows Terminal 設定檔名為 Profiles.json，要調整可以到下列路徑進行調整
# userName\AppData\Local\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\RoamingState

# 重要的項目為 name、guid、commandline 等設定，說明如下
# guid : 每組 commandline 定義屬於自己專用且不能重複的 guid
# name : 顯示在 Windows Terminal  +  中顯示的名字
# commandline  : 執行的 commandline
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

## 登入 Linux 執行個體 root

「多個」Linux 執行個體時，如何指定要變更哪個 Linux 執行個體的管理密碼

```PowerShell
wsl -u root

wsl -d Ubuntu -u root
```