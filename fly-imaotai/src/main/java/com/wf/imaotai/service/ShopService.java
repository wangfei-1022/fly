package com.wf.imaotai.service;

import com.wf.imaotai.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Shop> list(Shop shop);

    void refreshShop();
}
