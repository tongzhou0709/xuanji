package com.xuanji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuanji.entity.Menu;
import com.xuanji.entity.Role;
import com.xuanji.entity.RoleMenu;
import com.xuanji.entity.UserRole;
import com.xuanji.mapper.MenuMapper;
import com.xuanji.mapper.RoleMapper;
import com.xuanji.mapper.RoleMenuMapper;
import com.xuanji.mapper.UserRoleMapper;
import com.xuanji.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final MenuMapper menuMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public List<Role> list() {
        List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<Role>().eq(Role::getStatus, 1));
        
        for (Role role : roles) {
            List<Menu> menus = getMenusByRoleId(role.getId());
            List<String> menuNames = new ArrayList<>();
            for (Menu menu : menus) {
                menuNames.add(menu.getMenuName());
            }
            role.setMenuNames(menuNames);
        }
        
        return roles;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Role role) {
        roleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        roleMapper.deleteById(id);
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(
            new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId)
        );
        
        if (roleMenus.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> menuIds = new ArrayList<>();
        for (RoleMenu rm : roleMenus) {
            menuIds.add(rm.getMenuId());
        }
        
        return menuMapper.selectBatchIds(menuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return userRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }
}
