# Python 模組-內建 argparse(命令列解析模組)

```
```

## 目錄

- [Python 模組-內建 argparse(命令列解析模組)](#python-模組-內建-argparse命令列解析模組)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [限制](#限制)
  - [分組](#分組)
  - [一個參數要求前置參數](#一個參數要求前置參數)

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

## 限制

```Python
import argparse

parser = argparse.ArgumentParser()

# 添加必需參數
parser.add_argument('--required_param', required=True, help='必需參數')

# 添加可以接受多個值的參數
# 可以接受零個或多個值。
# 使用者可以透過提供多個參數值，例如 --multi_param value1 value2，也可以不提供任何參數值。
parser.add_argument('--multi_param', nargs='*', help='多值參數')

# 需要至少一個參數值，並且多個參數值應該由空格分隔。
# 使用者必須提供至少一個參數值，例如 --multi_param value1 value2。
# action='append' 的作用是將每個符合的參數值追加到清單中。
# default=[] 指定瞭如果沒有提供參數值時的預設值為空列表。
parser.add_argument('--multi_param', action='append', nargs='+', type=str, help='多值參數', default=[])

# 添加只能選擇特定值的參數
parser.add_argument('--choice_param', choices=['option1', 'option2'], help='可選值參數')

# 建立互斥組
group = parser.add_mutually_exclusive_group()

# 添加互斥參數
group.add_argument('--option1', action='store_true', help='選項1')
group.add_argument('--option2', action='store_true', help='選項2')

# 解析命令列參數
args = parser.parse_args()
```

## 分組

```Python
import argparse

# 建立 ArgumentParser 物件
parser = argparse.ArgumentParser()

# 建立一個參數群組
group1 = parser.add_argument_group('群組1', '群組1的描述')

# 將參數添加到群組1中
group1.add_argument('--option1', help='選項1')

# 建立另一個參數群組
group2 = parser.add_argument_group('群組2', '群組2的描述')

# 將參數添加到群組2中
group2.add_argument('--option2', help='選項2')

# 解析命令列參數
args = parser.parse_args()
```

## 一個參數要求前置參數

```Python
import argparse

# 創建解析器
parser = argparse.ArgumentParser()

# 添加前置參數
parser.add_argument('required_arg', help='必需的前置參數')

# 添加另一個參數，要求前置參數存在
parser.add_argument('--dependent_arg', nargs='?', help='依賴於前置參數的參數')

# 解析命令行參數
args = parser.parse_args()

# 使用前置參數和依賴參數
print('前置參數:', args.required_arg)
print('依賴參數:', args.dependent_arg)
```