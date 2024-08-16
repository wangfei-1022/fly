package com.wf.imaotai.service.impl;

import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> list(User user) {
        PageHelper.startPage(1, 10);
        List<User> xx1 = userMapper.getList();
        return xx1;
    }

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
}
