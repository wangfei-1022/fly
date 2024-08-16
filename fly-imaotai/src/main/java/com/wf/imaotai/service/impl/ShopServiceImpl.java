package com.wf.imaotai.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.mapper.ItemMapper;
import com.wf.imaotai.mapper.ShopMapper;
import com.wf.imaotai.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
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
        PageHelper.startPage(1, 10);
        List<Shop> xx1 = shopMapper.getList(shop);
        return xx1;
    }

    //    @Async
    @Override
    public void refreshShop() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json;charset=utf-8");
        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);
        String url = "https://static.moutai519.com.cn/mt-backend/xhr/front/mall/resource/get";
        String res = new RestTemplate().getForObject(url,  String.class);
        JSONObject body = JSONObject.parseObject(res);
        String shopUrl = body.getJSONObject("data").getJSONObject("mtshops_pc").getString("url");;

        // 清空
        shopMapper.clearAll();

        // 获取
        String shopRes = restTemplate.getForObject(shopUrl, String.class);
        System.out.println("shopRes = " + shopRes);
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
