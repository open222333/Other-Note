# Git Github 筆記

```
```

## 目錄

- [Git Github 筆記](#git-github-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [令牌的身份驗證](#令牌的身份驗證)

## 參考資料

[Github 官方文檔](https://docs.github.com/en/developers)

# 令牌的身份驗證

[Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)

`路徑`

```
Settings -> Developer settings -> Personal access tokens
生成令牌Token 成功後需複製保存 之後無法再次查看
```

```bash
# 查看到輸入密碼選項
git config --system --unset credential.helper

# 把token直接添加遠程倉庫鏈接中
# 避免同一個倉庫每次提交代碼都要輸入token
# <your_token>：換成你自己得到的token
# <USERNAME>：github的用戶名
# <REPO>：倉庫名稱
git remote set-url origin https://<your_token>@github.com/<USERNAME>/<REPO>.git
```