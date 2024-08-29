package com.wf.imaotai.service.impl;

import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
    public User getUserByMobile(Long mobile){
        User user = userMapper.selectByMobile(mobile);
        return user;
    }

    /*
    * 查询设置了预约的用户列表
    * */
    @Override
    public List<User> selectReservationUser() {
        PageHelper.startPage(1, 10);
        List<User> xx1 = userMapper.getList();
        return xx1;
    }


    @Override
    public int update(User user) {
        return userMapper.updateUser(user);
    }
}
