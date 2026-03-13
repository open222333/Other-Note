# Windows 工具 OpenSSH(連接工具)

```
OpenSSH 是使用 SSH 通訊協定進行遠端登入的連接工具。
它會加密用戶端與伺服器之間的所有流量，以消除竊聽、連接劫持和其他攻擊。
```

## 目錄

- [Windows 工具 OpenSSH(連接工具)](#windows-工具-openssh連接工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [卸載](#卸載)
- [指令](#指令)

## 參考資料

[安裝 OpenSSH](https://learn.microsoft.com/zh-tw/windows-server/administration/openssh/openssh_install_firstuse)

[適用于 Windows 10 1809 和 Windows Server 2019 的 OpenSSH 伺服器設定](https://learn.microsoft.com/zh-tw/windows-server/administration/openssh/openssh_server_configuration)

[OpenSSH 金鑰管理](https://docs.microsoft.com/zh-tw/windows-server/administration/openssh/openssh_keymanagement)

# 安裝

```PowerShell
# 以系統管理員身分執行 PowerShell
# 確認 OpenSSH 可供使用
Get-WindowsCapability -Online | Where-Object Name -like 'OpenSSH*'

# 若尚未安裝，傳回下列輸出：
# Name  : OpenSSH.Client~~~~0.0.1.0
# State : NotPresent

# Name  : OpenSSH.Server~~~~0.0.1.0
# State : NotPresent


# 安裝伺服器或用戶端元件
# Install the OpenSSH Client
Add-WindowsCapability -Online -Name OpenSSH.Client~~~~0.0.1.0

# Install the OpenSSH Server
Add-WindowsCapability -Online -Name OpenSSH.Server~~~~0.0.1.0
```

# 卸載

```PowerShell
# Uninstall the OpenSSH Client
Remove-WindowsCapability -Online -Name OpenSSH.Client~~~~0.0.1.0

# Uninstall the OpenSSH Server
Remove-WindowsCapability -Online -Name OpenSSH.Server~~~~0.0.1.0
```

# 指令

```PowerShell
# 啟動並設定 OpenSSH 伺服器以進行初始使用，請以系統管理員身分開啟 PowerShell，然後執行下列命令以啟動 sshd service
# Start the sshd service
Start-Service sshd

# OPTIONAL but recommended:
Set-Service -Name sshd -StartupType 'Automatic'

# Confirm the Firewall rule is configured. It should be created automatically by setup. Run the following to verify
if (!(Get-NetFirewallRule -Name "OpenSSH-Server-In-TCP" -ErrorAction SilentlyContinue | Select-Object Name, Enabled)) {
    Write-Output "Firewall Rule 'OpenSSH-Server-In-TCP' does not exist, creating it..."
    New-NetFirewallRule -Name 'OpenSSH-Server-In-TCP' -DisplayName 'OpenSSH Server (sshd)' -Enabled True -Direction Inbound -Protocol TCP -Action Allow -LocalPort 22
} else {
    Write-Output "Firewall rule 'OpenSSH-Server-In-TCP' has been created and exists."
}


# 連線至 OpenSSH 伺服器
ssh username@servername
```