CREATE DATABASE IF NOT EXISTS bds360_identity_db;
CREATE DATABASE IF NOT EXISTS bds360_payment_db;
CREATE DATABASE IF NOT EXISTS bds360_listing_db;
CREATE DATABASE IF NOT EXISTS bds360_notification_db;
-- CREATE DATABASE IF NOT EXISTS bds360_statistic_db;

-- Chỉ cần gán quyền, MySQL tự động hiểu mật khẩu root từ environment
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;