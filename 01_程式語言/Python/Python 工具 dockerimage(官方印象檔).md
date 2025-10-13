# Python 工具 dockerimage(官方印象檔)

```
20251012
```

## 目錄

- [Python 工具 dockerimage(官方印象檔)](#python-工具-dockerimage官方印象檔)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [通用 Python Docker 基底鏡像](#通用-python-docker-基底鏡像)
- [範例](#範例)
  - [通用](#通用)
  - [精簡通用](#精簡通用)
  - [Alpine](#alpine)
  - [保持不關容器(進入容器輸入指令)](#保持不關容器進入容器輸入指令)

## 參考資料

[]()

# 通用 Python Docker 基底鏡像

| 類型       | Image 名稱                                        | 說明                         | 適合用途                                           |
| -------- | ----------------------------------------------- | -------------------------- | ---------------------------------------------- |
| 標準完整版  | `python:3.11`                                   | 含 pip、常用工具、Debian 作業系統     | 開發環境、一般用途                                      |
| 輕量版    | `python:3.11-slim`                              | 減少不必要套件（約 120MB）           | 生產環境（推薦）                                       |
| 超精簡版  | `python:3.11-alpine`                            | 基於 Alpine Linux，極小（約 60MB） | 容器數量多或追求極輕量的應用                                 |
| 完整工具包 | `python:3.11-bullseye` 或 `python:3.11-bookworm` | 包含更多系統工具與相容性最佳             | 編譯需要 C 擴展（如 `cryptography`, `numpy`, `pandas`） |

| 需求           | 推薦鏡像                                      |
| ------------ | ----------------------------------------- |
| 一般開發或 API 服務 | `python:3.11-slim` ✅                      |
| 機器學習 / 編譯套件  | `python:3.11-bullseye` ✅                  |
| 最小體積部署       | `python:3.11-alpine`                      |
| 想用最新版本       | `python:3.12-slim` 或 `python:latest-slim` |

# 範例

## 通用

```dockerfile
# 使用官方 Python 基底鏡像（輕量、穩定）
FROM python:3.11-slim

# 確保系統更新與安裝常用工具
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    curl \
    ca-certificates \
    git \
    && rm -rf /var/lib/apt/lists/*

# 設定工作目錄
WORKDIR /app

# 複製 requirements.txt 並安裝依賴
COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

# 複製專案程式碼
COPY . .

# 預設啟動指令（可依專案修改）
CMD ["python", "main.py"]
```

## 精簡通用

```dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt
CMD ["python", "main.py"]
```

## Alpine

很多 Python 套件需要編譯（如 cryptography, lxml），而 Alpine 預設沒有 GCC 與 OpenSSL dev。

需手動安裝

```dockerfile
FROM python:3.11-alpine

RUN apk add --no-cache gcc musl-dev libffi-dev openssl-dev

WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt
CMD ["python", "main.py"]
```

## 保持不關容器(進入容器輸入指令)

```dockerfile
# 使用官方 Python 3.11 映像檔
FROM python:3.11-slim

# 設定工作目錄
WORKDIR /app

# 複製 requirements.txt 並安裝依賴
COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# 複製整個專案檔案到容器內部
COPY . .

# 預設執行保持容器不結束
CMD ["tail", "-f", "/dev/null"]
```