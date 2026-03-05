-- 更新admin用户密码为 123456Qq!
-- 使用BCrypt加密后的密码 (cost factor: 10)
UPDATE sys_user SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE username = 'admin';