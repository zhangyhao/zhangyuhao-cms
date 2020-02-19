package com.zhangyuhao.service.impl;
 
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhangyuhao.common.CmsUtils;
import com.zhangyuhao.dao.UserMapper;
import com.zhangyuhao.entity.User;
import com.zhangyuhao.service.UserService;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper mapper;
	
	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return mapper.getUserByUsername(username);
	}

	@Override
	public int regirect(@Valid User u) {
		//计算密文
		String encry = CmsUtils.encry(u.getPassword(), u.getUsername());
		u.setPassword(encry);
		return mapper.regirect(u);
	}

	@Override
	public User login(User u) {
		// TODO Auto-generated method stub
		u.setPassword(CmsUtils.encry(u.getPassword(), u.getUsername()));
		User user = mapper.login(u);
		return user;
	}

}
