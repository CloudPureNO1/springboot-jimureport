1. 创建数据库并制定为utf8
```sql
 create database jimureport default character set utf8 collate utf8_general_ci;
```

2. 创建用户
```sql
--   create user 'username'@'host' identified by password
-- 指定该用户在哪个主机上可以登陆，如果是本地用户可用localhost，如果想让该用户可以从任意远程主机登陆，可以使用通配符%
create user 'jimu'@'%' identified by 'jimu';
```
3. 授权
```sql
-- grant privileges on databasename.tablename to 'username'@'host'
-- 用户的操作权限，如SELECT，INSERT，UPDATE等，如果要授予所的权限则使用ALL
-- 用以上命令授权的用户不能给其它用户授权，如果想让该用户可以授权，用以下命令:
-- grant privileges on databasename.tablename to 'username'@'host' with grant option;
grant all on *.* to 'jimu'@'%'
```

4. 导出
```sql
-- 添加 -d 导出结果
-- mysqldump -uroot -pdbpasswd -d dbname >db.sql;
mysqldump -hrdsnimyju26nerqo.mysql.rds.aliyuncs.com -udisabled2 -p  --set-gtid-purged=off --column-statistics=0  -d disabled > d:\\disabled.sql

-- --set-gtid-purged=off   关闭 gtid 
-- --column-statistics=0   关闭 column-statistics
mysqldump -hrdsnimyju26nerqo.mysql.rds.aliyuncs.com -udisabled2 -p  --set-gtid-purged=off --column-statistics=0   disabled > d:\\disabled.sql

-- 没有 -d 导出结构和数据
-- mysqldump -uroot -pdbpasswd  dbname >db.sql;

-- 导出表，有-d 和 没有-d 与整个库一样
-- mysqldump -uroot -pdbpasswd dbname test>db.sql;

-- 导出数据中dbname多张表（test1,test2,test3）结构及表数据用用空格隔开
mysqldump -hrdsnimyju26nerqo.mysql.rds.aliyuncs.com -udisabled2 -p  --set-gtid-purged=off --column-statistics=0  disabled aa01 aa02 aa03 aa04 aa05 aa06 aa07 aa08 aa10 aa22 aa23 aa26 aa26_cn aa27 aa36 > d:\\disabled-aa-table-data.sql

-- 导出存储过程及函数
-- mysqldump -hhostname -uusername -ppassword -ntd -R databasename > prorandfunc.sql
mysqldump -hrdsnimyju26nerqo.mysql.rds.aliyuncs.com -udisabled2 -p -ntd -R  --set-gtid-purged=off --column-statistics=0  disabled > d:\\disabled-pro-fn.sql

-- 查询存储过程及函数
-- show procedure status;
-- show function status;
```
6. 导入
```sql
-- source 导入
-- 登录mysql ，选择数据库  ，source sql文件

-- mysql 命令导入
-- mysql -hhost -uusername -p databasename < sql文件
mysql -hlocalhost -udisabled -p disabled < disabled.sql
```
7. 导入报错
>ERROR 1418 (HY000) at line 1323: This function has none of DETERMINISTIC, NO SQL, or READS SQL DATA in its declaration and binary logging is enabled (you *might* want to use the less safe log_bin_trust_function_creators variable)

开启了bin-log, 我们就必须指定我们的函数是否是
1.  DETERMINISTIC 不确定的
2.  NO SQL 没有SQl语句，当然也不会修改数据
3.  READS SQL DATA 只是读取数据，当然也不会修改数据
4.  MODIFIES SQL DATA 要修改数据
5.  CONTAINS SQL 包含了SQL语句

其中在function里面，只有 DETERMINISTIC, NO SQL 和 READS SQL DATA 被支持。
如果我们开启了 bin-log, 我们就必须为我们的function指定一个参数
可以
> 登录数据库 
> set global log_bin_trust_function_creators =1;
就解决了。
 
