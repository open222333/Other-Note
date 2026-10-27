# PHP 資安漏洞紀錄(CVE與緊急應變)

```
記錄 PHP 核心與生態系的重大漏洞、受影響版本與應變方式。
最後更新：2026-10-21（台灣時間 15:00）。2026-10 雙 RCE 的修補預定 10-21 22:00（台灣時間）釋出，
CVE 編號、受影響版本、修補版本在官方公告前都屬「待確認」，釋出後要回來更新本篇。
```

## 目錄

- [PHP 資安漏洞紀錄(CVE與緊急應變)](#php-資安漏洞紀錄cve與緊急應變)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [2026-10 PHP 核心雙 RCE（CVE-2026-109618 / CVE-2026-109619）](#2026-10-php-核心雙-rcecve-2026-109618--cve-2026-109619)
  - [摘要](#摘要)
  - [時間軸](#時間軸)
  - [已知與待確認](#已知與待確認)
  - [影響範圍與支援分支](#影響範圍與支援分支)
  - [應變清單](#應變清單)
- [2026 年其他 PHP 漏洞與安全更新](#2026-年其他-php-漏洞與安全更新)
- [歷史重大漏洞](#歷史重大漏洞)
- [指令](#指令)
  - [盤點 PHP 版本與執行模式](#盤點-php-版本與執行模式)
  - [偵測可疑子程序（無 EDR 的 Linux）](#偵測可疑子程序無-edr-的-linux)
  - [入侵跡象檢查](#入侵跡象檢查)
  - [php-cgi 緩解（cgi.fix\_pathinfo）](#php-cgi-緩解cgifix_pathinfo)

## 參考資料

[CyberQ - PHP 核心雙 RCE 漏洞提前外洩！企業偵測與緊急應變指南（2026-10-21）](https://cyberq.tw/2026/10/21/two-cvss-92-rce-vulnerabilities/)

[PHP 官方 - Upcoming Critical PHP Security Release（2026-10-20）](https://www.php.net/archive/2026.php#2026-10-20-1)

[BleepingComputer - PHP warns admins of two critical flaws, emergency patches coming tomorrow](https://www.bleepingcomputer.com/news/security/php-warns-admins-of-two-critical-flaws-emergency-patches-coming-tomorrow/)

[watchTowr - PHP Remote Code Execution FAQ](https://watchtowr.com/intelligence/php-cve-2026-109618-faq/)

[Sansec - Critical PHP RCE: CVE-2026-109618 and CVE-2026-109619](https://sansec.io/research/php2shell)

[Mallory - PHP's Critical RCE Fixes Skip 884,000 Internet-Facing Hosts](https://mallory.ai/blog/php-rce-rapid-response)

[Huntress - PHP2Shell: Leaked Critical PHP Vulnerabilities](https://www.huntress.com/blog/php2shell-leaked-critical-php-vulnerabilities)

[Florian Roth - process-watcher（本次事件設定說明）](https://github.com/Neo23x0/process-watcher/blob/main/emerging-threats/php-rce-october-26/README.md)

[PHP 官方 - Supported Versions（各分支支援期限）](https://www.php.net/supported-versions.php)

[PHP 官方 - ChangeLog（各版本修補的 CVE）](https://www.php.net/ChangeLog-8.php)

[oss-security - PHP 8.5.9 / 8.4.24 / 8.3.33 / 8.2.33 安全更新（2026-07-30）](https://www.openwall.com/lists/oss-security/2026/07/30/29)

[Pantheon - PHP 8.2/8.3/8.4/8.5 security updates（2026-09）](https://docs.pantheon.io/release-notes/2026/09/php-82-83-84-85-security-updates)

[PHP 筆記](PHP_筆記.md)

# 2026-10 PHP 核心雙 RCE（CVE-2026-109618 / CVE-2026-109619）

## 摘要

| 項目 | 內容 |
|---|---|
| 類型 | PHP 核心的遠端程式碼執行（RCE），兩個漏洞 |
| 嚴重度 | 官方：CVSS v4.0 **9.2**（兩者）；外洩貼文稱 10.0，以官方為準 |
| 狀態（2026-10-21 15:00 台灣時間） | 修補**尚未釋出**；預定 10-21 22:00 台灣時間（14:00 UTC）；野外攻擊尚未觀察到；未列入 CISA KEV |
| 特殊之處 | 協調揭露前資訊先外洩，PHP 官方被迫提前約一天釋出；修補前**沒有官方確認的緩解方式** |
| 預期風險 | Sansec 預估修補釋出後 15 分鐘內就會出現大規模攻擊；外流工具名為 PHP2Shell，「2shell」通常代表一個請求就能拿到 shell（無需驗證） |

## 時間軸

時間為 UTC（台灣時間 +8）。

| 時間 | 事件 |
|---|---|
| 10-20 15:20 | Hacker News 貼文宣稱兩個 CVSS 10.0 PHP RCE 將於 10-22 揭露，影響 PHP 5 以後所有版本；貼文被刪但截圖已流傳 |
| 10-20 下午 | Florian Roth：漏洞在 PHP 核心，只有 PHP 8 會獲得修補；watchTowr 證實外洩屬實並已重現 CVE-2026-109618 |
| 10-20 下午 | PHP 官方發布預告，排定 10-21 14:00 UTC 釋出 |
| 10-20 19:35 | watchTowr FAQ：尚無野外攻擊、未列入 CISA KEV、修補前無因應措施 |
| 10-20 22:15 | Mallory 曝險分析：約 88 萬台對外主機執行已停止支援的 PHP |
| **10-21 14:00** | **PHP 官方預定釋出修補與完整細節** |

## 已知與待確認

| 項目 | 目前資訊 | 狀態 |
|---|---|---|
| CVE 編號 | CVE-2026-109618、CVE-2026-109619 | watchTowr 證實；官方公告未提及；cve.org 仍為 Reserved |
| 漏洞位置 | PHP 核心 | 研究人員說法，官方待確認 |
| 受影響版本 | 外洩內容稱 PHP 5、7、8 全受影響（Sansec：5.4.0 以後） | 待官方確認 |
| 修補版本 | **8.5.12、8.4.27、8.3.36、8.2.35**（研究社群引述） | 單一來源，以官方公告為準 |
| 臨時緩解 | 無官方確認；Sansec 文章關鍵字出現 `session.upload_progress.enabled`、`session.use_strict_mode`，暗示可能與 session 機制有關 | **推測，官方細節公布前不建議據此改設定** |
| 技術細節 / PoC | 未公開 | 修補後預期數小時內出現 |

## 影響範圍與支援分支

PHP 官方只維護 8.2 以後的分支，**8.1 以前不會有官方修補**；8.2 的安全支援到 2026-12-31。

| 分支 | 對外主機數（Mallory，依 Shodan X-Powered-By 統計） | 官方修補 |
|---|---|---|
| 8.2 ～ 8.5 | 612,269 | 有 |
| 8.0、8.1 | 185,789 | 無（已停止支援） |
| 7.x | 329,358 | 無 |
| 5.x | 368,570 | 無 |

- 約 59%（883,717 台）回報版本的對外主機拿不到官方修補；另有約 74 萬台隱藏版本號未納入統計。
- **用 Linux 發行版套件安裝 PHP**（Debian、Ubuntu、RHEL）：發行版可能把修補回移到舊版本，版本號不一定會變成 8.x.12 這類號碼，要以**發行版的安全公告**為準。
- 共享主機（cPanel、Plesk）要等主機商派送更新。
- 容易漏掉的 PHP：廠商設備或 Windows 軟體內建的 `php.exe` / `php-cgi.exe`、Docker 映像檔裡的 PHP。

## 應變清單

**修補釋出前**

- 盤點所有 PHP：版本、執行模式（PHP-FPM / mod_php / php-cgi）、安裝來源（官方 / 發行版套件 / Docker 映像 / 廠商內建），見「指令 → 盤點」。
- 8.2 以上：預排 10-21 22:00（台灣時間）後的緊急更新時段，對外系統優先。
- 8.1 以下：沒有修補可等。升級到受支援分支，或用 VPN、IP 白名單、WAF 限制存取，必要時先下線。
- 詢問主機商、設備廠商的更新時程。
- 準備偵測：重點是 PHP 程序產生異常子程序（見「指令 → 偵測」）。
- 關閉版本號外洩：`php.ini` 設 `expose_php = Off`，避免被掃描器直接篩出可攻擊版本。

**修補釋出後**

- 8.2 ～ 8.5 立即更新到官方公告的修補版本；發行版套件確認已有回移修補。
- Docker：更新基底映像（例如 `php:8.3-fpm`）並**重建、重新部署**容器，只 pull 不重建不會生效。
- 更新後重啟 PHP-FPM / Apache，確認 `php -v` 與實際執行中的版本一致。
- 監看網頁根目錄的新檔案、網頁伺服器的異常子程序、新增的 cron / 帳號（見「指令 → 入侵跡象檢查」）。
- 發現入侵跡象：隔離主機、保留網頁伺服器與 PHP 日誌，再清查 webshell。

**流程面（ISO 27001 / ITGC 稽核）**：資產清冊是否涵蓋內嵌 PHP、緊急修補是否有預先核准的流程、停止支援的 PHP 是否列入風險登錄並有補償控制。

# 2026 年其他 PHP 漏洞與安全更新

| 時間 | 事件 | 重點 |
|---|---|---|
| 2026-09-24 | PHP 8.5.11、8.4.26、8.3.35、8.2.34 | 2026 年第四輪安全更新；ChangeLog 列 11 個 CVE，最高為 CVE-2026-91765（CVSS v3.1 7.5，對外的 SoapServer 端點可被弄到程序崩潰） |
| 2026-09 | Comment2Shell（CVE-2026-93485） | WordPress 從 XSS 串接到 RCE 的攻擊鏈 |
| 2026-08-22 | php-src PR #23411 | 修補 php-cgi 的問題，與 2019 年的 CVE-2019-11043 高度相似；截至 10-20 尚未合併，**未確認可利用、未確認與本次 RCE 相關**（緩解見「指令 → php-cgi 緩解」） |
| 2026-07-30 | PHP 8.5.9、8.4.24、8.3.33、8.2.33 | 含 libgd 升級（CVE-2026-9672）、pgsql 擴充以 `E'...'` 反斜線跳脫造成的 SQL injection（CVE-2026-17543） |
| 2026-07-18 | wp2shell（CVE-2026-63030、CVE-2026-60137） | WordPress 無需驗證的 RCE 攻擊鏈；Calif 宣稱已重現（Calif 也是本次外洩工具 PHP2Shell 的署名來源） |
| 2026-07 | CVE-2026-12184、CVE-2026-14355（彙整網站資料，待核對官方 ChangeLog） | PHP-FPM 在 TLS 初始化失敗時的遠端 DoS；OpenSSL 擴充的記憶體損壞 |
| 2026-07-02、05-07 | 第二、第一輪安全更新 | 版本與 CVE 見官方 ChangeLog |
| 2026-05 前後 | Calif「MAD Bugs」研究 | `unserialize()` 的 use-after-free，宣稱以約 2,000 個 HTTP 請求在 PHP 8.5.5 取得 shell；CVE 編號待查 |

趨勢：2026 下半年攻擊研究從應用層（WordPress 外掛、主題）移向 PHP 直譯器核心；核心漏洞不分應用種類，需要清查的範圍大得多。

# 歷史重大漏洞

| CVE | 年份 | 內容 | 教訓 |
|---|---|---|---|
| CVE-2024-4577 | 2024 | 台灣 DEVCORE 發現：**Windows** 上 PHP-CGI 的參數注入 RCE：在繁中、簡中、日文等語系下，Windows 的字元轉換（Best-Fit）讓攻擊者繞過 CVE-2012-1823 的修補；公開後迅速遭大規模利用 | 台灣常見的繁中 Windows + XAMPP 環境首當其衝；php-cgi 模式風險高 |
| CVE-2019-11043 | 2019 | PHP-FPM 在 nginx 特定設定（`fastcgi_split_path_info`）下的 RCE，公開後迅速遭大規模掃描 | 核心漏洞從公開到野外攻擊非常快 |
| CVE-2012-1823 | 2012 | php-cgi 把查詢字串當命令列參數，造成 RCE；技術細節意外先公開，官方緊急修補 | 與本次一樣是「細節先外流、官方被迫緊急修補」 |

# 指令

## 盤點 PHP 版本與執行模式

```bash
# CLI 版本與 SAPI（cli / fpm-fcgi / cgi-fcgi / apache2handler）
php -v
php -r 'echo PHP_VERSION, " ", PHP_SAPI, PHP_EOL;'

# 主機上所有 PHP 執行檔（可能同時裝多個版本）
which -a php php-cgi php-fpm 2>/dev/null
ls /usr/bin/php* /usr/sbin/php-fpm* 2>/dev/null

# 執行中的 PHP 程序：判斷是 FPM、php-cgi 還是 mod_php（Apache）
ps -eo pid,user,args | grep -E 'php-fpm|php-cgi|apache2|httpd' | grep -v grep

# 發行版套件版本（看回移修補要比對這個，不是 php -v）
dpkg -l | grep -E '^ii\s+php'          # Debian / Ubuntu
rpm -qa | grep -i php                    # RHEL / CentOS / Rocky

# Docker：哪些容器用 PHP 映像，以及容器內的實際版本
docker ps --format '{{.Names}}\t{{.Image}}' | grep -i php
docker exec <容器名稱> php -v

# 網站是否對外洩漏 PHP 版本
curl -sI https://example.com | grep -i x-powered-by
```

## 偵測可疑子程序（無 EDR 的 Linux）

RCE 被利用時，通常會表現為 PHP 程序（php-fpm、php-cgi、apache2、httpd）產生 shell 或下載工具等子程序。

```bash
# 列出 PHP / Apache 程序底下的 shell、直譯器、網路工具子程序；有輸出就要調查
for p in $(pgrep -x 'php-fpm[0-9.]*|php-cgi[0-9.]*|apache2|httpd'); do
  ps --ppid "$p" -o pid=,user=,comm=,args=
done | grep -E '\s(sh|bash|dash|zsh|python[0-9.]*|perl|curl|wget|nc|ncat|socat)\s'
```

- `pgrep -x` 比對程序名稱，不會誤抓到自己這行指令；php-fpm 正常的 worker 子程序不會被列出。
- 也要留意命令列含 `/dev/tcp/`、`fsockopen`、`mkfifo`（反向 shell），或 `whoami`、`id`、`uname -a` 這類偵察指令。
- 這只是單次快照，要持續監控可用 Florian Roth 的 process-watcher（參考資料），建議先用觀察模式。

## 入侵跡象檢查

```bash
# 網頁根目錄最近 24 小時新增 / 修改的 PHP 檔（依實際路徑調整）
find /var/www -name '*.php' -mmin -1440 -ls

# 常見 webshell 特徵（會有誤判，結果要人工確認）
grep -rlE 'eval\s*\(\s*(base64_decode|gzinflate|\$_(POST|GET|REQUEST))' /var/www --include='*.php'

# 新增的排程與帳號
ls -la /etc/cron.* /var/spool/cron/crontabs 2>/dev/null
crontab -l -u www-data 2>/dev/null
grep -E ':/bin/(ba)?sh$' /etc/passwd
```

## php-cgi 緩解（cgi.fix_pathinfo）

只在**三個條件同時成立**時需要處理（對應 PR #23411 的 php-cgi 問題）：PHP 以 php-cgi（不是 FPM）執行、`cgi.fix_pathinfo = 1`（預設）、網頁伺服器會轉送 `PATH_INFO`（例如 nginx 用 `fastcgi_split_path_info`）。

```bash
# 確認目前值
php-cgi -i 2>/dev/null | grep -i cgi.fix_pathinfo
php --ini                                  # 找出實際載入的 php.ini
```

```ini
; php.ini（先在測試環境確認網站路由正常再上線）
cgi.fix_pathinfo = 0
```

```nginx
# 或在 nginx 的 PHP location 加上：檔案不存在就直接 404，不交給 PHP
location ~ \.php$ {
    try_files $fastcgi_script_name =404;
    # ... 其餘 fastcgi 設定
}
```

根本解法是改用 PHP-FPM。
