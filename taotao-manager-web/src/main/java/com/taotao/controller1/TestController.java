package com.taotao.controller1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.service.TestService;

@Controller
public class TestController {
	@Autowired
	private TestService testService;
	
	@RequestMapping("/test/queryNow")
	@ResponseBody
	public String queryNow(){
		//引用服务
		//注入服务
		//调用服务的方法
		return testService.queryNow();
	}
}
