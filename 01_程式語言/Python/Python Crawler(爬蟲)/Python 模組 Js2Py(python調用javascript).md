# Python 模組 Js2Py(python調用javascript)

```
將 JS 代碼直接轉譯成 Python 代碼
```

## 參考資料

[Js2Py pypi](https://pypi.org/project/Js2Py/)

# 指令

```bash
# 安裝
pip install Js2Py
```

# 用法

```Python
import js2py

add = js2py.eval_js("""
     function add(x, y) {
         return x + y;
     }
""")
```
