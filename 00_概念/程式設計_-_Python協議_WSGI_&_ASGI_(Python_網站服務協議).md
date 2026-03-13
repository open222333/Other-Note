# 程式設計 - Python協議 WSGI & ASGI (Python 網站服務協議)

```
定義的Web伺服器和Web應用程式或框架之間的一種簡單而通用的介面

WSGI(Web Server Gateway Interface) 是一種協議，或是叫做規範。
Web伺服器閘道介面（Python Web Server Gateway Interface，縮寫為WSGI）是為Python語言定義的Web伺服器和Web應用程式或框架之間的一種簡單而通用的介面。
自從WSGI被開發出來以後，許多其它語言中也出現了類似介面。

ASGI
異步服務器網關接口( ASGI) 是Web 服務器將請求轉發到支持異步的Python編程語言框架和應用程序的調用約定。它被構建為Web 服務器網關接口(WSGI) 的後繼者。
WSGI為同步Python應用程序提供了一個標準，而 ASGI 為異步和同步應用程序提供了一個標準，具有WSGI向後兼容的實現以及多個服務器和應用程序框架。
```

## 目錄

- [程式設計 - Python協議 WSGI \& ASGI (Python 網站服務協議)](#程式設計---python協議-wsgi--asgi-python-網站服務協議)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [WSGI 概念](#wsgi-概念)
- [ASGI 概念](#asgi-概念)
- [WSGI ASGI 比較](#wsgi-asgi-比較)
- [託管選項](#託管選項)
- [自主部署選項](#自主部署選項)

## 參考資料

[WSGI Python Web Server Gateway Interface(Web伺服器閘道器介面) - wiki](https://zh.wikipedia.org/zh-tw/Web%E6%9C%8D%E5%8A%A1%E5%99%A8%E7%BD%91%E5%85%B3%E6%8E%A5%E5%8F%A3)

[ASGI Asynchronous Server Gateway Interface(異步服務器網關接口) - wiki](https://en.wikipedia.org/wiki/Asynchronous_Server_Gateway_Interface)

[什麼是 WSGI & ASGI ?](https://medium.com/@eric248655665/%E4%BB%80%E9%BA%BC%E6%98%AF-wsgi-%E7%82%BA%E4%BB%80%E9%BA%BC%E8%A6%81%E7%94%A8-wsgi-f0d5f3001652)

[WSGI & ASGI](https://quietbo.com/2022/05/08/wsgi-asgi/)

[WSGI 規範內容與說明](https://wsgi.readthedocs.io/en/latest/index.html)

[ASGI Documentation 文檔](https://asgi.readthedocs.io/en/latest/)

# WSGI 概念

```
WSGI 是 Web Server Gateway Interface 的縮寫，是一種協議，或是叫做規範

WSGI server 意思指的是遵照 WSGI 規範的 server

WSGI 提供的是一套規範，提供 web server 與 python web application 之間的一個接口，只要符合這項規範的任何一個 web server，都能與符合規範的 application 進行串接
```

# ASGI 概念

```
Asynchronous Server Gateway Interface(異步服務器網關接口)

ASGI 是 WSGI 的繼承者，已經存在的WSGI應用可以直接在ASGI服務器中運行

ASGI 代表異步服務器網關接口，是爲異步、同步應用程序提供標準，支持 WSGI 不支持當前 web 開發中的一些新的協議標準

介於網絡協議服務和Python應用之間的標準接口，能夠處理多種通用的協議類型，包括HTTP，HTTP2和WebSocket
```

# WSGI ASGI 比較

```
相同之處:
	WSGI 與 ASGI都指定接口並位於 Web 服務器和 Python Web 應用程序或框架之間。
兩者的區別:
	WSGI是基於HTTP協議模式的，不支持WebSocket
	ASGI的誕生則是為了解決Python常用的WSGI不支持當前Web開發中的一些新的協議標準。
	ASGI對於WSGI原有的模式的支持和WebSocket的擴展，即ASGI是WSGI的擴展。
```

# 託管選項

* Heroku
* Google App Engine
* AWS Elastic Beanstalk
* Azure (IIS)
* PythonAnywhere

# 自主部署選項

* 獨立WSGI容器
    1. Gunicorn
    2. uWSGI
    3. Gevent
    4. Twisted Web
    5. 代理設置
* uWSGI
	1. 使用uwsgi啟動你的應用
	2. 配置nginx
* mod_wsgi (Apache)
	1. 安裝mod_wsgi
	2. 創建一個.wsgi文件
	3. 配置Apache
	4. 故障排除
	5. 支持自動重載
	6. 使用虛擬環境
* FastCGI
	1. 創建一個.fcgi文件
	2. 配置Apache
	3. 配置lighttpd
	4. 配置nginx
* CGI
	1. 創建一個.cgi文件
	2. 服務器設置
