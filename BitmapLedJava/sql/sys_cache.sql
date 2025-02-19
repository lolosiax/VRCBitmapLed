/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:37:15
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for sys_cache
-- ----------------------------
DROP TABLE IF EXISTS "sys_cache";
CREATE TABLE "sys_cache" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "path" varchar(255) NOT NULL,
  "tag" varchar(255) DEFAULT NULL,
  "value" mediumtext,
  "expires_at" timestamp DEFAULT NULL,
  "created_by" varchar(40) DEFAULT NULL,
  "created_at" timestamp DEFAULT NULL,
  "updated_by" varchar(40) DEFAULT NULL,
  "updated_at" timestamp DEFAULT NULL,
  "deleted" tinyint(1) DEFAULT NULL
);

PRAGMA foreign_keys = true;
