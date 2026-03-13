# VSCode 擴充套件 Markdown Preview Enhanced(Markdown 插件)

```
```

## 目錄

- [VSCode 擴充套件 Markdown Preview Enhanced(Markdown 插件)](#vscode-擴充套件-markdown-preview-enhancedmarkdown-插件)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [開啟 MPE 預覽](#開啟-mpe-預覽)
  - [建立自己的 CSS（假設叫 style.css）](#建立自己的-css假設叫-stylecss)
  - [MPE 匯出 HTML 命令](#mpe-匯出-html-命令)

## 參考資料

[官方文檔](https://shd101wyy.github.io/markdown-preview-enhanced/#/zh-tw/?id=markdown-preview-enhanced)

# 用法

## 開啟 MPE 預覽

在 Markdown 檔案內按：

```
Ctrl + Shift + V
```

或右鍵 → Markdown Preview Enhanced: Open Preview

## 建立自己的 CSS（假設叫 style.css）

```
project/
   vpn.md
   style.css
```

在 Markdown 開頭加入：

```markdown
<!-- MPE Styles -->
<style src="style.css"></style>
```

## MPE 匯出 HTML 命令

```
Ctrl + Shift + P
```
