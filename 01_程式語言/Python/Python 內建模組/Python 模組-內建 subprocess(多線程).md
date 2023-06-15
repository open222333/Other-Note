# Python 模組-內建 subprocess(多線程)

```
在程式內建立子行程
```

## 目錄

- [Python 模組-內建 subprocess(多線程)](#python-模組-內建-subprocess多線程)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [run函式](#run函式)
	- [判斷檔案是否存在於遠端伺服器](#判斷檔案是否存在於遠端伺服器)

## 參考資料

[subprocess 官方文檔](https://docs.python.org/zh-tw/3/library/subprocess.html)

# 用法

## run函式

```Python
import subprocess

# 執行命令，捕獲輸出、錯誤和返回碼
result = subprocess.run(["ls", "-l"], capture_output=True, text=True)

# path = ''
# command = f'ls -al {path}'
# result = subprocess.run(command, capture_output=True, text=True)


# 獲取命令的輸出
output = result.stdout

# 獲取命令的錯誤輸出
error = result.stderr

# 獲取命令的返回碼
returncode = result.returncode
```

## 判斷檔案是否存在於遠端伺服器

```Python
import subprocess

ssh_host = 'username@host'
file = '/path/to/test'

resp = subprocess.call(['ssh', ssh_host, f'test -e {file}'])
if resp == 0:
	print (f'{file} exists')
else:
	print (f'{file} does not exist')
```
