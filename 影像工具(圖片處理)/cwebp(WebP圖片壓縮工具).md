# cwebp(WebP圖片壓縮工具)

```
cwebp 是 Google libwebp 套件內建的命令列工具，用來將 JPEG / PNG 等圖片轉換並壓縮成 WebP 格式，可指定畫質或無損模式，同畫質下檔案通常比 JPEG 小 25-35%、比 PNG 小更多。
```

## 目錄

- [cwebp(WebP圖片壓縮工具)](#cwebpwebp圖片壓縮工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Windows](#windows)
- [指令](#指令)
  - [基本轉換](#基本轉換)
  - [批次轉換整個資料夾](#批次轉換整個資料夾)
  - [壓到指定檔案大小以下（逐步降低品質）](#壓到指定檔案大小以下逐步降低品質)
- [例外狀況](#例外狀況)

## 參考資料

[libwebp cwebp 官方文件](https://developers.google.com/speed/webp/docs/cwebp)

[WebP 官方介紹](https://developers.google.com/speed/webp)

# 安裝

## Homebrew (MacOS)

```bash
brew install webp
```

## Debian (Ubuntu)

```bash
sudo apt install webp
```

## RedHat (CentOS)

```bash
sudo yum install libwebp-tools
```

## Windows

```
從 https://developers.google.com/speed/webp/download 下載預編譯執行檔（libwebp 內含 cwebp.exe），解壓後加入 PATH。
```

# 指令

## 基本轉換

```bash
# -q 為品質參數 (0-100)，數字越低檔案越小、畫質越差，預設 75
cwebp -q 80 input.jpg -o output.webp
```

## 批次轉換整個資料夾

```bash
# 用迴圈搭配 cwebp，副檔名改為 .webp
for f in *.jpg; do cwebp -q 80 "$f" -o "${f%.*}.webp"; done

# 或用 ImageMagick mogrify 批次轉檔
mogrify -format webp -quality 80 *.jpg
```

## 壓到指定檔案大小以下（逐步降低品質）

cwebp 沒有內建指定目標檔案大小的參數，需自行寫迴圈遞減品質直到符合限制：

```bash
TARGET_KB=500
QUALITY=90
while [ $QUALITY -ge 10 ]; do
  cwebp -q $QUALITY input.jpg -o output.webp
  SIZE_KB=$(du -k output.webp | cut -f1)
  if [ "$SIZE_KB" -le "$TARGET_KB" ]; then
    echo "完成：品質 $QUALITY，大小 ${SIZE_KB}KB"
    break
  fi
  QUALITY=$((QUALITY - 10))
done
```

若來源圖片本身已經是 webp（非第一次壓縮），每降一次品質都是在既有壓縮基礎上再壓，畫質損失會比從原圖（JPEG/PNG）直接轉壓更明顯：

- 每次降幅不要太大（建議一次降 10），邊調邊比較畫質
- 優先考慮同時縮小長寬尺寸（`-resize`），往往比單純降畫質更有效、畫質也更自然

```bash
# 同時縮小尺寸（寬 1600px，高度依比例自動計算）與降畫質
cwebp -q 75 -resize 1600 0 input.jpg -o output.webp
```

# 例外狀況

<!-- 錯誤訊息、異常排除；每個問題一小節：錯誤訊息 → 原因 → 解法 -->
