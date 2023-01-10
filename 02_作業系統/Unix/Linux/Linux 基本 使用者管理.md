# Linux 基本 使用者管理

## 目錄

- [Linux 基本 使用者管理](#linux-基本-使用者管理)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [新增使用者相關](#新增使用者相關)
- [文檔](#文檔)
	- [紀錄帳號](#紀錄帳號)
	- [規範群組名稱與 GID 的對應](#規範群組名稱與-gid-的對應)
	- [UID/GID密碼參數](#uidgid密碼參數)
- [指令](#指令)
	- [新增使用者(useradd passwd)](#新增使用者useradd-passwd)

## 參考資料

### 新增使用者相關

[Linux 新增使用者 useradd 指令用法教學與範例](https://blog.gtwang.org/linux/linux-useradd-command-tutorial-examples/)

# 文檔

## 紀錄帳號

```
/etc/passwd
結構：
帳號名稱:密碼:UID:GID:使用者資訊說明欄:家目錄:Shell
	UID：
	0 系統管理員 //讓其他的帳號名稱也具有 root 的權限時，將該帳號的 UID 改為 0 即可。
	1~999 系統帳號 //保留給系統使用的 ID，其實除了 0 之外，其他的 UID 權限與特性並沒有不一樣。預設 1000 以下的數字讓給系統作為保留帳號只是一個習慣。

	密碼：
	早期 Unix 系統的密碼放在這欄位，但因為這個檔案的特性是所有的程序都能夠讀取，容易造成密碼資料被竊取， 因此密碼資料改放到 /etc/shadow 中。這裡只會看到一個『 x 』

	Shell：
	使用者登入系統後就會取得一個 Shell 來與系統的核心溝通以進行使用者的操作任務。
```

## 規範群組名稱與 GID 的對應

```
/etc/group
結構：
	群組名稱:群組密碼:GID:此群組支援的帳號名稱


/etc/shadow
結構：
	帳號名稱:密碼:最近更動密碼的日期:密碼不可被更動的天數:密碼需要重新變更的天數:密碼需要變更期限前的警告天數:密碼過期後的帳號寬限時間(密碼失效日):帳號失效日期:保留
```


## UID/GID密碼參數

```
/etc/login.defs

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
ENCRYPT_METHOD SHA512    <==密碼加密的機制使用的是 sha512 這一個機制！
```

# 指令

## 新增使用者(useradd passwd)

```bash
# 新增使用者
sudo useradd gtwang

	# 選項與參數：
	# 	-u  ：後面接的是 UID ，是一組數字。直接指定一個特定的 UID 給這個帳號
	# 	-g  ：後面接的那個群組名稱就是我們上面提到的 initial group 啦～
	# 		該群組的 GID 會被放置到 /etc/passwd 的第四個欄位內。
	# 	-G  ：後面接的群組名稱則是這個帳號還可以加入的群組。
	# 		這個選項與參數會修改 /etc/group 內的相關資料喔！
	# 	-M  ：強制！不要建立使用者家目錄！(系統帳號預設值)
	# 	-m  ：強制！要建立使用者家目錄！(一般帳號預設值)
	# 	-c  ：這個就是 /etc/passwd 的第五欄的說明內容啦～可以隨便我們設定的啦～
	# 	-d  ：指定某個目錄成為家目錄，而不要使用預設值。務必使用絕對路徑！
	# 	-r  ：建立一個系統的帳號，這個帳號的 UID 會有限制 (參考 /etc/login.defs)
	# 	-s  ：後面接一個 shell ，若沒有指定則預設是 /bin/bash 的啦～
	# 	-e  ：後面接一個日期，格式為『YYYY-MM-DD』此項目可寫入 shadow 第八欄位，
	# 		亦即帳號失效日的設定項目囉
	# 	-f  ：後面接 shadow 的第七欄位項目，指定密碼是否會失效。0為立刻失效，
	# 		-1 為永遠不失效(密碼只會過期而強制於登入時重新設定而已。)

# useradd的預設值：
useradd -D

	# 資料位置 /etc/default/useradd
	# 	GROUP=100		<==預設的群組
	# 	HOME=/home		<==預設的家目錄所在目錄
	# 	INACTIVE=-1		<==密碼失效日，在 shadow 內的第 7 欄
	# 	EXPIRE=			<==帳號失效日，在 shadow 內的第 8 欄
	# 	SHELL=/bin/bash		<==預設的 shell
	# 	SKEL=/etc/skel		<==使用者家目錄的內容資料參考目錄
	# 	CREATE_MAIL_SPOOL=yes   <==是否主動幫使用者建立郵件信箱(mailbox)

# 設定密碼
sudo passwd account_name

	# 選項與參數：
	# 	--stdin ：可以透過來自前一個管線的資料，作為密碼輸入，對 shell script 有幫助！
	# 	-l  ：是 Lock 的意思，會將 /etc/shadow 第二欄最前面加上 ! 使密碼失效
	# 	-u  ：與 -l 相對，是 Unlock 的意思！
	# 	-S  ：列出密碼相關參數，亦即 shadow 檔案內的大部分資訊。
	# 	-n  ：後面接天數，shadow 的第 4 欄位，多久不可修改密碼天數
	# 	-x  ：後面接天數，shadow 的第 5 欄位，多久內必須要更動密碼
	# 	-w  ：後面接天數，shadow 的第 6 欄位，密碼過期前的警告天數
	# 	-i  ：後面接天數，shadow 的第 7 欄位，密碼失效天數


# 指定家目錄
sudo useradd -d /data/gtwang gtwang

# 若要讓 useradd 在新增使用者時不要自動建立家目錄（通常用於某些有安全性考量的系統特殊帳號），可以加上 -M 參數：
# 不建立家目錄
sudo useradd -M gtwang

# 系統上的使用者都有一個專屬的使用者 ID（UID），在新增使用者時，useradd 預設會自動以遞增的方式指定一個尚未被使用的 ID 給新的使用者
# 如果我們想要自行指定使用者的 ID，可以使用 -u 參數：
# 指定使用者 ID
sudo useradd -u 1500 gtwang

# 每一位 Linux 使用者都會有一個主要的群組，預設的狀況下 useradd 會自動新增一個跟帳號相同名稱的群組，並設定為該帳號主要的群組。
# 如果要將新的使用者加入既有的群組中，可以加上 -g 參數，並指定群組的名稱或群組 ID：
# 指定主要群組
sudo useradd -g team gtwang

# 加上 -e 參數並指定使用的期限：
# 設定帳號使用期限
sudo useradd -e 2019-03-17 gtwang

# 另一個會產生帳號停用的狀況就是密碼過期，-f 參數可以指定在密碼過期多少天之後，才真正停用帳號：
# 設定帳號使用期限
sudo useradd -f 45 gtwang

# 設定使用者真實姓名
sudo useradd -c "G. T. Wang" gtwang

# 指定登入 Shell
sudo useradd -s /sbin/nologin gtwang


# 骨架目錄
# 	所謂的骨架目錄（skeleton directory）就是一個家目錄的預設範本，裡面包含各種預設的設定檔或目錄（例如 .bashrc 這類的設定檔），在新增使用者並建立家目錄時，就會把骨架目錄中的所有檔案複製過來。

# 系統上預設的骨架目錄是 /etc/skel，若要更改骨架目錄，則可使用 -k 參數：

# 更改骨架目錄
sudo useradd -k /etc/custom.skel gtwang


# 查看shadow使用的加密的機制
authconfig --test | grep hashing

# 觀察系統所有的程序資料
ps aux

# 也是能夠觀察所有系統的資料
ps -lA

# 連同部分程序樹狀態
ps axjf
	# 選項與參數：
	# 	-A ：所有的 process 均顯示出來，與 -e 具有同樣的效用；
	# 	-a ：不與 terminal 有關的所有 process ；
	# 	-u ：有效使用者 (effective user) 相關的 process ；
	# 	x ：通常與 a 這個參數一起使用，可列出較完整資訊。
	# 輸出格式規劃：
	# 	l ：較長、較詳細的將該 PID 的的資訊列出；
	# 	j ：工作的格式 (jobs format)
	# 	-f ：做一個更為完整的輸出。
```