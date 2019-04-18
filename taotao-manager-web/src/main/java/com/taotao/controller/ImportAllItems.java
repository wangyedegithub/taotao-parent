package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

@Controller
public class ImportAllItems {
	@Autowired
	private SearchItemService service;
	@RequestMapping("/index/importall")
	@ResponseBody
	public TaotaoResult getSearchItemList() throws Exception{
		System.out.println("conterllor正在进行。。。。。。。。");
		return service.AllSerachItems();
	}
}
