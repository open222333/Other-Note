# RainLoop(Web 郵件客戶端)

```
RainLoop 是一個現代、輕量級的 Web 郵件客戶端，支持多語言，包括中文。它易於配置，並提供直觀的用戶界面
```

## 目錄

- [RainLoop(Web 郵件客戶端)](#rainloopweb-郵件客戶端)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)

## 參考資料

[官方網站](https://www.rainloop.net/)

[官方文檔](https://www.rainloop.net/docs/)

# 安裝

## docker-compose 部署

```yml
version: '3'
services:
  rainloop:
    image: hardware/rainloop
    container_name: rainloop
    ports:
      - "8888:80"
    volumes:
      - ./data:/rainloop/data
    restart: always
```

訪問 RainLoop 界面
在瀏覽器中打開 http://<your-server-ip>:8888，將會看到 RainLoop 的登錄界面。

初始設置
點擊界面底部的 "Admin Panel" 進入管理面板。
使用默認的管理員帳戶登錄（默認帳號和密碼都是 admin）。

配置郵件伺服器
在管理面板中，添加郵件伺服器配置，例如 IMAP 和 SMTP 服務器地址。
可以配置多個域名和郵件伺服器。

設置中文界面
在管理面板中，導航到 Interface 設置，選擇 Language，然後選擇 Chinese (Traditional)。

Nginx 配置文檔

```conf
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

    location / {
        proxy_pass http://localhost:8888;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```
