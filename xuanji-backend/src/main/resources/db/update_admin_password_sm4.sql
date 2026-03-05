-- 更新admin用户密码为正确的SM4加密
-- 123456Qq! 的SM4加密: 2c1bd96631f850bc03d3daa8b7232dd6
UPDATE sys_user SET password = '2c1bd96631f850bc03d3daa8b7232dd6' WHERE username = 'admin';
