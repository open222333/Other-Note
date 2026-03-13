# Git 筆記

```
```

## 目錄

- [Git 筆記](#git-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [檔案結構](#檔案結構)
    - [圖形化介面軟體相關](#圖形化介面軟體相關)
    - [git 原理相關](#git-原理相關)
    - [狀況相關](#狀況相關)
      - [管理 commit log](#管理-commit-log)
    - [Gitea(Git 私服)](#giteagit-私服)
    - [例外狀況相關](#例外狀況相關)
- [Git 基本概念](#git-基本概念)
  - [專案檔案結構](#專案檔案結構)
    - [基本資料夾結構](#基本資料夾結構)
    - [Git 特殊文件](#git-特殊文件)
    - [GitHub 特殊文件和資料夾](#github-特殊文件和資料夾)
- [安裝](#安裝)
  - [Debian(CentOS)](#debiancentos)
  - [Ubuntu](#ubuntu)
  - [Mac](#mac)
  - [SSH 綁定 創建](#ssh-綁定-創建)
  - [git-credential 個人令牌 token](#git-credential-個人令牌-token)
- [Git 指令](#git-指令)
  - [Config 環境設定](#config-環境設定)
  - [Remote – 遠端設定](#remote--遠端設定)
  - [Pull – 拉取下來 從遠端更新本地(有衝突會合併)](#pull--拉取下來-從遠端更新本地有衝突會合併)
  - [Push – 將 Commit 送出去](#push--將-commit-送出去)
  - [Add](#add)
  - [Git 刪除檔案(取消追蹤)](#git-刪除檔案取消追蹤)
  - [Commit 提交](#commit-提交)
  - [Branch 分支](#branch-分支)
  - [Git checkout 切換 branch](#git-checkout-切換-branch)
  - [Fetch 抓取](#fetch-抓取)
  - [Git track all remote branch 抓取所有遠端分支](#git-track-all-remote-branch-抓取所有遠端分支)
  - [diff比較](#diff比較)
  - [Tag](#tag)
  - [Bash 刪除所有Tag](#bash-刪除所有tag)
  - [log 查看log紀錄](#log-查看log紀錄)
  - [show](#show)
  - [reset 還原 退回版本 (捨棄 commit)](#reset-還原-退回版本-捨棄-commit)
  - [update-ref 刪除HEAD(適用取消首次commit)](#update-ref-刪除head適用取消首次commit)
  - [grep 查詢](#grep-查詢)
  - [stash 暫存](#stash-暫存)
  - [merge 合併](#merge-合併)
  - [Git Rebase 重改並合併歷史Commit](#git-rebase-重改並合併歷史commit)
  - [Git blame](#git-blame)
  - [Git 還原已被刪除的檔案](#git-還原已被刪除的檔案)
  - [Git 維護](#git-維護)
  - [Git revert 資料還原](#git-revert-資料還原)
  - [Git Conflict Strategy 處理已經衝突的檔案](#git-conflict-strategy-處理已經衝突的檔案)
  - [Git Blame 逐條程式碼Log](#git-blame-逐條程式碼log)
  - [設定永遠快取密碼，則可執行以下指令進行設定](#設定永遠快取密碼則可執行以下指令進行設定)
  - [設置git忽略](#設置git忽略)
- [範例](#範例)
  - [更換子模組路徑](#更換子模組路徑)
    - [手動清除指令](#手動清除指令)
  - [不想合併，但又需要解決這些未追蹤檔案的問題](#不想合併但又需要解決這些未追蹤檔案的問題)
  - [Python 更改資料夾內所有專案設定](#python-更改資料夾內所有專案設定)
  - [Git pull 錯誤操作](#git-pull-錯誤操作)
  - [Git submodule 子模組](#git-submodule-子模組)
  - [Git subtree 子樹](#git-subtree-子樹)
- [例外狀況](#例外狀況)
  - [fatal: unable to access : Failed to connect to bitbucket.org port 443: Connection timed out](#fatal-unable-to-access--failed-to-connect-to-bitbucketorg-port-443-connection-timed-out)
  - [TCP connection reset by peer](#tcp-connection-reset-by-peer)

## 參考資料

[官方網站](https://git-scm.com/)

[為你自己學 Git](https://gitbook.tw/)

[Git 安裝教學](https://git-scm.com/book/zh-tw/v2/%E9%96%8B%E5%A7%8B-Git-%E5%AE%89%E8%A3%9D%E6%95%99%E5%AD%B8)

[Git 文檔](https://git-scm.com/book/zh-tw/v2/%E9%96%8B%E5%A7%8B-%E9%97%9C%E6%96%BC%E7%89%88%E6%9C%AC%E6%8E%A7%E5%88%B6)

[git 免費電子書](https://git-scm.com/book/zh-tw/v2)

[git 免費電子書](https://kknews.cc/zh-tw/code/xlx33jo.html)

[Git上的三種工作流程](https://medium.com/i-think-so-i-live/git%E4%B8%8A%E7%9A%84%E4%B8%89%E7%A8%AE%E5%B7%A5%E4%BD%9C%E6%B5%81%E7%A8%8B-10f4f915167e)

[gitignore範本](https://github.com/github/gitignore)

### 檔案結構

[GitHub Repository Structure Best Practices - GitHub 存儲庫結構最佳實踐](https://medium.com/code-factory-berlin/github-repository-structure-best-practices-248e6effc405)

### 圖形化介面軟體相關

[Fork](https://git-fork.com/)

[Sourcetree](https://www.sourcetreeapp.com/)

### git 原理相關

[Git 內部原理 - Packfiles](http://iissnan.com/progit/html/zh-tw/ch9_4.html)

### 狀況相關

[【狀況題】手邊的工作做到一半，臨時要切換到別的任務](https://gitbook.tw/chapters/faq/stash)

#### 管理 commit log

[使用 git commit template 管理 git log](https://medium.com/dev-chill/%E4%BD%BF%E7%94%A8-git-commit-template-%E7%AE%A1%E7%90%86-git-log-cb70f95fda2f)

[如何使用 git commit template 與 git hooks 管理團隊的 git log](https://allen-hsu.github.io/2017/07/02/git-message-template-and-githook/)

```
管理 commit log
目的：統一團隊之中的 commit message 格式。
```

### Gitea(Git 私服)

```
建立一個容易安裝，執行快速，安装和使用體驗良好的自建 Git 服務。
採用 GO 為後端語言，Go 可以產生各平台使用的執行檔。
它支援 Linux、macOS 和 Windows 外，處理器架構包含 amd64、i386、ARM 和 PowerPC 等。
```

[Gitea 官方網站](https://gitea.io/zh-tw/)

[關於 Gitea](https://docs.gitea.io/zh-tw/)

### 例外狀況相關

[fatal: unable to access : Failed to connect to bitbucket.org port 443: Connection timed out](https://stackoverflow.com/questions/52050241/fatal-unable-to-access-failed-to-connect-to-bitbucket-org-port-443-connectio)

[TCP connection reset by peer](https://blog.csdn.net/bin9wei/article/details/121299033)

# Git 基本概念

```
倉庫 repository(簡稱repo)：
	一個目錄，此目錄的所有檔案都通過Git實現版本管理，Git能追蹤並記錄該目錄中發生的所有更新。

Directory：
	使用Git管理的一個目錄，也就是一個倉庫，包含我們的工作空間和Git的管理空間。

WorkSpace：
	需要通過Git進行版本控制的目錄和檔案，這些目錄和檔案組成了工作空間。

.git：
	存放Git管理資訊的目錄，初始化倉庫的時候自動建立。

Index/Stage：
	暫存區，或者叫待提交更新區，在提交進入repo之前，我們可以把所有的更新放在暫存區。

Local Repo：
	本地倉庫，一個存放在本地的版本庫HEAD會只是當前的開發分支（branch）。

Stash：
	是一個工作狀態儲存棧，用於儲存/恢復WorkSpace中的臨時狀態。
```

## 專案檔案結構

### 基本資料夾結構

```
src：源代碼資料夾！但是，在使用標頭的語言中（或者如果有適用於應用程序的框架）不要將這些文件放在這裡。
test：單元測試、集成測試……去這裡。
.config：它應該與本地機器上的設置相關的本地配置。
.build：此資料夾應包含與構建過程相關的所有腳本（PowerShell、Docker compose……）。
dep：這是應該存儲所有依賴項的目錄。
doc：文檔資料夾
res：用於項目中的所有靜態資源。例如，圖像。
samples：提供“Hello World”和支持文檔的 Co 代碼。
tools：方便你使用的目錄。應包含腳本以自動執行項目中的任務，例如，構建腳本、重命名腳本。例如通常包含 .sh、.cmd。
```

### Git 特殊文件

```
.gitignore：供 git 忽略的 blob 列表。影響 git add 和 git clean 等命令。你可以使用 gitignore.io 來生成一個乾淨有用的 gitignore。
.gitattributes：讓定義文件的屬性（例如，更改文件在 diff 中的外觀）。
.mailmap：讓你告訴 git 歷史中重複的名字或電子郵件實際上是同一個人。
.gitmodules：讓定義子模塊（ git 存儲庫的子目錄，這些子目錄是其他 git 存儲庫的簽出）。
```

### GitHub 特殊文件和資料夾

```
README：README或README.txt或README.md等是一個回答項目的內容、原因和方式的文件。GitHub 將識別並自動顯示README存儲庫訪問者。這是一個很棒的列表，包含更專業的自述文件。

LICENSE：LICENSEor LICENSE.txtor LICENSE.mdetc. 是解釋合法許可的文件，比如任何權利，任何限制，任何規定等。GitHub開發了一個工具來幫助你選擇合適的許可：https://choosealicense.com/

CHANGELOG：CHANGELOG或CHANGELOG.txt或CHANGELOG.md等是描述回購中發生的事情的文件。版本號增加、軟件更新、錯誤修復……是文件內容的示例。

CONTRIBUTORS：CONTRIBUTORS或CONTRIBUTORS.txt或CONTRIBUTORS.md等是一個文件，列出了對回購做出貢獻的人。

AUTHORS：AUTHORS或AUTHORS.txt或AUTHORS.md等是一個文件，列出了項目的重要作者，例如與作品有法律關係的人。

SUPPORT： SUPPORT orSUPPORT.txt或SUPPORT.mdetc. 是一個文件，解釋了讀者如何獲得有關存儲庫的幫助。GitHub 在“New Issue”頁面上鍊接了這個文件。

SECURITY： SECURITY描述的項目的安全策略，包括當前通過安全更新維護的版本列表。它還提供了有關的用戶如何提交漏洞報告的說明。有關詳細信息，請查看以下鏈接。

CODE_OF_CONDUCT： CODE_OF_CONDUCT該文件解釋瞭如何參與社區以及如何解決項目社區成員之間的任何問題。這裡有一些例子。

CONTRIBUTING： CONTRIBUTING是一個解釋人們應該如何貢獻的文件，它可以幫助驗證人們是否提交了格式正確的拉取請求並打開了有用的問題。GitHub 在“新問題”頁面和“新拉取請求”頁面上鍊接此文件。這有助於人們了解如何做出貢獻。

ACKNOWLEDGMENTS： ACKNOWLEDGMENTS或ACKNOWLEDGMENTS.txt或ACKNOWLEDGMENTS.md等是描述相關工作的文件，例如其他項目是依賴項，或庫，或模塊，或具有想要包含在項目中的自己的版權或許可。

CODEOWNERS： CODEOWNERS是定義負責存儲庫中代碼的個人或團隊的文件。當有人打開修改他們擁有的代碼的拉取請求時，代碼所有者將被自動請求審查。當具有管理員或所有者權限的人啟用了必需的審查時，他們還可以選擇要求代碼所有者的批准，然後作者才能將拉取請求合併到存儲庫中。

FUNDING：funding.yml是為的項目籌集資金或支持的項目的文件。

ISSUE_TEMPLATE：當將問題模板添加到存儲庫時，項目貢獻者將自動在問題正文中看到模板的內容。模板自定義和標準化希望在貢獻者打開問題時包含的信息。要將多個問題模板添加到存儲庫，請ISSUE_TEMPLATE/在項目根目錄中創建一個目錄。在該ISSUE_TEMPLATE/目錄中，可以根據需要創建任意數量的問題模板，例如ISSUE_TEMPLATE/bugs.md. 此列表包含多個問題和拉取請求模板。

PULL_REQUEST_TEMPLATE：當將PULL_REQUEST_TEMPLATE文件添加到存儲庫時，項目貢獻者將自動在拉取請求正文中看到模板的內容。模板自定義和標準化希望在貢獻者創建拉取請求時包含的信息。PULL_REQUEST_TEMPLATE/可以在任何支持的資料夾中創建一個子目錄以包含多個拉取請求模板。
```

# 安裝

## Debian(CentOS)

```bash
# 安裝 EPEL
yum install epel-release -y
# 添加 Wandisco Git repository
rpm --import https://packagecloud.io/wandisco/git/gpgkey
tee /etc/yum.repos.d/wandisco-git.repo << 'EOF'
[wandisco-git]
name=Wandisco GIT Repository
baseurl=https://packages.wandisco.com/centos/7/git/$basearch/
enabled=1
gpgcheck=1
gpgkey=https://packagecloud.io/wandisco/git/gpgkey
EOF
```

```bash
# 關閉
yum-config-manager --disable wandisco-git
# 啟動
yum-config-manager --enable wandisco-git
```

```bash
yum install epel-release -y
# Debian
yum install git -y
yum install git-all -y
```

## Ubuntu

```bash
# Ubuntu
apt-get install git-all
```

## Mac

```bash
# Homebrew
brew install git

# 更新
brew upgrade git
```

## SSH 綁定 創建

[Linux 基本 網路相關](../../02_作業系統/01_Unix/01_Linux/Linux%20基本%20網路相關.md)

```bash
# 創建RSA SSH Key
ssh-keygen

# 查看key內容
cat [key_filename]

# 從 ssh-rsa 複製到 username@pc-name
ssh-rsa ~一連串金鑰~ jaycelin@HOME-PC

# 設定金鑰代理
eval "$(ssh-agent -s)"	# 先使用下列指令開啟SSH代理伺服器
ssh-add ~/.ssh/id_rsa	# 接著將我們剛剛產生的金鑰加入到SSH Agent
```

## git-credential 個人令牌 token

[git-credential-store - Helper to store credentials on disk](https://git-scm.com/docs/git-credential-store)

.git-credentials 檔案內格式

```
STORAGE FORMAT
The .git-credentials file is stored in plaintext.
Each credential is stored on its own line as a URL like:

https://user:pass@example.com

沒有用 明確設置--file，有兩個文件 git-credential-store 將按優先順序搜索憑據：

~/.git-credentials
用戶特定的憑據文件。

$XDG_CONFIG_HOME/git/credentials
第二個用戶特定的憑據文件。
如果$XDG_CONFIG_HOME未設置或為空，$HOME/.config/git/credentials將被使用。
~/.git-credentials如果也有匹配的憑據，則不會使用存儲在此文件中的任何憑據。
如果有時使用不支持的舊版本 Git，最好不要創建此文件。

對於憑證查找，文件按上面給出的順序讀取，找到的第一個匹配憑證優先於列表下方文件中找到的憑證。

默認情況下，憑證存儲將寫入列表中的第一個現有文件。
如果這些文件都不存在，~/.git-credentials將被創建並寫入。
```

git 相關指令

```bash
# 將token存在 path/to/.sample-credentials
git config --global credential.helper 'store --file path/to/.sample-credentials'

git config --global credential.helper 'store --file ~/.git-credentials'
```

# Git 指令

```bash
# 查看git版本
git version

### Git status ###
# 看目前的狀態
git status
```

## Config 環境設定

```
常見的 Git 設定檔案位置與範圍
範圍	位置	說明
系統層級	/etc/gitconfig	所有使用者、所有專案共用（很少修改）
使用者層級	~/.gitconfig 或 ~/.config/git/config	特定使用者帳號全域套用
專案層級	專案資料夾/.git/config	僅作用於該 Git 專案
任意目錄自訂	透過 GIT_CONFIG_GLOBAL, GIT_CONFIG_SYSTEM, GIT_CONFIG 環境變數指定
```

```bash
### Config 環境設定 ###
# 設定全域(--global) 帳號名
git config --global user.name "username"
# 設定全域 郵箱
git config --global user.email "test@email.com"

# 打開 Git 的 color 顏色設定，如 git status

# 因為初次未設定，從 git config –list 會找不到該參數名，所以列上
git config --global color.ui true
# 移除KEY
git config --global --unset user.name

# HTTPS記憶帳號密碼，執行後下次HTTPS登入資訊會被儲存：
git config credential.helper store

# 移除設定與已存帳密
git config --unset credential.helper
```

```sh
# 全局配置
git config --global user.name "Global User"
git config --global user.email "globaluser@example.com"

# 專案配置（進入專案資料夾）
git config user.name "Project User"
git config user.email "projectuser@example.com"
```

## Remote – 遠端設定

```bash
### Remote – 遠端設定 ###
# 列出remote repository info
git remote -v

# 僅列Repositories
git remote show

# 新增Repository URL
git remote add origin [new url]

# 新增new-origin repository並指定URL
git remote add new-origin [new url]

# 更改Repository URL
git remote set-url origin [new url]

# 新增Repository Push URL (manipulate push URLs)
git remote set-url --add --push origin git:# backup.url.here

# 增加遠端 Repository 的 branch(origin -> project)
git remote add new-branch http:# git.example.com.tw/project.git

# 停用遠端Repository push & fetch
git remote set-url --push new-origin disabled

# 刪除Repository
git remote rm new-origin
```

## Pull – 拉取下來 從遠端更新本地(有衝突會合併)

```bash
### Git Pull – 拉取下來 從遠端更新本地(有衝突會合併) ###
# 預設為origin Master
git pull
git pull origin master

# 實際作用原理為先 git fetch 遠端的 branch，然後與本地端的 branch 做 merge，產生一個 merge commit 節點
git pull --rebase

###### 示意圖
##############################
#     1. 把本地 repo. 從上次 pull 之後的變更暫存起來
#     2. 回復到上次 pull 時的情況
#     3. 套用遠端的變更
#     4. 最後再套用剛暫存下來的本地變更。
#
#     假設合併前是這樣：
#
#           D---E master
#          /
#     A---B---C---F origin/master
#     使用 merge 合併後：
#
#           D--------E
#          /          \
#     A---B---C---F----G   master, origin/master
#     如果是 rebase 的方式，就不會有 G 合併點：
#     A---B---C---F---D'---E'   master, origin/master
# 所謂的”遠端”預設叫做origin，當你有多個不同遠端伺服器時，就會取不同名子了。
##############################
```

## Push – 將 Commit 送出去

```bash
### Git Push – 將 Commit 送出去
# 預設為origin Master
git push
git push origin master

# 實際的作用原理為將本地端的 master branch 與遠端的 master branch 作 fast-forward 合併。
# 如果出現 [rejected] 錯誤的話，表示你必須先作 pull。
git push origin +HEAD

# 適用於搭配reset刪除遠端commit
git push origin +HEAD:{brach}
```

## Add

```bash
### Git Add ###
#
git add .

# 將資料先暫存到 staging area, add 之後再新增的資料, 於此次 commit 不會含在裡面.
git add filename

# 修改過的檔案, 也要 add. (不然 commit 要加上 -a 的參數)
git add modify-file

# 只加修改過的檔案, 新增的檔案不加入.
git add -u

# 進入互動模式
git add -i
```

## Git 刪除檔案(取消追蹤)

```bash
### Git 刪除檔案 ###
git rm filename

### Git 清除Cache ###
# 例如Git含有submodule欲刪除後，需要清空.git/index Cache以讓Main module追蹤原submodule目錄。
# 取消追蹤單個文件
git rm --cached filename.txt

# 取消追蹤多個文件
git rm --cached file1.txt file2.txt

# 取消追蹤整個目錄（遞歸）
git rm --cached -r directory/

# 提交更改
git commit -m "停止追蹤文件"

# 如果想要保留在工作目錄中，不刪除實際文件，請使用下面的命令
git rm --cached --ignore-unmatch filename.txt

# 修改檔名、搬移目錄
git mv filename new-filename
```

## Commit 提交

```bash
### Git Commit 提交 ###
git commit
git commit -m 'commit message'

# 將所有修改過得檔案都 commit, 但是 新增的檔案 還是得要先 add.
git commit -a -m 'commit -message'

# -v 可以看到檔案哪些內容有被更改, -a 把所有修改的檔案都 commit
git commit -a -v

#  無異動Commit (Commit message only)
git commit --allow-empty

#  修改、修正已提交Commit
git commit --amend
git commit --amend --date="Wed Feb 16 14:00 2017 +0800"
git commit --amend --date="now"
```

## Branch 分支

```bash
### Git Branch 分支 ###
# 列出目前有多少 branch
git branch

# 產生新的 branch (名稱: new-branch), 若沒有特別指定, 會由目前所在的 branch / master 直接複製一份.
git branch new-branch

# 由 master 產生新的 branch(new-branch)
git branch new-branch master

# 由 tag(v1) 產生新的 branch(new-branch)
git branch new-branch v1

# 更改Local Branch名稱
git branch -m <new-branch-name>
git branch -m <old-branch> <new-branch-name>

# 刪除Local Branch
git branch -d new-branch

# 強制刪除 new-branch
git branch -D new-branch

# 強制刪除所有Local Branches除了當前branch by BASH
git branch | grep -v \* | xargs git branch -D

# 僅刪Merged過的Branches
git branch --merged | grep -v \* | xargs git branch -D

# 產生新的 branch, 並同時切換過去 new-branch
git checkout -b new-branch test

# 列出所有 remote repository  branch
git branch -r

# 列出所有 branch
git branch -a
```

## Git checkout 切換 branch

```bash
### Git checkout 切換 branch
# 本地修改沒有提交的 都捨棄
git checkout .

# 切換到 branch-name
git checkout branch-name

#取消對文件的修改。還原到最近的版本，放棄本地做的修改。
git checkout -- <file>

# 切換到 master
git checkout master

# 從 master 建立新的 new-branch, 並同時切換過去 new-branch
git checkout -b new-branch master

# 由現在的環境為基礎, 建立新的 branch
git checkout -b newbranch

# 於 origin 的基礎, 建立新的 branch
git checkout -b newbranch origin

# 還原檔案到 Repository 狀態
git checkout filename

# 將所有檔案都 checkout 出來(最後一次 commit 的版本), 注意, 若有修改的檔案都會被還原到上一版. (git checkout -f 亦可)
git checkout HEAD .

# 將所有檔案都 checkout 出來(xxxx commit 的版本, xxxx 是 commit 的編號前四碼), 注意, 若有修改的檔案都會被還原到上一版.
git checkout xxxx .

# 恢復到上一次 Commit 的狀態(* 改成檔名, 就可以只恢復那個檔案)
git checkout -- *

### Git remote branch 維護遠端分支檔案 ###
# 更新所有 Remote branchs
git remote update

# 完整更新(追蹤已刪除--prune)所有 Remote branchs
git remote update -p

# 列出所有 Remote branchs
git branch -r

# 刪除Remote branch
git push origin :branch
git push origin --delete branch # v1.7.0 support
```

## Fetch 抓取

```bash
### Git Fetch 抓取 / Git Checkout 切換 Repository 的 branch ###
# 抓取
git fetch origin

# 抓取 reps-branch, 並將此 branch 以相同名稱建立於 local 的 reps-branch
git checkout --track origin/reps-branch

# !!!!!!!!!!!!!!!!!!!!!!!!!!!!
# 抓取 reps-branch, 並將此 branch 以自訂名稱建立於 local 的 reps-branch
# 須先執行 git fetch
git checkout --track -b reps-branch origin/reps-branch
```

## Git track all remote branch 抓取所有遠端分支

```bash
### Git track all remote branch 抓取所有遠端分支 ###
# Bash - 將Remote Branches全名抓下來至Local(存在branch name問題不一致問題)
for remote in `git branch -r `; do git branch --track $remote; done

# Bash - 完美指令(剔除HEAD以及更名為Local使用的Branch名稱)
for remote in `git branch -r | grep -v /HEAD`; do git checkout --track $remote ; done

#  No Merged branches only
for remote in `git branch -r --no-merged | grep -v /HEAD`; do git checkout --track $remote ; done

# 其實結果同上
for remote in `git branch -r --no-merged | grep -v /HEAD `; do git branch --track ${remote/"origin/"/""}; done
```

## diff比較

```bash
### Git diff ###
# 與 Master 有哪些資料不同
git diff master

# 比較 staging area 跟本來的 Repository
git diff --cached

# tag1, 與 tag2 的 diff
git diff tag1 tag2

# tag1, 與 tag2 的 file1, file2 的 diff
git diff tag1:file1 tag2:file2

# 比較 目前位置 與 staging area
git diff

# 比較 staging area 與 Repository 差異
git diff --cached

# 比較目前位置 與 Repository 差別
git diff HEAD

# 比較目前位置 與 branch(new-branch) 的差別
git diff new-branch
git diff --stat

#  比較最新版與最新版前一次版本的差異
git diff HEAD^ HEAD

#  檢視更新的簡略統計資訊。
git diff ---stat

#  在更新的訊息後方顯示更動的檔案列表。
git diff --name-only

#  顯示新增、更動、刪除的檔案列表。
git diff --name-status

#  配合檔案狀態來篩選顯示檔案列表。
git diff --diff-filter= [(A|C|D|M|R|T)…​[*]]]
	# A = Added
	# C = Copied
	# M = Modified
	# R = Renamed
	# T = Changed
	# D =Delete
```

## Tag

```bash
### Git Tag ###
# 新增Tag至當前commit
git tag v1.0.0

# 新增Tag至ebff commit
git tag v1 ebff

# Tag也可以下中文, 任何文字都可以
git tag 中文 ebff

# 刪除 tag=tagname
git tag -d tagname

# 刪除遠端 tag=tagname
git push --delete origin tag

# 推送所有Local Tags至remote
git push --tags

# 抓取remote Tags
git fetch --prune --tags

# 列出remote Tags
git ls-remote --tags origin
```

## Bash 刪除所有Tag

```bash
### Bash 刪除所有Tag ###
# Local
git tag | xargs git tag -d

# Remote
git tag -l | xargs -n 1 git push --delete origin
```

## log 查看log紀錄

```bash
### Git log 查看log紀錄 ###
# 將所有 log 秀出
git log

# 秀出所有的 log (含 branch)
git log --all

# 將所有 log 和修改過得檔案內容列出
git log -p

# 將此檔案的 commit log 和 修改檔案內容差異部份列出
git log -p filename

# 列出此次 log 有哪些檔案被修改
git log --name-only

# 查每個版本間的更動檔案和行數
git log --stat --summary

# 這個檔案的所有 log
git log filename

# 這個目錄的所有 log
git log directory

# log 裡面有 foo() 這字串的.
git log -S "foo()"

# 不要秀出 merge 的 log
git log --no-merges

# 最後這 2週的 log
git log --since="2 weeks ago"

# 秀 log 的方式
git log --pretty=oneline

# 秀 log 的方式
git log --pretty=short
git log --pretty=format:'%h was %an, %ar, message: %s'

# 會有簡單的文字圖形化, 分支等.
git log --pretty=format:'%h : %s' --graph

# 依照主分支排序
git log --pretty=format:'%h : %s' --topo-order --graph

# 依照時間排序
git log --pretty=format:'%h : %s' --date-order --graph
```

## show

```bash
### Git show ###
# 查 log 是 commit ebff810c461ad1924fc422fd1d01db23d858773b 的內容
git show ebff

# 查 tag:v1 的修改內容
git show v1

# 查 tag:v1 的 test.txt 檔案修改內容
git show v1:test.txt

# 此版本修改的資料
git show HEAD

# 前一版修改的資料
git show HEAD^

# 前前一版修改的資料
git show HEAD^^

# 前前前前一版修改的資料
git show HEAD~4
```

## reset 還原 退回版本 (捨棄 commit)

```bash
### Git reset 還原 ###
# HEAD 表示當前版本
# HEAD^ 上一個版本
# HEAD^^ 上上一個版本
# HEAD^^^ 上上上一個版本
# HEAD~0 表示當前版本
# HEAD~1 上一個版本
# HEAD^2 上上一個版本
# HEAD^3 上上上一個版本
# 版本號
git reset [-- soft | -- mixed | -- hard ] [ HEAD ]
# --mixed為默認，可以不用帶該參數，用於重置暫存區的文件與上一次的提交(commit)保持一致，工作區文件內容保持不變。

# 取消最後一次的 commit，但保留變更內容
git reset --soft HEAD^

# 還原到最前面 取消 commit 並且丟棄變更
git reset --hard HEAD^

git reset --hard HEAD~3

# 將本地的狀態回退到和遠程的一樣
git reset --hard origin/master

#  從 staging area 狀態回到 unstaging 或 untracked (檔案內容並不會改變)
git reset HEAD filename

# 查看 commit 歷史
git log
# 回退到指定版本 可使用相同的 --soft 或 --hard 選項
git reset 052e

# 回退 hello.php的版本到上一個版本
git reset HEAD^ hello.php
```

## update-ref 刪除HEAD(適用取消首次commit)

```bash
### Git update-ref ###
# 刪除HEAD(適用取消首次commit)
git update-ref -d HEAD
```

## grep 查詢

```bash
### Git grep ###
# 查 v1 是否有 "te" 的字串
git grep "te" v1

# 查現在版本是否有 "te" 的字串
git grep "te"
```

## stash 暫存

```bash
### Git stash 暫存 ###
# 丟進暫存區(同git stash)
git stash save [<messgae>]

# 列出所有暫存區的資料
git stash list

# 取出stash並移除，預設最新一筆,
git stash pop [<stash>]

# 顯示stash差異，`-p`顯示diff，預設最新一筆
git stash show [<stash>]

# 取出最新的一筆 stash 暫存資料. 但是 stash 資料不移除
git stash apply

# 把 stash 都清掉
git stash drop

# 把 stash 都清掉
git stash clear
```

## merge 合併

```bash
### Git merge 合併 ###
# 合併另一個 branch，若沒有 conflict 衝突會直接 commit。若需要解決衝突則會再多一個 commit。
git merge
git merge master
git merge new-branch

# 衝突策略(Conflict strategy)
# 衝突時以Current Branch為準
git merge <branch> -X ours

# 衝突時以目標Branch為準
git merge <branch> -X their

# 將另一個 branch 的 commit 合併為一筆，特別適合需要做實驗的 fixes bug 或 new feature，最後只留結果。合併完不會幫你先 commit。
git merge –squash

# 只合併特定其中一個 commit。如果要合併多個，可以加上 -n 指令就不會先幫你 commit，這樣可以多 pick幾個要合併的 commit，最後再 git commit 即可。
git cherry-pick 321d76f
```

## Git Rebase 重改並合併歷史Commit

```bash
### Git Rebase 重改並合併歷史Commit ###
# -i 進入對話模式執行rebase，可以將第一個`pick`以後都設`fixup`
git rebase -i [commit]

##################
# pick 6fedbe6 Update README.md
# fixup b4c9305 Update README.md
# fixup a1d2257 Update README.md
##################
# 上例後兩個commit會直接併回第一個commit
```

## Git blame

```bash
### Git blame ###
# 關於此檔案的所有 commit 紀錄
git blame filename
```

## Git 還原已被刪除的檔案

```bash
### Git 還原已被刪除的檔案 ###
# 查看已刪除的檔案
git ls-files -d
# 將已刪除的檔案還原
git ls-files -d | xargs git checkout --
```

## Git 維護

```bash
### Git 維護 ###
# 整理前和整理後的差異, 可由: git count-objects 看到.
git gc
git gc --prune
git fsck --full
```

## Git revert 資料還原

```bash
### Git revert 資料還原 ###
# 回到前一次 commit 的狀態
git revert HEAD

# 回到前前一次 commit 的狀態
git revert HEAD^

# 從 staging area 狀態回到 unstaging 或 untracked (檔案內容並不會改變)
git reset HEAD filename

# 從 unstaging 狀態回到最初 Repository 的檔案(檔案內容變回修改前)
git checkout filename
```

## Git Conflict Strategy 處理已經衝突的檔案

```bash
### Git Conflict Strategy 處理已經衝突的檔案 ###
# 以我方(HEAD)策略處理套用全部檔案
git checkout --ours .
# 以對方(合併Branch)策略處理套用全部檔案
git checkout --theirs .
# 單檔處理範例
git checkout --ours filename
```

## Git Blame 逐條程式碼Log

```bash
### Git Blame 逐條程式碼Log ###
# 列出此檔逐筆最後異動紀錄
git blame [file]
 # 僅列出56至62行
git blame -L 56,62 [file]
```

## 設定永遠快取密碼，則可執行以下指令進行設定

```bash
# 設定永遠快取密碼，則可執行以下指令進行設定
# 如果使用 store 認證輔助方法，帳號密碼將會以明碼的方式儲存在 ~/.git-credentials 檔案中。
git config --global credential.helper store

	輸入
	protocol=https
	host=bitbucket.org
	username=deploy@appdet.com
	password=$password

```

## 設置git忽略

```bash
# 新增文檔
touch .gitignore
# 編輯
vim .gitignore

# # : 表註解
# / 結尾：表目錄
# * : 表示匹配 0 或多個字元
# ? : 表示匹配 0 或 1 個字元
# [] : 表示匹配中括弧內的任一個字元
# ! : 則是用來表示追蹤特定文件，有可能在前面規則中過濾掉了某個資料夾，但該資料夾下某個文件卻是要追蹤的，就可以用 !

# 過濾已追蹤的檔案
git rm -r --cached

### 範例 ###
# 忽略 secret.yml 檔案
secret.yml

# 忽略 config 目錄下的 database.yml 檔案
config/database.yml

# 忽略所有 db 目錄下附檔名是 .sqlite3 的檔案
/db/*.sqlite3

# 忽略所有附檔名是 .tmp 的檔案
*.tmp

# 當然你要忽略自己也可以，只是通常不會這麼做
.gitignore
```

# 範例

## 更換子模組路徑

Step 1：移除子模組資料夾與 Git 索引

```sh
# 移除 Git 中的索引（注意：這不會刪除實際資料夾）
git submodule deinit -f <repo-name>
git rm -f <repo-name>
```

若這步出錯，跳到下方「手動清除方式」。

Step 2：刪除 Git modules 設定資料夾

```sh
rm -rf .git/modules/<repo-name>
```

Step 3：編輯 .gitmodules 檔案，刪除以下段落

```
[submodule "<repo-name>"]
    path = ...
    url = ...
```

然後執行

```sh
git add .gitmodules
```

Step 4：commit 移除動作

```sh
git commit -m "Remove submodule <repo-name>"
```

### 手動清除指令

如果出錯（例如找不到 pathspec）

若之前手動刪過資料夾或 .git/modules/...，Git 就可能會報錯。

這時請用以下手動方式清掉所有痕跡：

```sh
# 刪除實體資料夾（如果還存在）
rm -rf <repo-path>

# 刪除 .gitmodules 裡的 mongo_data_process 區塊
# 或使用 sed 直接移除（備份先做一份）
sed -i.bak '/submodule "<repo-name>"/,+2d' .gitmodules

# 刪除 .git/config 中的對應段落（手動編輯建議）
```

```sh
git add .gitmodules
git commit -am "Force remove broken <repo-name> submodule"
```

## 不想合併，但又需要解決這些未追蹤檔案的問題

`方法一`：暫時將這些檔案加到 .gitignore

這樣可以讓 Git 忽略這些檔案：

將檔案名稱加入 .gitignore：

```sh
echo "docker-compose.yml" >> .gitignore
echo "dir/file1.toml" >> .gitignore
echo "dir/file2.toml" >> .gitignore
echo "dir/file3.toml" >> .gitignore
```

更新 Git 索引：

```sh
git add .gitignore
git commit -m "Temporarily ignore untracked files"
```

這樣可以繼續進行其他操作而不會受到未追蹤檔案的影響。

`方法二`：將檔案暫時移動到其他目錄

創建一個臨時目錄來存放這些檔案：

```sh
mkdir temp_backup
```

將未追蹤的檔案移動到臨時目錄：

```sh
mv docker-compose.yml temp_backup/
mv dir/file1.toml temp_backup/
mv dir/file2.toml temp_backup/
mv dir/file3.toml temp_backup/
```

進行需要的操作（不包含合併）

將檔案移回原來的位置：

```sh
mv temp_backup/docker-compose.yml .
mv temp_backup/file1.toml dir/
mv temp_backup/file2.toml dir/
mv temp_backup/file3.toml dir/
```

刪除臨時目錄：

```sh
rmdir temp_backup
```

`方法三`：使用 Git stash

將未追蹤的檔案暫存起來：

```sh
git stash push --include-untracked
```

進行需要的操作（不包含合併）

取回暫存的檔案：

```sh
git stash pop
```

## Python 更改資料夾內所有專案設定

```python
import os

# git 設定內容
name = "Tom.Li"
email = "open222333@gmail.com"

# 忽略的資料夾
ingore_dir = ['.vscode']

files = os.listdir()
for file in files:
    if os.path.isdir(file) and file not in ingore_dir:
        file = os.path.abspath(file)
        print(file)
        os.system(f"cd {file} && git config user.name {name}")
        os.system(f"cd {file} && git config user.email {email}")
```

## Git pull 錯誤操作

[git pull 撤销误操作](https://blog.csdn.net/code_segment/article/details/78597441)

```bash
# 查看歷史變更紀錄
git reflog (分支名)

git reset --hard 分支名(HEAD 或 branch)@{n}
```

## Git submodule 子模組

```bash
# 如果子模組資料夾是空的，可能是因為在主倉庫中未初始化或更新子模組。
# 執行以下命令以初始化和克隆子模組
git submodule update --init --recursive

# 已經初始化過子模組，但它們的資料夾仍然是空的，請執行以下命令來更新子模組
git submodule update --recursive --remote
```

```bash
# 添加子模組
git submodule add <repository_url> <path_to_submodule>

# 初始化和更新子模組
git submodule init
git submodule update

# 克隆包含子模組的儲存庫
git clone --recursive <repository_url>

# 手動檢查子模組的狀態
git submodule status
```

## Git subtree 子樹

```bash
# 添加子樹
git remote add -f <remote_name> <repository_url>
git merge -s ours --no-commit <remote_name>/<branch_name>
git read-tree --prefix=<path_to_subtree>/ -u <remote_name>/<branch_name>
git commit -m "Merge <remote_name> subtree into <path_to_subtree>"

# 更新子樹
git pull -s subtree <remote_name> <branch_name>

# 推送變更到遠端儲存庫
git push <remote_name> master
```

# 例外狀況

## fatal: unable to access : Failed to connect to bitbucket.org port 443: Connection timed out

```sh
git --version
```

更新 git 版本

## TCP connection reset by peer

```sh
git --version
```

更新 git 版本
