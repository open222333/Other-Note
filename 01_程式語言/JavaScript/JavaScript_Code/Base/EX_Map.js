// Map練習
// 建立Map
// let testMap = new Map();
let testMap = new Map([
  [1, 'one'],
  [2, 'two'],
]);

let keyString = 'a string',
  keyObj = {},
  keyFunc = function () {};

// 透過.set(key, value) 添加Map 鍵值
testMap.set(keyString, 'value associated with string');
testMap.set(keyObj, 'value associated with object');
testMap.set(keyFunc, 'value associated with function');

// 透過.get(key)取得value
let m = testMap.get(keyObj);
console.log(m);

// 透過.delete(key)刪除key
let result_delete = testMap.delete(1);
console.log(result_delete);

// 方法
let result_has = testMap.has(keyString); // 透過.has判斷是否有
console.log(result_has);

// 屬性
console.log(testMap.size); // 取得物件大小