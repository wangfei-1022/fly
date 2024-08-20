package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

    @Select("SELECT * FROM imaotai_log")
    public List<Log> getList();
}
