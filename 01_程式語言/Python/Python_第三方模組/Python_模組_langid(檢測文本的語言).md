# Python 模組 langid(檢測文本的語言)

```
自然語言處理（Natural language processing）
NLP的語言檢測方式
```

## 目錄

- [Python 模組 langid(檢測文本的語言)](#python-模組-langid檢測文本的語言)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[langid pypi](https://pypi.org/project/langid/)

# 指令

```bash
# 安裝
pip install langid
```

# 用法

```Python
import langid

def detect_language(text):
    language, confidence = langid.classify(text)
    print(f'Text: {text}')
    print(f'Detected Language: {language} (Confidence: {confidence})')

if __name__ == '__main__':
    text_to_detect = "Hello, this is a simple example."
    detect_language(text_to_detect)
```
