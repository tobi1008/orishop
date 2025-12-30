-- MySQL dump 10.13  Distrib 8.0.41, for macos15 (arm64)
--
-- Host: localhost    Database: orishop
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `phone_number` varchar(20) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `district` varchar(100) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `ward` varchar(100) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `detail_address` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_address_user` (`user_id`),
  CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,2,'0337349540','HCM','Thủ Đức','Thủ Đức','113 ',1),(2,1,'123123123123','HCM','HCM','HCM','113',1);
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `slug` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Các loại son môi cao cấp','https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=300&q=80','Son môi','son-moi'),(2,'Sản phẩm skincare chính hãng','https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=300&q=80','Chăm sóc da','cham-soc-da'),(3,'Phấn phủ, kem nền, má hồng','https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=300&q=80','Trang điểm mặt','trang-diem-mat'),(4,'Nước hoa nam nữ chính hãng','https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=300&q=80','Nước hoa','nuoc-hoa');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_vietnamese_ci,
  `created_at` datetime(6) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6uv0qku8gsu6x1r2jkrtqwjtn` (`product_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  CONSTRAINT `FK6uv0qku8gsu6x1r2jkrtqwjtn` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `message` text COLLATE utf8mb4_vietnamese_ci,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `subject` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons`
--

DROP TABLE IF EXISTS `coupons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `discount_type` varchar(20) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `discount_value` decimal(38,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `usage_limit` int DEFAULT NULL,
  `used_count` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons`
--

LOCK TABLES `coupons` WRITE;
/*!40000 ALTER TABLE `coupons` DISABLE KEYS */;
INSERT INTO `coupons` VALUES (1,'Tobi50%','PERCENT',50.00,'2025-12-30','2026-02-27',100,0),(2,'Tobi','AMOUNT',30000.00,'2025-12-30','2026-03-05',10,0);
/*!40000 ALTER TABLE `coupons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flash_sale_products`
--

DROP TABLE IF EXISTS `flash_sale_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flash_sale_products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sale_price` decimal(38,2) NOT NULL,
  `flash_sale_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi9j0ic0rvcs995lpn2pitc8sy` (`flash_sale_id`),
  KEY `FK841wqtv97s8rmhwasq40luav9` (`product_id`),
  CONSTRAINT `FK841wqtv97s8rmhwasq40luav9` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKi9j0ic0rvcs995lpn2pitc8sy` FOREIGN KEY (`flash_sale_id`) REFERENCES `flash_sales` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flash_sale_products`
--

LOCK TABLES `flash_sale_products` WRITE;
/*!40000 ALTER TABLE `flash_sale_products` DISABLE KEYS */;
INSERT INTO `flash_sale_products` VALUES (1,325000.00,1,1),(2,90000.00,1,2),(3,160000.00,1,3),(4,712000.00,1,4),(5,475000.00,1,5),(6,520000.00,2,1);
/*!40000 ALTER TABLE `flash_sale_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flash_sales`
--

DROP TABLE IF EXISTS `flash_sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flash_sales` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text COLLATE utf8mb4_vietnamese_ci,
  `end_time` datetime(6) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flash_sales`
--

LOCK TABLES `flash_sales` WRITE;
/*!40000 ALTER TABLE `flash_sales` DISABLE KEYS */;
INSERT INTO `flash_sales` VALUES (1,'Siêu Sales 22/12 tất cả sản phẩm','2026-02-19 12:00:00.000000','Sales 22/12','2025-12-23 12:00:00.000000',_binary ''),(2,'Siêu Sales','2026-01-30 00:00:00.000000','Black Friday','2025-12-30 00:00:00.000000',_binary '\0');
/*!40000 ALTER TABLE `flash_sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `script` varchar(1000) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','init','SQL','V1__init.sql',-53941687,'root','2025-12-06 05:18:22',89,1),(2,'2','remove default admin','SQL','V2__remove_default_admin.sql',1832902646,'root','2025-12-06 05:18:22',4,1),(3,'3','add payment status','SQL','V3__add_payment_status.sql',698003702,'root','2025-12-18 07:48:01',23,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_coupons`
--

DROP TABLE IF EXISTS `order_coupons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL,
  `coupon_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_oc_order` (`order_id`),
  KEY `fk_oc_coupon` (`coupon_id`),
  CONSTRAINT `fk_oc_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_oc_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_coupons`
--

LOCK TABLES `order_coupons` WRITE;
/*!40000 ALTER TABLE `order_coupons` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_coupons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) DEFAULT NULL,
  `quantity` int NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (7,325000.00,11,24,1),(8,325000.00,4,25,1),(9,475000.00,2,26,5),(10,2500000.00,1,27,8),(11,160000.00,3,28,3),(12,160000.00,2,29,3),(13,90000.00,1,30,2);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `shipping_address` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `shipping_phone` varchar(20) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `shipping_name` varchar(100) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `payment_method` varchar(50) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `payment_status` tinyint(1) DEFAULT '0',
  `coupon_code` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `discount_amount` decimal(38,2) DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `return_reason` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `order_code` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dhk2umg8ijjkg4njg6891trit` (`order_code`),
  KEY `fk_order_user` (`user_id`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (24,1,1787500.00,'CANCELLED','113, HCM, HCM, HCM','12342412414','AZ','VNPAY','2025-12-30 20:02:47',0,'Tobi50%',1787500.00,'','Quá ok',NULL),(25,1,1270000.00,'CANCELLED','113, HCM, HCM, HCM','01231231231','Le Van B','VNPAY','2025-12-30 20:06:42',1,'Tobi',30000.00,'aaaa','Tao quá đẹp trai',NULL),(26,1,950000.00,'RETURNED','113, HCM, HCM, HCM','123123123','A','COD','2025-12-30 20:21:00',1,NULL,0.00,'','Quá ngu',NULL),(27,1,2500000.00,'RETURNED','113, HCM, HCM, HCM','1231231312','ABV','COD','2025-12-30 21:06:44',1,NULL,0.00,'','Quân đẳng cấp quá | STK: MB 0337349540 Tobi',NULL),(28,1,480000.00,'CANCELLED','113, HCM, HCM, HCM','1231231231','TObi','COD','2025-12-30 21:17:26',0,NULL,0.00,'',NULL,NULL),(29,1,320000.00,'RETURNED','113, HCM, HCM, HCM','12312','A','COD','2025-12-30 21:19:11',1,NULL,0.00,'','a | STK: a',NULL),(30,1,90000.00,'PENDING','113, HCM, HCM, HCM','032312312','A','COD','2025-12-30 21:23:57',0,NULL,0.00,'',NULL,'ORD6374177F24');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` text COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `is_primary` bit(1) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqnq71xsohugpqwf3c9gxmsuy` (`product_id`),
  CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (1,'https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=600&q=80',_binary '',1),(2,'https://images.unsplash.com/photo-1627384113743-6bd5a479fffd?auto=format&fit=crop&w=600&q=80',_binary '\0',1),(4,'https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=600&q=80',_binary '\0',2),(5,'https://images.unsplash.com/photo-1625093742435-6fa192b6fb10?auto=format&fit=crop&w=600&q=80',_binary '',3),(6,'https://images.unsplash.com/photo-1617391986616-a36c92d54406?auto=format&fit=crop&w=600&q=80',_binary '\0',3),(8,'https://images.unsplash.com/photo-1625093742435-6fa192b6fb10?auto=format&fit=crop&w=600&q=80',_binary '\0',4),(9,'https://images.unsplash.com/photo-1627384113743-6bd5a479fffd?auto=format&fit=crop&w=600&q=80',_binary '',5),(12,'https://images.unsplash.com/photo-1616683693504-3ea7e9ad6fec?auto=format&fit=crop&w=600&q=80',_binary '\0',6),(13,'https://images.unsplash.com/photo-1616683693504-3ea7e9ad6fec?auto=format&fit=crop&w=600&q=80',_binary '',7),(14,'https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?auto=format&fit=crop&w=600&q=80',_binary '\0',7),(15,'https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80',_binary '',8),(16,'https://images.unsplash.com/photo-1556228720-191739c23b2d?auto=format&fit=crop&w=600&q=80',_binary '\0',8),(18,'https://images.unsplash.com/photo-1616683693504-3ea7e9ad6fec?auto=format&fit=crop&w=600&q=80',_binary '\0',9),(20,'https://images.unsplash.com/photo-1616683693504-3ea7e9ad6fec?auto=format&fit=crop&w=600&q=80',_binary '\0',10),(21,'https://images.unsplash.com/photo-1516975080664-ed2fc6a32937?auto=format&fit=crop&w=600&q=80',_binary '',11),(22,'https://images.unsplash.com/photo-1503236823255-943cb751f9c4?auto=format&fit=crop&w=600&q=80',_binary '\0',11),(24,'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=600&q=80',_binary '\0',12),(25,'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=600&q=80',_binary '',13),(26,'https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=600&q=80',_binary '\0',13),(28,'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=600&q=80',_binary '\0',14),(32,'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?auto=format&fit=crop&w=600&q=80',_binary '\0',16),(33,'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?auto=format&fit=crop&w=600&q=80',_binary '',17),(34,'https://images.unsplash.com/photo-1585386959984-a4155224a1ad?auto=format&fit=crop&w=600&q=80',_binary '\0',17),(35,'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?auto=format&fit=crop&w=600&q=80',_binary '',18),(36,'https://images.unsplash.com/photo-1585386959984-a4155224a1ad?auto=format&fit=crop&w=600&q=80',_binary '\0',18),(37,'https://images.unsplash.com/photo-1541643600914-78b084683601?auto=format&fit=crop&w=600&q=80',_binary '',19),(38,'https://images.unsplash.com/photo-1594035910387-fea4779426e9?auto=format&fit=crop&w=600&q=80',_binary '\0',19),(39,'https://images.unsplash.com/photo-1585386959984-a4155224a1ad?auto=format&fit=crop&w=600&q=80',_binary '',20),(40,'https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=600&q=80',_binary '\0',20),(41,'http://res.cloudinary.com/dzp1uddef/image/upload/v1767034992/ukozekme2ebisb2d4854.jpg',_binary '\0',15);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` text COLLATE utf8mb4_vietnamese_ci,
  `discount_price` decimal(38,2) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `slug` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `stock_quantity` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'2025-12-30 01:59:30.668000','Son MAC màu đỏ gạch cực hot.',NULL,'Son MAC Chili',650000.00,'son-mac-chili',100,1),(2,'2025-12-30 01:59:30.680000','Son kem lì Black Rouge màu đỏ đất.',NULL,'Son Black Rouge A12',180000.00,'son-black-rouge-a12',99,1),(3,'2025-12-30 01:59:30.684000','Màu đỏ nâu cực sang chảnh.',NULL,'Son 3CE Taupe',320000.00,'son-3ce-taupe',100,1),(4,'2025-12-30 01:59:30.686000','Dòng son cao cấp YSL màu cam cháy.',NULL,'Son YSL 212',890000.00,'son-ysl-212',100,1),(5,'2025-12-30 01:59:30.689000','Huyền thoại đỏ tươi của Dior.',NULL,'Son Dior 999',950000.00,'son-dior-999',100,1),(6,'2025-12-30 01:59:30.692000','Dịu nhẹ cho mọi loại da.',NULL,'Sữa rửa mặt Cetaphil',250000.00,'srm-cetaphil',100,2),(7,'2025-12-30 01:59:30.702000','Nước hoa hồng không mùi dưỡng ẩm sâu.',NULL,'Toner Klairs',310000.00,'toner-klairs',100,2),(8,'2025-12-30 01:59:30.710000','Serum phục hồi ban đêm thần thánh.',NULL,'Serum Estee Lauder ANR',2500000.00,'serum-estee-lauder',100,2),(9,'2025-12-30 01:59:30.714000','Phục hồi da, giảm kích ứng.',NULL,'Kem dưỡng La Roche-Posay B5',350000.00,'kem-duong-b5',100,2),(10,'2025-12-30 01:59:30.721000','Tẩy trang dịu nhẹ cho da nhạy cảm.',NULL,'Tẩy trang Bioderma hồng',450000.00,'tay-trang-bioderma',100,2),(11,'2025-12-30 01:59:30.727000','Kiềm dầu cực tốt.',NULL,'Phấn phủ Innisfree',150000.00,'phan-phu-innisfree',100,3),(12,'2025-12-30 01:59:30.734000','Che phủ tốt, tiệp màu da.',NULL,'Kem nền Maybelline Fit Me',220000.00,'kem-nen-fitme',100,3),(13,'2025-12-30 01:59:30.742000','Màu hồng cam nhũ vàng iconic.',NULL,'Má hồng Nars Orgasm',780000.00,'ma-hong-nars',100,3),(14,'2025-12-30 01:59:30.752000','Che phủ hoàn hảo vết thâm mụn.',NULL,'Che khuyết điểm The Saem',90000.00,'ckd-the-saem',100,3),(15,'2025-12-30 01:59:30.763000','Lâu trôi, không lem.',NULL,'Kẻ mắt Kiss Me',280000.00,'ke-mat-kissme',100,3),(16,'2025-12-30 01:59:30.766000','Hương thơm cổ điển, sang trọng.',NULL,'Nước hoa Chanel No.5',3500000.00,'chanel-no5',100,4),(17,'2025-12-30 01:59:30.770000','Mùi hương nam tính, mạnh mẽ.',NULL,'Nước hoa Dior Sauvage',2800000.00,'dior-sauvage',100,4),(18,'2025-12-30 01:59:30.775000','Tự do, quyến rũ cho phái nữ.',NULL,'Nước hoa YSL Libre',3200000.00,'ysl-libre',100,4),(19,'2025-12-30 01:59:30.778000','Hương gỗ đàn hương độc đáo.',NULL,'Nước hoa Le Labo 33',5500000.00,'le-labo-33',100,4),(20,'2025-12-30 01:59:30.780000','Xạ hương quyến rũ.',NULL,'Nước hoa Narciso Rodriguez For Her',2600000.00,'narciso-hong',100,4);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recently_viewed`
--

DROP TABLE IF EXISTS `recently_viewed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recently_viewed` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `viewed_at` datetime(6) NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9rd0vyea43aug5ggyu3i2r68q` (`product_id`),
  KEY `FK6e3sbjf7qnbbmaff019w7p8b1` (`user_id`),
  CONSTRAINT `FK6e3sbjf7qnbbmaff019w7p8b1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9rd0vyea43aug5ggyu3i2r68q` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recently_viewed`
--

LOCK TABLES `recently_viewed` WRITE;
/*!40000 ALTER TABLE `recently_viewed` DISABLE KEYS */;
INSERT INTO `recently_viewed` VALUES (1,'2025-12-30 20:31:45.520000',1,1),(2,'2025-12-30 03:19:45.763000',20,2),(3,'2025-12-30 20:29:48.976000',5,1),(4,'2025-12-30 20:31:49.592000',9,1),(5,'2025-12-30 20:33:41.717000',6,1);
/*!40000 ALTER TABLE `recently_viewed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` text COLLATE utf8mb4_vietnamese_ci,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'Tobi bro','2025-12-30 02:20:07.201000',5,1,1),(2,'Hơi buồn admin Quân Trần','2025-12-30 20:29:06.051000',1,5,1);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `setting_key` varchar(255) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `value` text COLLATE utf8mb4_vietnamese_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_swd05dvj4ukvw5q135bpbbfae` (`setting_key`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,'seo_desc','OriShop cung cấp các sản phẩm mỹ phẩm chính hãng, chất lượng cao với giá tốt nhất.'),(2,'contact_phone','0123456789'),(3,'currency_symbol','₫'),(4,'footer_desc','OriShop – Thiên đường mua sắm mỹ phẩm chính hãng, web bán mỹ phẩm số 1 Việt Nam.\r\nOriShop cung cấp đa dạng các sản phẩm mỹ phẩm chính hãng từ những thương hiệu hàng đầu trong và ngoài nước. Cam kết 100% hàng thật, giá cạnh tranh, cập nhật xu hướng làm đẹp mới nhất, giao hàng nhanh chóng trên toàn quốc và dịch vụ chăm sóc khách hàng tận tâm. OriShop – nơi tôn vinh vẻ đẹp tự nhiên của bạn mỗi ngày.'),(5,'social_youtube','https://youtube.com'),(6,'page_return_policy','<h1>Chính sách đổi trả</h1><p>Nội dung đang cập nhật...</p>'),(7,'primary_color','#ec407a'),(8,'seo_title','OriShop - Mỹ phẩm chính hãng'),(9,'contact_email','support@orishop.com'),(10,'site_name','OriShop'),(11,'site_logo','http://res.cloudinary.com/dzp1uddef/image/upload/v1767027558/zepkwfj3tbkltmxig7oe.jpg'),(12,'social_tiktok','https://tiktok.com'),(13,'social_instagram','https://instagram.com'),(14,'social_facebook','https://facebook.com'),(15,'page_privacy_policy','<h1>Chính sách bảo mật</h1><p>Nội dung đang cập nhật...</p>'),(16,'map_embed','https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3918.471676880053!2d106.77259277570497!3d10.851724657805167!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3175276e7ea103df%3A0xb6cf10bb7d719327!2zSFVURUNIIEtoHU9uZyBOZ2jhu4cgQ2Fv!5e0!3m2!1svi!2s!4v1710000000000!5m2!1svi!2s'),(17,'contact_address','123 Đường ABC, Quận XYZ, TP.HCM'),(18,'page_terms_of_use','<h1>Điều khoản sử dụng</h1><p>Nội dung đang cập nhật...</p>'),(19,'seo_keywords','my pham, son moi, duong da, orishop'),(20,'_csrf','zvI0KTCneXwG5J2bzoVWrcaYAAROOACPPbLO-LJlx3H6dakEr8VVGAHFSkUrgq76rKhinfSsLWYsXmSiBdH6zoBW_xTKEJhm'),(21,'contact_zalo','https://chat.zalo.me/'),(22,'contact_telegram','https://web.telegram.org/');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `address` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@orishop.com','$2a$10$7Rbsw52KyjiV4SCrOVwEzeeYikSE66dNsfKmtfD3LpN1sRTeMbeVS','Admin','ROLE_ADMIN','0337349540',NULL,1,NULL),(2,'tobi@quyenlt.com','$2a$10$H3GHABKlff8q1dndamd.7OTE22FAUsERWn4a8GNL0AFj4r.xIyUQS','Tobi','ROLE_USER','0337349540',NULL,1,NULL),(3,'tobi@tobi.com','$2a$10$ukpc8wfeW53pb1p7iVYpjuDQ9lajZwdyuCpu7PeOR3sEtZGlsrqUC','Tobi','ROLE_USER','012312412',NULL,1,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlists`
--

DROP TABLE IF EXISTS `wishlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlists` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl7ao98u2bm8nijc1rv4jobcrx` (`product_id`),
  KEY `FK330pyw2el06fn5g28ypyljt16` (`user_id`),
  CONSTRAINT `FK330pyw2el06fn5g28ypyljt16` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKl7ao98u2bm8nijc1rv4jobcrx` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlists`
--

LOCK TABLES `wishlists` WRITE;
/*!40000 ALTER TABLE `wishlists` DISABLE KEYS */;
/*!40000 ALTER TABLE `wishlists` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-30 21:26:57
