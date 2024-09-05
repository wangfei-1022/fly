package com.wf.imaotai.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wf.common.core.RedisCache;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.dto.ItemInfo;
import com.wf.imaotai.model.dto.MapPoint;
import com.wf.imaotai.entity.Shop;
import com.wf.common.exception.ServiceException;
import com.wf.imaotai.mapper.ShopMapper;
import com.wf.imaotai.model.request.ShopRequest;
import com.wf.imaotai.service.ItemService;
import com.wf.imaotai.service.ShopService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public List<Shop> getShopListNoPage(ShopRequest shopRequest) {
        List<Shop> shopList = shopMapper.getList(shopRequest);
        return shopList;
    }

    @Override
    public List<Shop> getShoplist(ShopRequest shopRequest) {
        PageHelper.startPage(shopRequest.initPage());
        List<Shop> shopList = shopMapper.getList(shopRequest);
        return shopList;
    }

    @Override
    public List<Shop> provinceList(ShopRequest shopRequest) {
        List<Shop> xx1 = shopMapper.provinceList(shopRequest);
        return xx1;
    }

    @Override
    public List<Shop> cityList(ShopRequest shopRequest) {
        List<Shop> cityList = shopMapper.cityList(shopRequest);
        return cityList;
    }

    @Override
    public List<Shop> districtList(ShopRequest shopRequest) {
        List<Shop> districtList = shopMapper.districtList(shopRequest);
        return districtList;
    }

    /*
    * 根据省市获取到门店地址
    */
    public List<ItemInfo> reGetShopsByProvince(String province, String itemId) {

        long dayTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        String url = "https://static.moutai519.com.cn/mt-backend/xhr/front/mall/shop/list/slim/v3/" + itemService.getCurrentSessionId() + "/" + province + "/" + itemId + "/" + dayTime;

        String urlRes = HttpUtil.get(url);
        JSONObject res = null;
        try {
            res = JSONObject.parseObject(urlRes);
        } catch (JSONException jsonException) {
            String message = "查询所在省市的投放产品和数量error: " + url;
            throw new ServiceException(message);
        }

        if (!res.containsKey("code") || !res.getString("code").equals("2000")) {
            String message = "查询所在省市的投放产品和数量error: " + url;
            throw new ServiceException(message);
        }
        //组合信息
        List<ItemInfo> imtItemInfoList = new ArrayList<>();

        JSONObject data = res.getJSONObject("data");
        JSONArray shopList = data.getJSONArray("shops");

        JSONArray itemsList = data.getJSONArray("items");
        Map<String, JSONObject> itemMap = new HashMap<>();
        // 把对应的预约项目构建出来
        for (Object item : itemsList) {
            JSONObject itemObj = (JSONObject) item;
            itemMap.put(itemObj.getString("itemId"), itemObj);
        }

        for (Object obj : shopList) {
            JSONObject shops = (JSONObject) obj;
            JSONArray items = shops.getJSONArray("items");
            for (Object item : items) {
                JSONObject itemObj = (JSONObject) item;
                if (itemObj.getString("itemId").equals(itemId)) {
                    JSONObject xx = itemMap.get(itemId);
                    ItemInfo iItem = new ItemInfo(shops.getString("shopId"), itemObj, xx.getString("title"));
                    //添加
                    imtItemInfoList.add(iItem);
                }
            }
        }
        return imtItemInfoList;
    }


    @Override
    public List<ItemInfo> getShopsByProvince(String province, String itemId) {
        String key = "mt_province:" + province + "." + itemService.getCurrentSessionId() + "." + itemId;
        List<ItemInfo> cacheList = redisCache.getCacheList(key);
        if (cacheList != null && cacheList.size() > 0) {
            return cacheList;
        } else {
            List<ItemInfo> imtItemInfoList = reGetShopsByProvince(province, itemId);
            ShopRequest shopRequest = new ShopRequest();
            shopRequest.setProvinceName(province);
            //获取门店列表
            List<Shop> iShops = getShopListNoPage(shopRequest);
            //获取今日的门店信息列表
            imtItemInfoList.forEach(v -> {
                 Shop shop = iShops.stream().filter(i -> i.getShopId() == v.getShopId()).findFirst().get();
                 v.setProvinceName(shop.getProvinceName());
                 v.setCityName(shop.getCityName());
                 v.setDistrictName(shop.getDistrictName());
                 v.setTenantName(shop.getTenantName());
                 v.setFullAddress(shop.getFullAddress());
                 v.setLat(shop.getLat());
                 v.setLng(shop.getLng());
            });

            redisCache.reSetCacheList(key, imtItemInfoList);
            redisCache.expire(key, 60, TimeUnit.MINUTES);
            return imtItemInfoList;
        }
    }

    @Override
    public List<ItemInfo> deliverySearch(ShopRequest shopRequest) {
        String itemId = "10941";
        String province = shopRequest.getProvinceName();
        //查询所在省市的投放产品和数量
        List<ItemInfo> imtItemInfoList = getShopsByProvince(province, itemId);
        return imtItemInfoList;
    }

    public String getMaxInventoryShopId(List<ItemInfo> itemInfoList, String city) {
        List<ItemInfo> collect = itemInfoList.stream().filter(i -> i.getCityName() == city).sorted(Comparator.comparing(ItemInfo::getInventory).reversed()).collect(Collectors.toList());

        if (collect != null && collect.size() > 0) {
            return collect.get(0).getShopId();
        }
        return null;
    }


    @Override
    public String getShopId(User user, String itemId) {
        int appointmentType = user.getAppointmentType();
        String province = user.getProvinceName();
        String city = user.getCityName();

        //查询所在省市的投放产品和数量
        List<ItemInfo> itemInfoList = getShopsByProvince(province, itemId);

        String shopId = "";
        if (appointmentType == 1) {
            //预约本市出货量最大的门店
            shopId = getMaxInventoryShopId(itemInfoList, city);
        } else {
            shopId = user.getShopId();
        }

        if (StringUtils.isEmpty(shopId)) {
            throw new ServiceException("申购时根据类型获取的门店商品id为空");
        }
        return shopId;
    }

    // 获取的是门店地址
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
            Shop shop = new Shop(shopObj);
            list.add(shop);
        }
        shopMapper.insertShopBatch(list);
    }
}
