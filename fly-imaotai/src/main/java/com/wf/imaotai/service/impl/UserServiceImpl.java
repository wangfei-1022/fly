package com.wf.imaotai.service.impl;

import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.model.request.UserRequest;
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
    public List<User> getList(UserRequest userRequest) {
        PageHelper.startPage(userRequest.initPage());
        List<User> userList = userMapper.getList(userRequest);
        return userList;
    }

    @Override
    public User getUserByMobile(String mobile) {
        User user = userMapper.selectByMobile(mobile);
        return user;
    }

    /*
    * 查询设置了预约的用户列表
    * */
    @Override
    public List<User> selectReservationUser() {
        UserRequest userRequest = new UserRequest();
        PageHelper.startPage(1, 10);
        List<User> userList = userMapper.getList(userRequest);
        return userList;
    }


    @Override
    public int update(User user) {
        return userMapper.updateUser(user);
    }
}
