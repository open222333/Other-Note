# MySQL 工具 Percona XtraBackup(資料備份的工具)

```
Xtrabackup是一個對InnoDB做資料備份的工具，支援線上熱備份(備份時不影響資料讀寫)，是商業備份工具InnoDB Hotbackup的一個很好的替代品。
Xtrabackup有兩個主要的工具：xtrabackup、innobackupex

    xtrabackup只能備份InnoDB和XtraDB兩種資料表，而不能備份MyISAM資料表
    innobackupex是參考了InnoDB Hotbackup的innoback指令碼修改而來的.innobackupex是一個perl指令碼封裝，封裝了xtrabackup。主要是為了方便的 同時備份InnoDB和MyISAM引擎的表，但在處理myisam時需要加一個讀鎖。並且加入了一些使用的選項。如slave-info可以記錄備份恢 復後，作為slave需要的一些資訊，根據這些資訊，可以很方便的利用備份來重做slave。

Xtrabackup可以做

    線上(熱)備份整個庫的InnoDB、 XtraDB表

在xtrabackup的上一次整庫備份基礎上做增量備份(innodb only)
以流的形式產生備份，可以直接儲存到遠端機器上(本機硬碟空間不足時很有用)

MySQL資料庫本身提供的工具並不支援真正的增量備份，二進位制日誌恢復是point-in-time(時間點)的恢復而不是增量備份。
Xtrabackup工具支援對InnoDB儲存引擎的增量備份，工作原理如下：

(1)首先完成一個完全備份，並記錄下此時檢查點的LSN(Log Sequence Number)。
(2)在程序增量備份時，比較表空間中每個頁的LSN是否大於上次備份時的LSN，如果是，則備份該頁，同時記錄當前檢查點的LSN。
首 先，在logfile中找到並記錄最後一個checkpoint(“last checkpoint LSN”)，然後開始從LSN的位置開始拷貝InnoDB的logfile到xtrabackup_logfile接著，開始拷貝全部的資料文 件.ibd在拷貝全部資料檔案結束之後，才停止拷貝logfile。

因為logfile裡面記錄全部的資料修改情況，所以，即時在備份過程中資料檔案被修改過了，恢復時仍然能夠通過解析xtrabackup_logfile保持資料的一致。
```

### innobackupex 對比 xtrabackup

```
xtrabackup：
	檔案格式：備份 innodb、xtradb，不能備份 MyISAM
	innodb 不需要 LOCK 就可以備份
innobackupex：(裡面封裝 xtrabackup 的 script 在裡面)
	支援 innodb、xtradb (靠 xtrabackup) 和 MyISAM (主要做這個)
	MyISAM 備份時會做 READ LOCK
	通常直接使用 innobackupex 即可，若有確認資料庫沒有 MyISAM 格式的話，也可以直接使用 xtrabackup。

xtrabackup 僅複製 InnoDB 數據和日誌。它不會復製表定義文件（.frm 文件）、MyISAM 數據、用戶、權限或位於 InnoDB 數據之外的整個數據庫的任何其他部分。
要備份這些數據，您需要一個包裝腳本，例如 innobackupex。
```

## 目錄

- [MySQL Percona XtraBackup(資料備份的工具)](#mysql-percona-xtrabackup資料備份的工具)
		- [innobackupex 對比 xtrabackup](#innobackupex-對比-xtrabackup)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [Percona XtraBackup(備份工具)相關](#percona-xtrabackup備份工具相關)
		- [XtraBackup 心得相關](#xtrabackup-心得相關)
- [安裝](#安裝)
	- [CentOS7](#centos7)
	- [MacOS](#macos)
	- [Ubuntu](#ubuntu)
- [指令](#指令)
	- [xtrabackup選項](#xtrabackup選項)
		- [用法範例](#用法範例)
		- [innobackupex選項](#innobackupex選項)
		- [備份(即時備份)步驟](#備份即時備份步驟)

## 參考資料

### Percona XtraBackup(備份工具)相關

[percona 官網](https://www.percona.com/software/documentation)

[xtrabackup 選項參考](https://docs.percona.com/percona-xtrabackup/2.4/xtrabackup_bin/xbk_option_reference.html)

[innobackupex 選項參考](https://docs.percona.com/percona-xtrabackup/2.4/innobackupex/innobackupex_option_reference.html)

### XtraBackup 心得相關

[mysql数据库备份：Xtrabackup - ](https://zhuanlan.zhihu.com/p/503948465)

```
xtrabackup：是用於熱備份innodb, xtradb表中數據的工具，不能備份其他類型的表，也不能備份數據表結構
innobackupex：是將xtrabackup進行封裝的perl腳本，可以備份和恢復MyISAM表以及數據表結構
```

[xtrabackup backup and restore mysql database](https://pankajconnect.medium.com/xtrabackup-backup-and-restore-mysql-database-221300bd8fef)

[在 Red Hat Enterprise Linux 和 CentOS 上安裝 Percona XtraBackup](https://docs.percona.com/percona-xtrabackup/2.4/installation/yum_repo.html#whats-in-each-rpm-package)

[Running Percona XtraBackup in a Docker container](https://www.percona.com/doc/percona-xtrabackup/2.4/installation/docker.html)

[bitnami/percona-xtrabackup](https://hub.docker.com/r/bitnami/percona-xtrabackup/)

[Xtrabackup介紹](https://www.itread01.com/content/1547450246.html)

[xtrabackup - 手冊頁](https://www.mankier.com/1/xtrabackup#)

[innobackupex參數說明](https://www.cnblogs.com/weiyiming007/p/10282593.html)

[xtrabackup 使用說明](https://www.cnblogs.com/linhaifeng/articles/15021166.html)

[【MYSQL】Percona XtraBackup 備份指令與還原](https://rosalie1211.blogspot.com/2019/04/mysqlpercona-xtrabackup.html)

[MySQL Percona innobackupex 和 XtraBackup 有何不同？](https://blog.longwin.com.tw/2022/09/mysql-percona-innobackupex-xtrabackup-different-2022/)

[Can I backup remote databases from my local server - 我可以從本地服務器備份遠程數據庫嗎](https://forums.percona.com/t/can-i-backup-remote-databases-from-my-local-server/2334/3)

[dijeesh-mysql_remote_xtrabackup Github](https://github.com/dijeesh/mysql_remote_xtrabackup/blob/master/mysql_remote_stream_backup.sh)

[如何將 mysql xtrabackup 帶到遠程服務器](https://stackoverflow.com/questions/71567854/how-to-take-mysql-xtrabackup-to-a-remote-server)

[MySQL8.0 使用Xtrabackup对数据库进行部分备份恢复](https://www.modb.pro/db/448714)

# 安裝

## CentOS7

```bash
# 安裝 percona-release 的 yum 存儲庫
yum install https://repo.percona.com/yum/percona-release-latest.noarch.rpm -y
# 列出 可安裝套件
yum list | grep percona
# 安裝
yum install percona-xtrabackup-24 -y
```

## MacOS

```bash
# MacOS
brew install percona-xtrabackup
```

## Ubuntu

### Ubuntu 24.04 LTS (MySQL 5.7) (percona-xtrabackup-24)

```bash
wget https://repo.percona.com/apt/percona-release_1.0-25.generic_all.deb
dpkg -i percona-release_1.0-25.generic_all.deb
apt-get update
apt-get install percona-xtrabackup-24 -y
```



```sh
# 新增 Percona 官方 repository（如果還沒裝過）
wget https://repo.percona.com/apt/percona-release_latest.generic_all.deb
dpkg -i percona-release_latest.generic_all.deb

# 啟用 tools（包含 xtrabackup 2.4）
percona-release setup tools

# 更新並安裝 xtrabackup 2.4
apt update
apt install percona-xtrabackup-24
```

#### E: Package 'percona-xtrabackup-24' has no installation candidate

手動下載安裝 XtraBackup 2.4（適用於 MySQL 5.7）

查看 Ubuntu 版本

```sh
lsb_release -a
```

```sh
wget https://downloads.percona.com/downloads/Percona-XtraBackup-2.4/LATEST/binary/debian/jammy/x86_64/percona-xtrabackup-24_2.4.29-1.jammy_amd64.deb

dpkg -i percona-≈-24_2.4.29-1.jammy_amd64.deb
```

安裝依賴（Ubuntu 24.04 可能要補裝依賴庫）

自動安裝缺少的相依套件並修復安裝

```sh
apt-get install -f
```

```sh
apt-cache search percona-xtrabackup
```

#### Percona Server 8.0 (2.4匯出的只能2.4匯入)

ps80 是 Percona Server 8.0 的縮寫，也會加裝 xtrabackup
(2.4匯出的只能2.4匯入)

```sh
wget https://repo.percona.com/apt/percona-release_latest.generic_all.deb
dpkg -i percona-release_latest.generic_all.deb
percona-release setup ps80
apt-get update

apt-get install percona-xtrabackup
```

### Ubuntu 18.04 LTS (MySQL 5.7) (percona-xtrabackup-24)

匯入 Percona APT GPG 金鑰

```sh
wget https://www.percona.com/downloads/RPM-GPG-KEY-percona
apt-key add RPM-GPG-KEY-percona
```

加入 Percona APT 套件來源

```sh
echo "deb http://repo.percona.com/apt bionic main" | sudo tee /etc/apt/sources.list.d/percona.list
```

手動匯入正確的 GPG 公鑰

```sh
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 9334A25F8507EFA5
```

更新套件庫

```sh
apt update
```

安裝 XtraBackup 2.4

```sh
apt install percona-xtrabackup-24 -y
```

驗證是否安裝成功

```sh
xtrabackup --version
```

# 指令

## xtrabackup選項

```bash
# xtrabackup：是用於熱備份innodb, xtradb表中數據的工具，不能備份其他類型的表，也不能備份數據表結構；
xtrabackup
	# --apply-log-only
	# 	prepare備份的時候只執行redo階段，用於增量備份。
	# --backup
	# 	創建備份並且放入--target-dir目錄中
	# --close-files
	# 	不保持文件打開狀態，xtrabackup打開表空間的時候通常不會關閉文件句柄，目的是為了正確處理DDL操作。
	# 	如果表空間數量非常巨大並且不適合任何限制，一旦文件不在被訪問的時候這個選項可以關閉文件句柄.打開這個選項會產生不一致的備份。
	# --compact
	# 	創建一份沒有輔助索引的緊湊備份
	# --compress
	# 	壓縮所有輸出數據，包括事務日誌文件和元數據文件，通過指定的壓縮算法，目前唯一支持的算法是quicklz.結果文件是qpress歸檔格式，每個xtrabackup創建的*.qp文件都可以通過qpress程序提取或者解壓縮
	# --compress-chunk-size
	# 	壓縮線程工作buffer的字節大小，默認是64K
	# --compress-threads
	# 	xtrabackup進行並行數據壓縮時的worker線程的數量，該選項默認值是1，並行壓縮（'compress-threads'）可以和並行文件拷貝('parallel')一起使用。
	# 	例如:'--parallel=4 --compress --compress-threads=2'會創建4個IO線程讀取數據並通過管道傳送給2個壓縮線程。
	# --create-ib-logfile
	# 	這個選項目前還沒有實現，目前創建Innodb事務日誌，你還是需要prepare兩次。
	# --datadir=DIRECTORY
	# 	backup的源目錄，mysql實例的數據目錄。
	# 	從my.cnf中讀取，或者命令行指定。
	# --defaults-extra-file=[MY.CNF]
	# 	在global files文件之後讀取，必須在命令行的第一選項位置指定。
	# --defaults-file=[MY.CNF]
	# 	唯一從給定文件讀取默認選項，必須是個真實文件，必須在命令行第一個選項位置指定。
	# --defaults-group=GROUP-NAME
	# 	從配置文件讀取的組，innobakcupex多個實例部署時使用。
	# --export
	# 	為導出的表創建必要的文件
	# --extra-lsndir=DIRECTORY
	# 	(for --bakcup):在指定目錄創建一份xtrabakcup_checkpoints文件的額外的備份。
	# --incremental-basedir=DIRECTORY
	# 	創建一份增量備份時，這個目錄是增量別分的一份包含了full bakcup的Base數據集。
	# --incremental-dir=DIRECTORY
	# 	prepare增量備份的時候，增量備份在DIRECTORY結合full backup創建出一份新的full backup。
	# --incremental-force-scan
	# 	創建一份增量備份時，強制掃描所有增在備份中的數據頁即使完全改變的page bitmap數據可用。
	# --incremetal-lsn=LSN
	# 	創建增量備份的時候指定lsn。
	# --innodb-log-arch-dir
	# 	指定包含歸檔日誌的目錄。
	# 	只能和xtrabackup --prepare選項一起使用。
	# --innodb-miscellaneous
	# 	從My.cnf文件讀取的一組Innodb選項。
	# 	以便xtrabackup以同樣的配置啟動內置的Innodb。
	# 	通常不需要顯示指定。
	# --log-copy-interval
	# 	這個選項指定了log拷貝線程check的時間間隔（默認1秒）。
	# --log-stream
	# 	xtrabakcup不拷貝數據文件，將事務日誌內容重定向到標準輸出直到--suspend-at-end文件被刪除。
	# 	這個選項自動開啟--suspend-at-end。
	# --no-defaults
	# 	不從任何選項文件中讀取任何默認選項,必須在命令行第一個選項。
	# --databases
	# 	指定了需要備份的數據庫和表。
	# --database-file
	# 	指定包含數據庫和表的文件格式為databasename1.tablename1為一個元素，一個元素一行。
	# --parallel
	# 	指定備份時拷貝多個數據文件並發的進程數，默認值為1。
	# --prepare
	# 	xtrabackup在一份通過--backup生成的備份執行還原操作，以便準備使用。
	# --print-default
	# 	打印程序參數列表並退出，必須放在命令行首位。
	# --print-param
	# 	使xtrabackup打印參數用來將數據文件拷貝到datadir並還原它們。
	# --rebuild_indexes
	# 	在apply事務日誌之後重建innodb輔助索引，只有和--prepare一起才生效。
	# --rebuild_threads
	# 	在緊湊備份重建輔助索引的線程數，只有和--prepare和rebuild-index一起才生效。
	# --stats
	# 	xtrabakcup掃描指定數據文件並打印出索引統計。
	# --stream=name
	# 	將所有備份文件以指定格式流向標準輸出，目前支持的格式有xbstream和tar。
	# --suspend-at-end
	# 	使xtrabackup在--target-dir目錄中生成xtrabakcup_suspended文件。
	# 	在拷貝數據文件之後xtrabackup不是退出而是繼續拷貝日誌文件並且等待知道xtrabakcup_suspended文件被刪除。
	# 	這項可以使xtrabackup和其他程序協同工作。
	# --tables=name
	# 	正則表達式匹配database.tablename。
	# 	備份匹配的表。
	# --tables-file=name
	# 	--tables-file =/tmp/tbname.txt
	# 	指定文件，一個表名一行。
	# --target-dir=DIRECTORY
	# 	指定backup的目的地，如果目錄不存在，xtrabakcup會創建。
	# 	如果目錄存在且為空則成功。
	# 	不會覆蓋已存在的文件。
	# --throttle
	# 	指定每秒操作讀寫對的數量。
	# --tmpdir=name
	# 	當使用--print-param指定的時候打印出正確的tmpdir參數。
	# --to-archived-lsn=LSN
	# 	指定prepare備份時apply事務日誌的LSN，只能和xtarbackup --prepare選項一起用。
	# --user-memory=
	# 	通過--prepare prepare備份時候分配多大內存，目的像innodb_buffer_pool_size。
	# 	默認值100M如果你有足夠大的內存。
	# 	1-2G是推薦值，支持各種單位(1MB,1M,1GB,1G)。
	# --version
	# 	打印xtrabackup版本並退出。
	# --xbstream
	# 	支持同時壓縮和流式化。
	# 	需要客服傳統歸檔tar,cpio和其他不允許動態streaming生成的文件的限制，例如動態壓縮文件，xbstream超越其他傳統流式/歸檔格式的的優點是，並發stream多個文件並且更緊湊的數據存儲（所以可以和--parallel選項選項一起使用xbstream格式進行streaming）。
```

### 用法範例

```bash
### 匯出
# xtrabackup完全複製
xtrabackup --user={$username} --password={$password} --backup --target-dir={$xtrabackup_path}

# $exclude_list 排除的table 例如： db1.table1 db2.table2 db2.table3
xtrabackup --user={$username} --password={$password} --databases-exclude={$exclude_list} --backup --target-dir={$xtrabackup_path}

# 傳送
rsync -Pr {$xtrabackup_path} root@{$slave ip}:{$xtrabackup_path}

### 匯入
# 停止mysql
service mysql stop
systemctl stop mysqld

# 備份mysql目錄(可選)
cd /var/lib
cp -r ./mysql ./mysql.bak

# mysql目錄資料清空
cd mysql
rm -rf ./*

# 準備備份
xtrabackup --prepare --target-dir={$xtrabackup_path}

# 恢復備份
xtrabackup --copy-back --target-dir={$xtrabackup_path}

# mysql目錄權限修改
chown -R mysql.mysql /var/lib/mysql

# 啟動mysql
service mysql start
systemctl start mysqld
```

### innobackupex選項

```bash
innobackupex

	# --apply-log
	# 	該選項表示同xtrabackup的--prepare參數，一般情況下，在備份完成後，數據還不能用於恢復操作，因為備份的數據中可能會包含尚未提交的事務或已經提交但尚未同步至數據文件中的事務。
	# 	因此，此時數據文件仍處理不一致狀態。
	# 	--apply-log(準備)的作用是通過回滾未提交的事務及同步已經提交的事務至數據文件使數據文件處於一致性狀態。
	# 	對xtrabackup的--prepare參數的封裝
	# --backup-locks
	# 	僅支持percona server5.6，如果server不支持，開啟不讀私人和產生影響
	# --close-files
	# 	2.2.5引入的新特性
	# 	關閉不再訪問的文件句柄，這個選項直接傳遞給xtrabackup，當xtrabackup打開表空間通常並不關閉文件句柄目的是正確的處理DDL操作。
	# 	如果表空間數量巨大，這是一種可以關閉不再訪問的文件句柄的方法。
	# 	使用該選項有風險，會有產生不一致備份的可能
	# --compact
	# 	創建一份沒有輔助索引的緊湊的備份，該選項直接傳遞給xtrabackup
	# --compress
	# 	該選項指導xtrabackup壓縮innodb數據文件的backup的拷貝，直接傳遞給xtrabackup的子進程
	# --compress-threads
	# 	該選項指定並行壓縮的worker線程的數量，直接傳遞給xtrabackup的子進程
	# --compress-chunk-size
	# 	這個選項指定每個壓縮線程的內部worker buffer的大小。
	# 	單位是字節，默認是64K。
	# 	直接傳遞給xtrabackup子進程
	# --copy-back
	# 	執行還原操作，從備份目錄中最近的一份備份中拷貝所有文件到datadir，innobackupex --copy-back選項除非指定innobackupex --force-non-empty-directories選項，否則不會拷貝覆蓋所有的文件
	# --databases=LIST
	# 	指定innoabckupex備份的DB列表，該選項接受一個一個字符串參數或者包含DB列表的文件的全路徑。
	# 	如果沒有指定該選項，所有包含innodb和myam表的DB會被備份，請確認--databases包含所有的innodb數據庫和表，以便所有的innodb.frm文件也同樣備份，如果列表非常長的話。
	# 	可以以文件代替
	# --decompress
	# 	解壓所有值錢通過--compress選項壓縮成的.qp文件。
	# 	innodbakcupex --parallel選項允許多個文件同時解壓。
	# 	為了解壓，qpress工具必須有安裝並且訪問這個文件的權限。
	# 	這個進程將在同一個位置移除原來的壓縮/加密文件
	# --decrypt=ENCRYPTION-ALGORITHM
	# 	解密所有之前通過--encrypt選項加密的.xbcrypt文件。
	# --innobackup --parallel選項允許同時多個文件解密
	# --defaults-file=[MY.CNF]
	# 	該選項指定了從哪個文件讀取MySQL配置，必須放在命令行第一個選項的位置
	# --defaults-extra-file=[MY.CNF]
	# 	指定了在標準defaults-file之前從哪個額外的文件讀取MySQL配置，必須在命令行的第一個選項的位置
	# --default-group=GROUP-NAME
	# 	這個選項接受了一個字符串參數指定讀取配置文件的group，在一機多實例的時候需要指定
	# --encrypt=ENCRYPTION_ALGORITHM
	# 	該選項指定了xtrabackup通過ENCRYPTION_ALGORITHM的算法加密innodb數據文件的備份拷貝，該選項直接傳遞給xtrabackup子進程
	# --encrypt-key=ENCRYPTION_KEY
	# 	指導xtrabackup使用了--encrypt選項時候使用ENCRYPTION_KEY這個KEY，直接傳遞給xtrabackup子進程
	# --encrypt-key-file=ENCRYPTION_KEY_FILE
	# 	這個選項告訴xtrabackup使用--encrypt的時候。
	# Key存在了ENCRYPTION_KEY_FILE這個文件中
	# --encrypt-chunk-size=#
	# 	這個選項指定了每個加密線程內部worker buffer的大小，單位字節，直接傳遞給xtrabackup子進程
	# --export=DIRECTORY
	# 	這個選項直接傳遞給xtrabackup --export選項。
	# 開啟可導出單獨的表之後再導入其他Mysql中
	# --extra-lsndir=DIRECTORY
	# 	這個選項接受一個字符串參數指定保存額外一份xtrabackup_checkpoints文件的目錄，直接傳遞給xtrabackup --extra-lsndir選項
	# --force-non-empty-directories
	# 	指定該參數時候，使得innobackupex --copy-back或innobackupex --move-back選項轉移文件到非空目錄，已存在的文件不會被覆蓋，如果--copy- back和--move-back文件需要從備份目錄拷貝一個在datadir已經存在的文件，會報錯失敗
	# --galera-info
	# 	該選項生成了包含創建備份時候本地節點狀態的文件xtrabackup_galera_info文件，該選項只適用於備份PXC。
	# --history=NAME
	# 	percona server5.6的備份歷史記錄在percona_schema.xtrabackup_history表
	# --host=HOST
	# 	選項指定了TCP/IP連接的數據庫實例IP
	# --ibbackup=IBBACKUP-BINARY
	# 	這個選項指定了使用哪個xtrabackup二進製程序。IBBACKUP-BINARY是運行percona xtrabackup的命令。
	# 	這個選項適用於xtrbackup二進制不在你是搜索和工作目錄，如果指定了該選項，innoabackupex自動決定用的二進製程序
	# --include=REGEXP
	# 	正則表達式匹配表的名字[db.tb]，直接傳遞給xtrabackup --tables選項。
	# --incremental
	# 	這個選項告訴xtrabackup創建一個增量備份，直接傳遞給xtrabakcup子進程，當這個選項指定，需要同時指定--incremental-lisn或者--incremental-basedir。
	# 如果沒有指定，默認傳給xtrabackup --incremental-basedir，值為Backup BASE目錄中的第一個時間戳目錄
	# --incremental-basedir=DIRECTORY
	# 	這個選項接受了一個字符串參數指定含有full backup的目錄為增量備份的base目錄，與--incremental同時使用
	# --incremental-dir=DIRECTORY
	# 	指定了增量備份的目錄，結合full backup生成生成一份新的full bakcup
	# --incremettal-history-name=NAME
	# 	這個選項指定了存儲在PERCONA_SCHEMA.xtrabackup_history基於增量備份的歷史記錄的名字。Percona Xtrabackup搜索歷史表查找最近（innodb_to_lsn）成功備份並且將to_lsn值作為增量備份啟動出事lsn.與innobackupex--incremental-history-uuid互斥。如果沒有檢測到有效的lsn，xtrabackup會返回error
	# --incremetal-history-uuid=UUID
	# 	這個選項指定了存儲在percona_schema.xtrabackup_history基於增量備份的特定歷史記錄的UUID
	# --incremental-lsn=LSN
	# 	這個選項指定增量備份的LSN，與--incremental選項一起使用
	# --kill-long-queries-timeout=SECONDS
	# 	這個選項指定innobackupex從開始執行FLUSH TABLES WITH READ LOCK到kill掉阻塞它的這些查詢之間等待的秒數，默認值為0.以為著Innobakcupex不會kill任何查詢，使用這個選項xtrabackup需要有Process和super權限。

	# --kill-long-query-type=all|select
	# 	指定kill的類型，默認是all
	# --ftwrl-wait-timeout=SECONDS
	# 	執行FLUSH TABLES WITH READ LOCK之前，innobackupex等待阻塞查詢執行完成等待秒數，超時的時候如果查詢仍然沒有執行完，innobackupex會終止並報錯，默認為0，innobakcupex不等待查詢完成立刻FLUSH
	# --ftwrl-wait-threshold=SECONDS
	# 	指定innoabckupex檢測到長查詢和innobackupex --ftwrl-wait-timeount不為0，這個長查詢可以運行的閾值，
	# --ftwrl-wait-query-type=all|update
	# 	指定innobakcupex獲得全局鎖之前允許那種查詢完成，默認是ALL
	# --log-copy-interval=
	# 	這個選項指定了每次拷貝log線程完成檢查之間的間隔（毫秒）
	# --move-back
	# 	從備份目錄中將最近一份備份中的所有文件移動到datadir目錄中
	# --no-lock
	# 	關閉FTWRL的表鎖，只有在你所有表都是Innodb表並且你不關心backup的binlog pos點
	# 	如果有任何DDL語句正在執行或者非InnoDB正在更新時（包括mysql庫下的表） ，都不應該使用這個選項，後果是導致備份數據不一致
	# 	如果考慮備份因為獲得鎖失敗，，可以考慮--safe-slave-backup立刻停止複制線程
	# --no-timestamp
	# 	這個選項阻止在BACKUP-ROOT-DIR裡創建一個時間戳子目錄，指定了該選項的話，備份在BACKUP-ROOT-DIR完成
	# --no-version-check
	# 	這個選項禁用由--version-check打開的version check
	# --parallel=NUMBER-OF-THREADS
	# 	指定xtrabackup並行複制的子進程數。
	# 	注意是文件級別並行，如果有多個ibd文件，他們會並行拷貝，如果所有的表存在一個表空間文件中，沒有任何作用
	# 	直接傳遞給xtrabakcup --parallel選項
	# --password = PASSWORD
	# --port = PORT
	# --rebuild-indexes
	# 	與--apply-log一起用時候才有效。
	# 	並且直接傳遞給xtrabackup，在apply log之後重建所有輔助索引，該選項用於Prepare緊湊備份。
	# --rebuild-threads=NUMBER-OF-THREADS
	# 	與--apply-log和--rebuild-index選項一起用時候才生效，重建索引的時候，xtrabacup以指定的線程數並行的處理表空間文件
	# --redo-only
	# 	這個選項在prepare base full backup，往其中merge增量備份（但不包括最後一個）時候使用。
	# 	傳遞給xtrabackup --apply-log-only的選項。
	# 	這個強制xtrabackup跳過rollback並且只重做redo
	# --rsync
	# 	通過rsync工具優化本地傳輸，當指定這個選項，innobackupex使用rsync拷貝非Innodb文件而替換cp，當有很多DB和表的時候會快很多。
	# 	不能--stream一起使用
	# --safe-slave-backup
	# 	指定的時候innobackupex會在執行FLUSH TABLES WITH READ LOCK停止sql線程，並且直到show status裡slave_open_temp_tables的值為0的時候start backup。
	# 	如果沒有打開的臨時表，就開始備份，否則sql線程start或者stop直到沒有打開的臨時表，如果在innobackupex --safe-slave-backup-timeout之後slave_open_temp_tables的值仍沒有變成0備份就會失敗。
	# 	SQL線程會在backup完成之後重啟。
	# --safe-slave-backup-timeout=SECONDS
	# 	innobackupex --safe-slave-backup應該等多少秒等slave_open_temp_tables變成0，默認是300秒
	# --scpopt=SCP-OPTIONS
	# 	當--remost-host指定的時候，指定傳給scp的命令行選項。
	# 	如果沒有指定，默認為-Cp -c arcfour
	# --slave-info
	# 	對slave進行備份的時候使用，打印出master的名字和binlog pos，同樣將這些信息以change master的命令寫入xtrabackup_slave_info文件。
	# 	可以通過基於這份備份啟動一個從庫並且保存在xtrabackup_slave_info文件中的binlog pos點創建一個新的從庫
	# --socket
	# 	連接本地實例的時候使用
	# --sshopt=SSH-OPTIONS
	# 	在指定了--remost-host的時候，指定傳給ssh的命令行選項
	# --stream=STREAMNAME
	# 	流式備份的格式，backup完成之後以指定格式到STDOUT，目前只支持tar和xbstream。
	# 	使用xbstream為percona xtrabakcup髮型版本，如果在這個選項之後指定了路徑。
	# 	會理解值為tmpdir
	# --tables-file=FILE
	# 	指定含有表列表的文件，格式為database.table，該選項直接傳給xtrabackup --tables-file
	# --throttle=IOS
	# 	指定每秒IO操作的次數，直接傳遞給xtrabackup --throttle選項。
	# 	只作用於bakcup階段有效。
	# 	apply-log和--copy-back不生效不要一起用
	# --tmpdir=DIRECTORY
	# 	指定--stream的時候，指定臨時文件存在哪裡，在streaming和拷貝到遠程server之前，事務日誌首先存在臨時文件裡。
	# --use-memory=#
	# 	只能和--apply-log選項一起使用，prepare a backup的時候，xtrabackup做crash recovery分配的內存大小，單位字節。
	# 	也可(1MB、1M、1G、1GB)，直接傳給xtrabackup --use-memory選項
	# --version
	# 	顯示Innobackupex版本和版權信息後退出
	# --version-check
	# 	innobackupex在與server創建連接之後的備份階段進行版本檢查
```

### 備份(即時備份)步驟

```bash
###############
### 來源主機 ###
###############
# master全表鎖定只讀 - mysql指令
FLUSH TABLES WITH READ LOCK;

# 檢查master是否lock - mysql指令
# bin_log資料(紀錄file和position)
SHOW MASTER STATUS;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |     2584 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
# {下方設定 $master bin_log filename} = mysql-bin.000001
# {下方設定 $log position} = 2584

# 備份(可先lock MySQL Table(表))
xtrabackup --user={$username} --password={$password} --backup --target-dir={$xtrabackup_path}

# 將/data目錄傳到mysql-slave機器
rsync -Pr {$xtrabackup_path} root@{$slave ip}:{$xtrabackup_path}

# 解開lock - mysql指令
UNLOCK TABLES;

###############
### 目標主機 ###
###############
# 停止mysql
service mysql stop
systemctl stop mysqld

# 備份mysql目錄
cd /var/lib
cp -r ./mysql ./mysql.bak

# mysql目錄資料清空
cd mysql
rm -rf ./*

# 準備備份
xtrabackup --prepare --target-dir={$xtrabackup_path}

# 恢復備份
xtrabackup --copy-back --target-dir={$xtrabackup_path}

# 排除部分資料表
xtrabackup --user=$MYSQLDB_USER --password=$MYSQLDB_PASS --databases-exclude="db1.table1 db2.table2" --backup --target-dir={$xtrabackup_path}

# mysql目錄權限修改
chown -R mysql.mysql /var/lib/mysql

# 啟動mysql
service mysql start
systemctl start mysqld
```
