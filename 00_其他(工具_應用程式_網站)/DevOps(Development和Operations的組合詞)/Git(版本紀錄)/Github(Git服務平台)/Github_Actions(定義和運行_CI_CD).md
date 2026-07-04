# Github Actions（定義和運行 CI/CD）

```
GitHub Actions 是 GitHub 內建的 CI/CD 服務。
workflow 文件放在 .github/workflows/*.yml，由 GitHub 自動執行。
```

## 目錄

- [Github Actions（定義和運行 CI/CD）](#github-actions定義和運行-cicd)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
- [Workflow 檔案結構](#workflow-檔案結構)
  - [觸發條件（on）](#觸發條件on)
  - [Jobs 設定](#jobs-設定)
  - [Steps 與 Actions](#steps-與-actions)
  - [環境變數與 Secrets](#環境變數與-secrets)
  - [條件判斷（if）](#條件判斷if)
  - [矩陣策略（matrix）](#矩陣策略matrix)
- [常用 Actions](#常用-actions)
- [範例](#範例)
  - [Python 測試 + 部署](#python-測試--部署)
  - [Node.js 測試](#nodejs-測試)
  - [Docker 建置與推送](#docker-建置與推送)
  - [SSH 遠端部署](#ssh-遠端部署)
  - [定時排程執行](#定時排程執行)
  - [iOS 建置（Xcode）](#ios-建置xcode)
  - [Android 建置（Gradle）](#android-建置gradle)

## 參考資料

[GitHub Actions — 官方文檔](https://docs.github.com/en/actions)

[GitHub Actions Marketplace](https://github.com/marketplace?type=actions)

[Workflow syntax — 官方參考](https://docs.github.com/en/actions/writing-workflows/workflow-syntax-for-github-actions)

[官方範例集](https://github.com/actions/starter-workflows)

---

# 核心概念

| 概念 | 說明 |
|---|---|
| **Workflow** | 一個自動化流程，對應一個 `.yml` 文件 |
| **Event（觸發條件）** | 決定何時啟動 workflow（push、PR、schedule…） |
| **Job** | workflow 中的一個工作單元，可並行執行 |
| **Step** | job 中的單一步驟，順序執行 |
| **Action** | 可重複使用的操作模組（官方 / Marketplace / 自訂） |
| **Runner** | 執行 job 的虛擬機（GitHub 提供或自架） |
| **Secret** | 加密的敏感環境變數，在 repo Settings 中設定 |
| **Artifact** | job 之間或 workflow 後可保存的檔案產出物 |

---

# Workflow 檔案結構

```yaml
name: Workflow 名稱                 # 顯示在 Actions 頁面的名稱

on: ...                             # 觸發條件

env:                                # 全域環境變數（所有 job 可用）
  APP_ENV: production

jobs:
  job-name:                         # job 的 ID（英文、數字、- 、_）
    runs-on: ubuntu-latest          # Runner 類型
    steps:
      - name: 步驟名稱
        run: echo "Hello"
```

## 觸發條件（on）

```yaml
on:
  # 推送到指定分支時觸發
  push:
    branches:
      - main
      - 'release/**'
    paths:                          # 只有指定路徑有變更才觸發
      - 'src/**'
      - '**.py'

  # Pull Request 時觸發
  pull_request:
    branches:
      - main
    types: [opened, synchronize, reopened]

  # 定時觸發（UTC 時間，cron 格式）
  schedule:
    - cron: '0 2 * * *'            # 每天 UTC 02:00

  # 手動觸發（可傳入參數）
  workflow_dispatch:
    inputs:
      environment:
        description: '部署環境'
        required: true
        default: 'staging'
        type: choice
        options: [staging, production]

  # 其他 workflow 完成後觸發
  workflow_run:
    workflows: ["CI"]
    types: [completed]
```

## Jobs 設定

```yaml
jobs:
  build:
    runs-on: ubuntu-latest          # ubuntu-latest / macos-latest / windows-latest

    # job 之間的依賴（等 build 完成才執行 deploy）
    needs: [build]

    # 設定執行環境（搭配 branch protection rules）
    environment: production

    # job 層級的環境變數
    env:
      NODE_ENV: production

    # 設定 timeout（分鐘）
    timeout-minutes: 30

    # 並行執行控制
    concurrency:
      group: deploy-${{ github.ref }}
      cancel-in-progress: true
```

## Steps 與 Actions

```yaml
steps:
  # 使用官方 Action
  - name: Checkout 程式碼
    uses: actions/checkout@v4
    with:
      fetch-depth: 0                # 0 = 完整歷史（git log 需要）

  # 執行 shell 命令
  - name: 安裝依賴
    run: |
      pip install --upgrade pip
      pip install -r requirements.txt

  # 帶條件的步驟
  - name: 只在 main 分支部署
    if: github.ref == 'refs/heads/main'
    run: echo "Deploying..."

  # 失敗時也執行（cleanup 用）
  - name: 清除暫存
    if: always()
    run: rm -rf ./tmp

  # 設定輸出供後續步驟使用
  - name: 取得版本號
    id: get-version
    run: echo "version=$(cat VERSION)" >> $GITHUB_OUTPUT

  - name: 使用版本號
    run: echo "Version is ${{ steps.get-version.outputs.version }}"
```

## 環境變數與 Secrets

```yaml
# 在 repo Settings → Secrets and variables → Actions 設定 Secrets

steps:
  - name: 使用 Secret
    env:
      DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
      API_KEY: ${{ secrets.API_KEY }}
    run: |
      echo "連線到資料庫..."

  # 內建變數
  - name: 內建 GitHub 變數
    run: |
      echo "觸發分支: ${{ github.ref_name }}"
      echo "Commit SHA: ${{ github.sha }}"
      echo "觸發者: ${{ github.actor }}"
      echo "Repo 名稱: ${{ github.repository }}"
      echo "Event 類型: ${{ github.event_name }}"
```

## 條件判斷（if）

```yaml
# 常用條件表達式
if: github.event_name == 'push'
if: github.ref == 'refs/heads/main'
if: github.event.pull_request.merged == true
if: contains(github.event.head_commit.message, '[skip ci]') == false
if: success()                       # 前一步驟成功
if: failure()                       # 前一步驟失敗
if: always()                        # 無論成敗都執行
if: cancelled()                     # workflow 被取消時
```

## 矩陣策略（matrix）

```yaml
jobs:
  test:
    runs-on: ${{ matrix.os }}
    strategy:
      fail-fast: false              # 其中一個失敗不停止其他
      matrix:
        os: [ubuntu-latest, macos-latest]
        python-version: ['3.10', '3.11', '3.12']
        exclude:
          - os: macos-latest
            python-version: '3.10'

    steps:
      - uses: actions/setup-python@v5
        with:
          python-version: ${{ matrix.python-version }}
```

---

# 常用 Actions

| Action | 說明 |
|---|---|
| `actions/checkout@v4` | 檢出 repo 程式碼 |
| `actions/setup-python@v5` | 設定 Python 環境 |
| `actions/setup-node@v4` | 設定 Node.js 環境 |
| `actions/setup-java@v4` | 設定 Java / JDK 環境 |
| `actions/cache@v4` | 快取依賴（加速 build） |
| `actions/upload-artifact@v4` | 上傳建置產出物 |
| `actions/download-artifact@v4` | 下載其他 job 的產出物 |
| `docker/login-action@v3` | 登入 Docker Registry |
| `docker/build-push-action@v5` | 建置並推送 Docker Image |
| `appleboy/ssh-action@v1` | SSH 遠端執行指令 |
| `peaceiris/actions-gh-pages@v4` | 部署到 GitHub Pages |

---

# 範例

## Python 測試 + 部署

```yaml
# .github/workflows/ci.yml
name: CI/CD

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up Python
        uses: actions/setup-python@v5
        with:
          python-version: '3.12'

      - name: Cache pip
        uses: actions/cache@v4
        with:
          path: ~/.cache/pip
          key: ${{ runner.os }}-pip-${{ hashFiles('requirements*.txt') }}

      - name: Install dependencies
        run: |
          pip install -r requirements.txt
          pip install -r requirements-dev.txt

      - name: Run tests
        run: pytest --cov=app --cov-report=xml

  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v4

      - name: Deploy via SSH
        uses: appleboy/ssh-action@v1
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          script: |
            cd /var/www/myapp
            git pull origin main
            pip install -r requirements.txt
            systemctl restart myapp
```

## Node.js 測試

```yaml
name: Node.js CI

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        node-version: [18, 20, 22]

    steps:
      - uses: actions/checkout@v4

      - name: Use Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v4
        with:
          node-version: ${{ matrix.node-version }}
          cache: 'npm'

      - run: npm ci
      - run: npm run build --if-present
      - run: npm test
```

## Docker 建置與推送

```yaml
name: Docker Build & Push

on:
  push:
    branches: [main]
    tags: ['v*']

jobs:
  docker:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}

      - name: Extract metadata
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: myuser/myapp
          tags: |
            type=ref,event=branch
            type=semver,pattern={{version}}

      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}
          cache-from: type=gha
          cache-to: type=gha,mode=max
```

## SSH 遠端部署

```yaml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to server
        uses: appleboy/ssh-action@v1
        with:
          host: ${{ secrets.HOST }}
          username: ${{ secrets.USERNAME }}
          key: ${{ secrets.SSH_KEY }}
          port: 22
          script: |
            cd /var/www/myapp
            git pull origin main
            docker compose pull
            docker compose up -d --remove-orphans
            docker image prune -f
```

## 定時排程執行

```yaml
name: Scheduled Task

on:
  schedule:
    - cron: '0 1 * * *'            # 每天 UTC 01:00（台灣 09:00）
  workflow_dispatch:               # 允許手動觸發

jobs:
  run-task:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: '3.12'
      - run: python scripts/daily_task.py
        env:
          API_KEY: ${{ secrets.API_KEY }}
```

## iOS 建置（Xcode）

```yaml
name: iOS Build

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: macos-latest           # iOS 必須用 macOS Runner
    steps:
      - uses: actions/checkout@v4

      - name: Select Xcode version
        run: sudo xcode-select -s /Applications/Xcode_15.4.app

      - name: Install CocoaPods dependencies
        run: pod install

      - name: Build Archive
        run: |
          xcodebuild archive \
            -workspace MyApp.xcworkspace \
            -scheme MyApp \
            -configuration Release \
            -destination "generic/platform=iOS" \
            -archivePath ./build/MyApp.xcarchive \
            CODE_SIGN_IDENTITY="" \
            CODE_SIGNING_REQUIRED=NO

      - name: Export IPA
        run: |
          xcodebuild -exportArchive \
            -archivePath ./build/MyApp.xcarchive \
            -exportPath ./build/output \
            -exportOptionsPlist ExportOptions.plist

      - name: Upload IPA artifact
        uses: actions/upload-artifact@v4
        with:
          name: MyApp-${{ github.sha }}
          path: ./build/output/MyApp.ipa
```

## Android 建置（Gradle）

```yaml
name: Android Build

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Decode Keystore
        run: |
          echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 --decode > app/release-key.jks

      - name: Build Release AAB
        run: ./gradlew bundleRelease
        env:
          KEYSTORE_PATH: release-key.jks
          KEYSTORE_PASS: ${{ secrets.KEYSTORE_PASS }}
          KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
          KEY_PASS: ${{ secrets.KEY_PASS }}

      - name: Upload AAB artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-release-${{ github.sha }}
          path: app/build/outputs/bundle/release/app-release.aab
```
