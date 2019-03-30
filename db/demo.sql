/*
 Navicat Premium Data Transfer

 Source Server         : aws 数据库
 Source Server Type    : MySQL
 Source Server Version : 50640
 Source Host           : product.cywvswlgkka9.us-east-1.rds.amazonaws.com
 Source Database       : demo

 Target Server Type    : MySQL
 Target Server Version : 50640
 File Encoding         : utf-8

 Date: 03/30/2019 13:07:09 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) COLLATE utf8_bin NOT NULL,
  `content` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_product_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `product`
-- ----------------------------
BEGIN;
INSERT INTO `product` VALUES ('1', '鞋子', '鞋子'), ('2', '鞋子理论', null), ('3', '鞋子尺码', null), ('4', '鞋子价格', null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
