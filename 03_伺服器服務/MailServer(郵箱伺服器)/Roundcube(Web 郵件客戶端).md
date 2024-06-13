# Roundcube(Web 郵件客戶端)

```
Roundcube 是一個功能豐富的 Web 郵件客戶端，支持多種語言，包括中文。

它提供現代的用戶界面，支持多帳戶管理和擴展功能。
```

## 目錄

- [Roundcube(Web 郵件客戶端)](#roundcubeweb-郵件客戶端)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
- [高級配置](#高級配置)
  - [插件和擴展](#插件和擴展)

## 參考資料

[官方網站](https://roundcube.net/)

[官方文檔 Github Wiki](https://github.com/roundcube/roundcubemail/wiki)

[dockerhub](https://hub.docker.com/r/roundcube/roundcubemail/)

[roundcube docker-compose 範例](https://github.com/roundcube/roundcubemail-docker/tree/master/examples)

# 安裝

## docker-compose 部署

```yml
version: '3'
services:
  db:
    image: mariadb
    container_name: roundcube_db
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: example
      MYSQL_DATABASE: roundcubemail
      MYSQL_USER: roundcube
      MYSQL_PASSWORD: example
    volumes:
      - db-data:/var/lib/mysql
  roundcube:
    image: roundcube/roundcubemail
    container_name: roundcube
    restart: unless-stopped
    ports:
      - 8080:80
    environment:
      ROUNDCUBEMAIL_DB_TYPE: mysql
      ROUNDCUBEMAIL_DB_HOST: db
      ROUNDCUBEMAIL_DB_USER: roundcube
      ROUNDCUBEMAIL_DB_PASSWORD: example
      ROUNDCUBEMAIL_DB_NAME: roundcubemail
    volumes:
      - roundcube-data:/var/www/html
    depends_on:
      - db
```

訪問 Roundcube 界面

    在瀏覽器中打開 http://<your-server-ip>:8080，將會看到 Roundcube 的登錄界面。

初始配置

    使用 Roundcube 的管理面板完成初始配置，包括設置 IMAP 和 SMTP 服務器。
    確保數據庫連接信息正確，以便 Roundcube 能夠連接到 MariaDB 數據庫。

配置中文界面

    登錄 Roundcube 後，導航到 Settings -> Preferences -> User Interface，在 Language 下拉菜單中選擇 Chinese (Traditional)。

nginx 配置文檔

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
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

# 高級配置

## 插件和擴展

插件和擴展

Roundcube 支持多種插件和擴展功能，可以從官方插件庫安裝所需的插件。

安裝插件的方法如下：

    下載插件並將其解壓到 plugins 目錄。

    編輯 config/config.inc.php 文件，添加插件名稱到 plugins 數組。

```php
$config['plugins'] = array('plugin1', 'plugin2');
```

主題

```php
$config['skin'] = 'your_theme';
```