package com.boco.eoms.base.dao.jdbc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.boco.eoms.base.dao.IQueryJdbcDao;
import com.boco.eoms.base.tool.LogOutTool;

@Repository("queryJdbcDao")
public class QueryJdbcDaoImpl extends JdbcDaoSupport implements IQueryJdbcDao{
	
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	
	@Override
	public List<Map<String, Object>> getQueryList(String sql) throws Exception {
		LogOutTool.info(QueryJdbcDaoImpl.class,"query sql = " + sql);
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
		return list;
	}

	@Override
	public void insertInfoBySql(String sql) {
		LogOutTool.info(QueryJdbcDaoImpl.class,"insert sql = " + sql);
		this.getJdbcTemplate().execute(sql);
	}

	@Override
	public void updateInfoBySql(String sql) {
		LogOutTool.info(QueryJdbcDaoImpl.class,"update sql = " + sql);
		this.getJdbcTemplate().execute(sql);
	}

}
