# MongoDB 工具 Compass(可視化管理工具)

```
連接到MongoDB： 在啟動MongoDB Compass後，你可以通過提供MongoDB的連接字串或直接選擇本地或遠程數據庫，輕鬆連接到MongoDB伺服器。
可視化查詢： MongoDB Compass允許你使用直觀的可視化工具來構建和執行查詢。你可以使用擴展的查詢過濾器，輕鬆地構建複雜的查詢條件。
實時數據探索： 透過MongoDB Compass，你可以瀏覽和探索數據，並在實時中查看更新。這使得你能夠更好地理解數據庫中的內容。
索引管理： Compass允許你查看、創建和刪除索引。它還提供了分析工具，以幫助你優化數據庫性能。
地理空間查詢： 如果你的數據包含地理空間信息，Compass提供了地理空間查詢的功能，讓你可以輕鬆地進行地理空間數據的查詢和分析。
自動完成功能： 在輸入命令時，Compass提供自動完成功能，以幫助你更快速地輸入合法的命令。
性能分析： Compass提供性能分析工具，讓你可以監視數據庫的性能並識別可能的優化機會。
導出和導入數據： 你可以使用Compass將數據導出為JSON或CSV文件，同時也可以導入數據到MongoDB數據庫。
集群支持： 如果你的MongoDB是一個集群（如複本集或分片集群），Compass也提供了相應的支持，讓你能夠輕鬆地管理集群。
```

## 目錄

- [MongoDB 工具 Compass(可視化管理工具)](#mongodb-工具-compass可視化管理工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
    - [MongoDB Compass配置文件的YAML格式](#mongodb-compass配置文件的yaml格式)
    - [MongoDB Compass配置文件的JSON格式](#mongodb-compass配置文件的json格式)
    - [環境變數](#環境變數)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[What is MongoDB Compass?](https://www.mongodb.com/docs/compass/current/#what-is-mongodb-compass-)

### 心得相關

# 安裝

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
```

## Docker 部署

```yml
version: '3'
services:
  mongo:
    image: mongo
    container_name: mongo
    ports:
      - 27017:27017
    volumes:
      - ./data/mongo:/data/db
  compass:
    image: mongo/mongosh:latest
    container_name: mongodb-compass
    restart: always
    volumes:
        # 持久化數據： 這個映射允許MongoDB Compass容器內的數據保存在主機的 ~/.mongodb-compass 目錄中，使得數據在容器重啟後仍然保留。
        # 這對於保存用戶配置、歷史查詢等數據是非常有用的。

        # 設定文件和數據保存： MongoDB Compass通常將用戶的設定文件、連接歷史和其他數據保存在 /root/.mongodb-compass 目錄中。
        # 通過這個映射，這些數據將被保存在主機上的 ~/.mongodb-compass 目錄中。

        # 這樣的映射使得 MongoDB Compass 在 Docker 容器中運行時能夠與主機上的相應目錄進行數據共享和持久化。
        # 這樣的做法是為了保證 MongoDB Compass 的配置和數據可以在容器停止和啟動時保持不變。
      - ~/.mongodb-compass:/root/.mongodb-compass
    command: mongosh --host mongo --port 27017 --username YOUR_MONGODB_USERNAME --password YOUR_MONGODB_PASSWORD --authenticationDatabase admin --db YOUR_MONGODB_DATABASE
```

### MongoDB Compass配置文件的YAML格式

```yml
# compass-config.yml
hosts:
  - name: "mongo"
    connectionInfo:
      port: 27017
      sslMethod: "disabled"
      authentication:
        mechanism: "SCRAM-SHA-1"
        username: "YOUR_MONGODB_USERNAME"
        password: "YOUR_MONGODB_PASSWORD"
        source: "admin"
        isAtlas: false
      readonly: false
  - name: "YOUR_MONGODB_DATABASE"
    collections: []
```

```yml
version: '3'
services:
  mongo:
    image: mongo
    container_name: mongo
    ports:
      - 27017:27017
    volumes:
      - ./data/mongo:/data/db
  compass:
    image: mongo/mongosh:latest
    container_name: mongodb-compass
    restart: always
    volumes:
      - ~/.mongodb-compass:/root/.mongodb-compass
      - ./compass-config.yml:/etc/mongosh/compass-config.yml
    command: mongosh --config /etc/mongosh/compass-config.yml
```

### MongoDB Compass配置文件的JSON格式

```json
{
  "hosts": [
    {
      "name": "localhost",
      "connectionInfo": {
        "port": 27017,
        "sslMethod": "disabled",
        "authentication": {
          "mechanism": "SCRAM-SHA-1",
          "username": "your_username",
          "password": "your_password",
          "source": "admin",
          "isAtlas": false
        },
        "readonly": false
      }
    }
  ],
  "readonly": false,
  "showAutoIndex": false,
  "theme": "auto",
  "showDB": true,
  "exportPath": ".",
  "keepKeyOrder": false,
  "isAtlas": false,
  "showWelcomeTour": false
}
```

```yml
version: '3'
services:
  mongo:
    image: mongo
    container_name: mongo
    ports:
      - 27017:27017
    volumes:
      - ./data/mongo:/data/db

  compass:
    image: mongo/mongosh:latest
    container_name: mongodb-compass
    restart: always
    volumes:
      - ~/.mongodb-compass:/root/.mongodb-compass
      - ./compass-config.json:/etc/mongosh/compass-config.json
    command: mongosh --config /etc/mongosh/compass-config.json
    networks:
      - mongodb-network

networks:
  mongodb-network:
    driver: bridge
```

### 環境變數

```yml
version: '3'
services:
  mongo:
    image: mongo
    container_name: mongo
    ports:
      - 27017:27017
    volumes:
      - ./data/mongo:/data/db

  compass:
    image: mongo/mongosh:latest
    container_name: mongodb-compass
    restart: always
    environment:
      - MONGODB_COMPASS_CONFIG=/etc/mongosh/compass-config.json
      - MONGODB_USERNAME=your_username
      - MONGODB_PASSWORD=your_password
    networks:
      - mongodb-network
    command: mongosh --config $MONGODB_COMPASS_CONFIG --username $MONGODB_USERNAME --password $MONGODB_PASSWORD

networks:
  mongodb-network:
    driver: bridge
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令
