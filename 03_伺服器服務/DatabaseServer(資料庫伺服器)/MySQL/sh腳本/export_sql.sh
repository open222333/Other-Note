#!/bin/bash
date=`date +"%Y-%m-%d"`
time=`date -d "${date}" +%s`
mysqldump -uroot -p'Password' -n --replace database table > /tmp/testSpeed/domain-cn-${date}.sql
mysqldump -uroot -p'Password' -nt --replace database table -w'vod_addtime>'${time} > /tmp/testSpeed/vod-cn-${date}.sql
mysqldump -uroot -p'Password' -n --replace database table  > /tmp/testSpeed/tag-cn-${date}.sql
mysqldump --tab=/var/lib/mysql-files --fields-terminated-by=',' --fields-enclosed-by='"' --lines-terminated-by='\n' --no-create-info --user=root --password=Password database table
echo "採集影片.域名測速匯出時間 : "`date` >> /tmp/check.log
find /tmp/testSpeed/ -mtime +2 -name '*.sql' -exec rm -rf "{}" \;