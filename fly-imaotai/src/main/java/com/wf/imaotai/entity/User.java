package com.wf.imaotai.entity;

import com.alibaba.fastjson.JSONObject;
import com.wf.imaotai.util.AESUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
public class User {

    private Long id;

    private String mobile;

    private String name;

    private String token;

    private String cookie;

    private String deviceId;

    private String shopId;

    private String itemCode;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String lat;

    private String lng;

    private int minute;

    private int appointmentType;

    private int appointmentTimeType;

    private Date expireTime;

    private Date delFlag;

    private Date updateTime;

    private Date createTime;

    private String jsonResult;


    public User() {

    }
    public User(String mobile, JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("data");
        this.id = data.getLong("userId");
        this.name = data.getString("userName");
        this.mobile = mobile;
        this.token = data.getString("token");
        this.cookie = data.getString("cookie");
        this.jsonResult = StringUtils.substring(jsonObject.toJSONString(), 0, 2000);

        String info = AESUtil.decodeBase64String(this.token);
        JSONObject jObject = JSONObject.parseObject(info);
        long exp = jObject.getLong("exp");
        Date date = new Date(exp*1000);
        this.expireTime = date;
    }

    public User(String mobile, String deviceId, JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("data");
        this.id = data.getLong("userId");
        this.name = data.getString("userName");
        this.mobile = mobile;
        this.token = data.getString("token");
        this.cookie = data.getString("cookie");
        this.deviceId = deviceId.toLowerCase();
        this.jsonResult = StringUtils.substring(jsonObject.toJSONString(), 0, 2000);


//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 30);
//        Date thirtyDaysLater = calendar.getTime();

        String info = AESUtil.decodeBase64String(this.token);
        JSONObject jObject = JSONObject.parseObject(info);
        long exp = jObject.getLong("exp");
        Date date = new Date(exp*1000);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = sdf.format(date);
//        System.out.println(dateString);
        this.expireTime = date;
    }

}
