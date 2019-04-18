/**
 * 
 */
package com.taotao.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.cart.service.CartService;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.CookieUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * @author 王爷
 *
 */
@Controller
@RequestMapping("order")
public class OrderController {
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	/**
	 * 展示购物车中的商品的列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("order-cart")
	public String showOrderCart(HttpServletRequest request, HttpServletResponse response) {
		// 1.获取拦截器中创建session中的用户
		TbUser user=(TbUser) request.getAttribute("USER_INFO");
		// 2.展示用户的地址列表数据 ：根据用户信息中的用户id查询，这里暂时使用静态数据。

		// 3.展示支付的方式列表数据：从数据库中查询支付的方式列表，这里暂时使用静态数据。

		// 4.获得购物车信息
		List<TbItem> cartList = cartService.getCartList(user.getId());
		// 5.将列表 展示到页面中(传递到页面中通过model)
		request.setAttribute("cartList", cartList);
		return "order-cart";
	}
	
	@RequestMapping(value="create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
		// 1、接收表单提交的数据OrderInfo。
		// 2、补全用户信息。
		TbUser user = (TbUser) request.getAttribute("USER_INFO");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		// 3、调用Service创建订单。
		TaotaoResult result = orderService.createOrder(orderInfo);
		//取订单号
		String orderId = result.getData().toString();
		// a)需要Service返回订单号
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		// b)当前日期加三天。
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
		// 4、返回逻辑视图展示成功页面
		return "success";
	}


}
