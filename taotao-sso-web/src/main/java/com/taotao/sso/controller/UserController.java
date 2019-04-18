package com.taotao.sso.controller;




import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.CookieUtils;
import com.taotao.commons.util.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult UserRegisterCheck(@PathVariable String param,@PathVariable Integer type){
		return service.userRegisterCheck(param, type);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult UserRegister(TbUser user){
		return service.userRegister(user);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult UserLogin(HttpServletRequest request,HttpServletResponse response,String username,String password){
		//1.引入服务
		//2.注入服务
		//3.调用服务
		TaotaoResult result = service.userLogin(username, password);
		if (result.getStatus()==200) {
			CookieUtils.setCookie(request, response,TT_TOKEN_KEY, result.getData().toString());
		}
		return result;
	}
	
	@RequestMapping(value="/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		
		//判断是否是Jsonp请求
		if(StringUtils.isNotBlank(callback)){
			//如果是jsonp 需要拼接 类似于fun({id:1});
			TaotaoResult result = service.getUserByToken(token);
			String jsonpstr = callback+"("+JsonUtils.objectToJson(result)+")";
			return jsonpstr;
		}
		//如果不是jsonp
		//1.调用服务
		TaotaoResult result = service.getUserByToken(token);
		return JsonUtils.objectToJson(result);
	}
	
	@RequestMapping(value="/logout/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public TaotaoResult getUserLogout(@PathVariable String token){
		System.out.println(token);
		//判断是否是Jsonp请求
		return service.userLogout(token);
	}
	//退出登录
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public Object userLogout(@PathVariable String token, String callback) {
	TaotaoResult result = null;
	result = service.userLogout(token);
	if (StringUtils.isBlank(callback)) {
		return JsonUtils.objectToJson(result);
	} 
	else {
		String jsonpstr = callback+"("+JsonUtils.objectToJson(result)+")";
		return jsonpstr;
	}
	}

}
