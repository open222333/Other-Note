# Python 王者歸來 讀書筆記

```
string、json、bytes的轉換
json->string
str = json.dumps(jsonobj)

bytes->string
str = str(bytes,‘utf-8’)

string->json
json = json.loads(str)

json.dumps()
將python物件轉成json字串，返回type為str
從python物件轉換為json字串

json.loads()
將json字串，返回python物件，返回type為dict
從json字串轉換為python物件

串列(list) name_list=[元素1,...,元素n]
元組(tuple) name_list=(元素1,...,元素n)
#元素值與元素個數不可變動，可稱不可改變的串列 應用在串列上的方法或函數只要不更改元組內容及可應用在元組上

運算式:
//:回傳整數結果
%:回傳餘數

函數 help(物件.方法名稱)
函數 divmod(x,y):得到商和餘數，以元組(tuple)值
函數 type():可以列出變數的資料型態類別 ch3_1.py
函數 bin():可以將一般整數數字轉換為2進位，Python中定義凡是0b開頭的數字，代表這是2進位的整數 ch3_5.py
函數 oct():可以將一般整數數字轉換為8進位，Python中定義凡是0o開頭的數字，代表這是8進位的整數 ch3_6.py
函數 hex():可以將一般整數數字轉換為16進位，Python中定義凡是0x開頭的數字，代表這是16進位的整數 ch3_7.py
函數 int():將資料型態強制轉換成整數 ch3_19.py
函數 float():將資料型態強制轉換成浮點數
函數 abs():計算絕對值
函數 pow(x,y):返回x的y次方
函數 round():處理位數左邊是奇數則使用四捨五入，是偶數則使用五捨六入。ex:round(1.5) = 2 ，round(2.5)=2 ch3_10.py
函數 str():將資料型態強制轉換成字串 ch3_23.py
函數 chr(x):傳回函數x值得ASCII碼值或Unicode字元
函數 ord(x):傳回字元參數x的Unicode碼值
函數 encode():將字串轉為byte資料，參數為編碼的方法 ex:acsii
函數 decode():將byte資料轉字串，參數為編碼的方法 ex:acsii
函數 print(value,......,sep="",end="\n",file=sys.stdont,flush=False)
    參數 value:表示想要輸出的資料，可以一次輸出多筆資料，個資料間以逗號隔開
    參數 sep:當輸出多筆資料時，可以插入各筆資料的分隔字元，預設是一個空白字元
    參數 end:當輸出結束時所插入的字元，預設是插入換行字元
    參數 file:資料輸出位置，預設是sys.stdont，也就是螢幕。
    參數 flush:是否清除緩衝區，預設為不清除
    函數 format():加強版格式化輸出 print("{}...輸出格式區...".format(變數系列區,...))


ch4
函數 open():開啟一個檔案供讀取或寫入 file_Obj = open(file,mode="r") #只列出常用參數
    "r":預設，開啟檔案只供讀取(read)
    "w":開啟檔案供寫入，如果原先檔案有內容將被覆蓋
    "a":開啟檔案供寫入，如果原先檔案有內容，新內容將被添加在後面
    "x":開啟一個新的檔案供寫入，如果所開啟的檔案已經存在會產生錯誤
    下列為檔案類型:
    "b":開啟二進位檔案模式
    "t":預設，開啟文字檔案模式
    file_Obj:檔案物件，print()可輸出導向此物件，不使用時，使用file_Obj.close()關閉，才可以返回作業系統的檔案管理員觀察執行結果。

函數 input():讀取使用者輸入的資料 value = input("prompt:")，所輸入的資料value型態是字串
函數 eval():處理字串的數學運算，result = eval(expression)，ch4_17.py
函數 dir():列出內建函數


math.radian():將角度轉成弧度

6-1串列(list)
索引值[-1]:最後一個串列元素
max():最大值
min():最小值
sum():總和
len():串列個數
del name_list[i]:刪除i串列元素
del name_list[start:end]:刪除區間串列元素
6-1-3
串列切片(list slices)

6-2字串常用方法
lower():將字串轉成小寫
upper():將字串轉成大寫
title():將字串轉成第一個字母大寫，其餘是小寫
rstrip():刪除字串尾端的空白
lstrip():刪除字串開始端的空白
strip():刪除字串頭尾兩端的空白
center():字串在指定寬度置中對齊
rjust():字串在指定寬度靠右對齊
ljust():字串在指定寬度靠左對齊

串列常用
append():串列後端添加元素，name_list[3] = value，串列A.append(串列B)，ch6_24.py
insert():插入元素在任意位置，insert(索引,元素內容)，ch6_25.py
pop():與del不一樣的是刪除後可傳回刪除的值，value = name_list.pop(i)，若i沒有指定則刪除末位元素，ch6_26.py
remove():刪除指定元素，name_list.remove(欲刪除的元素內容)，ch6_27.py
reverse():顛倒排序串列元素，永久性更改，name_list.reverse()，ch6_28.py
sort():由小排到大，永久性更改，name_list.sort()，由大排到小，於參數添加reverse=True，ch6_29.py
sorted():由小排到大，非永久性更改，new_list = sorted(name_list,reverse = True)，ch6_31.py
index():索引值 = 串列名稱.index(搜尋值)，搜尋不存在會發生錯誤 #建議先使用in運算式(6-10) 判斷搜尋值是否在串列內，再使用index())，ch6_33.py
count():傳回特定元素出現的次數，搜尋值不在串列內會傳回0，次數 = 串列名稱.count(搜尋值)，ch6_35.py
extend():只能使用來串接串列，將串列分解為元素，一一插入串列，串列A.extend(串列B)，ch6_39.py
id():列出物件的位址
copy():淺複製，b=a.copy()，子物件指向一樣位址，因此會連動
deepcopy():淺複製，b=copy.deepcopy(a)，完全獨立，需import copy模組

6-9
函數 list():將參數內的物件轉成串列
函數 split():將字串以空格或其他符號為分隔符號拆開成一個串列
語法:str.split(str="", num=string.count(str))
str-分隔符，默認為所有的空字符，包括空格，換行（\ n），製表符（\ t）等。
num-分割次數。默認為-1，即分隔所有。

函數 join():連接字串.join(串列)，串列元素會用連接字串組成字串
函數 startswith():可以列出字串起始文是否是特定子字串
函數 endswith():可以列出字串結束文是否是特定子字串
函數 replace(ch1,ch2):將ch1字串由另一個字串ch2取代

6-10
in 和 not in 運算式 dict
boolean_value = obj1 in obj2 物件obj1在物件obj2內會傳回True
boolean_value = obj1 not in obj2 物件obj1不在物件obj2內會傳回True

6-11
is 和 is not 運算式
boolean_value = obj1 is obj2 物件obj1等於obj2會傳回True
boolean_value = obj1 is not obj2 物件obj1不等於obj2會傳回True

6-12
enumerate物件
enumerate()方法可將iterable(迭代)類數值的元素用索引值與元素配對方式傳回
返回的數據稱enumerate物件
iterable類數值可以是 串列(list) 元組(tuple) 集合(set) range()
語法 obj = enumerate(iterable[,start = 0]) #若省略start =設定，預設索引值是0
enumerate物件轉成串列後，元素型態是元組(8-10說明) ch8_13.py

7-2
range()函數
可使用range()產生一個等差集序列，可將此等差差集序列當成回圈的計數器
可迭代物件占用記憶體空間，可使用range()物件
語法 range(start,stop,step) #stop是必須的值 省略start範圍為0:stop-1step預設為1
#特色:不需預先儲存所有序列範圍的值，因此可節省記憶體與增加程式效率

串列生成式(list generator)
新串列 = [運算式 for 項目 in 可迭代物件]
新串列 = [運算式 for 項目 in 可迭代物件 if 條件式]

7-3
for迴圈
break 強制離開迴圈
continue 迴圈暫時停止不往下執行
for else迴圈 若if續速條件是False

7-4
while迴圈
哨兵值(Sentinel Value):在while迴圈設定一個輸入值當作迴圈值星結束的值
break 強制離開迴圈
continue 迴圈暫時停止不往下執行
pass 什麼都不做 通常應用在尚未設計完成的迴圈避免無限迴圈

8-6
元組切片(tuple slices)

8-8
list(元組):將元組資料型態改為串列 #可以更安全的保護資料 增加程式執行速度(因為結構較簡單)
tuple(串列):將串列資料型態改為元組

8-11
zip():2個或更多個可迭代物件，可用zip()將多個物件打包成zip物件 zip函數內增加"*" 相當於可以unzip()

8-12
生成式(generator):語法:num = (n for n in range(範圍))

9-1(ch9_31.py)
#設計大型程式時，必須記住字典的鍵是不可變的，因此串列、字典、集合不適合當作字典的鍵。元組適合做字典的鍵

字典(dict):非序列資料結構，無法使用類似串列的索引。使用"鍵:值"方式配對儲存的，操作時使用鍵(key)取得值(value)的內容。定義語法:name_dict{鍵1:值1,...,鍵n:值n,}
鍵(key)一般使用字串以及數字，不可重複
值(value)可以是python的所有物件，數字、字串、串列、字典...等，最右邊的,可有可無

新增字典元素:name_dict[key] = value
列出字典元素的值:name_dict[key]
刪除字典元素:del name_dict[key]
字典的pop():ret_value = dictObj.pop(key) #dictObj是欲刪除元素的字典
字典的popitem():valueTup = dictObj.popitem() #dictObj是欲刪除元素的字典 可隨機刪除字典元素
clear():將字典內的所有元素刪除，字典依舊存在
複製字典:夫製出來的字典位址不同 語法:new_dict = name_dict.copy()
len(dict):取得元素數量
驗證鍵元素是否存在 語法:鍵 in name_dict
update():合併字典 將2個字典合併可以使用update() 語法:dict_A.update(dict_B)
dict():可將雙值序列的串列轉為字典
人工智慧-語意分析:將文章分割成片段用字典儲存，具體須購買人工智慧語意分析的書籍
item():遍歷字典
keys():遍歷字典的鍵
value():遍歷字典的值
若有一個oldDict字典想依照字典的值(value)排序 可使用: newList = sorted(oldDict.items(),key=lambda item:items[1])，此串列newList的元素是元組，元組內有2個元素分別是字典內的鍵與值
9-7
fromkeys():這是建立字典的一種方法 語法如下:name_dict = dict.fromkey(seq[,value]) #使用seq序列建立字典，使用seq序列建立字典，序列內容將是字典的鍵，如果沒有設value則用none當字典鍵的值
get():搜尋字典的鍵，如果鍵存在則傳回該鍵的值，如果不存在則傳回預設值 語法:ret_value = dict.get(key[,default = none]) #dict是要搜尋的字典
setdefault():搜尋字典的鍵，如果鍵存在則傳回該鍵的值，如果不存在則將"鍵:值"加入字典。"鍵:預設值"，若有預設值則將"鍵:預設值"加入字典，若無預設值則將"鍵:none"加入字典 語法:ret_value = dict.setdefault(key[,default = none])
9-9(ch9_32.py) (ch9_35.py 設定加密程式)
字典生成式:新字典 = {鍵運算式:值運算式 for 運算式 in 可迭代物件}

10-1
可使用{}以及set()建立集合，要建立空集合需使用set()函數，集合是不可變的。使用set()函數建立集合，參數的內容可以是字串(string)、串列(list)、元組(tuple)、字典(dict)...等。字串、串列、元組會被轉成集合元素，而字典則是鍵(key)被轉成集合元素。

交集:2個集合內相同的元素 符號:"&" 方法:intersection()
聯集:2個集合內所有元素 符號:"|" 方法:union()
差集:A、B兩個集合，想獲得A元素但不屬於B元素，使用(A-B)想獲得B元素但不屬於A元素，符號:"-" 方法:difference()
對稱差級:A、B兩個集合，排除同時是A也是B的元素。 符號:"^" 方法:symmetric_difference()
等於:可獲得2個集合是否相等，是傳回"True"，否傳回"False"。 符號:"=="
不等於:可獲得2個集合是否不相等，是傳回"True"，否傳回"False"。 符號:"!="
in:判斷元素是否在集合內
not in:判斷元素是否不是在集合內

10-3
適用集合的方法:
add():加一個元素到集合
clear():刪除集合所有元素。傳回值是None
copy():複製集合 語法:new_集合 = old_集合.copy()
difference_update():刪除集合內與另一個集合重複的元素。 語法:集合A.difference_update(集合B)
discard():如果是集合成員則刪除，可以刪除集合的元素，如果元素不在集合內也不會產生錯誤。 語法:value = 集合A.discard(欲刪除的元素)
intersection_update():此方法傳回集合的交集。 語法:ret_value = A.intersection_update(*B) *B代表可以1到多個集合，*B是B、C，執行後A的內容將會是A、B、C的交集(共同元素)
isdisjoint():如果2個集合沒有共同元素則返回True。 語法:ret_boolean = 集合A.isdisjoint(集合B)
issubset():如果集合A是集合B的子集合則返回True。集合A的元素均可集合B發現，集合A為子集合。
isuperset():如果這個集合B是集合A的父集合則返回True。集合A的元素均可集合B發現，集合B為父集合。
pop():傳回所刪除的元素，若是空集合則返回False。隨機刪除集合內的元素。 語法:ret_element = 集合A.pop() ;ret_element是刪除的元素
remove():刪除指定元素，如果此元素不存在，程式將返回KeyError 語法:集合.remove(欲刪除的元素)
symmetric_difference_update():使用對稱集更新集合內容。  語法:集合A.symmetric_difference_update(集合B)
update():使用聯集更新集合內容。將集合B的元素加到集合A內 語法:集合A.update(集合B)

10-4
適合集合的基本函數
enumerate(): 傳回連續整數配對的enumerate物件
len():元素數量
max():最大值
min():最小值
sorted():傳回已經排序的串列，集合本身則不改變
sum():總合

10-5
凍結集合(frozenset):set是可變集合，frozenset是不可變集合，這是一個新的類別(class)。只要設定元素後，這個凍結集合就不可再更改了。

10-6
集合生成式: 新集合 = {運算式 for 運算式 in 可迭代項目}

11-1
函式的定義
def 函式名稱 (參數值1[,參數值2,...]):
	"""函式註解"""
程式碼區塊
retrue[回傳值1,回傳值2] #中括號可有可無

11-2
參數預設值 函數設計有含預設值，必須放置在參數列的最右邊

11-3
函數傳回值 沒有"return [回傳值]" 直譯時自動回傳會處理成"return None"
None在python是一個特殊的值，如果將它當作布林值使用，可將它視為是False

11-4 ch11_19.py ch11_20.py
主程式呼叫函數時傳遞整數變數，這個程式會在主程式以及函數中列出此變數的值與位址的變化

主程式呼叫函數時傳遞串列變數，這個程式會在主程式以及函數中列出此變數的值與位址的變化
函數內串列內容改變，主程式內的內容也改變

若函數的參數串列預設值為空字串(testList = [])，若重複呼叫此函數，回遺留先前呼叫的內容，設計此類的函數建議使用testList = None

11-5
傳遞任意數量的參數:參數前加上"*" 所傳遞出來的參數群組化成元組(tuple)
關鍵字參數:參數用"參數名稱 = 值"形式呈現。 若使用在函數，關鍵字參數將會變成任意數量的字典元素，其中引數是鍵，對應的值是字典的值

11-6
函數文件字串Docstring:"""註解內的文字"""可使用help(函數)列印出來，或是使用print(函數.__doc__)只顯示註解，"__"由兩個"_"組成
函數是一個物件:可以向物件一樣給予賦值
函數可以是資料結構的成員(串列、元組...的元素)
高階函數(Higher-order function):函數可以當作參數傳遞給其他函數
函數當參數與*args不定量的參數

嵌套函數:函數內部也有函數
閉包(closure):內部函數是一個動態產生的程式，當它可以記住函數以外的程式所建立的環境變數值時，可以稱這個內部函數為閉包(closure)

11-7
遞迴式函數設計 recursive
函數可以護轎其ㄊ函數也可以呼叫自己 特色:
1.每次呼叫自己都會使範圍越來越小
2.必須要有一個終止的條件來結束遞迴函數

可導入sys使用 getrecursionlimit()函數 設定預設的遞迴數量
import sys
sys.getrecursionlimit()

getrecursionlimit():獲得目前Python預設的遞迴數量
setrecursionlimit():設定目前Python預設的遞迴數量

11-8
區域變數與全域變數
區域變數(local variable):變數影響範圍限定在一個函數內
全域變數(global variable):變數影響範圍在一整個程式
###建議全域變數以及區域變數不要設計成相同名稱
locals():可以用字典的方式列出所有的區域變數名稱以及內容
globals():可以用字典的方式列出所有的全域變數名稱以及內容

11-9
匿名函數(anomymous function)(lambda):指沒有名稱的函數，適合使用在程式中只存在一小段時間的情形。
特色:可以有很多參數，但是只能有一個程式表達式
語法:lambda arg1[,arg2,arg3,...]:expression #arg是參數可以多個，expression是匿名函數lambda表達式的內容。

使用匿名函數的理由:
一個lambda函數更加的使用時機是存在一個函數的內部

11-9-3
匿名函數應用在高階函數的應用

filter():主要是篩選序列，將iterable(可以重複執行，字串、串列、元組)的元素(item)依序放進func(item)內，然後將func()函數執行結果是True的元素(item)組成新的篩選元素(filter object)傳回 語法: filter(func,iterable)

取得filter元素的方式:item for item in filter_object ，與下方for迴圈類似
for item in filter_object:
	print(item)
或可使用:
oddlist = [item for item in filter_object]

map():依次對iterable重複執行，將iterable(可以重複執行，字串、串列、元組)的元素(item)依序放進func(item)內，然後將結果傳回 語法:map(func,iterable)

reduce():先對可迭代物件的第1和第2元素操作，結果在跟第3個元素操作，直到最後元素，reduce(f,[a,b,c,d]) = f(f(f(a,b),c),d)。 語法:reduce(func,iterable)
###早期是內建元素，現在要使用需先import functools
###字串轉整數的函數:ch11_37_1.py

11-12
關鍵字 yield
11-13
裝飾器(Decorator):在函數新增一些功能但不更改原先函數
裝飾器堆疊(decorator stacking):一個函數可以有2個以上的裝飾器，方式在函數上方設定裝飾器函數即可，當有多個裝飾器函數時，會由下往上次序一次執行裝飾器
常用:
1.為函數增加除錯的檢查功能

12.1
類別
class Classname():
	statement1

12-1-3:類別的建構方法:
建構方法(constructor):初始化類別，在類別內建立一個初始化方法(method),這是一個特殊的方法，當在程式內宣告這個類別的物件時將自動執行這個方法。
初始化方法有個固定名稱:__init__()，initiakzation的縮寫
初始化方法定義中，self是必須的，同時須放在所有參數的最前面(最左邊)，代表的是類別本身的物件

12-2
類別的訪問權限:封裝(encapsulatuon)
在屬性名稱前加__，便是宣告私有屬性，類別外的程式無法使用
在方法名稱前加__，便是宣告私有方法，類別外的程式無法使用
#### 可用以下方法存取私有屬性、方法: 物件名稱._類別名稱 私有屬性 EX:hungbank._Banks__balance = 10000

#property()使用觀念: 新式屬性 = property(getter[,setter[,fdel[,doc]]])
getter是獲取屬性值函數，setter是設定屬性值函數，fdel是刪除屬性值的函數，doc是屬性描述，傳回的是新式屬性，未來可以由此新式屬性存取私有屬性內容

####類別的方法#####
1.實例方法(屬性):特色有self，屬性開頭是self，同時所有方法第一個參數是self。使用時須建立此類別的物件，然後由物件調用。

2.類別方法(屬性):前面是@classmethod，第一個參數習慣使用cls。使用時不需要實例化，可由類別本身直接調用。類別屬性會隨時被更新。

3.靜態方法:特色是由@staticmethod開頭，不須原先的self和cls參數。此方法也是由類別名稱直接調用。

12-3 繼承
被繼承的類別:父類別(parent class)、基底類別(base class)、超類別(superclass)
繼承的類別:子類別(child class)、衍生類別(derived class)

父類別的公有方法以及屬性，在子類別可以直接引用。

基底類別(base class)必須在衍生類別(derived class)前面 結構如下:
class BaseClassName():
	Base Class 內容
class DerivedClassName(BaseClassName):
	Derived Class 內容

12-3-2 如何取得基底類別的私有屬性
類別定義外基本上是無法取得類別的私有屬性，即使是衍生類別。若真要取得可使用return方式傳回私有屬性的內容。

12-3-3 衍生類別與基底類別有相同名稱的屬性
Python會先找尋衍生類別是否有此屬性，若沒有則使用基底類別的名稱內容。

12-3-4 衍生類別與基底類別有相同名稱的方法
# 此為物件導向的多型(polymorphism)，多型不一定需要是繼承關係的類別。
# 多型:相同的函數名稱，放入不同類型的物件可以產生不同的結果。

12-3-7
super():繼承觀念內可使用此方法取得基底類別的屬性。 super().__init__()
在三代同堂的類別中很重要

兄弟A、B類別屬性的取得: B類別().B類別屬性 #A類別取的B類別的屬性

12-4 多型(polymorphism)
同一個介面或方法(Method)可以有多個實作型態

12-5 多重繼承
一個類別繼承多個類別

Python在super()的多重繼承算是協同作業(co-operative)，需在基底類別也增加super設定才能正常作業 EX:ch12_19_1.py

12-6-1
type():取得函數內物件的資料型態
由於大型城市可能是多人合作設計，可使用這個函數了解某個物件的資料型態

12-6-2
isinstance():傳回物件的類別是否屬於某一個類別，若物件的類別是屬於第2個參數類別或子類別則傳回Trus 否則False。 語法:isinstance(物件,類別) #傳回Trus False

12-7
__XX__大多是特殊屬性或方法

__doc__:列印類別與方法內的註解
__name__:判別此程式是自己執行或是被其他程式import導入當成模組使用

12-8 類別的特殊方法
__str__(): # 定義物件的字串描述，用來定義傳回物件描述字串，通常用來描述的字串是對使用者友善的說明文字，如果對物件使用str()，所呼叫的就是__str__()。
__repr__():定義對開發人員較有意義的描述，例如傳回產生實例的類別名稱之類的，如果對物件使用repr()，則所呼叫的就是__repr__()
__iter__():將類別定義成一個迭代物件，此時類別需設計一個next()方法取的下一個值，直到達到結束條件，可以使用raise Stoplteration(ch15)終止繼續
__eq__(self,other):定義self == other 運算，#等於
__ne__(self,other):定義self != other 運算，#不等於
__lt__(self,other):定義self < other 運算，#小於
__gt__(self,other):定義self > other 運算，#大於
__le__(self,other):定義self <= other 運算，#小於或等於
__ge__(self,other):定義self >= other 運算，#大於或等於
__add__(self,other):定義self + other 運算，#加法
__sub__(self,other):定義self - other 運算，#減法
__mul__(self,other):定義self * other 運算，#乘法
__floordiv__(self,other):定義self // other 運算，#整數除法
__truediv__(self,other):定義self / other 運算，#除法
__mod__(self,other):定義self % other 運算，#餘數
__pow__(self,other):定義self ** other 運算，#次方

13-1 將自建的函數儲存在模組中
13-2 應用自己建立的函數模組
import 模組名稱 #導入模組
模組名稱.函數名稱 #引用模組的函數
from 模組名稱 import 函數名稱 #導入模組內單一特定的函數
from 模組名稱 import 函數名稱A,函數名稱B,... #導入模組內多個函數
from 模組名稱 import * #導入模組內所有函數
from 模組名稱 import 函數名稱 as 替代名稱 #使用as給函數指定替代名稱
import 模組名稱 #導入模組as 替代名稱 #使用as給模組指定替代名稱


13-5 隨機數random模組
randint(min,max) #產生min(含)與max(含)之間的整數值
choice():從串列(List)隨機傳回一個元素
shuffle():將串列元素重新排列
sample():語法:sample(串列,數量)，隨機傳回第2個參數數量的串列元素
uniform():隨機產生(x,y)之間的浮點數
random():隨機產生0.0-1.0之間的浮點數

13-6 時間time模組
time():傳回自1970年1月1日 00:00:00AM以來的秒數
sleep():可讓工作暫停，單位是秒
asctime():以可閱讀方式列出系統時間
localtime():列出目前時間的結構資料，同時使用索引列出個別內容

13-7 系統sys模組
sys.version :目前python版本
物件 stdin:standard input的縮寫，指從螢幕輸入(可想成Python Shell視窗)。可搭配readline()方法，然後可讀取螢幕輸入直到按下Enter的字串。
物件 stdout:standard output的縮寫，指從螢幕輸出(可想成Python Shell視窗)。可搭配write()方法，然後可讀取螢幕輸出
屬性 platform:傳回目前Python使用的平台
屬性 path:列出目前環境變數
屬性 executable:列出目前所使用Python可執行檔案的路徑
方法 getwindowsversion():傳回目前Python環境的Windows作業系統的版本
方法 getrecursionlimit():獲得目前Python預設的遞迴數量
方法 setrecursionlimit():設定目前Python預設的遞迴數量
屬性 argv:列出命令列引數，會以串列形式記錄在sys.argv內。

read([size]):從檔案當前位置起讀取size個位元組，若無引數size，則表示讀取至檔案結束為止，它範圍為字串物件
readline(n):該方法每次讀出一行內容，所以，讀取時佔用記憶體小，比較適合大檔案，該方法返回一個字串物件。n可輸入正整數參數，代表所讀取的字元數，其中一個中文字或空格都算一個字元。
readlines():讀取整個檔案所有行，儲存在一個列表(list)變數中，每行作為一個元素，但讀取大檔案會比較佔記憶體。
write():寫入檔案
writelines():可以將 array 的內容一次過寫入寫案, 但要自行加入 “\n” 到每一個換行
with():用 wite 寫入檔案, 跟上面最大分別是, 不用 close() 關閉檔案

13-8 keyword模組
Python部分關鍵字的功能

屬性 kwlist:含有所有Python的關鍵字
方法 iskeyword():傳回參數內的字串是否為關鍵字，是傳回True，否傳回False。

13-9 日期calendar模組
isleap():列出某年是否為閏年
month():印出月曆
calendar():印出年曆

13-10
一,collections模組:
方法 defaultdict(func):為新建立的字典設定預設值，參數是一個函數。如果參數是int，則參數相當於是int()，預設值會回傳0。如果參數是list或dist，預設值分別傳回"[]"、"{}"。如果省略參數，預設傳回None。
物件 Counter():可以將串列元素轉成字典的鍵，字典的職責是元素在串列出現的次數。對Counter物件而言，我們可以使用加法、減法，相加的方式是所有元素相加，若有重複的元素則鍵的值會相加。若想列出A有B沒有的元素，可以使用A-B。可以使用&當作交集符號，|是聯集符號。聯集與加法不一樣，不會將數量相加，只是取多的部分。焦急則是取少的部分。
most_common():如果省略參數n，可以參考 鍵:值 的數量由大到小傳回。n是設定傳回多少元素。

方法 deque():資料結構中的雙頭序列，基本上這是具有堆疊stack與序列queue的功能，可從左右兩邊增加元素或刪除元素。

pop():移除右邊的元素並回傳
popleft():移除左邊的元素並回傳 也可使用[::-1]將字串反轉

items():以列表返回可遍歷的(鍵, 值) 元組數組。

***回文(palindrome):從左右兩邊往內移動，如果相同就一直比對到中央，如果全部相同就是回文，否則不是回文。

二,pprint模組:
pprint():執行一行輸出一個元素。

三,itertools模組
方法 chain():將參數內的元素內容一一迭代出來。
方法 cycle():產生無限迭代。
方法 accumulate(x,func):累計x的值。func是函數，可空值，依照func列出累計的計算結果。

四,string模組
屬性 printable:列出所有ASCII可列印字元，最後幾個是溢出字元。可以用string.printable[:-5] 排除


14-1
os模組:
方法 getcwd():列出目前工作目錄
方法 mkdir(path):建立path目錄
方法 rmdir(path):刪除path目錄，只限制是空目錄。刪除有檔案的目錄參考 14-5-7
方法 remove(path):刪除path檔案
方法 chdir(path):將目前工作資料夾改至path
方法 listdir():以串列方式列出特定工作目錄內容。
方法 walk():遍歷目錄樹，每次執行迴圈將傳回3個值。目前工作目錄名稱(dirName)、目前工作目錄底下的子目錄串列(subdirNames)、目前工作目錄底下的檔案串列(fileNames)。
語法:
for dirName,sub_dirNames,fileNames in os.walk(目錄路徑): # dirName,sub_dirNames,fileNames可以更改，順序不可更改
	程式區塊
目錄路徑可以是絕對位址和相對位址，os.walk('.')代表目前工作目錄。


方法 chdir():將工作目錄改成參數內的
方法 os.path.dirname():返回目錄 去掉檔名
方法 os.path.abspath():返回絕對路徑
方法 os.path.basename():去除目錄的路徑，回傳檔案名稱(包含副檔名)
__file__表示當前檔案的path

os.path模組:包含在os模組內
方法 abspath(path):傳回path的絕對路徑。通常用來將檔案或資料夾的相對路徑轉成絕對路徑。
方法 relpath(path,start):傳回從start到path的相對路徑，如果省略start，則傳回目前工作目錄到path的相對路徑
方法 exist(path):如果path的檔案或資料夾存在傳回True否則傳回False
方法 isabs(path):如果path的檔案或資料夾是絕對路徑傳回True否則傳回False
方法 isdir(path):如果path是資料夾傳回True否則傳回False
方法 isfile(path):如果path是檔案傳回True否則傳回False
方法 join():將字串結合為一個檔案路徑
方法 getsize():傳回特定檔案大小

glob模組:
方法 glob():獲得特定工作目錄的內容。 "*"萬用字元，"?"任意字元，"[abc]"必須是abc字元

14-2
關鍵字 with:開啟檔案並在結束後不需要此檔案時自動將它關閉
語法:
with open(欲開啟的檔案, mode) as 檔案物件:
	相關系列指令

open():mode部分
'r'功能是讀取，可省略。
'w'功能是寫入，若檔案不存在則建立檔案物件，若存在則清除原內容寫入新內容。
'a'功能是寫入，若檔案不存在則建立檔案物件，若存在則在末端寫入新內容。
'rb'開啟二進位檔(一般圖檔、語音檔...)
'wb'寫入二進位檔

14-2-3 逐行讀取檔案內容 PS:記事本會有換行符號 所以輸出會每輸出一行空一行 使用rstrip()刪除末端字元
for line in file_Obj: # line 和 file_Obj可以自行取名，file_Obj是檔案物件
	迴圈相關指令

14-2-4 使用readlines()逐行讀取檔案內容
可在with外 使用檔案物件內容

14-2-5 數據組合
14-2-6 字串的替換
14-2-7 數據的搜尋
14-2-8 數據的搜尋 find()
方法 find():可執行數據搜尋，搜尋到數據會傳回數據的索引位置。沒有找到會傳回-1。
語法:index = S.find(sub[,start[,end]]) # S代表被搜尋的字串，sub是欲搜尋字串。index是如果搜尋到時傳回的索引值，start和end是可以被搜尋的字串的區間，若省略表示全部搜尋，如果沒有找到則傳回-1給index

14-2-9 數據的搜尋 rfind()
方法 rfind():搜尋特定子字串最後一次出現的位置。
語法: index = S.rfind(sub[,start[,end]]) # S代表被搜尋的字串，sub是欲搜尋字串。index是如果搜尋到時傳回的索引值，start和end是可以被搜尋的字串的區間，若省略表示全部搜尋，如果沒有找到則傳回-1給index

14-3

14-3-1 將執行結果寫入空的文件
write(): 輸出到檔案。 語法:len = 檔案物件.write(欲輸出資料) # 可將資料輸出到檔案物件

14-3-2 數值資料須轉成字串後輸出

14-4-2 隨機讀取二進位檔案
方法 tell():傳回從檔案開頭算起，目前讀寫指針的位置，以byte為單位。
方法 seek():傳回目前讀寫指針相對整體資料的位移植。 語法:offsetValue =seek(offset,origin)
origin意義:
	origin = 0 :預設值，讀寫指針移至開頭算起的第offset的byte位置。
	origin = 1 :讀寫指針移至目前位置算起的第offset的byte位置。
	origin = 2 :讀寫指針移至相對結尾的第offset的byte位置。


14-5 shutil模組
提供一些方法執行檔案或目錄的複製、刪除、更動位置和更改名稱。

方法 copy():執行檔案的複製。 語法:shutil.copy(source,destination)
方法 copytree():複製目錄。 語法與copy()相同。
方法 move():執行檔案的移動。 語法:shutil.move(source,destination)
方法 rmtree():刪除底下有目錄的目錄。

send2trash模組(需安裝)
pip install send2trash
方法 send2trash():刪除的檔案和目錄會進入資源回收桶。

14-6 zipFile模組
14-6-1 壓縮檔案
方法 zipFile():建立一份壓縮後的檔名，加上參數"w"
語法:fileZip = zipfile.ZipFile('out.zip','w') # out.zip是未來儲存壓縮的結果。
fileZip和out.zip都可以自由設定名稱。fileZip是壓縮檔物件代表的是out.zip。zipFile無法對目錄進行壓縮，可使用迴圈處理。

14-6-2 讀取zip檔案
方法 namelist():傳回zip檔案內所有被壓縮的檔案或目錄名稱，並以串列傳回此物件。
方法 infolist():傳回檔案名稱filename、檔案大小file_size、壓縮結果大小compress_size、檔案時間date_time等資訊。

14-6-3 解壓縮
方法 extractall():解壓縮。

14-7 encode編碼
file)Obj = open(file, mode = 'r', encoding = "cp950") # encoding = "cp950"為Python預設值

utf-8(8-bit Unicode Transformation Format)，是一種適合多語系的編碼規則，主要精神是使用可變長度位元組方式儲存字元以節省記憶體空間。

BOM(Byte Order Mark) 位元組順序記號:中文Windows系統的記事本，以utf-8執行編碼時，會在文件前端增加BOM，主要功能是判斷文字以Unicode表示時，位元組排序方式。
\ufeff u代表Unicode編碼格式，fe和ff是16進位的編碼格式。Big Endian(BE)系統:數值較大的byte放前面。Little Endian(LE)系統:數值較小的byte放前面。

open()函數使用時，encoding='utf-8-sig'可在逐行讀取時將BOM去除

######python: line=f.readlines()消除line中\n的方法
f = open("name.txt")
date = f.read().splitlines()
print(date)
f.close()

14-8 pyperclip模組
剪貼簿模組 屬於第三方模組
pip install pyperclip

方法 copy():將串列資料拷貝至剪貼簿
方法 paste():將剪貼簿資料拷貝回字串變數


14-9 專題  分析檔案 加密檔案

15-1 try - except 異常處理
語法:
try:
	指令
except 異常物件:
	異常處理程序
else:
	正確處理程序

15-1-4 FileNotFoundError 找不到檔案

15-2 常見的異常物件
AttributeError 通常是指物件沒有這個屬性
Exception 一般錯誤皆可使用
FileNotFoundError 找不到open()開啟的檔案
IOError 輸入或輸出時發生錯誤
IndexError 索引超出範圍區間
KeyError 在映射中沒有這個鍵
MemoryError 需求記憶體超出範圍
NameError 物件名稱為宣告
SyntaxError 語法錯誤
SystemError 直譯器的系統錯誤
TypeError 資料型態錯誤
ValueError 輸入無效參數
ZeroDivisionError 除數為0

try:
	指令
except Exception as err: # err是任意取的變數名稱，內容是msg
	print("message":+ str(msg)) #列印錯誤訊息
關鍵字 finally：配合try使用，放置於except、else之後，不論是否有異常發生都會執行finally內的程式碼。
關鍵字 assert：確保程式執行的某個階段須符合一定的條件，不符合則拋出異常，終止程式並印出異常原因。執行時 使用 -O:停用斷言
    語法: assert 條件,'字串' 條件若是True繼續執行
關鍵字 raise：自行定義狀況為異常
    語法:raise Exception("msg") # 呼叫Exception msg是傳遞錯誤訊息

traceback模組 協助程式設計師在程式設計階段，對整個程式執行狀態做一個全面性的安全檢查。
方法 format_exc():紀錄程式錯誤的traceback字串

15-7 logging模組 程式日誌
logging.disable(logging.CRITICAL)  # 關閉logging
logging等級

DEBUG 可追蹤關鍵變數變化過程
logging.debug()
INFO
logging.info() 記錄一般發生的事件
WARNING 不影響程式執行，但未來可能會導致問題的發生
logging.warning()
ERROR
logging.error() 顯示程式在某些狀態將引發錯誤的緣由
CRITICAL
logging.critical() 顯示將整個系統中斷或當掉的錯誤

設定顯示資訊的等級 語法:
logging.basicConfig(filename='out.txt',level=logging.DEBUG,format='')

format格式:
asctime:系統時間
message:錯誤訊息
levelname:顯示錯誤等級

16 正則表達式(Regular Expression)
執行模式的比對與搜尋。

re模組
MatchObject物件(Regex物件)方法
方法 compile():建立Regex物件
    參數:re.VERBOSE：在正則表達式內增加註解
方法 search():re.search(pattern,string,flags)
    參數:re.I(re.IGNORECASE)：搜尋時忽略大小寫
    參數:re.VERBOSE：在正則表達式內增加註解
方法 findall():re.findall(pattern,string,flags)
    參數:re.I(re.IGNORECASE)：搜尋時忽略大小寫
    參數:re.VERBOSE：在正則表達式內增加註解
方法 group():將搜尋結果回傳
方法 end():可傳回搜尋到字串的結束位置
方法 start():可傳回搜尋到字串的起始位置
方法 span():可傳回搜尋到字串的(起始,結束)位置
方法 sub(pattern,newstr,msg):使用新的字串取代原本字串的內容

-------------------------------------------------------
正則表達式
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
#中文
[\u4e00-\u9fa5]
#數字
[0-9]*$
英文和數字
[A-Za-z0-9]+$ 或 [A-Za-z0-9]{4,40}$
#網址URL
[a-zA-z]+://[^\s]*
#年月日期格式
([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))
#Email
r'[\w.-]+@[^@\s]+\.[a-zA-Z]{2,10}$'
#台灣手機
r'(?:0|886-?)9\d{2}-?\d{6}'
#台灣電話
0[2-8-]+[0-7]+[0-9]
#抓HTML標籤內的文字
>([\w\s()]*?)</a>
#抓HTML標籤中的連結(以wiki為例子)
\/wiki\/[\w-]*
https://en.wikipedia.org/wiki/Unsupervised_learning
-------------------------------------------------------
cmp(x, y): 用於比較2個對象，如果x < y 返回-1, 如果x == y 返回0, 如果x > y 返回1。
```