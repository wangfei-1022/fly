package com.wf.imaotai.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Shop {

    private String shopId;

    private String address;

    private String fullAddress;

    private String lat;

    private String lng;

    private int district;

    private String districtName;

    private int city;

    private String cityName;

    private int province;

    private String provinceName;

    private String tenantName;

    private String name;

    private String tags;

    private Date createTime;



    public Shop() {

    }
    public Shop(JSONObject jsonObject) {
        if(jsonObject != null) {
            this.shopId = jsonObject.getString("shopId");
            this.address = jsonObject.getString("address");
            this.province = jsonObject.getInteger("province");
            this.provinceName = jsonObject.getString("provinceName");
            this.city = jsonObject.getInteger("city");
            this.cityName = jsonObject.getString("cityName");
            this.district = jsonObject.getInteger("district");
            this.districtName = jsonObject.getString("districtName");
            this.fullAddress = jsonObject.getString("fullAddress");
            this.lat = jsonObject.getString("lat");
            this.lng = jsonObject.getString("lng");
            this.name = jsonObject.getString("name");
            this.tags = jsonObject.getString("tags");
            this.tenantName = jsonObject.getString("tenantName");
            this.createTime = new Date();
        }
    }
}
