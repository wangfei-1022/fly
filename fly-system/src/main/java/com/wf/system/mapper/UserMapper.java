package com.wf.system.mapper;

import com.wf.system.entity.User;
import com.wf.system.model.dto.LoginDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM system_user WHERE mobile = #{username} and password = #{password}")
    public User getOne(LoginDTO loginDTO);

    @Select("INSERT INTO system_user(mobile, email, name) VALUES(#{mobile}, #{email}, #{name})")
    public Long addUser(User user);

    @Select("SELECT * FROM system_user")
    public List<User> getList(User user);
}
