# Python 工具 copier(專案模板產生工具)

```
copier 是 Python 寫的專案模板產生工具：建立時依 copier.yml 提問並自動代入檔案；模板更新後可用 copier update 把改動同步到已建立的專案。
```

## 目錄

- [Python 工具 copier(專案模板產生工具)](#python-工具-copier專案模板產生工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [概念](#概念)
  - [與其他做法比較](#與其他做法比較)
  - [運作方式](#運作方式)
- [安裝](#安裝)
- [配置文檔](#配置文檔)
  - [copier.yml（問題定義）](#copieryml問題定義)
  - [模板檔案（.jinja）](#模板檔案jinja)
  - [.copier-answers.yml（回答紀錄）](#copier-answersyml回答紀錄)
- [指令](#指令)
  - [建立新專案](#建立新專案)
  - [同步模板更新](#同步模板更新)
  - [同時套用多個模板](#同時套用多個模板)
- [套用到自己的 Template 模板](#套用到自己的-template-模板)
- [例外狀況](#例外狀況)
  - [update 合併衝突](#update-合併衝突)

## 參考資料

[copier 官方文件](https://copier.readthedocs.io/)

[copier GitHub](https://github.com/copier-org/copier)

[Python 工具 cookiecutter 筆記](Python_工具_cookiecutter(創建專案模板的命令行工具).md)

[Python 工具 pip / pipx 筆記](Python_工具_pip_pipx(套件管理器).md)

[工作流程 - 專案模板(Template_Repo規劃)](../../00_概念/工作流程_-_專案模板(Template_Repo規劃).md)

# 概念

## 與其他做法比較

copier 和 GitHub 的「Use this template」都是從模板建立新專案，差別在兩點：

1. **建立時提問並自動代入**：例如專案名稱、port、要不要資料庫，回答完直接產生改好的專案，不用 clone 下來再一個個手動改
2. **模板更新可同步到已建立的專案**：這是最大特色

| 做法 | 建立時代入變數 | 模板更新後同步 |
|---|---|---|
| GitHub「Use this template」 | ❌ 手動改 | ❌ 複製一次就斷開 |
| cookiecutter | ✅ | ❌ 複製一次就斷開 |
| copier | ✅ | ✅ `copier update` |

選擇建議：

- 只建一、兩個專案 → GitHub template 就夠用
- 會從同一個模板建很多專案，且希望模板改進後各專案都跟得上 → 改用 copier

## 運作方式

- 模板 repo 根目錄放 `copier.yml`，定義要問的問題
- 需要代入變數的檔案，檔名加上 `.jinja` 結尾，內容用 `{{ 變數 }}`
- 建立後專案內多一個 `.copier-answers.yml`，記錄模板來源、版本與當時的回答
- `copier update` 依 `.copier-answers.yml` 比對模板改了什麼，再合併進專案（類似 git merge）
- 模板版本靠 **git tag** 判斷（例如 `v1.0.0`）

# 安裝

```bash
pipx install copier
```

# 配置文檔

## copier.yml（問題定義）

放在模板 repo 根目錄：

```yaml
# copier.yml
project_name:
  type: str
  help: 專案名稱
web_port:
  type: int
  default: 8080
guest_mode:
  type: bool
  default: true
  help: 是否開放訪客瀏覽
```

## 模板檔案（.jinja）

需要代入的檔案，檔名加 `.jinja`，產生時會去掉副檔名：

```bash
# .env.development.jinja → 產生 .env.development
VITE_APP_NAME={{ project_name }}
VITE_GUEST_MODE={{ guest_mode | lower }}
```

## .copier-answers.yml（回答紀錄）

- 由 copier 自動產生在新專案內，記錄使用的模板、版本與回答
- `copier update` 依此比對，**要加入版本控制，不要手動修改**

# 指令

## 建立新專案

會逐一詢問 `copier.yml` 裡的問題：

```bash
copier copy gh:open222333/Template-ReactDashboard finance-web
```

## 同步模板更新

模板有新版本（新 tag）後，在專案目錄執行，會把模板的改動合併進來，並保留自己的修改：

```bash
cd finance-web
copier update
```

## 同時套用多個模板

每個模板用不同的回答紀錄檔，即可套進同一個專案：

```bash
copier copy --answers-file .copier-answers.api.yml gh:open222333/Template-PythonFlaskAPI .
copier copy --answers-file .copier-answers.web.yml gh:open222333/Template-ReactDashboard frontend
```

# 套用到自己的 Template 模板

目前 `repo/` 內 11 個 `Template-*` 都還沒有 `copier.yml`，只能用 GitHub template 或手動 clone。改造步驟：

1. 每個模板根目錄加上 `copier.yml`
2. 把專案名稱、port 這類寫死的值換成 `{{ 變數 }}`，對應檔案改名為 `*.jinja`
3. 模板 repo 打上版本 tag（例如 `v1.0.0`），之後每次改進再打新 tag，`copier update` 才能偵測到新版本

```bash
git tag v1.0.0
git push origin v1.0.0
```

# 例外狀況

## update 合併衝突

- `copier update` 的合併方式類似 git merge：模板與專案改到同一處時會產生衝突，需手動處理後再 commit
- 建議在 git 工作區乾淨（變更都已 commit）時執行 `copier update`，方便檢視與還原合併結果
