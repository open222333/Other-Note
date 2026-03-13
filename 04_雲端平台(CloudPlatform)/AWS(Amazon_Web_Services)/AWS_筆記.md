# AWS 筆記

## 目錄

- [AWS 筆記](#aws-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [操作筆記](#操作筆記)
	- [清快取](#清快取)
- [除錯筆記](#除錯筆記)
	- [Unable to parse config file: /Users/name/ .aws/ credentials aws CLI](#unable-to-parse-config-file-usersname-aws-credentials-aws-cli)

## 參考資料

[官方文件](https://docs.aws.amazon.com/index.html)

# 操作筆記

## 清快取

```
進入 -> 無效判定 -> 建立無效判定 -> /* (全清) -> 等候已完成
```

# 除錯筆記

## Unable to parse config file: /Users/name/ .aws/ credentials aws CLI

`發生狀況 環境`

```
CentOS7
aws
Unable to parse config file: /Users/name/ .aws/ credentials aws CLI
```
[Unable to parse config file: /Users/name/ .aws/ credentials aws CLI](https://www.dailytask.co/task/unable-to-parse-config-file-usersnameawscredentials-ahmed-zidan)

```bash
vi ~/.aws/credentials
```

It should looks like the following syntax.

```conf
[default]
aws_secret_access_key = xxxxxxxxxxxxxxxxx
aws_access_key_id = keyxxxxxxxx
```