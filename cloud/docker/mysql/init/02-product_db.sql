/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 127.0.0.1:3306
 Source Schema         : product_db

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 17/05/2025 09:01:46
*/
-- 使用 product_db 数据库
CREATE DATABASE IF NOT EXISTS product_db;
USE product_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` decimal(20, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (43, '商品R-018', 179.99);
INSERT INTO `products` VALUES (45, '商品T-020', 199.99);
INSERT INTO `products` VALUES (46, '商品U-021', 209.99);
INSERT INTO `products` VALUES (47, '商品V-02222', 219.99);
INSERT INTO `products` VALUES (48, '商品W-023', 229.99);
INSERT INTO `products` VALUES (49, '商品X-024', 239.99);
INSERT INTO `products` VALUES (50, '商品Y-025', 249.99);
INSERT INTO `products` VALUES (51, '商品Z-026', 259.99);
INSERT INTO `products` VALUES (52, '商品AA-027', 269.99);
INSERT INTO `products` VALUES (53, '商品AB-028', 279.99);
INSERT INTO `products` VALUES (54, '商品AC-029', 289.99);
INSERT INTO `products` VALUES (55, '商品AD-030', 299.99);
INSERT INTO `products` VALUES (56, '商品AE-031', 309.99);
INSERT INTO `products` VALUES (57, '商品AF-032', 319.99);
INSERT INTO `products` VALUES (58, '商品AG-033', 329.99);
INSERT INTO `products` VALUES (59, '商品AH-034', 339.99);
INSERT INTO `products` VALUES (60, '商品AI-035', 349.99);
INSERT INTO `products` VALUES (61, '商品AJ-036', 359.99);
INSERT INTO `products` VALUES (62, '商品AK-037', 369.99);
INSERT INTO `products` VALUES (63, '商品AL-038', 379.99);
INSERT INTO `products` VALUES (64, '商品AM-039', 389.99);
INSERT INTO `products` VALUES (65, '商品AN-040', 399.99);
INSERT INTO `products` VALUES (66, '商品AO-041', 409.99);
INSERT INTO `products` VALUES (67, '商品AP-042', 419.99);
INSERT INTO `products` VALUES (68, '商品AQ-043', 429.99);
INSERT INTO `products` VALUES (69, '商品AR-044', 439.99);
INSERT INTO `products` VALUES (70, '商品AS-045', 449.99);
INSERT INTO `products` VALUES (71, '商品AT-046', 459.99);
INSERT INTO `products` VALUES (72, '商品AU-047', 469.99);
INSERT INTO `products` VALUES (73, '商品AV-048', 479.99);
INSERT INTO `products` VALUES (74, '商品AW-049', 489.99);
INSERT INTO `products` VALUES (75, '商品AX-050', 499.99);
INSERT INTO `products` VALUES (76, '商品AY-051', 509.99);
INSERT INTO `products` VALUES (77, '商品AZ-052', 519.99);
INSERT INTO `products` VALUES (78, '商品BA-053', 529.99);
INSERT INTO `products` VALUES (79, '商品BB-054', 539.99);
INSERT INTO `products` VALUES (80, '商品BC-055', 549.99);
INSERT INTO `products` VALUES (81, '商品BD-056', 559.99);
INSERT INTO `products` VALUES (82, '商品BE-057', 569.99);
INSERT INTO `products` VALUES (83, '商品BF-058', 579.99);
INSERT INTO `products` VALUES (84, '商品BG-059', 589.99);
INSERT INTO `products` VALUES (85, '商品BH-060', 599.99);
INSERT INTO `products` VALUES (86, '商品BI-061', 609.99);
INSERT INTO `products` VALUES (87, '商品BJ-062', 619.99);
INSERT INTO `products` VALUES (88, '商品BK-063', 629.99);
INSERT INTO `products` VALUES (89, '商品BL-064', 639.99);
INSERT INTO `products` VALUES (90, '商品BM-065', 649.99);
INSERT INTO `products` VALUES (91, '商品BN-066', 659.99);
INSERT INTO `products` VALUES (92, '商品BO-067', 669.99);
INSERT INTO `products` VALUES (93, '商品BP-068', 679.99);
INSERT INTO `products` VALUES (94, '商品BQ-069', 689.99);
INSERT INTO `products` VALUES (95, '商品BR-070', 699.99);
INSERT INTO `products` VALUES (96, '商品BS-071', 709.99);
INSERT INTO `products` VALUES (97, '商品BT-072', 719.99);
INSERT INTO `products` VALUES (98, '商品BU-073', 729.99);
INSERT INTO `products` VALUES (99, '商品BV-074', 739.99);
INSERT INTO `products` VALUES (100, '111111', 11.00);

SET FOREIGN_KEY_CHECKS = 1;
