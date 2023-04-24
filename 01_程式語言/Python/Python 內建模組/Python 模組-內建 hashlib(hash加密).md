# Python 模組-內建 hashlib(hash加密)

```
這個模塊針對許多不同的安全哈希和消息摘要算法實現了一個通用接口。

包括 FIPS 安全哈希算法 SHA1, SHA224, SHA256, SHA384 和 SHA512 (定義於 FIPS 180-2) 以及 RSA 的 MD5 算法 (定義於互聯網 RFC 1321)。

術語 "安全哈希" 和 "消息摘要" 是同義的。

較舊的算法被稱為消息摘要。現代的術語是安全哈希。
```

## 目錄

- [Python 模組-內建 hashlib(hash加密)](#python-模組-內建-hashlibhash加密)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[hashlib 官方文檔](https://docs.python.org/zh-tw/3/library/hashlib.html)

[Hashing Passwords in Python](https://www.vitoshacademy.com/hashing-passwords-in-python/)

# 用法

```Python
import hashlib, binascii, os


def hash_password(password):
    """Hash a password for storing."""
    salt = hashlib.sha256(os.urandom(60)).hexdigest().encode('ascii')
    pwdhash = hashlib.pbkdf2_hmac('sha512', password.encode('utf-8'),
                                salt, 100000)
    pwdhash = binascii.hexlify(pwdhash)
    return (salt + pwdhash).decode('ascii')

def verify_password(stored_password, provided_password):
    """Verify a stored password against one provided by user"""
    salt = stored_password[:64]
    stored_password = stored_password[64:]
    pwdhash = hashlib.pbkdf2_hmac('sha512',
                                  provided_password.encode('utf-8'),
                                  salt.encode('ascii'),
                                  100000)
    pwdhash = binascii.hexlify(pwdhash).decode('ascii')
    return pwdhash == stored_password
```

```Python
import hashlib

path = 'teststr'
salt = hashlib.md5()
salt.update(path.encode('utf-8'))
salt.hexdigest()


path = 'teststr'
salt = hashlib.sha256()
salt.update(path.encode('utf-8'))
salt.hexdigest()
```
