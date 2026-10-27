# Linux 工具 age(檔案公鑰加密)

```
age 是簡單、現代的檔案加密工具（同時是檔案格式與 Go 函式庫），由 Filippo Valsorda（@FiloSottile）與 Ben Cartwright-Cox（@benjojo）設計。
特色：金鑰短、沒有設定選項、可接 UNIX 管線（tar | age）；可用 age 公鑰、SSH 公鑰或密碼加密；
v1.3.0 起支援後量子（post-quantum）金鑰。定位類似「只做加密的簡化版 GPG」——不做簽章、不管信任網。
```

## 目錄

- [Linux 工具 age(檔案公鑰加密)](#linux-工具-age檔案公鑰加密)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [概念](#概念)
  - [公鑰加密怎麼運作](#公鑰加密怎麼運作)
  - [金鑰格式](#金鑰格式)
  - [age 與 GPG 比較](#age-與-gpg-比較)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (Fedora)](#redhat-fedora)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [預編譯執行檔](#預編譯執行檔)
- [指令](#指令)
  - [產生金鑰](#產生金鑰)
  - [用公鑰加密 / 用私鑰解密](#用公鑰加密--用私鑰解密)
  - [多個收件人](#多個收件人)
  - [用密碼加密](#用密碼加密)
  - [用 SSH 金鑰加密](#用-ssh-金鑰加密)
  - [後量子金鑰](#後量子金鑰)
  - [查看加密檔資訊](#查看加密檔資訊)
  - [常見用法：備份加密](#常見用法備份加密)
- [例外狀況](#例外狀況)

## 參考資料

[age GitHub](https://github.com/FiloSottile/age)

[age(1) man page](https://filippo.io/age/age.1)

[age 格式規格 age-encryption.org/v1](https://age-encryption.org/v1)

[rage（相容的 Rust 實作）](https://github.com/str4d/rage)

[awesome-age（外掛、工具、整合清單）](https://github.com/FiloSottile/awesome-age)

[age-plugin-yubikey（用 YubiKey 保存私鑰）](https://github.com/str4d/age-plugin-yubikey)

[SOPS（用 age 加密設定檔裡的機密）](https://github.com/getsops/sops)

[OpenSSL / OpenSSH 筆記（SSH 金鑰產生）](../Linux_SSH_工具/Linux_工具_OpenSSL_OpenSSH(連線、密鑰).md)

[敏感檔案加密工具比較與選擇（age 以外的選項：GPG、7-Zip、SOPS、LUKS、VeraCrypt、restic…）](../../../../../00_概念/資安/資安_-_敏感檔案加密(加密工具比較與選擇).md)

# 概念

## 公鑰加密怎麼運作

```
公鑰（age1...）：可以公開，給任何想加密檔案給你的人
私鑰（AGE-SECRET-KEY-1...）：只有你持有，用來解密

別人用你的公鑰加密 → 只有你的私鑰解得開
（像一個人人都能投信、只有你有鑰匙的信箱）
```

- 加密時不需要對方在線上、不需要事先交換密碼，只要拿到對方的公鑰。
- 一個檔案可以同時加密給多個收件人，每個人用自己的私鑰都能解開。
- 底層：收件人金鑰用 X25519 交換、檔案內容用 ChaCha20-Poly1305 加密並驗證完整性；密碼模式用 scrypt 衍生金鑰。檔案被竄改時解密會失敗，不會解出錯誤內容。
- **age 只做加密，不做簽章**：任何拿到你公鑰的人都能加密檔案給你，解密成功不代表「確定是某人寄的」。需要證明來源時，另外用簽章工具（例如 `ssh-keygen -Y sign`、minisign）。

## 金鑰格式

| 種類 | 格式範例 | 用途 |
|---|---|---|
| age 公鑰（收件人） | `age1ql3z7hjy54pw3hyww5ayyfg7zqgvc7w3j2elw8zmrj2kg5sfn9aqmcac8p` | 加密用，可以公開 |
| age 私鑰（身分） | `AGE-SECRET-KEY-1...` | 解密用，**不可外流** |
| 後量子公鑰 / 私鑰 | `age1pq1...`（約 2000 字元）/ `AGE-SECRET-KEY-PQ-1...` | v1.3.0+，抵抗未來量子電腦攻擊 |
| SSH 公鑰 / 私鑰 | `ssh-ed25519 AAAA...`、`ssh-rsa AAAA...` / `~/.ssh/id_ed25519` | 直接拿現有 SSH 金鑰加解密 |

## age 與 GPG 比較

| 項目 | age | GPG |
|---|---|---|
| 功能範圍 | 只有檔案加密 | 加密、簽章、信任網、金鑰伺服器、Email |
| 設定 | 沒有設定檔、沒有選項可調 | 設定多、演算法可選 |
| 金鑰 | 一行字串，可以直接貼 | 金鑰環（keyring），要匯入匯出 |
| 簽章 | 不支援 | 支援 |
| 適合 | 加密備份、傳機密檔、把機密放進 git（搭配 SOPS） | 需要簽章、Email 加密、既有 GPG 生態 |

# 安裝

## Debian (Ubuntu)

```bash
# Debian 12+、Ubuntu 22.04+
sudo apt install age

# Debian 11：需先啟用 bullseye-backports 才有 v1.0.0+
sudo apt install age/bullseye-backports

age --version
```

## RedHat (Fedora)

```bash
# Fedora 33+；RHEL / Rocky 等套件庫沒有時，改用下方預編譯執行檔
sudo dnf install age
```

## Homebrew (MacOS)

```bash
brew install age
```

## Windows

```powershell
winget install --id FiloSottile.age
# 或 choco install age.portable / scoop bucket add extras && scoop install age
```

## 預編譯執行檔

```bash
# Linux amd64 最新版（官方下載點，可用 Sigsum 證明驗證）
curl -L -o age.tar.gz "https://dl.filippo.io/age/latest?for=linux/amd64"
tar xzf age.tar.gz
sudo install age/age age/age-keygen /usr/local/bin/

# 有 Go 環境也可以從原始碼安裝
go install filippo.io/age/cmd/...@latest
```

# 指令

## 產生金鑰

```bash
# 產生一組金鑰，私鑰寫進 key.txt，公鑰印在畫面上
age-keygen -o key.txt
# Public key: age1ql3z7hjy54pw3hyww5ayyfg7zqgvc7w3j2elw8zmrj2kg5sfn9aqmcac8p

chmod 600 key.txt

# 之後要再查公鑰
age-keygen -y key.txt
```

`key.txt` 內容（含公鑰註解與私鑰）：

```
# created: 2026-10-21T15:00:00+08:00
# public key: age1ql3z7hjy54pw3hyww5ayyfg7zqgvc7w3j2elw8zmrj2kg5sfn9aqmcac8p
AGE-SECRET-KEY-1...
```

私鑰要另外備份到安全的地方（密碼管理器、離線媒體）；**私鑰遺失，用它加密的檔案永遠解不開**。

## 用公鑰加密 / 用私鑰解密

```bash
# 加密：-r 指定收件人公鑰，-o 輸出檔（不給 -o 就輸出到 stdout）
age -r age1ql3z7hjy54pw3hyww5ayyfg7zqgvc7w3j2elw8zmrj2kg5sfn9aqmcac8p -o secret.txt.age secret.txt

# 解密：-d 解密，-i 指定私鑰檔
age -d -i key.txt -o secret.txt secret.txt.age

# 輸出成文字格式（PEM），方便貼進 Email、聊天、設定檔
age -a -r age1... secret.txt > secret.txt.age.pem
```

| 參數 | 說明 |
|---|---|
| `-e` / `--encrypt` | 加密（預設，可省略） |
| `-d` / `--decrypt` | 解密 |
| `-r` / `--recipient` | 收件人公鑰（age1...、ssh-ed25519...），可重複 |
| `-R` / `--recipients-file` | 從檔案讀收件人，一行一個，`#` 開頭是註解；`-` 代表從 stdin 讀 |
| `-i` / `--identity` | 私鑰檔，可重複；用不到的會被忽略 |
| `-p` / `--passphrase` | 改用密碼加密 |
| `-a` / `--armor` | 輸出 PEM 文字格式 |
| `-o` / `--output` | 輸出檔；**已存在會被直接覆寫** |

## 多個收件人

```bash
# 直接列多個 -r：每個人都能用自己的私鑰解開
age -o backup.tar.age -r age1aaa... -r age1bbb... backup.tar

# 或寫成收件人清單檔
cat > recipients.txt <<'EOF'
# Alice
age1ql3z7hjy54pw3hyww5ayyfg7zqgvc7w3j2elw8zmrj2kg5sfn9aqmcac8p
# Bob
age1lggyhqrw2nlhcxprm67z43rta597azn8gknawjehu9d9dl0jq3yqqvfafg
EOF
age -R recipients.txt backup.tar > backup.tar.age
```

建議把自己的公鑰也加進收件人，否則加密給別人後，自己反而解不開。

## 用密碼加密

```bash
# 加密時輸入密碼；直接按 Enter 會自動產生一組安全的密碼並顯示
age -p secrets.txt > secrets.txt.age

# 解密時自動偵測是密碼加密，會提示輸入密碼
age -d secrets.txt.age > secrets.txt

# 用密碼保護私鑰檔本身（私鑰要放在雲端等不完全可信的地方時）
age-keygen | age -p > key.age
age -d -i key.age secrets.txt.age > secrets.txt     # 會先詢問 key.age 的密碼
```

密碼模式需要互動輸入，**不適合排程腳本**；排程請用公鑰模式（加密端只需要公鑰，私鑰不必放在伺服器上）。

## 用 SSH 金鑰加密

```bash
# 用現有 SSH 公鑰加密、SSH 私鑰解密（支援 ssh-ed25519、ssh-rsa；不支援 ssh-agent）
age -R ~/.ssh/id_ed25519.pub example.jpg > example.jpg.age
age -d -i ~/.ssh/id_ed25519 example.jpg.age > example.jpg

# 加密給某個 GitHub 使用者（用他公開在 GitHub 的 SSH 公鑰）
curl https://github.com/<帳號>.keys | age -R - example.jpg > example.jpg.age
```

- SSH 模式會在加密檔裡嵌入公鑰標記，可以被追蹤「這個檔案是加密給哪把金鑰」。
- SSH 金鑰常被當成「可隨時撤銷的登入憑證」，持有者不一定會長期保管；存在 YubiKey 裡的 SSH 金鑰也無法用來解密。長期保存的資料建議用專用的 age 金鑰。

## 後量子金鑰

```bash
# 產生混合後量子金鑰（ML-KEM-768 + X25519），需 age v1.3.0+
age-keygen -pq -o key.txt
age-keygen -y key.txt > recipient.txt         # 公鑰很長（約 2000 字元），用檔案傳遞
age -R recipient.txt example.jpg > example.jpg.age
age -d -i key.txt example.jpg.age > example.jpg
```

要保存很多年的機密（「現在先偷存、未來用量子電腦解」的威脅），可以考慮改用後量子金鑰。

## 查看加密檔資訊

```bash
# 不解密就能看收件人類型、是否後量子、各部分大小（較新版本才有 age-inspect）
age-inspect secrets.age
age-inspect --json secrets.age
```

## 常見用法：備份加密

```bash
# 打包目錄並加密（伺服器只需要放公鑰）
tar czf - /var/www | age -r age1... > www_$(date +%F).tar.gz.age

# 還原（在持有私鑰的機器上）
age -d -i key.txt www_2026-10-21.tar.gz.age | tar xzf -

# MySQL 備份加密：帳密放在 ~/.my.cnf，不要寫在指令裡
mysqldump --single-transaction mydb | gzip | age -R /etc/backup/recipients.txt > mydb_$(date +%F).sql.gz.age
```

加密後的備份可以放到不完全可信的地方（雲端儲存、異地主機），對方拿到檔案也看不到內容。

# 例外狀況

| 狀況 | 原因 / 處理 |
|---|---|
| `no identity matched any of the recipients` | 用來解密的私鑰不在這個檔案的收件人裡；確認 `-i` 指的是對的私鑰，或用 `age-inspect` 看收件人類型 |
| 輸出檔內容被蓋掉 | `-o` 指到已存在的檔案會直接覆寫；加密與解密的輸出不要指到原檔 |
| 解密時要求密碼但不知道密碼 | 該檔案是用 `-p` 密碼加密的，私鑰幫不上忙；密碼遺失無法復原 |
| SSH 金鑰解密失敗 | age 不支援 `ssh-agent`，必須用 `-i` 指定私鑰檔；有密碼的 SSH 私鑰會提示輸入密碼 |
| 舊版 age 不認得 `age1pq1...` 公鑰 | 後量子金鑰需要 v1.3.0+，或另外安裝 `age-plugin-pq` 外掛 |
| 解密失敗並提示檔案損毀 | 加密檔被竄改或傳輸不完整（age 有完整性驗證，不會解出錯誤內容）；重新取得原始檔案 |
