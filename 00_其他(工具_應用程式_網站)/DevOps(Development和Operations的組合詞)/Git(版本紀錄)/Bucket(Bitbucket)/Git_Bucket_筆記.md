# Git Bucket 筆記

```
```

## 目錄

- [Git Bucket 筆記](#git-bucket-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [bitbucket webhook 創建與接收處理](#bitbucket-webhook-創建與接收處理)
- [code review流程](#code-review流程)
- [認證方式](#認證方式)

## 參考資料

[Bitbucket Cloud resources](https://support.atlassian.com/bitbucket-cloud/resources/)

[atlassian REST APIs](https://developer.atlassian.com/cloud/bitbucket/rest/intro/)

[Bitbucket API Token(認證設定)](./Bitbucket_API_Token(認證設定).md)

# bitbucket webhook 創建與接收處理

[Create and trigger a webhook tutorial](https://support.atlassian.com/bitbucket-cloud/docs/create-and-trigger-a-webhook-tutorial/)

[Webhooks REST APIs](https://developer.atlassian.com/cloud/bitbucket/rest/api-group-webhooks/)

# code review流程

[透過 bitbucket 用 pull request 做 code review](https://www.atlassian.com/git/tutorials/learn-about-code-review-in-bitbucket-cloud)

# 認證方式

App Password 已於 2026-07-28 全面停用（CHANGE-3222），clone / push 一律改用 **API Token** 或 **SSH**；沿用舊 App Password 的 git over HTTPS 操作會回傳 `410`。

建立方式、Scope、Credential 快取清除與錯誤排除見 [Bitbucket API Token(認證設定)](./Bitbucket_API_Token(認證設定).md)。
