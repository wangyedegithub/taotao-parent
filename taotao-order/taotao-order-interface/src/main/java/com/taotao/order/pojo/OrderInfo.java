/**
 * 
 */
package com.taotao.order.pojo;

/**
 * @author 王爷
 *
 */

import java.io.Serializable;
import java.util.List;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * 扩展TbORder
 * @author 王爷
 *
 */
public class OrderInfo extends TbOrder implements Serializable {
	private List<TbOrderItem> orderItems;// 商品信息
	private TbOrderShipping orderShipping;//配送地址信息

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

}
