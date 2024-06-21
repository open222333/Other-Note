# Python 模組-內建 copy(淺層 (shallow) 和深層 (deep) 複製操作)

```
Python 的賦值陳述式不複製物件，而是建立目標和物件的繫結 (binding) 關係。
對於可變 (mutable) 或包含可變項目 (mutable item) 的集合，有時會需要一份副本來改變特定副本，而不必改變其他副本。
本模組提供了通用的淺層複製和深層複製操作（如下所述）。
```

## 目錄

- [Python 模組-內建 copy(淺層 (shallow) 和深層 (deep) 複製操作)](#python-模組-內建-copy淺層-shallow-和深層-deep-複製操作)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [深複製(Deep copy)](#深複製deep-copy)

## 參考資料

[copy 官方文檔](https://docs.python.org/zh-tw/3/library/copy.html)

# 用法

## 深複製(Deep copy)

```Python
import copy

# 原始列表
original_list = [[1, 2], [3, 4], [5]]

# 使用 deepcopy() 深複製(Deep copy)
copied_list = copy.deepcopy(original_list)

# 修改新列表
copied_list[0][0] = 100

# 结果
print("原始列表:", original_list)
print("修改後的新列表:", copied_list)
```
