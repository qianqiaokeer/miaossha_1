package com.imooc.miaosha.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.imooc.miaosha.pojo.User;

@Mapper
public interface UserDao {
	//根据id查询user
	@Select("select * from user where id=#{id}")
	public User getUserById(@Param("id")long id);
	//插入user信息
	@Insert("insert into user (id,name) values(#{id},#{name})")
	public int insertUser(User user);
}
