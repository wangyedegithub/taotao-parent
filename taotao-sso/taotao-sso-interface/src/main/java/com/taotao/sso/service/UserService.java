package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
	//对注册信息验证
	public TaotaoResult userRegisterCheck(String param,Integer type);
	
	//注册
	public TaotaoResult userRegister(TbUser user);
	
	//登录
	public TaotaoResult userLogin(String username,String password);
	
	//过期验证
	public TaotaoResult getUserByToken(String token);
	
	//安全退出
	public TaotaoResult userLogout(String token);

}
