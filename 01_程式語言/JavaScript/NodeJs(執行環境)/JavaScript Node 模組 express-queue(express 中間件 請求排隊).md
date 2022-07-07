# JavaScript Node 模組 express-queue(express 中間件 請求排隊)

```
Express 中間件使用隊列限制同時處理請求的數量
```

## 參考資料

[express-queue npm 頁面](https://www.npmjs.com/package/express-queue)

# 指令

```bash
# 安裝
npm install express-queue
```

# 用法

```JavaScript
// 基本用法
const express = require('express');
const queue = require('express-queue');
const app = express();

app.use(queue({ activeLimit: 2, queuedLimit: -1 }));
app.get('/api', queue({ activeLimit: 2, queuedLimit: -1}));
// activeLimit - 同時處理的最大請求
// queuedLimit - 在拒絕之前隊列中的最大請求數（-1 表示不拒絕）
// rejectHandler - 達到 queuedLimit 時調用的處理程序

// 獲取當前隊列長度
const express = require('express');
const queue = require('express-queue');
const app = express();
const expressQueue = require('../');
const queueMw = expressQueue({ activeLimit: 2, queuedLimit: -1 });

app.use(queueMw);
console.log(`queueLength: ${queueMw.queue.getLength()}`);
```

```JavaScript
'use strict';
// 範例
const debug   = require('debug')('app');
const express = require('express');
const http    = require('http');
const httpPort = 8080;

const app = express();

const queue = require('express-queue');
// Using queue middleware
const queueMw = queue({ activeLimit: 2, queuedLimit: 6 });
app.use(queueMw);
// May be also:
// app.use(queue({ activeLimit: 2, queuedLimit: -1 }));
// - or -
// app.use('/test1', queue({ activeLimit: 2 }) );

const RESPONSE_DELAY = 1000; // Milliseconds

let counter = 0;

app.get('/test1', function (req, res) {
  let cnt = counter++; // local var inside the closure
  console.log(`get(test1): [${cnt}/request] queueLength: ${queueMw.queue.getLength()}`);

  const result = { test: 'test' };

  setTimeout(function() {
    console.log(`get(test1): [${cnt}/ready] queueLength: ${queueMw.queue.getLength()}` );
    res
      .status(200)
      .send(result);
    console.log(`get(test1): [${cnt}/sent] queueLength: ${queueMw.queue.getLength()}`);
  }, RESPONSE_DELAY);

});


const server = http.createServer(app);

server.listen(httpPort, function () {
  console.log(`* Server listening at ${server.address().address}:${server.address().port}`)
});
```