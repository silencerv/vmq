CREATE DATABASE IF NOT EXISTS mq;
CREATE TABLE mq.message_producer (
 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
 `message_id` varchar(255) NOT NULL COMMENT '消息id',
 `subject` varchar(255) NOT NULL COMMENT '消息主题',
 `content` TEXT NOT NULL COMMENT '消息属性',
 `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
 `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_message_id` (`message_id`)
)ENGINE=InnoDB

CREATE TABLE mq.message (
 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
 `message_id` varchar(255) NOT NULL COMMENT '消息id',
 `subject` varchar(255) NOT NULL COMMENT '消息主题',
 `content` TEXT NOT NULL COMMENT '消息属性',
 `try_count` INT NOT NULL DEFAULT 0 COMMENT '重试次数',
 `next_retry` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()  COMMENT '下次重试时间',
 `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
 `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_message_id` (`message_id`),
  KEY `idx_next_retry` (`next_retry`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8