# Bash 基本 筆記

```
副檔名 .sh
```

```bash
# 將該檔案設定為可執行
chmod +x demo.sh
```

## 撰寫 shell script 的良好習慣

```
script 的功能
script 的版本資訊
script 的作者與聯絡方式
script 的版權宣告方式
script 的 History (歷史紀錄)
script 內較特殊的指令，使用『絕對路徑』的方式來下達
script 運作時需要的環境變數預先宣告與設定。
```

## 目錄

- [Bash 基本 筆記](#bash-基本-筆記)
		- [撰寫 shell script 的良好習慣](#撰寫-shell-script-的良好習慣)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [特殊說明相關](#特殊說明相關)
- [特殊變數](#特殊變數)
- [常用的環境變數](#常用的環境變數)
- [範例](#範例)
	- [將目前執行 process 的 PID 依照數字大小排序](#將目前執行-process-的-pid-依照數字大小排序)
	- [特殊 set -e exec](#特殊-set--e-exec)

## 參考資料

[Linux Shell Script 入門教學](https://blog.techbridge.cc/2019/11/15/linux-shell-script-tutorial/)

[第十二章、學習 Shell Scripts - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0340bashshell-scripts.php)

[shell中讀取ini配置。](https://www.firbug.com/a/202108/677572.html)

### 特殊說明相關

[What are the special dollar sign shell variables?(特殊變數)](https://stackoverflow.com/questions/5163144/what-are-the-special-dollar-sign-shell-variables)

[What does set -e and exec "$@" do for docker entrypoint scripts?](https://stackoverflow.com/questions/39082768/what-does-set-e-and-exec-do-for-docker-entrypoint-scripts)

[Shell重定向 ＆>file、2>&1、1>&2 、/dev/null的区别](https://blog.csdn.net/u011630575/article/details/52151995)

### 特殊檔案相關

[/dev/zero](https://zh.wikipedia.org/zh-tw//dev/zero)

[/dev/null](https://zh.wikipedia.org/zh-tw//dev/null)

### 用法相關

[在 Bash 中解析 JSON](https://www.delftstack.com/zh-tw/howto/linux/parse-json-in-bash/#%25E5%259C%25A8-bash-%25E4%25B8%25AD%25E4%25BD%25BF%25E7%2594%25A8-jq%25E8%25BC%2595%25E9%2587%258F%25E7%25B4%259A%25E9%259D%2588%25E6%25B4%25BB%25E7%259A%2584%25E5%2591%25BD%25E4%25BB%25A4%25E5%2588%2597-json-%25E8%2599%2595%25E7%2590%2586%25E5%25B7%25A5%25E5%2585%25B7%25E8%25A7%25A3%25E6%259E%2590-json)

[shell脚本选项设置及解析](https://blog.csdn.net/happyhorizion/article/details/80431468)

[Bash Script 語法解析 - 各種單雙括弧、特殊符號語法](https://medium.com/vswe/bash-shell-script-cheat-sheet-15ce3cb1b2c7)

# 特殊變數

```bash
# 位置參數
$1, $2, $3
# 所有位置參數的類似數組的構造，{$1, $2, $3 ...}。
$@
# 所有位置參數的 IFS 展開，$1 $2 $3 ...。
$*
# 位置參數的數量。
$#
# shell 設置的當前選項。
$-
# 當前shell（不是子shell）的pid。
$$
# 最近的參數（或啟動後立即啟動當前 shell 的命令的 abs 路徑）。
$_
# (輸入）字段分隔符。
$IFS
# 最近的前台管道退出狀態。
$?
# 最近的後台命令的PID。
$!
# shell 或 shell 腳本的名稱
$0
```

# 特殊符號

```
~ 帳戶的 home 目錄

# 井號 (comments) 註解文字

;; 連續分號 (Terminator) 專用在 case 的選項，擔任 Terminator 的角色。

. 逗號 (dot,就是“點”) 一個 dot 代表當前目錄，兩個 dot 代表上層目錄。

'str' 單引號 (single quote) 被單引號用括住的內容，將被視為單一字串
在引號內的代表變數的$符號，沒有作用，也就是說，他被視為一般符號處理，防止任何變量替換。

"str" 雙引號 (double quote) 被雙引號用括住的內容，將被視為單一字串。
它防止通配符擴展，但允許變量擴展。

`command` 倒引號 (backticks) 倒引號內會被視為指令執行

, 逗點 (comma，標點中的逗號) 這個符號常運用在運算當中當做"區隔"用途。

/ 斜線 (forward slash) 在路徑表示時，代表目錄。
通常單一的 / 代表 root 根目錄的意思

\ 倒斜線
放在指令前，有取消 aliases的作用；
放在特殊符號前，則該特殊符號的作用消失；放在指令的最末端，表示指令連接下一行。

| 管道 (pipeline) pipeline 是 UNIX 系統，基礎且重要的觀念。連結上個指令的標準輸出，做為下個指令的標準輸入。

! 驚嘆號(negate or reverse) 通常它代表反邏輯的作用，譬如條件偵測中，用 != 來代表"不等於"

: 冒號 在 bash 中，這是一個內建指令："什麼事都不干"，但返回狀態值 0。
: echo $? # 回應為 0 : > f.$$ 上面這一行，相當於 cat /dev/null >f.$$。
不僅寫法簡短了，而且執行效率也好上許多。
有時，也會出現以下這類的用法 : ${HOSTNAME?} ${USER?} ${MAIL?} 這行的作用是，檢查這些環境變數是否已設置，沒有設置的將會以標準錯誤顯示錯誤訊息。
像這種檢查如果使用類似 test 或 if這類的做法，基本上也可以處理，但都比不上上例的簡潔與效率。

? 問號 (wild card) 在文件名擴展(Filename expansion)上扮演的角色是匹配一個任意的字元，但不包含 null 字元。

* 星號 (wild card) 相當常用的符號。在文件名擴展(Filename expansion)上，她用來代表任何字元，包含 null 字元。

** 次方運算 兩個星號在運算時代表 "次方" 的意思。

$ 錢號(dollar sign) 變量替換(Variable Substitution)的代表符號

${} 變量的正規表達式 bash 對 ${} 定義了不少用法。

$*
引用script的執行引用變量，引用參數的算法與一般指令相同，指令本身為0，其後為1，然後依此類推。
引用變量的代表方式如下： $0, $1, $2, $3, $4, $5, $6, $7, $8, $9, ${10}, ${11}..... 個位數的，可直接使用數字，但兩位數以上，則必須使用 {} 符號來括住。 $* 則是代表所有引用變量的符號。

$@
 $@ 則仍舊保留每個引用變量的區段觀念。

$# 引用變量的總數量是多少。

$? 狀態值 (status variable) 一般來說，UNIX(linux) 系統的進程以執行系統調用exit()來結束的。這個回傳值就是status值。回傳給父進程，用來檢查子進程的執行狀態。 一般指令程序倘若執行成功，其回傳值為 0；失敗為 1。

(   ) 指令群組 (command group) 用括號將一串連續指令括起來，這種用法對 shell 來說，稱為指令群組。如下面的例子：(cd ~ ; vcgh=`pwd` ;echo $vcgh)，指令群組有一個特性，shell會以產生 subshell來執行這組指令。因此，在其中所定義的變數，僅作用於指令群組本身。

((  )) 這組符號的作用與 let 指令相似，用在算數運算上，是 bash 的內建功能。所以，在執行效率上會比使用 let指令要好許多。 #!/bin/bash(( a = 10 ))echo -e "inital value, a = $a\n"(( a++))echo "after a++, a = $a"

{  } 大括號 (Block of code) 有時候 script 當中會出現，大括號中會夾著一段或幾段以"分號"做結尾的指令或變數設定。

### 建議使用 [[]]
[   ] 中括號 常出現在流程控制中，扮演括住判斷式的作用。 if [ "$?" != 0 ]thenecho "Executes error"exit1fi 這個符號在正則表達式中擔任類似 "範圍" 或 "集合" 的角色

### 建議使用
[[     ]] 這組符號與先前的 [] 符號，基本上作用相同，但她允許在其中直接使用 || 與&& 邏輯等符號。 #!/bin/bashread akif [[ $ak > 5 || $ak< 9 ]]thenecho $akfi

|| 邏輯符號 這個會時常看到，代表 or 邏輯的符號。

&& 邏輯符號 這個也會常看到，代表 and 邏輯的符號。

& 後台工作 單一個& 符號，且放在完整指令列的最後端，即表示將該指令列放入後台中工作。 tar cvfz data.tar.gz data > /dev/null&

 \<...\> 單字邊界 這組符號在規則表達式中，被定義為"邊界"的意思。

 +加號(plus)
在運算式中，她用來表示"加法"。
expr 1 + 2 + 3
此外在規則表達式中，用來表示"很多個"的前面字元的意思。
# grep '10\+9' fileB109100910000910000931010009#這個符號在使用時，前面必須加上escape 字元。


-減號(dash)
在運算式中，她用來表示"減法"。
expr 10 - 2
此外也是系統指令的選項符號。
ls -expr 10 - 2
在GNU 指令中，如果單獨使用- 符號，不加任何該加的文件名稱時，代表"標準輸入"的意思。這是GNU指令的共通選項。譬如下例
tar xpvf -
這裡的- 符號，既代表從標準輸入讀取資料。
不過，在cd 指令中則比較特別
cd -
這代表變更工作目錄到"上一次"工作目錄。


%除法(Modulo)
在運算式中，用來表示"除法"。
expr 10 % 2
此外，也被運用在關於變量的規則表達式當中的下列
${parameter%word}${parameter%%word}
一個% 表示最短的word 匹配，兩個表示最長的word 匹配。


=等號(Equals)
常在設定變數時看到的符號。
vara=123echo " vara = $vara"
或者像是PATH 的設定，甚至應用在運算或判斷式等此類用途上。


==等號(Equals)
常在條件判斷式中看到，代表"等於" 的意思。
if [ $vara == $varb ]
...下略

!=不等於
常在條件判斷式中看到，代表"不等於" 的意思。
if [ $vara != $varb ]
...下略

^
這個符號在規則表達式中，代表行的"開頭" 位置，在[]中也與"!"(嘆號)一樣表示“非”
```

# 常用的環境變數

```
HOME：當前使用者之家目錄的路徑。
SHELL：當前所使用的shell之名稱。
PATH：搜尋命令執行檔時，所要搜尋的目錄。
MAIL：和收發e-mail有關。
HISTSIZE：與歷史命令的數量有關。
SAVEHIST：與歷史命令有關。
LANG：目前系統所使用的語系。如：sh_TW.UTF-8，是繁體中文萬國碼。
RANDOM：此環境變數較特別，它是「亂數」的意思，在呼叫它時會產生一個正整數亂數（介於0到32767之間）。
```

# Shell重定向 ＆>file、2>&1、1>&2 、/dev/null的區別

```
在shell腳本中，默認情況下，總是有三個文件處於打開狀態，標準輸入(鍵盤輸入)、標準輸出（輸出到屏幕）、標準錯誤（也是輸出到屏幕），它們分別對應的文件描述符是0，1，2 。

默認為標準輸出重定向，
與 1> 相同 2>&1  意思是把 標準錯誤輸出 重定向到 標準輸出.
&>file  意思是把標準輸出 和 標準錯誤輸出 都重定向到文件file中
/dev/null是一個文件，這個文件比較特殊，所有傳給它的東西它都丟棄掉
```

# 範例

## 將目前執行 process 的 PID 依照數字大小排序

```bash
# 宣告使用 /bin/bash
#!/bin/bash

echo "=== 將目前執行 process 的 PID 依照數字大小排序，取出前 10 名 === "

# ps 為列出 process 相關資訊，透過 | pipe 管線傳遞資料。awk 可以根據 pattern 進行資料處理（這邊印出第一欄 PID）而 sort 是進行排序，其排序時，預設都是把資料當作字串來排序，若想讓資料根據實際數值的大小來排序，可以加上 -n 參數。-r 則是由大到小排序，預設是由小到大
ps | awk '{print $1}' | sort -rn | head -10
```

## 特殊 set -e exec

```bash
#!/bin/bash
set -e

... code ...
exec "$@"
```

# 偽裝置

## /dev/zero

```
產生連續的NUL字元的串流(數值為0)。

/dev/zero在類UNIX系統中是一個特殊的裝置檔案。
/dev/zero在被讀取時會提供無限的空字元（ASCII NUL, 0x00）。
它的典型用法包括用它提供的字元流來覆蓋資訊，以及產生一個特定大小的空白檔案。
```

## /dev/null

```
在類Unix系統中是一個特殊的裝置檔案，它丟棄一切寫入其中的資料（但報告寫入操作成功），讀取它則會立即得到一個EOF[1]。
在程式設計師行話，尤其是Unix行話中，/dev/null被稱為位元桶[2]或者黑洞。

空裝置通常被用於丟棄不需要的輸出流，或作為用於輸入流的空檔案。這些操作通常由重新導向完成。

/dev/null是一個特殊檔案，而不是目錄，因此不能使用Unix命令mv將檔案移動到其中。使用rm命令才是Unix中刪除檔案的正確方法。
```

## /dev/full

```
永遠在被填滿狀態的裝置。
```

## /dev/loop

```
Loop裝置
```

## /dev/random

```
產生一個虛假隨機的任意長度字元串流。(Blocking)
```

## /dev/urandom

```
產生一個虛假隨機的任意長度字元串流。(Non-Blocking)
```

# 命名約定

```
在基於Linux的系統中，裝置節點一般在/dev下，通常使用如下的字首：

fb：frame緩衝
fd：軟碟
hd：IDE硬碟或光碟機
lp：印表機
par：並列埠
pt：偽終端
s：SCSI裝置
scd：SCSI音訊光碟機
sd：SCSI硬碟
sg：SCSI通用裝置
sr：SCSI資料光碟機
st：SCSI磁帶
tty：終端
ttyS：序列埠
```

## 輸出/輸入重導向

```
> >> < << :> &> 2&> 2<>>& >&2

文件描述符(File Descriptor)，用一個數字（通常為0-9）來表示一個文件。
常用的文件描述符如下：
文件描述符 名稱 常用縮寫 默認值
     0 標準輸入 stdin 鍵盤
     1 標準輸出 stdout 屏幕
     2 標準錯誤輸出 stderr 屏幕
我們在簡單地用<或>時，相當於使用0< 或1> （下面會詳細介紹）。
* cmd > file
把cmd命令的輸出重定向到文件file中。如果file已經存在，則清空原有文件，使用bash的noclobber選項可以防止複蓋原有文件。
* cmd >> file
把cmd命令的輸出重定向到文件file中，如果file已經存在，則把信息加在原有文件後面。
* cmd < file
使cmd命令從file讀入
* cmd << text
從命令行讀取輸入，直到一個與text相同的行結束。除非使用引號把輸入括起來，此模式將對輸入內容進行shell變量替換。如果使用<<- ，則會忽略接下來輸入行首的tab，結束行也可以是一堆tab再加上一個與text相同的內容，可以參考後面的例子。
* cmd <<< word
把word（而不是文件word）和後面的換行作為輸入提供給cmd。
* cmd <> file
以讀寫模式把文件file重定向到輸入，文件file不會被破壞。僅當應用程序利用了這一特性時，它才是有意義的。
* cmd >| file
功能同>，但即便在設置了noclobber時也會復蓋file文件，注意用的是|而非一些書中說的!，目前僅在csh中仍沿用>!實現這一功能。
: > filename      把文件"filename"截斷為0長度.# 如果文件不存在, 那麼就創建一個0長度的文件(與'touch'的效果相同).
cmd >&
把輸出到文件符m的信息重定向到文件描述符n
cmd >&-關閉標準輸出
cmd <&n輸入來自文件描述符n
cmd m<&n m來自文件描述各個n
cmd <&-關閉標準輸入
cmd <&n-移動輸入文件描述符n而非複制它。（需要解釋）
cmd >&n-移動輸出文件描述符n而非複制它。（需要解釋）
注意： >&實際上複製了文件描述符，這使得cmd > file 2>&1與cmd 2>&1 >file的效果不一樣。
```