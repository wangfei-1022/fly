package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.constant.AppointmentTimeType;
import com.wf.imaotai.constant.AppointmentType;
import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.model.dto.ItemInfo;
import com.wf.imaotai.model.request.ShopRequest;
import com.wf.imaotai.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imt/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 查询i茅台商品列表
     */
    @GetMapping("/list")
    public R list(ShopRequest shopRequest) {
        List<Shop> shops = shopService.getShoplist(shopRequest);
        PageInfo<Shop> shopPageInfo = new PageInfo<>(shops);
        return R.success(shopPageInfo);
    }

    /**
     * 查询i茅台商品列表
     */
    @GetMapping("/list/no/page")
    public R listAll(ShopRequest shopRequest) {
        List<Shop> shops = shopService.getShopListNoPage(shopRequest);
        PageInfo<Shop> shopPageInfo = new PageInfo<>(shops);
        return R.success(shopPageInfo);
    }


    @GetMapping("/delivery/search")
    public R deliverySearch(ShopRequest shopRequest) {
        if(shopRequest.getProvinceName() == null) {
            return R.error("请先填写省份");
        }
        List<ItemInfo> shops = shopService.deliverySearch(shopRequest);
        PageInfo<ItemInfo> shopPageInfo = new PageInfo<>(shops);
        return R.success(shopPageInfo);
    }

    /**
     * 刷新i茅台商品列表
     */
    @GetMapping(value = "/refresh", name = "刷新i茅台商品列表")
    public R refreshShop() {
        shopService.refreshShop();
        return R.success("刷新成功");
    }

    /**
     * 省
     */
    @GetMapping(value = "/province", name = "获取省")
    public R provinceList(ShopRequest shopRequest) {
        List<Shop> shops = shopService.provinceList(shopRequest);
        return R.success(shops);
    }

    /**
     * 市
     */
    @GetMapping(value = "/province/city", name = "获取市")
    public R cityList(ShopRequest shopRequest) {
        if(shopRequest.getProvinceName() == null) {
            return R.error("请先填写省份");
        }
        List<Shop> shops = shopService.cityList(shopRequest);
        return R.success(shops);
    }

    /**
     * 市
     */
    @GetMapping(value = "/province/city/district", name = "获取区")
    public R districtList(ShopRequest shopRequest) {
        if(shopRequest.getProvinceName() == null) {
            return R.error("请先填写省份");
        }
        if(shopRequest.getCityName() == null) {
            return R.error("请先填写市");
        }
        List<Shop> shops = shopService.districtList(shopRequest);
        return R.success(shops);
    }

    @GetMapping("appointment/type")
    public R appointmentType() {
        return R.success(AppointmentType.values());
    }

    @GetMapping("appointment/time/type")
    public R appointmentTimeType() {
        return R.success(AppointmentTimeType.values());
    }
}
