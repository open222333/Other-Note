# Linux 工具 nohup(背景執行)

## 參考資料

[Linux、Windows后台运行Python脚本](https://blog.csdn.net/zhu6201976/article/details/103434413\)

# 指令

```bash
# 只要將要執行的程式放在參數中即可，而通常會在尾端加上 & 把程式放在背景執行：
# 讓程式登出後可繼續執行
nohup /path/my_program &

# &>/home/{user}/myout.txt &: 執行的log位置
nohup python3 manage.py runserver &>/home/{user}/myout.txt &
nohup uwsgi --ini APIWeb_Django/uwsgi.ini  &>/home/openuser01/uwsgi_log.txt &

# 看進程編號
ps U {user} -f

# 關閉程式
kill XXX進程編號

# Linux：python或nohup命令
# python命令：
# 參數u，時時輸出內容到文件。
python test.py &
python -u test.py >> test.log &

# nohup命令：
# 基本用法：
# 後台使用Python3運行test.py文件，日誌默認輸出到當前目錄nohup.out或~/nohup.out文件。
nohup python3 test.py &

# 後台使用Python3運行test.py文件，日誌全部輸出到當前目錄nohup.out文件。
nohup python3 test.py > nohup.out 2>&1 &

# 查看所有Python3進程
ps -ef | grep python3
```