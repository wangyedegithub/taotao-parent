package com.taotao.search.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {
	@Test
	public void add() throws Exception{
		//1.创建solrserver 建立链接指定地址
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		//2.创建solrInputdocument
		SolrInputDocument document=new SolrInputDocument();
		//3.向文本中添加域
		document.addField("id", "test001");
		document.addField("item_title", "这是一个测试");
		//4.将文档提交到索引库
		solrServer.add(document);
		solrServer.commit();
	}
	@Test
	public void testquery() throws Exception{
		//1.创建solrserver对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		//2.创建solrquery对象   设置各种过滤条件，主查询条件 排序
		SolrQuery query = new SolrQuery();
		//3.设置查询的条件
		query.setQuery("九月天");
		query.addFilterQuery("item_price:[0 TO 3000000000]");
		query.set("df", "item_title");
		//4.执行查询
		QueryResponse response = solrServer.query(query);
		//5.获取结果集
		SolrDocumentList results = response.getResults();
		System.out.println("查询的总记录数"+results.getNumFound());
		//6.遍历结果集  打印
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
		}
	}
	
	
}
