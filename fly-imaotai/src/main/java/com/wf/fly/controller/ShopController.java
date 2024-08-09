package com.wf.fly.controller;

import com.wf.fly.common.R;
import com.wf.fly.entity.Shop;
import com.wf.fly.service.ShopService;
import com.wf.fly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imaotai/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 查询i茅台商品列表
     */
    @GetMapping("/list")
    public R list(Shop iShop) {
        List<Shop> page = shopService.list(iShop);
        return R.success(page);
    }


    /**
     * 刷新i茅台商品列表
     */
    @GetMapping(value = "/refresh", name = "刷新i茅台商品列表")
    public R refreshShop() {
        shopService.refreshShop();
        return R.success("刷新成功");
    }


}
