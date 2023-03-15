# JavaScript 內建-物件 Intl(國際化API)

```
Intl 對像是 ECMAScript 國際化 API 的一個命名空間，它提供了精確的字符串對比、數字格式化，和日期時間格式化。
由三個類別構成 Intl.Collator, Intl.NumberFormat, Intl.DateTimeFormat
```

## 目錄

- [JavaScript 內建-物件 Intl(國際化API)](#javascript-內建-物件-intl國際化api)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [Intl.NumberFormat (格式化數字)](#intlnumberformat-格式化數字)
	- [Intl.DateTimeFormat](#intldatetimeformat)
	- [Intl.DisplsyName](#intldisplsyname)

## 參考資料

[Intl MDN Web Doc](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl)

[Intl.Collator](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Intl/Collator)

[Intl.NumberFormat](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat)

[Intl.DateTimeFormat](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Intl/DateTimeFormat)

[Timezone - Get Timezone Enum IANA 取的IANA時區列表](https://learn.microsoft.com/zh-tw/rest/api/maps/timezone/get-timezone-enum-iana?tabs=HTTP)

# 用法

## Intl.NumberFormat (格式化數字)

```JavaScript
// Intl.NumberFormat
new Intl.NumberFormat('en').format(1000); // 1,000

// Intl.NumberFormat (格式化數字)
/**
 * stytle: 數字格式類型，預設 "decimal"，百分比 "percent"，金額 "currency"
 * currency: 如果 stytle 是 "currency"，則此特性為必要。ISO貨幣碼 USD(美元),GBP(英鎊)
 * currencyDisplay: 如果 stytle 是 "currency"，貨幣如何顯示，預設值: 貨幣符號 "symbol"。使用ISO碼 "code", 貨幣名稱 "name"
 * useGrouping: 是否使用千分隔號。 false 則為不使用。
 * minimumIntegerDigits: 格式化整數最小位數，若未數較少則往左補零。預設值 1。可用範圍 0 - 20。
 * minimumFractionDigits: 格式化小數部分，小於最小值則往右補零。預設為 0。
 * maximumFractionDigits: 格式化小數部分，大於最大值則被捨入(rounded)。預設為 3。可用範圍 0 - 20。
 * minimumSignificantDigits: 格式化有效數字，小於最小值則往右補零。若有指定則覆寫小數、整數特性。可用範圍 1 - 21。
 * maximumSignificantDigits: 格式化有效數字，小於最小值則往右補零。若有指定則覆寫小數、整數特性。可用範圍 1 - 21。
 */
let euros = Intl.NumberFormat("es", {style: "currency", currency: "EUR"});
euros.format(10) // => '10,00 €' 10歐元 西班牙格式

let pounds = Intl.NumberFormat("en", {style: "currency", currency: "GBP"});
pounds.format(1000) // => '£1,000.00' 1000英鎊 英國格式

// 將format當成獨立函式使用
let data = [0.05, 0.75, 1];
let formatData = Intl.NumberFormat(undefined, {
  style: "percent",
  minimumFractionDigits: 1,
  maximumFractionDigits: 1,
}).format;
data.map(formatData); // => [ '5.0%', '75.0%', '100.0%' ]

// 阿拉伯語用他們數字表達十進位數字
let arabic = Intl.NumberFormat("ar", {useGrouping: false}).format;
arabic(1234567890); // => ١٢٣٤٥٦٧٨٩٠

/**
 * 印地語(Hindi)是使用預設的ASCII 0-9數字
 * 修改地區設定，使用印度樣式的分組(Indian-style grouping)和天城文數字進行格式化
 * -u- 進行Unicode延伸。
 * nu為數字系統延伸。
 * deva為天城文(Devanagari)簡稱。
 */
let hindi = Intl.NumberFormat("hi-IN-u-nu-deva").format;
hindi(1234567890); // => १,२३,४५,६७,८९०
```

## Intl.DateTimeFormat

```JavaScript
/**
 * year: 四位數年 "numeric"，二位數縮寫 "2-digit"
 * month: 數字 "numeric"，呈現二位數 "2-digit"，完整名稱 "long"，縮寫名稱 "short"，不保證唯一的高度縮寫 "narrow"
 * day: 數字 "numeric"，呈現二位數 "2-digit"
 * weekday: 完整名稱 "long"，縮寫名稱 "short"，不保證唯一的高度縮寫 "narrow"
 * era: 日期是否以一個年代(era)格式化，例如 CE BCE。完整名稱 "long"，縮寫名稱 "short"，不保證唯一的高度縮寫 "narrow"
 * hour, minute, second: 顯示時間，數字 "numeric"，呈現二位數 "2-digit"
 * timeZone: 日期的時區，使用UTC IANA(可能認得)，省略則使用當地時間。
 * timeZoneName: 格式化後的日期或時間中，時區該如何顯示。完整名稱 "long"，縮寫或數值的名稱 "short"
 * hour12: 是否使用12小時制。預設值為地區設定。
 * hourCycle: 指定子夜(midnight)。預設值為地區設定。hour12優先序高於此特性。指定為12 "h12", 指定為24 "h24", 指定為0 子夜前的小時為11pm "h11", 指定為0 子夜前的小時為23 "h23"
 */

// 基本日期格式
let d = new Date("2020-01-02T13:14:15Z");
Intl.DateTimeFormat("en-US").format(d); // => 1/2/2020
Intl.DateTimeFormat("fr-FR").format(d); // => 02/01/2020

// 顯示日期與月份
let opts = {weekday: "long", month: "long", year: "numeric", day: "numeric"};
Intl.DateTimeFormat("en-US", opts).format(d); // => Thursday, January 2, 2020
Intl.DateTimeFormat("fr-FR", opts).format(d); // => jeudi 2 janvier 2020

// 紐約(New York)的時間，顯示給說法語的加拿大人
opts = {hour: "numeric", minute: "2-digit", timeZone: "America/New_York"};
Intl.DateTimeFormat("fr-CA", opts).format(d); // => 8 h 14

// 在地區設定 添加 -u-ca-歷法名稱 使用不同歷法
opts = {year: "numeric", era: "short"};
Intl.DateTimeFormat("en", opts).format(d); // => 2020 AD
Intl.DateTimeFormat("en-u-ca-iso8601", opts).format(d); // => 2020 AD
Intl.DateTimeFormat("en-u-ca-hebrew", opts).format(d); // => 5780 AM
Intl.DateTimeFormat("en-u-ca-buddhist", opts).format(d); // => 2563 BE
Intl.DateTimeFormat("en-u-ca-islamic", opts).format(d); // => 1441 AH
Intl.DateTimeFormat("en-u-ca-persian", opts).format(d); // => 1398 AP
Intl.DateTimeFormat("en-u-ca-indian", opts).format(d); // => 1941 Saka
Intl.DateTimeFormat("en-u-ca-chinese", opts).format(d); // => 2019(ji-hai)
Intl.DateTimeFormat("en-u-ca-japanese", opts).format(d); // => 2 Reiwa

// Intl.DataTimeFormat
function ISOtoLongDate(isoString, locale = 'en-US') {
  const options = { month: 'long', day: 'numeric', year: 'numeric' };
  const date = new Date(isoString);
  const longDate = new Intl.DateTimeFormat(locale, options).format(date);
  return longDate;
}
```

## Intl.DisplsyName

```JavaScript
/** 根據 Region Code 取得該語言下顯示的「地區名稱」 */
const regionNames = new Intl.DisplayNames(['zh-Hant'], {
  type: 'region',
});
regionNames.of('TW'); // 台灣
regionNames.of('US'); // 美國

/** 根據 language-script-region 取得該語言下顯示的「語言名稱」 */
const languageNames = new Intl.DisplayNames(['zh-Hant'], {
  type: 'language',
});
languageNames.of('zh-Hant'); // 繁體中文
languageNames.of('zh-TW'); // 中文（台灣）

/** 根據 script code 取得該語言下顯示的「script 名稱」 */
const scriptNames = new Intl.DisplayNames(['zh-Hant'], {
  type: 'script',
});
scriptNames.of('Hant'); // 繁體
scriptNames.of('Hans'); // 簡體
scriptNames.of('Latn'); // 拉丁文

/** 根據 currency 取得該語言下顯示的「幣別名稱」 */
const currencyNames = new Intl.DisplayNames(['zh-Hant'], {
  type: 'currency',
});
currencyNames.of('TWD'); // 新台幣
currencyNames.of('KRW'); // 韓元
currencyNames.of('JPY'); // 日圓

/** 根據 date time field 取得該語言下顯示的「幣別名稱」 */
const dateTimeFieldNames = new Intl.DisplayNames('zh-Hant', {
  type: 'dateTimeField',
});
dateTimeFieldNames.of('year'); // 年
dateTimeFieldNames.of('weekOfYear'); // 週
```