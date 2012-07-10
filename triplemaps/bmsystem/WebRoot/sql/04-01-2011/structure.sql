/*
SQLyog Enterprise - MySQL GUI v7.02 
MySQL - 5.1.31-community : Database - bmsystems
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`bmsystems` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `bmsystems`;

/*Table structure for table `bm_loans` */

DROP TABLE IF EXISTS `bm_loans`;

CREATE TABLE `bm_loans` (
  `loanid` int(11) NOT NULL AUTO_INCREMENT,
  `loanamount` int(11) DEFAULT NULL,
  `duedate` datetime DEFAULT NULL,
  `paiddate` datetime DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`loanid`),
  KEY `FK_bm_loans` (`userid`),
  CONSTRAINT `FK_bm_loans` FOREIGN KEY (`userid`) REFERENCES `bm_users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bm_loans` */

LOCK TABLES `bm_loans` WRITE;

UNLOCK TABLES;

/*Table structure for table `bm_premiums` */

DROP TABLE IF EXISTS `bm_premiums`;

CREATE TABLE `bm_premiums` (
  `premiumid` int(11) NOT NULL AUTO_INCREMENT,
  `pamount` int(11) DEFAULT NULL,
  `duedate` datetime DEFAULT NULL,
  `paiddate` datetime DEFAULT NULL,
  `penalty` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`premiumid`),
  KEY `FK_bm_premiums` (`userid`),
  CONSTRAINT `FK_bm_premiums` FOREIGN KEY (`userid`) REFERENCES `bm_users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bm_premiums` */

LOCK TABLES `bm_premiums` WRITE;

UNLOCK TABLES;

/*Table structure for table `bm_users` */

DROP TABLE IF EXISTS `bm_users`;

CREATE TABLE `bm_users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) DEFAULT NULL,
  `middleName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `address1` varchar(150) DEFAULT NULL,
  `address2` varchar(150) DEFAULT NULL,
  `contactNo` int(11) DEFAULT NULL,
  `recAmount` int(11) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `bm_users` */

LOCK TABLES `bm_users` WRITE;

insert  into `bm_users`(`userid`,`firstName`,`middleName`,`lastName`,`address1`,`address2`,`contactNo`,`recAmount`) values (1,'Rajesh','H','Rathod','Test','Test',2147483647,500);

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
