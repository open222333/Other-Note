# JavaScript Node 工具 npm(軟體套件管理系統)

```
npm是Node.js預設的、用JavaScript編寫的軟體套件管理系統。
```

## 目錄

- [JavaScript Node 工具 npm(軟體套件管理系統)](#javascript-node-工具-npm軟體套件管理系統)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [配置](#配置)
- [指令](#指令)

## 參考資料

[npm 官網](https://www.npmjs.com/)

[npm 中文官網](https://www.npmjs.cn/)

[npm 文檔](https://docs.npmjs.com/)

[npm cli 指令列表](https://docs.npmjs.com/cli/v8/commands)

# 配置

`package.json`

```
dependencies: 執行環境會需要
devDependencies: 開發或測試環境需要
optionalDependencies: 不一定在每個環境都能夠裝起來
```

# 指令

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

# 列出所有已安裝的套件
npm list
```
