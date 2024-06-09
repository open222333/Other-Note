# Docker Mailserver(後端郵件伺服器)

```
Docker Mailserver 是一個易於部署且可配置的完整郵件伺服器套件。

Docker Mailserver 本身是一個後端郵件伺服器，主要通過配置文件和命令行進行管理，並沒有特定的圖形用戶界面 (GUI)，因此也沒有區分語言介面。
```

## 目錄

- [Docker Mailserver(後端郵件伺服器)](#docker-mailserver後端郵件伺服器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)
  - [docker-compose 啟動](#docker-compose-啟動)

## 參考資料

[docker-mailserver Github](https://github.com/docker-mailserver/docker-mailserver)

[docker-mailserver 文檔](https://docker-mailserver.github.io/docker-mailserver/latest/)

# 安裝

## RedHat (CentOS)

確保已安裝 Docker 和 Docker Compose

下載 Docker Mailserver

```bash
git clone https://github.com/docker-mailserver/docker-mailserver.git
cd docker-mailserver
```

配置 env

```bash
cp .env.sample .env
```

創建配置目錄

```bash
mkdir -p config/{ssl,postfix,opendkim,opendmarc,postscreen}
```

docker-compose.yml

```yml
version: '3.8'

services:
  mailserver:
    image: docker.io/mailserver/docker-mailserver:latest
    hostname: mail
    domainname: example.com
    container_name: mailserver
    ports:
      - "25:25"
      - "143:143"
      - "587:587"
      - "993:993"
    volumes:
      - ./config:/tmp/docker-mailserver
    env_file: .env
    cap_add:
      - NET_ADMIN
      - SYS_PTRACE
    restart: always
```

# 指令

## docker-compose 啟動

```bash
docker-compose up -d mailserver
```

創建郵件用戶

```bash
docker exec -it mailserver setup email add user@example.com
```

設置密碼

```bash
docker exec -it mailserver setup email passwd user@example.com
```