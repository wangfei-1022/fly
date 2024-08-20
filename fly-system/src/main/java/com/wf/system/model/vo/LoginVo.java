package com.wf.system.model.vo;

import lombok.Data;

@Data
public class LoginVo {
    private Integer id;

    private String account;

    private Integer isvip;

    private Object status;

    private String token;
}
