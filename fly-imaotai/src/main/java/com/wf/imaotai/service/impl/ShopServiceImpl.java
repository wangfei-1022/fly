package com.wf.imaotai.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wf.imaotai.domain.ItemInfo;
import com.wf.imaotai.domain.MapPoint;
import com.wf.imaotai.entity.Shop;
import com.wf.common.exception.ServiceException;
import com.wf.imaotai.mapper.ShopMapper;
import com.wf.imaotai.service.ItemService;
import com.wf.imaotai.service.LogService;
import com.wf.imaotai.service.ShopService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private LogService logService;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public List<Shop> getShoplist() {
        PageHelper.startPage(1, 10);
        List<Shop> xx1 = shopMapper.getList();
        return xx1;
    }

    public List<ItemInfo> reGetShopsByProvince(String province, String itemId) {

        long dayTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        String url = "https://static.moutai519.com.cn/mt-backend/xhr/front/mall/shop/list/slim/v3/" + itemService.getCurrentSessionId() + "/" + province + "/" + itemId + "/" + dayTime;

        String urlRes = HttpUtil.get(url);
        JSONObject res = null;
        try {
            res = JSONObject.parseObject(urlRes);
        } catch (JSONException jsonException) {
            String message = "查询所在省市的投放产品和数量error: " + url;
            logService.logError(message);
            throw new ServiceException(message);
        }

//        JSONObject res = JSONObject.parseObject(HttpUtil.get(url));
        if (!res.containsKey("code") || !res.getString("code").equals("2000")) {
            String message = "查询所在省市的投放产品和数量error: " + url;
            logService.logError(message);
            throw new ServiceException(message);
        }
        //组合信息
        List<ItemInfo> imtItemInfoList = new ArrayList<>();

        JSONObject data = res.getJSONObject("data");
        JSONArray shopList = data.getJSONArray("shops");

        for (Object obj : shopList) {
            JSONObject shops = (JSONObject) obj;
            JSONArray items = shops.getJSONArray("items");
            for (Object item : items) {
                JSONObject itemObj = (JSONObject) item;
                if (itemObj.getString("itemId").equals(itemId)) {
                    ItemInfo iItem = new ItemInfo(shops.getString("shopId"),
                            itemObj.getIntValue("count"), itemObj.getString("itemId"), itemObj.getIntValue("inventory"));
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
//        List<ItemInfo> cacheList = redisCache.getCacheList(key);
//        if (cacheList != null && cacheList.size() > 0) {
//            return cacheList;
//        } else {
            List<ItemInfo> imtItemInfoList = reGetShopsByProvince(province, itemId);
//            redisCache.reSetCacheList(key, imtItemInfoList);
//            redisCache.expire(key, 60, TimeUnit.MINUTES);
            return imtItemInfoList;
//        }
    }

    @Override
    public int reservationBatch() {
        return 0;
    }

    public String getMaxInventoryShopId(List<ItemInfo> list1, List<Shop> list2, String city) {

        //本城市的shopId集合
        List<String> cityShopIdList = list2.stream().filter(iShop -> iShop.getCityName().contains(city))
                .map(Shop::getShopId).collect(Collectors.toList());

        List<ItemInfo> collect = list1.stream().filter(i -> cityShopIdList.contains(i.getShopId())).sorted(Comparator.comparing(ItemInfo::getInventory).reversed()).collect(Collectors.toList());


        if (collect != null && collect.size() > 0) {
            return collect.get(0).getShopId();
        }

        return null;
    }

    public static Double getDisdance(MapPoint point1, MapPoint point2) {
        double lat1 = (point1.getLatitude() * Math.PI) / 180; //将角度换算为弧度
        double lat2 = (point2.getLatitude() * Math.PI) / 180; //将角度换算为弧度
        double latDifference = lat1 - lat2;
        double lonDifference = (point1.getLongitude() * Math.PI) / 180 - (point2.getLongitude() * Math.PI) / 180;
        //计算两点之间距离   6378137.0 取自WGS84标准参考椭球中的地球长半径(单位:m)
        return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDifference / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(lonDifference / 2), 2))) * 6378137.0;
    }

    public String getMinDistanceShopId(List<Shop> list2, String province, String lat, String lng) {
        //本省的
        List<Shop> shopList = list2.stream().filter(iShop -> iShop.getProvinceName().contains(province))
                .collect(Collectors.toList());

        MapPoint myPoint = new MapPoint(Double.parseDouble(lat), Double.parseDouble(lng));
        for (Shop shop : shopList) {
            MapPoint point = new MapPoint(Double.parseDouble(shop.getLat()), Double.parseDouble(shop.getLng()));
            Double disdance = getDisdance(myPoint, point);
            shop.setDistance(disdance);
        }

        List<Shop> collect = shopList.stream().sorted(Comparator.comparing(Shop::getDistance)).collect(Collectors.toList());

        return collect.get(0).getShopId();

    }

    @Override
    public String getShopId(int shopType, String itemId, String province, String city, String lat, String lng) {
        //查询所在省市的投放产品和数量
        List<ItemInfo> shopList = getShopsByProvince(province, itemId);
        //取id集合
        List<String> shopIdList = shopList.stream().map(ItemInfo::getShopId).collect(Collectors.toList());
        //获取门店列表
        List<Shop> iShops = getShoplist();
        //获取今日的门店信息列表
        List<Shop> list = iShops.stream().filter(i -> shopIdList.contains(i.getShopId())).collect(Collectors.toList());

        String shopId = "";
        if (shopType == 1) {
            //预约本市出货量最大的门店
            shopId = getMaxInventoryShopId(shopList, list, city);
            if (StringUtils.isEmpty(shopId)) {
                //本市没有则预约本省最近的
                shopId = getMinDistanceShopId(list, province, lat, lng);
            }
        } else {
            //预约本省距离最近的门店
            shopId = getMinDistanceShopId(list, province, lat, lng);
        }

        if (StringUtils.isEmpty(shopId)) {
            throw new ServiceException("申购时根据类型获取的门店商品id为空");
        }


        return shopId;
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
