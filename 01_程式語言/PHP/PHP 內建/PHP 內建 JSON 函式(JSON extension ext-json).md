# PHP 內建 JSON 函式(JSON extension ext-json)

```
從 PHP 5.2.0 開始：

json_encode()      // 將 array / object 轉為 JSON 字串
json_decode()      // 將 JSON 字串轉為 array / object
json_last_error()  // 取得最近一次 JSON 操作的錯誤代碼
```

| 常數                  | 說明         |
| ------------------- | ---------- |
| `JSON_ERROR_NONE`   | 沒有錯誤       |
| `JSON_ERROR_SYNTAX` | JSON 語法錯誤  |
| `JSON_ERROR_UTF8`   | UTF-8 編碼錯誤 |
| `JSON_ERROR_DEPTH`  | 巢狀層級過深     |

## 目錄

- [PHP 內建 JSON 函式(JSON extension ext-json)](#php-內建-json-函式json-extension-ext-json)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)
- [用法](#用法)
  - [json\_decode json\_encode](#json_decode-json_encode)

## 參考資料

[JSON 函式](https://www.php.net/manual/zh/book.JSON.php)

# 安裝

```bash
```

# 指令

```bash
```

# 用法

## json_decode json_encode

如果 $content 是合法 JSON，就把裡面所有字串中的舊網域換成新網域，再轉回 JSON

```PHP
$data = json_decode($content, true);

if (json_last_error() === JSON_ERROR_NONE) {
    array_walk_recursive($data, function (&$value) use ($old_host, $new_host) {
        if (is_string($value)) {
            $value = str_replace($old_host, $new_host, $value);
        }
    });

    $content = json_encode($data, JSON_UNESCAPED_SLASHES);
}
```
