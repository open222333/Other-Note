# Hugo(靜態網站產生器(Static Site Generator，SSG))

```
寫 Markdown (.md)
Hugo 自動把它轉成 完整的 HTML 網站（含排版、主題、美化、選單、TOC、列表、目錄樹）

不需要資料庫、不需要 PHP、不需要後端。
輸出的網站在任何地方都能放：

✔ GitHub Pages
✔ Cloudflare Pages
✔ Netlify
✔ Nginx / Apache
✔ 本機開啟直接瀏覽
```

| 提供             | Hugo 做的事        | 最終結果 |
| --------------- | --------------- | ---- |
| Markdown        | 自動轉成 HTML + CSS | 完整網站 |
| 圖片              | 自動壓縮/轉 WebP     | 更快   |
| 文章 Front Matter | 自動變分類、標籤        | 整理內容 |
| `/content` 資料夾  | 變成 pages        | 建站   |


## 目錄

- [Hugo(靜態網站產生器(Static Site Generator，SSG))](#hugo靜態網站產生器static-site-generatorssg)
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
- [用法](#用法)
  - [最小化 Hugo 設定 + 美術範例](#最小化-hugo-設定--美術範例)
  - [把單一 Markdown 轉成有樣式的 HTML](#把單一-markdown-轉成有樣式的-html)

## 參考資料

[官方網站](https://gohugo.io/)

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
sudo apt update
sudo apt install hugo
# 或用 snap: sudo snap install hugo --channel=extended
```

## RedHat (CentOS)

```bash
```

## Homebrew (MacOS)

```bash
brew install hugo
```

## Windows

```powershell
choco install hugo -confirm
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 用法

## 最小化 Hugo 設定 + 美術範例

建立 Hugo 專案

```sh
hugo new site mydoc
cd mydoc
```

建立主題資料夾（custom theme）

```sh
mkdir themes/simple-doc
```

目錄

```
themes/simple-doc/
 ├── layouts/
 │    ├── _default/
 │    │     ├── baseof.html
 │    │     ├── single.html
 │    │     └── list.html
 └── static/
      └── css/
           └── style.css
```

baseof.html（全域模板）

themes/simple-doc/layouts/_default/baseof.html

```html
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{{ .Title }}</title>

    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <main class="container">
        {{ block "main" . }}{{ end }}
    </main>
</body>
</html>
```

single.html（Markdown 轉的 HTML）

themes/simple-doc/layouts/_default/single.html

```
{{ define "main" }}
<article class="content">
    <h1>{{ .Title }}</h1>
    {{ .Content }}
</article>
{{ end }}
```

簡潔美觀 CSS（置中 + 舒適排版）

themes/simple-doc/static/css/style.css

```css
body {
    margin: 0;
    padding: 0;
    font-family: "Noto Sans TC", sans-serif;
    background: #f7f7f7;
}

.container {
    width: 100%;
    max-width: 820px;
    margin: 0 auto;
    padding: 40px 20px;
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 18px rgba(0,0,0,0.08);
}

.content h1, .content h2, .content h3 {
    text-align: center;
    margin-top: 40px;
}

.content p, .content ul, .content ol {
    font-size: 18px;
    line-height: 1.8;
}

.content ul, .content ol {
    padding-left: 20px;
}

.content li {
    margin: 10px 0;
}

/* 行內列表模式 1. aaaa */
.content ol.inline > li {
    display: inline-block;
    margin-right: 20px;
}
```

## 把單一 Markdown 轉成有樣式的 HTML

若只是想把單一 Markdown 轉成 HTML（套用上述 theme），最簡方式就是把 Markdown 放 content/post/xxx.md，然後 hugo 一次建置，取 public/post/xxx/index.html 或 public/post/xxx/ 內的 index.html。這文件已包含 link rel=stylesheet 指向 /css/artistic.css（Hugo 會把 static/ 裡的 CSS 原封不動複製到 public/css/）。
