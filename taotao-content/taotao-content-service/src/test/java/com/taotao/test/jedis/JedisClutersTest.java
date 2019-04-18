//package com.taotao.test.jedis;
//
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.taotao.commons.jedis.JedisClient;
//
//
//
//public class JedisClutersTest {
//	@Test
//	public void testJedisCluters(){
//		//初始化Spring容器
//		ApplicationContext content=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
//		//获取实现类
//		JedisClient client=content.getBean(JedisClient.class);
//		client.set("JedisCvakey", "JedisCvalue");
//		System.out.println(client.get("JedisCvakey"));
//	} 
//}
