# PHP Lumen lumen-admin 筆記

```
```

## 目錄

- [PHP Lumen lumen-admin 筆記](#php-lumen-lumen-admin-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝步驟](#安裝步驟)
- [指令](#指令)

## 參考資料

[lumen-admin](https://github.com/gzydong/lumen-admin)

# 安裝步驟

```bash
# git clone lumen專案
git clone https://github.com/gzydong/lumen-admin.git

# 進資料夾(可能會更改名)
cd lumen-admin

# 安裝composer依賴包
composer install

# 給予storage資料夾權限
chmod -R 755 storage

# 複製.env
cp .env.example .env
vim .env

# 資料庫遷移
php artisan migrate
```

```conf
APP_NAME=Lumen
APP_ENV=local
APP_KEY=
APP_DEBUG=true
APP_URL=http://localhost
APP_TIMEZONE=UTC

LOG_CHANNEL=stack
LOG_SLACK_WEBHOOK_URL=

DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=homestead
DB_USERNAME=homestead
DB_PASSWORD=secret

CACHE_DRIVER=file
QUEUE_CONNECTION=sync
```

# 指令

LumenCMS

```
LumenCMS是一個輕量級的CMS系統，也可以作為一個通用的後台管理框架使用。
集成了用戶管理、權限管理、日誌管理等後台管理框架的通用功能。
```

```bash
# git clone 專案
git clone git@github.com:gzydong/ant-admin.git

# 安裝 依賴擴展組件
yarn install OR npm install

# 啟動本地開發環境
npm run serve

# 生產環境構建項目
npm run build

# 添加數據
php artisan db:seed
```