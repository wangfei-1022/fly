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

    List<Shop> getList(ShopRequest request);

    List<Shop> districtList(ShopRequest request);

    List<Shop> cityList(ShopRequest request);

    List<Shop> provinceList(ShopRequest request);

    Long insertShop(Shop shop);

    Long insertShopBatch(List<Shop> list);

    Shop getShopById(String shopId);

    @Update("truncate imaotai_shop")
    Long clearAll();
}
