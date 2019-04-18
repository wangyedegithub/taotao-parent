package com.taotao.content.service;

import java.util.List;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;

public interface ContentCategroyService {
	public List<EasyUITreeNode> getContentCatListByParentId(Long parentId);
	public TaotaoResult createCategroy(Long parentId,String name);
	public TaotaoResult updateCategroy(Long id,String name);
	public TaotaoResult deleteCategroy(Long id);
}
