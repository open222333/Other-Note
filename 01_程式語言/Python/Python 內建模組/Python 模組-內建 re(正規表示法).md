# Python 模組-內建 re(正規表示法)

```
正規表示法(Regular Expression)
執行模式的比對與搜尋。
```

## 目錄

- [Python 模組-內建 re(正規表示法)](#python-模組-內建-re正規表示法)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [驗證除錯工具相關](#驗證除錯工具相關)
- [用法](#用法)
- [正則表達式](#正則表達式)

## 參考資料

[re --- 正则表达式操作 (文檔)](https://docs.python.org/zh-tw/3/library/re.html)

[正則表達式再現工具](https://c.runoob.com/front-end/854/)

[正規表示法 Regular Expression (Regex)](https://www.fooish.com/regex-regular-expression/)

### 驗證除錯工具相關

[語法測試工具網站 - 推薦](https://regex101.com/)

[語法測試工具網站](https://www.debuggex.com/)

[語法測試工具網站](https://regexr.com/)

# 用法

```python
import re
# MatchObject物件(Regex物件)方法

# 建立Regex物件
compile()
    # 參數:re.VERBOSE：在正則表達式內增加註解
re.search(pattern,string,flags)
    # 參數:re.I(re.IGNORECASE)：搜尋時忽略大小寫
    # 參數:re.VERBOSE：在正則表達式內增加註解
re.findall(pattern,string,flags)
    # 參數:re.I(re.IGNORECASE)：搜尋時忽略大小寫
    # 參數:re.VERBOSE：在正則表達式內增加註解

# 將搜尋結果回傳
group()
m = re.match(r"(?P<first_name>\w+) (?P<last_name>\w+)", "Isaac Newton")
m['first_name']
m['last_name']

m = re.match(r"(?P<first_name>\w+) (?P<last_name>\w+)", "Malcolm Reynolds")
m.group('first_name')
m.group('last_name')

m = re.match(r"(\w+) (\w+)", "Isaac Newton, physicist")
m.group(0)       # The entire match
m.group(1)       # The first parenthesized subgroup.
m.group(2)       # The second parenthesized subgroup.
m.group(1, 2)    # Multiple arguments give us a tuple.

# 可傳回搜尋到字串的結束位置
end()
# 可傳回搜尋到字串的起始位置
start()
# 可傳回搜尋到字串的(起始,結束)位置
span()
# 使用新的字串取代原本字串的內容
sub(pattern,newstr,msg)
```

```Python
import re

# 使用 re.match 進行匹配開頭
pattern_match = re.compile(r'\d+')
result_match = pattern_match.match('123abc')

if result_match:
    print("Match found (re.match):", result_match.group())
else:
    print("No match (re.match)")

# 使用 re.search 進行全局匹配
pattern_search = re.compile(r'\d+')
result_search = pattern_search.search('abc123xyz456')

if result_search:
    print("Match found (re.search):", result_search.group())
else:
    print("No match (re.search)")

# 使用 re.findall 找到所有匹配
pattern_findall = re.compile(r'\d+')
result_findall = pattern_findall.findall('abc123xyz456')

if result_findall:
    print("Matches found (re.findall):", result_findall)
else:
    print("No matches (re.findall)")

# 使用分組捕獲
pattern_groups = re.compile(r'(\d+)-(\d+)-(\d+)')
result_groups = pattern_groups.match('2023-12-04')

if result_groups:
    print("Year:", result_groups.group(1))
    print("Month:", result_groups.group(2))
    print("Day:", result_groups.group(3))
else:
    print("No match (groups)")
```

# 正則表達式

```
():分組
{min,max}:比對次數min~max範圍內，參數可空
[]:進行字元分類

|：可同時比對多個字串
?：某個括號內的字串或正則表達式是可有可無
*：某個括號內的字串或正則表達式可從0到多次出現
+：某個括號內的字串或正則表達式可從1到多次出現
^：某個括號內的字串或正則表達式需出現被搜尋字串的起始位置
$：某個括號內的字串或正則表達式需出現被搜尋字串的最後位置
.：單一字元 搜尋換行字元以外的所有字元。
.*：所有字元 搜尋換行字元以外的所有字元。

字元分類：
[a-z]:代表a-z的小寫字元
[A-Z]:代表A-Z的大寫字元
[aeiouAEIOU]:代表英文發音的母音字元
[2-5]:代表2-5的數字
^：中括號內左方，搜尋不在這些字元內的所有字元

特殊字元：
\d：0-9之間的整數字元
\D：0-9之間的整數字元以外的其他字元
\s：空白、定位、Tab鍵、換行、換頁字元
\S：空白、定位、Tab鍵、換行、換頁字元以外的其他字元
\w：數字、字母和底線_字元，[A-Za-z0-9_]
\W：數字、字母和底線_字元，[A-Za-z0-9_]，以外的其他字元

常用規則：
# 搜尋關鍵字
pat = re.compile(r'(.*?)(關鍵字)(.*?)')

# 中文
[\u4e00-\u9fa5]

# 數字
[0-9]*$

# 英文和數字
[A-Za-z0-9]+$ 或 [A-Za-z0-9]{4,40}$

# 網址URL
[a-zA-z]+://[^\s]*

# 年月日期格式
([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))

# Email
r'[\w.-]+@[^@\s]+\.[a-zA-Z]{2,10}$'

# 台灣手機
r'(?:0|886-?)9\d{2}-?\d{6}'

# 台灣電話
0[2-8-]+[0-7]+[0-9]

# 抓HTML標籤內的文字
>([\w\s()]*?)</a>

# 抓HTML標籤中的連結(以wiki為例子)
\/wiki\/[\w-]*
https://en.wikipedia.org/wiki/Unsupervised_learning

# 域名
[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?
```