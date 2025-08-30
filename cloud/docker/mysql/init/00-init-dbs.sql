-- 创建多个数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS auth_db;
CREATE DATABASE IF NOT EXISTS product_db;

-- 创建用户（如果不存在）
CREATE USER IF NOT EXISTS 'auth_user'@'%' IDENTIFIED BY 'Adm!nP@ss2024';
CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'Adm!nP@ss2024';

-- 授权数据库访问
GRANT ALL PRIVILEGES ON auth_db.* TO 'auth_user'@'%';
GRANT ALL PRIVILEGES ON product_db.* TO 'product_user'@'%';

FLUSH PRIVILEGES;
