package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.service.CartService;
import com.taotao.cart.service.CookieCartService;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.CookieUtils;
import com.taotao.commons.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("cart")
public class CartController {
	/*
	 * 实现购物车功能 1.根据TT_TICKET查看redis中存在用户信息 2.b) 调用购物车服务，把商品数据保存在redis的购物车中的
	 * 参数包含：用户id，商品id，商品数量 展示购物车详情页 获取用户信息，需要用户的id
	 * 调用购物车服务，把该用户的所有购物车数据查询数来，根据用户id查询 跳转到购物车页面（cart.jsp），显示数据
	 */
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;
	@Autowired
	private CookieCartService cookieCartService;

	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;
	
	@RequestMapping("add/{itemId}")
	public String addItemToCart(@PathVariable("itemId") Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 1.获得商品信息
		TbItem item = (TbItem) itemService.getItemById(itemId).getData();
		item.setNum(0);
		TbUser user =cookieCartService.checkTokenGetStatus(request);
		if (user != null) {
			cartService.addItemUserCart(item, num, user.getId());
		} else
			cookieCartService.addItemCookieCart(item, num, request, response);
		return "cartSuccess";
	}

	@RequestMapping("/cart")
	public String showCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		TbUser user = cookieCartService.checkTokenGetStatus(request);
		List<TbItem> list = new ArrayList<>();
		if (user != null) {
			//1.获得cookie中的类容
			List<TbItem> Items=cookieCartService.getCartItemList(request);
			//2.同步到redis中
			for (TbItem tbItem : Items) {
				cartService.addItemUserCart(tbItem, tbItem.getNum(),user.getId());
			}
			list = cartService.getCartList(user.getId());
		} else {
			list = cookieCartService.getCartItemList(request);
		}
		model.addAttribute("cartList", list);
		return "cart";
	}

	// 删除商品
	@RequestMapping("/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		TbUser user = cookieCartService.checkTokenGetStatus(request);
		List<TbItem> list = new ArrayList<>();
		if (user != null) {
			cartService.deleteItemCartByItemId(user.getId(), itemId);
		} else {
			cookieCartService.deleteCartItem(request, response, itemId);
		}
		return "redirect:/cart/cart.html";
	}

	// 修改商品
	@RequestMapping("/update/num/{itemId}/{itemNum}")
	@ResponseBody
	public TaotaoResult updateCartItem(@PathVariable Long itemId, @PathVariable Integer itemNum,
			HttpServletRequest request, HttpServletResponse response) {
		TbUser user = cookieCartService.checkTokenGetStatus(request);
		TaotaoResult result = null;
		List<TbItem> list = new ArrayList<>();
		if (user != null) {
			result = cartService.updateItemCartByItemId(user.getId(), itemId, itemNum);
		} else {
			result = cookieCartService.updateCartItem(request, response, itemId, itemNum);
		}
		return result;
	}

}
