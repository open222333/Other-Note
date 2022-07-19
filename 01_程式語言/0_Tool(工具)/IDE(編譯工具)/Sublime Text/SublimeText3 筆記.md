# SublimeText3 筆記

```
```

## 目錄

- [SublimeText3 筆記](#sublimetext3-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [快捷鍵](#快捷鍵)
- [設定檔](#設定檔)
	- [20220118](#20220118)
	- [20220204](#20220204)

## 參考資料

[官方文檔](https://www.sublimetext.com/docs/)


# 快捷鍵

```
command + T：查詢/前往文件
command + shift + T：重新打開最近關閉的文件
command + R：查詢/前往 funcdtion | method
command + L：選擇當前光標整行
command + D：選擇當前光標所在單詞，繼續按 command + D 同時選中下一個同樣單詞，被選中的單詞可被同時修改
command + shift + D：複製當前行
command + J：合併行
control + shift + K 或 command + X：刪除當前行
command + K + K：刪除光標到行尾
command + delete：刪除光標到行首
command + K + U：轉換為大寫
command + K + L：轉換為小寫
Comamand + shift + V：粘貼並縮進
command + shift + F：查找與替換
command + /：註釋 / 取消註釋
control + M：前往匹配的括號
command + option + V：從歷史中粘貼
command + P：跳轉、前往文件、前往項目、命令提示、前往 method 等
control + G：前往指定行
control + -：光標跳回上一個位置
control + shift + -：光標位置恢復
control + `：打開控制台
command + control + ↑ 或 ↓：當前行向上或向下整行移動
command + enter：當前行後插入新行
command + shift + enter：當前行前插入新行
command + 數字：跳轉到對應的標籤頁，用於快速切換不同文件
command + option + 1或2或3或4或5：分屏為 1 列或 2列或 3列或 4列 或網格 4 組
control + 數字： 焦點移動到相應的分屏
control + shift + 數字： 移動當前文件到相應的分屏
```

# 設定檔

Sublime_setting.json
```json
{
	"show_encoding": true,
	"save_on_focus_lost": true,
	"folder_exclude_patterns":[".tmp.drivedownload",".tmp.driveupload"],
	"file_exclude_patterns":[".DS_Store"],
}

{
	"show_encoding": true,
	"save_on_focus_lost": true,
	"folder_exclude_patterns":[".tmp.drivedownload", ".tmp.driveupload", "icon"],
	"file_exclude_patterns":[".DS_Store", "Icon?", "icon"],
	"font_size": 14,
}
```
---
## 20220118

```json
{
	"tab_size": 4,
	"word_wrap": "true",
	"show_encoding": true,
	"font_size": 18,
	"save_on_focus_lost": true,
	"folder_exclude_patterns":[
		".tmp.drivedownload",
		".tmp.driveupload",
		"icon"
	],
	"file_exclude_patterns":[
		"icon",
		"Icon?",
		".DS_Store",
	],
}
```
---
## 20220204

```json
{
	"tab_size": 4,
	"word_wrap": "true",
	"show_encoding": true,
	"font_size": 18,
	"save_on_focus_lost": true,
	// 不顯示的資料夾
	"folder_exclude_patterns":[
		".tmp.drivedownload",
		".tmp.driveupload",
		"icon"
	],
	// 不顯示的檔案
	"file_exclude_patterns":[
		"icon",
		"Icon?",
		".DS_Store",
	],
	// https://www.sublimetext.com/docs/indentation.html
	"translate_tabs_to_spaces": false,
	"detect_indentation": false,
    // 刪除不必要空白
    "trim_trailing_white_space_on_save": true,
}
```
