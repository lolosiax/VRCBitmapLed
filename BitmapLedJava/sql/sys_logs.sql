/*
 Navicat Premium Dump SQL

 Source Server         : BitmapLedJava
 Source Server Type    : SQLite
 Source Server Version : 3045000 (3.45.0)
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3045000 (3.45.0)
 File Encoding         : 65001

 Date: 19/02/2025 19:37:23
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS "sys_logs";
CREATE TABLE "sys_logs" (
  "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  "level" varchar(10),
  "user_id" varchar(40),
  "user_name" varchar(50),
  "method" varchar(10),
  "url" text,
  "params" text,
  "response" text,
  "created_by" varchar(40),
  "updated_by" varchar(40),
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL,
  "deleted" tinyint(1) NOT NULL DEFAULT 0
);

PRAGMA foreign_keys = true;
