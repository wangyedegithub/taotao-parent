/**
 * 
 */
package com.taotao.order.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.CookieUtils;
import com.taotao.sso.service.UserService;

/**
 * @author 王爷
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;

	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Value("${ORDER_CART_URL}")
	private String ORDER_CART_URL;
	
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1.从cookie中获取用户的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		// 2.判断token是否存在，
		if (StringUtils.isEmpty(token)) {
			// 3.如果不存在，说明没登录 ---》重定向到登录的页面
			// request.getRequestURL().toString()：就是访问的URL
			response.sendRedirect(SSO_LOGIN_URL + "/page/login?redirect=" + request.getRequestURL().toString());
			return false;
		}
		// 4.如果token存在，调用SSO的服务 查询用户的信息（看是否用户已经过期）
		TaotaoResult result = userService.getUserByToken(token);
		if (result.getStatus() != 200) {
			// 5.用户已经过期 --》重定向到登录的页面
			response.sendRedirect(SSO_LOGIN_URL + "/page/login?redirect=" + ORDER_CART_URL+"/order/order-cart.html");
			return false;
		}
		// 6.用户没过期（说明登录了）--》放行
		// 设置用户信息到request中 ，目标方法的request就可以获取用户的信息
		request.setAttribute("USER_INFO", result.getData());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
