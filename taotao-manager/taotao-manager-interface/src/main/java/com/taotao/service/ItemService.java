package com.taotao.service;

import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	//根据当前页码和行数 
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
	
	public TaotaoResult saveItem(TbItem item,String desc);

	public TaotaoResult getItemsDescById(long id);
	
	public TaotaoResult updateItem(TbItem item,String desc);
	
	public TaotaoResult deleteItem(long[] ids);
	
	public TaotaoResult instockItem(long[] ids);
	
	public TaotaoResult reshelfItem(long[] ids);
	
	/**
	 * 根据商品的id查询商品的数据
	 * @param itemId
	 * @return
	 */
	public TaotaoResult  getItemById(Long itemId);
}
