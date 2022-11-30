# JavaScript JQuery 套件 JqGrid(顯示資料表)

```
```

## 目錄

- [JavaScript JQuery 套件 JqGrid(顯示資料表)](#javascript-jquery-套件-jqgrid顯示資料表)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [安裝相關](#安裝相關)
		- [範例相關](#範例相關)
			- [複選框 multiselect](#複選框-multiselect)
- [用法](#用法)
	- [使用步驟](#使用步驟)
	- [複選框 multiselect](#複選框-multiselect-1)

## 參考資料

[官方文檔](http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs)

[Demo](http://trirand.com/blog/jqgrid/jqgrid.html)

[教學](https://www.guriddo.net/documentation/guriddo/javascript/)

[jqGrid 參數](http://www.trirand.com/jqgridwiki/doku.php?id=wiki:options)

### 安裝相關

[官網 詳細步驟](http://www.trirand.com/jqgridwiki/doku.php?id=wiki:how_to_install)

### 範例相關

[jQuery學習筆記——jqGrid的使用記錄（實現分頁、搜尋功能）](https://codertw.com/%E5%89%8D%E7%AB%AF%E9%96%8B%E7%99%BC/253022/)

[Jqgrid之onSelectRow 和onCellSelect 事件不能一起用-yellowcong](https://blog.csdn.net/yelllowcong/article/details/78890992)

[jqGrid表格添加‘操作’列，自定义按钮](https://blog.csdn.net/nnaay_/article/details/104670844)

[選定的行背景顏色](https://stackoverflow.com/questions/4305223/selected-row-background-color)

[JQGrid Header grouping 多重標頭](http://www.trirand.com/jqgridwiki/doku.php?id=wiki:groupingheadar)

[子網格 subGrid - JqGrid - search using subgrid columns and rebuild main grid(jqGrid：使用子網格列搜索並重建主網格)](https://stackoverflow.com/questions/33530408/jqgrid-search-using-subgrid-columns-and-rebuild-main-grid)

[子網格 subGrid - SubGrid](https://www.guriddo.net/documentation/guriddo/javascript/user-guide/subgrid/)

[樹狀網格 treegrid - Demo](http://trirand.com/blog/jqgrid/jqgrid.html)

#### 複選框 multiselect

[複選框 multiselect - selecting all the rows of all the pages of a jqgrid programmatically?](https://stackoverflow.com/questions/24935133/selecting-all-the-rows-of-all-the-pages-of-a-jqgrid-programmatically/24941828#24941828)

[複選框 multiselect - jqGrid: How to use multiselect on different pages](https://stackoverflow.com/questions/11567915/jqgrid-how-to-use-multiselect-on-different-pages)

[複選框 multiselect - Demo](http://trirand.com/blog/jqgrid/jqgrid.html)

[複選框 multiselect - 【jquery-jqGrid外掛】jqGrid 多選 複選框 編輯列](https://www.itread01.com/content/1548541448.html)

[複選框 multiselect - jqGrid 翻頁行保持選中](https://programmer.group/jqgrid-remains-selected-when-using-multiselect-to-turn-pages.html)

[複選框 multiselect - jqGrid: How to use multiselect on different pages(jqGrid：如何在不同頁面上使用多選)](https://stackoverflow.com/questions/11567915/jqgrid-how-to-use-multiselect-on-different-pages)

[複選框 multiselect - 第4個jqGrid範例: 資料列處理(取得選取的資料 以及 新增按鈕)](http://jdev.tw/blog2/1640/jqgrid-data-manipulation)

# 用法

## 使用步驟

1. 下載 jqGrid
    http://www.trirand.com/blog/?page_id=6

2. 創建 js css 資料夾 放入下載資料 如以下：

```
/myproject/css/
	ui.jqgrid.css
	/ui-lightness/
		/images/
		jquery-ui-1.7.2.custom.css
/myproject/js/
	/i18n/
		grid.locale-bg.js
		list of all language files
		….
	Changes.txt
	jquery-1.4.2.min.js
	jquery.jqGrid.min.js
```

## 複選框 multiselect

jqGrid Method的執行方法語法是：

```js
$("#grid_id").jqGrid('方法', 參數 [,參數...]);
```

部份常用方法列表如下：

```
方法	說明
getRowData	取得列物件
delRowData	刪除指定的資料列
setRowData	修改指定的資料列
setSelection	切換指定的列為勾選或不勾選
addRowData	新增一筆資料列到最後
sortGrid	對Grid資料做排序處理
reloadGrid	重新讀資料到Grid
```

部份Grid參數(getGridParam)：

```
參數	說明
url	取資料的網址
sortname	排序的欄名
sortorder	排序是升冪或降冪
selrow	取得被選取列
selarrrow	多選狀態下取得被選取的列記錄(select array row)
page	取得目前頁碼
rowNum	要求的列數
datatype	資料類型
records	Grid裡的記錄筆數
```
