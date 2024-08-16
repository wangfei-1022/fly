package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ItemMapper {

    @Update("truncate shop")
    public Long truncateItem();

    @Insert("INSERT INTO shop(id, shop_id, address, full_address, lat, lng, district, district_name, city, city_name, province, province_name, open_start_time, open_end_time, tenant_name, name, layaway, tags, create_time) " +
            "VALUES(#{id}, #{shopId}, #{address},#{fullAddress}, #{lat}, #{lng},#{district}, #{districtName}, #{city}, #{cityName},  #{province}, #{provinceName}, #{openStartTime}, #{openEndTime}, #{tenantName}, #{name}, #{layaway}, #{tags}, #{createTime})")
    public Long addItem(Item item);
}
