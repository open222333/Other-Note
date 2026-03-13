# Python 模組 tqdm(進度條)

```
```

## 目錄

- [Python 模組 tqdm(進度條)](#python-模組-tqdm進度條)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [基本 for 迴圈進度條](#基本-for-迴圈進度條)
  - [tqdm 搭配 list](#tqdm-搭配-list)
  - [tqdm 搭配手動更新（手動模式）](#tqdm-搭配手動更新手動模式)
  - [在函式內使用 tqdm](#在函式內使用-tqdm)
  - [tqdm 與 enumerate](#tqdm-與-enumerate)
  - [tqdm 設定描述文字（desc）](#tqdm-設定描述文字desc)
  - [tqdm + pandas（讀取 DataFrame）](#tqdm--pandas讀取-dataframe)
  - [tqdm.notebook（Jupyter 專用）](#tqdmnotebookjupyter-專用)
  - [使用 tqdm 顯示檔案處理進度（含 initial 與 total）](#使用-tqdm-顯示檔案處理進度含-initial-與-total)
  - [自訂進度條字元](#自訂進度條字元)
    - [完整自訂 bar\_format（最強大）](#完整自訂-bar_format最強大)
    - [改變進度條顏色（使用 colorama）](#改變進度條顏色使用-colorama)
    - [使用 tqdm 的 Spinner（非進度條 適合無法預估時間的工作。）](#使用-tqdm-的-spinner非進度條-適合無法預估時間的工作)

## 參考資料

[tqdm pypi](https://pypi.org/project/tqdm/)

[tqdm - github](https://github.com/tqdm/tqdm)

# 指令

```bash
# 安裝
pip install tqdm
```

# 用法

```Python
from tqdm import tqdm

import time

# 使用 range() 來模擬一個循環
for i in tqdm(range(10)):
    time.sleep(0.1)  # 模擬某個耗時操作

# 自訂進度條的外觀
for i in tqdm(range(10), desc="Processing", ncols=100):
    time.sleep(0.1)
```

## 基本 for 迴圈進度條

```Python
from tqdm import tqdm
import time

for i in tqdm(range(100)):
    time.sleep(0.05)  # 模擬工作
```

## tqdm 搭配 list

```Python
from tqdm import tqdm
import time

items = ["a", "b", "c", "d"]

for item in tqdm(items):
    time.sleep(0.3)
```

## tqdm 搭配手動更新（手動模式）

```Python
from tqdm import tqdm
import time

pbar = tqdm(total=5)

for i in range(5):
    time.sleep(1)
    pbar.update(1)

pbar.close()
```

## 在函式內使用 tqdm

```Python
from tqdm import tqdm
import time

def process_items(items):
    for item in tqdm(items, desc="正在處理"):
        time.sleep(0.2)

process_items(range(10))
```

## tqdm 與 enumerate

```Python
from tqdm import tqdm
import time

for idx, value in tqdm(enumerate(range(50)), total=50):
    time.sleep(0.05)
```

## tqdm 設定描述文字（desc）

```Python
from tqdm import tqdm
import time

for i in tqdm(range(20), desc="下載中"):
    time.sleep(0.1)
```

## tqdm + pandas（讀取 DataFrame）

```Python
import pandas as pd
from tqdm import tqdm
import time

df = pd.DataFrame({"a": range(10)})

for _, row in tqdm(df.iterrows(), total=len(df)):
    time.sleep(0.1)
```

## tqdm.notebook（Jupyter 專用）

```Python
from tqdm.notebook import tqdm
import time

for i in tqdm(range(100)):
    time.sleep(0.05)
```

## 使用 tqdm 顯示檔案處理進度（含 initial 與 total）

```Python
import os
import time
from tqdm import tqdm

file_path = "example.txt"

file_size = 500_000   # 已存在的部份（續傳場景）
total = 1_000_000      # 需要下載的剩餘大小

# 建立 tqdm 進度條
with tqdm(
    total=total + file_size,     # 整個進度＝已下載＋未下載
    initial=file_size,           # 初始進度（續傳時非常重要）
    unit='B',                    # 使用 Bytes 作為單位
    unit_scale=True,             # 自動轉 KB/MB/GB
    desc=os.path.basename(file_path),  # 顯示檔名
    ascii=True                   # 在終端使用 ASCII 進度條（兼容性高）
) as pbar:

    chunk_size = 50_000  # 每次更新的「下載區塊大小」（模擬下載）
    downloaded = 0

    # 模擬下載剩餘資料
    while downloaded < total:
        time.sleep(0.1)  # 模擬耗時動作
        downloaded += chunk_size

        pbar.update(chunk_size)  # 更新進度條
```

## 自訂進度條字元

```Python
from tqdm import tqdm
import time

# 進度使用 '█'，未完成部分使用 '.'
bar_format = "{l_bar}{bar}| {n_fmt}/{total_fmt}"

for i in tqdm(range(100), bar_format=bar_format, ascii=False):
    time.sleep(0.03)
```

| 完成 | 未完成 | 外觀用途      |
| -- | --- | --------- |
| █  | ░   | 粗體區塊風     |
| #  | -   | 常見 CLI 樣式 |
| ■  | □   | 方塊高對比     |
| ●  | ○   | 圓點風       |

### 完整自訂 bar_format（最強大）

```Python
from tqdm import tqdm
import time

bar_format = (
    "{l_bar}"            # 左側：desc + %
    "{bar}"              # 進度條本體
    " {n_fmt}/{total_fmt}"  # 顯示數量
    " [{elapsed}<{remaining}]"  # 時間
)

for i in tqdm(range(100), bar_format=bar_format):
    time.sleep(0.03)
```

### 改變進度條顏色（使用 colorama）

```Python
from tqdm import tqdm
import time
from colorama import Fore, Style

color_bar = Fore.GREEN  # 綠色進度條

bar_format = (
    f"{color_bar}"       # 套上顏色
    "{l_bar}{bar}"
    f"{Style.RESET_ALL}" # 重設顏色
    " {n_fmt}/{total_fmt}"
)

for i in tqdm(range(100), bar_format=bar_format):
    time.sleep(0.03)
```

可用顏色：

```
Fore.RED
Fore.GREEN
Fore.YELLOW
Fore.BLUE
Fore.MAGENTA
Fore.CYAN
Fore.WHITE
```

### 使用 tqdm 的 Spinner（非進度條 適合無法預估時間的工作。）

```Python
from tqdm import tqdm
import time

with tqdm(total=0, bar_format="{desc} {spinner}") as t:
    for i in range(50):
        t.set_description("工作進行中")
        time.sleep(0.1)
```
