package com.wf.system.controller;

import com.wf.common.common.R;
import com.wf.system.entity.User;
import com.wf.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/getUser")
    public R<String> getUser(){
        return R.success("Get唱歌不犯法");
    }

    @GetMapping("/list")
    public R<List<User>> list(){
        User user = new User();
        List<User> userList =  sysUserService.list(user);
        return R.success(userList);
    }

    @GetMapping("/getRouters")
    public R<String> getRouters(){
        return R.success("Get唱歌不犯法");
    }

    @GetMapping("/getInfo")
    public R<User> getInfo(){
        User user = new User();
        List<String> list = new ArrayList<>();
        list.add("1");
        user.setRoles(list);
        return R.success(user);
    }
    @PostMapping("/login")
    public R<String> login(@RequestBody User user){
        sysUserService.login(user);
        return R.success("登录成功");
    }

    @PostMapping("/add")
    public R<String> addUser(@RequestBody User user){
        sysUserService.addUser(user);
        return R.success("新建成功");
    }

    @PostMapping("/logout")
    public R<String> logout(){
        sysUserService.logout();
        return R.success("退出成功");
    }

}
