# VSCode 擴充套件 EditorConfig for VS Code(設定代碼編寫風格)

```
```

## 目錄

- [VSCode 擴充套件 EditorConfig for VS Code(設定代碼編寫風格)](#vscode-擴充套件-editorconfig-for-vs-code設定代碼編寫風格)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [用法](#用法)
- [範例](#範例)
  - [基本](#基本)

## 參考資料

[editorconfig 官網](https://editorconfig.org/)

[EditorConfig for VS Code 市集](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig)

### 心得相關

[使用 EditorConfig 建立可攜式自訂編輯器設定](https://learn.microsoft.com/zh-tw/visualstudio/ide/create-portable-custom-editor-options?view=vs-2022)

# 用法

`.editorconfig`

指定是否應該在當前文件夾中尋找 .editorconfig 文件的上級文件夾。

如果設置為 true，則不會向上查找其他 .editorconfig 文件。

```ini
root = true
```

# 範例

## 基本

```ini
root = true

[*]
end_of_line = lf
insert_final_newline = true
charset = utf-8
trim_trailing_whitespace = true
indent_style = space
indent_size = 2
```