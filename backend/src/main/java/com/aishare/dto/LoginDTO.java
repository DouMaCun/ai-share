package com.aishare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "账号不能为空")
    private String account; // email or username

    @NotBlank(message = "密码不能为空")
    private String password;
}
