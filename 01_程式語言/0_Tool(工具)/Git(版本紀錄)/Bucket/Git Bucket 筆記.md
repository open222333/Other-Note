# Git Bucket 筆記

```
```

## 目錄

- [Git Bucket 筆記](#git-bucket-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [bitbucket webhook 創建與接收處理](#bitbucket-webhook-創建與接收處理)
- [code review流程](#code-review流程)
- [app passwords](#app-passwords)

## 參考資料

[Bitbucket Cloud resources](https://support.atlassian.com/bitbucket-cloud/resources/)

[atlassian REST APIs](https://developer.atlassian.com/cloud/bitbucket/rest/intro/)

# bitbucket webhook 創建與接收處理

[Create and trigger a webhook tutorial](https://support.atlassian.com/bitbucket-cloud/docs/create-and-trigger-a-webhook-tutorial/)

[Webhooks REST APIs](https://developer.atlassian.com/cloud/bitbucket/rest/api-group-webhooks/)

# code review流程

[透過 bitbucket 用 pull request 做 code review](https://www.atlassian.com/git/tutorials/learn-about-code-review-in-bitbucket-cloud)

# app passwords

若clone時一直遇到權限不足,可使用

```
Personal settings -> App passwords

App passwords allow users to access their Bitbucket account through apps such as Sourcetree. We'll generate the app passwords for you, and you won't need to remember them.
```