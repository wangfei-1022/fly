package com.wf.xiaomi.mapper;

import com.wf.xiaomi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    public Long login(User user);

    @Select("INSERT INTO user(mobile, email, name) VALUES(#{mobile}, #{email}, #{name})")
    public Long addUser(User user);
}
