# PHP 工具 valet-plus(本地開發環境管理工具)

```
Valet+ 是一個本地開發環境管理工具，主要針對 MacOS，用於簡單地配置 PHP 和其他開發工具。
它基於 Homebrew 和 Dnsmasq，可以快速設置多個 PHP 環境。
```

## 目錄

- [PHP 工具 valet-plus(本地開發環境管理工具)](#php-工具-valet-plus本地開發環境管理工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)

## 參考資料

[valet-plus 官方網站](https://valetlinux.plus/)

[valet-plus github](https://github.com/weprovide/valet-plus)

# 安裝

```bash
# 使用 Homebrew 安裝 Valet+
brew tap weprovide/valet-plus
brew install valet-plus

# 初始化 Valet+
valet install

# 切換 PHP 版本
valet use php@7.4

# 檢查 PHP 版本
php -v
```
