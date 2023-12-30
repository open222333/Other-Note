# Python 模組 pre-commit(提升程式碼品質)

```
pre-commit 是一個工具，用於在 Git 提交之前運行一組預設或自定義的代碼檢查和格式化操作。它允許開發人員在提交代碼之前自動運行各種檢查，以確保代碼的一致性、質量和符合項目的規範。

以下是 pre-commit 的主要用途和優點：

代碼風格檢查： 可以使用 pre-commit 在提交之前運行代碼風格檢查工具，例如 Pylint、ESLint、Black、或其他語言和框架特定的檢查器。這有助於確保代碼風格符合項目或團隊的規範。

代碼格式化： 通常，pre-commit 還允許運行代碼格式化工具，例如 autopep8、Prettier 等，以自動修復代碼風格問題。

靜態代碼分析： 你可以配置 pre-commit 使用靜態代碼分析工具（如 Bandit、Flake8、ShellCheck 等）檢查代碼中的潛在問題，例如安全漏洞或未使用的變量。

文件檢查： pre-commit 可以檢查文件中的一致性，確保文件包含所需的標題、授權聲明等。

測試： 你可以包括測試腳本，確保在提交之前運行測試套件，以捕捉潛在的錯誤。

自定義檢查： 除了預設的插件之外，pre-commit 還支持自定義檢查器，你可以根據項目的需求添加自己的檢查。

使用 pre-commit 有助於提高代碼庫的整體質量，減少錯誤，並確保代碼風格的一致性。當多人合作開發項目時，這種自動化的代碼檢查流程特別有價值，有助於確保整個團隊都遵從相同的最佳實踐。
```

## 目錄

- [Python 模組 pre-commit(提升程式碼品質)](#python-模組-pre-commit提升程式碼品質)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [設定 .pre-commit-config.yaml](#設定-pre-commit-configyaml)
	- [.pre-commit-config.yaml 檔案](#pre-commit-configyaml-檔案)
		- [範例](#範例)

## 參考資料

[pre-commit pypi](https://pypi.org/project/pre-commit/)

[用 pre-commit 輕鬆提升程式碼品質](https://myapollo.com.tw/blog/pre-commit-the-best-friend-before-commit/)

# 指令

```bash
# 安裝
pip install pre-commit
```

`指令產生.pre-commit-config.yaml 設定檔`

```bash
pre-commit sample-config > .pre-commit-config.yaml
```

`安裝 pre-commit hooks`

```bash
pre-commit install --install-hooks
```

# 用法

## 設定 .pre-commit-config.yaml

```
.
├── README.md
├── requirements.txt
├── .git
├── .pre-commit-config.yaml
└── project/
```

## .pre-commit-config.yaml 檔案

```yaml
# See https://pre-commit.com for more information
# See https://pre-commit.com/hooks.html for more hooks
repos:
-   repo: https://github.com/pre-commit/pre-commit-hooks
    rev: v3.2.0
    hooks:
    -   id: trailing-whitespace
    -   id: end-of-file-fixer
    -   id: check-yaml
    -   id: check-added-large-files
```

### 範例

```yaml
# .pre-commit-config.yaml

# 這是一個 .pre-commit-config.yaml 配置文件的範例

repos:
-   repo: https://github.com/pre-commit/pre-commit-hooks
    rev: v4.0.1
    hooks:
        # 去除尾隨空格的 hook
    -   id: trailing-whitespace
        # 修復文件結尾的 hook
    -   id: end-of-file-fixer

-   repo: https://github.com/pre-commit/mirrors-eslint
    rev: v7.32.0
    hooks:
        # 使用 ESLint 進行代碼檢查的 hook，並帶有 --fix 參數
    -   id: eslint
        args: ['--fix']

-   repo: https://github.com/pre-commit/mirrors-black
    rev: v20.8b1
    hooks:
        # 使用 Black 進行代碼格式化的 hook
    -   id: black

-   repo: https://github.com/pre-commit/mirrors-isort
    rev: v5.15.2
    hooks:
        # 使用 isort 進行 import 排序的 hook
    -   id: isort
```
