package com.zhangyuhao.dao;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.zhangyuhao.entity.User;
 
public interface UserMapper {

	//姓名唯一
	@Select("select id,username,password from cms_user where username = #{value} limit 1")
	User getUserByUsername(String username);

	//注册
	@Insert("insert into cms_user(username,password,locked,create_time,score,role)"
			+ "values(#{username},#{password},0,now(),0,0)")
	int regirect(@Valid User u);

	//登录
	@Select("select id,username,password,nickname,birthday,gender,locked,create_time"
			+ " createTime,update_time updateTime,url,role from cms_user where username=#{username}"
			+ " and password=#{password} limit 1")
	User login(User u);

}
