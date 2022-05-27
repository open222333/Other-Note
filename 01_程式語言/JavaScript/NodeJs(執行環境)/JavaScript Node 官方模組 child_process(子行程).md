# JavaScript Node 模組 child_process(子行程)

```
child_process 模組,通過它可以開啟多個子程序，在多個子程序之間可以共享記憶體空間，可以通過子程序的互相通訊來實現資訊的交換。
```

## 參考資料

[child_process npm 頁面](https://www.npmjs.com/package/child_process)

[child_process api 文檔](https://nodejs.org/docs/latest-v18.x/api/child_process.html)

# 用法

```JavaScript
/**
 * 方法	概述
 * spawn	執行命令，預設為不會新建 shell ，執行一個命令，不會回傳結果。
 * exec	建立 shell 並在 shell 中執行命令，可通過 callback 來獲得執行結果。
 * execFile	執行檔案，預設情況下不會新建 shell
 * fork	執行檔案，建立與父執行緒的神秘溝通管道，可以在這之中進行信息的傳送，也不會回傳結果。
 */

// child_process.spawn
const { spawn } = require('child_process');
const pwd = spawn('pwd');

pwd.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
});

pwd.stderr.on('data', (data) => {
    console.error(`stderr: ${data}`);
});

pwd.on('close', (code) => {
    console.log(`child process exit code: ${code}`);
});

// child_process.exec
const { exec } = require('child_process');
exec('ls', (error, stdout, stderr) => {
    if (error) {
        console.error(`error: ${error}`);
        return;
    }
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
});

// child_process.execFile
const { execFile } = require('child_process');
execFile('node', ['hello.js'], (error, stdout, stderr) => {
    if (error) {
        console.error(`error: ${error}`);
        return;
    }
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
});

// child_process.fork
const { fork } = require('child_process')
const path = require('path')
let child = fork(path.join(__dirname, './fork1.js'))
child.on('message', (data) => {
    console.log(`父行程接收到訊息 -> ${data}`)
})
child.send('hello world')
child.on('error', (err) => {
    console.error(err)
})
```