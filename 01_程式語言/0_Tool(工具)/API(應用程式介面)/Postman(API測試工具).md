# Postman(API測試工具)

```
```

## 目錄

- [Postman(API測試工具)](#postmanapi測試工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [轉檔相關](#轉檔相關)
- [API文檔 轉換 Markdown文檔](#api文檔-轉換-markdown文檔)

## 參考資料

[官方文檔](https://learning.postman.com/docs/getting-started/introduction/)

[Postman 設定環境變數](https://ithelp.ithome.com.tw/articles/10253126)

[如何在 POSTMAN 中針對不同環境設定參數進行測試](https://blog.yowko.com/postman-parameter-test/)

## 轉檔相關

[postman-to-markdown - API文檔 轉換 Markdown文檔](https://www.npmjs.com/package/postman-to-markdown?activeTab=readme)

# API文檔 轉換 Markdown文檔

```bash
# 安裝js套件
npm install -g postman-to-markdown

# 轉換
postman-to-markdown Target.postman_collection.json
```