package com.wf.imaotai.service;

import com.wf.imaotai.domain.ItemInfo;
import com.wf.imaotai.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Shop> getShoplist(Shop shop);

    void refreshShop();

    String getShopId(int shopType, String itemId, String province, String city, String lat, String lng);

    List<ItemInfo> getShopsByProvince(String province, String itemId);

    int reservationBatch();
}
