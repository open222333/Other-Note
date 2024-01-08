# Algorithm Levenshtein distance(萊文斯坦距離)

```
Levenshtein distance 是一種用於衡量兩個字串之間相似度的算法，它表示兩個字串之間需要進行的最小編輯操作次數，包括插入、刪除和替換。
```

## 目錄

- [Algorithm Levenshtein distance(萊文斯坦距離)](#algorithm-levenshtein-distance萊文斯坦距離)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [Python 相關](#python-相關)
- [實作](#實作)
	- [Python](#python)
	- [C](#c)
	- [Java](#java)

## 參考資料

[萊文斯坦距離](https://zh.wikipedia.org/zh-tw/%E8%90%8A%E6%96%87%E6%96%AF%E5%9D%A6%E8%B7%9D%E9%9B%A2#%E6%BC%94%E7%AE%97%E6%B3%95)

### Python 相關

[]()

# 實作

## Python

```python
def levenshtein_distance(str1, str2):
    len_str1 = len(str1)
    len_str2 = len(str2)

    # 初始化一個二維矩陣，用於保存中間結果
    matrix = [[0] * (len_str2 + 1) for _ in range(len_str1 + 1)]

    # 初始化第一行和第一列
    for i in range(len_str1 + 1):
        matrix[i][0] = i
    for j in range(len_str2 + 1):
        matrix[0][j] = j

    # 填充矩陣，計算最小編輯距離
    for i in range(1, len_str1 + 1):
        for j in range(1, len_str2 + 1):
            cost = 0 if str1[i - 1] == str2[j - 1] else 1
            matrix[i][j] = min(
                matrix[i - 1][j] + 1,        # 刪除
                matrix[i][j - 1] + 1,        # 插入
                matrix[i - 1][j - 1] + cost  # 替換
            )

    return matrix[len_str1][len_str2]

# 例子字串
str1 = "kitten"
str2 = "sitting"

# 計算 Levenshtein distance
distance = levenshtein_distance(str1, str2)

print(f"Levenshtein distance between '{str1}' and '{str2}': {distance}")
```

## C

```c
```

## Java

```java
```
