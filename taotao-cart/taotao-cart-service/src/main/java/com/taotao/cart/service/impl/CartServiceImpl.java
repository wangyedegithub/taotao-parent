package com.taotao.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taotao.cart.service.CartService;

import com.taotao.commons.jedis.JedisClient;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.JsonUtils;
import com.taotao.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClient client;

	@Value("${TT_CART_REDIS_PRE_KEY}")
	private String TT_CART_REDIS_PRE_KEY;

	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;

	// COOKIE中购物车商品对应的key
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;
	
	// 购物车cookie生存期
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;

	/**
	 * 添加商品到用户存入redis
	 * @param 商品，数量，用户id
	 * @return TaotaoResult.ok();
	 */
	@Override
	public TaotaoResult addItemUserCart(TbItem item, Integer num, Long userId) {
		// TODO Auto-generated method stub
		// 1.查询 可以根据 商品id 和用户id获取某一个商品是否在购物车中
		TbItem itemtem = queryItemByItemIdAndUserId(item.getId(), userId);
		// 2.判断要添加的商品是否存在于列表中
		if (itemtem != null) {
			// 3.如果存在，直接数量相加
			itemtem.setNum(itemtem.getNum() + num);

			// 图片只取一张
			// 设置到redis
			client.hset(TT_CART_REDIS_PRE_KEY + ":" + userId + "", itemtem.getId() + "",
					JsonUtils.objectToJson(itemtem));
		} else {
			// 不存在购物车中
			// 不全item的信息
			item.setNum(num);
			// .设置商品的图片为一张
			if (item.getImage() != null) {
				item.setImage(item.getImage().split(",")[0]);
			}
			// 存到商品到redis中
			client.hset(TT_CART_REDIS_PRE_KEY + ":" + userId + "", item.getId() + "", JsonUtils.objectToJson(item));
		}
		return TaotaoResult.ok();
	}

	/**
	 * 根据商品id和用户id查询购物车中是否存在此商品
	 * @param itemId
	 * @param userId
	 * @return TbItem tbItem或null
	 */
	private TbItem queryItemByItemIdAndUserId(Long itemId, Long userId) {
		String string = client.hget(TT_CART_REDIS_PRE_KEY + ":" + userId + "", itemId + "");
		if (StringUtils.isNoneBlank(string)) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			return tbItem;
		}
		return null;
	}

	/**
	 * 根据用户id获取购物车的商品的列表
	 * @param userID
	 * @return 商品列表 List<TbItem> list
	 */
	public List<TbItem> getCartList(Long userId) {
		Map<String, String> map = client.hgetAll(TT_CART_REDIS_PRE_KEY + ":" + userId + "");
		//
		List<TbItem> list = new ArrayList<>();
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();// 商品的jSON数据
				// 转成POJO
				TbItem item = JsonUtils.jsonToPojo(value, TbItem.class);
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 更新购物车中商品数量存入redis
	 * 
	 * @param userId,itemId,num
	 * @return TaotaoResult.ok();
	 */
	@Override
	public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num) {
		// 1.在购物车中查询商品记录
		TbItem item = queryItemByItemIdAndUserId(itemId, userId);
		// 2.更新redis中的数据
		item.setNum(num);
		// 3.把购物车中数据存入redis中
		client.hset(TT_CART_REDIS_PRE_KEY + ":" + userId + "", item.getId() + "", JsonUtils.objectToJson(item));
		return TaotaoResult.ok();
	}

	/**
	 * 删除购物车中商品
	 * @param userId
	 *            itemId
	 * @return TaotaoResult.ok();
	 */
	@Override
	public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId) {
		client.hdel(TT_CART_REDIS_PRE_KEY + ":" + userId + "", itemId + "");
		return TaotaoResult.ok();
	}
}
