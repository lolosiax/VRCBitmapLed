/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:44:17
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role" (
  "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  "role_name" varchar(50) DEFAULT NULL,
  "type" varchar(50) DEFAULT NULL,
  "created_at" timestamp DEFAULT NULL,
  "created_by" varchar(40) DEFAULT NULL,
  "updated_at" timestamp DEFAULT NULL,
  "updated_by" varchar(40) DEFAULT NULL,
  "deleted" tinyint(1) NOT NULL DEFAULT 0
);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "sys_role" VALUES (1, '超级管理员', 'super_admin', '2022-06-05 10:44:51', NULL, '2023-05-29 09:34:56', NULL, 0);
INSERT INTO "sys_role" VALUES (2, '管理员', 'admin', '2022-07-05 13:48:08', NULL, '2022-11-13 16:34:33', NULL, 0);
INSERT INTO "sys_role" VALUES (4, '用户', 'user', '2022-10-10 15:52:45', NULL, '2023-01-10 15:13:39', NULL, 0);
INSERT INTO "sys_role" VALUES (5, '访客', 'guest', '2024-06-24 15:57:06', NULL, '2024-06-24 15:57:08', NULL, 0);

-- ----------------------------
-- Auto increment value for sys_role
-- ----------------------------
UPDATE "sqlite_sequence" SET seq = 5 WHERE name = 'sys_role';

PRAGMA foreign_keys = true;
