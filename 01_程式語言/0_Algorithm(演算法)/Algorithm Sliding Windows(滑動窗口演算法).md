# Algorithm Sliding Windows(滑動窗口演算法)

```
1.在字串S中使用雙指標中的左右指標技巧，初始化 left = right = 0，把索引閉區間 [left, right] 稱為一個「視窗」。

2.先不斷地增加 right 指標擴大視窗 [left, right]，直到視窗中的字串符合要求（包含了 T 中的所有字元）。

3.停止增加 right，轉而不斷增加 left 指標縮小視窗 [left, right]，直到視窗中的字串不再符合要求（不包含 T 中的所有字元了）。同時，每次增加 left，都要更新一輪結果。

4.重複第 2 和第 3 步，直到 right 到達字串 S 的盡頭。
```

* 0003_LongestSubstringWithoutRepeatingCharacters.py