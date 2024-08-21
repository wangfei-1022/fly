package com.wf.system.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginVo {
    private Long id;
    private String name;
    private String email;
    private Long mobile;
    private Date createTime;
    private Date updateTime;
    private String token;
}
