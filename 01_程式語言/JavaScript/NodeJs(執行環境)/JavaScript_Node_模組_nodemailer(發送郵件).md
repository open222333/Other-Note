# JavaScript Node 模組 nodemailer(發送郵件)

```
Nodemailer 是一個用於 Node.js 應用程序的模塊，可以輕鬆發送電子郵件。
```

## 目錄

- [JavaScript Node 模組 nodemailer(發送郵件)](#javascript-node-模組-nodemailer發送郵件)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[文檔](https://nodemailer.com/about/)

# 指令

```bash
# 安裝
npm install nodemailer
```

# 用法

```JavaScript
"use strict";
const nodemailer = require("nodemailer");

// async..await 需要在函式內使用
async function main() {
  // 使用 ethereal.email 創建測試帳號 沒有用於測試的真實郵件帳戶時才需要
  // Only needed if you don't have a real mail account for testing
  let testAccount = await nodemailer.createTestAccount();

  // create reusable transporter object using the default SMTP transport
  let transporter = nodemailer.createTransport({
    host: "smtp.ethereal.email",
    port: 587,
    secure: false, // true for 465, false for other ports
    auth: {
      user: testAccount.user, // generated ethereal user
      pass: testAccount.pass, // generated ethereal password
    },
  });

  // send mail with defined transport object
  let info = await transporter.sendMail({
    from: '"Fred Foo 👻" <foo@example.com>', // sender address
    to: "bar@example.com, baz@example.com", // list of receivers
    subject: "Hello ✔", // Subject line
    text: "Hello world?", // plain text body
    html: "<b>Hello world?</b>", // html body
  });

  console.log("Message sent: %s", info.messageId);
  // Message sent: <b658f8ca-6296-ccf4-8306-87d57a0b4321@example.com>

  // Preview only available when sending through an Ethereal account
  console.log("Preview URL: %s", nodemailer.getTestMessageUrl(info));
  // Preview URL: https://ethereal.email/message/WaQKMgKddxQDoou...
}

main().catch(console.error);

```