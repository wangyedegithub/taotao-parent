package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper catmapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatListByParentId(Long parentId) {
		// TODO Auto-generated method stub
		//准备查询条件
		TbItemCatExample example=new TbItemCatExample();
		Criteria  criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list=catmapper.selectByExample(example);
		//分类列表转换成TreeNode的列表
		List<EasyUITreeNode> nodes =new ArrayList<>();
		for (TbItemCat listCat:list) {
			EasyUITreeNode node =new EasyUITreeNode();
			node.setId(listCat.getId());
			node.setState(listCat.getIsParent()?"closed":"open");
			node.setText(listCat.getName());
			nodes.add(node);
		}
		return nodes;
	}

}
