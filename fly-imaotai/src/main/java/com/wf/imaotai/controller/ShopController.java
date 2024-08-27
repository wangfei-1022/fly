package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.constant.AppointmentType;
import com.wf.imaotai.entity.Shop;
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
    public R list(Shop shop) {
        List<Shop> shops = shopService.getShoplist(shop);
        PageInfo<Shop> shopPageInfo = new PageInfo<>(shops);
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

    @GetMapping("appointment/type")
    public R listUserType() {
        return R.success(AppointmentType.values());
    }

}
