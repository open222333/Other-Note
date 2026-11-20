# Linux 基本 使用者管理

```
Linux 使用者與群組管理：帳號設定檔、新增 / 修改 / 刪除使用者與群組、密碼與帳號期限、
鎖定帳號、切換身分、sudo 權限（visudo、sudoers.d、Cmnd_Alias）。
```

> 適用版本：指令適用主流發行版；Debian / Ubuntu 的管理員群組是 `sudo`，RedHat 系（CentOS / Rocky / AlmaLinux）是 `wheel`

## 目錄

- [Linux 基本 使用者管理](#linux-基本-使用者管理)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [配置文檔](#配置文檔)
  - [/etc/passwd（帳號）](#etcpasswd帳號)
  - [/etc/shadow（密碼與期限）](#etcshadow密碼與期限)
  - [/etc/group（群組）](#etcgroup群組)
  - [/etc/login.defs（UID/GID 與密碼預設值）](#etclogindefsuidgid-與密碼預設值)
  - [/etc/default/useradd 與 /etc/skel（useradd 預設值）](#etcdefaultuseradd-與-etcskeluseradd-預設值)
- [指令](#指令)
  - [查詢使用者與群組](#查詢使用者與群組)
  - [useradd passwd (新增使用者)](#useradd-passwd-新增使用者)
    - [新增使用者範例](#新增使用者範例)
  - [usermod (修改使用者)](#usermod-修改使用者)
  - [userdel (刪除使用者)](#userdel-刪除使用者)
  - [passwd chage (密碼與帳號期限)](#passwd-chage-密碼與帳號期限)
  - [鎖定 / 停用帳號](#鎖定--停用帳號)
  - [groupadd groupmod groupdel gpasswd (群組管理)](#groupadd-groupmod-groupdel-gpasswd-群組管理)
  - [su sudo (切換身分)](#su-sudo-切換身分)
  - [visudo 設定使用者群組的指令限制與權限](#visudo-設定使用者群組的指令限制與權限)
    - [sudoers 配置檔 Cmnd\_Alias](#sudoers-配置檔-cmnd_alias)
    - [情境：把帳號從限定指令（例如只能跑 docker）改為完整 sudo all](#情境把帳號從限定指令例如只能跑-docker改為完整-sudo-all)
- [範例](#範例)
  - [建立可 SSH 金鑰登入的 sudo 帳號](#建立可-ssh-金鑰登入的-sudo-帳號)
  - [建立受限群組（restricted）](#建立受限群組restricted)
  - [Cmnd\_Alias](#cmnd_alias)
    - [限制群組 admins 只能執行重啟/關機命令](#限制群組-admins-只能執行重啟關機命令)
    - [群組 maintainers 可以進行系統維護操作](#群組-maintainers-可以進行系統維護操作)
- [例外狀況](#例外狀況)
  - [手動建立 home 目錄（如果已經存在使用者但沒 home）](#手動建立-home-目錄如果已經存在使用者但沒-home)
  - [加入群組後沒有生效](#加入群組後沒有生效)
  - [usermod: user is currently used by process](#usermod-user-is-currently-used-by-process)
  - [user is not in the sudoers file](#user-is-not-in-the-sudoers-file)
  - [sudoers.d 的設定沒有生效](#sudoersd-的設定沒有生效)
  - [如果 /etc/sudoers.tmp 已經存在，要「取消」它](#如果-etcsudoerstmp-已經存在要取消它)

## 參考資料

[Linux 新增使用者 useradd 指令用法教學與範例](https://blog.gtwang.org/linux/linux-useradd-command-tutorial-examples/)

[useradd(8) - Linux manual page](https://man7.org/linux/man-pages/man8/useradd.8.html)

[usermod(8) - Linux manual page](https://man7.org/linux/man-pages/man8/usermod.8.html)

[chage(1) - Linux manual page](https://man7.org/linux/man-pages/man1/chage.1.html)

[sudoers(5) - Sudo manual](https://www.sudo.ws/docs/man/sudoers.man/)

[getent 筆記（查詢 passwd / group）](Linux_工具/Linux_網路狀態部分/Linux_工具_getent(查詢系統名稱解析).md)

[chmod 筆記（檔案權限）](Linux_工具/Linux_基本/Linux_工具_chmod(權限).md)

[ps 筆記（查看使用者的程序）](<Linux_工具/Linux_程序(Process)部分/Linux_工具_ps(檢查處理程序狀態).md>)

[OpenSSL / OpenSSH 筆記（SSH 金鑰、關閉密碼登入）](Linux_工具/Linux_SSH_工具/Linux_工具_OpenSSL_OpenSSH(連線、密鑰).md)

# 配置文檔

| 檔案 | 內容 | 權限 |
|---|---|---|
| `/etc/passwd` | 帳號基本資料 | 所有人可讀 |
| `/etc/shadow` | 密碼雜湊、密碼與帳號期限 | 只有 root |
| `/etc/group` | 群組與成員 | 所有人可讀 |
| `/etc/gshadow` | 群組密碼、群組管理員 | 只有 root |
| `/etc/login.defs` | UID/GID 範圍、密碼期限預設值 | |
| `/etc/default/useradd` | `useradd` 預設值 | |
| `/etc/skel/` | 新使用者家目錄的範本 | |

- 不要直接編輯 `/etc/passwd`、`/etc/shadow`；用指令修改，或用 `vipw` / `vigr`（會鎖檔並檢查）。

## /etc/passwd（帳號）

```
帳號名稱:密碼:UID:GID:說明欄:家目錄:Shell
alice:x:1001:1001:Alice Chen:/home/alice:/bin/bash
```

| 欄位 | 說明 |
|---|---|
| 密碼 | 固定為 `x`，實際密碼放在 `/etc/shadow`（`/etc/passwd` 所有程序都能讀，放密碼容易被竊取） |
| UID | `0` = root（任何帳號 UID 改成 0 就有 root 權限）；`1`～`999` 系統帳號；`1000` 以上一般帳號（只是慣例，權限特性沒有差別） |
| GID | 主要群組（initial group） |
| Shell | 登入後使用的 shell；`/usr/sbin/nologin`、`/bin/false` 代表不能登入 |

## /etc/shadow（密碼與期限）

```
帳號名稱:密碼:最近更動密碼的日期:密碼不可被更動的天數:密碼需要重新變更的天數:密碼需要變更期限前的警告天數:密碼過期後的帳號寬限時間(密碼失效日):帳號失效日期:保留
```

| 欄 | 內容 | 對應指令 |
|---|---|---|
| 2 | 密碼雜湊；開頭 `!` 或 `*` 代表鎖定 / 無密碼 | `passwd -l` / `passwd -u` |
| 3 | 最近更改密碼日（1970-01-01 起算天數；`0` = 下次登入強制改密碼） | `chage -d` |
| 4 | 幾天內不可再改密碼 | `chage -m` / `passwd -n` |
| 5 | 幾天內必須改密碼 | `chage -M` / `passwd -x` |
| 6 | 過期前幾天警告 | `chage -W` / `passwd -w` |
| 7 | 密碼過期後寬限天數，之後停用 | `chage -I` / `passwd -i` |
| 8 | 帳號失效日 | `chage -E` / `useradd -e` |

## /etc/group（群組）

```
群組名稱:群組密碼:GID:成員（逗號分隔）
docker:x:998:alice,bob
```

- 這裡只列「附加群組」成員；以該群組為主要群組（`/etc/passwd` 第 4 欄）的帳號不會列在這裡。

## /etc/login.defs（UID/GID 與密碼預設值）

```
MAIL_DIR        /var/spool/mail  <==使用者預設郵件信箱放置目錄

PASS_MAX_DAYS   99999    <==/etc/shadow 內的第 5 欄，多久需變更密碼日數
PASS_MIN_DAYS   0        <==/etc/shadow 內的第 4 欄，多久不可重新設定密碼日數
PASS_MIN_LEN    5        <==密碼最短的字元長度，已被 pam 模組取代，失去效用！
PASS_WARN_AGE   7        <==/etc/shadow 內的第 6 欄，過期前會警告的日數

UID_MIN          1000    <==使用者最小的 UID，意即小於 1000 的 UID 為系統保留
UID_MAX         60000    <==使用者能夠用的最大 UID
SYS_UID_MIN       201    <==保留給使用者自行設定的系統帳號最小值 UID
SYS_UID_MAX       999    <==保留給使用者自行設定的系統帳號最大值 UID
GID_MIN          1000    <==使用者自訂群組的最小 GID，小於 1000 為系統保留
GID_MAX         60000    <==使用者自訂群組的最大 GID
SYS_GID_MIN       201    <==保留給使用者自行設定的系統帳號最小值 GID
SYS_GID_MAX       999    <==保留給使用者自行設定的系統帳號最大值 GID

CREATE_HOME     yes      <==在不加 -M 及 -m 時，是否主動建立使用者家目錄？
UMASK           077      <==使用者家目錄建立的 umask ，因此權限會是 700
USERGROUPS_ENAB yes      <==使用 userdel 刪除時，是否會刪除初始群組
ENCRYPT_METHOD SHA512    <==密碼雜湊演算法（新版多為 YESCRYPT）
```

```bash
# 查看目前密碼雜湊演算法
grep ^ENCRYPT_METHOD /etc/login.defs
```

- 修改只影響之後新增的帳號，不影響既有帳號。

## /etc/default/useradd 與 /etc/skel（useradd 預設值）

```bash
# 查看 useradd 預設值
useradd -D
```

```
GROUP=100                <==預設的群組
HOME=/home               <==預設的家目錄所在目錄
INACTIVE=-1              <==密碼失效日，在 shadow 內的第 7 欄
EXPIRE=                  <==帳號失效日，在 shadow 內的第 8 欄
SHELL=/bin/sh            <==預設的 shell（Debian / Ubuntu 為 /bin/sh，建議建帳號時用 -s 指定）
SKEL=/etc/skel           <==使用者家目錄的內容資料參考目錄
CREATE_MAIL_SPOOL=no     <==是否主動幫使用者建立郵件信箱(mailbox)
```

- 骨架目錄（skeleton directory）：建立家目錄時會把 `/etc/skel` 裡的檔案（`.bashrc`、`.profile` 等）複製過去。想讓所有新帳號都有某個設定檔，就放進 `/etc/skel`。

# 指令

## 查詢使用者與群組

```bash
whoami                      # 目前使用者
id                          # 目前使用者的 UID、GID、所屬群組
id alice                    # 指定使用者
groups alice                # 所屬群組

getent passwd alice         # 帳號資料（含 LDAP / SSSD 等外部來源）
getent group docker         # 群組與成員

# 列出所有一般帳號（UID 1000 以上，排除 nobody）
awk -F: '$3 >= 1000 && $3 < 65534 {print $1, $3, $6, $7}' /etc/passwd

# 列出可登入的帳號（shell 不是 nologin / false）
grep -vE 'nologin|false$' /etc/passwd

# 列出有 sudo 權限的人
getent group sudo           # Debian / Ubuntu
getent group wheel          # RedHat 系
sudo -l -U alice            # 某帳號能執行哪些 sudo 指令
```

登入紀錄：

```bash
who                 # 目前登入的使用者
w                   # 目前登入者與正在執行的指令
last                # 歷史登入紀錄（/var/log/wtmp）
last alice          # 指定使用者
sudo lastb          # 登入失敗紀錄（/var/log/btmp）
lastlog             # 每個帳號最後一次登入時間（部分新版發行版改為 lastlog2）
```

## useradd passwd (新增使用者)

```bash
useradd [選項] alice
```

| 選項 | 說明 |
|---|---|
| `-m` | 建立家目錄（一般帳號建議都加） |
| `-M` | 不建立家目錄（系統帳號） |
| `-d DIR` | 指定家目錄（絕對路徑） |
| `-s SHELL` | 登入 shell，例 `/bin/bash`、`/usr/sbin/nologin` |
| `-u UID` | 指定 UID |
| `-g GROUP` | 主要群組（initial group，寫入 `/etc/passwd` 第 4 欄）；不指定時建立同名群組 |
| `-G G1,G2` | 附加群組（寫入 `/etc/group`） |
| `-c "說明"` | 說明欄（例如真實姓名） |
| `-r` | 建立系統帳號（UID 落在 `SYS_UID_MIN`～`SYS_UID_MAX`） |
| `-e YYYY-MM-DD` | 帳號失效日（shadow 第 8 欄） |
| `-f DAYS` | 密碼過期後幾天停用帳號（shadow 第 7 欄；`0` 立刻、`-1` 永不） |
| `-k DIR` | 改用其他骨架目錄（需搭配 `-m`） |

```bash
useradd -d /data/alice -m alice         # 指定家目錄
useradd -M alice                        # 不建立家目錄
useradd -u 1500 alice                   # 指定 UID
useradd -g team alice                   # 指定主要群組
useradd -e 2026-12-31 alice             # 帳號使用期限
useradd -f 45 alice                     # 密碼過期 45 天後停用
useradd -c "Alice Chen" alice           # 說明欄
useradd -s /usr/sbin/nologin alice      # 不能登入（服務用帳號）
useradd -m -k /etc/custom.skel alice    # 改用其他骨架目錄

# 服務用的系統帳號：無家目錄、不能登入
useradd -r -M -s /usr/sbin/nologin myservice
```

- Debian / Ubuntu 另有互動式的 `adduser alice`（會自動建家目錄、問密碼與說明），寫腳本時用 `useradd`。

### 新增使用者範例

```bash
# 1. 建立使用者（家目錄 + bash）
useradd -m -s /bin/bash alice

# 2. 設定密碼（互動輸入）
passwd alice

# 3. 加入附加群組
usermod -aG docker alice

# 4. 驗證
id alice
ls -ld /home/alice
```

一行建立使用者、家目錄、加群組並設定密碼：

```bash
useradd -m -s /bin/bash -G restricted alice && passwd alice
```

批次設定密碼（`chpasswd` 讀 `帳號:密碼`；密碼不要寫在指令或 history 裡）：

```bash
read -rs -p "password: " PW && echo "alice:$PW" | chpasswd && unset PW
```

## usermod (修改使用者)

```bash
usermod -aG docker alice            # 加入附加群組（-a 一定要加）
usermod -G docker alice             # ⚠ 沒有 -a：附加群組會被「覆蓋」成只剩 docker
usermod -g team alice               # 改主要群組
usermod -s /bin/bash alice          # 改登入 shell（一般使用者可用 chsh -s 自己改）
usermod -c "Alice Chen" alice       # 改說明欄
usermod -d /data/alice -m alice     # 改家目錄並搬移內容（-m）
usermod -u 1600 alice               # 改 UID（家目錄內的檔案會跟著改，其他位置的檔案要自己 chown）
usermod -e 2026-12-31 alice         # 設定帳號失效日（-e "" 取消）

# 改帳號名稱（同時改家目錄與同名群組）
usermod -l bob -d /home/bob -m alice
groupmod -n bob alice
```

- 從群組移除：`gpasswd -d alice docker`。
- 群組變更要**重新登入**才會生效（見「加入群組後沒有生效」）。

## userdel (刪除使用者)

```bash
# 先確認沒有程序在跑，有的話先結束
ps -u alice
sudo pkill -u alice                 # 或 sudo loginctl terminate-user alice

userdel alice                       # 只刪帳號，保留家目錄
userdel -r alice                    # 連家目錄與郵件信箱一起刪

# 找出系統上還屬於該 UID 的檔案（刪帳號後會變成只顯示數字的孤兒檔案）
sudo find / -xdev -uid 1001 2>/dev/null
```

- 刪除前先備份家目錄；也可以先「鎖定」帳號觀察一段時間再刪。
- 記得一併清掉 `/etc/sudoers.d/` 裡該帳號的設定與 crontab（`crontab -r -u alice`）。

## passwd chage (密碼與帳號期限)

```bash
passwd                      # 改自己的密碼
passwd alice                # root 改別人的密碼
passwd -S alice             # 密碼狀態（P 有密碼、L 鎖定、NP 無密碼）
passwd -l alice             # 鎖定密碼（shadow 第 2 欄前加 !）
passwd -u alice             # 解除鎖定
passwd -d alice             # 刪除密碼（⚠ 可能變成免密碼登入）
passwd -e alice             # 讓密碼立即過期，下次登入強制更改
```

| `passwd` 選項 | 對應 shadow 欄位 |
|---|---|
| `-n DAYS` | 第 4 欄：幾天內不可再改密碼 |
| `-x DAYS` | 第 5 欄：幾天內必須改密碼 |
| `-w DAYS` | 第 6 欄：過期前警告天數 |
| `-i DAYS` | 第 7 欄：過期後寬限天數 |
| `--stdin` | 從管線讀密碼（只有 RedHat 系支援；其他發行版用 `chpasswd`） |

```bash
chage -l alice                  # 查看密碼與帳號期限
chage -d 0 alice                # 下次登入強制改密碼（建帳號給別人時常用）
chage -M 90 -W 7 alice          # 每 90 天要改密碼，過期前 7 天提醒
chage -E 2026-12-31 alice       # 帳號失效日（-E -1 取消）
chage -I 30 alice               # 密碼過期 30 天後停用帳號
```

## 鎖定 / 停用帳號

| 做法 | 密碼登入 | SSH 金鑰登入 | 已登入的 session | 還原 |
|---|---|---|---|---|
| `passwd -l alice` / `usermod -L alice` | 擋 | **可能仍可登入** | 不影響 | `passwd -u` / `usermod -U` |
| `usermod -s /usr/sbin/nologin alice` | 擋（互動登入） | 擋（互動登入） | 不影響 | 改回 `/bin/bash` |
| `chage -E 0 alice`（帳號過期） | 擋 | 擋 | 不影響 | `chage -E -1 alice` |

完整停用（離職、帳號外洩）：

```bash
sudo chage -E 0 alice                       # 帳號過期，所有登入方式都擋
sudo usermod -s /usr/sbin/nologin alice
sudo passwd -l alice
sudo pkill -KILL -u alice                   # 踢掉已登入的 session
sudo mv /home/alice/.ssh/authorized_keys /home/alice/.ssh/authorized_keys.disabled
```

## groupadd groupmod groupdel gpasswd (群組管理)

```bash
getent group devops             # 確認群組是否存在
groupadd devops                 # 新增群組
groupadd -g 2000 devops         # 指定 GID
groupadd -r mysvc               # 系統群組
groupmod -n ops devops          # 改名
groupmod -g 2100 ops            # 改 GID
groupdel ops                    # 刪除（有使用者以它為主要群組時不能刪）

gpasswd -a alice devops         # 加入成員（等同 usermod -aG devops alice）
gpasswd -d alice devops         # 移除成員
gpasswd -M alice,bob devops     # 直接設定成員清單（覆蓋）
newgrp devops                   # 不重新登入，在目前 shell 切換到新群組
```

常見群組：

| 群組 | 權限 | 注意 |
|---|---|---|
| `sudo`（Debian / Ubuntu）、`wheel`（RedHat 系） | 完整 sudo | |
| `docker` | 可直接操作 Docker | **等同 root**（`docker run -v /:/host` 就能改主機任何檔案） |
| `adm` | 讀 `/var/log` 下的系統 log | |
| `www-data`（Debian）、`nginx` / `apache`（RedHat） | Web 服務的檔案 | |

## su sudo (切換身分)

```bash
su - alice                      # 切換成 alice（- 載入對方完整登入環境）
su -                            # 切換成 root（需 root 密碼）
sudo -i                         # 用自己的密碼取得 root 登入 shell
sudo -u alice -i                # 以 alice 身分開 shell
sudo -u www-data php artisan    # 以指定使用者執行單一指令
sudo -l                         # 自己能用 sudo 執行哪些指令
sudo -k                         # 清掉 sudo 密碼快取，下次要重新輸入
```

## visudo 設定使用者群組的指令限制與權限

```bash
sudo visudo                         # 編輯 /etc/sudoers（存檔前檢查語法）
sudo EDITOR=vim visudo              # 指定編輯器
sudo visudo -c                      # 只檢查語法，不開編輯器、不產生 /etc/sudoers.tmp
less /etc/sudoers                   # 只查看
ls /etc/sudoers.d/                  # 查看額外設定檔
ps aux | grep visudo                # 確認沒有其他人正在用 visudo
```

sudoers 基本格式：

```
使用者  主機=(身分)  選項: 指令
```

```
root      ALL=(ALL:ALL) ALL                                 # root 全部權限
alice     ALL=(ALL) ALL                                     # alice 可用 sudo（需輸入自己的密碼）
alice     ALL=(ALL) NOPASSWD: ALL                           # 免密碼（⚠ 帳號外洩 = 主機失守）
alice     ALL=(ALL) /usr/bin/systemctl restart nginx        # 只允許特定指令（含參數）
%devops   ALL=(ALL) /usr/bin/systemctl                      # % 開頭代表群組
deploy    ALL=(www-data) NOPASSWD: /usr/bin/php             # 只能以 www-data 身分執行 php
```

- 指令一律寫**絕對路徑**（用 `which systemctl` 查）。
- 同一使用者有多條規則時，**最後符合的那條生效**；`sudoers.d` 的檔案依檔名順序讀入。

### sudoers 配置檔 Cmnd_Alias

Cmnd_Alias 必須寫在 sudoers 配置檔裡，兩種做法：

| 做法 | 優點 | 缺點 |
|---|---|---|
| 主檔 `/etc/sudoers`（`sudo visudo`） | 直接生效 | 語法錯誤可能鎖死 sudo；系統升級可能覆蓋 |
| `/etc/sudoers.d/<檔名>`（**推薦**） | 不動主檔、升級不覆蓋、可依群組拆檔管理 | 檔名不能含 `.` 或以 `~` 結尾（會被忽略） |

```bash
sudo visudo -f /etc/sudoers.d/restricted          # 建立 / 編輯
sudo visudo -c -f /etc/sudoers.d/restricted       # 驗證語法
sudo chmod 440 /etc/sudoers.d/restricted          # 權限必須是 0440
```

定義別名給群組 / 使用者使用：

```
Cmnd_Alias MY_ALIAS = /usr/bin/systemctl restart nginx, /usr/bin/systemctl reload nginx

%群組名稱 ALL=(ALL) MY_ALIAS
username  ALL=(ALL) MY_ALIAS
```

```bash
sudo -l -U username     # 查看使用者允許的指令
```

### 情境：把帳號從限定指令（例如只能跑 docker）改為完整 sudo all

以下以帳號 `deploy` 為例。先找出目前的設定位置：

```bash
sudo grep -rn deploy /etc/sudoers /etc/sudoers.d/
```

通常會看到：

```
/etc/sudoers.d/deploy:1:deploy ALL=(ALL) NOPASSWD: /usr/bin/docker
```

`方法 A`：修改 sudoers 設定檔（建議）。用 `visudo` 編輯，存檔前會檢查語法：

```bash
sudo visudo -f /etc/sudoers.d/deploy
```

```
deploy ALL=(ALL:ALL) ALL
# 或維持免密碼
deploy ALL=(ALL:ALL) NOPASSWD: ALL
```

`方法 B`：加入 sudo 群組（RedHat 系為 `wheel`）：

```bash
sudo usermod -aG sudo deploy
```

- 需要輸入密碼，帳號必須已設定密碼（`sudo passwd deploy`）；**重新登入**後才生效。
- 記得刪除原本只允許 docker 的那行，避免混淆。

驗證，看到 `(ALL : ALL) ALL` 即代表成功：

```bash
sudo -l -U deploy
```

注意：

- **資安風險**：若此帳號原本給 CI/CD 或自動化腳本使用，改成 `NOPASSWD: ALL` 後，一旦金鑰外洩，攻擊者即可完全控制主機。
- **保留後路**：修改期間另外保持一個 root session 開著，設定出錯還能修回來。

# 範例

## 建立可 SSH 金鑰登入的 sudo 帳號

```bash
# 1. 建立帳號並加入 sudo 群組（RedHat 系改 wheel）
sudo useradd -m -s /bin/bash -G sudo alice
sudo passwd alice                  # sudo 時要用到；只用金鑰登入也建議設密碼
sudo chage -d 0 alice              # 第一次登入強制改密碼

# 2. 放入公鑰（權限錯誤會導致金鑰登入失敗）
sudo install -d -m 700 -o alice -g alice /home/alice/.ssh
echo "$ALICE_PUBKEY" | sudo tee /home/alice/.ssh/authorized_keys > /dev/null
sudo chown alice:alice /home/alice/.ssh/authorized_keys
sudo chmod 600 /home/alice/.ssh/authorized_keys

# 3. 另開終端機測試能登入、能 sudo 之後，才關掉原本的 session
ssh alice@203.0.113.10
sudo -v
```

## 建立受限群組（restricted）

```bash
groupadd restricted
groupadd restricted_no_docker
sudo visudo -f /etc/sudoers.d/restricted
```

```
Cmnd_Alias RESTRICTED_COMMAND = /bin/cp, /bin/systemctl, /usr/bin/* /var/app/dir/*, /usr/local/bin/docker-compose, /usr/bin/docker, /usr/bin/vim, /bin/cat
Cmnd_Alias RESTRICTED_NO_DOCKER_COMMAND = /usr/bin/* /var/app/dir/*, /bin/cat

%restricted             ALL=(ALL)       RESTRICTED_COMMAND
%restricted_no_docker   ALL=(ALL)       RESTRICTED_NO_DOCKER_COMMAND
```

- `%restricted`：可執行 `RESTRICTED_COMMAND` 的所有命令。
- `%restricted_no_docker`：可執行 `RESTRICTED_NO_DOCKER_COMMAND`，不含 Docker。

這份設定實際上**幾乎等於完整 root**，使用前要知道：

| 寫法 | 問題 |
|---|---|
| `/usr/bin/* /var/app/dir/*` | sudoers 解讀為「`/usr/bin` 下**任何**指令，參數符合 `/var/app/dir/*`」；`*` 也會比對空白與 `../`，例如 `sudo /usr/bin/bash /var/app/dir/x.sh` 就能以 root 執行任意腳本 |
| `/usr/bin/vim` | vim 內 `:!bash` 可開 root shell；要讓人以 root 編輯檔案改用 `sudoedit`（`Cmnd_Alias` 寫 `sudoedit /var/app/dir/*.conf`） |
| `/usr/bin/docker`、`docker-compose` | `docker run -v /:/host` 可改主機任何檔案，等同 root |
| `/bin/cp`、`/bin/cat`（不限參數） | 可讀 `/etc/shadow`、可覆蓋 `/etc/sudoers` |
| `/bin/systemctl`（不限參數） | 可啟停任何服務 |

比較安全的寫法：指令與參數寫死，只放真正需要的操作：

```
Cmnd_Alias APP_CMDS = /usr/bin/systemctl restart myapp, /usr/bin/systemctl status myapp, /usr/bin/journalctl -u myapp *
%restricted ALL=(ALL) APP_CMDS
```

## Cmnd_Alias

### 限制群組 admins 只能執行重啟/關機命令

```
Cmnd_Alias REBOOT_CMDS = /sbin/reboot, /sbin/shutdown
%admins ALL=(ALL) REBOOT_CMDS
```

### 群組 maintainers 可以進行系統維護操作

```
Cmnd_Alias MAINT_CMDS = /sbin/reboot, /usr/bin/systemctl restart nginx, /usr/bin/systemctl restart mysql
%maintainers ALL=(ALL) MAINT_CMDS
```

# 例外狀況

## 手動建立 home 目錄（如果已經存在使用者但沒 home）

```bash
mkdir -p /home/username
cp -rT /etc/skel /home/username              # 補上預設設定檔（.bashrc 等）
chown -R username:username /home/username    # -R 遞迴修改，包含所有子資料夾及檔案
chmod 700 /home/username
usermod -d /home/username username           # 修改 home 設定
```

## 加入群組後沒有生效

- 群組是在**登入時**決定的，`usermod -aG` 之後已登入的 session 不會變。
- 解法：登出再登入（SSH 要重新連線）；或在目前 shell 執行 `newgrp docker` 暫時套用。
- 確認：`id`（目前 session）和 `id alice`（設定檔）結果不同，就是還沒重新登入。

## usermod: user is currently used by process

改帳號名稱、UID、家目錄時，該使用者有程序在跑就會失敗：

```bash
ps -u alice                         # 查看是哪些程序
sudo pkill -u alice                 # 結束（必要時 -KILL）
sudo loginctl terminate-user alice  # 或結束整個登入 session
```

## user is not in the sudoers file

```
alice is not in the sudoers file.  This incident will be reported.
```

- 原因：帳號不在 `sudo` / `wheel` 群組，sudoers 也沒有對應規則。
- 解法：用 root（或其他有 sudo 的帳號）執行 `usermod -aG sudo alice`，或在 `/etc/sudoers.d/` 加規則；之後 alice 重新登入。

## sudoers.d 的設定沒有生效

依序檢查：

- 檔名含 `.` 或以 `~` 結尾會被**直接忽略**（例如 `deploy.conf` 不會生效，要改成 `deploy`）。
- 主檔 `/etc/sudoers` 最後要有 `@includedir /etc/sudoers.d`（舊版寫法 `#includedir`，`#` 不是註解）。
- 權限要是 `0440`、擁有者 root。
- 後面其他規則覆蓋了它（最後符合的規則生效），用 `sudo -l -U alice` 看實際結果。

## 如果 /etc/sudoers.tmp 已經存在，要「取消」它

`visudo` 異常結束時會留下 `/etc/sudoers.tmp`，下次開啟會提示檔案忙碌。先確認沒有人正在使用、主檔語法正常再刪除：

```bash
ps aux | grep visudo
visudo -c
# /etc/sudoers: parsed OK
rm -f /etc/sudoers.tmp
```
