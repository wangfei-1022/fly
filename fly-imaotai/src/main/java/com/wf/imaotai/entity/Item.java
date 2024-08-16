package com.wf.imaotai.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class Item {
    public String id;

    public Item(JSONObject jsonObject) {
        if(jsonObject != null) {
            this.id = jsonObject.getString("id");
        }
    }
}
