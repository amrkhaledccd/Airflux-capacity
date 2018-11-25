
DROP TABLE IF EXISTS `aircraft`;

CREATE TABLE `aircraft` (
  `serial` varchar(255) NOT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `registration` varchar(255) DEFAULT NULL,
  `city_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`serial`),
);


DROP TABLE IF EXISTS `airport`;

CREATE TABLE `airport` (
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `city_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`),
);

DROP TABLE IF EXISTS `city`;

CREATE TABLE `city` (
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
);

DROP TABLE IF EXISTS `schedule`;

CREATE TABLE `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `departure` datetime DEFAULT NULL,
  `flight_time` varchar(255) DEFAULT NULL,
  `destination_code` varchar(255) DEFAULT NULL,
  `origin_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
);