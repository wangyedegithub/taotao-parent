package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategroyService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
@Service
public class ContentCategroyServiceImpl implements ContentCategroyService{
	@Autowired
	private TbContentCategoryMapper mapper;
	@Override
	public List<EasyUITreeNode> getContentCatListByParentId(Long parentId) {
		// TODO Auto-generated method stub
		//准备查询条件
	/*	TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);//select * from tbcontentcategory where parent_id=1
		List<TbContentCategory> list=mapper.selectByExample(example);*/
		List<TbContentCategory> list=getChildNodeList(parentId);
		List<EasyUITreeNode> nodes =new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node =new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			node.setText(tbContentCategory.getName());
			nodes.add(node);
		}
		return nodes;
	}
	//添加content的分类
	@Override
	public TaotaoResult createCategroy(Long parentId, String name) {
		// TODO Auto-generated method stub
		//补全TB属性
		TbContentCategory category=new TbContentCategory();
		category.setCreated(new Date());
		category.setUpdated(new Date());
		category.setIsParent(false);
		category.setParentId(parentId);
		category.setStatus(1);
		category.setSortOrder(1);
		category.setName(name);
		mapper.insert(category);
		//如果要添加的节点的父节点本身是叶子子节点需要更新为父节点
		TbContentCategory parent=mapper.selectByPrimaryKey(parentId);
		if (parent.getIsParent()==false) {
			parent.setIsParent(true);
			mapper.updateByPrimaryKeySelective(parent);//更新
		}
		return TaotaoResult.ok(category);
	}
	//重命名
	@Override
	public TaotaoResult updateCategroy(Long Id, String name) {
		// TODO Auto-generated method stub
		TbContentCategory category1=mapper.selectByPrimaryKey(Id);
		category1.setName(name);
		mapper.updateByPrimaryKeySelective(category1);
		return TaotaoResult.ok(category1);
	}
	//删除
	@Override
	public TaotaoResult deleteCategroy(Long id) {
		// TODO Auto-generated method stub
		TbContentCategory category2=mapper.selectByPrimaryKey(id);
		//递归删除
		if (category2.getIsParent()) {
			//1.找到下面的所有子节点
			List<TbContentCategory> list2=getChildNodeList(id);
			for (TbContentCategory tbContentCategory1 : list2) {
				mapper.deleteByPrimaryKey(tbContentCategory1.getId());
			}
		}
		mapper.deleteByPrimaryKey(id);
		if(getChildNodeList(category2.getParentId()).size()==0){
			TbContentCategory category3=mapper.selectByPrimaryKey(category2.getParentId());
			category3.setIsParent(false);
			mapper.updateByPrimaryKeySelective(category3);
		}
		return TaotaoResult.ok();
	}
	//获得所有子节点
	public List<TbContentCategory> getChildNodeList(long id){
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(id);
		return mapper.selectByExample(example);
	}
}
