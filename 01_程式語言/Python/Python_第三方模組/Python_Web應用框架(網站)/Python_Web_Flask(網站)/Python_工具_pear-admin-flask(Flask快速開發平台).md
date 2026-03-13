# Python 工具 pear-admin-flask(Flask快速開發平台)

```
Pear Admin Flask 是一個基於 Flask 的開源管理系統，它提供了一個現代化的後台管理界面。
要在 Docker 中運行 Pear Admin Flask，可以按照以下步驟來構建和運行這個 Docker 映像。
```

## 目錄

- [Python 工具 pear-admin-flask(Flask快速開發平台)](#python-工具-pear-admin-flaskflask快速開發平台)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [安裝](#安裝)
  - [Dockerfile](#dockerfile)
- [用法](#用法)

## 參考資料

[pearadmin/pear-admin-flask Pear Admin Flask - Github](https://github.com/pearadmin/pear-admin-flask)

# 指令

```bash
# 安裝
pip install sample
```

# 安裝

## Dockerfile

```bash
# 使用官方的 Python 映像作為基礎映像
FROM python:3.9-slim

# 設置工作目錄
WORKDIR /app

# 複製當前目錄內容到工作目錄
COPY . .

# 安裝依賴包
RUN pip install --no-cache-dir -r requirements.txt

# 暴露應用程序運行的端口
EXPOSE 5000

# 設置啟動命令
CMD ["python", "app.py"]
```

# 用法

```Python
```
