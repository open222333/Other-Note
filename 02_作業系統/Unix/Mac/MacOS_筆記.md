# MacOS 筆記

```
macOS 系統操作指令、環境變數設定、iCloud Drive 檔案管理與儲存空間排查筆記
```

## 目錄

- [MacOS 筆記](#macos-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [操作指令](#操作指令)
	- [iCloud Drive 檔案管理](#icloud-drive-檔案管理)
	- [狀況 System Data 佔用大量儲存空間](#狀況-system-data-佔用大量儲存空間)
- [Mac 環境變數](#mac-環境變數)
	- [狀況 Mac 一直進入安全模式](#狀況-mac-一直進入安全模式)

## 參考資料

[指令查詢](https://ss64.com/osx/)

## 操作指令

```bash
# 查看監聽的port
netstat -an | grep LISTEN

lsof -i -P -n | grep LISTEN

# 查看 ssh連線 進程
ps aux | grep ssh

# 修改主機名稱 指令
sudo scutil -—set pref HostName WorkMacBookPro
```

## iCloud Drive 檔案管理

iCloud Drive 為雙向同步：從 Finder 或終端機刪除本機檔案，等同刪除雲端那一份。要釋放本機空間但保留雲端內容，用「移除下載項目」而非刪除。

| 需求 | 做法 |
|---|---|
| 釋放本機空間、雲端保留 | Finder 右鍵 → `Remove Download`（移除下載項目），檔名旁出現雲朵圖示，雙擊可重新下載 |
| 交給系統自動移除本機副本 | System Settings → Apple Account → iCloud → `See All` → iCloud Drive → 開啟 `Optimize Mac Storage` |
| 本機留一份、與雲端脫勾 | 關閉 iCloud Drive 同步時選「保留副本」，家目錄產生 `iCloud Drive（歸檔）` 資料夾，之後兩邊各自獨立 |
| 誤刪還原 | iCloud.com → 帳號設定 → `Restore Files`，30 天內可回復 |

注意事項：

- `Optimize Mac Storage` 由系統決定移除哪些檔案，無法指定；要精準控制用 `Remove Download`。
- `Optimize Mac Storage` 只在 `Sync this Mac` 開啟時才會出現；較新版 macOS 也可能移到 System Settings → General → Storage。
- 開啟「桌面與文件檔案夾」同步後，`~/Desktop`、`~/Documents` 也屬於 iCloud 範圍，同樣規則適用。
- 移除下載只釋放本機硬碟，**不會減少 iCloud 儲存用量**；要省雲端空間必須真的刪檔。
- 勿用 `rm` 清理 iCloud 目錄，會被視為刪除並同步到雲端。

```bash
# 查看 iCloud 本機快取用量（已下載檔案有時被歸類到 System Data）
du -sh ~/Library/Application\ Support/CloudDocs
```

## 狀況 System Data 佔用大量儲存空間

`System Data`（舊稱「其他」）不是系統檔案，而是儲存空間頁面的未分類集合：快取、Time Machine 本機快照、log、swap、Docker 映像、開發者工具產物都算在這一格。

排查：

```bash
# 家目錄 Library 前 25 大目錄
du -xh -d 2 ~/Library 2>/dev/null | sort -hr | head -25

# 常見大型目錄逐一確認
du -sh ~/Library/Caches \
       ~/Library/Mail \
       ~/Library/Application\ Support/MobileSync/Backup \
       ~/Library/Application\ Support/CloudDocs 2>/dev/null
```

清理（依常見佔用量排序）：

```bash
# 1. Time Machine 本機快照：未接外接硬碟也會產生，常見 20~80GB
tmutil listlocalsnapshots /
sudo tmutil thinlocalsnapshots / 999999999999 4

# 2. Docker：Docker.raw 只會增長不會自動縮小
docker system df
docker system prune -a --volumes
ls -lh ~/Library/Containers/com.docker.docker/Data/vms/0/data/Docker.raw

# 3. Xcode / 模擬器產物
du -sh ~/Library/Developer/Xcode/DerivedData \
       ~/Library/Developer/Xcode/iOS\ DeviceSupport \
       ~/Library/Developer/CoreSimulator/Devices 2>/dev/null
rm -rf ~/Library/Developer/Xcode/DerivedData/*
xcrun simctl delete unavailable

# 4. Homebrew 快取
brew cleanup -s
rm -rf "$(brew --cache)"
```

- Docker 清理後 `Docker.raw` 仍過大 → Docker Desktop → Troubleshoot → `Reset to factory defaults`，會清空所有本機映像。
- `~/Library/Application Support/MobileSync/Backup` 為 iPhone 本機備份，單次 5~15GB。
- 清理完成後**重新開機**，`System Data` 數字才會正確更新；部分空間需重開才真正釋放。

# Mac 環境變數
```bash
# 列出環境變數
printenv

# 列出環境變數下的 Path
echo $PATH
```

新增環境變數

```bash
# 打開 bash profile
vi ~/.bash_profile

# 新增路徑到 PATH
export PATH=$PATH:路徑名稱
export PATH=$PATH:$HOME/bin/

# 執行 bash profile
source ~/.bash_profile
```

## 狀況 Mac 一直進入安全模式

解決方法：
```
重置NVRAM
```

開機時一直按著

	Command + Option + P + R

Mac會默默存取使用者的喜好設定

也就是`NVRAM` 以前叫做`PRAM`

NVRAM是 **非揮發性的隨機存取記憶體**`(Non-Volatile Random-Access Memory)`

