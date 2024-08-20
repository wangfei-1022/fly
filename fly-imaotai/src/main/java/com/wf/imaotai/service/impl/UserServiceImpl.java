package com.wf.imaotai.service.impl;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.service.ItemService;
import com.wf.imaotai.service.ShopService;
import com.wf.imaotai.service.UserService;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public ShopService shopService;

    @Autowired
    public ItemService itemService;

    private final static String SALT = "2af72f100c356273d46284f6fd1dfc08";
    private final static String AES_KEY = "qbhajinldepmucsonaaaccgypwuvcjaa";
    private final static String AES_IV = "2018534749963515";

    @Override
    public List<User> list(User user) {
        PageHelper.startPage(1, 10);
        List<User> xx1 = userMapper.getList();
        return xx1;
    }

    @Override
    public User getUserByMobile(String mobile){
        User user = userMapper.selectByMobile(mobile);
        return user;
    }

    private static String signature(String content, long time) {

        String text = SALT + content + time;
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    @Override
    public String getMTVersion() {
        String mtVersion = "";
        String url = "https://apps.apple.com/cn/app/i%E8%8C%85%E5%8F%B0/id1600482450";
        String htmlContent = restTemplate.getForObject(url, String.class);
        Pattern pattern = Pattern.compile("new__latest__version\">(.*?)</p>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(htmlContent);
        if (matcher.find()) {
            mtVersion = matcher.group(1);
            mtVersion = mtVersion.replace("版本 ", "");
        }
        return mtVersion;

    }

    @Override
    public boolean sendCode(String mobile, String deviceId) {
        Map<String, Object> data = new HashMap<>();
        data.put("mobile", mobile);
        final long curTime = System.currentTimeMillis();
        data.put("md5", signature(mobile, curTime));
        data.put("timestamp", String.valueOf(curTime));
//        data.put("MT-APP-Version", MT_VERSION);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("MT-Device-ID", deviceId);
        httpHeaders.add("MT-APP-Version", getMTVersion());
        httpHeaders.add("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<Map> entity = new HttpEntity<>(data, httpHeaders);

        String res = restTemplate.postForObject("https://app.moutai519.com.cn/xhr/front/user/register/vcode", entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(res);
        //成功返回 {"code":2000}
        System.out.println("「发送验证码返回」：" + jsonObject.toJSONString());
        if (jsonObject.getString("code").equals("2000")) {
            return Boolean.TRUE;
        } else {
            System.out.println("「发送验证码-失败」：" + jsonObject.toJSONString());
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean login(String mobile, String code, String deviceId) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("vCode", code);

        final long curTime = System.currentTimeMillis();
        map.put("md5", signature(mobile + code + "" + "", curTime));

        map.put("timestamp", String.valueOf(curTime));
        map.put("MT-APP-Version", getMTVersion());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("MT-Device-ID", deviceId);
        httpHeaders.add("MT-APP-Version", getMTVersion());
        httpHeaders.add("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<Map> entity = new HttpEntity<>(map, httpHeaders);

//        String res = restTemplate.postForObject("https://app.moutai519.com.cn/xhr/front/user/register/login", entity, String.class);
       String res = "{\"code\":2000,\"data\":{\"birthday\":\"1991-10-22\",\"idType\":0,\"verifyStatus\":1,\"cookie\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcyNjY0MzEyMSwidXNlcklkIjoxMTE2OTc2NzA0LCJkZXZpY2VJZCI6ImU5NjAxOGVlLTM5ZmEtNDY2NS05MTdjLWUyYWJlNjhkNGIzNyIsImlhdCI6MTcyNDA1MTEyMX0.V4taJIRjQQWkRM0-OP-TamNLrH4GaMya2P0frM2NTYA\",\"userTag\":0,\"idCode\":\"341022199110221110\",\"mobile\":\"166****1022\",\"userName\":\"汪*\",\"userId\":1116976704,\"did\":\"did:mt:1:0x35b112ea7a9a11efb53d395d507c69e56fc8fc79122e52c172225b4c2c8183ba\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcyNjY0MzEyMSwidXNlcklkIjoxMTE2OTc2NzA0LCJkZXZpY2VJZCI6ImU5NjAxOGVlLTM5ZmEtNDY2NS05MTdjLWUyYWJlNjhkNGIzNyIsImlhdCI6MTcyNDA1MTEyMX0.MDUdgI4qbawWfob4I3_BXtivVAv_80DG2dyEAiwWjz8\"}}";
        User user = userMapper.selectByMobile(mobile);
        JSONObject body = JSONObject.parseObject(res);

        if (body.getString("code").equals("2000")) {
            System.out.println("「登录请求-成功」" + body.toJSONString());
            if (user != null) {
                deviceId = user.getDeviceId();
            }
            user = new User(mobile, deviceId, body);
            if (user == null) {
                userMapper.insertUser(user);
            } else {
                userMapper.updateUser(user);
            }
            return true;
        } else {
            System.out.println("「登录请求-失败」" + body.toJSONString());
            return false;
        }
    }

    public static String AesEncrypt(String params) {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, AES_KEY.getBytes(), AES_IV.getBytes());
        return aes.encryptBase64(params);
    }
    public JSONObject reservation(User user, String itemId, String shopId) {
        Map<String, Object> map = new HashMap<>();
        JSONArray itemArray = new JSONArray();
        Map<String, Object> info = new HashMap<>();
        info.put("count", 1);
        info.put("itemId", itemId);

        itemArray.add(info);

        map.put("itemInfoList", itemArray);

        map.put("sessionId", itemService.getCurrentSessionId());
        map.put("userId", user.getUserId().toString());
        map.put("shopId", shopId);

        map.put("actParam", AesEncrypt(JSON.toJSONString(map)));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("MT-Lat", user.getLat());
        httpHeaders.add("MT-Lng", user.getLng());
        httpHeaders.add("MT-Token", user.getToken());
        httpHeaders.add("MT-Info", "028e7f96f6369cafe1d105579c5b9377");
        httpHeaders.add("MT-Device-ID", user.getDeviceId());
        httpHeaders.add("MT-APP-Version", getMTVersion());
        httpHeaders.add("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("userId", user.getUserId().toString());
        HttpEntity<Map> entity = new HttpEntity<>(map, httpHeaders);

        String res = restTemplate.postForObject("https://app.moutai519.com.cn/xhr/front/mall/reservation/add", entity, String.class);
        JSONObject body = JSONObject.parseObject(res);
        //{"code":2000,"data":{"successDesc":"申购完成，请于7月6日18:00查看预约申购结果","reservationList":[{"reservationId":17053404357,"sessionId":678,"shopId":"233331084001","reservationTime":1688608601720,"itemId":"10214","count":1}],"reservationDetail":{"desc":"申购成功后将以短信形式通知您，请您在申购成功次日18:00前确认支付方式，并在7天内完成提货。","lotteryTime":1688637600000,"cacheValidTime":1688637600000}}}
        if (body.getInteger("code") != 2000) {
            String message = body.getString("message");
//            throw new ServiceException(message);
        }
//        logger.info(body.toJSONString());
        return body;
    }

    @Override
    public void reservation(User user) {
        if (StringUtils.isEmpty(user.getItemCode())) {
            return;
        }
        String[] items = user.getItemCode().split("@");

        String logContent = "";
        for (String itemId : items) {
            try {
                String shopId = shopService.getShopId(user.getShopType(), itemId,
                        user.getProvinceName(), user.getCityName(), user.getLat(), user.getLng());
                //预约
                JSONObject json = reservation(user, itemId, shopId);
                logContent += String.format("[预约项目]：%s\n[shopId]：%s\n[结果返回]：%s\n\n", itemId, shopId, json.toString());

                //随机延迟3～5秒
                Random random = new Random();
                int sleepTime = random.nextInt(3) + 3;
                Thread.sleep(sleepTime * 1000);
            } catch (Exception e) {
                logContent += String.format("执行报错--[预约项目]：%s\n[结果返回]：%s\n\n", itemId, e.getMessage());
            }
        }

//        try {
//            //预约后领取耐力值
//            String energyAward = getEnergyAward(iUser);
//            logContent += "[申购耐力值]:" + energyAward;
//        } catch (Exception e) {
//            logContent += "执行报错--[申购耐力值]:" + e.getMessage();
//        }
        //日志记录
//        IMTLogFactory.reservation(iUser, logContent);
        //预约后延迟领取耐力值
//        getEnergyAwardDelay(iUser);
    }
}
