/**
 * 
 */
package com.taotao.order.service;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

/**
 * @author 王爷
 *
 */
public interface OrderService {
	public TaotaoResult createOrder(OrderInfo orderInfo);
}
