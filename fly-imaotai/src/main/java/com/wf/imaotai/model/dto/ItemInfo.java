package com.wf.imaotai.model.dto;

import lombok.Data;

@Data
public class ItemInfo {

    private String shopId;

    private int count;

    private String itemId;

    /**
     * 库存
     */
    private int inventory;

    public ItemInfo(String shopId, int count, String itemId, int inventory) {
        this.shopId = shopId;
        this.count = count;
        this.itemId = itemId;
        this.inventory = inventory;
    }
}
