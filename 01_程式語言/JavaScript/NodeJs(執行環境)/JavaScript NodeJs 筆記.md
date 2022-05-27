# JavaScript NodeJs 筆記

```
Node.js 是能夠在伺服器端運行 JavaScript 的開放原始碼、跨平台執行環境。
```

## 參考資料

[官方網站](https://nodejs.org/en/)

[NVM 安裝與使用 Node.js](https://pjchender.dev/nodejs/nvm/)

[官方API文檔](https://nodejs.org/api/)

[使用 VS Code 來開發 Node.js](https://ithelp.ithome.com.tw/articles/10225889)

[Node.js 中文网](http://nodejs.cn/learn/introduction-to-nodejs)

# nvm 指令

[nvm github](https://github.com/nvm-sh/nvm)

```
nvm 是 Node.js 的版本管理器 (version manager)，可在同一台主機上安裝多個版本的 Node.js 環境，因為不同專案可能會使用不同的 Node.js 版本，那就需要透過一個版本管理器來切換不同的 Node.js 版本。
```

```bash
# 安裝nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.37.2/install.sh | bash

# 重新開啟 Terminal 後，檢測是否安裝成功
command -v nvm
# 收到回應
# nvm

# 安裝nvm
# MacOS 可透過 homebrew安裝:
# 安裝 NVM（Node Version Manager）
brew install nvm

# 安裝某個版本的 node
nvm install $version

# 解除安裝指定版本
nvm uninstall $version

# 使用某個版本的 node
nvm use $version

# 列出本機已安裝的版本
nvm ls

# 列出所有可安裝版本
nvm ls-remote

# 察看別名對應node版本
nvm alias

# 設定別名
nvm alias $name $version

# 設定預設開啟的 node 版本
nvm alias default $version

# 查看 nvm 版本
nvm --version

# 察看 Node.js 的安裝路徑
nvm which $version

# 指定要執行的 Node.js 版本
nvm exec $version node

# 察看目前使用版本
nvm current

# 查看某一版本 nvm 的 PATH
nvm which 8.11.1

# MacOS
# 透過 Homebrew 移除Node.js
brew uninstall node

```

# npm 指令

[npm 文檔](https://docs.npmjs.com/)

[npm cli 指令列表](https://docs.npmjs.com/cli/v8/commands)

```
npm是Node.js預設的、用JavaScript編寫的軟體套件管理系統。
```

`package.json`

```
dependencies: 執行環境會需要
devDependencies: 開發或測試環境需要
optionalDependencies: 不一定在每個環境都能夠裝起來
```

```bash
# 查看版本
npm -v

# 更新npm版本
npm install -g npm
	-g
		全局安裝 自動加入PATH

# 生成package.json 會詢問問題
npm init
	--yes
		跳過問題生成默認package.json

# 下載 dependencies(依賴) 根據 package.json
npm install $package@$version
	-g
		全局安裝 自動加入PATH
	--save
		安裝並加入 dependencies
	--save-dev
		安裝並加入 devDependencies
	-O
		安裝並加入 optionalDependencies

# 更新套件
npm update
```

# yarn 指令

[yarn 官方網站](https://yarnpkg.com/)

[yarn cli 指令列表](https://yarnpkg.com/cli/install)

```
Yarn是Facebook在2016年為Node.js JavaScript運行時環境開發的軟件打包系統，它提供了速度，一致性，穩定性和安全性。
```

```bash
# 安裝 項目依賴項
yarn install
```

# node 指令

[node api 文檔](https://nodejs.org/api/)

[node cli 指令列表](https://nodejs.org/api/cli.html)

```bash
# 進入nodejs 工作階段
node

# 執行javascript
node $javascript_file

# 查看版本
node -v
```

# Node.JS 模組規範 - CJS(CommonJS) 與 ESM(ES Modules)

[Node 模組規範鏖戰：難以相容的 CJS 與 ESM - 2022-02-21](https://iter01.com/662822.html)

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

