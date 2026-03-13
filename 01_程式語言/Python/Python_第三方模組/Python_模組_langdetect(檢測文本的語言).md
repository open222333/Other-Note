# Python 模組 langdetect(檢測文本的語言)

```
自動檢測文本的語言，並返回檢測到的主要語言。

請注意，這個方法並不完美，有時可能會出現誤檢測，特別是對於複雜的多語言文本。

如果你需要更高效、更精確的語言檢測，並希望支援更多語言，則使用Google Cloud Natural Language API或其他相似的服務可能更適合。
```

## 目錄

- [Python 模組 langdetect(檢測文本的語言)](#python-模組-langdetect檢測文本的語言)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[langdetect pypi](https://pypi.org/project/langdetect/)

# 指令

```bash
# 安裝
pip install langdetect
```

# 用法

```Python
from langdetect import detect

def detect_language(text: str):
    """檢測文本語言
    ISO 639-1
    https://zh.wikipedia.org/zh-tw/ISO_639-1

    Args:
        text (str): _description_

    Returns:
        _type_: _description_
    """
    try:
        language = detect(text)
        print(f'文本: {text}')
        print(f'檢測語言: {language}')
        return (True, language)
    except Exception as err:
        msg = f'檢測文本語言 發生錯誤: {err}'
        print(msg)
        return (False, {'error': msg})


if __name__ == '__main__':
    text_to_detect = "Hello, this is a simple example."
    is_success, result = detect_language(text_to_detect)
```
