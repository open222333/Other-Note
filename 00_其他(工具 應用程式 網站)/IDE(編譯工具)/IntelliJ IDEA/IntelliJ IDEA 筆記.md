# IntelliJ IDEA 筆記

```
```

## 目錄

- [IntelliJ IDEA 筆記](#intellij-idea-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [結構相關](#結構相關)
- [iml檔](#iml檔)

## 參考資料

[官方網站](https://www.jetbrains.com/idea/)

[【IntelliJ IDEA 入門指南】Java 開發者的神兵利器](https://ithelp.ithome.com.tw/articles/10255147)

### 結構相關

```
所有特定於專案的檔案都將移至.idea資料夾，如果刪除該專案，它將重新建立。
當構建/編譯專案時，.out資料夾包含專案的輸出，即包含.class檔案。
```

[What is the .idea folder? - .idea資料夾](https://rider-support.jetbrains.com/hc/en-us/articles/207097529-What-is-the-idea-folder-)

[What goes to out folder? - .out資料夾](https://intellij-support.jetbrains.com/hc/en-us/community/posts/206764975-What-goes-to-out-folder-)

# iml檔

```
IML是IntelliJ IDEA的，用來開發Java應用程序的IDE創建的模塊文件。
它存儲有關開發模塊，它可能是Java，插件，Android或Maven的組件信息;節省了模塊路徑，依賴關係和其他設置。
```