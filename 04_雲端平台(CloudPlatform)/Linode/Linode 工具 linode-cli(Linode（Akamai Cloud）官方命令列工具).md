# Linode 工具 linode-cli(Linode（Akamai Cloud）官方命令列工具)

```
```

linode-cli 可以做什麼

| 類型       | 功能                |
| -------- | ----------------- |
| 主機       | 建立 / 刪除 / 開關 / 重建 |
| 備份       | 啟用 / 還原 / 查詢      |
| Disk     | 管理磁碟              |
| Volume   | Block Storage     |
| Firewall | 防火牆規則             |
| IP       | 轉移 IP             |
| Image    | 自訂 Image          |
| 網路       | VPC / VLAN        |
| Account  | 帳務 / 使用量          |

## 目錄

- [Linode 工具 linode-cli(Linode（Akamai Cloud）官方命令列工具)](#linode-工具-linode-clilinodeakamai-cloud官方命令列工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [用法](#用法)
  - [Linode（主機）相關](#linode主機相關)
  - [Backup（備份）](#backup備份)
  - [Disk / Volume（救資料常用）](#disk--volume救資料常用)
  - [防火牆（Firewall）](#防火牆firewall)
  - [IP / 網路](#ip--網路)
  - [實用技巧](#實用技巧)

## 參考資料

[]()

# 安裝

安裝與初始化（只做一次）

```sh
pip install linode-cli
linode-cli configure
```

設定時會問：

API Token

預設 region（可略）

預設 type（可略）

設定檔位置：

```
~/.config/linode-cli/config
```

# 用法

## Linode（主機）相關

列出所有主機

```sh
linode-cli linodes list
```

查看單一主機詳細資料

```sh
linode-cli linodes view <LINODE_ID>
```

建立新主機（Image）

```sh
linode-cli linodes create \
  --image linode/ubuntu22.04 \
  --region ap-northeast \
  --type g6-dedicated-8 \
  --label my-server \
  --root_pass 'StrongPass!'
```

從 Backup 建立主機

```sh
linode-cli linodes create \
  --backup_id <BACKUP_ID> \
  --region ap-northeast \
  --type g6-dedicated-32 \
  --label restore-from-backup \
  --root_pass 'StrongPass!'
```

開 / 關 / 重開 主機

```sh
linode-cli linodes boot <ID>
linode-cli linodes shutdown <ID>
linode-cli linodes reboot <ID>
```

刪除主機（不可逆）

```sh
linode-cli linodes delete <ID>
```

## Backup（備份）

列出某台主機的備份

```sh
linode-cli backups list <LINODE_ID>
```

查看單一備份

```sh
linode-cli backups view <BACKUP_ID>
```

啟用 / 停用自動備份

```sh
linode-cli linodes update <ID> --backups_enabled true
linode-cli linodes update <ID> --backups_enabled false
```

## Disk / Volume（救資料常用）

列出主機磁碟

```sh
linode-cli linodes disks-list <LINODE_ID>
```

建立 Disk（空白）

```sh
linode-cli linodes disk-create <LINODE_ID> \
  --label data \
  --size 50000
```

Volumes（Block Storage）

```sh
linode-cli volumes list
linode-cli volumes attach <VOLUME_ID> --linode_id <ID>
linode-cli volumes detach <VOLUME_ID>
```

## 防火牆（Firewall）

列出防火牆

```sh
linode-cli firewalls list
```

綁定防火牆

```sh
linode-cli firewalls attach <FIREWALL_ID> --linodes <LINODE_ID>
```

## IP / 網路

查看 IP

```sh
linode-cli networking ips-list
```

將 IP 轉移到另一台主機

```sh
linode-cli networking ip-assign \
  --assignments '[{"linode_id":NEW_ID,"address":"IP"}]'
```

## 實用技巧

只看 JSON（方便寫 script）

```sh
linode-cli linodes list --json
```

自訂輸出欄位

```sh
linode-cli linodes list \
  --format "id,label,status,ipv4,region"
```

Dry run（看 API payload）

```sh
linode-cli linodes create --help
```