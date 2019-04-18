package com.taotao.search.mapper;

import java.util.List;

import com.taotao.commons.pojo.SearchItem;

public interface SearchItemMapper {
	public List<SearchItem> getSearchItemList();
	
	//根据商品的id查询商品的数据
	public SearchItem getSearchItemById(Long itemId);
}
