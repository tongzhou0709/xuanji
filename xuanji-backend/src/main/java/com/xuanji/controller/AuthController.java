package com.xuanji.controller;

import com.xuanji.common.Result;
import com.xuanji.dto.LoginDTO;
import com.xuanji.service.UserService;
import com.xuanji.utils.JwtUtils;
import com.xuanji.vo.LoginVO;
import com.xuanji.vo.UserInfoVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(@RequestHeader("Authorization") String token) {
        // 从token中提取用户名
        String username = jwtUtils.getUsernameFromToken(token.replace("Bearer ", ""));
        UserInfoVO userInfoVO = userService.getUserInfo(username);
        return Result.success(userInfoVO);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // 这里可以做一些清理工作，比如将token加入黑名单
        return Result.success();
    }

}
