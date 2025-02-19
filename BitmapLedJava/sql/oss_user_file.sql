/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:37:07
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for oss_user_file
-- ----------------------------
DROP TABLE IF EXISTS "oss_user_file";
CREATE TABLE "oss_user_file" (
  "id" varchar(40) NOT NULL,
  "md5" char(32) NOT NULL,
  "file_name" varchar(255) NOT NULL,
  "service" varchar(255) NOT NULL,
  "created_at" timestamp DEFAULT NULL,
  "updated_at" timestamp DEFAULT NULL,
  "created_by" varchar(40) DEFAULT NULL,
  "updated_by" varchar(40) DEFAULT NULL,
  "deleted" tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY ("id")
);

PRAGMA foreign_keys = true;
