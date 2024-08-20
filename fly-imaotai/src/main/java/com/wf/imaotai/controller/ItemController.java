package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.domain.OrderType;
import com.wf.imaotai.domain.AppointmentType;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imt/item")
public class ItemController {
    @Autowired
    public ItemService itemService;

    @GetMapping("list")
    public R list(Item item) {
        List<Item> items = itemService.list(item);
        PageInfo<Item> itemPageInfo = new PageInfo<>(items);
        return R.success(itemPageInfo);
    }

    @GetMapping("order/type")
    public R list() {
        return R.success(itemService.convertSelection(OrderType.values()));
    }




    @GetMapping("/refresh")
    public R refresh(Item item) {
        itemService.getCurrentSessionId();
        return R.success("");
    }
}
