package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;

public interface UserService {
    int addUser(User user);

    int login(User user);
}
