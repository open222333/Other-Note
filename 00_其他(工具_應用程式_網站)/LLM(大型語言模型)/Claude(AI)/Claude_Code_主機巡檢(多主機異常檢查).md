# Claude Code 主機巡檢(多主機異常檢查)

```
用 Claude Code 的 subagent 檢查多台主機是否異常。
做法：唯讀腳本透過 SSH 收集狀態 → 只能跑這支腳本的 subagent 判讀 → 權限鎖住；
主機多時由主 Claude 分派多個 subagent 平行檢查，要定期跑就用 claude -p 掛 cron。
```

## 目錄

- [Claude Code 主機巡檢(多主機異常檢查)](#claude-code-主機巡檢多主機異常檢查)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [架構](#架構)
- [配置文檔](#配置文檔)
  - [唯讀收集腳本 scripts/fleet\_check.sh](#唯讀收集腳本-scriptsfleet_checksh)
  - [巡檢 subagent .claude/agents/host-checker.md](#巡檢-subagent-claudeagentshost-checkermd)
  - [權限鎖定 .claude/settings.json](#權限鎖定-claudesettingsjson)
  - [subagent 常用欄位](#subagent-常用欄位)
- [指令](#指令)
  - [手動檢查](#手動檢查)
  - [主機多時平行檢查](#主機多時平行檢查)
  - [定期自動跑（cron + claude -p）](#定期自動跑cron--claude--p)
- [什麼時候不用 Claude](#什麼時候不用-claude)

## 參考資料

[Claude Code - Subagents 官方文件](https://code.claude.com/docs/en/sub-agents)

[Claude Code - Settings / Permissions](https://code.claude.com/docs/en/settings)

[Claude Dispatch(多Agent分派) 筆記（Agent SDK 寫程式分派子 Agent）](Claude_Dispatch(多Agent分派).md)

[Claude API 主機巡檢系統筆記（寫程式版：可擴充檢查項目與回報方式）](Claude_API_主機巡檢系統(可擴充架構).md)

[AI 主機巡檢系統設計（通用系統設計文件）](../../../01_程式語言/AI/AI_主機巡檢系統設計(系統設計文件).md)

[CLAUDE.md 筆記](CLAUDE_md(專案設定檔).md)

[Linux 使用者管理筆記 → sudoers Cmnd_Alias（限制巡檢帳號只能跑特定指令）](../../../02_作業系統/Unix/Linux/Linux_基本_使用者管理(Linux指令).md#sudoers-配置檔-cmnd_alias)

[df / du 筆記 → 磁碟滿了排查（常見連鎖異常）](../../../02_作業系統/Unix/Linux/Linux_工具/Linux_磁碟與檔案系統部分/Linux_工具_df_du(硬碟空間資訊).md#磁碟滿了use-100排查與清理)

# 架構

```
專案目錄（例如 /opt/fleet）
├── hosts.txt                     # 主機清單（~/.ssh/config 的別名，一行一台）
├── scripts/fleet_check.sh        # 唯讀收集腳本：平行 SSH，每台輸出一個檔
└── .claude/
    ├── agents/host-checker.md    # 巡檢 subagent：只能跑腳本 + 讀檔，負責判讀
    └── settings.json             # 權限：只允許跑腳本，禁止直接 ssh / sudo / rm
```

- **收資料交給腳本、判讀交給 Claude**：結果穩定、省 token，也不讓 Claude 在主機上自由下指令。
- **subagent 的好處**：大量原始輸出留在 subagent 自己的 context，只回傳摘要給主對話；可以同時派多個平行跑。

# 配置文檔

## 唯讀收集腳本 scripts/fleet_check.sh

```bash
#!/usr/bin/env bash
# 唯讀健康檢查：./scripts/fleet_check.sh <輸出目錄> host1 host2 ...
# 主機名用 ~/.ssh/config 的別名，金鑰登入
set -u
out=$1; shift; mkdir -p "$out"

check() {
  ssh -o BatchMode=yes -o ConnectTimeout=5 "$1" 'bash -s' > "$out/$1.txt" 2>&1 <<'REMOTE'
echo "== uptime / load";   uptime
echo "== 磁碟 >=80%";      df -hP -x tmpfs -x devtmpfs | awk 'NR==1 || $5+0>=80'
echo "== 記憶體";          free -h | head -2
echo "== systemd 失敗";    systemctl --failed --no-legend 2>/dev/null
echo "== 異常容器";        docker ps -a --format '{{.Names}}\t{{.Status}}' 2>/dev/null | grep -E 'Restarting|Exited|unhealthy|Dead'
echo "== 近 1 小時錯誤";   journalctl -p err --since "-1h" --no-pager -q 2>/dev/null | tail -20
echo "== 近 24 小時 OOM";  journalctl -k --since "-24h" --no-pager -q 2>/dev/null | grep -i "out of memory" | tail -5
REMOTE
  [ $? -ne 0 ] && echo "== SSH 連線失敗" >> "$out/$1.txt"
}

for h in "$@"; do check "$h" & done   # 平行連線
wait
```

```bash
chmod +x scripts/fleet_check.sh
./scripts/fleet_check.sh /tmp/fleet $(cat hosts.txt)   # 先手動跑一次確認輸出
```

- `BatchMode=yes`：不跳密碼提示，金鑰失敗直接報錯，不會卡住。
- `ConnectTimeout=5`：主機掛掉時 5 秒就放棄，記成「SSH 連線失敗」。
- 巡檢帳號用專門的低權限帳號，不要用 root。
- 加進 `docker` 群組等於有 root 權限；較安全的做法是用 sudoers `Cmnd_Alias` 只開 `docker ps`、`journalctl`（見參考資料的使用者管理筆記），腳本裡對應改成 `sudo docker ps ...`。

## 巡檢 subagent .claude/agents/host-checker.md

```markdown
---
name: host-checker
description: 巡檢主機、檢查伺服器是否異常時使用。只讀取狀態，不做任何修改。
tools: Bash, Read
model: sonnet
---
你是唯讀的主機巡檢員。

1. 只能執行 ./scripts/fleet_check.sh 收集資料，再用 Read 讀輸出檔。
   不得重啟服務、刪檔、改設定。
2. 判斷基準：
   - 磁碟 ≥85% 警告、≥95% 嚴重
   - 容器 Restarting / Exited / unhealthy
   - systemd 有 failed unit
   - 近 1 小時有 err 等級日誌、有 OOM
   - SSH 連不上
3. 找出連鎖原因，例如：磁碟滿 → 資料庫寫不進去 → 容器重啟 → 其他服務 DNS 解析失敗。
4. 輸出：
   - 每台一列：主機｜正常/警告/嚴重｜異常項目｜可能原因｜建議處置
   - 建議處置只寫指令，不執行
   - 最後列出需要人工處理的項目
```

## 權限鎖定 .claude/settings.json

```json
{
  "permissions": {
    "allow": ["Bash(./scripts/fleet_check.sh *)"],
    "deny": ["Bash(ssh *)", "Bash(sudo *)", "Bash(rm *)"]
  }
}
```

- `deny` 擋的是 Claude **直接下**的指令：Claude 不能自己 `ssh` 進主機跑任意指令；腳本內部的 ssh 不受影響。
- 只靠 subagent 的 `tools` 欄位不夠細：`tools` / `disallowedTools` 是整個工具開或關（例如寫 `Bash(git push *)` 會把整個 Bash 拿掉），要限制「Bash 只能跑哪些指令」得靠 settings 的 permissions。

## subagent 常用欄位

| 欄位 | 必填 | 說明 |
|---|---|---|
| `name` | 是 | subagent 名稱，例如 `host-checker`（不能有 `:`） |
| `description` | 是 | 什麼情況要交給它；Claude 依這段決定要不要自動委派 |
| `tools` | 否 | 允許的工具（白名單），例如 `Bash, Read`；不寫就繼承全部 |
| `disallowedTools` | 否 | 禁用的工具（黑名單） |
| `model` | 否 | `sonnet` / `opus` / `haiku` / `inherit` 或完整 model ID |
| `permissionMode` | 否 | `default`、`plan`、`dontAsk` 等 |
| `maxTurns` | 否 | 最多跑幾輪，避免無限執行 |

放置位置：專案層 `.claude/agents/`（隨 repo 共用）、使用者層 `~/.claude/agents/`（所有專案可用）。

# 指令

## 手動檢查

在專案目錄開 `claude`：

```
用 host-checker 檢查 web1 web2 db1
```

## 主機多時平行檢查

讓主 Claude 拆組、派多個 subagent 平行跑，每個只回傳摘要，主對話不會被大量輸出塞滿：

```
把 hosts.txt 的主機分成 3 組，各派一個 host-checker 平行檢查，最後彙整成一張表
```

## 定期自動跑（cron + claude -p）

`claude -p` 是非互動模式，跑完印出結果就結束：

```bash
0 * * * * cd /opt/fleet && claude -p "用 host-checker 檢查 hosts.txt 所有主機，只回報異常" \
  --allowedTools "Bash(./scripts/fleet_check.sh *)" "Read" >> /var/log/fleet_check.log 2>&1
```

- 跑 cron 的機器要先登入 Claude Code，或設定 `ANTHROPIC_API_KEY` 環境變數；金鑰不要寫進腳本或 repo。
- 要推通知（Telegram 等）：在 cron 後面接一段腳本，結果有「嚴重」就呼叫通知 API。

# 什麼時候不用 Claude

| 需求 | 建議 |
|---|---|
| 單純門檻告警（磁碟 >90%、服務掛了就通知） | 腳本或監控工具（New Relic、Uptime Kuma、Prometheus）就夠，便宜又穩定 |
| 跨主機判讀、找連鎖原因、看日誌猜根因 | 適合用 Claude：例如「DNS 解析失敗」真正原因其實是磁碟滿導致資料庫容器重啟 |

兩者可以搭配：監控工具負責即時告警，Claude 巡檢負責定期總結與根因分析。
