package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Shop;
import com.wf.imaotai.model.request.ShopRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShopMapper {

    public List<Shop> getList(ShopRequest request);

    public Long insertShop(Shop shop);

    public Long insertShopBatch(List<Shop> list);

    public Shop getShopById(String shopId);

    @Update("truncate imaotai_shop")
    public Long clearAll();
}
