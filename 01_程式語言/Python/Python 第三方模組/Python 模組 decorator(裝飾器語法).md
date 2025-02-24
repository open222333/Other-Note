# Python 模組 decorator(裝飾器語法)

```
為什麼使用 decorator 模組？
雖然 Python 內建裝飾器語法（@ 符號）可以用 functools.wraps 來保留原始函式資訊，但 decorator 模組讓自訂裝飾器的寫法更簡潔，特別是在處理 帶有參數的裝飾器 或 多層裝飾器 時非常方便。

內建的替代方案
如果你不想安裝 decorator，可以用 Python 內建的 functools.wraps 來達到類似的效果
```

## 目錄

- [Python 模組 decorator(裝飾器語法)](#python-模組-decorator裝飾器語法)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)

## 參考資料

[decorator pypi](https://pypi.org/project/decorator/)

# 指令

```bash
# 安裝
pip install decorator
```

# 安裝

```bash
```

# 用法

內建的 functools.wraps

```Python
from functools import wraps

def _k_t_verify(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        print("執行前驗證")
        result = func(*args, **kwargs)
        print("執行後驗證")
        return result
    return wrapper

@_k_t_verify
def my_function(x, y):
    print(f"運行 my_function: {x} + {y} = {x + y}")
    return x + y

my_function(3, 4)
```

decorator.decorator(_k_t_verify) 讓 _k_t_verify 變成標準裝飾器格式。

_k_t_verify 會接收原始函式 func，並在其前後添加額外的行為。

使用 @verify_decorator 時，_k_t_verify 會自動接收 my_function 作為 func 參數。

這種方法比手動處理 functools.wraps 更簡潔，適合有多個裝飾器的情境。

```Python
from decorator import decorator

def _k_t_verify(func, *args, **kwargs):
    print("執行前驗證")
    result = func(*args, **kwargs)
    print("執行後驗證")
    return result

# 使用 decorator.decorator 讓 _k_t_verify 變成真正的裝飾器
verify_decorator = decorator.decorator(_k_t_verify)

# 使用裝飾器
@verify_decorator
def my_function(x, y):
    print(f"運行 my_function: {x} + {y} = {x + y}")
    return x + y

# 測試
my_function(3, 4)
```
