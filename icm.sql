-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: icm
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Table structure for table `actions_needed`
--

DROP TABLE IF EXISTS `actions_needed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actions_needed` (
  `idrequest` int(11) NOT NULL,
  `idCharge` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `stage` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `actionsNeeded` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actions_needed`
--

LOCK TABLES `actions_needed` WRITE;
/*!40000 ALTER TABLE `actions_needed` DISABLE KEYS */;
/*!40000 ALTER TABLE `actions_needed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_report`
--

DROP TABLE IF EXISTS `activity_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_report` (
  `numberofrequests` int(11) DEFAULT NULL,
  `numrejectedrequests` int(11) NOT NULL,
  `workdaysintreatment` int(11) NOT NULL,
  `median` int(11) NOT NULL,
  `standarddeviation` int(11) NOT NULL,
  `frequencydistribution` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_report`
--

LOCK TABLES `activity_report` WRITE;
/*!40000 ALTER TABLE `activity_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checking_failure`
--

DROP TABLE IF EXISTS `checking_failure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checking_failure` (
  `idrequest` int(11) NOT NULL,
  `failure` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idrequest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checking_failure`
--

LOCK TABLES `checking_failure` WRITE;
/*!40000 ALTER TABLE `checking_failure` DISABLE KEYS */;
INSERT INTO `checking_failure` VALUES (2,'ddd'),(3,'jjj'),(4,'gg'),(21,'jjjj');
/*!40000 ALTER TABLE `checking_failure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `iddocument` int(11) NOT NULL,
  `fileName` varchar(45) NOT NULL,
  `fileType` varchar(45) NOT NULL,
  `path` varchar(300) NOT NULL,
  `idrequest` varchar(45) NOT NULL,
  UNIQUE KEY `iddocument_UNIQUE` (`iddocument`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
INSERT INTO `document` VALUES (1,'0_1','pdf','C:/ICM/Documents/0_1.pdf/','0'),(2,'2_1','pdf','C:/ICM/Documents/2_1.pdf/','2'),(3,'1_1','pdf','C:/ICM/Documents/1_1.pdf/','1'),(4,'1_2','pdf','C:/ICM/Documents/1_2.pdf/','1'),(5,'1_3','pdf','C:/ICM/Documents/1_3.pdf/','1'),(6,'1_4','pdf','C:/ICM/Documents/1_4.pdf/','1'),(7,'2_1','pdf','C:/ICM/Documents/2_1.pdf/','2'),(8,'7_1','pdf','C:/ICM/Documents/7_1.pdf/','7'),(9,'9_1','pdf','C:/ICM/Documents/9_1.pdf/','9');
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `iduser` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `employeeTitle` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `role` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('123','Information Engineer','Review Leader'),('123123','Information Engineer','Manager'),('12321','Information Engineer','Review Member'),('1234','Information Engineer','Inspector'),('2','Information Engineer',''),('321321','Information Engineer',NULL),('33522','Information Engineer','Review Member');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_report`
--

DROP TABLE IF EXISTS `evaluation_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_report` (
  `idreq` int(11) NOT NULL,
  `location` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `risk` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `time` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idreq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_report`
--

LOCK TABLES `evaluation_report` WRITE;
/*!40000 ALTER TABLE `evaluation_report` DISABLE KEYS */;
INSERT INTO `evaluation_report` VALUES (1,'loc13','des13','res31','risk13','time13'),(2,'loc2','des2','res2','risk2','time2'),(3,'loc3','desc3','res3','risk3','time3'),(4,'gg','gg','gg','gg','gg'),(5,'gg','gg','gg','gg','gg'),(6,'qweqw','eqweqw','eqweqwe','qweqweqwe','asdas'),(21,'loc21','des21','res21','risk21','time21');
/*!40000 ALTER TABLE `evaluation_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `idMessage` int(11) NOT NULL AUTO_INCREMENT,
  `iduser` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `titleMessage` varchar(400) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `contentMessage` varchar(400) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `dateMessage` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idMessage`),
  UNIQUE KEY `idMessage_UNIQUE` (`idMessage`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (8,'123','One Day Left For Stage: Evaluation','One day left to finish stage for request id: 7','18/01/2020','','ALERT'),(9,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','18/01/2020','','ALERT'),(10,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','18/01/2020','','ALERT'),(11,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','18/01/2020','','ALERT'),(12,'','One Day Left For Stage: Review','One day left to finish stage for request id: 4','19/01/2020','','ALERT'),(13,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(14,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(15,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(16,'123','One Day Left For Stage: Evaluation','One day left to finish stage for request id: 8','19/01/2020','','ALERT'),(17,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(18,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(19,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(20,'33522','One Day Left For Stage: Evaluation','One day left to finish stage for request id: 22','19/01/2020','','ALERT'),(21,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(22,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(23,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(24,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(25,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(26,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(27,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(28,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(29,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(30,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(31,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(32,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(33,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(34,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(35,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(36,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(37,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(38,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(39,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(40,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(41,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(42,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(43,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(44,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(45,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(46,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(47,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(48,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(49,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(50,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(51,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(52,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(53,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(54,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(55,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(56,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(57,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(58,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(59,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(60,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(61,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(62,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(63,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(64,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(65,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(66,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(67,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(68,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','19/01/2020','','ALERT'),(69,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(70,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(71,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','19/01/2020','','ALERT'),(72,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(73,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(74,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','19/01/2020','','ALERT'),(75,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(76,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(77,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','19/01/2020','','ALERT'),(78,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(79,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(80,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','19/01/2020','','ALERT'),(81,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(82,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(83,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(84,'','One Day Left For Stage: Review','One day left to finish stage for request id: 5','20/01/2020','','ALERT'),(85,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(86,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(87,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(88,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(89,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(90,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(91,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(92,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(93,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(94,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(95,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(96,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(97,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(98,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(99,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(100,'','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(101,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(102,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(103,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(104,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(105,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(106,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(107,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(108,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(109,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(110,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(111,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(112,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(113,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(114,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(115,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(116,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(117,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(118,'','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(119,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(120,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(121,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(122,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(123,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(124,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(125,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(126,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(127,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(128,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(129,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(130,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(131,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(132,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(133,'','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(134,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(135,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 4','20/01/2020','','ALERT'),(136,'','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(137,'123123','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(138,'1234','Time Exception For Stage: Review','Time exception in stage for request id: 5','20/01/2020','','ALERT'),(139,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(140,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(141,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 7','20/01/2020','','ALERT'),(142,'123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(143,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(144,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 8','20/01/2020','','ALERT'),(145,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(146,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(147,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 21','20/01/2020','','ALERT'),(148,'33522','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(149,'123123','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT'),(150,'1234','Time Exception For Stage: Evaluation','Time exception in stage for request id: 22','20/01/2020','','ALERT');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_behind_report`
--

DROP TABLE IF EXISTS `performance_behind_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_behind_report` (
  `median` int(11) NOT NULL,
  `standarddeviation` int(11) NOT NULL,
  `distributionofdelays` int(11) NOT NULL,
  `durationofdelay` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_behind_report`
--

LOCK TABLES `performance_behind_report` WRITE;
/*!40000 ALTER TABLE `performance_behind_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `performance_behind_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_report`
--

DROP TABLE IF EXISTS `performance_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_report` (
  `durationapprovedextensions` int(11) NOT NULL,
  `durationactivitytimeadded` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_report`
--

LOCK TABLES `performance_report` WRITE;
/*!40000 ALTER TABLE `performance_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `performance_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request` (
  `idrequest` int(11) NOT NULL,
  `currentState` varchar(400) NOT NULL,
  `changeRequested` varchar(400) NOT NULL,
  `requestPurpose` varchar(400) NOT NULL,
  `comments` varchar(400) DEFAULT NULL,
  `ITSystem` varchar(45) NOT NULL,
  `submissionDate` varchar(45) NOT NULL,
  `iduser` varchar(45) NOT NULL,
  `RequestStatus` varchar(45) NOT NULL,
  `totalTime` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idrequest`),
  UNIQUE KEY `idrequest_UNIQUE` (`idrequest`),
  KEY `fk_request_user_idx` (`iduser`),
  CONSTRAINT `fk_user_user` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
INSERT INTO `request` VALUES (1,'f','f','f','f','Moodle','04/01/2020','12321','opened','0'),(2,'h','h','h','h','Office','05/01/2020','12321','opened','0'),(3,'asdas','dasdasd','asdasd','asdasdasd','Office','06/01/2020','12321','opened','0'),(4,'asdasd','asdasda','sdasdasd','asdasdas','Library','06/01/2020','12321','frozen','6'),(5,'asdasd','asdasda','sdasdasd','asdasdas','Library','06/01/2020','12321','frozen','8'),(6,'asdasd','asdasda','sdasdasd','asdasdas','Library','06/01/2020','12321','frozen','0'),(7,'asdasd','asdsdaasd','asdsadasdsa','asdasdas','Moodle','06/01/2020','12321','frozen','2'),(8,'asdasd','asdsdaasd','asdsadasdsa','asdasdas','Moodle','06/01/2020','12321','opened','3'),(9,'asd','asd','asd','asd','Office','06/01/2020','12321','opened','0'),(10,'asd','asd','asd','asd','Office','06/01/2020','12321','opened','0'),(11,'gg','gg','gg','null','Moodle','06/01/2020','12321','opened','0'),(12,'vv','vv','vv','null','Library','07/01/2020','12321','opened','0'),(13,'nn','nn','nn','null','Braude Website','07/01/2020','12321','opened','0'),(14,'vbb','bbb','bbb','null','Info Braude','07/01/2020','12321','opened','0'),(15,'qweqwe','qweqwe','qweqweqwe','qweqweqweqweqwe','Moodle','10/01/2020','1234','opened','0'),(16,'qweqwe','qweqwe','qweqweqwe','qweqweqweqweqwe','Moodle','10/01/2020','1234','opened','0'),(17,'qweqwe','qweqwe','qweqweqwe','qweqweqweqweqwe','Moodle','10/01/2020','1234','opened','0'),(18,'asdasd','asdasdas','dasdasdasd','asdasdasdas','Moodle','10/01/2020','1234','opened','0'),(19,'asdasdas','dasdasdas','dasdasdasdas','dasdasdasdasdasdasd','Moodle','14/01/2020','1234','opened','0'),(20,'asdasdas','dasdasdas','dasdasdasd','asdasdasdasdasdasdasd','Moodle','16/01/2020','1234','opened','0'),(21,'qweqw','eqweqw','eqweqwe','qweqweqweqw','Moodle','16/01/2020','1234','frozen','1'),(22,'ggg','gg','ggg','null','Info Braude','16/01/2020','123','opened','3'),(23,'fbf','fbb','fbf','null','Library','16/01/2020','123','opened','0'),(24,'gg','gg','gg','null','Moodle','16/01/2020','123','opened','0'),(25,'ggg','ggg','gg','null','Office','16/01/2020','123','opened','0'),(26,'afafa','sfafas','asfasfasf','asfasfasf','Moodle','17/01/2020','12321','opened','0'),(27,'asdfsda','fsadfs','adfsdfs','adfasdf','Office','20/01/2020','1234','not started','0');
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request_handling`
--

DROP TABLE IF EXISTS `request_handling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request_handling` (
  `idrequest` int(11) NOT NULL,
  `idCharge` varchar(45) NOT NULL,
  `executionTime` varchar(45) DEFAULT NULL,
  `currentStage` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idrequest`),
  KEY `iduser_idx` (`idCharge`),
  KEY `idrequest_idx` (`idrequest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request_handling`
--

LOCK TABLES `request_handling` WRITE;
/*!40000 ALTER TABLE `request_handling` DISABLE KEYS */;
INSERT INTO `request_handling` VALUES (1,'','','Closing',NULL),(2,'','','Closing',NULL),(3,'','','Closing',NULL),(4,'','0','Review','frozen'),(5,'','0','Review','frozen'),(6,'321321','','Evaluation','frozen'),(7,'123','0','Evaluation','frozen'),(8,'123','0','Evaluation',NULL),(21,'1234','0','Evaluation','frozen'),(22,'33522','0','Evaluation',''),(23,'12321','','Evaluation',''),(24,'1234','','Evaluation',''),(25,'33522','','Evaluation',''),(26,'12321','','Evaluation',''),(27,'2','','Evaluation','');
/*!40000 ALTER TABLE `request_handling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_decision`
--

DROP TABLE IF EXISTS `review_decision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_decision` (
  `idrequest` int(11) NOT NULL,
  `decision` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idrequest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_decision`
--

LOCK TABLES `review_decision` WRITE;
/*!40000 ALTER TABLE `review_decision` DISABLE KEYS */;
INSERT INTO `review_decision` VALUES (1,'Approve'),(3,'Approve'),(4,'Approve'),(21,'Approve');
/*!40000 ALTER TABLE `review_decision` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `systems`
--

DROP TABLE IF EXISTS `systems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `systems` (
  `systemName` varchar(45) NOT NULL,
  `iduser` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`systemName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `systems`
--

LOCK TABLES `systems` WRITE;
/*!40000 ALTER TABLE `systems` DISABLE KEYS */;
INSERT INTO `systems` VALUES ('Braude Website',''),('Info Braude',''),('Library',''),('Moodle','1234'),('Office','');
/*!40000 ALTER TABLE `systems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_requests`
--

DROP TABLE IF EXISTS `time_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_requests` (
  `currentStage` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `idRequest` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `idCharge` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `timeRequested` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `idTimeRequest` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `reason` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idTimeRequest`),
  UNIQUE KEY `idTimeRequest_UNIQUE` (`idTimeRequest`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_requests`
--

LOCK TABLES `time_requests` WRITE;
/*!40000 ALTER TABLE `time_requests` DISABLE KEYS */;
INSERT INTO `time_requests` VALUES ('Evaluation','6','123','2','Approved',5,'Extend','reason1');
/*!40000 ALTER TABLE `time_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `iduser` varchar(45) NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  `department` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `iduser_UNIQUE` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1','Moshe','Cohen','moshe.c@gmail.com','Worker',' ','123'),('123','Udi','Dudi','udi@walla.co.il','Worker','Information Technology','123'),('12312','Eliran','Sabag','e.Sabag@gmail.com','Worker','','123'),('123123','Adir','Miller','Amiller@gmail.com','Worker','Information Technology','123'),('12321','Avi','Dada','avi.d@yahoo.com','Worker','Information Technology','123'),('1234','Sassi','haga','sassih@gmail.com','Worker','Information Technology','123'),('15486','Yakir','Cohen','yakirc@nana10.co.il','Lecturer','Software','123'),('2','Dudo','Aharon','dudu.a@gmail.com','Worker','Information Technology','123'),('3','Yonit','Levi','yonit.l@gmail.com','Worker',' ','123'),('321321','Gabi','Ploni','gploni@gmail.com','Worker','Information Technology','123'),('33522','David','Oivey','oidavid@gmail.com','Worker','Information Technology','123'),('4','Jovani','Roso','jovani.r@gmail.com','Worker',' ','123'),('5','Ora','Dahan','orah.d@gmail.com','Worker',' ','123');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-20 14:45:46
