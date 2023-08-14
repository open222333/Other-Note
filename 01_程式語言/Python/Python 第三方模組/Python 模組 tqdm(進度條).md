# Python 模組 tqdm(進度條)

```
```

## 目錄

- [Python 模組 tqdm(進度條)](#python-模組-tqdm進度條)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

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
