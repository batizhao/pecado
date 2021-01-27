DROP DATABASE `pecado-dp`;
DROP DATABASE `pecado-ims`;
DROP DATABASE `pecado-system`;
DROP DATABASE `pecado-test`;

CREATE DATABASE IF NOT EXISTS `pecado-dp`;
CREATE DATABASE IF NOT EXISTS `pecado-ims`;
CREATE DATABASE IF NOT EXISTS `pecado-system`;
CREATE DATABASE IF NOT EXISTS `pecado-test`;

USE `pecado-dp`;

CREATE TABLE `ds` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '名称',
  `url` varchar(255) NOT NULL COMMENT 'url',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` int DEFAULT '0' COMMENT '是否可用',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源';

INSERT INTO `ds` (`id`, `name`, `url`, `username`, `password`, `status`, `createTime`, `updateTime`)
VALUES
	(1,'system','jdbc:mysql://localhost:3306/pecado-system?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','root','iKNB/cBzVsexgv3M90wY5D+I/nkf91sYKEbFs8nnhMq//jhEXwwbpHZ31yh3P4L/',0,'2020-09-29 10:00:00','2020-09-29 11:00:00'),
	(2,'ims','jdbc:mysql://localhost:3306/pecado-ims?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','root','iKNB/cBzVsexgv3M90wY5D+I/nkf91sYKEbFs8nnhMq//jhEXwwbpHZ31yh3P4L/',0,'2020-09-29 10:00:00','2020-09-29 11:00:00');