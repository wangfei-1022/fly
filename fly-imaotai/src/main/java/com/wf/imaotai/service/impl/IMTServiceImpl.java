package com.wf.imaotai.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.wf.common.exception.ServiceException;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.service.*;
import com.wf.imaotai.util.IMTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IMTServiceImpl implements IMTService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    public ShopService shopService;

    @Autowired
    public ItemService itemService;

    @Autowired
    public IMTUtil imtUtil;

    @Override
    public boolean sendCode(String mobile, String deviceId) {
        String res = imtUtil.sendCode(mobile, deviceId);
        JSONObject jsonObject = JSONObject.parseObject(res);

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
        String  res = imtUtil.login(mobile, code, deviceId);
        User user = userMapper.selectByMobile(mobile);
        JSONObject body = JSONObject.parseObject(res);

        if (body.getString("code").equals("2000")) {
            System.out.println("「登录请求-成功」" + body.toJSONString());
            if (user != null) {
                deviceId = user.getDeviceId();
            }
            User user1 = new User(mobile, deviceId, body);
            if (user == null) {
                userMapper.insertUser(user1);
            } else {
                userMapper.updateUser(user1);
            }
            return true;
        } else {
            System.out.println("「登录请求-失败」" + body.toJSONString());
            return false;
        }
    }

    @Override
    public void reservation(User user) {
        if (StringUtils.isEmpty(user.getItemCode())) {
            return;
        }
        String[] items = user.getItemCode().split("@");

        for (String itemId : items) {
            String logContent = "";
            try {
                String shopId = shopService.getShopId(user, itemId);
                //预约
                JSONObject json = imtUtil.reservation(user, itemId, shopId, itemService.getCurrentSessionId());
                log.info(json.toJSONString());
                logContent += String.format("[预约项目]：%s\n[shopId]：%s\n[结果返回]：%s\n\n", itemId, shopId, json.toString());

                //随机延迟3～5秒
                Random random = new Random();
                int sleepTime = random.nextInt(3) + 3;
                Thread.sleep(sleepTime * 1000);
                // 预约后延迟领取耐力值
                getEnergyAwardDelay(user);
            } catch (Exception e) {
                logContent += String.format("执行报错--[预约项目]：%s\n[结果返回]：%s\n\n", itemId, e.getMessage());
            }
            logService.record(user, logContent);
        }
    }

    public void getEnergyAwardDelay(User user) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String logContent = "";
                //sleep 10秒
                try {
                    Thread.sleep(10000);
                    //预约后领取耐力值
                    String energyAward = imtUtil.getEnergyAward(user);
                    logContent += "[申购耐力值]:" + energyAward;
                } catch (Exception e) {
                    logContent += "执行报错--[申购耐力值]:" + e.getMessage();
                }
                logService.record(user, logContent);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void travelReward(String mobile) {
        User user = userMapper.selectByMobile(mobile);
        String logContent = "";
        try {
            String s = travelRewardUser(user);
            logContent += "[获得旅行奖励]:" + s;
            logService.record(user, logContent);
        } catch (Exception e) {
            logContent += "执行报错--[获得旅行奖励]:" + e.getMessage();
            logService.record(user, logContent);
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * 获得旅行奖励
     *
     * @param iUser
     * @return
     */
    public String travelRewardUser(User iUser) {
        //9-20点才能领取旅行奖励
        int hour = DateUtil.hour(new Date(), true);
        if (!(9 <= hour && hour < 20)) {
            String message = "活动未开始，开始时间9点-20点";
            throw new ServiceException(message);
        }
        Map<String, Integer> pageData = imtUtil.getUserIsolationPageData(iUser);
        Integer status = pageData.get("status");
        if (status == 3) {
            Integer currentPeriodCanConvertXmyNum = pageData.get("currentPeriodCanConvertXmyNum");
            Double travelRewardXmy = imtUtil.getXmTravelReward(iUser);
            // 获取小茅运
            imtUtil.receiveReward(iUser);
            //首次分享获取耐力
            imtUtil.shareReward(iUser);
            //本次旅行奖励领取后, 当月实际剩余旅行奖励
            if (travelRewardXmy > currentPeriodCanConvertXmyNum) {
                String message = "当月无可领取奖励";
                throw new ServiceException(message);
            }
        }

        Integer remainChance = pageData.get("remainChance");
        if (remainChance < 1) {
            String message = "当日旅行次数已耗尽";
            throw new ServiceException(message);
        } else {
            //小茅运旅行活动
            return imtUtil.startTravel(iUser);
        }
    }

    @Async
    @Override
    public void reservationBatch() {
        int minute = DateUtil.minute(new Date());
        List<User> users = userService.selectReservationUserByMinute(minute);
        for (User user : users) {
            log.info("「开始预约用户」" + user.getMobile());
            //预约
            reservation(user);
            //延时3秒
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    * 用作定时任务的批量获取旅行奖励
    * */
    @Async
    @Override
    public void travelRewardBatch() {
        try {
            List<User> users = userService.selectReservationUser();

            for (User user : users) {
                log.info("「开始获得旅行奖励」" + user.getMobile());
                travelRewardUser(user);
                //延时3秒
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * 用作定时任务的刷新需要的参数
    * */
    @Override
    public void refreshAll() {
        imtUtil.refreshMTVersion();
        shopService.refreshShop();
        itemService.refreshItem();
    }

    /*
    * 用作定时任务-18点之后的结果查询
    * */
    @Override
    public void appointmentResults() {
        log.info("申购结果查询开始=========================");
        List<User> users = userService.selectReservationUser();
        for (User user : users) {
            try {
                imtUtil.appointmentResults(user);
            } catch (Exception e) {
                log.error("查询申购结果失败:失败原因->{}", e.getMessage(), e);
            }
        }
        log.info("申购结果查询结束=========================");
    }
}
