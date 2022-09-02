-- 修改 MySQL 服务器的查询包最大限制为 500M
-- 但每次重启 MySQL 之后（包括重启电脑/关机之后）都需要手动再执行下
-- 直接去 MySQL Workbench 上去执行即可
set global max_allowed_packet = 500 * 1024 * 1024;
-- 设置之后，需要将 MySQL Workbench 重新打开下
-- 设置之后，需要重启 Tomcat，让 MySQL 重新连接