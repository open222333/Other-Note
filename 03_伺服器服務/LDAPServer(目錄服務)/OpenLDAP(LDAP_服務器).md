# OpenLDAP(LDAP 服務器)

```
```

## 目錄

- [OpenLDAP(LDAP 服務器)](#openldapldap-服務器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)
  - [使用 LDIF 檔案（LDAP Data Interchange Format）來初始化目錄。](#使用-ldif-檔案ldap-data-interchange-format來初始化目錄)
  - [新增用戶](#新增用戶)
- [常用 LDAP 管理工具](#常用-ldap-管理工具)

## 參考資料

[]()

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
apt-get update
apt-get install slapd ldap-utils
```

使用 dpkg-reconfigure slapd 指令重新設定 OpenLDAP

```bash
dpkg-reconfigure slapd
```

依照提示進行以下配置

```
Omit OpenLDAP server configuration: 選擇"No"
DNS domain name: 輸入您的域名，例如example.com
Organization name: 輸入您的組織名稱，例如Example Company
Administrator password: 設定管理員密碼
Confirm password: 確認管理員密碼
Database backendend to use: 選擇MDB
Remove the database when slapd is purged: 選擇"No"
Move old database: 選擇"Yes"
Allow LDAPv2 protocol: 選擇"No"
```

## RedHat (CentOS)

```bash
yum install openldap-servers openldap-clients -y
```

建立管理員密碼並產生 LDIF 檔案

```bash
slappasswd
```

建立設定檔 config.ldif

```
dn: olcDatabase={2}hdb,cn=config
changetype: modify
replace: olcRootPW
olcRootPW: {SSHA}YourGeneratedPasswordHash
```

使用 ldapmodify 指令套用變更

```bash
ldapmodify -Y EXTERNAL -H ldapi:/// -f config.ldif
```

建立 base.ldif 檔案來初始化目錄

```
dn: dc=example,dc=com
objectClass: top
objectClass: dcObject
objectClass: organization
o: Example Company
dc: example

dn: ou=users,dc=example,dc=com
objectClass: top
objectClass: organizationalUnit
ou: users

dn: ou=groups,dc=example,dc=com
objectClass: top
objectClass: organizationalUnit
ou: groups
```

導入目錄結構

```bash
ldapadd -x -D "cn=admin,dc=example,dc=com" -W -f base.ldif
```

## 配置文檔

通常在 `/etc/ldap/` 或 `/etc/openldap/`

主要的配置文件包括 slapd.conf 和 ldap.conf

建立設定檔 config.ldif

```
dn: olcDatabase={2}hdb,cn=config
changetype: modify
replace: olcRootPW
olcRootPW: {SSHA}YourGeneratedPasswordHash
```

### 基本範例

```ini
dn: dc=example,dc=com
objectClass: top
objectClass: dcObject
objectClass: organization
o: Example Company
dc: example

dn: ou=users,dc=example,dc=com
objectClass: top
objectClass: organizationalUnit
ou: users
```

# 指令

## 服務操作

```bash
# 啟動服務
systemctl start slapd

# 查詢啟動狀態
systemctl status slapd

# 重新啟動
systemctl restart slapd

# 停止服務
systemctl stop slapd

# 開啟開機自動啟動
systemctl enable slapd

# 關閉開機自動啟動
systemctl disable slapd
```

## 使用 LDIF 檔案（LDAP Data Interchange Format）來初始化目錄。

範例 LDIF 檔案 init.ldif

```
dn: dc=example,dc=com
objectClass: top
objectClass: dcObject
objectClass: organization
o: Example Company
dc: example

dn: ou=users,dc=example,dc=com
objectClass: top
objectClass: organizationalUnit
ou: users
```

使用 ldapadd 指令匯入 LDIF 文件

```bash
ldapadd -x -D "cn=admin,dc=example,dc=com" -W -f init.ldif
```

## 新增用戶

範例 LDIF 檔案 user.ldif

```
dn: uid=john.doe,ou=users,dc=example,dc=com
objectClass: inetOrgPerson
uid: john.doe
sn: Doe
cn: John Doe
mail: john.doe@example.com
userPassword: password
```

```bash
ldapadd -x -D "cn=admin,dc=example,dc=com" -W -f user.ldif
```

# 常用 LDAP 管理工具

phpLDAPadmin：基於 Web 的 LDAP 管理介面。

Apache Directory Studio：強大的 LDAP 管理和瀏覽工具。

ldapsearch：命令列工具，用於搜尋 LDAP 目錄。
