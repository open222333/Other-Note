# WebServer(網站伺服器) 筆記


## 託管選項

* Heroku
* Google App Engine
* AWS Elastic Beanstalk
* Azure (IIS)
* PythonAnywhere

## 自主部署選項

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
