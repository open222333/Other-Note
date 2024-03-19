# GitLab(DevOps平台)

```
GitLab是由GitLab公司開發的、基於Git的整合軟體開發平台。
GitLab且具有wiki以及線上編輯、issue跟蹤功能、CI/CD等功能。

版本控制：GitLab 提供了強大的 Git 存儲庫管理功能，支持分支管理、合併請求、代碼審查等。

CI/CD：GitLab 內置了持續集成和持續交付的功能，可以自動化構建、測試和部署代碼。它支持多種語言和環境，並提供了豐富的配置選項。

代碼審查：GitLab 提供了強大的代碼審查功能，可以通過合併請求進行團隊成員之間的代碼審查和討論。

項目管理：GitLab 提供了問題跟踪、任務管理、里程碑等功能，幫助團隊組織和跟踪項目的進度和工作。

部署和運維：GitLab 內置了容器註冊表、Kubernetes 集群集成、自動化部署等功能，方便部署和運維應用程序。

集成和擴展：GitLab 支持與其他工具和服務的集成，如Slack、Jira、Jenkins 等，並提供了豐富的API和插件機制。
```

## 目錄

- [GitLab(DevOps平台)](#gitlabdevops平台)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
	- [Ubuntu](#ubuntu)
	- [CentOS7](#centos7)
- [指令](#指令)

## 參考資料

[GitLab - wiki](https://zh.wikipedia.org/zh-tw/GitLab)

[GitLab 社群版（不含專有元件）](https://gitlab.com/gitlab-org/gitlab)

[GitLab 官方網站](https://about.gitlab.com/)

[GitLab 官方文檔(安裝教學在下方)](https://docs.gitlab.com/)

# 安裝

## Ubuntu

```bash
# 安裝 GitLab
curl -sS https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.deb.sh | sudo bash
apt-get install gitlab-ce
# 編輯 GitLab 配置文件
nano /etc/gitlab/gitlab.rb

# 重新配置 GitLab
gitlab-ctl reconfigure

# 啟動 GitLab 服務
gitlab-ctl start

# 檢查 GitLab 狀態
gitlab-ctl status
```

## CentOS7

```bash
# 安裝配置必要的依賴項目
yum update
yum install -y curl policycoreutils-python openssh-server

# 安裝郵件服務（這裡使用 Postfix）
yum install -y postfix
systemctl enable postfix
systemctl start postfix

# 安裝 GitLab 的存儲庫
curl -sS https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.rpm.sh | sudo bash

# 安裝 GitLab
yum install -y gitlab-ce

# 配置 GitLab
# 打開 /etc/gitlab/gitlab.rb 配置文件，按照需要修改相應的參數。特別是要確保 external_url 設置為你的 GitLab 網址。
vim /etc/gitlab/gitlab.rb

# 重新配置 GitLab
gitlab-ctl reconfigure

# 啟動 GitLab 服務
gitlab-ctl start

# 檢查 GitLab 狀態
gitlab-ctl status
```

# 指令

```bash
# 啟動 GitLab
gitlab-ctl start

# 停止 GitLab
gitlab-ctl stop

# 重新啟動 GitLab
gitlab-ctl restart

# 重新配置 GitLab
gitlab-ctl reconfigure

# 查看 GitLab 狀態
gitlab-ctl status

# 停止所有 GitLab 組件（NGINX、PostgreSQL、Redis 等）
gitlab-ctl stop

# 啟動所有 GitLab 組件
gitlab-ctl start

# 重啟所有 GitLab 組件
gitlab-ctl restart

# 查看 GitLab 日誌
gitlab-ctl tail
```