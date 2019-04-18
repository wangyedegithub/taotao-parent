package com.taotao.search.service.Impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.commons.pojo.SearchItem;
import com.taotao.commons.pojo.SearchResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private SearchItemMapper mapper;
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchDao searchdao;

	public TaotaoResult AllSerachItems() throws Exception {
		List<SearchItem> SearchItemList = mapper.getSearchItemList();
		// 将列表中的元素放入索引库
		for (SearchItem searchItem : SearchItemList) {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId().toString());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_desc", searchItem.getItem_desc());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_category_name", searchItem.getCategory_name());
			solrServer.add(document);
		}
		System.out.println("插入完成");
		solrServer.commit();
		return TaotaoResult.ok();
	}

	@Override
	public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
		// 1.创建solrquery对象
		SolrQuery query = new SolrQuery();
		// 2.设置主查询条件
		if (StringUtils.isNotBlank(queryString)) {
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");
		}
		// 2.1设置过滤条件 设置分页
		if (page == null)
			page = 1;
		if (rows == null)
			rows = 60;
		query.setStart((page - 1) * rows);// page-1 * rows
		query.setRows(rows);
		// 2.2.设置默认的搜索域
		query.set("df", "item_keywords");
		// 2.3设置高亮
		query.setHighlight(true);
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		query.addHighlightField("item_title");// 设置高亮显示的域

		// 3.调用dao的方法 返回的是SearchResult 只包含了总记录数和商品的列表
		SearchResult search = searchdao.search(query);
		// 4.设置SearchResult 的总页数
		long pageCount = 0l;
		pageCount = search.getRecordCount() / rows;
		if (search.getRecordCount() % rows > 0) {
			pageCount++;
		}
		search.setPageCount(pageCount);
		// 5.返回
		return search;
	}

	@Override
	public TaotaoResult updateSearchItemById(Long itemId) throws Exception {
		 return searchdao.updateSearchItemById(itemId);
	}
	

}
