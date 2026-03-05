SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE sys_org;
INSERT INTO sys_org (id, org_name, org_code, parent_id, org_level, sort, status) VALUES
(1, '北京分行', 'BJB', 0, 1, 1, 1),
(2, '办公室', 'BGS', 1, 2, 1, 1),
(3, '信息技术部', 'ITD', 1, 2, 2, 1),
(4, '综合科', 'ZHK', 2, 3, 1, 1),
(5, '技术开发科', 'TDD', 3, 3, 1, 1),
(6, '运行维护科', 'OMD', 3, 3, 2, 1);
SET FOREIGN_KEY_CHECKS = 1;

-- 更新管理员用户的 org_id 到一个科级机构（如综合科 ID=4）
UPDATE sys_user SET org_id = 4 WHERE username = 'admin';
