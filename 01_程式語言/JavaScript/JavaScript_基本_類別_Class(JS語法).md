# JavaScript 基本 類別 Class

```
一個類別的成員(members)，或者說實體(instances)，會有自己的特性(properties)存放或定義它們的狀態，定義其行為的方法(methods)
```

## 目錄

- [JavaScript 基本 類別 Class](#javascript-基本-類別-class)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [類別與原型](#類別與原型)
- [類別與建構器](#類別與建構器)
	- [建構器和new.target](#建構器和newtarget)
	- [建構器、類別身份，以及instanceof](#建構器類別身份以及instanceof)
	- [constructor特性](#constructor特性)
- [使用class關鍵字的類別](#使用class關鍵字的類別)
	- [靜態方法(static method)](#靜態方法static-method)
	- [取值器(getter)、設值器(setter)、和其他方法形式](#取值器getter設值器setter和其他方法形式)
	- [公開、私有和靜態欄位](#公開私有和靜態欄位)
	- [範例](#範例)
- [子類別](#子類別)
	- [使用extends和super衍生子類別](#使用extends和super衍生子類別)
	- [委任(Delegation)而非繼承](#委任delegation而非繼承)
	- [類別階層架構(Class Hierarchies)與抽象類別(Abstract Classes)](#類別階層架構class-hierarchies與抽象類別abstract-classes)

## 參考資料

[]()

# 類別與原型

`工廠函式`

```JavaScript
// 簡單的JavaScript類別

function range(from, to) {
  // 工廠函式 回傳一個新的範圍(range)物件
  let r = Object.create(range.methods); // 使用Object.create() 創建一個物件繼承 定義共有的方法

  // 儲存新的範圍物件
  r.from = from;
  r.to = to;

  return r;
}

// 這個原型物件定義所有的範圍物件都會繼承的方法
range.methods = {
  // 若x在範圍中 則回傳true 是用數值範圍 文字 Date
  includes(x) { return this.from <= x && x <= this.to; },

  // 使這個類別的實體可迭代的一個產生器函式 只能用於數值範圍
  *[Symbol.iterator]() {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  },

  // 回傳該範圍的一個字串表示值
  toString() { return "(" + this.from + "..." + this.to + ")"; }
};

// range物件使用範例
let r = range(1, 3); // 創建range物件
r.includes(2) // true : 2在範圍中
r.toString() // => "(1...3)"
console.log([...r]); // 藉由迭代器轉成陣列
```

# 類別與建構器

1. `建構式函式 不創建以及回傳該物件 只有初始化this`

2. `由工廠函式修改成建構式函式`

3. `建構器由關鍵字new調用`

```JavaScript
// 類別與建構器

/**
 * 以下為舊式定義類別方法 JavaScript已有class並擁有良好的支援
*/

// 使用建構器的一個Range類別
function Range(from, to) {
  // Range物件的一個建構式函式
  // 不創建以及回傳該物件 只有初始化this
  this.from = from;
  this.to = to;
}

// 所有的Range物件都繼承此物件
// 此特性需命名為prototype才能運作
Range.prototype = {
  // 若x在此範圍中 回傳true 適用數值 文字 Date
  includes: function (x) { return this.from <= x && x <= this.to; },
  // 使類別的實體可迭式的一個產生器函式 適用 數值
  [Symbol.iterator]: function* () {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  },
  // 回傳該範圍的一個字串表示值
  toString: function () { return "(" + this.from + "..." + this.to + ")"; }
};
```

## 建構器和new.target

```
在函式的主體中能以運算式 new.target 分辨該函式是否作為一個建構器調用
若運算式的值有定義，該函式是透過new關鍵字被當作建構式調用
若為undefined，則是當作函式調用
```

## 建構器、類別身份，以及instanceof

```JavaScript
// 建構器、類別身份，以及instanceof
// 使用建構器的一個Range類別
function Range(from, to) {
  // Range物件的一個建構式函式
  // 不創建以及回傳該物件 只有初始化this
  this.from = from;
  this.to = to;
}

// 所有的Range物件都繼承此物件
// 此特性需命名為prototype才能運作
// Range類別會把預先定義的Range.prototype物件覆寫為它自己的一個物件，而定義的原型物件沒有constructor特性
Range.prototype = {
  // 若x在此範圍中 回傳true 適用數值 文字 Date
  includes: function (x) { return this.from <= x && x <= this.to; },
  // 使類別的實體可迭式的一個產生器函式 適用 數值
  [Symbol.iterator]: function* () {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  },
  // 回傳該範圍的一個字串表示值
  toString: function () { return "(" + this.from + "..." + this.to + ")"; }
};

// 建立一個Range物件 使用new
let r = new Range(1, 3);

// 測試 物件是否為某類別
// instanceof運算子 是檢查 r是否繼承自Range.prototype
console.log(r instanceof Range);

// 證實上方說明
function Strange() {};
Strange.prototype = Range.prototype;
console.log(new Strange() instanceof Range);

// isPrototypeOf() 測試物件的原型鏈
// console.log(range.methods.isPrototypeOf(r);
```

## constructor特性

```
constructor（建構子）是個隨著 class 一同建立並初始化物件的特殊方法。
```

```JavaScript
// constructor特性
// 每個常規的JavaScript都自動會有一個prototype特性，此特性為一個物件，
let F = function () {}; // 函式物件
let p = F.prototype; // 與F關聯的原型物件
let c = p.constructor; // 與原型關聯的函式
console.log(c === F); // F.prototype.constructor === F

let o = new F(); // 建立類別F的一個物件o
console.log(o.constructor === F); // constructor特性指出其類別


// Range類別會把預先定義的Range.prototype物件覆寫為它自己的一個物件，而定義的原型物件沒有constructor特性
function Range(from, to) {
  this.from = from;
  this.to = to;
}

Range.prototype = {
  constructor: Range, // 明確設定建構器的反向參考
  includes: function (x) { return this.from <= x && x <= this.to; },
  [Symbol.iterator]: function* () {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  },
  toString: function () { return "(" + this.from + "..." + this.to + ")"; }
};

// 另一個方式
// 使用預先定義的原型物件以及其constructor特性 一次新增一個方法給它
Range.prototype.includes = function (x) {
  return this.from <= x && x <= this.to;
};
Range.prototype.toString = function () {
  return "(" + this.from + "..." + this.to + ")";
};
```

# 使用class關鍵字的類別

```JavaScript
class Range {
  constructor(from, to) {
    // 儲存這物件的非繼承特性
    this.from = from;
    this.to = to;
  }

  // 若x在此範圍則回傳true 否則回傳false
  include(x) { return this.from <= x && x <= this.to; }

  // 使此類別的實體可迭代
  *[Symbol.iterator]() {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  }

  toString() { return `(${this.from}...${this.to})`; }
}

// 類別Span繼承自Range
class Span extends Range {
  constructor(start, length) {
    if (length >= 0) {
      super(start, start + length);
    } else {
      super(start + length, start)
    }
  }
}
```

## 靜態方法(static method)

```JavaScript
class Range {
  constructor(from, to) {
    this.from = from;
    this.to = to;
  }

  include(x) { return this.from <= x && x <= this.to; }

  *[Symbol.iterator]() {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  }

  toString() { return `(${this.from}...${this.to})`; }

  // 靜態方法
  // 定義的方法為Range.parse(), 而非 Range.prototype.parse(), 須透過建構器調用
  static parse(s) {
    let matches = s.matches(/^\((\d+)\.\.\.(\d+)\)$/);
    if (!matches) {
      throw new TypeError(`Cannot parse Range from "${s}".`)
    }
    return new Range(parseInt(matches[1]), parseInt(matches[2]));
  }
}

let r = Range.parse('(1...10'); // 回傳一個新的Range物件
r.parse('(1...10)'); // TypeError: r.parse非函式
```

## 取值器(getter)、設值器(setter)、和其他方法形式

```
物件字面值中允許的所有簡寫方法定義語法
在類別主體中也都允許
```

```JavaScript
class Range {
  constructor(from, to) {
    this.from = from;
    this.to = to;
  }

  include(x) { return this.from <= x && x <= this.to; }

  // 產生器方法
  *[Symbol.iterator]() {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  }

  toString() { return `(${this.from}...${this.to})`; }

  static parse(s) {
    let matches = s.matches(/^\((\d+)\.\.\.(\d+)\)$/);
    if (!matches) {
      throw new TypeError(`Cannot parse Range from "${s}".`)
    }
    return new Range(parseInt(matches[1]), parseInt(matches[2]));
  }
}
```

## 公開、私有和靜態欄位

```JavaScript
class Buffer_old {
  constructor() {
    this.size = 0;
    this.capacity = 4096;
    this.buffer = new Uint8Array(this.capacity);
  }
}

// 新語法 建構器被標準化的實體欄位可省略this
class Buffer {
  #size = 0; // 私有欄位
  // this.#size = 0; // 沒有效果，除非有直接在該類別主體中包含那個欄位的宣告
  capacity = 4096;
  buffer = new Uint8Array(this.capacity); // 仍然需要使用this來參考這些欄位

  // 定義一個取值器(getter)來提供那個值得唯讀存取
  get size() { return this.#size; }

  // 在一個公開或私有欄位宣告前加上static，那些欄位會被創建為建構器函式的特性，而非建構器的特性
  static integerRnagePattern = /^\((\d+)\.\.\.(\d+)\)$/;
  static parse(s) {
    let matches = s.matches(Range.integerRnagePattern);
    if (!matches) {
      throw new TypeError(`Cannot parse Range from "${s}".`)
    }
    return new Range(parseInt(matches[1]), matches[2]);
  }
}
```
## 範例

```JavaScript
// 範例 一個複數類別
/**
 * 這個Complex類別的實體代表複數
 * 一個複數就是一個實體(real number)與一個虛數(imaginary number)的和(sum)
 * 而那個虛數i是-1的平方根(square root)
 */
class Complex {
  constructor (real, imaginary) {
    this.r = real; // 此欄位存放數字的實部
    this.i = imaginary; // 此欄位存放數字的虛部
  }

  // 複數的加法
  plus (that) {
    return new Complex(this.r + that.r, this.i + that.i);
  }

  // 複數的乘法
  times (that) {
    return new Complex(this.r * that.r - this.i * that.i, this.r * that.i + this.i * that.r);
  }

  // 複數算數方法的靜態變體
  static sum (c, d) { return c.plus(d); }
  static product (c, d) { return c.times(d); }

  // 定義取值器的實體方法
  get real () { return this.r; }
  get imaginary () { return this.i; }
  get magnitude () { return Math.hypot(this.r, this.i); }

  // 類別應該幾乎會有一個toString()方法
  toString () { return `${this.r},${this.i}`; }

  // 定義一個方法 測試類別的兩個實體
  equals (that) {
    return that instanceof Complex && this.r === that.r && this.i === that.i;
  }

  // 一旦類別主體內的靜態欄位受到支援
  // 就能定義常數
  // static ZERO = new Complex(0, 0);
}

// 存放預先定義的一些實用常數
Complex.ZERO = new Complex(0, 0);
Complex.ONE = new Complex(1, 0);
Complex.I = new Complex(0, 1);

// 新增方法至現有的類別
Complex.prototype.conj = function () { return new Complex(this.r, -this.i); };

// 使用類別範例
let c = new Complex(2, 3);
let d = new Complex(c.i, c.r);

console.log(c.plus(d).toString());
c.magnitude // 一個取值器函式
Complex.product(c, d) // 靜態方法
console.log(Complex.ZERO.toString())
```

# 子類別

```
class A 為 超類別(superclass)
class B 為 子類別(subclass)
class B extend(擴充) class A
class A subclass(衍生) class B
```

## 使用extends和super衍生子類別

```JavaScript
// TypedMap.js 會檢查間值與值之型別的一個Map的子類別
class TypedMap extends Map {
  constructor (keyType, valueType, entries) {
    if (entries) {
      for (let [k, v] of entries) {
        if (typeof k !== keyType || typeof v !== valueType) {
          throw new TypeError(`Worng type for entry [${k}, ${v}]`);
        }
      }
    }

    // 以(經過型別檢查的)初始項目初始化超類別
    super(entries);

    // 藉由儲存型別來初始化這個子類別
    this.keyType = keyType;
    this.valueType = valueType;
  }

  // 重新定義set()
  set (key, value) {
    // 若鍵值或值的型別不對，就擲出一個錯誤
    if (this.keyType && typeof key !== this.keyType) {
      throw new TypeError(`${key} is not of type ${this.keyType}`);
    }
    if (this.valueType && typeof value !== this.valueType) {
      throw new TypeError(`${value} is not of type ${this.valueType}`);
    }

    // 若型別正確 就用超類別版本的set()方法，以實際把項目新增到映射
    return super.set(key, value);
  }
}
```

## 委任(Delegation)而非繼承

```
新的類別 若所需的方法功能重複 可委任給其他類別的方法
```

`優先選擇合成而非繼承`

```JavaScript
/**
 * 透過委任實作的一個類集合類別
 *
 * 追蹤一個值最新增了幾次
 */

class Histogram {

  // 初始化 創建一個Map物件
  constructor () {
    this.map = new Map();
  }

  //若該鍵值沒有出現在此Map則為0
  count (key) { return this.map.get(key) || 0; }

  // 類集合方法has()會在次數大於0時回傳True
  has (key) { return this.count(key > 0); }

  // 直方圖的大小(size)為Map中的項目
  get size () { return this.map.size; }

  // 新增一個鍵值，則遞增它在Map中的次數
  add (key) { this.map.set(key, this.count(key) + 1); }

  // 刪除一個鍵值 若次數降為零 則在Map刪除該鍵值
  delete (key) {
    let count = this.count(key);
    if (count === 1) {
      this.map.delete(key);
    } else if (count > 1) {
      this.map.set(key, count - 1);
    }
  }

  // 迭代 一個Histogram只會回傳儲存在其中的鍵值
  [Symbol.iterator] () { return this.map.keys(); }

  // 其他迭代器方法會委任Map物件
  keys () { return this.map.keys(); }
  values () { return this.map.values(); }
  entries () { return this.addmap.entries(); }
}
```

## 類別階層架構(Class Hierarchies)與抽象類別(Abstract Classes)

```JavaScript
// 一個階層架構的抽象與具體集合類別

/**
 * AvstractSet 類別定義單一抽象方法has()
 */
class AbstracSet {
  has(x) {
    throw new Error("Abstrac method");
  }
}

/**
 * NotSet 是 AbstractSet的具體子類別
 * 此集合的成員，全都為不是其他集合成員的值
 * 因以另外集合來定義，不可寫入
 * 因有無限成員，不可列舉
 * 功能為，測試成員資格，並使用科學記號將之轉換為一個字串
 */
class NotSet extends AbstracSet {
  constructor(set) {
    super();
    this.set = set;
  }

  // 對繼承來的抽象方法實作
  has(x) {
    return !this.set.has(x);
  }

  toString() {
    return `{x| x ∉ ${this.set.toString()}}`;
  }
}

/**
 * RangeSet 是 AbstractSet的具體子類別
 * 此集合的成員，全都是介於from和to之間的值，包含端點
 * 因成員可以是浮點數，不可列舉，並且無有意義的大小
 */
class RangeSet extends AbstracSet {
  constructor(from, to) {
    super();
    this.from = from;
    this.to = to;
  }

  has(x) {
    return x >= this.from && x <= this.to;
  }
  toString() {
    return `x| ${this.from} ≤ x ≤ ${this.to}`;
  }
}

/**
 * AbstractEnumerableSet 是 AbstractSet的抽象子類別
 * 定義一個抽象的取值器回傳集合的大小 以及 一個抽象的迭代器
 * 實作具體的 isEmpty(),toString(),equals()方法
 */
class AbstractEnumerableSet extends AbstracSet {
  get size() {
    throw new Error("Abstract method");
  }

  [Symbol.iterator]() {
    throw new Error("Abstract method");
  }

  isEmpty() {
    return this.size === 0;
  }
  toString() {
    return `{${Array.from(this).join(",")}}`;
  }
  equals(set) {
    // 如果另一個集合不可列舉 回傳 false
    if (!(set instanceof AbstractEnumerableSet)) return false;
    // 如果沒有相同大小 就不相等
    if (this.size !== set.size) return false;
    // 迴圈跑過這個集合的元素，若有元素不在另一個集合，則不相等
    for (let element of this) {
      if (!set.has(element)) return false;
    }

    // 都相符則集合相等
    return true;
  }
}

/**
 * SingletonSet 是 AbstractSet的具體子類別
 * 單體集合是一個只有單一成員的唯讀集合
 */
class SingletonSet extends AbstractEnumerableSet {
  constructor(member) {
    super();
    this.member = member;
  }

  // 實作3個方法
  has(x) {
    return x === this.member;
  }
  get size() {
    return 1;
  }
  *[Symbol.iterator]() {
    yield this.member;
  }
}

/**
 * AbstractWritableSet 是 AbstractEnumerableSet的抽象子類別
 * 定義 抽象方法insert(),remove()
 * 在集合中插入和移除個別元素，實作 add(),subtract(),intersect()
 */
class AbstractWritableSet extends AbstractEnumerableSet {
  insert(x) {
    throw new Error("Abstract method");
  }
  remove(x) {
    throw new Error("Abstract method");
  }

  add(set) {
    for (let element of set) {
      this.insert(element);
    }
  }

  subtract(set) {
    for (let element of set) {
      this.remove(element);
    }
  }

  intersect(set) {
    for (let element of set) {
      if (!set.has(element)) {
        this.remove(element);
      }
    }
  }
}

/**
 * BitSet 是 AbstractWritableSet的具體子類別
 * 具有有效率的固定大小集合
 * 適合用於 元素是非負整數而且小於某個最大大小集合
 */
class BitSet extends AbstractWritableSet {
  constructor(max) {
    super();
    this.max = max; // 最大整數
    this.n = 0; // 集合中有多少整數
    this.numBytes = Math.floor(max / 8) + 1; // 需要的位元組(bytes)
    this.data = new Uint8Array(this.numBytes);
  }

  // 內部方法 檢查一個值是否為此集合的合法成員
  _valid(x) {
    return Number.isInteger(x) && x >= 0 && x <= this.max;
  }

  // 內部方法 測試data陣列的指定位元組的指定位元是否有設定 回傳true false
  _has(byte, bit) {
    return (this.data[byte] & BitSet.bit[bit]) !== 0;
  }

  // BitSet 是否有 x
  has(x) {
    if (this._valid(x)) {
      let byte = Math.floor(x / 8);
      let bit = x % 8;
      return this._has(byte, bit);
    } else {
      return false;
    }
  }

  // 把值x 插入到這個BitSet
  insert(x) {
    if (this._valid(x)) {
      let byte = Math.floor(x / 8);
      let bit = x % 8;
      if (!this._has(byte, bit)) {
        /**
         * 若那個位元尚未設定則設定他並遞增集合
         * x |= y	x = x | y
         */
        this.data[byte] |= BitSet.bits[bit];
        this.n++;
      }
    } else {
      throw new TypeError("Invalid set element: " + x);
    }
  }

  remove(x) {
    if (this._valid(x)) {
      let byte = Math.floor(x / 8);
      let bit = x % 8;
      if (this._has(byte, bit)) {
        this.data[byte] &= BitSet.masks[bit];
        this.n--;
      }
    } else {
      throw new TypeError("Invalid set element: " + x);
    }
  }

  get size() {
    return this.n;
  }

  *[Symbol.iterator]() {
    for (let i = 0; i <= this.max; i++) {
      if (this.has(i)) {
        yield i;
      }
    }
  }
}

BitSet.bits = new Uint8Array([1, 2, 4, 8, 16, 32, 64, 128]);
BitSet.masks = new Uint8Array([~1, ~2, ~4, ~8, ~16, ~32, ~64, ~128]);
```
