/*
Navicat MySQL Data Transfer

Source Server         : mysql_server
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : server_monitor

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2013-07-31 16:03:32
*/

delimiter $$

CREATE DATABASE `server_monitor` /*!40100 DEFAULT CHARACTER SET utf8 */$$

USE server_monitor;$$

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `servers`
-- ----------------------------
DROP TABLE IF EXISTS `servers`;
CREATE TABLE `servers` (
  `server_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT 'localhost',
  `adress` varchar(128) NOT NULL DEFAULT '127.0.0.1',
  `port` int(11) unsigned NOT NULL DEFAULT '80',
  `url_path` varchar(128) NOT NULL DEFAULT 'http://localhost:80/',
  `state` enum('OK','WARN','FAIL') NOT NULL DEFAULT 'FAIL',
  `response` varchar(200) DEFAULT '""',
  `last_check` timestamp NULL DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`server_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1128 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of servers
-- ----------------------------
INSERT INTO `servers` VALUES ('2', 'yandex.ua', 'yandex.ua', '80', 'http://yandex.ua/yandsearch?text=http%3A%2F%2Ftest.ru%2F&lr=146&redircnt=1373541618.1&ncrnd=1126', 'FAIL', 'activate to check', '2013-07-28 23:47:36', '2013-07-05 21:47:43', '0');
INSERT INTO `servers` VALUES ('3', 'mail.ru', 'mail.ru', '80', 'http://mail.ru/', 'OK', 'HTTP/1.1 200 OK', '2013-07-28 23:47:41', '2013-07-05 22:56:54', '1');
INSERT INTO `servers` VALUES ('4', 'ya.ru', 'http://ya.ru/', '80', 'http://ya.ru/', 'OK', 'HTTP/1.1 200 Ok', '2013-07-28 23:47:41', '2013-07-06 01:16:46', '1');
INSERT INTO `servers` VALUES ('5', 'www.springsource.org', 'www.springsource.org', '80', 'http://www.springsource.org/', 'OK', 'HTTP/1.1 200 OK', '2013-07-28 23:47:41', '2013-07-15 09:47:35', '1');
INSERT INTO `servers` VALUES ('6', 'google.com.ua', 'google.com.ua', '80', 'http://www.google.com.ua/', 'OK', 'HTTP/1.1 200 OK', '2013-07-28 23:47:41', '2013-07-15 09:49:02', '1');
INSERT INTO `servers` VALUES ('7', 'vk.com', 'vk.com', '80', 'http://vk.com/warntestlink', 'WARN', 'HTTP/1.1 404 Not Found', '2013-07-28 23:47:41', '2013-07-28 23:23:29', '1');

-- ----------------------------
-- Table structure for `settings`
-- ----------------------------
DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings` (
  `server_check_interval` int(11) NOT NULL DEFAULT '5',
  `client_refresh_interval` int(11) NOT NULL DEFAULT '10',
  `server_timeout` int(11) NOT NULL DEFAULT '3',
  `smtp_server_adress` varchar(128) NOT NULL DEFAULT 'unknown',
  `smtp_port` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`server_check_interval`,`client_refresh_interval`,`server_timeout`,`smtp_server_adress`,`smtp_port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of settings
-- ----------------------------
INSERT INTO `settings` VALUES ('10', '20', '3', 'admin@mail.ru', '2525');

-- ----------------------------
-- Table structure for `user_roles`
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `USER_ROLE_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) unsigned NOT NULL,
  `AUTHORITY` varchar(45) NOT NULL,
  PRIMARY KEY (`USER_ROLE_ID`),
  KEY `FK_user_roles` (`USER_ID`),
  CONSTRAINT `FK_user_roles` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_roles
-- ----------------------------

-- ----------------------------
-- Table structure for `user_server`
-- ----------------------------
DROP TABLE IF EXISTS `user_server`;
CREATE TABLE `user_server` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pair` (`user_id`,`server_id`),
  KEY `fk_server` (`server_id`),
  KEY `fk_user_idx` (`user_id`),
  CONSTRAINT `fk_server` FOREIGN KEY (`server_id`) REFERENCES `servers` (`server_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_server
-- ----------------------------

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(128) NOT NULL,
  `name` varchar(128) NOT NULL,
  `password` varchar(64) DEFAULT NULL,
  `email` varchar(64) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` timestamp NULL DEFAULT NULL,
  `admin` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=1089 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of users
-- ----------------------------
