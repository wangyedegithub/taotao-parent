package com.taotao.cart.service;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;

public interface CartService {
	
	public TaotaoResult addItemUserCart(TbItem item,Integer num,Long userId);	
	
	public List<TbItem> getCartList(Long userId);
	
	public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num);
	
	public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId);
	
}
