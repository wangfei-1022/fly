package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.Shop;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Select("SELECT * FROM imaotai_item")
    public List<Item> getList();
    @Update("truncate imaotai_item")
    public Long truncateItem();

    @Insert("INSERT INTO imaotai_item(id, item_id, item_code, content, title, picture, create_time) " +
            "VALUES(#{id}, #{itemId}, #{itemCode},#{content}, #{title}, #{picture}, #{createTime})")
    public Long addItem(Item item);
}
