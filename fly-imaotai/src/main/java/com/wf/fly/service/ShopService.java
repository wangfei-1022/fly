package com.wf.fly.service;

import com.wf.fly.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Shop> list(Shop shop);

    void refreshShop();
}
