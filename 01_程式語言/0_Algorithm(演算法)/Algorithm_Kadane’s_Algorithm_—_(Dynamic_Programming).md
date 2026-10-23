# Algorithm Kadane’s Algorithm — (Dynamic Programming)

```
Kadane 演算法：以動態規劃在 O(n) 求最大子陣列和。
```

## 目錄

- [Algorithm Kadane’s Algorithm — (Dynamic Programming)](#algorithm-kadanes-algorithm--dynamic-programming)
	- [目錄](#目錄)
	- [參考資料](#參考資料)

```
Kadane算法掃描一次整個數列的所有數值，在每一個掃描點計算以該點數值為結束點的子數列的最大和（正數和）。該子數列由兩部分組成：以前一個位置為結束點的最大子數列、該位置的數值。因為該算法用到了「最佳子結構」（以每個位置為終點的最大子數列都是基於其前一位置的最大子數列計算得出），該算法可看成動態規劃的一個例子。
```

## 參考資料

[]()