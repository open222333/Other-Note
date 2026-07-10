# 文件 A｜自動化診斷檔（DIAGNOSIS）

> 版本：1.0（2026-07-04）
> 適用範圍：appdet.com 基礎設施專案（Ubuntu 24.04 / MySQL 8.4 主從 / Redis / Docker / Telegram-Claude agent / Bitbucket avnight）

---

## 第 0 章：制度總則（所有文件共用，優先級最高）

1. **單一事實來源**：本套四份文件（A/B/C/D）是制度的唯一版本。任何修正一律直接編輯原檔，**禁止建立副本**（如 `xxx_v2.md`、`xxx_final.md`、`xxx_copy.md`）。
2. **錯誤觀念必須刪除，不是註記**：發現舊內容錯誤時，直接刪除錯誤段落並寫入正確內容，不保留刪除線或「舊做法」段落。需要追溯歷史時交給 Git，不靠檔案內殘留。
3. **版本紀錄只留一行**：每次修改在文件開頭的「版本」行更新日期與版本號即可，不維護冗長 changelog。
4. **舊筆記的處理**：`ubuntu-server-setup` skill 等既有筆記若與本文件衝突，以本文件為準，並應盡快回頭修正該筆記（見第 4 章的待修正清單）。

---

## 第 1 章：痛點一｜上下文與 Token 爆量（最常消耗 token）

### 症狀
- Claude Code 或 Telegram agent 處理小任務時，token 用量與任務規模不成比例。
- Agent 迴圈每一輪都重送完整對話歷史與檔案內容（API 無狀態，歷史逐輪累加）。
- 長對話後期回應品質下降、開始遺忘早期約定。

### 根因
1. 讓模型讀取整個專案目錄，而非指定檔案。
2. 對話不清理、不壓縮，一個 session 混雜多個不相關任務。
3. 交辦任務時沒有附上「剛好夠用」的上下文，模型只好自己去翻，翻的過程全算 token。

### 修復方向
- 專案根目錄維護 `.claudeignore`，排除 `node_modules/`、`.git/`、`dist/`、`*.log`、備份檔與 dump 檔。
- **一個任務一個 session**：任務完成即 `/clear`；同任務過長時用 `/compact`。
- 所有交辦一律使用文件 D 的模板：模板強制填寫「相關檔案清單」與「不需要讀的範圍」。
- 跨 session 交接改用「交接摘要」（文件 B 的回報格式），不轉貼原始對話。

### 完成定義（可驗收）
- [ ] `.claudeignore` 已存在於每個活躍 repo。
- [ ] 近一週的交辦訊息 100% 使用文件 D 模板。
- [ ] 單一任務 session 不再出現「請回顧我們最開始說的」這類補救訊息。

---

## 第 2 章：痛點二｜認證與金鑰漂移（最易發生錯誤）

### 症狀
- Bitbucket HTTPS 推送突然失敗（App Password 已被 API Token 取代）。
- Sourcetree 與 Fork.app 的 OAuth 憑證互相覆蓋。
- SSH 連線出現 host key mismatch（伺服器重建後 ECDSA key 變更）。

### 根因
認證方式散落在多個工具（HTTPS token、OAuth、SSH），且外部服務會片面變更政策；每次失效都靠臨場排錯，重複消耗時間與 token。

### 修復方向
- **統一收斂到 SSH 認證**：所有 Git 遠端（含 `avnight/proxysql`）改用 SSH URL；淘汰 HTTPS token 與 GUI 工具的 OAuth 依賴。
- 建立金鑰盤點表（放在私有 repo 的 `docs/keys-inventory.md`）：每把 key 的用途、所在主機、建立日期。
- 標準處置流程：伺服器重建後第一步固定執行 `ssh-keygen -R <host>` 再重新信任，不繞道、不停用 `StrictHostKeyChecking`。

### 完成定義
- [ ] `git remote -v` 在所有 repo 都顯示 `git@bitbucket.org:` 開頭。
- [ ] 金鑰盤點表存在且三個月內更新過。
- [ ] 認證類故障的處理不再需要重新研究，直接照流程走。

---

## 第 3 章：痛點三｜外部狀態假設腐化（硬編碼值失效）

### 症狀
- Telegram 群組升級為 supergroup 後 `chat_id` 改變，通知全數失敗（error 400 + `migrate_to_chat_id`）。
- MySQL 官方 APT repo GPG key 過期，安裝流程中斷（因此改用 deb bundle）。
- MySQL 8.4 移除 `mysql_native_password`，沿用舊設定的連線直接失敗。

### 根因
把「當下正確的外部識別碼與行為」寫死在程式與筆記中，外部服務一變動就整條鏈路斷掉，且錯誤觀念殘留在舊筆記裡繼續誤導後續任務。

### 修復方向
- 所有外部識別碼（chat_id、token、host、port）進 `.env` 或設定檔，程式碼零硬編碼。
- Telegram 發送加入自動遷移處理：捕捉 400 錯誤中的 `migrate_to_chat_id`，自動更新儲存值並重送。
- 每次因外部變動修復問題後，**必須同步刪修相關舊筆記**（總則第 2 條），這是任務「完成」的必要條件（見文件 C 的完成清單）。

### 完成定義
- [ ] `grep -rn "chat_id\s*=" src/` 找不到寫死的數值。
- [ ] Telegram 發送函式含 migrate 自動處理。
- [ ] 舊筆記中不再存在已知失效的做法。

---

## 第 4 章：舊筆記待修正清單（發現即補充，修完即刪行）

| # | 位置 | 錯誤/風險觀念 | 應改為 |
|---|------|--------------|--------|
| 1 | ubuntu-server-setup / Redis 段 | `bind 0.0.0.0` + `protected-mode no` 直接對外開放 | 限定 bind 私網 IP 或以防火牆/Docker network 隔離，補上此前提說明 |
| 2 | ubuntu-server-setup / Redis 段 | 設定檔內示範明文密碼 | 改為引用環境變數或密鑰管理的寫法 |
| 3 | （新發現時往下加） | | |
