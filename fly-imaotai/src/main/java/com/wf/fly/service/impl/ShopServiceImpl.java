package com.wf.fly.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wf.fly.entity.Shop;
import com.wf.fly.entity.User;
import com.wf.fly.mapper.ShopMapper;
import com.wf.fly.mapper.UserMapper;
import com.wf.fly.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    public RestTemplate restTemplate;
    @Override
    public List<Shop> list(Shop shop) {
        List<Shop> x = shopMapper.list(shop);
        return x;
    }

    //    @Async
    @Override
    public void refreshShop() {
        String url = "https://static.moutai519.com.cn/mt-backend/xhr/front/mall/resource/get";
        String res = new RestTemplate().getForObject(url, String.class);
        JSONObject body = JSONObject.parseObject(res);
        String shopUrl = body.getJSONObject("data").getJSONObject("mtshops_pc").getString("url");;

        // 清空
        shopMapper.clearAll();

        // 获取
        String shopRes = new RestTemplate().getForObject(shopUrl, String.class);
        JSONObject jsonObject = JSONObject.parseObject(shopRes);
        Set<String> shopIdSet = jsonObject.keySet();
        List<Shop> list = new ArrayList<>();

        for (String shopId : shopIdSet) {
            JSONObject shopObj = jsonObject.getJSONObject(shopId);
            Shop shop = new Shop(shopId, shopObj);
            shop.setShopId(shopId);
            list.add(shop);
            shopMapper.addShop(shop);
        }
    }
}
