# Python 基本 筆記

## 目錄

- [Python 基本 筆記](#python-基本-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [安裝相關](#安裝相關)
		- [特殊變數、方法相關](#特殊變數方法相關)
		- [環境變數相關](#環境變數相關)
		- [修飾詞相關](#修飾詞相關)
		- [pip(模組管理工具)](#pip模組管理工具)
		- [Command Line Interface(CLI)](#command-line-interfacecli)
	- [Python書籍推薦](#python書籍推薦)
	- [Python之禪](#python之禪)
- [環境變數](#環境變數)
- [指令 Command Line Interface(CLI)](#指令-command-line-interfacecli)
	- [指令 pip(模組管理工具)](#指令-pip模組管理工具)
	- [指令 python](#指令-python)
- [型別](#型別)
	- [字串](#字串)
		- [字串前 u (Unicode) u"String"](#字串前-u-unicode-ustring)
	- [串列(list)](#串列list)
	- [字典(dict)](#字典dict)
	- [元組(tuple)](#元組tuple)
	- [集合(set)](#集合set)
- [類別(class)](#類別class)
	- [修飾詞 staticmethod](#修飾詞-staticmethod)
	- [修飾詞 classmethod](#修飾詞-classmethod)
	- [修飾詞 property](#修飾詞-property)
		- [範例](#範例)
- [關鍵字](#關鍵字)
	- [yield](#yield)
- [函式](#函式)
	- [lambda](#lambda)
	- [\*args \*\*kwargs](#args-kwargs)
- [特殊變數 特殊方法](#特殊變數-特殊方法)

## 參考資料

[官方網站](https://www.python.org/)

[官方網站 下載](https://www.python.org/downloads/)

[Python Developer's Guide 官方教學](https://devguide.python.org/versions/#versions)

[內建函式 官方文檔](https://docs.python.org/3/library/functions.html)

[內建模組 官方文檔](https://docs.python.org/zh-tw/3/py-modindex.html)

[命令列與環境 官方](https://docs.python.org/zh-tw/3/using/cmdline.html)

### 安裝相關

[linux 安裝 官方教學](https://devguide.python.org/getting-started/setup-building/#linux)

[【Python】在 CentOS 7 上安裝 Python3](https://kirin.idv.tw/python-install-python3-in-centos7/)

### 特殊變數、方法相關

[Can someone explain __all__ in Python](https://stackoverflow.com/questions/44834/can-someone-explain-all-in-python)

### 環境變數相關

[環境變數 Windows](https://hackmd.io/@yizhewang/B1zdXG4br)

### 修飾詞相關

[Python 的 staticmethod 與 classmethod](https://ji3g4zo6qi6.medium.com/python-tips-5d36df9f6ad5)

[[Python教學] @property是什麼? 使用場景和用法介紹](https://www.maxlist.xyz/2019/12/25/python-property/)

### pip(模組管理工具)

[pip documentation 官方文檔](https://pip.pypa.io/en/stable/#)

[Python 工具 pip(安裝模組)](/01_程式語言/01_Python/Python%20工具%20pip(安裝模組).txt)

[安裝 pip](https://pip.pypa.io/en/stable/getting-started/)

### Command Line Interface(CLI)

[命令行與環境](https://docs.python.org/zh-tw/3/using/cmdline.html)

## Python書籍推薦

```
第一本: 精通 Python：運用簡單的套件進行現代運算
入門書 介紹滿多實例、常用套件跟業界常見的應用

第二本: Python 自動化的樂趣：搞定重複瑣碎&單調無聊的工作 推薦
相信對於剛學程式語言的人會有很大的啟發性，因為你會明白原來你平常工作有太多事情都可以用Python幫你輕鬆搞定，其實大部分上班族白天都在瞎忙，浪費時間浪費人力在做例行公事。

第三本: Python錦囊妙計 第三版
詳細介紹Python最常用也是最好用的幾個套件的用法

第四本: Python專家實踐指南：搭乘專業開發者的學習便車
不明所以的變數名稱、用print()在debug、大量重複的程式碼、不相干的邏輯全部硬塞在一個module、寫了一堆莫名的運算沒註解

第五本: 流暢的 Python：清晰、簡潔、有效的程式設計
每次讀完一個小節就會有豁然開朗的感覺，你會更了解Python，寫的code會更有效率，跟別人寫的看起來就有差，會更Pythonic。
```
## Python之禪

```python
# The Zen of Python
import this
```

# 環境變數

```python
# 將目前工作目錄設定為指令搞所在目錄
os.chdir(os.path.dirname(os.path.abspath(__file__)))
```

# 指令 Command Line Interface(CLI)

## 指令 pip(模組管理工具)

```bash
# python2 查看pip版本
pip -v

# python3 查看pip版本
python3 -m pip -v

# 在 Debian 的衍生產品上，例如 Ubuntu，使用 APT。
apt-get install python3.7
# 在 Red Hat 和衍生產品，請使用 yum。
yum install python37
# 在 SUSE 和衍生產品，請使用 zypper。
zypper install python3-3.7

curl -O https://bootstrap.pypa.io/get-pip.py
python3 get-pip.py --user

# 新增可執行檔路徑 (~/.local/bin)到PATH變數。
# 在使用者資料夾中尋找 shell 的描述檔指令碼。
# 如果不確定擁有哪個shell，請執行
echo $SHELL
    # Bash – .bash_profile、.profile 或 .bash_login。
    # Zsh – .zshrc
    # Tcsh - – .tcshrc、.cshrc 或 .login。

# 將匯出命令新增至您的描述檔指令碼。
# 下列範例會將由 LOCAL_PATH 代表的路徑新增至目前的 PATH 變數。
export PATH=LOCAL_PATH:$PATH

# 將第一個步驟所述的描述檔指令碼載入您目前的工作階段。
# 下列範例會載入由 PROFILE_SCRIPT 代表的描述檔指令碼。
source ~/PROFILE_SCRIPT
```

## 指令 python

```bash
python [-bBdEhiIOqsSuvVWx?] [-c command | -m module-name | script | - ] [args]
	-m <module-name>
	# 輸出Python 版本號
	-V ,--version

### linux ###
# 查看python默認鏈接的為其中python路徑
which python

# 查看電腦中安裝的所有python路徑
whereis python

# 將系統默認的python版本由python2改為python3
echo alias python=python3 >> ~/.bashrc

# python3的默認版本 由3.5改成3.6
echo alias python3=python3.6 >> ~/.baashrc

# 修改完後 更新
source ~/.bashrc
```

# 型別

## 字串

### 字串前 u (Unicode) u"String"

```
在Python 2.x
程式中所有字串，其實都是原始位元組集合
如果原始碼中寫了非 ASCII 字元串
必須在第一行放置編碼聲明（encoding declaration）
```

```Python
# coding=Big5
text = '測試'
print len(text) # 顯示 4
```

```
程式會顯示 4 而不是 2
因為 Big5 每個中文字佔兩個位元組
而兩個中文字就佔四個位元組
所以上例中，len() 實際上是計算出 '測試' 兩字 Big5 編碼實現下的位元組長度
為了支援 Unicode，Python 2.x 提供了 u 前置字來產生 unicode 物件
```

```Python
# coding=Big5
text = u'測試'
print type(text) # 顯示 <type 'unicode'>
print len(text)  # 顯示 2
```

## 串列(list)

```
以[ ] 中括號來表示
```

```Python
# 取得串列元素數目	n=len(list1)	n=6
n = len(list1)

# 取得元素最小值	n=min(list1)	n=1
n = min(list1)

# 取得元素最大值	n=max(list1)	n=6
n = max(list1)

# 第1次n1元素的索引值	n=list1.index(3)	n=2
n = list1.index(n1)

# n1元素出現的次數	n=list1.count(3)	n=1
n = list1.count(n1)

# 將n1作為元素加在串列最後	list1.append(8)	list1=[1,2,3,4,5,6,8]
list1.append(n1)

# 將x中元素逐一做為元素加在串列最後	list1.extend(x)	list1=[1,2,3,4,5,6,8,9]
list1.extend(x)

# 在位置n加入n1元素	list1.insert(3,8)	list1=[1,2,3,8,4,5,6]
list1.insert(n, n1)

# 取出最後元素並移除	n=list1.pop()	n=6,list1=[1,2,3,4,5]
n = list1.pop()

# 移除第1次的n1元素	list1.remove(3)	list1=[1,2,4,5,6]
list1.remove(n1)

# 反轉串列順序	list1.reverse()	list1=[6,5,4,3,2,1]
list1.reverse()

# 將串列由小排到大	list1.sort()	list1=[1,2,3,4,5,6]
list1.sort()
```

## 字典(dict)

```
每一個元素都由鍵 (key) 和值 (value) 構成，結構為key: value。
不同的元素之間會以逗號分隔，並且以大括號 {}圍住。
```

```Python
# 宣告
d = {key1: value1, key2: value2}
```

## 元組(tuple)

```
元組（tuple）與列表（list）類似，不同之處在於元組的元素不能修改。
```

## 集合(set)

```
只允許不可變成員，例如：元組，不允許串列、字典被加入集合。
```

# 類別(class)

## 修飾詞 staticmethod

```
因為是被綁定在類別中，所以不需要建立物件即可使用。
也因此無法存取物件資料，只能對傳入的參數做處理。
```

```Python
@staticmethod
def func(args, ...)
```

## 修飾詞 classmethod

```
classmethod與staticmethod的差別
因為classmethod利用的cls來把自己綁定在類別上，所以classmethod可以透過cls來呼叫類別內的成員；
但staticmethod只能操作傳入的參數。
```

```Python
@classmethod
def func(cls, args...)
```

## 修飾詞 property

```
@property 是要實現物件導向中設計中封裝的實現方式

特性:
	將 class (類) 的方法轉換為 只能讀取的 屬性
	重新實現一個屬性的 setter、getter 和 deleter 方法
```

### 範例

```Python
# 將 class (類) 的方法轉換為 只能讀取的 屬性
# 重新實現一個屬性的 setter、getter 和 deleter 方法
class Bank_acount:
    def __init__(self):
        self._password = '預設密碼 0000'

    @property
    def password(self):
        return self._password

    @password.setter
    def password(self, value):
        self._password = value

    @password.deleter
    def password(self):
        del self._password
        print('del complite')
```

```Python
from werkzeug.security import generate_password_hash, check_password_hash

'''
案例：當使用者在建立會員帳號密碼時，密碼傳送進資料庫前會被加密的過程
'''

class User:
    @property
    def password(self):
        raise AttributeError('password is not readable attribute')

    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)

    def verify_password(self, password):
        return check_password_hash(self.password_hash, password)
```

# 關鍵字

## yield

```
為了節省記憶體的使用
因此 yield 設計來的目的，就是為了單次輸出內容。
可以把 yield 暫時看成 return，但是這個 return 的功能只有單次。
一旦程式執行到 yield 後，程式就會把值丟出，並暫時停止。
```

# 函式

## lambda

```Python
lambda parameter_list: expression
```

## *args **kwargs

```python
# 一個星號會以 tuple 的方式引入

# 兩個星號會以 dict 的方式引入

def fun(a, *args, kw1, **kwargs):
    print(a, args, kw1, kwargs, sep=' # ')

fun(1, 2, 3, 4, 5, kw1=6, g=7, f=8, l=9)
# 1 # (2, 3, 4, 5) # 6 # {'g': 7, 'f': 8, 'l': 9}
```

# 特殊變數 特殊方法

```
限制其他檔案導入此檔時，只會導入__all__的內容。
__all__

列印類別與方法內的註解
__doc__

判別此程式是自己執行或是被其他程式import導入當成模組使用
__name__


類別的特殊方法

__str__()
定義物件的字串描述，用來定義傳回物件描述字串，通常用來描述的字串是對使用者友善的說明文字，如果對物件使用str()，所呼叫的就是__str__()。

__repr__()
定義對開發人員較有意義的描述，例如傳回產生實例的類別名稱之類的，如果對物件使用repr()，則所呼叫的就是__repr__()

__iter__()
將類別定義成一個迭代物件，此時類別需設計一個next()方法取的下一個值，直到達到結束條件，可以使用raise Stoplteration(ch15)終止繼續

定義self == other 運算，#等於
__eq__(self,other)

定義self != other 運算，#不等於
__ne__(self,other)

定義self < other 運算，#小於
__lt__(self,other)

定義self > other 運算，#大於
__gt__(self,other)

定義self <= other 運算，#小於或等於
__le__(self,other)

定義self >= other 運算，#大於或等於
__ge__(self,other)

定義self + other 運算，#加法
__add__(self,other)

定義self - other 運算，#減法
__sub__(self,other)

定義self * other 運算，#乘法
__mul__(self,other)

定義self // other 運算，#整數除法
__floordiv__(self,other)

定義self / other 運算，#除法
__truediv__(self,other)

定義self % other 運算，#餘數
__mod__(self,other)

定義self ** other 運算，#次方
__pow__(self,other)
```
