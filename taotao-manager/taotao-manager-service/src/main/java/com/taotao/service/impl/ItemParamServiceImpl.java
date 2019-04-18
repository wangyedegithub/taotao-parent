package com.taotao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ItemParamService;
@Service
public class ItemParamServiceImpl implements ItemParamService {
	@Autowired
	private TbItemParamMapper mapper;
	@Override
	public EasyUIDataGridResult getItemParamList(Integer page, Integer rows) {
		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 30;
		}
		PageHelper.startPage(page, rows);
		TbItemParamExample example=new TbItemParamExample();
		List<TbItemParam> list=mapper.selectByExampleWithBLOBs(example);
		System.out.println(list.size());
		PageInfo<TbItemParam> info = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		return result;
	}
	@Override
	public TaotaoResult deleteItemParam(long[] ids) {
		// TODO Auto-generated method stub
		for (long id : ids) {
			mapper.deleteByPrimaryKey(id);
		}
		return TaotaoResult.ok();
	}

}
