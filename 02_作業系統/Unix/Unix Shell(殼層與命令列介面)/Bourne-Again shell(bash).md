# Bourne-Again shell(bash)

```
Unix shell的一種
```

## 目錄

- [Bourne-Again shell(bash)](#bourne-again-shellbash)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [啟動指令碼](#啟動指令碼)

## 參考資料

[Bash - WIKI](https://zh.wikipedia.org/zh-tw/Bash)

[官方網站](https://www.gnu.org/software/bash/)

# 啟動指令碼

```
啟動指令碼

bash啟動的時候會執行各種不同的指令碼。

當bash作為一個登入的互動shell被呼叫，或者作為非互動shell但帶有--login參數被呼叫時，它首先讀入並執行檔案/etc/profile。然後它會依次尋找~/.bash_profile，~/.bash_login，和~/.profile，讀入並執行第一個存在且可讀的檔案。--noprofile參數可以阻止bash啟動時的這種行為。

當一個登入shell登出時，bash讀取並執行~/.bash_logout檔案，如果此檔案存在。

當一個互動的非登入shell啟動後，bash讀取並執行~/.bashrc檔案。這個行為可以用--norc參數阻止。--rcfile file參數強制bash讀取並執行指定的file而不是預設的~/.bashrc。
```