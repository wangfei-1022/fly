package com.wf.system.service.impl;

import com.wf.system.entity.User;
import com.wf.system.mapper.UserMapper;
import com.wf.system.model.dto.LoginDTO;
import com.wf.system.service.SysUserService;
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
    public User getOne(LoginDTO loginDTO) {
        User user = userMapper.getOne(loginDTO);
        return user;
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
