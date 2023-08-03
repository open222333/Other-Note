# Python 基本 LinkList(鏈結串列)

```
```

## 目錄

- [Python 基本 LinkList(鏈結串列)](#python-基本-linklist鏈結串列)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[]()

# 用法

```Python
class ListNode:

    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


class LinkList:

    def __init__(self) -> None:
        self.linklist = None

    def generate_list(self, nodes: list) -> ListNode:
        """將串列依照順序 生成 鏈結串列

        Args:
            nodes (list): _description_

        Returns:
            ListNode: _description_
        """
        if len(nodes) == 0:
            return None

        # 創建第一個節點
        self.linklist = ListNode(nodes[0])
        current = self.linklist

        for val in nodes[1:]:
            current.next = ListNode(val)
            current = current.next
        return self.linklist

    def set_linklist(self, linklist):
        self.linklist = linklist

    def __traverse(self, listnode: ListNode):
        """遍歷 鏈結串列

        Args:
            listnode (ListNode): _description_
        """
        if listnode.next is None:
            print(listnode.val, end=' -> ')
            print("None")
        else:
            print(listnode.val, end=' -> ')
            self.__traverse(listnode.next)

	def deleteDuplicates(self) -> Optional[ListNode]:
		"""移除相鄰重複值

		Returns:
			Optional[ListNode]: _description_
		"""
        if self.linklist == None:
            return None

        curr = self.linklist
        while curr.next:
            if curr.val == curr.next.val:
                curr.next = curr.next.next
            else:
                curr = curr.next
        return self.linklist

    def print_linklist(self):
        """印出 鏈結串列
        """
        self.__traverse(self.linklist)

```

```Python
class Node():
    '''節點'''

    def __init__(self, data=None):
        self.data = data  # 資料
        self.next = None  # 向前指標
        self.previous = None  # 向後指標


class DoubleLinkedList():
    '''鏈結串列'''

    def __init__(self):
        self.head = None  # 鏈結串列頭部的節點
        self.tail = None  # 鏈結串列尾部的節點

    def add_double_node(self, new_node):
        '''將節點加入鏈結串列'''
        if isinstance(new_node, Node):  # 先確認此item是否為節點
            if self.head == None:  # 處理雙向鏈結串列是空的
                self.head = new_node  # 頭是new_node
                new_node.previous = None  # 指向前方
                new_node.next = None  # 指向後方
                self.tail = new_node  # 尾節點也是new_node
            else:  # 處理雙向鏈結串列不是空的
                self.tail.next = new_node  # 尾節點指標指向new_node
                new_node.previous = self.tail  # 新節點前方指標指向前
                self.tail = new_node  # 新節點成為尾部節點
        return

    def print_list_from_head(self):
        '''從頭部列印鏈結串列'''
        ptr = self.head  # 指標指向鏈結串列第1個節點
        while ptr:
            print(ptr.data)  # 列印節點
            ptr = ptr.next  # 移動指標到下一個節點

    def print_list_from_tail(self):
        '''從尾部列印鏈結串列'''
        ptr = self.tail  # 指標指向鏈結串列尾部節點
        while ptr:
            print(ptr.data)  # 列印節點
            ptr = ptr.previous  # 移動指標到前一個節點

```
