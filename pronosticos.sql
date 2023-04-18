-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: pronosticos
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `pronostico`
--

DROP TABLE IF EXISTS `pronostico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pronostico` (
  `id` int NOT NULL AUTO_INCREMENT,
  `participante` varchar(45) NOT NULL,
  `equipo1` varchar(45) NOT NULL,
  `gana1` char(1) DEFAULT NULL,
  `empate` char(1) DEFAULT NULL,
  `gana2` char(1) DEFAULT NULL,
  `equipo2` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pronostico`
--

LOCK TABLES `pronostico` WRITE;
/*!40000 ALTER TABLE `pronostico` DISABLE KEYS */;
INSERT INTO `pronostico` VALUES (1,'Mariana','Argentina','X','','','Arabia Saudita'),(2,'Mariana','Polonia','','X','','Mexico'),(3,'Mariana','Argentina','X','','','Mexico'),(4,'Mariana','Arabia Saudita','','','X','Polonia'),(5,'Mariana','Argentina','X','','','Polonia'),(6,'Mariana','Arabia Saudita','','','X','Mexico'),(7,'Mariana','Argentina','X','','','Australia'),(8,'Mariana','Argentina','X','','','Paises Bajos'),(9,'Mariana','Argentina','X','','','Croacia'),(10,'Mariana','Argentina','X','','','Francia'),(11,'Pedro','Argentina','X','','','Arabia Saudita'),(12,'Pedro','Polonia','','','X','Mexico'),(13,'Pedro','Argentina','X','','','Mexico'),(14,'Pedro','Arabia Saudita','','X','','Polonia'),(15,'Pedro','Argentina','X','','','Polonia'),(16,'Pedro','Arabia Saudita','','X','','Mexico'),(17,'Pedro','Argentina','X','','','Australia'),(18,'Pedro','Argentina','','X','','Paises Bajos'),(19,'Pedro','Argentina','X','','','Croacia'),(20,'Pedro','Argentina','X','','','Francia'),(21,'Facundo','Argentina','','','X','Arabia Saudita'),(22,'Facundo','Polonia','','','X','Mexico'),(23,'Facundo','Argentina','X','','','Mexico'),(24,'Facundo','Arabia Saudita','','','X','Polonia'),(25,'Facundo','Argentina','X','','','Polonia'),(26,'Facundo','Arabia Saudita','','','X','Mexico'),(27,'Facundo','Argentina','X','','','Australia'),(28,'Facundo','Argentina','','','X','Paises Bajos'),(29,'Facundo','Argentina','X','','','Croacia'),(30,'Facundo','Argentina','','X','','Francia');
/*!40000 ALTER TABLE `pronostico` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-18 16:30:13
