# Python 腳本規範

```
撰寫 Python 腳本（自動化、排程、資料處理）的統一約束：
結構、命名、環境、錯誤處理、檢查工具。新腳本必守，舊腳本修改時向此靠攏。
```

## 目錄

- [Python 腳本規範](#python-腳本規範)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [基本結構](#基本結構)
- [命名](#命名)
- [環境與相依](#環境與相依)
- [參數與設定](#參數與設定)
- [錯誤處理與日誌](#錯誤處理與日誌)
- [檢查工具](#檢查工具)
- [安全](#安全)

## 參考資料

[PEP 8 官方風格指南](https://peps.python.org/pep-0008/)

[Python 官方文檔](https://docs.python.org/3/)

# 基本結構

- 入口一律放在 `main()`，用 `if __name__ == "__main__":` 呼叫，禁止裸寫頂層邏輯。
- 檔頭 docstring 依序寫：用途（一行）、用法、參數說明、前置需求。

```python
#!/usr/bin/env python3
"""備份資料庫並上傳。

用法：python backup.py <tag> [--cleanup]
前置：pip install -r requirements.txt
"""

def main() -> int:
    ...
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
```

# 命名

- 模組 / 函式 / 變數：`snake_case`；類別：`PascalCase`；常數：`UPPER_SNAKE_CASE`。
- 腳本檔名用動詞開頭描述動作：`export_orders.py`、`sync_images.py`。

# 環境與相依

- 一律在虛擬環境執行（`venv` 或 `poetry`），禁止污染系統 Python。
- 相依固定寫入 `requirements.txt`（或 `pyproject.toml`）並鎖版本。
- 指定最低 Python 版本，於 README 或 docstring 註明。

```bash
python3 -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
```

# 參數與設定

- 命令列參數用 `argparse`，禁止手刻 `sys.argv` 解析（單一位置參數除外）。
- 可能因環境而改的值（主機、路徑、帳號）集中在檔案最上方常數區或 `.env`，不散落在邏輯中。
- 密碼、token 一律 `os.environ[...]` 讀取，不寫死在程式碼。

# 錯誤處理與日誌

- 用 `logging` 取代 `print` 輸出狀態；等級：進度 `info`、可續行問題 `warning`、終止 `error`。
- 只捕捉預期得到的例外，禁止裸 `except:`；捕捉後要嘛處理、要嘛 `raise`。
- 失敗要以非 0 退出（`raise SystemExit(1)`），讓外層（cron、CI）能判斷。
- 長時間迴圈定期輸出進度（筆數 / 百分比），可用 `tqdm`。

```python
import logging
logging.basicConfig(level=logging.INFO, format="[%(asctime)s] %(levelname)s %(message)s")
```

# 檢查工具

- 提交前必跑格式化與靜態檢查：

```bash
# 格式化（擇一，專案內統一）
autopep8 --in-place --aggressive *.py
# 或
black .

# 靜態檢查
flake8 .
# 型別註記多的專案再加
mypy .
```

- 公開函式加型別註記（type hints）。

# 安全

- 外部輸入（參數、檔案、API 回應）先驗證再使用。
- 組 SQL 用參數化查詢，禁止字串拼接。
- 呼叫外部指令用 `subprocess.run([...], check=True)` 傳 list，禁止 `shell=True` 拼字串。
