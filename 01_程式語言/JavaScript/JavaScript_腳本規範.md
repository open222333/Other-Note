# JavaScript 腳本規範

```
撰寫 JavaScript / Node.js 腳本（自動化、建置、資料處理）的統一約束：
結構、命名、模組、非同步、錯誤處理、檢查工具。新腳本必守，舊腳本修改時向此靠攏。
```

## 目錄

- [JavaScript 腳本規範](#javascript-腳本規範)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [基本結構](#基本結構)
- [命名](#命名)
- [模組與相依](#模組與相依)
- [非同步](#非同步)
- [參數與設定](#參數與設定)
- [錯誤處理與日誌](#錯誤處理與日誌)
- [檢查工具](#檢查工具)
- [安全](#安全)

## 參考資料

[Node.js 官方文檔](https://nodejs.org/docs/latest/api/)

[ESLint 官方網站](https://eslint.org/)

# 基本結構

- 入口包成 `main()`，結尾統一處理錯誤與退出碼：

```javascript
#!/usr/bin/env node
// 用途：同步訂單到報表（一行說明）
// 用法：node sync_orders.js <tag> [--cleanup]

async function main() {
  // ...
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
```

# 命名

- 變數 / 函式：`camelCase`；類別：`PascalCase`；常數：`UPPER_SNAKE_CASE`。
- 宣告一律 `const` 優先，需重新賦值才用 `let`，禁止 `var`。
- 腳本檔名小寫底線或連字號：`sync_orders.js`。

# 模組與相依

- 新腳本用 ESM（`import` / `export`，`package.json` 設 `"type": "module"`）；既有 CJS 專案維持 `require` 不混用。
- 相依鎖版本：提交 `package-lock.json`；安裝用 `npm ci` 而非 `npm install`（CI／部署時）。
- 在 `package.json` `engines` 指定 Node 版本。

# 非同步

- 一律 `async/await`，禁止 callback 波動拳與裸 `.then()` 鏈。
- 平行處理用 `Promise.all`（全成敗）或 `Promise.allSettled`（允許部分失敗）。
- 迴圈內 await 注意序列化成本，可平行就平行。

# 參數與設定

- 參數解析：簡單旗標可讀 `process.argv`，兩個以上選項用 `node:util` 的 `parseArgs`。
- 環境相關值集中檔案最上方常數區或 `.env`（`dotenv`），不散落邏輯中。
- 密碼、token 讀 `process.env.XXX`，不寫死。

# 錯誤處理與日誌

- 狀態輸出走 `console.error`（stderr），資料輸出走 `console.log`（stdout），方便管線串接。
- 只捕捉預期例外；捕捉後要嘛處理、要嘛 rethrow，禁止吞掉錯誤。
- 失敗以非 0 退出（`process.exit(1)` 或 main().catch 統一處理）。
- 長時間任務定期輸出進度（筆數 / 百分比）。

# 檢查工具

- 提交前必跑：

```bash
# 格式化
npx prettier --write .

# 靜態檢查
npx eslint .
```

- 需要型別的腳本改用 TypeScript 或加 JSDoc 型別註記（`// @ts-check`）。

# 安全

- 外部輸入先驗證再使用；組 SQL 用參數化查詢。
- 呼叫外部指令用 `child_process.execFile` / `spawn` 傳陣列，避免 `exec` 拼字串。
- 不用 `eval`、`new Function` 處理外部資料。
