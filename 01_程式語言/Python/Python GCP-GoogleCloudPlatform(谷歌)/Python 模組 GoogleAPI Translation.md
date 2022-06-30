# Python 模組 GoogleAPI Translation

```
Python google翻譯API 官方(需要付費)
```

## 參考資料

[Translate strings (Basic edition)](https://cloud.google.com/translate/docs/samples/translate-text-with-model)

[google-cloud-translate 參考文檔](https://googleapis.dev/python/translation/latest/index.html)

[Cloud Translation API 代碼範例](https://cloud.google.com/translate/docs/samples)


[免費套件 (不穩定)](https://clay-atlas.com/blog/2020/05/05/python-cn-note-package-googletrans-google-translate/)

# 基本指令

```bash
# 免費套件 (不穩定)
pip3 install googletrans

# 安裝套件
pip install google-cloud-storage
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