# Apache 筆記

```
Apache HTTP Server，老牌開源網站伺服器，市佔率長期前三。
以模組化架構著稱，支援 .htaccess 目錄層級設定、VirtualHost 多站點。
Debian 系列服務名稱為 apache2，RedHat 系列為 httpd。
```

## 目錄

- [Apache 筆記](#apache-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (macOS)](#homebrew-macos)
  - [配置文檔](#配置文檔)
    - [重要路徑對照](#重要路徑對照)
    - [VirtualHost 基本範例](#virtualhost-基本範例)
    - [VirtualHost SSL 範例](#virtualhost-ssl-範例)
    - [.htaccess 常用設定](#htaccess-常用設定)
- [指令](#指令)
  - [版本確認](#版本確認)
  - [服務管理（systemctl）](#服務管理systemctl)
  - [舊式指令（SysV init，CentOS 6 以前）](#舊式指令sysv-initcentos-6-以前)
  - [apachectl（兩種發行版通用）](#apachectl兩種發行版通用)
  - [模組管理（Debian）](#模組管理debian)
  - [站點管理（Debian）](#站點管理debian)
    - [sites-available / sites-enabled 機制](#sites-available--sites-enabled-機制)
    - [停用預設站點](#停用預設站點)

## 參考資料

[Apache 官方文檔](https://httpd.apache.org/docs/)

[Apache VirtualHost 文檔](https://httpd.apache.org/docs/current/vhosts/)

[Apache 模組索引](https://httpd.apache.org/docs/current/mod/)

[.htaccess 文檔](https://httpd.apache.org/docs/current/howto/htaccess.html)

---

# 安裝

## docker-compose 部署

```yml
services:
  apache:
    image: httpd:2.4
    container_name: apache
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./html:/usr/local/apache2/htdocs/
      - ./conf/httpd.conf:/usr/local/apache2/conf/httpd.conf
    restart: unless-stopped
```

## Debian (Ubuntu)

```bash
sudo apt update
sudo apt install apache2 -y

# 啟動並設定開機自動啟動
sudo systemctl enable --now apache2

# 確認版本
apache2 -v
```

## RedHat (CentOS)

```bash
# CentOS 7 / RHEL 7+
sudo yum install httpd -y

# CentOS 8 / RHEL 8+ / Rocky Linux
sudo dnf install httpd -y

# 啟動並設定開機自動啟動
sudo systemctl enable --now httpd

# 確認版本
httpd -v
```

## Homebrew (macOS)

```bash
brew install httpd

# 啟動服務
brew services start httpd

# 確認版本
httpd -v

# 設定檔位置
# /opt/homebrew/etc/httpd/httpd.conf  （Apple Silicon）
# /usr/local/etc/httpd/httpd.conf     （Intel）
```

## 配置文檔

### 重要路徑對照

| 項目 | Debian (Ubuntu) | RedHat (CentOS) |
|---|---|---|
| 服務名稱 | `apache2` | `httpd` |
| 主設定檔 | `/etc/apache2/apache2.conf` | `/etc/httpd/conf/httpd.conf` |
| 站點設定目錄 | `/etc/apache2/sites-available/` | `/etc/httpd/conf.d/` |
| 模組設定目錄 | `/etc/apache2/mods-available/` | `/etc/httpd/conf.modules.d/` |
| 網頁根目錄 | `/var/www/html/` | `/var/www/html/` |
| 存取 Log | `/var/log/apache2/access.log` | `/var/log/httpd/access_log` |
| 錯誤 Log | `/var/log/apache2/error.log` | `/var/log/httpd/error_log` |

### VirtualHost 基本範例

```apacheconf
# /etc/apache2/sites-available/example.com.conf

<VirtualHost *:80>
    ServerName   example.com
    ServerAlias  www.example.com
    DocumentRoot /var/www/example.com/public

    <Directory /var/www/example.com/public>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>

    ErrorLog  ${APACHE_LOG_DIR}/example.com_error.log
    CustomLog ${APACHE_LOG_DIR}/example.com_access.log combined
</VirtualHost>
```

### VirtualHost SSL 範例

```apacheconf
<VirtualHost *:443>
    ServerName example.com
    DocumentRoot /var/www/example.com/public

    SSLEngine on
    SSLCertificateFile    /etc/ssl/certs/example.com.crt
    SSLCertificateKeyFile /etc/ssl/private/example.com.key
    # 若有 CA bundle：
    # SSLCertificateChainFile /etc/ssl/certs/ca-bundle.crt

    <Directory /var/www/example.com/public>
        AllowOverride All
        Require all granted
    </Directory>
</VirtualHost>

# HTTP → HTTPS 轉址
<VirtualHost *:80>
    ServerName example.com
    Redirect permanent / https://example.com/
</VirtualHost>
```

### .htaccess 常用設定

```apacheconf
# 需要 AllowOverride All 才生效

# URL rewrite（需啟用 mod_rewrite）
Options -MultiViews
RewriteEngine On
RewriteCond %{REQUEST_FILENAME} !-f
RewriteRule ^ index.php [QSA,L]

# 禁止存取特定目錄
Options -Indexes

# 強制 HTTPS
RewriteCond %{HTTPS} off
RewriteRule ^ https://%{HTTP_HOST}%{REQUEST_URI} [L,R=301]

# 設定 Cache-Control
<FilesMatch "\.(jpg|jpeg|png|gif|ico|css|js)$">
    Header set Cache-Control "max-age=2592000, public"
</FilesMatch>
```

---

# 指令

## 版本確認

```bash
# Ubuntu/Debian
apache2 -v

# CentOS/RedHat
httpd -v

# 兩者通用
apachectl -v
```

## 服務管理（systemctl）

```bash
# ── Ubuntu / Debian（apache2）──────────────────────
systemctl start   apache2    # 啟動
systemctl stop    apache2    # 停止
systemctl restart apache2    # 重啟（短暫中斷）
systemctl reload  apache2    # 重新載入設定（不中斷連線）
systemctl status  apache2    # 查看狀態

systemctl enable  apache2    # 開機自動啟動
systemctl disable apache2    # 取消開機自動啟動

# ── CentOS / RedHat（httpd）────────────────────────
systemctl start   httpd
systemctl stop    httpd
systemctl restart httpd
systemctl reload  httpd
systemctl status  httpd

systemctl enable  httpd
systemctl disable httpd
```

## 舊式指令（SysV init，CentOS 6 以前）

```bash
/etc/init.d/httpd start
/etc/init.d/httpd stop
/etc/init.d/httpd restart
/etc/init.d/httpd configtest   # 語法檢查

chkconfig httpd on             # 開機啟動
chkconfig httpd off            # 取消開機啟動
```

## apachectl（兩種發行版通用）

```bash
apachectl start
apachectl stop
apachectl restart
apachectl graceful             # 優雅重啟（不中斷現有連線）
apachectl configtest           # 語法檢查
apachectl -M                   # 列出已載入模組
apachectl -S                   # 顯示 VirtualHost 設定摘要
```

## 模組管理（Debian）

```bash
# 啟用模組
sudo a2enmod rewrite
sudo a2enmod ssl
sudo a2enmod headers

# 停用模組
sudo a2dismod rewrite

# 套用後需要 reload
sudo systemctl reload apache2
```

## 站點管理（Debian）

```bash
# 啟用站點（在 sites-available 建立設定後）
sudo a2ensite example.com.conf

# 停用站點
sudo a2dissite example.com.conf

# 套用後需要 reload
sudo systemctl reload apache2

# 列出已啟用站點
ls /etc/apache2/sites-enabled/
```

### sites-available / sites-enabled 機制

`sites-available/` 存放所有設定檔原始檔；`sites-enabled/` 只存 symlink，Apache 實際讀的是 symlink 指向的檔案。

```
/etc/apache2/
├── sites-available/
│   ├── 000-default.conf       # 原始檔
│   ├── 001-8888.conf
│   └── default-ssl.conf
└── sites-enabled/
    ├── 000-default.conf -> ../sites-available/000-default.conf   # symlink（啟用中）
    └── 001-8888.conf    -> ../sites-available/001-8888.conf      # symlink（啟用中）
    # default-ssl.conf 沒有 symlink → 停用狀態
```

`a2ensite` / `a2dissite` 的本質就是新增或移除 `sites-enabled/` 裡的 symlink。

**`.conf` 副檔名是必要的**，因為 `apache2.conf` 用 glob 載入：

```apacheconf
# /etc/apache2/apache2.conf
IncludeOptional sites-enabled/*.conf
```

沒有 `.conf` 結尾的檔案（例如 `.bak`）不會被載入，即使放在 `sites-enabled/` 也無效。

### 停用預設站點

Debian 安裝後預設啟用 `000-default.conf`（HTTP）與視情況的 `default-ssl.conf`（HTTPS），通常不需要這兩個：

```bash
# 停用預設 HTTP 站點
a2dissite 000-default.conf

# 停用預設 SSL 站點（若有啟用）
a2dissite default-ssl.conf

service apache2 reload
```

執行後 `sites-enabled/` 移除對應 symlink，`sites-available/` 的原始檔不動。

> 若直接在 `sites-available/` 改名（如 `.bak`），但 `sites-enabled/` 裡的 symlink 仍存在，
> 該 symlink 會變成 broken symlink，Apache reload 時會出現警告。
> 應先用 `a2dissite` 移除 symlink，再改名或保留原始檔。
