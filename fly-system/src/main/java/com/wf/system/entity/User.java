package com.wf.system.entity;

import com.wf.common.entity.BaseEntity;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class User extends BaseEntity implements Serializable {
    private Long id;
    private String name;
    private String password;
    private Long mobile;
    private String email;
    private List<String > roles;

    public User(){

    }

}
