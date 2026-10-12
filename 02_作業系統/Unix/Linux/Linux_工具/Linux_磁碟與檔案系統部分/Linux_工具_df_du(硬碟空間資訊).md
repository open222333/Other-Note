# Linux 工具 df du(硬碟空間資訊)

```
磁碟的整體資料是在 superblock 區塊中，但是每個各別檔案的容量則在 inode 當中記載的。
那在文字介面底下該如何叫出這幾個資料

df：列出檔案系統的整體磁碟使用量；
du：評估檔案系統的磁碟使用量(常用在推估目錄所佔容量)
```

## 目錄

- [Linux 工具 df du(硬碟空間資訊)](#linux-工具-df-du硬碟空間資訊)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [例外狀況](#例外狀況)
	- [磁碟滿了（Use% 100%）排查與清理](#磁碟滿了use-100排查與清理)
		- [常見連鎖症狀](#常見連鎖症狀)
		- [Step 0：確認狀況](#step-0確認狀況)
		- [Step 1：先清可重建的東西](#step-1先清可重建的東西)
		- [Step 2：暫停寫入來源](#step-2暫停寫入來源)
		- [Step 3：用 ext4 保留區讓服務先恢復](#step-3用-ext4-保留區讓服務先恢復)
		- [Step 4：找出大目錄並清理資料](#step-4找出大目錄並清理資料)
		- [Step 5：Docker 與其他佔用](#step-5docker-與其他佔用)
		- [Step 6：恢復保留區、重啟服務](#step-6恢復保留區重啟服務)
		- [預防再次塞滿](#預防再次塞滿)

## 參考資料

[df(1) - Linux man page](https://linux.die.net/man/1/df)

[du(1) — Linux manual page](https://man7.org/linux/man-pages/man1/du.1.html)

[7.2.1 磁碟與目錄的容量： df, du - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0230filesystem.php#df)

[Docker 筆記（docker system df / prune、容器 log 清理）](../../../../../00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Docker(開放平臺軟體)/Docker_筆記.md)

[journalctl 筆記（--vacuum-size 清理日誌）](../Linux_程序(Process)部分/Linux_工具_journalctl(1)(檢視和查詢_systemd_日誌信息).md)

# 指令

```bash
# 列出檔案系統的整體磁碟使用量
df [-ahikHTm] [目錄或檔名]
	-a  ：列出所有的檔案系統，包括系統特有的 /proc 等檔案系統；
	-k  ：以 KBytes 的容量顯示各檔案系統；
	-m  ：以 MBytes 的容量顯示各檔案系統；
	-h  ：以人們較易閱讀的 GBytes, MBytes, KBytes 等格式自行顯示；
	-H  ：以 M=1000K 取代 M=1024K 的進位方式；
	-T  ：連同該 partition 的 filesystem 名稱 (例如 xfs) 也列出；
	-i  ：不用磁碟容量，而以 inode 的數量來顯示

# 輸出容易閱讀和理解的
df -h
Filesystem      Size  Used   Avail Use% Mounted on
/dev/sda6       29G   4.2G   23G   16%     /
udev            1.5G  4.0K   1.5G   1%     /dev
tmpfs           604M  892K   603M   1%     /run
none            5.0M     0   5.0M   0%     /run/lock
none            1.5G  156K   1.5G   1%     /run/shm

# 顯示所有的信息
df --total
Filesystem     1K-blocks    Used    Available Use% Mounted on
/dev/sda6       29640780 4320720    23814372  16%     /
udev             1536756       4    1536752   1%      /dev
tmpfs             617620     892    616728    1%      /run
none                5120       0    5120      0%      /run/lock
none             1544044     156    1543888   1%      /run/shm
total           33344320 4321772    27516860  14%

# 評估檔案系統的磁碟使用量(常用在推估目錄所佔容量)
du [-ahskm] 檔案或目錄名稱
	-a  ：列出所有的檔案與目錄容量，因為預設僅統計目錄底下的檔案量而已。
	-h  ：以人們較易讀的容量格式 (G/M) 顯示；
	-s  ：列出總量而已，而不列出每個各別的目錄佔用容量；
	-S  ：不包括子目錄下的總計，與 -s 有點差別。
	-k  ：以 KBytes 列出容量顯示；
	-m  ：以 MBytes 列出容量顯示；
```

# 例外狀況

## 磁碟滿了（Use% 100%）排查與清理

磁碟滿時，資料庫、Docker、log 都可能寫入失敗。處理順序：確認狀況 → 暫停寫入 → 騰出空間讓服務恢復 → 找出佔用並清理 → 恢復設定 → 加上預防。

### 常見連鎖症狀

```
資料庫容器（如 MongoDB）寫 journal 時 No space left on device
→ 容器 crash，進入重啟循環（restart: always 會無限重啟）
→ 其他容器連它時出現 name resolution 失敗 / no route to host
  （容器一直在重啟，Docker 內部 DNS 解析不到該服務名稱）
```

看到「連不到資料庫、DNS 解析失敗」不要只查網路，先看磁碟與容器狀態。

### Step 0：確認狀況

```bash
df -hT /                                         # 用量與檔案系統類型（ext4 / xfs）
tune2fs -l /dev/md0 | grep -E "Reserved block count|Free blocks|Block size"
docker ps -a --format '{{.Names}}\t{{.Status}}'  # 找 Restarting 的容器
docker inspect -f '{{.Name}} 重啟次數={{.RestartCount}}' $(docker ps -aq)
docker logs --tail 20 <容器名稱> 2>&1 | grep "No space left"
```

⚠ `tune2fs -l` 的 `Free blocks` 是 superblock 裡的數值，檔案系統**掛載中不會即時更新**，看剩餘空間一律以 `df` 為準；`Reserved block count` 則是準的，可以照抄下來備用。

### Step 1：先清可重建的東西

```bash
journalctl --vacuum-size=200M     # systemd 日誌，詳見 journalctl 筆記
docker builder prune -f           # build cache
apt-get clean                     # RedHat 系：yum clean all / dnf clean all
```

通常只能清出幾 GB，大容量磁碟不夠用，但先做。

### Step 2：暫停寫入來源

暫停會持續寫檔的服務（爬蟲、上傳、備份排程），否則清出來的空間馬上又被吃掉。

### Step 3：用 ext4 保留區讓服務先恢復

ext4 預設保留 5% 給 root（15T 約 750G，可能已被調整過，以 `tune2fs -l` 實際值為準）。

**關鍵**：容器內的服務多半不是用 root 跑（例如 MongoDB 官方映像是 uid 999），**用不到保留區**。只要實際剩餘空間小於保留區，`df` 的 Avail 對一般使用者就是 0——這時清掉幾 GB 也沒用，服務照樣寫不進去。

```bash
# 1. 先記下原本的保留 block 數，之後要還原（例如 39069465 blocks ≈ 149 GB）
tune2fs -l /dev/md0 | grep "Reserved block count"

# 2. 暫時降低保留區；若剩餘空間比 0.1% 還小，就必須直接歸零
tune2fs -m 0 /dev/md0

# 3. 重啟服務，確認是 Up 而不是 Restarting
docker restart <資料庫容器>
docker ps --filter name=<資料庫容器>
```

XFS 沒有保留區可調，直接進 Step 4。

### Step 4：找出大目錄並清理資料

```bash
# -x 只看同一個檔案系統，避免掃到 /proc、其他掛載點
du -xh --max-depth=1 / 2>/dev/null | sort -h | tail -20
du -xh --max-depth=2 /srv /home /data /var 2>/dev/null | sort -h | tail -30

# 有裝 ncdu 會更方便：互動瀏覽，按 d 刪除
ncdu -x /

# 跑檔案伺服器類容器（如 filebrowser）時，查容器實際掛載的資料目錄
docker inspect $(docker ps -q) --format '{{.Name}} {{range .Mounts}}{{.Source}} {{end}}'
```

刪業務資料的原則：

- **先預覽、再刪除**：清理腳本先跑不刪檔的列表模式，確認筆數與容量再加刪除參數。
- **從釋放量最大的批次先刪**，盡快把空間拉回來，不要從最舊、最小的開始。
- 確認刪除條件會漏掉什麼（例如時間欄位為 null 的檔案不會被選到）、有沒有異地備份（刪了就沒了）。
- 多行指令分開貼上執行，兩行黏在一起會變成 `unrecognized arguments`。

### Step 5：Docker 與其他佔用

```bash
docker system df -v      # images / containers / volumes / build cache 各佔多少
docker image prune -a    # 沒在用的 image
docker volume ls -f dangling=true   # 先確認內容，再 docker volume prune（volume 可能有資料）

# 容器 log 常常很大，用 truncate 清空，不要 rm
du -sh /var/lib/docker/containers/*/*-json.log | sort -h | tail
truncate -s 0 /var/lib/docker/containers/<id>/<id>-json.log

# 檔案刪了但 process 還開著，空間不會釋放；看到大的 (deleted) 檔就重啟該 process / 容器
lsof +L1 2>/dev/null | sort -k7 -n | tail
```

log 上限設定與更多清理方式，見 [Docker 筆記 → log造成硬碟沒有空間](../../../../../00_其他(工具_應用程式_網站)/DevOps(Development和Operations的組合詞)/Docker(開放平臺軟體)/Docker_筆記.md#log造成硬碟沒有空間)。

### Step 6：恢復保留區、重啟服務

保留區歸零只是緊急手段，空間清出來後**一定要加回**，否則下次塞滿時 root 也沒有餘裕登入處理、系統服務（syslog、journald 等）也會跟著寫不進去。

加回的時機：`df` 的可用空間要**遠大於**保留區大小。加回後非 root 服務的可用空間會減少「保留區」那麼多，若剩餘空間不夠大，加回當下服務又會寫不進去（例如剩 4.5 TB、保留區 149 GB → 安全；剩 100 GB、保留區 149 GB → 不能加回，繼續清）。

```bash
# 1. 確認剩餘空間遠大於保留區（以 df 為準，不要看 tune2fs 的 Free blocks）
df -h /

# 2. 用 Step 3 記下的 block 數還原
tune2fs -r <原本的保留 block 數> /dev/md0     # 例如 tune2fs -r 39069465 /dev/md0

# 3. 確認保留區已恢復、服務正常
tune2fs -l /dev/md0 | grep "Reserved block count"
df -h / && docker ps -a --format '{{.Names}}\t{{.Status}}'   # Avail 少了約保留區大小；沒有容器在 Restarting

# 4. 重啟其他受影響的容器
docker restart <其他受影響的容器>
```

- 用 `-r`（block 數）還原比 `-m`（百分比）精準，原本若不是整數百分比也能完整還原。
- `tune2fs -r` / `-m` 可以在掛載中直接執行，不用卸載、不用重開機，立即生效。
- 換算容量：保留 block 數 × Block size（`tune2fs -l` 的 `Block size`，通常 4096）。例如 39,069,465 × 4096 = 160,028,528,640 bytes ≈ 149 GiB（`df -h` 顯示的 G 是 GiB，十進位約 160 GB）。
- 檢查資料庫：磁碟滿的期間可能寫入失敗，確認資料完整性。
- 恢復 Step 2 暫停的寫入來源。

### 預防再次塞滿

| 措施 | 做法 |
|---|---|
| 磁碟用量告警 | 排程檢查 `df`，超過 85% / 95% 發通知（Telegram、Email 等） |
| 自動保留期限 | 定期清理已確認備份的舊資料，不要等滿了才大量刪除 |
| 資料庫與大量檔案分開放 | 資料庫目錄放另一個分割區或設 quota；同一顆碟一滿，資料庫和清理工具會一起掛 |
| 容器健康監控 | 加 healthcheck，重啟次數異常時告警（重啟循環可能好幾週沒人發現） |
| Docker 日誌上限 | `/etc/docker/daemon.json` 設 `"log-opts": {"max-size": "50m", "max-file": "3"}` |
