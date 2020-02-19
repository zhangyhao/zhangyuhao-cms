package com.zhangyuhao.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.zhangyuhao.common.CmsContant;
import com.zhangyuhao.entity.User;
import com.zhangyuhao.service.UserService;
 
public class CmsInterceptor implements HandlerInterceptor{

	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		User user = (User) request.getSession().getAttribute(CmsContant.USER_Key);
		if(user!=null){
			
			return true;
		}
		
		//从cookie当中获取用户信息
		User user2 = new User();
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if("username".equals(cookies[i].getName())){
				user2.setUsername(cookies[i].getValue());
			}
			if("userpwd".equals(cookies[i].getName())){
				user2.setPassword(cookies[i].getValue());
			}
		}
		
		//说明cookie中存放的用回信息不完整
		if(null==user2.getUsername()||null==user2.getPassword()){
			response.sendRedirect("/user/login");
			return false;
		}
		//利用cookie中用户信息进行登录操作
		user = userService.login(user2);
		if(user!=null){
			request.getSession().setAttribute(CmsContant.USER_Key, user);
			return true;
		}
		response.sendRedirect("/user/login");
		return false;
	}
}
