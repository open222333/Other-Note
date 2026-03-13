# Python 模組-內建 setuptools(Python 專案的工具集)

```
setuptools 是一個用於 Python 專案的工具集，主要用於專案的構建、打包、安裝和分發。如果你想深入了解 setuptools 的用法和功能，以下是一本推薦的書籍：

書名：《Python Projects》

作者： Laura Cassell, Alan Gauld

出版日期： 2014 年

出版商： Apress

ISBN-13： 978-1430258554

這本書提供了關於 Python 項目結構、setuptools 的使用、打包和分發 Python 專案的詳細信息。它還涵蓋了一些與 Python 相關的其他主題，如測試、文檔撰寫等。這是一本適合希望深入瞭解 Python 專案管理的開發者的書籍。
```

## 目錄

- [Python 模組-內建 setuptools(Python 專案的工具集)](#python-模組-內建-setuptoolspython-專案的工具集)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [將專案上傳到 PyPI（Python Package Index）時，提供更詳細的說明資訊](#將專案上傳到-pypipython-package-index時提供更詳細的說明資訊)

## 參考資料

[setuptools pypi](https://pypi.org/project/setuptools/)

[setuptools 官方文檔](https://setuptools.pypa.io/en/latest/)

# 用法

```Python
from setuptools import setup, find_packages

# 定義專案的元信息
setup(
    name='example_project',           # 專案名稱
    version='1.0.0',                  # 專案版本
    description='An example project', # 專案描述
    author='Your Name',               # 作者名稱
    author_email='your@email.com',    # 作者電子郵件
    url='https://github.com/example', # 專案 URL

    # 定義專案的依賴項
    install_requires=[
        'dependency1',
        'dependency2',
    ],

    # 包含專案的所有包
    packages=find_packages(),

    # 指定包含的數據文件
    package_data={
        'example_project': [
            'data/*.txt',
            'templates/*.html',
        ],
    },

    # 指定命令行腳本
    entry_points={
        'console_scripts': [
            'example_command = example_project:main_function',
        ],
    },

    # 專案的分類
    classifiers=[
        'Environment :: Console',
        'Intended Audience :: Developers',
        'License :: OSI Approved :: MIT License',
        'Programming Language :: Python',
        'Programming Language :: Python :: 3',
    ],
)
```

`構建專案： setuptools 可以協助你定義專案的結構和元信息，包括項目名稱、版本、作者、依賴項等。`

這些信息通常存放在 setup.py 文件中。

```Python
from setuptools import setup, find_packages

setup(
    name='your_project',
    version='1.0.0',
    packages=find_packages(),
    install_requires=[
        'package1',
        'package2',
    ],
)
```

`打包專案： 使用 setuptools 可以將你的 Python 專案打包為可供分發的不同格式，例如 source distribution（源碼分發包）、wheel distribution（輪子分發包）等。`

```bash
python setup.py sdist bdist_wheel
```

`安裝專案： 通過 setuptools，你可以使用 pip 安裝你的 Python 專案。`

```
pip install .
```

`指定 entry points： 你可以使用 entry_points 定義命令行腳本，這在安裝專案時會被添加到 PATH 中。`

這使得你的專案可以像命令行工具一樣被執行。

```Python
setup(
    # ...
    entry_points={
        'console_scripts': [
            'your_command = your_module:main_function',
        ],
    },
)
```

`管理依賴項： install_requires 允許你指定專案的依賴項，這些依賴項將在安裝專案時被自動安裝。`

```Python
setup(
    # ...
    install_requires=[
        'package1',
        'package2',
    ],
)
```

`設定包含的數據文件： 通過 package_data 可以指定哪些額外的數據文件將被包含在分發包中。`

```Python
setup(
    # ...
    package_data={
        'your_package': [
            'data/*.txt',
            'templates/*.html',
        ],
    },
)
```

`定義插件： 使用 setuptools，你可以使用 entry points 定義插件機制，使得你的專案可以被擴展或擴展其他專案。`

```Python
setup(
    # ...
    entry_points={
        'your_project.plugins': [
            'plugin_name = your_module:PluginClass',
        ],
    },
)
```

## 將專案上傳到 PyPI（Python Package Index）時，提供更詳細的說明資訊

```Python
import os

# 獲取當前腳本檔案所在的目錄
cur_dir = os.path.dirname(__file__)
# 構建 README.md 檔案的完整路徑
readme = os.path.join(cur_dir, 'README.md')

# 檢查 README.md 是否存在
if os.path.exists(readme):
    # 如果存在，使用 with open 來安全地讀取檔案內容
    with open(readme) as fh:
        # 讀取 README.md 內容並指定給 long_description 變數
        long_description = fh.read()
else:
    # 如果 README.md 不存在，將 long_description 設置為空字符串
    long_description = ''
```