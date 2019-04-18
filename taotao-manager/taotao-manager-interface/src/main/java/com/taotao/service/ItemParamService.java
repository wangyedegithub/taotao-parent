package com.taotao.service;

import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;

public interface ItemParamService {
	public EasyUIDataGridResult getItemParamList(Integer page,Integer rows);
	
	public TaotaoResult deleteItemParam(long[] ids);
	
}
