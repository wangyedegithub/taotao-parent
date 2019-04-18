package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.CookieUtils;
import com.taotao.commons.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 未登录状态下购物车相关操作
 * @author 王洁
 *
 */
@Service
public class CookieCartService {

	// COOKIE中购物车商品对应的key
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;
	// 购物车cookie生存期
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;
	@Autowired
	private UserService userService;
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;

	/**
	 * 判断用户登录
	 * 
	 * @param request
	 * @return user或null
	 */
	public TbUser checkTokenGetStatus(HttpServletRequest request) {
		// 2.根据TT_TOKEN_KEY查看redis中存在用户信息
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		// 3.判断用户是否登录
		if (token != null) {
			TbUser user = (TbUser) userService.getUserByToken(token).getData();
			// 添加Cart到用户
			if (user != null) {
				return user;

			} else {
				// 添加cart到cookie
				return null;
			}
		} else
			return null;
	}
	/**
	 * 添加商品到cookie
	 * 
	 * @param item
	 * @param num
	 * @param request
	 * @param response
	 * @return TaotaoResult.ok()
	 */
	//分层思想写在interface中
	public TaotaoResult addItemCookieCart(TbItem item, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		TbItem cartItem = null;
		// 取购物车商品列表
		List<TbItem> itemList = getCartItemList(request);
		// 判断购物车商品列表中是否存在此商品
		for (TbItem cItem : itemList) {
			// 如果存在此商品
			if (cItem.getId().equals(item.getId())) {
				// 增加商品数量
				cItem.setNum(cItem.getNum() + num);
				cartItem = cItem;
				break;
			}
		}
		if (cartItem == null) {
			cartItem = new TbItem();
			// 设置数量
			item.setNum(num);
			// 补全信息。
			if (item.getImage() != null) {
				item.setImage(item.getImage().split(",")[0]);
			}
			cartItem = item;
			// 添加到购物车列表
			itemList.add(cartItem);
		}
		// 把购物车列表写入cookie
		System.out.println(JsonUtils.objectToJson(itemList));
		CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(itemList), EXPIRE_TIME, true);
		return TaotaoResult.ok();
	}

	/**
	 * 从cookie中取商品列表
	 * @param request
	 * @return List<TbItem> list或ArrayList<>()
	 */
	public List<TbItem> getCartItemList(HttpServletRequest request) {
		// 从cookie中取商品列表
		String cartJson = CookieUtils.getCookieValue(request, TT_CART_KEY, true);
		if (cartJson == null) {
			return new ArrayList<>();
		}
		// 把json转换成商品列表
		try {
			List<TbItem> list = JsonUtils.jsonToList(cartJson, TbItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	
	/**
	 * 更新购物车中商品数量到cookie中
	 * @param request response itemId num
	 * @return TaotaoResult.ok();
	 */
	public TaotaoResult updateCartItem(HttpServletRequest request, HttpServletResponse response, Long itemId,
			Integer num) {
		// 1.从cookie中取商品列表
		List<TbItem> list = getCartItemList(request);
		// 2.遍历list找到指定商品
		for (TbItem tbItem : list) {
			if (tbItem.getId().equals(itemId)) {
				// 3.更新数量
				tbItem.setNum(num);
			}
		}
		// 4.数据存入cookie中
		CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(list), EXPIRE_TIME, true);
		return TaotaoResult.ok();
	}

	/**
	 * 根据itemId删除cookie中的商品
	 * @param request
	 * @param response
	 * @param itemId 商品id
	 * @return TaotaoResult.ok()
	 */
	public TaotaoResult deleteCartItem(HttpServletRequest request, HttpServletResponse response, Long itemId) {
		// 1.从cookie中取商品列表
		List<TbItem> list = getCartItemList(request);
		// 2.遍历list找到指定商品
		for (TbItem tbItem : list) {
			if (tbItem.getId().equals(itemId)) {
				// 3.删除此数据
				list.remove(tbItem);
				break;
			}
		}
		// 4.数据存入cookie中
		if(list.size()>0){
		CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(list), EXPIRE_TIME, true);
		}
		else {
			CookieUtils.deleteCookie(request, response, TT_CART_KEY);
		}
		return TaotaoResult.ok();
	}
}
