package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM imaotai_user")
    public List<User> getList();

    @Select("SELECT * FROM imaotai_user WHERE id = #{id}")
    public Long login(User user);

    @Select("INSERT INTO imaotai_user(mobile, email, name) VALUES(#{mobile}, #{email}, #{name})")
    public Long addUser(User user);
}
