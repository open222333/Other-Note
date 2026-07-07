# Bitbucket API Token

```
Bitbucket Cloud 的單一用途存取金鑰，支援指定 Scope 的細粒度授權
適用場景：腳本自動化、CI/CD 工具、REST API 呼叫、Git 操作
```

## 目錄

- [Bitbucket API Token](#bitbucket-api-token)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [建立 API Token](#建立-api-token)
- [Token 權限範圍（Scopes）](#token-權限範圍scopes)
- [搭配 Git 指令使用](#搭配-git-指令使用)
  - [互動式輸入（密碼提示）](#互動式輸入密碼提示)
  - [直接嵌入 URL](#直接嵌入-url)
  - [更新現有 Remote](#更新現有-remote)
- [常見情境：App Password 已停用需遷移](#常見情境app-password-已停用需遷移)
- [搭配 Bitbucket REST API 使用](#搭配-bitbucket-rest-api-使用)
  - [Basic Auth（curl）](#basic-authcurl)
  - [Authorization Header（Base64）](#authorization-headerbase64)
- [Linux Credential 快取清除](#linux-credential-快取清除)
- [注意事項](#注意事項)

## 參考資料

[Using API tokens - Bitbucket Cloud 官方文件](https://support.atlassian.com/bitbucket-cloud/docs/using-api-tokens/)

[Bitbucket REST API 2.0](https://developer.atlassian.com/cloud/bitbucket/rest/intro/)

---

# 說明

API Token 是針對單一用途建立、具有指定 Scope 的存取金鑰，與帳號密碼獨立。

| 特性 | 說明 |
|---|---|
| 單一用途 | 每個 Token 建立時指定用途與 Scope |
| 細粒度授權 | 僅開放必要的 API 權限，不暴露帳號完整權限 |
| 可隨時撤銷 | 不影響帳號密碼，可單獨刪除 Token |
| 適用場景 | 腳本、CI/CD pipeline、REST API 呼叫、Git clone/push |

---

# 建立 API Token

1. 登入 Bitbucket Cloud
2. 右上角頭像 → **Manage account**
3. 左側選單 → **Personal Bitbucket settings** → **API tokens**
4. 點擊 **Create token**
5. 填寫：
   - **Token name**：描述用途（例如 `ci-deploy-token`）
   - **Scopes**：選擇需要的權限範圍（見下方說明）
6. 點擊 **Create** → 複製並保存 Token（離開頁面後無法再查看）

> Token 建立後僅顯示一次，請立即存入密鑰管理工具（如 Vault、GitHub Secrets）。

---

# Token 權限範圍（Scopes）

Token 必須設定至少一個 Scope 才能使用。

| Scope | 說明 |
|---|---|
| `account` | 讀取帳號資訊 |
| `repository` | 讀取 Repository（含 clone） |
| `repository:write` | 讀寫 Repository（含 push） |
| `pullrequest` | 讀取 Pull Request |
| `pullrequest:write` | 建立 / 更新 Pull Request |
| `pipeline` | 讀取 Pipeline 資訊 |
| `pipeline:write` | 觸發 Pipeline |
| `webhook` | 管理 Webhook |

---

# 搭配 Git 指令使用

認證資訊：
- **Bitbucket username**：帳號設定頁取得
- **API token**：建立時複製的 Token 值

## 互動式輸入（密碼提示）

```bash
# 使用 Bitbucket username
git clone https://{bitbucket_username}@bitbucket.org/{workspace}/{repository}.git

# 使用固定用戶名（不帶個人 username）
git clone https://x-bitbucket-api-token-auth@bitbucket.org/{workspace}/{repository}.git
```

> 執行後在 Password 提示輸入 API Token（非帳號密碼）。

## 直接嵌入 URL

```bash
# 使用 Bitbucket username
git clone https://{bitbucket_username}:{api_token}@bitbucket.org/{workspace}/{repository}.git

# 使用固定用戶名
git clone https://x-bitbucket-api-token-auth:{api_token}@bitbucket.org/{workspace}/{repository}.git
```

> 嵌入 URL 會將 Token 明文存入 git config / shell 歷史，CI/CD 環境請改用環境變數注入。

## 更新現有 Remote

```bash
git remote set-url origin https://{bitbucket_username}:{api_token}@bitbucket.org/{workspace}/{repository}.git
```

---

# 常見情境：App Password 已停用需遷移

Bitbucket Cloud 已停用 App Password 認證，若 repo 的 remote URL 仍是舊式帳密組合（帳號 + App Password），clone / push 會出現認證失敗。

**判斷方式**

```bash
git remote -v
# 輸出格式類似 https://{username}@bitbucket.org/{workspace}/{repository}.git
# 且 push/pull 提示輸入密碼卻驗證失敗，代表該帳密已失效，需改用 API Token
```

**處理步驟**

1. 依「[建立 API Token](#建立-api-token)」建立新 Token，Scope 至少勾選 `repository:write`（push 需要寫入權限）
2. 依「[更新現有 Remote](#更新現有-remote)」章節，將 remote URL 換成 Token 版本：

```bash
git remote set-url origin https://{bitbucket_username}:{api_token}@bitbucket.org/{workspace}/{repository}.git
```

---

# 搭配 Bitbucket REST API 使用

## Basic Auth（curl）

```bash
curl --user '{email}:{api_token}' \
     --request GET \
     --url 'https://api.bitbucket.org/2.0/repositories/{workspace}/{repository}'
```

## Authorization Header（Base64）

```bash
# 1. 編碼 credentials
encoded=$(echo -n '{email}:{api_token}' | base64)

# 2. 帶入 Header
curl --header "Authorization: Basic $encoded" \
     --request GET \
     --url 'https://api.bitbucket.org/2.0/repositories/{workspace}/{repository}'
```

---

# Linux Credential 快取清除

更換 API Token 後，若 Linux 上 git pull 仍顯示認證失敗，需清除舊的快取憑證。

**步驟一：確認目前的 credential helper**

```bash
git config --list | grep credential
```

**步驟二：依 helper 類型處理**

| Helper | 清除方式 |
|---|---|
| `store`（明文 `~/.git-credentials`）| 刪除對應行後重新 pull |
| `cache`（記憶體暫存）| `git credential-cache exit` |
| 無 helper | 直接重新輸入即可 |

使用 `store` 時清除舊記錄：

```bash
# 查看現有憑證
cat ~/.git-credentials

# 刪除 bitbucket.org 的舊記錄
sed -i '/bitbucket.org/d' ~/.git-credentials

# 重新 pull，password 輸入 API Token
git pull
```

使用 `cache` 時清除：

```bash
git credential-cache exit
git pull
```

**步驟三（可選）：直接嵌入 URL 繞過快取**

```bash
git remote set-url origin https://{bitbucket_username}:{api_token}@bitbucket.org/{workspace}/{repository}.git
git pull
```

---

# 注意事項

- **Token 安全**：不可 commit 進版控，應存入環境變數或密鑰管理工具
- **OAuth 優先**：第三方整合建議改用 OAuth，Token 適合個人腳本與 CI/CD
- **Token 撤銷**：懷疑洩漏時立即至 Account settings → API tokens 刪除對應 Token
- **Scope 最小權限**：建立時僅開放當下任務需要的 Scope，避免過度授權
