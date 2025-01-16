# Linux Ubuntu 筆記

```
apt-get(軟體套件管理工具)
```

## 目錄

- [Linux Ubuntu 筆記](#linux-ubuntu-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [apt apt-get](#apt-apt-get)

## 參考資料

[官方中文站](https://www.ubuntu-tw.org/modules/tinyd0/)

# apt apt-get

```
特點	apt-get	apt
首次出現	1998 年，作為 Debian 的軟體包管理工具	2014 年，從 Debian/Ubuntu 衍生而來
使用目的	用於腳本和進階使用者，功能更專業和全面	為一般使用者設計，簡化操作並改進介面
指令格式	偏向技術性，功能分散在多個工具中	整合多種功能，更直觀且易用
互動性	沒有美化的進度條和提示	有進度條和更清晰的輸出
支援的操作	支援的操作更細化，比如 dist-upgrade	整合了 apt-get 和 apt-cache 的功能
```

```
操作	apt-get	apt
更新軟體來源庫	 apt-get update	 apt update
升級已安裝的套件	 apt-get upgrade	 apt upgrade
安裝軟體包	 apt-get install <pkg>	 apt install <pkg>
移除軟體包	 apt-get remove <pkg>	 apt remove <pkg>
自動移除多餘套件	 apt-get autoremove	 apt autoremove
查詢軟體包信息	apt-cache show <pkg>	apt show <pkg>
```

推薦使用情境

apt

```
適合日常使用。
當需要執行常見操作（如安裝、移除、升級）時，apt 更簡單直觀。
```

apt-get

```
適合腳本編寫和進階使用者。
當需要更細粒度的控制（如特定版本的安裝、降級）時，apt-get 更靈活。
```