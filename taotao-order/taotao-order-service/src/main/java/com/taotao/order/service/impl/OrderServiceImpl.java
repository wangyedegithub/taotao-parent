/**
 * 
 */
package com.taotao.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.commons.jedis.JedisClient;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * @author 王爷
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient client;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;	//订单号的key
	
	@Value("${ORDER_ID_INIT}")
	private String ORDER_ID_INIT;	//默认订单id初始值
	
	@Value("${ORDER_ITEM_ID_GEN_KEY}")	//订单明细表的id的key
	private String ORDER_ITEM_ID_GEN_KEY;

	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		// 1、接收表单的数据
		// 2、生成订单id
		if (!client.exists(ORDER_ID_GEN_KEY)) {
			//设置初始值把订单id存在redis中
			client.set(ORDER_ID_GEN_KEY, ORDER_ID_INIT);
		}
		//生成订单id
		String str =  new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		int rannum = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
		Date date=new Date();
		String orderId = str.concat(String.valueOf(rannum));
		System.out.println(orderId);
		//补全订单信息
		orderInfo.setOrderId(orderId);
		orderInfo.setPostFee("0");
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);
		// 3、向订单表插入数据。
		orderMapper.insert(orderInfo);
		// 4、向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成明细id
			Long orderItemId = client.incr(ORDER_ITEM_ID_GEN_KEY);
			System.out.println(orderItemId);
			//tbOrderItem.setId(orderItemId.toString());
			tbOrderItem.setOrderId(orderId);
			//插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		// 5、向订单物流表插入数据。
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		// 6、返回TaotaoResult。
		return TaotaoResult.ok(orderId);
	}

}
