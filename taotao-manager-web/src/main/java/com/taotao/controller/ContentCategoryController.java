package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategroyService;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategroyService service;
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(value="id",defaultValue="0") Long parentId){
		return service.getContentCatListByParentId(parentId);
	}
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult createContentCat(long parentId,String name){
		return service.createCategroy(parentId, name);
	}
	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCat(long id,String name){
		return service.updateCategroy(id, name);
	}
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCat(long id){
		return service.deleteCategroy(id);
	}
}
