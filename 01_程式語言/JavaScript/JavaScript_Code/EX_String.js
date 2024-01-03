// 字串的API
let s = 'Hello, world'

// 取得一個字串的某些部分
r1 = s.substring(1, 4) // 第2,3,4個字元
r2 = s.slice(1, 4) // 第2,3,4個字元
r3 = s.slice(-3) // 後3個字元
r4 = s.split(', ') // 以,切分字串

//搜尋一個字串
r5 = s.indexOf('l') // 第一個字母l的位置
r6 = s.indexOf('l', 3) // 3以後的第一個l的位置
r7 = s.indexOf('zz') // -1, s不包含zz
r8 = s.lastIndexOf('l') // 最後一個字母l的位置

// ES6 與之後版本的Boolean搜尋函式
r9 = s.startsWith('Hell') // true 字串開頭
r10 = s.endsWith('!') // false 結尾是否
r11 = s.includes('or') // true 是否包含

// 建立一個字串修改過的版本
r12 = s.replace('llo', 'ya') //取代
r13 = s.toLowerCase() // 轉小寫
r14 = s.toUpperCase() //轉大寫
r15 = s.normalize() // unicode NFC常態化
r16 = s.normalize('NFD') // unicode NFD常態化 NFKC NFKD

// 檢視一個字串個別的(16位元)字元
r17 = s.charAt(0) //第一個字元
r18 = s.charAt(s.length - 1) //最後一個字元
r19 = s.charCodeAt(0) // 指定位置上的16位元數字
r20 = s.codePointAt(0) // ES6 能用於> 16位元的編碼位置

// ES2017 中的字串填充函式(padding function)
r21 = 'x'.padStart(3) // 左邊加上空格 直到長度為3
r22 = 'x'.padEnd(3) // 右邊加上空格 直到長度為3
r23 = 'x'.padStart(3, '*') // 左邊加上* 直到長度為3
r24 = 'x'.padEnd(3, '-') // 右邊加上- 直到長度為3

// 空格修剪函式
r25 = 'test '.trim() // 頭尾移除空白 ES5版
r26 = 'test '.trimStart() //移除左邊空白
r27 = 'test '.trimEnd() // 移除右邊空白

// 其他字串方法
r28 = s.concat('!') // 用+運算子來代替
r29 = '<>'.repeat(5) // 重複五次

console.log(r29)
