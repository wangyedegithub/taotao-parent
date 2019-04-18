package com.taotao.search.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrCloudTest {
	@Test
	public void testSolrCloud() throws Exception{
		//1.创建solrServer 集群的实现类
		//zkHost指定zookeeper的集群列表
		CloudSolrServer cloudSolrServer=new 
				CloudSolrServer("192.168.25.133:2181,192.168.25.133:2182,192.168.25.133:2183");
		//2.设置默认的搜索collection 默认的索引库
		cloudSolrServer.setDefaultCollection("collection2");
		//3.创建inputSolrDocument对象
		SolrInputDocument document=new SolrInputDocument();
		//4.添加域到文档中
		document.addField("id", "testCloudId");
		document.addField("item_title", "Hello Word");
		//5.将文档提交到索引库中
		cloudSolrServer.add(document);
		//6.提交
		cloudSolrServer.commit();
	}
}
