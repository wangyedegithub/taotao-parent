package com.taotao.protal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.commons.util.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.protal.service.TbUservices;
import com.taotao.sso.service.UserService;


public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private	TbUservices userService;
	/**
	 * 在handler执行之前返回
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.判断用户是否登陆
		//从cookie获取token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//如果token为null拦截返回false
		if(token!=null){
		//根据token获取用户信息，调用sso中service
		TbUser user=userService.getUserByToken(token);
		//1.1取不到用户信息，需要跳转到登录页面，把用户请求的url传递给登录页面
		if (null == user) {
			//跳转到登录页面，把用户请求的url作为参数传递给登录页面。
			response.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN 
					+ "?redirect=" + request.getRequestURL());
			//返回false
			return false;
		}
		else 
			return true;
		}

		//返回false拦截
		//1.2取到用户信息放行
		// 返回值决定handler是否执行
		System.out.println(userService.SSO_BASE_URL);
		response.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN 
				+ "?redirect=" + request.getRequestURL());
		//返回false
		return false;
	}
	/**
	 * 在handler执行之后返回modelandview之前执行
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 返回值决定handler是否执行
		
	}
	
	/**
	 * 在handler执行之后返回modelandview之后执行
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
