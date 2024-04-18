package com.wf.fly.service;

import com.wf.fly.entity.User;

public interface UserService {
    int addUser(User user);

    int login(User user);
}
