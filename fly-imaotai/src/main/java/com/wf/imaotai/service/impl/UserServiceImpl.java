package com.wf.imaotai.service.impl;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wf.common.exception.ServiceException;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.service.ItemService;
import com.wf.imaotai.service.ShopService;
import com.wf.imaotai.service.UserService;
import com.wf.imaotai.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    @Override
    public int update(User user) {
        return userMapper.updateUser(user);
    }
}
