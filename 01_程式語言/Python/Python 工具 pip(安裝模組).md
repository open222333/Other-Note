# Python 工具 pip(安裝模組)

## 參考資料

[pip](http://pip.readthedocs.org/en/stable/user_guide/#configuration)

`需要在 pip.ini 檔案中指定預設安裝位置`

[Pip freeze generating '@ file:///' on conda environment](https://stackoverflow.com/questions/62863020/pip-freeze-generating-file-on-conda-environment)

[pip list](https://pip.pypa.io/en/stable/cli/pip_list/)

[pip freeze](https://pip.pypa.io/en/stable/cli/pip_freeze/)

[所有指令](https://pip.pypa.io/en/stable/cli/)

# 指令

```bash
# 查看pip版本 以及目前pip安裝的site-packages路徑
pip -V

# 更新pip版本
python -m pip install --upgrade pip

# 從PiPy 或 --index 選項指定的網址上搜索名稱或摘要包含 <query> 的包
pip search
	-i, --index <url>
	# 默認為https://pypi.org/pypi

# 它會將環境的目前套件清單記錄到 requirements.txt
pip freeze > requirements.txt

# 將環境的目前套件清單記錄到 requirements.txt，且不會出現"@ file:///"格式
pip list --format=freeze > requirements.txt

    # 選擇輸出格式： --format
    # 列出最新的軟件包：-u,--uptodate
    # 列出過時的軟件包：-o,--outdated
    # 列出不依賴於其他包的包： --not-required

# 安裝套件時使用
pip install -r requirements.txt

# 安裝指定版本
pip install package==version

# 更新指定版本
pip install --upgrade package==version

# 移除安裝過的套件
pip uninstall package

# 更新套件
pip install -U package

# 只下載不安裝
pip download

# whereis - linux 查看指令路徑
whereis pip

# where - windows 查看指令路徑
where pip
```

# 無法連線pypi

[Python pip离线安装package方法总结（以TensorFlow为例）](https://imshuai.com/python-pip-install-package-offline-tensorflow?fbclid=IwAR3PzgsWlO36VkWjDr0UafrpuiyqL7l3D10XEK4lffQgllroZswA4DG4sfs)