package com.wf.imaotai.mapper;

import com.wf.imaotai.entity.Item;
import com.wf.imaotai.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM imaotai_user")
    public List<User> getList();


    @Select("SELECT * FROM imaotai_user WHERE mobile = #{mobile}")
    public User selectByMobile(String mobile);

    @Select("SELECT * FROM imaotai_user WHERE id = #{id}")
    public Long login(User user);

    @Insert("INSERT INTO imaotai_user(user_id, mobile, name, token, cookie, device_id, shop_id, item_code, province_name, city_name, address, expire_time, random_minute, del_flag, create_time) " +
            "VALUES(#{userId}, #{mobile}, #{name}, #{token}, #{cookie}, #{deviceId},#{shopId}, #{itemCode}, #{provinceName},#{cityName}, #{address}, #{expireTime},#{randomMinute}, #{delFlag}, #{createTime})")
    public int insertUser(User user);

    @Update("UPDATE imaotai_user SET " +
            "user_id = #{userId}, " +
            "name = #{name}, " +
            "token = #{token}, " +
            "cookie = #{cookie}, " +
            "device_id = #{deviceId}, " +
            "shop_id = #{shopId}, " +
            "item_code = #{itemCode}, " +
            "province_name = #{provinceName}, " +
            "city_name = #{cityName}, " +
            "address = #{address}, " +
            "expire_time = #{expireTime}, " +
            "random_minute = #{randomMinute}, " +
            "del_flag = #{delFlag}, " +
            "update_time = #{updateTime} " +
            "WHERE mobile = #{mobile}")
    public int updateUser(User user);
}
