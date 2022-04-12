# JavaScript Node 模組 prettyjson-256(json美化工具)

```
以可容納 256 種顏色和擴展格式選項的彩色 YAML 樣式格式格式化 JSON 數據。
這個包以人類可讀的格式格式化類似於 util.inspect 或 prettyjson 的對象。
它支持多種格式選項以及 256 色輸出。

它的主要目的是裝飾要與調試包裝器一起使用的對象和字符串。
建議使用其他 repo debugger-256進行實際日誌記錄或作為構建自己的起點。
```

## 參考資料

[prettyjson-256 npm 頁面](https://www.npmjs.com/package/prettyjson-256)

# 指令

```bash
# 安裝
# 對於命令行訪問：
# 這將prettyjson-256全局安裝，因此它會自動添加到PATH.
npm install -g prettyjson-256

# 在項目中使用：
npm install --save-dev prettyjson-256
```

# 用法

```JavaScript
const pjson = require('prettyjson-256');
let customOptions =  {
  // 自定義選項
  // sort object keys or array values alphabetically
  alphabetizeKeys:    false,
  // how many spaces to indent nested objects
  defaultIndentation: 2,
  // maximum depth of nested levels to display for an object
  depth:              -1,
  // what to display if value is an empty array, object, or string (-1 is finite)
  emptyArrayMsg:      '(empty array)',
  emptyObjectMsg:     '{}',
  emptyStringMsg:     '(empty string)',
  // don't output any color
  noColor:            false,
  // show array indexes, this will prevent array from sorting if alphabetizeKeys is on
  numberArrays:       false,
  // show if contained in an object an array, string, or another object is empty
  showEmpty:          true,
  // color codes for different output elements
  colors:             {
    boolFalse:        { fg: [5, 4, 4] },
    boolTrue:         { fg: [4, 4, 5] },
    dash:             { fg: [2, 5, 4] },
    date:             { fg: [0, 5, 2] },
    depth:            { fg: [9] },
    empty:            { fg: [13] },
    functionHeader:   { fg: [13] },
    functionTag:      { fg: [4, 4, 5] },
    keys:             { fg: [2, 5, 4] },
    number:           { fg: [2, 4, 5] },
    string:           null
  }
};
// 初始化 自定義選項
pjson.init(customOptions);
// 印出
console.log(prettyjson.render(data, startIndent));

// 第二種寫法
console.log(prettyjson.render(data, startIndent, customOptions));
```