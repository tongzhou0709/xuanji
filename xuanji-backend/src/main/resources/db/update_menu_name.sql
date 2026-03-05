UPDATE sys_menu SET menu_name = '系统管理' WHERE path = '/system' AND parent_id = 0;
UPDATE sys_menu SET menu_name = '用户管理' WHERE path = '/system/user';
UPDATE sys_menu SET menu_name = '机构管理' WHERE path = '/system/org';
UPDATE sys_menu SET menu_name = '角色管理' WHERE path = '/system/role';
UPDATE sys_menu SET menu_name = '菜单管理' WHERE path = '/system/menu';
