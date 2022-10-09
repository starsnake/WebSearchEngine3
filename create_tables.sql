SET character_set_client = utf8mb4;
CREATE TABLE `search_engine`.`field` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `selector` varchar(255) DEFAULT NULL,
  `weight` float NOT NULL,
  CONSTRAINT PK_FIELD PRIMARY KEY (`id`)
);

CREATE TABLE `search_engine`.`site`(
`id` INT NOT NULL AUTO_INCREMENT,
`status` ENUM('INDEXING', 'INDEXED', 'FAILED') NOT NULL,
`status_time` DATETIME NOT NULL,
`last_error` TEXT,
`url` VARCHAR(255) NOT NULL,
`name` VARCHAR(255) NOT NULL, 
CONSTRAINT PK_SITE PRIMARY KEY (`id`)
 );

CREATE TABLE `search_engine`.`page`(
`id` INT NOT NULL AUTO_INCREMENT,
`site_id` INT NOT NULL,
`path` TEXT NOT NULL,
`code` INT NOT NULL,
`content` MEDIUMTEXT NOT NULL,
CONSTRAINT PK_PAGE PRIMARY KEY (`id`),
KEY `FK_PAGE_site_id_idx` (`site_id`),
KEY `page_path_idx` (`path`(100)),
CONSTRAINT `FK_PAGE_site_id` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
 ) DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `search_engine`.`lemma`(
`id` INT NOT NULL AUTO_INCREMENT,
`site_id` INT NOT NULL,
`lemma` VARCHAR(255) NOT NULL,
`frequency` INT NOT NULL, 
CONSTRAINT PK_LEMMA PRIMARY KEY (`id`),
KEY `FK_LEMMA_site_id_idx` (`site_id`),
CONSTRAINT `FK_LEMMA_site_id` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
 );

CREATE TABLE `search_engine`.`index`(
`id` INT NOT NULL AUTO_INCREMENT,
`page_id` INT NOT NULL,
`lemma_id` INT NOT NULL,
`rank` FLOAT NOT NULL,
CONSTRAINT PK_INDEX PRIMARY KEY (`id`),
KEY `FK_INDEX_page_id_idx` (`page_id`),
CONSTRAINT `FK_INDEX_page_id` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`),
KEY `FK_INDEX_lemma_id_idx` (`lemma_id`),
CONSTRAINT `FK_INDEX_lemma_id` FOREIGN KEY (`lemma_id`) REFERENCES `lemma` (`id`)
);

insert into `search_engine`.`field` ( `name`, `selector`, `weight`) value('title', 'title', 1);
insert into `search_engine`.`field` ( `name`, `selector`, `weight`) value('body', 'body', 0.8);

