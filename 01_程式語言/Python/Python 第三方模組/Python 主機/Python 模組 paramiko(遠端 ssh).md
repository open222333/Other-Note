# Python 模組 paramiko(遠端 ssh)

```
```

## 目錄

- [Python 模組 paramiko(遠端 ssh)](#python-模組-paramiko遠端-ssh)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [使用密鑰文件進行 SSH 連接](#使用密鑰文件進行-ssh-連接)
  - [上傳和下載文件](#上傳和下載文件)
  - [遠端連線主機 使用 docker 指令匯出 log 到本地](#遠端連線主機-使用-docker-指令匯出-log-到本地)

## 參考資料

[paramiko pypi](https://pypi.org/project/paramiko/)

[官方文檔](https://www.paramiko.org/)

[Git](https://github.com/paramiko/paramiko)

# 指令

```bash
# 安裝
pip install paramiko
```

# 用法

```Python
import paramiko

# 建立一個 SSH 客戶端對象
ssh = paramiko.SSHClient()

# 自動添加主機密鑰
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())

# 使用用戶名和密碼連接到遠程主機
ssh.connect('your_server_address', username='your_username', password='your_password')

# 執行遠程命令
stdin, stdout, stderr = ssh.exec_command('ls -l')

# 獲取命令輸出
output = stdout.read().decode()
print(output)

# 關閉連接
ssh.close()
```

## 使用密鑰文件進行 SSH 連接

```Python
import paramiko

# 建立一個 SSH 客戶端對象
ssh = paramiko.SSHClient()

# 自動添加主機密鑰
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())

# 使用密鑰文件連接到遠程主機
private_key = paramiko.RSAKey.from_private_key_file('/path/to/private/key')
ssh.connect('your_server_address', username='your_username', pkey=private_key)

# 執行遠程命令
stdin, stdout, stderr = ssh.exec_command('ls -l')

# 獲取命令輸出
output = stdout.read().decode()
print(output)

# 關閉連接
ssh.close()
```

## 上傳和下載文件

上傳文件

```Python
import paramiko

# 建立一個 SSH 客戶端對象
ssh = paramiko.SSHClient()
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
ssh.connect('your_server_address', username='your_username', password='your_password')

# 使用 SFTP 上傳文件
sftp = ssh.open_sftp()
sftp.put('/local/path/to/file', '/remote/path/to/file')
sftp.close()

# 關閉連接
ssh.close()
```

下載文件

```Python
import paramiko

# 建立一個 SSH 客戶端對象
ssh = paramiko.SSHClient()
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
ssh.connect('your_server_address', username='your_username', password='your_password')

# 使用 SFTP 下載文件
sftp = ssh.open_sftp()
sftp.get('/remote/path/to/file', '/local/path/to/file')
sftp.close()

# 關閉連接
ssh.close()
```

## 遠端連線主機 使用 docker 指令匯出 log 到本地

```Python
import paramiko
import os

def ssh_connect(hostname, port, username, password):
    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(hostname, port, username, password)
    return ssh

def execute_docker_command(ssh, command):
    stdin, stdout, stderr = ssh.exec_command(command)
    output = stdout.read().decode()
    error = stderr.read().decode()
    if error:
        print(f"錯誤: {error}")
    return output

def save_log_to_local(filename, log_data):
    with open(filename, 'w') as file:
        file.write(log_data)

def main():
    hostname = '遠端主機IP'
    port = 22  # SSH 預設埠號
    username = '你的使用者名稱'
    password = '你的密碼'
    docker_command = 'docker logs 容器名稱'

    # 建立 SSH 連線
    ssh = ssh_connect(hostname, port, username, password)
    print("已成功連線到遠端主機")

    # 執行 Docker 指令並取得日誌
    log_data = execute_docker_command(ssh, docker_command)
    print("已成功取得日誌資料")

    # 將日誌儲存到本地
    local_log_filename = 'docker_logs.txt'
    save_log_to_local(local_log_filename, log_data)
    print(f"日誌已儲存到本地檔案: {local_log_filename}")

    # 關閉 SSH 連線
    ssh.close()

if __name__ == "__main__":
    main()
```
