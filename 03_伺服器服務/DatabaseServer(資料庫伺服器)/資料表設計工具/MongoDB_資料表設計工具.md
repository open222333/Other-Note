# MongoDB 資料表設計工具

```
MongoDB 是 Schema-less（無固定結構），但實務上仍需要設計文件結構。
設計重點不是欄位型別，而是「嵌入（Embed）vs 引用（Reference）」的取捨。
工具用於視覺化現有集合結構、設計文件模型、分析資料分佈。
```

## 目錄

- [MongoDB 資料表設計工具](#mongodb-資料表設計工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [工具比較](#工具比較)
- [MongoDB Compass（Schema Analyzer）](#mongodb-compassschema-analyzer)
- [Hackolade](#hackolade)
  - [安裝](#安裝)
  - [設計文件模型](#設計文件模型)
- [Studio 3T](#studio-3t)
- [文件結構設計原則](#文件結構設計原則)
  - [嵌入 vs 引用](#嵌入-vs-引用)
  - [常見設計模式](#常見設計模式)

## 參考資料

[MongoDB Compass 官方](https://www.mongodb.com/products/tools/compass)

[Hackolade 官網](https://hackolade.com/)

[Studio 3T 官網](https://studio3t.com/)

[MongoDB Schema Design Best Practices](https://www.mongodb.com/developer/products/mongodb/mongodb-schema-design-best-practices/)

---

# 工具比較

| 工具 | 類型 | 費用 | 分析現有 DB | 設計新 Schema | 視覺化 | 適合情境 |
|---|---|---|---|---|---|---|
| **MongoDB Compass** | 桌面 | 免費 | ✅（Schema 取樣） | ❌ | ✅ | 分析現有集合的資料分佈與結構 |
| **Hackolade** | 桌面 | 付費（有試用） | ✅（逆向工程） | ✅（拖拉設計） | ✅ | 專業 NoSQL Schema 設計，支援 BSON 型別 |
| **Studio 3T** | 桌面 | 付費（有試用） | ✅ | ✅ | ✅ | 全功能 MongoDB IDE，Schema 是附加功能 |
| **dbdiagram.io** | 網頁 | 免費 | ❌ | ✅（手繪） | ✅ | 快速手繪集合關聯圖，不匯出 MongoDB 語法 |

---

# MongoDB Compass（Schema Analyzer）

官方 GUI 工具，內建 Schema 分析功能，對現有集合取樣後顯示欄位分佈。

**查看 Schema：**

```
1. 連接 MongoDB
2. 選擇 Database → Collection
3. 點擊「Schema」頁籤
4. 點擊「Analyze Schema」（取樣最多 1000 份文件）
```

取樣後顯示：
- 每個欄位出現的比例（%）
- 欄位的資料型別分佈
- 字串欄位的常見值
- 數值欄位的分佈直方圖

> 適合用來了解現有集合的實際資料結構，特別是繼承的舊專案。

---

# Hackolade

專為 NoSQL 設計的 Schema 設計工具，原生支援 BSON 型別（ObjectId、Date、Decimal128 等）與嵌入文件設計。

## 安裝

```bash
# macOS
brew install --cask hackolade

# 或從官網下載
# https://hackolade.com/download.html
```

## 設計文件模型

1. 新增 **MongoDB Target** 模型
2. 建立 **Collection**，加入欄位：
   - 支援型別：`String`、`ObjectId`、`Date`、`Array`、`Object`（嵌入文件）等
   - 可設定 `required`、`default`、`description`
3. 設定嵌入文件（Sub-document）：
   - 新增 `Object` 型別欄位 → 展開後加入子欄位
4. 設定集合間的**引用關係**（Reference）：
   - 拖拉連線，選擇 `Foreign Key`（邏輯上的，MongoDB 不強制）
5. **正向工程**：匯出 JSON Schema 或 MongoDB Validator 語法

匯出 MongoDB Schema Validator：

```json
{
  "$jsonSchema": {
    "bsonType": "object",
    "required": ["username", "email"],
    "properties": {
      "username": {
        "bsonType": "string",
        "description": "必填，唯一"
      },
      "email": {
        "bsonType": "string"
      },
      "address": {
        "bsonType": "object",
        "properties": {
          "city":    { "bsonType": "string" },
          "zipcode": { "bsonType": "string" }
        }
      }
    }
  }
}
```

套用至集合：

```js
db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["username", "email"],
      properties: {
        username: { bsonType: "string" },
        email:    { bsonType: "string" }
      }
    }
  }
})

// 修改現有集合的驗證規則
db.runCommand({
  collMod: "users",
  validator: { $jsonSchema: { ... } }
})
```

---

# Studio 3T

功能完整的 MongoDB IDE，Schema Explorer 可從現有集合反產結構圖。

```
1. 連接 MongoDB
2. 右鍵 Collection → Schema Explorer
3. 取樣文件後顯示欄位樹狀結構
4. 可匯出 JSON Schema
```

付費功能包含：視覺化查詢建構器、SQL 查詢轉 MQL、資料遷移等。

---

# 文件結構設計原則

## 嵌入 vs 引用

MongoDB 沒有 JOIN，資料關聯有兩種策略：

| | 嵌入（Embed） | 引用（Reference） |
|---|---|---|
| 做法 | 子文件直接放入父文件 | 儲存另一個集合的 `_id` |
| 查詢效率 | 高（單次讀取） | 低（需兩次查詢或 `$lookup`） |
| 更新效率 | 低（需更新所有父文件） | 高（只更新被引用的文件） |
| 文件大小 | 增大（上限 16MB） | 小 |
| 適合情境 | 一對一、一對少量多、子資料不共用 | 一對大量多、多對多、子資料被多處共用 |

**判斷規則：**

```
「子資料會被多個父文件共用嗎？」
  → 是 → 引用（Reference）
  → 否 → 嵌入（Embed）

「子資料數量有上限嗎？」
  → 無上限（如留言數） → 引用，否則文件會無限膨脹
  → 有限且少（如地址 1–3 筆） → 嵌入
```

## 常見設計模式

**一對多（嵌入）** — 訂單 + 明細

```js
// orders collection
{
  _id: ObjectId("..."),
  customer_id: ObjectId("..."),   // 引用 customers
  total: 1500,
  items: [                        // 嵌入，不共用
    { product_id: ObjectId("..."), name: "商品A", qty: 2, price: 300 },
    { product_id: ObjectId("..."), name: "商品B", qty: 1, price: 900 }
  ]
}
```

**一對多（引用）** — 使用者 + 文章

```js
// posts collection（引用 users，文章數量無上限）
{
  _id: ObjectId("..."),
  author_id: ObjectId("..."),     // 引用 users._id
  title: "文章標題",
  content: "..."
}
```

**多對多（引用陣列）** — 文章 + 標籤

```js
// posts collection
{
  _id: ObjectId("..."),
  title: "文章標題",
  tag_ids: [ObjectId("..."), ObjectId("...")]   // 引用 tags
}

// tags collection
{
  _id: ObjectId("..."),
  name: "技術"
}
```
