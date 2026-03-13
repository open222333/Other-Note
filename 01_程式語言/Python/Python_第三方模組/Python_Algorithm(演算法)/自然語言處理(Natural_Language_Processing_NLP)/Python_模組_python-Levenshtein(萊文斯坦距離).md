# Python 模組 python-Levenshtein(萊文斯坦距離)

```
Levenshtein distance 是一種用於衡量兩個字串之間相似度的算法，它表示兩個字串之間需要進行的最小編輯操作次數，包括插入、刪除和替換。
```

## 目錄

- [Python 模組 python-Levenshtein(萊文斯坦距離)](#python-模組-python-levenshtein萊文斯坦距離)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[python-Levenshtein pypi](https://pypi.org/project/python-Levenshtein/)

# 指令

```bash
# 安裝
pip install python-Levenshtein
```

# 用法

```Python
import Levenshtein

# 例子字串
str1 = "kitten"
str2 = "sitting"

# 計算 Levenshtein distance
distance = Levenshtein.distance(str1, str2)

print(f"Levenshtein distance between '{str1}' and '{str2}': {distance}")
```
