package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;

import java.util.List;

public interface UserService {
    List<User> list(User user);

    int addUser(User user);

    int login(User user);
}
