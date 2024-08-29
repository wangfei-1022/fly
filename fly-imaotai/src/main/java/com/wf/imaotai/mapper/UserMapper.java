package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.request.UserRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    public List<User> getList(UserRequest userRequest);

    @Select("SELECT * FROM imaotai_user WHERE mobile = #{mobile}")
    public User selectByMobile(Long mobile);

    public int insertUser(User user);

    public int updateUser(User user);
}
