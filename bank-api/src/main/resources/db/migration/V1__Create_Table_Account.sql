CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `account_balance` double NOT NULL,
  PRIMARY KEY (`id`)
)