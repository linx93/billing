/*
 Navicat Premium Data Transfer

 Source Server         : 公司-mysql-3306
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : 192.168.1.100:3306
 Source Schema         : billing_management_system

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 28/07/2022 10:32:44
*/


create schema billing_management_system collate utf8mb4_general_ci

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_records
-- ----------------------------
DROP TABLE IF EXISTS `order_records`;
CREATE TABLE `order_records` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `platform_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台名称',
  `platform_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台代码',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  `amount` decimal(10,0) DEFAULT NULL COMMENT '金额',
  `desc_info` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述信息',
  `pay_time` bigint DEFAULT NULL COMMENT '支付时间',
  `consumer_name` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户名称',
  `consumer_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户code或者说是客户id，客户的唯一标识',
  `billing_status` int DEFAULT NULL COMMENT '开票状态[0：未开票、1：开票中、2：已开票]',
  `billing_url` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '票据地址',
  `notify_url` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '票据状态通知地址',
  `notify_status` int DEFAULT NULL COMMENT '票据状态通知状态[0:未通知 1通知成功 2:通知失败]',
  `consumer_company_code` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户企业社会统一信用代码，开普票的时候使用',
  `pay_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_records_id_uindex` (`id`),
  UNIQUE KEY `order_records_order_id_uindex` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单记录表';

-- ----------------------------
-- Records of order_records
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `nick_name` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称或者姓名',
  `account` varchar(300) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账户',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  `password` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `role_id` int DEFAULT NULL COMMENT '角色id，预留后续扩展使用',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `sys_user_info_account_uindex` (`account`),
  UNIQUE KEY `sys_user_info_user_id_uindex` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, '曲终人散', 'linx', 1655710415, 1655710415, '$2a$10$aIT4w6s9c6yJHflampo/n.vOKzwP/JTHJU0aDhZyH6XQQP3YWNaWW', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
