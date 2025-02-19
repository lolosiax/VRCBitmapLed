/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:37:50
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user" (
  "id" varchar(40) NOT NULL,
  "user_name" varchar(50) NOT NULL,
  "password" varchar(255) DEFAULT NULL,
  "real_name" varchar(50) DEFAULT NULL,
  "phone" varchar(20) DEFAULT NULL,
  "email" varchar(255) DEFAULT NULL,
  "avatar" varchar(255) DEFAULT NULL,
  "is_use" tinyint(1) DEFAULT 1,
  "created_at" timestamp NOT NULL DEFAULT current_timestamp,
  "created_by" varchar(40) DEFAULT NULL,
  "updated_at" timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  "updated_by" varchar(40) DEFAULT NULL,
  "deleted" tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY ("id")
);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "sys_user" VALUES ('00000000-0000-0000-0000-000000000000', 'guest', '$2a$10$qYlm0SsCI0Pcyral/LdxhOiIA9mmRiTi12zxRjVoIZ7Btx/ZxtNxK', '访客', '', NULL, NULL, 1, '2024-06-24 15:12:28', NULL, '2024-06-24 15:30:10', '00000000-0000-0000-0000-000000000000', 0);
INSERT INTO "sys_user" VALUES ('ab635c13-d266-42af-8bae-f7834b628f60', 'admin', '$2a$10$TBLXfwLKf.M6zC0XeiOElOGZluu9kMWP1hersa9kbzmzaNeaw2IYS', '超级管理员', '', NULL, NULL, 1, '2022-07-05 17:19:45', NULL, '2023-06-16 08:39:20', NULL, 0);

PRAGMA foreign_keys = true;
