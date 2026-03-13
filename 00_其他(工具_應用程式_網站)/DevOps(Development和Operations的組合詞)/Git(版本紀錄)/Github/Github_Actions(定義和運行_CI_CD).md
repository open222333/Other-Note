# Github Actions(定義和運行 CI CD)

```
GitHub Actions 是由 GitHub 提供的一項服務，因此 workflow 文件和 GitHub Actions 功能是 GitHub 特有的。
它們緊密集成在 GitHub 平台上，使得在 GitHub 存儲庫中定義和運行 CI/CD 過程變得非常方便。

儘管 GitHub Actions 是 GitHub 提供的一項服務，但還有其他類似的 CI/CD 服務和工具，可以在不同的平台上使用。例如：

GitLab CI/CD： GitLab 提供了內建的 CI/CD 功能，使用 .gitlab-ci.yml 文件定義工作流程。

Bitbucket Pipelines： Bitbucket 也有內建的 CI/CD 工具，使用 bitbucket-pipelines.yml 文件配置。

Travis CI： Travis CI 是一個獨立的 CI 服務，它可以與 GitHub、Bitbucket 和 GitLab 一起使用。

Jenkins： Jenkins 是一個自由開源的 CI/CD 工具，可以在自己的伺服器上運行。

每個 CI/CD 服務都有自己的特定配置方式，但基本思想是相似的：
定義何時應該執行工作流程，以及工作流程中應該執行哪些步驟。
因此，即使 GitHub Actions 是 GitHub 的特有功能，其他 CI/CD 工具也提供了類似的功能。
```

## 目錄

- [Github Actions(定義和運行 CI CD)](#github-actions定義和運行-ci-cd)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [workflow 文件相關](#workflow-文件相關)
- [範例](#範例)

## 參考資料

[官方網站](https://github.com/features/actions)

[官方文檔](https://docs.github.com/en/actions)

### workflow 文件相關

```
GitHub Actions 中的 workflow 文件是一種配置文件，用於定義自動化的 CI/CD（持續整合/持續部署）過程。
這些文件包含了一系列指令和步驟，GitHub 根據這些指令自動執行特定的任務。workflow 文件通常以 YAML 格式編寫。

主要元素和結構：

觸發條件（on）： 定義了何時應該觸發 workflow，例如推送到特定分支、創建 pull request、定期執行等。

工作（jobs）： workflow 可以包含一個或多個 job。每個 job 可以在獨立的運行環境中執行，並且可以並行運行。每個 job 可以包含一系列步驟。

步驟（steps）： 每個 job 由一個或多個步驟構成。每個步驟都是一個獨立的操作，例如檢出程式碼、安裝依賴、運行測試等。

動作（actions）： 步驟中的操作可以是內置的 shell 命令，也可以是來自 GitHub Marketplace 或其他地方的事先定義的 actions。Actions 是可重複使用的自動化任務，可以在 workflow 中使用。

環境（environments）： 可以定義一個或多個運行環境，例如 VM、容器等。每個 job 可以在指定的運行環境中執行。

workflow 文件提供了一種以程式化的方式定義 CI/CD 流程，使團隊能夠在代碼變更時自動執行測試、構建和佈署等操作。
這有助於確保代碼的品質，並且可以更快地部署新功能。
```

# 範例

主要特點：

觸發條件 (on)： 在每次推送（push）到 main 分支時觸發 workflow。

Jobs (jobs)： 定義了一個名為 build 的 job，它在 Ubuntu 最新版本運行。

步驟 (steps)： 每個 job 包含一系列步驟，每個步驟執行一個操作。

actions/checkout@v2: 使用官方的 checkout action 來檢出程式碼。
actions/setup-python@v2: 使用官方的 setup-python action 來設置 Python 環境。
安裝依賴和執行測試的步驟。
這只是一個簡單的範例，實際的 workflow 可能包含更多的步驟，例如構建、測試、部署等。根據項目的需求，你可以添加更多的步驟和自定義操作。

```yaml
name: CI

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout repository
      uses: actions/checkout@v2

    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: '3.8'

    - name: Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install -r requirements.txt

    - name: Run tests
      run: |
        python -m pytest
```
