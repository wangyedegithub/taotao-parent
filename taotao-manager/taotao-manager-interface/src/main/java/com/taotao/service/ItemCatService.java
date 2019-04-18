package com.taotao.service;

import java.util.List;

import com.taotao.commons.pojo.EasyUITreeNode;

public interface ItemCatService {
	/**
	 * 根据父节点的id查询子节点的列表
	 * @param parentId
	 * @return
	 */
	public List<EasyUITreeNode> getItemCatListByParentId(Long parentId);
}
