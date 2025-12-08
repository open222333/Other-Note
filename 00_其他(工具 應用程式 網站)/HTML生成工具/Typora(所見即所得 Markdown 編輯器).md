# Typora(所見即所得 Markdown 編輯器)

```
Typora 是一款所見即所得（WYSIWYG）的 Markdown 編輯器，本身就已內建許多編輯、匯出、轉換工具，不需要安裝外部插件即可使用。

Typora 內建工具（常用）

    1.1 Markdown ⇄ HTML

        輸出 HTML：
        File → Export → HTML
        直接複製為 HTML：
        Edit → Copy as → HTML

    1.2 Markdown ⇄ PDF

        內建 PDF 產生
        File → Export → PDF
        排版由 CSS 控制，可自訂主題。

    1.3 匯出成 Word (.docx)

        Typora 內建調用 Pandoc（如果你有安裝），可以：
        File → Export → Word (.docx)
        如未安裝 Pandoc，要先安裝（ macOS / Linux 都用得上）。

    1.4 匯出成多格式（需要 Pandoc）

        如果電腦安裝了 Pandoc，Typora 會自動啟用更多 export 格式：

            LaTeX
            ePub
            MediaWiki
            OpenOffice / ODT
            DocBook
            RTF
```

## 目錄

- [Typora(所見即所得 Markdown 編輯器)](#typora所見即所得-markdown-編輯器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[官網](https://typora.io/)

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
# 匯入 GPG Key
wget -qO - https://typora.io/linux/public-key.asc | sudo apt-key add -

# 加入 source
sudo add-apt-repository 'deb https://typora.io/linux ./'

# 安裝
sudo apt update
sudo apt install typora
```

## RedHat (CentOS)

```bash
```

## Homebrew (MacOS)

```bash
brew install --cask typora
```

## Windows

[官網 安裝（推薦）](https://typora.io/)

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令

把 doc.md 轉成完整 HTML 檔：

```sh
pandoc doc.md -o doc.html
```

只輸出 HTML fragment（不含 <html> <head>）：

```sh
pandoc --standalone=false doc.md -o fragment.html
```

加入 CSS（產生完整頁面並嵌入指定 CSS）：

```sh
pandoc doc.md -c styles.css -s -o doc.html
```
