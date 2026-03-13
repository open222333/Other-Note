# Python 模組 pytesseract(光學字符識別 (OCR))

```
pytesseract 是 Tesseract-OCR 的 Python 包裝器，用於從圖像中進行光學字符識別 (OCR)。

光學字符識別 (OCR) 說明
光學字符識別 (OCR, Optical Character Recognition) 是一種技術，用於將圖像中的文字轉換為可編輯的文本。
這項技術對於從掃描的文件、照片中的文字以及其他包含文字的圖像中提取數據非常有用。
OCR 系統通過識別圖像中的字符形狀，將其轉換為對應的文本數據。
```

## 目錄

- [Python 模組 pytesseract(光學字符識別 (OCR))](#python-模組-pytesseract光學字符識別-ocr)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)
  - [識別圖像中的單個字符](#識別圖像中的單個字符)
  - [輸出字典](#輸出字典)
  - [識別圖像中的表格](#識別圖像中的表格)
  - [處理噪聲和預處理圖像](#處理噪聲和預處理圖像)

## 參考資料

[pytesseract pypi](https://pypi.org/project/pytesseract/)

# 指令

```bash
# 安裝
pip install pytesseract
```

# 安裝

安裝 Tesseract-OCR

`在 Ubuntu 上`

```sh
apt-get update
apt-get install tesseract-ocr
```

`在 macOS 上`

```sh
brew install tesseract
```

`在 CentOS 上`

安裝 EPEL 釋出庫

```sh
yum install epel-release
```

更新系統並安裝依賴項

```sh
yum update
yum install gcc autoconf automake libtool pkgconfig
yum install leptonica leptonica-devel
```

Tesseract-OCR 的最新版本可以從其 GitHub 頁面下載並自行編譯

```sh
# 下載 Tesseract 源代碼
wget https://github.com/tesseract-ocr/tesseract/archive/refs/tags/5.0.0-alpha-20201231.tar.gz

# 解壓縮
tar -zxvf 5.0.0-alpha-20201231.tar.gz
cd tesseract-5.0.0-alpha-20201231

# 配置和編譯
./autogen.sh
./configure
make
make install
ldconfig
```

安裝語言數據包

```sh
mkdir -p /usr/local/share/tessdata
cd /usr/local/share/tessdata
wget https://github.com/tesseract-ocr/tessdata/raw/master/chi_tra.traineddata
```

驗證安裝

```sh
tesseract --version
```

安裝 pytesseract

```bash
pip install pytesseract
```

# 用法

```Python
import pytesseract
from PIL import Image

# 加載圖像
image = Image.open('path_to_image.png')

# 使用 pytesseract 進行 OCR 識別
text = pytesseract.image_to_string(image)

print(text)
```

設置 Tesseract 的路徑

如果 Tesseract-OCR 沒有在默認路徑中安裝，可以手動設置路徑

```Python
import pytesseract

# 設置 Tesseract 可執行文件的路徑
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
```

OCR 配置選項

可以通過傳遞配置選項來調整 OCR 的行為。例如，指定語言：

```Python
text = pytesseract.image_to_string(image, lang='chi_tra')  # 使用繁體中文
```

```Python
# 定義要識別的區域 (左，上，右，下)
box = (100, 100, 400, 400)
region = image.crop(box)

text = pytesseract.image_to_string(region)
print(text)
```

## 識別圖像中的單個字符

使用 pytesseract 可以識別單個字符，這對驗證碼識別等應用非常有用

```Python
text = pytesseract.image_to_boxes(image)
print(text)
```

## 輸出字典

可以將 OCR 結果輸出為字典格式，其中包含更多信息，如字符位置信息

```Python
details = pytesseract.image_to_data(image, output_type=pytesseract.Output.DICT)
print(details)
```

## 識別圖像中的表格

pytesseract 也可以用於識別表格中的數據：

```Python
table_data = pytesseract.image_to_string(image, config='--psm 6')
print(table_data)
```

## 處理噪聲和預處理圖像

在某些情況下，可能需要預處理圖像以提高 OCR 結果的準確性。

可以使用 PIL 或 OpenCV 進行圖像預處理：

```Python
import cv2
import numpy as np

# 讀取圖像
image = cv2.imread('path_to_image.png')

# 轉換為灰度圖像
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

# 應用二值化處理
_, binary = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY)

# 保存並使用 pytesseract 進行識別
cv2.imwrite('binary_image.png', binary)
text = pytesseract.image_to_string(Image.open('binary_image.png'))

print(text)
```
