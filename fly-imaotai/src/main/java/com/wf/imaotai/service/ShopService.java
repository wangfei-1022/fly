package com.wf.imaotai.service;

import com.wf.imaotai.model.dto.ItemInfo;
import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.model.request.ShopRequest;

import java.util.List;

public interface ShopService {
    List<Shop> getShoplist(ShopRequest shopRequest);

    void refreshShop();

    String getShopId(int shopType, String itemId, String province, String city, String lat, String lng);

    List<ItemInfo> getShopsByProvince(String province, String itemId);
}
