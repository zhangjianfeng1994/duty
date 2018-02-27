package com.boco.eoms.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.eoms.base.dao.IQueryJdbcDao;
import com.boco.eoms.base.service.IQueryJdbcService;
import com.boco.eoms.base.tool.LogOutTool;

@Service("queryJdbcService")
public class QueryJdbcServiceImpl implements IQueryJdbcService {
	
	@Autowired
	private IQueryJdbcDao queryJdbcDao;
	@Override
	public List<Map<String, Object>> getQueryList(String sql) throws Exception {
		List<Map<String, Object>> list = queryJdbcDao.getQueryList(sql);
		return list;
	}

	@Override
	public void insertInfoBySql(String sql) {
		queryJdbcDao.insertInfoBySql(sql);
	}

	@Override
	public void updateInfoBySql(String sql) {
		queryJdbcDao.updateInfoBySql(sql);
	}

}
