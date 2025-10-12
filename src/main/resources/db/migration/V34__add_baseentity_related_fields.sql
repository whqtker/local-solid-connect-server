-- application 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'application' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE application ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'application' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE application ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- post_image 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'post_image' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE post_image ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'post_image' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE post_image ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- post_like 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'post_like' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE post_like ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'post_like' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE post_like ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- interested_country 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'interested_country' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE interested_country ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'interested_country' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE interested_country ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- interested_region 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'interested_region' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE interested_region ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'interested_region' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE interested_region ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- channel 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'channel' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE channel ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'channel' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE channel ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- mentor 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'mentor' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE mentor ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'mentor' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE mentor ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- mentoring 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'mentoring' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE mentoring ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'mentoring' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE mentoring ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- liked_news 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'liked_news' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE liked_news ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'liked_news' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE liked_news ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- report 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'report' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE report ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'report' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE report ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- site_user 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'site_user' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE site_user ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'site_user' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE site_user ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- language_requirement 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'language_requirement' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE language_requirement ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'language_requirement' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE language_requirement ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- liked_university_info_for_apply 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'liked_university_info_for_apply' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE liked_university_info_for_apply ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'liked_university_info_for_apply' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE liked_university_info_for_apply ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- university_info_for_apply 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'university_info_for_apply' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE university_info_for_apply ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'university_info_for_apply' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE university_info_for_apply ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- university 테이블
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'university' AND COLUMN_NAME = 'created_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE university ADD COLUMN created_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'university' AND COLUMN_NAME = 'updated_at');
SET @s = IF(@col_exists = 0, 'ALTER TABLE university ADD COLUMN updated_at DATETIME(6)', 'SELECT "Column already exists"');
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
