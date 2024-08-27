package com.wf.imaotai.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("'shop'")
public class Shop implements Serializable {

    private int id;

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

    private Date openStartTime;

    private Date openEndTime;

    private String tenantName;

    private String name;

    private String layaway;

    private String tags;

    private Date createTime;

    private Double distance;

    public Shop(String shopId, JSONObject jsonObject) {
        if(jsonObject != null) {
            this.shopId = shopId;
            this.provinceName = jsonObject.getString("provinceName");
            this.cityName = jsonObject.getString("cityName");
            this.districtName = jsonObject.getString("districtName");
            this.fullAddress = jsonObject.getString("fullAddress");
            this.lat = jsonObject.getString("lat");
            this.lng = jsonObject.getString("lng");
            this.name = jsonObject.getString("name");
            this.tenantName = jsonObject.getString("tenantName");
            this.createTime = new Date();
        }
    }
}
