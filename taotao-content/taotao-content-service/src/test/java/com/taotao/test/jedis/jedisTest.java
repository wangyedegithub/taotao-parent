//package com.taotao.test.jedis;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.Test;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPool;
//
//public class jedisTest {
//	@Test
//	public void testJedis(){
//		Jedis jedis=new Jedis("192.168.25.133",6379);//host和port
//		jedis.set("key12345", "value1");
//		System.out.println(jedis.get("key12345"));
//		jedis.close();
//	}
//	//连接数据库
//	@Test
//	public void testJedisPool(){
//		//建立JedisPool的连接对象
//		JedisPool pool=new JedisPool("192.168.25.133",6379);
//		//获取Jedis对象
//		Jedis jedis=pool.getResource();
//		jedis.set("pool", "1234");
//		System.out.println(jedis.get("pool"));
//		jedis.close();
//		pool.close();
//	}
//	//集群版此时
//	@Test
//	public void testJedisCluster(){
//		Set<HostAndPort> nodes=new HashSet<>();
//		nodes.add(new HostAndPort("192.168.25.133",7001));
//		nodes.add(new HostAndPort("192.168.25.133",7002));
//		nodes.add(new HostAndPort("192.168.25.133",7003));
//		nodes.add(new HostAndPort("192.168.25.133",7004));
//		nodes.add(new HostAndPort("192.168.25.133",7005));
//		nodes.add(new HostAndPort("192.168.25.133",7006));
//		JedisCluster cluster=new JedisCluster(nodes);
//		cluster.set("cluster的key", "cluster的value");
//		System.out.println(cluster.get("cluster的key"));
//		cluster.close();
//	}
//}
