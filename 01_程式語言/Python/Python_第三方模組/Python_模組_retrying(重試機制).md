# Python 模組 retrying(重試機制)

```
retrying 模組是一個簡單的 Python 庫，用於實現重試機制。
它可以讓您在函數遇到異常時自動重試，以應對臨時性錯誤或不穩定的情況。這裡是如何使用 retrying 模組的詳細說明：
```

## 目錄

- [Python 模組 retrying(重試機制)](#python-模組-retrying重試機制)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [基本用法 裝飾器 @retry](#基本用法-裝飾器-retry)
  - [設置僅在特定條件下重試，例如特定異常或返回值](#設置僅在特定條件下重試例如特定異常或返回值)

## 參考資料

[retrying pypi](https://pypi.org/project/retrying/)

# 指令

```bash
# 安裝
pip install retrying
```

# 用法

## 基本用法 裝飾器 @retry

```Python
import os
import requests
from urllib.parse import urlparse, basename
from retrying import retry
from io import BytesIO
from PIL import Image

class ImageDownloader:
    def __init__(self, logger):
        self.logger = logger

    # stop_max_attempt_number: 最大重試次數。
    # wait_fixed: 每次重試之間的固定等待時間（毫秒）。
    # wait_random_min 和 wait_random_max: 每次重試之間的隨機等待時間範圍（毫秒）。
    # stop_max_delay: 最長重試時間（毫秒）。
    # retry_on_exception: 自定義重試條件，接收一個異常並返回一個布爾值，指示是否應該重試。

    @retry(stop_max_attempt_number=3, wait_fixed=2000)
    def download_image(self, img_url: str, dir_path: str = 'download_image_output', img_name: str = None, img_extension: str = None):
        """下載圖片

        Args:
            img_url (str): 圖片網址
            dir_path (str): 存放資料夾位置
            img_name (str, optional): _description_. Defaults to None.
            img_extension (str, optional): _description_. Defaults to None.
        """
        if not os.path.exists(dir_path):
            os.makedirs(dir_path)

        try:
            urlparse_result = urlparse(img_url)
            file_path = basename(urlparse_result.path)
            filename, extension = os.path.splitext(file_path)

            if img_name is None:
                img_name = filename
            if img_extension is None:
                img_extension = extension

            if img_extension[0] != '.':
                img_extension = f'.{img_extension}'

            img = f'{dir_path}/{img_name}{img_extension}'
        except Exception as err:
            self.logger.error(f'下載圖片 處理圖片路徑 發生錯誤: {err}', exc_info=True)
            return False

        try:
            img_content = requests.get(url=img_url, stream=True)
            # 驗證圖片是否正常
            is_valid = self.is_valid_image_url(img_content.content)
            if is_valid:
                with open(img, 'wb') as f:
                    for chunk in img_content.iter_content(chunk_size=1024):
                        f.write(chunk)
            else:
                raise RuntimeError(f'{img} 圖片網址驗證失敗: {img_url}')
        except Exception as err:
            self.logger.error(f'下載圖片 {img} 發生錯誤: {err}', exc_info=True)
            raise
        return True

    def is_valid_image_url(self, image_content: bytes):
        """驗證網址的圖片是否正常

        Args:
            # image_url (str): 網址
            image_content (bytes):
                取得 下載圖片 的 bytes
                response = requests.get(image_url)
                response.raise_for_status()  # 檢查是否成功下載
                response.content

        Returns:
            _type_: _description_
        """
        try:
            # 驗證圖片
            with Image.open(BytesIO(image_content)) as img:
                img.verify()
            return True
        except Exception as e:
            return False
```

## 設置僅在特定條件下重試，例如特定異常或返回值

```Python
from retrying import retry

@retry(retry_on_exception=lambda x: isinstance(x, IOError))
def my_function():
    print("執行函數")
    raise IOError("IOError 測試異常")

my_function()

```