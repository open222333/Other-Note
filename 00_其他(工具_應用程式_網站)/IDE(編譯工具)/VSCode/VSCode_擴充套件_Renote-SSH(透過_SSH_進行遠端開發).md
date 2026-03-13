# VSCode 擴充套件 Renote-SSH(透過 SSH 進行遠端開發)

```
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

[Linux 工具 OpenSSL OpenSSH(連線、密鑰).md](https://github.com/open222333/Other-Note/blob/main/02_%E4%BD%9C%E6%A5%AD%E7%B3%BB%E7%B5%B1/Unix/Linux/Linux%20%E5%B7%A5%E5%85%B7/Linux%20%E5%8A%A0%E5%AF%86/Linux%20%E5%B7%A5%E5%85%B7%20OpenSSL%20OpenSSH(%E9%80%A3%E7%B7%9A%E3%80%81%E5%AF%86%E9%91%B0).md)

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
