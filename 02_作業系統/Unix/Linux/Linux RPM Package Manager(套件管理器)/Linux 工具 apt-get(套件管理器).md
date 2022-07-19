# Linux 工具 apt-get(套件管理器)

```
```

## 目錄

- [Linux 工具 apt-get(套件管理器)](#linux-工具-apt-get套件管理器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[官方網站](http://apt-rpm.org/)

```
適用linux版本：
	Debian
	Ubuntu
```

# 指令

```bash
# 更新套件的最新資訊
sudo apt update

# 安裝套件
sudo apt install <package-name>

# 更新套件
sudo apt upgrade <package-name>

# 移除套件
sudo apt remove/purge <package-name>

# 移除不再使用的依賴和庫文件
sudo apt autoremove

# 查看套件的可安裝的版本
sudo apt-cache policy <package-name>

# 查看套件更詳細的資訊
sudo apt-cache showpkg <package-name>
```