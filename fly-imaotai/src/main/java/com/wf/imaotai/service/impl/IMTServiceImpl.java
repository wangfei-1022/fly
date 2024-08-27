package com.wf.imaotai.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wf.common.core.RedisCache;
import com.wf.common.exception.ServiceException;
import com.wf.imaotai.constant.IMTCache;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.UserMapper;
import com.wf.imaotai.service.*;
import com.wf.imaotai.util.AESUtil;
import com.wf.imaotai.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class IMTServiceImpl implements IMTService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public ShopService shopService;

    @Autowired
    public ItemService itemService;

    @Override
    public String getMTVersion() {
        String mtVersion = Convert.toStr(redisCache.getCacheObject(IMTCache.MT_VERSION));
        if (StringUtils.isNotEmpty(mtVersion)) {
            return mtVersion;
        }
        String url = "https://apps.apple.com/cn/app/i%E8%8C%85%E5%8F%B0/id1600482450";
        String htmlContent = cn.hutool.http.HttpUtil.get(url);
        Pattern pattern = Pattern.compile("new__latest__version\">(.*?)</p>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(htmlContent);
        if (matcher.find()) {
            mtVersion = matcher.group(1);
            mtVersion = mtVersion.replace("版本 ", "");
        }
        redisCache.setCacheObject(IMTCache.MT_VERSION, mtVersion);
        return mtVersion;
    }

    @Override
    public boolean sendCode(Long mobile, String deviceId) {
        Map<String, Object> data = new HashMap<>();
        data.put("mobile", mobile);
        final long curTime = System.currentTimeMillis();
        data.put("md5", AESUtil.signature(mobile.toString(), curTime));
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
    public boolean login(Long mobile, String code, String deviceId) {
        Map<String, String> data = new HashMap<>();
        data.put("mobile", mobile.toString());
        data.put("vCode", code);

        final long curTime = System.currentTimeMillis();
        data.put("md5", AESUtil.signature(mobile + code + "" + "", curTime));

        data.put("timestamp", String.valueOf(curTime));
        data.put("MT-APP-Version", getMTVersion());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("MT-Device-ID", deviceId);
        httpHeaders.add("MT-APP-Version", getMTVersion());
        httpHeaders.add("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<Map> entity = new HttpEntity<>(data, httpHeaders);

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


    public JSONObject reservation(User user, String itemId, String shopId) {
        Map<String, Object> data = new HashMap<>();
        JSONArray itemArray = new JSONArray();
        Map<String, Object> info = new HashMap<>();
        info.put("count", 1);
        info.put("itemId", itemId);

        itemArray.add(info);

        data.put("itemInfoList", itemArray);

        data.put("sessionId", itemService.getCurrentSessionId());
        data.put("userId", user.getUserId().toString());
        data.put("shopId", shopId);

        data.put("actParam", AESUtil.AesEncrypt(JSON.toJSONString(data)));


        Headers.Builder httpHeaders = new Headers.Builder();


//        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("MT-Lat", user.getLat());
        httpHeaders.add("MT-Lng", user.getLng());
        httpHeaders.add("MT-Token", user.getToken());
        httpHeaders.add("MT-Info", "028e7f96f6369cafe1d105579c5b9377");
        httpHeaders.add("MT-Device-ID", user.getDeviceId());
        httpHeaders.add("MT-APP-Version", getMTVersion());
        httpHeaders.add("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("userId", user.getUserId().toString());
//        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(data, httpHeaders);
        String res = HttpUtil.post("https://app.moutai519.com.cn/xhr/front/mall/reservation/add", data, httpHeaders.build());

//        String res = restTemplate.postForObject("https://app.moutai519.com.cn/xhr/front/mall/reservation/add", entity, String.class);
        JSONObject body = JSONObject.parseObject(res);
        //{"code":2000,"data":{"successDesc":"申购完成，请于7月6日18:00查看预约申购结果","reservationList":[{"reservationId":17053404357,"sessionId":678,"shopId":"233331084001","reservationTime":1688608601720,"itemId":"10214","count":1}],"reservationDetail":{"desc":"申购成功后将以短信形式通知您，请您在申购成功次日18:00前确认支付方式，并在7天内完成提货。","lotteryTime":1688637600000,"cacheValidTime":1688637600000}}}
        if (body.getInteger("code") != 2000) {
            String message = body.getString("message");
            throw new ServiceException(message);
        }
        log.info(body.toJSONString());
        return body;
    }

    @Override
    public void reservation(Long mobile) {
        User user = userMapper.selectByMobile(mobile);
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
                log.info(json.toJSONString());
                logContent += String.format("[预约项目]：%s\n[shopId]：%s\n[结果返回]：%s\n\n", itemId, shopId, json.toString());

                //随机延迟3～5秒
                Random random = new Random();
                int sleepTime = random.nextInt(3) + 3;
                Thread.sleep(sleepTime * 1000);
            } catch (Exception e) {
                logContent += String.format("执行报错--[预约项目]：%s\n[结果返回]：%s\n\n", itemId, e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }

        logService.record(user, logContent);
        // 预约后延迟领取耐力值
        getEnergyAwardDelay(user);
    }

    public void getEnergyAwardDelay(User iUser) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String logContent = "";
                //sleep 10秒
                try {
                    Thread.sleep(10000);
                    //预约后领取耐力值
                    String energyAward = getEnergyAward(iUser);
                    logContent += "[申购耐力值]:" + energyAward;
                } catch (Exception e) {
                    logContent += "执行报错--[申购耐力值]:" + e.getMessage();
                }
                logService.record(iUser, logContent);
            }
        };
        new Thread(runnable).start();

    }

    //获取申购耐力值
    @Override
    public String getEnergyAward(User iUser) {
        String url = "https://h5.moutai519.com.cn/game/isolationPage/getUserEnergyAward";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.POST, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .header("MT-Lat", iUser.getLat())
                .header("MT-Lng", iUser.getLng())
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");

        String body = request.execute().body();

        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(body);
        if (jsonObject.getInteger("code") != 200) {
            String message = jsonObject.getString("message");
            throw new ServiceException(message);
        }

        return body;
    }

    @Override
    public void travelReward(Long mobile) {
        User user = userMapper.selectByMobile(mobile);
        String logContent = "";
        try {
            String s = travelRewardUser(user);
            logContent += "[获得旅行奖励]:" + s;
        } catch (Exception e) {
            logContent += "执行报错--[获得旅行奖励]:" + e.getMessage();
        }
        //日志记录
        logService.record(user, logContent);
    }

    // 领取小茅运
    public void receiveReward(User iUser) {
        String url = "https://h5.moutai519.com.cn/game/xmTravel/receiveReward";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.POST, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .header("MT-Lat", iUser.getLat())
                .header("MT-Lng", iUser.getLng())
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");

        HttpResponse execute = request.execute();
        com.alibaba.fastjson2.JSONObject body = com.alibaba.fastjson2.JSONObject.parseObject(execute.body());

        if (body.getInteger("code") != 2000) {
            String message = "领取小茅运失败";
            throw new ServiceException(message);
        }
    }

    public void shareReward(User iUser) {
        log.info("「领取每日首次分享获取耐力」：" + iUser.getMobile());
        String url = "https://h5.moutai519.com.cn/game/xmTravel/shareReward";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.POST, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .header("MT-Lat", iUser.getLat())
                .header("MT-Lng", iUser.getLng())
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");

        HttpResponse execute = request.execute();
        com.alibaba.fastjson2.JSONObject body = com.alibaba.fastjson2.JSONObject.parseObject(execute.body());

        if (body.getInteger("code") != 2000) {
            String message = "领取每日首次分享获取耐力失败";
            throw new ServiceException(message);
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
        Map<String, Integer> pageData = getUserIsolationPageData(iUser);
        Integer status = pageData.get("status");
        if (status == 3) {
            Integer currentPeriodCanConvertXmyNum = pageData.get("currentPeriodCanConvertXmyNum");
            Double travelRewardXmy = getXmTravelReward(iUser);
            // 获取小茅运
            receiveReward(iUser);
            //首次分享获取耐力
            shareReward(iUser);
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
            return startTravel(iUser);
        }
    }

    //小茅运旅行活动
    public String startTravel(User iUser) {
        String url = "https://h5.moutai519.com.cn/game/xmTravel/startTravel";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.POST, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");
        String body = request.execute().body();
        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(body);
        if (jsonObject.getInteger("code") != 2000) {
            String message = "开始旅行失败：" + jsonObject.getString("message");
            throw new ServiceException(message);
        }
        com.alibaba.fastjson2.JSONObject data = jsonObject.getJSONObject("data");
        //最后返回 {"startTravelTs":1690798861076}
        return jsonObject.toString();
    }


    //查询 可获取小茅运
    public Double getXmTravelReward(User iUser) {
        //查询旅行奖励:
        String url = "https://h5.moutai519.com.cn/game/xmTravel/getXmTravelReward";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.GET, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");
        String body = request.execute().body();
        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(body);
        if (jsonObject.getInteger("code") != 2000) {
            String message = jsonObject.getString("message");
            throw new ServiceException(message);
        }
        com.alibaba.fastjson2.JSONObject data = jsonObject.getJSONObject("data");
        Double travelRewardXmy = data.getDouble("travelRewardXmy");
        //例如 1.95
        return travelRewardXmy;

    }

    //获取用户页面数据
    public Map<String, Integer> getUserIsolationPageData(User iUser) {
        //查询小茅运信息
        String url = "https://h5.moutai519.com.cn/game/isolationPage/getUserIsolationPageData";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.GET, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");
        String body = request.form("__timestamp", DateUtil.currentSeconds()).execute().body();

        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(body);
        if (jsonObject.getInteger("code") != 2000) {
            String message = jsonObject.getString("message");
            throw new ServiceException(message);
        }
        com.alibaba.fastjson2.JSONObject data = jsonObject.getJSONObject("data");
        //xmy: 小茅运值
        String xmy = data.getString("xmy");
        //energy: 耐力值
        int energy = data.getIntValue("energy");
        com.alibaba.fastjson2.JSONObject xmTravel = data.getJSONObject("xmTravel");
        com.alibaba.fastjson2.JSONObject energyReward = data.getJSONObject("energyReward");
        //status: 1. 未开始 2. 进行中 3. 已完成
        Integer status = xmTravel.getInteger("status");
        // travelEndTime: 旅行结束时间
        Long travelEndTime = xmTravel.getLong("travelEndTime");
        //remainChance 今日剩余旅行次数
        int remainChance = xmTravel.getIntValue("remainChance");

        //可领取申购耐力值奖励
        Integer energyValue = energyReward.getInteger("value");

        if (energyValue > 0) {
            //获取申购耐力值
            getEnergyAward(iUser);
            energy += energyValue;
        }

        // 本月剩余旅行奖励
        int exchangeRateInfo = getExchangeRateInfo(iUser);
        if (exchangeRateInfo <= 0) {
            String message = "当月无可领取奖励";
            throw new ServiceException(message);
        }

        Long endTime = travelEndTime * 1000;
        // 未开始
        if (status == 1) {
            if (energy < 100) {
                String message = String.format("耐力不足100, 当前耐力值:%s", energy);
                throw new ServiceException(message);
            }
        }
        // 进行中
        if (status == 2) {
            Date date = new Date(endTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);
            String message = String.format("旅行暂未结束,本次旅行结束时间:%s ", formattedDate);
            throw new ServiceException(message);
        }
        Map<String, Integer> map = new HashMap<>();

        map.put("remainChance", remainChance);
        map.put("status", status);
        map.put("currentPeriodCanConvertXmyNum", getExchangeRateInfo(iUser));
        return map;
    }

    // 获取本月剩余奖励耐力值
    public int getExchangeRateInfo(User iUser) {
        String url = "https://h5.moutai519.com.cn/game/synthesize/exchangeRateInfo";
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(Method.GET, url);

        request.header("MT-Device-ID", iUser.getDeviceId())
                .header("MT-APP-Version", getMTVersion())
                .header("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .cookie("MT-Token-Wap=" + iUser.getCookie() + ";MT-Device-ID-Wap=" + iUser.getDeviceId() + ";");

        String body = request.form("__timestamp", DateUtil.currentSeconds()).execute().body();
        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(body);
        if (jsonObject.getInteger("code") != 2000) {
            String message = jsonObject.getString("message");
            throw new ServiceException(message);
        }
        //获取本月剩余奖励耐力值
        return jsonObject.getJSONObject("data").getIntValue("currentPeriodCanConvertXmyNum");
    }

    @Async
    @Override
    public void reservationBatch() {
        List<User> iUsers = userService.selectReservationUser();
        for (User iUser : iUsers) {
            log.info("「开始预约用户」" + iUser.getMobile());
            //预约
            reservation(iUser.getMobile());
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
            List<User> iUsers = userService.selectReservationUser();

            for (User user : iUsers) {
                log.info("「开始获得旅行奖励」" + user.getMobile());
                travelRewardUser(user);
                //延时3秒
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refreshMTVersion() {
        redisCache.deleteObject(IMTCache.MT_VERSION);
        getMTVersion();
    }

    /*
    * 用作定时任务的刷新需要的参数
    * */
    @Override
    public void refreshAll() {
        refreshMTVersion();
        shopService.refreshShop();
        itemService.refreshItem();
    }

    /*
    * 用作定时任务-18点之后的结果查询
    * */
    @Override
    public void appointmentResults() {
        log.info("申购结果查询开始=========================");
        List<User> iUsers = userService.selectReservationUser();
        for (User user : iUsers) {
            try {
                String url = "https://app.moutai519.com.cn/xhr/front/mall/reservation/list/pageOne/query";
                String body = cn.hutool.http.HttpUtil.createRequest(Method.GET, url)
                        .header("MT-Device-ID", user.getDeviceId())
                        .header("MT-APP-Version", getMTVersion())
                        .header("MT-Token", user.getToken())
                        .header("User-Agent", "iOS;16.3;Apple;?unrecognized?").execute().body();
                com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(body);
                log.info("查询申购结果回调: user->{},response->{}", user.getMobile(), body);
                if (jsonObject.getInteger("code") != 2000) {
                    String message = jsonObject.getString("message");
                    throw new ServiceException(message);
                }
                com.alibaba.fastjson2.JSONArray itemVOs = jsonObject.getJSONObject("data").getJSONArray("reservationItemVOS");
                if (Objects.isNull(itemVOs) || itemVOs.isEmpty()) {
                    log.info("申购记录为空: user->{}", user.getMobile());
                    continue;
                }
                for (Object itemVO : itemVOs) {
                    com.alibaba.fastjson2.JSONObject item = com.alibaba.fastjson2.JSON.parseObject(itemVO.toString());
                    // 预约时间在24小时内的
                    if (item.getInteger("status") == 2 && DateUtil.between(item.getDate("reservationTime"), new Date(), DateUnit.HOUR) < 24) {
                        String logContent = DateUtil.formatDate(item.getDate("reservationTime")) + " 申购" + item.getString("itemName") + "成功";
                        logService.record(user, logContent);
                    }
                }
            } catch (Exception e) {
                log.error("查询申购结果失败:失败原因->{}", e.getMessage(), e);
            }
        }
        log.info("申购结果查询结束=========================");
    }
}
