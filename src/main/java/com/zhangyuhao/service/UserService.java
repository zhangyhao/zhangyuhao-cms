package com.zhangyuhao.service;

import javax.validation.Valid;

import com.zhangyuhao.entity.User;
 
public interface UserService {

	User getUserByUsername(String username);

	int regirect(@Valid User u);

	User login(User u);

}
