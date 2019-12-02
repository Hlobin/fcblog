/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.6.46 : Database - feiblog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`feiblog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `feiblog`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(11) DEFAULT NULL,
  `admin_password` varchar(100) DEFAULT NULL,
  `admin_phone` varchar(11) DEFAULT NULL,
  `admin_email` varchar(100) DEFAULT NULL,
  `admin_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `admin_status` int(1) DEFAULT '1',
  `admin_delete` int(1) DEFAULT '1',
  `admin_expend_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_expend_id` (`admin_expend_id`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`admin_expend_id`) REFERENCES `admin_expend` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `admin` */

insert  into `admin`(`id`,`admin_name`,`admin_password`,`admin_phone`,`admin_email`,`admin_date`,`admin_status`,`admin_delete`,`admin_expend_id`) values (1,'admin','285e11c6df3f831e4460c80569d10343899bf51156878895','15281223316','1692454247@qq.com','2019-10-04 20:17:22',1,1,1),(2,'spring',NULL,'15281223316','1692454247@qq.com','2019-10-04 20:17:24',1,1,NULL),(5,'springBoot','d2f06ac4eb9de7fe0464414219269ae0f804098b00a1bc72','15281223316','1692454247@qq.com','2019-10-05 09:37:50',1,1,5),(7,'DrameCode','c6f393b0f24be85978f07550e9096de37384f5358a179107','15281223316','1692454247@qq.com','2019-10-08 16:30:11',1,0,7),(8,'SpringBoot','14464434f279c1b284d21a0466ba82a35656c4df55194a9e','15281223316','1692454247@qq.com','2019-10-08 16:30:58',1,1,8);

/*Table structure for table `admin_expend` */

DROP TABLE IF EXISTS `admin_expend`;

CREATE TABLE `admin_expend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_real_name` varchar(100) DEFAULT NULL,
  `admin_sex` varchar(100) DEFAULT NULL,
  `admin_birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `admin_self_evaluation` text,
  `admin_province` int(11) DEFAULT NULL,
  `admin_city` int(11) DEFAULT NULL,
  `admin_area` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `admin_expend` */

insert  into `admin_expend`(`id`,`admin_real_name`,`admin_sex`,`admin_birthday`,`admin_self_evaluation`,`admin_province`,`admin_city`,`admin_area`) values (1,'王若飞','男','1997-09-03 11:12:37','Java工程师',NULL,NULL,NULL),(2,'默认名称',NULL,'2019-10-05 09:24:30',NULL,NULL,NULL,NULL),(3,'默认名称',NULL,'2019-10-05 09:29:23',NULL,NULL,NULL,NULL),(4,'默认名称',NULL,'2019-10-05 09:33:41',NULL,NULL,NULL,NULL),(5,'默认名称',NULL,'2019-10-05 09:37:49',NULL,NULL,NULL,NULL),(6,'默认名称',NULL,'2019-10-05 09:38:55',NULL,NULL,NULL,NULL),(7,'默认名称',NULL,'2019-10-05 09:41:59',NULL,NULL,NULL,NULL),(8,'默认名称',NULL,'2019-10-08 16:30:57',NULL,NULL,NULL,NULL);

/*Table structure for table `admin_techlogy` */

DROP TABLE IF EXISTS `admin_techlogy`;

CREATE TABLE `admin_techlogy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) DEFAULT NULL,
  `techlogy_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `techlogy_id` (`techlogy_id`),
  CONSTRAINT `admin_techlogy_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `admin_techlogy_ibfk_2` FOREIGN KEY (`techlogy_id`) REFERENCES `techlogy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `admin_techlogy` */

/*Table structure for table `article` */

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_name` varchar(100) DEFAULT NULL,
  `article_author` varchar(11) DEFAULT NULL,
  `article_abstract` longtext,
  `article_status` int(1) DEFAULT '1',
  `article_image` varchar(500) DEFAULT NULL,
  `article_look` varchar(100) DEFAULT NULL,
  `article_top` varchar(100) DEFAULT NULL,
  `article_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `article_content` longtext,
  `article_delete` int(1) DEFAULT '1',
  `article_type_id` int(1) DEFAULT NULL,
  `article_read` int(11) DEFAULT NULL,
  `article_love` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `article_type_id` (`article_type_id`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`article_type_id`) REFERENCES `article_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `article` */

insert  into `article`(`id`,`article_name`,`article_author`,`article_abstract`,`article_status`,`article_image`,`article_look`,`article_top`,`article_date`,`article_content`,`article_delete`,`article_type_id`,`article_read`,`article_love`) values (1,'快速搭建博客','王若飞','怎样快速搭建博客',1,'http://123.57.64.9/group1/M00/00/00/rBg7DF3ji3mAfR-wAABe6EvMn2c428.jpg','开放浏览','checked','2019-12-01 00:00:00','<p>本文很长，记录了我博客建站初到现在的过程，还有我从毕业到现在的一个状态，感谢您的阅读，如果你还是学生，也许你能从此文中，找到我们曾经相似的地方。如果你已经工作，有自己的博客，我想，你并没有忘记当初建立个人博客的初衷吧！\n\n我的个人博客已经建站有8年的时间了，对它的热爱，一直都是只增未减。回想大学读书的那几年，那会儿非常流行QQ空间，我们寝室的室友还经常邀约去学校的网吧做自己的空间。系里有个男生，空间做得非常漂亮，什么悬浮，开场动画，音乐播放器，我们女生羡慕得不得了。还邀约他跟我们一起去通宵弄空间，网上可以找到很多免费的flash资源，还有音乐，那也是第一次接触js，知道在浏览器输入一个地址，修改一下数据，就能调用一些背景出来。然后把自己QQ空间弄得漂漂亮亮的，经常邀约室友来互踩。我记得08年地震，第二天晚上，我们寝室的几个人还淡定的在寝室装扮空间呢！</p><p><br></p><p style=\"text-align: center;\"><img src=\"http://123.57.64.9/group1/M00/00/00/rBg7DF3jkDGAD7m6AAJ4GWlqIMo442.jpg\"></p><p style=\"text-align: center;\"><img src=\"http://192.168.2.8/group1/M00/00/00/wKgCCF3jex6AOcBiAAJ4GWlqIMo444.jpg\"></p>',1,1,NULL,NULL),(2,'111',NULL,'11111',0,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jgXOAGiJxAABe6EvMn2c672.jpg','开放浏览','checked','2019-12-01 00:00:00','11我w<img src=\"http://192.168.2.8/group1/M00/00/00/wKgCCF3jgX6AUImzAAJ4GWlqIMo033.jpg\">',NULL,1,NULL,NULL);

/*Table structure for table `article_image` */

DROP TABLE IF EXISTS `article_image`;

CREATE TABLE `article_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_image` varchar(100) DEFAULT NULL,
  `article_id` int(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id` (`article_id`),
  CONSTRAINT `article_image_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `article_image` */

insert  into `article_image`(`id`,`article_image`,`article_id`) values (3,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jZa6AQ0t8AABe6EvMn2c014.jpg',1),(4,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jZ4aAA5o6AABe6EvMn2c250.jpg',1),(5,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jZ6aAfyvDAABe6EvMn2c931.jpg',1),(6,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jZ_uARm_aAABe6EvMn2c757.jpg',1),(7,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jaA2AKp2QAABe6EvMn2c924.jpg',1),(8,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jaGKAbG4JAALXI2n0gGA565.jpg',1),(9,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jaGSAcFwZAABe6EvMn2c813.jpg',1),(10,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jaPaAdiJ8AABe6EvMn2c110.jpg',1),(11,'http://192.168.2.8/group1/M00/00/00/wKgCCF3jaruAHPfSAABe6EvMn2c269.jpg',1),(12,'http://192.168.2.8/group1/M00/00/00/wKgCCF3ja0KAW305AABe6EvMn2c410.jpg',1);

/*Table structure for table `article_type` */

DROP TABLE IF EXISTS `article_type`;

CREATE TABLE `article_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_type_name` varchar(11) DEFAULT NULL,
  `article_count` int(11) DEFAULT NULL,
  `article_type_delete` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `article_type` */

insert  into `article_type`(`id`,`article_type_name`,`article_count`,`article_type_delete`) values (1,'技术',NULL,0),(2,'1111',NULL,0),(3,'1',NULL,1);

/*Table structure for table `authority` */

DROP TABLE IF EXISTS `authority`;

CREATE TABLE `authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority_name` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf32;

/*Data for the table `authority` */

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_user_name` varchar(11) COLLATE utf8_german2_ci DEFAULT NULL,
  `comment_user_email` varchar(100) COLLATE utf8_german2_ci DEFAULT NULL,
  `comment_content` longtext COLLATE utf8_german2_ci,
  `comment_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment_article_id` int(100) DEFAULT NULL,
  `comment_user_id` int(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_article_id` (`comment_article_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`comment_article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_german2_ci;

/*Data for the table `comment` */

/*Table structure for table `techlogy` */

DROP TABLE IF EXISTS `techlogy`;

CREATE TABLE `techlogy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `technology_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `techlogy` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(11) DEFAULT NULL,
  `user_password` varchar(100) DEFAULT NULL,
  `user_image` varchar(100) DEFAULT NULL,
  `user_sex` varchar(11) DEFAULT NULL,
  `user_city` varchar(255) DEFAULT NULL,
  `user_sign` varchar(255) DEFAULT NULL,
  `user_phone` varchar(11) DEFAULT NULL,
  `user_email` varchar(100) DEFAULT NULL,
  `user_classify` varchar(100) DEFAULT NULL,
  `user_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_delete` int(1) DEFAULT '1',
  `authority_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `authority_id` (`authority_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
