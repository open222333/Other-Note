# Github(版本紀錄)

```
```

## 目錄

- [Github(版本紀錄)](#github版本紀錄)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [令牌的身份驗證](#令牌的身份驗證)
- [.gitignore 模板](#gitignore-模板)

## 參考資料

[Github 官方文檔](https://docs.github.com/en/developers)

[gitignore範本](https://github.com/github/gitignore)

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

# .gitignore 模板

```
各種常見的 .gitignore 模板。

以下是一些常見模板的簡要說明：

Global:
Global/Windows: 適用於Windows操作系統的全局模板，忽略了一些Windows特定的文件。
Global/macOS: 適用於macOS的全局模板，忽略了一些macOS特定的文件。
Global/Linux: 適用於Linux操作系統的全局模板，忽略了一些Linux特定的文件。

Languages:
C: 適用於C語言項目的模板，忽略了一些編譯生成的文件。
C++: 適用於C++語言項目的模板，忽略了一些編譯生成的文件。
Java: 適用於Java項目的模板，忽略了一些編譯生成的文件。
Python: 適用於Python項目的模板，忽略了一些Python編譯生成的文件和虛擬環境。
Node: 適用於Node.js項目的模板，忽略了 node_modules 目錄等。

Frameworks:
Ruby on Rails: 適用於Ruby on Rails項目的模板，忽略了一些生成的文件和目錄。

IDEs:
Visual Studio: 適用於Visual Studio項目的模板，忽略了一些Visual Studio生成的文件。
Eclipse: 適用於Eclipse項目的模板，忽略了一些Eclipse生成的文件。

Others:
TeX: 適用於LaTeX項目的模板，忽略了一些LaTeX生成的文件。
WordPress: 適用於WordPress項目的模板，忽略了一些WordPress生成的文件。
```
