package com.wf.xiaomi.service;

import com.wf.xiaomi.entity.User;

public interface UserService {
    int addUser(User user);

    int login(User user);
}
