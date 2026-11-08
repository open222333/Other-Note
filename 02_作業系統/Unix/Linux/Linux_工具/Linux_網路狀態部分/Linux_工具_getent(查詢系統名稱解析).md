# Linux 工具 getent(查詢系統名稱解析)

```
getent（get entries）透過 NSS（Name Service Switch）查詢系統資料庫：hosts、passwd、group、services 等。
查主機名稱時，走的是和應用程式（mysql、curl、PHP…）相同的解析流程，
會依 /etc/nsswitch.conf 的順序考慮 /etc/hosts、DNS、本機快取（systemd-resolved / nscd），
比 dig 更接近「程式實際連線時看到的結果」。
```

## 目錄

- [Linux 工具 getent(查詢系統名稱解析)](#linux-工具-getent查詢系統名稱解析)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [getent 與 dig 的差別](#getent-與-dig-的差別)
- [指令](#指令)
  - [查主機名稱（hosts / ahosts）](#查主機名稱hosts--ahosts)
  - [查其他資料庫](#查其他資料庫)
  - [在腳本中使用](#在腳本中使用)
- [例外狀況](#例外狀況)
  - [getent hosts 只顯示 IPv6，看不到 IPv4](#getent-hosts-只顯示-ipv6看不到-ipv4)
  - [getent 和 dig 結果不一樣](#getent-和-dig-結果不一樣)

## 參考資料

[getent(1) - Linux manual page](https://man7.org/linux/man-pages/man1/getent.1.html)

[nsswitch.conf(5) - Linux manual page](https://man7.org/linux/man-pages/man5/nsswitch.conf.5.html)

[dig 筆記](./Linux_工具_dig(DNS偵錯工具).md)

[Linux 網路狀態部分 總覽](./Linux_網路狀態部分_總覽.md)

[Linux 使用者管理（getent group）](../../Linux_基本_使用者管理(Linux指令).md)

# 安裝

- Linux 內建（glibc 的一部分，Debian / Ubuntu 在 `libc-bin`、RedHat 系在 `glibc-common`），不用另外安裝。
- macOS 沒有 `getent`，查主機解析改用：

```bash
dscacheutil -q host -a name example.com
```

# getent 與 dig 的差別

| 項目 | `getent` | `dig` |
|---|---|---|
| 查詢方式 | 走 NSS，和應用程式相同 | 直接問 DNS 伺服器 |
| `/etc/hosts` | 會考慮 | 不看 |
| 本機快取（systemd-resolved / nscd） | 會考慮 | 不經過（除非 DNS 伺服器就是 `127.0.0.53`） |
| 指定 DNS 伺服器 | 不行 | 可以（`dig @8.8.8.8`） |
| 顯示 TTL、記錄類型、權威伺服器 | 不會 | 會 |
| 適合 | 確認「程式會連到哪個 IP」 | 排查 DNS 記錄本身、確認 DNS 是否生效 |

排查順序：程式連錯主機 → 先 `getent ahosts` 看程式實際解析結果 → 和 `dig` 不一致就查 `/etc/hosts` 或快取。

# 指令

## 查主機名稱（hosts / ahosts）

```bash
# 只顯示一筆（有 IPv6 時通常顯示 IPv6）
getent hosts db.example.com

# 全部位址（IPv4 + IPv6，每個位址會依 socket 類型重複列出）
getent ahosts db.example.com

# 只看 IPv4 / 只看 IPv6
getent ahostsv4 db.example.com
getent ahostsv6 db.example.com

# 反查 IP
getent hosts 203.0.113.10
```

`ahosts` 輸出範例（同一個位址會依 STREAM / DGRAM / RAW 各列一次）：

```
2001:db8::10    STREAM db.example.com
2001:db8::10    DGRAM
2001:db8::10    RAW
203.0.113.10    STREAM
203.0.113.10    DGRAM
203.0.113.10    RAW
```

只要不重複的 IPv4 清單：

```bash
getent ahostsv4 db.example.com | awk '{print $1}' | sort -u
```

## 查其他資料庫

```bash
getent passwd tom          # 使用者（含 LDAP / SSSD 等外部來源）
getent group docker        # 群組與成員
getent services 3306       # port 對應的服務名稱
getent protocols tcp
getent networks
```

查看各資料庫的查詢來源與順序：

```bash
grep -E '^(hosts|passwd|group):' /etc/nsswitch.conf
# hosts: files dns   ← 先查 /etc/hosts，再查 DNS
```

## 在腳本中使用

結束碼：`0` 找到、`2` 找不到該筆資料，適合拿來做判斷：

```bash
if getent hosts "$DB_HOST" > /dev/null; then
  echo "解析成功"
else
  echo "無法解析 $DB_HOST" >&2
  exit 1
fi

# 取得第一個 IPv4
DB_IP=$(getent ahostsv4 "$DB_HOST" | awk 'NR==1{print $1}')
```

# 例外狀況

## getent hosts 只顯示 IPv6，看不到 IPv4

- `getent hosts` 只顯示一筆，主機有 AAAA 記錄時通常優先顯示 IPv6，不代表沒有 IPv4。
- 用 `getent ahosts`（全部）或 `getent ahostsv4`（只看 IPv4）確認。

## getent 和 dig 結果不一樣

依序檢查：

```bash
# 1. /etc/hosts 是否寫死了舊 IP
grep -n "db.example.com" /etc/hosts

# 2. NSS 查詢順序
grep '^hosts:' /etc/nsswitch.conf

# 3. 本機快取（systemd-resolved）
resolvectl query db.example.com
sudo resolvectl flush-caches
```

- DNS 剛改記錄時，`dig @權威伺服器` 已是新值，但本機快取或上游 DNS 還在 TTL 內，`getent` 仍會拿到舊值。
