# Python 模組-內建 argparse(命令列解析模組)

```
```

## 參考資料

[argparse 官方文檔](https://docs.python.org/zh-tw/3/library/argparse.html)

# 用法

```Python
import argparse

msg = """幫助訊息

-h test"""

parser = argparse.ArgumentParser(description=msg)

parser.add_argument('-o', '--output', help='show output')
parser.add_argument('-n', '--name', type=str, default='nobady')

# 從命令列讀取參數
args = parser.parse_args()

if args.output:
    print(f'{args.name} 測試輸出訊息 {args.output}')

```
