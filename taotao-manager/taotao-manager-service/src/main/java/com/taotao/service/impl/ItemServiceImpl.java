package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.IdUtils;
import com.taotao.commons.util.JsonUtils;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Destination destination;
	@Autowired
	private JedisClient client;
	@Value("${ITEM_INFO_KEY}")
	private String ITEM_INFO_KEY;
	@Value("${ITEM_INFO_KEY_EXPIRE}")
	private Integer ITEM_INFO_KEY_EXPIRE; 
	
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 30;
		}
		PageHelper.startPage(page, rows);
		//准备查询条件
		TbItemExample example = new TbItemExample();
		//查询所有
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//把查询结果放入分页工具中
		PageInfo<TbItem> info = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//总数
		result.setTotal((int) info.getTotal());
		//集合
		result.setRows(info.getList());
		return result;
	}

	@Override
	public TaotaoResult saveItem(TbItem item, String desc) {
		// 1.生成商品id
		long itemId = IdUtils.genItemId();
		item.setId(itemId);
		// 补全商品的信息
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 状态1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);
		// 添加到数据库
		tbItemMapper.insertSelective(item);
		// 3.补全商品描述中的属性
		TbItemDesc desc2 = new TbItemDesc();
		desc2.setItemDesc(desc);
		desc2.setItemId(itemId);
		desc2.setCreated(item.getCreated());
		desc2.setUpdated(item.getCreated());
		// 4.插入商品描述数据
		// 注入tbitemdesc的mapper
		descMapper.insertSelective(desc2);
		
		//添加发送消息的业务逻辑
		send(itemId);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult getItemsDescById(long id) {
		// 添加缓存的原则是，不能够影响现在有的业务逻辑
				// 查询缓存
		long itemId=id;
		try {
				// 从缓存中查询
				String jsonstring = client.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");
				if (StringUtils.isNotBlank(jsonstring)) {// 不为空则直接返回
					System.out.println("描述有缓存");
					client.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
					TbItemDesc itemDesc=JsonUtils.jsonToPojo(jsonstring, TbItemDesc.class);
					return TaotaoResult.ok(itemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = descMapper.selectByPrimaryKey(id);
		try {
			client.set(ITEM_INFO_KEY + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			client.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(itemDesc);
	}

	@Override
	public TaotaoResult updateItem(TbItem item,String desc) {
		// 3.补全商品描述中的属性
		TbItemDesc desc2 = descMapper.selectByPrimaryKey(item.getId());
		desc2.setItemDesc(desc);
		desc2.setUpdated(new Date());
		desc2.setCreated(desc2.getCreated());
		// 4.插入商品描述数据
		// 注入tbitemdesc的mapper
		descMapper.updateByPrimaryKey(desc2);
		
		// 补全商品的信息
		TbItem item1=tbItemMapper.selectByPrimaryKey(item.getId());
		item.setCreated(item1.getCreated());
		item.setUpdated(new Date());
		// 状态1-正常，2-下架，3-删除',
		item.setStatus(item1.getStatus());
		// 添加到数据库
		tbItemMapper.updateByPrimaryKey(item);
		System.out.println(item.getPrice());
		//添加发送消息的业务逻辑
		long itemId=item.getId();
		send(itemId);
		return TaotaoResult.ok();
	}
	//删除商品
	@Override
	public TaotaoResult deleteItem(long[] ids) {
		for (long id : ids) {
			tbItemMapper.deleteByPrimaryKey(id);
			descMapper.deleteByPrimaryKey(id);
			//添加发送消息得业务逻辑
			long itemId=id;
			send(itemId);
		}
		return TaotaoResult.ok();
	}
	//下架商品
	@Override
	public TaotaoResult instockItem(long[] ids) {
		for (long id : ids) {
			TbItem item=tbItemMapper.selectByPrimaryKey(id);
			// 状态1-正常，2-下架，3-删除',
			if(item.getStatus()==(byte) 1){
			item.setStatus((byte) 2);
			tbItemMapper.updateByPrimaryKeySelective(item);
			long itemId=id;
			send(itemId);
			}
		}
		return TaotaoResult.ok();
	}
	//上架
	@Override
	public TaotaoResult reshelfItem(long[] ids) {
		for (long id : ids) {
			TbItem item=tbItemMapper.selectByPrimaryKey(id);
			// 状态1-正常，2-下架，3-删除',
			if(item.getStatus()==(byte) 2){
			item.setStatus((byte) 1);
			tbItemMapper.updateByPrimaryKeySelective(item);
			long itemId=id;
			send(itemId);
			}
		}
		return TaotaoResult.ok();
	}
	
	public void send(long itemId){
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 返回消息商品id
				return session.createTextMessage(itemId+"");
			}
		});
	}

	@Override
	public TaotaoResult getItemById(Long itemId) {
		try {
			// 从缓存中查询
			String jsonstring = client.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(jsonstring)) {// 不为空则直接返回
				System.out.println("商品有缓存");
				client.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
				TbItem item=JsonUtils.jsonToPojo(jsonstring, TbItem.class);
				return TaotaoResult.ok(item);
			}
		} catch (Exception e) {
		e.printStackTrace();
		}

		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		try {
			client.set(ITEM_INFO_KEY + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			client.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(item);
	}
}
