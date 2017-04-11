CREATE DATABASE  IF NOT EXISTS `vehicle_tracking_system` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `vehicle_tracking_system`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: vehicle_tracking_system
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.21-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `image_journey`
--

DROP TABLE IF EXISTS `image_journey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_journey` (
  `schedule_id` int(11) NOT NULL,
  `link_image` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  KEY `schedule_id` (`schedule_id`),
  CONSTRAINT `image_journey_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_journey`
--

LOCK TABLES `image_journey` WRITE;
/*!40000 ALTER TABLE `image_journey` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_journey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `migrations`
--

DROP TABLE IF EXISTS `migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `migrations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `migrations`
--

LOCK TABLES `migrations` WRITE;
/*!40000 ALTER TABLE `migrations` DISABLE KEYS */;
/*!40000 ALTER TABLE `migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_user` (
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `role_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `role_user_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_user`
--

LOCK TABLES `role_user` WRITE;
/*!40000 ALTER TABLE `role_user` DISABLE KEYS */;
INSERT INTO `role_user` VALUES (6,1,NULL),(7,1,NULL);
/*!40000 ALTER TABLE `role_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (0,'staff','Staff'),(1,'driver','Driver');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `schedule_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `description` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `intend_start_time` datetime DEFAULT NULL,
  `intend_end_time` datetime DEFAULT NULL,
  `start_point_address` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `location_lat_start` double DEFAULT NULL,
  `location_long_start` double DEFAULT NULL,
  `end_point_address` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `location_lat_end` double DEFAULT NULL,
  `location_long_end` double DEFAULT NULL,
  `real_start_time` datetime DEFAULT NULL,
  `real_end_time` datetime DEFAULT NULL,
  `schedule_status_type_id` int(11) DEFAULT NULL,
  `device_id` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `driver_id` (`driver_id`),
  KEY `vehicle_id` (`vehicle_id`),
  KEY `schedule_ibfk_3` (`schedule_status_type_id`),
  KEY `device_id` (`device_id`),
  CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`),
  CONSTRAINT `schedule_ibfk_3` FOREIGN KEY (`schedule_status_type_id`) REFERENCES `schedule_status` (`schedule_status_type_id`),
  CONSTRAINT `schedule_ibfk_4` FOREIGN KEY (`device_id`) REFERENCES `token` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,6,3,NULL,'2017-03-19 08:00:00','2017-03-19 22:00:00',NULL,9.234667,102.466743,NULL,12.244567,108.355529,NULL,NULL,1,'1234'),(2,7,2,NULL,'2017-03-19 13:00:00','2017-03-20 09:00:00',NULL,10.234668,101.466745,NULL,14.244357,107.456532,NULL,NULL,1,'5678');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_active`
--

DROP TABLE IF EXISTS `schedule_active`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_active` (
  `schedule_id` int(11) NOT NULL,
  `location_lat` double DEFAULT NULL,
  `location_long` double DEFAULT NULL,
  `fuel` float DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `device_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `schedule_id` (`schedule_id`),
  KEY `device_id` (`device_id`),
  CONSTRAINT `schedule_active_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`),
  CONSTRAINT `schedule_active_ibfk_2` FOREIGN KEY (`device_id`) REFERENCES `token` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_active`
--

LOCK TABLES `schedule_active` WRITE;
/*!40000 ALTER TABLE `schedule_active` DISABLE KEYS */;
INSERT INTO `schedule_active` VALUES (1,12.454356,107.983723,120343,80,'2017-03-19 09:32:54','1234'),(1,15.674321,101.665478,200343,82,'2017-03-20 14:02:25','1234'),(1,17.674321,102.544212,245221,66,'2017-03-20 22:45:23','1234'),(2,15.674321,101.665478,450353,71,'2017-03-12 19:20:43','8765'),(2,15.454356,107.983723,653421,81,'2017-03-22 08:12:23','8765');
/*!40000 ALTER TABLE `schedule_active` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_status`
--

DROP TABLE IF EXISTS `schedule_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_status` (
  `schedule_status_type_id` int(11) NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  PRIMARY KEY (`schedule_status_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_status`
--

LOCK TABLES `schedule_status` WRITE;
/*!40000 ALTER TABLE `schedule_status` DISABLE KEYS */;
INSERT INTO `schedule_status` VALUES (1,'DANG CHAY'),(2,'DA HOAN THANH'),(3,'DA HUY');
/*!40000 ALTER TABLE `schedule_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `device_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `time` datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES ('1234','2017-03-26 08:00:00',NULL),('4321','2017-03-26 14:45:10',NULL),('5678','2017-03-26 09:05:23',NULL),('8765','2017-03-27 10:04:03',NULL),('abcd','2017-03-28 15:00:00',NULL);
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_status`
--

DROP TABLE IF EXISTS `user_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_status` (
  `user_status_type_id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`user_status_type_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_status`
--

LOCK TABLES `user_status` WRITE;
/*!40000 ALTER TABLE `user_status` DISABLE KEYS */;
INSERT INTO `user_status` VALUES (1,'Active','Active'),(2,'Inactive','Inactive');
/*!40000 ALTER TABLE `user_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` varchar(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `user_status_type_id` int(11) DEFAULT NULL,
  `remember_token` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `forgot_code` varchar(6) DEFAULT NULL,
  `forgot_code_created` timestamp NULL DEFAULT NULL,
  `reset_password_token` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `driver_status_type_id` (`user_status_type_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`user_status_type_id`) REFERENCES `user_status` (`user_status_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (6,'tri','$2y$10$wwQgk9Oo.ZEUn2wipLiadee.djhUX9lExTOWnJcq.exeqrIcFgQwe','tri@gmail.com',NULL,NULL,NULL,NULL,1,'7I6VDNOdEU0kE3Hn4HOSybqdeSX8XdU4lyZRdnPl9s2HELGpJmLWe3z4HlGw','2017-04-03 02:17:16','2017-04-10 21:18:37',NULL,NULL,NULL,NULL),(7,'tuan','$2y$10$ehIyHT9JsIiehaNV0N7wN.Idgwx1ngWojlOqmKp.6TC9pkRK5Y/jC','tuan@gmail.com','0969456713','1995-06-09','Nam','KTX khu B',1,'oNSNHME22iTRM1wUaKuDvGQGFWcI2m4lZIYeccnRSWKBskmh0C88NQv8uALa','2017-04-03 21:11:17','2017-04-10 23:23:07','https://avatarfiles.alphacoders.com/751/75182.jpg',NULL,NULL,'randomToken'),(8,'tri1','$2y$10$PhYmM/OgAK8BU4SBz1ZFpewIkyAfpW8l0fWvAK4n3fcajUAiYa50a','tri1@gmail.com',NULL,NULL,NULL,NULL,1,NULL,'2017-04-04 02:15:57','2017-04-04 02:15:57',NULL,NULL,NULL,NULL),(9,'tri1','$2y$10$zA8el17phMhx5LV.BrWN1O44ltiPZ0u.MomWUBg0BKxPPqtmRM7uS','tri2@gmail.com',NULL,NULL,NULL,NULL,1,NULL,'2017-04-04 02:18:53','2017-04-04 02:21:51',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_status`
--

DROP TABLE IF EXISTS `vehicle_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicle_status` (
  `vehicle_status_type_id` int(11) NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  PRIMARY KEY (`vehicle_status_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_status`
--

LOCK TABLES `vehicle_status` WRITE;
/*!40000 ALTER TABLE `vehicle_status` DISABLE KEYS */;
INSERT INTO `vehicle_status` VALUES (1,'DANG HOAT DONG'),(2,'DANG RANH'),(3,'HONG XE');
/*!40000 ALTER TABLE `vehicle_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicles` (
  `vehicle_id` int(11) NOT NULL AUTO_INCREMENT,
  `number_plate` varchar(20) NOT NULL,
  `vehicle_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `vehicle_status_type_id` int(11) DEFAULT NULL,
  `frame_number` varchar(50) DEFAULT NULL,
  `gross_ton` float DEFAULT NULL,
  `accreditation_date_start` date DEFAULT NULL,
  `accreditation_date_end` date DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`),
  KEY `vehicle_status_type_id` (`vehicle_status_type_id`),
  CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`vehicle_status_type_id`) REFERENCES `vehicle_status` (`vehicle_status_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,'51F 1245','Huyndai-91','Huyndai',1,NULL,NULL,NULL,NULL),(2,'86B2-0987','Huyndai-86','Huyndai',2,NULL,NULL,NULL,NULL),(3,'37F1-1234','Huyndai-02','Huyndai',1,NULL,NULL,NULL,NULL),(4,'49G3-6785','Huyndai-49','Huyndai',2,NULL,NULL,NULL,NULL),(5,'12B2-3454','Huyndai-12','Huyndai',3,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warning`
--

DROP TABLE IF EXISTS `warning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `warning` (
  `warning_id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) DEFAULT NULL,
  `location_lat` double DEFAULT NULL,
  `location_long` double DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `warning_type_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`warning_id`),
  KEY `driver_id` (`driver_id`),
  KEY `warning_type_id` (`warning_type_id`),
  CONSTRAINT `warning_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `warning_ibfk_2` FOREIGN KEY (`warning_type_id`) REFERENCES `warning_type` (`warning_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warning`
--

LOCK TABLES `warning` WRITE;
/*!40000 ALTER TABLE `warning` DISABLE KEYS */;
/*!40000 ALTER TABLE `warning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warning_type`
--

DROP TABLE IF EXISTS `warning_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `warning_type` (
  `warning_type_id` int(11) NOT NULL,
  `type` varchar(25) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `defaut_time` int(11) NOT NULL,
  PRIMARY KEY (`warning_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warning_type`
--

LOCK TABLES `warning_type` WRITE;
/*!40000 ALTER TABLE `warning_type` DISABLE KEYS */;
INSERT INTO `warning_type` VALUES (1,'Kẹt Xe',NULL,2),(2,'Picachu',NULL,3),(3,'Hỏng đường',NULL,4);
/*!40000 ALTER TABLE `warning_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-11 13:23:49
