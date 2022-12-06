# Python 模組-內建 tarfile(tar壓縮檔)

```
讀寫tar歸檔文件

tarfile 模塊可以用來讀寫 tar 歸檔，包括使用 gzip, bz2 和 lzma 壓縮的歸檔。請使用 zipfile 模塊來讀寫 .zip 文件，或者使用 shutil 的高層級函數。
```

## 目錄

- [Python 模組-內建 tarfile(tar壓縮檔)](#python-模組-內建-tarfiletar壓縮檔)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [範例相關](#範例相關)
- [用法](#用法)
	- [壓縮](#壓縮)
		- [壓縮並打包文件夾下的所有文件及目錄](#壓縮並打包文件夾下的所有文件及目錄)
	- [解壓縮](#解壓縮)

## 參考資料

[tarfile 官方文檔](https://docs.python.org/zh-tw/3/library/tarfile.html)

### 範例相關

[tarfile 刘江的博客教程](https://www.liujiangblog.com/course/python/63)

[[Python] 使用 tarfile 壓縮、解壓縮檔案](https://clay-atlas.com/blog/2020/08/14/python-cn-tarfile-compression-uncompression/)

# 用法

## 壓縮

```Python
# coding: utf-8
import os
import tarfile


# tarfile example
def tar_dir(path):
    tar = tarfile.open('test.tar.gz', 'w:gz')

    for root, dirs, files in os.walk(path):
        for file_name in files:
            tar.add(os.path.join(root, file_name))

    tar.close()


if __name__ == '__main__':
    path = 'test'
    tar_dir(path)
```

### 壓縮並打包文件夾下的所有文件及目錄

```Python
import tarfile
tar = tarfile.open("sample.tar", "w")
for name in ["foo", "bar", "quux"]:
    tar.add(name)
tar.close()
```

```Python
with tarfile.open("sample.tar", "w") as tar:
	for name in ["foo", "bar", "quux"]:
		tar.add(name)
```

## 解壓縮

```Python
# coding: utf-8
import os
import tarfile


# tarfile example
def tar_extract(file_path):
    tar = tarfile.open(file_path, 'r:gz')
    tar.extractall()


if __name__ == '__main__':
    file_path = 'test.tar.gz'
    tar_extract(file_path)
```