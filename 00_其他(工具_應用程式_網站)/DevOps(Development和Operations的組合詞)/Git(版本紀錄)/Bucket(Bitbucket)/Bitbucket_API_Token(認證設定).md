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
- [搭配 Bitbucket REST API 使用](#搭配-bitbucket-rest-api-使用)
  - [Basic Auth（curl）](#basic-authcurl)
  - [Authorization Header（Base64）](#authorization-headerbase64)
- [例外狀況](#例外狀況)
  - [Git 操作回傳 410：App Password 已停用](#git-操作回傳-410app-password-已停用)
  - [換 Token 後仍認證失敗：Credential 快取未清](#換-token-後仍認證失敗credential-快取未清)
  - [Token 正確卻回 401：Username 欄位用錯](#token-正確卻回-401username-欄位用錯)
- [注意事項](#注意事項)

## 參考資料

[Using API tokens - Bitbucket Cloud 官方文件](https://support.atlassian.com/bitbucket-cloud/docs/using-api-tokens/)

[Bitbucket REST API 2.0](https://developer.atlassian.com/cloud/bitbucket/rest/intro/)

[CHANGE-3222 App Password 停用公告](https://developer.atlassian.com/cloud/bitbucket/changelog#CHANGE-3222)

---

# 說明

API Token 是針對單一用途建立、具有指定 Scope 的存取金鑰，與帳號密碼獨立。

| 特性 | 說明 |
|---|---|
| 單一用途 | 每個 Token 建立時指定用途與 Scope |
| 細粒度授權 | 僅開放必要的 API 權限，不暴露帳號完整權限 |
| 可設定到期 | 建立時指定 expiry，過期自動失效 |
| 可隨時撤銷 | 不影響帳號密碼，可單獨刪除 Token |
| 適用場景 | 腳本、CI/CD pipeline、REST API 呼叫、Git clone/push |

> App Password 為前代機制，已於 2026-07-28 全面停用，一律改用 API Token（見「[例外狀況](#例外狀況)」）。

---

# 建立 API Token

1. 登入 Bitbucket Cloud
2. 右上角頭像 → **Account settings**
3. 左側選單 → **Security** → **Create and manage API tokens**
4. 點擊 **Create API token with scopes**
5. 填寫：
   - **Name**：描述用途（例如 `ci-deploy-token`）
   - **Expiry**：到期日
   - **App**：選擇 **Bitbucket**
   - **Scopes**：勾選需要的權限範圍（見下方說明）
6. 點擊 **Create** → 複製並保存 Token（離開頁面後無法再查看）

> Token 建立後僅顯示一次，請立即存入密鑰管理工具（如 Vault、GitHub Secrets）。

---

# Token 權限範圍（Scopes）

Token 必須設定至少一個 Scope 才能使用，Git 操作與 REST API 皆然。

| Scope | 說明 |
|---|---|
| `read:repository:bitbucket` | 讀取 Repository（clone / fetch / pull 必要） |
| `write:repository:bitbucket` | 寫入 Repository（push 必要，需與 read 併用） |
| `read:account` | 讀取帳號資訊 |
| `read:pullrequest:bitbucket` | 讀取 Pull Request |
| `write:pullrequest:bitbucket` | 建立 / 更新 Pull Request |
| `read:pipeline:bitbucket` | 讀取 Pipeline 資訊 |
| `write:pipeline:bitbucket` | 觸發 Pipeline |
| `write:webhook:bitbucket` | 管理 Webhook |

---

# 搭配 Git 指令使用

認證資訊：
- **Bitbucket username**：Account settings → Bitbucket profile settings 取得，**大小寫敏感**
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

# 例外狀況

## Git 操作回傳 410：App Password 已停用

**錯誤訊息**

```
remote: CHANGE-3222 - Functionality has been deprecated
remote: App passwords are deprecated and must be replaced with API tokens.
remote: https://developer.atlassian.com/cloud/bitbucket/changelog#CHANGE-3222
fatal: unable to access 'https://bitbucket.org/{workspace}/{repository}.git/': The requested URL returned error: 410
```

**原因**

Bitbucket Cloud 停用 App Password，本機 credential helper 仍送出舊的 App Password，伺服器直接拒絕。

| 時程 | 狀態 |
|---|---|
| 2026-06-09 ~ 07-27 | Brownout，斷線窗口逐步拉長 |
| 2026-07-28 起 | 全面停用 |

| 操作類型 | 失敗狀態碼 |
|---|---|
| Git over HTTPS | `410 Gone` |
| REST API | `401 Unauthorized` |

**解法**

1. 依「[建立 API Token](#建立-api-token)」建立 Token，Scope 勾 `read:repository:bitbucket` +（要 push 則加）`write:repository:bitbucket`
2. 清除本機舊憑證，見「[換 Token 後仍認證失敗](#換-token-後仍認證失敗credential-快取未清)」
3. 重新執行 git 操作，Password 提示輸入 API Token

```bash
git fetch origin
# Username: {bitbucket_username}   ← 大小寫需與帳號設定頁完全一致
# Password: {api_token}
```

改用 SSH 可免除 Token 到期維護：

```bash
git remote set-url origin git@bitbucket.org:{workspace}/{repository}.git
```

> 同一組 App Password 常同時用於 CI/CD、部署腳本與其他本機 clone，需一併更換。

## 換 Token 後仍認證失敗：Credential 快取未清

**錯誤訊息**

已更換 API Token，`git pull` / `git fetch` 仍回 `401` 或 `410`，且未跳出帳密輸入提示。

**原因**

credential helper 自動回填舊的 App Password，git 未向使用者索取新憑證。

**解法**

先確認目前使用的 helper：

```bash
git config --list | grep credential
```

| 平台 / Helper | 清除方式 |
|---|---|
| macOS `osxkeychain` | `git credential-osxkeychain erase` |
| Linux `store`（明文 `~/.git-credentials`） | 刪除對應行後重新 pull |
| Linux `cache`（記憶體暫存） | `git credential-cache exit` |
| 無 helper | 直接重新輸入即可 |

macOS（osxkeychain）：

```bash
git credential-osxkeychain erase <<EOF
protocol=https
host=bitbucket.org
EOF

# 重新輸入 Bitbucket username 與 API Token
git fetch origin
```

Linux（store）：

```bash
# 查看現有憑證
cat ~/.git-credentials

# 刪除 bitbucket.org 的舊記錄
sed -i '/bitbucket.org/d' ~/.git-credentials

git pull
```

Linux（cache）：

```bash
git credential-cache exit
git pull
```

繞過快取（可選）：

```bash
git remote set-url origin https://{bitbucket_username}:{api_token}@bitbucket.org/{workspace}/{repository}.git
git pull
```

## Token 正確卻回 401：Username 欄位用錯

**錯誤訊息**

Token 已建立且 Scope 正確，仍回 `401 Unauthorized` 或 `Invalid credentials`。

**原因**

Git 與 REST API 使用的 Username 欄位不同。

| 用途 | Username 欄位 |
|---|---|
| Git over HTTPS | Bitbucket username（**大小寫敏感**）或固定值 `x-bitbucket-api-token-auth` |
| REST API | Atlassian 帳號 email |

**解法**

Bitbucket username 於 Account settings → **Bitbucket profile settings** 查看，需與頁面顯示完全一致；不確定時 Git 端改用 `x-bitbucket-api-token-auth`。

---

# 注意事項

- **Token 安全**：不可 commit 進版控，應存入環境變數或密鑰管理工具
- **OAuth 優先**：第三方整合建議改用 OAuth，Token 適合個人腳本與 CI/CD
- **Token 撤銷**：懷疑洩漏時立即至 Account settings → Security → API tokens 刪除對應 Token
- **Scope 最小權限**：建立時僅開放當下任務需要的 Scope，避免過度授權
- **到期提醒**：Token 有 expiry，到期前需輪替，CI/CD 應排入維護排程
