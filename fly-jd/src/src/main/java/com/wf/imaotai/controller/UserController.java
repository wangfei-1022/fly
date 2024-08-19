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
@RequestMapping("/imt/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public R getList(User user){
        List<User> items = userService.list(user);
        PageInfo<User> itemPageInfo = new PageInfo<>(items);
        return R.success(itemPageInfo);
    }
    @GetMapping("/getUserByMobile")
    public R<User> getUserByMobile(@RequestParam String mobile){
        User user = userService.getUserByMobile(mobile);
        return R.success(user);
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody User user){
        userService.login(user.getMobile(), user.getCode(), user.getDeviceId());
        return R.success("登录成功");
    }

    @PostMapping("/sendCode")
    public R<String> sendCode(@RequestBody User user){
        userService.sendCode(user.getMobile(), user.getDeviceId());
        return R.success("新建成功");
    }

    @PostMapping("/sendCode")
    public R<String> reservation(@RequestBody User user){
        userService.reservation(user);
        return R.success("新建成功");
    }

}
