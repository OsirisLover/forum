/*
 Navicat Premium Data Transfer

 Source Server         : hjt1
 Source Server Type    : MySQL
 Source Server Version : 80039
 Source Host           : localhost:3306
 Source Schema         : forum_db

 Target Server Type    : MySQL
 Target Server Version : 80039
 File Encoding         : 65001

 Date: 19/06/2025 21:46:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '帖子编号，主键，自增',
  `boardId` bigint(0) NOT NULL COMMENT '关联板块编号，非空',
  `userId` bigint(0) NOT NULL COMMENT '发帖人，非空，关联用户编号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题，非空，最大长度100个字符',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子正文，非空',
  `visitCount` int(0) NOT NULL DEFAULT 0 COMMENT '访问量，默认0',
  `replyCount` int(0) NOT NULL DEFAULT 0 COMMENT '回复数据，默认0',
  `likeCount` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数，默认0',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1 禁用，默认0',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 1 是，默认0',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间，精确到秒，非空',
  `updateTime` datetime(0) NOT NULL COMMENT '修改时间，精确到秒，非空',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article
-- ----------------------------
INSERT INTO `t_article` VALUES (5, 1, 5, '然后呢', '**hgknjhgf**', 1, 0, 0, 0, 0, '2025-06-11 09:20:11', '2025-06-11 09:20:11');
INSERT INTO `t_article` VALUES (6, 5, 5, '骂面试官', ':fa-frown-o: :fa-frown-o:', 0, 0, 0, 0, 0, '2025-06-11 09:24:29', '2025-06-11 09:24:29');
INSERT INTO `t_article` VALUES (7, 9, 5, '今天怎么下了一天的大雨', '今天这雨下的没法出门了', 1, 0, 1, 0, 0, '2025-06-19 20:51:33', '2025-06-19 20:51:33');

-- ----------------------------
-- Table structure for t_article_reply
-- ----------------------------
DROP TABLE IF EXISTS `t_article_reply`;
CREATE TABLE `t_article_reply`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自增',
  `articleId` bigint(0) NOT NULL COMMENT '关联帖子编号，非空',
  `postUserId` bigint(0) NOT NULL COMMENT '楼主用户，关联用户编号，非空',
  `replyId` bigint(0) NULL DEFAULT NULL COMMENT '关联回复编号，支持楼中楼',
  `replyUserId` bigint(0) NULL DEFAULT NULL COMMENT '楼主下的回复用户编号，支持楼中楼',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回贴内容，长度500个字符，非空',
  `likeCount` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数，默认0',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态 0 正常，1禁用，默认0',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除 0否 1是，默认0',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间，精确到秒，非空',
  `updateTime` datetime(0) NOT NULL COMMENT '更新时间，精确到秒，非空',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article_reply
-- ----------------------------
INSERT INTO `t_article_reply` VALUES (1, 1, 2, NULL, NULL, '```java\npublic class Hi{\n        public static void main(String[] args){\n            System.out.println(\"Hello\");\n        }\n}\n```', 0, 0, 0, '2024-03-04 14:31:45', '2024-03-04 14:31:45');
INSERT INTO `t_article_reply` VALUES (2, 1, 3, NULL, NULL, '`public class Hi{\n        public static void main(String[] args){\n            System.out.println(\"Hello\");\n        }\n}`', 0, 0, 0, '2024-03-04 15:22:37', '2024-03-04 15:22:37');
INSERT INTO `t_article_reply` VALUES (3, 1, 2, NULL, NULL, '测试帖子回复接口3', 0, 0, 0, '2025-06-11 09:10:36', '2025-06-11 09:10:36');

-- ----------------------------
-- Table structure for t_board
-- ----------------------------
DROP TABLE IF EXISTS `t_board`;
CREATE TABLE `t_board`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '版块编号，主键，自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版块名，非空',
  `articleCount` int(0) NOT NULL DEFAULT 0 COMMENT '帖子数量，默认0',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序优先级，升序，默认0，',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态，0 正常，1禁用，默认0',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除 0否，1是，默认0',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间，精确到秒，非空',
  `updateTime` datetime(0) NOT NULL COMMENT '更新时间，精确到秒，非空',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '版块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_board
-- ----------------------------
INSERT INTO `t_board` VALUES (1, 'Java', 3, 1, 0, 0, '2023-01-14 19:02:18', '2023-01-14 19:02:18');
INSERT INTO `t_board` VALUES (2, 'C++', 0, 2, 0, 0, '2023-01-14 19:02:41', '2023-01-14 19:02:41');
INSERT INTO `t_board` VALUES (3, '前端技术', 0, 3, 0, 0, '2023-01-14 19:02:52', '2023-01-14 19:02:52');
INSERT INTO `t_board` VALUES (4, 'MySQL', 0, 4, 0, 0, '2023-01-14 19:03:02', '2023-01-14 19:03:02');
INSERT INTO `t_board` VALUES (5, '面试宝典', 1, 5, 0, 0, '2023-01-14 19:03:24', '2023-01-14 19:03:24');
INSERT INTO `t_board` VALUES (6, '经验分享', 0, 6, 0, 0, '2023-01-14 19:03:48', '2023-01-14 19:03:48');
INSERT INTO `t_board` VALUES (7, '招聘信息', 0, 7, 0, 0, '2023-01-25 21:25:33', '2023-01-25 21:25:33');
INSERT INTO `t_board` VALUES (8, '福利待遇', 0, 8, 0, 0, '2023-01-25 21:25:58', '2023-01-25 21:25:58');
INSERT INTO `t_board` VALUES (9, '灌水区', 2, 9, 0, 0, '2023-01-25 21:26:12', '2023-01-25 21:26:12');

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '站内信编号，主键，自增',
  `postUserId` bigint(0) NOT NULL COMMENT '发送者，并联用户编号',
  `receiveUserId` bigint(0) NOT NULL COMMENT '接收者，并联用户编号',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容，非空，长度255个字符',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态 0未读 1已读，默认0',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除 0否，1是，默认0',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间，精确到秒，非空',
  `updateTime` datetime(0) NOT NULL COMMENT '更新时间，精确到秒，非空',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '站内信表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户编号，主键，自增',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，非空，唯一',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密后的密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称，非空',
  `phoneNum` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `gender` tinyint(0) NOT NULL DEFAULT 2 COMMENT '0女 1男 2保密，非空，默认2',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '为密码加盐，非空',
  `avatarUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像URL，默认系统图片',
  `articleCount` int(0) NOT NULL DEFAULT 0 COMMENT '发帖数量，非空，默认0',
  `isAdmin` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否管理员，0否 1是，默认0',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注，自我介绍',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态 0 正常，1 禁言，默认0',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除 0否 1是，默认0',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间，精确到秒',
  `updateTime` datetime(0) NOT NULL COMMENT '更新时间，精确到秒',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_username_uindex`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (2, 'zs', '2301febedb2dbd7f5f440f92cc452815', '张三', '', NULL, 2, 'c4d891c360c34b7184c3f424833bed3b', NULL, 2, 0, NULL, 0, 0, '2024-03-04 14:23:38', '2024-03-04 14:23:38');
INSERT INTO `t_user` VALUES (3, 'ls', 'dd0c1c91b071ceaf312744b1f0be3675', '李四', '', NULL, 2, 'f8f39d4400314f5b9318b3902e08b7fc', NULL, 1, 0, NULL, 0, 0, '2024-03-04 15:21:31', '2024-03-04 15:21:31');
INSERT INTO `t_user` VALUES (4, 'admin', '43f6be9c4dedcb665fd1878c9262df52', '程序员', '', NULL, 2, '10fff185e56748d2a203b2d36f60b1b1', NULL, 0, 0, NULL, 0, 0, '2025-06-11 09:10:37', '2025-06-11 09:10:37');
INSERT INTO `t_user` VALUES (5, 'hjt', '37d6a47b0e8fbddb382335ef5c452d94', 'HJT', '', NULL, 2, '6549e98fa41b465884cc92e08c503012', NULL, 3, 1, NULL, 0, 0, '2025-06-11 09:18:40', '2025-06-11 09:18:40');

SET FOREIGN_KEY_CHECKS = 1;
