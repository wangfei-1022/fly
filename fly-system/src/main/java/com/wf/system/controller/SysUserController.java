package com.wf.system.controller;

import com.wf.common.common.R;
import com.wf.system.config.JwtProperties;
import com.wf.system.entity.User;
import com.wf.system.model.dto.LoginDTO;
import com.wf.system.model.vo.LoginVo;
import com.wf.system.service.SysUserService;
import com.wf.system.util.CaptchaUtil;
import com.wf.system.util.JwtUtil;
import com.wf.system.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/system/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private JwtProperties jwtProperties;

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

    public String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i <length; i++){
            int index  = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    @GetMapping("/getCaptcha")
    public R<String> GetCaptcha(HttpSession session) {
        // 随机生成四位验证码原始数据
        String randomString = generateRandomString(4);
        System.out.println("captchaCode " + randomString);

        // 将验证码保存到session中
        session.setAttribute("captcha", randomString); // 使用方法参数session
        String ImageByBase64 = CaptchaUtil.generateCaptchaImage(randomString);
        return R.success(ImageByBase64);
    }

    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        String captcha = (String) session.getAttribute("captcha");
        System.out.println("captchaCode " + captcha + "传递过来的验证码" + loginDTO.getCaptcha());
        if(loginDTO.getCaptcha() == null || !loginDTO.getCaptcha().equalsIgnoreCase(captcha)) {
            session.removeAttribute("captcha");
            return R.error("验证码出错了");
        }
        String encryptToMD5 = MD5Util.encryptToMD5(loginDTO.getPassword());

        User user = sysUserService.getUserByMobile(loginDTO.getUsername());

        if(user == null) {
            session.removeAttribute("captcha");
            return R.error("查不到此用户");
        }

        if(!user.getPassword().equals(encryptToMD5)) {
            session.removeAttribute("captcha");
            return R.error("用户密码不对");
        }
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(user,loginVo);
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
        loginVo.setToken(token);
        return R.success(loginVo);
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
