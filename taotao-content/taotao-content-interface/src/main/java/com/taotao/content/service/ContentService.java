package com.taotao.content.service;

import java.util.List;

import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows);
	
	public TaotaoResult saveContent(TbContent content);
	
	//首页展示广告图片
	public List<TbContent> getContentListByCategoryId(Long categoryId);
	
	public TaotaoResult updateContent(TbContent content);
	
	public TaotaoResult deleteContent(long[] ids);
	
}
