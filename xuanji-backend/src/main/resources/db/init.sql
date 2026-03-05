CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  real_name VARCHAR(50),
  phone VARCHAR(20),
  email VARCHAR(100),
  org_id BIGINT,
  post_id BIGINT,
  eoa_id VARCHAR(50),
  usap_account VARCHAR(50),
  status TINYINT DEFAULT 1,
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_username (username),
  INDEX idx_org_id (org_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_post (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  post_code VARCHAR(64) NOT NULL UNIQUE,
  post_name VARCHAR(50) NOT NULL,
  post_sort INT DEFAULT 0,
  status TINYINT DEFAULT 1,
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_post_code (post_code),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_org (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  org_name VARCHAR(100) NOT NULL,
  org_code VARCHAR(50) NOT NULL UNIQUE,
  parent_id BIGINT DEFAULT 0,
  org_level INT DEFAULT 1,
  sort INT DEFAULT 0,
  status TINYINT DEFAULT 1,
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_org_code (org_code),
  INDEX idx_parent_id (parent_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(50) NOT NULL,
  role_code VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(200),
  status TINYINT DEFAULT 1,
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_role_code (role_code),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_menu (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  menu_name VARCHAR(50) NOT NULL,
  menu_type TINYINT NOT NULL,
  parent_id BIGINT DEFAULT 0,
  path VARCHAR(200),
  component VARCHAR(200),
  icon VARCHAR(100),
  permission VARCHAR(100),
  sort INT DEFAULT 0,
  status TINYINT DEFAULT 1,
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_parent_id (parent_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_role (user_id, role_id),
  INDEX idx_user_id (user_id),
  INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role_menu (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_role_menu (role_id, menu_id),
  INDEX idx_role_id (role_id),
  INDEX idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 重点任务表
CREATE TABLE IF NOT EXISTS key_task (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  main_domain VARCHAR(100) NOT NULL COMMENT '主要领域',
  task_name VARCHAR(200) NOT NULL COMMENT '重点任务名称',
  specific_measures TEXT COMMENT '具体举措',
  expected_effect TEXT COMMENT '预期实现效果',
  keyword1 VARCHAR(50) COMMENT '关键词1',
  keyword2 VARCHAR(50) COMMENT '关键词2',
  keyword3 VARCHAR(50) COMMENT '关键词3',
  deliverables TEXT COMMENT '标志成果或交付物',
  lead_department VARCHAR(100) NOT NULL COMMENT '牵头部门',
  lead_person VARCHAR(50) NOT NULL COMMENT '牵头人',
  planned_start_date DATE NOT NULL COMMENT '计划开始时间',
  planned_end_date DATE NOT NULL COMMENT '计划完成时间',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_by VARCHAR(50) COMMENT '更新人',
  deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
  INDEX idx_main_domain (main_domain),
  INDEX idx_lead_department (lead_department),
  INDEX idx_lead_person (lead_person),
  INDEX idx_planned_dates (planned_start_date, planned_end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重点任务表';

-- 插入示例数据
INSERT INTO key_task (main_domain, task_name, specific_measures, expected_effect, keyword1, keyword2, keyword3, deliverables, lead_department, lead_person, planned_start_date, planned_end_date, create_by) VALUES
('数字化转型', '企业级数据中台建设', '1. 构建统一数据采集体系\n2. 建立数据治理框架\n3. 开发数据服务接口', '实现数据资产化管理，提供统一数据服务', '数据中台', '数据治理', '数字化转型', '数据中台平台、数据治理规范文档', '信息技术部', 'admin', '2026-01-01', '2026-12-31', 'admin'),
('产品创新', '新一代智能产品研发', '1. 市场调研与需求分析\n2. 产品原型设计\n3. 技术方案验证\n4. 小批量试产', '推出具有市场竞争力的智能产品，提升品牌影响力', '智能产品', '产品创新', '研发', '产品原型、技术方案、试产报告', '研发部', 'admin', '2026-03-01', '2026-09-30', 'admin'),
('市场拓展', '华东区域市场开发', '1. 建立区域销售团队\n2. 发展渠道合作伙伴\n3. 开展品牌推广活动', '实现华东区域市场占有率提升15%', '市场拓展', '区域开发', '销售', '销售团队建设方案、渠道合作协议、品牌推广计划', '市场部', 'admin', '2026-02-01', '2026-08-31', 'admin');

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE sys_user_role;
TRUNCATE TABLE sys_role_menu;
TRUNCATE TABLE sys_menu;
TRUNCATE TABLE sys_role;
TRUNCATE TABLE sys_post;
TRUNCATE TABLE sys_org;
TRUNCATE TABLE sys_user;

-- 插入机构数据
INSERT INTO sys_org (id, org_name, org_code, parent_id, org_level, sort, status) VALUES
(1, '北京分行', 'BJB', 0, 1, 1, 1),
(2, '办公室', 'BGS', 1, 2, 1, 1),
(3, '信息技术部', 'ITD', 1, 2, 2, 1),
(4, '综合科', 'ZHK', 2, 3, 1, 1),
(5, '技术开发科', 'TDD', 3, 3, 1, 1),
(6, '运行维护科', 'OMD', 3, 3, 2, 1);

-- 插入用户数据
INSERT INTO sys_user (id, username, password, real_name, phone, email, org_id, status) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '管理员', '13800138000', 'admin@abchina.com.cn', 4, 1);

-- 插入角色数据
INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES
(1, 'SUPER_ADMIN', 'SUPER_ADMIN', 'all permissions', 1),
(2, 'USER', 'USER', 'normal user', 1);

-- 插入菜单数据
-- 先插入所有一级菜单
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, path, component, icon, permission, sort, status) VALUES
(1, '任务管理', 0, 0, '/task', 'Layout', 'el-icon-s-claim', 'task:manage:list', 1, 1),
(2, '系统管理', 0, 0, '/system', 'Layout', 'el-icon-setting', '', 2, 1),
(3, '个人中心', 0, 0, '/profile', 'Layout', 'el-icon-user', 'system:profile:list', 3, 1),
(4, '公告管理', 0, 0, '/notice', 'Layout', 'el-icon-bell', 'system:notice:list', 4, 1),
(5, '后台管理', 0, 0, '/admin', 'Layout', 'el-icon-monitor', 'system:admin:list', 5, 1),
(6, '评价管理', 0, 0, '/evaluation', 'Layout', 'el-icon-star-on', 'evaluation:manage:list', 6, 1),
(7, '周报管理', 0, 0, '/weekly', 'Layout', 'el-icon-notebook-2', 'weekly:manage:list', 7, 1);

-- 插入系统管理下的二级菜单
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, path, component, icon, permission, sort, status) VALUES
(21, '用户管理', 1, 2, '/system/user', 'system/user/index', 'el-icon-user', 'system:user:list', 1, 1),
(22, '机构管理', 1, 2, '/system/org', 'system/org/index', 'el-icon-office-building', 'system:org:list', 2, 1),
(23, '角色管理', 1, 2, '/system/role', 'system/role/index', 'el-icon-s-custom', 'system:role:list', 3, 1),
(24, '菜单管理', 1, 2, '/system/menu', 'system/menu/index', 'el-icon-menu', 'system:menu:list', 4, 1);

-- 任务管理下的二级菜单 (parent_id = 1)
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, path, component, icon, permission, sort, status) VALUES
(11, '重点任务', 1, 1, 'key', 'ParentView', 'el-icon-s-flag', 'task:key:list', 1, 1),
(12, '项目工作', 1, 1, 'project', 'ParentView', 'el-icon-s-management', 'task:project:list', 2, 1);

-- 重点任务下的三级菜单 (parent_id = 11)
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, path, component, icon, permission, sort, status) VALUES
(111, '任务看板', 1, 11, 'board', 'task/key/board/index', 'el-icon-data-board', 'task:key:board', 1, 1),
(112, '任务列表', 1, 11, 'list', 'task/key/list/index', 'el-icon-tickets', 'task:key:list', 2, 1),
(113, '批量导入', 1, 11, 'import', 'task/key/import/index', 'el-icon-upload2', 'task:key:import', 3, 1);

-- 项目工作下的三级菜单 (parent_id = 12)
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, path, component, icon, permission, sort, status) VALUES
(121, '项目看板', 1, 12, 'board', 'task/project/board/index', 'el-icon-data-analysis', 'task:project:board', 1, 1),
(122, '项目管理', 1, 12, 'manage', 'task/project/manage/index', 'el-icon-notebook-1', 'task:project:manage', 2, 1),
(123, '项目经理管理', 1, 12, 'pm', 'task/project/pm/index', 'el-icon-s-custom', 'task:project:pm', 3, 1),
(124, '项目维护', 1, 12, 'maintain', 'task/project/maintain/index', 'el-icon-set-up', 'task:project:maintain', 4, 1);

-- 关联权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
