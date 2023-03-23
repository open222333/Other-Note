# AWS 安裝CloudWatch

```
Amazon CloudWatch 是針對 DevOps 工程師、開發人員、網路可靠性工程師 (SRE)、IT 管理員和產品擁有者建置的監控和可觀察性服務。

提供資料和可行的洞見以監控應用程式、回應整個系統的效能變化、最佳化資源使用情況。

以日誌、指標和事件的形式收集監控和操作資料。

可以統一檢視營運運作狀態，並全面了解在 AWS 上和內部部署執行的 AWS 資源、應用程式和服務。

可以使用 CloudWatch 來偵測環境中的異常行為、設定警示、將日誌和指標並列展示、採取自動動作、對問題進行故障排除，還有探索洞見以確保順暢執行。
```

## 目錄

- [AWS 安裝CloudWatch](#aws-安裝cloudwatch)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝 AWS CloudWatch 代理程式](#安裝-aws-cloudwatch-代理程式)
	- [`CentOS`](#centos)
	- [`Ubuntu`](#ubuntu)
	- [建立 CloudWatch 代理程式組態檔案](#建立-cloudwatch-代理程式組態檔案)
- [用法](#用法)

## 參考資料

[安裝 CloudWatch 代理程式](https://docs.aws.amazon.com/zh_tw/AmazonCloudWatch/latest/monitoring/install-CloudWatch-Agent-on-EC2-Instance.html)

[對 CloudWatch 進行故障診斷agent](https://docs.aws.amazon.com/zh_tw/AmazonCloudWatch/latest/monitoring/troubleshooting-CloudWatch-Agent.html)

[Amazon CloudWatch](https://aws.amazon.com/tw/cloudwatch/)

# 安裝 AWS CloudWatch 代理程式

## `CentOS`

```bash
### 安裝AWS CloudWatch代理程式
wget https://s3.amazonaws.com/amazoncloudwatch-agent/centos/amd64/latest/amazon-cloudwatch-agent.rpm

sudo rpm -U ./amazon-cloudwatch-agent.rpm

### 安裝AWS CLI
yum install unzip -y

curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"

unzip awscliv2.zip

sudo ./aws/install

# 確認安裝
aws --version

### 建立 AmazonCloudWatchAgent 代理程式的 CloudWatch 設定檔
aws configure --profile AmazonCloudWatchAgent

ID:{$aws_monitor_id}
KEY:{$aws_monitor_key}
region:ap-northeast-1
output format:json

```

## `Ubuntu`

```bash
### 安裝AWS CloudWatch代理程式
wget https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb

dpkg -i -E ./amazon-cloudwatch-agent.deb

### 安裝AWS CLI
apt-get install unzip -y

curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"

unzip awscliv2.zip

sudo ./aws/install

# 確認安裝
aws --version

### 建立 AmazonCloudWatchAgent 代理程式的 CloudWatch 設定檔
aws configure --profile AmazonCloudWatchAgent

ID:{$aws_monitor_id}
KEY:{$aws_monitor_key}
region:ap-northeast-1
output format:json
```


## 建立 CloudWatch 代理程式組態檔案

```bash
cd /opt/aws/amazon-cloudwatch-agent/bin
vi config.json
```

`需根據需求 修改 config.json 內容`

```json
{
  "agent": {
    "metrics_collection_interval": 60,
    "run_as_user": "root"
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [{
          "file_path": "/var/log/aws",
          "log_group_name": "aws",
          "log_stream_name": "{機器的名稱}"
        }]
      }
    }
  },
  "metrics": {
    "namespace": "{機器的名稱}",
    "metrics_collected": {
      "cpu": {
        "measurement": [
          "cpu_usage_user"
        ],
        "metrics_collection_interval": 60,
        "totalcpu": true
      },
      "mem": {
        "measurement": [
          "mem_used_percent"
        ],
        "metrics_collection_interval": 60
      },
      "disk": {
        "measurement": [
          "disk_used_percent"
        ],
        "metrics_collection_interval": 60,
        "resources": [
          "/"
        ]
      },
      "diskio": {
        "measurement": [
          "diskio_reads",
          "diskio_writes"
        ],
        "metrics_collection_interval": 60,
        "resources": [
          "sda"
        ]
      },
      "net": {
        "measurement": [
          "net_bytes_sent",
          "net_bytes_recv"
        ],
        "metrics_collection_interval": 60,
        "resources": [
          "eth0"
        ]
      }
    }
  }
}
```

# 用法

```bash
# 啟動 CloudWatch 代理程式
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m onPremise -s -c file:./config.json

# 關掉 CloudWatch 監控
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a stop

# 查詢本機 CloudWatch 代理程式的狀態
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a status
```