# TypeScript 筆記

```
TypeScript 是 JavaScript 的型別的超集，它可以編譯成純 JavaScript。編譯出來的 JavaScript 可以執行在任何瀏覽器上。TypeScript 編譯工具可以執行在任何伺服器和任何系統上。TypeScript 是開源的。
```

## 目錄

- [TypeScript 筆記](#typescript-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)

## 參考資料

[官網](https://www.typescriptlang.org/)

[官方文檔](https://www.typescriptlang.org/docs/handbook/basic-types.html)

[中文文檔](https://zhongsp.gitbooks.io/typescript-handbook/content/)

[TypeScript新手指南](https://willh.gitbook.io/typescript-tutorial/introduction/what-is-typescript)

# 安裝

```bash
# 安裝 TypeScript
npm install -g typescript
npm install --save-dev typescript
# 創建默認 tsconfig.json
npx tsc --init
```

```json
{
  "compilerOptions": {
    "target": "esnext",
    "module": "esnext",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules"]
}
```

```
"compilerOptions": 這個對象包含了 TypeScript 編譯器的各種選項配置。

"target": 指定編譯後的 JavaScript 代碼的目標版本。在這個示例中，目標版本被設置為 "esnext"，表示使用最新的 ECMAScript 版本。

"module": 指定生成的 JavaScript 模塊化系統。這裡設置為 "esnext"，表示使用最新的 ECMAScript 模塊化系統。

"strict": 啟用嚴格的類型檢查。將其設置為 true 表示開啟嚴格模式。

"esModuleInterop": 啟用默認的導入和導出行為。將其設置為 true 可以更方便地在 TypeScript 中使用導入和導出。

"skipLibCheck": 設置為 true 將跳過對聲明文件（.d.ts）的類型檢查。這可以加快編譯速度。

"forceConsistentCasingInFileNames": 強制保持文件名的一致性。設置為 true 將確保在不同操作系統上對文件名的大小寫處理一致。

"include": 指定要包含在編譯中的文件或文件夾的匹配模式。這裡的示例配置將 src 文件夾及其子文件夾下的所有文件都包含在編譯中。

"exclude": 指定要排除在編譯之外的文件或文件夾的匹配模式。這裡的示例配置將排除 node_modules 文件夾。
```