-- 角色权限初始化脚本
-- 版本: 1.0.2
DELETE FROM sys_user_role;
DELETE FROM sys_role_menu;
DELETE FROM sys_role;
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES (1, '超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 1);
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES (2, '总经理', 'GENERAL_MANAGER', '管理所有事务和用户', 1);
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES (3, '副总经理', 'DEPUTY_GENERAL_MANAGER', '协助总经理管理事务', 1);
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES (4, '经理', 'MANAGER', '管理部门日常事务', 1);
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES (5, '副经理', 'DEPUTY_MANAGER', '协助经理管理事务', 1);
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES (6, '员工', 'EMPLOYEE', '普通员工权限', 1);
INSERT INTO sys_role_menu (role_id, menu_id) SELECT 1, id FROM sys_menu;
INSERT INTO sys_role_menu (role_id, menu_id) SELECT 2, id FROM sys_menu;
INSERT INTO sys_role_menu (role_id, menu_id) SELECT 3, id FROM sys_menu;
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (4, 1), (4, 21), (4, 3), (4, 4), (4, 6), (4, 7);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (5, 1), (5, 3), (5, 4), (5, 7);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (6, 1), (6, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);
