# JavaScript Node 工具 npm(軟體套件管理系統)

```
npm是Node.js預設的、用JavaScript編寫的軟體套件管理系統。
```

## 目錄

- [JavaScript Node 工具 npm(軟體套件管理系統)](#javascript-node-工具-npm軟體套件管理系統)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [](#)
- [配置](#配置)
- [指令](#指令)
- [例外狀況](#例外狀況)
  - [node: /lib64/libm.so.6: version \`GLIBC\_2.27' not found](#node-lib64libmso6-version-glibc_227-not-found)

## 參考資料

[npm 官網](https://www.npmjs.com/)

[npm 中文官網](https://www.npmjs.cn/)

[npm 文檔](https://docs.npmjs.com/)

[npm cli 指令列表](https://docs.npmjs.com/cli/v8/commands)

###

[node: /lib64/libm.so.6: version `GLIBC_2.27' not found ](https://www.cnblogs.com/dingshaohua/p/17103654.html)

# 配置

`package.json`

```
dependencies: 執行環境會需要
devDependencies: 開發或測試環境需要
optionalDependencies: 不一定在每個環境都能夠裝起來
```

# 指令

```bash
# 查看版本
npm -v

# 更新npm版本
npm install -g npm
	-g
		全局安裝 自動加入PATH
    --save 安裝套件時，也同步更新 package.json 裡的資訊。

# 生成package.json 會詢問問題, 創建一個 package.json 文件
npm init
	--yes
		跳過問題生成默認package.json

npm init -y

# 下載 dependencies(依賴) 根據 package.json
npm install $package@$version
	-g
		全局安裝 自動加入PATH
	--save
		安裝並加入 dependencies(package.json)
	--save-dev
		安裝並加入 devDependencies(package.json)
	-O
		安裝並加入 optionalDependencies

# 更新套件
npm update

# 列出所有已安裝的套件
npm list

# 解除安裝
npm uninstall example-package --save
    -g
		全局安裝
	--save
		安裝並加入 dependencies(package.json)
	--save-dev
		安裝並加入 devDependencies
```

# 例外狀況

## node: /lib64/libm.so.6: version `GLIBC_2.27' not found

```
npm -v
node: /lib64/libm.so.6: version `GLIBC_2.27' not found (required by node)
node: /lib64/libc.so.6: version `GLIBC_2.25' not found (required by node)
node: /lib64/libc.so.6: version `GLIBC_2.28' not found (required by node)
node: /lib64/libstdc++.so.6: version `CXXABI_1.3.9' not found (required by node)
node: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.20' not found (required by node)
node: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.21' not found (required by node)
```

```bash
# 安裝所需的glibc-2.28
wget http://ftp.gnu.org/gnu/glibc/glibc-2.28.tar.gz
tar xf glibc-2.28.tar.gz
cd glibc-2.28/ && mkdir build  && cd build
../configure --prefix=/usr --disable-profile --enable-add-ons --with-headers=/usr/include --with-binutils=/usr/bin

# configure: error:
# *** These critical programs are missing or too old: make bison compiler
# *** Check the INSTALL file for required versions.
# 升級gcc與make
# 升级GCC(默认为4 升级为8)
yum install -y centos-release-scl
yum install -y devtoolset-8-gcc*
mv /usr/bin/gcc /usr/bin/gcc-4.8.5
ln -s /opt/rh/devtoolset-8/root/bin/gcc /usr/bin/gcc
mv /usr/bin/g++ /usr/bin/g++-4.8.5
ln -s /opt/rh/devtoolset-8/root/bin/g++ /usr/bin/g++

# 升级 make(默认为3 升级为4)
wget http://ftp.gnu.org/gnu/make/make-4.3.tar.gz
tar -xzvf make-4.3.tar.gz && cd make-4.3/
./configure  --prefix=/usr/local/make
make && make install
cd /usr/bin/ && mv make make.bak
ln -sv /usr/local/make/bin/make /usr/bin/make
```