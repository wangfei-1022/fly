package com.wf.system.model.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;

    private String password;

    //    验证码
    private String captcha;
}
