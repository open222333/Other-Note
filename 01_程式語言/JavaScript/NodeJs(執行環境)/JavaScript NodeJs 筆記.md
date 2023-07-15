# JavaScript NodeJs 筆記

```
Node.js 是能夠在伺服器端運行 JavaScript 的開放原始碼、跨平台執行環境。
```

## 目錄

- [JavaScript NodeJs 筆記](#javascript-nodejs-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [安裝相關](#安裝相關)
		- [應用相關](#應用相關)
- [Node.JS 模組規範 - CJS(CommonJS) 與 ESM(ES Modules)](#nodejs-模組規範---cjscommonjs-與-esmes-modules)
	- [在 Node 中使用 ESM 語法：](#在-node-中使用-esm-語法)
	- [CJS 引用 ESM 模組](#cjs-引用-esm-模組)

## 參考資料

### 安裝相關

[Building Node.js on supported platforms](https://github.com/nodejs/node/blob/main/BUILDING.md#building-nodejs-on-supported-platforms)



### 應用相關

[Node 模組規範鏖戰：難以相容的 CJS 與 ESM - 2022-02-21](https://iter01.com/662822.html)

[Node.js 中文网](http://nodejs.cn/learn/introduction-to-nodejs)

[使用 VS Code 來開發 Node.js](https://ithelp.ithome.com.tw/articles/10225889)


# Node.JS 模組規範 - CJS(CommonJS) 與 ESM(ES Modules)

```
自 13.2.0 版本開始，Node.js 在保留了 CommonJS（CJS）語法的前提下，新增了對 ES Modules（ESM）語法的支援。
```

Node 預設只支援 CJS 語法，這意味著你書寫了一個 ESM 語法的 js 檔案，將無法被執行。

## 在 Node 中使用 ESM 語法：

1. `在 package.json 中新增 "type": "module" 配置項。`Node 會將和 package.json 檔案同路徑下的模組，全部當作 ESM 來解析。
   
2. `將希望使用 ESM 的檔案改為 .mjs 字尾。`不需要修改 package.json，Node 會自動地把全部 xxx.mjs 檔案都作為 ESM 來解析。

小技巧 - 存在較多陳舊的 CJS 模組

```
把它們全部挪到一個資料夾，在該資料夾路徑下新增一個內容為 {"type": "commonjs"} 的 package.json 即可。
```

## CJS 引用 ESM 模組

```
不能並使用 require 引入 ES 模組
應當改為使用 CJS 模組內建的動態 import 方法
```

```JavaScript
import('./esm/b.js').then(({ foo }) => {
    foo();
});

// or

(async () => {
    const { foo } = await import('./esm/b.js');
})();
```