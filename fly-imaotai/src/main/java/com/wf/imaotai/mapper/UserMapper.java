package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    public Long login(User user);

    @Select("INSERT INTO user(mobile, email, name) VALUES(#{mobile}, #{email}, #{name})")
    public Long addUser(User user);
}
