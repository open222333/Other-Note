# Linux 工具 apk(Alpine套件管理器)

```
```

## 目錄

- [Linux 工具 apk(Alpine套件管理器)](#linux-工具-apkalpine套件管理器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [存儲庫相關](#存儲庫相關)
- [設定文檔](#設定文檔)
	- [軟體儲存庫設置](#軟體儲存庫設置)
- [指令](#指令)

## 參考資料

[Alpine Package Keeper - 官方維基](https://wiki.alpinelinux.org/wiki/Alpine_Package_Keeper)

[Alpine Linux 下的包管理工具 - 指令](https://wangchujiang.com/linux-command/c/apk.html)

### 存儲庫相關


[Official Alpine Linux mirrors - 官方鏡像](https://mirrors.alpinelinux.org/)

# 設定文檔

## 軟體儲存庫設置

```
/etc/apk/repositories 配置文件中包含了軟體儲存庫設置，默認是: /media/sdb1/apks
```

# 指令

```bash
# 根據設置 更新軟體包列表 更新最新本地镜像源
apk update

# 更新 所有安裝程式(包含核心程式)
apk upgrade

# 指定更新軟體包
apk add --upgrade busybox

# 查找所以可用软件包
apk search

# 查找所以可用软件包及其描述内容
apk search -v

# 通过软件包名称查找软件包
apk search -v 'acf*'

# 通过描述文件查找特定的软件包
apk search -v -d 'docker'

# 列出所有已安装的软件包
apk info

# 显示完整的软件包信息
apk info -a zlib

# 显示指定文件属于的包
apk info --who-owns /sbin/lbu
```
