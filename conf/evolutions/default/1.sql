# User schema

# --- !Ups

CREATE TABLE `user` (
   `id`             VARCHAR(64)     NOT NULL,
   `name`           VARCHAR(45)     NOT NULL,
   `password`       VARCHAR(64)     NOT NULL,
   `create_date`    DATETIME        NOT NULL,
   `update_date`    DATETIME        NOT NULL,
   `version`        INT(11)         NOT NULL,
   `deleted`        BOOL            NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_follow` (
    `id`            VARCHAR(64)     NOT NULL,
    `from_user_id`  VARCHAR(64)     NOT NULL,
    `to_user_id`    VARCHAR(64)     NOT NULL,
    `create_date`   DATETIME        NOT NULL,
    `update_date`   DATETIME        NOT NULL,
    `version`       INT(11)         NOT NULL,
    `deleted`       BOOL            NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_from_user_id` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_to_user_id` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_status` (
  `id`              VARCHAR(64)     NOT NULL,
  `from_user_id`    VARCHAR(64)     NOT NULL,
  `status_type`     INT(11)         NOT NULL,
  `status`          VARCHAR(255)    NOT NULL,
  `create_date`     DATETIME        NOT NULL,
  `update_date`     DATETIME        NOT NULL,
  `version`         INT(11)         NOT NULL,
  `deleted`         BOOL            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_status1` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_status_to_user` (
  `id`              VARCHAR(64)     NOT NULL,
  `user_status_id`  VARCHAR(64)     NOT NULL,
  `to_user_id`      VARCHAR(64)     NOT NULL,
  `create_date`     DATETIME        NOT NULL,
  `update_date`     DATETIME        NOT NULL,
  `version`         INT(11)         NOT NULL,
  `deleted`         BOOL            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_status_to_user1` FOREIGN KEY (`user_status_id`) REFERENCES `user_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_status_to_user2` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs
DROP TABLE IF EXISTS `user_status_to_user`;

DROP TABLE IF EXISTS `user_status`;

DROP TABLE IF EXISTS `user_follow`;

DROP TABLE IF EXISTS `user`;
