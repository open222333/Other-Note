# Python 模組 PyExecJS(python調用javascript)

```
python調用javascript
```

## 目錄

- [Python 模組 PyExecJS(python調用javascript)](#python-模組-pyexecjspython調用javascript)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[PyExecJS pypi](https://pypi.org/project/PyExecJS/)

# 指令

```bash
# 安裝
pip install PyExecJS
```

# 用法

```Python
import execjs

execjs.eval("'red yellow blue'.split(' ')")
ctx = execjs.compile("""
	function add(x, y) {
		return x + y;
	}
""")
ctx.call("add", 1, 2)
```
