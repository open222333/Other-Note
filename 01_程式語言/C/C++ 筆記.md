# C++ 筆記

## 目錄

- [C++ 筆記](#c-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [編譯與連結C程式](#編譯與連結c程式)
- [基本語法 關鍵字](#基本語法-關鍵字)
- [基本語法 運算子](#基本語法-運算子)
- [基本語法 if else , switch case](#基本語法-if-else--switch-case)
- [基本語法 for, while](#基本語法-for-while)
- [常用函式 字串函式](#常用函式-字串函式)
- [常用函式 型別轉換函式](#常用函式-型別轉換函式)

## 參考資料

[GCC 編譯器基本使用教學與範例](https://blog.gtwang.org/programming/gcc-comipler-basic-tutorial-examples/)


## 編譯與連結C程式

```bash
# 編譯 C 程式 預設輸出檔名 a.out
gcc hello.c

# 編譯 C 程式，指定輸出檔名 -o
gcc -o hello hello.c

# 執行編譯好的程式
./a.out

# 顯示警告訊息、加入除錯資訊
g++ -Wall -g -o hello hello.cpp
```

# 基本語法 關鍵字

```
字元
char

字串
string

整數
short
short int
-2^15 ~ 2^15 - 1

unsigned short
unsigned short int
0 ~ 65535

int
long int
-2^31 ~ 2^31 - 1

unsighed int
unsigned long int
0 ~ 2^32 - 1

long long
long long int
-2^63 ~ 2^63 - 1

unsigned long long
unsigned long long int
0 ~ 2^64 - 1

浮點數
float
-3.4E38 ~ 3.4E38

double
-1.7E308 ~ 1.7E308

布林值
bool
true false
```

# 基本語法 運算子

```
+   加
-   減
*   乘
/   除
%   餘數

<   小於
<=  小於等於
>   大於
>=  大於等於
!=  不等於
==  等於

&&  and
||  or
!   not

++  遞增
--  遞減
```

# 基本語法 if else , switch case

```
if (條件式){
    程式碼
}else if (條件式){
    程式碼
}else{
    程式碼
}

switch (變數){
    case 條件:
        程式碼
    default:
        程式碼
}
```

# 基本語法 for, while

```
while (條件式) {
    述句
}
```

# 常用函式 字串函式

```c
strcpy(char * destination, char * source)
// 將source複製到destination

strncpy(char * destination, char * source, int n)
// 將source前n個字元複製到destination

strcat(char * destination,const char * source)
// 將字串source的串接到字串destination後面

strncat(char * destination,const char * source, int n)
// 將字串source的前n個字元串接到字串destination後面

strcmp(const char * str1, const char * str2)
// 回傳 字元轉換成ASCII後相減，0為相等

strncmp(const char * str1, const char *str2, int n)
// 只比較前n個字元，回傳 字元轉換成ASCII後相減，0為相等

strstr(const char * str1, const char * str2)
// 找出字串str2在字串str1的位置，找不到回傳NULL

strlok(char * str, const char * delimiters)
// 以delimiters切割字串

strlen(const char * str)
// 回傳字串str長度
```

# 常用函式 型別轉換函式

```c
// stdlib.h 或 cstdlib
atof(char * str)
// 將字串轉成浮點數

atoi(char * str)
// 將字串轉成整數

atol(char * str)
// 將字串轉成長整數

// ctype.h 或 cctype
toupper(int s)
// 轉成大寫

tolower(int s)
// 轉成小寫
```