# Python 模組 cryptography(加密)

```
對稱加密使用相同的密鑰來加密和解密數據。
非對稱加密使用一對公開金鑰和私有金鑰。
```

```
儲存使用者密碼或者進行加密時，常常會使用一個稱為「鹽值（salt）」的隨機數。
這是一個隨機生成的數據，通常與密碼一同進行雜湊或者密碼學上的運算。

這裡有一些原因使用鹽值的好處：

提高安全性：
如果兩個使用者使用相同的密碼，如果沒有鹽值，它們的雜湊值將會相同。
這樣的情況下，如果一個使用者的帳戶被攻破，其他使用相同密碼的帳戶也會受到威脅。
有了鹽值，即便密碼相同，由於鹽值不同，雜湊值也會不同。

防止彩虹表攻擊：
彩虹表是一種預先計算好的密碼雜湊值對應明文密碼的表格。
使用鹽值可以大大增加破解的難度，因為攻擊者無法預先計算每種可能的鹽值。

每次加密都是唯一的：
使用鹽值，即便相同的密碼，每次加密的結果也會不同。
這種方式更加安全。

PBKDF2HMAC（Password-Based Key Derivation Function 2 HMAC）用來從密碼和鹽值生成密鑰，這個密鑰然後被用在加密和解密的過程中。
這是為了確保密碼不會以原始形式保存，提高了安全性。
```

## 目錄

- [Python 模組 cryptography(加密)](#python-模組-cryptography加密)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [對稱加密 (Symmetric Encryption)](#對稱加密-symmetric-encryption)
	- [非對稱加密 (Asymmetric Encryption)](#非對稱加密-asymmetric-encryption)

## 參考資料

[cryptography pypi](https://pypi.org/project/cryptography/)

# 指令

```bash
# 安裝
pip install cryptography
```

# 用法

## 對稱加密 (Symmetric Encryption)

```Python
# 對稱加密 (Symmetric Encryption)
from cryptography.fernet import Fernet

# 生成隨機密鑰
key = Fernet.generate_key()

# 初始化 Fernet 對象
cipher_suite = Fernet(key)

# 加密
text_to_encrypt = "Hello, World!"
encrypted_text = cipher_suite.encrypt(text_to_encrypt.encode('utf-8'))

# 解密
decrypted_text = cipher_suite.decrypt(encrypted_text)
print("Original Text:", text_to_encrypt)
print("Encrypted Text:", encrypted_text)
print("Decrypted Text:", decrypted_text.decode('utf-8'))
```

## 非對稱加密 (Asymmetric Encryption)

```Python
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives.asymmetric import padding

# 生成 RSA 密鑰對
private_key = rsa.generate_private_key(
    public_exponent=65537,
    key_size=2048,
)

public_key = private_key.public_key()

# 加密
text_to_encrypt = "Hello, World!"
ciphertext = public_key.encrypt(
    text_to_encrypt.encode('utf-8'),
    padding.OAEP(
        mgf=padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
    )
)

# 解密
decrypted_text = private_key.decrypt(
    ciphertext,
    padding.OAEP(
        mgf=padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
    )
)

print("Original Text:", text_to_encrypt)
print("Encrypted Text:", ciphertext)
print("Decrypted Text:", decrypted_text.decode('utf-8'))
```

```Python
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.fernet import Fernet

def encrypt_aes(data, password):
    # 使用 PBKDF2 進行密鑰派生
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        salt=b'salt',
        iterations=100000,
        length=32,
        backend=default_backend()
    )
    key = kdf.derive(password)

    # 使用 AES 加密
    cipher = Cipher(algorithms.AES(key), modes.CFB8(), backend=default_backend())
    encryptor = cipher.encryptor()
    ciphertext = encryptor.update(data) + encryptor.finalize()

    return ciphertext

def decrypt_aes(ciphertext, password):
    # 使用 PBKDF2 進行密鑰派生
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        salt=b'salt',
        iterations=100000,
        length=32,
        backend=default_backend()
    )
    key = kdf.derive(password)

    # 使用 AES 解密
    cipher = Cipher(algorithms.AES(key), modes.CFB8(), backend=default_backend())
    decryptor = cipher.decryptor()
    data = decryptor.update(ciphertext) + decryptor.finalize()

    return data

def encrypt_fernet(data, key):
    f = Fernet(key)
    encrypted_data = f.encrypt(data)
    return encrypted_data

def decrypt_fernet(encrypted_data, key):
    f = Fernet(key)
    decrypted_data = f.decrypt(encrypted_data)
    return decrypted_data

# 示範
data_to_encrypt = b"Hello, this is a secret message."
password_for_aes = b"password_for_aes"
key_for_fernet = Fernet.generate_key()

# 使用 AES 加密和解密
encrypted_aes = encrypt_aes(data_to_encrypt, password_for_aes)
decrypted_aes = decrypt_aes(encrypted_aes, password_for_aes)

print(f"AES 加密資料: {encrypted_aes}")
print(f"AES 解密資料: {decrypted_aes.decode()}")

# 使用 Fernet 加密和解密
encrypted_fernet = encrypt_fernet(data_to_encrypt, key_for_fernet)
decrypted_fernet = decrypt_fernet(encrypted_fernet, key_for_fernet)

print(f"Fernet 加密資料: {encrypted_fernet}")
print(f"Fernet 解密資料: {decrypted_fernet.decode()}")
```

```Python
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes, hmac
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.fernet import Fernet

def encrypt_aes(data, password):
    # 使用 PBKDF2 派生密碼
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        salt=b'my_salt',  # 在實際應用中，應使用唯一的鹽值
        iterations=100000,
        length=32,
        backend=default_backend()
    )
    key = kdf.derive(password)

    # 使用 AES 加密
    cipher = Cipher(algorithms.AES(key), modes.CFB, backend=default_backend())
    encryptor = cipher.encryptor()
    ciphertext = encryptor.update(data) + encryptor.finalize()

    return ciphertext

def decrypt_aes(ciphertext, password):
    # 使用 PBKDF2 派生密碼
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        salt=b'my_salt',  # 在實際應用中，應使用唯一的鹽值
        iterations=100000,
        length=32,
        backend=default_backend()
    )
    key = kdf.derive(password)

    # 使用 AES 解密
    cipher = Cipher(algorithms.AES(key), modes.CFB, backend=default_backend())
    decryptor = cipher.decryptor()
    decrypted_data = decryptor.update(ciphertext) + decryptor.finalize()

    return decrypted_data

def encrypt_fernet(data, key):
    # 使用 Fernet 加密
    cipher_suite = Fernet(key)
    cipher_text = cipher_suite.encrypt(data)

    return cipher_text

def decrypt_fernet(cipher_text, key):
    # 使用 Fernet 解密
    cipher_suite = Fernet(key)
    decrypted_data = cipher_suite.decrypt(cipher_text)

    return decrypted_data

# 測試資料
data_to_encrypt = b"Hello, cryptography!"
password_for_aes = b"my_secret_password_for_aes"
key_for_fernet = Fernet.generate_key()

# AES 加密和解密
ciphertext_aes = encrypt_aes(data_to_encrypt, password_for_aes)
decrypted_data_aes = decrypt_aes(ciphertext_aes, password_for_aes)

# Fernet 加密和解密
ciphertext_fernet = encrypt_fernet(data_to_encrypt, key_for_fernet)
decrypted_data_fernet = decrypt_fernet(ciphertext_fernet, key_for_fernet)

# 顯示結果
print(f"原始資料: {data_to_encrypt}")
print(f"AES 加密後: {ciphertext_aes}")
print(f"AES 解密後: {decrypted_data_aes}")
print(f"Fernet 加密後: {ciphertext_fernet}")
print(f"Fernet 解密後: {decrypted_data_fernet}")
```