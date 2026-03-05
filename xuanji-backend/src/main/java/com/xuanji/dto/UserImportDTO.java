package com.xuanji.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserImportDTO {

    @NotBlank(message = "用户名不能为空")
    @ExcelProperty("用户名")
    private String username;

    @NotBlank(message = "姓名不能为空")
    @ExcelProperty("姓名")
    private String realName;

    @ExcelProperty("手机号")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@abchina\\.com\\.cn$", message = "邮箱必须以@abchina.com.cn结尾")
    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("EOA ID")
    private String eoaId;

    @ExcelProperty("USAP账号")
    private String usapAccount;

    @ExcelProperty("部门")
    private String orgName;

    @ExcelProperty("职位")
    private String postName;

    @ExcelProperty("角色")
    private String roleNames;

}
