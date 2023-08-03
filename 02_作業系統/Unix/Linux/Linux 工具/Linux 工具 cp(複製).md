# Linux 工具 cp(複製檔案或目錄)

```
```

## 目錄

- [Linux 工具 cp(複製檔案或目錄)](#linux-工具-cp複製檔案或目錄)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[cp(1) — Linux manual page](https://man7.org/linux/man-pages/man1/cp.1.html)

[cp (複製檔案或目錄)](https://linux.vbird.org/linux_basic/centos7/0220filemanager.php#cp)

# 指令

```bash
cp [-adfilprsu] 來源檔(source) 目標檔(destination)
cp [options] source1 source2 source3 .... directory
	選項與參數：
	-a  ：相當於 -dr --preserve=all 的意思，至於 dr 請參考下列說明；(常用)
	-d  ：若來源檔為連結檔的屬性(link file)，則複製連結檔屬性而非檔案本身；
	-f  ：為強制(force)的意思，若目標檔案已經存在且無法開啟，則移除後再嘗試一次；
	-i  ：若目標檔(destination)已經存在時，在覆蓋時會先詢問動作的進行(常用)
	-l  ：進行硬式連結(hard link)的連結檔建立，而非複製檔案本身；
	-p  ：連同檔案的屬性(權限、用戶、時間)一起複製過去，而非使用預設屬性(備份常用)；
	-r  ：遞迴持續複製，用於目錄的複製行為；(常用)
	-s  ：複製成為符號連結檔 (symbolic link)，亦即『捷徑』檔案；
	-u  ：destination 比 source 舊才更新 destination，或 destination 不存在的情況下才複製。
	--preserve=all ：除了 -p 的權限相關參數外，還加入 SELinux 的屬性, links, xattr 等也複製了。
	最後需要注意的，如果來源檔有兩個以上，則最後一個目的檔一定要是『目錄』才行！
# 複製以名稱dir開頭的所有目錄的內容，除了dir2目錄
cp -r `ls -A | grep dir| grep -v "dir2"` /tmp/sahil/

# 複製除dir2以外的所有dir目錄
cp -r !(dir2) /sahil

# 除目錄dir2之外，當前工作目錄中的所有目錄和子目錄都將復製到/ sahil中
cp -r !(file3) /sahil
```
