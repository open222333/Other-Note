# 範本

```
支援批次修改多個參數（如 number_of_replicas 和 refresh_interval），還有備份原設定，以防需要還原
```

```
自動掃描所有 monstache.* 索引

先備份設定到 backup_settings_$index.json

批次修改：

number_of_replicas = 0

refresh_interval = 24h
```

## 使用方式

### 批次修改多個參數

```sh
chmod +x update_monstache_index_settings.sh
```

```sh
./update_monstache_index_settings.sh
```

### 還原設定腳本（使用備份檔案）

```sh
chmod +x restore_monstache_index_settings.sh
```

```sh
./restore_monstache_index_settings.sh
```