# Linux 工具 rsync(同步鏡像備份)

```
```

## 目錄

- [Linux 工具 rsync(同步鏡像備份)](#linux-工具-rsync同步鏡像備份)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [深入學習相關](#深入學習相關)
    - [排錯相關](#排錯相關)
- [安裝](#安裝)
- [指令](#指令)
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
# 安裝 rsync
# 在 Ubuntu 或 Debian 上：
apt-get update
apt-get install rsync

# 在 CentOS 或 Red Hat 上：
yum install rsync

# 在 Fedora 上：
dnf install rsync

# 在 openSUSE 上：
zypper install rsync
```

# 指令

```bash
rsync [-avrlptgoD] [-e ssh] [user@host:/dir] [/local/path]
	選項與參數：
	-v ：觀察模式，可以列出更多的資訊，包括鏡像時的檔案檔名等；
	-q ：與 -v  相反，安靜模式，略過正常資訊，僅顯示錯誤訊息；
	-r ：遞迴複製！可以針對『目錄』來處理！很重要！
	-u ：僅更新 (update)，若目標檔案較新，則保留新檔案不會覆蓋；
	-l ：複製連結檔的屬性，而非連結的目標原始檔案內容；
	-p ：複製時，連同屬性 (permission) 也保存不變！
	-g ：保存原始檔案的擁有群組；
	-o ：保存原始檔案的擁有人；
	-D ：保存原始檔案的裝置屬性 (device)
	-t ：保存原始檔案的時間參數；
	-I ：忽略更新時間 (mtime) 的屬性，檔案比對上會比較快速；
	-z ：在資料傳輸時，加上壓縮的參數！
	-e ：使用的通道協定，例如使用 ssh 通道，則 -e ssh
	-a ：相當於 -rlptgoD ，所以這個 -a 是最常用的參數了！
	-P, --progress :顯示進度
	--temp-dir=/tmp :指定暫存檔位置
	--remove-source-files :同步完成刪除原檔

rsync -av --progress [user@host:/dir] [/local/path]

# 利用 student 的身份登入 clientlinux.centos.vbird 將家目錄複製到本機 /tmp
rsync -v student@192.168.100.10:~ /tmp

# 排除特定文件/目錄的複制
# 使用rysnc命令複製文件或文件夾，請使用–exclude標誌
rsync -av --progress --exclude="dir2" dir* /sahilsending incremental file listdir1/dir3/dir4/dir5/
sent 82 bytes received 28 bytes 220.00 bytes/sectotal size is 0 speedup is 0.00[root@linuxnix tmp]

# 遠程複製文件時使用–exclude標誌
rsync -av --progress --exclude="dir2" dir* 192.168.19.142:/sahilsending incremental file listdir1/dir3/dir4/dir5/
```

``

```bash
rsync -av /source/ /dest/ --log-file=mylog.log
```

`執行過程中的標準錯誤輸出儲存到文件`

source: 源目錄或文件的路徑，表示要從哪裡同步文件和目錄。可以是本地文件系統的路徑，也可以是遠程系統的路徑（使用 SSH 協議）。

destination: 目標目錄的路徑，表示同步後文件和目錄將存儲在這個目錄下。同步操作會將 source 中的文件和目錄同步到 destination 中。

```bash
rsync [options] source destination 2> error.log
```

`同時保存標準輸出和標準錯誤到同一個文件`

```bash
rsync [options] source destination > output.log 2>&1
```

# rsync 與 scp 區別

```
主要區別在於它們複製文件的方式。

scp基本上讀取源文件並將其寫入目標。它在本地或通過網絡執行簡單的線性複制。

rsync還可以在本地或通過網絡複製文件。但它採用特殊的增量傳輸算法和一些優化來使操作更快。考慮通話。

rsync有大量的命令行選項，允許用戶微調其行為。它支持複雜的過濾規則，以批處理模式、守護模式等方式運行，scp只有幾個開關。
```

# 狀況

```
1.@ERROR: auth failed on module xxxxx
    rsync: connection unexpectedly closed (90 bytes read so far)
    rsync error: error in rsync protocol data stream (code 12) at io.c(150)
    这是因为密码设错了, 无法登入成功, 请检查一下 rsyncd.scrt 中的密码, 二端是否一致? 


2.password file must not be other-accessible 
    continuing without password file 
    Password:
    这表示 rsyncd.scrt 的档案权限属性不对, 应设为 600。


3.@ERROR: chroot failed
    rsync: connection unexpectedly closed (75 bytes read so far)
    rsync error: error in rsync protocol data stream (code 12) at io.c(150) 　　
    这通常是您的 rsyncd.conf 中的 path 路径所设的那个目录并不存在所致.请先用 mkdir开设好要备份目录


4.@ERROR: access denied to www from unknown (192.168.1.123)
    rsync: connection unexpectedly closed (0 bytes received so far) [receiver]
    rsync error: error in rsync protocol data stream (code 12) at io.c(359)
    最后原因终于找到了。因为有两个网段都需要同步该文件夹内容，但没有在hosts allow 后面添加另一个IP段
    hosts allow = 192.168.1.0/24
    改为
    hosts allow = 192.168.1.0/24 192.168.2.0/24
    重新启动rsync服务，问题解决 


5.rsync: failed to connect to 172.21.50.8: No route to host (113)
    rsync error: error in socket IO (code 10) at clientserver.c(104) [receiver=2.6.9]
    对方没开机、防火墙阻挡、通过的网络上有防火墙阻挡，都有可能。关闭防火墙，其实就是把tcp udp 的873端口打开
    启动服务：rsync --daemon --config=/etc/rsyncd.conf


6.@ERROR: auth failed on module backup
    rsync error: error starting client-server protocol (code 5) at main.c(1506) [Receiver=3.0.7]
    client端没有设置/etc/rsync.pas这个文件，而在使用rsync命令的时候，加了这个参数--password-file=/etc/rsync.scrt 


7.rsync: recv_generator: mkdir "/teacherclubBackup/rsync……" failed: No space left on device (28)
    *** Skipping any contents from this failed directory ***
    磁盘空间满了 


8.rsync: opendir "/kexue" (in dtsChannel) failed: Permission denied (13)
    同步目录的权限设置不对，改为755 


9.rsync: read error: Connection reset by peer (104)
    rsync error: error in rsync protocol data stream (code 12) at io.c(759) [receiver=3.0.5]
    未启动xinetd守护进程
    [root@CC02 /]# service xinetd start


10.rsync: unable to open configuration file "/etc/rsyncd.conf": No such file or directory
    xnetid查找的配置文件位置默认是/etc下，在/etc下找不到rsyncd.conf文件


11.rsync: failed to connect to 203.100.192.66: Connection timed out (110)
    rsync error: error in socket IO (code 10) at clientserver.c(124) [receiver=3.0.5]
    连接服务器超时，检查服务器的端口netstat –tunlp，远程telnet测试


12.[root@client cron.daily.rsync]# sh root.sh  
    ERROR: password file must not be other-accessible
    rsync error: syntax or usage error (code 1) at authenticate.c(175) [Receiver=3.0.9]
    创建密码文件，root用户用的是 rsyncroot.password，权限是600


13.如何通过ssh进行rsync，而且无须输入密码？
　　可以通过以下几个步骤
　　1. 通过ssh-keygen在server A上建立SSH keys，不要指定密码，你会在~/.ssh下看到identity和identity.pub文件 
　　2. 在server B上的home目录建立子目录.ssh
　　3. 将A的identity.pub拷贝到server B上
　　4. 将identity.pub加到~[user b]/.ssh/authorized_keys
　　5. 于是server A上的A用户，可通过下面命令以用户B ssh到server B上了。e.g. ssh -l userB serverB。这样就使server A上的用户A就可以ssh以用户B的身份无需密码登陆到server B上了。


14.如何通过在不危害安全的情况下通过防火墙使用rsync?
　　解答如下：
　  这通常有两种情况，一种是服务器在防火墙内，一种是服务器在防火墙外。无论哪种情况，通常还是使用ssh，这时最好新建一个备份用户，并且配置sshd 仅允许这个用户通过RSA认证方式进入。如果服务器在防火墙内，则最好限定客户端的IP地址，拒绝其它所有连接。如果客户机在防火墙内，则可以简单允许防 火墙打开TCP端口22的ssh外发连接就ok了。


15.我能将更改过或者删除的文件也备份上来吗？
　　当然可以。你可以使用如：rsync -other -options -backupdir = ./backup-2000-2-13  ...这样的命令来实现。这样如果源文件:/path/to/some/file.c改变了，那么旧的文件就会被移到./backup- 2000-2-13/path/to/some/file.c，这里这个目录需要自己手工建立起来


16.我需要在防火墙上开放哪些端口以适应rsync？
　　视情况而定。rsync可以直接通过873端口的tcp连接传文件，也可以通过22端口的ssh来进行文件传递，但你也可以通过下列命令改变它的端口：
　　rsync --port 8730 otherhost::
　　或者
　　rsync -e 'ssh -p 2002' otherhost:


17.我如何通过rsync只复制目录结构，忽略掉文件呢？　
　　rsync -av --include '*/' --exclude '*' source-dir dest-dir


18.为什么我总会出现"Read-only file system"的错误呢？
　　看看是否忘了设"read only = no"了



19. uid 4294967295 (-1) is impossible to set on

        在server端添加一行参数：

        fake super = yes

        重启rsync服务即可。再同步就不会出现错误信息了。
```