package com.wf.xiaomi.controller;

import com.wf.xiaomi.common.R;
import com.wf.xiaomi.entity.User;
import com.wf.xiaomi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
