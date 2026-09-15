# Commit 分類(內容分類建議)

```
Commit 訊息要解決的問題只有一個：三個月後有人 git log 找「這行為什麼變成這樣」時，
能不能靠訊息本身就篩出正確的那幾筆，而不用一筆一筆點開看 diff。

分類（type）就是那個篩選條件。它決定了 git log --grep 撈不撈得到、
自動產生的 changelog 分不分得出「使用者看得到的改動」與「內部整理」、
以及 code review 時看到標題就知道該用什麼標準審。

本篇採用 Conventional Commits 格式，並依 avnight 各專案（api2019、jjkk-flask）
的實際使用情況調整，另附「該選哪個 type」的判斷流程與常見情境對照。
```

## 目錄

- [Commit 分類(內容分類建議)](#commit-分類內容分類建議)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [格式](#格式)
- [分類 type](#分類-type)
- [範圍 scope](#範圍-scope)
- [主旨怎麼寫](#主旨怎麼寫)
- [判斷流程：該選哪個 type](#判斷流程該選哪個-type)
- [常見情境對照](#常見情境對照)
- [Body 與 Footer](#body-與-footer)
- [Breaking Change](#breaking-change)
- [與 dev\_note 分類的對應](#與-dev_note-分類的對應)
- [現況統計](#現況統計)
- [指令](#指令)

## 參考資料

[Conventional Commits 1.0.0](https://www.conventionalcommits.org/zh-hant/v1.0.0/)

[Angular Commit Message Format](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit)

[How to Write a Git Commit Message](https://cbea.ms/git-commit/)

[Semantic Versioning 2.0.0](https://semver.org/lang/zh-TW/)

---

# 格式

```
<type>(<scope>): <主旨>
<空行>
<body：為什麼要改，不是改了什麼>
<空行>
<footer：BREAKING CHANGE / 關聯票號>
```

```
type    必填，小寫，見下方分類表
scope   選填，改動的模組或功能領域，小寫，用底線
主旨    必填，繁體中文，祈使句，不加句號，50 字內
```

範例：

```
fix(video): 字幕 m3u8 網址在正式站給出 http，改用與影片同一套的主機判斷

request.host_url 的 scheme 取自 wsgi.url_scheme，但 ReverseProxied 只讀
X-Scheme，正式站 nginx 沒帶，所以一律回 http，前端是 https 會被當 mixed
content 擋掉。改成沿用 get_m3u8_source() 的規則：測試環境讀 M3U8_*，
正式環境硬組 https。

Refs: HTHD-201
```

---

# 分類 type

## 會影響使用者（會進 changelog）

| type | 中文 | 用在什麼情況 | 版號影響 |
|------|------|-------------|---------|
| `feat` | 新增功能 | 新 API、新參數、新平台差異行為。**原本沒有的東西** | MINOR |
| `fix` | 修正 bug | 既有行為壞掉或不符預期。使用者／後台回報的異常大多在這 | PATCH |
| `perf` | 效能改善 | 行為完全不變，但變快、變省資源 | PATCH |

## 不影響使用者（內部改動）

| type | 中文 | 用在什麼情況 |
|------|------|-------------|
| `refactor` | 重構 | 行為完全不變的結構調整。**行為有變就不是 refactor** |
| `docs` | 文件 | README、CLAUDE.md、apidoc 註解、程式碼註解 |
| `test` | 測試 | 只動測試檔。順手補的測試併進該筆 `feat` / `fix`，不另開 |
| `build` | 建置 | requirements.txt、Dockerfile、docker-compose、打包流程 |
| `ci` | CI/CD | pipeline、部署腳本、自動化設定 |
| `style` | 格式 | 縮排、引號、排版。**不含 CSS**，CSS 改動屬於 `feat` / `fix` |
| `chore` | 雜項 | 上面都不是的瑣事。**能歸到別的 type 就不要用 chore** |
| `revert` | 還原 | 還原先前的 commit，主旨寫 `revert: <原主旨>`，body 附原 SHA |

## 建議額外採用（本地慣例）

這三個標準 Conventional Commits 沒有，但在這邊很常見，分出來對日後追查很有幫助：

| type | 中文 | 用在什麼情況 | 為什麼值得獨立 |
|------|------|-------------|--------------|
| `data` | 資料修正／同步 | 程式沒問題，是資料髒掉、沒同步、欄位沒補。含一次性補資料腳本 | 常被誤標成 `fix`，但下次遇到同樣現象要查的是資料流程不是程式 |
| `security` | 安全性 | 權限、簽章、金鑰、越權、資料外洩 | 需要單獨被撈出來稽核，混在 `fix` 裡會被埋掉 |
| `hotfix` | 緊急修復 | 直接對 `master` 出手、跳過正常流程的修復 | 事後要回頭確認有沒有補回 develop／有沒有補測試 |

> 若團隊決定不擴充，`data` → `fix`、`security` → `fix`、`hotfix` → `fix`，
> 但**一定要在 body 第一行標明**，例如 `安全性修正：...`。

---

# 範圍 scope

```
scope 寫「改動落在哪個模組／功能領域」，不是寫檔名，也不是寫分支名。
一律小寫，多字用底線。改動橫跨多個模組時省略 scope，不要塞兩個。
```

api2019 實際在用的 scope（依使用次數）：

```
refund  chinese_dubbing  video  member  breast_coin  ai_chinese_dubbing
search  domain_monitor   tasks  ngs_avdata  api  celery  actor  models
main_screen  event  schedule  mysql_queries  endpoints
```

jjkk-flask 額外要注意的 scope：

```
s1 / sw2 兩個 Blueprint 是兩份程式碼。
只改一邊時 scope 要寫清楚（例：fix(sw2): ...）；
兩邊同步改則用功能名（例：fix(video): ...）並在 body 註明「s1 / sw2 皆已同步」。
```

---

# 主旨怎麼寫

```
✅ 祈使句，說明「這個 commit 做了什麼」
✅ 繁體中文
✅ 50 字內，不加句號
✅ 寫「改了什麼行為」，不是「改了哪個檔案」

❌ 修改 endpoints.py            ← 看不出行為
❌ 修正 bug                     ← 沒有資訊量
❌ 優化                         ← 優化什麼？
❌ 依 PM 要求修改               ← 三個月後沒人記得 PM 說了什麼
```

好壞對照：

| ❌ | ✅ |
|---|---|
| `fix: 修正搜尋問題` | `fix(search): iOS 搜尋結果仍會出現奶幣影片，補上第二層過濾` |
| `feat: 新增參數` | `feat(video): m3u8 API 新增 subtitle=1，回傳含加密字幕軌的 master.m3u8` |
| `refactor: 整理程式碼` | `refactor(member): 把 member_select() 改寫成 SQLAlchemy，行為不變` |
| `chore: 更新設定` | `build: docker-compose 拆出 worker 與 redis 服務` |

---

# 判斷流程：該選哪個 type

```
1. 這次改動之後，使用者（或後台使用者）看得到差別嗎？
   看不到 → 走「不影響使用者」那組：refactor / docs / test / build / ci / style / chore
   看得到 → 往下

2. 這個行為原本就該是這樣，只是壞了？
   是 → fix
   不是（原本就沒有這個東西）→ feat

3. 行為完全一樣，只是變快／變省？
   → perf

4. 程式其實沒動，是資料／設定的問題？
   資料 → data（或 fix + body 註明）
   環境、部署、相依 → build / ci
```

四條容易搞錯的分界：

```
feat vs fix
  判準是「原本該不該有這個行為」，不是「改動大不大」。
  補上原本規格就寫明、但漏掉沒做的東西 → fix。
  規格外新加的東西 → feat。

refactor vs fix
  refactor 的定義是「行為完全不變」。只要有任何一個 case 的輸出變了，
  就不是 refactor。重構過程中順手修好的 bug 要拆成獨立一筆 fix。

fix vs data
  程式碼有改 → fix。程式碼沒改、只是補資料或改設定 → data。
  「改了同步邏輯讓資料以後不會再髒掉」是 fix，
  「跑腳本把已經髒掉的資料補回來」是 data。

chore vs 其他
  chore 是最後的退路。動到相依套件是 build，動到部署是 ci，
  動到文件是 docs。真的都不是才用 chore。
```

---

# 常見情境對照

| 情境 | type | 範例主旨 |
|------|------|---------|
| 新 API 端點 | `feat` | `feat(video): 新增取得加密字幕 vtt 的端點` |
| 既有 API 加選填參數 | `feat` | `feat(search): 搜尋 API 新增 platform 參數以區分 iOS 行為` |
| 回傳值算錯 | `fix` | `fix(member): 看片次數在同一次觀看被重複扣點` |
| 規格有寫但漏做 | `fix` | `fix(search): iOS 關鍵字搜尋結果漏了奶幣過濾` |
| 加索引讓查詢變快 | `perf` | `perf(mysql_queries): member_log 加複合索引，日報表查詢由 8s 降到 0.3s` |
| 函式拆小、行為不變 | `refactor` | `refactor(video): 把字幕主機判斷抽成 get_subtitle_url_base()` |
| 只補 docstring | `docs` | `docs(video): 補上 get_m3u8_source() 的 TEST_MODE 分支說明` |
| 更新 CLAUDE.md | `docs` | `docs: 補上 dev_note 撰寫規範與分類定義` |
| 只加測試 | `test` | `test(video): 補上字幕軌與影片軌 netloc 一致性的斷言` |
| requirements 升版 | `build` | `build: redis-py 升到 3.5.3 並修正 setex 參數順序` |
| docker-compose 拆服務 | `build` | `build: docker-compose 拆出 worker 與 redis` |
| 部署腳本改動 | `ci` | `ci: 部署流程加上 apidoc 自動產生` |
| 統一引號、排版 | `style` | `style(api): 字串統一改為雙引號` |
| 跑腳本補歷史資料 | `data` | `data(video): 補跑 sync_avdata_video 為既有影片補上 video_subtitles` |
| 修越權漏洞 | `security` | `security(api): m3u8 端點的 k/t 簽章未驗證過期時間` |
| 還原上一筆 | `revert` | `revert: fix(member) 還原 watched_count 上限跳過查詢邏輯` |
| 合併分支 | — | 保留 git 預設的 `Merge branch 'xxx'`，不用套格式 |

---

# Body 與 Footer

```
Body 寫「為什麼」，不是「做了什麼」——做了什麼看 diff 就知道，
為什麼只有當下的你知道。

值得寫進 body 的：
  - 根因是什麼（尤其是不直覺的那種）
  - 為什麼選這個做法、否決了哪些做法
  - 有什麼已知的副作用或限制
  - 相容性考量（舊 client、舊資料）

不用寫進 body 的：
  - 改了哪幾個檔案
  - 逐行說明程式碼在做什麼
  - 「依需求修改」這種沒有資訊的句子
```

Footer 慣例：

```
Refs: HTHD-183                  # 關聯票號
Closes: HTHD-183                # 這筆 commit 直接關掉該票
BREAKING CHANGE: <說明>         # 破壞相容性
Co-authored-by: Name <email>    # 共同作者
```

---

# Breaking Change

```
只要舊的呼叫方式會壞掉，就是 breaking change，一定要標。
標法有兩種，擇一即可（建議兩個都用，最明顯）：

1. type 後面加驚嘆號：feat(api)!: 移除 v2 影片端點
2. footer 加 BREAKING CHANGE: <說明>

判準是「舊的 client 不改程式會不會壞」：
  移除欄位 / 改欄位型別 / 改端點路徑 / 改必填參數  → 是
  新增選填參數 / 新增回傳欄位                        → 不是
```

---

# 與 dev_note 分類的對應

```
docker/20260604-AV9_api/dev_note/CLAUDE.md 與
docker/20260724-JK_api/dev_note/CLAUDE.md 的「主分類」刻意沿用同一組代號，
所以寫 commit 跟寫 dev_note 用的是同一套詞彙，不用轉換。
```

| dev_note 主分類 | 對應 commit type | 備註 |
|----------------|-----------------|------|
| `fix` | `fix` | — |
| `feat` | `feat` | — |
| `perf` | `perf` | — |
| `data` | `data`（或 `fix` + body 註明） | — |
| `ops` | `build` / `ci` | 只動 env、compose、nginx 時通常沒有 commit |
| `refactor` | `refactor` | — |
| `security` | `security`（或 `fix` + body 註明） | — |
| `investigate` | 無 commit | 追查後排除，只留紀錄 |
| `guide` | `docs` | 純流程紀錄 |

```
一筆 dev_note 可能對應多筆 commit（主因一筆、次因一筆、還原一筆），
所以 dev_note 的「目前處理狀態」表格要列出每一筆的完整 40 碼 SHA。
反過來，一筆 commit 不一定要有 dev_note——追查超過半小時才需要。
```

---

# 現況統計

```
以下為 api2019 近 400 筆 commit 的實測分佈（2026-09 取樣），
用來對照「規範」與「實際」的落差，不是目標值。
```

| type | 筆數 | 佔比 |
|------|-----:|-----:|
| `fix` | 149 | 37% |
| `feat` | 133 | 33% |
| `refactor` | 24 | 6% |
| `chore` | 17 | 4% |
| `docs` | 16 | 4% |
| `perf` | 1 | 0.3% |
| 未套格式（含 Merge） | 60 | 15% |

```
可以改善的地方：

1. 未套格式的 60 筆裡，扣掉 Merge 之後多半是「新增sourcemap上傳newrelic功能」
   這類中文開頭的訊息——內容其實寫得清楚，只是少了 type 前綴。
2. perf 只有 1 筆，實務上不太可能。效能改善很可能被標成了 fix 或 refactor。
3. chore 17 筆中有一部分應該是 build（相依套件、compose）。
4. 幾乎沒有 body。根因與取捨都只存在於 dev_note 或當事人腦中，
   git log 本身查不到——這是目前最大的缺口。
```

---

# 指令

```bash
### 檢查與查詢 ###
# 看某個 type 的所有改動
git log --oneline --grep '^fix'

# 看某個模組的所有改動
git log --oneline --grep '^[a-z]*(video):'

# 只看會進 changelog 的（feat / fix / perf）
git log --oneline --grep '^\(feat\|fix\|perf\)'

# 統計 type 分佈
git log -400 --pretty=%s | grep -oE '^[a-z]+(\([^)]*\))?:' | sed 's/(.*//' | sort | uniq -c | sort -rn

# 找出沒套格式的 commit
git log -400 --pretty='%h %s' | grep -vE '^[0-9a-f]+ ([a-z]+(\([^)]*\))?:|Merge )'

# 查某一行是哪筆 commit 改的、為什麼
git log -L <起行>,<迄行>:<檔案>
git blame -L <起行>,<迄行> <檔案>
```

```bash
### commit template（讓每次 commit 都看到格式提示）###
cat > ~/.gitmessage <<'TPL'
# <type>(<scope>): <主旨，祈使句，50 字內，不加句號>
#
# type: feat fix perf refactor docs test build ci style chore revert data security
#
# --- body：為什麼要改（根因、取捨、副作用），不是改了什麼 ---
#
# --- footer ---
# Refs: HTHD-000
# BREAKING CHANGE: <說明>
TPL

git config --global commit.template ~/.gitmessage
```

```bash
### 修正已經打錯的訊息 ###
# 改最後一筆
git commit --amend

# 改前 N 筆（互動式，把要改的那行 pick 改成 reword）
git rebase -i HEAD~<N>

# 已經推上去的話，改完要強推（確認沒有別人在用這個分支）
git push --force-with-lease
```

```bash
### 自動檢查（選用，需 Node）###
npm install --save-dev @commitlint/cli @commitlint/config-conventional

cat > commitlint.config.js <<'CFG'
module.exports = {
    extends: ["@commitlint/config-conventional"],
    rules: {
        "type-enum": [2, "always", [
            "feat", "fix", "perf", "refactor", "docs", "test",
            "build", "ci", "style", "chore", "revert",
            "data", "security", "hotfix",
        ]],
        "subject-max-length": [2, "always", 50],
        "subject-full-stop": [2, "never", "。"],
    },
};
CFG

# 掛成 commit-msg hook
npx husky init
echo 'npx --no -- commitlint --edit "$1"' > .husky/commit-msg
```
