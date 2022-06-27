# JavaScript Node 工具 pm2(node程序管理器)

```
pm2 是一個 node 的程序管理器
pm2 可以讓 node 服務 crash 掉之後，自動幫我們重啟
pm2 可以在 server 重啟之後，自動幫我們重啟
pm2 可利用 CPU 多核，開啟多程序，已達到類似負載平衡的效果
	Graceful reload 可達成類似 rolling upgrade 的效果，0 downtime 升級
	多程序多服務，可提升處理 request 的速度
	可設定 cron 排程自動重啟時間
pm2 提供多項資訊，包含已重啟次數、 CPU 用量、 memory 用量, process id, 等等…
pm2 可以在指定的條件下，自動幫我們重啟，條件可以是’up time’, ‘已使用多少 memory’, 等等…,
pm2 可以幫我們整理 log, 讓 log 以我們想要的週期分割檔案，並保存我們想要的數量，若有超過，自動刪除。
pm2 提供簡單的部署方式，可一次性部署到多台 server
pm2 可與 CD / CD 工具做結合， CI / CD 部署也沒有問題
```

## 參考資料

[pm2 npm 頁面](https://www.npmjs.com/package/pm2)

[pm2 官方文檔](https://pm2.keymetrics.io/docs/usage/pm2-doc-single-page/)

[pm2 - 用法大全](https://tn710617.github.io/zh-tw/pm2/#%E6%9C%89%E8%AE%8A%E6%9B%B4%E6%99%82%E9%87%8D%E5%95%9F)

# 指令

```bash
# 全域安裝 -g
npm install pm2@latest -g
npm install -g pm2

# 更新 PM2
npm install pm2@latest -g && pm2 update

# 安裝 log 套件
pm2 install pm2-logrotate
	# config 檔位置
	/home/user/.pm2/module_conf.json

	module_conf.json設定檔參數
	max_size (預設 10M):
		當 log 檔案達到多大時， logrotate module 會將它分割成另外一個檔案。 logrotate module 有可能在檢查檔案時，檔案已經超過指定的大小了，所以超過一些些是可能的。 單位可以自行指定, 10G, 10M, 10K
	retain (預設 30 個 log 檔案):
		預設最多保存的 log 數量，如果設定為 7 的話，將會保存目前的 log, 以及最多 7 個 log 檔案
	compress (預設 false):
		壓縮所有循環 log 檔案
	dateFormat (時間格式，預設 YYYY-MM-DD_HH-mm-ss) :
		檔案命名的時間格式
	rotateModule (預設 true) :
		跟其他 apps 一樣，循環 pm2’s module
	workerInterval (預設 30 秒) :
		多久 logrotate 會檢查一次 log 檔案大小
	rotateInterval (預設每天午夜循環, 範例 0 0 * * * ):
		除了設定檔案大小以外，我們也可以設定以時間為單位去循環，格式上採用 node-schedule
	TZ (預設為系統時間):
		檔案命名的時間會根據你所設定的時區而改變

# 自動補齊 支援 pm2 指令可以打 tab 自動補齊
pm2 completion install

```

# 用法

```bash
# 查看列表
pm2 list
pm2 ls

# 顯示$app_name的詳細資訊
pm2 show $app_name

# 啟動
pm2 start app.js
	--name
		指定 app 一個名字
	--watch
		檔案有變更時，會自動重新啟動
	--max-memory-restart
		Memory 使用超過這個門檻時，會自動重啟
	--log
		指定 log 的位址, 若要指定新位址，需將原本的 process 刪掉，再重新啟動指定
	--output
		指定 output log 位址
	--error
		指定 error log 位址
	--log-date-format
		指定 log 的格式
	--merge-logs
		同一個 app 跑多程序時，不要依據程序 id 去分割 log, 全部合在一起
	--arg1 --arg2 --arg3
		指派額外的參數
	--restart-delay
		自動重啟時，要 delay 多久
	--time
		給 log 加上前綴
	--no-autorestart
		不要自動重啟
	--cron
		指定 cron 規律，強制重啟
	--no-daemon
		無 daemon 模式， listen log 模式
	--spa
		限定 serve 使用, 會重導所有的請求到 index.html
	--basic-auth-username --basic-auth-password
		用於靜態檔, 讓該頁面需要帳號密碼方可存取

# 重啟
pm2 restart app_name
	除了 app_name 之外，你也可以指定
	all : 啟動所有程序
	id : 該程序 id

# 停止
pm2 stop app_name
	除了 app_name 之外，你也可以指定
	all : 啟動所有程序
	id : 該程序 id

# 刪除並停止
pm2 delete app_name

# 查看log
pm2 logs $id(指定程序)
	--lines $number
		指定倒數 $number 行
	--format
		指定輸出格式 format
	--json
		指定輸出格式 json

# 清空 log
pm2 flush

# 產生開機 script
pm2 startup

# 取消開機自動重啟
pm2 unstartup

# 儲存下次重啟時，預設啟動的 process
pm2 save

# 如果有更新 node 的版本，記得更新 script
pm2 unstartup && pm2 startup && pm2 save

```

# pm2 ecosystem

```
CLI 工具固然不錯，但只要是人難免手滑打錯或漏打參數。
pm2 ecosystem 解決了這個問題，只要好好的打上一次，以後除非設定有變更，否則啟動服務只需要短短幾個指令，而且 ecosystem 檔案還可以納入 git 控管，跟著專案跑
```

```bash
# 產生範例 ecosystem file
pm2 ecosystem

pm2 start ecosystem.config.js
# 從 ecosystem 中只啟動特定 app
pm2 start ecosystem.config.js --only yourApp

```

```js
// 參數範例 ecosystem.config.js
module.exports = {
    apps: [
        // First application
        {
            // App 名稱
            name: 'app1',
            // 執行服務的入口檔案
            script: './server.js',
            // 你的服務所在位置
            cwd: 'var/www/yourApp/',
            // 分為 cluster 以及 fork 模式
            exec_mode: 'cluster',
            // 只適用於 cluster 模式，程序啟動數量
            instances: 0,
            // 適合開發時用，檔案一有變更就會自動重啟
            watch: false,
            // 當佔用的 memory 達到 500M, 就自動重啟
            max_memory_restart: '500M',
            // 可以指定要啟動服務的 node 版本
            interpreter: '/root/.nvm/versions/node/v8.16.0/bin/node',
            // node 的額外參數
            // 格式可以是 array, 像是 "args": ["--toto=heya coco", "-d", "1"], 或是 string, 像是 "args": "--to='heya coco' -d 1"
            interpreter_args: "port=3001 sitename='first pm2 app'",
            // 同上
            node_args: "port=3001 sitename='first pm2 app'",
            // 'cron' 模式指定重啟時間，只支持 cluster 模式
            cron_restart: "0 17 * * *",
            // log 顯示時間
            time: true,
            // 可經由 CLI 帶入的參數
            args: '-a 13 -b 12',
            // 想要被忽略的檔案或資料夾, 支援正則，指定的檔案或資料夾如果內容有變更，服務將不會重啟
            // 格式可以是 array, 像是 "args": ["--toto=heya coco", "-d", "1"], 或是 string, 像是 "args": "--to='heya coco' -d 1"
            ignore_watch: ["[\/\\]\./", "node_modules"],
            // 支援 source_map, 預設 true, 細節可參考
            // http://pm2.keymetrics.io/docs/usage/source-map-support/
            // https://www.html5rocks.com/en/tutorials/developertools/sourcemaps/
            source_map_support: true,
            // instance_var, 詳見以下連結
            // http://pm2.keymetrics.io/docs/usage/environment/#specific-environment-variables
            instance_var: 'NODE_APP_INSTANCE',
            // log 的時間格式
            log_date_format: 'YYYY-MM-DD HH:mm Z',
            // 錯誤 log 的指定位置
            error_file: '/var/log',
            // 正常輸出 log 的指定位置
            out_file: '/var/log',
            // 同一個 app 有多程序 id, 如果設定為 true 的話， 同 app 的 log 檔案將不會根據不同的程序 id 分割，會全部合在一起
            combine_logs: true,
            // 同上
            merge_logs: true,
            // pid file 指定位置, 預設 $HOME/.pm2/pid/app-pm_id.pid
            pid_file: 'user/.pm2/pid/app-pm_id.pid',
            // pm2 會根據此選項內的時間來判定程序是否有成功啟動
            // 格式可使用 number 或 string, number 的話， 3000 代表 3000 ms。 string 的話, 可使用 '1h' 代表一個小時, '5m' 代表五分鐘, '10s' 代表十秒
            min_uptime: '5',
            // 單位為 ms, 如果在該時間內 app 沒有聽 port 的話，強制重啟
            listen_timeout: 8000,
            // 當執行 reload 時，因為 graceful reload 會等到服務都沒有被存取了才會斷開，如果超過這個時間，強制斷開重啟
            // 細節可參考官方文件 http://pm2.keymetrics.io/docs/usage/signals-clean-restart/
            kill_timeout: 1600,
            // 一般來說，服務等待 listen 事件觸發後，執行 reload, 若此選項為 true, 則等待 'ready' message
            // 細節可參考官方文件 http://pm2.keymetrics.io/docs/usage/signals-clean-restart/
            wait_ready: false,
            // pm2 具有 crash 自動重啟的功能。 但若異常狀況重啟超過此選項的指定次數，則停止自動重啟功能。 異常與否的判定，預設為 1 秒，也就是說如果服務啟動不足一秒又立即重啟，則異常重啟次數 + 1。 若 min_uptime 選項有指定，則以 min_uptime 指定的最小正常啟動時間為標準來判斷是否為異常重啟
            // 細節可參考官方文件 http://pm2.keymetrics.io/docs/usage/signals-clean-restart/
            max_restarts: 10,
            // 單位為 ms, 預設為 0, 若有指定時間，則 app 會等待指定時間過後重啟
            restart_delay: 4000,
            // 預設為 true, 若設為 false, pm2 將會關閉自動重啟功能, 也就是說 app crash 之後將不會自動重啟
            autorestart: true,
            // 預設為 true, 預設執行 pm2 start app 時，只要 ssh key 沒問題， pm2 會自動比較 local 跟 remote, 看是否為最新的 commit，若否，會自動下載更新。 此功能有版本問題，需新版才支援
            vizion: true,
            // 進階功能，當使用 Keymetrics 的 dashboard 執行 pull 或 update 操作後，可以觸發執行的一系列指令
            post_update: ["npm install", "echo launching the app"],
            // defaults to false. if true, you can start the same script several times which is usually not allowed by PM2
            // 預設為 false, 如果設定為 true,
            force: false,
            // 當不指定 env 時，會套用此 object 裡頭的環境變數, 例如 pm2 start ecosystem.js
            env: {
                COMMON_VARIABLE: 'true',
                NODE_ENV: '',
                ID: '44'
            },
            // 當有指定 env 時，會套用此 object 裡頭的環境變數, 例如 pm2 start ecosystem.js --env production
            env_production: {
                NODE_ENV: 'production',
                ID: '55'
            },
            // 同上
            env_development: {
                NODE_ENV: 'development'
            }
        },
        // 第二個 app, 很多資訊上面有介紹過的就不再重複
        {
            // Serve 模式, 可服務靜態資料夾
            script: "serve",
            env: {
                PM2_SERVE_PATH: '.',
                PM2_SERVE_PORT: 8080
                },
            name: 'app2',
            // 預設模式，可應用在其他語言, cluster 只可用在 node.js
            exec_mode: 'fork',
            interpreter: '/root/.nvm/versions/node/v8.16.0/bin/node',
            time: true,
        }
    ],
    // 這一個區塊是部署的部分
    deploy: {
        // production
        production: {
            // 要登入執行 pm2 的 user
            user: 'root',
            // 支援多個 host 部署
            host: ['host1', 'host2'],
            // remote 要檢查的 public key 的位置
            key: 'path/to/some.pem',
            // 要部署的分支
            ref: 'origin/master',
            // Git 倉庫位址
            repo: 'git@gitlab.com:user/yourProject.git',
            // 要部署到 server 上的資料夾路徑
            path: '/var/www/yourProjectName',
            // 如果 ssh 有設定好，從 local 連到 remote 端將不會再詢問是否將 remote 端的 public key 加到 known host
            "ssh_options": "StrictHostKeyChecking=no",
            // 在 pm2 要從 local 端連到 remote 端之前要執行的指令，可以多個指令，由 ; 分割，也可以指定 shell script 的檔案路徑
            "pre-setup": 'apt update -y; apt install git -y',
            // 當 pm2 在 remote 機器上將專案 clone 下來之後會執行的指令，同上，可以多個指令，由 ; 分割，也可以指定 shell script 的檔案路徑
            "post-setup": "ls -la",
            // 當 pm2 在 local 要連上 remote 部署之前 ，在 local 端所要執行的指令, 同上，可以多個指令，由 ; 分割，也可以指定 shell script 的檔案路徑
            "pre-deploy-local" : "echo 'This is a local executed command'",
            // 部署完成後, 所要執行的指令 同上，可以多個指令，由 ; 分割，也可以指定 shell script 的檔案路徑
            'post-deploy': 'sudo /root/.nvm/versions/node/v8.16.0/bin/npm install && sudo /root/.nvm/versions/node/v8.16.0/bin/npm rebuild && /root/.nvm/versions/node/v8.16.0/bin/pm2 reload ecosystem.config.js',
            env_production: {
                 NODE_ENV: 'production'
            }
        },
        staging: {
            user: 'root',
            host: ['host3', 'host4'],
            ref: 'origin/staging',
            repo: 'git@gitlab.com:user/yourProject.git',
            path: '/var/www/yourProjectName',
            "ssh_options": "StrictHostKeyChecking=no",
            "pre-setup": 'apt update -y; apt install git -y',
            "post-setup": "ls -la",
            "pre-deploy-local" : "echo 'This is a local executed command'",
            'post-deploy': 'sudo /root/.nvm/versions/node/v8.16.0/bin/npm install && sudo /root/.nvm/versions/node/v8.16.0/bin/npm rebuild && /root/.nvm/versions/node/v8.16.0/bin/pm2 reload ecosystem.config.js',
            env_production: {
                 NODE_ENV: 'staging'
            }
        },
    },
};
```