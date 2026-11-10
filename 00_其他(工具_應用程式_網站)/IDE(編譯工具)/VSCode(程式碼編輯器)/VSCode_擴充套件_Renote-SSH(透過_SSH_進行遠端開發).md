# VSCode 擴充套件 Renote-SSH(透過 SSH 進行遠端開發)

```
VSCode 擴充套件 Remote-SSH：透過 SSH 在遠端主機開發，含金鑰建立與連線設定。
```

## 目錄

- [VSCode 擴充套件 Renote-SSH(透過 SSH 進行遠端開發)](#vscode-擴充套件-renote-ssh透過-ssh-進行遠端開發)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [ssh相關](#ssh相關)
    - [心得相關](#心得相關)
- [用法](#用法)
  - [建立 ssh](#建立-ssh)

## 參考資料

[Remote - SSH](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-ssh)

### ssh相關

[Linux 工具 OpenSSL OpenSSH(連線、密鑰).md](../../../02_作業系統/Unix/Linux/Linux_工具/Linux_SSH_工具/Linux_工具_OpenSSL_OpenSSH(連線、密鑰).md)

### 心得相關

[使用VSCode Remote透過 SSH 進行遠端開發](https://hackmd.io/@brick9450/vscode-remote)

# 用法

## 建立 ssh

創建金鑰

```bash
ssh-keygen -f ~/.ssh/rsa
```

將公鑰複製到遠端主機

```bash
ssh-copy-id -i ~/.ssh/rsa.pub root@hostname
```

編輯檔案 .ssh/config

```conf
Host hostname
    User root
    HostName xxx.xxx.xxx.xxx
    Port 22
    ServerAliveInterval 60
    IdentityFile ~/.ssh/rsa
```
