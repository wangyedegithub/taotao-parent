package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	private ContentService service;
	
	@RequestMapping(value ="/content/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveContent(TbContent content){
		return service.saveContent(content);
	}
	
	@RequestMapping(value ="/content/query/list", method = RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getContentList(long categoryId,Integer page,Integer rows){
		return service.getContentList(categoryId, page, rows);
	}
	
	@RequestMapping(value ="/content/edit", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateContent(TbContent content){
		return service.updateContent(content);
	}
	
	@RequestMapping(value ="/content/delete", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult deleteContent(long[] ids){
		return service.deleteContent(ids);
	}
	
}
