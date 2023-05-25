-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: vps817240.ovh.net    Database: info_team01_schema
-- ------------------------------------------------------
-- Server version	8.0.33-0ubuntu0.20.04.2

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
-- Table structure for table `athlete`
--

DROP TABLE IF EXISTS `athlete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `athlete` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `birthdate` date NOT NULL,
  `genre` varchar(255) NOT NULL,
  `discipline` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `discipline` (`discipline`),
  CONSTRAINT `athlete_ibfk_1` FOREIGN KEY (`discipline`) REFERENCES `discipline` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=220 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athlete`
--

LOCK TABLES `athlete` WRITE;
/*!40000 ALTER TABLE `athlete` DISABLE KEYS */;
INSERT INTO `athlete` VALUES (3,'Chevalier','Lucas','France','2001-11-06','homme','Football'),(4,'Saliba','William','France','2001-03-24','homme','Football'),(5,'Tanguy','Kouassi','France','2002-06-07','homme','Football'),(6,'Badiashile','Benoît','France','2001-03-26','homme','Football'),(7,'Lukeba','Castello','France','2002-12-17','homme','Football'),(8,'Gusto','Malo','France','2003-05-19','homme','Football'),(9,'Truffert','Adrien','France','2001-11-20','homme','Football'),(10,'Merlin','Quentin','France','2002-05-16','homme','Football'),(11,'Camavinga','Eduardo','France','2002-11-10','homme','Football'),(12,'Lepenant','Johann','France','2002-10-22','homme','Football'),(13,'Thuram','Khéphren','France','2001-03-26','homme','Football'),(14,'Koné','Manu','France','2001-05-17','homme','Football'),(15,'Zaïre-Emery','Warren','France','2006-03-08','homme','Football'),(16,'Doué','Désiré','France','2005-06-03','homme','Football'),(17,'Cherki','Rayan','France','2003-03-17','homme','Football'),(18,'Ben Seghir','Eliesse','France','2005-02-16','homme','Football'),(19,'Cho','Mohamed-Ali','France','2004-01-19','homme','Football'),(20,'Tel','Mathys','France','2005-04-27','homme','Football'),(21,'Wahi','Elye','France','2003-01-02','homme','Football'),(22,'Kalimuendo','Arnaud','France','2002-01-20','homme','Football'),(23,'Rutter','Georginio','France','2002-04-20','homme','Football');
/*!40000 ALTER TABLE `athlete` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discipline`
--

DROP TABLE IF EXISTS `discipline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discipline` (
  `name` varchar(255) NOT NULL,
  `flag` tinyint(1) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discipline`
--

LOCK TABLES `discipline` WRITE;
/*!40000 ALTER TABLE `discipline` DISABLE KEYS */;
INSERT INTO `discipline` VALUES ('Athlétisme',1),('Basketball',1),('Canoë-kayak',0),('Cyclisme',1),('Escalade',0),('Escrime',0),('Football',1),('Golf',0),('Gymnastique',1),('Handball',0),('Judo',0),('Natation',0),('Natation en eau libre',1),('Natation synchronisée',1),('Sport automobile',0),('Tennis',0),('Tir',0),('Voile',0),('Volleyball',0);
/*!40000 ALTER TABLE `discipline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session` (
  `code` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `fromHour` time NOT NULL,
  `toHour` time NOT NULL,
  `discipline` varchar(255) NOT NULL,
  `site` int NOT NULL,
  `description` text NOT NULL,
  `type` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  PRIMARY KEY (`code`),
  KEY `discipline` (`discipline`),
  KEY `site` (`site`),
  CONSTRAINT `session_ibfk_1` FOREIGN KEY (`discipline`) REFERENCES `discipline` (`name`),
  CONSTRAINT `session_ibfk_2` FOREIGN KEY (`site`) REFERENCES `site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session`
--

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` VALUES ('BAS97','2023-05-23','21:00:00','23:00:00','Basketball',4,'Boulogne-Levallois vs Cholet','qualifications','homme'),('FBL01','2024-07-24','11:00:00','13:00:00','Football',1,'groupe C (C1vsC2)','qualifications','homme'),('FBL02','2024-07-24','15:00:00','17:00:00','Football',2,'groupe B (B1vsB2)','qualifications','Homme'),('FBL03','2024-07-24','17:00:00','19:00:00','Football',3,'groupe C (C3vsC4)','qualifications','Homme'),('FBL04','2024-07-24','17:00:00','19:00:00','Football',4,'groupe A (A3vsA4)','qualifications','Homme'),('FBL05','2024-07-24','19:00:00','21:00:00','Football',5,'groupe D (D1vsD2)','qualifications','Homme'),('FBL06','2024-07-24','19:00:00','21:00:00','Football',6,'groupe B (B3vsB4)','qualifications','Homme'),('FBL07','2024-07-24','21:00:00','23:00:00','Football',1,'groupe D (D3vsD4)','qualifications','Homme'),('FBL08','2024-07-24','21:00:00','23:00:00','Football',7,'groupe A (A1vsA2)','qualifications','Homme'),('FBL09','2024-07-25','17:00:00','19:00:00','Football',3,'groupe C (C1vsC2)','qualifications','Femme'),('FBL10','2024-07-25','17:00:00','19:00:00','Football',2,'groupe A (A3vsA4)','qualifications','Femme'),('FBL11','2024-07-25','19:00:00','21:00:00','Football',5,'groupe C (C3vsC4)','qualifications','Femme'),('FBL12','2024-07-25','19:00:00','21:00:00','Football',7,'groupe B (B3vsB4)','qualifications','Femme'),('FBL13','2024-07-25','21:00:00','23:00:00','Football',6,'groupe A (A1vsA2)','qualifications','Femme'),('FBL14','2024-07-25','21:00:00','23:00:00','Football',4,'groupe B (B1vsB2)','qualifications','Femme'),('FBL15','2024-07-27','15:00:00','17:00:00','Football',5,'groupe C (C4vsC2)','qualifications','Homme'),('FBL16','2024-07-27','15:00:00','17:00:00','Football',6,'groupe B (B1vsB3)','qualifications','Homme'),('FBL17','2024-07-27','17:00:00','19:00:00','Football',3,'groupe C (C1vsC3)','qualifications','Homme'),('FBL18','2024-07-27','17:00:00','19:00:00','Football',2,'groupe B (B4vsB2)','qualifications','Homme'),('FBL19','2024-07-27','19:00:00','21:00:00','Football',1,'groupe D (D2vsD4)','qualifications','Homme'),('FBL20','2024-07-27','19:00:00','21:00:00','Football',7,'groupe A (A4vsA2)','qualifications','Homme'),('FBL21','2024-07-27','21:00:00','23:00:00','Football',5,'groupe D (D1vsD3)','qualifications','Homme'),('FBL22','2024-07-27','21:00:00','23:00:00','Football',4,'groupe A (A1vsA3)','qualifications','Homme'),('FBL23','2024-07-28','17:00:00','19:00:00','Football',1,'groupe C (C4vsC2)','qualifications','Femme'),('FBL24','2024-07-28','17:00:00','19:00:00','Football',6,'groupe A (A4vsA2)','qualifications','Femme'),('FBL25','2024-07-28','19:00:00','21:00:00','Football',3,'groupe C (C1vsC3)','qualifications','Femme'),('FBL26','2024-07-28','19:00:00','21:00:00','Football',4,'groupe B (B4vsB2)','qualifications','Femme'),('FBL27','2024-07-28','21:00:00','23:00:00','Football',2,'groupe A (A1vsA3)','qualifications','Femme'),('FBL28','2024-07-28','21:00:00','23:00:00','Football',7,'groupe B (B1vsB3)','qualifications','Femme'),('FBL29','2024-07-30','15:00:00','17:00:00','Football',1,'groupe C (C4vsC1)','qualifications','Homme'),('FBL30','2024-07-30','15:00:00','17:00:00','Football',5,'groupe C (C2vsC3)','qualifications','Homme'),('FBL31','2024-07-30','17:00:00','19:00:00','Football',6,'groupe B (B4vsB1)','qualifications','Homme'),('FBL32','2024-07-30','17:00:00','19:00:00','Football',4,'groupe B (B2vsB3)','qualifications','Homme'),('FBL33','2024-07-30','19:00:00','21:00:00','Football',2,'groupe A (A2vsA3)','qualifications','Homme'),('FBL34','2024-07-30','19:00:00','21:00:00','Football',7,'groupe A (A4vsA1)','qualifications','Homme'),('FBL35','2024-07-30','21:00:00','23:00:00','Football',1,'groupe D (D2vsD3)','qualifications','Homme'),('FBL36','2024-07-30','21:00:00','23:00:00','Football',3,'groupe D (D4vsD1)','qualifications','Homme'),('FBL37','2024-07-31','17:00:00','19:00:00','Football',3,'groupe C (C2vsC3)','qualifications','Femme'),('FBL38','2024-07-31','17:00:00','19:00:00','Football',5,'groupe C (C4vsC1)','qualifications','Femme'),('FBL39','2024-07-31','19:00:00','21:00:00','Football',2,'groupe B (B2vsB3)','qualifications','Femme'),('FBL40','2024-07-31','19:00:00','21:00:00','Football',7,'groupe B (B4vsB1)','qualifications','Femme'),('FBL41','2024-07-31','21:00:00','23:00:00','Football',6,'groupe A (A4vsA1)','qualifications','Femme'),('FBL42','2024-07-31','21:00:00','23:00:00','Football',4,'groupe A (A2vsA3)','qualifications','Femme'),('FBL43','2024-08-02','15:00:00','18:00:00','Football',1,'(1Bvs2A)','qualifications','Homme'),('FBL44','2024-08-02','18:00:00','21:00:00','Football',6,'(1Dvs2C)','qualifications','Homme'),('FBL45','2024-08-02','19:00:00','22:00:00','Football',7,'(1Cvs2D)','qualifications','Homme'),('FBL46','2024-08-02','21:00:00','00:00:00','Football',5,'(1Avs2B)','qualifications','Homme'),('FBL47','2024-08-03','15:00:00','18:00:00','Football',1,'(1Bvs2C)','qualifications','Femme'),('FBL48','2024-08-03','18:00:00','21:00:00','Football',6,'(1Cvs3A/B)','qualifications','Femme'),('FBL49','2024-08-03','19:00:00','22:00:00','Football',7,'(2Avs2B)','qualifications','Femme'),('FBL50','2024-08-03','21:00:00','00:00:00','Football',3,'(A1vs3B/C)','qualifications','Femme'),('FBL51','2024-08-05','18:00:00','21:00:00','Football',7,'(V26vsV28)','qualifications','Homme'),('FBL52','2024-08-05','21:00:00','00:00:00','Football',6,'(V25vsV27)','qualifications','Homme'),('FBL53','2024-08-06','18:00:00','21:00:00','Football',6,'(V19vsV21)','qualifications','Femme'),('FBL54','2024-08-06','21:00:00','00:00:00','Football',7,'(V19vsV21)','qualifications','Femme'),('FBL55','2024-08-08','17:00:00','20:00:00','Football',3,'(RU29vsRU30)','medailles','Homme'),('FBL56','2024-08-09','15:00:00','18:00:00','Football',6,'(RU23vsRU24)','medailles','Femme'),('FBL57','2024-08-09','18:00:00','21:30:00','Football',1,'(V29vsV30)','medailles','Homme'),('FBL58','2024-08-10','17:00:00','20:30:00','Football',1,'(V23vs24)','medailles','Femme'),('FBL99','2023-06-07','17:30:00','17:30:00','Football',6,' ','qualifications','homme'),('GOL01','2023-08-15','10:00:00','14:00:00','Golf',9,'golf premier tour h','qualifications','homme');
/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES (1,'Parc des Princes','Paris','stade'),(2,'Stade Geoffroy-Guichard','Saint-Étienne','stade'),(3,'Stade de la Beaujoire','Nantes','Stade'),(4,'Allianz Riviera','Nice','Stade'),(5,'Matmut Atlantique','Bordeaux','Stade'),(6,'Groupama Stadium','Lyon','Stade'),(7,'Orange Vélodrome','Marseille','stade'),(8,'Centre national de tir','Châteauroux','lieu_public'),(9,'Golf national','Saint-Quentin-en-Yvelines','golf'),(11,'Le Centre Aquatique','Saint-Denis','centre_aquatique');
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `hashedPassword` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (11,'Louei','$2a$10$BCbuoEzFWTXQ3KRwoKA1g.I5UDIKPj3lD4/icO3DLLW/8VDq8wAtC','$2a$10$BCbuoEzFWTXQ3KRwoKA1g.','admin'),(12,'Oussama','$2a$10$jaCrDK5.yUzxIvZUq04BNO/zjynRRrhTkZ/W0kHjIJJnkRJ3QB0kq','$2a$10$jaCrDK5.yUzxIvZUq04BNO','session_handler'),(13,'Gurwan','$2a$10$AnnzVH9N4nsmQcsINsDmqOrnvxoM6ur0RYRzozn63.jOEJ9D6d9Tu','$2a$10$AnnzVH9N4nsmQcsINsDmqO','better_handler');
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

-- Dump completed on 2023-05-25 20:58:45
