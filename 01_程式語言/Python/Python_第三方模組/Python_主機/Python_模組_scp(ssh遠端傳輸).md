# Python 模組 scp(ssh遠端傳輸)

```
```

## 目錄

- [Python 模組 scp(ssh遠端傳輸)](#python-模組-scpssh遠端傳輸)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[scp pypi](https://pypi.org/project/scp/)

# 指令

```bash
# 安裝
pip install scp
```

# 用法

```Python
import paramiko
from scp import SCPClient

def create_ssh_client(hostname, port, username, password):
    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(hostname, port=port, username=username, password=password)
    return ssh

def scp_get(remote_host, port, username, password, remote_path, local_path):
    ssh = create_ssh_client(remote_host, port, username, password)
    with SCPClient(ssh.get_transport()) as scp:
        scp.get(remote_path, local_path)

def scp_put(remote_host, port, username, password, local_path, remote_path):
    ssh = create_ssh_client(remote_host, port, username, password)
    with SCPClient(ssh.get_transport()) as scp:
        scp.put(local_path, remote_path)

if __name__ == "__main__":
    remote_host = "your_remote_host"
    port = 22
    username = "your_username"
    password = "your_password"

    # 获取远程文件到本地
    remote_path = "/path/to/remote/file"
    local_path = "/path/to/local/destination"
    scp_get(remote_host, port, username, password, remote_path, local_path)

    # 将本地文件传输到远程服务器
    local_file = "/path/to/local/file"
    remote_dest = "/path/to/remote/destination"
    scp_put(remote_host, port, username, password, local_file, remote_dest)
```
