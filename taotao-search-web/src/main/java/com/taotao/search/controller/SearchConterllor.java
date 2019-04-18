package com.taotao.search.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.commons.pojo.SearchResult;
import com.taotao.search.service.SearchItemService;

@Controller
public class SearchConterllor {
	@Value("${ITEM_ROWS}")
	private Integer ITEM_ROWS;
	@Autowired
	private SearchItemService service;
	@RequestMapping("/search")
	public String search(@RequestParam(defaultValue="1") Integer page,@RequestParam(value="q") String queryString,Model model) throws Exception{
		//处理乱码
		queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
		SearchResult result=service.search(queryString, page, ITEM_ROWS);
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", result.getPageCount());
		/*System.out.println("总页数数"+result.getPageCount());*/
		model.addAttribute("itemList",result.getItemList());
		/*System.out.println("总记录数"+result.getRecordCount());
		System.out.println("查询条件"+queryString);*/
		model.addAttribute("page", page);
		return "search";
	}
}
