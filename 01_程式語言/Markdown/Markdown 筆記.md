# Markdown 筆記

## 參考資料

[Markdown文件](https://markdown.tw/)

[Docs Markdown reference](https://docs.microsoft.com/en-us/contribute/markdown-reference)

[支援的語言 隔離的程式碼區塊](https://docs.microsoft.com/zh-tw/contribute/code-in-docs#supported-languages)

# 範例

# H1 大標題
## H2 標題
### H3 子標題
#### H4 小標題
##### H5 小標題
###### H6 小標題

H1 大標題
======

H2 標題
------

Text

**Bold**

*Italic*

<u>underline</u>

~~delete~~

\#1\#2\#3

Text`Highlight`Remark

+ Item1
    + Item1.1
    + Item1.2

- Item2
    - Item2.1
    - Item2.2
        * Item2.2.1

1. Number
2. Number
3. Number

- [ ] CheckBox
+ [X] To-Do
* [ ] To-Do

Text Block

	Text Block

```
Text - 文字說明
```

```java
// Java Code
System.out.println("Hello Word");
```

```diff
+ System.out.println("Hello Word");
- System.out.println("Hello Word");
```

---

***

<https://gitlab.com/GammaRayStudio>

[Blogger](https://gamma-ray-studio.blogspot.com/)

[Youtube]

[YouTube]: https://www.youtube.com/user/rhxs020

![Gamma-Ray-Studio](assets/001/gamma-ray-studio.png)

表格語法

	Table - Align : 表格對齊方式，置左、置右、置中
	Table - Text : 表格中使用文字符號
	Table - Html : 在 Markdown 中，使用 Html 表格標籤
	Table - Span : 使用 Html 標籤，合併表格欄位
	Table - Mix : 混合使用 Markdown 格式與 Html 標籤
	Table - Item : 使用 Html 標籤，在表格中呈現項目列表

### Table - Align

| Markdown             | Simple               | Table           |
| :------------------- | -------------------: |:---------------:|
| left-aligned column  | right-aligned column | centered column |
| $100                 | $100                 | $100            |
| $10                  | $10                  | $10             |
| $1                   | $1                   | $1              |

### Table - Text

1 | 2 | 3
--- | --- | ---
one | two | three
*Italic* | `Hightlight` | **Bold**

### Table - Html

<table>
  <thead>
    <tr>
      <th>Month</th>
      <th>Savings</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>January</td>
      <td>$100</td>
    </tr>
    <tr>
      <td>February</td>
      <td>$80</td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <td>Sum</td>
      <td>$180</td>
    </tr>
  </tfoot>
</table>

### Table - Span

<table>
  <tr>
    <th>Item1</th>
    <th>Item2</th>
    <th>Item3</th>
  </tr>
  <tr>
    <td>A1</td>
    <td colspan="2">A2</td>
  </tr>
  <tr>
    <td rowspan="2">B1</td>
    <td>B2</td>
    <td>B3</td>
  </tr>
  <tr>
    <td>C2</td>
    <td>C3</td>
  </tr>
</table>

### Table - Mix

|Table 1|Table 2|
|--|--|
|<table> <tr><th>Table 1 Heading 1</th><th>Table 1 Heading 2</th></tr><tr><td>Row 1 Column 1</td><td>Row 1 Column 2</td></tr> </table>| <table> <tr><th>Table 2 Heading 1</th><th>Table 2 Heading 2</th></tr><tr><td>Row 1 Column 1</td><td>Row 1 Column 2</td></tr> </table>|

### Html - Item
<table>
  <thead>
    <tr>
      <th>Java</th>
      <th>Android</th>
      <th>iOS</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td valign="top" width="300">
          <ul>
              <li>OwO</li>
              <li>OwO</li>
              <li>OwO</li>
          </ul>
      </td>
      <td valign="top" width="300">
         <ul>
         <li>OwO#1</li>
            <li>OwO#2
               <ul>
                <li>OwO@3</li>
                <li>OwO@4</li>
                <li>OwO@5</li>
                </ul>
            </li>
          </ul>
      </td>
      <td valign="top" width="300">
        <ul>
         <li>OwO#1</li>
            <li>OwO#2
               <ul>
                <li>OwO@3</li>
                <li>OwO@4</li>
                <li>OwO@5
                <ul>
                    <li>OwO$1</li>
                    <li>OwO$2</li>
                </ul>
                </li>
            </ul>
        </li>
        </ul>
      </td>
    </tr>
  </tbody>
</table>
