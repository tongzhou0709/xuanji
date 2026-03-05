package com.xuanji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanji.dto.LoginDTO;
import com.xuanji.entity.User;
import com.xuanji.vo.LoginVO;
import com.xuanji.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    LoginVO login(LoginDTO loginDTO);

    UserInfoVO getUserInfo(String username);

    Page<User> page(Page<User> page, User user);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    void updatePassword(Long userId, String oldPassword, String newPassword);

    int importUsers(MultipartFile file);

}
