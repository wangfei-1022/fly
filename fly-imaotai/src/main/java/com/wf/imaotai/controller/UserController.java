package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imaotai/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public R getList(User user){
        List<User> items = userService.list(user);
        PageInfo<User> itemPageInfo = new PageInfo<>(items);
        return R.success(itemPageInfo);
    }
    @GetMapping("/getUser")
    public R<String> getUser(){
        return R.success("登录成功");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody User user){
        userService.login(user);
        return R.success("登录成功");
    }

    @PostMapping("/add")
    public R<String> addUser(@RequestBody User user){
        userService.addUser(user);
        return R.success("新建成功");
    }

}
