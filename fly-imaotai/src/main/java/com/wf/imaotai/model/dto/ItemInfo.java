package com.wf.imaotai.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.wf.imaotai.entity.Shop;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItemInfo implements Serializable {

    private String shopId;

    private int count;

    private String districtName;

    private String cityName;

    private String provinceName;

    private String tenantName;

    private String fullAddress;

    private String itemId;

    private String itemTitle;


    private String lat;

    private String lng;

    // 用作计算的时候用的。后续看看是不是调整到dto
    private Double distance;
    /**
     * 库存
     */
    private int inventory;

    public ItemInfo(String shopId, JSONObject itemInfo, String itemTitle) {
        this.shopId = shopId;
        this.tenantName = ""; // 等待后续补充
        this.fullAddress = ""; // 等待后续补充
        this.districtName = ""; // 等待后续补充
        this.provinceName = ""; // 等待后续补充
        this.cityName = ""; // 等待后续补充
        this.lat = ""; // 等待后续补充
        this.lng = ""; // 等待后续补充
        this.count = itemInfo.getIntValue("count");
        this.itemId = itemInfo.getString("itemId");
        this.itemTitle = itemTitle;
        this.inventory = itemInfo.getIntValue("inventory");
    }

    public ItemInfo(JSONObject shops, JSONObject itemInfo) {
        this.shopId = shops.getString("shopId");
        this.tenantName = shops.getString("ownerName");
        this.count = itemInfo.getIntValue("count");
        this.itemId = itemInfo.getString("itemId");
        this.inventory = itemInfo.getIntValue("inventory");
    }
}
