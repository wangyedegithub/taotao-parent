package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commons.jedis.JedisClient;
import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper mapper;
	
	@Autowired
	private JedisClient client;

	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;
	@Override
	public TaotaoResult saveContent(TbContent content) {
		// 补全商品属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		mapper.insertSelective(content);
		try {
			client.hdel(CONTENT_KEY, content.getCategoryId()+"");
			System.out.println("当插入时，清空缓存!!!!!!!!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 20;
		}
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = mapper.selectByExampleWithBLOBs(example);
		for (TbContent tbContent : list) {
			System.out.println("title" + tbContent.getContent());
		}
		PageInfo<TbContent> info = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		return result;
	}
	//首页展示广告列表
	@Override
	public List<TbContent> getContentListByCategoryId(Long categoryId) {
		//添加缓存不能影响正常的业务逻辑
		//判断 是否redis中有数据  如果有   直接从redis中获取数据 返回
		try {
			String jsonstr = client.hget(CONTENT_KEY, categoryId+"");//从redis数据库中获取内容分类下的所有的内容。
			//如果存在，说明有缓存
			if(StringUtils.isNotBlank(jsonstr)){
			System.out.println("这里有缓存啦！！！！！");
				return JsonUtils.jsonToList(jsonstr, TbContent.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TbContentExample example=new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list=mapper.selectByExample(example);
		
		//将数据写入到redis数据库中   
		// 注入jedisclient 
		// 调用方法写入redis   key - value
		try {
			System.out.println("没有缓存！！！！！！");
			client.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public TaotaoResult updateContent(TbContent content) {
		mapper.updateByPrimaryKeyWithBLOBs(content);
		//当添加内容的时候，需要清空此内容所属的分类下的所有的缓存
		try {
			client.hdel(CONTENT_KEY, content.getCategoryId()+"");
			System.out.println("当插入时，清空缓存!!!!!!!!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteContent(long[] ids) {
	for (long id : ids) {
		mapper.deleteByPrimaryKey(id);
		try {
			client.hdel(CONTENT_KEY, mapper.selectByPrimaryKey(id).getCategoryId()+"");
			System.out.println("当插入时，清空缓存!!!!!!!!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		return TaotaoResult.ok();
	}
}
