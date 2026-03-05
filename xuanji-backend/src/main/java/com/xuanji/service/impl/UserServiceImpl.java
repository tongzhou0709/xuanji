package com.xuanji.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanji.common.BusinessException;
import com.xuanji.dto.LoginDTO;
import com.xuanji.dto.UserImportDTO;
import com.xuanji.entity.Org;
import com.xuanji.entity.Post;
import com.xuanji.entity.Role;
import com.xuanji.entity.User;
import com.xuanji.mapper.OrgMapper;
import com.xuanji.mapper.PostMapper;
import com.xuanji.mapper.RoleMapper;
import com.xuanji.mapper.UserMapper;
import com.xuanji.service.UserService;
import com.xuanji.utils.JwtUtils;
import com.xuanji.utils.SM4Utils;
import com.xuanji.vo.LoginVO;
import com.xuanji.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final OrgMapper orgMapper;
    private final PostMapper postMapper;
    private final RoleMapper roleMapper;
    private final JwtUtils jwtUtils;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            log.error("用户不存在: {}", loginDTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        log.info("数据库密码: {}", user.getPassword());
        log.info("输入密码: {}", loginDTO.getPassword());
        
        boolean matches = SM4Utils.matches(loginDTO.getPassword(), user.getPassword());
        log.info("SM4匹配结果: {}", matches);
        
        if (!matches) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        log.info("登录获取用户角色: userId={}, username={}", user.getId(), user.getUsername());
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        log.info("登录用户角色列表: {}", roles);
        if (roles == null) {
            roles = new ArrayList<>();
        }
        
        List<String> permissions;

        if (roles.contains("SUPER_ADMIN")) {
            permissions = Arrays.asList("*:*:*");
        } else {
            permissions = userMapper.selectPermissionsByUserId(user.getId());
            log.info("登录用户权限列表: {}", permissions);
            if (permissions == null) {
                permissions = new ArrayList<>();
            }
        }

        return new LoginVO(token, user.getUsername(), user.getRealName(), roles, permissions);
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            log.error("用户不存在: {}", username);
            throw new BusinessException(401, "用户不存在");
        }

        log.info("获取用户角色: userId={}, username={}", user.getId(), username);
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        log.info("用户角色列表: {}", roles);
        if (roles == null) {
            roles = new ArrayList<>();
        }
        
        List<String> permissions;

        if (roles.contains("SUPER_ADMIN")) {
            permissions = Arrays.asList("*:*:*");
        } else {
            permissions = userMapper.selectPermissionsByUserId(user.getId());
            log.info("用户权限列表: {}", permissions);
            if (permissions == null) {
                permissions = new ArrayList<>();
            }
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setRealName(user.getRealName());
        userInfoVO.setAvatar("");
        userInfoVO.setRoles(roles);
        userInfoVO.setPermissions(permissions);

        return userInfoVO;
    }

    @Override
    public Page<User> page(Page<User> page, User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (user != null) {
            if (StringUtils.hasText(user.getUsername())) {
                wrapper.like(User::getUsername, user.getUsername());
            }
            if (StringUtils.hasText(user.getRealName())) {
                wrapper.like(User::getRealName, user.getRealName());
            }
            if (user.getOrgId() != null) {
                wrapper.eq(User::getOrgId, user.getOrgId());
            }
        }
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        
        // 填充机构名称
        userPage.getRecords().forEach(u -> {
            if (u.getOrgId() != null) {
                Org org = orgMapper.selectById(u.getOrgId());
                if (org != null) {
                    u.setOrgName(org.getOrgName());
                }
            }
        });
        
        return userPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        validateUser(user);
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(SM4Utils.encrypt(user.getPassword()));
        } else {
            user.setPassword(SM4Utils.encrypt("123456Qq!"));
        }
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        validateUser(user);
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(SM4Utils.encrypt(user.getPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user != null && user.getOrgId() != null) {
            Org org = orgMapper.selectById(user.getOrgId());
            if (org != null) {
                user.setOrgName(org.getOrgName());
            }
            if (user.getPostId() != null) {
                Post post = postMapper.selectById(user.getPostId());
                if (post != null) {
                    user.setPostName(post.getPostName());
                }
            }
        }
        return user;
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(400, "用户不存在");
        }
        if (!SM4Utils.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "原密码错误");
        }
        user.setPassword(SM4Utils.encrypt(newPassword));
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importUsers(MultipartFile file) {
        List<UserImportDTO> list = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), UserImportDTO.class, new ReadListener<UserImportDTO>() {
                @Override
                public void invoke(UserImportDTO data, AnalysisContext context) {
                    list.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().doRead();
        } catch (IOException e) {
            throw new BusinessException(500, "文件读取失败");
        }

        int count = 0;
        for (UserImportDTO dto : list) {
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setRealName(dto.getRealName());
            user.setPhone(dto.getPhone());
            user.setEmail(dto.getEmail());
            user.setEoaId(dto.getEoaId());
            user.setUsapAccount(dto.getUsapAccount());
            user.setStatus(1);
            user.setPassword(SM4Utils.encrypt("123456Qq!"));

            // 处理部门
            if (StringUtils.hasText(dto.getOrgName())) {
                LambdaQueryWrapper<Org> orgWrapper = new LambdaQueryWrapper<>();
                orgWrapper.eq(Org::getOrgName, dto.getOrgName());
                Org org = orgMapper.selectOne(orgWrapper);
                if (org != null) {
                    user.setOrgId(org.getId());
                }
            }

            // 处理职位
            if (StringUtils.hasText(dto.getPostName())) {
                LambdaQueryWrapper<Post> postWrapper = new LambdaQueryWrapper<>();
                postWrapper.eq(Post::getPostName, dto.getPostName());
                Post post = postMapper.selectOne(postWrapper);
                if (post != null) {
                    user.setPostId(post.getId());
                }
            }

            validateUser(user);
            userMapper.insert(user);

            // 处理角色
            if (StringUtils.hasText(dto.getRoleNames())) {
                String[] roleNames = dto.getRoleNames().split(",");
                for (String roleName : roleNames) {
                    LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
                    roleWrapper.eq(Role::getRoleName, roleName.trim());
                    Role role = roleMapper.selectOne(roleWrapper);
                    if (role != null) {
                        userMapper.insertUserRole(user.getId(), role.getId());
                    }
                }
            }
            count++;
        }
        return count;
    }

    private void validateUser(User user) {
        if (StringUtils.hasText(user.getEmail())) {
            if (!user.getEmail().endsWith("@abchina.com.cn")) {
                throw new BusinessException(400, "邮箱必须以@abchina.com.cn结尾");
            }
        }
        
        // 校验机构级别：用户必须属于“科级”（Level 3）
        if (user.getOrgId() != null) {
            Org org = orgMapper.selectById(user.getOrgId());
            if (org == null || org.getOrgLevel() != 3) {
                throw new BusinessException(400, "用户必须属于科级机构");
            }
        } else {
            throw new BusinessException(400, "用户必须所属一个机构");
        }
    }
}
