-- ============================================================
-- 功能描述：用户管理模块优化
-- 作者：AI Assistant
-- 日期：2026-03-04
-- 版本：v1.0.2
-- 说明：用户创建时必须属于某个机构，添加机构树展示，点击用户跳转详情页
-- ============================================================

-- 本次更新无需数据库结构变更，用户表已包含org_id字段
-- 如需验证用户表结构，执行：
-- DESCRIBE sys_user;

-- 验证数据
SELECT u.id, u.username, u.real_name, u.eoa_id, u.email, o.org_name 
FROM sys_user u 
LEFT JOIN sys_org o ON u.org_id = o.id 
ORDER BY u.id 
LIMIT 10;
