# JavaScript Node 模組 aws-sdk(AWS SDK)

```
AWS SDK for JavaScript
使用 JavaScript API 來建置 Node.js 或瀏覽器的程式庫或應用程式。
```

## 目錄

- [JavaScript Node 模組 aws-sdk(AWS SDK)](#javascript-node-模組-aws-sdkaws-sdk)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[aws-sdk npm 頁面](https://www.npmjs.com/package/aws-sdk)

[AWS 文檔](https://docs.aws.amazon.com/zh_tw/sdk-for-javascript/v3/developer-guide/welcome.html)

[AWS SDK for JavaScript v3 API文檔](https://docs.aws.amazon.com/AWSJavaScriptSDK/v3/latest/index.html)

# 指令

```bash
# 安裝
npm install aws-sdk
```

# 用法

```JavaScript
let aws = require('aws-sdk');
// configure AWS SDK
aws.config.loadFromPath('./aws.config.json');
```

`aws.config.json`:

```json
{
    "accessKeyId": "accessKeyId",
    "secretAccessKey": "secretAccessKey",
    "region": "us-west-2"
}
```