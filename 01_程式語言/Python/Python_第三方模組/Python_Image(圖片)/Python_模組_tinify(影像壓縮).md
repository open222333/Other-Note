# Python 模組 tinify(影像壓縮)

```
用於縮小影像檔案大小而保持影像品質。
這對於需要在網路應用、行動應用程式等情境中傳輸影像的場合非常有用，因為較小的影像檔案能更快地載入和傳輸，提高應用效能和使用者體驗。
```

## 目錄

- [Python 模組 tinify(影像壓縮)](#python-模組-tinify影像壓縮)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[tinify pypi](https://pypi.org/project/tinify/)

[官方網站](https://tinypng.com/)

[官方網站 Developer API - 註冊郵箱 取得 api key](https://tinypng.com/developers)

# 指令

```bash
# 安裝
pip install tinify
```

# 用法

```Python
import tinify

# 設置 TinyPNG 的 API 密鑰
# 替換為 在 TinyPNG 網站上註冊後獲得的 API 密鑰
tinify.key = "你的 API 密鑰"

# 指定要壓縮的圖像文件路徑
input_file_path = "input_image.png"
output_file_path = "output_image.png"

# 壓縮圖像
source = tinify.from_file(input_file_path)
source.to_file(output_file_path)

print(f"圖像壓縮完成，壓縮前大小: {source.size}, 壓縮後大小: {source.result().size}")
```
