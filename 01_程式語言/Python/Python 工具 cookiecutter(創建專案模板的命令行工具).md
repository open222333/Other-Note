# Python 工具 cookiecutter(創建專案模板的命令行工具)

```
Cookiecutter 是一個用於創建專案模板的命令行工具，可以根據預定義的模板生成新的專案目錄結構。
```

## 目錄

- [Python 工具 cookiecutter(創建專案模板的命令行工具)](#python-工具-cookiecutter創建專案模板的命令行工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [創建基本的 Python 專案結構](#創建基本的-python-專案結構)

## 參考資料

[cookiecutter pypi](https://pypi.org/project/cookiecutter/)

[cookiecutter 官方網站](https://www.cookiecutter.io/)

[cookiecutter Github](https://github.com/cookiecutter/cookiecutter)

# 指令

```bash
# 安裝
pip install cookiecutter
```

# 用法

## 創建基本的 Python 專案結構

```bash
cookiecutter gh:cookiecutter/cookiecutter-pypackage
```

將提示輸入一些項目資訊，例如專案名稱、作者名稱等。

完成後，Cookiecutter 將生成一個新的專案目錄結構，其中包含了基本的 Python 專案文件和配置。

可以根據需要進一步修改和定製這個專案。

```
setup.py：用於定義專案的元資訊和安裝配置。
README.md：專案的說明文件。
LICENSE：專案的授權文件。
requirements.txt：列出了專案所需的所有依賴。
測試配置文件，例如 pytest.ini 或 tox.ini。
項目結構，包含了主要的 Python 代碼目錄結構。
```
