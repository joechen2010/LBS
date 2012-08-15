CREATE DATABASE IF NOT EXISTS `mutong` character set utf8;
CREATE USER 'joechen8310'@'localhost' IDENTIFIED BY 'system';
GRANT ALL PRIVILEGES ON joechen8310.* TO joechen8310@`%`  IDENTIFIED BY 'joechen8310';
GRANT ALL PRIVILEGES ON joechen8310.* TO joechen8310@`localhost` IDENTIFIED BY 'joechen8310';

FLUSH PRIVILEGES;



