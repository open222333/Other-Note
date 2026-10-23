# CI CD 總覽

```
本目錄收錄 CI/CD（持續整合 / 持續交付）工具：自架的 Jenkins、Bamboo、TeamCity，與雲端的 CircleCI、Travis CI。
```

## 目錄

- [CI CD 總覽](#ci-cd-總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [工具清單](#工具清單)
- [類型比較](#類型比較)
- [各工具說明](#各工具說明)
  - [Jenkins](#jenkins)
  - [CircleCI](#circleci)
  - [Bamboo](#bamboo)
  - [TeamCity](#teamcity)
  - [Travis CI](#travis-ci)
- [需求選擇建議](#需求選擇建議)

## 參考資料

- [Jenkins 筆記](./Jenkins(開源CI工具).md)
- [CircleCI 筆記](./CircleCI(CI工具).md)
- [GitLab 筆記（GitLab CI/CD）](../GitLab(DevOps平台).md)
- [Bamboo 官方網站](https://www.atlassian.com/software/bamboo)
- [TeamCity 官方網站](https://www.jetbrains.com/teamcity/)
- [Travis CI 官方網站](https://www.travis-ci.com/)

---

# 工具清單

| 工具 | 類型 | 開發者 | 主要用途 | 筆記 |
|---|---|---|---|---|
| **Jenkins** | 自架、開源 | Jenkins 社群 | 外掛生態最大，什麼都能接 | [Jenkins](./Jenkins(開源CI工具).md) |
| **CircleCI** | 雲端（另有自架版） | CircleCI | 設定檔簡單、並行執行快 | [CircleCI](./CircleCI(CI工具).md) |
| **GitLab CI/CD** | 內建於 GitLab | GitLab | 程式碼在 GitLab 時直接用 | [GitLab](../GitLab(DevOps平台).md) |
| **Bamboo** | 自架、商業 | Atlassian | 與 Jira / Bitbucket 整合 | 本篇 |
| **TeamCity** | 自架 / 雲端、商業 | JetBrains | 多語言支援、整合豐富 | 本篇 |
| **Travis CI** | 雲端 | Travis CI | 早期 GitHub 開源專案常用 | 本篇 |

---

# 類型比較

| 項目 | Jenkins | CircleCI | GitLab CI/CD | Bamboo | TeamCity | Travis CI |
|---|---|---|---|---|---|---|
| 部署方式 | 自架 | 雲端為主 | GitLab 內建 | 自架 | 自架 / 雲端 | 雲端 |
| 授權 | 開源免費 | 依用量計費 | 依 GitLab 方案 | 商業 | 商業（有免費額度） | 依用量計費 |
| 設定方式 | Jenkinsfile / Web UI | `.circleci/config.yml` | `.gitlab-ci.yml` | Web UI / Bamboo Specs | Web UI / Kotlin DSL | `.travis.yml` |
| 維運成本 | 高（主機、外掛升級） | 低 | 低～中 | 中 | 中 | 低 |

---

# 各工具說明

## Jenkins

開源 CI 工具，外掛生態最豐富，可支援各種語言與版本控制系統。適合需要完全掌控建置環境、或有內網部署需求的團隊；缺點是主機與外掛都要自己維護。

## CircleCI

雲端 CI/CD 平台，支援多種語言與框架，設定檔容易上手，並行執行能力強。適合不想自架、想快速導入的團隊。

## Bamboo

Atlassian 開發的 CI/CD 工具，能與 Jira、Bitbucket 等 Atlassian 產品無縫整合，提供視覺化的流水線設計。適合已經全面使用 Atlassian 生態的團隊。

## TeamCity

JetBrains 開發的 CI 伺服器，支援各種語言與工具，擴充性與整合選項豐富。適合 JetBrains IDE 使用者或 .NET / Java 為主的團隊。

## Travis CI

雲端 CI 服務，特別針對 GitHub 專案提供整合，支援多種語言與平台，設定簡單。早期開源專案大量使用；免費額度縮減後，GitHub 專案多改用 GitHub Actions。

---

# 需求選擇建議

| 情境 | 推薦工具 | 理由 |
|---|---|---|
| 內網、要完全掌控建置環境 | Jenkins | 開源、可自架、外掛多 |
| 程式碼放在 GitLab | GitLab CI/CD | 不用另外接工具 |
| 程式碼放在 GitHub、不想自架 | GitHub Actions、CircleCI | 整合直接、免維護 |
| 團隊用 Jira / Bitbucket | Bamboo | 同一家產品整合最好 |
| JetBrains 生態、需要商業支援 | TeamCity | 與 IDE 整合好 |
