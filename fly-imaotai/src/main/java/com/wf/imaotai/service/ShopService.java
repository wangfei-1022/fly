package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.dto.ItemInfo;
import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.model.request.ShopRequest;

import java.util.List;

public interface ShopService {

    List<Shop> getShopListNoPage(ShopRequest shopRequest);
    List<Shop> getShoplist(ShopRequest shopRequest);

    List<Shop> provinceList(ShopRequest shopRequest);

    List<Shop> cityList(ShopRequest shopRequest);

    List<Shop> districtList(ShopRequest shopRequest);

    void refreshShop();

    String getShopId(User user, String itemId);

    List<ItemInfo> getShopsByProvince(String province, String itemId);

    List<ItemInfo> deliverySearch(ShopRequest shopRequest);
}
