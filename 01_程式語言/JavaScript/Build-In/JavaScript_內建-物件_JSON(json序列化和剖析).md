# JavaScript 內建-物件 JSON(json序列化和剖析)

```
JSON 物件包含了解析、或是轉換為 JavaScript Object Notation（JSON）格式的方法。
這物件不能被呼叫或建構；而除了它的兩個方法屬性以外，本身也沒有特別的功能。

將資料轉換為位元組或字元串流被稱為序列化(serialization)

undefined 會被省略
```

## 目錄

- [JavaScript 內建-物件 JSON(json序列化和剖析)](#javascript-內建-物件-jsonjson序列化和剖析)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [仿製原生 JSON 物件](#仿製原生-json-物件)

## 參考資料

[JSON MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/JSON)

# 用法

```JavaScript
const code = '"\u2028\u2029"';
// 方法
// JSON.parse()
// 解析 JSON 字串，能改變給定值和屬性、接著回傳解析值。
JSON.parse(code); // evaluates to "\u2028\u2029" in all engines

// 使用reviver函式過濾某些特性
let data = JSON.parse(text, function (key, value) {
  // 移除特性名稱以一個底線開頭的任何值
  if (key[0] === "_") return undefined;

  // 若值是 ISO 8601 日期格式的一個字串，就轉為一個Date。
  if (
    typeof value === "string" &&
    /^\d\d\d\d-\d\d-\d\dT\d\d:\d\d:\d\d.\d\d\dZ$/.test(value)
  ) {
    return new Date(value);
  }

  return value;
});

// JSON.stringify()
// 若要求序列化沒有原生支援的值，會查看值是否有toJSON()方法
// 回傳給定的 JSON 對應字串，可自行決定只想包括哪些特定屬性、或替換的屬性值。
let o = {s: "", n: 0, a: [true, false, null]};
let s = JSON.stringify(o); // => s == '{"s":"","n":0,"a":[true,false,null]}'

// 指定要序列化什麼欄位，以及要以什麼順序序列化他們
let text = JSON.stringify(address, ["city", "state", "country"]);

// 指定一個 replacer函式，略過值為RegExp的特性
let json = JSON.stringify(o, (k, v) => (v instanceof RegExp ? undefined : v));

```

## 仿製原生 JSON 物件

```JavaScript
/**
 * 舊版瀏覽器並不支援 JSON。
 * 你可以在程式碼開頭插入以下程式碼，以解決這個問題。
 * 這個程式碼能實做不支援原生 JSON 物件的瀏覽器（如 Internet Explorer 6）。
 */
if (!window.JSON) {
  window.JSON = {
    parse: function(sJSON) { return eval('(' + sJSON + ')'); },
    stringify: (function () {
      var toString = Object.prototype.toString;
      var hasOwnProperty = Object.prototype.hasOwnProperty;
      var isArray = Array.isArray || function (a) { return toString.call(a) === '[object Array]'; };
      var escMap = {'"': '\\"', '\\': '\\\\', '\b': '\\b', '\f': '\\f', '\n': '\\n', '\r': '\\r', '\t': '\\t'};
      var escFunc = function (m) { return escMap[m] || '\\u' + (m.charCodeAt(0) + 0x10000).toString(16).substr(1); };
      var escRE = /[\\"\u0000-\u001F\u2028\u2029]/g;
      return function stringify(value) {
        if (value == null) {
          return 'null';
        } else if (typeof value === 'number')
          return isFinite(value) ? value.toString() : 'null';
        } else if (typeof value === 'boolean') {
          return value.toString();
        } else if (typeof value === 'object') {
          if (typeof value.toJSON === 'function') {
            return stringify(value.toJSON());
          } else if (isArray(value)) {
            var res = '[';
            for (var i = 0; i < value.length; i++)
              res += (i ? ', ' : '') + stringify(value[i]);
            return res + ']';
          } else if (toString.call(value) === '[object Object]') {
            var tmp = [];
            for (var k in value) {
              // in case "hasOwnProperty" has been shadowed
              if (hasOwnProperty.call(value, k))
                tmp.push(stringify(k) + ': ' + stringify(value[k]));
            }
            return '{' + tmp.join(', ') + '}';
          }
        }
        return '"' + value.toString().replace(escRE, escFunc) + '"';
      };
    })()
  };
}
```