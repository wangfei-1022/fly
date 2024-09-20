package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.model.request.UserRequest;
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

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    public R getList(UserRequest userRequest) {
        List<User> items = userService.getList(userRequest);
        PageInfo<User> itemPageInfo = new PageInfo<>(items);
        return R.success(itemPageInfo);
    }

    @GetMapping("/getUserByMobile")
    public R<User> getUserByMobile(@RequestParam String mobile) {
        User user = userService.getUserByMobile(mobile);
        return R.success(user);
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody UserRequest userRequest) {
        imtService.login(userRequest.getMobile(), userRequest.getCode(), userRequest.getDeviceId());
        return R.success("登录成功");
    }

    @PostMapping("/update")
    public R<String> update(@RequestBody User user) {
        userService.update(user);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    public R<String> delete(@RequestBody User user) {
        userService.delete(user);
        return R.success("删除成功");
    }

    @PostMapping("/sendCode")
    public R<String> sendCode(@RequestBody User user) {
        imtService.sendCode(user.getMobile(), user.getDeviceId());
        return R.success("新建成功");
    }

    @PostMapping("/reservation")
    public R<String> reservation(@RequestBody UserRequest userRequest) {
        User user = userMapper.selectByMobile(userRequest.getMobile());
        imtService.reservation(user);
        return R.success("预约成功");
    }

    @PostMapping("/travelReward")
    public R<String> travelReward(@RequestBody User user) {
        imtService.travelReward(user.getMobile());
        return R.success("旅行成功");
    }

}
