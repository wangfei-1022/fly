package com.wf.fly.mapper;

import com.wf.fly.entity.Shop;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface ShopMapper {
    @Select("SELECT * FROM shop")
    public List<Shop> list(Shop shop);

    @Select("INSERT INTO user(id, shopId, address, fullAddress, lat, lng, district, districtName, city, cityName, province, provinceName, openStartTime, openEndTime, tenantName, name, layaway, tags, createTime) " +
            "VALUES(#{id}, #{shopId}, #{address},#{fullAddress}, #{lat}, #{lng},#{district}, #{districtName}, #{city}, #{cityName},  #{province}, #{provinceName}, #{openStartTime}, #{openEndTime}, #{tenantName}, #{name}, #{layaway}, #{tags}, #{createTime})")
    public Long addShop(Shop shop);

    @Update("truncate shop")
    public Long clearAll();
}
