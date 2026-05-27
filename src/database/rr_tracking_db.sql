CREATE DATABASE  IF NOT EXISTS `rr_tracking_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rr_tracking_db`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: rr_tracking_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Juan dela Cruz','juan@email.com','09171234567','Davao City','2026-05-26 23:26:10'),(2,'Maria Santos','maria@email.com','09281234567','Cebu City','2026-05-26 23:26:10');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `return_requests`
--

DROP TABLE IF EXISTS `return_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_requests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL,
  `customer_id` int NOT NULL,
  `product_name` varchar(150) NOT NULL,
  `return_reason` text NOT NULL,
  `return_date` date NOT NULL,
  `return_status` enum('REQUESTED','UNDER_REVIEW','ITEM_RECEIVED','REFUND_SENT') DEFAULT 'REQUESTED',
  `refund_status` enum('PENDING','APPROVED','REJECTED','COMPLETED') DEFAULT 'PENDING',
  `refund_amount` decimal(10,2) DEFAULT '0.00',
  `handled_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  KEY `handled_by` (`handled_by`),
  CONSTRAINT `return_requests_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `return_requests_ibfk_2` FOREIGN KEY (`handled_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `return_requests`
--

LOCK TABLES `return_requests` WRITE;
/*!40000 ALTER TABLE `return_requests` DISABLE KEYS */;
INSERT INTO `return_requests` VALUES (1,'11',1,'Barbie Doll','No Hair','2026-05-27','REQUESTED','PENDING',0.00,1,'2026-05-26 23:55:52','2026-05-26 23:55:52');
/*!40000 ALTER TABLE `return_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_logs`
--

DROP TABLE IF EXISTS `transaction_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `details` text,
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `transaction_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_logs`
--

LOCK TABLES `transaction_logs` WRITE;
/*!40000 ALTER TABLE `transaction_logs` DISABLE KEYS */;
INSERT INTO `transaction_logs` VALUES (1,1,'LOGIN','System Administrator logged in.','2026-05-26 23:37:21'),(2,1,'LOGIN','System Administrator logged in.','2026-05-26 23:42:55'),(3,1,'LOGIN','System Administrator logged in.','2026-05-26 23:43:19'),(4,1,'LOGIN','System Administrator logged in.','2026-05-26 23:43:36'),(5,1,'LOGIN','System Administrator logged in.','2026-05-26 23:43:52'),(6,1,'LOGIN','System Administrator logged in.','2026-05-26 23:44:09'),(7,1,'LOGIN','System Administrator logged in.','2026-05-26 23:44:41'),(8,1,'LOGIN','System Administrator logged in.','2026-05-26 23:46:48'),(9,1,'LOGIN','System Administrator logged in.','2026-05-26 23:48:43'),(10,1,'LOGIN','System Administrator logged in.','2026-05-26 23:49:14'),(11,1,'LOGIN','System Administrator logged in.','2026-05-26 23:49:30'),(12,1,'LOGIN','System Administrator logged in.','2026-05-26 23:49:52'),(13,1,'LOGIN','System Administrator logged in.','2026-05-26 23:50:17'),(14,1,'LOGIN','System Administrator logged in.','2026-05-26 23:50:53'),(15,1,'LOGIN','System Administrator logged in.','2026-05-26 23:51:08'),(16,1,'LOGIN','System Administrator logged in.','2026-05-26 23:52:09'),(17,1,'LOGIN','System Administrator logged in.','2026-05-26 23:52:33'),(18,1,'LOGIN','System Administrator logged in.','2026-05-26 23:52:59'),(19,1,'LOGIN','System Administrator logged in.','2026-05-26 23:53:14'),(20,1,'ADD_REQUEST','Added return request for Order ID: 11','2026-05-26 23:55:52'),(21,1,'LOGIN','System Administrator logged in.','2026-05-26 23:57:47'),(22,1,'ADD_USER','Added user: jo.cavan','2026-05-26 23:58:08'),(23,1,'LOGOUT','System Administrator logged out.','2026-05-26 23:58:09'),(24,3,'LOGIN','Jo Cavan logged in.','2026-05-26 23:58:16'),(25,3,'LOGIN','Jo Cavan logged in.','2026-05-26 23:59:21'),(26,3,'LOGIN','Jo Cavan logged in.','2026-05-27 00:00:18'),(27,3,'LOGIN','Jo Cavan logged in.','2026-05-27 00:00:42'),(28,3,'LOGIN','Jo Cavan logged in.','2026-05-27 00:01:01'),(29,1,'LOGIN','System Administrator logged in.','2026-05-27 00:01:39'),(30,1,'LOGIN','System Administrator logged in.','2026-05-27 00:02:11'),(31,1,'LOGIN','System Administrator logged in.','2026-05-27 00:02:41'),(32,1,'LOGIN','System Administrator logged in.','2026-05-27 00:03:12'),(33,1,'LOGIN','System Administrator logged in.','2026-05-27 00:22:31'),(34,1,'LOGIN','System Administrator logged in.','2026-05-27 00:24:14'),(35,1,'LOGIN','System Administrator logged in.','2026-05-27 00:24:43'),(36,1,'LOGIN','System Administrator logged in.','2026-05-27 00:25:16'),(37,1,'LOGIN','System Administrator logged in.','2026-05-27 00:25:51'),(38,1,'LOGIN','System Administrator logged in.','2026-05-27 00:26:12'),(39,1,'LOGIN','System Administrator logged in.','2026-05-27 00:28:48'),(40,1,'LOGIN','System Administrator logged in.','2026-05-27 00:29:09'),(41,1,'LOGIN','System Administrator logged in.','2026-05-27 00:54:26'),(42,1,'LOGOUT','System Administrator logged out.','2026-05-27 00:55:45'),(43,3,'LOGIN','Jo Cavan logged in.','2026-05-27 00:55:53'),(44,3,'LOGIN','Jo Cavan logged in.','2026-05-27 00:57:59'),(45,3,'LOGIN','Jo Cavan logged in.','2026-05-27 00:58:57'),(46,1,'LOGIN','System Administrator logged in.','2026-05-27 01:01:00'),(47,1,'LOGIN','System Administrator logged in.','2026-05-27 01:01:32'),(48,1,'LOGOUT','System Administrator logged out.','2026-05-27 01:01:43'),(49,1,'LOGIN','System Administrator logged in.','2026-05-27 01:05:22'),(50,1,'LOGOUT','System Administrator logged out.','2026-05-27 01:05:41'),(51,1,'LOGIN','System Administrator logged in.','2026-05-27 01:10:43'),(52,1,'LOGOUT','System Administrator logged out.','2026-05-27 01:11:02');
/*!40000 ALTER TABLE `transaction_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',
  `full_name` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin123','ADMIN','System Administrator','2026-05-26 23:26:10'),(2,'staff1','staff123','STAFF','Jane Reyes','2026-05-26 23:26:10'),(3,'jo.cavan','staff123','STAFF','Jo Cavan','2026-05-26 23:58:08');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-27  9:25:15
