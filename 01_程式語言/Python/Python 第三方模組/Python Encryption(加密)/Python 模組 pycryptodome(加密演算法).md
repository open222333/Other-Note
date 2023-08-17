# Python 模組 pycryptodome(加密演算法)

```
```

## 目錄

- [Python 模組 pycryptodome(加密演算法)](#python-模組-pycryptodome加密演算法)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [錯誤相關](#錯誤相關)
- [指令](#指令)
- [用法](#用法)
	- [AES加密和解密](#aes加密和解密)
	- [SHA-256雜湊](#sha-256雜湊)
	- [RSA加密和解密](#rsa加密和解密)

## 參考資料

[pycryptodome pypi](https://pypi.org/project/pycryptodome/)

[pycryptodomex pypi](https://pypi.org/project/pycryptodomex/)

[pycryptodome 文檔](https://pycryptodome.readthedocs.io/en/latest/src/introduction.html)

[PyCryptodome’s documentation](https://pycryptodome.readthedocs.io/en/latest/)

### 錯誤相關

[SystemError: PY_SSIZE_T_CLEAN macro must be defined for '#' formats](https://stackoverflow.com/questions/70705404/systemerror-py-ssize-t-clean-macro-must-be-defined-for-formats)

# 指令

```bash
# 安裝
pip install pycryptodome
```

# 用法

## AES加密和解密

```Python
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad

# 生成16位元組的金鑰和初始化向量
key = get_random_bytes(16)
iv = get_random_bytes(16)

# 加密
cipher = AES.new(key, AES.MODE_CBC, iv)
data = b"Hello, this is some data!"
cipher_text = cipher.encrypt(pad(data, AES.block_size))

# 解密
decipher = AES.new(key, AES.MODE_CBC, iv)
plain_text = unpad(decipher.decrypt(cipher_text), AES.block_size)

print("Original data:", data)
print("Decrypted data:", plain_text)
```

## SHA-256雜湊

```Python
from Crypto.Hash import SHA256

data = b"This is some data to hash."
hash_object = SHA256.new(data)
hash_value = hash_object.digest()

print("Hash value:", hash_value)
```

## RSA加密和解密

```Python
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP

# 生成RSA金鑰對
key = RSA.generate(2048)

# 加密
cipher = PKCS1_OAEP.new(key.publickey())
encrypted_data = cipher.encrypt(b"Hello, this is some data!")

# 解密
decipher = PKCS1_OAEP.new(key)
plain_text = decipher.decrypt(encrypted_data)

print("Original data:", b"Hello, this is some data!")
print("Decrypted data:", plain_text)
```
