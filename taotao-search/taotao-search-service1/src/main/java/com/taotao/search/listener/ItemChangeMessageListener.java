package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;



/**
 * 接收消息的监听器
 * @title ItemChangeMessageListener.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public class ItemChangeMessageListener implements MessageListener {
	//注入service 直接调用 方法更新即可
	@Autowired
	private SearchItemService service;
	
	@Override
	public void onMessage(Message message) {
		//判断消息的类型是否为textmessage
		if(message instanceof TextMessage){
			//如果是 获取商品的id 
			TextMessage message2 = (TextMessage)message;
			String itemidstr;
			try {
				//获取的就是商品的id的字符串
				itemidstr = message2.getText();
				Long itemId = Long.parseLong(itemidstr);
				//通过商品的id查询数据   需要开发mapper 通过id查询商品(搜索时)的数据
				//更新索引库
				TaotaoResult taotaoResult = service.updateSearchItemById(itemId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
