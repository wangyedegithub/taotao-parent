package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;

@Controller
public class ItemParamController {
	@Autowired
	private ItemParamService service;
	
	@RequestMapping(value="item/param/list",method = RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult ItemParamList(Integer page,Integer rows){
		return service.getItemParamList(page, rows);
	}
	
	@RequestMapping(value="item/param/delete",method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult deleteItemParam(long[] ids){
		return service.deleteItemParam(ids);
	}
}
