# MySQL 工具 Orchestrator(HA-高可用 工具)

```
Orchestrator 是一個用於 MySQL 高可用性和自動故障恢復的開源工具。
它的目標是簡化 MySQL 複製拓撲結構的管理和監控，以便在主節點故障時自動執行切換。

以下是 Orchestrator 的一些主要特點和功能：

拓撲發現： Orchestrator 可以自動發現 MySQL 複製拓撲結構，並提供實時的拓撲圖，顯示主從關係和複製延遲。
自動故障切換： 當主節點發生故障時，Orchestrator 可以自動執行故障切換，將一個從節點提升為新的主節點，並更新其他節點的複製關係。
手動故障切換： 除了自動故障切換外，Orchestrator 還提供了手動切換的選項，允許管理員手動觸發故障切換。
拓撲變更計劃： Orchestrator 可以幫助規劃拓撲的變更，例如添加或刪除節點，以確保變更不會引起服務中斷。
可視化界面： Orchestrator 提供一個直觀的 Web 界面，方便管理員查看和管理 MySQL 拓撲結構。
事件通知： Orchestrator 可以配置為通過 Webhook 或其他方式發送事件通知，以便管理員及時了解拓撲變更和故障切換情況。
集成 API： Orchestrator 提供 REST API，允許與其他工具和自動化系統進行集成。

請注意，Orchestrator 主要用於管理 MySQL 複製拓撲，而不是 MySQL Cluster。
如果使用 MySQL Cluster，可能需要考慮其他工具或方法。
請確保查閱 Orchestrator 的官方文檔以獲取詳細的配置和使用信息。
```

## 目錄

- [MySQL 工具 Orchestrator(HA-高可用 工具)](#mysql-工具-orchestratorha-高可用-工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [安裝步驟](#安裝步驟)
  - [Docker 部署](#docker-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
    - [官方範例 生產配置文件](#官方範例-生產配置文件)
- [指令](#指令)

## 參考資料

[Orchestrator Github](https://github.com/openark/orchestrator)

[Orchestrator 文檔](https://github.com/openark/orchestrator/blob/master/docs/README.md)

[Orchestrator 參數](https://github.com/openark/orchestrator/blob/master/go/config/config.go)

[Orchestrator 配置](https://github.com/openark/orchestrator/blob/master/docs/configuration.md)

[Orchestrator 設定範例文件](https://github.com/github/orchestrator/blob/master/docs/configuration-sample.md)

[Orchestrator 使用 docker 部署 便捷腳本](https://github.com/openark/orchestrator/blob/master/docs/docker.md)

### 心得相關

[【DB宝40】MySQL高可用管理工具Orchestrator简介及测试](https://cloud.tencent.com/developer/article/1820553)

# 安裝

## 安裝步驟

`安裝 Go`

[Go(Golang) 筆記](../../../01_程式語言/Golang/Go(Golang)%20筆記.md)

`使用 go get 安裝`

```bash
go get -u github.com/openark/orchestrator/...

# go get: 是 Go 語言工具的一部分，用於下載、編譯並安裝 Go 程序包。
# -u: 是 go get 的一個選項，表示 "update"，它告訴 Go 工具更新已安裝的包到最新版本。
# github.com/openark/orchestrator/...: 是要下載的 Go 程序包的完整 import 路徑。/... 表示下載指定程序包以及它的所有子包。
```

## Docker 部署

```bash
git clone https://github.com/openark/orchestrator.git
```

```yml
version: '3'
services:
  orchestrator:
    image: openark/orchestrator
    ports:
      - "3000:3000"
    volumes:
      - /path/to/orchestrator/conf.json:/etc/orchestrator.conf.json
```

## 配置文檔

通常在 `/etc/orchestrator.conf.json`

### 基本範例

```json
{
  "listenAddress": "0.0.0.0:3000",
  "db": {
    "host": "mysql-host",
    "port": 3306,
    "user": "orchestrator",
    "password": "your_password",
    "database": "orchestrator"
  },
  "mysqlTopologyCredentials": [
    {
      "user": "orchestrator",
      "password": "your_password",
      "host": "mysql-host",
      "port": 3306
    }
  ]
}
```

### 官方範例 生產配置文件

```
Debug： 啟用或禁用除錯模式，如果設置為 true，將會輸出更多調試信息。
ListenAddress： Orchestrator 的 Web 界面監聽的地址和端口。
MySQLTopologyCredentialsConfigFile： MySQL 拓撲資料庫的設定文件路徑。
MySQLTopologyMaxPoolConnections： MySQL 拓撲資料庫的最大連接數。
MySQLOrchestratorHost、MySQLOrchestratorPort、MySQLOrchestratorDatabase： Orchestrator 使用的 MySQL 資料庫的連接設定。
InstancePollSeconds： Orchestrator 刷新 MySQL 拓撲的時間間隔。
MySQLConnectTimeoutSeconds： MySQL 連接超時時間。
DiscoveryIgnoreReplicaHostnameFilters： 忽略特定主機名的拓撲發現。
ActiveNodeExpireSeconds： 活動節點過期時間。
ReadOnly： 將 Orchestrator 設置為只讀模式。
AuthenticationMethod、HTTPAuthUser、HTTPAuthPassword： Web 界面的身份驗證相關設定。
PowerAuthUsers： 具有特權的使用者列表。
ClusterNameToAlias： 將集群名稱映射為別名。
ServeAgentsHttp： 啟用或禁用 Orchestrator 提供代理 HTTP 服務的功能。
UseSSL、SSLSkipVerify、SSLPrivateKeyFile、SSLCertFile、SSLCAFile： 使用 SSL 加密連接的相關設定。
AccessTokenUseExpirySeconds、AccessTokenExpiryMinutes： 存取令牌的過期時間。
DetectClusterAliasQuery： 檢測集群別名的查詢。
DetectDataCenterQuery： 檢測數據中心的查詢。
PromotionIgnoreHostnameFilters： 在升主過程中忽略的主機名。
PreventCrossDataCenterMasterFailover、PreventCrossRegionMasterFailover： 防止跨數據中心或跨區域升主。
RaftEnabled、RaftBind、RaftDataDir、DefaultRaftPort： Raft 協議相關設定。
ConsulAddress、RaftNodes： Consul 和 Raft 協議相關設定。
```

```json
{
  "Debug": true,
  "EnableSyslog": false,
  "ListenAddress": ":3000",
  "MySQLTopologyCredentialsConfigFile": "/etc/mysql/orchestrator.cnf",
  "MySQLTopologySSLPrivateKeyFile": "",
  "MySQLTopologySSLCertFile": "",
  "MySQLTopologySSLCAFile": "",
  "MySQLTopologySSLSkipVerify": true,
  "MySQLTopologyUseMutualTLS": false,
  "MySQLTopologyMaxPoolConnections": 3,
  "MySQLOrchestratorHost": "127.0.0.1",
  "MySQLOrchestratorPort": 3306,
  "MySQLOrchestratorDatabase": "orchestrator",
  "MySQLOrchestratorCredentialsConfigFile": "/etc/mysql/orchestrator_srv.cnf",
  "MySQLOrchestratorSSLPrivateKeyFile": "",
  "MySQLOrchestratorSSLCertFile": "",
  "MySQLOrchestratorSSLCAFile": "",
  "MySQLOrchestratorSSLSkipVerify": true,
  "MySQLOrchestratorUseMutualTLS": false,
  "MySQLConnectTimeoutSeconds": 1,
  "DefaultInstancePort": 3306,
  "ReplicationLagQuery": "select round(absolute_lag) from meta.heartbeat_view",
  "SlaveStartPostWaitMilliseconds": 1000,
  "DiscoverByShowSlaveHosts": false,
  "InstancePollSeconds": 5,
  "DiscoveryIgnoreReplicaHostnameFilters": [
    "a_host_i_want_to_ignore[.]example[.]com",
    ".*[.]ignore_all_hosts_from_this_domain[.]example[.]com",
    "a_host_with_extra_port_i_want_to_ignore[.]example[.]com:3307"
  ],
  "ReadLongRunningQueries": false,
  "SkipMaxScaleCheck": true,
  "BinlogFileHistoryDays": 10,
  "UnseenInstanceForgetHours": 240,
  "SnapshotTopologiesIntervalHours": 0,
  "InstanceBulkOperationsWaitTimeoutSeconds": 10,
  "ActiveNodeExpireSeconds": 5,
  "HostnameResolveMethod": "default",
  "MySQLHostnameResolveMethod": "@@hostname",
  "SkipBinlogServerUnresolveCheck": true,
  "ExpiryHostnameResolvesMinutes": 60,
  "RejectHostnameResolvePattern": "",
  "ReasonableReplicationLagSeconds": 10,
  "ProblemIgnoreHostnameFilters": [

  ],
  "VerifyReplicationFilters": false,
  "MaintenanceOwner": "orchestrator",
  "ReasonableMaintenanceReplicationLagSeconds": 20,
  "MaintenanceExpireMinutes": 10,
  "MaintenancePurgeDays": 365,
  "CandidateInstanceExpireMinutes": 60,
  "AuditLogFile": "",
  "AuditToSyslog": false,
  "AuditPageSize": 20,
  "AuditPurgeDays": 365,
  "RemoveTextFromHostnameDisplay": ":3306",
  "ReadOnly": false,
  "AuthenticationMethod": "",
  "HTTPAuthUser": "",
  "HTTPAuthPassword": "",
  "AuthUserHeader": "",
  "PowerAuthUsers": [
    "*"
  ],
  "ClusterNameToAlias": {
    "127.0.0.1": "test suite"
  },
  "AccessTokenUseExpirySeconds": 60,
  "AccessTokenExpiryMinutes": 1440,
  "DetectClusterAliasQuery": "select ifnull(max(cluster_name), '') as cluster_alias from meta.cluster where anchor=1",
  "DetectClusterDomainQuery": "",
  "DataCenterPattern": "",
  "DetectDataCenterQuery": "select 'redacted'",
  "PhysicalEnvironmentPattern": "",
  "PromotionIgnoreHostnameFilters": [

  ],
  "ServeAgentsHttp": false,
  "UseSSL": false,
  "UseMutualTLS": false,
  "SSLSkipVerify": false,
  "SSLPrivateKeyFile": "",
  "SSLCertFile": "",
  "SSLCAFile": "",
  "SSLValidOUs": [

  ],
  "StatusEndpoint": "/api/status",
  "StatusSimpleHealth": true,
  "StatusOUVerify": false,
  "HttpTimeoutSeconds": 60,
  "StaleSeedFailMinutes": 60,
  "SeedAcceptableBytesDiff": 8192,
  "SeedWaitSecondsBeforeSend": 2,
  "PseudoGTIDPattern": "drop view if exists `meta`.`_pseudo_gtid_hint__asc:",
  "PseudoGTIDPatternIsFixedSubstring": true,
  "PseudoGTIDMonotonicHint": "asc:",
  "DetectPseudoGTIDQuery": "select count(*) as pseudo_gtid_exists from meta.pseudo_gtid_status where anchor = 1 and time_generated > now() - interval 1 day",
  "BinlogEventsChunkSize": 10000,
  "BufferBinlogEvents": true,
  "SkipBinlogEventsContaining": [
    "@@SESSION.GTID_NEXT= 'ANONYMOUS'"
  ],
  "ReduceReplicationAnalysisCount": false,
  "FailureDetectionPeriodBlockMinutes": 60,
  "RecoveryPeriodBlockSeconds": 600,
  "RecoveryIgnoreHostnameFilters": [

  ],
  "RecoverMasterClusterFilters": [
    "*"
  ],
  "RecoverIntermediateMasterClusterFilters": [
    "*"
  ],
  "OnFailureDetectionProcesses": [
    "/redacted/our-orchestrator-recovery-handler -t 'detection' -f '{failureType}' -h '{failedHost}' -C '{failureCluster}' -A '{failureClusterAlias}' -n '{countReplicas}'"
  ],
  "PreGracefulTakeoverProcesses": [
    "echo 'Planned takeover about to take place on {failureCluster}. Master will switch to read_only' >> /tmp/recovery.log"
  ],
  "PreFailoverProcesses": [
    "/redacted/our-orchestrator-recovery-handler -t 'pre-failover' -f '{failureType}' -h '{failedHost}' -C '{failureCluster}' -A '{failureClusterAlias}' -n '{countReplicas}'"
  ],
  "PostFailoverProcesses": [
    "/redacted/our-orchestrator-recovery-handler -t 'post-failover' -f '{failureType}' -h '{failedHost}' -H '{successorHost}' -C '{failureCluster}' -A '{failureClusterAlias}' -n '{countReplicas}' -u '{recoveryUID}'"
  ],
  "PostUnsuccessfulFailoverProcesses": [
    "/redacted/our-orchestrator-recovery-handler -t 'post-unsuccessful-failover' -f '{failureType}' -h '{failedHost}' -C '{failureCluster}' -A '{failureClusterAlias}' -n '{countReplicas}' -u '{recoveryUID}'"
  ],
  "PostMasterFailoverProcesses": [
    "/redacted/do-something # e.g. kick pt-heartbeat on promoted master"
  ],
  "PostIntermediateMasterFailoverProcesses": [
  ],
  "PostGracefulTakeoverProcesses": [
    "echo 'Planned takeover complete' >> /tmp/recovery.log"
  ],
  "CoMasterRecoveryMustPromoteOtherCoMaster": true,
  "DetachLostSlavesAfterMasterFailover": true,
  "ApplyMySQLPromotionAfterMasterFailover": true,
  "PreventCrossDataCenterMasterFailover": false,
  "PreventCrossRegionMasterFailover": false,
  "MasterFailoverLostInstancesDowntimeMinutes": 60,
  "PostponeReplicaRecoveryOnLagMinutes": 10,
  "OSCIgnoreHostnameFilters": [

  ],
  "GraphitePollSeconds": 60,
  "GraphiteAddr": "",
  "GraphitePath": "",
  "GraphiteConvertHostnameDotsToUnderscores": true,
  "BackendDB": "mysql",
  "MySQLTopologyReadTimeoutSeconds": 3,
  "MySQLDiscoveryReadTimeoutSeconds": 3,
  "SQLite3DataFile": "/var/lib/orchestrator/orchestrator-sqlite.db",
  "RaftEnabled": false,
  "RaftBind": "redacted",
  "RaftDataDir": "/var/lib/orchestrator",
  "DefaultRaftPort": 10008,
  "ConsulAddress": "redacted:8500",
  "RaftNodes": [
    "redacted",
    "redacted",
    "redacted"
  ]
}
```

# 指令

```bash
# 以配置文件 /path/to/orchestrator.conf.json 中的設置啟動 Orchestrator
orchestrator -c /path/to/orchestrator.conf.json

# 查看 MySQL 複製拓撲的狀態，包括主從關係、複製延遲等
orchestrator-client -c discover

# 手動執行故障切換，提升從節點為新的主節點
orchestrator-client -c relocate

# 手動移動複製節點，即更改複製的拓撲
orchestrator-client -c move

# 顯示 Orchestrator 的操作歷史，包括手動和自動操作
orchestrator-client -c history

# 顯示拓撲的變更計劃，例如添加或刪除節點
orchestrator-client -c topology

# 啟動 Orchestrator 的 Web 界面，預設端口是 3000
orchestrator -http 3000

# 獲取更多信息和選項
orchestrator-client --help
```

