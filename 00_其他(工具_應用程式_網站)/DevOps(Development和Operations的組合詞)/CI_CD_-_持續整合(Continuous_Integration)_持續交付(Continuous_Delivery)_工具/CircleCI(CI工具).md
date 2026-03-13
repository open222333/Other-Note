# CircleCI(CI工具)

```
CircleCI是一個持續集成和持續交付平台，支援多種編程語言和框架。
它具有易於設置的配置文件和強大的並行執行功能。
```

## 目錄

- [CircleCI(CI工具)](#circlecici工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法大綱](#用法大綱)
- [.circleci/config.yml 範例](#circleciconfigyml-範例)

## 參考資料

[CircleCI 官方網站](https://circleci.com/)

[CircleCI 官方文檔](https://circleci.com/docs/)

# 用法大綱

```
註冊和設置帳戶：
前往CircleCI官方網站（https://circleci.com）並註冊一個帳戶。
在註冊完成後，使用GitHub或Bitbucket帳戶進行登錄。

專案配置：
在CircleCI中，每個專案都需要一個配置文件。
在專案的根目錄中創建一個名為.circleci/config.yml的文件。
.circleci/config.yml文件是用於定義CI/CD流程的YAML配置文件。

配置CI/CD流程：
在.circleci/config.yml文件中定義您的CI/CD流程。
可以指定要運行的測試、部署到哪個環境、執行任務和腳本等。

提交代碼：
將代碼推送到版本控制倉庫（如GitHub或Bitbucket）。

觸發CI/CD流程：
每次推送代碼時，CircleCI都會自動檢測並觸發CI/CD流程。
可以在CircleCI的網頁界面上查看流程運行的詳細日誌和結果。
```

# .circleci/config.yml 範例

```
說明

檢出代碼：使用checkout步驟從版本控制倉庫中檢出代碼。

安裝依賴：使用pip安裝requirements.txt文件中列出的所有依賴。

執行測試：使用pytest運行測試。

構建和推送Docker映像：使用docker命令構建Docker映像，並將其標記為具有唯一的標籤（使用$CIRCLE_SHA1）和latest標籤。然後，使用docker login登錄到Docker註冊表，並使用docker push推送映像到註冊表。
```

```yml
version: 2.1
jobs:
  build:
    docker:
      - image: circleci/python:3.8

    steps:
      - checkout

      - run:
          name: Install dependencies
          command: pip install -r requirements.txt

      - run:
          name: Run tests
          command: pytest

      - run:
          name: Build and push Docker image
          command: |
            docker build -t myapp:${CIRCLE_SHA1} .
            docker tag myapp:${CIRCLE_SHA1} myapp:latest
            docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
            docker push myapp:${CIRCLE_SHA1}
            docker push myapp:latest

workflows:
  version: 2
  build-and-deploy:
    jobs:
      - build
```