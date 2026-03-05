package com.xuanji.service;

import com.xuanji.entity.Menu;
import com.xuanji.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> list();
    void saveRole(Role role);
    void updateRole(Role role);
    void deleteRole(Long id);
    
    List<Menu> getMenusByRoleId(Long roleId);
    void assignMenus(Long roleId, List<Long> menuIds);
    
    List<Role> getRolesByUserId(Long userId);
    void assignUserRoles(Long userId, List<Long> roleIds);
}
