package com.wf.imaotai.service.impl;

import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.model.request.ShopRequest;
import com.wf.imaotai.model.request.UserRequest;
import com.wf.imaotai.service.ShopService;
import com.wf.imaotai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopService shopService;

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

    /*
     * 查询设置了预约的用户列表
     * */
    @Override
    public List<User> selectReservationUserByMinute(int minute) {
        UserRequest userRequest = new UserRequest();
        userRequest.setMinute(minute);
        PageHelper.startPage(1, 10);
        List<User> userList = userMapper.getList(userRequest);
        return userList;
    }


    @Override
    public int update(User user) {
        Shop shop = null;
        if(user.getShopId() != null) {
            shop = shopService.getShopById(user.getShopId());
        } else {
            ShopRequest shopRequest = new ShopRequest();
            shopRequest.setProvinceName(user.getProvinceName());
            shopRequest.setCityName(user.getCityName());
            List<Shop> shops = shopService.getShopList(shopRequest);
            shop = shops.get(0);
        }
        if(shop != null) {
            user.setLat(shop.getLat());
            user.setLng(shop.getLng());
        }
        return userMapper.updateUser(user);
    }

    @Override
    public int delete(User user) {
        return userMapper.deleteUser(user);
    }

    @Override
    public void updateUserMinuteBatch() {
        Long userCount = userMapper.selectCount();
        if (userCount > 60) {
            userMapper.updateUserMinuteEven();
        }else {
            userMapper.updateUserMinuteBatch();
        }
    }
}
