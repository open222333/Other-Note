# Linux 工具 yum(套件管理器)

## 參考資料

[官方網站](http://yum.baseurl.org/)

[yum(8) — Linux manual page](https://man7.org/linux/man-pages/man8/yum.8@@yum.html)

```
適用linux版本：
	RHEL
	CentOS
	Fedora
	Scientific Linux
	Oracle Linux
```

# 資源擴充庫(EPEL)

[Extra Packages for Enterprise Linux (EPEL)](https://docs.fedoraproject.org/en-US/epel/)

[Centos環境下，安裝Epel擴充資源庫](https://www.astralweb.com.tw/under-centos-environment-expand-the-repository-by-install-epel/)

[第 12 堂課：軟體管理與安裝及登錄檔初探](http://linux.vbird.org/linux_basic_train/unit12.php)


```bash
# 列出所有的軟體庫
yum repolist all

# Centos指令 安裝擴充資源庫
yum install epel-release -y
```


[依這個連結目錄去找到相對應的版本](http://dl.fedoraproject.org/pub/epel/)

```bash
# 查詢你的Centos版本 對應查到的版本
cat /etc/redhat-release //

wget http://dl.fedoraproject.org/pub/epel/5/x86_64/epel-release-5-4.noarch.rpm

sudo rpm -Uvh epel-release-5*.rpm
```

# 指令

```bash
# 安裝指定的安裝包package
yum install <package>

# 全部更新
yum update

# 更新指定程式包package
yum update <package>

# 檢查可更新的程式
yum check-update

# 升級指定程式包package
yum upgrade <package>

# 列出所有可以安裝或更新的包的資訊
yum info

# 顯示安裝包資訊package
yum info package

# 顯示所有已經安裝和可以安裝的程式包
yum list

# 顯示指定程式包安裝情況package
yum list package

# 搜尋匹配特定字元的package的詳細資訊
yum search package

# 刪除程式包package
yum remove | erase package

# 檢視程式package依賴情況
yum deplist package

# 清除快取目錄下的軟體包
yum clean packages

# 清除快取目錄下的 headers
yum clean headers

# 清除快取目錄下舊的 headers
yum clean oldheaders

# 清除快取目錄下的軟體包及舊的headers
yum clean
yum clean all

# 更新套件資料庫 只會更新套件清單, 不會安裝套件
yum makecache
```