# Docker 筆記

## 參考資料

[官方文檔](https://docs.docker.com/reference/)

[官方網站](https://docs.docker.com/)

[docker 指令(官方)](https://docs.docker.com/engine/reference/commandline/cli/)

[docker-compose 指令 (官方)](https://docs.docker.com/compose/reference/)

[docker Hub 官網](https://hub.docker.com/search?type=image&image_filter=store%2Cofficial)

---
## 安裝步驟 Docker 安裝 Docker-compose 安裝

[安裝官方文檔 右邊列表有其他系統的安裝步驟](https://docs.docker.com/engine/install/)

[CentOS 安裝 Docker 官方文檔](https://docs.docker.com/engine/install/centos/)


## 設置存儲庫
```bash
sudo yum install -y yum-utils
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

    # 可選：啟用夜間或測試存儲庫。

	# 這些存儲庫包含在docker.repo上面的文件中，但默認情況下是禁用的。
	# 您可以在穩定存儲庫旁邊啟用它們。
	# 啟用夜間存儲庫：
	sudo yum-config-manager --enable docker-ce-nightly

	# 啟用測試通道：
	sudo yum-config-manager --enable docker-ce-test

	# 您可以通過運行帶有標誌的命令來禁用夜間或測試存儲庫 。
	# 要重新啟用它，請使用該標誌。
	# 禁用夜間存儲庫：
	yum-config-manager--disable--enable


# 安裝最新版本的 Docker Engine 和 containerd，或者進入下一步安裝特定版本：
sudo yum install docker-ce docker-ce-cli containerd.io -y

# 啟動服務
systemctl start docker
# 查詢啟動狀態
systemctl status docker
# 重新啟動
systemctl restart docker
# 停止服務
systemctl stop docker
# 開機啟動
systemctl enable docker

# 通過運行hello-world 映像驗證 Docker Engine 是否已正確安裝。
sudo docker run hello-world

# 安裝Docker-Compose

	#下載 Docker Compose 的當前穩定版本
	sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

	# 對二進製文件應用可執行權限
	sudo chmod +x /usr/local/bin/docker-compose

	# 安裝 Docker Compose
	sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

## 指令 docker

```bash
# 顯示 docker 的資訊
docker info

# 顯示 docker 的版本
docker version

# 使用Dockerfile 建立印象檔
docker build
	-t <Docker image 名稱>
	--build-arg <變數1=... 變數2=...>

# 刪除 Docker image
docker rmi [Image ID]
	-f 強制

# 查看正在執行的containers
docker ps
	-a 所有容器,包含停止的

# 查看images id
docker images
	--all , -a		顯示所有圖像（默認隱藏中間圖像）
	--digests		顯示摘要
	--filter , -f		根據提供的條件過濾輸出
	--format		使用 Go 模板打印漂亮的圖像
	--no-trunc		不要截斷輸出
	--quiet , -q		僅顯示圖像 ID

# 停止 Container
docker stop [Container ID]

# 刪除 Container
docker rm [Container ID]

# 清除沒在使用的image
docker system prune
	-a 額外刪除任何已停止的容器和所有未使用的image


# 透過 iamge 執行並產生一個新的 container
docker run [OPTIONS] [Image 名稱]:[Image 版本] [執行指令]
	-i, --interactive 互動模式
	-t, --tty         配置一個終端機
	-d, --detach      在背景執行
	-p 				  本機端port:docker內部port
	--cpus=1.5        限制容器只能使用到 1.5 顆實體的 CPU。
	# 允許 Docker 容器使用 300m 的記憶體，以及 1g - 300m = 700m 的 swap 交換空間。
	--memory=300m     限制記憶體使用量
	--memory-swap=1g  限制swap交換空間

# 查看contianer的logs紀錄
docker logs -f --tail=20 [Container ID]
	--tail 顯示筆數
	-f

# 取得一個指定版本的image,如果不指定,預設版本則為latest
docker pull [Image 名稱]:[Image 版本]

# 查詢正在執行的 container
docker ps

# 進入 container
docker exec [OPTIONS] [Container ID] [執行指令]
docker exec -ti [Container ID] bash
	-i, --interactive 互動模式
	-t, --tty         配置一個終端機

# 將容器連接到網絡
docker network connect

# 創建一個網絡
docker network create

# 斷開容器與網絡的連接
docker network disconnect

# 顯示一個或多個網絡的詳細信息
docker network inspect

# 列出網絡
docker network ls

# 刪除所有未使用的網絡
docker network prune

# 移除一個或多個網絡
docker network rm
```

---
## 指令 docker-compose

[官方文檔](https://docs.docker.com/compose/samples-for-compose/)

```bash
# 建立並執行容器
docker-compose up -d

# 同一個容器執行多個 —scale !!!
docker-compose up -—scale %CONTAINER_NAME%=5 -d

# 根據yml檔案建立並執行容器 -f file.yml
docker-compose -f file.yml up -d

# 建立並執行容器並清除沒在檔案內的容器
	--remove-orphans
	# orphans(孤兒)
docker-compose up -d --remove-orphans

# 列出容器
docker-compose ps

# 執行容器
docker-compose start

# 重新執行容器
docker-compose start [Name]

# 停止容器
docker-compose stop

# 停止並刪除容器
docker-compose down

# 印出log
docker-compose logs

# 進入容器
docker-compose exec <service> bash  -f  docker exec -it <id or container-name> bash

# 要查看應用程序的輸出
docker logs $container_id
	# --tail 顯示筆數
	# -f
```

---
## 指令 docker hub

[docker Hub 官網](https://hub.docker.com/search?type=image&image_filter=store%2Cofficial)

```bash
# 登出(確保您已註銷並且不會引起任何衝突)
docker logout

# 標記 使用特定版本:名稱:1.0.0(版本),默認'latest'
docker tag <imageId> myusername/docker-whale

# 登入
docker login --username=myusername

# 重啟 容器
docker restart [OPTIONS] CONTAINER [CONTAINER...]

# push到DockerHub 使用特定版本:名稱:1.0.0(版本),默認'latest'
docker push myusername/docker-whale
```

---
# 範例 Dockerfile

[Dockerfile reference](https://docs.docker.com/engine/reference/builder/)

`檔名要是Dockerfile`

```dockerfile
# 使用官方的 Python 執行環境作為基本的 Docker 影像
FROM python:2.7-slim

# 設定工作目錄為 /app
WORKDIR /app

# 複製目前目錄下的內容，放進 Docker 容器中的 /app
ADD . /app

# 安裝 requirements.txt 中所列的必要套件
RUN pip install -r requirements.txt

# 讓 80 連接埠可以從 Docker 容器外部存取
EXPOSE 80

# 定義環境變數
ENV NAME World

# 當 Docker 容器啟動時，自動執行 app.py
CMD ["python", "app.py"]


FROM pytorch/pytorch:latest
RUN pip install Pillow
RUN pip install flask
WORKDIR /models
COPY ./script ./
EXPOSE 5002
ENTRYPOINT ["bash","run.sh"]


# From: 依據哪個Docker image，可以直接在Docker hub去搜尋已經被建立好的Docker Image，好處是可以節省自己建立環境、安裝套件的時間。
# RUN: 執行的指令，通常會在內部用 pip install 去安裝一些套件
# WORKDIR: 去到該資料夾，跟 RUN cd 不同的地方在於，RUN cd 只會在剛行程式碼去到指定位置，但下一行執行時，就又會回到原本的地方。
# COPY: 可以將本機端的資料，複製到 Docker 內部所指定的位置
# EXPOSE: 開啟的 Port
# ENTRYPOINT: Docker 建立好後，需要執行的指令
```

Python Dockerfile範例：

```dockerfile
FROM python:3.6.12-buster
MAINTAINER ex@mail.com
WORKDIR /usr/src/app
RUN apt-get update; exit 0
RUN apt-get install vim net-tools iftop -y
RUN apt-get install libmediainfo-dev -y

COPY requirements.txt /usr/src/app/requirements.txt
RUN pip3 install -r /usr/src/app/requirements.txt
RUN apt-get install ffmpeg -y

COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

ENTRYPOINT      ["/entrypoint.sh"]
```

---
# 範例 docker-compose

[Github 範例](https://github.com/docker/awesome-compose)

```yml
# 設定Container重新啟動的規則
# 目前支援no、always、on-failure、unless-stopped等四個選項
# 一般會設為「always」，即當Container停止或開機時可以自動啟動Container
restart: always

# 指定服務的映像檔名稱或映像檔ID
image: mysql:5.7

# 掛載目錄
volumes:
    # 使用絕對路徑
  - /opt/cache:/tmp/cache

    # 以 Compose 配置文件為中心的相對路徑
  - ./cache:/tmp/cache

    # ~/ 表示的是目錄
  - ~/cache://tmp/cache

# 容器
services:
    backend:
    image: awesome/database
    volumes:
        - db-data:/etc/data # 設定volume

    backup:
    image: backup-service
    volumes:
        - db-data:/var/lib/backup/data # 設定volume

volumes:
    db-data: # 共用db-data這個volume


# 定義容器內的環境變數，類似docker run -e的效果
environment:
  - MYSQL_USER=wordpress

# 設定容器間的依賴關係，即決定容器啟動的先後順序
depends_on: # 依賴 db 服務
    - db

# 用來定義本機與容器間Port的對映關係，基本的格式也是HOST:CONTAINER
ports:
    - "3000" # 不指定本機Port，本機會隨機自動產生
    - "45678:22" # 本機的45678 Port對映到容器的Port 22

# 開放port
expose:
    - 1234


# 容器設定networks
services:
  frontend: # 服務一
    image: awesome/webapp
    networks:
      - front-tier
      - back-tier

  backend: # 服務二
    image: awesome/backapp
    networks:
      - back-tier

# 定義網路
networks:
    front-tier:
    back-tier:

networks:
  app-network:
    driver: bridge
    	# 官方說明
    	# https://docs.docker.com/network/

    	# bridge:默認網絡驅動程序。橋接網絡。
    	host: Docker主機網路。

    		# 錯誤 解決方案
    		Docker ERROR: only one instance of "host" network is allowed
    		https://stackoverflow.com/questions/63777655/docker-error-only-one-instance-of-host-network-is-allowed
    		network_mode: host

# 使用預先存在的網絡
# 如果希望容器加入預先存在的網絡，請使用以下external選項：
services:
  # ...
networks:
  default:
    external:
      name: my-pre-existing-network

# overlay:將多個Docker守護進程連接在一起，使swarm服務能夠相互通信。
# ipvlan:完全控制IPv4和IPv6尋址。
# macvlan:將MAC地址分配給容器，網絡上顯示為物理設備。

# 預設Docker Compose會自動建立default network，其名稱為「目錄名稱_default」
# 換言之，networks是可以省略的
```

# 多個服務使用同資料 示例用例
當您有多個具有通用配置的服務時，擴展單個服務非常有用。
下面的示例是具有兩個服務的 Compose 應用程序：一個 Web 應用程序和一個隊列工作器。
這兩種服務使用相同的代碼庫並共享許多配置選項。

在common.yml 中，我們定義了通用配置：

```yml
services:
  app:
    build: .
    environment:
      CONFIG_FILE_PATH: /code/config
      API_KEY: xxxyyy
    cpu_shares: 5
```

在docker-compose.yml 中，我們定義了使用通用配置的具體服務：

```yml
services:
  webapp:
    extends:
      file: common.yml
      service: app
    command: /code/run_web_app
    ports:
      - 8080:8080
    expose:
      - "80"
    depends_on:
      - queue
      - db

  queue_worker:
    extends:
      file: common.yml
      service: app
    command: /code/run_worker
    depends_on:
      - queue
```

# 連線 主機別名

`IMAGE`://`DOCKER_IP`:`PORT`

```yml
version: "3.9"
services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres
    ports:
      - "8001:5432"
```