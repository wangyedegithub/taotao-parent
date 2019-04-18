package com.taotao.controller1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	//跳转index页面	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	//查询商品url:item-list
	@RequestMapping("/item-list")
	public String showItemList(){
		return "item-list";
	}
	//查询，删除，等等商品功能接收url:item-list返回到item-list
	//restful 用{page}代表接收值返回值weipage
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return "page";
	}
		
	
}
