package com.wf.imaotai.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Item {

    private Long id;

    private String itemId;

    private String itemCode;

    private String title;

    private String content;

    private String picture;

    private Date createTime;

    public Item(){

    };
    public Item(JSONObject item) {
        if(item != null) {
            this.itemCode = item.getString("itemCode");
            this.title =  item.getString("title");;
            this.content =  item.getString("content");;
            this.picture =  item.getString("picture");;
            this.createTime = new Date();
        }
    }
}
