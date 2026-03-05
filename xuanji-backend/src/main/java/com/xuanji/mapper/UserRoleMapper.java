package com.xuanji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuanji.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1")
    List<com.xuanji.entity.Role> selectRolesByUserId(Long userId);
}
