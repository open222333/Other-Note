# Python 模組 retry(錯誤重試處理)

```
retry是一個用於錯誤處理的模塊，功能類似try-except，但更加快捷方便
```

## 目錄

- [Python 模組 retry(錯誤重試處理)](#python-模組-retry錯誤重試處理)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[retry pypi](https://pypi.org/project/retry/)

[Python中retry的简单用法](https://www.cnblogs.com/feffery/p/10347348.html)

[Python异常重试解决方案](https://www.biaodianfu.com/python-error-retry.html)

[python之retry函数](https://blog.csdn.net/weixin_42575020/article/details/105553838)

# 指令

```bash
# 安裝
pip install retry
```

# 用法

```Python
# 作為裝飾器進行使用
from retry import retry

@retry()
def demo():
    print('Error')
    raise

demo()

# 主要參數：
# exceptions：傳入指定的錯誤類型，默認為Exception，即捕獲所有類型的錯誤，也可傳入元組形式的多種指定錯誤類型

# tries：定義捕獲錯誤之後重複運行次數，默認為-1，即為無數次

# delay：定義每次重複運行之間的停頓時長，單位秒，默認為0，即無停頓

# backoff：呈指數增長的每次重複運行之間的停頓時長，需要配合delay來使用，譬如delay設置為3，backoff設置為2，則第一次間隔為3*2**0=1秒，第二次3*2**1=2秒，第三次3*2**2=4秒，以此類推，默認為1

# max_delay：定義backoff和delay配合下出現的等待時間上限，當delay*backoff**n大於max_delay時，等待間隔固定為該值而不再增長

import time
from retry import retry

start_time = time.clock()

@retry(delay=1,tries=4,backoff=2)
def demo(start_time):
    print(round(time.clock()-start_time,0))
    raise

demo(start_time)
```



