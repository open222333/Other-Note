# Linux 工具 nano(1)(文字編輯器)

```
進入後底部會看到像這樣的指令提示：

^G Get Help  ^O Write Out  ^W Where Is   ^K Cut Text  ^C Cur Pos
^X Exit      ^R Read File  ^\ Replace    ^U Uncut     ^T To Spell


^ 代表 Ctrl 鍵。
```

## 目錄

- [Linux 工具 nano(1)(文字編輯器)](#linux-工具-nano1文字編輯器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[nano(1) — Linux man page](https://linux.die.net/man/1/nano)

[nano(1) — Linux manual page](https://man7.org/linux/man-pages/man1/nano.1.html)

# 用法

編輯文字

```
直接輸入即可編輯。
```

儲存檔案

```
Ctrl + O → 儲存檔案 (會要求確認檔名，按 Enter 即可)。
```

離開編輯器

```
Ctrl + X → 離開。
```

若有修改，會問是否要儲存：

```
Y → 儲存，接著 Enter 確認檔名。

N → 不儲存。
```

複製 / 剪下 / 貼上

```
Ctrl + K → 剪下整行。

Ctrl + U → 貼上。
（也可以重複使用 Ctrl + K 剪下多行，再一次貼上。）
```

搜尋文字

```
Ctrl + W → 搜尋。

輸入關鍵字後按 Enter。

之後可以按 Alt + W 找下一個。
```

取代文字

```
*Ctrl + * → 取代。
```
