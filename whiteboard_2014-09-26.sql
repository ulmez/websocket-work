# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.15)
# Database: whiteboard
# Generation Time: 2014-09-26 21:21:49 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table color
# ------------------------------------------------------------

DROP TABLE IF EXISTS `color`;

CREATE TABLE `color` (
  `id` int(10) unsigned NOT NULL,
  `yellow` tinyint(1) DEFAULT NULL,
  `green` tinyint(1) DEFAULT NULL,
  `blue` tinyint(1) DEFAULT NULL,
  `red` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_color_postit` FOREIGN KEY (`id`) REFERENCES `postit` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `color` WRITE;
/*!40000 ALTER TABLE `color` DISABLE KEYS */;

INSERT INTO `color` (`id`, `yellow`, `green`, `blue`, `red`)
VALUES
	(19,1,0,0,0),
	(21,0,0,1,0),
	(29,1,0,0,0),
	(30,0,1,0,0),
	(31,0,0,0,1),
	(32,0,0,1,0);

/*!40000 ALTER TABLE `color` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table postit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `postit`;

CREATE TABLE `postit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `information` text NOT NULL,
  `whiteboard_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_postit_whiteboard1_idx` (`whiteboard_id`),
  CONSTRAINT `fk_postit_whiteboard1` FOREIGN KEY (`whiteboard_id`) REFERENCES `whiteboard` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `postit` WRITE;
/*!40000 ALTER TABLE `postit` DISABLE KEYS */;

INSERT INTO `postit` (`id`, `title`, `information`, `whiteboard_id`)
VALUES
	(19,'Flummo','Mitt flum medelande.',22),
	(21,'Sindar','Alv alv alv...',23),
	(29,'Lite','Kul i jul...',22),
	(30,'Snurre','Sprätt.',22),
	(31,'Fungerar','Riktigt bra ju!!!',22),
	(32,'Varför','Detta trams!',22);

/*!40000 ALTER TABLE `postit` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table whiteboard
# ------------------------------------------------------------

DROP TABLE IF EXISTS `whiteboard`;

CREATE TABLE `whiteboard` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `whiteboard` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `whiteboard` WRITE;
/*!40000 ALTER TABLE `whiteboard` DISABLE KEYS */;

INSERT INTO `whiteboard` (`id`, `whiteboard`)
VALUES
	(22,'Gris gruppen'),
	(23,'Woppa grupp'),
	(24,'Ny grupp');

/*!40000 ALTER TABLE `whiteboard` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
