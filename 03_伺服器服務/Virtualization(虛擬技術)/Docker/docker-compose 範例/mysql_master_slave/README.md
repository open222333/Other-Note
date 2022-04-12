clone 資料庫的資料夾 已建立
docker-compose up -d 拉起容器

建立 MySQL master-slave 架構

 ======== master server 部分 ========

-- 確認mysql-master server_id和log_bin變數
SHOW VARIABLES LIKE 'server_id%';
SHOW VARIABLES LIKE 'log_bin%';

-- 顯示master狀態：該File列顯示日誌文件的名稱並Position顯示文件中的位置。記錄這些值。稍後在設置副本時需要它們。它們表示副本應開始處理來自源的新更新的複制坐標。
SHOW MASTER STATUS;

-- 建立master-slave使用者：要創建新帳戶，請使用CREATE USER。要授予此帳戶複製所需的權限，請使用該GRANT 語句。如果您僅為複制目的創建帳戶，則該帳戶只需要 REPLICATION SLAVE權限。例如，要設置一個repl可以從example.com域內的任何主機連接以進行複制 的新用戶
CREATE USER 'user'@'host' IDENTIFIED BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'user'@'host';

-- 列出所有使用者帳號
SELECT User,Host FROM mysql.user;

 ======== slave server 部分 ========

-- 檢查mysql-slave server_id和read_only變數
SHOW VARIABLES LIKE 'server_id%';
SHOW VARIABLES LIKE 'read_only%';

-- 新增mysql-slave設定master資料 綁定到master
CHANGE MASTER TO MASTER_HOST = 'master ip',
MASTER_PORT = 3306,
MASTER_USER = 'user',
MASTER_PASSWORD = 'password',
-- 在master server時，輸入 SHOW MASTER STATUS 找出的資訊;
MASTER_LOG_FILE = 'master bin_log filename',
MASTER_LOG_POS = 'log position';

-- 確認slave狀態：SHOW SLAVE STATUS，注意：Slave_IO_Running: Yes、Slave_SQL_Running: Yes
SHOW SLAVE STATUS\G

-- 執行slave
START SLAVE;
