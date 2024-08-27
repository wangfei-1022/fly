package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.service.IMTService;
import com.wf.imaotai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imt/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IMTService imtService;

    @GetMapping("/list")
    public R getList(User user){
        List<User> items = userService.list(user);
        PageInfo<User> itemPageInfo = new PageInfo<>(items);
        return R.success(itemPageInfo);
    }
    @GetMapping("/getUserByMobile")
    public R<User> getUserByMobile(@RequestParam Long mobile){
        User user = userService.getUserByMobile(mobile);
        return R.success(user);
    }

    @PostMapping("/login")
    public R<String> login(@RequestParam Long mobile, @RequestParam String code, @RequestParam String deviceId){
        imtService.login(mobile, code, deviceId);
        return R.success("登录成功");
    }

    @PostMapping("/update")
    public R<String> update(@RequestBody User user){
        userService.update(user);
        return R.success("修改成功");
    }

    @PostMapping("/sendCode")
    public R<String> sendCode(@RequestBody User user){
        imtService.sendCode(user.getMobile(), user.getDeviceId());
        return R.success("新建成功");
    }

    @PostMapping("/reservation")
    public R<String> reservation(@RequestBody User user){
        imtService.reservation(user.getMobile());
        return R.success("预约成功");
    }

    @PostMapping("/travelReward")
    public R<String> travelReward(@RequestBody User user){
        imtService.travelReward(user.getMobile());
        return R.success("旅行成功");
    }

}
