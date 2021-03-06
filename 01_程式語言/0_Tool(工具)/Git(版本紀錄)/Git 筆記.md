# Git 筆記

```
```

## 目錄

- [Git 筆記](#git-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [管理 commit log](#管理-commit-log)
		- [圖形化介面軟體](#圖形化介面軟體)
- [Git 基本概念](#git-基本概念)
- [Linux 安裝 Git](#linux-安裝-git)
- [Mac 安裝 Git](#mac-安裝-git)
- [SSH 綁定 創建](#ssh-綁定-創建)
- [git-credential 個人令牌 token](#git-credential-個人令牌-token)
- [Git 指令](#git-指令)
- [設置git忽略](#設置git忽略)
- [程式輔助範例](#程式輔助範例)
	- [Python 更改資料夾內所有專案設定](#python-更改資料夾內所有專案設定)
- [Git pull 錯誤操作](#git-pull-錯誤操作)

## 參考資料

[官方網站](https://git-scm.com/)

[為你自己學 Git](https://gitbook.tw/)

[Git 安裝教學](https://git-scm.com/book/zh-tw/v2/%E9%96%8B%E5%A7%8B-Git-%E5%AE%89%E8%A3%9D%E6%95%99%E5%AD%B8)

[Git 文檔](https://git-scm.com/book/zh-tw/v2/%E9%96%8B%E5%A7%8B-%E9%97%9C%E6%96%BC%E7%89%88%E6%9C%AC%E6%8E%A7%E5%88%B6)

[git 免費電子書](https://git-scm.com/book/zh-tw/v2)

[git 免費電子書](https://kknews.cc/zh-tw/code/xlx33jo.html)

[Git上的三種工作流程]
(https://medium.com/i-think-so-i-live/git%E4%B8%8A%E7%9A%84%E4%B8%89%E7%A8%AE%E5%B7%A5%E4%BD%9C%E6%B5%81%E7%A8%8B-10f4f915167e)

### 管理 commit log

[使用 git commit template 管理 git log](https://medium.com/dev-chill/%E4%BD%BF%E7%94%A8-git-commit-template-%E7%AE%A1%E7%90%86-git-log-cb70f95fda2f)

[如何使用 git commit template 與 git hooks 管理團隊的 git log](https://allen-hsu.github.io/2017/07/02/git-message-template-and-githook/)

```
管理 commit log
目的：統一團隊之中的 commit message 格式。
```

### 圖形化介面軟體

[Fork](https://git-fork.com/)

[Sourcetree](https://www.sourcetreeapp.com/)

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

# Linux 安裝 Git

```bash
# Debian
yum install git-all -y

# Ubuntu
apt-get install git-all
```

# Mac 安裝 Git

```bash
# Homebrew
brew install git

# 更新
brew upgrade git
```

# SSH 綁定 創建

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

# git-credential 個人令牌 token

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
如果您有時使用不支持的舊版本 Git，最好不要創建此文件。

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

### Git Config 環境設定 ###
# 設定全域(--global) 帳號名
git config --global user.name "username"
# 設定全域 郵箱
git config --global user.email 'test@email.com'

# 打開 Git 的 color 顏色設定，如 git status

# 因為初次未設定，從 git config –list 會找不到該參數名，所以列上
git config --global color.ui true
# 移除KEY
git config --global --unset user.name

# HTTPS記憶帳號密碼，執行後下次HTTPS登入資訊會被儲存：
git config credential.helper store

# 移除設定與已存帳密
git config --unset credential.helper

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

### Git Push – 將 Commit 送出去
# 預設為origin Master
git push
git push origin master

# 實際的作用原理為將本地端的 master branch 與遠端的 master branch 作 fast-forward 合併。
# 如果出現 [rejected] 錯誤的話，表示你必須先作 pull。
git push origin +HEAD

# 適用於搭配reset刪除遠端commit
git push origin +HEAD:{brach}


### Git Add ###
# 將資料先暫存到 staging area, add 之後再新增的資料, 於此次 commit 不會含在裡面.
git add filename

# 修改過的檔案, 也要 add. (不然 commit 要加上 -a 的參數)
git add modify-file

# 只加修改過的檔案, 新增的檔案不加入.
git add -u

# 進入互動模式
git add -i

### Git 刪除檔案 ###
git rm filename

### Git 清除Cache ###
# 例如Git含有submodule欲刪除後，需要清空.git/index Cache以讓Main module追蹤原submodule目錄。
git rm --cached -r folder

# 修改檔名、搬移目錄
git mv filename new-filename

### Git status ###
# 看目前的狀態
git status

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

### Git Fetch 抓取 / Git Checkout 切換 Repository 的 branch ###
# 抓取
git fetch origin

# 抓取 reps-branch, 並將此 branch 以相同名稱建立於 local 的 reps-branch
git checkout --track origin/reps-branch

# !!!!!!!!!!!!!!!!!!!!!!!!!!!!
# 抓取 reps-branch, 並將此 branch 以自訂名稱建立於 local 的 reps-branch
# 須先執行 git fetch
git checkout --track -b reps-branch origin/reps-branch

### Git track all remote branch 抓取所有遠端分支 ###
# Bash - 將Remote Branches全名抓下來至Local(存在branch name問題不一致問題)
for remote in `git branch -r `; do git branch --track $remote; done

# Bash - 完美指令(剔除HEAD以及更名為Local使用的Branch名稱)
for remote in `git branch -r | grep -v /HEAD`; do git checkout --track $remote ; done

#  No Merged branches only
for remote in `git branch -r --no-merged | grep -v /HEAD`; do git checkout --track $remote ; done

# 其實結果同上
for remote in `git branch -r --no-merged | grep -v /HEAD `; do git branch --track ${remote/"origin/"/""}; done

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

### Bash 刪除所有Tag ###
# Local
git tag | xargs git tag -d

# Remote
git tag -l | xargs -n 1 git push --delete origin

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

### Git reset 還原 ###
#  還原到最前面
git reset --hard HEAD

#  還原到前一個commit
git reset --hard HEAD^
git reset --hard HEAD~3

#  本地檔案不異動，純紀錄異動
git reset --soft HEAD~3

#  從 staging area 狀態回到 unstaging 或 untracked (檔案內容並不會改變)
git reset HEAD filename

### Git update-ref ###
# 刪除HEAD(適用取消首次commit)
git update-ref -d HEAD

### Git grep ###
# 查 v1 是否有 "te" 的字串
git grep "te" v1

# 查現在版本是否有 "te" 的字串
git grep "te"
```

[【狀況題】手邊的工作做到一半，臨時要切換到別的任務](https://gitbook.tw/chapters/faq/stash)

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

### Git Rebase 重改並合併歷史Commit ###
# -i 進入對話模式執行rebase，可以將第一個`pick`以後都設`fixup`
git rebase -i [commit]

##################
# pick 6fedbe6 Update README.md
# fixup b4c9305 Update README.md
# fixup a1d2257 Update README.md
##################
# 上例後兩個commit會直接併回第一個commit

### Git blame ###
# 關於此檔案的所有 commit 紀錄
git blame filename

### Git 還原已被刪除的檔案 ###
# 查看已刪除的檔案
git ls-files -d
# 將已刪除的檔案還原
git ls-files -d | xargs git checkout --

### Git 維護 ###
# 整理前和整理後的差異, 可由: git count-objects 看到.
git gc
git gc --prune
git fsck --full

### Git revert 資料還原 ###
# 回到前一次 commit 的狀態
git revert HEAD

# 回到前前一次 commit 的狀態
git revert HEAD^

# 從 staging area 狀態回到 unstaging 或 untracked (檔案內容並不會改變)
git reset HEAD filename

# 從 unstaging 狀態回到最初 Repository 的檔案(檔案內容變回修改前)
git checkout filename

### Git Conflict Strategy 處理已經衝突的檔案 ###
# 以我方(HEAD)策略處理套用全部檔案
git checkout --ours .
# 以對方(合併Branch)策略處理套用全部檔案
git checkout --theirs .
# 單檔處理範例
git checkout --ours filename

### Git Blame 逐條程式碼Log ###
# 列出此檔逐筆最後異動紀錄
git blame [file]
 # 僅列出56至62行
git blame -L 56,62 [file]

# 設定永遠快取密碼，則可執行以下指令進行設定
# 如果使用 store 認證輔助方法，帳號密碼將會以明碼的方式儲存在 ~/.git-credentials 檔案中。
git config --global credential.helper store

	輸入
	protocol=https
	host=bitbucket.org
	username=deploy@appdet.com
	password=$password

```

# 設置git忽略

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

# 程式輔助範例

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

# Git pull 錯誤操作

[git pull 撤销误操作](https://blog.csdn.net/code_segment/article/details/78597441)

```bash
# 查看歷史變更紀錄
git reflog (分支名)

git reset --hard 分支名(HEAD 或 branch)@{n}
```
