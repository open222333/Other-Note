# 程式設計 - SPA CSR SSR MPA SSG(前端框架)

## 目錄

- [前端框架 SPA CSR SSR MPA SSG](#前端框架-spa-csr-ssr-mpa-ssg)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [SSR(Server Side Render)](#ssrserver-side-render)
	- [MPA(Multi-Page Application)](#mpamulti-page-application)
	- [CSR(Client Side Render)](#csrclient-side-render)
	- [SPA(Single-page Application)](#spasingle-page-application)
	- [SSG(Static Site Generation)](#ssgstatic-site-generation)
	- [SEO(Search Engine Optimization)](#seosearch-engine-optimization)

## 參考資料

[淺談 SPA、CSR、SSR、MPA、SSG 專有名詞](https://israynotarray.com/other/20210529/2519649612/)

---

## SSR(Server Side Render)

伺服器渲染

## MPA(Multi-Page Application)

多頁式應用程式

## CSR(Client Side Render)

客戶端渲染

Vue, React, Angular 預設上都 CSR

```
伺服器純粹做一些邏輯與資料處理給使用者
然後透過使用者的瀏覽器去處理畫面

通常 後端就只需要傳遞JSON給使用者的瀏覽器處理
```

AJAX(Asynchronous JavaScript and XML) 技術

## SPA(Single-page Application)

單一頁面應用程式

## SSG(Static Site Generation)

SSG的概念會將原本CSR只有一行標籤的狀況變成類似SSR的狀況

```
Static Site Generation
靜態頁面生成
Server Side Generation
伺服器生成
```

CSR 模式會有 SEO 問題

一些為了解決 SEO 問題的框架

```
Nuxt.js(Vue)
Next.js(React)
```

---
## SEO(Search Engine Optimization)

搜尋引擎最佳化