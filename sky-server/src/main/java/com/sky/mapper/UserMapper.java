package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.User;



@Mapper
public interface UserMapper {

    /**
     * 根据openId获取用户
     * @param openId
     */
    @Select("select * from user where openid = #{openid}")
    public User getByOpenid(String openid);

    /**
     * 
     * @param user
     */
    public void insert(User user);

    @Select("select * from user where id = #{userId}")
    public User getById(Long userId);

}
