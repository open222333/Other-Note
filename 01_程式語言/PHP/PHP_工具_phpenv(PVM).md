# PHP 工具 PHP Version Manager (PVM)

```
PHP Version Manager (PVM) 是一個方便的工具，用於在多個 PHP 版本之間進行管理和切換。
它的設計類似於 Python 的 pyenv 或 Node.js 的 nvm。
這裡提供了 PVM 的安裝和使用指南。

```

## 目錄

- [PHP 工具 PHP Version Manager (PVM)](#php-工具-php-version-manager-pvm)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[]()

# 安裝

```bash
git clone https://github.com/phpenv/phpenv.git ~/.phpenv
```

設置環境變量 在 shell 配置文件中（例如 .bashrc 或 .zshrc），添加以下行

然後加載配置文件

```bash
export PATH="$HOME/.phpenv/bin:$PATH"
eval "$(phpenv init -)"
source ~/.bashrc  # 或者 source ~/.zshrc
```

安裝 PHP-Build 插件

PHP-Build 是一個用於安裝 PHP 的插件。你需要安裝這個插件來使 PVM 可以安裝不同版本的 PHP

```bash
git clone https://github.com/php-build/php-build ~/.phpenv/plugins/php-build
```

更新

```sh
cd ~/.phpenv
git pull
cd ~/.phpenv/plugins/php-build
git pull
```

# 指令

查看可用的 PHP 版本

```bash
phpenv install -l
```

安裝特定版本的 PHP

```bash
phpenv install 7.4.16
```

設置全局 PHP 版本, 設置系統使用的默認 PHP 版本

```bash
phpenv global 7.4.16
```

設置本地 PHP 版本, 設置當前目錄使用的 PHP 版本

```bash
phpenv local 7.4.16
```

查看當前使用的 PHP 版本

```sh
phpenv version
```

列出已安裝的 PHP 版本

```sh
phpenv versions
```

卸載 PHP 版本

```sh
phpenv uninstall 7.4.16
```
