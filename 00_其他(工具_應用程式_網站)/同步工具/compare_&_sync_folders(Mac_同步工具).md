# compare & sync folders(Mac 同步工具)

```
```

## 目錄

- [compare \& sync folders(Mac 同步工具)](#compare--sync-foldersmac-同步工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [規則](#規則)
  - [A \< + \> B](#a----b)
  - [A \< - \> B](#a-----b)

## 參考資料

[apple 商店](https://apps.apple.com/tw/app/compare-sync-folders/id1001460601?mt=12)

[同步規則](https://www.greenworldsoft.com/compare-sync-folders-help.php#Sync-modes)

# 規則

## A < + > B

`雙向同步（Bidirectional synchronization）不追蹤刪除（without tracking deletions）`

```
這種同步方式保守又安全，不會因為誤刪檔案導致資料損失。適合：

雙端備份

USB 隨身碟同步

需要手動決定刪除的場景
```

新增或修改的檔案會在兩邊同步

刪除的檔案不會被同步刪除

## A < - > B

`雙向同步（含刪除追蹤）`

```
若同步的是一個本機資料夾與一個網路資料夾，而那個網路資料夾「一開始是空的或斷線了」，系統會認為：

網路端刪除了所有檔案

結果本機端也會被刪除相同檔案
```

同步會追蹤新增、修改、刪除

所有變更都會反映在對方資料夾中

同步後，兩個位置內容完全一致（鏡像）
