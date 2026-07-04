# Logstash 插件 Filebeat

```
Filebeat 是 Elastic Stack 的一部分，用於收集、轉送和聚合日誌數據。
需要在每個希望收集日誌數據的伺服器上安裝 Filebeat。
```

## 目錄

- [Logstash 插件 Filebeat](#logstash-插件-filebeat)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)
- [範例](#範例)
  - [配置多個輸入，並為每個輸入添加標籤，以區分不同的資料表數據](#配置多個輸入並為每個輸入添加標籤以區分不同的資料表數據)

## 參考資料

[Filebeat 官方文檔](https://www.elastic.co/guide/en/beats/filebeat/current/index.html)

# 安裝

## Debian (Ubuntu)

添加 Elastic APT 存儲庫 下載並安裝 Elastic 公鑰：

```bash
wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
```

將 Elastic APT 存儲庫添加到 sources.list.d 目錄中

```bash
sh -c 'echo "deb https://artifacts.elastic.co/packages/7.x/apt stable main" > /etc/apt/sources.list.d/elastic-7.x.list'
```

更新包索引並安裝 Filebeat

```bash
apt-get update
apt-get install filebeat
```

## 配置文檔

通常在 `/etc/filebeat/filebeat.yml`

### 基本範例

```yml
filebeat.inputs:
- type: log
  enabled: true
  paths:
    - /path/to/your/logfile.log
  tags: ["table1"]

- type: log
  enabled: true
  paths:
    - /another/path/to/your/logfile.log
  tags: ["table2"]

output.logstash:
  hosts: ["localhost:5044"]
```

# 指令

使用 Filebeat 自帶的測試命令來驗證配置是否正確

```bash
filebeat test config
```

## 服務操作

```bash
# 啟動服務
systemctl start filebeat

# 查詢啟動狀態
systemctl status filebeat

# 重新啟動
systemctl restart filebeat

# 停止服務
systemctl stop filebeat

# 開啟開機自動啟動
systemctl enable filebeat

# 關閉開機自動啟動
systemctl disable filebeat
```

# 範例

## 配置多個輸入，並為每個輸入添加標籤，以區分不同的資料表數據

```conf
filebeat.inputs:
- type: log
  paths:
    - /path/to/table1.log
  tags: ["table1"]

- type: log
  paths:
    - /path/to/table2.log
  tags: ["table2"]

output.logstash:
  hosts: ["localhost:5044"]
```

```conf
input {
  beats {
    port => 5044
  }
}

filter {
  if "table1" in [tags] {
    mutate {
      add_field => { "table" => "table1" }
    }
  }

  if "table2" in [tags] {
    mutate {
      add_field => { "table" => "table2" }
    }
  }
}

output {
  if "table1" in [tags] {
    elasticsearch {
      hosts => ["localhost:9200"]
      index => "prefix_table1-%{+YYYY.MM.dd}"
      document_id => "%{id}"
    }
  }

  if "table2" in [tags] {
    elasticsearch {
      hosts => ["localhost:9200"]
      index => "prefix_table2-%{+YYYY.MM.dd}"
      document_id => "%{id}"
    }
  }

  stdout {
    codec => rubydebug
  }
}
```

依序啟動

```bash
filebeat -e -c /path/to/filebeat.yml
logstash -f /path/to/logstash.conf
```
