package com.wf.imaotai.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.mapper.ItemMapper;
import com.wf.imaotai.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    public ItemMapper itemMapper;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public List<Item> list(Item item) {
        PageHelper.startPage(1, 10);
        List<Item> xx1 = itemMapper.getList();
        return xx1;
    }

    @Override
    public String getCurrentSessionId() {
        String mtSessionId = "";
        long dayTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String res = restTemplate.getForObject("https://static.moutai519.com.cn/mt-backend/xhr/front/mall/index/session/get/" + dayTime, String.class);
        //替换 current_session_id 673 ['data']['sessionId']
        JSONObject jsonObject = JSONObject.parseObject(res);

        if (jsonObject.getString("code").equals("2000")) {
            JSONObject data = jsonObject.getJSONObject("data");
            mtSessionId = data.getString("sessionId");
            itemMapper.truncateItem();
            //item插入数据库
            JSONArray itemList = data.getJSONArray("itemList");
            for (Object obj : itemList) {
                JSONObject item = (JSONObject) obj;
                Item shopItem = new Item("", item);
                itemMapper.addItem(shopItem);
            }
        }
        return mtSessionId;
    }
}
