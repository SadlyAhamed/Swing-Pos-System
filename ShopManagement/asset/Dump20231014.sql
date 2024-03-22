-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: shopmanagement
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `customar`
--

DROP TABLE IF EXISTS `customar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customarname` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customar`
--

LOCK TABLES `customar` WRITE;
/*!40000 ALTER TABLE `customar` DISABLE KEYS */;
INSERT INTO `customar` VALUES (1,'hasan','01768098654','mirpur'),(2,'limon','0179876535','rajbari'),(3,'masud','0179876535','soriatpur');
/*!40000 ALTER TABLE `customar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeetable`
--

DROP TABLE IF EXISTS `employeetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employeetable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `phonenumber` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `salary` float DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeetable`
--

LOCK TABLES `employeetable` WRITE;
/*!40000 ALTER TABLE `employeetable` DISABLE KEYS */;
INSERT INTO `employeetable` VALUES (1,'limon','076543756575','sahabag',20000,'2023-09-03'),(2,'romjan','076543756575','mirpur',25000,'2023-09-03'),(3,'sabit','076548967574','mirpur-10, dhaka 1225',35500,'2023-09-03'),(4,'rohim','07654896776','mirpur-2, dhaka 1225',21500,'2023-09-03');
/*!40000 ALTER TABLE `employeetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasetable`
--

DROP TABLE IF EXISTS `purchasetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasetable` (
  `purchaseid` int NOT NULL AUTO_INCREMENT,
  `productname` varchar(45) DEFAULT NULL,
  `unitprice` float DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `totalprice` float DEFAULT NULL,
  `purchasedate` date DEFAULT NULL,
  `supplier` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`purchaseid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasetable`
--

LOCK TABLES `purchasetable` WRITE;
/*!40000 ALTER TABLE `purchasetable` DISABLE KEYS */;
INSERT INTO `purchasetable` VALUES (1,'mouse',2000,50,100000,'2023-10-01','Nur Telicome'),(2,'keybord',4000,50,100000,'2023-10-02','Nur Telicome'),(3,'ram',4030,30,120900,'2023-10-03','Bornali Telicome'),(4,'ssd',30000,30,900000,'2023-10-01','Fast Line ltd.');
/*!40000 ALTER TABLE `purchasetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salestable`
--

DROP TABLE IF EXISTS `salestable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salestable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `unit` float DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `totalPrice` float DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `actualPrice` float DEFAULT NULL,
  `cashReceive` float DEFAULT NULL,
  `cashReturn` float DEFAULT NULL,
  `customarName` varchar(45) DEFAULT NULL,
  `selerid` varchar(15) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salestable`
--

LOCK TABLES `salestable` WRITE;
/*!40000 ALTER TABLE `salestable` DISABLE KEYS */;
INSERT INTO `salestable` VALUES (1,'keybord',5000,10,50000,5,47500,47500,0,'limon','romjan',NULL),(2,'ssd',6000,7,42000,4,40320,40400,-80,'hasan','sabit',NULL),(3,'mouse',2200,5,11000,2,10780,10800,-20,'limon','romjan',NULL);
/*!40000 ALTER TABLE `salestable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stocktable`
--

DROP TABLE IF EXISTS `stocktable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stocktable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `unitprice` float(8,2) DEFAULT NULL,
  `quantity` float(8,2) DEFAULT '0.00',
  `totalamount` float(12,2) DEFAULT NULL,
  PRIMARY KEY (`id`,`name`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stocktable`
--

LOCK TABLES `stocktable` WRITE;
/*!40000 ALTER TABLE `stocktable` DISABLE KEYS */;
INSERT INTO `stocktable` VALUES (1,'mouse',2000.00,50.00,100000.00),(2,'keybord',4000.00,50.00,100000.00),(3,'ram',4030.00,30.00,120900.00),(4,'ssd',30000.00,30.00,900000.00);
/*!40000 ALTER TABLE `stocktable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliertable`
--

DROP TABLE IF EXISTS `suppliertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliertable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `number` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `productname` varchar(45) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliertable`
--

LOCK TABLES `suppliertable` WRITE;
/*!40000 ALTER TABLE `suppliertable` DISABLE KEYS */;
INSERT INTO `suppliertable` VALUES (1,'sojib Fashion','0873465454856','dhanmondi','keybord',40),(2,'sojib telicom','0873465454856','dhanmondi','mouse',40),(3,'a4 tech telicom','0873465454856','dhanmondi, dhaka 1200','mouse',40),(4,'Raju','234324','dfdsjk','keybord',20),(5,'hasib','234324','gazipur','mouse',40),(6,'sofiq','234324','gazipur','mouse',40);
/*!40000 ALTER TABLE `suppliertable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-14  1:39:15
