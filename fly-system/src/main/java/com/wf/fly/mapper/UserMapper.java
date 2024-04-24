package com.wf.fly.mapper;

import com.wf.fly.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    public Long login(User user);

    @Select("INSERT INTO user(mobile, email, name) VALUES(#{mobile}, #{email}, #{name})")
    public Long addUser(User user);

    @Select("SELECT * FROM user")
    public List<User> getList(User user);
}
