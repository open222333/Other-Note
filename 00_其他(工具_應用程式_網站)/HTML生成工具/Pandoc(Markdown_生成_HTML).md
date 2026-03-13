# Pandoc(Markdown 生成 HTML)

```
```

## 目錄

- [Pandoc(Markdown 生成 HTML)](#pandocmarkdown-生成-html)
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

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

[到 Releases： 安裝官方最新版（推薦） pandoc-*.amd64.deb](https://github.com/jgm/pandoc/releases/latest)

```bash
sudo dpkg -i pandoc-*.deb
```

## RedHat (CentOS)

```bash
```

## Homebrew (MacOS)

```bash
brew install pandoc
```

更新

```bash
brew upgrade pandoc
```

確認版本

```sh
pandoc --version
```

## Windows

[MSI 安裝（推薦）](https://github.com/jgm/pandoc/releases/latest)

下載：

```
pandoc-*.windows-x86_64.msi
```

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
