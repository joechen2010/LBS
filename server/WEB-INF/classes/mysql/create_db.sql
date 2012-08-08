CREATE DATABASE IF NOT EXISTS `mutong` character set utf8;
GRANT ALL PRIVILEGES ON mutong.* TO mutong@`%`  IDENTIFIED BY 'mutong';
GRANT ALL PRIVILEGES ON mutong.* TO mutong@`localhost` IDENTIFIED BY 'mutong';

FLUSH PRIVILEGES;



