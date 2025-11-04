USE `bitcoin_verification`;

CREATE TABLE `t_address_btc`(
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `address` varchar(64) NOT NULL DEFAULT '' COMMENT '地址字符串',
  `balance` decimal(27, 9) NOT NULL DEFAULT '0' COMMENT '当前余额',
  `gmt_create` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `gmt_update` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY(`id`),
  UNIQUE KEY `uniq_address`(`address`)
) DEFAULT CHARSET=utf8mb4 COMMENT="BTC地址信息表";
