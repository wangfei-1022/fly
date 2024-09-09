package com.wf.system.service;

import com.wf.system.entity.User;
import com.wf.system.model.dto.LoginDTO;

import java.util.List;

public interface SysUserService {
    int addUser(User user);

    User getUserByMobile(String mobile);

    List<User> list(User user);

    int logout();
}
