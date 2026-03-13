# Python 模組 google-cloud-translate(付費 google翻譯API 官方)

```
Python google翻譯API 官方(需要付費)
```

## 目錄

- [Python 模組 google-cloud-translate(付費 google翻譯API 官方)](#python-模組-google-cloud-translate付費-google翻譯api-官方)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [基本指令](#基本指令)
- [用法](#用法)

## 參考資料

[Translate API - google官方文檔](https://cloud.google.com/translate/docs/reference/api-overview)

[All Cloud Translation code samples - 官方雲端翻譯程式碼範例](https://cloud.google.com/translate/docs/samples?language=python)

[Translate strings (Basic edition) - 官方雲端翻譯程式碼範例](https://cloud.google.com/translate/docs/samples/translate-text-with-model)

[google-cloud-translate 參考文檔](https://googleapis.dev/python/translation/latest/index.html)

[Python client library](https://cloud.google.com/translate/docs/reference/libraries/v2/python)

# 基本指令

```bash
# 安裝套件
pip install google-cloud-storage

# 安裝套件 translatev2
pip install google-cloud-translate==2.0.1
```

# 用法

```Python
from google.cloud import translate_v2

from google.cloud import translate


def detect_language(
    project_id: str = "YOUR_PROJECT_ID",
) -> translate.DetectLanguageResponse:
    """Detecting the language of a text string.

    Args:
        project_id: The GCP project ID.

    Returns:
        The detected language of the text.
    """
    client = translate.TranslationServiceClient()

    location = "global"

    parent = f"projects/{project_id}/locations/{location}"

    # Detail on supported types can be found here:
    # https://cloud.google.com/translate/docs/supported-formats
    response = client.detect_language(
        content="Hello, world!",
        parent=parent,
        mime_type="text/plain",  # mime types: text/plain, text/html
    )

    # Display list of detected languages sorted by detection confidence.
    # The most probable language is first.
    for language in response.languages:
        # The language detected
        print(f"Language code: {language.language_code}")
        # Confidence of detection result for this language
        print(f"Confidence: {language.confidence}")

    return response
```

```Python
from google.cloud import translate_v2 as translate

def translate_text(text, target_language='en'):
    client = translate.Client()

    result = client.translate(text, target_language=target_language)

    print(f'Original Text: {text}')
    print(f'Translation: {result["input"]}')
    print(f'Translated Text: {result["translatedText"]}')

if __name__ == '__main__':
    text_to_translate = "你好，這是一個簡單的範例。"
    translate_text(text_to_translate)
```