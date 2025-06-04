# apiDoc(API文檔編寫工具 js)

```
```

## 目錄

- [apiDoc(API文檔編寫工具 js)](#apidocapi文檔編寫工具-js)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)
- [apidoc.js 註解範例](#apidocjs-註解範例)

## 參考資料

[官方網址](https://apidocjs.com/)

# 安裝

```sh
npm install -g apidoc
```

```sh
npm install --save-dev apidoc
```

# 指令

```sh
apidoc -i apidocs -o apidocs/output -c apidocs/apidoc.json
```

```
-i：要掃描的目錄（可以指向你的 controller 目錄）
-o：輸出的 HTML 文件位置
```

# apidoc.js 註解範例

```php
/**
 * @api {get} /get_channel_statistics_by_code 根據渠道碼查詢統計
 * @apiName GetChannelStatisticsByCode
 * @apiGroup Channel
 * @apiVersion 1.0.0
 *
 * @apiParam {String} channel_code 渠道碼（必要）
 * @apiParam {String} date 查詢日期 (格式: YYYY-MM-DD)
 *
 * @apiSuccess {String} channel_code 渠道碼
 * @apiSuccess {Number} channel_installed_unique_counts 不重複安裝數
 * @apiSuccess {Number} channel_active_unique_counts 不重複活躍用戶數
 * @apiSuccess {Number} all_channel_installed_unique_counts 所有渠道不重複安裝
 * @apiSuccess {Number} firstOrders 首儲訂單數
 * @apiSuccess {Number} firstAmount 首儲金額
 *
 * @apiError 404 沒有資料
 */
```

```php
/**
 * @api {get} /get_channel_statistics_by_code 根據渠道碼查詢每日統計資料
 * @apiName GetChannelStatisticsByCode
 * @apiGroup ChannelStatistics
 * @apiVersion 1.0.0
 * @apiDescription 根據指定 <code>渠道碼</code> 及 <code>日期</code> 查詢每日統計數據，包括：
 * - 不重複安裝數
 * - 不重複活躍用戶數
 * - 所有渠道不重複安裝數
 * - 首儲訂單數
 * - 首儲購買金額
 *
 * ⚠️ 僅允許特定 IP 存取。
 *
 * @apiHeader {String} Accept application/json
 *
 * @apiParam {String} channel_code 渠道碼（必填）
 * @apiParam {String} date 指定日期（格式：YYYY-MM-DD）（必填）
 *
 * @apiExample {curl} 使用範例：
 *     curl -X GET "https://example.com/get_channel_statistics_by_code?channel_code=123&date=2025-06-03" \
 *          -H "Accept: application/json"
 *
 * @apiSuccess {String} channel_code 渠道碼
 * @apiSuccess {String} message 狀態訊息（如 "ok"）
 * @apiSuccess {Number} channel_installed_unique_counts 不重複安裝數
 * @apiSuccess {Number} channel_active_unique_counts 不重複活躍用戶數
 * @apiSuccess {Number} all_channel_installed_unique_counts 所有渠道不重複安裝數
 * @apiSuccess {Number} first_orders 首儲訂單數
 * @apiSuccess {Number} first_amount 首儲購買金額
 *
 * @apiSuccessExample {json} 回傳成功範例：
 *     HTTP/1.1 200 OK
 *     {
 *       "channel_code": "123",
 *       "message": "ok",
 *       "channel_installed_unique_counts": 125,
 *       "channel_active_unique_counts": 93,
 *       "all_channel_installed_unique_counts": 231,
 *       "first_orders": 12,
 *       "first_amount": 3600
 *     }
 *
 * @apiError (Error 403) Forbidden 拒絕存取，IP 不允許
 * @apiErrorExample {json} 錯誤回應（IP 不允許）：
 *     HTTP/1.1 403 Forbidden
 *     {
 *       "message": "拒絕存取，IP 不允許"
 *     }
 *
 * @apiError (Error 404) NotFound 查無資料
 * @apiErrorExample {json} 錯誤回應（查無資料）：
 *     HTTP/1.1 404 Not Found
 *     {
 *       "message": "沒有資料"
 *     }
 *
 * @apiError (Error 500) ServerError 伺服器錯誤
 * @apiErrorExample {json} 錯誤回應（伺服器錯誤）：
 *     HTTP/1.1 500 Internal Server Error
 *     {
 *       "message": "取得資料發生錯誤",
 *       "error": "SQLSTATE[42S02]: Base table or view not found..."
 *     }
 */
```
