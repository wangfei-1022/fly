package com.wf.fly.service;

import com.wf.fly.entity.User;

import java.util.List;

public interface SysUserService {
    int addUser(User user);

    int login(User user);

    List<User> list(User user);
}
