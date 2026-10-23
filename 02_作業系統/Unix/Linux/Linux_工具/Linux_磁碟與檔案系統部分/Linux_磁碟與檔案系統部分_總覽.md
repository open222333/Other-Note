# Linux 磁碟與檔案系統部分 總覽

```
本目錄收錄 Linux 磁碟空間、I/O 監控、檔案系統掛載、遠端共享與備份同步工具。
```

## 目錄

- [Linux 磁碟與檔案系統部分 總覽](#linux-磁碟與檔案系統部分-總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [工具清單](#工具清單)
- [類型比較](#類型比較)
  - [遠端目錄方案比較](#遠端目錄方案比較)
- [各工具說明](#各工具說明)
- [需求選擇建議](#需求選擇建議)

## 參考資料

- [df / du](./Linux_工具_df_du(硬碟空間資訊).md)
- [iostat](./Linux_工具_iostat(監視磁盤活動).md)
- [mount](./Linux_工具_mount(檔案系統掛載).md)
- [NFS](./Linux_工具_nfs-utils_nfs-kernel-server(Network_File_System(NFS)遠端掛載).md)
- [rsync](./Linux_工具_rsync(同步鏡像備份).md)
- [sshfs](./Linux_工具_sshfs(掛載遠端主機的目錄).md)
- [swapon](./Linux_工具_swapon(查看Swap狀態).md)

---

# 工具清單

| 工具 | 類型 | 主要用途 | 筆記 |
|---|---|---|---|
| **df / du** | 空間 | 檔案系統用量、目錄佔用大小 | [df / du](./Linux_工具_df_du(硬碟空間資訊).md) |
| **iostat** | 監控 | CPU 與磁碟 I/O 統計 | [iostat](./Linux_工具_iostat(監視磁盤活動).md) |
| **mount** | 掛載 | 掛載檔案系統、`/etc/fstab` 開機自動掛載 | [mount](./Linux_工具_mount(檔案系統掛載).md) |
| **NFS** | 遠端共享 | 多台主機共用同一個目錄 | [NFS](./Linux_工具_nfs-utils_nfs-kernel-server(Network_File_System(NFS)遠端掛載).md) |
| **sshfs** | 遠端掛載 | 透過 SSH 把遠端目錄掛到本機 | [sshfs](./Linux_工具_sshfs(掛載遠端主機的目錄).md) |
| **rsync** | 同步 / 備份 | 增量同步、鏡像備份 | [rsync](./Linux_工具_rsync(同步鏡像備份).md) |
| **swapon** | Swap | 查看、啟用、停用 swap | [swapon](./Linux_工具_swapon(查看Swap狀態).md) |

---

# 類型比較

| 問題 | 工具 | 備註 |
|---|---|---|
| 磁碟滿了 | `df -h` → `du -sh` | df 看哪個分割區滿，du 找哪個目錄大 |
| df 和 du 數字對不上 | `lsof +L1` | 常見原因：已刪除但仍被程式開著的檔案、保留 block |
| 磁碟很慢 | iostat | 看 `%util`、`await` |
| 記憶體不夠 | swapon、`free -h` | 加 swap 只是緩衝，不能取代記憶體 |

## 遠端目錄方案比較

| 方案 | 協定 | 優點 | 缺點 | 適用 |
|---|---|---|---|---|
| **NFS** | NFS | 效能好、多台同時掛載 | 要架伺服器，預設不加密 | 內網多台共用目錄 |
| **sshfs** | SSH | 只要有 SSH 就能用、加密 | 效能較差，斷線要重掛 | 臨時存取遠端檔案 |
| **rsync** | SSH / rsync | 增量、可排程 | 不是即時共享，是複製 | 備份、部署、搬資料 |

---

# 各工具說明

- **df / du**：日常空間檢查；筆記含 ext4 保留 block 的處理方式。
- **iostat**：判斷瓶頸是否在磁碟 I/O，需安裝 sysstat。
- **mount**：掛載本機或 NFS；寫進 `/etc/fstab` 前先 `mount -a` 測試，避免開機卡住。
- **NFS**：伺服器端設定 `/etc/exports`，客戶端用 mount 掛載。
- **sshfs**：免架服務，適合開發機臨時存取。
- **rsync**：`--delete` 會刪除目標端多出的檔案，先用 `-n`（dry-run）確認。
- **swapon**：建立 swap 檔、確認啟用狀態。

---

# 需求選擇建議

| 情境 | 推薦工具 | 理由 |
|---|---|---|
| 找出佔空間的目錄 | du | `du -sh * \| sort -h` |
| 多台 Web 主機共用上傳目錄 | NFS | 同時掛載、效能好 |
| 開發時編輯遠端主機的檔案 | sshfs | 不需額外服務 |
| 定期備份到另一台主機 | rsync + cron | 增量傳輸、省頻寬 |
| 小記憶體 VPS 偶爾 OOM | swapon（加 swap 檔） | 快速緩解 |
| 懷疑磁碟 I/O 拖慢服務 | iostat | 看使用率與等待時間 |
