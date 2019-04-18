package com.taotao.protal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.commons.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.protal.pojo.Ad1Node;

@Controller
public class PageController {
	@Autowired
	private ContentService contentservice;
	
	@Value("${AD1_CATEGORY_ID}")
	private Long categoryId;
	
	@Value("${AD1_HEIGHT_B}")
	private String AD1_HEIGHT_B;
	
	@Value("${AD1_HEIGHT}")
	private String AD1_HEIGHT;
	
	@Value("${AD1_WIDTH}")
	private String AD1_WIDTH;
	
	@Value("${AD1_WIDTH_B}")
	private String AD1_WIDTH_B;
	/**
	 * 展示首页
	 * @return
	 */
	//接收URL的请求http://localhost:8082/index.html
	@RequestMapping("/index")
	public String showIndex(Model model){
		//引入服务
		//注入服务
		//添加业务逻辑，根据内容分类的id 查询 内容列表
		List<TbContent> contentlist = contentservice.getContentListByCategoryId(categoryId);
		//转成自定义的POJO   AD1NOde的列表
		List<Ad1Node> nodes = new ArrayList<>();
		for (TbContent tbContent : contentlist) {
			Ad1Node node = new Ad1Node();
			node.setAlt(tbContent.getSubTitle());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setHref(tbContent.getUrl());
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			nodes.add(node);
		}
		//传递数据给JSP
		model.addAttribute("ad1", JsonUtils.objectToJson(nodes));
		return "index";//响应jsp
	}
}
