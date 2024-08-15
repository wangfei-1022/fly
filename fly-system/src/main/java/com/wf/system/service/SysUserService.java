package com.wf.system.service;

import com.wf.system.entity.User;

import java.util.List;

public interface SysUserService {
    int addUser(User user);

    int login(User user);

    List<User> list(User user);

    int logout();
}
