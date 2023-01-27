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
