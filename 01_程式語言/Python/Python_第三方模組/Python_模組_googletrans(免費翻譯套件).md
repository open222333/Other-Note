# Python 模組 googletrans(免費翻譯套件)

```
```

## 目錄

- [Python 模組 googletrans(免費翻譯套件)](#python-模組-googletrans免費翻譯套件)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [用法](#用法)

## 參考資料

[googletrans pypi](https://pypi.org/project/googletrans/)

[[Python] 使用 googletrans 套件來進行 Google 翻譯 - 免費套件 (不穩定)](https://clay-atlas.com/blog/2020/05/05/python-cn-note-package-googletrans-google-translate/)

[03 奇妙的Python庫之【googletrans(翻譯)】](https://ppfocus.com/0/ed8a8aa02.html)

# 安裝

```bash
pip3 install googletrans
```

# 用法

```Python
import googletrans
from pprint import pprint


# Initial
translator = googletrans.Translator()

# Basic Translate
results = translator.translate('我覺得今天天氣不好。')
print(results)
print(results.text)
```