/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:42:02
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for sys_user_roles
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_roles";
CREATE TABLE "sys_user_roles" (
  "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  "user_id" varchar(40) NOT NULL,
  "role_id" integer NOT NULL,
  "created_at" timestamp DEFAULT NULL,
  "created_by" varchar(40) DEFAULT NULL,
  "updated_at" timestamp DEFAULT NULL,
  "updated_by" varchar(40) DEFAULT NULL,
  "deleted" tinyint(1) NOT NULL DEFAULT 0
);

-- ----------------------------
-- Records of sys_user_roles
-- ----------------------------
INSERT INTO "sys_user_roles" VALUES (1, '00000000-0000-0000-0000-000000000000', 4, NULL, NULL, NULL, NULL, 0);
INSERT INTO "sys_user_roles" VALUES (2, 'ab635c13-d266-42af-8bae-f7834b628f60', 1, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Auto increment value for sys_user_roles
-- ----------------------------
UPDATE "sqlite_sequence" SET seq = 2 WHERE name = 'sys_user_roles';

PRAGMA foreign_keys = true;
