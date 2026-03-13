# Python 概念 PyPI專案

```
```

## 目錄

- [Python 概念 PyPI專案](#python-概念-pypi專案)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[sample 官方文檔](https://docs.python.org/zh-tw/3/library/sample.html)

# 用法

```arduino
my_project/
│
├── my_project/
│   ├── __init__.py
│   ├── my_module.py
│   └── ...
│
├── tests/
│   ├── test_my_module.py
│   └── ...
│
├── README.md
├── LICENSE
├── setup.py
└── ...
```

my_project/：主專案目錄。

my_project/：Python 套件源代碼目錄。

__init__.py：空文件，用於指示目錄是一個 Python 包。

my_module.py：示例模組文件，包含一些功能性代碼。

tests/：測試目錄，包含測試文件。

test_my_module.py：示例測試文件。

README.md：專案文檔，描述專案的用途、安裝方法等。

LICENSE：許可證文件，描述專案的授權許可。

setup.py：專案的安裝配置文件，用於將專案打包並發佈到 PyPI。

安裝配置文件 `setup.py`

```Python
from setuptools import setup, find_packages

setup(
    name='my_project',
    version='1.0.0',
    packages=find_packages(),
    author='Your Name',
    author_email='your@email.com',
    description='Description of your project',
    long_description=open('README.md').read(),
    long_description_content_type='text/markdown',
    url='https://github.com/your_username/my_project',
    license='MIT',
    classifiers=[
        'Programming Language :: Python :: 3',
        'License :: OSI Approved :: MIT License',
        'Operating System :: OS Independent',
    ],
    python_requires='>=3.6',
)
```

測試文件 `test_my_module.py`

```Python
import unittest
from my_project.my_module import my_function

class TestMyModule(unittest.TestCase):
    def test_my_function(self):
        self.assertEqual(my_function(2, 3), 5)

if __name__ == '__main__':
    unittest.main()
```

README.md 文件：

在 README.md 文件中寫入專案介紹、用法說明、安裝指南等。

LICENSE 文件：

選擇適合專案的許可證，例如 MIT、GPL 等，並在 LICENSE 文件中包含許可證文本。
