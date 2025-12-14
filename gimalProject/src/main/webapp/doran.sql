--
-- Table structure for table `dream_post`
--

DROP TABLE IF EXISTS `dream_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dream_post` (
  `dream_id` bigint NOT NULL AUTO_INCREMENT,
  `writer_id` bigint NOT NULL,
  `writer_type` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `category_code` varchar(30) NOT NULL,
  `condition_code` varchar(20) NOT NULL,
  `price` int NOT NULL DEFAULT '0',
  `dong` varchar(50) NOT NULL,
  `thumbnail_url` text,
  `status` varchar(20) NOT NULL DEFAULT 'OPEN',
  `view_count` int NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dream_id`),
  KEY `idx_dream_status_dong` (`status`,`dong`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_resource`
--

DROP TABLE IF EXISTS `file_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_url` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `file_type` varchar(30) DEFAULT NULL,
  `size` bigint DEFAULT NULL,
  `used_type` varchar(30) NOT NULL,
  `used_id` bigint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
