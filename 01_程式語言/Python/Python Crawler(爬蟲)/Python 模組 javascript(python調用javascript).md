# Python 模組 javascript(python調用javascript)

```
python調javascript
```

## 參考資料

[javascript pypi](https://pypi.org/project/javascript/)

# 指令

```bash
# 安裝
pip3 install javascript
```

# 用法

```Python
from javascript import require, globalThis

chalk, fs = require("chalk"), require("fs")

print("Hello", chalk.red("world!"), "it's", globalThis.Date().toLocaleString())
fs.writeFileSync("HelloWorld.txt", "hi!")
```
