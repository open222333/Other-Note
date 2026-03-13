# Python 模組 GitPython(git套件)

```
```

## 目錄

- [Python 模組 GitPython(git套件)](#python-模組-gitpythongit套件)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[GitPython pypi](https://pypi.org/project/GitPython/)

# 指令

```bash
# 安裝
pip install GitPython
```

# 用法

```Python
from git import Repo


# 先決條件
full_local_path = "/path/to/repo/"
username = "your-username"
token = "your-token"
remote = f"https://{username}:{token}@github.com/some-account/some-repo.git"

# Clone repository
# 會儲存憑證在 .git/config
Repo.clone_from(remote, full_local_path)

# Commit changes
repo = Repo(full_local_path)
repo.git.add("rel/path/to/dir/with/changes/")
repo.index.commit("Some commit message")

# Push changes
# 憑證已存在 .git/config
repo = Repo(full_local_path)
origin = repo.remote(name="origin")
origin.push()

repo = Repo(full_local_path)
config = repo.config_reader()
config.get_value("user", "name")
```
