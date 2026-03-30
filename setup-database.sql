-- ============================================================
--   LOKA eCommerce — MySQL Database Setup Script
--   Run this ONCE before starting the backend services.
--   Make sure MySQL is running on localhost:3306
-- ============================================================

-- Create databases (Spring Boot will create tables automatically via Hibernate DDL)
CREATE DATABASE IF NOT EXISTS loka_users CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS loka_products CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS loka_orders CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create application user (optional — uses root by default)
-- CREATE USER IF NOT EXISTS 'loka_user'@'localhost' IDENTIFIED BY 'loka_password';
-- GRANT ALL PRIVILEGES ON loka_users.* TO 'loka_user'@'localhost';
-- GRANT ALL PRIVILEGES ON loka_products.* TO 'loka_user'@'localhost';
-- GRANT ALL PRIVILEGES ON loka_orders.* TO 'loka_user'@'localhost';
-- FLUSH PRIVILEGES;

SELECT 'Databases created successfully!' AS Status;
SHOW DATABASES LIKE 'loka_%';
