# Algorithm 筆記

## 參考資料

# 時間複雜度

```
時間複雜度公式(演算法的漸進時間複雜度)：
	T(n) = O(f(n))
	f(n) 表示每行程式碼執行次數之和
	O 表示正比例關係。
```

# 空間複雜度

```
一個演算法在執行過程中臨時佔用儲存空間大小的一個量度
	S(n) = O(f(n))
```

# 陣列(Array)

```
資料放在連續的記憶體空間，陣列資料為元素

時間複雜度：
	讀取 O(1)
	插入 O(n)
	刪除 O(n)
	搜尋 O(log n)

優點：
	簡單好用、容易理解、讀取速度很快
```

# 鏈結串列(Linked list)

```
由指標區以及資料區組成
指標區指向下一個節點元素
與陣列不同於資料元素散落在記憶體各處
```

# 佇列(Queue)

```
線性的資料結構
由一端插入(enqueue)資料，另一端取出(dequeue)資料(先進先出)
```

# 堆疊(Stack)

```
線性的資料結構
由一端放入(push)資料，另一端取出(pop)資料(先進後出)
```

# 二元樹(Binary Tree)

```
樹狀的資料結構

每個節點有三個資料：
	數據(data)
	右指標(right)
	左指標(left)

最頂端為根節點(root node)
最末端為葉節點(leaves node)

深度(depth)
	每n層 最多有2^(n-1)個節點

建立規則：
	第一個數據為 根節點(root node)
	新數據比目前節點 大送至右 小送至左

刪除節點(三種狀況)：
	若有0個子節點：
		直接刪除
	若有1個子節點：
		將子節點移至刪除的節點
	若有2個子節點：
		1.取左節點最大值
		2.取右節點最小值

滿二元樹(Full Binary Tree)：
	除了葉節點(leaves nodes)沒有子節點外，其他都有2個子節點。

完全二元樹(Complete Binary Tree)：
	最深層每一個節點均是滿的，且最深層的最右節點左邊均是滿的。

平衡二元樹(Balanced Binary Tree)：
	每個節點的2個子節點的深度差異不超過1

完美二元樹(Perfect Binary Tree)：
	除了最深層的節點外，每個節點均是滿的。
	所有完美二元樹街是完全二元樹。
```

# 堆積樹(Heap Tree)

```
一種二元樹
外觀屬於 完全二元樹

最大堆積樹(Maximum Heap)
	根節點為最大值
	每個父節點的值一定大於或等於子節點的值

最小堆積樹(Minimum Heap)
	根節點為最小值
	每個父節點的值一定小於或等於子節點的值
```

# 雜湊表(Hash Table)

```
Hash 雜湊碼 哈希值 散列值
結構:
	key : value
```

# 排序(Sort)

```Python
def bubble_sort(nLst):
    '''泡沫排序法(Bubble Sort)'''
    length = len(nLst)
    for i in range(length - 1):
        print("第 %d 次外圈排序" % (i + 1))
        for j in range(length - 1 - i):
            if nLst[j] > nLst[j + 1]:
                nLst[j], nLst[j + 1] = nLst[j + 1], nLst[j]
            print("第 %d 次內圈排序 : " % (j + 1), nLst)
    return nLst

def cocktail_sort(nLst):
    '''雞尾酒排序法(Cocktail Sort)
	先左到右一次回圈 完成會得到最大值 在右到左 若有一次都沒變更值 則排序完成'''
    n = len(nLst)
    is_sorted = True
    start = 0  # 前端索引
    end = n - 1  # 末端索引
    while is_sorted:
        is_sorted = False  # 重置是否排序完成
        for i in range(start, end):  # 往右比較
            if (nLst[i] > nLst[i + 1]):
                nLst[i], nLst[i + 1] = nLst[i + 1], nLst[i]
                is_sorted = True
        print("往後排序過程：", nLst)
        if not is_sorted:  # 如果沒有交換就結束
            break
        end -= 1  # 末端索引左移一個索引
        for i in range(end - 1, start - 1, -1):  # 往左比較
            if (nLst[i] > nLst[i + 1]):
                nLst[i], nLst[i + 1] = nLst[i + 1], nLst[i]
                is_sorted = True
        start += 1  # 前端索引右移一個索引
        print("往前排序過程：", nLst)
    return nLst

def selection_sort(nLst):
	'''選擇排序法(Selection Sort)
	將最小值與最小索引值對調'''
    for i in range(len(nLst) - 1):
        index = i  # 最小值的索引
        for j in range(i + 1, len(nLst)):  # 找最小值的索引
            if nLst[index] > nLst[j]:
                index = j
        if i == index:  # 如果目前索引是最小值索引
            pass  # 不更動
        else:
            nLst[i], nLst[index] = nLst[index], nLst[i]  # 資料對調
        print("第 %d 次 迴圈排序" % (i + 1), nLst)
    return nLst

def insertion_sort(nLst):
    '''插入排序(Insertion Sort)'''
    n = len(nLst)
    if n == 1:  # 只有一筆資料
        print("第%d次迴圈排序" % n, nLst)
        return nLst
    print("第1次迴圈排序", nLst)
    
    # 重點迴圈
    for i in range(1, n):
        for j in range(i, 0, -1):
            if nLst[j] < nLst[j - 1]:
                nLst[j], nLst[j - 1] = nLst[j - 1], nLst[j]
            else:
                break
        print("第%d次迴圈排序" % (i + 1), nLst)
    return nLst


class Heaptree():
    '''堆積樹排序(Heap Sort)'''

    def __init__(self) -> None:
        self.heap = []  # 堆積樹串列
        self.size = 0  # 堆積樹串列元素個數

    def data_down(self, i):
        '''如果節點值大於子節點
        則資料與較小的子節點對調'''
        while (i * 2 + 2) <= self.size:
            # 如果有子節點則繼續
            mi = self.get_min_index(i)  # 取得較小值的子節點
            if self.heap[i] > self.heap[mi]:
                # 如果目前節點大於子節點
                self.heap[i], self.heap[mi] = self.heap[mi], self.heap[i]
            i = mi

    def get_min_index(self, i):
        # 傳回較小值的子節點索引
        if i * 2 + 2 >= self.size:  # 只有一個在左子節點
            return i * 2 + 1  # 傳回左子節點索引
        else:
            # 如果左子節點小于右子節點
            if self.heap[i * 2 + 1] < self.heap[i * 2 + 2]:
                return i * 2 + 1  # True傳回左子節點索引
            else:
                return i * 2 + 2  # False傳回右子節點索引

    def build_heap(self, mylist):
        '''建立堆積樹'''
        i = (len(mylist) // 2) - 1  # 從有子節點的節點開始處理
        self.size = len(mylist)  # 得到串列個數
        self.heap = mylist  # 初步建立堆積樹串列
        while (i >= 0):
            self.data_down(i)  # 從下層往上處理
            i = i - 1

    def get_min(self):
        min_ret = self.heap[0]
        self.size -= 1
        self.heap[0] = self.heap[self.size]
        self.heap.pop()
        self.data_down(0)
        return min_ret

```