package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;

import java.util.List;

public interface UserService {
    List<User> list(User user);
    User getUserByMobile(String mobile);

    boolean sendCode(String mobile, String deviceId);

    boolean login(String mobile, String code, String deviceId);

    int update(User user);

    void reservation(User user);

    String getMTVersion();
}
