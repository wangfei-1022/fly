package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.request.UserRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    public List<User> getList(UserRequest userRequest);

    @Select("SELECT * FROM imaotai_user WHERE mobile = #{mobile}")
    public User selectByMobile(String mobile);

    public int insertUser(User user);

    public int updateUser(User user);

    @Delete("DELETE FROM imaotai_user WHERE mobile = #{mobile}")
    public int deleteUser(User user);

    @Select("SELECT COUNT(*) FROM imaotai_user")
    public Long selectCount();

    @Update("UPDATE imaotai_user SET `minute` = (SELECT FLOOR(RAND() * 50 + 1)) WHERE minute = \"0\"")
    void updateUserMinuteBatch();

    @Update("SET @row_number = 0;\n" +
            "UPDATE imaotai_user\n" +
            "SET `minute` = (@row_number := @row_number + 1) % 50 + 1\n" +
            "ORDER BY RAND();")
    void updateUserMinuteEven();
}
