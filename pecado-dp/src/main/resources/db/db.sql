CREATE TABLE `ds` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '名称',
  `url` varchar(255) NOT NULL COMMENT 'url',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` int DEFAULT '0' COMMENT '是否可用',
  `createdTime` datetime NOT NULL COMMENT '创建时间',
  `updatedTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源';

INSERT INTO `ds` (`id`, `name`, `url`, `username`, `password`, `status`, `createdTime`, `updatedTime`)
VALUES
	(1,'system','jdbc:mysql://localhost:3306/pecado-system?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','root','9dcJ5GzdWew1PFp2LU/rVEp/eQZpLXhU',0,'2020-09-29 10:00:00','2020-09-29 11:00:00'),
	(2,'ims','jdbc:mysql://localhost:3306/pecado-ims?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','root','9dcJ5GzdWew1PFp2LU/rVEp/eQZpLXhU',0,'2020-09-29 10:00:00','2020-09-29 11:00:00');

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;