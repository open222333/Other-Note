# Python 模組 Pillow PLI(影像處理)

```
```

## 目錄

- [Python 模組 Pillow PLI(影像處理)](#python-模組-pillow-pli影像處理)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [範例相關](#範例相關)
- [指令](#指令)
- [用法](#用法)
  - [判斷圖片是否正常](#判斷圖片是否正常)
  - [取得 圖片資訊](#取得-圖片資訊)

## 參考資料

[pillow pypi](https://pypi.org/project/pillow/)

[pillow 文檔](https://pillow.readthedocs.io/en/stable/)

### 範例相關

[Python实现将webp格式的图片转化成jpg/png](https://www.jianshu.com/p/fc7f9e0f50df)

# 指令

```bash
# 安裝
pip install Pillow
```

# 用法

```Python
import os
import sys
from PIL import Image

for infile in sys.argv[1:]:
    f, e = os.path.splitext(infile)
    outfile = f + ".jpg"
    if infile != outfile:
        try:
            with Image.open(infile) as im:
                im.save(outfile)
        except OSError:
            print("cannot convert", infile)
```

## 判斷圖片是否正常

```Python
from PIL import Image

def is_valid_image(file_path):
    try:
        with Image.open(file_path) as img:
            img.verify()
        return True
    except Exception as e:
        print(f'Error: {e}')
        return False

if __name__ == '__main__':
    image_path = 'path/to/your/image.jpg'
    if is_valid_image(image_path):
        print('The image is valid.')
    else:
        print('The image is not valid.')
```

```Python
# 判斷網址的圖片
import requests
from PIL import Image
from io import BytesIO

def is_valid_image_url(image_url):
    try:
        # 下載圖片
        response = requests.get(image_url)
        response.raise_for_status()  # 檢查是否成功下載

        # 驗證圖片
        with Image.open(BytesIO(response.content)) as img:
            img.verify()

        return True
    except Exception as e:
        print(f'Error: {e}')
        return False

if __name__ == '__main__':
    image_url = 'https://example.com/test.jpg'
    if is_valid_image_url(image_url):
        print('The image from the URL is valid.')
    else:
        print('The image from the URL is not valid.')
```

## 取得 圖片資訊

```Python
import requests
from PIL import Image
from io import BytesIO


def get_image_dimensions(url: str):
    """取得圖片網址的圖片寬度高度

    Args:
        url (str): 圖片網址
    """
    try:
        response = requests.get(url)
        image_data = BytesIO(response.content)
        image = Image.open(image_data)
        width, height = image.size

        return {
            'width': width,
            'height': height
        }
    except Exception as err:
        print(f'取得圖片網址的圖片寬度高度 發生錯誤: {err}', exc_info=True)
        return {
            'width': None,
            'height': None
        }

def get_local_image_dimensions(file_path: str):
    """取得本地圖片的圖片寬度高度

    Args:
        file_path (str): 圖片路徑
    """
    try:
        image = Image.open(file_path)
        width, height = image.size
        return {
            'width': width,
            'height': height
        }
    except Exception as err:
        print(f'取得本地圖片的圖片寬度高度 發生錯誤: {err}', exc_info=True)
        return {
            'width': None,
            'height': None
        }
```