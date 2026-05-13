# Python 模組-內建 unittest(單元測試框架)

```
```

## 目錄

- [Python 模組-內建 unittest(單元測試框架)](#python-模組-內建-unittest單元測試框架)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[unittest 官方文檔](https://docs.python.org/zh-tw/3/library/unittest.html)

# 用法

```Python
import unittest

def add(a, b):
    return a + b

class TestAddFunction(unittest.TestCase):

    def test_add_positive_numbers(self):
		'''
		將使用 self.assertEqual() 斷言方法來測試 add() 函數是否正確地計算兩個正數相加的結果。
		期望 add(2, 3) 的結果是 5。
		'''
        self.assertEqual(add(2, 3), 5)

    def test_add_negative_numbers(self):
		'''
		這個測試類似，但是這次測試 add() 函數是否正確地計算兩個負數相加的結果。
		期望 add(-2, -3) 的結果是 -5。
		'''
        self.assertEqual(add(-2, -3), -5)

if __name__ == '__main__':
    unittest.main()
```
