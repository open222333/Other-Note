# Python 模組 OpenCC(簡轉繁)

```
```

## 目錄

- [Python 模組 OpenCC(簡轉繁)](#python-模組-opencc簡轉繁)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[OpenCC pypi](https://pypi.org/project/OpenCC/)

# 指令

```
hk2s: 繁體中文 (香港) -> 簡體中文
s2hk: 簡體中文 -> 繁體中文 (香港)
s2t: 簡體中文 -> 繁體中文
s2tw: 簡體中文 -> 繁體中文 (台灣)
s2twp: 簡體中文 -> 繁體中文 (台灣, 包含慣用詞轉換)
t2hk: 繁體中文 -> 繁體中文 (香港)
t2s: 繁體中文 -> 簡體中文
t2tw: 繁體中文 -> 繁體中文 (台灣)
tw2s: 繁體中文 (台灣) -> 簡體中文
tw2sp: 繁體中文 (台灣) -> 簡體中文 (包含慣用詞轉換 )
```

```bash
# 安裝
pip install OpenCC
```

# 用法

```Python
import opencc
converter = opencc.OpenCC('s2t.json')
converter.convert('汉字')  # 漢字

# 預設配置文件
# s2t.json Simplified Chinese to Traditional Chinese 簡體到繁體
# t2s.json Traditional Chinese to Simplified Chinese 繁體到簡體
# s2tw.json Simplified Chinese to Traditional Chinese (Taiwan Standard) 簡體到臺灣正體
# tw2s.json Traditional Chinese (Taiwan Standard) to Simplified Chinese 臺灣正體到簡體
# s2hk.json Simplified Chinese to Traditional Chinese (Hong Kong variant) 簡體到香港繁體
# hk2s.json Traditional Chinese (Hong Kong variant) to Simplified Chinese 香港繁體到簡體
# s2twp.json Simplified Chinese to Traditional Chinese (Taiwan Standard) with Taiwanese idiom 簡體到繁體（臺灣正體標準）並轉換爲臺灣常用詞彙
# tw2sp.json Traditional Chinese (Taiwan Standard) to Simplified Chinese with Mainland Chinese idiom 繁體（臺灣正體標準）到簡體並轉換爲中國大陸常用詞彙
# t2tw.json Traditional Chinese (OpenCC Standard) to Taiwan Standard 繁體（OpenCC 標準）到臺灣正體
# hk2t.json Traditional Chinese (Hong Kong variant) to Traditional Chinese 香港繁體到繁體（OpenCC 標準）
# t2hk.json Traditional Chinese (OpenCC Standard) to Hong Kong variant 繁體（OpenCC 標準）到香港繁體
# t2jp.json Traditional Chinese Characters (Kyūjitai) to New Japanese Kanji (Shinjitai) 繁體（OpenCC 標準，舊字體）到日文新字體
# jp2t.json New Japanese Kanji (Shinjitai) to Traditional Chinese Characters (Kyūjitai) 日文新字體到繁體（OpenCC 標準，舊字體）
# tw2t.json Traditional Chinese (Taiwan standard) to Traditional Chinese 臺灣正體到繁體（OpenCC 標準）
```
