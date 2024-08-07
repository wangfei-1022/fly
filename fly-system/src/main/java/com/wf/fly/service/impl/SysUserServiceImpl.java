package com.wf.fly.service.impl;

import com.wf.fly.entity.User;
import com.wf.fly.mapper.UserMapper;
import com.wf.fly.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public int addUser(User user) {
        userMapper.addUser(user);
        return 0;
    }

    @Override
    public int login(User user) {
        userMapper.login(user);
        return 0;
    }

    @Override
    public List<User> list(User user) {
        List<User> userList = userMapper.getList(user);
        return userList;
    }

    @Override
    public int logout() {
        return 0;
    }
}
