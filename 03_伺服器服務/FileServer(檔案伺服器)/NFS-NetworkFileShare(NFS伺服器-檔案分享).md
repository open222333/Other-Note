# NFS-NetworkFileShare(NFS伺服器-檔案分享) 筆記

```
```

## 目錄

- [NFS-NetworkFileShare(NFS伺服器-檔案分享) 筆記](#nfs-networkfilesharenfs伺服器-檔案分享-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
	- [CentOS7](#centos7)
- [參數](#參數)
- [NFS 配置文件](#nfs-配置文件)
- [使用者](#使用者)
- [RPC (Remote Procedure Call，RPC)](#rpc-remote-procedure-callrpc)

## 參考資料

[Linux 工具 nfs-utils nfs-kernel-server(Network File System(NFS)遠端掛載)](../../02_作業系統/Unix/Linux/Linux%20工具/Linux%20磁碟與檔案系統部分/Linux%20工具%20nfs-utils%20nfs-kernel-server(Network%20File%20System(NFS)遠端掛載).md)

[RHEL / CentOS 7 安裝 NFS Server](https://www.ltsplus.com/linux/rhel-centos-7-install-nfs-server)

[NFS Server 端設定](https://dywang.csie.cyut.edu.tw/dywang/rhcsaNote/node61.html)

[NFS Client 端設定](https://dywang.csie.cyut.edu.tw/dywang/rhcsaNote/node62.html)

[Linux 磁碟與檔案系統管理](https://linux.vbird.org/linux_basic/centos7/0230filesystem.php#fstab)

[Understanding NFS Port 2049 With Examples](https://www.howtouselinux.com/post/nfs-port)

[Understanding Rpcbind and RPC and Port 111](https://www.howtouselinux.com/post/understanding-rpcbind-and-rpc)

[Understanding Portmap with NFSv3 and Port 111](https://www.howtouselinux.com/post/understanding-portmap-with-examples)

[遠端程序呼叫 Remote Procedure Call，RPC](https://zh.wikipedia.org/zh-tw/%E9%81%A0%E7%A8%8B%E9%81%8E%E7%A8%8B%E8%AA%BF%E7%94%A8)

# 安裝

## CentOS7

```bash
#######################
### NFS Server 安裝 ###
#######################
# 安裝 NFS
yum install nfs-utils -y

# 安裝 rpcbind
yum install rpcbind -y

# 設定分享的目錄, 以下是 /var/nfsshare
mkdir /var/nfsshare
chmod -R 777 /var/nfsshare/

# 開啟 /etc/exports 檔案, 加入以下內容
# 分享目錄	分享給主機(參數)
# /var/nfsshare 192.168.0.11/255.255.255.0(rw,sync,no_root_squash,no_all_squash)
# {sharefolder}  {local_ip}/{mask_ip}(rw,sync,no_root_squash,no_all_squash)
# sharefolder: 要分享的目錄位置
# local_ip / mask_ip: 這台 SERVER 要提供服務所綁定的 IP 網段及可以使用的 IP 網段範圍
# # 參數
    # rw：read-write，可讀寫的權限；
    # ro：read-only，唯讀的權限；
    # sync：資料同步寫入到記憶體與硬碟當中；
    # async：資料會先暫存於記憶體當中，而非直接寫入硬碟。
    # no_root_squash：
    #   登入 NFS 主機使用分享目錄的使用者如果是 root，對於分享的目錄具有 root 的權限。
    #   極不安全，不建議使用。
    # root_squash：
    #   登入 NFS 主機使用分享目錄的使用者如果是 root，權限將被壓縮成匿名使用者，
    #   通常他的 UID 與 GID 都會變成 nobody(nfsnobody) 那個系統帳號的身份；
    # all_squash：
    #   不論登入 NFS 的使用者身份為何，都會被壓縮成為匿名使用者，通常是 nobody(nfsnobody) 。
    # anonuid：
    #   anon 意指 anonymous (匿名者)，自訂匿名使用者的 UID。
    # anongid：自訂匿名使用者的是變成 GID。
vim /etc/exports

# 重新設定 /etc/exports 後，利用 exportfs 處理 nfs 的掛載，可不需重新啟動 nfs。
exportfs [-aruv]
	# 參數：
	# -a ：全部掛載(或卸載) /etc/exports 檔案內的設定
	# -r ：重新掛載 /etc/exports 的設定，亦同步更新 /etc/exports
	# 	及 /var/lib/nfs/xtab 的內容。
	# -u ：卸載某一目錄
	# -v ：在 export 時將分享的目錄顯示到螢幕上。

# 查看可掛載 NFS 伺服器分享的目錄
showmount -e
showmount [-ae] [hostname|IP]
	# 參數：
	# -a ：顯示目前主機與用戶端的 NFS 連線分享的狀態；
	# -e ：顯示某部主機的 /etc/exports 所分享的目錄資料。


# 設定防火牆
firewall-cmd --zone=public --add-service=nfs --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-all

# 啟動 NFS Server, 設定開機自動執行
systemctl start nfs
systemctl enable nfs

systemctl start rpcbind
systemctl enable rpcbind

# systemctl enable nfs-server
# systemctl enable nfs-lock
# systemctl enable nfs-idmap
# systemctl start nfs-server
# systemctl start nfs-lock
# systemctl start nfs-idmap

# 檢查服務狀態
showmount -e [IP]

#######################
### NFS Client 安裝 ###
#######################
# 安裝 NFS
yum install nfs-utils -y

# 建立 NFS 目錄掛載點
mkdir -p /mnt/nfs/var/nfsshare

# 掃瞄 NFS 伺服器分享的目錄
# 查看可掛載 NFS 伺服器分享的目錄
showmount -e
showmount [-ae] [hostname|IP]
	# 參數：
	# -a ：顯示目前主機與用戶端的 NFS 連線分享的狀態；
	# -e ：顯示某部主機的 /etc/exports 所分享的目錄資料。

# 掛載 192.168.0.10 分享出來的目錄
mount -t nfs 192.168.0.10:/var/nfsshare

# 掛載
# sharefolder: server 端分享出來的路徑
# folder_name: client 端要掛載的目錄名稱
mount -t nfs 192.168.187.168:{sharefolder}  {folder_name}

# 掛載 使用 /etc/fstab ,開機即掛載 NFS
# Device Mount point  filesystem parameters dump fsck
# [裝置/UUID等]  [掛載點]  [檔案系統]  [檔案系統參數]  [dump]  [fsck]
# 192.168.0.10:/var/nfsshare /mnt/nfs/var/nfsshare nfs defaults 0 0
vim /etc/fstab

# 查看掛載情形
df -h

# 卸載
umount /home/guests/
```

# 參數

```
NFS 特殊的掛載參數
參數	預設	內容意義

fg/bg
fg
當執行掛載時，該掛載的行為會在前景 (fg) 還是在背景 (bg) 執行。

soft/hard
hard
當兩者之間有任何一部主機離線，hard RPC 會持續的呼叫，直到對方恢復連線為止；soft RPC 會在 time out 後『重複』呼叫，而非『持續』呼叫。

intr
沒中斷
使用 hard 方式掛載若加上 intr，則當 RPC 持續呼叫中，該次呼叫可以被中斷。

rsize/wrize
1024
讀出(rsize)與寫入(wsize)的區塊大小 (block size)。
```

`檔案系統參數 parameters 使用在/etc/fstab`

```
async/sync
非同步/同步
設定磁碟是否以非同步方式運作
預設為 async(效能較佳)

auto/noauto
自動/非自動
當下達 mount -a 時，此檔案系統是否會被主動測試掛載。
預設為 auto。

rw/ro
可讀寫/唯讀
讓該分割槽以可讀寫或者是唯讀的型態掛載上來，如果你想要分享的資料是不給使用者隨意變更的， 這裡也能夠設定為唯讀。
則不論在此檔案系統的檔案是否設定 w 權限，都無法寫入

exec/noexec
可執行/不可執行
限制在此檔案系統內是否可以進行『執行』的工作
如果是純粹用來儲存資料的目錄，那麼可以設定為 noexec 會比較安全。
不過，這個參數也不能隨便使用，因為你不知道該目錄下是否預設會有執行檔。

user/nouser
允許/不允許使用者掛載
是否允許使用者使用 mount 指令來掛載

suid/nosuid
具有/不具有 suid 權限
該檔案系統是否允許 SUID 的存在
如果不是執行檔放置目錄，也可以設定為 nosuid 來取消這個功能

defaults
同時具有 rw, suid, dev, exec, auto, nouser, async 等參數。
基本上，預設情況使用 defaults 設定即可
```

# NFS 配置文件

`/etc/sysconfig/nfs`

```conf
# mound進程設置固定埠;
MOUNTD_PORT=892

# quotad進程設置固定埠;
RQUOTAD_PORT=875

# lockd進程設置固定埠;
LOCKD_TCPPORT=32803
LOCKD_UDPPORT=32769
```

# 使用者

```bash
# 創建用戶
useradd -u 1000 test
# 創建共享目錄
mkdir /share

# 給共享目錄設置test用戶權限
setfacl -m u:test:rwx /share $ cat /etc/exports /share 172.168.0.0/16(rw,async)

```

# RPC (Remote Procedure Call，RPC)

```
現RPC協議的軟體是RPCbind程序包，進程名稱是portmapper。

RPC並不提供任何具體的服務，要想提供具體的服務就必須在Linux上提供具體的軟體。

而 NFS 就是基於RPC協議工作的，如NFS客戶端——-Portmap客戶端——Portmap服務端——NFS服務端。
```