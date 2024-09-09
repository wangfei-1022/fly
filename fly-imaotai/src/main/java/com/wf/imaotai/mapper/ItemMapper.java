package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<Item> getList();

    @Update("truncate imaotai_item")
    Long truncateItem();

    Long insertItem(Item item);
}
