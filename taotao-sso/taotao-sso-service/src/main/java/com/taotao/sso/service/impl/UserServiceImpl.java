package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.util.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserService;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private TbUserMapper mapper;
	
	@Autowired
	private JedisClient client;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;
	@Override
	public TaotaoResult userRegisterCheck(String param, Integer type) {
		// TODO Auto-generated method stub
		//数据type有三种状态1用户名username 2，手机号phone 3，邮箱Email
		//1.注入mapper
		//2.设置查询条件
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		//3.判断传递过来的参数名称
		if (type ==1) {
			if (StringUtils.isEmpty(param)) {
				return TaotaoResult.ok(false);
			}
			criteria.andUsernameEqualTo(param);
		}
		else if(type==2) {
			criteria.andPhoneEqualTo(param);
		}
		else if (type==3) {
			criteria.andEmailEqualTo(param);
		}
		else {
			//非法数据
			return TaotaoResult.build(400, "非法数据");
		}
		//查询到数据，数据不可用
		List<TbUser> list = mapper.selectByExample(example);
		if (list.size()>0 && list!=null) {
			return TaotaoResult.ok(false);
		}
		return TaotaoResult.ok(true);
	}
	
	@Override
	public TaotaoResult userRegister(TbUser user) {
		// TODO Auto-generated method stub
		//1.注入mapper
		//2.校验数据
		//2.1 校验用户名和密码不能为空
		if(StringUtils.isEmpty(user.getUsername())){
			return TaotaoResult.build(400, "注册失败. 请校验数据后请再提交数据");
		}
		if(StringUtils.isEmpty(user.getPassword())){
			return TaotaoResult.build(400, "注册失败. 请校验数据后请再提交数据");
		}
		//2.2校验数据是否可用phone和Email不为空时校验数据
		if (!(boolean) userRegisterCheck(user.getUsername(), 1).getData()) {
			return TaotaoResult.build(400, "注册失败. 请校验数据后请再提交数据");
		}
		
		if (user.getPhone()!=null) {
			if (!(boolean) userRegisterCheck(user.getPhone(), 2).getData()) {
				return TaotaoResult.build(400, "注册失败. 请校验数据后请再提交数据");
			}
		}
		
		if (user.getEmail()!=null) {
			if (!(boolean) userRegisterCheck(user.getEmail(), 3).getData()) {
				return TaotaoResult.build(400, "注册失败. 请校验数据后请再提交数据");
			}
		}
		//3.如果校验成功   补全其他的属性
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//4.对密码进行MD5加密
		String md5password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5password);
		//5.插入数据
		mapper.insertSelective(user);
		//6.返回taotaoresult
		return TaotaoResult.ok();
	}
	
	@Override
	/**
	 * return 登录成功，返回ture和生成titck失败返回400，
	 */
	public TaotaoResult userLogin(String username, String password) {
		// TODO Auto-generated method stub
		
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
			return TaotaoResult.build(400, "用户名或密码为空，请重新登陆");
		}
		//准备查询条件
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = mapper.selectByExample(example);
		//校验用户名
		if (list.size()==0||list==null) 
		{
			return TaotaoResult.build(400, "用户名或密码错误，请重新登陆");
		}
		//校验密码，先加密
		TbUser user=list.get(0);
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!md5DigestAsHex.equals(user.getPassword())){//表示用户的密码不正确
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		//校验成功，生成token
		String token=UUID.randomUUID().toString();
		//存放数据到jedis中
		user.setPassword(null);
		client.set(USER_INFO+":"+token, JsonUtils.objectToJson(user));
		//设置过期时间 来模拟session
		client.expire(USER_INFO+":"+token, EXPIRE_TIME);
		//7.把token设置cookie当中    在表现层设置
		// 添加写cookie的逻辑，cookie的有效期是关闭浏览器失效
		//CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		return TaotaoResult.ok(token);
	}
	
	@Override
	public TaotaoResult getUserByToken(String token) {
		//1.注入jedisclient
		//2.调用根据token查询 用户信息（JSON）的方法   get方法
		String strjson = client.get(USER_INFO+":"+token);
		//3.判断是否查询到
		if(StringUtils.isNotBlank(strjson)){
			//5.如果查询到  需要返回200  包含用户的信息  用户信息转成对象
			TbUser user = JsonUtils.jsonToPojo(strjson, TbUser.class);
			//重新设置过期时间
			client.expire(USER_INFO+":"+token, EXPIRE_TIME);
			return TaotaoResult.ok(user);
		}
		//4.如果查询不到 返回400
		return TaotaoResult.build(400, "用户已过期");
	}

	@Override
	public TaotaoResult userLogout(String token) {
		// TODO Auto-generated method stub
		String strjson = client.get(USER_INFO+":"+token);
		//3.判断是否查询到
		if(StringUtils.isNotBlank(strjson)){
			//设置过期时间为零
			client.expire(USER_INFO+":"+token, 0);
		}
		return TaotaoResult.ok();
	}

}
