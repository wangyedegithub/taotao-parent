package com.taotao.test.pageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextBeanFactoryReference;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	@Test
	public void testPageHelper(){
		//1.加载配置文件
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//2.获取mapper代理对象
		TbItemMapper tbItemMapper=context.getBean(TbItemMapper.class);
		//3.设置的分页信息
		PageHelper.startPage(1, 3);//每页3条数据，对随后紧挨这的第一条List生效
		//4.获取对象结果集
		TbItemExample example=new TbItemExample();
		List<TbItem> list1 =tbItemMapper.selectByExample(example);//example查询条件此时相当于select * from tb_item;
		List<TbItem> list2 =tbItemMapper.selectByExample(example);
		System.out.println("第一条记录数"+list1.size()+"第二条记录数"+list2.size());
		for (TbItem tbItem : list1) {
			System.out.println("名称"+tbItem.getTitle()+"价格"+tbItem.getPrice());
		}
	}
	
}
