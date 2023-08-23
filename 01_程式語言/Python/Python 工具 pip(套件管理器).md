# Python 工具 pip(套件管理器)

## 目錄

- [Python 工具 pip(套件管理器)](#python-工具-pip套件管理器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [指令心得相關](#指令心得相關)
    - [狀況相關](#狀況相關)
- [安裝](#安裝)
- [指令](#指令)
- [狀況](#狀況)
  - [無法連線pypi](#無法連線pypi)
  - [錯誤訊息 It is a distutils installed project](#錯誤訊息-it-is-a-distutils-installed-project)

## 參考資料

[官方文檔網站](https://pip.pypa.io/en/stable/#)

[User Guide - 官方教學](http://pip.readthedocs.org/en/stable/user_guide/#configuration)

`需要在 pip.ini 檔案中指定預設安裝位置`

[官方 指令](https://pip.pypa.io/en/stable/cli/)

### 指令心得相關

[Pip freeze generating '@ file:///' on conda environment - 匯出requirement.txt格式](https://stackoverflow.com/questions/62863020/pip-freeze-generating-file-on-conda-environment)

[pip list](https://pip.pypa.io/en/stable/cli/pip_list/)

[pip freeze](https://pip.pypa.io/en/stable/cli/pip_freeze/)

### 狀況相關

[pip cannot uninstall <package>: "It is a distutils installed project"](https://stackoverflow.com/questions/53807511/pip-cannot-uninstall-package-it-is-a-distutils-installed-project)

[Python module not found even though "Requirement Already satisfied in Pip"](https://stackoverflow.com/questions/45345377/python-module-not-found-even-though-requirement-already-satisfied-in-pip)

# 安裝

```bash
### CentOS7 ###
yum update
yum install epel-release
# Python2
yum –y install python2-pip
# Python3
yum –y install python3-pip

### Ubuntu ###
# Python3
apt update
apt install python3-pip
# Python2 使用 get-pip.py 腳本
apt update
# 使用 curl 下載 get-pip.py 腳本
curl https://bootstrap.pypa.io/pip/2.7/get-pip.py --output get-pip.py
# 使用 python2 運行腳本以安裝 pip
python get-pip.py
# 確認是否安裝
pip2 --version
```

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

# 若遇到錯誤 忽略錯誤繼續安裝下一個
while read requirement; do pip install $requirement || true; done < requirements.txt


# 安裝指定版本
pip install $package==version

# 查看package可安裝版本
pip install $package==

# 更新指定版本
pip install --upgrade $package==version
pip install -U $package==version

# 移除安裝過的套件
pip uninstall $package

# 更新套件
pip install -U $package

# 只下載不安裝
pip download

# whereis - linux 查看指令路徑
whereis pip

# where - windows 查看指令路徑
where pip
```

# 狀況

## 無法連線pypi

[Python pip离线安装package方法总结（以TensorFlow为例）](https://imshuai.com/python-pip-install-package-offline-tensorflow?fbclid=IwAR3PzgsWlO36VkWjDr0UafrpuiyqL7l3D10XEK4lffQgllroZswA4DG4sfs)

## 錯誤訊息 It is a distutils installed project

```
Most probably, you have installed this package via your OS' package manager, so you need to use that rather than pip to update or remove it, too.



已使用其他套管理工具下載過

使用 --ignore-installed 選項可能遇到風險
It may work (potentially for a long enough time for your business needs), but may just as well break things on the system in unpredictable ways. One thing is sure: it makes the system's configuration unsupported and thus unmaintainable -- because you have essentially overwritten files from your distribution with some other arbitrary stuff. E.g.:

	If the new files are binary incompatible with the old ones, other software from the distribution built to link against the originals will segfault or otherwise malfunction.

	If the new version has a different set of files, you'll end up with a mix of old and new files which may break dependent software as well as the package itself.

	If you change the package with your OS' package manager later, it will overwrite pip-installed files, with similarly unpredictable results.

	If there are things like configuration files, differences in them between the versions can also lead to all sorts of breakage.
```

```bash
# 强制重新安装
pip install --ignore-installed $package
```