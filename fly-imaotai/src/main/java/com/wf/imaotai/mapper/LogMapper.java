package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Log;
import com.wf.imaotai.model.request.LogRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    List<Log> getList(LogRequest request);

    @Insert("INSERT INTO imaotai_log(id, mobile, content, create_time) " +
            "VALUES(#{id}, #{mobile}, #{content}, #{createTime})")
    int insetLog(Log log);
}
