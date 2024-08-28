package com.wf.imaotai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Log implements Serializable {

    private Long id;

    private Long userId;

    private String ip;

    private String url;

    private String content;

    private String method;

    private String requestMethod;

    private String appName;

    private String logName;

    private Long mobile;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
