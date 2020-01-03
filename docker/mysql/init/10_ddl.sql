CREATE DATABASE IF NOT EXISTS greeting;

CREATE TABLE greetings
(
    `id`      int(11)      NOT NULL AUTO_INCREMENT,
    `message` varchar(100) NOT NULL DEFAULT 'Hello, World!',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

