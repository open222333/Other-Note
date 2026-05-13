# Linux 工具 rsync(同步鏡像備份)

## 目錄

- [Linux 工具 rsync(同步鏡像備份)](#linux-工具-rsync同步鏡像備份)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [深入學習相關](#深入學習相關)
    - [排錯相關](#排錯相關)
- [安裝](#安裝)
- [指令](#指令)
  - [常用選項](#常用選項)
  - [基本用法](#基本用法)
  - [--delete 注意事項](#--delete-注意事項)
  - [rsync 暫存檔](#rsync-暫存檔)
  - [日誌輸出](#日誌輸出)
- [rsync 與 scp 區別](#rsync-與-scp-區別)
- [狀況](#狀況)

## 參考資料

[rsync(1) - Linux man page](https://linux.die.net/man/1/rsync)

[以 rsync 進行同步鏡像備份 - 鳥哥](https://linux.vbird.org/linux_server/centos6/0310telnetssh.php#rsync)

### 深入學習相關

[How does `scp` differ from `rsync`? - `scp` 與 `rsync` 有何不同？](https://stackoverflow.com/questions/20244585/how-does-scp-differ-from-rsync)

### 排錯相關

[rsync故障排除解答](https://blog.51cto.com/53cto/1771826)

# 安裝

```bash
# Ubuntu / Debian
apt-get install rsync

# CentOS / Red Hat
yum install rsync

# Fedora
dnf install rsync

# openSUSE
zypper install rsync
```

# 指令

## 常用選項

```
-v  觀察模式，列出傳輸中的檔案名稱等資訊
-q  安靜模式，僅顯示錯誤訊息
-r  遞迴複製，針對目錄處理
-u  僅更新，若目標檔案較新則保留，不覆蓋
-l  複製連結檔屬性，而非連結目標的原始檔案
-p  保留權限（permission）
-g  保留擁有群組
-o  保留擁有人
-D  保留裝置屬性
-t  保留時間參數
-I  忽略 mtime，比對速度較快
-z  傳輸時壓縮
-e  指定通道協定，例如 -e ssh
-a  等同 -rlptgoD，最常用的參數
-P / --progress  顯示進度
--delete         刪除目標端有但來源端沒有的檔案（鏡像同步用，見下方注意）
--temp-dir=/tmp  指定暫存檔位置
--remove-source-files  同步完成後刪除來源檔案
--exclude="dir"  排除指定目錄或檔案
```

## 基本用法

```bash
# 本地複製（遞迴）
rsync -av /source/ /dest/

# 透過 SSH 傳送至遠端
rsync -avz -e ssh /local/path user@host:/remote/path

# 透過 SSH Key 傳送
rsync -avz -e "ssh -i /root/.ssh/id_rsa" /local/path user@host:/remote/path

# 傳送指定檔案（不影響目標目錄其他檔案）
rsync -avz file1.sql.gz file2.sql.gz user@host:/remote/path/

# 傳送多個檔案（陣列方式，Shell 腳本中常用）
rsync -avz "${FILES[@]}" user@host:/remote/path/

# 從遠端拉取
rsync -v user@192.168.100.10:~ /tmp

# 排除特定目錄
rsync -av --progress --exclude="dir2" /source/ /dest/

# 僅複製目錄結構，忽略檔案
rsync -av --include '*/' --exclude '*' /source/ /dest/
```

## --delete 注意事項

`--delete` 會讓目標目錄與來源完全一致，**來源端沒有的檔案會從目標端刪除**。

```bash
# 目錄對目錄同步（--delete 會刪除目標端多餘的檔案）
rsync -avz --delete /source/ user@host:/dest/

# 若只想傳送新檔案、不刪除目標端任何東西，直接指定來源檔案即可
rsync -avz file.sql.gz user@host:/dest/
```

`--delete` 適合用在完整鏡像備份，不適合用在「只傳送特定檔案」的情境。

## rsync 暫存檔

rsync 傳輸大檔案時，會在目標目錄建立隱藏暫存檔，傳輸完成後自動重新命名為正式檔名。

```
傳輸中：.all_databases-20260511.sql.gz.rx3APn   ← 隱藏檔，隨機後綴
傳輸完成：all_databases-20260511.sql.gz
```

看到此類暫存檔代表傳輸仍在進行中，屬正常現象。

## 日誌輸出

```bash
# 輸出至 log 檔
rsync -av /source/ /dest/ --log-file=mylog.log

# 標準錯誤輸出至 error.log
rsync [options] source destination 2> error.log

# 標準輸出與標準錯誤合併
rsync [options] source destination > output.log 2>&1
```

# rsync 與 scp 區別

scp 讀取來源檔案後直接寫入目標，執行簡單的線性複製，適合單次傳送單一檔案。

rsync 採用增量傳輸演算法，只傳送差異部分，適合大量檔案或定期備份。此外 rsync 支援複雜過濾規則、守護程序模式、傳輸壓縮等進階功能，scp 則選項較少。

```bash
# scp：單一檔案傳送
scp file.sql.gz user@host:/dest/

# rsync：單一檔案傳送（用法相近，但有增量傳輸優化）
rsync -avz file.sql.gz user@host:/dest/

# rsync：路徑參數方式（常見於 xtrabackup 等工具）
rsync -Pr /path/to/backup user@host:/path/to/backup
```

# 狀況

**1. auth failed on module**

```
@ERROR: auth failed on module xxxxx
rsync: connection unexpectedly closed (90 bytes read so far)
rsync error: error in rsync protocol data stream (code 12) at io.c(150)
```

密碼錯誤，檢查 rsyncd.scrt 兩端密碼是否一致。

---

**2. password file must not be other-accessible**

```
password file must not be other-accessible
continuing without password file
```

rsyncd.scrt 權限不對，應設為 600：`chmod 600 /etc/rsyncd.scrt`

---

**3. chroot failed**

```
@ERROR: chroot failed
rsync error: error in rsync protocol data stream (code 12) at io.c(150)
```

rsyncd.conf 中 path 所指定的目錄不存在，先 `mkdir` 建立。

---

**4. access denied from unknown**

```
@ERROR: access denied to www from unknown (192.168.1.123)
```

hosts allow 未包含該 IP 段，修改 rsyncd.conf：

```
hosts allow = 192.168.1.0/24 192.168.2.0/24
```

---

**5. No route to host**

```
rsync: failed to connect to 172.21.50.8: No route to host (113)
```

對方未開機、防火牆封鎖，或網路上有防火牆阻擋。確認 TCP/UDP 873 埠是否開放，或啟動服務：

```bash
rsync --daemon --config=/etc/rsyncd.conf
```

---

**6. auth failed on module backup（client 端未設密碼檔）**

```
@ERROR: auth failed on module backup
rsync error: error starting client-server protocol (code 5)
```

使用了 `--password-file` 但 client 端未建立對應密碼檔。

---

**7. No space left on device**

```
rsync: recv_generator: mkdir "..." failed: No space left on device (28)
```

磁碟空間已滿，清理空間後重試。

---

**8. Permission denied**

```
rsync: opendir "/kexue" (in dtsChannel) failed: Permission denied (13)
```

同步目錄權限設定錯誤，改為 755：`chmod 755 /kexue`

---

**9. Connection reset by peer**

```
rsync: read error: Connection reset by peer (104)
```

xinetd 守護程序未啟動：`service xinetd start`

---

**10. 找不到 rsyncd.conf**

```
rsync: unable to open configuration file "/etc/rsyncd.conf": No such file or directory
```

xinetd 預設從 /etc 讀取設定檔，確認 rsyncd.conf 存在於 /etc。

---

**11. Connection timed out**

```
rsync: failed to connect to 203.100.192.66: Connection timed out (110)
```

連線逾時，確認伺服器埠號（`netstat -tunlp`）及遠端 telnet 測試連通性。

---

**12. password file must not be other-accessible（600 未設）**

```
ERROR: password file must not be other-accessible
rsync error: syntax or usage error (code 1) at authenticate.c(175)
```

密碼檔權限應設為 600：`chmod 600 /etc/rsyncroot.password`

---

**13. 透過 SSH 免密碼同步**

1. `ssh-keygen` 在 Server A 建立 SSH keys（不設密碼），生成 `~/.ssh/identity` 與 `identity.pub`
2. 在 Server B 建立 `~/.ssh` 目錄
3. 將 A 的 `identity.pub` 複製到 Server B
4. 加入 `~/.ssh/authorized_keys`
5. Server A 即可免密碼 SSH 登入 Server B

---

**14. 透過防火牆使用 rsync**

使用 SSH（port 22）傳輸，建立專用備份帳號，配置 sshd 僅允許該帳號以 RSA 金鑰登入。若伺服器在防火牆內，建議限定 client IP；若 client 在防火牆內，開放 TCP port 22 對外連線即可。

---

**15. 備份已更改或刪除的檔案**

```bash
rsync -av --backup --backup-dir=./backup-$(date +%F) /source/ /dest/
```

---

**16. 開放防火牆埠號**

- 直接傳輸：TCP 873
- 透過 SSH：TCP 22

指定自訂埠號：

```bash
rsync --port 8730 otherhost::
rsync -e 'ssh -p 2002' otherhost:
```

---

**17. Read-only file system**

rsyncd.conf 中忘記設定 `read only = no`。

---

**18. uid 4294967295 (-1) is impossible to set on**

在 server 端的 rsyncd.conf 加入：

```
fake super = yes
```

重啟 rsync 服務後問題解決。
