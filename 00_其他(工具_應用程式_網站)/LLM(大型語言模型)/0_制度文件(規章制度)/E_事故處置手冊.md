# 文件 E｜事故處置手冊（INCIDENT_RUNBOOK）

> 版本：1.1（2026-07-11）新增第 0 章告警基準、劇本 6 Master failover
> 定位：出事當下的固定劇本。**事故中不即興發揮、不研究新做法**；劇本走完仍未解決 → 依文件 B 升 R，並依文件 C 清單 3 判斷是否叫人。
> 執行前提：一律先確認自己在哪台主機（`hostname` + `ip a`），Master 與 Slave 弄反是最嚴重的事故放大器。

---

## 第 0 章：告警來源與健康基準（什麼算「事故」）

**告警來源**（收到告警才進本手冊；來源有增減時更新本表）：

| 來源 | 涵蓋範圍 | 現況 |
|------|---------|------|
| Telegram bot 通知 | <待填：監控哪些項目> | <待填> |
| 人工發現（使用者回報） | 全部 | — |
| <待填：有無 cron 健康檢查腳本> | | |

**健康基準與告警閾值**（低於「注意」不動作；`<待確認>` 由使用者核定後改為定值）：

| 指標 | 正常 | 注意（回報不動作） | 告警（進對應劇本） |
|------|------|-------------------|-------------------|
| 磁碟使用率 `df` | < 80% | 80–89% | ≥ 90% → 劇本 4 |
| `Seconds_Behind_Source` | < 60 秒 | 60–300 秒 | > 300 秒持續 10 分鐘<待確認> → 劇本 1 分支 C |
| Replica IO/SQL Running | Yes/Yes | — | 任一 No → 劇本 1 |
| Redis `redis-cli ping` | PONG | — | 無回應 → 劇本 2 |
| Redis `used_memory` | < 70% maxmemory<待確認> | 70–90% | ≥ 90% → 劇本 2 判斷樹 2 |
| `systemctl --failed` | 0 筆 | — | ≥ 1 筆 → 對應服務劇本 |
| Telegram 通知 | 送達 | — | 連續失敗 → 劇本 3 |
| Master 主機 | SSH + `SELECT 1` 可達 | — | 皆不可達 → 劇本 6 |

---

## 通用開場（任何事故第一分鐘）

```bash
hostname && date
df -h                      # 磁碟滿是多數怪病的根因，先排除
free -h                    # 記憶體 / swap
systemctl --failed         # 有哪些服務掛了
```

- [ ] 記下當下時間與症狀原文（錯誤訊息全文，不要改寫）。
- [ ] 事故期間**禁止**：升級套件、改設定檔格式、重建容器 image。只做劇本內動作。

---

## 劇本 1：MySQL Replication 斷裂

### 觸發症狀
監控告警，或 `SHOW REPLICA STATUS\G` 出現 `Replica_IO_Running: No` 或 `Replica_SQL_Running: No`。

### 判斷樹

```bash
# 在 Slave 上
mysql -e "SHOW REPLICA STATUS\G" | grep -E "Running|Last_.*Errno|Last_.*Error|Seconds_Behind"
```

**分支 A：`Replica_IO_Running: No`（連不上 Master）**
1. `ping <master_ip>`、`mysql -h <master_ip> -u repl -p -e "SELECT 1"` 測連線與帳號。
2. 若錯誤含 `Authentication plugin` → 確認使用 `caching_sha2_password` 且連線參數含 `GET_SOURCE_PUBLIC_KEY=1`（8.4 已無 `mysql_native_password`，**不要**嘗試改回舊插件）。
3. 網路/帳號正常 → `STOP REPLICA; START REPLICA;` 一次。仍失敗 → ⬆ 升 R。

**分支 B：`Replica_SQL_Running: No`（套用 binlog 出錯）**
1. 讀 `Last_SQL_Error` 全文記下。
2. **禁止**自動執行 `SET GLOBAL sql_replica_skip_counter` 或跳過 GTID——一律 ⬆ 升 R 決策，因為跳事件可能造成主從資料永久分歧。

**分支 C：`Seconds_Behind_Source` 很大但兩個 Running 都是 Yes**
- 屬延遲非斷裂。檢查 Slave 磁碟 IO 與是否有人在 Slave 跑大查詢。只觀察、回報，不動設定。

### 事後必做
- [ ] 確認 Slave 的 `super_read_only` 仍為 ON。
- [ ] 依文件 I 寫一則覆盤；若根因是舊筆記誤導 → 依總則刪修。

---

## 劇本 2：Redis 無回應 / 掛掉

```bash
systemctl status redis-server
redis-cli -a "$REDIS_PASS" ping        # 期望 PONG
journalctl -u redis-server -n 50 --no-pager
```

判斷樹：
1. `ping` 有 PONG 但應用連不上 → 查應用端連線設定與網路（防火牆、Docker network），不是 Redis 的問題。
2. 服務死掉且 log 顯示 OOM → `free -h` 確認；重啟一次 `systemctl restart redis-server`；重複發生 → ⬆ 升 R 討論 `maxmemory` 策略。
3. log 出現大量陌生來源連線或 `Possible SECURITY ATTACK` → **立即**以防火牆封鎖 6379 對外，⬆ 升 R + ⛔ 通知使用者（對應文件 A 第 4 章已知風險：`bind 0.0.0.0`）。
4. 本專案 Redis 設定 `save ""` 無持久化 → 重啟即掉資料屬**預期行為**，不要當成資料遺失事故處理，但要回報受影響的快取範圍。

---

## 劇本 3：Telegram 通知失聯

```bash
# 手動測試發送
curl -s "https://api.telegram.org/bot$TELEGRAM_TOKEN/sendMessage" \
  -d chat_id="$CHAT_ID" -d text="runbook test" | head -c 500
```

判斷樹（依回應 error_code）：
- `400` 且含 `migrate_to_chat_id` → 群組升級 supergroup。取新 ID 更新 `.env`，重啟 bot，驗證。程式應已有自動遷移處理（文件 A 痛點三）；若沒有 → 開一張 D 實作單。
- `401 Unauthorized` → token 失效或被 revoke → ⛔ 問使用者（涉及金鑰，見文件 F）。
- `403 Forbidden` → bot 被踢出群組或被停用 → ⛔ 問使用者。
- `429` → 被限流，等待 `retry_after` 秒，檢查是否有通知迴圈在洗頻。
- 無回應/timeout → 主機出網問題：`curl -sI https://api.telegram.org` 測連外，再查 DNS 與防火牆。

## 劇本 4：磁碟滿（df 顯示 ≥ 90%）

```bash
du -xh / --max-depth=2 2>/dev/null | sort -rh | head -20
```

常見元兇與處置：
1. Docker：`docker system df` → 可安全執行 `docker image prune -f` 與 `docker builder prune -f`；**禁止** `docker system prune -a --volumes`（會刪 volume）。
2. MySQL binlog 堆積：**禁止**手動 `rm` binlog 檔；⬆ 升 R 以 `PURGE BINARY LOGS` 並先確認 Slave 已套用位置之後才可清。
3. 應用 log → 壓縮搬移可以，刪除前先確認不在事故調查範圍內。
4. 清完仍 ≥ 85% → ⬆ 升 R 討論擴容。

## 劇本 5：Docker Compose 服務起不來

```bash
docker compose ps
docker compose logs --tail=100 <service>
```

1. Port 衝突（`address already in use`）→ `ss -tlnp | grep <port>` 找占用者，回報後再決定殺誰。
2. 環境變數缺失 → 對照文件 G 的 `.env` 欄位清單補齊。
3. `restart` 一次可解 → 執行並記錄；同一服務一週內第二次靠 restart 續命 → 依文件 I 立覆盤、開 D 任務單查根因。
4. 需要 `docker compose down` 重建 → 正式環境屬服務中斷，⛔ 依文件 C 清單 3 先問。

## 劇本 6：Master 完全失效（failover：Slave 升級為 Master）

> **P1。全程由 R 主導，每一步都是 ⛔——AI 不得自動執行 failover，只能逐步提出指令由使用者同意或親手執行。**
> 這是文件 F「Slave 不得解除 `super_read_only`」的**唯一例外**，且仍需使用者對該步驟明確同意。

### 觸發症狀
Master 同時滿足：SSH 不通、`mysql -h <master_ip> -e "SELECT 1"` 失敗、Linode 控制台顯示異常或無法開機。**只有應用連不上但 SSH 可通 → 不是本劇本**，回劇本 1/5。

### 第一步：排除假死（做完才准談 failover）
1. 從第二個觀測點（本機 + Slave）各測一次，排除網路分區。
2. Linode 控制台查主機狀態與 Linode 事故公告。
3. 可透過控制台重啟且預期 10 分鐘內回復 → **優先重啟，不 failover**（failover 代價遠大於短暫停機）。

### Failover 程序（每步 ⛔，依序執行）
```bash
# 在 Slave 上
# 1. 確認 relay log 已全部套用（兩者相等才可繼續，否則等待或回報落差）
mysql -e "SHOW REPLICA STATUS\G" | grep -E "Retrieved_Gtid_Set|Executed_Gtid_Set"
# 2. 停止複製
mysql -e "STOP REPLICA;"
# 3. 解除唯讀（本套制度唯一允許情境）
mysql -e "SET GLOBAL super_read_only = OFF; SET GLOBAL read_only = OFF;"
```
4. 應用切換：`.env` / 連線字串指向 Slave IP，逐服務重啟並驗證（走 H 變更單，快照與回滾照填）。
5. 以 `[P1]` 通報使用者：切換完成時間、資料落差（步驟 1 的 GTID 差距）、目前架構為單點。

### 事後必做（缺一即未結案）
- [ ] 舊 Master 修復後**禁止直接開回線上接受寫入**（split-brain）。一律以新 Master 的備份重建為新 Slave，重建走 H 變更單。
- [ ] 更新文件 G 主機清單與角色、文件 F 第 3 章主機分級。
- [ ] 依文件 I 寫覆盤；檢討本劇本步驟與實際落差並當場修訂。

---

## 事故分級與通報

| 等級 | 定義 | 動作 |
|------|------|------|
| P1 | 正式服務對外不可用 / 資料可能遺失或分歧 | 立即 ⛔ 通知使用者（Telegram + 標題冠 `[P1]`），同步走劇本 |
| P2 | 冗餘失效（如 Slave 斷）但主服務正常 | 走劇本，完成或卡住時通報一次 |
| P3 | 內部工具異常（通知、CI）| 走劇本，日結回報即可 |

新場景發生且本手冊沒有劇本 → 事故解決後**必須**把劇本補進本檔（總則：直接編輯，不建副本）。
