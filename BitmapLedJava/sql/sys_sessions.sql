/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:37:42
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for sys_sessions
-- ----------------------------
DROP TABLE IF EXISTS "sys_sessions";
CREATE TABLE "sys_sessions" (
  "id" varchar(40) NOT NULL,
  "data" mediumtext,
  "created_at" timestamp DEFAULT NULL,
  "updated_at" timestamp DEFAULT NULL,
  "deleted" tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY ("id")
);

PRAGMA foreign_keys = true;
