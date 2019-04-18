package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
//全局异常处理器
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception ex) {
		//1.日志写入日志文件打印出来
		System.out.println(ex.getMessage());
		ex.printStackTrace();
		//2.及时通知开发人员,发短信，发邮件
		System.out.println("发送短信给。。");
		//2.友情提示你的网络错误请重试
		ModelAndView view=new ModelAndView();
		view.setViewName("error/exception");
		view.addObject("message","您的网络有问题,请重试");
		return view;
	}

}
