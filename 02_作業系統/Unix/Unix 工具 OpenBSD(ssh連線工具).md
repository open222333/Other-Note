# Unix 工具 OpenBSD(ssh連線工具)

## 參考資料

[OpenBSD (~/.ssh/config 參數)](https://man.openbsd.org/ssh_config.5)

[OpenSSH Manual Pages (指令)](https://www.openssh.com/manual.html)


# 指令

```bash
# 永久添加私鑰
ssh-add

vim /etc/ssh/ssh_config

	# IdentityFile ~/.ssh/key

# 文件需要有chmod 600
chmod 600 /etc/ssh/ssh_config
```

設置特定於一個主機的密鑰

`~/.ssh/config`

```conf
Host alias-name                      # 用來連線的 alias 名稱
HostName server.name                 # host domain 或 ip
Port port-number                     # host 的 SSH port
IdentitiesOnly yes                   # 使用指定的 key
IdentityFile ~/.ssh/private_ssh_file # 指定 pem 或 pub 的 key 路徑
```