package com.wf.imaotai.service;


import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.Shop;

import java.util.List;

public interface ItemService {
    List<Item> list(Item item);

    String getCurrentSessionId();

}
