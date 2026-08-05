#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Other-Note 憑證歷史清除腳本
#
# 用途：以 git-filter-repo 將 13 組已外洩憑證從「全部 657+ 個 commit」中抹除，
#       替換為 REDACTED_* 佔位符，然後 force push 覆蓋 GitHub 上的歷史。
#
# 前提：先到各服務後台「撤銷」這些憑證。改歷史不等於撤銷 ——
#       已被抓走的 token 只有撤銷才會失效。
#
# 執行：在 macOS Terminal（不是 Claude）中執行
#   cd /Users/4ge0/Desktop/GitRepository/repo/Other-Note
#   bash _security/purge-secrets.sh
# ---------------------------------------------------------------------------
set -euo pipefail

REPO="/Users/4ge0/Desktop/GitRepository/repo/Other-Note"
SEC="$REPO/_security"
BACKUP="$HOME/Desktop/Other-Note-backup-$(date +%Y%m%d-%H%M%S).bundle"

cd "$REPO"

echo "==> 0. 前置檢查"
command -v python3 >/dev/null || { echo "找不到 python3"; exit 1; }
[ -f "$SEC/git-filter-repo" ]  || { echo "找不到 $SEC/git-filter-repo"; exit 1; }
[ -f "$SEC/replacements.txt" ] || { echo "找不到 $SEC/replacements.txt"; exit 1; }

# --- 清除死鎖檔 -------------------------------------------------------------
# Claude 透過檔案橋接操作時，掛載禁止 unlink，git 建了 lock 檔卻刪不掉。
# Terminal 有完整權限，可以安全清除。先確認真的沒有 git 程序在跑。
if pgrep -x git >/dev/null 2>&1; then
  echo "!! 偵測到執行中的 git 程序，請先結束它再執行本腳本"
  pgrep -ax git
  exit 1
fi

STALE=$(find .git -name '*.lock' -o -name 'index.lock.stale' -o -name 'tmp_obj_*' 2>/dev/null | wc -l | tr -d ' ')
if [ "$STALE" != "0" ]; then
  echo "    清除 $STALE 個死鎖 / 暫存殘檔"
  find .git \( -name '*.lock' -o -name 'index.lock.stale' -o -name 'tmp_obj_*' \) -delete 2>/dev/null || true
fi
# ---------------------------------------------------------------------------

if [ -n "$(git status --porcelain --untracked-files=no)" ]; then
  echo "!! 工作目錄有未提交的變更，請先 commit 或 stash 後再執行"
  git status --short
  exit 1
fi

echo "==> 1. 備份完整歷史到 $BACKUP"
git bundle create "$BACKUP" --all >/dev/null
echo "    備份完成（要還原：git clone \"$BACKUP\" 還原目錄）"

echo "==> 2. 記錄改寫前狀態"
BEFORE_COMMITS=$(git rev-list --all --count)
BEFORE_HEAD=$(git rev-parse HEAD)
ORIGIN_URL=$(git remote get-url origin)
echo "    commits=$BEFORE_COMMITS  HEAD=$BEFORE_HEAD"
echo "    origin=$ORIGIN_URL"

echo "==> 3. 重寫歷史（git-filter-repo --replace-text）"
python3 "$SEC/git-filter-repo" --replace-text "$SEC/replacements.txt" --force

echo "==> 4. 還原 origin remote（filter-repo 會刻意移除）"
git remote add origin "$ORIGIN_URL" 2>/dev/null || git remote set-url origin "$ORIGIN_URL"

echo "==> 5. 驗證：全歷史殘留掃描"
AFTER_COMMITS=$(git rev-list --all --count)
RESIDUE=$(git cat-file --batch-all-objects --batch-check='%(objectname) %(objecttype)' \
  | awk '$2=="blob"{print $1}' | git cat-file --batch \
  | grep -acF \
      -e '5020521993:AAH7drnyxRjbte5oFdlY93HXMzX5rIqAioQ' \
      -e 'M095I758qq4ab3EQPqE0uc0JaFKiPGD0DUmdpO65uyZ' \
      -e 'OypnXJMWd6lUEtBmSkBifiBmg4Klqzc3NeMPixq3lbm' \
      -e '2f3748a8f2a08838c3869627c7822d9b' \
      -e '68debe05603e4431cc2ba7e424567e03' \
      -e '47e85017ac296bc0b3073dab58fbdb0617a03828' \
      -e 'EUMModIySUtlEQccGFA8hs' \
      -e 'jz2l4k1HcnjOszeNNk4w4Z' \
      -e 'U34cb393ef0a5e9acb847b25765c4778d' \
      -e '9sNHzK9fe' -e '9sNHZK9fe' \
    || true)

echo "    commits: $BEFORE_COMMITS -> $AFTER_COMMITS"
echo "    殘留筆數: $RESIDUE"

if [ "$AFTER_COMMITS" != "$BEFORE_COMMITS" ]; then
  echo "!! commit 數對不上，請人工確認後再 push"; exit 1
fi
if [ "$RESIDUE" != "0" ]; then
  echo "!! 仍有殘留，請勿 push"; exit 1
fi
echo "    ✅ 驗證通過：commit 數不變、憑證零殘留"

echo
echo "==> 6. Force push（會覆蓋 GitHub 上的歷史，且無法復原）"
echo "    目標：$ORIGIN_URL"
read -r -p "    確定要推送嗎？輸入 yes 繼續： " ANS
if [ "$ANS" = "yes" ]; then
  git push --force --all origin
  git push --force --tags origin
  echo "    ✅ 已推送"
  echo
  echo "    後續必做："
  echo "    - 到 GitHub Support 申請清除快取的舊 commit"
  echo "      （force push 後，舊 SHA 直連網址仍可能讀得到）"
  echo "      https://support.github.com/request"
  echo "    - 通知所有 clone / fork 過此 repo 的人重新 clone"
  echo "    - 其他機器上的舊 clone 直接砍掉重 clone，不要 pull"
else
  echo "    已略過推送。之後手動執行："
  echo "      git push --force --all origin && git push --force --tags origin"
fi

echo
echo "==> 7. 清理"
echo "    可刪除：$SEC  （備份留在 $BACKUP）"
