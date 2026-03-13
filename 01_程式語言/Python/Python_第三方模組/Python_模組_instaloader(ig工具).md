# Python 模組 instaloader(ig工具)

```
用於下載 Instagram 資料的 Python 模塊。它支援下載用戶資料、圖片、影片、高亮故事等。
```

## 目錄

- [Python 模組 instaloader(ig工具)](#python-模組-instaloaderig工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [下載用戶的資料](#下載用戶的資料)
  - [下載單個帖子](#下載單個帖子)
  - [下載用戶的所有圖片](#下載用戶的所有圖片)

## 參考資料

[instaloader pypi](https://pypi.org/project/instaloader/)

[instaloader github](https://github.com/instaloader/instaloader)

# 指令

```bash
# 安裝
pip install instaloader
```

# 用法

## 下載用戶的資料

```Python
import instaloader

# 創建 Instaloader 實例
L = instaloader.Instaloader()

# 輸入 Instagram 用戶名稱
profile_name = 'username'

# 獲取用戶資料
profile = instaloader.Profile.from_username(L.context, profile_name)

# 輸出用戶的一些資訊
print("Username:", profile.username)
print("Posts:", profile.mediacount)
print("Followers:", profile.followers)
print("Following:", profile.followees)

# 下載用戶的所有帖子
for post in profile.get_posts():
    L.download_post(post, target=profile.username)
```

## 下載單個帖子

```Python
import instaloader

# 創建 Instaloader 實例
L = instaloader.Instaloader()

# 輸入 Instagram 帖子的短碼
post_shortcode = 'shortcode'

# 獲取帖子
post = instaloader.Post.from_shortcode(L.context, post_shortcode)

# 下載帖子
L.download_post(post, target='.')
```

## 下載用戶的所有圖片

```Python
import instaloader

# 創建 Instaloader 實例
L = instaloader.Instaloader()

# 輸入 Instagram 用戶名稱
profile_name = 'username'

# 獲取用戶資料
profile = instaloader.Profile.from_username(L.context, profile_name)

# 下載用戶的所有圖片
L.download_profile(profile_name, profile_pic_only=True)
```
