package com.taotao.protal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Service
public class TbUservices {
	@Autowired
	private	UserService userService;
	
	@Value("${SSO_BASE_URL}")
	public String SSO_BASE_URL;
	@Value("${SSO_PAGE_LOGIN}")
	public String SSO_PAGE_LOGIN;
	
	
	public TbUser getUserByToken(String token) {
		try {
			//调用sso系统的服务，根据token取用户信息
			TaotaoResult result = userService.getUserByToken(token);
			System.out.println(SSO_BASE_URL);
			System.out.println(SSO_PAGE_LOGIN);
			//
			if (result.getStatus() == 200) {
				TbUser user = (TbUser) result.getData();
				return user;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
