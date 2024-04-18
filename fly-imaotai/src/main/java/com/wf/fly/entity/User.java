package com.wf.fly.entity;

import lombok.Data;
import java.io.Serializable;
@Data
public class User implements Serializable{
    private Long id;
    private String name;
    private String password;
    private Long mobile;
    private String email;

}
